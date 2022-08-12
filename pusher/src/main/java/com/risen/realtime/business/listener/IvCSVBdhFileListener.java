package com.risen.realtime.business.listener;

import com.risen.common.consumer.ConditionConsumer;
import com.risen.common.cosntant.Constant;
import com.risen.common.cosntant.Symbol;
import com.risen.common.util.*;
import com.risen.realtime.business.job.IvCSVBdhDataPusher;
import com.risen.realtime.business.service.ReadExcelSpecialService;
import com.risen.realtime.framework.cache.PushSourceCache;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.datasource.DataInsertUtil;
import com.risen.realtime.framework.dto.ExcelFileDetailDTO;
import com.risen.realtime.framework.dto.ExcelFileItemDTO;
import com.risen.realtime.framework.dto.InsertCountDTO;
import com.risen.realtime.framework.entity.PushSinkEntity;
import com.risen.realtime.framework.entity.PushSourceEntity;
import com.risen.realtime.framework.log.LogSender;
import com.risen.realtime.framework.monitor.BaseFileListener;
import com.risen.realtime.framework.service.BuildTaskKey;
import com.risen.realtime.framework.service.SystemCacheService;
import com.risen.realtime.framework.service.TaskLocationService;
import com.risen.realtime.framework.service.TaskValidService;
import com.risen.realtime.framework.util.ReadExcelUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/27 8:52
 */
@Component
public class IvCSVBdhFileListener extends BaseFileListener {

    @Override
    public void onFileChange(File file) {
        if (TaskValidService.taskConfigValid(taskKey())||!file.getName().contains("背钝化")) {
            return;
        }

        String error = "没有配置需要读取的excel是第几个sheet,需要配置成数字";
        PushSourceCache pushSourceCache = SystemCacheService.getSystemObj(PushSourceCache.class);
        PushSourceEntity source = pushSourceCache.get(taskKey());

        if (ObjectUtils.isEmpty(source) || ObjectUtils.isEmpty(source.getDataTable()) || !StringUtils.isNumeric(source.getDataTable())) {
            LogUtil.info(error);
            LogSender.printError(error);
            return;
        }

        String startLog = "开始采集EXCEL文件";
        LogUtil.info(startLog);
        LogSender.printError(startLog);

        InsertCountDTO insertCountDTO = new InsertCountDTO();
        ConditionConsumer consumer = () -> {
            onFileChangeAction(file, insertCountDTO);
        };
        executeAndPrint(file, insertCountDTO, consumer);
    }

    private void onFileChangeAction(File file, InsertCountDTO insertCount) {
        try {
            String locationKey = BuildTaskKey.createLocationKey(taskKey(), file.getAbsolutePath());
            PushTaskCache pushTaskCache = SystemCacheService.getSystemObj(PushTaskCache.class);
            PushSinkEntity pushSinkEntity = pushTaskCache.getTaskSink(taskKey());
            PushSourceCache pushSourceCache = SystemCacheService.getSystemObj(PushSourceCache.class);
            PushSourceEntity source = pushSourceCache.get(taskKey());
            int lastRow = TaskLocationService.getCurrentLocation(locationKey);
            int readStartPoint = 13;
            if (PredicateUtil.isNotEmpty(pushSinkEntity) && PredicateUtil.isNotEmpty(pushSinkEntity.getStartIndex())) {
                readStartPoint = pushSinkEntity.getStartIndex();
            }
            if (lastRow > readStartPoint) {
                readStartPoint = lastRow;
            }
            ExcelFileDetailDTO excelFileDetailDTO = ReadExcelUtil.readExcel(file.getAbsolutePath(), Integer.valueOf(source.getDataTable()), readStartPoint, pushSinkEntity.getColumnsCount());
            String ip = ServiceUtil.getServiceIpByPrefix(Constant.FILTER_IP);
            Optional.ofNullable(excelFileDetailDTO).ifPresent(item -> {
                InsertCountDTO index = new InsertCountDTO();
                Consumer<List<ExcelFileItemDTO>> consumer = s -> {
                    List<String> needInsertList = ReadExcelSpecialService.dataBdhTransfer(s, insertCount, index);
                    Consumer<List<String>> excelConsumer = f -> {
                        List<String> finalInsertList = new ArrayList<>();
                        f.forEach(u -> {
                            StringBuilder builder = new StringBuilder();
                            builder.append(Symbol.QUOTA);
                            builder.append(file.getName());
                            builder.append(Symbol.QUOTA);
                            builder.append(Symbol.SYMBOL_COMMA);
                            builder.append(Symbol.QUOTA);
                            builder.append(ip);
                            builder.append(Symbol.QUOTA);
                            builder.append(Symbol.SYMBOL_COMMA);
                            builder.append(Symbol.QUOTA);
                            builder.append(DateUtil.parseDate(file.lastModified(), DateUtil.YYYY_MM_DD_HH_MM_SS));
                            builder.append(Symbol.QUOTA);
                            builder.append(Symbol.SYMBOL_COMMA);
                            builder.append(u);
                            finalInsertList.add(builder.toString());
                        });
                        DataInsertUtil.dataInsert(taskKey(), finalInsertList, null);
                        LogUtil.info("更新位置为：{}", index.getCount());
                        TaskLocationService.updateLocation(locationKey, index.getCount());
                    };
                    ForeachUtil.forEach(needInsertList, excelConsumer);
                };
                ForeachUtil.forEach(item.getDataList(), consumer);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String taskKey() {
        return IvCSVBdhDataPusher.TASK_KEY;
    }


}
