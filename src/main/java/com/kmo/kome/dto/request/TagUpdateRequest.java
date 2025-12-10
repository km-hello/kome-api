package com.kmo.kome.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新标签请求类。
 * 用于封装更新标签信息时提交的数据。
 * 主要应用于标签相关的更新操作请求中。
 */
@Data
public class TagUpdateRequest {
    @NotBlank(message = "标签名称不能为空")
    @Size(min = 1, max = 50, message = "标签名称不能超过50个字符，且不能为空")
    private String name;
}
