package com.kmo.kome.security;

import com.kmo.kome.service.impl.UserDetailsServiceImpl;
import com.kmo.kome.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 认证过滤器
 * <p>
 * 拦截每个 HTTP 请求，检查 Header 中是否包含有效的 JWT Token。
 * 如果包含，则解析用户身份并存入 Spring Security 上下文。
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * 过滤器的核心逻辑
     *
     * @param request     HTTP 请求
     * @param response    HTTP 响应
     * @param filterChain 过滤器链
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 从请求头获取 Authorization
        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // 2. 检查格式是否为 "Bearer xxx"
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            token = header.substring(7);
            // 校验 Token 的有效性 (签名、过期时间)
            if (jwtUtils.validateToken(token)) {
                username = jwtUtils.getUsernameFromToken(token);
            }
        }

        // 3. 如果 Token 有效且 SecurityContext 中未认证 (避免重复认证)
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 从数据库加载用户信息
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // 创建认证令牌 (包含用户信息和权限)
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            // 设置请求详情 (IP, SessionId 等)
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // 将认证信息存入 SecurityContext，标记该请求已通过认证
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 4. 放行请求，进入下一个过滤器
        filterChain.doFilter(request, response);
    }
}
