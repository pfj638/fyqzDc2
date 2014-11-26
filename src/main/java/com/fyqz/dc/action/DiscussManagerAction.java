package com.fyqz.dc.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.fyqz.dc.common.BaseAction;
import com.fyqz.dc.common.Constants;
import com.fyqz.dc.common.OperResultForJson;
import com.fyqz.dc.dto.DiscussManager;
import com.fyqz.dc.dto.DiscussManagerForm;
import com.fyqz.dc.dto.Page;
import com.fyqz.dc.entity.Document;
import com.fyqz.dc.entity.User;
import com.fyqz.dc.service.DiscussManagerService;
import com.fyqz.dc.util.FileUtil;

/**
 * @file:   com.fyqz.dc.action.DiscussManager.java
 * @brief:   大成项目-案例研讨-初始化 Action
 * @author:   林明阳
 * @date: 2014-8-22下午4:03:30
 * @version: 大成1.0
 */
public class DiscussManagerAction extends BaseAction {

	private static final long serialVersionUID = 123456789123L;
	@Autowired
	private DiscussManagerService discissservice;
	private DiscussManagerForm discussManagerForm;
	public List<HashMap> list;
	private InputStream inputStream;

	/**
	 * 
	 * show: 案例实例 初始化列表页
	 * @return
	 * @exception
	 */
	public String discussInitList() {
		User user = getLoginUser();
		System.out.println(this.getRequest().getParameter("discussManagerForm.type"));
		try {
			log.info("加载案例实例-初始化列表页！"); 
			discussManagerForm.setPage(discissservice.initPage(discussManagerForm.getPage(), 10));
			DiscussManager discuss=new DiscussManager(user,Constants.DiscussStatus.DISCUSS_STATUS_READY,discussManagerForm.getType());
			try {
				discussManagerForm.getPage().setTotalCount(discissservice.findDiscussInitCount(getFyqzContext(),discuss, discussManagerForm.getPage()));
			} catch (Exception e) {
				log.error("查询研讨总数失败！",e);
			}
			list = discissservice.findDiscussInitList(getFyqzContext(), discuss, discussManagerForm.getPage());
			log.debug("初始化列表页查询结果：", list);
			log.info("加载案例实例-初始化列表页完毕！");
		} catch (Exception e) {
			log.error("加载案例实例-初始化列表页失败", e);
		}
		return "list";
	}

	/**
	 * 
	 * show: 跳转案例实例 初始化
	 * @return
	 * @exception
	 */
	public String toInitDiscuss() {

		return "init";
	}

	/**
	 * 
	 * show: 案例实例  初始化案例研讨
	 * @return
	 * @exception
	 */
	public String addInitDiscuss() {

		try {
			log.info("案例实例  初始化案例研讨！"); 
			discissservice.addInitDiscuss(getFyqzContext(),"", discussManagerForm.getDiscussId());
			log.info("案例实例  初始化案例研讨成功！");
		} catch (Exception e) {
			log.error("案例实例  初始化案例研讨失败！",e);
		}
		return "initlist";
	}

	/**
	 * 
	 * show: 案例实例  启动案例研讨
	 * @return
	 * @exception
	 */
	@OperResultForJson
	public String ajaxaddStartDiscuss() {

		log.info("案例实例  启动案例研讨！");
		try {
			discissservice.addStarttDiscuss(getFyqzContext(), discussManagerForm.getDiscussId());
			operResult.setData("启动案例研讨成功");
			operResult.setSuccess(true);
			log.info("案例实例  启动案例研讨成功！");
		} catch (Exception e) {
			log.info("案例实例  启动案例研讨失败！");
			operResult.setErrorDesc("启动案例研讨失败");
		}
		return NONE;
	}

	/**
	 * 
	 * show: 案例实例 初始化--附件上传
	 * @return
	 * @exception
	 */
	@OperResultForJson
	public String ajaxuploadDocument() {

		log.info("案例实例 初始化--附件上传！");
		try {
			String path = FileUtil.uploadFileaUpdate(discussManagerForm.getUpload(),
					discussManagerForm.getUploadFileName());
			log.debug("附件上传PATH：" + path);
			log.info("案例实例 初始化--附件上传成功！");
			Document doc = discissservice.addDocumentForDiscuss(getFyqzContext(), path,
					discussManagerForm.getUploadFileName(), discussManagerForm.getDiscussId());
			log.debug("附件数据：", doc);
			log.info("案例实例 初始化--附件数据保存成功！");
			operResult.setData(doc);
		} catch (Exception e) {
			operResult.setErrorDesc("附件上传失败");
			log.error("附件上传失败", e);
		}
		return NONE;
	}

	/**
	 * 
	 * show: 案例实例 初始化--加载所有案例附件
	 * @return
	 * @exception
	 */
	@OperResultForJson
	public String ajaxfindDocumentByDiscussId() {

		try {
			log.info("案例实例--加载所有案例附件！");
			List<Document> docList = discissservice.findDocumentByDiscussId(getFyqzContext(),
					discussManagerForm.getDiscussId());
			log.info("加载所有案例附件结果："+ docList.size()+docList.get(0).getPath());
			log.info("案例实例--加载所有案例附件成功！");
			operResult.setData(docList);
		} catch (Exception e) {
			// TODO: handle exception
			operResult.setErrorDesc("加载所有案例附件失败");
			log.error("加载所有案例附件失败", e);
		}
		return NONE;
	}

	/**
	 * 
	 * show: 案例实例 初始化--删除某个附件
	 * @return
	 * @exception
	 */
	@OperResultForJson
	public String ajaxdeleteDocumentById() {

		log.info("案例实例 初始化--删除某个附件！");
		try {
			discissservice.deleteDocumentById(getFyqzContext(), discussManagerForm.getDocumentId());
			log.info("案例实例 初始化--删除某个附件成功！");
		} catch (Exception e) {
			operResult.setErrorDesc("删除某个附件失败");
			log.error("删除某个附件失败", e);
		}
		return NONE;
	}

	/**
	 * 
	 * show: 案例实例 初始化---判断此文件是否存在
	 * @return
	 * @exception
	 */
	@OperResultForJson
	public String ajaxHasFile() {

		log.info("案例实例 初始化---判断此文件是否存在!");
		try {
			Document doc = discissservice.loadDocument(getFyqzContext(),
					discussManagerForm.getDocumentId());
			File file = new File(doc.getPath());
			if (file.exists()) {
				log.info("文件存在!");
			} else {
				operResult.setErrorDesc("附件不存在");
				log.info("附件不存在!");
			}
		} catch (Exception e) {
			operResult.setErrorDesc("判断此文件是否存在");
			log.error("判断此文件是否存在", e);
		}
		return NONE;
	}

	/**
	 * 
	 * show:  案例实例 初始化---下载文件
	 * @return
	 * @exception
	 */
	public String downloadFile() {

		log.info("案例实例 初始化---下载文件");
		try {
			Document doc = discissservice.loadDocument(getFyqzContext(),discussManagerForm.getDocumentId());
			//FileUtil.downloadXls(this.getResponse(), doc.getPath(), doc.getName());
			inputStream=new FileInputStream(new File(doc.getPath()));
		    String	fileName = new String(doc.getName().getBytes("gb2312"), "ISO-8859-1");
			discussManagerForm.setUploadFileName(fileName);
		} catch (Exception e) {
			log.error("下载文件失败！", e);
		}
		return "download";
		//return NONE;
	}

	/**
	 * 
	 * show: 案例实例 --启动案例研讨列表页
	 * @return
	 * @exception
	 */
	public String discussStartList() {

		log.info("案例实例 --启动案例研讨列表页！");
		try {
			User user = getLoginUser();
			discussManagerForm.setPage(discissservice.initPage(discussManagerForm.getPage(), 10));
			DiscussManager discuss=new DiscussManager(user,Constants.DiscussStatus.DISCUSS_STATUS_INIT_FINISH,discussManagerForm.getType());
			try {
				discussManagerForm.getPage().setTotalCount(discissservice.findDiscussInitCount(getFyqzContext(),discuss, discussManagerForm.getPage()));
			} catch (Exception e) {
				log.error("查询启动案例研讨总数失败！");
			}
			list = discissservice.findDiscussInitList(getFyqzContext(),discuss, discussManagerForm.getPage());
			log.debug("启动案例研讨列表页查询结果：", list);
			log.info("加载案例实例-启动案例研讨列表页完毕！");
		} catch (Exception e) {
			log.error("加载案例实例-启动案例研讨列表页失败", e);
		}
		return "start";
	}

	/**
	 * 
	 * show: 案例实例 --参与案例研讨列表页
	 * @return
	 * @exception
	 */
	public String discussAttendList() {

		log.info("案例实例 --参与案例研讨列表页！");
		try {
			User user = getLoginUser();
			discussManagerForm.setPage(discissservice.initPage(discussManagerForm.getPage(), 10));
			DiscussManager discuss=new DiscussManager(user,Constants.DiscussStatus.DISCUSS_STATUS_BEGIN,discussManagerForm.getType());
			try {
				discussManagerForm.getPage().setTotalCount(discissservice.findDiscussInitCount(getFyqzContext(),discuss, discussManagerForm.getPage()));
			} catch (Exception e) {
				log.error("查询参与案例研讨总数失败！",e);
			}
			list = discissservice.findDiscussInitList(getFyqzContext(), discuss, discussManagerForm.getPage());
			log.debug("参与案例研讨列表页查询结果：", list);
			log.info("加载案例实例-参与案例研讨列表页完毕！");
		} catch (Exception e) {
			log.error("加载案例实例-参与案例研讨列表页失败", e);
		}
		return "attend";
	}

	public DiscussManagerForm getDiscussManagerForm() {

		return discussManagerForm;
	}

	public void setDiscussManagerForm(DiscussManagerForm discussManagerForm) {

		this.discussManagerForm = discussManagerForm;
	}

	public List<HashMap> getList() {

		return list;
	}

	public void setList(List<HashMap> list) {

		this.list = list;
	}

	
	public InputStream getInputStream() {
	
		return inputStream;
	}

	
	public void setInputStream(InputStream inputStream) {
	
		this.inputStream = inputStream;
	}
	
}
