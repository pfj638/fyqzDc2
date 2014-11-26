
package com.fyqz.dc.intrercepter;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;



public class FyqzLoggingInterceptor extends AbstractInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(FyqzLoggingInterceptor.class);
    private static final String FINISH_MESSAGE = "结束执行action: ";
    private static final String START_MESSAGE = "开始执行action ";

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        logMessage(invocation, START_MESSAGE);
        String result = invocation.invoke();
        logMessage(invocation, FINISH_MESSAGE);
        return result;
    }

    private void logMessage(ActionInvocation invocation, String baseMessage) {
        if (LOG.isInfoEnabled()) {
            StringBuilder message = new StringBuilder(baseMessage);
            String namespace = invocation.getProxy().getNamespace();

            if ((namespace != null) && (namespace.trim().length() > 0)) {
                message.append(namespace).append("/");
            }

            message.append(invocation.getProxy().getActionName());
            if (LOG.isInfoEnabled()) {
        	LOG.info(message.toString());
            }
        }
    }

}
