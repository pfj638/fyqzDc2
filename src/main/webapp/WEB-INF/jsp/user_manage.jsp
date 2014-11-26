<!--
  @大成管理系统
  @file user_manage.jsp
  @author: 夏选瑜
  @date: 2014-8-21
  @内容说明　大成用户管理页
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
	<title>用户维护</title>
<link type="text/css" rel="stylesheet" href="<%=path%>/css/top.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/bottom.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/public.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/discuss.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/empower.css" />
	<link type="text/css" rel="stylesheet" href="<%=path%>/js/jBox/Skins/myStyle/jbox.css"/>
	
	<script type="text/javascript" src="<%=path%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jBox/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jBox/i18n/jquery.jBox-zh-CN.js"></script>
    <script type="text/javascript" src="<%=path%>/js/simpleMessageBox.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jQueryValidation/jquery.validate.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jquery.form.js"></script>
    <script type="text/javascript" src="<%=path%>/js/common/OperResult.js"></script>
    <script type="text/javascript" src="<%=path%>/js/dc/page.js"></script>
    <script type="text/javascript" src="<%=path%>/js/dc/user_manage.js?v=1.1"></script>
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
	<div class="dc_discontent">
	    <div class="dc_curt_1">
	        <div class="dc_curt_2">当前页面：用户维护</div>
	    </div>
	    <span class="tab_em_title">
	    	用户维护
	    </span>
	<form id="query_id" action="<%=path %>/user_queryUserByPage.action" method="post">
	    <table class="tab_em" cellpadding="0" cellspacing="0" border="0" bgcolor="#FFFFFF">
	    	<tr class="first_tr" bgcolor="#e9e9e9">
	            <td colspan="4">查询条件</td>
	        </tr>
	        <tr>
	        	<td>真实姓名</td>
	            <td><input type="text" id="realName_id" name="userForm.realName" style="width:187px"/></td>
	            <td>性别</td>
	            <td>
	            <select class="text width_1"  name="userForm.sex" size="1" id="sex_id" style="width:187px">
		        	<option selected=true value="">全部</option>
		        	<option value="m">男</option>
		        	<option value="f">女</option>
		      	</select>
		      </td>
	        </tr>
	        <tr>
	            <td>用户类别</td>
	            <td>
	            <select class="text width_1"  name="userForm.userType" size="1" id="userType_id" style="width:187px">
	            	<option selected=true value="">全部</option>
		      	</select>
		      	</td>
	            <td>职称</td>
	            <td>
	            <select class="text width_1"  name="userForm.professionalTitle" size="1" id="professionalTitle_id" style="width:187px">
	            	<option selected=true value="">全部</option>
		    	</select>
	            </td>
	        </tr>
	        <tr class="first_tr" bgcolor="#e9e9e9">
	            <td colspan="2"><input class="choose_btn" id="userSelect_id" type="button" value="查询" /></td>
	            <td colspan="2"><input class="choose_btn" id="addUser_id" type="button" value="新增" /></td>
	        </tr>
	    </table>
	    <br/>
		<table class="tab_em" cellpadding="0" cellspacing="0" border="0" bgcolor="#FFFFFF">
	    	<tr class="first_tr" bgcolor="#e9e9e9">
	            <td>真实姓名</td>
	            <td>注册名</td>
	            <td>性别</td>
	            <td>用户类型</td>
	            <td>职称</td>
	            <td>操作</td>
	        </tr>
	        <s:iterator var="item" value="userList">
	        <tr>
	            <td>${item.realName }</td>
	            <td>${item.loginName }</td>
	            <td>${item.sex }</td>
	            <td>${item.type.name }</td>
	            <td>${item.professionalTitle.name }</td>
	            <td>
	            	<input class="btn_sq" type="button" onclick="modify(this);" value="修改" name="${item.id }"/> 
	            	<input class="btn_sq" type="button" onclick="deleteUser(this)" value="删除" name="${item.id }"/>
	            	<input class="btn_sq" type="button" onclick="roleSetup(this)" value="角色设置" name="${item.id }"/>
	            </td>
	        </tr>
	        </s:iterator>
	    </table>
	    </form>
	    <form id="pageInfo_id" action="<%=path %>/user_queryUserByPage.action" method="post">
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
	           <input type="hidden" id="pageSize" name="userForm.page.pageSize" value="${userForm.page.pageSize }"/>
	           <input type="hidden" id="pageCount" name="userForm.page.pageCount" value="${userForm.page.pageCount }"/>
	           <input type="hidden" id="totalCount" name="userForm.page.totalCount" value="${userForm.page.totalCount }"/>
	           <input type="hidden" id="pageIndex" name="userForm.page.pageIndex" value="${userForm.page.pageIndex }"/>
	           <input type="hidden" id="condition" name="userForm.page.condition" value='${userForm.page.condition }'/>
	    </div>
	    </form>
	</div>
	</div>	
<!---------底部------------->

	<div class="dc_bottom"><span>国防科学技术大学 版权所有 www.nudt.edu.cn</span></div>

</body>
</html>
