
package com.fyqz.dc.intrercepter;

import com.fyqz.dc.common.BaseAction;
import com.fyqz.dc.dto.ExtOperResult;
import com.fyqz.dc.util.AnnotationUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.config.entities.ExceptionMappingConfig;
import com.opensymphony.xwork2.interceptor.ExceptionHolder;
import com.opensymphony.xwork2.interceptor.ExceptionMappingInterceptor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/*
 * Copyright (C), 2002-2013
 * FileName:
 * Author:   Shi Hai Yang
 * Date:     2014/5/6 14:38
 * Description: 主要对于加annotaion的进行处理特殊的异常，
 * ajax的异常自动跳转到异常页
 */
public class JsonExceptionInterceptor extends ExceptionMappingInterceptor {
	private static final long serialVersionUID = 2561501602354465910L;
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        String result;

        try {
            result = invocation.invoke();
        } catch (Exception e) {

            //记录日志
            if (isLogEnabled()) {
                handleLogging(e);
            }

            //这里特殊校验有注解的异常
            boolean ajaxRequest = isAjaxRequest();
            if (ajaxRequest) {
                Object action = invocation.getAction();
                //是ajax请求，并且增加了一个@OperResultForJson
                if (action != null && action instanceof BaseAction && AnnotationUtil.checkAnnotationExist(invocation, action)) {
                    final BaseAction baseAction = ((BaseAction) action);
                    final ExtOperResult operResult = (ExtOperResult) baseAction.getOperResult();
                    //通知框架，处理已经失败
                    operResult.setSysSuccess(false);
                    //设置返回给普通用户的错误信息
                    operResult.setErrorDesc("发生系统故障，请检查你的代码:" + ExceptionUtils.getStackTrace(e));
                    operResult.setCode(OperRetCodeConstant.RET_SYSTEM_ERR);
                    return ActionSupport.NONE;
                }
            }

            List<ExceptionMappingConfig> exceptionMappings = invocation.getProxy().getConfig().getExceptionMappings();
            String mappedResult = this.findResultFromExceptions(exceptionMappings, e);
            if (mappedResult != null) {
                result = mappedResult;
                publishException(invocation, new ExceptionHolder(e));
            } else {
                throw e;
            }
        }

        return result;
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
