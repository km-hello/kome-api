package com.kmo.kome.dto.request;

import lombok.Data;

import java.util.List;

/**
 * 更新文章请求类。
 * 用于封装更新文章时提交的数据。
 * 包括文章标题、摘要、内容、封面图片、是否置顶、发布状态及标签列表等字段。
 * 主要应用于文章相关的更新操作请求中。
 */
@Data
public class PostUpdateRequest {
    private String title;

    private String slug;

    private String summary;

    private String content;

    private String coverImage;

    // 是否置顶
    private Boolean isPinned;

    // 状态: 1=已发布, 0=草稿
    private Integer status;

    // 标签列表
    private List<Long> tagIds;
}
