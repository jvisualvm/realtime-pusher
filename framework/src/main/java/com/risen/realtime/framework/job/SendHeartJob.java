package com.risen.realtime.framework.job;

import com.risen.common.cosntant.Constant;
import com.risen.common.util.ServiceUtil;
import com.risen.realtime.framework.cosntant.JobTypeEnum;
import com.risen.realtime.framework.datasource.DataInsertUtil;
import com.risen.realtime.framework.manager.DataJobManager;
import com.risen.realtime.framework.service.DataTransferService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/27 11:21
 */
@Component
@DisallowConcurrentExecution
public class SendHeartJob extends DataJobManager {


    @Override
    public String taskKey() {
        return "risen:data:heart:time";
    }

    @Override
    public JobTypeEnum taskType() {
        return JobTypeEnum.SYSTEM;
    }

    @Override
    public boolean runWithServiceStart() {
        return true;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        StringBuilder deleteBuilder = new StringBuilder();
        deleteBuilder.append(" and ip= ");
        deleteBuilder.append(DataTransferService.parseDataByMode(taskKey(), ServiceUtil.getServiceIpByPrefix(Constant.FILTER_IP)));

        DataInsertUtil.dataDelete(taskKey(), deleteBuilder.toString());


        List<String> resultList = new ArrayList<>();
        StringBuilder insertBuilder = new StringBuilder();

        insertBuilder.append(DataTransferService.parseDataByMode(taskKey(), ServiceUtil.getServiceIpByPrefix(Constant.FILTER_IP)));

        resultList.add(insertBuilder.toString());

        DataInsertUtil.dataInsert(taskKey(), resultList, null);


    }
}
