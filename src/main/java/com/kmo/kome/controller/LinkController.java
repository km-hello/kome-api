package com.kmo.kome.controller;

import com.kmo.kome.common.PageResult;
import com.kmo.kome.common.Result;
import com.kmo.kome.dto.request.LinkCreateRequest;
import com.kmo.kome.dto.request.LinkQueryRequest;
import com.kmo.kome.dto.request.LinkUpdateRequest;
import com.kmo.kome.dto.response.LinkResponse;
import com.kmo.kome.service.LinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    /**
     * 创建新的友情链接。
     * 接收前端提交的创建请求对象，调用服务方法处理创建操作，并返回创建结果的 ID。
     *
     * @param request 创建友情链接的请求对象，包含名称、链接地址、头像和描述信息。
     * @return 包含新创建友情链接 ID 的结果对象。
     */
    @PostMapping("/api/admin/links")
    public Result<Long> createLink(@Valid @RequestBody LinkCreateRequest request){
        return Result.success(linkService.createLink(request));
    }

    /**
     * 更新指定的友情链接信息。
     * 接收友链 ID 和更新请求对象，调用服务方法更新指定的友链信息，并返回操作结果。
     *
     * @param id 要更新的友情链接 ID。
     * @param request 更新友情链接的请求对象，包含友链名称、链接地址、头像和描述信息。
     * @return 一个空的 {@code Result<Void>} 对象，表示更新操作的结果。
     */
    @PutMapping("/api/admin/links/{id}")
    public Result<Void> updateLinkById(@PathVariable Long id,@Valid @RequestBody LinkUpdateRequest request){
        return Result.success(linkService.updateLinkById(id, request));
    }

    /**
     * 根据指定的友情链接 ID 删除友链。
     * 调用服务层方法执行删除操作，并返回操作结果。
     *
     * @param id 要删除的友情链接 ID。
     * @return 一个空的 {@code Result<Void>} 对象，表示删除操作的结果。
     */
    @DeleteMapping("/api/admin/links/{id}")
    public Result<Void> deleteLinkById(@PathVariable Long id){
        return Result.success(linkService.deleteLinkById(id));
    }

    /**
     * 获取公开的友情链接列表。
     * 根据查询参数筛选友链信息，并返回符合条件的公开友链列表。
     *
     * @param request 友链查询请求对象，包含分页参数和关键词筛选条件。
     * @return 包含公开友链信息的结果对象，数据为 {@code List<LinkResponse>} 类型。
     */
    @GetMapping("/api/links")
    public Result<List<LinkResponse>> getPublicLinkList(@Valid LinkQueryRequest request){
        return Result.success(linkService.getPublicLinkList(request));
    }

    /**
     * 获取后台管理端的友情链接分页数据。
     * 根据查询请求参数执行分页查询，并返回包含分页信息的友情链接列表。
     *
     * @param request 友链查询请求对象，包含分页参数和关键词筛选条件。
     * @return 包含分页结果的 {@code Result<PageResult<LinkResponse>>} 对象，其中数据部分为友情链接分页列表。
     */
    @GetMapping("/api/admin/links")
    public Result<PageResult<LinkResponse>> getAdminLinkPage(@Valid LinkQueryRequest request){
        return Result.success(linkService.getAdminLinkPage(request));
    }


}
