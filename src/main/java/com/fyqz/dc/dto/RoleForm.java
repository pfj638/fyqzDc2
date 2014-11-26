package com.fyqz.dc.dto;

import com.fyqz.dc.common.BaseForm;

/**
 * @file:   com.fyqz.dc.dto.RoleForm.java
 * @brief:   文件描述
 * @author:   Administrator
 * @date: 2014-8-25下午1:51:43
 * @version: 大成1.0
 */
public class RoleForm extends BaseForm {

	/**
	 * 角色主键id
	 */
	private String roleId;
	/**
	 * 角色名称
	 */
	private String name;
	/**
	 * 角色权限值
	 */
	private Integer permission;
	/**
	 * 角色描述
	 */
	private String roleDesc;

	public String getRoleId() {

		return roleId;
	}

	public void setRoleId(String roleId) {

		this.roleId = roleId;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public Integer getPermission() {

		return permission;
	}

	public void setPermission(Integer permission) {

		this.permission = permission;
	}

	public String getRoleDesc() {

		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {

		this.roleDesc = roleDesc;
	}
}
