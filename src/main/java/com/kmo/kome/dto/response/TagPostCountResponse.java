package com.kmo.kome.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 标签文章计数响应 DTO 类。
 * 用于封装标签及其对应文章数量的信息，并返回给前端。
 * 通常在统计标签文章数量的接口中使用。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagPostCountResponse {
    private Long id;
    private String name;
    // 该标签下文章的数量
    private Long postCount;

    private LocalDateTime createTime;
}
