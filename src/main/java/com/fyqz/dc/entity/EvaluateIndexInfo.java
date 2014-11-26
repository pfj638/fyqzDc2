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
 * EvaluateIndexInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_CMS_EVALUATEINDEX_INFO")

public class EvaluateIndexInfo  implements java.io.Serializable {


    // Fields    

     private String id;
     private EvaluateIndex evaluateIndex;
     private String x;
     private String y;


    // Constructors

    /** default constructor */
    public EvaluateIndexInfo() {
    }

    
    /** full constructor */
    public EvaluateIndexInfo(EvaluateIndex evaluateIndex, String x, String y) {
        this.evaluateIndex = evaluateIndex;
        this.x = x;
        this.y = y;
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
    @ManyToOne
    @JoinColumn(name="evaluate_index_id")

    public EvaluateIndex getEvaluateIndex() {
        return this.evaluateIndex;
    }
    
    public void setEvaluateIndex(EvaluateIndex evaluateIndex) {
        this.evaluateIndex = evaluateIndex;
    }
    
    @Column(name="x", length=32)

    public String getX() {
        return this.x;
    }
    
    public void setX(String x) {
        this.x = x;
    }
    
    @Column(name="y", length=32)

    public String getY() {
        return this.y;
    }
    
    public void setY(String y) {
        this.y = y;
    }
   








}