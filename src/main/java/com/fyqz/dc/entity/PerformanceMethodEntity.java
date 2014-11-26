package com.fyqz.dc.entity;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/26 9:44
 * Description:  
 */

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DC_PERFORMANCE_METHOD_TAG" )
public class PerformanceMethodEntity implements java.io.Serializable {
    private String id;

    /**
     * action的class的完全限定名称
     * 如果是method，则存储的是method的名称
     */
    protected String name;
    /**
     * 用户能读明白的描述
     */
    protected String desc;

    /**
     * 对于class而言，显示例如user_*
     * 如果是method,显示如user_init
     */
    protected String mappingName;

    /**
     * 注解的分类　，默认值为 1
     */
    protected Integer category;

    /**
     * 所属的类
     */
    private PerformanceClassEntity classEntity;

    // Property accessors
    @GenericGenerator(name = "generator", strategy = "uuid.hex")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    @Column(name = "tag_category")
    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    @Column(name = "mapping_name")
    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    @Column(name = "tag_desc")
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Column(name = "real_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="ref_class")
    public PerformanceClassEntity getClassEntity() {
        return classEntity;
    }

    public void setClassEntity(PerformanceClassEntity classEntity) {
        this.classEntity = classEntity;
    }
}
