package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
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

    /**
     * 根据文章 ID 删除指定的文章及其关联数据。
     * 该方法首先检查文章是否存在，如果不存在则抛出业务异常，
     * 然后删除文章与标签的关联记录，最后删除文章信息。
     *
     * @param id 待删除文章的唯一标识符
     * @return 空值，表示删除操作已完成
     * @throws ServiceException 如果文章不存在，则抛出包含 404 状态的业务异常
     */
    @Override
    @Transactional
    public Void deletePostById(Long id) {
        // 检查 post 是否存在
        Post post = this.getById(id);
        if(post == null){
            throw new ServiceException(ResultCode.NOT_FOUND, "未找到该文章");
        }

        // 删除 post_tag 关联表中的相关记录
        postTagMapper.delete(
                new LambdaQueryWrapper<PostTag>()
                        .eq(PostTag::getPostId, id)
        );

        // 删除文章
        this.removeById(id);
        return null;
    }
}
