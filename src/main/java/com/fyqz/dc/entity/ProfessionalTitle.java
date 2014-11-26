package com.fyqz.dc.entity;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


/**
 * ProfessionalTitle entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_BMS_PROFESSIONAL_TITLE")

public class ProfessionalTitle  implements java.io.Serializable {


    // Fields    

     private String id;
     private String name;


    // Constructors

    /** default constructor */
    public ProfessionalTitle() {
    }

    
    /** full constructor */
    public ProfessionalTitle(String name) {
        this.name = name;
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
    
    @Column(name="name", length=32)

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
   








}