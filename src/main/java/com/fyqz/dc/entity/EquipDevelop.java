package com.fyqz.dc.entity;
// default package

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;


/**
 * EquipDevelop entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_CMS_EQUIP_DEVELOP")

public class EquipDevelop  implements java.io.Serializable {


    // Fields    

     private String id;
     private String itemId;
     private String processId;
     private Discuss discuss;
     private String creator;
     private Date createTime;
     private String flag;
     private String state;
     private String validity;
     private String validityKnow;
     private String technology;
     private String technologyKnow;
     private String cost;
     private String costKnow;
     private Score score;


    // Constructors

    /** default constructor */
    public EquipDevelop() {
    }

    
    /** full constructor */
    public EquipDevelop(String itemId, String processId, Discuss discuss, String creator, Date createTime, String flag, String state, String validity, String validityKnow, String technology, String technologyKnow, String cost, String costKnow, Score score) {
        this.itemId = itemId;
        this.processId = processId;
        this.discuss = discuss;
        this.creator = creator;
        this.createTime = createTime;
        this.flag = flag;
        this.state = state;
        this.validity = validity;
        this.validityKnow = validityKnow;
        this.technology = technology;
        this.technologyKnow = technologyKnow;
        this.cost = cost;
        this.costKnow = costKnow;
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
    
    @Column(name="item_id", length=32)

    public String getItemId() {
        return this.itemId;
    }
    
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    
    @Column(name="process_id", length=32)

    public String getProcessId() {
        return this.processId;
    }
    
    public void setProcessId(String processId) {
        this.processId = processId;
    }
    @ManyToOne
    @JoinColumn(name="discuss_Id")

    public Discuss getDiscuss() {
        return this.discuss;
    }
    
    public void setDiscuss(Discuss discuss) {
        this.discuss = discuss;
    }
    
    @Column(name="creator", length=32)

    public String getCreator() {
        return this.creator;
    }
    
    public void setCreator(String creator) {
        this.creator = creator;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="create_time", length=10)

    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    @Column(name="flag", length=32)

    public String getFlag() {
        return this.flag;
    }
    
    public void setFlag(String flag) {
        this.flag = flag;
    }
    
    @Column(name="state", length=32)

    public String getState() {
        return this.state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    @Column(name="validity", length=32)

    public String getValidity() {
        return this.validity;
    }
    
    public void setValidity(String validity) {
        this.validity = validity;
    }
    
    @Column(name="validity_know", length=32)

    public String getValidityKnow() {
        return this.validityKnow;
    }
    
    public void setValidityKnow(String validityKnow) {
        this.validityKnow = validityKnow;
    }
    
    @Column(name="technology", length=32)

    public String getTechnology() {
        return this.technology;
    }
    
    public void setTechnology(String technology) {
        this.technology = technology;
    }
    
    @Column(name="technology_know", length=32)

    public String getTechnologyKnow() {
        return this.technologyKnow;
    }
    
    public void setTechnologyKnow(String technologyKnow) {
        this.technologyKnow = technologyKnow;
    }
    
    @Column(name="cost", length=32)

    public String getCost() {
        return this.cost;
    }
    
    public void setCost(String cost) {
        this.cost = cost;
    }
    
    @Column(name="cost_know", length=32)

    public String getCostKnow() {
        return this.costKnow;
    }
    
    public void setCostKnow(String costKnow) {
        this.costKnow = costKnow;
    }
    @OneToOne
    @JoinColumn(name="score_id")

    public Score getScore() {
        return this.score;
    }
    
    public void setScore(Score score) {
        this.score = score;
    }
   








}