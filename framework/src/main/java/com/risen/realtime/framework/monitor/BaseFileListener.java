package com.risen.realtime.framework.monitor;

import com.risen.common.consumer.ConditionConsumer;
import com.risen.common.util.LogUtil;
import com.risen.realtime.framework.dto.InsertCountDTO;
import com.risen.realtime.framework.log.LogSender;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.springframework.util.StopWatch;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/27 8:54
 */
public abstract class BaseFileListener extends FileAlterationListenerAdaptor {

    private static ConcurrentHashMap<String, BaseFileListener> implTree = new ConcurrentHashMap<>();

    public BaseFileListener() {
        implTree.put(taskKey(), this);
    }

    public abstract String taskKey();

    public static BaseFileListener getImplTree(String taskKey) {
        return implTree.get(taskKey);
    }

    public static List<BaseFileListener> getImplTree() {
        return implTree.values().stream().collect(Collectors.toList());
    }


    public void executeAndPrint(File file, InsertCountDTO insertCountDTO, ConditionConsumer consumer) {
        StopWatch sw = new StopWatch();
        sw.start(file.getName() + "发生变化,系统开始读取文件增量数据并入库");
        LogSender.printInfo(file.getName() + "发生变化,系统开始读取文件增量数据并入库");

        consumer.accept();

        sw.stop();
        sw.prettyPrint();
        LogUtil.info(sw.prettyPrint() + ",本轮入库数量：" + insertCountDTO.getCount());
        LogSender.printInfo(sw.prettyPrint() + ",本轮入库数量：" + insertCountDTO.getCount());
    }


}
