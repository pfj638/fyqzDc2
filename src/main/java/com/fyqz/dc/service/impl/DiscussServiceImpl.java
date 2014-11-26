package com.fyqz.dc.service.impl;  

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fyqz.dc.common.Constants;
import com.fyqz.dc.common.FYQZContext;
import com.fyqz.dc.dao.DiscussDao;
import com.fyqz.dc.dao.DiscussRoleDao;
import com.fyqz.dc.dao.UserDao;
import com.fyqz.dc.dao.UserDiscussRoleDao;
import com.fyqz.dc.dto.DiscussForm;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.entity.Discuss;
import com.fyqz.dc.entity.DiscussRole;
import com.fyqz.dc.entity.User;
import com.fyqz.dc.entity.UserDiscussRole;
import com.fyqz.dc.service.DiscussService;
 
 
/**  
 * @description: [描述信息]
 * @fileName： com.fyqz.dc.service.impl.DiscussServiceImpl.java 
 * @createTime: 2014-8-14上午10:17:54
 * @creater: [创建人]
 * @editTime： 2014-8-14上午10:17:54
 * @modifier: [修改人]  
 */
@Service
public class DiscussServiceImpl implements DiscussService{
	
	 private static final Logger log = LoggerFactory.getLogger(DiscussServiceImpl.class.getName());
	 
	@Autowired
	private UserDao userDao;
	@Autowired
	private DiscussDao discussDao;
	@Autowired
	private DiscussRoleDao discussRoleDao;
	@Autowired
	private UserDiscussRoleDao userDiscussRoleDao;
	
	public void addDiscuss(FYQZContext fyqzContext, Discuss discuss, DiscussForm discussForm) throws Exception {

		log.info("调用者的基本信息{}", fyqzContext);
		DiscussRole discussRole = null;
		User user = null;
		discussDao.save(discuss);
		/*增加研讨支持人*/
		if (null != discussForm.getHost()) {
			for (int i = 0; i < discussForm.getHost().size(); i++) {
				UserDiscussRole userDiscussRole = new UserDiscussRole();
				user = userDao.get(discussForm.getHost().get(i));
				discussRole = discussRoleDao.getByName("host");
				userDiscussRole.setDiscuss(discuss);
				userDiscussRole.setDiscussRole(discussRole);
				userDiscussRole.setUser(user);
				userDiscussRoleDao.save(userDiscussRole);
			}
		}
		/*增加研讨专家*/
		if (null != discussForm.getExpert()) {
			for (int i = 0; i < discussForm.getExpert().size(); i++) {
				UserDiscussRole userDiscussRole = new UserDiscussRole();
				user = userDao.get(discussForm.getExpert().get(i));
				discussRole = discussRoleDao.getByName("expert");
				userDiscussRole.setDiscuss(discuss);
				userDiscussRole.setDiscussRole(discussRole);
				userDiscussRole.setUser(user);
				userDiscussRoleDao.save(userDiscussRole);
			}
		}
		/*增加研讨观摩人员*/
		if (null != discussForm.getView()) {
			for (int i = 0; i < discussForm.getView().size(); i++) {
				UserDiscussRole userDiscussRole = new UserDiscussRole();
				user = userDao.get(discussForm.getView().get(i));
				discussRole = discussRoleDao.getByName("view");
				userDiscussRole.setDiscuss(discuss);
				userDiscussRole.setDiscussRole(discussRole);
				userDiscussRole.setUser(user);
				userDiscussRoleDao.save(userDiscussRole);
			}
		}
	}
	
	public Discuss queryById(FYQZContext fyqzContext, String discussId) throws Exception {

		log.info("调用者的基本信息{}", fyqzContext);
		return discussDao.get(discussId);
	}
	
	public List<UserDiscussRole> queryParticipate(FYQZContext fyqzContext, String discussId)
			throws Exception {

		log.info("调用者的基本信息{}", fyqzContext);
		return userDiscussRoleDao.getByDiscuss(discussId);
	}

	public Object queryByPage(FYQZContext fyqzContext, Discuss discuss, Page page) throws Exception {

		log.info("调用者的基本信息{}", fyqzContext);
		return discussDao.queryByPage(discuss, page);
	}

	public void checkDiscuss(FYQZContext fyqzContext, DiscussForm discussForm, User checkUser)
			throws Exception {
		log.info("调用者的基本信息{}", fyqzContext);
		Discuss discuss = discussDao.get(discussForm.getDiscussId());
		if(discuss.getStatus().equals(Constants.DiscussStatus.DISCUSS_STATUS_APPLY)){
			discuss.setOpinion(discussForm.getOpinion());
			if(discussForm.getCheckType().equals("true")){
				discuss.setStatus(Constants.DiscussStatus.DISCUSS_STATUS_READY);
			}else if(discussForm.getCheckType().equals("false")){
				discuss.setStatus(Constants.DiscussStatus.DISCUSS_STATUS_REFUSE);
			}
		}
		discussDao.save(discuss);
	}
	
}
 