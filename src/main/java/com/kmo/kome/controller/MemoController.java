package com.kmo.kome.controller;

import com.kmo.kome.common.PageResult;
import com.kmo.kome.common.Result;
import com.kmo.kome.dto.request.MemoQueryRequest;
import com.kmo.kome.dto.response.MemoResponse;
import com.kmo.kome.service.MemoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

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
