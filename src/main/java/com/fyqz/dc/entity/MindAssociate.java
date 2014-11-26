package com.fyqz.dc.entity;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * MindAssociate entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_CMS_MIND_ASSOCIATE")

public class MindAssociate  implements java.io.Serializable {


    // Fields    

     private String id;
     private String associateId;
     private String fromId;
     private String toId;
     private String fromSide;
     private String toSide;
     private String color;
     private String positionPoints;
     private Mind mind;


    // Constructors

    /** default constructor */
    public MindAssociate() {
    }

    
    /** full constructor */
    public MindAssociate(String associateId, String fromId, String toId, String fromSide, String toSide, String color, String positionPoints, Mind mind) {
        this.associateId = associateId;
        this.fromId = fromId;
        this.toId = toId;
        this.fromSide = fromSide;
        this.toSide = toSide;
        this.color = color;
        this.positionPoints = positionPoints;
        this.mind = mind;
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
    
    @Column(name="associate_id", length=32)

    public String getAssociateId() {
        return this.associateId;
    }
    
    public void setAssociateId(String associateId) {
        this.associateId = associateId;
    }
    
    @Column(name="from_id", length=32)

    public String getFromId() {
        return this.fromId;
    }
    
    public void setFromId(String fromId) {
        this.fromId = fromId;
    }
    
    @Column(name="to_id", length=32)

    public String getToId() {
        return this.toId;
    }
    
    public void setToId(String toId) {
        this.toId = toId;
    }
    
    @Column(name="from_side", length=32)

    public String getFromSide() {
        return this.fromSide;
    }
    
    public void setFromSide(String fromSide) {
        this.fromSide = fromSide;
    }
    
    @Column(name="to_side", length=32)

    public String getToSide() {
        return this.toSide;
    }
    
    public void setToSide(String toSide) {
        this.toSide = toSide;
    }
    
    @Column(name="color", length=32)

    public String getColor() {
        return this.color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    @Column(name="position_points", length=500)

    public String getPositionPoints() {
        return this.positionPoints;
    }
    
    public void setPositionPoints(String positionPoints) {
        this.positionPoints = positionPoints;
    }
    @ManyToOne
    @JoinColumn(name="mind_id")

    public Mind getMind() {
        return this.mind;
    }
    
    public void setMind(Mind mind) {
        this.mind = mind;
    }
   








}