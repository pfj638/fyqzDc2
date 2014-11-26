package com.fyqz.dc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.fyqz.dc.dao.PermissionDao;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.entity.Permission;

/**
 * @file:   com.fyqz.dc.dao.impl.PermissionDaoImpl.java
 * @brief:   文件描述
 * @author:   Administrator
 * @date: 2014-8-25下午4:21:00
 * @version: 大成1.0
 */
@Repository
public class PermissionDaoImpl extends BaseDaoImpl<Permission> implements PermissionDao {

	public Object queryByPage(Page page, Permission permission) throws Exception {

		List<Permission> permissionList = null;
		StringBuffer from = new StringBuffer("from Permission p");
		StringBuffer where = new StringBuffer(" where 1=1 ");
		String orderBy = "";
		if (null == page) {
			from = new StringBuffer("select count(*) from Permission p ");
		}
		if (null != page && null != page.getOrderField()) {
			orderBy = page.getOrderField();
		}
		final List<Object> param = new ArrayList<Object>();
		if (null != permission && permission.getName() != null) {
			where.append(" and p.name like ?");
			param.add("%" + permission.getName() + "%");
		}
		StringBuffer hql = from.append(where).append(orderBy);
		if (null == page) {
			return queryTotleRows(hql.toString(), QUERY_TYPE_HQL, param);
		} else {
			permissionList = queryByGeneric(page, hql.toString(), QUERY_TYPE_HQL, param);
			return permissionList;
		}
	}
}
