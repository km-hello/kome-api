package com.kmo.kome.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 社交链接 DTO
 * 用于存储用户的社交媒体链接信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialLink {
    /**
     * 平台标识: github, twitter, email, homepage, rss, bilibili, weibo 等
     */
    private String platform;

    /**
     * 链接地址
     */
    private String url;
}
