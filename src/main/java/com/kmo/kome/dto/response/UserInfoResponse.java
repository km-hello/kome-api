package com.kmo.kome.dto.response;

import com.kmo.kome.dto.SkillItem;
import com.kmo.kome.dto.SocialLink;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户信息响应 DTO 类。
 * 用于封装用户相关的核心信息并返回给前端。
 * 通常在获取用户详情相关的接口中使用。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String username;
    private String nickname;
    private String avatar;
    private String email;
    private String description;
    private List<SocialLink> socialLinks;
    private List<SkillItem> skills;
}
