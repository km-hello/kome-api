package com.kmo.kome.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 创建文章请求类。
 * 用于封装创建新文章时提交的数据。
 * 包括文章标题、内容、摘要、封面图片、是否置顶、发布状态及标签列表等字段。
 * 主要应用于文章相关的创建操作请求中。
 */
@Data
public class PostCreateRequest {
    @NotBlank(message = "文章标题不能为空")
    private String title;

    private String summary;

    @NotBlank(message = "文章内容不能为空")
    private String content;

    private String coverImage;

    // 是否置顶
    private Boolean isPinned = false;

    // 状态: 1=已发布, 0=草稿, 默认 0
    private Integer status = 0;

    // 标签列表
    private List<Long> tagIds;

}
