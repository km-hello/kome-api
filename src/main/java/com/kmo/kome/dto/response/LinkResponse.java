package com.kmo.kome.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 链接响应 DTO 类。
 * 用于封装链接相关的信息并返回给前端。
 * 通常在链接相关的接口中使用。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkResponse {
    private Long id;
    private String name;
    private String url;
    private String avatar;
    private String description;
    private Integer status;
}
