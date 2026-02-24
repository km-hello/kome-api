package com.kmo.kome.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * AI Slug 生成请求类。
 * 用于封装生成 URL Slug 时提交的数据，包含文章标题。
 */
@Data
public class AiSlugRequest {
    @NotBlank(message = "文章标题不能为空")
    @Size(max = 255, message = "文章标题过长")
    private String title;
}
