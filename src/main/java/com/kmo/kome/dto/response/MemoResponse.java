package com.kmo.kome.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *  Memo 响应 DTO 类。
 * 用于封装 Memo 的相关信息并返回给前端。
 * 通常在 Memo 管理相关的接口中使用。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoResponse {
    private Long id;
    private String content;

    private Boolean isPinned;
    private Integer status;

    private LocalDateTime createTime;
}
