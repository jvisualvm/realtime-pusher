package com.risen.realtime.framework.service;

import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/3/14 19:35
 */
@Service
public class ManagerService {


    public Map listMemoryStatus() {
        Map<String, Map<String, Object>> memoryStatusMap = new LinkedHashMap<>();

        OperatingSystemMXBean osb = ManagementFactory.getOperatingSystemMXBean();
        Map<String, Object> systemStatusMap = new LinkedHashMap<>();
        systemStatusMap.put("操作系统名称", osb.getName());
        systemStatusMap.put("系统版本", osb.getArch());
        systemStatusMap.put("核心数量", osb.getAvailableProcessors());
        systemStatusMap.put("操作系统版本", osb.getVersion());
        systemStatusMap.put("系统负载", osb.getSystemLoadAverage());
        memoryStatusMap.put("系统信息", systemStatusMap);

        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> jvmStatusMap = new LinkedHashMap<>();
        long max = runtime.maxMemory();
        long total = runtime.totalMemory();
        long free = runtime.freeMemory();
        long usable = max - total + free;
        jvmStatusMap.put("已分配内存", total / 1024 / 1024 + "M");
        jvmStatusMap.put("最大内存", max / 1024 / 1024 + "M");
        jvmStatusMap.put("已分配内存中的剩余空间", free / 1024 / 1024 + "M");
        jvmStatusMap.put("最大可用内存", usable / 1024 / 1024 + "M");
        jvmStatusMap.put("JVM进程数量", runtime.availableProcessors());
        memoryStatusMap.put("JVM使用情况", jvmStatusMap);


        return memoryStatusMap;
    }


}
