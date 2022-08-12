package com.risen.realtime.business.listener;

import com.risen.common.consumer.ConditionConsumer;
import com.risen.common.cosntant.Constant;
import com.risen.common.cosntant.Symbol;
import com.risen.common.util.*;
import com.risen.realtime.business.job.IvCSVDataPusher;
import com.risen.realtime.business.service.ReadSpecialService;
import com.risen.realtime.framework.cache.PushTaskCache;
import com.risen.realtime.framework.datasource.DataInsertUtil;
import com.risen.realtime.framework.dto.CSVFileDetailDTO;
import com.risen.realtime.framework.dto.InsertCountDTO;
import com.risen.realtime.framework.entity.PushSinkEntity;
import com.risen.realtime.framework.log.LogSender;
import com.risen.realtime.framework.monitor.BaseFileListener;
import com.risen.realtime.framework.service.*;
import com.risen.realtime.framework.util.ReadCsvUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/27 8:52
 */
@Component
public class IvCSVFileListener extends BaseFileListener {

    private static final String END_FLAG = "总计";

    @Override
    public void onFileChange(File file) {
        if (TaskValidService.taskConfigValid(taskKey())) {
            return;
        }
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
            String endFlag = PredicateUtil.isNotEmpty(pushSinkEntity.getEndFlag()) ? pushSinkEntity.getEndFlag() : END_FLAG;

            int readStartPoint = 2;
            if (PredicateUtil.isNotEmpty(pushSinkEntity) && PredicateUtil.isNotEmpty(pushSinkEntity.getStartIndex())) {
                readStartPoint = pushSinkEntity.getStartIndex();
            }
            CSVFileDetailDTO csvFileDetailDTO = ReadCsvUtil.readCsvFile(file, readStartPoint);
            InsertCountDTO locCount = new InsertCountDTO();
            Consumer<List<String>> consumer = s -> {
                int size = 0;
                try {
                    List<String> needList = ReadSpecialService.filterList(endFlag, csvFileDetailDTO.getDataList());
                    List<String> finalNeedDataList = new ArrayList<>();
                    size = needList.size();
                    if (size <= 1) {
                        return;
                    }

                    if (size % 2 == 0) {
                        locCount.setCount(size);
                        int lastLocation = TaskLocationService.getCurrentLocation(locationKey);
                        finalNeedDataList.addAll(s.subList(lastLocation, size));
                    } else {
                        locCount.setCount(size - 1);
                        int lastLocation = TaskLocationService.getCurrentLocation(locationKey);
                        finalNeedDataList.addAll(s.subList(lastLocation, size - 1));
                    }
                    if (!CollectionUtils.isEmpty(finalNeedDataList) && finalNeedDataList.size() % 2 == 0) {
                        insertCount.setCount(buildSqlAndSend2Queue(buildListAfterSend2Queue(locationKey, endFlag, finalNeedDataList), file));
                    }
                    //立刻更新位置信息
                    TaskLocationService.updateLocation(locationKey, locCount.getCount());
                    LogUtil.info("数据入库结束，开始更新位置信息为" + locCount.getCount());
                    LogSender.printInfo("数据入库结束，开始更新位置信息为" + locCount.getCount());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            ForeachUtil.forEach(csvFileDetailDTO.getDataList(), consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> buildListAfterSend2Queue(String locationKey, String endFlag, List<String> dataList) {
        AtomicBoolean canRun = new AtomicBoolean(true);
        List<String> needAppendList = new ArrayList<>();
        InsertCountDTO index = new InsertCountDTO();
        index.setCount(1);
        dataList.forEach(s -> {
            if (s.contains(endFlag)) {
                canRun.set(false);
                TaskLocationService.removeLocation(locationKey);
                return;
            }
            if (canRun.get()) {
                String finalDataRow = null;
                if (index.getCount() % 2 == 0) {
                    finalDataRow = ReadSpecialService.deleteNotNeedChar(s, true, true);
                } else {
                    finalDataRow = ReadSpecialService.deleteNotNeedChar(s, false, true);
                }
                needAppendList.add(finalDataRow);
            }
            index.setCount(index.getCount() + 1);
        });
        return needAppendList;
    }

    private Integer buildSqlAndSend2Queue(List<String> needAppendList, File file) {
        String ip = ServiceUtil.getServiceIpByPrefix(Constant.FILTER_IP);
        List<String> sqlDataList = new ArrayList<>();
        for (int i = 0, m = needAppendList.size(); i < m; i++) {
            if ((i + 1) % 2 != 0) {
                StringBuilder builder = new StringBuilder();
                builder.append(DateUtil.parseDate(file.lastModified(), DateUtil.YYYY_MM_DD_HH_MM_SS));
                builder.append(Symbol.SYMBOL_COMMA);
                builder.append(ip);
                builder.append(Symbol.SYMBOL_COMMA);
                builder.append(file.getName());
                builder.append(Symbol.SYMBOL_COMMA);
                builder.append(needAppendList.get(i));
                builder.append(Symbol.SYMBOL_COMMA);
                //需要对后续数据进行转换
                builder.append(needAppendList.get(i + 1));

                String transfer = DataTransferService.parseDataByMode(taskKey(), builder.toString());
                if (PredicateUtil.isNotEmpty(transfer)) {
                    sqlDataList.add(transfer);
                }
            }
        }
        if (PredicateUtil.isNotEmpty(sqlDataList)) {
            //按照模式对字符串进行处理
            DataInsertUtil.dataInsert(taskKey(), sqlDataList, null);
        }
        return sqlDataList.size();
    }


    @Override
    public String taskKey() {
        return IvCSVDataPusher.TASK_KEY;
    }

}
