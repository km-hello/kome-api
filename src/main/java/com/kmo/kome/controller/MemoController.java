package com.kmo.kome.controller;

import com.kmo.kome.common.PageResult;
import com.kmo.kome.common.Result;
import com.kmo.kome.dto.request.MemoCreateRequest;
import com.kmo.kome.dto.request.MemoQueryRequest;
import com.kmo.kome.dto.response.MemoResponse;
import com.kmo.kome.service.MemoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    /**
     * 创建备忘录。
     * 接收备忘录创建请求对象，调用服务层方法生成新的备忘录，并返回新备忘录的 ID。
     *
     * @param request 备忘录创建请求对象，包含备忘录的内容、是否置顶以及状态信息。
     * @return 包含新建备忘录 ID 的结果对象。
     */
    @PostMapping("/api/admin/memos")
    public Result<Long> createMemo(@Valid @RequestBody MemoCreateRequest request){
        return Result.success(memoService.createMemo(request));
    }


    /**
     * 查询公开的备忘录分页列表。
     * 根据请求参数筛选符合条件的备忘录，返回分页结果。
     *
     * @param request 包含分页参数（页码、每页记录数）及筛选条件（关键词、状态）的查询请求对象。
     * @return 包含公开备忘录分页数据的结果对象，数据类型为 {@code Result<PageResult<MemoResponse>>}。
     */
    @GetMapping("/api/memos")
    public Result<PageResult<MemoResponse>> getPublicMemoPage(@Valid MemoQueryRequest request){
        return Result.success(memoService.getPublicMemoPage(request));
    }


    /**
     * 查询管理员的备忘录分页列表。
     * 根据请求参数筛选符合条件的备忘录，返回分页结果。
     *
     * @param request 包含分页参数（页码、每页记录数）及筛选条件（关键词、状态）的查询请求对象。
     * @return 包含备忘录分页数据的结果对象，数据类型为 {@code Result<PageResult<MemoResponse>>}。
     */
    @GetMapping("/api/admin/memos")
    public Result<PageResult<MemoResponse>> getAdminMemoPage(@Valid MemoQueryRequest request){
        return Result.success(memoService.getAdminMemoPage(request));
    }
}
