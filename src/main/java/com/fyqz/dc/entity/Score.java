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
 * Score entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_CMS_SCORE")

public class Score  implements java.io.Serializable {


    // Fields    

     private String id;
     private String processId;
     private MindItem item;
     private String type;
     private String effect;
     private String feasibility;
     private String cost;
     private String result;
     private User expert;/*打分专家*/


    // Constructors

    /** default constructor */
    public Score() {
    }

    
    /** full constructor */
    public Score(String processId, MindItem item, String type, String effect, String feasibility, String cost, String result, User expert) {
        this.processId = processId;
        this.item = item;
        this.type = type;
        this.effect = effect;
        this.feasibility = feasibility;
        this.cost = cost;
        this.result = result;
        this.expert = expert;
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
    @JoinColumn(name="item_id")

    public MindItem getItem() {
        return this.item;
    }
    
    public void setItem(MindItem item) {
        this.item = item;
    }
    
    @Column(name="type", length=32)

    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @Column(name="effect", length=32)

    public String getEffect() {
        return this.effect;
    }
    
    public void setEffect(String effect) {
        this.effect = effect;
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

    @ManyToOne
    @JoinColumn(name="expert")
	public User getExpert() {
	
		return expert;
	}


	
	public void setExpert(User expert) {
	
		this.expert = expert;
	}
   








}