package com.risen.realtime.framework.service;

import com.risen.common.cosntant.Symbol;
import com.risen.common.util.LogUtil;
import com.risen.common.util.PredicateUtil;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.cosntant.DataModelEnum;
import com.risen.realtime.framework.entity.PushSinkEntity;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/29 20:32
 */
public class DataTransferService {

    public static String parseDataByMode(String taskKey, String dataRow) {
        PushTaskCache pushTaskCache = SystemCacheService.getSystemObj(PushTaskCache.class);
        PushSinkEntity pushSinkEntity = pushTaskCache.getTaskSink(taskKey);
        List<Integer> discardList = new ArrayList<>();
        if (PredicateUtil.isNotEmpty(pushSinkEntity.getDiscardRule())) {
            List<Integer> discardId = Arrays.asList(pushSinkEntity.getDiscardRule().split(Symbol.SYMBOL_COMMA)).stream().map(s -> Integer.valueOf(s)).collect(Collectors.toList());
            discardList.addAll(discardId);
        }
        if (DataModelEnum.STR_MODE.getCode().equals(pushSinkEntity.getDataMode())) {
            return parseStrByMode(discardList, dataRow);
        }
        if (DataModelEnum.MAP_MODEL.getCode().equals(pushSinkEntity.getDataMode())) {
            return parseNumberByMode(pushSinkEntity, dataRow, discardList);
        }
        if (DataModelEnum.FILE_MODEL.getCode().equals(pushSinkEntity.getDataMode())) {
            return dataRow;
        } else {
            LogUtil.error("没有配置转换模式，无法进行数据处理，此条数据丢弃：" + dataRow);
            return null;
        }

    }

    public static String parseNumberByMode(PushSinkEntity pushSinkEntity, String dataRow, List<Integer> discardList) {
        StringBuilder builder = new StringBuilder();
        String dataList = pushSinkEntity.getDataMap();
        if (StringUtils.isEmpty(dataList)) {
            LogUtil.error("没有配置转换模式，将全部存储为字符串，可能会导致该批数据入库失败！请确认");
            return parseStrByMode(discardList, dataRow);
        }
        List<Integer> mapList = Arrays.asList(dataList.split(Symbol.SYMBOL_COMMA)).stream().map(s -> Integer.valueOf(s)).collect(Collectors.toList());
        String[] detail = dataRow.split(Symbol.SYMBOL_COMMA);
        int i = 0;
        int emptyCount = 0;
        for (String s : detail) {
            i++;
            if (mapList.contains(i)) {
                if (Symbol.STR_NULL.equals(s) || s == null) {
                    builder.append(s).append(Symbol.SYMBOL_COMMA);
                    if (!discardList.contains(i)) {
                        emptyCount++;
                    }
                    continue;
                }
                if (ObjectUtils.isEmpty(s)) {
                    if (!discardList.contains(i)) {
                        emptyCount++;
                    }
                }
                if (PredicateUtil.isNotEmpty(s) && !StringUtils.isNumeric(s)) {
                    LogUtil.error("字段配置的转化为数字类型，但是实际值不是数字,此条数据丢弃：" + dataRow);
                    return null;
                } else {
                    builder.append(s).append(Symbol.SYMBOL_COMMA);
                }
            } else {
                builder.append(Symbol.QUOTA).append(s).append(Symbol.QUOTA);
                builder.append(Symbol.SYMBOL_COMMA);
            }
        }
        if (emptyCount >= 1) {
            LogUtil.info(buildEmptyErrorMessage(emptyCount, dataRow));
            return null;
        }
        return builder.deleteCharAt(builder.length() - 1).toString();
    }

    public static String parseStrByMode(List<Integer> discardList, String dataRow) {
        StringBuilder builder = new StringBuilder();
        String[] detail = dataRow.split(Symbol.SYMBOL_COMMA);
        int emptyCount = 0;
        int index = 0;
        for (String s : detail) {
            index++;
            if (Symbol.STR_NULL.equals(s) || s == null) {
                if (!discardList.contains(index)) {
                    emptyCount++;
                }
                builder.append(s);
            } else {
                builder.append(Symbol.QUOTA).append(s).append(Symbol.QUOTA);
            }
            builder.append(Symbol.SYMBOL_COMMA);
        }

        if (emptyCount >= 1) {
            LogUtil.info(buildEmptyErrorMessage(emptyCount, dataRow));
            return null;
        }
        return builder.deleteCharAt(builder.length() - 1).toString();
    }


    private static String buildEmptyErrorMessage(int emptyCount, String dataRow) {
        StringBuilder errorBuilder = new StringBuilder();
        errorBuilder.append("【数据不全】丢弃数据，目前为空个数是：");
        errorBuilder.append(emptyCount);
        errorBuilder.append("，源数据：");
        errorBuilder.append(dataRow);
        return errorBuilder.toString();
    }


}
