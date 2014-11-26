package com.fyqz.dc.dao;

import java.util.List;

import com.fyqz.dc.dto.Page;
import com.fyqz.dc.entity.User;

/**
 * @description: [用户Dao接口]
 * @fileName： com.fyqz.dc.dao.UserDao.java
 * @createTime: 2014-6-6下午2:10:46
 * @creater: [夏选瑜]
 * @editTime： 2014-6-6下午2:10:46
 * @modifier: [夏选瑜]
 */
public interface UserDao extends BaseDao<User> {

	/**
	 * 
	 * @description: 登陆
	 * @editTime： 2014-6-9下午2:08:15
	 * @modifier: [修改人]  
	 * @param loginName
	 * @param password
	 * @return 登陆用户对象
	 * @throws Exception
	 */
	public User login(String loginName, String password) throws Exception;

	/**
	 * 
	 * @description: 检查用户名是否存在
	 * @editTime： 2014-6-9下午2:09:32
	 * @modifier: [修改人]  
	 * @param loginName
	 * @return true or false
	 * @throws Exception
	 */
	public boolean hasLoginName(String loginName) throws Exception;

	/**
	 * 
	 * @description: 分页检索用户
	 * @editTime： 2014-6-9下午2:09:54
	 * @modifier: [修改人]  
	 * @param page
	 * @param user
	 * @return 用户对象列表
	 * @throws Exception
	 */
	public Object queryByPage(Page page, User user) throws Exception;

	/**
	 * 
	 * @description: 功能描述
	 * @editTime： 2014-6-9下午2:10:12
	 * @modifier: [修改人]  
	 * @param loginName
	 * @throws Exception
	 */
	public void deleteUserByLoginName(String loginName) throws Exception;
}
