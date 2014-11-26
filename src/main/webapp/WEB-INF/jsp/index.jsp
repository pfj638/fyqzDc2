<!--
  @大成管理系统
  @file index.jsp
  @author: 夏选瑜
  @date: 2014-8-21
  @内容说明　大成系统主页
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>首页</title>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/top.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/bottom.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/public.css"/>
	<link type="text/css" rel="stylesheet" href="<%=path%>/css/index.css"/>
	<script type="text/javascript" src="<%=path%>/js/jquery-1.8.0.min.js"></script>
	<script type="text/javascript">
	$(function(){
		var $height=$(".content_index").height();
		var $w_height=$(document).height()-296;
		$height=$w_height;
		$(".content_index").css("height",$w_height);
		})
	</script>
</head>

	<body>
	<!---------头部------------->
	<%@include file="/common/header.jsp" %>
	<div class="dc_banner">
		<div class="dc_banner_div1">
    		<img src="image/dc_banner.jpg" />
		</div>
	</div>
	<!-------------------内容---------------------->
	<div class="content">

	<div class="content_index">
		<div class="content_index_div1" style="float:left;"><span>基于SWOT分析方法的战略研讨</span></div>
    	<div class="content_index_div2" style="float:right;"><span>作战能力风险与装备发展评估研究</span></div>
    	<div class="content_index_div3" style="float:left;"><span>基于德尔菲调查和科学计量的国防科技预见</span></div>
    	<div class="content_index_div4" style="float:right;"><span>基于探索性分析的作战体系能力评估</span></div>

	</div>
	</div>
	<!---------底部------------->
	<div class="dc_bottom">国防科学技术大学 版权所有 www.nudt.edu.cn</div>
	
	</body>
</html>
