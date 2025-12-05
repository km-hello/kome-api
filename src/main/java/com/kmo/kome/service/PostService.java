package com.kmo.kome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmo.kome.dto.request.PostCreateRequest;
import com.kmo.kome.dto.request.PostUpdateRequest;
import com.kmo.kome.dto.response.PostDetailResponse;
import com.kmo.kome.entity.Post;
import jakarta.validation.Valid;

/**
 * 博客文章业务接口
 * <p>
 * 继承 IService<Post> 接口，提供文章相关的基础 CRUD 操作能力。
 * 扩展了创建文章的自定义业务方法。
 */
public interface PostService extends IService<Post> {
    Long createPost(@Valid PostCreateRequest request);

    Void deletePostById(Long id);

    Void updatePostById(Long id, @Valid PostUpdateRequest request);

    PostDetailResponse getPostById(Long id);
}

