package com.kmo.kome.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文章详情响应 DTO 类。
 * 用于封装文章的详细信息并返回给前端。
 * 通常在获取文章详情的接口中使用。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponse {
    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String content;
    private String coverImage;
    private Integer views;
    private Integer readTime;
    private Boolean isPinned;
    private Integer status;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
