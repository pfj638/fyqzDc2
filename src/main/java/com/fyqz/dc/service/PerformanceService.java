package com.fyqz.dc.service;

import com.fyqz.dc.common.performance.ActionPerformanceEvent;
import com.fyqz.dc.common.performance.ClassPerformanceTag;

import java.util.List;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/25 17:21
 * Description:  
 */
public interface PerformanceService {
    /**
     * 主要是将提取到的Action的静态结构数据放到数据库中
     *
     */
    public void initTagTable(List<ClassPerformanceTag> classPerformanceTags);

    /**
     * 处理一个peformanceEvent
     */
    public void processEvent(ActionPerformanceEvent event);
}
