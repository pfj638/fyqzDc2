package com.fyqz.dc.entity;  

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
 
 
/**  
 * @description: [描述信息]
 * @fileName： com.fyqz.dc.entity.UserDiscussRole.java 
 * @createTime: 2014-8-18上午10:39:52
 * @creater: [创建人]
 * @editTime： 2014-8-18上午10:39:52
 * @modifier: [修改人]  
 */
@Entity
@Table(name="DC_CMS_USER_DISCUSS_ROLE")
public class UserDiscussRole implements java.io.Serializable {

	private String id; //主键id
	private DiscussRole discussRole;/*角色*/
	private Discuss discuss;/*研讨案例*/
	private User user;/*用户*/
	// Constructors

    /** default constructor */
    public UserDiscussRole() {
    }

    
    /** full constructor */
    public UserDiscussRole(DiscussRole discussRole, Discuss discuss, User user) {
        this.discussRole = discussRole;
        this.discuss = discuss;
        this.user = user;
        
    }
    
	@GenericGenerator(name="generator", strategy="uuid.hex")@Id @GeneratedValue(generator="generator")
	@Column(name="id", unique=true, nullable=false, length=32)
	public String getId() {
	
		return id;
	}
	
	public void setId(String id) {
	
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "discuss_role_id")
	public DiscussRole getDiscussRole() {
	
		return discussRole;
	}
	
	public void setDiscussRole(DiscussRole discussRole) {
	
		this.discussRole = discussRole;
	}
	
	@ManyToOne
	@JoinColumn(name = "discuss_id")
	public Discuss getDiscuss() {
	
		return discuss;
	}
	
	public void setDiscuss(Discuss discuss) {
	
		this.discuss = discuss;
	}
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getUser() {
	
		return user;
	}
	
	public void setUser(User user) {
	
		this.user = user;
	}
	
	
}
 