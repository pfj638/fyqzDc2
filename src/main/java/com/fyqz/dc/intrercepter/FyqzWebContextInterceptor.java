package com.fyqz.dc.intrercepter;

import com.fyqz.dc.common.BaseAction;
import com.fyqz.dc.common.Constants;
import com.fyqz.dc.common.FYQZContext;
import com.fyqz.dc.entity.User;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

/**
 * 用于自动赋值调用的上下文信息
 * 请注意，该拦截器依赖于i18,请放在i18拦截器之后，否则不能工作
 * 这里从session中取而不是request取的原因就是需要考虑i18的影响
 */
public class FyqzWebContextInterceptor extends AbstractInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(FyqzWebContextInterceptor.class);

    public String intercept(ActionInvocation invocation) throws Exception {
        Object action = invocation.getAction();
        if (action != null && action instanceof BaseAction) {
            final FYQZContext fyqzContext = new FYQZContext();
            final Map<String, Object> session = invocation.getInvocationContext().getSession();
            fyqzContext.setLocale(invocation.getInvocationContext().getLocale());
            //这是来源于web
            fyqzContext.setClientType(Constants.ClientType.CLIENT_TYPE_WEB);
            final User user = (User) session.get(Constants.WebInitConfigConstant.USER_IN_SESSION_NAME);
            if (user != null) {
                fyqzContext.setUserName(user.getLoginName());
            }
            final HttpServletRequest request = ServletActionContext.getRequest();
            if (request != null) {
                fyqzContext.setRequesterIP(request.getRemoteAddr());
            }
            ((BaseAction) action).setFyqzContext(fyqzContext);
        }
        return invocation.invoke();
    }



}
