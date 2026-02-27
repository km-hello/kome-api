package com.kmo.kome.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * AI 服务配置类。
 * <p>
 * 绑定 {@code ai.*} 配置项，注册调用 OpenAI 兼容 API 的 WebClient Bean。
 * Authorization header 在 AiServiceImpl 中每次请求时动态设置。
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ai")
@EnableConfigurationProperties(AiConfig.class)
public class AiConfig {

    /** API 密钥，用于调用 AI 服务接口的身份认证 */
    private String apiKey;

    /** AI 服务的基础 URL */
    private String baseUrl;

    /** 使用的 AI 模型名称 */
    private String model;

    /** 请求超时时间（秒） */
    private Integer timeout;

    /**
     * 配置用于调用 AI 服务的 WebClient。
     * <p>
     * 设置基础 URL、默认 Content-Type 为 JSON，
     * 并将内存缓冲区上限设置为 512KB 以支持较大的响应体。
     *
     * @return 配置完成的 WebClient 实例
     */
    @Bean
    public WebClient aiWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(512 * 1024))
                .build();
    }
}
