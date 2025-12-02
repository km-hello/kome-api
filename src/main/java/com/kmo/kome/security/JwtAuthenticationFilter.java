package com.kmo.kome.security;

import com.kmo.kome.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 认证过滤器
 * <p>
 * 拦截每个 HTTP 请求，检查 Header 中是否包含有效的 JWT Token。
 * 如果包含，则解析用户身份并存入 Spring Security 上下文。
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    /**
     * 处理 HTTP 请求时的过滤逻辑，用于校验 JWT Token 的合法性。
     * 从请求头中解析 Token，验证其有效性，并将认证信息存入 SecurityContext 中。
     * 如果 Token 不合法或者未提供，则直接放行请求，不进行认证处理。
     *
     * @param request 当前的 HTTP 请求对象，用于获取必要的请求头和详情信息。
     * @param response 当前的 HTTP 响应对象，用于响应客户端的请求。
     * @param filterChain 过滤器链，用于将请求传递到下一个过滤器。
     * @throws ServletException 当处理过程中发生与 Servlet 相关的异常时抛出。
     * @throws IOException 当处理过程中发生 I/O 错误时抛出。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 从请求头获取 Authorization
        String header = request.getHeader("Authorization");
        String token = null;
        Long userId = null;

        // 2. 检查格式是否为 "Bearer xxx"
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            token = header.substring(7);

            // 校验 Token 并解析 userId
            try {
                userId = jwtUtils.getUserIdFromToken(token);
            }catch (Exception e){
                log.warn("JWT token is invalid: {}", e.getMessage());
            }
        }

        // 3. 如果 Token 有效且 SecurityContext 中未认证 (避免重复认证)
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 不再从数据库加载用户信息，直接用Token中的userId构建认证令牌
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
            // 设置请求详情 (IP, SessionId 等)
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // 将认证信息存入 SecurityContext，标记该请求已通过认证
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 4. 放行请求，进入下一个过滤器
        filterChain.doFilter(request, response);
    }
}
