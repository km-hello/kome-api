package com.kmo.kome.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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

    private List<TagResponse> tags;

    /**
     * 上一篇文章（比当前文章发布时间早的最新一篇）
     */
    private PostNavResponse previous;

    /**
     * 下一篇文章（比当前文章发布时间晚的最早一篇）
     */
    private PostNavResponse next;
}
