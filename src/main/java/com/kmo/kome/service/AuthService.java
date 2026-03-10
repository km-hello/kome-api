package com.kmo.kome.service;

import com.kmo.kome.dto.request.AuthLoginRequest;
import com.kmo.kome.dto.response.AuthInfoResponse;
import com.kmo.kome.dto.response.AuthLoginResponse;

/**
 * 认证业务接口。
 * 负责登录会话创建与当前认证信息获取。
 */
public interface AuthService {

    /**
     * 用户登录并创建 JWT 会话。
     *
     * @param request 登录请求参数。
     * @return 登录响应，包含 token 与当前认证信息。
     */
    AuthLoginResponse login(AuthLoginRequest request);

    /**
     * 获取当前认证信息。
     *
     * @param currentUserId 当前登录用户 ID。
     * @return 当前认证信息。
     */
    AuthInfoResponse getAuthInfo(Long currentUserId);
}
