package com.kmo.kome.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 备忘录响应 DTO 类。
 * 用于封装备忘录的相关信息并返回给前端。
 * 通常在备忘录管理相关的接口中使用。
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
    private LocalDateTime updateTime;
}
