package com.fyqz.dc.entity;
// default package

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * MindItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="DC_CMS_MIND_ITEM")

public class MindItem  implements java.io.Serializable {


    // Fields    

     private String id;
     private String itemId;
     private Mind mind;
     private String parentId;
     private String side;
     private String text;
     private String color;
     private String nodeColor;
     private String fontColor;
     private String tag;
     private String comment;
     private String value;
     private String status;
     private String collapsed;
     private String layout;
     private String shape;
     private String level;
     private List<MindItem> children = null;


    // Constructors

    /** default constructor */
    public MindItem() {
    }

    
    /** full constructor */
    public MindItem(String itemId, Mind mind, String parentId, String side, String text, String color, String nodeColor, String fontColor, String tag, String comment, String value, String status, String collapsed, String layout, String shape, String level) {
        this.itemId = itemId;
        this.mind = mind;
        this.parentId = parentId;
        this.side = side;
        this.text = text;
        this.color = color;
        this.nodeColor = nodeColor;
        this.fontColor = fontColor;
        this.tag = tag;
        this.comment = comment;
        this.value = value;
        this.status = status;
        this.collapsed = collapsed;
        this.layout = layout;
        this.shape = shape;
        this.level = level;
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
    
    @Column(name="item_id", length=32)

    public String getItemId() {
        return this.itemId;
    }
    
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    @ManyToOne
    @JoinColumn(name="mind_id")

    public Mind getMind() {
        return this.mind;
    }
    
    public void setMind(Mind mind) {
        this.mind = mind;
    }
    
    @Column(name="parent_id", length=32)

    public String getParentId() {
        return this.parentId;
    }
    
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    
    @Column(name="side", length=32)

    public String getSide() {
        return this.side;
    }
    
    public void setSide(String side) {
        this.side = side;
    }
    
    @Column(name="text", length=32)

    public String getText() {
        return this.text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    @Column(name="color", length=32)

    public String getColor() {
        return this.color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    @Column(name="node_color", length=32)

    public String getNodeColor() {
        return this.nodeColor;
    }
    
    public void setNodeColor(String nodeColor) {
        this.nodeColor = nodeColor;
    }
    
    @Column(name="font_color", length=32)

    public String getFontColor() {
        return this.fontColor;
    }
    
    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }
    
    @Column(name="tag", length=32)

    public String getTag() {
        return this.tag;
    }
    
    public void setTag(String tag) {
        this.tag = tag;
    }
    
    @Column(name="comment", length=32)
	public String getComment() {
	
		return comment;
	}


	
	public void setComment(String comment) {
	
		this.comment = comment;
	}


	@Column(name="value", length=32)

    public String getValue() {
        return this.value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    @Column(name="status", length=32)

    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Column(name="collapsed", length=32)

    public String getCollapsed() {
        return this.collapsed;
    }
    
    public void setCollapsed(String collapsed) {
        this.collapsed = collapsed;
    }
    
    @Column(name="layout", length=32)

    public String getLayout() {
        return this.layout;
    }
    
    public void setLayout(String layout) {
        this.layout = layout;
    }
    
    @Column(name="shape", length=32)

    public String getShape() {
        return this.shape;
    }
    
    public void setShape(String shape) {
        this.shape = shape;
    }
    
    @Column(name="level", length=32)

    public String getLevel() {
        return this.level;
    }
    
    public void setLevel(String level) {
        this.level = level;
    }


	@OneToMany
	@JoinTable(name = "dc_cms_parent_child", joinColumns = { @JoinColumn(name = "parent_id") }, inverseJoinColumns = { @JoinColumn(name = "child_id") })
	public List<MindItem> getChildren() {
	
		return children;
	}


	
	public void setChildren(List<MindItem> children) {
	
		this.children = children;
	}
   








}