<!--
  @大成管理系统
  @file user_role_setup.jsp
  @author: 夏选瑜
  @date: 2014-8-21
  @内容说明　大成用户的角色设置页
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>用户角色设置</title>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/top.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/bottom.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/public.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/empower.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/js/jBox/Skins/myStyle/jbox.css"/>
	
	<script type="text/javascript" src="<%=path%>/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jBox/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jBox/i18n/jquery.jBox-zh-CN.js"></script>
    <script type="text/javascript" src="<%=path%>/js/simpleMessageBox.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jQueryValidation/jquery.validate.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jquery.form.js"></script>
    <script type="text/javascript" src="<%=path%>/js/common/OperResult.js"></script>
    <script type="text/javascript" src="<%=path%>/js/dc/user_role_setup.js?v=1.1"></script>
    <script type="text/javascript">
	$(function(){
		var $height=$(".dc_discontent").height();
		var $w_height=$(document).height()-166;
		$height=$w_height;
		$(".dc_discontent").css("height",$w_height);
		});
	</script>
</head>

<body>
<input type="hidden" id="rootPath" value="<%=path%>" />
	<!---------头部------------->
	<%@include file="/common/header.jsp" %>
	<div class="content">
	<form id="setupForm">
	<div class="dc_discontent">
	    <div class="dc_curt_1">
	        <div class="dc_curt_2">当前页面：角色设置</div>
	    </div>
	    <span class="tab_em_title">
	    	角色设置
	    </span>
		<table class="tab_em" id="roleList_id" cellpadding="0" cellspacing="0" border="0" bgcolor="#FFFFFF">
	    	<tr class="first_tr" bgcolor="#e9e9e9">
	            <td></td>
	            <td>角色名称</td>
	            <td>描述</td>
	        </tr>
	        <tr>
	            <td><input type="radio" name="userRole"/></td>
	            <td>系统管理员</td>
	            <td>维护系统安全</td>
	        </tr>
	        <tr>
	            <td><input type="radio" name="userRole" /></td>
	            <td>研讨管理员</td>
	            <td>申请研讨</td>
	        </tr>
	        <tr>
	            <td><input type="radio" name="userRole"/></td>
	            <td>普通用户</td>
	            <td>常看看看</td>
	        </tr>
	    </table>
	    <div align="center"><input type="button" id="setupRole_id" value="保存"/></div>
	    <input type="hidden" name="userForm.userId" value="${requestScope.user.id }"/>
	    <input type="hidden" id="userRoleInfo" value="${requestScope.user.role.id }"/>
	</div>
	</form>
	</div>
<!---------底部------------->

	<div class="dc_bottom"><span>国防科学技术大学 版权所有 www.nudt.edu.cn</span></div>

</body>
</html>
