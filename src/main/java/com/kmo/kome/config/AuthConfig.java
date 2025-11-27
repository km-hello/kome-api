package com.kmo.kome.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 认证基础配置类
 * <p>
 * 将 AuthenticationManager 和 PasswordEncoder 独立配置，
 * 是为了打破 SecurityConfig 与 Service 层的循环依赖问题。
 */
@Configuration
@RequiredArgsConstructor
public class AuthConfig {

    /**
     * 配置密码加密器
     * 使用 BCrypt 强哈希算法
     *
     * @return PasswordEncoder 实现实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置认证管理器
     * Spring Security 的核心组件，负责协调认证流程
     *
     * @param config 认证配置
     * @return AuthenticationManager 实例
     * @throws Exception 如果无法获取 AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
