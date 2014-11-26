package com.fyqz.dc.entity;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


/**
 * SortRule entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_CMS_SORT_RULE")

public class SortRule  implements java.io.Serializable {


    // Fields    

     private String id;
     private String processId;
     private String name;
     private String feasibility;
     private String cost;
     private String result;


    // Constructors

    /** default constructor */
    public SortRule() {
    }

    
    /** full constructor */
    public SortRule(String processId, String name, String feasibility, String cost, String result) {
        this.processId = processId;
        this.name = name;
        this.feasibility = feasibility;
        this.cost = cost;
        this.result = result;
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
    
    @Column(name="process_id", length=32)

    public String getProcessId() {
        return this.processId;
    }
    
    public void setProcessId(String processId) {
        this.processId = processId;
    }
    
    @Column(name="name", length=32)

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="feasibility", length=32)

    public String getFeasibility() {
        return this.feasibility;
    }
    
    public void setFeasibility(String feasibility) {
        this.feasibility = feasibility;
    }
    
    @Column(name="cost", length=32)

    public String getCost() {
        return this.cost;
    }
    
    public void setCost(String cost) {
        this.cost = cost;
    }
    
    @Column(name="result", length=32)

    public String getResult() {
        return this.result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
   








}