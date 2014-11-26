package com.fyqz.dc.common;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/21 16:50
 * Description:  
 */
public class PerformanceInterceptor implements MethodInterceptor {
    private static final Logger log = LoggerFactory.getLogger(PerformanceInterceptor.class.getName());

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        final Object ret = invocation.proceed();
        final long executionTime = System.currentTimeMillis() - startTime;
        final StringBuilder sb = new StringBuilder();
        sb.append("service性能:");
        final Object[] args = invocation.getArguments();
        if (args != null && args.length > 0) {
            Object contextArg = args[0];
            if (contextArg != null && contextArg instanceof FYQZContext) {
                final FYQZContext context = (FYQZContext) contextArg;
                sb.append(context.toString());
                /*sb.append("调用的用户名:");
                sb.append(context.getUserName());
                sb.append(",调用者Ip:");
                sb.append(context.getRequesterIP());*/
            }
        }
        sb.append(",方法名:");
        sb.append(invocation.getThis().getClass().getName());
        sb.append(".");
        sb.append(invocation.getMethod().getName());
        sb.append("(),共耗时");
        sb.append(executionTime);
        sb.append("ms");
        log.info(sb.toString());
        return ret;
    }
}
