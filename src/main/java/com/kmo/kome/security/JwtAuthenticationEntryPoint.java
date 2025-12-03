package com.kmo.kome.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmo.kome.common.Result;
import com.kmo.kome.common.ResultCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 用于处理未认证访问时的入口点类。
 * 当用户尝试访问需要认证的资源且未通过认证时，该类会拦截请求并返回指定的响应内容。
 * 实现了 {@code AuthenticationEntryPoint} 接口。
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 当用户尝试访问未经授权的资源时触发的方法。
     * 该方法用于构造并返回包含错误信息的 JSON 响应，以通知客户端认证失败的具体原因。
     *
     * @param request 当前请求对象，包含客户端的请求信息。
     * @param response 当前响应对象，用于向客户端返回具体的响应内容。
     * @param authException 认证异常对象，包含认证失败的详细信息。
     * @throws IOException 如果在写入响应时发生 I/O 错误。
     * @throws ServletException 如果处理请求时发生服务端错误或请求处理异常。
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 设置响应头
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401

        // 构建 Result 对象
        Result<Object> result = Result.fail(ResultCode.UNAUTHORIZED);

        // 手动转 JSON 并写入响应
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}
