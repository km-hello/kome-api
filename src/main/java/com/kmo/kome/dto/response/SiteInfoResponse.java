package com.kmo.kome.dto.response;

import com.kmo.kome.dto.SkillItem;
import com.kmo.kome.dto.SocialLink;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 站点信息响应 DTO 类。
 * 用于封装站点的基本信息，包括所有者信息和统计数据，并返回给前端。
 * 通常在获取站点信息的接口中使用。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteInfoResponse {

    private OwnerInfo owner;
    private Stats stats;

    // 内部类：所有者信息
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OwnerInfo{
        private String nickname;
        private String avatar;
        private String description;
        private LocalDateTime createdAt;    // 站点创建时间（管理员账户创建时间）
        private List<SocialLink> socialLinks;  // 社交链接列表
        private List<SkillItem> skills;        // 技能列表
    }

    // 内部类：站点统计信息
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Stats{
        private Long publishedPostCount;    // 已发布的文章数
        private Long draftPostCount;        // 草稿文章数

        private Long usedTagCount;          // 使用中的标签数（有关联已发布文章的）
        private Long unusedTagCount;        // 未使用的标签数（没有关联已发布文章的）

        private Long publishedMemoCount;    // 已发布的说说数
        private Long draftMemoCount;        // 草稿说说数

        private Long publishedLinkCount;    // 已发布的友链数
        private Long draftLinkCount;        // 草稿友链数
    }

}
