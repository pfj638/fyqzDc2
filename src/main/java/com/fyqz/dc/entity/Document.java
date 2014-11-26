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
 * Document entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_CMS_DOCUMENT")

public class Document  implements java.io.Serializable {


    // Fields    

     private String id;
     private String processId;
     private String type;
     private String name;
     private String path;
     private Date insertDate;
     private User creator;
     private String docDesc;


    // Constructors

    /** default constructor */
    public Document() {
    }

    
    /** full constructor */
    public Document(String processId, String type, String name, String path, Date insertDate, User creator, String docDesc) {
        this.processId = processId;
        this.type = type;
        this.name = name;
        this.path = path;
        this.insertDate = insertDate;
        this.creator = creator;
        this.docDesc = docDesc;
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
    
    @Column(name="type", length=32)

    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @Column(name="name", length=200)

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="path", length=200)

    public String getPath() {
        return this.path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="insert_date", length=10)

    public Date getInsertDate() {
        return this.insertDate;
    }
    
    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }
    @ManyToOne
    @JoinColumn(name="creator")

    public User getCreator() {
        return this.creator;
    }
    
    public void setCreator(User creator) {
        this.creator = creator;
    }
    
    @Column(name="doc_desc", length=500)

    public String getDocDesc() {
        return this.docDesc;
    }
    
    public void setDocDesc(String docDesc) {
        this.docDesc = docDesc;
    }
   








}