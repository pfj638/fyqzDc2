package com.fyqz.dc.dto;

import com.fyqz.dc.common.BaseForm;

/**  
 * @description: [保存分页信息]
 * @fileName： com.fyqz.dc.dto.Page.java 
 * @createTime: 2014-6-9上午10:01:22
 * @creater: [创建人]
 * @editTime： 2014-6-9上午10:01:22
 * @modifier: [修改人]  
 */
public class Page extends BaseForm{

	private static final long serialVersionUID = 3276501611903195910L;
	/**
	 * 页号，当前是第几页
	 */
	private int pageIndex;
	/**
	 * 每一页显示的条数
	 */
	private int pageSize;
	/**
	 * 总页数
	 */
	private int pageCount;
	/**
	 * 总条数，数据库中总共有多少条数据
	 */
	private int totalCount;
	/**
	 * 排序方式
	 */
	private String orderField;
	/**
	 * condition;
	 */
	private String condition;

	public int getPageIndex() {

		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {

		this.pageIndex = pageIndex;
	}

	public int getPageSize() {

		return pageSize;
	}

	public void setPageSize(int pageSize) {

		this.pageSize = pageSize <= 0 ? 20 : pageSize;
	}

	public int getPageCount() {

		return pageCount;
	}

	public void setPageCount(int pageCount) {

		this.pageCount = pageCount;
	}

	public int getTotalCount() {

		return totalCount;
	}

	public void setTotalCount(int totalCount) {

		this.pageCount = (totalCount % pageSize == 0) ? totalCount / pageSize : totalCount
				/ pageSize + 1;
		this.totalCount = totalCount;
	}

	public String getOrderField() {

		return orderField;
	}

	public void setOrderField(String orderField) {

		this.orderField = orderField;
	}

	public void setOrderField(String orderRule, String field) {

		this.orderField = " order by " + field + " " + orderRule;
	}

	public int getFirstResult() {

		return (pageIndex - 1) * pageSize;
	}

	public int getMaxResults() {

		return pageSize;
	}

	public String getCondition() {

		return condition;
	}

	public void setCondition(String condition) {

		this.condition = condition;
	}
}
