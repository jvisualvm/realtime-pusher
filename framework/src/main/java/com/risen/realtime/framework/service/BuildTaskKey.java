package com.risen.realtime.framework.service;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/4 11:30
 */
public class BuildTaskKey {


    public static String createLocationKey(String taskKey, String filePath) {
        return String.format("%s:%s", taskKey, filePath);
    }


}
