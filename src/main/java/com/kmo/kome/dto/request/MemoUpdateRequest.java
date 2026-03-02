package com.kmo.kome.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 更新 Memo 请求类。
 * 用于封装更新 Memo 时提交的数据。
 * 包括 Memo 的内容、是否置顶以及状态字段。
 */
@Data
public class MemoUpdateRequest {
    @Size(min = 1, message = "{validation.memo.content.size}")
    private String content;

    private Boolean isPinned;

    @Range(min = 0, max = 1, message = "{validation.memo.status.range}")
    private Integer status;
}
