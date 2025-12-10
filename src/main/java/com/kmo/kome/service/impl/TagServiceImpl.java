package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmo.kome.common.PageResult;
import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.TagWhitPostIdDTO;
import com.kmo.kome.dto.request.TagQueryRequest;
import com.kmo.kome.dto.response.TagPostCountResponse;
import com.kmo.kome.dto.response.TagResponse;
import com.kmo.kome.entity.PostTag;
import com.kmo.kome.entity.Tag;
import com.kmo.kome.mapper.TagMapper;
import com.kmo.kome.service.PostTagService;
import com.kmo.kome.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标签服务实现类
 * 实现了 TagService 接口，提供创建和更新标签的业务逻辑。
 * 通过继承 ServiceImpl<TagMapper, Tag> 实现通用的增删查改功能，并加入特定的业务约束。
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final PostTagService postTagService;


    /**
     * 创建一个新的标签。如果标签名称已存在，则抛出业务异常。
     *
     * @param name 新标签的名称
     * @return 创建成功的标签信息
     * @throws ServiceException 如果标签名称已存在
     */
    @Override
    public TagResponse createTag(String name) {
        // 检查标签是否已存在
        Tag tag = getOne(
                new LambdaQueryWrapper<Tag>().eq(Tag::getName, name)
        );
        if (tag != null) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "标签已存在");
        }

        // 创建新标签
        Tag newTag = new Tag();
        newTag.setName(name);
        save(newTag);
        return new TagResponse(newTag.getId(), newTag.getName());
    }

    /**
     * 更新指定的标签信息。
     * 如果标签不存在或新的标签名称已被占用，则抛出业务异常。
     *
     * @param id   要更新的标签的唯一标识
     * @param name 要更新的标签的新名称
     * @return 更新后的标签信息
     * @throws ServiceException 如果标签不存在或名称已被占用
     */
    @Override
    public TagResponse updateTagById(Long id, String name) {
        // 检查标签是否存在
        Tag oldTag = getById(id);
        if (oldTag == null) {
            throw new ServiceException(ResultCode.NOT_FOUND, "标签不存在");
        }

        // 检查新标签名是否被占用
        boolean isTagNameTaken = lambdaQuery()
                .eq(Tag::getName, name)
                .ne(Tag::getId, id)
                .exists();
        if (isTagNameTaken) {
            throw new ServiceException(ResultCode.BAD_REQUEST, "标签名称已被占用");
        }

        // 更新标签名称
        update(Wrappers.<Tag>lambdaUpdate()
                .eq(Tag::getId, id)
                .set(Tag::getName, name)
        );
        return new TagResponse(id, name);
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
        Tag tag = getById(id);
        if (tag == null) {
            throw new ServiceException(ResultCode.NOT_FOUND, "标签不存在");
        }

        // 检查标签是否在使用中
        long postCount = postTagService.count(
                new LambdaQueryWrapper<PostTag>().eq(PostTag::getTagId, id)
        );
        if (postCount > 0) {
            String errorMessage = String.format("无法删除标签 '%s'，有 %d 篇文章正在使用它。", tag.getName(), postCount);
            throw new ServiceException(ResultCode.FORBIDDEN, errorMessage);
        }
        // 删除标签
        removeById(id);
        return null;
    }

    /**
     * 获取后台管理系统的标签分页数据。
     * 该方法用于查询包含标签及其关联文章数量的分页结果，返回的数据用于后台管理系统展示。
     *
     * @param request 标签查询请求，包含分页参数（页码和每页数量）。
     * @return 包含标签及其关联文章数量信息的分页结果。
     */
    @Override
    public PageResult<TagPostCountResponse> getAdminTagPage(TagQueryRequest request) {
        // 创建分页请求
        Page<TagPostCountResponse> pageRequest = new Page<>(request.getPageNum(), request.getPageSize());

        // 调用为后台管理接口定制的 Mapper 方法
        Page<TagPostCountResponse> tagPage = baseMapper.selectAdminTagPage(pageRequest);

        // 封装返回结果
        return PageResult.<TagPostCountResponse>builder()
                .records(tagPage.getRecords())
                .total(tagPage.getTotal())
                .size(tagPage.getSize())
                .current(tagPage.getCurrent())
                .build();
    }

    /**
     * 获取所有公开标签的列表，包括标签及其关联文章的数量。
     * 该方法常用于前端展示公开标签信息的场景。
     *
     * @return 包含标签及其文章数量信息的列表
     */
    @Override
    public List<TagPostCountResponse> getPublicTagList() {
        return baseMapper.selectPublicTagList();
    }


    /**
     * 根据文章 ID 查询关联的标签列表。
     * 此方法封装了对数据库的查询操作，用于获取指定文章所关联的所有标签。
     *
     * @param postId 文章的唯一标识，用于查找其关联的标签。
     * @return 包含标签信息的列表，每个元素表示一个标签。
     */
    @Override
    public List<TagResponse> findTagsByPostId(Long postId) {
        return baseMapper.findTagsByPostId(postId);
    }

    /**
     * 根据文章 ID 列表查询关联的标签信息。
     * 此方法封装了对数据库的查询操作，用于批量获取指定文章所关联的所有标签。
     *
     * @param postIds 文章 ID 的列表，用于批量查找每篇文章的关联标签。
     * @return 包含帖子 ID 和其对应标签信息的列表，每个元素表示一篇文章的标签详情。
     */
    @Override
    public List<TagWhitPostIdDTO> findTagsByPostIds(List<Long> postIds) {
        return baseMapper.findTagsByPostIds(postIds);
    }
}
