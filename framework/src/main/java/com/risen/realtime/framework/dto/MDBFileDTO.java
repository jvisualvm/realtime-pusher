package com.risen.realtime.framework.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/29 19:35
 */
@Data
public class MDBFileDTO {
    private List<String> fieldList;
    private Integer maxKey;
    private List<String> dataList = new ArrayList<>();
}
