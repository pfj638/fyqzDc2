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
 * SortResult entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_CMS_SORT_RESULT")

public class SortResult  implements java.io.Serializable {


    // Fields    

     private String id;
     private String processId;
     private SortRule rule;
     private String score;


    // Constructors

    /** default constructor */
    public SortResult() {
    }

    
    /** full constructor */
    public SortResult(String processId, SortRule rule, String score) {
        this.processId = processId;
        this.rule = rule;
        this.score = score;
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
    @ManyToOne
    @JoinColumn(name="rule_id")

    public SortRule getRule() {
        return this.rule;
    }
    
    public void setRule(SortRule rule) {
        this.rule = rule;
    }
    
    @Column(name="score", length=32)

    public String getScore() {
        return this.score;
    }
    
    public void setScore(String score) {
        this.score = score;
    }
   








}