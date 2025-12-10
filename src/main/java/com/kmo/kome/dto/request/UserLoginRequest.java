package com.kmo.kome.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 登录请求 DTO
 * 用于接收前端提交的登录表单数据
 */
@Data
public class UserLoginRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(max = 50, message = "用户名过长")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(max = 100, message = "密码过长")
    private String password;
}
