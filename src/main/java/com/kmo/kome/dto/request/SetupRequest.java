package com.kmo.kome.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 首次设置管理员请求 DTO
 * 用于接收首次访问系统时设置管理员账户的表单数据
 */
@Data
public class SetupRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 50, message = "用户名长度需在4-50之间")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "用户名只能包含字母、数字、下划线和减号")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W_]).{8,64}$",
            message = "密码需包含字母、数字和特殊字符，长度8-64")
    private String password;

    @Size(max = 50, message = "昵称长度不能超过50")
    private String nickname;

    @Size(max = 255, message = "头像URL长度不能超过255")
    private String avatar;

    @Size(max = 255, message = "个人简介长度不能超过255")
    private String description;

    @Email(message = "邮箱格式不正确")
    @Size(max = 100)
    private String email;
}
