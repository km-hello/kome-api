package com.kmo.kome.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI 生成结果响应类。
 * 用于封装 AI 服务返回的生成结果，包含单个文本字段。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiResultResponse {
    private String result;
}
