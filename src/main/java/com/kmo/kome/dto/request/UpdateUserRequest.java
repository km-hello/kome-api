package com.kmo.kome.dto.request;

import lombok.Data;

/**
 * 更新用户信息请求类。
 * 用于封装更新用户基本信息时提交的数据，包括用户名、昵称、头像、邮箱和个人描述等字段。
 * 主要应用于用户信息更新业务逻辑中。
 */
@Data
public class UpdateUserRequest {
    private String username;
    private String nickname;
    private String avatar;
    private String email;
    private String description;
}
