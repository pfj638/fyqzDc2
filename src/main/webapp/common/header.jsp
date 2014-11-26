<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="/common/hiddenParams.jsp"%>
 <!---------头部------------->
<!--<div class="dc_top_1">-->
	<div class="dc_logo">
    <img class="lg_pic" src="<%=path%>/image/da_logo.gif" />
    <div class="dc_logo_div1"><%=session.getAttribute("PRODUCT_NAME")%></div>
    <div class="dc_logo_div2">
       	<ul class="dc_logo_div2_ul1">
        	<li><img src="<%=path%>/image/per.gif" /></li> 
        </ul>
    	<ul class="dc_logo_div2_ul2">
        	<li>管理员：hiow 欢迎登陆平台</li>
            <li><img src="<%=path%>/image/jt.gif" /> 退出系统</li>
        </ul>
    </div>
</div>
<!--<div class="dc_top_2">-->
<div class="dc_nav">
	<ul class="dc_menu">
   		<li>
        	<a href="<%=path %>/forward_toMain.action">首页</a> 
        </li>
       	        <li >
	        	<a href="#">  swot分析战略</a>
	            <ul>
	            	<li><a href="<%=path %>/forward_toDiscussNew.action">研讨申请</a></li>
	                <li><a href="<%=path %>/discuss_queryCheckDiscuss.action">研讨授权</a></li>
	                <li><a href="<%=path %>/bms/discuss_discussInitList.action?discussManagerForm.type=swot">研讨初始化</a></li>
	                <li><a href="<%=path %>/bms/discuss_discussStartList.action?discussManagerForm.type=swot">启动研讨</a></li>
	                <li><a href="<%=path %>/bms/discuss_discussAttendList.action?discussManagerForm.type=swot">参与研讨</a></li>
	                <li><a href="#">研讨列表</a></li>
	            </ul>
	        </li>	
	        <li >
	        	<a href="#">  装备价格评估</a>
	            <ul>
	            	<li><a href="#">研讨申请</a></li>
	                <li><a href="#">研讨授权</a></li>
	                <li><a href="<%=path %>/bms/discuss_discussInitList.action?discussManagerForm.type=equip">研讨初始化</a></li>
	                <li><a href="<%=path %>/bms/discuss_discussStartList.action?discussManagerForm.type=equip">启动研讨</a></li>
	                <li><a href="<%=path %>/bms/discuss_discussAttendList.action?discussManagerForm.type=equip">参与研讨</a></li>
	                <li><a href="#">研讨列表</a></li>
	            </ul>
	        </li>
        <li>
        	<a href="#">国防科技预见</a>
            <ul>
				<li><a href="#">预见工程申请</a>
				</li>
				<li><a href="#">预见工程授权</a>
				</li>
				<li><a href="#">科技预见工程初始化</a>
				</li>
				<li><a href="#">提出备选课题</a>
				</li>
				<li><a href="#">启动专家调查</a>
				</li>
				<li><a href="#">调查打分</a>
				</li>
				<li><a href="#">统计分析</a>
				</li>
				<li><a href="#">预见工程列表 </a>
				</li>
			</ul>

            
        </li>	
        <li>
        	<a href="#">体系能力评估</a>
            <ul>
            	<li><a>评估</a></li> 
            </ul>
        </li>	
        <li>
        	<a href="#">辅助工具</a>
            <ul>
            	<li><a>  思维导图</a></li>
                <li><a>态势展示工具</a></li>
                <li><a>Citespace</a></li> 
            </ul>
        </li>
        <li>
        	<a href="#">知识库</a> 
        </li>
        <li>
        	<a href="#">用户管理</a>
                <ul>
	            	<li><a href="<%=path %>/user_queryUserByPage.action">用户维护</a></li>
	                <li><a href="<%=path %>/role_queryRoleByPage.action">角色维护</a></li>
	                <li><a href="<%=path %>/permission_queryPermissionByPage.action">权限维护</a></li>
	            </ul>
        </li>
        <li>
        	<a href="#">系统管理</a>
            <ul>
            	<li><a>基础数据维护</a></li>
                <li><a>运行日志</a></li>
            </ul>
        </li>	
        <li style="border-right:none">
        	<a href="#">帮助</a> 
        </li>	 	
    </ul>
</div>
<!--</div>-->

  

