package com.risen.realtime.business.filter;

import com.risen.realtime.business.job.IvCSVBmDataPusher;
import com.risen.realtime.framework.monitor.BaseDirectoryFilter;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/27 10:07
 */
@Component
public class IvCsvBmDirectoryFilter extends BaseDirectoryFilter {

    @Override
    public String taskKey() {
        return IvCSVBmDataPusher.TASK_KEY;
    }

    @Override
    public boolean accept(File dir, String name) {
        return super.accept(dir, name);
    }
}
