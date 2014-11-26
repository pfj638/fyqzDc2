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

import com.fyqz.dc.dao.DiscussRoleDao;
import com.fyqz.dc.entity.DiscussRole;
 
 
/**  
 * @description: [描述信息]
 * @fileName： com.fyqz.dc.dao.impl.DiscussRoleDaoImpl.java 
 * @createTime: 2014-8-19上午9:33:57
 * @creater: [创建人]
 * @editTime： 2014-8-19上午9:33:57
 * @modifier: [修改人]  
 */
@Repository
public class DiscussRoleDaoImpl extends BaseDaoImpl<DiscussRole> implements DiscussRoleDao{
	
	@SuppressWarnings("unchecked")
	public DiscussRole getByName(String name) throws Exception {

		final String hql = "from DiscussRole dr where dr.name=:name";
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		List<DiscussRole> discussRoleList = getHibernateTemplate().execute(
				new HibernateCallback<List<DiscussRole>>() {

					public List<DiscussRole> doInHibernate(Session session)
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
		if (0 < discussRoleList.size()) {
			return discussRoleList.get(0);
		}
		return null;
	}
}
 