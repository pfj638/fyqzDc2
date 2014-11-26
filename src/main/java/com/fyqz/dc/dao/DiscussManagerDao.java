package com.fyqz.dc.dao;

import java.util.HashMap;
import java.util.List;

import com.fyqz.dc.dto.DiscussManager;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.entity.Discuss;
import com.fyqz.dc.entity.Document;

/**
 * @file:   com.fyqz.dc.dao.DiscussManagerDao.java 
 * @brief:   [研讨案例初始化-启动-参与--dao层接口
 * @author:   林明阳
 * @date: 2014-8-22下午4:03:30
 * @version: 大成1.0
 */
public interface DiscussManagerDao extends BaseDao<Discuss> {

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
	public List<HashMap> findDiscussInitList(DiscussManager discuss, Page page) throws Exception;

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
	public int findDiscussInitCount(DiscussManager discuss,Page page) throws Exception;

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
	public List<Document> findDocumentByDiscussId(String discussId) throws Exception;
}
