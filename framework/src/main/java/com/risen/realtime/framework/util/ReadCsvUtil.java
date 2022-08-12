package com.risen.realtime.framework.util;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.risen.common.cosntant.Symbol;
import com.risen.common.util.LogUtil;
import com.risen.realtime.framework.dto.CSVFileDetailDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/27 10:38
 */
public class ReadCsvUtil {

    public static CSVFileDetailDTO readCsvFile(File file, int readStartPoint) {
        LogUtil.info("开始读取文件" + file.getName());
        CSVFileDetailDTO csv = new CSVFileDetailDTO();
        List<String> dataList = new ArrayList<>();
        CSVReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), Symbol.GBK);
            CSVParser csvParser = new CSVParserBuilder().withSeparator(Symbol.CHAR_COMMA).withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS).build();
            reader = new CSVReaderBuilder(is).withCSVParser(csvParser).build();
            int index = 1;
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (index > readStartPoint) {
                    StringBuilder builder = new StringBuilder();
                    int start = 0;
                    for (String item : nextLine) {
                        if (start == 0) {
                            ++start;
                            builder.append(item);
                        } else {
                            builder.append(Symbol.CHAR_COMMA + item);
                        }
                    }
                    dataList.add(builder.toString());
                } else {
                    ++index;
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.info("读取行数:" + dataList.size());
        csv.setDataList(dataList);
        return csv;
    }

}
