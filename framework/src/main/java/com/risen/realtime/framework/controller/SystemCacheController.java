package com.risen.realtime.framework.controller;

import com.risen.common.response.Result;
import com.risen.common.response.ResultProxy;
import com.risen.realtime.framework.service.SystemCacheRefreshService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/12 12:59
 */
@RestController
@RequestMapping(value = "/refresh/cache")
@AllArgsConstructor
@Api(tags = "系统缓存刷新接口")
public class SystemCacheController {

    private SystemCacheRefreshService service;

    @PutMapping(value = "/url")
    @ApiOperation(tags = "刷新数据写入地址缓存", value = "刷新数据写入地址缓存")
    public Result refreshConfigCache() {
        service.refreshSinkCache();
        return ResultProxy.build().successIgnoreBody();
    }

    @PutMapping(value = "/task")
    @ApiOperation(tags = "刷新任务缓存", value = "刷新任务缓存")
    public Result refreshTaskCache() {
        service.refreshTaskCache();
        return ResultProxy.build().successIgnoreBody();
    }

    @PutMapping(value = "/location")
    @ApiOperation(tags = "刷新任务位置缓存", value = "刷新任务位置缓存")
    public Result refreshLocationCache() {
        service.refreshLocationCache();
        return ResultProxy.build().successIgnoreBody();
    }

    @PutMapping(value = "/user")
    @ApiOperation(tags = "刷新用户缓存", value = "刷新用户缓存")
    public Result refreshUserCache() {
        service.refreshUserCache();
        return ResultProxy.build().successIgnoreBody();
    }


}
