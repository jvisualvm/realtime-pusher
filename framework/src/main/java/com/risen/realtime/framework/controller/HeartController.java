package com.risen.realtime.framework.controller;

import com.risen.common.response.Result;
import com.risen.common.response.ResultProxy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/12 12:59
 */
@RestController
@RequestMapping(value = "/heart")
@AllArgsConstructor
@Api(tags = "心跳接口")
public class HeartController {

    @GetMapping(value = "/info")
    @ApiOperation(tags = "心跳检测", value = "心跳检测")
    public Result heart() {
        return ResultProxy.build().successIgnoreBody();
    }

}
