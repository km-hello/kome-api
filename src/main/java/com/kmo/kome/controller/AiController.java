package com.kmo.kome.controller;

import com.kmo.kome.common.Result;
import com.kmo.kome.dto.request.AiSlugRequest;
import com.kmo.kome.dto.request.AiSummaryRequest;
import com.kmo.kome.dto.response.AiResultResponse;
import com.kmo.kome.service.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI 辅助功能控制器。
 * <p>
 * 提供文章摘要生成和 URL Slug 生成的 API 端点。
 * 路径 {@code /api/admin/ai/**} 受 SecurityConfig 中
 * {@code .requestMatchers("/api/admin/**").authenticated()} 规则保护。
 */
@RestController
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    /**
     * 根据文章内容生成摘要。
     * 接收文章正文内容，调用 AI 服务生成简短的中文摘要并返回。
     *
     * @param request 包含文章内容的请求对象。
     * @return 包含生成摘要的结果对象。
     */
    @PostMapping("/api/admin/ai/summary")
    public Result<AiResultResponse> generateSummary(@Valid @RequestBody AiSummaryRequest request) {
        return Result.success(aiService.generateSummary(request));
    }

    /**
     * 根据文章标题生成 URL Slug。
     * 接收文章标题，调用 AI 服务生成 SEO 友好的英文 slug 并返回。
     *
     * @param request 包含文章标题的请求对象。
     * @return 包含生成 slug 的结果对象。
     */
    @PostMapping("/api/admin/ai/slug")
    public Result<AiResultResponse> generateSlug(@Valid @RequestBody AiSlugRequest request) {
        return Result.success(aiService.generateSlug(request));
    }
}
