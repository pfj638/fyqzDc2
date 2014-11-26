package com.fyqz.dc.service;

import java.util.HashMap;
import java.util.List;

import com.fyqz.dc.common.FYQZContext;
import com.fyqz.dc.dto.DiscussManager;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.entity.Discuss;
import com.fyqz.dc.entity.Document;

 
/**
 * @file:   com.fyqz.dc.service.DiscussManagerService.java
 * @brief:   大成项目-案例研讨-初始化模块业务实现接口
 * @author:   林明阳
 * @date: 2014-8-22下午4:03:30
 * @version: 大成1.0
 */
public interface DiscussManagerService {

 
	/**
	 * 
	 * show: 查询研讨实例 
	 * @param fyqzContext
	 * @param discuss
	 * @param page
	 * @return
	 * @throws Exception
	 * @exception
	 */
	public List<HashMap> findDiscussInitList(FYQZContext fyqzContext, DiscussManager discuss,
			Page page) throws Exception;

	/**
	 * 
	 * show: 查询研讨实例  总条数
	 * @param fyqzContext
	 * @param discuss
	 * @param page
	 * @return
	 * @throws Exception
	 * @exception
	 */
	public int findDiscussInitCount(FYQZContext fyqzContext, DiscussManager discuss, Page page)
			throws Exception;

	/**
	 * 
	 * show: 研讨实例   初始化
	 * @param fyqzContext
	 * @param path
	 * @param discussId
	 * @throws Exception
	 * @exception
	 */
	public void addInitDiscuss(FYQZContext fyqzContext, String path, String discussId)
			throws Exception;

	/**
	 * 
	 * show: 研讨实例   启动
	 * @param fyqzContext
	 * @param discussId
	 * @throws Exception
	 * @exception
	 */
	public void addStarttDiscuss(FYQZContext fyqzContext, String discussId) throws Exception;

	/**
	 * 
	 * show: 加载案例
	 * @param fyqzContext
	 * @param discussId
	 * @return
	 * @throws Exception
	 * @exception
	 */
	public Discuss load(FYQZContext fyqzContext, String discussId) throws Exception;

	/**
	 * 
	 * show: 加载案例附件
	 * @param fyqzContext
	 * @param documentId
	 * @return
	 * @throws Exception
	 * @exception
	 */
	public Document loadDocument(FYQZContext fyqzContext, String documentId) throws Exception;

	/**
	 * 
	 * show: 文件上传成功后 数据保存
	 * @param fyqzContext
	 * @param path
	 * @param filename
	 * @param discussId
	 * @return
	 * @throws Exception
	 * @exception
	 */
	public Document addDocumentForDiscuss(FYQZContext fyqzContext, String path, String filename,
			String discussId) throws Exception;

	/**
	 * 
	 * show: 查询案例研讨所有附件
	 * @param fyqzContext
	 * @param discussId
	 * @return
	 * @throws Exception
	 * @exception
	 */
	public List<Document> findDocumentByDiscussId(FYQZContext fyqzContext, String discussId)
			throws Exception;

	/**
	 * 
	 * show: 删除案例研讨中的附件
	 * @param fyqzContext
	 * @param documentId
	 * @throws Exception
	 * @exception
	 */
	public void deleteDocumentById(FYQZContext fyqzContext, String documentId) throws Exception;

	/**
	 * 
	 * show: 初始化page对象
	 * @param page
	 * @param pageSize
	 * @throws Exception
	 * @exception
	 */
	public Page initPage(Page page,int pageSize) throws Exception;
}
