package com.fyqz.dc.dao;  

import com.fyqz.dc.entity.DiscussRole;
 
 
/**  
 * @description: [描述信息]
 * @fileName： com.fyqz.dc.dao.DiscussRoleDao.java 
 * @createTime: 2014-8-19上午9:33:18
 * @creater: [创建人]
 * @editTime： 2014-8-19上午9:33:18
 * @modifier: [修改人]  
 */
public interface DiscussRoleDao extends BaseDao<DiscussRole>{
	
	/**
	 * 
	 * @description: 根据研讨角色名获取研讨角色对象
	 * @editTime： 2014-8-19上午9:39:21
	 * @modifier: [修改人]  
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public DiscussRole getByName(String name) throws Exception;
}
 