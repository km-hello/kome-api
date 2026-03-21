package com.kmo.kome.service.prompt;

import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.utils.MessageHelper;
import lombok.Getter;
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
 * 启动时从 classpath 读取固定提示词文件，避免在业务代码中直接内联长提示词。
 */
@Component
public class AiPromptProvider {

    private static final String SUMMARY_SYSTEM_PROMPT_PATH = "classpath:prompts/summary-system.md";
    private static final String SLUG_SYSTEM_PROMPT_PATH = "classpath:prompts/slug-system.md";

    @Getter
    private final String summarySystemPrompt;

    @Getter
    private final String slugSystemPrompt;

    public AiPromptProvider(ResourceLoader resourceLoader, MessageHelper messageHelper) {
        this.summarySystemPrompt = loadPrompt(resourceLoader, messageHelper, SUMMARY_SYSTEM_PROMPT_PATH);
        this.slugSystemPrompt = loadPrompt(resourceLoader, messageHelper, SLUG_SYSTEM_PROMPT_PATH);
    }

    private String loadPrompt(ResourceLoader resourceLoader, MessageHelper messageHelper, String location) {
        Resource resource = resourceLoader.getResource(location);
        try (InputStream inputStream = resource.getInputStream()) {
            String prompt = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8).trim();
            if (!StringUtils.hasText(prompt)) {
                throw new ServiceException(
                        ResultCode.INTERNAL_SERVER_ERROR,
                        messageHelper.get("error.ai.promptEmpty", location)
                );
            }
            return prompt;
        } catch (IOException e) {
            throw new ServiceException(
                    ResultCode.INTERNAL_SERVER_ERROR,
                    messageHelper.get("error.ai.promptLoadFailed", location),
                    e
            );
        }
    }
}
