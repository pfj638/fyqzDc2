package com.fyqz.dc.service.impl;

import com.fyqz.dc.common.performance.ActionPerformanceEvent;
import com.fyqz.dc.common.performance.ClassPerformanceTag;
import com.fyqz.dc.common.performance.MethodPerformanceTag;
import com.fyqz.dc.dao.PerformanceDao;
import com.fyqz.dc.entity.PerformanceClassEntity;
import com.fyqz.dc.entity.PerformanceLog;
import com.fyqz.dc.entity.PerformanceMethodEntity;
import com.fyqz.dc.service.PerformanceService;
import com.fyqz.dc.util.CollectionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/25 17:22
 * Description:  
 */
@Service
public class PerformanceServiceImpl implements PerformanceService {


    @Autowired
    private PerformanceDao performanceDao;


    @Override
    public void initTagTable(List<ClassPerformanceTag> classPerformanceTags) {
        performanceDao.deleteAll(PerformanceMethodEntity.class);
        performanceDao.deleteAll(PerformanceClassEntity.class);
        if (CollectionUtil.isNotEmpty(classPerformanceTags)) {
//            final List<PerformanceClassEntity> allEntity = new ArrayList<PerformanceClassEntity>();
            for (ClassPerformanceTag classPerformanceTag : classPerformanceTags) {
                final PerformanceClassEntity classEntity = new PerformanceClassEntity();
                classEntity.setName(classPerformanceTag.getName());
                classEntity.setMappingName(classPerformanceTag.getMappingName());
                classEntity.setDesc(classPerformanceTag.getDesc());
                classEntity.setCategory(classPerformanceTag.getCategory());
                final List<MethodPerformanceTag> methodPerformanceTags = classPerformanceTag.getMethodPerformanceTags();
                if (CollectionUtil.isNotEmpty(methodPerformanceTags)) {
                    final List<PerformanceMethodEntity> methodEntityList = new ArrayList<PerformanceMethodEntity>();
                    for (MethodPerformanceTag methodTag : methodPerformanceTags) {
                        final PerformanceMethodEntity entity = new PerformanceMethodEntity();
                        entity.setName(methodTag.getName());
                        entity.setMappingName(methodTag.getMappingName());
                        entity.setDesc(methodTag.getDesc());
                        entity.setCategory(methodTag.getCategory());
                        entity.setClassEntity(classEntity);//设置所属对象
                        methodEntityList.add(entity);
                    }
                    classEntity.setMethodEntities(methodEntityList);
                }

                performanceDao.saveOrUpdate(classEntity);

//                allEntity.add(classEntity);
            }

        }
    }

    @Override
    public void processEvent(ActionPerformanceEvent event) {
        final PerformanceLog log=new PerformanceLog();
        BeanUtils.copyProperties(event,log);
        performanceDao.save(log);


    }


    public PerformanceDao getPerformanceDao() {
        return performanceDao;
    }

    public void setPerformanceDao(PerformanceDao performanceDao) {
        this.performanceDao = performanceDao;
    }


}
