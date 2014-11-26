package com.fyqz.dc.entity;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


/**
 * DisscussRole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_CMS_DISCUSS_ROLE")

public class DiscussRole  implements java.io.Serializable {


    // Fields    

     private String id;
     private String name;
     private Integer permission;
     private String roleDesc;


    // Constructors

    /** default constructor */
    public DiscussRole() {
    }

    
    /** full constructor */
    public DiscussRole(String name, Integer permission, String roleDesc) {
        this.name = name;
        this.permission = permission;
        this.roleDesc = roleDesc;
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
    
    @Column(name="permission")

    public Integer getPermission() {
        return this.permission;
    }
    
    public void setPermission(Integer permission) {
        this.permission = permission;
    }
    
    @Column(name="role_desc", length=500)

    public String getRoleDesc() {
        return this.roleDesc;
    }
    
    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }
   








}