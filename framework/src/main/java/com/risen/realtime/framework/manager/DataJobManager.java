package com.risen.realtime.framework.manager;

import com.risen.realtime.framework.cosntant.JobTypeEnum;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.UnableToInterruptJobException;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 11:13
 */
public abstract class DataJobManager implements InterruptableJob {

    protected boolean stopTask = false;

    private static ConcurrentHashMap<String, DataJobManager> implTree = new ConcurrentHashMap<>();

    public DataJobManager() {
        implTree.put(taskKey(), this);
    }

    public abstract String taskKey();


    public abstract JobTypeEnum taskType();

    public abstract boolean runWithServiceStart();

    @Override
    public abstract void execute(JobExecutionContext jobExecutionContext);


    public static DataJobManager getImplTree(String taskKey) {
        return implTree.get(taskKey);
    }

    public static List<DataJobManager> getImplTree() {
        return implTree.values().stream().collect(Collectors.toList());
    }


    @Override
    public void interrupt() throws UnableToInterruptJobException {
        stopTask = true;
    }

}
