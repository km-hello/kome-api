package com.kmo.kome.controller;

import com.kmo.kome.common.Result;
import com.kmo.kome.dto.request.PostCreateRequest;
import com.kmo.kome.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
