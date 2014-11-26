<!--
  @大成管理系统
  @file discuss_check.jsp
  @author: 夏选瑜
  @date: 2014-8-21
  @内容说明　大成研讨授权页
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
	<title>研讨申请</title>
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
    <script type="text/javascript" src="<%=path%>/js/dc/discuss_check.js?v=1.1"></script>
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
		<form id="checkForm">
		<input type="hidden" name="discussForm.discussId" value="${requestScope.discuss.id }"/>
		<div class="dc_curt_1">
		    <div class="dc_curt_2">当前页面：研讨授权</div>
		</div>
		<table class="tab_1"width="48%"  cellpadding="0" cellspacing="0"  align="center">
		  <tr height="29" style="border-radius:5px;">
		    <td class="first_td" id="first_tr" colspan="2">研讨授权</td>
		    </tr>
		  <tr>
		    <td class="first_td"  width="15%">研讨名称：</td>
		    <td width="85%">
		      <span>${requestScope.discuss.name }</span>
		   </td>
		  </tr>
		  <tr>
		    <td class="first_td">研讨流程：</td>
		    <td>
		      <span>${requestScope.discuss.deployment }</span>
		    </td>
		  </tr>
		  <tr>
		    <td class="first_td">研讨时间：</td>
		    <td><span>${requestScope.discuss.insertDate }</span></td>
		  </tr>
		  <tr>
		    <td class="first_td">主办单位：</td>
		    <td><span>${requestScope.discuss.sponsor }</span></td>
		  </tr>
		  <tr>
		    <td class="first_td">地点：</td>
		    <td><span>${requestScope.discuss.address }</span></td>
		  </tr>
		  <tr>
		    <td class="first_td">研讨管理员：</td>
		    <td><span>${requestScope.discuss.proposer.loginName }</span></td>
		  </tr>
		  <tr>
		    <td class="first_td">概述：</td>
		    <td><span>${requestScope.discuss.discussDesc }</span></td>
		  </tr>
		  <tr>
		    <td class="first_td">参与人员：</td>
		    <td>
		    <table id="participateList_id" class="tab_2" cellpadding="0" cellspacing="0">
		      <tr class="tab_2_firsttr">
		        <td>姓名</td>
		        <td>部职别</td>
		        <td>类别</td>
		        <td>职称</td>
		        <td>角色</td>
		      </tr>
		      <s:iterator var="items" value="participateList">
		      <tr>
		        <td>${items.user.loginName }</td>
		        <td>${items.user.jobTitle }</td>
		        <td>${items.user.type.name }</td>
		        <td>${items.user.professionalTitle.name }</td>
		        <td>${items.discussRole.roleDesc }</td>
		      </tr>
		      </s:iterator>
		    </table>
		    </td>
		  </tr>
		  <tr>
		  <td class="first_td">授权意见：</td>
		  <td><textarea class="text_area" name="discussForm.opinion"></textarea></td>
		  </tr>
		  <tr><td></td><td><input type="button" id="checkAgree_id" value="同 意"/><input type="button" value="驳 回" id="checkRefuse_id"/></td></tr>
		</table>
		<input type="hidden" id="checkType" name="discussForm.checkType"/>
		</form>
		</div>
	</div>
	<!---------底部------------->
	<div class="dc_bottom"><span>国防科学技术大学 版权所有 www.nudt.edu.cn</span></div>
	
	</body>
</html>
