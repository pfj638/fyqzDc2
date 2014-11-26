package com.fyqz.dc.action;

import java.util.List;

import com.fyqz.dc.common.performance.BusinessDesc;
import org.springframework.beans.factory.annotation.Autowired;

import com.fyqz.dc.common.BaseAction;
import com.fyqz.dc.common.OperResultForJson;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.dto.RoleForm;
import com.fyqz.dc.entity.Role;
import com.fyqz.dc.service.RoleService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @file:   com.fyqz.dc.action.RoleAction.java
 * @brief:   文件描述
 * @author:   Administrator
 * @date: 2014-8-25下午1:50:49
 * @version: 大成1.0
 */
@BusinessDesc("角色模块")
public class RoleAction extends BaseAction {

	@Autowired
	private RoleService roleService;
	/**
	 * 传递角色参数
	 */
	private RoleForm roleForm;
	/**
	 * 保存查询的角色列表
	 */
	private List<Role> roleList;

	/**
	 * 
	 * show: 新增角色
	 * @return
	 * @exception
	 */
	@OperResultForJson
    @BusinessDesc("新增角色")
	public String ajaxAdd() {

		log.info("新增角色");
		try {
			roleService.addRole(fyqzContext, roleForm);
		} catch (Exception e) {
			log.error("新增角色失败", e);
			operResult.setErrorDesc("新增角色失败");
		}
		return NONE;
	}
	
	/**
	 * 
	 * show: 删除角色
	 * @return
	 * @exception
	 */
	@OperResultForJson
    @BusinessDesc("删除角色")
	public String ajaxDelete(){
		
		log.info("删除角色");
		try{
			roleService.deleteRole(fyqzContext,roleForm.getRoleId());
		}catch(Exception e){
			log.error("删除角色失败",e);
			operResult.setErrorDesc("删除角色失败");
		}
		return NONE;
	}
	
	/**
	 * 
	 * show: 分页查询角色
	 * @return
	 * @exception
	 */
    @BusinessDesc("分页查询角色")
	public String queryRoleByPage() {

		log.info("跳转到角色维护界面，并分页查询角色");
		int total = 0;
		Gson gson = new Gson();
		if (null == roleForm) {
			roleForm = new RoleForm();
		}
		if (null != roleForm.getPage()) {
			Page page = roleForm.getPage();
			String condition = page.getCondition();
			roleForm = gson.fromJson(condition, new TypeToken<RoleForm>() {}.getType());
			roleForm.setPage(page);
		}
		try {
			total = (Integer) roleService.queryByPage(getFyqzContext(), roleForm, null);
		} catch (Exception e) {
			log.error("查询角色总数失败", e);
		}
		if (null == roleForm.getPage()) {
			roleForm.setPage(new Page());
			roleForm.getPage().setPageSize(10);
			roleForm.getPage().setPageIndex(1);
			roleForm.getPage().setTotalCount(total);
			roleForm.getPage().setCondition(gson.toJson(roleForm));
		} else {
			roleForm.getPage().setTotalCount(total);
			if (roleForm.getPage().getPageIndex() > roleForm.getPage().getPageCount()) {
				roleForm.getPage().setPageIndex(roleForm.getPage().getPageCount());
			}
		}
		try {
			roleList = (List<Role>) roleService.queryByPage(getFyqzContext(), roleForm,
					roleForm.getPage());
		} catch (Exception e) {
			log.error("角色列表查询失败");
		}
		return "toRoleManage";
	}

	/**
	 * 
	 * show: 跳转到角色修改页面
	 * @return
	 * @exception
	 */
    @BusinessDesc("跳转到角色修改页面")
	public String toModify() {

		log.info("跳转到角色修改页面");
		try {
			Role role = roleService.queryRole(fyqzContext, roleForm.getRoleId());
			this.getRequest().setAttribute("role", role);
		} catch (Exception e) {
			log.info("跳转到修改页面时角色查询出错");
		}
		return "toModify";
	}

	/**
	 * 
	 * show: 角色信息修改
	 * @return
	 * @exception
	 */
	@OperResultForJson
    @BusinessDesc("角色信息修改")
	public String ajaxModify() {

		log.info("角色信息修改");
		try {
			roleService.updateRole(fyqzContext, roleForm);
		} catch (Exception e) {
			log.error("角色信息修改失败", e);
			operResult.setErrorDesc("角色信息修改失败");
		}
		return NONE;
	}
	
	/**
	 * 
	 * show: 查询所有的角色列表
	 * @return
	 * @exception
	 */
	@OperResultForJson
    @BusinessDesc("查询所有的角色列表")
	public String ajaxQueryAll(){
		
		log.info("查询所有的角色列表");
		try{
			roleList = roleService.queryAllRole(fyqzContext);
			operResult.setData(roleList);
		}catch(Exception e){
			log.error("角色列表查询失败",e);
			operResult.setErrorDesc("角色列表查询失败");
		}
		return NONE;
	}
	
	/**
	 * 
	 * show: 跳转角色权限设置页面
	 * @return
	 * @exception
	 */
    @BusinessDesc("跳转角色权限设置页面")
	public String toPermissionSetup(){
		
		log.info("跳转角色权限设置页面");
		try{
			Role role = roleService.queryRole(fyqzContext, roleForm.getRoleId());
			this.getRequest().setAttribute("role", role);
		}catch(Exception e){
			log.error("跳转到角色权限页面时角色查询出错", e);
		}
		return "toPermissionSetup";
	}
	
	/**
	 * 
	 * show: 为用户设置角色
	 * @return
	 * @exception
	 */
	@OperResultForJson
    @BusinessDesc("角色权限设置")
	public String ajaxSetupPermission(){
		
		log.info("角色权限设置");
		try{
			roleService.setupPermission(fyqzContext, roleForm);
		}catch(Exception e){
			log.error("角色权限设置出错", e);
			operResult.setErrorDesc("角色权限设置出错");
		}
		return NONE;
	}

	public RoleForm getRoleForm() {

		return roleForm;
	}

	public void setRoleForm(RoleForm roleForm) {

		this.roleForm = roleForm;
	}

	public List<Role> getRoleList() {

		return roleList;
	}

	public void setRoleList(List<Role> roleList) {

		this.roleList = roleList;
	}
}
