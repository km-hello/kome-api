package com.kmo.kome.service.impl;

import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.request.AiSlugRequest;
import com.kmo.kome.dto.request.AiSummaryRequest;
import com.kmo.kome.dto.response.AiResultResponse;
import com.kmo.kome.service.AiService;
import com.kmo.kome.service.prompt.AiPromptProvider;
import com.kmo.kome.utils.MessageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * AI 服务实现类。
 * <p>
 * 基于 Spring AI 调用 OpenAI 兼容模型，提供文章摘要生成和 URL Slug 生成功能。
 */
@Slf4j
@Service
public class AiServiceImpl implements AiService {

    private static final int SUMMARY_MAX_INPUT_LENGTH = 50000;
    private static final double SUMMARY_TEMPERATURE = 0.4;
    private static final String SUMMARY_OMITTED_MARKER = "\n\n[中间内容已省略]\n\n";
    private static final double SLUG_TEMPERATURE = 0.2;

    private final ChatClient chatClient;
    private final AiPromptProvider aiPromptProvider;
    private final MessageHelper messageHelper;

    public AiServiceImpl(
            ChatClient.Builder chatClientBuilder,
            AiPromptProvider aiPromptProvider,
            MessageHelper messageHelper
    ) {
        // 使用 Spring AI 自动装配的 Builder，复用统一的模型与连接配置
        this.chatClient = chatClientBuilder.build();
        this.aiPromptProvider = aiPromptProvider;
        this.messageHelper = messageHelper;
    }

    /**
     * 根据文章内容生成简短的中文摘要。
     * 内容超过 50000 字符时会自动截断，以避免超出模型上下文限制。
     *
     * @param request 包含文章正文内容的请求对象，内容不能为空。
     * @return 包含生成摘要的结果对象。
     * @throws ServiceException 当 AI 服务调用失败或返回结果为空时抛出。
     */
    @Override
    public AiResultResponse generateSummary(AiSummaryRequest request) {
        String summary = normalizeSummary(callChatApi(
                aiPromptProvider.getSummarySystemPrompt(),
                prepareSummaryInput(request.getContent()),
                SUMMARY_TEMPERATURE
        ));
        return new AiResultResponse(summary);
    }

    /**
     * 根据文章标题生成 SEO 友好的英文 URL Slug。
     * 生成后会进行后处理，确保 slug 仅包含小写字母、数字和连字符。
     *
     * @param request 包含文章标题的请求对象，标题不能为空。
     * @return 包含格式合规的英文 slug 的结果对象。
     * @throws ServiceException 当 AI 服务调用失败或返回结果为空时抛出。
     */
    @Override
    public AiResultResponse generateSlug(AiSlugRequest request) {
        String slug = normalizeSlug(callChatApi(
                aiPromptProvider.getSlugSystemPrompt(),
                request.getTitle(),
                SLUG_TEMPERATURE
        ));
        return new AiResultResponse(slug);
    }

    /**
     * 为摘要生成准备输入内容。
     * <p>
     * 当正文超出模型允许的最大输入长度时，保留开头与结尾片段，
     * 并在中间插入省略标记，尽量同时保留主题背景与最终结论。
     *
     * @param content 文章正文内容。
     * @return 适合发送给模型的摘要生成输入。
     */
    private String prepareSummaryInput(String content) {
        if (content.length() <= SUMMARY_MAX_INPUT_LENGTH) {
            return content;
        }
        // 预留省略标记长度，确保拼接后的输入仍不超过模型侧的上限。
        int availableLength = SUMMARY_MAX_INPUT_LENGTH - SUMMARY_OMITTED_MARKER.length();
        // 开头通常包含主题与背景，结尾通常包含结论与结果，因此同时保留首尾片段。
        int headLength = availableLength * 3 / 5;
        int tailLength = availableLength - headLength;
        return content.substring(0, headLength)
                + SUMMARY_OMITTED_MARKER
                + content.substring(content.length() - tailLength);
    }

    /**
     * 规范化摘要输出。
     * <p>
     * 将模型返回内容收敛为单行纯文本，并去除常见的摘要前缀与包裹引号。
     *
     * @param raw 模型返回的原始摘要文本。
     * @return 清洗后的摘要文本。
     * @throws ServiceException 当清洗后结果为空时抛出。
     */
    private String normalizeSummary(String raw) {
        // 统一收敛为单行纯文本，去掉模型偶尔补出的“摘要：”前缀和包裹引号。
        String summary = raw
                .replace("\r", " ")
                .replace("\n", " ")
                .replace("\t", " ")
                .replaceAll("\\s+", " ")
                .replaceAll("^(摘要|摘要文本)\\s*[:：]\\s*", "")
                .replaceAll("^[\"'“”‘’《》「」『』]+", "")
                .replaceAll("[\"'“”‘’《》「」『』]+$", "")
                .trim();
        if (StringUtils.hasText(summary)) {
            return summary;
        }
        throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, messageHelper.get("error.ai.emptyResult"));
    }

    /**
     * 规范化 slug 输出。
     * <p>
     * 仅提取首行有效内容，并清洗常见前缀、代码包裹符以及不符合 slug 规范的字符。
     *
     * @param raw 模型返回的原始 slug 文本。
     * @return 清洗后的 slug 文本。
     * @throws ServiceException 当清洗后结果为空时抛出。
     */
    private String normalizeSlug(String raw) {
        // 模型若输出多行解释，只取第一行有效内容作为候选 slug。
        String firstLine = raw.lines()
                .map(String::trim)
                .filter(StringUtils::hasText)
                .findFirst()
                .orElse(raw.trim());

        // 兜底清洗常见的“slug:”前缀、代码包裹符和非 slug 字符。
        String slug = firstLine
                .replaceAll("(?i)^slug\\s*[:：]\\s*", "")
                .replace("`", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");

        if (StringUtils.hasText(slug)) {
            return slug;
        }
        throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, messageHelper.get("error.ai.emptyResult"));
    }

    /**
     * 调用 Spring AI ChatClient 生成文本内容。
     *
     * @param systemPrompt 系统提示词，用于指导模型的输出格式和风格。
     * @param userMessage  用户消息，即需要模型处理的输入文本。
     * @param temperature  温度参数，控制输出的随机性（值越低越确定）。
     * @return 模型生成的文本内容。
     * @throws ServiceException 当请求失败或返回结果为空时抛出。
     */
    private String callChatApi(String systemPrompt, String userMessage, double temperature) {
        try {
            // 按请求覆盖 temperature，其他参数（模型、base-url、api-key）走配置文件
            String content = chatClient
                    .prompt()
                    .system(systemPrompt)
                    .user(userMessage)
                    .options(OpenAiChatOptions.builder().temperature(temperature).build())
                    .call()
                    .content();

            if (content == null || content.isBlank()) {
                throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, messageHelper.get("error.ai.emptyResult"));
            }
            return content.trim();
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("AI service call failed", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, messageHelper.get("error.ai.unavailable"));
        }
    }
}
