package com.fyqz.dc.service;

import java.util.List;

import com.fyqz.dc.common.FYQZContext;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.dto.PermissionForm;
import com.fyqz.dc.entity.Permission;

/**
 * @file:   com.fyqz.dc.service.PermissionService.java
 * @brief:   文件描述
 * @author:   Administrator
 * @date: 2014-8-25下午4:24:57
 * @version: 大成1.0
 */
public interface PermissionService {

	/**
	 * 
	 * show: 分页查询权限
	 * @param fyqzContext
	 * @param permissionForm
	 * @param page
	 * @return
	 * @throws Exception
	 * @exception
	 */
	public Object queryByPage(FYQZContext fyqzContext, PermissionForm permissionForm, Page page)
			throws Exception;

	/**
	 * 
	 * show: 根据权限id获取权限对象
	 * @param fyqzContext
	 * @param id
	 * @return
	 * @throws Exception
	 * @exception
	 */
	public Permission queryPermission(FYQZContext fyqzContext, String id) throws Exception;

	/**
	 * 
	 * show: 新增权限
	 * @param fyqzContext
	 * @param roleForm
	 * @throws Exception
	 * @exception
	 */
	public void addPermission(FYQZContext fyqzContext, PermissionForm permissionForm)
			throws Exception;

	/**
	 * 
	 * show: 更新权限
	 * @param fyqzContext
	 * @param roleForm
	 * @throws Exception
	 * @exception
	 */
	public void updatePermission(FYQZContext fyqzContext, PermissionForm permissionForm)
			throws Exception;
	
	/**
	 * 
	 * show: 删除权限
	 * @param fyqzContext
	 * @param permissionFormId
	 * @throws Exception
	 * @exception
	 */
	public void deletePermission(FYQZContext fyqzContext, String permissionId) throws Exception;
	
	/**
	 * 
	 * show: 查询所有权限
	 * @param fyqzContext
	 * @return
	 * @throws Exception
	 * @exception
	 */
	public List<Permission> queryAllPermission(FYQZContext fyqzContext) throws Exception;
}
