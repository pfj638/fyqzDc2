package com.fyqz.dc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * DiscussMind entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_CMS_DISCUSS_MIND")

public class DiscussMind  implements java.io.Serializable {


    // Fields    

     private String id;
     private String processId;
     private Mind mind;
     private String step;
     private String type;
     private String path;


    // Constructors

    /** default constructor */
    public DiscussMind() {
    }

    
    /** full constructor */
    public DiscussMind(String processId, Mind mind, String step, String type, String path) {
        this.processId = processId;
        this.mind = mind;
        this.step = step;
        this.type = type;
        this.path = path;
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
    
    @OneToOne
    @JoinColumn(name="mind_id")

    public Mind getMind() {
        return this.mind;
    }
    
    public void setMind(Mind mind) {
        this.mind = mind;
    }
    
    @Column(name="step", length=32)

    public String getStep() {
        return this.step;
    }
    
    public void setStep(String step) {
        this.step = step;
    }
    
    @Column(name="type", length=32)

    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @Column(name="path", length=500)

    public String getPath() {
        return this.path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
   








}