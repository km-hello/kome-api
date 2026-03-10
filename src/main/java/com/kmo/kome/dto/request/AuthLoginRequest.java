package com.kmo.kome.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 登录请求 DTO
 * 用于接收前端提交的登录表单数据
 */
@Data
public class AuthLoginRequest {
    @NotBlank(message = "{validation.user.usernameLogin.notBlank}")
    @Size(max = 50, message = "{validation.user.usernameLogin.size}")
    private String username;

    @NotBlank(message = "{validation.user.passwordLogin.notBlank}")
    @Size(max = 100, message = "{validation.user.passwordLogin.size}")
    private String password;
}
