package com.fyqz.dc.entity;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


/**
 * Permission entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_BMS_PERMISSION")

public class Permission  implements java.io.Serializable {


    // Fields    

     private String id;
     private String name;
     private Integer value;
     private String permissionDesc;


    // Constructors

    /** default constructor */
    public Permission() {
    }

    
    /** full constructor */
    public Permission(String name, Integer value, String permissionDesc) {
        this.name = name;
        this.value = value;
        this.permissionDesc = permissionDesc;
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
    
    @Column(name="value")

    public Integer getValue() {
        return this.value;
    }
    
    public void setValue(Integer value) {
        this.value = value;
    }
    
    @Column(name="permission_desc", length=500)

    public String getPermissionDesc() {
        return this.permissionDesc;
    }
    
    public void setPermissionDesc(String permissionDesc) {
        this.permissionDesc = permissionDesc;
    }
   








}