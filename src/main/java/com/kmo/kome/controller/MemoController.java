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
     * 更新指定 ID 的备忘录。
     * 接收备忘录 ID 和更新请求对象，调用服务层方法更新备忘录内容，并返回操作结果。
     *
     * @param id 要更新的备忘录 ID。
     * @param request 备忘录更新请求对象，包含更新后的内容、是否置顶和状态信息。
     * @return 一个空的 {@code Result<Void>} 对象，表示更新操作的结果。
     */
    @PutMapping("/api/admin/memos/{id}")
    public Result<Void> updateMemoById(@PathVariable Long id,@Valid @RequestBody MemoUpdateRequest request){
        return Result.success(memoService.updateMemoById(id, request));
    }

    /**
     * 根据指定的备忘录 ID 删除备忘录。
     * 调用服务层方法执行删除操作，并返回删除操作的结果。
     *
     * @param id 要删除的备忘录 ID。
     * @return 一个空的 {@code Result<Void>} 对象，表示删除操作的结果。
     */
    @DeleteMapping("/api/admin/memos/{id}")
    public Result<Void> deleteMemoById(@PathVariable Long id){
        return Result.success(memoService.deleteMemoById(id));
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
