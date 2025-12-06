package com.kmo.kome.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章概要响应 DTO 类。
 * 用于封装文章的基本信息并返回给前端。
 * 通常在文章列表或概要信息的接口中使用。
 */
@Data
public class PostSimpleResponse {
    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String coverImage;
    private Integer views;
    private Integer readTime;
    private Boolean isPinned;
    private Integer status;

    private LocalDateTime createTime;

    private List<TagResponse> tags;
}
