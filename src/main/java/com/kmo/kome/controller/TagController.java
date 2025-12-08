package com.kmo.kome.controller;

import com.kmo.kome.common.PageResult;
import com.kmo.kome.common.Result;
import com.kmo.kome.dto.request.TagCreateRequest;
import com.kmo.kome.dto.request.TagQueryRequest;
import com.kmo.kome.dto.request.TagUpdateRequest;
import com.kmo.kome.dto.response.TagPostCountResponse;
import com.kmo.kome.dto.response.TagResponse;
import com.kmo.kome.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Result<TagResponse> updateTagById(@PathVariable Long id, @RequestBody TagUpdateRequest request){
        return Result.success(tagService.updateTagById(id, request.getName()));
    }

    /**
     * 根据指定的标签 ID 删除标签。
     * 调用服务层方法删除标签，并返回操作结果。
     *
     * @param id 要删除的标签 ID。
     * @return 一个空的 {@code Result<Void>} 对象，表示删除操作的结果。
     */
    @DeleteMapping("/api/admin/tags/{id}")
    public Result<Void> deleteTagById(@PathVariable Long id){
        return Result.success(tagService.deleteTagById(id));
    }

    /**
     * 分页查询标签信息及其对应的文章数量。
     * 接收分页查询请求对象，返回分页结果，包括标签信息和文章数量。
     *
     * @param request 标签查询请求参数，包含分页页码和每页数量。
     * @return 包含标签信息及文章数量的分页查询结果。
     */
    @GetMapping("/api/admin/tags")
    public Result<PageResult<TagPostCountResponse>> getAdminTagPage(TagQueryRequest request){
        return Result.success(tagService.getAdminTagPage(request));
    }

    /**
     * 获取公共可见的标签及其对应的文章数量列表。
     * 调用服务层方法，查询所有公开标签的信息并返回。
     *
     * @return 包含标签及其文章数量的结果对象列表。
     */
    @GetMapping("/api/tags")
    public Result<List<TagPostCountResponse>> getPublicTagPage(){
        return Result.success(tagService.getPublicTagList());
    }

}
