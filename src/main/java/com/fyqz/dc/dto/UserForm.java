package com.fyqz.dc.dto;  

import com.fyqz.dc.common.BaseForm;
 
 
/**  
 * @description: [传递页面用户参数]
 * @fileName： com.fyqz.dc.dto.UserForm.java 
 * @createTime: 2014-8-15上午9:31:31
 * @creater: [创建人]
 * @editTime： 2014-8-15上午9:31:31
 * @modifier: [修改人]  
 */
public class UserForm extends BaseForm{
	
	/**
	 * 用户主键id
	 */
	private String userId;
	/**
	 * 登录名称
	 */
	private String loginName;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 保存真实姓名
	 */
	private String realName;
	/**
	 * 保存用户权限
	 */
    private Integer permission;
    /**
     * 保存用户性别
     */
    private String sex;
    /**
     * 保存用户类型
     */
    private String userType;
    /**
     * 保存用户届数
     */
    private String year;
    /**
     * 保存用户部职别
     */
    private String jobTitle;
    /**
     * 保存用户职称
     */
    private String professionalTitle;
    /**
     * 保存用户邮箱
     */
    private String email;
    /**
     * 保存用户班级
     */
    private String grade;
    /**
     * 保存用户角色
     */
    private String role;
    
	public String getLoginName() {

		return loginName;
	}

	public void setLoginName(String loginName) {

		this.loginName = loginName;
	}

	public String getPassword() {

		return password;
	}

	public void setPassword(String password) {

		this.password = password;
	}

	public String getRealName() {

		return realName;
	}

	public void setRealName(String realName) {

		this.realName = realName;
	}

	public Integer getPermission() {

		return permission;
	}

	public void setPermission(Integer permission) {

		this.permission = permission;
	}

	public String getSex() {

		return sex;
	}

	public void setSex(String sex) {

		this.sex = sex;
	}
	
	public String getUserType() {
	
		return userType;
	}

	public void setUserType(String userType) {
	
		this.userType = userType;
	}

	public String getYear() {

		return year;
	}

	public void setYear(String year) {

		this.year = year;
	}

	public String getJobTitle() {

		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {

		this.jobTitle = jobTitle;
	}

	public String getProfessionalTitle() {

		return professionalTitle;
	}

	public void setProfessionalTitle(String professionalTitle) {

		this.professionalTitle = professionalTitle;
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	public String getGrade() {

		return grade;
	}

	public void setGrade(String grade) {

		this.grade = grade;
	}

	public String getRole() {

		return role;
	}

	public void setRole(String role) {

		this.role = role;
	}

	public String getUserId() {

		return userId;
	}

	public void setUserId(String userId) {

		this.userId = userId;
	}
	
}
 