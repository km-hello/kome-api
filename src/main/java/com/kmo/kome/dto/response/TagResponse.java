package com.kmo.kome.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签响应 DTO 类。
 * 用于封装标签相关信息并返回给前端。
 * 通常在标签相关的接口中使用。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagResponse {
    private Long id;
    private String name;
}
