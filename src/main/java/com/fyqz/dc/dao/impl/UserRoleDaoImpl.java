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

import com.fyqz.dc.dao.UserRoleDao;
import com.fyqz.dc.entity.Role;


/**  
 * @description: [描述信息]
 * @fileName： com.fyqz.dc.dao.impl.UserRoleDaoImpl.java 
 * @createTime: 2014-6-6下午2:10:46
 * @creater: [夏选瑜]
 * @editTime： 2014-6-6下午2:10:46
 * @modifier: [夏选瑜]  
 */
@Repository
public class UserRoleDaoImpl extends BaseDaoImpl<Role> implements UserRoleDao {

	@SuppressWarnings("unchecked")
	public boolean hasRoleName(String name) throws Exception {

		final String hql = "select count(id) from com.fyqz.dc.entity.Role where name=:name";
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		List<Integer> countList = getHibernateTemplate().execute(
				new HibernateCallback<List<Integer>>() {

					public List<Integer> doInHibernate(Session session) throws HibernateException,
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
		return (null != countList && countList.size() > 0 && Integer.parseInt(countList.get(0)
				.toString()) > 0) ? true : false;
	}

	@SuppressWarnings("unchecked")
	public int getRoleCount() throws Exception {

		List<Integer> list = this.getHibernateTemplate().find(
				"select count(id) from com.fyqz.dc.entity.Role");
		return (null != list && list.size() > 0) ? Integer.parseInt(list.get(0).toString()) : 0;
	}

	public void deleteRoleByNo(String roleNo) throws Exception {

		final String hql = "delete from Role ro where ro.roleNo=:roleNo";
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleNo", roleNo);
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

	@SuppressWarnings("unchecked")
	public Role getRoleByNo(String roleNo) throws Exception {

		final String hql = "from Role ro where ro.roleNo=:roleNo";
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleNo", roleNo);
		List<Role> roleList = getHibernateTemplate().execute(new HibernateCallback<List<Role>>() {

			public List<Role> doInHibernate(Session session) throws HibernateException,
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
		if (0 < roleList.size()) {
			return roleList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Role> getAllFixedRole() throws Exception {

		final String hql = "from Role ro where ro.type=:type";
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "fixed");
		return getHibernateTemplate().execute(new HibernateCallback<List<Role>>() {

			public List<Role> doInHibernate(Session session) throws HibernateException,
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
	}

	@SuppressWarnings("unchecked")
	public List<Role> getAllTempRole() throws Exception {

		final String hql = "from Role ro where ro.type=:type";
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "temp");
		return getHibernateTemplate().execute(new HibernateCallback<List<Role>>() {

			public List<Role> doInHibernate(Session session) throws HibernateException,
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
	}
}
