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
 * Disscuss entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_CMS_DISCUSS")

public class Discuss  implements java.io.Serializable {


    // Fields    

     private String id;
     private String processId;
     private String name;
     private String deployment;
     private Date insertDate;
     private String sponsor;
     private String address;
     private User proposer;/*研讨提出人*/
     private String discussDesc;
     private String status;
     private String opinion;/*授权意见*/
     private User checkUser;/*授权者*/
     private Date checkDate;/*授权时间*/


    // Constructors

    /** default constructor */
    public Discuss() {
    }

    
    /** full constructor */
    public Discuss(String processId, String name, String deployment, Date insertDate, String sponsor, String address, User proposer, String discussDesc, String status, String opinion) {
        this.processId = processId;
        this.name = name;
        this.deployment = deployment;
        this.insertDate = insertDate;
        this.sponsor = sponsor;
        this.address = address;
        this.proposer = proposer;
        this.discussDesc = discussDesc;
        this.status = status;
        this.opinion = opinion;
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
    
    @Column(name="name", length=32)

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="deployment", length=32)

    public String getDeployment() {
        return this.deployment;
    }
    
    public void setDeployment(String deployment) {
        this.deployment = deployment;
    }
    
    @Temporal(TemporalType.DATE)
    @Column(name="insert_date", length=10)

    public Date getInsertDate() {
        return this.insertDate;
    }
    
    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }
    
    @Column(name="sponsor", length=32)

    public String getSponsor() {
        return this.sponsor;
    }
    
    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }
    
    @Column(name="address", length=32)

    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    @ManyToOne
    @JoinColumn(name="proposer")

    public User getProposer() {
        return this.proposer;
    }
    
    public void setProposer(User proposer) {
        this.proposer = proposer;
    }
    
    @Column(name="discuss_desc", length=500)

    public String getDiscussDesc() {
        return this.discussDesc;
    }
    
    public void setDiscussDesc(String discussDesc) {
        this.discussDesc = discussDesc;
    }
    
    @Column(name="status", length=32)

    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Column(name="opinion", length=500)

    public String getOpinion() {
        return this.opinion;
    }
    
    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
    
    @ManyToOne
    @JoinColumn(name="check_user")
    
	public User getCheckUser() {

		return checkUser;
	}

	public void setCheckUser(User checkUser) {

		this.checkUser = checkUser;
	}
	
	@Temporal(TemporalType.DATE)
    @Column(name="check_date", length=10)
	
	public Date getCheckDate() {

		return checkDate;
	}

	public void setCheckDate(Date checkDate) {

		this.checkDate = checkDate;
	}
}