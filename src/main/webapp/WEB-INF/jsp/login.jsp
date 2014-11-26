<!--
  @大成管理系统
  @file login.jsp
  @author: 夏选瑜
  @date: 2014-8-21
  @内容说明　大成登录页
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>大成登录</title>
		<link type="text/css"  rel="stylesheet" href="<%=path%>/css/public.css" />
		<link type="text/css"  rel="stylesheet" href="<%=path%>/css/login.css" />
		<link type="text/css"  rel="stylesheet" href="<%=path%>/js/jBox/Skins/myStyle/jbox.css" />
        <script type="text/javascript" src="<%=path%>/js/jquery-1.8.0.min.js"></script>
        <script type="text/javascript" src="<%=path%>/js/jBox/jquery.jBox-2.3.min.js"></script>
        <script type="text/javascript" src="<%=path%>/js/simpleMessageBox.js"></script>
        <script type="text/javascript" src="<%=path%>/js/jQueryValidation/jquery.validate.min.js"></script>
        <script type="text/javascript" src="<%=path%>/js/common/OperResult.js?v=1.22"></script>
        <script type="text/javascript" src="<%=path%>/js/dc/login.js?v=1.22"></script>
		<style type="text/css">
	        .invalid {
	            color: #ea0000;
	        }
		</style>	
	</head>
	
	<body>
	<input type="hidden" id="urlPath" value="<%=path%>" />
	<form id="login_form_id">
		<div class="login_div">
		    <div class="logdiv_a">
		   		<div class="logdiv_a_form">
		        	<input title="请输入用户名!" name="userForm.loginName" maxlength="10"
							id="loginName" class="form_text userName_validate required" type="text" /><br />
		            <input style="margin-top:6px;" title="请输入密码!" name="userForm.password" maxlength="10" class="form_text required password_validate"
							id="password" type="password"/>
		            <input class="form_btn" id="loginBtn" type="button" />
		            <input class="form_btn" type="button" />
		            <div id="msg" style="width:220px;height:20px"></div>
		        </div>
		        
		    </div>
		</div>
	</form>	
	</body>
</html>
