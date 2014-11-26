package com.fyqz.dc.dao;

import com.fyqz.dc.entity.PerformanceClassEntity;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/26 10:06
 * Description:  
 */
public interface PerformanceDao extends BaseDao<PerformanceClassEntity> {
    public void deleteAll(Class<?> aClass);
}
