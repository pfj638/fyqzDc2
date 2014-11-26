package com.fyqz.dc.entity;
// default package

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;


/**
 * HandleLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_BMS_HANDLE_LOG")

public class HandleLog  implements java.io.Serializable {


    // Fields    

     private String id;
     private String handler;
     private Date time;
     private String type;
     private String module;
     private String handleDesc;


    // Constructors

    /** default constructor */
    public HandleLog() {
    }

    
    /** full constructor */
    public HandleLog(String handler, Date time, String type, String module, String handleDesc) {
        this.handler = handler;
        this.time = time;
        this.type = type;
        this.module = module;
        this.handleDesc = handleDesc;
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
    
    @Column(name="handler", length=32)

    public String getHandler() {
        return this.handler;
    }
    
    public void setHandler(String handler) {
        this.handler = handler;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="time", length=10)

    public Date getTime() {
        return this.time;
    }
    
    public void setTime(Date time) {
        this.time = time;
    }
    
    @Column(name="type", length=32)

    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @Column(name="module", length=32)

    public String getModule() {
        return this.module;
    }
    
    public void setModule(String module) {
        this.module = module;
    }
    
    @Column(name="handle_desc", length=500)

    public String getHandleDesc() {
        return this.handleDesc;
    }
    
    public void setHandleDesc(String handleDesc) {
        this.handleDesc = handleDesc;
    }
   








}