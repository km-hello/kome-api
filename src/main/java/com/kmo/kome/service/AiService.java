package com.kmo.kome.service;

import com.kmo.kome.dto.request.AiSlugRequest;
import com.kmo.kome.dto.request.AiSummaryRequest;
import com.kmo.kome.dto.response.AiResultResponse;
import jakarta.validation.Valid;

/**
 * AI 服务接口。
 * <p>
 * 提供基于 AI 的文章辅助功能，包括摘要生成和 URL Slug 生成。
 * 通过后端代理调用 OpenAI 兼容 API，API Key 安全存储在服务端。
 */
public interface AiService {

    /**
     * 根据文章内容生成简短的中文摘要。
     *
     * @param request 包含文章正文内容的请求对象。
     * @return 包含生成摘要的结果对象。
     */
    AiResultResponse generateSummary(@Valid AiSummaryRequest request);

    /**
     * 根据文章标题生成 SEO 友好的英文 URL Slug。
     *
     * @param request 包含文章标题的请求对象。
     * @return 包含生成 slug 的结果对象。
     */
    AiResultResponse generateSlug(@Valid AiSlugRequest request);
}
