package com.kmo.kome.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建文章请求类。
 * 用于封装创建新文章时提交的数据。
 * 包括文章标题、内容、摘要、封面图片、是否置顶、发布状态及标签列表等字段。
 * 主要应用于文章相关的创建操作请求中。
 */
@Data
public class PostCreateRequest {
    @NotBlank(message = "{validation.post.title.notBlank}")
    @Size(max = 255, message = "{validation.post.title.size}")
    private String title;

    @NotBlank(message = "{validation.post.slug.notBlank}")
    @Size(max = 255, message = "{validation.post.slug.size}")
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "{validation.post.slug.pattern}")
    private String slug;

    @Size(max = 500, message = "{validation.post.summary.size}")
    private String summary;

    @NotBlank(message = "{validation.post.content.notBlank}")
    private String content;

    @Size(max = 255, message = "{validation.post.coverImage.size}")
    private String coverImage;

    @NotNull(message = "{validation.post.isPinned.notNull}")
    private Boolean isPinned = false;

    // 状态: 1=已发布, 0=草稿, 默认 0
    @NotNull(message = "{validation.post.status.notNull}")
    @Range(min = 0, max = 1, message = "{validation.post.status.range}")
    private Integer status = 0;

    // 标签列表
    private List<Long> tagIds = new ArrayList<>();

}
