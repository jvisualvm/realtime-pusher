package com.risen.realtime.framework.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.risen.common.response.Result;
import com.risen.common.response.ResultProxy;
import com.risen.common.util.LogUtil;
import com.risen.common.util.PredicateUtil;
import com.risen.common.util.ThreadLocalUtil;
import com.risen.quartz.manager.QuartzManager;
import com.risen.realtime.framework.cache.PushSinkCache;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.cache.SystemObjectCache;
import com.risen.realtime.framework.cosntant.JobTypeEnum;
import com.risen.realtime.framework.cosntant.LoginConstant;
import com.risen.realtime.framework.cosntant.ServerConstant;
import com.risen.realtime.framework.cosntant.UserTypeEnum;
import com.risen.realtime.framework.dto.InsertSourceDTO;
import com.risen.realtime.framework.dto.UserInfoDTO;
import com.risen.realtime.framework.entity.PushSinkEntity;
import com.risen.realtime.framework.entity.PushTaskEntity;
import com.risen.realtime.framework.entity.PushUserEntity;
import com.risen.realtime.framework.mapper.PushSinkMapper;
import com.risen.realtime.framework.mapper.PushTaskMapper;
import com.risen.realtime.framework.mapper.PushUserMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/20 20:49
 */
@Service
@AllArgsConstructor
public class TaskManagerService {

    private PushTaskMapper taskMapper;
    private PushTaskCache pushTaskCache;
    private PushSinkCache pushSinkCache;
    private PushSinkMapper sinkMapper;
    private PushUserMapper pushUserMapper;
    private QuartzManager quartzManager;
    private SystemObjectCache systemObjectCache;

    private static int serviceStatus = 0;

    public Object listTask() {
        LambdaQueryWrapper<PushTaskEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (!UserTypeEnum.ADMIN.getCode().equals(LoginConstant.CURRENT_LOGIN_ROLE)) {
            queryWrapper.ne(true, PushTaskEntity::getTaskType, JobTypeEnum.SYSTEM.getType());
        }
        List<PushTaskEntity> pushTask = taskMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(pushTask)) {
            return new ArrayList<>();
        }
        pushTask.forEach(item -> {
            PushSinkEntity pushSink = sinkMapper.selectById(item.getTargetId());
            Optional.ofNullable(pushSink).ifPresent(s -> {
                item.setDataSourceName(s.getDataSourceName());
            });
        });
        return pushTask;
    }

    @Transactional
    public void updateTask(PushTaskEntity task) {
        PushTaskEntity pushTask = taskMapper.selectById(task.getTaskKey());
        taskMapper.updateById(task);
        pushTaskCache.loadCache();
        if (ObjectUtils.isEmpty(task.getTaskKey())) {
            LogUtil.info("该任务没有配置任务标识符，请联系管理员");
            return;
        }
        if (!task.getEnable() && quartzManager.taskIsRunning(task.getTaskKey())) {
            quartzManager.pauseTask(task.getTaskKey());
            return;
        }
        if (task.getEnable() && !quartzManager.taskIsRunning(task.getTaskKey())) {
            quartzManager.resumeTask(task.getTaskKey());
        }

        ThreadLocalUtil.inLocal.set(systemObjectCache);
        TaskDataService.updateMonitor(task, pushTask);

    }


    @Transactional
    public void deleteTask(String taskKey) {
        LambdaQueryWrapper<PushTaskEntity> query = new LambdaQueryWrapper();
        query.eq(StringUtils.isNotEmpty(taskKey), PushTaskEntity::getTaskKey, taskKey);
        taskMapper.delete(query);
        quartzManager.removeJob(taskKey);
        pushTaskCache.loadCache();
    }

    @Transactional
    public Result deleteDatasource(Integer id) {
        LambdaQueryWrapper<PushTaskEntity> queryTask = new LambdaQueryWrapper<>();
        queryTask.eq(!ObjectUtils.isEmpty(id), PushTaskEntity::getTargetId, id);
        long total = taskMapper.selectCount(queryTask);
        if (total > 0) {
            ResultProxy.build().failWithMsg("当前数据源已经绑定了任务无法删除");
        }
        LambdaQueryWrapper<PushSinkEntity> query = new LambdaQueryWrapper();
        query.eq(!ObjectUtils.isEmpty(id), PushSinkEntity::getId, id);
        sinkMapper.delete(query);
        pushSinkCache.loadCache();
        return ResultProxy.build().successIgnoreBody();
    }


    public Object listDataSource() {
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<PushSinkEntity> taskList = sinkMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(taskList)) {
            return new ArrayList<>();
        }
        return taskList;
    }

    @Transactional
    public void updateSink(PushSinkEntity pushSink) {
        sinkMapper.updateById(pushSink);
        pushSinkCache.loadCache();
        //如果数据源禁用了，需要暂停对应的所有任务
        Map<String, PushTaskEntity> taskEntityMap = pushTaskCache.getAllCacheValue();
        if (!CollectionUtils.isEmpty(taskEntityMap)) {
            taskEntityMap.forEach((k, v) -> {
                if (isNeedStopTask(v, pushSink)) {
                    quartzManager.pauseTask(k);
                }
                if (isNeedResumeTask(v, pushSink)) {
                    quartzManager.resumeTask(k);
                }
            });
        }
    }

    @Transactional
    public Result insertSink(InsertSourceDTO pushSink) {
        LambdaQueryWrapper<PushSinkEntity> query = new LambdaQueryWrapper<>();
        query.eq(PredicateUtil.isNotEmpty(pushSink.getUrl()), PushSinkEntity::getUrl, pushSink.getUrl());
        Long count = sinkMapper.selectCount(query);
        if (count > 0) {
            return ResultProxy.build().failWithMsg("该数据源已经存在，无法新增");
        } else {
            PushSinkEntity push = new PushSinkEntity();
            BeanUtils.copyProperties(pushSink, push);
            sinkMapper.insert(push);
        }
        pushSinkCache.loadCache();
        return ResultProxy.build().successIgnoreBody();
    }


    private boolean isNeedStopTask(PushTaskEntity v, PushSinkEntity pushSink) {
        return !pushSink.getEnable()
                && !ObjectUtils.isEmpty(v)
                && pushSink.getId().equals(v.getTargetId())
                && quartzManager.taskIsRunning(v.getTaskKey());
    }

    private boolean isNeedResumeTask(PushTaskEntity v, PushSinkEntity pushSink) {
        return pushSink.getEnable()
                && !ObjectUtils.isEmpty(v)
                && pushSink.getId().equals(v.getTargetId())
                && !quartzManager.taskIsRunning(v.getTaskKey());
    }

    public Result getUser(UserInfoDTO userInfoDTO) {
        LambdaQueryWrapper<PushUserEntity> query = new LambdaQueryWrapper<>();
        query.eq(PredicateUtil.isNotEmpty(userInfoDTO.getUserName()), PushUserEntity::getUserName, userInfoDTO.getUserName());
        query.eq(PredicateUtil.isNotEmpty(userInfoDTO.getPassword()), PushUserEntity::getPassword, userInfoDTO.getPassword());
        PushUserEntity pushUserEntity = pushUserMapper.selectOne(query);
        if (ObjectUtils.isEmpty(pushUserEntity)) {
            return ResultProxy.build().failWithMsg("该用户不存在，或账号密码错误");
        }

        LoginConstant.CURRENT_LOGIN_USER = pushUserEntity.getUserName();
        LoginConstant.CURRENT_LOGIN_PASSWORD = pushUserEntity.getPassword();
        LoginConstant.CURRENT_LOGIN_ROLE = pushUserEntity.getType();

        return ResultProxy.build().successWithBody(pushUserEntity.getType());
    }


    public int getServiceStatus() {
        if (ServerConstant.IsStopping) {
            return -1;
        }
        if (ServerConstant.IsRunning) {
            return 100;
        }
        return serviceStatus;
    }


}
