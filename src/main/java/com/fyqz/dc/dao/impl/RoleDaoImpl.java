package com.fyqz.dc.dao.impl;  

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.fyqz.dc.dao.RoleDao;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.entity.Role;

/**
 * @file:   com.fyqz.dc.dao.impl.RoleDaoImpl.java
 * @brief:   文件描述
 * @author:   Administrator
 * @date: 2014-8-25下午2:01:09
 * @version: 大成1.0
 */
@Repository
public class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDao{
	
	public Object queryByPage(Page page, Role role) throws Exception{
		List<Role> roleList = null;
		StringBuffer from = new StringBuffer("from Role r");
		StringBuffer where = new StringBuffer(" where 1=1 ");
		String orderBy = "";
		if (null == page) {
			from = new StringBuffer("select count(*) from Role r ");
		}
		if (null != page && null != page.getOrderField()) {
			orderBy = page.getOrderField();
		}
		final List<Object> param = new ArrayList<Object>();
		if (null != role && role.getName() != null) {
			where.append(" and r.name like ?");
			param.add("%" + role.getName() + "%");
		}
		StringBuffer hql = from.append(where).append(orderBy);
		if (null == page) {
			return queryTotleRows(hql.toString(), QUERY_TYPE_HQL, param);
		} else {
			roleList = queryByGeneric(page, hql.toString(), QUERY_TYPE_HQL, param);
			return roleList;
		}
	}
}
 