package com.kmo.kome.controller;

import com.kmo.kome.common.Result;
import com.kmo.kome.dto.response.SiteInfoResponse;
import com.kmo.kome.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SiteController {

    private final SiteService siteService;

    /**
     * 获取站点信息的接口。
     * 调用服务层方法获取站点的基本信息，并封装为统一的结果对象返回。
     *
     * @return 包含站点信息的结果对象，数据类型为 {@code SiteInfoResponse}。
     */
    @GetMapping("/api/site/info")
    public Result<SiteInfoResponse> getSiteInfo(){
        return Result.success(siteService.getSiteInfo());
    }

}
