package com.fyqz.dc.common;

import com.p6spy.engine.spy.P6DataSource;
import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/22 11:34
 * Description:  
 */
public class MyP6DataSource extends P6DataSource {

    public void close() {
        try {
            ((BasicDataSource) rds).close();
        } catch (Exception e) {

        }
    }

    public MyP6DataSource(DataSource source) {
        super(source);
    }

    public MyP6DataSource() {
    }
}
