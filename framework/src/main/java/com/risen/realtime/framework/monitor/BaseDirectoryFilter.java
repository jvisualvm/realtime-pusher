package com.risen.realtime.framework.monitor;

import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/27 10:04
 */
public abstract class BaseDirectoryFilter extends DirectoryFileFilter {

    private static ConcurrentHashMap<String, BaseDirectoryFilter> implTree = new ConcurrentHashMap<>();

    public BaseDirectoryFilter() {
        implTree.put(taskKey(), this);
    }

    public abstract String taskKey();


    public static BaseDirectoryFilter getImplTree(String taskKey) {
        return implTree.get(taskKey);
    }

    public static List<BaseDirectoryFilter> getImplTree() {
        return implTree.values().stream().collect(Collectors.toList());
    }

}
