package com.kmo.kome.controller;

import com.kmo.kome.common.Result;
import com.kmo.kome.dto.request.AuthLoginRequest;
import com.kmo.kome.dto.response.AuthInfoResponse;
import com.kmo.kome.dto.response.AuthLoginResponse;
import com.kmo.kome.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器。
 * 负责登录与当前认证信息获取。
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 处理用户登录请求，校验凭证并返回 JWT 会话信息。
     *
     * @param request 登录请求对象，包含用户名和密码。
     * @return 包含登录成功后令牌等数据的 {@code Result<AuthLoginResponse>} 实例。
     */
    @PostMapping("/api/auth/login")
    public Result<AuthLoginResponse> login(@Valid @RequestBody AuthLoginRequest request) {
        return Result.success(authService.login(request));
    }

    /**
     * 获取当前登录用户的认证信息。
     *
     * @param currentUserId 当前登录用户的唯一标识
     * @return 包含用户基本信息的 {@code Result<AuthInfoResponse>} 实例
     */
    @GetMapping("/api/admin/auth/info")
    public Result<AuthInfoResponse> getAuthInfo(@AuthenticationPrincipal Long currentUserId) {
        return Result.success(authService.getAuthInfo(currentUserId));
    }
}
