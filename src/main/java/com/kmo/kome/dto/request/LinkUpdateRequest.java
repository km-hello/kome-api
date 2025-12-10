package com.kmo.kome.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新友链请求类。
 * 用于封装更新友链信息时提交的数据。
 * 包括友链名称、链接地址、头像和描述等字段。
 */
@Data
public class LinkUpdateRequest {
    @Size(min = 1, max = 100, message = "友链名称不能超过100个字符, 且不能为空")
    private String name;

    @Size(max = 255, message = "友链URL长度不能超过255")
    private String url;

    @Size(max = 255, message = "友链头像链接长度不能超过255")
    private String avatar;

    @Size(max = 255, message = "友链描述不能超过255个字符")
    private String description;
}