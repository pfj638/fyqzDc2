package com.fyqz.dc.common.performance;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/25 16:01
 * Description:  
 */
public class PerformanceTag {
    /**
     * action的class的完全限定名称
     * 如果是method，则存储的是method的名称
     */
    protected String name;
    /**
     * 关于这个的用户能读明白的描述
     */
    protected String desc;

    /**
     * 对于class而言，显示例如user_*
     * 如果是method,显示如user_init
     */
    protected  String mappingName;

    /**
     * 注解的分类　，默认值为 1
     */
    protected  Integer category;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PerformanceTag{");
        sb.append("name='").append(name).append('\'');
        sb.append("category='").append(category).append('\'');
        sb.append("mappingName='").append(mappingName).append('\'');
        sb.append(", desc='").append(desc).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
