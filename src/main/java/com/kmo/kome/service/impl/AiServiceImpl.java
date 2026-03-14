package com.kmo.kome.service.impl;

import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.request.AiSlugRequest;
import com.kmo.kome.dto.request.AiSummaryRequest;
import com.kmo.kome.dto.response.AiResultResponse;
import com.kmo.kome.service.AiService;
import com.kmo.kome.utils.MessageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

/**
 * AI 服务实现类。
 * <p>
 * 基于 Spring AI 调用 OpenAI 兼容模型，提供文章摘要生成和 URL Slug 生成功能。
 */
@Slf4j
@Service
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;
    private final MessageHelper messageHelper;

    public AiServiceImpl(ChatClient.Builder chatClientBuilder, MessageHelper messageHelper) {
        // 使用 Spring AI 自动装配的 Builder，复用统一的模型与连接配置
        this.chatClient = chatClientBuilder.build();
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
        String content = request.getContent();
        // 截断内容至 50000 字符，避免超出模型上下文限制
        String truncated = content.length() > 50000 ? content.substring(0, 50000) : content;
        String systemPrompt = "为一篇技术博客文章生成中文摘要。风格参考少数派、阮一峰博客的文章描述：简洁清晰，有信息量。20-100字，只客观描述文章内容本身，不要出现'本文''读者''用户''帮助'等词，不要用疑问句，不要描述文章的目的或受众。只返回摘要文本。";
        String summary = callChatApi(systemPrompt, truncated, 0.4);
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
        String systemPrompt = "将标题转换为英文 URL slug。规则：小写+连字符，提炼标题核心含义（副标题、修饰语可省略），技术名词保留原文（如 react、docker），尽量简短但保证意义完整，不超过80个字符。只返回 slug。";
        String raw = callChatApi(systemPrompt, request.getTitle(), 0.2);
        // 后处理确保 slug 格式合规
        String slug = raw.toLowerCase()
                .replaceAll("[^a-z0-9\\-]", "")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
        return new AiResultResponse(slug);
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
