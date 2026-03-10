package com.kmo.kome.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 认证登录响应 DTO。
 * 返回会话信息与后台壳子所需的最小用户摘要。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginResponse {
    private String accessToken;
    private String tokenType;
    private Long expiresAt;
    private AuthInfoResponse user;
}
