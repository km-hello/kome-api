package com.kmo.kome.controller;

import com.kmo.kome.common.Result;
import com.kmo.kome.dto.request.PostCreateRequest;
import com.kmo.kome.dto.request.PostUpdateRequest;
import com.kmo.kome.dto.response.PostDetailResponse;
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
    public Result<Void> deletePostById(@PathVariable Long id){
        return Result.success(postService.deletePostById(id));
    }

    /**
     * 根据文章 ID 更新文章信息。
     * 接收文章更新请求对象，调用服务层方法根据文章 ID 更新对应的文章数据。
     *
     * @param id 要更新的文章 ID。
     * @param request 文章更新请求参数，包含文章的标题、摘要、内容、封面图片、置顶状态、文章状态及标签列表等内容。
     * @return 一个空的 {@code Result<Void>} 对象，表示更新操作的结果。
     */
    @PutMapping("/api/admin/posts/{id}")
    public Result<Void> updatePostById(@PathVariable Long id, @Valid @RequestBody PostUpdateRequest request){
        return Result.success(postService.updatePostById(id, request));
    }

    /**
     * 根据文章 ID 获取文章详情。
     * 调用服务层方法查询指定 ID 的文章信息，并返回文章的详细数据。
     *
     * @param id 要查询的文章 ID。
     * @return 包含文章详细信息的结果对象 {@code Result<PostDetailResponse>}。
     */
    @GetMapping("/api/admin/posts/{id}")
    public Result<PostDetailResponse> getPostById(@PathVariable Long id){
        return Result.success(postService.getPostById(id));
    }

    /**
     * 根据文章的唯一标识符（slug）获取文章详情。
     * 调用服务层方法查询指定 slug 的文章信息，并返回包含文章详情的结果对象。
     *
     * @param slug 文章的唯一标识符，用于精确定位文章。
     * @return 包含文章详细信息的结果对象 {@code Result<PostDetailResponse>}。
     */
    @GetMapping("/api/posts/{slug}")
    public Result<PostDetailResponse> getPostBySlug(@PathVariable String slug){
        return Result.success(postService.getPostBySlug(slug));
    }
}
