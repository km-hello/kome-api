package com.kmo.kome.controller;

import com.kmo.kome.common.Result;
import com.kmo.kome.dto.request.TagCreateRequest;
import com.kmo.kome.dto.request.TagUpdateRequest;
import com.kmo.kome.dto.response.TagResponse;
import com.kmo.kome.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 标签控制器类。
 * 负责处理与标签相关的请求，如创建和更新标签。
 * 使用了 @RestController 注解标识为控制器类，并通过 @RequiredArgsConstructor 自动生成构造函数注入服务对象。
 */
@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    /**
     * 创建一个新的标签。
     * 接收标签创建请求对象，调用服务层方法根据标签名称创建标签，并返回标签相关的信息。
     *
     * @param request 标签创建请求参数，包含标签名称。
     * @return 包含已创建标签信息的结果对象。
     */
    @PostMapping("/api/admin/tags")
    public Result<TagResponse> createTag(@RequestBody TagCreateRequest request){
        return Result.success(tagService.createTag(request.getName()));
    }

    /**
     * 更新指定的标签信息。
     * 接收标签 ID 和标签更新请求对象，调用服务层方法更新标签信息，并返回更新后的标签数据。
     *
     * @param id 要更新的标签 ID。
     * @param request 标签更新请求参数，包含新的标签名称。
     * @return 包含更新后标签信息的结果对象。
     */
    @PutMapping("/api/admin/tags/{id}")
    public Result<TagResponse> updateTag(@PathVariable Long id, @RequestBody TagUpdateRequest request){
        return Result.success(tagService.updateTag(id, request.getName()));
    }

}
