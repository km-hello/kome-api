package com.kmo.kome.service;

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
     * @param content 文章正文内容。
     * @return 生成的摘要文本。
     */
    String generateSummary(String content);

    /**
     * 根据文章标题生成 SEO 友好的英文 URL Slug。
     *
     * @param title 文章标题。
     * @return 生成的 slug 文本。
     */
    String generateSlug(String title);
}
