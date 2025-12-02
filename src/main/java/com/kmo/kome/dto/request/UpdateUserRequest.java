package com.kmo.kome.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotEmpty(message = "用户名不能为空")
    private String username;
    private String nickname;
    private String avatar;
    private String email;
    private String description;
}
