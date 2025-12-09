package com.kmo.kome.controller;

import com.kmo.kome.common.PageResult;
import com.kmo.kome.common.Result;
import com.kmo.kome.dto.request.MemoCreateRequest;
import com.kmo.kome.dto.request.MemoQueryRequest;
import com.kmo.kome.dto.request.MemoUpdateRequest;
import com.kmo.kome.dto.response.MemoResponse;
import com.kmo.kome.service.MemoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    /**
     * 创建 Memo 。
     * 接收 Memo 创建请求对象，调用服务层方法生成新的 Memo ，并返回新 Memo 的 ID。
     *
     * @param request  Memo 创建请求对象，包含 Memo 的内容、是否置顶以及状态信息。
     * @return 包含新建 Memo  ID 的结果对象。
     */
    @PostMapping("/api/admin/memos")
    public Result<Long> createMemo(@Valid @RequestBody MemoCreateRequest request){
        return Result.success(memoService.createMemo(request));
    }

    /**
     * 更新指定 ID 的 Memo 。
     * 接收 Memo  ID 和更新请求对象，调用服务层方法更新 Memo 内容，并返回操作结果。
     *
     * @param id 要更新的 Memo  ID。
     * @param request  Memo 更新请求对象，包含更新后的内容、是否置顶和状态信息。
     * @return 一个空的 {@code Result<Void>} 对象，表示更新操作的结果。
     */
    @PutMapping("/api/admin/memos/{id}")
    public Result<Void> updateMemoById(@PathVariable Long id,@Valid @RequestBody MemoUpdateRequest request){
        return Result.success(memoService.updateMemoById(id, request));
    }

    /**
     * 根据指定的 Memo  ID 删除 Memo 。
     * 调用服务层方法执行删除操作，并返回删除操作的结果。
     *
     * @param id 要删除的 Memo  ID。
     * @return 一个空的 {@code Result<Void>} 对象，表示删除操作的结果。
     */
    @DeleteMapping("/api/admin/memos/{id}")
    public Result<Void> deleteMemoById(@PathVariable Long id){
        return Result.success(memoService.deleteMemoById(id));
    }


    /**
     * 查询公开的 Memo 分页列表。
     * 根据请求参数筛选符合条件的 Memo ，返回分页结果。
     *
     * @param request 包含分页参数（页码、每页记录数）及筛选条件（关键词、状态）的查询请求对象。
     * @return 包含公开 Memo 分页数据的结果对象，数据类型为 {@code Result<PageResult<MemoResponse>>}。
     */
    @GetMapping("/api/memos")
    public Result<PageResult<MemoResponse>> getPublicMemoPage(@Valid MemoQueryRequest request){
        return Result.success(memoService.getPublicMemoPage(request));
    }


    /**
     * 查询管理员的 Memo 分页列表。
     * 根据请求参数筛选符合条件的 Memo ，返回分页结果。
     *
     * @param request 包含分页参数（页码、每页记录数）及筛选条件（关键词、状态）的查询请求对象。
     * @return 包含 Memo 分页数据的结果对象，数据类型为 {@code Result<PageResult<MemoResponse>>}。
     */
    @GetMapping("/api/admin/memos")
    public Result<PageResult<MemoResponse>> getAdminMemoPage(@Valid MemoQueryRequest request){
        return Result.success(memoService.getAdminMemoPage(request));
    }

    /**
     * 获取最新的 Memo 列表。
     * 调用服务层方法，根据指定的数量限制查询最近创建的 Memo ，并返回结果。
     *
     * @param limit 查询的 Memo 数量限制。如果未指定，默认为 2。
     * @return 包含最新 Memo 列表的结果对象，其数据类型为 {@code Result<List<MemoResponse>>}。
     */
    @GetMapping("/api/memos/latest")
    public Result<List<MemoResponse>> getLatestMemo(@RequestParam(value = "limit", defaultValue = "2") Integer limit){
        return Result.success(memoService.getLatestMemo(limit));
    }
}
