package com.kmo.kome.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新用户信息请求类。
 * 用于封装更新用户基本信息时提交的数据，包括用户名、昵称、头像、邮箱和个人描述等字段。
 * 主要应用于用户信息更新业务逻辑中。
 */
@Data
public class UserUpdateRequest {
    @Size(min = 4, max = 50, message = "用户名长度必须在4-50字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "用户名只能包含字母、数字、下划线和减号")
    private String username;

    @Size(max = 50, message = "昵称不能超过50个字符")
    private String nickname;

    @Size(max = 255, message = "头像链接过长")
    private String avatar;

    @Size(max = 100, message = "邮箱长度不能超过100")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Size(max = 255, message = "个人简介不能超过255个字符")
    private String description;
}
