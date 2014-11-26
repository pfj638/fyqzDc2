package com.fyqz.dc.dao.impl;

import com.fyqz.dc.dao.PerformanceDao;
import com.fyqz.dc.entity.PerformanceClassEntity;
import org.springframework.stereotype.Repository;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/26 10:07
 * Description:  
 */
@Repository
public class PerformanceDaoImpl extends BaseDaoImpl<PerformanceClassEntity> implements PerformanceDao {

    @Override
    public void deleteAll(Class<?> aClass) {
        getHibernateTemplate().bulkUpdate("delete from  " + aClass.getSimpleName());
    }
}
