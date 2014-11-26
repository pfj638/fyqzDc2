package com.fyqz.dc.entity;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


/**
 * UserType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_BMS_USER_TYPE")

public class UserType  implements java.io.Serializable {


    // Fields    

     private String id;
     private String name;


    // Constructors

    /** default constructor */
    public UserType() {
    }

    
    /** full constructor */
    public UserType(String name) {
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