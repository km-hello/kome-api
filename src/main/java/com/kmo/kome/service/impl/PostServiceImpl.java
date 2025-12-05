package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.request.PostCreateRequest;
import com.kmo.kome.dto.request.PostUpdateRequest;
import com.kmo.kome.entity.Post;
import com.kmo.kome.entity.PostTag;
import com.kmo.kome.mapper.PostMapper;
import com.kmo.kome.mapper.PostTagMapper;
import com.kmo.kome.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        // TODO 计算阅读时间
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

    /**
     * 根据文章 ID 更新文章及其关联的标签数据。
     * 如果指定的文章不存在，则抛出业务异常。
     * 更新包括文章的主表信息和关联的标签列表。
     *
     * @param id 待更新文章的唯一标识符
     * @param request 文章更新请求对象，包含文章的标题、摘要、内容、状态、标签等信息
     * @return 空值，表示更新操作已完成
     * @throws ServiceException 如果文章不存在，则抛出包含 404 状态的业务异常
     */
    @Override
    @Transactional
    public Void updatePostById(Long id, PostUpdateRequest request) {
        // 检查文章是否存在
        Post oldPost = this.getById(id);
        if(oldPost == null){
            throw new ServiceException(ResultCode.NOT_FOUND);
        }

        // 更新文章主表
        Post newPost = new Post();
        BeanUtils.copyProperties(request, newPost);
        newPost.setId(id);
        // TODO 计算阅读时间
        this.updateById(newPost);

        // 更新关联标签
        updatePostTags(id, request.getTagIds());

        return null;
    }

    /**
     * 更新指定文章的标签关联关系。
     * 此方法根据提供的新标签 ID 列表更新文章的标签关联：
     * - 如果新标签列表为空，则移除文章的所有标签。
     * - 删除新标签列表中没有但旧标签列表中存在的标签关联。
     * - 添加新标签列表中存在但旧标签列表中没有的标签关联。
     *
     * @param postId 文章的唯一标识符
     * @param newTagIds 新的标签 ID 列表。如果为空，则移除文章的所有标签。
     */
    private void updatePostTags(Long postId, List<Long> newTagIds) {
        // 查询当前文章已有的所有标签关联
        List<PostTag> oldPostTags = postTagMapper.selectList(
                new LambdaQueryWrapper<PostTag>().eq(PostTag::getPostId, postId)
        );

        Set<Long> oldTagIdsSet = oldPostTags.stream()
                .map(PostTag::getTagId)
                .collect(Collectors.toSet());

        // 如果新标签列表为空，则直接删除所有旧标签
        if(CollectionUtils.isEmpty(newTagIds)){
            if(!oldTagIdsSet.isEmpty()){
                postTagMapper.delete(new LambdaQueryWrapper<PostTag>().eq(PostTag::getPostId, postId));
            }
            return;
        }

        // 计算差集，进行增删操作
        Set<Long> newTagIdsSet = new HashSet<>(newTagIds);
        // 计算需要删除的关联，在 oldTagIds 中存在，但在 newTagIdsSet 中不存在
        List<Long> tagsToDelete = oldTagIdsSet.stream()
                .filter(tagId -> !newTagIdsSet.contains(tagId))
                .toList();

        if(!tagsToDelete.isEmpty()){
            postTagMapper.delete(
                    new LambdaQueryWrapper<PostTag>()
                    .eq(PostTag::getPostId, postId)
                    .in(PostTag::getTagId, tagsToDelete)
            );
        }

        // 计算需要新增的关联，在 newTagIdsSet 中存在，但在 oldTagIds 中不存在
        List<Long> tagsToAdd = newTagIdsSet.stream()
                .filter(tagId -> !oldTagIdsSet.contains(tagId))
                .toList();
        if(!tagsToAdd.isEmpty()){
            tagsToAdd.forEach(tagId -> {
                PostTag postTag = new PostTag();
                postTag.setPostId(postId);
                postTag.setTagId(tagId);
                postTagMapper.insert(postTag);
            });
        }
        return;
    }
}
