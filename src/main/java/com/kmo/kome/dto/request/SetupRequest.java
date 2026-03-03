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
    @NotBlank(message = "{validation.user.username.notBlank}")
    @Size(min = 4, max = 50, message = "{validation.user.username.size}")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "{validation.user.username.pattern}")
    private String username;

    @NotBlank(message = "{validation.user.password.notBlank}")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W_]).{8,64}$",
            message = "{validation.user.password.pattern}")
    private String password;

    @Size(max = 50, message = "{validation.user.nickname.size}")
    private String nickname;

    @Size(max = 255, message = "{validation.user.avatar.size}")
    private String avatar;

    @Size(max = 255, message = "{validation.user.description.size}")
    private String description;

    @Email(message = "{validation.user.email.format}")
    @Size(max = 100)
    private String email;
}
