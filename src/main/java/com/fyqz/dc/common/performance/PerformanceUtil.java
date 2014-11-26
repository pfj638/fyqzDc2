package com.fyqz.dc.common.performance;

import com.fyqz.dc.service.PerformanceService;
import org.springframework.context.ApplicationContext;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/25 17:21
 * Description:  
 */
public class PerformanceUtil {

    /**
     * 一个工具类方法用于处理Ａction中的事件
     *
     * @param appCtx
     * @param event
     */
    public static void processActionEvent(final ApplicationContext appCtx, final ActionPerformanceEvent event) {
        if (event == null || appCtx == null) {
            return;
        }
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        final PerformanceService service = appCtx.getBean(PerformanceService.class);
                        service.processEvent(event);
                    }
                }
        ).start();
    }
}
