package com.fyqz.dc.dto;

import com.fyqz.dc.entity.User;

/**
 * @file:   com.fyqz.dc.dto.DiscussManager.java 
 * @brief:   案例研讨-service 参数传递对象
 * @author:   林明阳
 * @date: 2014-8-22下午4:03:30
 * @version: 大成1.0
 */
public class DiscussManager {

	/**登陆用户对象*/
	private User user;
	/**案例研讨状态*/
	private String status;
	/**案例研讨类型*/
	private String type;

	public DiscussManager() {

	}

	public DiscussManager(User user, String status, String type) {

		super();
		this.user = user;
		this.status = status;
		this.type = type;
	}

	public User getUser() {

		return user;
	}

	public void setUser(User user) {

		this.user = user;
	}

	public String getStatus() {

		return status;
	}

	public void setStatus(String status) {

		this.status = status;
	}

	public String getType() {

		return type;
	}

	public void setType(String type) {

		this.type = type;
	}
}
