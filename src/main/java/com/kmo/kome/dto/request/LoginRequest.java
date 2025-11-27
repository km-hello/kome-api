package com.kmo.kome.dto.request;

import lombok.Data;

/**
 * 登录请求 DTO
 * 用于接收前端提交的登录表单数据
 */
@Data
public class LoginRequest {
    private String username;
    private String password;
}
