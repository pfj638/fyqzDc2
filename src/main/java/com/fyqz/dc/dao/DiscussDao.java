package com.fyqz.dc.dao;  

import java.util.List;

import com.fyqz.dc.dto.Page;
import com.fyqz.dc.entity.Discuss;
 
 
/**  
 * @description: [研讨Dao接口]
 * @fileName： com.fyqz.dc.dao.DiscussDao.java 
 * @createTime: 2014-8-14上午10:14:11
 * @creater: [创建人]
 * @editTime： 2014-8-14上午10:14:11
 * @modifier: [修改人]  
 */
public interface DiscussDao extends BaseDao<Discuss>{

	/**
	 * 
	 * @description: 分页查找研讨列表
	 * @editTime： 2014-8-20上午10:08:57
	 * @modifier: [修改人]  
	 * @param discuss
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public Object queryByPage(Discuss discuss, Page page) throws Exception;
}
 