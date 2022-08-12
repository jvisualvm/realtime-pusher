package com.risen.realtime.framework.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/29 21:39
 */
@Data
public class CSVFileDetailDTO {

    private Integer columns;
    private List<String> dataList = new ArrayList<>();
}
