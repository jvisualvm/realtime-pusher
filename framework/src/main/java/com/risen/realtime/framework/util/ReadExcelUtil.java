package com.risen.realtime.framework.util;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.risen.common.cosntant.Symbol;
import com.risen.common.util.LogUtil;
import com.risen.realtime.framework.dto.ExcelFileDetailDTO;
import com.risen.realtime.framework.dto.ExcelFileItemDTO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/8/9 13:52
 */
public class ReadExcelUtil {

    public static ExcelFileDetailDTO readExcel(String filePath, Integer sheetIndex, Integer startRow, Integer needColumns) {
        ExcelFileDetailDTO excel = new ExcelFileDetailDTO();
        List<ExcelFileItemDTO> dataList = new ArrayList<>();
        Workbook wb = null;
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            wb = WorkbookFactory.create(is);
            if (wb != null) {
                if (startRow == null) {
                    startRow = 0;
                }
                Sheet sheet = wb.getSheetAt(sheetIndex - 1);
                int lastMaxRowNum = sheet.getLastRowNum();
                Row firstRow = sheet.getRow(startRow);
                int maxColNum = firstRow.getPhysicalNumberOfCells();

                if (needColumns == null) {
                    needColumns = maxColNum;
                }
                LogUtil.info("lastMaxRowNum:{},maxColNum:{},startRow:{},needColumns:{}", lastMaxRowNum, maxColNum, startRow, needColumns);
                for (int i = startRow; i <= lastMaxRowNum; i++) {
                    StringBuilder builder = new StringBuilder();
                    builderAppend(i, sheet, builder, needColumns);
                    ExcelFileItemDTO itemDTO = new ExcelFileItemDTO(i, builder.toString());
                    dataList.add(itemDTO);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                wb.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        excel.setColumns(needColumns);
        excel.setDataList(dataList);
        return excel;
    }


    private static void builderAppend(int i, Sheet sheet, StringBuilder builder, Integer needColumns) {
        for (int j = 0; j < needColumns; j++) {
            Cell cell = sheet.getRow(i).getCell(j);
            String cellValue = getCellValueByCell(cell);
            if (ObjectUtils.isNotEmpty(cellValue)) {
                if (j == 0) {
                    builder.append(Symbol.QUOTA);
                    builder.append(cellValue);
                    builder.append(Symbol.QUOTA);
                } else {
                    builder.append(Symbol.SYMBOL_COMMA);
                    builder.append(Symbol.QUOTA);
                    builder.append(cellValue);
                    builder.append(Symbol.QUOTA);
                }
            } else {
                if (j == 0) {
                    builder.append(cellValue);
                } else {
                    builder.append(Symbol.SYMBOL_COMMA);
                    builder.append(cellValue);
                }
            }
        }
    }


    public static String getCellValueByCell(Cell cell) {
        if (cell == null || cell.toString().trim().equals("")) {
            return null;
        }
        String cellValue = null;
        switch (cell.getCellType().getCode()) {
            case 0:
                short format = cell.getCellStyle().getDataFormat();
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = null;
                    if (format == 20 || format == 32) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else if (format == 14 || format == 31 || format == 57 || format == 58) {
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    } else if (format == 177) {
                        sdf = new SimpleDateFormat("yyyy/MM/dd");
                    } else {
                        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    }
                    try {
                        cellValue = sdf.format(cell.getDateCellValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case 1:
                cellValue = cell.getStringCellValue();
                break;
            case 2:
                cellValue = cell.getCellFormula();
                break;
            case 3:
                cellValue = null;
                break;
            case 4:
                cellValue = cell.getBooleanCellValue() + "";
                break;
            case 5:
                cellValue = null;
                break;
            default:
                cellValue = null;
                break;
        }
        return cellValue;
    }

}