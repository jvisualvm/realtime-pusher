package com.risen.realtime.business.listener;

import com.risen.common.consumer.ConditionConsumer;
import com.risen.common.cosntant.Constant;
import com.risen.common.cosntant.Symbol;
import com.risen.common.util.DateUtil;
import com.risen.common.util.LogUtil;
import com.risen.common.util.PredicateUtil;
import com.risen.common.util.ServiceUtil;
import com.risen.realtime.business.job.IvMDBDataPusher;
import com.risen.realtime.framework.cache.PushSourceCache;
import com.risen.realtime.framework.datasource.DataInsertUtil;
import com.risen.realtime.framework.dto.InsertCountDTO;
import com.risen.realtime.framework.dto.MDBFileDTO;
import com.risen.realtime.framework.entity.PushSourceEntity;
import com.risen.realtime.framework.log.LogSender;
import com.risen.realtime.framework.monitor.BaseFileListener;
import com.risen.realtime.framework.service.*;
import com.risen.realtime.framework.util.ReadMdbUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/29 14:03
 */
@Component
public class IvMDBFileListener extends BaseFileListener {

    @Override
    public void onFileChange(File file) {
        String error = "该任务没有配置.mdb文件的表名称，无法进行读取";
        PushSourceCache pushSourceCache = SystemCacheService.getSystemObj(PushSourceCache.class);
        PushSourceEntity source = pushSourceCache.get(taskKey());
        if (ObjectUtils.isEmpty(source) || ObjectUtils.isEmpty(source.getDataTable())) {
            LogUtil.error(error);
            LogSender.printInfo(error);
            return;
        }
        if (TaskValidService.taskConfigValid(taskKey())) {
            return;
        }

        InsertCountDTO insertCount = new InsertCountDTO();
        ConditionConsumer consumer = () -> {
            onFileChangeAction(file, insertCount);
        };
        executeAndPrint(file, insertCount, consumer);

    }


    private void onFileChangeAction(File file, InsertCountDTO insertCount) {
        String locationKey = BuildTaskKey.createLocationKey(taskKey(), file.getAbsolutePath());
        try {
            PushSourceCache pushSourceCache = SystemCacheService.getSystemObj(PushSourceCache.class);
            PushSourceEntity source = pushSourceCache.get(taskKey());
            int maxKeyInTable = ReadMdbUtil.queryMaxKey(source.getUserName(), source.getPassword(), source.getDataTable(), file);
            int lastLocation = TaskLocationService.getCurrentLocation(locationKey);
            if (maxKeyInTable <= lastLocation) {
                return;
            }
            try {
                for (int i = lastLocation; i < maxKeyInTable; ) {
                    MDBFileDTO mdbFileDTO = ReadMdbUtil.readMDBFile(source.getUserName(), source.getPassword(), source.getDataTable(), file, i, maxKeyInTable);
                    if (CollectionUtils.isEmpty(mdbFileDTO.getDataList())) {
                        break;
                    }
                    List<String> dataList = mdbFileDTO.getDataList();
                    List<String> sqlDataList = new ArrayList<>();

                    createSqlList(file, dataList, sqlDataList);

                    if (PredicateUtil.isNotEmpty(sqlDataList)) {
                        DataInsertUtil.dataInsert(taskKey(), sqlDataList, mdbFileDTO.getFieldList());
                    }
                    insertCount.setCount(insertCount.getCount() + sqlDataList.size());
                    i = mdbFileDTO.getMaxKey();
                }

                //立刻更新位置信息
                TaskLocationService.updateLocation(locationKey, maxKeyInTable);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void createSqlList(File file, List<String> dataList, List<String> sqlDataList) {
        String ip = ServiceUtil.getServiceIpByPrefix(Constant.FILTER_IP);
        for (int m = 0; m < dataList.size(); m++) {
            StringBuilder builder = new StringBuilder();

            builder.append(file.getName());
            builder.append(Symbol.SYMBOL_COMMA);
            builder.append(ip);
            builder.append(Symbol.SYMBOL_COMMA);
            builder.append(DateUtil.parseDate(file.lastModified(), DateUtil.YYYY_MM_DD_HH_MM_SS));
            builder.append(Symbol.SYMBOL_COMMA);


            builder.append(dataList.get(m));


            String transfer = DataTransferService.parseDataByMode(taskKey(), builder.toString());
            if (PredicateUtil.isNotEmpty(transfer)) {
                //执行业务
                sqlDataList.add(transfer);
            }

        }
    }


    @Override
    public String taskKey() {
        return IvMDBDataPusher.TASK_KEY;
    }

}
