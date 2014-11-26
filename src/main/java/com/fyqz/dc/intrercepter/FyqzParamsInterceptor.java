package com.fyqz.dc.intrercepter;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/21 15:20
 * Description:  
 */
public class FyqzParamsInterceptor extends AbstractInterceptor {
    private static final long serialVersionUID = 3562355302088124018L;
    private static final Logger log = LoggerFactory.getLogger(FyqzParamsInterceptor.class.getName());

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        try {
            logParams(invocation);
        } catch (Exception e) {
            log.error("参数拦截器出现问题,",e);
        }
        return invocation.invoke();
    }

    private void logParams(ActionInvocation invocation){
        final String actionName = invocation.getProxy().getActionName();
        log.info("===========打印action{}参数开始===========",actionName);
        final Map<String, Object> params = invocation.getInvocationContext().getParameters();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();
            if (val != null && val.getClass().isArray()) {
                log.info("{}={}", key, Arrays.toString((Object[]) val));
            } else {
                log.info("{}={}", key, val);
            }
        }
        log.info("===========打印参数结束===============================");
    }
}
