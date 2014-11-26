package com.fyqz.dc.intrercepter;

import com.fyqz.dc.common.BaseAction;
import com.fyqz.dc.common.ProductConfig;
import com.fyqz.dc.config.ProductConfiguration;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/25 9:04
 * Description:  
 */
public class FyqzProductConfigInterceptor extends AbstractInterceptor {
    private static final Logger log = LoggerFactory.getLogger(FyqzProductConfigInterceptor.class.getName());

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        Object action = invocation.getAction();
        if (action != null && action instanceof BaseAction) {
            final BaseAction baseAction = ((BaseAction) action);
            final ProductConfig productConfig = baseAction.getProductConfig();
            final String productName = ProductConfiguration.instance().getProductName();
            if (productConfig != null) {
                productConfig.setProductName(productName);
            }
        }

        return invocation.invoke();
    }
}
