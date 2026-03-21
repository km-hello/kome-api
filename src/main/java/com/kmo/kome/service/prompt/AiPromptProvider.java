package com.kmo.kome.service.prompt;

import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.utils.MessageHelper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * AI 提示词提供者。
 * <p>
 * 按需从 classpath 读取固定提示词文件，并在首次成功读取后缓存，
 * 避免在业务代码中直接内联长提示词，也避免提示词资源问题阻塞应用启动。
 */
@Component
public class AiPromptProvider {

    private static final String SUMMARY_SYSTEM_PROMPT_PATH = "classpath:prompts/summary-system.md";
    private static final String SLUG_SYSTEM_PROMPT_PATH = "classpath:prompts/slug-system.md";

    private final ResourceLoader resourceLoader;
    private final MessageHelper messageHelper;

    /**
     * 摘要系统提示词缓存。
     * <p>
     * 使用 volatile，保证首次加载完成后，其他线程能立即看到最新值。
     */
    private volatile String summarySystemPrompt;

    /**
     * Slug 系统提示词缓存。
     * <p>
     * 使用 volatile，保证首次加载完成后，其他线程能立即看到最新值。
     */
    private volatile String slugSystemPrompt;

    /**
     * 创建 AI 提示词提供者。
     * <p>
     * 构造时仅注入依赖，不立即读取提示词文件，避免因 AI 提示词资源问题影响应用启动。
     *
     * @param resourceLoader Spring 资源加载器，用于读取 classpath 下的提示词文件。
     * @param messageHelper 国际化消息工具，用于生成统一的异常提示。
     */
    public AiPromptProvider(ResourceLoader resourceLoader, MessageHelper messageHelper) {
        this.resourceLoader = resourceLoader;
        this.messageHelper = messageHelper;
    }

    /**
     * 获取文章摘要功能的系统提示词。
     * <p>
     * 首次访问时懒加载并缓存，后续请求直接返回缓存结果。
     *
     * @return 已加载的摘要系统提示词。
     * @throws ServiceException 当提示词文件读取失败或内容为空时抛出。
     */
    public String getSummarySystemPrompt() {
        String prompt = summarySystemPrompt;
        if (prompt != null) {
            // 快速路径：已缓存时无须加锁，直接返回即可。
            return prompt;
        }
        synchronized (this) {
            // 再次检查，避免多个线程在首次访问时重复加载同一份提示词。
            if (summarySystemPrompt == null) {
                summarySystemPrompt = loadPrompt(SUMMARY_SYSTEM_PROMPT_PATH);
            }
            return summarySystemPrompt;
        }
    }

    /**
     * 获取 URL Slug 生成功能的系统提示词。
     * <p>
     * 首次访问时懒加载并缓存，后续请求直接返回缓存结果。
     *
     * @return 已加载的 Slug 系统提示词。
     * @throws ServiceException 当提示词文件读取失败或内容为空时抛出。
     */
    public String getSlugSystemPrompt() {
        String prompt = slugSystemPrompt;
        if (prompt != null) {
            // 快速路径：已缓存时无须加锁，直接返回即可。
            return prompt;
        }
        synchronized (this) {
            // 再次检查，避免多个线程在首次访问时重复加载同一份提示词。
            if (slugSystemPrompt == null) {
                slugSystemPrompt = loadPrompt(SLUG_SYSTEM_PROMPT_PATH);
            }
            return slugSystemPrompt;
        }
    }

    /**
     * 从 classpath 读取指定位置的提示词文件。
     * <p>
     * 读取成功后会去除首尾空白；若文件不存在、无法读取或内容为空，则抛出统一的业务异常。
     *
     * @param location 提示词文件位置，例如 {@code classpath:prompts/summary-system.md}。
     * @return 去除首尾空白后的提示词文本。
     * @throws ServiceException 当提示词文件读取失败或内容为空时抛出。
     */
    private String loadPrompt(String location) {
        Resource resource = resourceLoader.getResource(location);
        try (InputStream inputStream = resource.getInputStream()) {
            String prompt = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8).trim();
            if (!StringUtils.hasText(prompt)) {
                // 空文件或仅包含空白字符都视为无效配置，直接按业务异常处理。
                throw new ServiceException(
                        ResultCode.INTERNAL_SERVER_ERROR,
                        messageHelper.get("error.ai.promptEmpty", location)
                );
            }
            return prompt;
        } catch (IOException e) {
            // 保留原始 IO 异常，便于日志和排查，同时对外暴露统一的国际化错误消息。
            throw new ServiceException(
                    ResultCode.INTERNAL_SERVER_ERROR,
                    messageHelper.get("error.ai.promptLoadFailed", location),
                    e
            );
        }
    }
}
