package com.fyqz.dc.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/26 13:48
 * Description:  
 */
@Entity
@Table(name = "DC_PEFORMANCE_LOG")
public class PerformanceLog implements Serializable {
    private static final long serialVersionUID = -6978834925289954250L;

    private String id;

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
    private  String sessionId;


    @GenericGenerator(name = "generator", strategy = "uuid.hex")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXE_DATE")
    public Date getExeDate() {
        return exeDate;
    }

    public void setExeDate(Date exeDate) {
        this.exeDate = exeDate;
    }

    @Column(name = "REQUEST_IP")
    public String getRequesterIP() {
        return requesterIP;
    }

    public void setRequesterIP(String requesterIP) {
        this.requesterIP = requesterIP;
    }

    @Column(name = "CLIENT_TYPE")
    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    @Column(name = "USER_NAME")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "ACTION_NAME")
    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    @Column(name = "EXE_TIME")
    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }


    @Column(name = "SESSION_ID")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
