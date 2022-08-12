package com.risen.realtime.framework.dto;

import lombok.Data;
import org.apache.commons.io.monitor.FileAlterationMonitor;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/26 21:56
 */
@Data
public class MonitorRunDetailDTO {

    private boolean isRunning;
    private FileAlterationMonitor monitor;
}
