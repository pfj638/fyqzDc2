package com.fyqz.dc.dao;

import java.util.List;

import com.fyqz.dc.entity.Role;


/**  
 * @description: [描述信息]
 * @fileName： com.fyqz.dc.dao.impl.UserRoleDao.java 
 * @createTime: 2014-6-6下午2:10:46
 * @creater: [夏选瑜]
 * @editTime： 2014-6-6下午2:10:46
 * @modifier: [夏选瑜]  
 */
public interface UserRoleDao extends BaseDao<Role> {
	/**
	 * 
	 * @description: 判断角色名是否存在
	 * @editTime： 2014-6-9下午4:10:10
	 * @modifier: [修改人]  
	 * @param name
	 * @return true存在 false不存在
	 * @throws Exception
	 */
	public boolean hasRoleName(String name) throws Exception;
	/**
	 * 
	 * @description: 根据角色No删除角色
	 * @editTime： 2014-6-9下午4:11:03
	 * @modifier: [修改人]  
	 * @param roleNo
	 * @throws Exception
	 */
	public void deleteRoleByNo(String roleNo) throws Exception;
	/**
	 * 
	 * @description: 根据角色No获取角色
	 * @editTime： 2014-6-9下午4:11:54
	 * @modifier: [修改人]  
	 * @param roleNo
	 * @return 角色对象
	 * @throws Exception
	 */
	public Role getRoleByNo(String roleNo) throws Exception;
	/**
	 * 
	 * @description: 获取所有固定的角色
	 * @editTime： 2014-6-9下午4:12:20
	 * @modifier: [修改人]  
	 * @return 角色对象列表
	 * @throws Exception
	 */
	public List<Role> getAllFixedRole() throws Exception;
	/**
	 * 
	 * @description: 获取所有的临时角色列表
	 * @editTime： 2014-6-9下午4:13:18
	 * @modifier: [修改人]  
	 * @return 角色对象列表
	 * @throws Exception
	 */
	public List<Role> getAllTempRole() throws Exception;

}
