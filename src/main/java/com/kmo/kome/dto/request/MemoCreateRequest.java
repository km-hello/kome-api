package com.kmo.kome.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 创建 Memo 请求类。
 * 用于封装创建新 Memo 时提交的数据。
 * 包括 Memo 内容、是否置顶以及状态字段。
 */
@Data
public class MemoCreateRequest {
    @NotBlank(message = "内容不能为空")
    private String content;
    private Boolean isPinned = false;
    private Integer status = 0;
}
