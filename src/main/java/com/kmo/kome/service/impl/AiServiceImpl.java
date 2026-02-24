package com.kmo.kome.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.config.AiConfig;
import com.kmo.kome.service.AiService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * AI 服务实现类。
 * <p>
 * 通过 WebClient 调用 OpenAI 兼容的 Chat Completions API，
 * 提供文章摘要生成和 URL Slug 生成功能。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final WebClient aiWebClient;
    private final AiConfig aiConfig;

    /**
     * 根据文章内容生成简短的中文摘要。
     * 内容超过 50000 字符时会自动截断，以避免超出模型上下文限制。
     *
     * @param content 文章正文内容，不能为空。
     * @return 生成的中文摘要文本。
     * @throws ServiceException 当 AI 服务调用失败或返回结果为空时抛出。
     */
    @Override
    public String generateSummary(String content) {
        // 截断内容至 50000 字符，避免超出模型上下文限制
        String truncated = content.length() > 50000 ? content.substring(0, 50000) : content;
        String systemPrompt = "为一篇技术博客文章生成中文摘要。风格参考少数派、阮一峰博客的文章描述：简洁清晰，有信息量。30-80字，只客观描述文章内容本身，不要出现'本文''读者''用户''帮助'等词，不要用疑问句，不要描述文章的目的或受众。只返回摘要文本。";
        return callChatApi(systemPrompt, truncated, 0.4);
    }

    /**
     * 根据文章标题生成 SEO 友好的英文 URL Slug。
     * 生成后会进行后处理，确保 slug 仅包含小写字母、数字和连字符。
     *
     * @param title 文章标题，不能为空。
     * @return 格式合规的英文 slug 文本。
     * @throws ServiceException 当 AI 服务调用失败或返回结果为空时抛出。
     */
    @Override
    public String generateSlug(String title) {
        String systemPrompt = "将标题转换为英文 URL slug。规则：小写+连字符，提炼标题核心含义（副标题、修饰语可省略），技术名词保留原文（如 react、docker），3-6个单词。只返回 slug。";
        String raw = callChatApi(systemPrompt, title, 0.2);
        // 后处理确保 slug 格式合规
        return raw.toLowerCase()
                .replaceAll("[^a-z0-9\\-]", "")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
    }

    /**
     * 调用 OpenAI 兼容的 Chat Completions API。
     * 构造请求体，发送 POST 请求并解析响应中的消息内容。
     *
     * @param systemPrompt 系统提示词，用于指导模型的输出格式和风格。
     * @param userMessage  用户消息，即需要模型处理的输入文本。
     * @param temperature  温度参数，控制输出的随机性（值越低越确定）。
     * @return 模型生成的文本内容。
     * @throws ServiceException 当请求失败、响应为空或解析异常时抛出。
     */
    private String callChatApi(String systemPrompt, String userMessage, double temperature) {
        Map<String, Object> requestBody = Map.of(
                "model", aiConfig.getModel(),
                "temperature", temperature,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userMessage)
                )
        );

        try {
            ChatCompletionResponse response = aiWebClient.post()
                    .uri("/v1/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + aiConfig.getApiKey())
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(ChatCompletionResponse.class)
                    .timeout(Duration.ofSeconds(aiConfig.getTimeout()))
                    .block();

            if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
                throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, "AI 服务返回结果为空");
            }

            String content = response.getChoices().getFirst().getMessage().getContent();
            if (content == null || content.isBlank()) {
                throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, "AI 服务返回结果为空");
            }

            return content.trim();
        } catch (ServiceException e) {
            // 已是业务异常，直接抛出交给 GlobalExceptionHandler 处理
            throw e;
        } catch (Exception e) {
            // 将 WebClient 底层异常（超时、连接拒绝、JSON 解析等）转换为用户友好的业务异常
            log.error("调用 AI 服务失败", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, "AI 服务暂时不可用");
        }
    }

    // ==================== OpenAI 响应结构 ====================

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ChatCompletionResponse {
        private List<Choice> choices;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Choice {
        private Message message;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Message {
        private String role;
        private String content;
    }
}
