package com.fyqz.dc.entity;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * EvaluateIndex entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_CMS_EVALUATE_INDEX")

public class EvaluateIndex  implements java.io.Serializable {


    // Fields    

     private String id;
     private String name;
     private String unit;
     private String type;
     private MindItem item;
     private String processId;


    // Constructors

    /** default constructor */
    public EvaluateIndex() {
    }

    
    /** full constructor */
    public EvaluateIndex(String name, String unit, String type, MindItem item, String processId) {
        this.name = name;
        this.unit = unit;
        this.type = type;
        this.item = item;
        this.processId = processId;
    }

   
    // Property accessors
    @GenericGenerator(name="generator", strategy="uuid.hex")@Id @GeneratedValue(generator="generator")
    
    @Column(name="id", unique=true, nullable=false, length=32)

    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name="name", length=32)

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="unit", length=32)

    public String getUnit() {
        return this.unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    @Column(name="type", length=32)

    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    @ManyToOne
    @JoinColumn(name="item_id")

    public MindItem getItem() {
        return this.item;
    }
    
    public void setItem(MindItem item) {
        this.item = item;
    }
    
    @Column(name="process_id", length=32)

    public String getProcessId() {
        return this.processId;
    }
    
    public void setProcessId(String processId) {
        this.processId = processId;
    }
   








}