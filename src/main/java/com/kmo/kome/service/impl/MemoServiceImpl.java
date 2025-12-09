package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmo.kome.common.PageResult;
import com.kmo.kome.dto.request.MemoQueryRequest;
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
