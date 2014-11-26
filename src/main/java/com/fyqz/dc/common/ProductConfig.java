package com.fyqz.dc.common;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/25 9:01
 * Description: 用于配置产品的一些动态参数，例如参数的名称
 */
public class ProductConfig {
    /**
     * 用于存在产品的名称
     */
    private String productName;

    public ProductConfig() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
