package com.kmo.kome.config;

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
 * Spring Security 核心安全配置类
 * <p>
 * 负责配置 HTTP 请求的拦截规则、过滤器链、Session 策略等。
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 配置安全过滤器链
     *
     * @param http HttpSecurity 对象
     * @return SecurityFilterChain
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF (REST API 不需要)
                .csrf(AbstractHttpConfigurer::disable)
                // 设置 Session 为无状态 (使用 JWT)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 请求权限配置
                .authorizeHttpRequests(auth -> auth
                        // 登录接口
                        .requestMatchers("/api/user/login").permitAll()
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
