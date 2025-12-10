package com.kmo.kome.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 更新密码请求类。
 * 用于封装更新用户密码时提交的必要数据，包括旧密码和新密码。
 * 旧密码不能为空，新密码需满足长度要求（6到24位）。
 */
@Data
public class UserUpdatePasswordRequest {
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W_]).{8,64}$",
            message = "新密码必须包含字母、数字和特殊字符，且长度在8-64位之间")
    private String newPassword;
}
