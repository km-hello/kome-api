package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.response.TagResponse;
import com.kmo.kome.entity.PostTag;
import com.kmo.kome.entity.Tag;
import com.kmo.kome.mapper.PostTagMapper;
import com.kmo.kome.mapper.TagMapper;
import com.kmo.kome.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 标签服务实现类
 * 实现了 TagService 接口，提供创建和更新标签的业务逻辑。
 * 通过继承 ServiceImpl<TagMapper, Tag> 实现通用的增删查改功能，并加入特定的业务约束。
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final PostTagMapper postTagMapper;


    /**
     * 创建一个新的标签。如果标签名称已存在，则抛出业务异常。
     *
     * @param name 新标签的名称
     * @return 创建成功的标签信息
     * @throws ServiceException 如果标签名称已存在
     */
    @Override
    public TagResponse createTag(String name) {
        Tag tag = this.getOne(
                new LambdaQueryWrapper<Tag>().eq(Tag::getName, name)
        );

        if(tag != null){
            throw new ServiceException(ResultCode.BAD_REQUEST, "标签已存在");
        }

        Tag newTag = new Tag();
        newTag.setName(name);
        this.save(newTag);
        return new TagResponse(newTag.getId(), newTag.getName());
    }

    /**
     * 更新指定的标签信息。
     * 如果标签不存在或新的标签名称已被占用，则抛出业务异常。
     *
     * @param id 要更新的标签的唯一标识
     * @param name 要更新的标签的新名称
     * @return 更新后的标签信息
     * @throws ServiceException 如果标签不存在或名称已被占用
     */
    @Override
    public TagResponse updateTagById(Long id, String name) {
        Tag oldTag = this.getById(id);
        if(oldTag == null){
            throw new ServiceException(ResultCode.NOT_FOUND);
        }

        boolean isTagNameTaken = this.lambdaQuery()
                .eq(Tag::getName, name)
                .ne(Tag::getId, id)
                .exists();
        if(isTagNameTaken){
            throw new ServiceException(ResultCode.BAD_REQUEST, "标签名称已被占用");
        }

        Tag newTag = new Tag();
        newTag.setId(id);
        newTag.setName(name);
        this.updateById(newTag);
        return new TagResponse(newTag.getId(), newTag.getName());
    }

    /**
     * 根据主键 ID 删除指定的标签。
     * 在删除标签之前，会执行以下业务逻辑验证：
     * 1. 检查标签是否存在，若不存在则抛出 {@code ServiceException} 异常。
     * 2. 检查标签是否被使用，如果存在关联的文章则抛出 {@code ServiceException} 异常。
     *
     * @param id 要删除的标签的唯一标识
     * @return 被删除的标签信息，如果标签不存在或无法删除则会抛出异常
     * @throws ServiceException 如果标签不存在，抛出 {@code ResultCode.NOT_FOUND} 异常。
     *                          如果标签正在被使用，抛出 {@code ResultCode.FORBIDDEN} 异常，并附上错误原因。
     */
    @Override
    public Void deleteTagById(Long id) {
        // 检查标签是否存在
        Tag tag = this.getById(id);
        if(tag == null){
            throw new ServiceException(ResultCode.NOT_FOUND);
        }

        // 检查标签是否在使用中
        Long postCount = postTagMapper.selectCount(
                new LambdaQueryWrapper<PostTag>().eq(PostTag::getTagId, id)
        );
        if(postCount > 0){
            String errorMessage = String.format("无法删除标签 '%s'，有 %d 篇文章正在使用它。", tag.getName(), postCount);
            throw new ServiceException(ResultCode.FORBIDDEN, errorMessage);
        }
        // 删除标签
        this.removeById(id);
        return null;
    }
}
