<!--
  @大成管理系统
  @file role_modify.jsp
  @author: 夏选瑜
  @date: 2014-8-21
  @内容说明　大成角色修改页
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>角色新增</title>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/top.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/bottom.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/public.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/discuss.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/js/jBox/Skins/myStyle/jbox.css"/>
	
	<script type="text/javascript" src="<%=path%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jBox/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jBox/i18n/jquery.jBox-zh-CN.js"></script>
    <script type="text/javascript" src="<%=path%>/js/simpleMessageBox.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jQueryValidation/jquery.validate.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jquery.form.js"></script>
    <script type="text/javascript" src="<%=path%>/js/common/OperResult.js"></script>
    <script type="text/javascript" src="<%=path%>/js/dc/role_modify.js?v=1.1"></script>
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
		<form id="modifyForm">
		<div class="dc_curt_1">
		    <div class="dc_curt_2">当前页面：新增角色</div>
		</div>
		<table class="tab_1"width="48%"  cellpadding="0" cellspacing="0"  align="center">
		  <tr height="29" style="border-radius:5px;">
		    <td class="first_td" id="first_tr" colspan="2">新增角色</td>
		    </tr>
		  <tr>
		    <td class="first_td"  width="15%">角色名称：</td>
		    <td width="85%">
		      <input class="text width_1 required" type="text" name="roleForm.name" id="textfield" value="${requestScope.role.name }"/>
		   </td>
		  </tr>
		  <tr>
		    <td class="first_td">描述：</td>
		    <td>
		    <textarea class="required" rows="" cols="" name="roleForm.roleDesc" style="width:387px;">${requestScope.role.roleDesc }</textarea>
		    </td>
		  </tr>
		  <tr><td></td><td><input  type="button" id="modifyRole" value="保 存"/></td></tr>
		</table>
		<input type="hidden" name="roleForm.roleId" value="${requestScope.role.id}" />
		</form>
		</div>
	</div>
	<!---------底部------------->
	<div class="dc_bottom"><span>国防科学技术大学 版权所有 www.nudt.edu.cn</span></div>
	
	</body>
</html>
