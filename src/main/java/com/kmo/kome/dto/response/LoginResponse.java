package com.kmo.kome.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应 DTO
 * 登录成功后返回给前端的数据结构
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    // 核心数据 JWT Token
    private String token;

    // 过期时间
    private Long expiresIn;

    // 基础用户信息
    private String username;
    private String nickname;
    private String avatar;
}
