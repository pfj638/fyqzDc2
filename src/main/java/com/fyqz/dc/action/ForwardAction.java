package com.fyqz.dc.action;  

import com.fyqz.dc.common.BaseAction;
import com.fyqz.dc.common.performance.BusinessDesc;


/**  
 * @description: [用于页面简单跳转的action]
 * @fileName： com.fyqz.dc.action.ForwardAction.java 
 * @createTime: 2014-8-14下午4:00:46
 * @creater: [创建人]
 * @editTime： 2014-8-14下午4:00:46
 * @modifier: [修改人]  
 */
@BusinessDesc("跳转模块")
public class ForwardAction extends BaseAction{

    @BusinessDesc("跳转到主页")
	public String toMain() {
		
		log.info("跳转到主页");
		return "toMain";
	}

    @BusinessDesc("跳转到登录页")
	public String toLogin() {
		
		log.info("跳转到登录页");
		removeUserInSession();//删除用户登录信息
		return "toLogin";
	}
	
	public String toDiscussNew() {
		
		log.info("跳转到研讨申请页面");
		return "toDiscussNew";
	}
	
	public String toDiscussList() {
		
		log.info("跳转到研讨申请页面");
		return "toDiscussList";
	}
	
	public String toUserNew(){
		
		log.info("跳转到用户新增页面");
		return "toUserNew";
	}
	
	public String toRoleNew(){
		
		log.info("跳转到角色新增页面");
		return "toRoleNew";
	}
	
	public String toPermissionNew(){
		
		log.info("跳转到权限新增页面");
		return "toPermissionNew";
	}
}
 