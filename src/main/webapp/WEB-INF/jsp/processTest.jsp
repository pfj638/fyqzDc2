<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    
    <title>simple process test</title>
    
	<script type="text/javascript" src="<%=path%>/js/jquery-1.8.0.min.js"></script>

  </head>
 <script type="text/javascript">
 	function complete(){
 		var processId = ${processId};
 		var taskId = ${taskId};
 		var data = "?taskId="+taskId;
 		$.ajax({
 			url:"<%=path%>/activiti_complete.action"+data,
 			type:"POST",//请求方式 ,get/post
			dataType:"json",//请求回来的数据格式
			success:function(data){//请求成功后的回调函数
				alert("success!");
			}
 		});
 	}
 	
 	function task(){
 		var processId = ${processId};
 		var data = "?processId="+processId;
 		window.location.href="<%=path%>/activiti_task.action"+data;
 	}
 </script> 
   <body>
	  <div id="div">
	    <table width="397" border="1" align="center">
	      <tr>
	        <td width="190">流程</td>
	        <td width="191">${processId }</td>
	      </tr>
	      <tr>
	        <td>任务</td>
	        <td>${taskName }</td>
	      </tr>
	      <tr>
	        <td>处理人</td>
	        <td>${userName }</td>
	      </tr>
	    </table>
	    <input id="complete" type="button" value="完成" onclick="complete()"/>
	    <input id="task" type="button" value="任务" onclick="task()"/>
	  </div>
	  
  </body>
</html>
