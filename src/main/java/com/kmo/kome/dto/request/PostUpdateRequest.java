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
    @Size(min = 1, max = 255, message = "文章标题不能超过255个字符，且不能为空")
    private String title;

    @Size(max = 255, message = "文章别名不能超过255个字符")
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "文章别名格式不正确")
    private String slug;

    @Size(max = 500, message = "文章摘要不能超过500个字符")
    private String summary;

    @Size(min = 1, message = "文章内容不能为空")
    private String content;

    @Size(max = 255, message = "封面链接长度不能超过255")
    private String coverImage;

    // 是否置顶
    private Boolean isPinned;

    @Range(min = 0, max = 1, message = "状态只能是 0 或 1")
    private Integer status;

    // 标签列表
    private List<Long> tagIds;
}
