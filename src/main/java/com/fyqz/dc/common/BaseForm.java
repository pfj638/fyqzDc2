package com.fyqz.dc.common;

import com.fyqz.dc.dto.Page;

import java.io.Serializable;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/5/6 16:10
 * Description:这是一个基础类
   * 
 */
public class BaseForm implements Serializable {
	private static final long serialVersionUID = 8866501605927465910L;
    /**
     * 可以携带分页信息
     */
    protected Page page;
    /**
     * 产品的标题,用于显示在jsp的title中
     */
    private String productTitle;

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }


    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
