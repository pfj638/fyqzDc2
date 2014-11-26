package com.fyqz.dc.dto;

import com.fyqz.dc.common.BaseForm;

/**
 * @file:   com.fyqz.dc.dto.PermissionForm.java
 * @brief:   文件描述
 * @author:   Administrator
 * @date: 2014-8-25下午4:39:36
 * @version: 大成1.0
 */
public class PermissionForm extends BaseForm {

	/**
	 * 权限主键id
	 */
	private String permissionId;
	/**
	 * 权限名称
	 */
	private String name;
	/**
	 * 权限值
	 */
	private Integer value;
	/**
	 * 权限描述
	 */
	private String permissionDesc;

	public String getPermissionId() {

		return permissionId;
	}

	public void setPermissionId(String permissionId) {

		this.permissionId = permissionId;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public Integer getValue() {

		return value;
	}

	public void setValue(Integer value) {

		this.value = value;
	}

	public String getPermissionDesc() {

		return permissionDesc;
	}

	public void setPermissionDesc(String permissionDesc) {

		this.permissionDesc = permissionDesc;
	}
}
