package com.kmo.kome.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import java.util.List;
import java.util.Locale;

/**
 * 国际化配置类
 * <p>
 * 配置应用程序的多语言支持，主要包括：
 * 1. 基于 Accept-Language 请求头的区域解析器，默认语言为英文，支持简体中文；
 * 2. 消息源配置，从 classpath:messages 加载业务/异常消息资源文件，使用 UTF-8 编码。
 * <p>
 * DTO 校验消息由 Hibernate Validator 自动从 ValidationMessages*.properties 加载，无需额外配置。
 */
@Configuration
public class LocaleConfig {

    /**
     * 配置区域解析器。
     * <p>
     * 根据请求的 Accept-Language 头自动解析语言环境，
     * 默认返回英文，支持英文和简体中文。
     *
     * @return 基于 Accept-Language 头的区域解析器
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        resolver.setSupportedLocales(List.of(Locale.ENGLISH, Locale.SIMPLIFIED_CHINESE));
        return resolver;
    }

    /**
     * 配置消息源。
     * <p>
     * 加载 classpath:messages 资源文件，用于业务逻辑异常和全局异常消息的国际化。
     * 当消息 key 找不到时，直接返回 key 本身作为默认值。
     *
     * @return 可重新加载的消息源
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:messages");
        source.setDefaultEncoding("UTF-8");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }
}
