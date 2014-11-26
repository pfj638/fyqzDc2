package com.fyqz.dc.dao.impl;  

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.fyqz.dc.dao.DiscussManagerDao;
import com.fyqz.dc.dto.DiscussManager;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.entity.Discuss;
import com.fyqz.dc.entity.Document;
 
/**
 * @file:   com.fyqz.dc.dao.impl.DiscussManagerDaoImpl.java  
 * @brief:   [研讨案例初始化-启动-参与--dao层接口实现类
 * @author:   林明阳
 * @date: 2014-8-22下午4:03:30
 * @version: 大成1.0
 */
@Repository
public class DiscussManagerDaoImpl  extends BaseDaoImpl<Discuss> implements DiscussManagerDao{
	/**
	 * 
	 * show: 查询研讨实例 
	 * @date 2014-8-22下午4:16:55
	 * @author   林明阳
	 * @version 大成1.0
	 * @param discuss
	 * @param page
	 * @return
	 * @throws Exception
	 * @exception
	 */
	@SuppressWarnings("unchecked")//忽略泛型警告
	public List<HashMap> findDiscussInitList(DiscussManager discuss,Page page) throws Exception{
		List<HashMap> list1=null;
	    List<String> l=new ArrayList<String>();
	    l.add(discuss.getUser().getId());
	    l.add(discuss.getStatus());
	    l.add(discuss.getType());
	    String sql1="select d.*,u.real_name from dc_cms_discuss  d,dc_bms_user u where u.id=d.proposer and u.id=? and  d.status=? and d.deployment=? order by d.insert_date desc";
	    list1=(List<HashMap>)findBySQL(sql1,l.toArray(), page.getFirstResult(), page.getMaxResults());
		return list1;
	}
	
	/**
	 * 
	 * show: 查询研讨实例  总条数
	 * @date 2014-8-22下午4:17:10
	 * @author   林明阳
	 * @version 大成1.0
	 * @param discuss
	 * @param page
	 * @return
	 * @throws Exception
	 * @exception
	 */
	@SuppressWarnings("unchecked")
	public int findDiscussInitCount(DiscussManager discuss,Page page) throws Exception{
		List<HashMap> list1=null;
	    List<String> l=new ArrayList<String>();
	    l.add(discuss.getUser().getId());
	    l.add(discuss.getStatus());
	    l.add(discuss.getType());
	    String sql1="select count(*) nums from dc_cms_discuss  d,dc_bms_user u where u.id=d.proposer and u.id=? and  d.status=?  and d.deployment=? order by d.insert_date desc";
	    list1=(List<HashMap>)findBySQL(sql1,l.toArray(), -1, -1);
		return Integer.parseInt(list1.get(0).get("nums").toString());
	}

	/**
	 * 
	 * show: 查询案例研讨所有附件
	 * @date 2014-8-22下午4:17:18
	 * @author   林明阳
	 * @version 大成1.0
	 * @param discussId
	 * @return
	 * @throws Exception
	 * @exception
	 */
	@SuppressWarnings("unchecked")
	public List<Document> findDocumentByDiscussId(String discussId)throws Exception {
		String hql="from Document d where d.processId=?";
		List<Document> list1=null;
	    List<String> l=new ArrayList<String>();
	    l.add(discussId);
	    list1=(List<Document>)findByHQL(hql,l.toArray(), -1, -1);
		return list1;
	}
}
 