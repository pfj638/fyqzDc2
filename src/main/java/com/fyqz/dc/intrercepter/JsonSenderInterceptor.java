package com.fyqz.dc.intrercepter;

import com.fyqz.dc.common.BaseAction;
import com.fyqz.dc.common.Constants;
import com.fyqz.dc.util.AnnotationUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/*
 * Copyright (C), 2002-2013
 * FileName:
 * Author:   Shi Hai Yang
 * Date:     2014/5/6 14:38
 * Description: 主要用来对加annotation的进请求进行自动发送数据
 */

public class JsonSenderInterceptor extends AbstractInterceptor {
	
	private static final long serialVersionUID = 3587401605922018910L;

    private transient boolean myInit = false;

    private final Object lock = new Object();

    private String[] ignoreCheck = null;


   @Override
    /**
     * 拦截Json自动发送请求
     */
    public String intercept(ActionInvocation invocation) throws Exception {
       final ServletContext servletContext = ServletActionContext.getServletContext();
       if (!myInit) {
           synchronized (lock) {
               String ignorePatterns = servletContext.getInitParameter(Constants.WebInitConfigConstant.LOGIN_IGNORE_PATTERN);
               if (StringUtils.isNotEmpty(ignorePatterns)) {
                   ignoreCheck = StringUtils.split(ignorePatterns.trim(), ",");
               }
               myInit = true;
           }
       }

       boolean ajaxRequest = isAjaxRequest();
       if (ajaxRequest) {
           Object action = invocation.getAction();
           //是ajax请求，并且增加了一个@OperResultForJson
           if (action != null && action instanceof BaseAction && AnnotationUtil.checkAnnotationExist(invocation, action)) {
               BaseAction baseAction = ((BaseAction) action);
               boolean timeout=baseAction.isTimeout();//这是上一个拦截器的功能
               final String actionName = invocation.getProxy().getActionName();
               boolean needIgnore= PatternMatchUtils.simpleMatch(ignoreCheck, actionName);
               if (timeout && !needIgnore) {
                   //填充超时然后发送响应,直接发送响应回客户端
                   baseAction.sendCommonResp();
                   return ActionSupport.NONE;
               } else {
                   String ret = invocation.invoke();
                   baseAction.sendCommonResp();//发送ajax请求
                   return ret;
               }
           }
       }
        return invocation.invoke();
    }


    /**
     * 判断是否是ajax请求
     *
     * @return
     */
    private boolean isAjaxRequest() {
        final HttpServletRequest request = ServletActionContext.getRequest();
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }




}
