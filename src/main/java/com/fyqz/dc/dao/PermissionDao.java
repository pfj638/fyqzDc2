package com.fyqz.dc.dao;

import com.fyqz.dc.dto.Page;
import com.fyqz.dc.entity.Permission;

/**
 * @file:   com.fyqz.dc.dao.PermissionDao.java
 * @brief:   文件描述
 * @author:   Administrator
 * @date: 2014-8-25下午4:20:13
 * @version: 大成1.0
 */
public interface PermissionDao extends BaseDao<Permission> {

	/**
	 * 
	 * show:分页检索权限
	 * @param page
	 * @param permission
	 * @return
	 * @throws Exception
	 * @exception
	 */
	public Object queryByPage(Page page, Permission permission) throws Exception;
}
