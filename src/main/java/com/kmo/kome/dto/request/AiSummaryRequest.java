package com.kmo.kome.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * AI 摘要生成请求类。
 * 用于封装生成文章摘要时提交的数据，包含文章正文内容。
 */
@Data
public class AiSummaryRequest {
    @NotBlank(message = "文章内容不能为空")
    private String content;
}
