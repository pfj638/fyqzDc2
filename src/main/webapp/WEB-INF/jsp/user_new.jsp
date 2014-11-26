<!--
  @大成管理系统
  @file user_new.jsp
  @author: 夏选瑜
  @date: 2014-8-21
  @内容说明　大成用户新建页
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>用户新增</title>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/top.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/bottom.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/public.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/discuss.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/js/jBox/Skins/myStyle/jbox.css"/>
	
	<script type="text/javascript" src="<%=path%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jBox/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jBox/i18n/jquery.jBox-zh-CN.js"></script>
    <script type="text/javascript" src="<%=path%>/js/simpleMessageBox.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jQueryValidation/jquery.validate.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jquery.form.js"></script>
    <script type="text/javascript" src="<%=path%>/js/common/OperResult.js"></script>
    <script type="text/javascript" src="<%=path%>/js/dc/user_new.js?v=1.1"></script>
    <style type="text/css">
        .invalid {
            color: #ea0000;
        }
    </style>
    <script type="text/javascript">
	$(function(){
		var $height=$(".dc_discontent").height();
		var $w_height=$(document).height()-146;
		$height=$w_height;
		$(".dc_discontent").css("height",$w_height);
		});
	</script>
</head>

	<body>
	<input type="hidden" id="rootPath" value="<%=path%>" />
	<!---------头部------------->
	<%@include file="/common/header.jsp" %>
	
	<!-------------------内容---------------------->
	<div class="content">
		<div class="dc_discontent">
		<form id="addForm">
		<div class="dc_curt_1">
		    <div class="dc_curt_2">当前页面：新增用户</div>
		</div>
		<span class="tab_dis_title">
	    	新增用户
	    </span>
	    <div class="dc_discontent_div1">
		<table class="tab_1"width="48%"  cellpadding="0" cellspacing="0"  align="center">
		  <tr>
		    <td class="first_td"  width="15%">真实姓名：</td>
		    <td width="85%">
		      <input class="text width_1 required" type="text" name="userForm.realName" id="textfield" />
		   </td>
		  </tr>
		  <tr>
		    <td class="first_td "  width="15%">注册名：</td>
		    <td width="85%">
		      <input class="text width_1 title_validate required name_check" type="text" name="userForm.loginName" id="textfield1" />
		   </td>
		  </tr>
		  <tr>
		    <td class="first_td">性别：</td>
		    <td>
		      <select class="text width_1"  name="userForm.sex" size="1" id="select" style="width:387px">
		        <option value="m">男</option>
		        <option value="f">女</option>
		      </select>
		    </td>
		  </tr>
		  <tr>
		    <td class="first_td">用户类型：</td>
		    <td>
		      <select class="text width_1"  name="userForm.userType" size="1" id="userType_id" style="width:387px">
		      </select>
		    </td>
		  </tr>
		  <tr>
		    <td class="first_td required">部职别：</td>
		    <td><input class="text required" type="text" name="userForm.jobTitle" id="startDate_id" /></td>
		  </tr>
		  <tr>
		    <td class="first_td">职称：</td>
		    <td>
		    <select class="text width_1"  name="userForm.professionalTitle" size="1" id="professionalTitle_id" style="width:387px">
		    </select>
		     </td>
		  </tr>
		  <tr>
		    <td class="first_td">届数：</td>
		    <td>
		    <select class="text width_1"  name="userForm.year" size="1" id="year_id" style="width:387px">
		        <option value="2010">2010</option>
		        <option value="2011">2011</option>
		        <option value="2012">2012</option>
		        <option value="2013">2013</option>
		        <option selected=true value="2014">2014</option>
		        <option value="2015">2015</option>
		        <option value="2016">2016</option>
		        <option value="2017">2017</option>
		        <option value="2018">2018</option>
		     </select>
		    </td>
		  </tr>
		  <tr>
		    <td class="first_td">班级：</td>
		    <td>
		    <select class="text width_1"  name="userForm.grade" size="1" id="grade_id" style="width:387px">
		        <option value="1">教师</option>
		        <option value="2">学生</option>
		     </select>
		    </td>
		  </tr>
		  <tr>
		    <td class="first_td">邮箱：</td>
		    <td>
			<input class="text required email" type="text" name="userForm.email" id="textfield5" />
		    </td>
		  </tr>
		</table>
		</div>
		</form>
		<input class="submit_btn btn" type="button" id="addUser" value="保 存"/>
		</div>
	</div>
	<!---------底部------------->
	<div class="dc_bottom"><span>国防科学技术大学 版权所有 www.nudt.edu.cn</span></div>
	
	</body>
</html>
