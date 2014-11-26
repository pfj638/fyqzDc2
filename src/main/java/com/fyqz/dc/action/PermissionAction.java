package com.fyqz.dc.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fyqz.dc.common.BaseAction;
import com.fyqz.dc.common.OperResultForJson;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.dto.PermissionForm;
import com.fyqz.dc.entity.Permission;
import com.fyqz.dc.service.PermissionService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @file:   com.fyqz.dc.action.PermissionAction.java
 * @brief:   文件描述
 * @author:   Administrator
 * @date: 2014-8-25下午4:26:23
 * @version: 大成1.0
 */
public class PermissionAction extends BaseAction {

	@Autowired
	private PermissionService permissionService;
	/**
	 * 传递角色参数
	 */
	private PermissionForm permissionForm;
	/**
	 * 保存查询的角色列表
	 */
	private List<Permission> permissionList;

	/**
	 * 
	 * show: 新增权限
	 * @return
	 * @exception
	 */
	@OperResultForJson
	public String ajaxAdd() {

		log.info("新增权限");
		try {
			permissionService.addPermission(fyqzContext, permissionForm);
		} catch (Exception e) {
			log.error("新增权限失败", e);
			operResult.setErrorDesc("新增权限失败");
		}
		return NONE;
	}
	
	/**
	 * 
	 * show: 删除权限
	 * @return
	 * @exception
	 */
	@OperResultForJson
	public String ajaxDelete(){
		
		log.info("删除权限");
		try{
			permissionService.deletePermission(fyqzContext,permissionForm.getPermissionId());
		}catch(Exception e){
			log.error("删除权限失败",e);
			operResult.setErrorDesc("删除权限失败");
		}
		return NONE;
	}
	
	/**
	 * 
	 * show: 分页查询权限
	 * @return
	 * @exception
	 */
	public String queryPermissionByPage() {

		log.info("跳转到权限维护界面，并分页查询权限");
		int total = 0;
		Gson gson = new Gson();
		if (null == permissionForm) {
			permissionForm = new PermissionForm();
		}
		if (null != permissionForm.getPage()) {
			Page page = permissionForm.getPage();
			String condition = page.getCondition();
			permissionForm = gson.fromJson(condition, new TypeToken<PermissionForm>() {}.getType());
			permissionForm.setPage(page);
		}
		try {
			total = (Integer) permissionService.queryByPage(getFyqzContext(), permissionForm, null);
		} catch (Exception e) {
			log.error("查询权限总数失败", e);
		}
		if (null == permissionForm.getPage()) {
			permissionForm.setPage(new Page());
			permissionForm.getPage().setPageSize(10);
			permissionForm.getPage().setPageIndex(1);
			permissionForm.getPage().setTotalCount(total);
			permissionForm.getPage().setCondition(gson.toJson(permissionForm));
		} else {
			permissionForm.getPage().setTotalCount(total);
			if (permissionForm.getPage().getPageIndex() > permissionForm.getPage().getPageCount()) {
				permissionForm.getPage().setPageIndex(permissionForm.getPage().getPageCount());
			}
		}
		try {
			permissionList = (List<Permission>) permissionService.queryByPage(getFyqzContext(),
					permissionForm, permissionForm.getPage());
		} catch (Exception e) {
			log.error("权限列表查询失败");
		}
		return "toPermissionManage";
	}

	/**
	 * 
	 * show: 跳转到权限修改页面
	 * @return
	 * @exception
	 */
	public String toModify() {

		log.info("跳转到权限修改页面");
		try {
			Permission permission = permissionService.queryPermission(fyqzContext,
					permissionForm.getPermissionId());
			this.getRequest().setAttribute("permission", permission);
		} catch (Exception e) {
			log.info("跳转到修改页面时权限查询出错");
		}
		return "toModify";
	}

	/**
	 * 
	 * show: 权限信息修改
	 * @return
	 * @exception
	 */
	@OperResultForJson
	public String ajaxModify() {

		log.info("权限信息修改");
		try {
			permissionService.updatePermission(fyqzContext, permissionForm);
		} catch (Exception e) {
			log.error("权限信息修改失败", e);
			operResult.setErrorDesc("权限信息修改失败");
		}
		return NONE;
	}
	
	/**
	 * 
	 * show: 查询所有的权限列表
	 * @return
	 * @exception
	 */
	@OperResultForJson
	public String ajaxQueryAll(){
		
		log.info("查询所有的权限列表");
		try{
			permissionList = permissionService.queryAllPermission(fyqzContext);
			operResult.setData(permissionList);
		}catch(Exception e){
			log.error("权限列表查询失败",e);
			operResult.setErrorDesc("权限列表查询失败");
		}
		return NONE;
	}
	
	public PermissionForm getPermissionForm() {
	
		return permissionForm;
	}

	
	public void setPermissionForm(PermissionForm permissionForm) {
	
		this.permissionForm = permissionForm;
	}

	
	public List<Permission> getPermissionList() {
	
		return permissionList;
	}

	
	public void setPermissionList(List<Permission> permissionList) {
	
		this.permissionList = permissionList;
	}
	
}
