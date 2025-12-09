package com.kmo.kome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmo.kome.common.PageResult;
import com.kmo.kome.dto.request.MemoCreateRequest;
import com.kmo.kome.dto.request.MemoQueryRequest;
import com.kmo.kome.dto.request.MemoUpdateRequest;
import com.kmo.kome.dto.response.MemoResponse;
import com.kmo.kome.entity.Memo;
import jakarta.validation.Valid;

/**
 * 备忘录业务接口
 * <p>
 * 继承自 IService<Memo>，提供与备忘录相关的基础 CRUD 操作能力。
 * 主要用于处理备忘录的数据管理和相关业务逻辑。
 */
public interface MemoService extends IService<Memo> {
    PageResult<MemoResponse> getAdminMemoPage(@Valid MemoQueryRequest request);

    PageResult<MemoResponse> getPublicMemoPage(@Valid MemoQueryRequest request);

    Long createMemo(@Valid MemoCreateRequest request);

    Void updateMemoById(Long id, @Valid MemoUpdateRequest request);

    Void deleteMemoById(Long id);
}
