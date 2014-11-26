package com.fyqz.dc.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fyqz.dc.common.Constants;
import com.fyqz.dc.common.FYQZContext;
import com.fyqz.dc.dao.DiscussManagerDao;
import com.fyqz.dc.dto.DiscussManager;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.entity.Discuss;
import com.fyqz.dc.entity.Document;
import com.fyqz.dc.service.DiscussManagerService;

/**
 * @file:   com.fyqz.dc.service.impl.DiscussManagerServiceImpl.java
 * @brief:   大成项目-案例研讨-初始化模块业务实现类
 * @author:   林明阳
 * @date: 2014-8-22下午4:03:30
 * @version: 大成1.0
 */
@Service
public class DiscussManagerServiceImpl implements DiscussManagerService {

	@Autowired
	public DiscussManagerDao discussManager;

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
	public List<HashMap> findDiscussInitList(FYQZContext fyqzContext,DiscussManager discuss, Page page) throws Exception {

		List<HashMap> list = discussManager.findDiscussInitList(discuss, page);
		return list;
	}

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
	public int findDiscussInitCount(FYQZContext fyqzContext, DiscussManager discuss,Page page) throws Exception {

		int count = discussManager.findDiscussInitCount(discuss, page);
		return count;
	}

	/**
	 * 
	 * show: 研讨实例   初始化
	 * @param fyqzContext
	 * @param path
	 * @param discussId
	 * @throws Exception
	 * @exception
	 */
	public void addInitDiscuss(FYQZContext fyqzContext, String path, String discussId) {

		Discuss discuss = discussManager.load(Discuss.class, discussId);
		discuss.setStatus(Constants.DiscussStatus.DISCUSS_STATUS_INIT_FINISH);
		discussManager.saveOrUpdate(discuss);
	}

	/**
	 * 
	 * show: 研讨实例   启动
	 * @param fyqzContext
	 * @param discussId
	 * @throws Exception
	 * @exception
	 */
	public void addStarttDiscuss(FYQZContext fyqzContext, String discussId) {

		Discuss discuss = discussManager.load(Discuss.class, discussId);
		discuss.setStatus(Constants.DiscussStatus.DISCUSS_STATUS_BEGIN);
		discussManager.saveOrUpdate(discuss);
	}

	/**
	 * 
	 * show: 加载案例
	 * @param fyqzContext
	 * @param discussId
	 * @return
	 * @throws Exception
	 * @exception
	 */
	public Discuss load(FYQZContext fyqzContext, String discussId) {

		Discuss discuss = discussManager.load(Discuss.class, discussId);
		return discuss;
	}

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
			String discussId) throws Exception {

		Document doc = new Document();
		doc.setProcessId(discussId);
		doc.setPath(path);
		doc.setInsertDate(new Date());
		doc.setName(filename);
		doc.setType(filename.substring(filename.lastIndexOf("."), filename.length()));
		discussManager.save(doc);
		return doc;
	}

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
			throws Exception {

		List<Document> doclist = discussManager.findDocumentByDiscussId(discussId);
		return doclist;
	}

	/**
	 * 
	 * show: 删除案例研讨中的附件
	 * @param fyqzContext
	 * @param documentId
	 * @throws Exception
	 * @exception
	 */
	public void deleteDocumentById(FYQZContext fyqzContext, String documentId) throws Exception {

		Document doc = loadDocument(fyqzContext, documentId);
		discussManager.delete(doc);
	}

	/**
	 * 
	 * show: 加载案例附件
	 * @param fyqzContext
	 * @param documentId
	 * @throws Exception
	 * @exception
	 */
	public Document loadDocument(FYQZContext fyqzContext, String documentId) throws Exception {
		Document doc = (Document) discussManager.load(Document.class, documentId);
		return doc;
	}
	
	/**
	 * 
	 * show: 初始化page对象
	 * @param page
	 * @param pageSize
	 * @throws Exception
	 * @exception
	 */
	public Page initPage(Page page,int pageSize) throws Exception{
		if (null == page) {
			page = new Page();
			page.setPageSize(pageSize);
			page.setPageIndex(1);
		} else {
			if (page.getPageIndex() > page.getPageCount()) {
				page.setPageIndex(page.getPageCount());
			}
		}
		return page;
	}
}
