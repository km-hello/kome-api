package com.kmo.kome.dto.request;

import com.kmo.kome.dto.SkillItem;
import com.kmo.kome.dto.SocialLink;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 更新用户信息请求类。
 * 用于封装更新用户基本信息时提交的数据，包括用户名、昵称、头像、邮箱和个人描述等字段。
 * 主要应用于用户信息更新业务逻辑中。
 */
@Data
public class UserUpdateRequest {
    @NotBlank(message = "{validation.user.username.notBlank}")
    @Size(min = 4, max = 50, message = "{validation.user.username.size}")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "{validation.user.username.pattern}")
    private String username;

    @Size(max = 50, message = "{validation.user.nickname.size}")
    private String nickname;

    @Size(max = 255, message = "{validation.user.avatar.size}")
    private String avatar;

    @Size(max = 100, message = "{validation.user.email.size}")
    @Email(message = "{validation.user.email.format}")
    private String email;

    @Size(max = 255, message = "{validation.user.description.size}")
    private String description;

    /**
     * 社交链接列表
     */
    @Valid
    private List<SocialLink> socialLinks;

    /**
     * 技能列表
     */
    @Valid
    private List<SkillItem> skills;
}
