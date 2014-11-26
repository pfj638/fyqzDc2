package com.fyqz.dc.common.performance;

import java.util.Date;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/25 16:58
 * Description:  
 */
public class ActionPerformanceEvent {
    /**
     * 执行的时间
     */
    private long executionTime;

    /**
     * 执行的action名称
     */
    private String actionName;


    /**
     * 请求者登录用户名<br>
     */
    private String userName;

    /**
     * 客户端类型<br>
     * 如果请求来自网页，则取值为Web
     * 具体的参见
     * <br>
     */
    private String clientType;

    /**
     * 请求者的IP地址
     */
    private String requesterIP;


    private Date exeDate;

    /**
     * 请求如果来自web，则有sessionid
     */
    private String sessionId;



    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }


    public String getRequesterIP() {
        return requesterIP;
    }

    public void setRequesterIP(String requesterIP) {
        this.requesterIP = requesterIP;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getExeDate() {
        return exeDate==null? null:new Date(exeDate.getTime());
    }

    public void setExeDate(Date exeDate) {
        this.exeDate = exeDate==null? null: new Date(exeDate.getTime());
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
