<!--
  @大成管理系统
  @file discuss_check_list.jsp
  @author: 夏选瑜
  @date: 2014-8-21
  @内容说明　大成研讨授权列表页
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
	<title>申请研发</title>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/top.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/bottom.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/public.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/empower.css"/>
	
	<script type="text/javascript" src="<%=path%>/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jBox/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jBox/i18n/jquery.jBox-zh-CN.js"></script>
    <script type="text/javascript" src="<%=path%>/js/simpleMessageBox.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jQueryValidation/jquery.validate.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jquery.form.js"></script>
    <script type="text/javascript" src="<%=path%>/js/common/OperResult.js"></script>
    <script type="text/javascript" src="<%=path%>/js/dc/page.js?v=1.1"></script>
    <script type="text/javascript" src="<%=path%>/js/dc/discuss_check_list.js?v=1.1"></script>
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
	<form id="pageInfo_id" action="<%=path %>/discuss_queryCheckDiscuss.action" method="post">
	<div class="content">
	<div class="dc_discontent">
	    <div class="dc_curt_1">
	        <div class="dc_curt_2">当前页面：研讨授权</div>
	    </div>
	    <span class="tab_em_title">
	    	研讨授权
	    </span>
		<table class="tab_em" cellpadding="0" cellspacing="0" border="0" bgcolor="#FFFFFF">
	    	<tr class="first_tr" bgcolor="#e9e9e9">
	            <td>研讨名称</td>
	            <td>研讨流程</td>
	            <td>研讨时间</td>
	            <td>主办单位</td>
	            <td>操作</td>
	        </tr>
	        <s:iterator var="item" value="discussList">
	        <tr>
	            <td>${item.name }</td>
	            <td>${item.deployment }</td>
	            <td>${item.insertDate }</td>
	            <td>${item.sponsor }</td>
	            <td><input class="btn_sq" type="button" value="授权" onclick="checkDetail(this);" name="${item.id }"/></td>
	        </tr>
	        </s:iterator>	        
	    </table>
	    <div class="em_paging">
	    	<a>共<span id="totalShow_id"></span>条数据</a>
	        <a>每页显示
	        <select id="sizeSelect_id">
	        	<option value=10>10</option>
	        	<option value=15>15</option>
	        	<option value=20>20</option>
	        </select>条</a>
	        <a href="#">当前显示第<span id="indexShow_id"></span>页</a>
	         <a href="javascript:void()" onclick="beforePage();">上一页</a>
	          <a href="javascript:void()" onclick="nextPage();">下一页</a>
	           <a>跳至第<select id="indexSelect_id"></select>页</a>
	           <input type="hidden" id="pageSize" name="discussForm.page.pageSize" value="${discussForm.page.pageSize }"/>
	           <input type="hidden" id="pageCount" name="discussForm.page.pageCount" value="${discussForm.page.pageCount }"/>
	           <input type="hidden" id="totalCount" name="discussForm.page.totalCount" value="${discussForm.page.totalCount }"/>
	           <input type="hidden" id="pageIndex" name="discussForm.page.pageIndex" value="${discussForm.page.pageIndex }"/>
	    </div>
	</div>
	</div>
	</form>	
<!---------底部------------->

	<div class="dc_bottom"><span>国防科学技术大学 版权所有 www.nudt.edu.cn</span></div>

</body>
</html>
