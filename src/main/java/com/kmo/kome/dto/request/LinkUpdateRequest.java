package com.kmo.kome.dto.request;

import lombok.Data;

/**
 * 更新友链请求类。
 * 用于封装更新友链信息时提交的数据。
 * 包括友链名称、链接地址、头像和描述等字段。
 */
@Data
public class LinkUpdateRequest {
    private String name;
    private String url;
    private String avatar;
    private String description;
}