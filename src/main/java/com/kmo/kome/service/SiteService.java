package com.kmo.kome.service;

import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.request.SetupRequest;
import com.kmo.kome.dto.response.SiteInfoResponse;

/**
 * 站点业务接口
 * <p>
 * 用于定义与站点相关的业务操作接口。
 * 通常包括站点配置、统计信息等功能。
 */
public interface SiteService {

    /**
     * 获取站点的公开信息。
     * 返回站点所有者信息和公开统计数据（不包含草稿相关数据）。
     *
     * @return 包含站点公开信息的响应对象。
     */
    SiteInfoResponse getPublicSiteInfo();

    /**
     * 获取站点管理员的完整信息和统计数据。
     * 聚合站点所有者信息、文章/标签/说说/友链的详细统计。
     *
     * @return 包含站点管理员信息和完整统计数据的响应对象。
     * @throws ServiceException 如果站点数据缺失（无法获取管理者信息）。
     */
    SiteInfoResponse getAdminSiteInfo();

    /**
     * 检查系统是否已初始化（是否存在管理员账户）。
     *
     * @return true 表示已初始化，false 表示未初始化。
     */
    boolean isInitialized();

    /**
     * 首次设置管理员账户。
     * 仅当系统未初始化时可用。
     *
     * @param request 设置请求，包含用户名、密码、昵称、头像、简介和邮箱。
     * @throws ServiceException 如果系统已初始化。
     */
    void setupAdmin(SetupRequest request);
}
