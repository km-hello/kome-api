package com.kmo.kome.dto.request;

import lombok.Data;

/**
 * 更新备忘录请求类。
 * 用于封装更新备忘录时提交的数据。
 * 包括备忘录的内容、是否置顶以及状态字段。
 */
@Data
public class MemoUpdateRequest {
    private String content;
    private Boolean isPinned;
    private Integer status;
}
