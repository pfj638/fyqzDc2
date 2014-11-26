package com.fyqz.dc.action;  

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fyqz.dc.common.Constants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fyqz.dc.common.BaseAction;
import com.fyqz.dc.common.OperResultForJson;
import com.fyqz.dc.dto.DiscussForm;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.entity.Discuss;
import com.fyqz.dc.entity.User;
import com.fyqz.dc.entity.UserDiscussRole;
import com.fyqz.dc.service.DiscussService;
 
 
/**  
 * @description: [研讨action]
 * @fileName： com.fyqz.dc.action.DiscussAction.java 
 * @createTime: 2014-8-14上午10:09:04
 * @creater: [创建人]
 * @editTime： 2014-8-14上午10:09:04
 * @modifier: [修改人]  
 */
public class DiscussAction extends BaseAction{
	
	@Autowired
	private DiscussService discussService;
	
	private DiscussForm discussForm;
	
	/*
	 * 研讨参与者列表
	 */
	private List<UserDiscussRole> participateList = null;
	
	/*
	 * 研讨列表
	 */
	private List<Discuss> discussList = null;
	
	/*
	 * 分页信息
	 */
	
	
	
	/**
	 * 
	 * show: 研讨申请功能
	 * @return
	 * @exception
	 */
	@OperResultForJson
	public String ajaxAdd(){
		
		log.info("申请研讨");
		Discuss discuss = new Discuss();
		BeanUtils.copyProperties(discussForm, discuss, new String[]{"insertDate"});
		/*增加申请者*/
		User proposer = getLoginUser();
		discuss.setProposer(proposer);
		/*设置为申请状态*/
		discuss.setStatus(Constants.DiscussStatus.DISCUSS_STATUS_APPLY);
		/*增加创建时间*/
		Date insertDate = null;
		SimpleDateFormat sdFormat = new SimpleDateFormat(Constants.DateFormat.DATE_FORMATE_YYYYMMDDHHMMSS);
		try {
			insertDate = sdFormat.parse(discussForm.getInsertDate());
		} catch (ParseException e) {
            log.error("申请研讨时出错",e);
		}
		discuss.setInsertDate(insertDate);

		try{
			discussService.addDiscuss(getFyqzContext(), discuss,discussForm);
		}catch(Exception e){
			operResult.setErrorDesc("研讨申请失败");
			log.error("研讨申请失败",e);
		}
		return NONE;
	}
	
	/**
	 * 
	 * show: 分页查询研讨列表
	 * @return
	 * @exception
	 */
	public String queryCheckDiscuss(){

		log.info("分页获取需要进行授权的研讨列表");
		Discuss discuss = new Discuss();
		discuss.setStatus(Constants.DiscussStatus.DISCUSS_STATUS_APPLY);
		int total = 0;
		if(null == discussForm){
			discussForm = new DiscussForm();
		}
		try {
			total = (Integer) discussService.queryByPage(getFyqzContext(), discuss, null);
		} catch (Exception e) {
			log.error("查询研讨总数失败",e);
		}
		if (null == discussForm.getPage()) {
			discussForm.setPage(new Page());
			discussForm.getPage().setPageSize(10);
			discussForm.getPage().setPageIndex(1);
			discussForm.getPage().setTotalCount(total);
		}else{
			discussForm.getPage().setTotalCount(total);
			if(discussForm.getPage().getPageIndex() > discussForm.getPage().getPageCount()){
				discussForm.getPage().setPageIndex(discussForm.getPage().getPageCount());
			}
		}
		try {
			discussList = (List<Discuss>) discussService.queryByPage(getFyqzContext(), discuss, discussForm.getPage());
		} catch (Exception e) {
			log.error("研讨列表查询失败",e);
		}
		return "queryCheckDiscuss";
	}
	
	/**
	 * 
	 * show: 获取研讨申请的具体内容
	 * @return
	 * @exception
	 */
	public String detail(){
		
		log.info("根据获取研讨申请的具体内容");
		try{
			
			Discuss discuss= discussService.queryById(getFyqzContext(), discussForm.getDiscussId());
			participateList = discussService.queryParticipate(getFyqzContext(), discussForm.getDiscussId());
			getRequest().setAttribute("discuss", discuss);
		}catch(Exception e){
			log.error("查询出错",e);
		}
		return "detail";
	}
	
	/**
	 * 
	 * show: 研讨授权
	 * @return
	 * @exception
	 */
	@OperResultForJson
	public String ajaxCheck(){
		
		log.info("研讨进行{}授权", (null != discussForm.getCheckType() && discussForm.getCheckType().equals("true")) ? "同意" : "驳回");
	
		try{
			discussService.checkDiscuss(getFyqzContext(), discussForm, getLoginUser());
		}catch(Exception e){
			log.error("授权失败",e);
			operResult.setErrorDesc("授权失败");
		}
		return NONE;
	}

	
	public DiscussForm getDiscussForm() {
	
		return discussForm;
	}

	
	public void setDiscussForm(DiscussForm discussForm) {
	
		this.discussForm = discussForm;
	}
	public List<UserDiscussRole> getParticipateList() {
	
		return participateList;
	}

	
	public void setParticipateList(List<UserDiscussRole> participateList) {
	
		this.participateList = participateList;
	}

	
	public List<Discuss> getDiscussList() {
	
		return discussList;
	}

	
	public void setDiscussList(List<Discuss> discussList) {
	
		this.discussList = discussList;
	}

}
 