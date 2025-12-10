package com.kmo.kome.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建标签请求类。
 * 用于封装创建新标签时提交的数据。
 * 主要应用于标签相关的创建操作请求中。
 */
@Data
public class TagCreateRequest {
    @NotBlank(message = "标签名称不能为空")
    @Size(max = 50, message = "标签名称不能超过50个字符")
    private String name;
}
