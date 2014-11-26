package com.fyqz.dc.dto;

import java.io.File;
import com.fyqz.dc.common.BaseForm;

/**
 * @file:   com.fyqz.dc.dto.DiscussManagerForm.java 
 * @brief:   [研讨案例初始化-启动-参与--用于传递研讨基础数据对象
 * @author:   林明阳
 * @date: 2014-8-22下午4:03:30
 * @version: 大成1.0
 */
public class DiscussManagerForm extends BaseForm {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1235123518913165123L;
	/**
	 * 案例Id
	 *  */
	public String discussId;
	/** 
	 * 附件Id
	 * */
	public String documentId;
	/**
	 *  file文件
	 *  */
	private File upload;
	/** 
	 * file文件名称
	 * 
	 *  */
	private String uploadFileName;
	/** 
	 * file类型 
	 * */
	private String uploadContentType;
	/**
	 * 研讨类型
	 * */
	public String type;

	public DiscussManagerForm() {

	}

	public String getDiscussId() {

		return discussId;
	}

	public void setDiscussId(String discussId) {

		this.discussId = discussId;
	}

	public String getDocumentId() {

		return documentId;
	}

	public void setDocumentId(String documentId) {

		this.documentId = documentId;
	}

	public File getUpload() {

		return upload;
	}

	public void setUpload(File upload) {

		this.upload = upload;
	}

	public String getUploadFileName() {

		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {

		this.uploadFileName = uploadFileName;
	}

	public String getType() {

		return type;
	}

	public void setType(String type) {

		this.type = type;
	}

	public String getUploadContentType() {

		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {

		this.uploadContentType = uploadContentType;
	}
}
