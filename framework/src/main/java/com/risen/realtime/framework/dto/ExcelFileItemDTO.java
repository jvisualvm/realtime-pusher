package com.risen.realtime.framework.dto;

import lombok.Data;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/29 21:39
 */
@Data
public class ExcelFileItemDTO {

    private int rowNumber;
    private String rowData;


    public ExcelFileItemDTO(int rowNumber, String rowData) {
        this.rowNumber = rowNumber;
        this.rowData = rowData;
    }
}
