package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.response.TagResponse;
import com.kmo.kome.entity.Tag;
import com.kmo.kome.mapper.TagMapper;
import com.kmo.kome.service.TagService;
import org.springframework.stereotype.Service;

/**
 * 标签服务实现类
 * 实现了 TagService 接口，提供创建和更新标签的业务逻辑。
 * 通过继承 ServiceImpl<TagMapper, Tag> 实现通用的增删查改功能，并加入特定的业务约束。
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
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
    public TagResponse updateTag(Long id, String name) {
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
}
