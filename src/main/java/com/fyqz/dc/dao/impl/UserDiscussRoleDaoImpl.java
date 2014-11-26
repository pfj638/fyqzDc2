package com.fyqz.dc.dao.impl;  

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.fyqz.dc.dao.UserDiscussRoleDao;
import com.fyqz.dc.entity.UserDiscussRole;
 
 
/**  
 * @description: [描述信息]
 * @fileName： com.fyqz.dc.dao.impl.UserDiscussRoleDaoImpl.java 
 * @createTime: 2014-8-19上午9:26:05
 * @creater: [创建人]
 * @editTime： 2014-8-19上午9:26:05
 * @modifier: [修改人]  
 */
@Repository
public class UserDiscussRoleDaoImpl extends BaseDaoImpl<UserDiscussRole> implements UserDiscussRoleDao{

	@SuppressWarnings("unchecked")
	public List<UserDiscussRole> getByUser(String userId) throws Exception {
		final String hql = "from UserDiscussRole udr where udr.user.id=:userId";
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		List<UserDiscussRole> userDiscussRoleList = getHibernateTemplate().execute(
				new HibernateCallback<List<UserDiscussRole>>() {

					public List<UserDiscussRole> doInHibernate(Session session)
							throws HibernateException, SQLException {

						Query query = session.createQuery(hql);
						for (Map.Entry<String, Object> entry : params.entrySet()) {
							String key = entry.getKey();
							Object obj = entry.getValue();
							if (obj instanceof List) {
								query.setParameterList(key, (List) obj);
							} else {
								query.setParameter(key, obj);
							}
						}
						return query.list();
					}
				});
		return userDiscussRoleList;
	}

	@SuppressWarnings("unchecked")
	public List<UserDiscussRole> getByDiscuss(String discussId) throws Exception {
		final String hql = "from UserDiscussRole udr where udr.discuss.id=:discussId";
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("discussId", discussId);
		List<UserDiscussRole> userDiscussRoleList = getHibernateTemplate().execute(
				new HibernateCallback<List<UserDiscussRole>>() {

					public List<UserDiscussRole> doInHibernate(Session session)
							throws HibernateException, SQLException {

						Query query = session.createQuery(hql);
						for (Map.Entry<String, Object> entry : params.entrySet()) {
							String key = entry.getKey();
							Object obj = entry.getValue();
							if (obj instanceof List) {
								query.setParameterList(key, (List) obj);
							} else {
								query.setParameter(key, obj);
							}
						}
						return query.list();
					}
				});
		return userDiscussRoleList;
	}
	
}
 