package com.kmo.kome.controller;

import com.kmo.kome.common.Result;
import com.kmo.kome.dto.request.PostCreateRequest;
import com.kmo.kome.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 创建一篇新文章。
     * 接收文章创建请求对象，调用服务层方法创建新文章，并返回创建的文章 ID。
     *
     * @param request 文章创建请求参数，包含标题、内容、摘要、封面图片、状态等信息。
     * @return 包含新创建文章 ID 的结果对象。
     */
    @PostMapping("/api/admin/posts")
    public Result<Long> createPost(@Valid @RequestBody PostCreateRequest request){
        return Result.success(postService.createPost(request));
    }

    /**
     * 删除指定 ID 的文章。
     * 调用服务层方法根据文章 ID 删除对应的文章，并返回操作结果。
     *
     * @param id 要删除的文章 ID。
     * @return 一个空的 {@code Result<Void>} 对象，表示删除操作的结果。
     */
    @DeleteMapping("/api/admin/posts/{id}")
    public Result<Void> deletePost(@PathVariable Long id){
        return Result.success(postService.deletePostById(id));
    }
}
