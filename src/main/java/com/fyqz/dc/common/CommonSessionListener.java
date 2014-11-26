package com.fyqz.dc.common;

import com.fyqz.dc.config.ProductConfiguration;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/25 8:49
 * Description:  用于在会话中放置项目的名称
 */
public class CommonSessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

        final String productName = ProductConfiguration.instance().getProductName();
        httpSessionEvent.getSession().setAttribute(Constants.ProductConfig.PRODUCT_NAME_KEY, productName);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

        httpSessionEvent.getSession().removeAttribute(Constants.ProductConfig.PRODUCT_NAME_KEY);
    }
}
