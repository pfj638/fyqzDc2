package com.fyqz.dc.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fyqz.dc.common.FYQZContext;
import com.fyqz.dc.dao.RoleDao;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.dto.RoleForm;
import com.fyqz.dc.entity.Role;
import com.fyqz.dc.service.RoleService;

/**
 * @file:   com.fyqz.dc.service.impl.RoleServiceImpl.java
 * @brief:   文件描述
 * @author:   Administrator
 * @date: 2014-8-25下午1:59:24
 * @version: 大成1.0
 */
@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;

	public Object queryByPage(FYQZContext fyqzContext, RoleForm roleForm, Page page)
			throws Exception {

		Role role = new Role();
		BeanUtils.copyProperties(roleForm, role, new String[] {});
		return roleDao.queryByPage(page, role);
	}

	public Role queryRole(FYQZContext fyqzContext, String id) throws Exception {

		return roleDao.get(id);
	}

	public void addRole(FYQZContext fyqzContext, RoleForm roleForm) throws Exception {

		Role role = new Role();
		BeanUtils.copyProperties(roleForm, role);
		roleDao.save(role);
	}

	public void updateRole(FYQZContext fyqzContext, RoleForm roleForm) throws Exception {

		Role role = roleDao.get(roleForm.getRoleId());
		BeanUtils.copyProperties(roleForm, role);
		roleDao.update(role);
	}

	public void deleteRole(FYQZContext fyqzContext, String roleId) throws Exception {

		Role role = roleDao.get(roleId);
		roleDao.delete(role);
	}

	public List<Role> queryAllRole(FYQZContext fyqzContext) throws Exception {
		
		return roleDao.loadAll();
	}

	public void setupPermission(FYQZContext fyqzContext, RoleForm roleForm) throws Exception {

		Role role = roleDao.get(roleForm.getRoleId());
		role.setPermission(roleForm.getPermission());
		roleDao.save(role);
	}
	
	
}
