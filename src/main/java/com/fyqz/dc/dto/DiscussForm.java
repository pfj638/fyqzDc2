package com.fyqz.dc.dto;  

import java.util.List;

import com.fyqz.dc.common.BaseForm;
 
 
/**  
 * @description: [用于传递研讨基础数据对象]
 * @fileName： com.fyqz.dc.dto.DiscussForm.java 
 * @createTime: 2014-8-15上午11:37:25
 * @creater: [创建人]
 * @editTime： 2014-8-15上午11:37:25
 * @modifier: [修改人]  
 */
public class DiscussForm extends BaseForm{
	
	/**
	 * 研讨主键id
	 */
	private String discussId;
	/**
	 * 研讨流程实例id
	 */
	private String processId;
	/**
	 * 研讨名称
	 */
	private String name;
	/**
	 * 研讨创建流程类型
	 */
	private String deployment;
	/**
	 * 研讨申请时间
	 */
	private String insertDate;
	/**
	 * 主办单位
	 */
	private String sponsor;
	/**
	 * 举办地址
	 */
	private String address;
	/**
	 * 研讨申请人
	 */
	private String proposer;
	/**
	 * 研讨描述
	 */
	private String discussDesc;
	/**
	 * 研讨状态
	 */
	private String status;
	/**
	 * 授权意见
	 */
	private String opinion;
	/**
	 * 授权类型:同意还是驳回
	 */
	private String checkType;
	/**
	 * 研讨参与的专家
	 */
	private List<String> expert;
	/**
	 * 研讨参与的观摩人员
	 */
	private List<String> view;
	/**
	 *研讨参与的主持人 
	 */
	private List<String> host;

	public String getDiscussId() {

		return discussId;
	}

	public void setDiscussId(String discussId) {
	
		this.discussId = discussId;
	}

	public String getProcessId() {

		return processId;
	}

	public void setProcessId(String processId) {

		this.processId = processId;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getDeployment() {

		return deployment;
	}

	public void setDeployment(String deployment) {

		this.deployment = deployment;
	}

	public String getInsertDate() {

		return insertDate;
	}

	public void setInsertDate(String insertDate) {

		this.insertDate = insertDate;
	}

	public String getSponsor() {

		return sponsor;
	}

	public void setSponsor(String sponsor) {

		this.sponsor = sponsor;
	}

	public String getAddress() {

		return address;
	}

	public void setAddress(String address) {

		this.address = address;
	}
	
	public String getProposer() {
	
		return proposer;
	}

	
	public void setProposer(String proposer) {
	
		this.proposer = proposer;
	}

	public String getDiscussDesc() {

		return discussDesc;
	}

	public void setDiscussDesc(String discussDesc) {

		this.discussDesc = discussDesc;
	}

	public String getStatus() {

		return status;
	}

	public void setStatus(String status) {

		this.status = status;
	}

	public String getOpinion() {

		return opinion;
	}

	public void setOpinion(String opinion) {

		this.opinion = opinion;
	}

	public String getCheckType() {

		return checkType;
	}

	public void setCheckType(String checkType) {

		this.checkType = checkType;
	}

	public List<String> getExpert() {
	
		return expert;
	}

	
	public void setExpert(List<String> expert) {
	
		this.expert = expert;
	}

	
	public List<String> getView() {
	
		return view;
	}

	
	public void setView(List<String> view) {
	
		this.view = view;
	}

	
	public List<String> getHost() {
	
		return host;
	}

	
	public void setHost(List<String> host) {
	
		this.host = host;
	}


}
 