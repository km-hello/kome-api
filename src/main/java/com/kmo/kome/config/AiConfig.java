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
 * 绑定 {@code ai.*} 配置项，注册调用 OpenAI 兼容 API 的 WebClient Bean。
 * Authorization header 在 AiServiceImpl 中每次请求时动态设置。
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ai")
@EnableConfigurationProperties(AiConfig.class)
public class AiConfig {

    private String apiKey;
    private String baseUrl;
    private String model;
    private Integer timeout;

    @Bean
    public WebClient aiWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(512 * 1024))
                .build();
    }
}
