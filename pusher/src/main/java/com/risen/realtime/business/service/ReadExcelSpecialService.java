package com.risen.realtime.business.service;

import com.google.common.collect.Lists;
import com.risen.common.cosntant.Symbol;
import com.risen.common.util.LogUtil;
import com.risen.realtime.framework.dto.ExcelFileItemDTO;
import com.risen.realtime.framework.dto.InsertCountDTO;
import com.risen.realtime.framework.log.LogSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/8/10 15:54
 */
public class ReadExcelSpecialService {

    public static List<String> dataBmTransfer(List<ExcelFileItemDTO> s, InsertCountDTO insertCount, InsertCountDTO finishIndex) {
        List<List<ExcelFileItemDTO>> lst = Lists.partition(s, 5);
        List<String> dataList = new ArrayList<>();
        String errorLog = "当前不够5行，无法操作";
        boolean readFinish = false;
        int finishRowNumber = 0;
        for (int i = 0, m = lst.size(); i < m; i++) {
            String firstRow = null;
            List<String> result = new ArrayList<>();
            for (int j = 0, k = lst.get(i).size(); j < k; j++) {
                StringBuilder builder = new StringBuilder();
                String rowData = lst.get(i).get(j).getRowData();
                int rowNumber = lst.get(i).get(j).getRowNumber();
                if (k < 5) {
                    LogUtil.error(errorLog);
                    LogSender.printError(errorLog);
                    readFinish = true;
                    finishRowNumber = lst.get(i).get(0).getRowNumber();
                    break;
                }
                if (j == 0) {
                    firstRow = rowData.substring(0, rowData.lastIndexOf(Symbol.SYMBOL_COMMA));
                    builder.append(rowData);
                } else {
                    builder.append(firstRow);
                    builder.append(Symbol.SYMBOL_COMMA);
                    builder.append(rowData.substring(rowData.lastIndexOf(Symbol.SYMBOL_COMMA) + 1));
                }
                if (dataNeedFilter(rowNumber, builder.toString())) {
                    readFinish = true;
                    finishRowNumber = lst.get(i).get(0).getRowNumber();
                    break;
                }
                builder.append(Symbol.SYMBOL_COMMA);
                builder.append(Symbol.QUOTA);
                builder.append(rowNumber + 1);
                builder.append(Symbol.QUOTA);
                result.add(builder.toString());
            }
            if (result.size() == 5) {
                dataList.addAll(result);
            }
            result.clear();


            if (readFinish) {
                insertCount.setCount(dataList.size());
                finishIndex.setCount(finishRowNumber);
                break;
            }
        }
        return dataList;
    }


    public static List<String> dataBdhTransfer(List<ExcelFileItemDTO> s, InsertCountDTO insertCount, InsertCountDTO finishIndex) {
        List<List<ExcelFileItemDTO>> lst = Lists.partition(s, 40);
        List<String> dataList = new ArrayList<>();
        String errorLog = "当前不够40行，无法操作";
        boolean readFinish = false;
        int finishRowNumber = 0;
        for (int i = 0, m = lst.size(); i < m; i++) {
            String firstRow = null;
            String firstRowAppend = null;
            List<String> result = new ArrayList<>();
            for (int j = 0, k = lst.get(i).size(); j < k; j++) {
                StringBuilder builder = new StringBuilder();
                String rowData = lst.get(i).get(j).getRowData();
                int rowNumber = lst.get(i).get(j).getRowNumber();
                if (k < 40) {
                    LogUtil.error(errorLog);
                    LogSender.printError(errorLog);
                    readFinish = true;
                    finishRowNumber = lst.get(i).get(0).getRowNumber();
                    break;
                }
                if (j == 0) {
                    firstRow = rowData.substring(0, rowData.lastIndexOf(Symbol.SYMBOL_COMMA));
                    firstRowAppend = firstRow.substring(0, firstRow.lastIndexOf(Symbol.SYMBOL_COMMA));
                    builder.append(rowData);
                } else {
                    if (j < 20) {
                        builder.append(firstRow);
                        builder.append(Symbol.SYMBOL_COMMA);
                        builder.append(rowData.substring(rowData.lastIndexOf(Symbol.SYMBOL_COMMA) + 1));
                    } else if (j == 20) {
                        String[] temp = rowData.split(Symbol.SYMBOL_COMMA);
                        builder.append(firstRowAppend).append(Symbol.SYMBOL_COMMA).append(temp[5]).append(Symbol.SYMBOL_COMMA).append(temp[6]);
                    } else {
                        builder.append(firstRow);
                        builder.append(Symbol.SYMBOL_COMMA);
                        builder.append(rowData.substring(rowData.lastIndexOf(Symbol.SYMBOL_COMMA) + 1));
                    }
                }
                if (dataNeedFilter(rowNumber, builder.toString())) {
                    readFinish = true;
                    finishRowNumber = lst.get(i).get(0).getRowNumber();
                    break;
                }
                builder.append(Symbol.SYMBOL_COMMA);
                builder.append(Symbol.QUOTA);
                builder.append(rowNumber + 1);
                builder.append(Symbol.QUOTA);
                result.add(builder.toString());
            }
            if (result.size() == 40) {
                dataList.addAll(result);
            }
            result.clear();

            if (readFinish) {
                insertCount.setCount(dataList.size());
                finishIndex.setCount(finishRowNumber);
                break;
            }
        }
        return dataList;
    }

    public static boolean dataNeedFilter(int rowNumber, String rowData) {
        List<String> splitList = Arrays.stream(rowData.split(Symbol.SYMBOL_COMMA)).collect(Collectors.toList());
        int count = (int) splitList.stream().filter(l -> Symbol.STR_NULL.equals(l)).count();
        if (count > 0) {
            LogUtil.error("第：{}行数据不全被丢弃，原始数据：{}", rowNumber + 1, rowData);
            return true;
        }
        return false;
    }


}
