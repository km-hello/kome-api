package com.kmo.kome.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 创建友链请求类。
 * 用于封装创建新友链时提交的数据。
 * 包括友链名称、链接地址、头像和描述等字段。
 */
@Data
public class LinkCreateRequest {
    @NotBlank(message = "name 不能为空")
    private String name;
    @NotBlank(message = "url 不能为空")
    private String url;
    private String avatar;
    private String description;
}
