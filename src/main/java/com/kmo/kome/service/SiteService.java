package com.kmo.kome.service;

import com.kmo.kome.dto.request.SetupRequest;
import com.kmo.kome.dto.response.SiteInfoResponse;

/**
 * 站点业务接口
 * <p>
 * 用于定义与站点相关的业务操作接口。
 * 通常包括站点配置、统计信息等功能。
 */
public interface SiteService {
    SiteInfoResponse getPublicSiteInfo();

    SiteInfoResponse getAdminSiteInfo();

    /**
     * 检查系统是否已初始化（是否存在管理员账户）
     *
     * @return true 表示已初始化，false 表示未初始化
     */
    boolean isInitialized();

    /**
     * 首次设置管理员账户
     * 仅当系统未初始化时可用
     *
     * @param request 设置请求，包含用户名、密码和可选的邮箱
     */
    void setupAdmin(SetupRequest request);
}
