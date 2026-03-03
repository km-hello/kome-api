package com.kmo.kome.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * 国际化消息工具类
 * <p>
 * 封装 Spring MessageSource，提供便捷的 i18n 消息获取方法。
 * 自动从 {@link LocaleContextHolder} 获取当前请求的语言环境，
 * 供 Service 层和 ExceptionHandler 中使用。
 *
 * @see com.kmo.kome.config.LocaleConfig
 */
@Component
public class MessageHelper {

    private final MessageSource messageSource;

    public MessageHelper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * 根据当前请求的语言环境获取国际化消息。
     *
     * @param key  消息资源 key，例如 "error.user.notFound"
     * @param args 消息中的占位参数，例如 {0}、{1}
     * @return 对应语言的消息文本
     */
    public String get(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }
}
