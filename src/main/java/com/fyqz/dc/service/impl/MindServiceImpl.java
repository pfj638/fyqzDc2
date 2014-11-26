package com.fyqz.dc.service.impl;  

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fyqz.dc.dao.EvaluateIndexDao;
import com.fyqz.dc.dao.MindAssociateDao;
import com.fyqz.dc.dao.MindDao;
import com.fyqz.dc.dao.MindItemDao;
import com.fyqz.dc.entity.Mind;
import com.fyqz.dc.entity.MindAssociate;
import com.fyqz.dc.entity.MindItem;
import com.fyqz.dc.service.MindService;
 
 
/**  
 * @description: [描述信息]
 * @fileName： com.fyqz.dc.service.impl.MindServiceImpl.java 
 * @createTime: 2014-8-1下午2:25:31
 * @creater: [创建人]
 * @editTime： 2014-8-1下午2:25:31
 * @modifier: [修改人]  
 */
@Service
public class MindServiceImpl implements MindService{
	
	@Autowired
	private MindDao mindDao;
	@Autowired
	private MindItemDao mindItemDao;
	@Autowired
	private MindAssociateDao mindAssociateDao;
	@Autowired
	private EvaluateIndexDao evaluateIndexDao;
	
	
	public void saveMind(String mindJson) throws Exception {
		
		Mind mind = new Mind();
		JSONObject mindData = new JSONObject(mindJson);
		JSONObject root = mindData.getJSONObject("root");
		String mindId = mindData.getString("id");
		JSONArray associate = mindData.getJSONArray("associate");
		List<MindItem> items = new ArrayList<MindItem>();
		
		if(null != root){
			MindItem item = saveItem(root);
			JSONArray children = root.getJSONArray("children");
			if(null != children){
				for(int i=0;i<children.length();i++){
					items.add(saveChildren(children.getJSONObject(i)));
				}
				item.setChildren(items);
			}
			mind.setRoot(item);
		}
		if(null != mindId){
			mind.setMindId(mindId);
		}
		if(null != associate){
			for(int i=0;i<associate.length();i++){
				MindAssociate mindAssociate = saveAssociate(associate.getJSONObject(i));
				if(null != mindAssociate){
					mindAssociate.setMind(mind);
				}
			}
		}
		mindDao.save(mind);
	}

	public String getMindJson(String mindId) throws Exception {
		
		
		return null;
	}

	public Mind getMind(String mindId) throws Exception {

		return null;
	}

	public void deleteMind(String mindId) throws Exception {

		
	}
	
	public MindItem saveChildren(JSONObject child)throws Exception{
		List<MindItem> items = new ArrayList<MindItem>();
		JSONArray children = child.getJSONArray("children");
		MindItem item = saveItem(child);
		if(null != children){
			for(int i=0;i<children.length();i++){
				items.add(saveChildren(children.getJSONObject(i)));
			}
			item.setChildren(items);
		}
		return item;
	}
	
	public MindItem saveItem(JSONObject child)throws Exception{
		MindItem item = new MindItem();
		if(null != child.getString("id")){
			item.setItemId(child.getString("id"));
		}
		if(null != child.getString("side")){
			item.setSide(child.getString("side"));
		}
		if(null != child.getString("text")){
			item.setText(child.getString("text"));
		}
		if(null != child.getString("color")){
			item.setColor(child.getString("color"));
		}
		if(null != child.getString("nodeColor")){
			item.setNodeColor(child.getString("nodeColor"));
		}
		if(null != child.getString("fontColor")){
			item.setFontColor(child.getString("fontColor"));
		}
		if(null != child.getString("tag")){
			item.setTag(child.getString("tag"));
		}
		if(null != child.getString("comment")){
			item.setComment(child.getString("comment"));
		}
		if(null != child.getString("value")){
			item.setValue(child.getString("value"));
		}
		if(null != child.getString("status")){
			item.setStatus(child.getString("status"));
		}
		if(null != child.getString("collapsed")){
			item.setCollapsed(child.getString("collapsed"));
		}
		if(null != child.getString("layout")){
			item.setLayout(child.getString("layout"));
		}
		if(null != child.getString("shape")){
			item.setShape(child.getString("shape"));
		}
		mindItemDao.save(item);
		return item;
	}
	
	public MindAssociate saveAssociate(JSONObject associate){
		MindAssociate mindAssociate = new MindAssociate();
		if(null != associate.getString("id")){
			mindAssociate.setAssociateId(associate.getString("id"));
		}
		if(null != associate.getString("from")){
			mindAssociate.setFromId(associate.getString("from"));
		}
		if(null != associate.getString("to")){
			mindAssociate.setToId(associate.getString("to"));
		}
		if(null != associate.getString("fromSide")){
			mindAssociate.setFromSide(associate.getString("fromSide"));
		}
		if(null != associate.getString("toSide")){
			mindAssociate.setToSide(associate.getString("toSide"));
		}
		if(null != associate.getString("color")){
			mindAssociate.setColor(associate.getString("color"));
		}
		/*if(null != associate.getJSONArray("positionPoints")){
			JSONArray positionPoints = associate.getJSONArray("positionPoints");
			List<Point> positions = new ArrayList<Point>();
			if(null != positionPoints){
				for(int i=0;i<positionPoints.length();i++){
					Point point = new Point();
					JSONObject positionPoint = positionPoints.getJSONObject(i);
					if(null != positionPoint.getString("X") && null != positionPoint.getString("Y")){
						point.setLocation(Double.parseDouble(positionPoint.getString("X")), Double.parseDouble(positionPoint.getString("Y")));
					}
					positions.add(point);
				}
			}
			mindAssociate.setPositionPoints(positions);
		}*/
		mindAssociateDao.save(mindAssociate);
		return mindAssociate;
	}
	
}
 