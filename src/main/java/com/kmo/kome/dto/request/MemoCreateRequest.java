package com.kmo.kome.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 创建 Memo 请求类。
 * 用于封装创建新 Memo 时提交的数据。
 * 包括 Memo 内容、是否置顶以及状态字段。
 */
@Data
public class MemoCreateRequest {
    @NotBlank(message = "内容不能为空")
    private String content;

    @NotNull(message = "置顶状态不能为空")
    private Boolean isPinned = false;

    @NotNull(message = "Memo状态不能为空")
    @Range(min = 0, max = 1, message = "状态只能是 0 或 1")
    private Integer status = 0;
}
