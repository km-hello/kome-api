package com.kmo.kome.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

/**
 * 更新文章请求类。
 * 用于封装更新文章时提交的数据。
 * 包括文章标题、摘要、内容、封面图片、是否置顶、发布状态及标签列表等字段。
 * 主要应用于文章相关的更新操作请求中。
 */
@Data
public class PostUpdateRequest {
    @Size(min = 1, max = 255, message = "{validation.post.title.sizeUpdate}")
    private String title;

    @Size(max = 255, message = "{validation.post.slug.size}")
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "{validation.post.slug.pattern}")
    private String slug;

    @Size(max = 500, message = "{validation.post.summary.size}")
    private String summary;

    @Size(min = 1, message = "{validation.post.content.size}")
    private String content;

    @Size(max = 255, message = "{validation.post.coverImage.size}")
    private String coverImage;

    // 是否置顶
    private Boolean isPinned;

    @Range(min = 0, max = 1, message = "{validation.post.status.range}")
    private Integer status;

    // 标签列表
    private List<Long> tagIds;
}
