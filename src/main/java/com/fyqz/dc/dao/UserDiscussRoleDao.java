package com.fyqz.dc.dao;  

import java.util.List;

import com.fyqz.dc.entity.UserDiscussRole;
 
 
/**  
 * @description: [描述信息]
 * @fileName： com.fyqz.dc.dao.UserDiscussRoleDao.java 
 * @createTime: 2014-8-19上午9:22:38
 * @creater: [创建人]
 * @editTime： 2014-8-19上午9:22:38
 * @modifier: [修改人]  
 */
public interface UserDiscussRoleDao extends BaseDao<UserDiscussRole>{
	
	public List<UserDiscussRole> getByUser(String userId)throws Exception;
	public List<UserDiscussRole> getByDiscuss(String discussId)throws Exception;
	
	
}
 