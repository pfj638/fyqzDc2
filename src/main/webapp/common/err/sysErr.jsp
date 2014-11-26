<!--
@大成管理系统
@file sysErr.jsp
@author: 施海洋
@date: 2014-8-21
@内容说明　 json请求时出错，跳转到这里
-->
<!DOCTYPE PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>系统错误页面</title>
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="expires" content="0"/>
    <script type="text/javascript" src="<%=path%>/js/jquery-1.8.0.min.js"></script>
    <link href="<%=path%>/css/err.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="ord_stat_expbox">
    <h2><span class=""></span>提示</h2>

    <div class="draft_box"><p>抱歉，代码中发生错误</div>
    <div style="width: 800px;">
        错误描述:<s:property value="errForm.errDesc" />
    </div>
    <div class="draft_box_btnbox">
        &nbsp;
    </div>
</div>

</body>
</html>

