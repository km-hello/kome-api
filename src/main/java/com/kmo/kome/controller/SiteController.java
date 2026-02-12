package com.kmo.kome.controller;

import com.kmo.kome.common.Result;
import com.kmo.kome.dto.request.SetupRequest;
import com.kmo.kome.dto.response.SiteInfoResponse;
import com.kmo.kome.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SiteController {

    private final SiteService siteService;

    /**
     * 检查系统是否已初始化（公开接口）
     * 用于首次访问时判断是否需要跳转到设置页面
     *
     * @return true 表示已初始化，false 表示需要设置
     */
    @GetMapping("/api/site/initialized")
    public Result<Boolean> checkInitialized() {
        return Result.success(siteService.isInitialized());
    }

    /**
     * 首次设置管理员（公开接口，仅当未初始化时可用）
     *
     * @param request 设置请求，包含用户名、密码和可选的邮箱
     * @return 成功结果
     */
    @PostMapping("/api/site/setup")
    public Result<Void> setupAdmin(@Valid @RequestBody SetupRequest request) {
        siteService.setupAdmin(request);
        return Result.success();
    }

    /**
     * 获取公共可见的站点信息。
     * 调用服务层方法，查询站点的基本信息，包括所有者信息和统计数据，并返回给前端。
     *
     * @return 包含站点信息的结果对象，包括所有者的昵称、头像、描述以及站点的统计数据。
     */
    @GetMapping("/api/site/info")
    public Result<SiteInfoResponse> getPublicSiteInfo(){
        return Result.success(siteService.getPublicSiteInfo());
    }

    /**
     * 获取管理员权限下的站点信息。
     * 调用服务层方法，查询站点的详细信息，包括所有者信息和统计数据，并返回结果。
     *
     * @return 包含站点信息的结果对象，包括所有者的昵称、头像、描述以及站点的统计数据。
     */
    @GetMapping("/api/admin/site/info")
    public Result<SiteInfoResponse> getAdminSiteInfo(){
        return Result.success(siteService.getAdminSiteInfo());
    }

}
