package com.kmo.kome.service;

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
}
