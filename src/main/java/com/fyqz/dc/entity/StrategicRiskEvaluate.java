package com.fyqz.dc.entity;
// default package

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;


/**
 * StrategicRiskEvaluate entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_CMS_STRATEGIC_RISK_EVALUATE")

public class StrategicRiskEvaluate  implements java.io.Serializable {


    // Fields    

     private String id;
     private String nodeId;
     private String riskLv;
     private String riskLvName;
     private String taskId;
     private Discuss discuss;
     private User creator;
     private Date createTime;
     private String flag;


    // Constructors

    /** default constructor */
    public StrategicRiskEvaluate() {
    }

    
    /** full constructor */
    public StrategicRiskEvaluate(String nodeId, String riskLv, String riskLvName, String taskId, Discuss discuss, User creator, Date createTime, String flag) {
        this.nodeId = nodeId;
        this.riskLv = riskLv;
        this.riskLvName = riskLvName;
        this.taskId = taskId;
        this.discuss = discuss;
        this.creator = creator;
        this.createTime = createTime;
        this.flag = flag;
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
    
    @Column(name="node_id", length=32)

    public String getNodeId() {
        return this.nodeId;
    }
    
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    
    @Column(name="risk_lv", length=32)

    public String getRiskLv() {
        return this.riskLv;
    }
    
    public void setRiskLv(String riskLv) {
        this.riskLv = riskLv;
    }
    
    @Column(name="risk_lv_name", length=32)

    public String getRiskLvName() {
        return this.riskLvName;
    }
    
    public void setRiskLvName(String riskLvName) {
        this.riskLvName = riskLvName;
    }
    
    @Column(name="task_id", length=32)

    public String getTaskId() {
        return this.taskId;
    }
    
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    @ManyToOne
    @JoinColumn(name="discuss_id")

    public Discuss getDiscuss() {
        return this.discuss;
    }
    
    public void setDiscuss(Discuss discuss) {
        this.discuss = discuss;
    }
    @ManyToOne
    @JoinColumn(name="creator")

    public User getCreator() {
        return this.creator;
    }
    
    public void setCreator(User creator) {
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
   








}