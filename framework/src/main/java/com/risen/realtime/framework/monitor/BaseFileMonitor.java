package com.risen.realtime.framework.monitor;

import com.alibaba.fastjson.JSON;
import com.risen.common.util.LogUtil;
import com.risen.realtime.framework.dto.MonitorRunDetailDTO;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/26 17:18
 */
public class BaseFileMonitor {

    private static ConcurrentHashMap<String, MonitorRunDetailDTO> taskMonitor = new ConcurrentHashMap();

    public static ConcurrentHashMap<String, FileAlterationObserver> observerMap = new ConcurrentHashMap();

    private BaseFileMonitor() {

    }

    public static void monitor(String taskKey, String path, String fileSuffix, FileAlterationListener listener, DirectoryFileFilter fileFilter, long interval) {
        try {
            FileAlterationMonitor monitor = null;
            if (taskMonitor.containsKey(taskKey)) {
                monitor = taskMonitor.get(taskKey).getMonitor();
            } else {
                monitor = new FileAlterationMonitor(interval);
                MonitorRunDetailDTO monitorRunDetail = new MonitorRunDetailDTO();
                monitorRunDetail.setMonitor(monitor);
                monitorRunDetail.setRunning(false);
                taskMonitor.put(taskKey, monitorRunDetail);
            }

            if (!taskMonitor.get(taskKey).isRunning()) {
                IOFileFilter files = FileFilterUtils.or(fileFilter, FileFilterUtils.suffixFileFilter(fileSuffix));
                FileAlterationObserver observer = new FileAlterationObserver(new File(path), files);

                observerMap.put(taskKey, observer);

                monitor.addObserver(observer);
                observer.addListener(listener);
                MonitorRunDetailDTO runDetail = taskMonitor.get(taskKey);
                LogUtil.info("开启任务：{},目录：{}监控", taskKey, path);
                monitor.start();
                runDetail.setRunning(true);
                taskMonitor.put(taskKey, runDetail);
            }
            LogUtil.info("taskMonitor:{}", JSON.toJSONString(taskMonitor));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(String taskKey) {
        try {
            LogUtil.info("停止目录监控");
            taskMonitor.get(taskKey).getMonitor().removeObserver(observerMap.get(taskKey));
            taskMonitor.get(taskKey).getMonitor().stop();
            taskMonitor.remove(taskKey);
            observerMap.remove(taskKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean contain(String taskKey) {
        boolean isExists = false;
        try {
            isExists = taskMonitor.containsKey(taskKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExists;
    }


}
