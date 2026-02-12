package com.kmo.kome.config;

import com.kmo.kome.security.JwtAccessDeniedHandler;
import com.kmo.kome.security.JwtAuthenticationEntryPoint;
import com.kmo.kome.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 安全配置类
 * <p>
 * 配置 Spring Security 的安全相关组件，主要包括：
 * 1. 禁用 CSRF 并设置会话为无状态模式；
 * 2. 配置异常处理器，处理 JWT 认证失败或权限不足的情况；
 * 3. 配置接口权限控制规则，例如开放登录接口，保护后台管理接口；
 * 4. 添加 JWT 认证过滤器至过滤器链。
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /**
     * 配置 Spring Security 的过滤器链。
     * <p>
     * 此方法配置了安全相关的核心组件，主要包括：
     * 1. 禁用 CSRF 保护，适用于无状态的 REST API；
     * 2. 设置会话为无状态模式，使用 JWT 进行认证；
     * 3. 配置异常处理器，处理认证失败（401 未认证）或权限不足（403 禁止访问）的情况；
     * 4. 配置接口权限规则，开放特定接口访问权限，保护需认证的接口；
     * 5. 将自定义的 JWT 认证过滤器添加到过滤器链的指定位置。
     *
     * @param http HttpSecurity 对象，用于配置安全策略。
     * @return 配置完成的 SecurityFilterChain 实例。
     * @throws Exception 如果配置过程中发生错误，则抛出该异常。
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF (REST API 不需要)
                .csrf(AbstractHttpConfigurer::disable)
                // 设置 Session 为无状态 (使用 JWT)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 异常处理配置
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                // 请求权限配置
                .authorizeHttpRequests(auth -> auth
                        // 登录接口
                        .requestMatchers("/api/user/login").permitAll()
                        // 首次设置接口（公开）
                        .requestMatchers("/api/site/initialized", "/api/site/setup").permitAll()
                        // 后台管理接口 (需认证)
                        .requestMatchers("/api/admin/**").authenticated()
                        // 其他接口
                        .anyRequest().permitAll()
                )
                // 添加 JWT 过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
