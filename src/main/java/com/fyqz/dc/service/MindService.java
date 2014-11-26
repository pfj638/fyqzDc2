package com.fyqz.dc.service;  

import com.fyqz.dc.entity.Mind;
 
 
/**  
 * @description: [描述信息]
 * @fileName： com.fyqz.dc.service.MindService.java 
 * @createTime: 2014-8-1下午2:25:16
 * @creater: [创建人]
 * @editTime： 2014-8-1下午2:25:16
 * @modifier: [修改人]  
 */
public interface MindService {
	/**
	 * 
	 * @description: 解析json字符串并保存思维导图
	 * @editTime： 2014-8-1下午2:30:27
	 * @modifier: [修改人]  
	 * @param mindJson
	 * @throws Exception
	 */
	public void saveMind(String mindJson) throws Exception;
	/**
	 * 
	 * @description: 根据mindId获取思维导图的json字符串
	 * @editTime： 2014-8-1下午2:31:15
	 * @modifier: [修改人]  
	 * @return
	 * @throws Exception
	 */
	public String getMindJson(String mindId) throws Exception;
	/**
	 * 
	 * @description: 根据mindId获取思维导图
	 * @editTime： 2014-8-1下午2:34:55
	 * @modifier: [修改人]  
	 * @param mapId
	 * @return
	 * @throws Exception
	 */
	public Mind getMind(String mindId)throws Exception;
	/**
	 * 
	 * @description: 根据mindId删除思维导图
	 * @editTime： 2014-8-1下午2:36:29
	 * @modifier: [修改人]  
	 * @param mapId
	 * @throws Exception
	 */
	public void deleteMind(String mindId) throws Exception;
}
 