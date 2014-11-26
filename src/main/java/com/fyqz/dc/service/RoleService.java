package com.fyqz.dc.service;

import java.util.List;

import com.fyqz.dc.common.FYQZContext;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.dto.RoleForm;
import com.fyqz.dc.entity.Role;

/**
 * @file:   com.fyqz.dc.service.RoleService.java
 * @brief:   文件描述
 * @author:   Administrator
 * @date: 2014-8-25下午1:58:47
 * @version: 大成1.0
 */
public interface RoleService {

	/**
	 * 
	 * show: 分页查询角色
	 * @param fyqzContext
	 * @param roleForm
	 * @param page
	 * @return
	 * @throws Exception
	 * @exception
	 */
	public Object queryByPage(FYQZContext fyqzContext, RoleForm roleForm, Page page)
			throws Exception;

	/**
	 * 
	 * show: 根据角色id获取角色对象
	 * @param fyqzContext
	 * @param id
	 * @return
	 * @throws Exception
	 * @exception
	 */
	public Role queryRole(FYQZContext fyqzContext, String id) throws Exception;

	/**
	 * 
	 * show: 新增角色
	 * @param fyqzContext
	 * @param roleForm
	 * @throws Exception
	 * @exception
	 */
	public void addRole(FYQZContext fyqzContext, RoleForm roleForm) throws Exception;

	/**
	 * 
	 * show: 更新角色
	 * @param fyqzContext
	 * @param roleForm
	 * @throws Exception
	 * @exception
	 */
	public void updateRole(FYQZContext fyqzContext, RoleForm roleForm) throws Exception;
	
	/**
	 * 
	 * show: 删除角色
	 * @param fyqzContext
	 * @param roleId
	 * @throws Exception
	 * @exception
	 */
	public void deleteRole(FYQZContext fyqzContext, String roleId) throws Exception;
	
	/**
	 * 
	 * show: 查询所有角色
	 * @param fyqzContext
	 * @return
	 * @throws Exception
	 * @exception
	 */
	public List<Role> queryAllRole(FYQZContext fyqzContext) throws Exception;
	
	/**
	 * 
	 * show: 设置角色的权限
	 * @param fyqzContext
	 * @param roleForm
	 * @throws Exception
	 * @exception
	 */
	public void setupPermission(FYQZContext fyqzContext, RoleForm roleForm) throws Exception;
}
