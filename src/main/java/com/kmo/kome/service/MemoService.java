package com.kmo.kome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmo.kome.common.PageResult;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.request.MemoCreateRequest;
import com.kmo.kome.dto.request.MemoQueryRequest;
import com.kmo.kome.dto.request.MemoUpdateRequest;
import com.kmo.kome.dto.response.MemoResponse;
import com.kmo.kome.dto.response.MemoStatsResponse;
import com.kmo.kome.entity.Memo;
import jakarta.validation.Valid;

import java.util.List;

/**
 *  Memo 业务接口
 * <p>
 * 继承自 IService<Memo>，提供与 Memo 相关的基础 CRUD 操作能力。
 * 主要用于处理 Memo 的数据管理和相关业务逻辑。
 */
public interface MemoService extends IService<Memo> {

    /**
     * 获取管理员 Memo 分页列表。
     * 支持按关键字和状态筛选，包含置顶排序逻辑。
     *
     * @param request 包含分页参数和筛选条件的请求对象。
     * @return 包含 Memo 信息的分页结果。
     */
    PageResult<MemoResponse> getAdminMemoPage(@Valid MemoQueryRequest request);

    /**
     * 获取公开 Memo 分页列表。
     * 仅返回已发布的 Memo，按置顶优先排序。
     *
     * @param request 包含分页参数和筛选条件的请求对象。
     * @return 包含 Memo 信息的分页结果。
     */
    PageResult<MemoResponse> getPublicMemoPage(@Valid MemoQueryRequest request);

    /**
     * 创建一条新的 Memo 记录。
     *
     * @param request 包含 Memo 内容、置顶状态和发布状态的请求对象。
     * @return 新创建的 Memo ID。
     */
    Long createMemo(@Valid MemoCreateRequest request);

    /**
     * 根据 ID 更新指定的 Memo 信息。
     *
     * @param id Memo 的唯一标识符。
     * @param request 包含更新内容的请求对象。
     * @return 空值，表示更新操作已完成。
     * @throws ServiceException 如果 Memo 不存在。
     */
    Void updateMemoById(Long id, @Valid MemoUpdateRequest request);

    /**
     * 根据 ID 删除指定的 Memo 记录。
     *
     * @param id Memo 的唯一标识符。
     * @return 空值，表示删除操作已完成。
     * @throws ServiceException 如果 Memo 不存在。
     */
    Void deleteMemoById(Long id);

    /**
     * 获取最新的已发布 Memo 列表，按创建时间倒序排列。
     *
     * @param limit 最大返回数量，范围 1-4，默认值为 2。
     * @return 包含最新 Memo 的列表。
     */
    List<MemoResponse> getLatestMemo(Integer limit);

    /**
     * 查询 Memo 的统计信息，包括总记录数、总字数、当月新增数和最新创建时间。
     *
     * @return 包含统计数据的响应对象。
     */
    MemoStatsResponse getMemoStats();
}
