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
    @NotBlank(message = "文章标题不能为空")
    @Size(max = 255, message = "文章标题不能超过255个字符")
    private String title;

    @NotBlank(message = "文章别名不能为空")
    @Size(max = 255, message = "文章别名不能超过255个字符")
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "文章别名格式不正确")
    private String slug;

    @Size(max = 500, message = "文章摘要不能超过500个字符")
    private String summary;

    @NotBlank(message = "文章内容不能为空")
    private String content;

    @Size(max = 255, message = "封面链接长度不能超过255")
    private String coverImage;

    @NotNull(message = "置顶状态不能为空")
    private Boolean isPinned = false;

    // 状态: 1=已发布, 0=草稿, 默认 0
    @NotNull(message = "文章状态不能为空")
    @Range(min = 0, max = 1, message = "状态只能是 0 或 1")
    private Integer status = 0;

    // 标签列表
    private List<Long> tagIds = new ArrayList<>();

}
