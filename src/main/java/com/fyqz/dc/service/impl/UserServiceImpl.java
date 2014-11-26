package com.fyqz.dc.service.impl;  

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fyqz.dc.common.FYQZContext;
import com.fyqz.dc.dao.GradeDao;
import com.fyqz.dc.dao.ProfessionalTitleDao;
import com.fyqz.dc.dao.RoleDao;
import com.fyqz.dc.dao.UserDao;
import com.fyqz.dc.dao.UserTypeDao;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.dto.UserForm;
import com.fyqz.dc.entity.Grade;
import com.fyqz.dc.entity.ProfessionalTitle;
import com.fyqz.dc.entity.Role;
import com.fyqz.dc.entity.User;
import com.fyqz.dc.entity.UserType;
import com.fyqz.dc.service.UserService;
import com.fyqz.dc.util.MD5Encoder;
 
 
/**  
 * @description: [描述信息]
 * @fileName： com.fyqz.dc.service.impl.UserServiceImpl.java 
 * @createTime: 2014-8-15上午9:29:46
 * @creater: [创建人]
 * @editTime： 2014-8-15上午9:29:46
 * @modifier: [修改人]  
 */
@Service
public class UserServiceImpl implements UserService{
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class.getName());

	@Autowired
	private UserDao userDao;
	@Autowired
	private GradeDao gradeDao;
	@Autowired
	private UserTypeDao userTypeDao;
	@Autowired
	private ProfessionalTitleDao professionalTitleDao;
	@Autowired
	private RoleDao roleDao;

    public User login(FYQZContext fyqzContext, String loginName, String password) throws Exception {
        return userDao.login(loginName, password);
    }

	public boolean hasLoginName(FYQZContext fyqzContext, String loginName) throws Exception {
		return userDao.hasLoginName(loginName);
	}

	public List<User> queryAllUser(FYQZContext fyqzContext) throws Exception {
		return userDao.loadAll();
	}

	public User queryUser(FYQZContext fyqzContext, String id) throws Exception {
		return userDao.get(id);
	}
	
	public Map<String, Object> queryInfo(FYQZContext fyqzContext) throws Exception {
		
		Map<String,Object> info = new HashMap<String,Object>();
		List<Grade> gradeList = gradeDao.loadAll();
		List<UserType> userTypeList = userTypeDao.loadAll();
		List<ProfessionalTitle> professionalTitleList = professionalTitleDao.loadAll();
		info.put("grade", gradeList);
		info.put("userType", userTypeList);
		info.put("professionalTitle", professionalTitleList);
		return info;
	}
	
	public void addUser(FYQZContext fyqzContext, UserForm userForm) throws Exception{
		
		User user = new User();
		BeanUtils.copyProperties(userForm,user,new String[]{"type","professionalTitle","grade"});
		user.setPassword(MD5Encoder.encode(userForm.getLoginName()));
		if (null != userForm.getGrade()) {
			Grade grade = gradeDao.get(userForm.getGrade());
			user.setGrade(grade);
		}
		if (null != userForm.getUserType()) {
			UserType userType = userTypeDao.get(userForm.getUserType());
			user.setType(userType);
		}
		if (null != userForm.getProfessionalTitle()) {
			ProfessionalTitle professionalTitle = professionalTitleDao.get(userForm
					.getProfessionalTitle());
			user.setProfessionalTitle(professionalTitle);
		}
		userDao.save(user);
	}

	public Object queryByPage(FYQZContext fyqzContext, UserForm userForm, Page page)
			throws Exception {
		User user = new User();
		if (null != userForm) {
			BeanUtils.copyProperties(userForm, user, new String[] { "type", "professionalTitle",
					"grade" });
			if (null != userForm.getGrade()) {
				Grade grade = gradeDao.get(userForm.getGrade());
				user.setGrade(grade);
			}
			if (null != userForm.getUserType()) {
				UserType userType = userTypeDao.get(userForm.getUserType());
				user.setType(userType);
			}
			if (null != userForm.getProfessionalTitle()) {
				ProfessionalTitle professionalTitle = professionalTitleDao.get(userForm
						.getProfessionalTitle());
				user.setProfessionalTitle(professionalTitle);
			}
		}
		return userDao.queryByPage(page, user);
	}
	
	public void updateUser(FYQZContext fyqzContext, UserForm userForm) throws Exception{
		User user = userDao.get(userForm.getUserId());
		BeanUtils.copyProperties(userForm, user, new String[] { "type", "professionalTitle","password","role",
		"grade" });
		if (null != userForm.getGrade()) {
			Grade grade = gradeDao.get(userForm.getGrade());
			user.setGrade(grade);
		}
		if (null != userForm.getUserType()) {
			UserType userType = userTypeDao.get(userForm.getUserType());
			user.setType(userType);
		}
		if (null != userForm.getProfessionalTitle()) {
			ProfessionalTitle professionalTitle = professionalTitleDao.get(userForm
					.getProfessionalTitle());
			user.setProfessionalTitle(professionalTitle);
		}
		userDao.update(user);
	}
	
	public void deleteUser(FYQZContext fyqzContext, String userId) throws Exception{
		User user = userDao.get(userId);
		userDao.delete(user);
	}

	public void setupRole(FYQZContext fyqzContext, UserForm userForm) throws Exception {

		User user = userDao.get(userForm.getUserId());
		Role role = roleDao.get(userForm.getRole());
		user.setRole(role);
		userDao.update(user);
	}
	
}
 