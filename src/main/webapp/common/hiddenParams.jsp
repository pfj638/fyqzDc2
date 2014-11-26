<%@ page language="java" pageEncoding="UTF-8"%>
<%--流程实例id--%>
<input type="hidden" id="processTemplateId" name="templateForm.processTemplateId" value=""/>
<%--当前步骤--%>
<input type="hidden" id="curStep" name="templateForm.curStep" value=""/>
<%--登录人的权限--%>
<input type="hidden" id="userRole" name="userRole" value=""/>
<%--根路径--%>
<input type="hidden" id="rootPath" value="<%=path%>" />