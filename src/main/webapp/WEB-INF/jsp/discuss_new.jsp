<!--
  @大成管理系统
  @file discuss_new.jsp
  @author: 夏选瑜
  @date: 2014-8-21
  @内容说明　大成研讨申请页
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
	<link rel="stylesheet" type="text/css" href="<%=path%>/js/jBox/Skins/myStyle/jbox.css"/>
	
	<script type="text/javascript" src="<%=path%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jBox/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jBox/i18n/jquery.jBox-zh-CN.js"></script>
    <script type="text/javascript" src="<%=path%>/js/simpleMessageBox.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jQueryValidation/jquery.validate.min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jquery.form.js"></script>
    <script type="text/javascript" src="<%=path%>/js/common/OperResult.js"></script>
    <script type="text/javascript" src="<%=path%>/js/dc/discuss_new.js?v=1.1"></script>
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

	<body style="position:relative;">
	<input type="hidden" id="rootPath" value="<%=path%>" />
	<!---------头部------------->
	<%@include file="/common/header.jsp" %>
	
	<!-------------------内容---------------------->
	<div class="content">
	<div class="dc_discontent" >
		<form id="addForm">
		<div class="dc_curt_1">
		    <div class="dc_curt_2">当前页面：研讨申请</div>
		</div>
		<span class="tab_dis_title">
	    	研讨申请
	    </span>
	    <div class="dc_discontent_div1">
		<table class="tab_1" width="48%"  cellpadding="0" cellspacing="0"  align="center">
		  <tr>
		    <td class="first_td"  width="15%">研讨名称：</td>
		    <td width="85%">
		      <input class="text width_1 title_validate required" type="text" name="discussForm.name" id="textfield" />
		   </td>
		  </tr>
		  <tr>
		    <td class="first_td">研讨流程：</td>
		    <td>
		      <select class="text width_1"  name="discussForm.deployment" size="1" id="select" style="width:387px">
		        <option value="swot">基于swot分析方法的战略研究</option>
		        <option value="equip">作战能力风险与装备发展评估研讨</option>
		      </select>
		    </td>
		  </tr>
		  <tr>
		    <td class="first_td">研讨时间：</td>
		    <td><input class="text c_hui Wdate required" type="text" name="discussForm.insertDate" title="请选择案例开始时间" id="startDate_id" /></td>
		  </tr>
		  <tr>
		    <td class="first_td">主办单位：</td>
		    <td><input class="text required" type="text" name="discussForm.sponsor" id="textfield3" /></td>
		  </tr>
		  <tr>
		    <td class="first_td">地点：</td>
		    <td><input class="text required" type="text" name="discussForm.address" id="textfield4" /></td>
		  </tr>
		  <tr>
		    <td class="first_td">研讨管理员：</td>
		    <td><span>${sessionScope.user.loginName }</span></td>
		  </tr>
		  <tr>
		    <td class="first_td">概述：</td>
		    <td><textarea class="text_area description_validate" name="discussForm.discussDesc" style="resize:none"></textarea></td>
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
		      <tr>
		        <td></td>
		        <td></td>
		        <td></td>
		        <td></td>
		        <td></td>
		      </tr>
		    </table>
			<input class="choose_btn"  type="button" value="选择人员" onclick="show()"/>
		    </td>
		  </tr>
		</table>
		</div>
		</form>
		<div class="tab_3" id="tab_3">
		    <div class="tab_3_title">
		    	<font> 查询</font>
		    	<span>
		        	<input type="text" class="text_search"  />
		            <input type="button" class="btn_search"/>
		    	</span>
		         <img class="close_tab3" src="image/feedback_bad.gif" onclick="hiden_tab()"/>
		    </div>
		    <div style="overflow-x:hidden;height:200px;width:95%;float:left;margin:auto;margin-top:10px;text-align:center;"> 
		    <table id="userList_id" width="335px" border="0" cellpadding="0" cellspacing="0"  align="center"  frame="void" style="text-align:center" >
			  <tr>
			    <td width="20%">
			      <input type="checkbox" name="checkbox" id="selectAll_check" /></td>
			    <td width="20%">姓名</td>
			    <td width="20%">部职别</td>
			    <td width="20%">职称</td>
			    <td width="20%">类型</td>
			  </tr>
			  <tr>
			    <td><input type="checkbox" name="checkbox2" id="checkbox2" /></td>
			    <td>张三</td>
			    <td>教师</td>
			  </tr>
			  <tr>
			    <td><input type="checkbox" name="checkbox2" id="checkbox2" /></td>
			    <td>张三</td>
			    <td>教师</td>
			  </tr>
			  <tr>
			    <td><input type="checkbox" name="checkbox2" id="checkbox2" /></td>
			    <td>张三</td>
			    <td>教师</td>
			  </tr>
			  <tr>
			    <td><input type="checkbox" name="checkbox2" id="checkbox2" /></td>
			    <td>张三</td>
			    <td>教师</td>
			  </tr>
			</table> 
			</div>
			<div style="text-align: center;"><input type="button" value="确 定" id="userSelect_id" style="background:url(../image/dis_btn01.gif) no-repeat;" /></div>
		</div>
	<input class="submit_btn btn" type="button" id="addDiscuss" value="提 交"/>
	</div>
	</div>
	<!---------底部------------->
	<div class="dc_bottom"><span>国防科学技术大学 版权所有 www.nudt.edu.cn</span></div>
	
	</body>
</html>
			