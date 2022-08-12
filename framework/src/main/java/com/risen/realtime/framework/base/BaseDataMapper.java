package com.risen.realtime.framework.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/2 7:44
 */
@Mapper
public interface BaseDataMapper<T> extends BaseMapper<T> {


}
