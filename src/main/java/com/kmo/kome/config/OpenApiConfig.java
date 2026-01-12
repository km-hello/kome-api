package com.kmo.kome.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 配置类。
 * <p>
 * 用于配置 API 文档的基础信息，包括标题、版本、描述和联系信息。
 * 该配置适用于 Kome Blog 项目，提供清晰的文档以供开发和测试使用。
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Kome Blog API")
                        .version("1.0.0")
                        .description("Backend API for Kome Blog Project")
                        .contact(new Contact().name("KM"))
                );
    }
}
