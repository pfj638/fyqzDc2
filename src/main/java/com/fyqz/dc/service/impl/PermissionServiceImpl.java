package com.fyqz.dc.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fyqz.dc.common.FYQZContext;
import com.fyqz.dc.dao.PermissionDao;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.dto.PermissionForm;
import com.fyqz.dc.dto.RoleForm;
import com.fyqz.dc.entity.Permission;
import com.fyqz.dc.service.PermissionService;

/**
 * @file:   com.fyqz.dc.service.impl.PermissionServiceImpl.java
 * @brief:   文件描述
 * @author:   Administrator
 * @date: 2014-8-25下午4:25:08
 * @version: 大成1.0
 */
@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionDao permissionDao;

	public Object queryByPage(FYQZContext fyqzContext, PermissionForm permissionForm, Page page)
			throws Exception {

		Permission permission = new Permission();
		BeanUtils.copyProperties(permissionForm, permission, new String[] {});
		return permissionDao.queryByPage(page, permission);
	}

	public Permission queryPermission(FYQZContext fyqzContext, String id) throws Exception {

		return permissionDao.get(id);
	}

	public void addPermission(FYQZContext fyqzContext, PermissionForm permissionForm)
			throws Exception {

		Permission permission = new Permission();
		BeanUtils.copyProperties(permissionForm, permission);
		permissionDao.save(permission);
	}

	public void updatePermission(FYQZContext fyqzContext, PermissionForm permissionForm)
			throws Exception {

		Permission permission = permissionDao.get(permissionForm.getPermissionId());
		BeanUtils.copyProperties(permissionForm, permission);
		permissionDao.update(permission);
	}

	public void deletePermission(FYQZContext fyqzContext, String permissionId) throws Exception {

		Permission permission = permissionDao.get(permissionId);
		permissionDao.delete(permission);
	}

	public List<Permission> queryAllPermission(FYQZContext fyqzContext) throws Exception {

		return permissionDao.loadAll();
	}
}
