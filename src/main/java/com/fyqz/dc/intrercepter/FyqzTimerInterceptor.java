package com.fyqz.dc.intrercepter;

import com.fyqz.dc.common.BaseAction;
import com.fyqz.dc.common.FYQZContext;
import com.fyqz.dc.common.performance.ActionPerformanceEvent;
import com.fyqz.dc.common.performance.PerformanceUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.TimerInterceptor;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/21 16:08
 * Description:  
 */
public class FyqzTimerInterceptor extends TimerInterceptor implements ApplicationContextAware {
    private ApplicationContext appCtx;
    private static final Logger log = LoggerFactory.getLogger(FyqzTimerInterceptor.class.getName());

    private boolean logPerformanceToDb=true;


    @Override
    protected String invokeUnderTiming(ActionInvocation invocation) throws Exception {
        long startTime = System.currentTimeMillis();
        final String result = invocation.invoke();
        long executionTime = System.currentTimeMillis() - startTime;

        final StringBuilder message = new StringBuilder(100);
        final Object action = invocation.getAction();
        if (action != null && action instanceof BaseAction) {
            final FYQZContext context = ((BaseAction) action).getFyqzContext();
            message.append("action性能:");
            if (null != context) {
                message.append(context.toString());
            }
            message.append("执行action [");
            String namespace = invocation.getProxy().getNamespace();
            if ((namespace != null) && (namespace.trim().length() > 0)) {
                message.append(namespace).append("/");
            }
            final String actionName = invocation.getProxy().getActionName();
            message.append(actionName);

            message.append("] 共花费 ").append(executionTime).append(" ms.");

            if (logPerformanceToDb) {
                final HttpServletRequest request = ServletActionContext.getRequest();
                String sessionId = null;
                if (request != null) {
                    sessionId = request.getRequestedSessionId();
                }
                final ActionPerformanceEvent event = getEvent(context, actionName, executionTime, sessionId);
                if (event != null) {
                    try {
                        PerformanceUtil.processActionEvent(appCtx, event);
                    } catch (Exception e) {
                        log.error("保存性能数据时出现异常", e);
                    }
                }
            }
            doLog(getLoggerToUse(), message.toString());
        }
        return result;
    }

    private ActionPerformanceEvent getEvent(FYQZContext context, String actionName, long executionTime, String sessionId) {
        if (context == null) {
            return null;
        }
        ActionPerformanceEvent event = new ActionPerformanceEvent();
        BeanUtils.copyProperties(context,event);
        event.setActionName(actionName);
        event.setExecutionTime(executionTime);
        event.setExeDate(new Date());
        event.setSessionId(sessionId);
        return event;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appCtx = applicationContext;
    }


    public boolean isLogPerformanceToDb() {
        return logPerformanceToDb;
    }

    public void setLogPerformanceToDb(boolean logPerformanceToDb) {
        this.logPerformanceToDb = logPerformanceToDb;
    }
}
