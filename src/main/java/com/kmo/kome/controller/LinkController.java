package com.kmo.kome.controller;

import com.kmo.kome.common.Result;
import com.kmo.kome.dto.request.LinkQueryRequest;
import com.kmo.kome.dto.response.LinkResponse;
import com.kmo.kome.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    /**
     * 获取友情链接列表。
     * 接收查询请求对象，根据筛选条件查询友情链接列表，并返回封装的结果数据。
     *
     * @param request 友情链接查询请求参数，包含筛选关键词。
     * @return 包含友情链接信息的结果对象列表。
     */
    @GetMapping("/api/links")
    public Result<List<LinkResponse>> getLinkList(LinkQueryRequest request){
        return Result.success(linkService.getLinkList(request));
    }
}
