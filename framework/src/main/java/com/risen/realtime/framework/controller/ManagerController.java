package com.risen.realtime.framework.controller;

import com.risen.common.response.Result;
import com.risen.common.response.ResultProxy;
import com.risen.common.thread.ThreadPoolView;
import com.risen.realtime.framework.service.ManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("manager")
@AllArgsConstructor
@Api(tags = "系统管理接口")
public class ManagerController {

    private ManagerService managerService;

    @GetMapping("/thread/view")
    @ApiOperation("查看线程池执行情况")
    public Result uploadImage() {
        return ResultProxy.build().successWithBody(ThreadPoolView.getThreadPoolRunStatusView());
    }

    @GetMapping("/list/system/detail")
    @ApiOperation("查看linux系统信息和jvm使用情况")
    public Result getSystemAndJvmDetail() {
        return ResultProxy.build().successWithBody(managerService.listMemoryStatus());
    }


}
