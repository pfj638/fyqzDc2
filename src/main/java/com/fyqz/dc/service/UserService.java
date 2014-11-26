package com.fyqz.dc.service;  

import java.util.List;
import java.util.Map;

import com.fyqz.dc.common.FYQZContext;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.dto.UserForm;
import com.fyqz.dc.entity.User;
 
 
/**  
 * @description: [描述信息]
 * @fileName： com.fyqz.dc.service.UserService.java 
 * @createTime: 2014-8-15上午9:25:52
 * @creater: [创建人]
 * @editTime： 2014-8-15上午9:25:52
 * @modifier: [修改人]  
 */
public interface UserService {
	/**
	 * 
	 * @description: 判断用户登录是否成功
	 * @editTime： 2014-8-15上午9:38:14
	 * @modifier: [修改人]  
	 * @param loginName
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public User login(FYQZContext fyqzContext, String loginName, String password) throws Exception;
	
	/**
	 * 
	 * @description: 判断用户名是否存在
	 * @editTime： 2014-8-15上午9:45:01
	 * @modifier: [修改人]  
	 * @param loginName
	 * @return
	 * @throws Exception
	 */
	public boolean hasLoginName(FYQZContext fyqzContext, String loginName) throws Exception;
	
	/**
	 * 
	 * @description: 查询所有用户
	 * @editTime： 2014-8-18下午1:44:39
	 * @modifier: [修改人]  
	 * @return
	 * @throws Exception
	 */
	public List<User> queryAllUser(FYQZContext fyqzContext) throws Exception;
	
	/**
	 * 
	 * @description:根据用户id获取用户对象
	 * @editTime： 2014-8-19上午9:18:04
	 * @modifier: [修改人]  
	 * @return
	 * @throws Exception
	 */
	public User queryUser(FYQZContext fyqzContext, String id) throws Exception;
	
	/**
	 * 
	 * @description: 查找与用户表相关联的用户类型、职称、班级字典表
	 * @editTime： 2014-8-21下午1:39:33
	 * @modifier: [修改人]  
	 * @param fyqzContext
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryInfo(FYQZContext fyqzContext) throws Exception;
	
	/**
	 * 
	 * @description: 新增用户
	 * @editTime： 2014-8-21下午2:34:04
	 * @modifier: [修改人]  
	 * @param fyqzContext
	 * @param userForm
	 * @throws Exception
	 */
	public void addUser(FYQZContext fyqzContext, UserForm userForm) throws Exception;
	
	/**
	 * 
	 * @description: 分页查询用户
	 * @editTime： 2014-8-21下午4:09:53
	 * @modifier: [修改人]  
	 * @param fyqzContext
	 * @param userForm
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public Object queryByPage(FYQZContext fyqzContext, UserForm userForm, Page page) throws Exception;
	
	/**
	 * 
	 * show: 更新用户信息
	 * @param fyqzContext
	 * @param userForm
	 * @throws Exception
	 * @exception
	 */
	public void updateUser(FYQZContext fyqzContext, UserForm userForm) throws Exception;
	
	/**
	 * 
	 * show: 删除用户
	 * @param fyqzContext
	 * @param userId
	 * @throws Exception
	 * @exception
	 */
	public void deleteUser(FYQZContext fyqzContext, String userId) throws Exception;
	
	/**
	 * 
	 * show: 设置用户角色
	 * @param fyqzContext
	 * @param userForm
	 * @throws Exception
	 * @exception
	 */
	public void setupRole(FYQZContext fyqzContext, UserForm userForm) throws Exception;
}
 