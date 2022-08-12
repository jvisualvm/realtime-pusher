package com.risen.realtime.framework.controller;

import com.risen.common.response.Result;
import com.risen.common.response.ResultProxy;
import com.risen.realtime.framework.dto.InsertSourceDTO;
import com.risen.realtime.framework.dto.UserInfoDTO;
import com.risen.realtime.framework.entity.PushSinkEntity;
import com.risen.realtime.framework.entity.PushTaskEntity;
import com.risen.realtime.framework.service.TaskManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/12 12:59
 */
@RestController
@RequestMapping(value = "/manager")
@AllArgsConstructor
@Api(tags = "任务管理接口")
public class TaskManagerController {

    private TaskManagerService taskManagerService;

    @GetMapping(value = "/task/list")
    @ApiOperation(tags = "任务查询", value = "任务查询")
    public Result listTask() {
        return ResultProxy.build().successWithBody(taskManagerService.listTask());
    }


    @PutMapping(value = "/task/update")
    @ApiOperation(tags = "更新任务", value = "更新任务")
    public Result updateTask(@RequestBody PushTaskEntity task) {
        taskManagerService.updateTask(task);
        return ResultProxy.build().successIgnoreBody();
    }

    @PutMapping(value = "/task/delete/{key}")
    @ApiOperation(tags = "删除任务", value = "删除任务")
    public Result deleteTask(@PathVariable(value = "key") String taskKey) {
        taskManagerService.deleteTask(taskKey);
        return ResultProxy.build().successIgnoreBody();
    }

    @PutMapping(value = "/datasource/delete/{id}")
    @ApiOperation(tags = "删除数据源", value = "删除数据源")
    public Result deleteDatasource(@PathVariable(value = "id") Integer id) {
        return taskManagerService.deleteDatasource(id);
    }

    @GetMapping(value = "/sink/list")
    @ApiOperation(tags = "数据源配置查询", value = "数据源配置查询")
    public Result listDataSource() {
        return ResultProxy.build().successWithBody(taskManagerService.listDataSource());
    }

    @PutMapping(value = "/sink/update")
    @ApiOperation(tags = "数据源配置更新", value = "数据源配置更新")
    public Result updateSink(@RequestBody PushSinkEntity pushSink) {
        taskManagerService.updateSink(pushSink);
        return ResultProxy.build().successIgnoreBody();
    }

    @PutMapping(value = "/sink/inser")
    @ApiOperation(tags = "数据源配置新增", value = "数据源配置新增")
    public Result insertSink(@Validated @RequestBody InsertSourceDTO pushSink) {
        return taskManagerService.insertSink(pushSink);
    }


    @PostMapping(value = "/login")
    @ApiOperation(tags = "用户登录接口", value = "用户登录接口")
    public Result login(@Validated @RequestBody UserInfoDTO userInfoDTO) {
        return taskManagerService.getUser(userInfoDTO);
    }


    @GetMapping(value = "/service/status")
    @ApiOperation(tags = "服务状态查询", value = "服务状态查询")
    public Result getServiceStatus() {
        return ResultProxy.build().successWithBody(taskManagerService.getServiceStatus());
    }



    @PostMapping("/upload/directory")
    @ApiOperation(tags = "文件上传接口", value = "文件上传接口")
    public Result uploadImage(@RequestPart(required = true) MultipartFile multipartFile, HttpServletRequest request) {
        return ResultProxy.build().successWithBody(request.getSession().getServletContext().getRealPath("/") + multipartFile.getOriginalFilename());
    }

}
