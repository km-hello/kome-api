package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmo.kome.common.PageResult;
import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.request.MemoCreateRequest;
import com.kmo.kome.dto.request.MemoQueryRequest;
import com.kmo.kome.dto.request.MemoUpdateRequest;
import com.kmo.kome.dto.response.MemoResponse;
import com.kmo.kome.entity.Memo;
import com.kmo.kome.mapper.MemoMapper;
import com.kmo.kome.service.MemoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 备忘录服务实现类
 * <p>
 * 继承自 ServiceImpl<MemoMapper, Memo>，实现了 MemoService 接口。
 * 提供了与备忘录相关的业务逻辑和数据操作，并通过继承父类实现了基础的 CRUD 功能。
 */
@Service
public class MemoServiceImpl extends ServiceImpl<MemoMapper, Memo> implements MemoService {

    /**
     * 创建一条新的备忘录记录。
     * 根据传入的创建请求对象，将其属性值复制到备忘录实体中并保存到数据库。
     *
     * @param request 包含备忘录相关创建数据的请求对象，包括内容、是否置顶以及状态。
     * @return 返回值固定为 null。
     */
    @Override
    public Long createMemo(MemoCreateRequest request) {
        Memo memo = new Memo();
        BeanUtils.copyProperties(request, memo);
        save(memo);
        return memo.getId();
    }

    /**
     * 根据指定的备忘录 ID 更新备忘录信息。
     * 首先根据 ID 查询对应的备忘录记录，如记录不存在，则抛出业务异常；
     * 然后将请求对象中的更新信息复制到目标备忘录对象，并通过 ID 执行更新操作。
     *
     * @param id 备忘录的唯一标识符，用于指定待更新的记录
     * @param request 包含更新内容的请求对象，包含备忘录的内容、是否置顶以及状态字段
     * @return 更新操作的返回值，固定为 null
     * @throws ServiceException 当指定的备忘录记录不存在时，抛出此异常
     */
    @Override
    public Void updateMemoById(Long id, MemoUpdateRequest request) {
        Memo memo = getById(id);
        if(memo == null){
            throw new ServiceException(ResultCode.NOT_FOUND);
        }
        Memo updateMemo = new Memo();
        BeanUtils.copyProperties(request, updateMemo);
        updateMemo.setId(id);
        updateById(updateMemo);
        return null;
    }

    /**
     * 根据指定的备忘录 ID 删除备忘录记录。
     * 首先根据 ID 查询对应的备忘录记录，如记录不存在时将抛出业务异常；
     * 若记录存在，则执行删除操作。
     *
     * @param id 备忘录的唯一标识符，用于指定待删除的记录
     * @return 返回值固定为 null
     * @throws ServiceException 当指定的备忘录记录不存在时抛出此异常
     */
    @Override
    public Void deleteMemoById(Long id) {
        Memo memo = getById(id);
        if(memo == null){
            throw new ServiceException(ResultCode.NOT_FOUND);
        }
        removeById(id);
        return null;
    }

    /**
     * 获取最新的备忘录列表。
     * 根据指定的限制数量，查询并返回已发布的最新备忘录记录，按创建时间倒序排列。
     * 如果参数为空或超过限制范围，将使用默认值。
     *
     * @param limit 最大返回记录的数量，允许值范围为 1 至 4，如果为空或无效，默认值为 2
     * @return 包含最新备忘录的 MemoResponse 对象列表
     */
    @Override
    public List<MemoResponse> getLatestMemo(Integer limit) {
        // 设置默认限制数量
        final int DEFAULT_LIMIT = 2;
        if(limit == null || limit <= 0 || limit > 4){
            limit = DEFAULT_LIMIT;
        }

        // 查询列表，只查询已发布，按创建时间倒叙，限制数量
        List<Memo> memos = list(new LambdaQueryWrapper<Memo>()
                .eq(Memo::getStatus, 1)
                .orderByDesc(Memo::getCreateTime)
                .last("LIMIT " + limit)
        );

        // 转换并返回数据
        return memos.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * 获取公共备忘录分页列表。
     * 根据传入的查询请求，设置状态为公开状态（1），并调用后台管理查询方法获取分页结果。
     *
     * @param request 包含分页查询条件的请求对象，须包含页码和每页数量，可选关键词。
     * @return 包含查询结果的分页对象，记录列表为 MemoResponse 类型。
     */
    @Override
    public PageResult<MemoResponse> getPublicMemoPage(MemoQueryRequest request) {
        // 仅查询已发布的
        request.setStatus(1);
        return getAdminMemoPage(request);
    }

    /**
     * 获取备忘录分页列表，提供用于后台管理的分页查询功能。
     * 根据请求参数，包括关键词、状态、页码和每页数量，动态构建查询条件，
     * 并返回包含分页数据的结果对象。
     *
     * @param request 包含分页查询参数的对象，必须提供页码和每页数量，可选提供关键词和状态进行筛选
     * @return 包含查询结果的分页对象，记录列表为 MemoResponse 类型
     */
    @Override
    public PageResult<MemoResponse> getAdminMemoPage(MemoQueryRequest request) {
        // 构建分页对象
        Page<Memo> page = new Page<>(request.getPageNum(), request.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<Memo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(request.getKeyword()), Memo::getContent, request.getKeyword()) // 关键字搜索 (content LIKE '%keyword%')
                .eq(request.getStatus() != null, Memo::getStatus, request.getStatus()) // 状态筛选 (如果前端传了 status 就查特定的，没传就查所有)
                .orderByDesc(Memo::getIsPinned) // 排序: 置顶优先 -> 创建时间倒序
                .orderByDesc(Memo::getCreateTime);

        // 查询数据库
        Page<Memo> memoPage = page(page, wrapper);

        // 转换记录列表 (List<Memo> -> List<MemoResponse>)
        List<MemoResponse> responseList = memoPage.getRecords().stream()
                .map(this::toResponse)
                .toList();

        // 构建返回
        return PageResult.<MemoResponse>builder()
                .records(responseList)
                .total(memoPage.getTotal())
                .size(memoPage.getSize())
                .current(memoPage.getCurrent())
                .build();
    }

    /**
     * 将 Memo 实例转换为 MemoResponse 实例。
     * 通过 BeanUtils 工具类复制 Memo 对象的属性值到 MemoResponse 中。
     *
     * @param memo 待转换的 Memo 实例
     * @return 转换后的 MemoResponse 实例
     */
    private MemoResponse toResponse(Memo memo) {
        MemoResponse response = new MemoResponse();
        BeanUtils.copyProperties(memo, response);
        return response;
    }

}
