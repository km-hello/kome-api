package com.kmo.kome.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建友链请求类。
 * 用于封装创建新友链时提交的数据。
 * 包括友链名称、链接地址、头像和描述等字段。
 */
@Data
public class LinkCreateRequest {
    @NotBlank(message = "友链名称不能为空")
    @Size(max = 100, message = "友链名称不能超过100个字符")
    private String name;

    @NotBlank(message = "友链URL不能为空")
    @Size(max = 255, message = "友链URL长度不能超过255")
    private String url;

    @Size(max = 255, message = "友链头像链接长度不能超过255")
    private String avatar;

    @Size(max = 255, message = "友链描述不能超过255个字符")
    private String description;
}
