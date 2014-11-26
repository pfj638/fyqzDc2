<!--
  @大成管理系统
  @file discuss_init_list.jsp
  @author: 林明阳
  @date: 2014-8-21
  @内容说明　案例研讨初始化页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>案例研讨初始化</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link type="text/css" rel="stylesheet"	href="<%=path%>/css/top.css" />
<link type="text/css" rel="stylesheet"	href="<%=path%>/css/bottom.css" />
<link type="text/css" rel="stylesheet"	href="<%=path%>/css/public.css" />
<link type="text/css" rel="stylesheet"	href="<%=path%>/css/index.css" />
<link type="text/css" rel="stylesheet"	href="<%=path%>/css/discuss.css"  />
<link type="text/css" rel="stylesheet"	href="<%=path%>/css/init.css" />
<link type="text/css" rel="stylesheet"	href="<%=path%>/js/jBox/Skins/myStyle/jbox.css" />
<link type="text/css" rel="stylesheet"  href="styles.css">

<script type="text/javascript" src="<%=path%>/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=path%>/js/common/OperResult.js"></script>
<script type="text/javascript" src="<%=path%>/js/jBox/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="<%=path%>/js/jBox/i18n/jquery.jBox-zh-CN.js"></script>
<script type="text/javascript" src="<%=path%>/js/jquery.form.js"></script>
<script type="text/javascript" src="<%=path%>/js/simpleMessageBox.js"></script>
<script type="text/javascript" src="<%=path%>/js/dc/discuss_init_add.js?v=1.0"></script>
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
	<%@include file="/common/header.jsp"%>
	<!-------------------内容---------------------->
  <div class="content">
	<div class="dc_discontent">
		<div class="dc_curt_1">
			<div class="dc_curt_2">当前页面：研讨初始化</div>
		</div>
		<span class="tab_init_title"> SWOT研讨初始化 </span>
		<div class="init_div1">
			<div class="steps01">
				<form id="uploadForm" method="post" enctype="multipart/form-data">
					<font>背景知识：</font> 
					<input type="text" id="textfield" class="txt"	value="请选择上传文件" /> <input type="button" class="btn" value="选择文件" />
					<input name="discussManagerForm.upload" type="file" class="file" id="file" 	onchange="document.getElementById('textfield').value=this.value" size="60" /> 
					<input id="uploadBtn" type="button" value="上传" 	class="btn_fasong" />
			</div>
			</form>
			<table id="discussUpload" class="init_tab" border="0" cellpadding="0" cellspacing="0">
				<tr class="first_tr" id="dt">
					<td >文件名称</td>
					<td>操作</td>
				</tr>
			</table>

			<form id="discussAddForm" method="post"	action="<%=path%>/bms/discuss_addInitDiscuss.action" enctype="multipart/form-data">
				<input type="hidden" name="discussManagerForm.discussId" id="discussId" value="${discussManagerForm.discussId}" />
				<input type="hidden" name="discussManagerForm.type" value="${discussManagerForm.type}" />
				<ul class="init_ul1">
					<li>思维导图：</li>
					<li id="swdt"></li>
				</ul>
				<ul class="init_ul1">
					<li style="width:300px">&nbsp</li>
					<li><input type="button" value="下一步" class="btn_fasong"
						onclick="initDiscussAdd()" />
					</li>
				</ul>
			</form>

			<div style="clear:both"></div>
		</div>
	</div>
	</div>
	<!---------底部------------->

	<div class="dc_bottom">
		<span>国防科学技术大学 版权所有 www.nudt.edu.cn</span>
	</div>

</body>
</html>
