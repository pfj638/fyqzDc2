<!--
@大成管理系统
@file sysErr.jsp
@author: 施海洋
@date: 2014-8-21
@内容说明　 系统超时后跳转到这里
-->
<!DOCTYPE PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>会话超时</title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<script type="text/javascript" src="<%=path%>/js/jquery-1.8.0.min.js"></script>
<link href="<%=path%>/css/err.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="ord_stat_expbox">
<h2><span class=""></span>提示</h2>
<div class="draft_box"><p>抱歉，您的会话已经超时<span id="timer">15</span>秒后为您跳转相应产品页。如果不跳转
    <a style="color:red;" id="url" href="<%=path%>/forward_toLogin.action">请点这里</a>
    如果有其他问题请联系:<span class="tit_cl yahei f16">方圆奇正</span></p>
</div>
<div class="draft_box_btnbox">
&nbsp;
</div>
</div>
<script type=text/javascript>
			var t=$('#timer').text();
            var url=$('#url').attr("href");
			function f(){
				t--;
				if (t==0){
					location.href=url;
				}else{
					timer.innerHTML=t;
					setTimeout("f();",1000);
					}
			}
			f();
		</script>
</body>
</html>

