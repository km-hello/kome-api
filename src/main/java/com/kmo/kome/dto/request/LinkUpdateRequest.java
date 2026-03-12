package com.kmo.kome.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 更新友链请求类。
 * 用于封装更新友链信息时提交的数据。
 * 包括友链名称、链接地址、头像和描述等字段。
 */
@Data
public class LinkUpdateRequest {
    /** 仅允许 http/https 协议的链接地址 */
    private static final String HTTP_URL_PATTERN = "^https?://.+$";

    @NotBlank(message = "{validation.link.name.notBlank}")
    @Size(max = 100, message = "{validation.link.name.size}")
    private String name;

    @NotBlank(message = "{validation.link.url.notBlank}")
    @Size(max = 255, message = "{validation.link.url.size}")
    @Pattern(regexp = HTTP_URL_PATTERN, message = "{validation.link.url.pattern}")
    private String url;

    @Size(max = 255, message = "{validation.link.avatar.size}")
    @Pattern(regexp = HTTP_URL_PATTERN, message = "{validation.link.avatar.pattern}")
    private String avatar;

    @Size(max = 255, message = "{validation.link.description.size}")
    private String description;

    @NotNull(message = "{validation.link.status.notNull}")
    @Range(min = 0, max = 1, message = "{validation.link.status.range}")
    private Integer status;
}
