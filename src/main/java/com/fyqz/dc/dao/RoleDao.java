package com.fyqz.dc.dao;

import com.fyqz.dc.dto.Page;
import com.fyqz.dc.entity.Role;

/**
 * @file:   com.fyqz.dc.dao.RoleDao.java
 * @brief:   文件描述
 * @author:   Administrator
 * @date: 2014-8-25下午2:00:26
 * @version: 大成1.0
 */
public interface RoleDao extends BaseDao<Role> {

	/**
	 * 
	 * show: 分页检索角色
	 * @param page
	 * @param role
	 * @return
	 * @throws Exception
	 * @exception
	 */
	public Object queryByPage(Page page, Role role) throws Exception;
}
