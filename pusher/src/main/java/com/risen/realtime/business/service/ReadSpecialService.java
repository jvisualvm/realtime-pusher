package com.risen.realtime.business.service;

import com.risen.common.cosntant.Symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/27 16:47
 */

public class ReadSpecialService {

    public static String deleteNotNeedChar(String s, boolean needDeleteFirst, boolean needDeleteSecond) {
        StringBuilder builder = new StringBuilder();
        String[] info = s.split(Symbol.SYMBOL_COMMA);
        for (int index = 0, size = info.length; index < size; index++) {
            if (needDeleteSecond && index == 1) {
                continue;
            }
            if (needDeleteFirst && needDeleteSecond && (index == 0 || index == 1)) {
                continue;
            }
            builder.append(info[index]);
            builder.append(Symbol.SYMBOL_COMMA);
        }
        return builder.deleteCharAt(builder.length() - 1).toString();
    }


    public static List<String> filterList(String endFlag, List<String> dataList) {
        List<String> lst = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).contains(endFlag)) {
                break;
            }
            lst.add(dataList.get(i));
        }
        return lst;
    }

}
