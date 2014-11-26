package com.fyqz.dc.service;  

import java.util.List;

import com.fyqz.dc.common.FYQZContext;
import com.fyqz.dc.dto.DiscussForm;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.entity.Discuss;
import com.fyqz.dc.entity.User;
import com.fyqz.dc.entity.UserDiscussRole;
 
 
/**  
 * @description: [描述信息]
 * @fileName： com.fyqz.dc.service.DiscussService.java 
 * @createTime: 2014-8-14上午10:17:26
 * @creater: [创建人]
 * @editTime： 2014-8-14上午10:17:26
 * @modifier: [修改人]  
 */
public interface DiscussService {
	
	/**
	 * 
	 * @description: 新增新的研讨
	 * @editTime： 2014-8-18上午10:11:18
	 * @modifier: [修改人]  
	 * @param discuss
	 * @throws Exception
	 */
	public void addDiscuss(FYQZContext fyqzContext, Discuss discuss,DiscussForm discussForm) throws Exception;
	
	/**
	 * 
	 * @description: 根据id查询研讨案例
	 * @editTime： 2014-8-19下午2:35:07
	 * @modifier: [修改人]  
	 * @param discussId
	 * @return
	 * @throws Exception
	 */
	public Discuss queryById(FYQZContext fyqzContext, String discussId) throws Exception;
	
	/**
	 * 
	 * @description: 根据id查询参与研讨的参与者
	 * @editTime： 2014-8-19下午3:25:53
	 * @modifier: [修改人]  
	 * @param discusId
	 * @return
	 * @throws Exception
	 */
	public List<UserDiscussRole> queryParticipate(FYQZContext fyqzContext, String discusId) throws Exception;
	
	/**
	 * 
	 * @description: 分页查询
	 * @editTime： 2014-8-20上午9:57:25
	 * @modifier: [修改人]  
	 * @param discuss
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public Object queryByPage(FYQZContext fyqzContext, Discuss discuss, Page page) throws Exception;
	
	/**
	 * 
	 * @description: 对研讨进行授权
	 * @editTime： 2014-8-21上午10:25:03
	 * @modifier: [修改人]  
	 * @param fyqzContext
	 * @param discussForm
	 * @param checkUser
	 * @throws Exception
	 */
	public void checkDiscuss(FYQZContext fyqzContext, DiscussForm discussForm,
			User checkUser) throws Exception;
}
 