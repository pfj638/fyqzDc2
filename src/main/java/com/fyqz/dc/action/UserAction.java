package com.fyqz.dc.action;  

import java.util.List;
import java.util.Map;

import com.fyqz.dc.common.performance.BusinessDesc;
import org.springframework.beans.factory.annotation.Autowired;

import com.fyqz.dc.common.BaseAction;
import com.fyqz.dc.common.Constants;
import com.fyqz.dc.common.OperResultForJson;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.dto.UserForm;
import com.fyqz.dc.entity.User;
import com.fyqz.dc.service.UserService;
import com.fyqz.dc.util.MD5Encoder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
 
 
/**  
 * @description: [用户Action]
 * @fileName： com.fyqz.dc.action.UserAction.java 
 * @createTime: 2014-8-14上午10:08:48
 * @creater: [创建人]
 * @editTime： 2014-8-14上午10:08:48
 * @modifier: [修改人]  
 */
@BusinessDesc("用户模块")
public class UserAction extends BaseAction {
	
	@Autowired
	private UserService userService;
	
	/**
	 * 用户功能参数
	 */
	private UserForm userForm;
	
	/**
	 * 保存查询的用户列表
	 */
	private List<User> userList;
	
	/**
	 * 
	 * @description:用户登录
	 * @editTime： 2014-8-15上午9:36:02
	 * @modifier: [xiaxuanyu]  
	 * @return
	 * @throws Exception
	 */
	@OperResultForJson
    @BusinessDesc("用户登录")
    public String ajaxLogin() throws Exception {
		log.info("用户{}登录", userForm.getLoginName());
        final User user = userService.login(getFyqzContext(),
                userForm.getLoginName(),
                MD5Encoder.encode(userForm.getPassword()));// 检查登录
        if (null != user) {
            getHttpSession().setMaxInactiveInterval(60 * 50);//登录时也设置一次会话超时
            getSession().put(Constants.WebInitConfigConstant.USER_IN_SESSION_NAME, user);
        } else {
            operResult.setErrorDesc("用户名或者密码错误!");
        }
        return NONE;
    }
	
	@OperResultForJson
    @BusinessDesc("查询用户列表")
	public String ajaxQueryAllUser() throws Exception {
		
		log.info("查询所有用户");
		List<User> userList = null;
		try{
			userList = userService.queryAllUser(getFyqzContext());
		}catch(Exception e){
			operResult.setErrorDesc("用户查找失败");
		}
		if(null != userList){
			operResult.setData(userList);
		}
		return NONE;
	}
	
	/**
	 * 
	 * show: 查找与用户表相关联的用户类型、职称、班级字典表
	 * @return
	 * @exception
	 */
	@OperResultForJson
	public String ajaxQueryInfo() {

		log.info("查找与用户表相关联的用户类型、职称、班级字典表");
		Map<String,Object> result = null;
		try{
			result = userService.queryInfo(fyqzContext);
			operResult.setData(result);
		}catch(Exception e){
			log.error("用户类型、职称、班级字典表查询出错",e);
			operResult.setErrorDesc("用户类型、职称、班级字典表查询出错");
		}
		return NONE;
	}
	
	/**
	 * 
	 * show: 新增用户
	 * @return
	 * @exception
	 */
	@OperResultForJson
    @BusinessDesc("新增用户")
	public String ajaxAdd(){
		
		log.info("新增用户");
		try{
			userService.addUser(fyqzContext,userForm);
		}catch(Exception e){
			log.error("新增用户失败",e);
			operResult.setErrorDesc("新增用户失败");
		}
		return NONE;
	}
	/**
	 * 
	 * show: 用户注册名检测
	 * @return
	 * @exception
	 */
	@OperResultForJson
	public String ajaxLoginNameCheck(){
		
		log.info("用户注册名检测");
		try{
			operResult.setData(userService.hasLoginName(fyqzContext, userForm.getLoginName()));
		}catch(Exception e){
			log.error("用户注册名检测失败",e);
			operResult.setErrorDesc("用户注册名检测失败");
		}
		return NONE;
	}
	/**
	 * 
	 * show: 删除用户
	 * @return
	 * @exception
	 */
	@OperResultForJson
    @BusinessDesc("删除用户")
	public String ajaxDelete(){
		
		log.info("删除用户");
		try{
			userService.deleteUser(fyqzContext,userForm.getUserId());
		}catch(Exception e){
			log.error("删除用户失败",e);
			operResult.setErrorDesc("删除用户失败");
		}
		return NONE;
	}
	
	/**
	 * 
	 * show: 跳转到用户维护界面，并分页查询用户
	 * @return
	 * @exception
	 */
    @BusinessDesc("分页查询用户列表")
	public String queryUserByPage(){
		
		log.info("跳转到用户维护界面，并分页查询用户");
		int total = 0;
		Gson gson=new Gson();
		if(null == userForm){
			userForm = new UserForm();
		}
		if (null != userForm.getPage()) {
			Page page = userForm.getPage();
			String condition = page.getCondition();
			userForm = gson.fromJson(condition, new TypeToken<UserForm>() {}.getType());
			userForm.setPage(page);
		}
		try {
			total = (Integer) userService.queryByPage(getFyqzContext(), userForm, null);
		} catch (Exception e) {
			log.error("查询用户总数失败",e);
		}
		if (null == userForm.getPage()) {
			userForm.setPage(new Page());
			userForm.getPage().setPageSize(10);
			userForm.getPage().setPageIndex(1);
			userForm.getPage().setTotalCount(total);
			userForm.getPage().setCondition(gson.toJson(userForm));
		}else{
			userForm.getPage().setTotalCount(total);
			if(userForm.getPage().getPageIndex() > userForm.getPage().getPageCount()){
				userForm.getPage().setPageIndex(userForm.getPage().getPageCount());
			}
		}
		try {
			userList = (List<User>) userService.queryByPage(getFyqzContext(), userForm, userForm.getPage());
		} catch (Exception e) {
			log.error("用户列表查询失败", e);
		}
		return "toUserManage";
	}
	
	/**
	 * 
	 * show: 跳转用户修改页面
	 * @return
	 * @exception
	 */
    @BusinessDesc("跳转到用户修改")
	public String toModify(){
		
		log.info("跳转用户修改页面");
		Gson gson = new Gson();
		try{
			User user = userService.queryUser(fyqzContext, userForm.getUserId());
			this.getRequest().setAttribute("user", user);
			this.getRequest().setAttribute("userString", gson.toJson(user));
		}catch(Exception e){
			log.error("跳转到修改页面时用户查询出错", e);
		}
		return "toModify";
	}
	
	/**
	 * 
	 * show: 用户信息修改
	 * @return
	 * @exception
	 */
	@OperResultForJson
    @BusinessDesc("用户信息修改")
	public String ajaxModify(){
		log.info("用户信息修改");
		try{
			userService.updateUser(fyqzContext, userForm);
		}catch(Exception e){
			log.error("用户信息修改失败", e);
			operResult.setErrorDesc("用户信息修改失败");
		}
		return NONE;
	}
	
	/**
	 * 
	 * show: 跳转用户角色设置页面
	 * @return
	 * @exception
	 */
    @BusinessDesc("跳转用户角色设置页面")
	public String toRoleSetup(){
		
		log.info("跳转用户角色设置页面");
		try{
			User user = userService.queryUser(fyqzContext, userForm.getUserId());
			this.getRequest().setAttribute("user", user);
		}catch(Exception e){
			log.error("跳转到用户角色页面时用户查询出错", e);
		}
		return "toRoleSetup";
	}
	
	/**
	 * 
	 * show: 为用户设置角色
	 * @return
	 * @exception
	 */
	@OperResultForJson
    @BusinessDesc("用户角色设置")
	public String ajaxSetupRole(){
		
		log.info("用户角色设置");
		try{
			userService.setupRole(fyqzContext, userForm);
		}catch(Exception e){
			log.error("用户角色设置出错", e);
			operResult.setErrorDesc("用户角色设置出错");
		}
		return NONE;
	}

	public UserForm getUserForm() {
	
		return userForm;
	}

	public void setUserForm(UserForm userForm) {
	
		this.userForm = userForm;
	}

	public List<User> getUserList() {
	
		return userList;
	}
	
	public void setUserList(List<User> userList) {
	
		this.userList = userList;
	}

}
