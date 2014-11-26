package com.fyqz.dc.common.performance;

import com.fyqz.dc.util.CollectionUtil;

import java.util.Arrays;
import java.util.List;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/25 15:55
 * Description:  
 */
public class ClassPerformanceTag extends PerformanceTag {

    /**
     * 该Action类中所有的的action方法
     */
    private List<MethodPerformanceTag> methodPerformanceTags;


    public List<MethodPerformanceTag> getMethodPerformanceTags() {
        return methodPerformanceTags;
    }

    public void setMethodPerformanceTags(List<MethodPerformanceTag> methodPerformanceTags) {
        this.methodPerformanceTags = methodPerformanceTags;
    }



    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ClassPerformanceTag{");
        sb.append("name='").append(name).append('\'');
        sb.append("mappingName='").append(mappingName).append('\'');
        sb.append(", desc='").append(desc).append('\'');
        if (CollectionUtil.isNotEmpty(methodPerformanceTags)) {
            sb.append("methodPerformanceTags=").append(Arrays.toString(methodPerformanceTags.toArray()));
        }
        sb.append('}');
        return sb.toString();
    }
}
