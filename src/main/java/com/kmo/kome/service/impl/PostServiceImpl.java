package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmo.kome.common.PageResult;
import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.TagWhitPostIdDTO;
import com.kmo.kome.dto.request.PostArchiveQueryRequest;
import com.kmo.kome.dto.request.PostCreateRequest;
import com.kmo.kome.dto.request.PostQueryRequest;
import com.kmo.kome.dto.request.PostUpdateRequest;
import com.kmo.kome.dto.response.PostArchiveResponse;
import com.kmo.kome.dto.response.PostDetailResponse;
import com.kmo.kome.dto.response.PostNavResponse;
import com.kmo.kome.dto.response.PostSimpleResponse;
import com.kmo.kome.dto.response.TagResponse;
import com.kmo.kome.entity.Post;
import com.kmo.kome.entity.PostTag;
import com.kmo.kome.entity.Tag;
import com.kmo.kome.mapper.PostMapper;
import com.kmo.kome.service.PostService;
import com.kmo.kome.service.PostTagService;
import com.kmo.kome.service.TagService;
import com.kmo.kome.utils.PostUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文章服务实现类。
 * <p>
 * 实现文章的创建、更新、删除、详情查询、分页列表及归档等核心业务逻辑，
 * 包含 Slug 唯一性校验、标签关联管理及浏览量原子递增等功能。
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    private final PostTagService postTagService;
    private final TagService tagService;
    private final PostUtils postUtils;

    /**
     * 创建新文章。
     * 该方法用于保存一篇新文章，同时进行必要的校验工作，包括检查文章别名的唯一性、
     * 验证关联标签的合法性，并处理文章与标签的关联关系。
     *
     * @param request 创建文章的请求对象，包含文章的标题、内容、摘要、状态、标签等必要的信息。
     *                不能为 null，并且必须提供有效的标题、内容和标签信息。
     * @return 新创建的文章的唯一标识符（主键 ID）。
     * @throws ServiceException 如果文章别名重复或关联的标签无效，则抛出包含对应错误信息的业务异常。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPost(PostCreateRequest request) {
        // 检查别名是否被占用
        checkSlugUniqueness(request.getSlug(), null);

        // 校验标签合法性 (Fail-Fast: 如果标签不存在，直接报错，不进行后续 Insert)
        validateTagIds(request.getTagIds());

        // 保存文章
        Post newPost = new Post();
        BeanUtils.copyProperties(request, newPost);
        // 计算阅读时间
        newPost.setReadTime(postUtils.calculateReadTime(request.getContent()));
        save(newPost);

        // 处理关联标签 (统一使用 resetPostTags 处理关联)
        resetPostTags(newPost.getId(), request.getTagIds());

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
    @Transactional(rollbackFor = Exception.class)
    public Void deletePostById(Long id) {
        // 检查 post 是否存在
        Post post = getById(id);
        if(post == null){
            throw new ServiceException(ResultCode.NOT_FOUND, "文章不存在");
        }

        // 删除 post_tag 关联表中的相关记录
        postTagService.remove(
                new LambdaQueryWrapper<PostTag>()
                        .eq(PostTag::getPostId, id)
        );

        // 为了释放唯一索引，修改slug
        String newSlug = post.getSlug() + "_del_" + System.currentTimeMillis();
        post.setSlug(newSlug);

        // 更新 slug
        updateById(post);

        // 执行逻辑删除
        removeById(id);
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
    @Transactional(rollbackFor = Exception.class)
    public Void updatePostById(Long id, PostUpdateRequest request) {
        // 检查文章是否存在
        Post oldPost = getById(id);
        if(oldPost == null){
            throw new ServiceException(ResultCode.NOT_FOUND, "文章不存在");
        }

        // 检查别名是否被占用
        checkSlugUniqueness(request.getSlug(), id);

        // 检查标签合法性
        validateTagIds(request.getTagIds());

        // 更新文章主表
        Post newPost = new Post();
        BeanUtils.copyProperties(request, newPost);
        newPost.setId(id);
        // 更新阅读时间
        newPost.setReadTime(postUtils.calculateReadTime(request.getContent()));
        updateById(newPost);

        // 更新关联标签 (统一使用 resetPostTags 处理关联)
        resetPostTags(id, request.getTagIds());

        return null;
    }

    /**
     * 根据文章 ID 获取文章详情。
     * 如果指定的文章不存在，则抛出业务异常。
     *
     * @param id 文章的唯一标识符，不允许为空。
     * @return 包含文章详细信息的响应对象。
     * @throws ServiceException 如果文章不存在，则抛出包含 404 状态的业务异常。
     */
    @Override
    public PostDetailResponse getPostById(Long id) {
        // 根据 id 查询文章
        Post post = getById(id);
        if(post == null){
            throw new ServiceException(ResultCode.NOT_FOUND, "文章不存在");
        }

        // 后台接口不增加阅读量

        // 组装并返回 DTO
        return buildPostDetailResponse(post);
    }

    /**
     * 根据文章别名（Slug）获取文章详情。
     * 如果指定的文章不存在或未发布，则抛出业务异常。
     *
     * @param slug 文章的别名，用于唯一定位文章记录，不允许为空。
     * @return 包含文章详细信息的响应对象，包含文章主表的所有字段。
     * @throws ServiceException 如果文章不存在或未发布，则抛出包含 404 状态的业务异常。
     */
    @Override
    public PostDetailResponse getPostBySlug(String slug) {
        // 根据 slug 查询文章
        Post post = lambdaQuery()
                .eq(Post::getSlug, slug)
                .one();
        // 检查文章是否存在并且已发布
        if(post == null || post.getStatus() == 0){
            throw new ServiceException(ResultCode.NOT_FOUND, "文章不存在或未发布");
        }

        // 增加阅读量逻辑（原子递增，避免并发问题）
        // 使用 update_time = update_time 防止 ON UPDATE CURRENT_TIMESTAMP 触发
        // 这样只更新 views，不更新 update_time
        update(Wrappers.<Post>lambdaUpdate()
                .eq(Post::getId, post.getId())
                .setSql("views = views + 1, update_time = update_time")
        );

        return buildPostDetailResponse(post);
    }

    /**
     * 查询公开文章分页数据。
     * 该方法用于获取已发布的文章列表，并按照置顶优先的规则进行排序。
     * 仅返回符合筛选条件的文章概要信息。
     *
     * @param request 文章查询请求对象，用于定义分页参数（页码、每页数量）以及筛选条件（关键词、标签）。
     *                方法内部会将状态强制设置为已发布（status=1），并重置为按照置顶优先排序。
     * @return 包含文章概要信息的分页结果对象。结果包括总记录数、分页信息以及文章数据列表。
     */
    @Override
    public PageResult<PostSimpleResponse> getPublicPostPage(PostQueryRequest request) {
        // 仅允许查询已发布的文章
        request.setStatus(1);
        // 公开接口始终按置顶优先排序
        request.setIgnorePinned(false);
        return getAdminPostPage(request);
    }

    /**
     * 获取文章归档信息。
     * 该方法按年份和月份对已发布的文章进行归档，返回按时间分组的文章数据。
     *
     * @param request 文章归档查询请求对象，用于定义筛选条件，包括关键字、标签等过滤条件。
     * @return 包含按年份和月份分组的文章归档列表。在没有符合条件的文章时，返回空列表。
     */
    @Override
    public List<PostArchiveResponse> getArchivePosts(PostArchiveQueryRequest request) {
        // 内部构造分页查询请求，复用 getAdminPostPage
        PostQueryRequest query = new PostQueryRequest();
        query.setPageNum(1);
        query.setPageSize(-1); // 设置不分页
        query.setKeyword(request.getKeyword());
        query.setTagId(request.getTagId());
        query.setStatus(1); // 只查询已发布文章

        // 获取原始数据, 并按照创建时间倒叙排序
        List<PostSimpleResponse> rawPosts = getAdminPostPage(query).getRecords().stream()
                .sorted(Comparator.comparing(PostSimpleResponse::getCreateTime).reversed())
                .toList();
        // 空数据直接返回空列表
        if(CollectionUtils.isEmpty(rawPosts)){
            return Collections.emptyList();
        }

        return rawPosts.stream()
                .collect(Collectors.groupingBy(
                        post -> post.getCreateTime().getYear(), // 提取年份
                        LinkedHashMap::new, // 保序Map：年份降序插入
                        Collectors.groupingBy(
                                post -> post.getCreateTime().getMonthValue(), // 提取月份
                                LinkedHashMap::new, // 保序Map：月份降序插入
                                Collectors.mapping(this::convertToArchiveSimplePost, Collectors.toList()) // 大对象转换成小对象
                        )
                )).entrySet().stream()
                .map(yearEntry -> {
                    Integer year = yearEntry.getKey();
                    Map<Integer, List<PostArchiveResponse.ArchiveSimplePost>> monthMap = yearEntry.getValue();

                    // 构建月份
                    List<PostArchiveResponse.MonthGroup> months = monthMap.entrySet().stream()
                            .map(monthEntry -> PostArchiveResponse.MonthGroup.builder()
                                    .month(monthEntry.getKey())
                                    .posts(monthEntry.getValue())
                                    .total(monthEntry.getValue().size())
                                    .build())
                            .toList();
                    // 计算年 total
                    int yearTotal = months.stream().mapToInt(PostArchiveResponse.MonthGroup::getTotal).sum();
                    // 构建年份
                    return PostArchiveResponse.builder()
                            .year(year)
                            .months(months)
                            .total(yearTotal)
                            .build();
                } )
                .toList();
    }

    /**
     * 将文章概要响应对象转换为文章归档的简单文章对象。
     * 该方法用于构建归档数据时，将文章的基本信息封装为归档所需格式。
     *
     * @param origin 文章概要响应对象，包含文章的 ID、标题、别名（Slug）、标签和创建时间等基本信息。
     *               不能为 null，且需确保其字段值已被正确初始化。
     * @return 归档用的简单文章对象，包含文章的 ID、标题、别名（Slug）、标签和创建时间等信息。
     */
    private PostArchiveResponse.ArchiveSimplePost convertToArchiveSimplePost(PostSimpleResponse origin){
        return PostArchiveResponse.ArchiveSimplePost.builder()
                .id(origin.getId())
                .title(origin.getTitle())
                .slug(origin.getSlug())
                .tags(origin.getTags())
                .createTime(origin.getCreateTime())
                .build();
    }

    /**
     * 获取后台管理文章分页列表。
     * 根据提供的查询条件和分页参数，查询符合条件的文章主列表及其关联的标签信息，
     * 并返回封装完成的分页结果。
     *
     * @param request 查询请求对象，包括分页参数（页码和每页数量）、关键词过滤、标签筛选以及状态筛选等字段。
     * @return 包含文章概要信息和分页数据的结果对象。
     */
    @Override
    public PageResult<PostSimpleResponse> getAdminPostPage(PostQueryRequest request) {
        // 分页查询文章主列表
        Page<PostSimpleResponse> pageRequest = new Page<>(request.getPageNum(), request.getPageSize());
        // 如果是 archive 接口调用，怎不分页也不查询 count
        if(pageRequest.getSize() == -1){
            pageRequest.setSearchCount(false);
        }
        Page<PostSimpleResponse> postPage = baseMapper.selectPostPage(pageRequest, request);

        List<PostSimpleResponse> posts = postPage.getRecords();
        if(CollectionUtils.isEmpty(posts)){
            return PageResult.<PostSimpleResponse>builder()
                    .records(Collections.emptyList())
                    .total(0L)
                    .size(postPage.getSize())
                    .current(postPage.getCurrent())
                    .build();
        }

        // 批量获取关联的标签
        List<Long> postIds = posts.stream()
                .map(PostSimpleResponse::getId)
                .toList();
        List<TagWhitPostIdDTO> tagLinks = tagService.findTagsByPostIds(postIds);

        // 组装数据
        Map<Long, List<TagResponse>> postTagsMap = tagLinks.stream()
                .collect(Collectors.groupingBy(
                        TagWhitPostIdDTO::getPostId,
                        Collectors.mapping(
                                tagLink -> {
                                    TagResponse tagResponse = new TagResponse();
                                    tagResponse.setId(tagLink.getTagId());
                                    tagResponse.setName(tagLink.getTagName());
                                    return tagResponse;
                                },
                                Collectors.toList()
                        )
                ));
        posts.forEach( post ->
                post.setTags(postTagsMap.getOrDefault(post.getId(), Collections.emptyList())));

        // 封装并返回数据
        return PageResult.<PostSimpleResponse>builder()
                .records(posts)
                .total(postPage.getTotal())
                .size(postPage.getSize())
                .current(postPage.getCurrent())
                .build();
    }



    /**
     * 构建文章详情响应对象。
     * 根据给定的文章实体对象，复制其基本属性，查询并设置相关联的标签信息，
     * 最终返回封装完成的文章详情响应对象。
     *
     * @param post 文章实体对象，用于提供文章的基本信息和唯一标识符。
     * @return 包含文章详细信息和关联标签列表的响应对象。
     */
    private PostDetailResponse buildPostDetailResponse(Post post){
        // 复制文章基本属性
        PostDetailResponse response = new PostDetailResponse();
        BeanUtils.copyProperties(post, response);

        // 查询并设置标签列表
        List<TagResponse> tags = tagService.findTagsByPostId(post.getId());
        response.setTags(tags);

        // 查询并设置上一篇/下一篇导航（仅对已发布文章）
        if (post.getStatus() != null && post.getStatus() == 1) {
            response.setPrevious(findAdjacentPost(post.getCreateTime(), true));
            response.setNext(findAdjacentPost(post.getCreateTime(), false));
        }

        return response;
    }

    /**
     * 查询相邻的已发布文章。
     *
     * @param createTime 当前文章的创建时间
     * @param isPrevious true 查询上一篇（更早的），false 查询下一篇（更晚的）
     * @return 相邻文章的导航信息，如果不存在则返回 null
     */
    private PostNavResponse findAdjacentPost(LocalDateTime createTime, boolean isPrevious) {
        Post post = lambdaQuery()
                .select(Post::getId, Post::getTitle, Post::getSlug)
                .eq(Post::getStatus, 1)
                .apply(isPrevious
                        ? "create_time < {0}"
                        : "create_time > {0}", createTime)
                .orderBy(true, !isPrevious, Post::getCreateTime)
                .last("LIMIT 1")
                .one();

        if (post == null) {
            return null;
        }
        return PostNavResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .slug(post.getSlug())
                .build();
    }


    /**
     * 检查文章的别名（Slug）是否唯一。
     * 如果指定的别名已存在，且不属于当前文章（通过文章 ID 排除），
     * 则抛出业务异常。
     *
     * @param slug 待检查的文章别名，不允许为空。
     * @param postId 当前文章的唯一标识符，用于排除自身的别名。如果为空，则不排除任何记录。
     * @throws ServiceException 如果别名已存在，则抛出包含 400 状态的业务异常。
     */
    private void checkSlugUniqueness(String slug, Long postId) {
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<Post>()
                .eq(Post::getSlug, slug);

        if(postId != null){
            queryWrapper.ne(Post::getId, postId);
        }

        boolean isSlugTaken = exists(queryWrapper);

        if(slug != null && isSlugTaken){
            throw new ServiceException(ResultCode.BAD_REQUEST, "文章别名已被占用");
        }
    }

    /**
     * 验证标签 ID 列表的有效性。
     * 该方法用于检查传入的标签 ID 列表是否存在空值或无效数据。
     * 首先对标签 ID 进行去重操作，防止前端传入重复值导致逻辑错误；
     * 然后查询数据库确认所有标签 ID 是否有效；
     * 最后如果检测到无效的标签 ID，则抛出业务异常。
     *
     * @param tagIds 待验证的标签 ID 列表，不能为 null，且必须包含有效的标签 ID。
     *               如果列表为空或存在不存在的标签 ID，会抛出对应的业务异常。
     * @throws ServiceException 如果标签列表为空，或包含不存在的标签 ID，则抛出对应的异常。
     */
    private void validateTagIds(List<Long> tagIds){
        if(CollectionUtils.isEmpty(tagIds)){
            return;
        }
        // 1. 去重：防止前端传[1, 1]导致数量对比出错
        List<Long> distinctTagIds = tagIds.stream().distinct().toList();

        // 2. 批量查询数据库中存在的数量
        long count = tagService.count(
                new LambdaQueryWrapper<Tag>().in(Tag::getId, distinctTagIds)
        );

        // 3. 对比：如果查出来的数量 ！= 请求的数量，说明有 ID 不存在
        if(count != distinctTagIds.size()){
            throw new ServiceException(ResultCode.BAD_REQUEST, "包含不存在的标签");
        }
    }


    /**
     * 重置指定文章的标签关联关系。
     * 该方法会先清除文章的所有旧标签关联，然后根据提供的新标签 ID 列表添加新的关联记录。
     * 如果新标签列表为空，则表示清空所有关联的标签。
     *
     * @param postId 文章的唯一标识符，用于定位需要重置标签关联的目标文章。
     * @param tagIds 新的标签 ID 列表，如果为空则清空所有关联标签。
     */
    private void resetPostTags(Long postId, List<Long> tagIds) {
        // 1. 无论原来有没有，先删除旧关联 (对于新建文章，这一步删了个寂寞，但不影响逻辑)
        postTagService.remove(
                new LambdaQueryWrapper<PostTag>().eq(PostTag::getPostId, postId)
        );

        // 2. 如果新标签列表为空，说明是清空标签，直接返回
        if(CollectionUtils.isEmpty(tagIds)){
            return;
        }

        // 3. 插入新关联
        // 去重，防止数据库唯一索引冲突
        List<Long> uniqueTagIds = tagIds.stream().distinct().toList();
        List<PostTag> postTagList = uniqueTagIds.stream().map(tagId -> {
            PostTag postTag = new PostTag();
            postTag.setPostId(postId);
            postTag.setTagId(tagId);
            return postTag;
        }).toList();
        // 批量插入
        postTagService.saveBatch(postTagList);
    }
}
