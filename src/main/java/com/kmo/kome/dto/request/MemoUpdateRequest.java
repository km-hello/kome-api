package com.kmo.kome.dto.request;

import lombok.Data;

/**
 * 更新 Memo 请求类。
 * 用于封装更新 Memo 时提交的数据。
 * 包括 Memo 的内容、是否置顶以及状态字段。
 */
@Data
public class MemoUpdateRequest {
    private String content;
    private Boolean isPinned;
    private Integer status;
}
