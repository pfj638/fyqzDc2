package com.fyqz.dc.action;  

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import com.fyqz.dc.common.BaseAction;
 
/**  
 * @description: [描述信息]
 * @fileName： com.fyqz.dc.action.ActivitiAction.java 
 * @createTime: 2014-6-26上午11:04:42
 * @creater: [创建人]
 * @editTime： 2014-6-26上午11:04:42
 * @modifier: [修改人]  
 */
public class ActivitiAction extends BaseAction{
	
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private FormService formService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ManagementService managementService;
	
	private String processId;
	private String processName;
	private String taskId;
	private String taskName;
	private String userName;
	
	public String start(){
		
		userName = this.getLoginUser().getLoginName();
		List<User> expertList = new ArrayList<User>();
		Group agroup = identityService.newGroup("expert");
		identityService.saveGroup(agroup);
		
		User auser = identityService.createUserQuery().userId("expert1").singleResult();
		if(auser == null){			
			auser = identityService.newUser("expert1");
			identityService.saveUser(auser);
		}
		expertList.add(auser);
		//identityService.createMembership(auser.getId(), "expert");
		
		auser = identityService.createUserQuery().userId("expert2").singleResult();
		if(auser == null){			
			auser = identityService.newUser("expert2");
			identityService.saveUser(auser);
		}
		expertList.add(auser);
		//identityService.createMembership(auser.getId(), "expert");
		
		auser = identityService.createUserQuery().userId("host").singleResult();
		if(auser == null){			
			auser = identityService.newUser("host");
			identityService.saveUser(auser);
		}
		
		Map<String,Object> variables = new HashMap<String,Object>();
		variables.put("expertList", expertList);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("swot", variables);
		processId = processInstance.getId();
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
		if(tasks.size() != 1){
			for(int i=0;i<tasks.size();i++){
				taskService.claim(tasks.get(i).getId(), expertList.get(i).getId());
			}
		}
			taskName="null";
			taskId="null";
		for(Task task : tasks){
			if(userName.equals(task.getAssignee())){
				taskName=task.getName();
				taskId=task.getId();
			}
		}
		return "start";
	}
	
	public String complete(){
		if(null != taskId){
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			if(null != task){
				
				taskService.complete(taskId);
			}
		}		
		return null;
	}
	
	public String task(){
		userName = this.getLoginUser().getLoginName();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
		if(null != processInstance){
			processId = processInstance.getId();
			Map<String,Object> v=runtimeService.getVariables(processInstance.getProcessInstanceId());
			Map<String,Object> variables = processInstance.getProcessVariables();
			List<User> expertList= (List<User>)v.get("expertList");
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processId).list();
			if(tasks.size() != 1){
				for(int i=0;i<tasks.size();i++){
					taskService.claim(tasks.get(i).getId(), expertList.get(i).getId());
				}
			}
			taskName="null";
			taskId="null";
			for(Task task : tasks){
				if(userName.equals(task.getAssignee())){
					taskName=task.getName();
					taskId=task.getId();
				}
			}
			return "start";
		}else{
			List<Group> gList = identityService.createGroupQuery().list();
			for(Group g : gList){				
				identityService.deleteGroup(g.getId());
			}
			return "list";
		}
		
	}
	
	public String testProcessEngines(){
		List<User> assigneeList = new ArrayList<User>();
		List<User> a= identityService.createUserQuery().userId("xxy").list();		
		if(a.size()==0 ){
			User auser = identityService.newUser("xxy");
			identityService.saveUser(auser);
			assigneeList.add(auser);
		}else{
			assigneeList.add(a.get(0));
		}
		List<User> a1= identityService.createUserQuery().userId("ty").list();		
		if(a1.size()==0 ){
			User auser = identityService.newUser("ty");
			identityService.saveUser(auser);
			assigneeList.add(auser);
		}else{
			assigneeList.add(a1.get(0));
		}
		List<Group> g= identityService.createGroupQuery().groupId("expert").list();
		if(g.size()==0){
			Group agroup = identityService.newGroup("expert");
			identityService.saveGroup(agroup);
		}
		if(a.size() == 0){
			
			identityService.createMembership("xxy", "expert");
		}
		if(a1.size() == 0){
			
			identityService.createMembership("ty", "expert");
		}
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().list().get(0);
		String key = processDefinition.getKey();

		Map<String,Object> variables = new HashMap<String,Object>();
		variables.put("assigneeList", assigneeList);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, variables);
		List<Task> tasks = taskService.createTaskQuery().taskAssignee("xxy").list();
		List<Task> tasks1 = taskService.createTaskQuery().taskCandidateUser("xxy").list();
		List<Task> tasks2 = taskService.createTaskQuery().taskCandidateGroup("expert").list();
		taskService.claim(tasks2.get(0).getId(), "xxy");
		taskService.claim(tasks2.get(1).getId(), "ty");
		for (Task task : tasks) {
		    log.info("Task available: {}", task.getName());
		}
		taskService.complete(tasks2.get(0).getId());
		taskService.complete(tasks2.get(1).getId());
		return null;
	}
	
	public String testSwot(){
		List<User> expertList = new ArrayList<User>();
		List<User> a= identityService.createUserQuery().userId("host").list();		
		if(a.size()==0 ){
			User auser = identityService.newUser("host");
			identityService.saveUser(auser);
		}
		List<User> a1= identityService.createUserQuery().userId("xxy").list();		
		if(a1.size()==0 ){
			User auser = identityService.newUser("xxy");
			identityService.saveUser(auser);
			expertList.add(auser);
		}else{
			expertList.add(a1.get(0));
		}
		List<User> a2= identityService.createUserQuery().userId("ty").list();		
		if(a2.size()==0 ){
			User auser = identityService.newUser("ty");
			identityService.saveUser(auser);
			expertList.add(auser);
		}else{
			expertList.add(a2.get(0));
		}
		List<Group> g= identityService.createGroupQuery().groupId("expert").list();
		if(g.size()==0){
			Group agroup = identityService.newGroup("expert");
			identityService.saveGroup(agroup);
		}
		if(a.size() == 0){
			
			identityService.createMembership("xxy", "expert");
		}
		if(a1.size() == 0){
			
			identityService.createMembership("ty", "expert");
		}
		
		
		Map<String,Object> variables = new HashMap<String,Object>();
		variables.put("expertList", expertList);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("swot", variables);
		List<Task> tasks = null;
		List<Task> tasks2 = null;
		do{
			tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee("host").list();
			tasks2 = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskCandidateGroup("expert").list();
			for (int i=0;i<tasks2.size();i++) {
				log.info("Task available:{} " ,tasks2.get(i).getName());
				taskService.claim(tasks2.get(i).getId(), expertList.get(i).getId());
			}
			for(int i=0;i<tasks2.size();i++){
				taskService.complete(tasks2.get(i).getId());
			}
			for(Task task : tasks){
				log.info("Task available: {}" ,task.getName());
				taskService.complete(task.getId());
			}			
		}while(tasks.size() != 0 && tasks2.size() != 0);
		
		return null;
	}

	
	
	public String getProcessId() {
	
		return processId;
	}

	
	public void setProcessId(String processId) {
	
		this.processId = processId;
	}

	
	public String getProcessName() {
	
		return processName;
	}

	
	public void setProcessName(String processName) {
	
		this.processName = processName;
	}
	
	public String getTaskId() {
	
		return taskId;
	}

	
	public void setTaskId(String taskId) {
	
		this.taskId = taskId;
	}

	public String getTaskName() {
	
		return taskName;
	}

	
	public void setTaskName(String taskName) {
	
		this.taskName = taskName;
	}

	
	public String getUserName() {
	
		return userName;
	}

	
	public void setUserName(String userName) {
	
		this.userName = userName;
	}
	
}
 