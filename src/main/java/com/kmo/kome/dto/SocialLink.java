package com.kmo.kome.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
     * 平台标识: github, twitter, email, homepage, telegram 等
     */
    @NotBlank(message = "{validation.socialLink.platform.notBlank}")
    @Size(max = 50, message = "{validation.socialLink.platform.size}")
    private String platform;

    /**
     * 链接地址
     */
    @NotBlank(message = "{validation.socialLink.url.notBlank}")
    @Size(max = 255, message = "{validation.socialLink.url.size}")
    private String url;
}
