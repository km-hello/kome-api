package com.kmo.kome.dto.request;

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
    @Size(min = 1, max = 100, message = "{validation.link.name.sizeUpdate}")
    private String name;

    @Size(max = 255, message = "{validation.link.url.size}")
    private String url;

    @Size(max = 255, message = "{validation.link.avatar.size}")
    private String avatar;

    @Size(max = 255, message = "{validation.link.description.size}")
    private String description;

    @Range(min = 0, max = 1, message = "{validation.link.status.range}")
    private Integer status;
}
