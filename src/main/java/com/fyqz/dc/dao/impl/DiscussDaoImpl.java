package com.fyqz.dc.dao.impl;  

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.fyqz.dc.dao.DiscussDao;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.entity.Discuss;
 
 
/**  
 * @description: [研讨dao实现类]
 * @fileName： com.fyqz.dc.dao.impl.DiscussDaoImpl.java 
 * @createTime: 2014-8-14上午10:15:48
 * @creater: [创建人]
 * @editTime： 2014-8-14上午10:15:48
 * @modifier: [修改人]  
 */
@Repository
public class DiscussDaoImpl extends BaseDaoImpl<Discuss> implements DiscussDao{
	
	public Object queryByPage(Discuss discuss, Page page) throws Exception{
		
		List<Discuss> discussList = null;
		StringBuffer from = new StringBuffer("from Discuss d");
		String orderBy = "";
		StringBuffer where = new StringBuffer(" where 1=1");
		//参数
		final List<Object> params = new ArrayList<Object>();
		if (null == page) {
			from = new StringBuffer("select count(*) from Discuss d ");
		}
		if (null != page && null != page.getOrderField()) {
			orderBy = page.getOrderField();
		}
		if (null != discuss && null != discuss.getName() && !("".equals(discuss.getName()))) {
			where.append(" and d.name like ?");
			params.add("%" + discuss.getName() + "%");
		}
		if (null != discuss && null != discuss.getStatus() && !("".equals(discuss.getStatus()))) {
			where.append(" and d.status = ?");
			params.add(discuss.getStatus());
		}
		StringBuffer hql = from.append(where).append(orderBy);
		if (null == page) {
			return queryTotleRows(hql.toString(), QUERY_TYPE_HQL, params);
		} else {
			discussList = queryByGeneric(page, hql.toString(), QUERY_TYPE_HQL, params);
			return discussList;
		}
	}
	
}
 