package com.fyqz.dc.intrercepter;

import com.fyqz.dc.common.BaseAction;
import com.fyqz.dc.common.Constants;
import com.fyqz.dc.dto.ExtOperResult;
import com.fyqz.dc.dto.OperResult;
import com.fyqz.dc.entity.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 这个class目前用来做
 */

public class LoginInterceptor extends AbstractInterceptor {
	
	private static final long serialVersionUID = 3660357405927325910L;

    private transient boolean myInit = false;

    private final Object lock = new Object();

    private String[] loginCheck = null;

    private String[] ignoreCheck = null;


    @Override
    /**
     * 拦截未登录用户
     */
    public String intercept(ActionInvocation invocation) throws Exception {
        final ServletContext servletContext = ServletActionContext.getServletContext();
        if (!myInit) {
            synchronized (lock) {
                String checkPatterns = servletContext.getInitParameter(Constants.WebInitConfigConstant.LOGIN_CHECK_PATTERN);
                if (StringUtils.isNotEmpty(checkPatterns)) {
                    loginCheck = StringUtils.split(checkPatterns.trim(), ",");
                }
                String ignorePatterns = servletContext.getInitParameter(Constants.WebInitConfigConstant.LOGIN_IGNORE_PATTERN);
                if (StringUtils.isNotEmpty(ignorePatterns)) {
                    ignoreCheck = StringUtils.split(ignorePatterns.trim(), ",");
                }
                myInit = true;
            }
        }

        final boolean timeout = isTimeout(invocation);
        final String actionName = invocation.getProxy().getActionName();
        if (StringUtils.isEmpty(actionName) || PatternMatchUtils.simpleMatch(ignoreCheck, actionName)) {
            return invocation.invoke();
        } else if (PatternMatchUtils.simpleMatch(loginCheck, actionName)) {
            boolean ajaxRequest = isAjaxRequest();
            if (timeout && ajaxRequest) {
                //通知框架，已经出现超时错误
                final Object action = invocation.getAction();
                if (action != null && action instanceof BaseAction) {
                    final OperResult operResult = ((BaseAction) action).getOperResult();
                    ((ExtOperResult) operResult).setSysSuccess(false);
                }
                //这只是将用户响应中的字库设置超时
                setUserRespTimeout(invocation, true);
            }
            //非ajax请求
            if (timeout && !ajaxRequest) {
                return "timeout";
            }
        }
        return invocation.invoke();
    }


    /**
     * 设置是否超时变量到Action中
     *
     * @param invocation
     * @param timeout
     */
    private void setUserRespTimeout(ActionInvocation invocation, boolean timeout) {
        final Object action = invocation.getAction();
        if (action != null && action instanceof BaseAction) {
            ((BaseAction) action).setTimeout(timeout);
        }
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

    /**
     * 判断登录是否超时
     *
     * @param invocation
     * @return
     */
    private boolean isTimeout(ActionInvocation invocation) {
        final ActionContext actionContext = invocation.getInvocationContext();
        final Map<String, Object> session = actionContext.getSession();
        final User user = (User) session.get(Constants.WebInitConfigConstant.USER_IN_SESSION_NAME);
        return user == null;
    }




}
