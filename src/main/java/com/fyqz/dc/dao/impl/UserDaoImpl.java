package com.fyqz.dc.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.fyqz.dc.dao.UserDao;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.entity.User;

/**
 * @description: [用户Dao实体类]
 * @fileName： com.fyqz.dc.dao.impl.UserDaoImpl.java
 * @createTime: 2014-6-6下午2:10:46
 * @creater: [夏选瑜]
 * @editTime： 2014-6-6下午2:10:46
 * @modifier: [夏选瑜]
 */
@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {
	
	@SuppressWarnings("unchecked")
	public User login(String loginName, String password) throws Exception {

		final String hql = "from User where loginName=:loginName and password=:password";
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", loginName);
		params.put("password", password);
		List<User> userList = getHibernateTemplate().execute(new HibernateCallback<List<User>>() {

			public List<User> doInHibernate(Session session) throws HibernateException,
					SQLException {

				Query query = session.createQuery(hql);
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					String key = entry.getKey();
					Object obj = entry.getValue();
					query.setParameter(key, obj);
				}
				return query.list();
			}
		});
		return (null != userList && userList.size() > 0) ? userList.get(0) : null;
	}
	
	@SuppressWarnings("unchecked")
	public boolean hasLoginName(String loginName) throws Exception {

		final String hql = "from User where loginName=:loginName";
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", loginName);
		List<User> userList = getHibernateTemplate().execute(new HibernateCallback<List<User>>() {

			public List<User> doInHibernate(Session session) throws HibernateException,
					SQLException {

				Query query = session.createQuery(hql);
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					String key = entry.getKey();
					Object obj = entry.getValue();
					query.setParameter(key, obj);
				}
				return query.list();
			}
		});
		return (null != userList && userList.size() > 0);
	}
	
	@SuppressWarnings("unchecked")
	public Object queryByPage(final Page page, User user) {

		List<User> userList = null;
		StringBuffer from = new StringBuffer("from User u");
		StringBuffer where = new StringBuffer(" where 1=1 ");
		String orderBy = "";
		if (null == page) {
			from = new StringBuffer("select count(*) from User u ");
		}
		if (null != page && null != page.getOrderField()) {
			orderBy = page.getOrderField();
		}
		final List<Object> param = new ArrayList<Object>();
		if (null != user && user.getLoginName() != null) {
			where.append(" and u.loginName like ?");
			param.add("%" + user.getLoginName() + "%");
		}
		if (null != user && user.getRealName() != null) {
			where.append(" and u.realName like ?");
			param.add("%" + user.getRealName() + "%");
		}
		if (null != user && user.getType() != null) {
			where.append(" and u.type.id = ?");
			param.add(user.getType().getId());
		}
		if (null != user && user.getSex() != null && !user.getSex().equals("")) {
			where.append(" and u.sex = ?");
			param.add(user.getSex());
		}
		if (null != user && user.getGrade() != null) {
			where.append(" and u.grade.id = ?");
			param.add(user.getGrade().getId());
		}
		if (null != user && user.getProfessionalTitle() != null) {
			where.append(" and u.professionalTitle.id = ?");
			param.add(user.getProfessionalTitle().getId());
		}
		StringBuffer hql = from.append(where).append(orderBy);
		if (null == page) {
			return queryTotleRows(hql.toString(), QUERY_TYPE_HQL, param);
		} else {
			userList = queryByGeneric(page, hql.toString(), QUERY_TYPE_HQL, param);
			return userList;
		}
	}

	public void deleteUserByLoginName(String loginName) throws Exception {

		final String hql = "delete from User us where us.loginName=:loginName";
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", loginName);
		getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			public Integer doInHibernate(Session session) throws HibernateException, SQLException {

				Query query = session.createQuery(hql);
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					String key = entry.getKey();
					Object obj = entry.getValue();
					query.setParameter(key, obj);
				}
				return query.executeUpdate();
			}
		});
	}
}
