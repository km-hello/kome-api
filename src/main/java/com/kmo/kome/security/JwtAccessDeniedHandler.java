package com.kmo.kome.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmo.kome.common.Result;
import com.kmo.kome.common.ResultCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 处理访问被拒绝的情况的处理器实现类。
 * 当用户尝试访问其无权限的资源时，将向客户端返回一个标准的 JSON 格式的响应，
 * 内容包含状态码和提示信息，状态码为 403。
 * <p>
 * 此类实现了 Spring Security 提供的 AccessDeniedHandler 接口，
 * 用于统一处理权限不足所引发的异常。
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * 处理访问被拒绝的情况并向客户端返回 JSON 格式的错误响应。
     * 当用户尝试访问无权限的资源时，该方法会设置 HTTP 状态码为 403，并返回一条包含错误信息的响应。
     *
     * @param request HTTP 请求对象，用于获取客户端传递的请求数据和上下文信息。
     * @param response HTTP 响应对象，用于设置响应数据和响应头信息。
     * @param accessDeniedException 尝试访问无权限资源时抛出的异常，包含相关的错误信息。
     * @throws IOException 当处理响应时发生 I/O 错误时抛出。
     * @throws ServletException 当出现 Servlet 相关错误时抛出。
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403

        Result<Object> result = Result.fail(ResultCode.FORBIDDEN, "无访问权限");

        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}
