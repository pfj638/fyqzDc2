<!--
  @大成管理系统
  @file discuss_init_attend.jsp
  @author: 林明阳
  @date: 2014-8-21
  @内容说明　案例研讨加入页面
-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%
String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>参与案例研讨</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	
	<link type="text/css" rel="stylesheet" href="<%=path %>/css/top.css"  />
	<link type="text/css" rel="stylesheet" href="<%=path %>/css/bottom.css"  />
	<link type="text/css" rel="stylesheet" href="<%=path %>/css/public.css" />
	<link type="text/css" rel="stylesheet" href="<%=path %>/css/empower.css" />
	
    <script type="text/javascript" src="<%=path%>/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/dc/page.js?v=1.1"></script>
    <script type="text/javascript" src="<%=path%>/js/dc/discussInit.js"></script>
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
  <%@include file="/common/header.jsp"%>
    <div class="content">
    <form id="discussForm" method="post" action="<%=path%>/bms/discuss_toInitDiscuss.action"   enctype="multipart/form-data">
        <input type="hidden" name="discussManagerForm.discussId" id="discussId" />
        <input type="hidden" name="discussManagerForm.type" value="${discussManagerForm.type}" />
    </form>
    <form id="pageInfo_id" method="post" action="<%=path%>/bms/discuss_discussAttendList.action"   enctype="multipart/form-data">
        <input type="hidden" name="discussManagerForm.type" value="${discussManagerForm.type}" />
	  <div class="dc_discontent">
	    <div class="dc_curt_1">
	        <div class="dc_curt_2">当前页面：参与研讨</div>
	    </div>
	    <span class="tab_em_title">
	    	参与研讨
	    </span>
	    <table class="tab_em" cellpadding="0" cellspacing="0" border="0" bgcolor="#FFFFFF">
	     <tr class="first_tr" bgcolor="#e9e9e9">
	      <td>研讨名称</td>
	      <td>研讨流程</td>
	      <td>研讨时间</td>
	      <td>主办单位</td>
	      <td>研讨管理员</td>
	      <td>状态</td>
	      <td>操作</td>
	     </tr>
		    <c:forEach var="list" items="${list}" >
		      <tr>
		              <td>${list.name}</td>
				      <td>${list.deployment}</td>
				      <td><fmt:formatDate value="${list.insert_date}" pattern="yyyy-MM-dd HH-mm"/></td>
				      <td>${list.sponsor}</td>
				      <td>${list.real_name}</td>
				      <td>${list.status}</td>
				      <td>
				      <input class="btn_sq" type="button" value="参与"  onclick="initDiscussAttend('${list.id}');" />
						
					  </td>
		     </tr>
		    </c:forEach> 
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
				           <input type="hidden" id="pageSize" name="discussManagerForm.page.pageSize" value="${discussManagerForm.page.pageSize }"/>
		           <input type="hidden" id="pageCount" name="discussManagerForm.page.pageCount" value="${discussManagerForm.page.pageCount }"/>
		           <input type="hidden" id="totalCount" name="discussManagerForm.page.totalCount" value="${discussManagerForm.page.totalCount }"/>
		           <input type="hidden" id="pageIndex" name="discussManagerForm.page.pageIndex" value="${discussManagerForm.page.pageIndex }"/>
		            </div>
	</div>
	</form>

</div>
<!---------底部------------->

	<div class="dc_bottom"><span>国防科学技术大学 版权所有 www.nudt.edu.cn</span></div>
  </body>
</html>
