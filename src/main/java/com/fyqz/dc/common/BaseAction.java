package com.fyqz.dc.common;

import com.fyqz.dc.dto.ExtOperResult;
import com.fyqz.dc.dto.OperResult;
import com.fyqz.dc.entity.User;
import com.fyqz.dc.util.JsonUtil;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;


/**
 * @description:[公用Action基类,抽象出一些基本的通用的方法以便Action中使用]
 * @fileName：com.fyqz.dc.common.BaseAction
 * @createTime: 2014-3-24下午4:13:34
 * @creater:[shihaiyang]
 * @editTime：2014-3-24下午4:13:34
 */
public abstract class BaseAction extends ActionSupport {

    private static final long serialVersionUID = -1813825511266626681L;
    /**
     * 这里不使用static是因为方便继承的Action中在使用日志的时候能打印出真正的类名
     */
    protected Logger log = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * 用于传递上下文的调用信息
     */
    protected FYQZContext fyqzContext;

       /**
     * 主要初始化一个通用的服务器响应
     */
    protected final OperResult operResult = new ExtOperResult();

    /**
     * 用于取项目通用的配置参数
     */
    protected final ProductConfig productConfig=new ProductConfig();

    /**
     * 请在用户的Action类中自己去修改operResult的结果
     */
    public void sendCommonResp() {
        sendAjaxIgnoreError(operResult);
    }

    private void setErrUrl(OperResult operResult) {
        //额外去附加错误处理
        final HttpServletRequest req = ServletActionContext.getRequest();
        String ctxPath = req.getContextPath();
        String errPath = ctxPath + "/err_show.action";
        this.operResult.setExtraData(errPath);
    }


    public FYQZContext getFyqzContext() {
        return fyqzContext;
    }

    public void setFyqzContext(FYQZContext fyqzContext) {
        this.fyqzContext = fyqzContext;
    }

    public OperResult getOperResult() {
        return operResult;
    }

    public boolean isTimeout() {
        return operResult.isTimeout();
    }

    public void setTimeout(boolean timeout) {
        operResult.setTimeout(timeout);
    }

    /**
     * @return request对象
     */
    public HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    /**
     * @return response 对象
     */
    public HttpServletResponse getResponse() {

        return ServletActionContext.getResponse();
    }

    /**
     * @return session 对象
     */
    public Map<String, Object> getSession() {
        return ServletActionContext.getContext().getSession();
    }

    public HttpSession getHttpSession() {
        return getRequest().getSession();
    }

    /**
     * 获取登录的用户
     *
     * @return
     */
    protected User getLoginUser() {
        return (User) getSession().get(Constants.WebInitConfigConstant.USER_IN_SESSION_NAME);
    }


    /**
     * @param jsonStr json格式的字符串
     * @throws IOException
     */
    protected void sendAjaxResponse(String jsonStr) throws IOException {
        final HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("prama", "no-cache");
        response.setHeader("cache-control", "no-cache");
        final PrintWriter outPrintWrite = response.getWriter();
        outPrintWrite.write(jsonStr);
        outPrintWrite.flush();
    }


    /**
     * 发送json格式的响应，忽略异常
     *
     * @param obj
     */
    protected void sendAjaxIgnoreError(Object obj) {
        try {
            sendJsonAjax(obj);
        } catch (Exception e) {
            log.error("发送ajax请求时出现错误", e);
        }
    }

    /**
     * 将java对象转成json,并发送到客户端
     *
     * @param obj 需要发送的java对象
     * @throws IOException
     */
    protected void sendJsonAjax(Object obj) throws IOException {
        if (obj != null) {
            if (obj instanceof OperResult) {
                setErrUrl((OperResult) obj);
            }
            sendAjaxResponse(JsonUtil.toJson(obj));
        }
    }

    /**
     * 填写超时信息，并且把登录的url写到响应中去
     *
     * @param ret     不可为空
     * @param timeout 是否超时
     */
    protected void setTimeout(OperResult ret, boolean timeout) {
        ret.setTimeout(timeout);

    }


    /**
     * 删除登录中的登录信息
     */
    protected void removeUserInSession() {
        getSession().remove(Constants.WebInitConfigConstant.USER_IN_SESSION_NAME);
    }


    public ProductConfig getProductConfig() {
        return productConfig;
    }


    public String getProductName(){
        return productConfig.getProductName();
    }
}