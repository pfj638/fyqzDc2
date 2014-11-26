package com.fyqz.dc.entity;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * User entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_BMS_USER")

public class User  implements java.io.Serializable {


    // Fields    

     private String id;
     private String loginName;
     private String password;
     private String realName;
     private Integer permission;
     private String sex;
     private UserType type;
     private String year;
     private String jobTitle;
     private ProfessionalTitle professionalTitle;
     private String email;
     private Grade grade;
     private Role role;


    // Constructors

    /** default constructor */
    public User() {
    }


    /**
     *　@author 林明阳
     * @param loginName 登录名称
     * @param password
     * @param realName
     * @param permission
     * @param sex
     * @param type
     * @param year
     * @param jobTitle
     * @param professionalTitle
     * @param email
     * @param grade
     * @param role
     *
     */
    public User(String loginName, String password, String realName, Integer permission, String sex, UserType type, String year, String jobTitle, ProfessionalTitle professionalTitle, String email, Grade grade, Role role) {
        this.loginName = loginName;
        this.password = password;
        this.realName = realName;
        this.permission = permission;
        this.sex = sex;
        this.type = type;
        this.year = year;
        this.jobTitle = jobTitle;
        this.professionalTitle = professionalTitle;
        this.email = email;
        this.grade = grade;
        this.role = role;
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
    
    @Column(name="login_name", length=16)

    public String getLoginName() {
        return this.loginName;
    }
    
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    
    @Column(name="password", length=32)

    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Column(name="real_name", length=32)

    public String getRealName() {
        return this.realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    @Column(name="permission")

    public Integer getPermission() {
        return this.permission;
    }
    
    public void setPermission(Integer permission) {
        this.permission = permission;
    }
    
    @Column(name="sex", length=32)

    public String getSex() {
        return this.sex;
    }
    
    public void setSex(String sex) {
        this.sex = sex;
    }
    @OneToOne
    @JoinColumn(name="type")

    public UserType getType() {
        return this.type;
    }
    
    public void setType(UserType type) {
        this.type = type;
    }
    
    @Column(name="year", length=32)

    public String getYear() {
        return this.year;
    }
    
    public void setYear(String year) {
        this.year = year;
    }
    
    @Column(name="job_title", length=32)

    public String getJobTitle() {
        return this.jobTitle;
    }
    
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    @OneToOne
    @JoinColumn(name="professional_title")

    public ProfessionalTitle getProfessionalTitle() {
        return this.professionalTitle;
    }
    
    public void setProfessionalTitle(ProfessionalTitle professionalTitle) {
        this.professionalTitle = professionalTitle;
    }
    
    @Column(name="email", length=32)

    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    @OneToOne
    @JoinColumn(name="grade")

    public Grade getGrade() {
        return this.grade;
    }
    
    public void setGrade(Grade grade) {
        this.grade = grade;
    }
    @OneToOne
    @JoinColumn(name="role_id")

    public Role getRole() {
        return this.role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
   








}