package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmo.kome.dto.request.PostCreateRequest;
import com.kmo.kome.entity.Post;
import com.kmo.kome.entity.PostTag;
import com.kmo.kome.mapper.PostMapper;
import com.kmo.kome.mapper.PostTagMapper;
import com.kmo.kome.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    private final PostTagMapper postTagMapper;

    @Override
    @Transactional
    public Long createPost(PostCreateRequest request) {
        // 保存文章
        Post newPost = new Post();
        BeanUtils.copyProperties(request, newPost);
        this.save(newPost);

        // 处理关联标签
        List<Long> tagIds = request.getTagIds();
        if(tagIds != null && !tagIds.isEmpty()){
            List<PostTag> postTagList = tagIds.stream().map(tagId ->{
                PostTag postTag = new PostTag();
                postTag.setPostId(newPost.getId());
                postTag.setTagId(tagId);
                return postTag;
            }).toList();
            postTagList.forEach(this.postTagMapper::insert);
        }

        return newPost.getId();
    }
}
