package com.kmo.kome.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 认证信息响应 DTO。
 * 用于登录后的后台布局展示与会话恢复。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthInfoResponse {
    private Long id;
    private String username;
    private String nickname;
    private String avatar;
    private String email;
}
