package com.kmo.kome.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章导航响应 DTO 类。
 * 用于封装上一篇/下一篇文章的基本信息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostNavResponse {
    private Long id;
    private String title;
    private String slug;
}
