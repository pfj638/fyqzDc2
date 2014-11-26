package com.fyqz.dc.entity;
// default package

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * Mind entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_CMS_MIND")

public class Mind  implements java.io.Serializable {


    // Fields    

     private String id;
     private String mindId;
     private MindItem root;


    // Constructors

    /** default constructor */
    public Mind() {
    }

    
    /** full constructor */
    public Mind(String mindId, MindItem root) {
        this.mindId = mindId;
        this.root = root;
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
    
    @Column(name="mind_id", length=32)

    public String getMindId() {
        return this.mindId;
    }
    
    public void setMindId(String mindId) {
        this.mindId = mindId;
    }
    @OneToOne
    @JoinColumn(name="root")

    public MindItem getRoot() {
        return this.root;
    }
    
    public void setRoot(MindItem root) {
        this.root = root;
    }
   

}