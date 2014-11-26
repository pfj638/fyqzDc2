/**
 * Created on 2012-2-21 <br>
 * Copyright IBM Corporation.  All Rights Reserved.<br>
 */
package com.fyqz.dc.common;

import java.io.Serializable;
import java.util.Locale;

/**
 * 主要用于记录调用的上下文信息
 *
 * @author: shihaiyang <br>
 */
public class FYQZContext implements Serializable {

    private static final long serialVersionUID = 1868228672956795867L;
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

    /**
     * 请求者的Locale类型<br>
     * 目前系统只支持zh_CN<br>
     * 未来将支持en及zh_HK<br>
     */
    private Locale locale;


    /**
     * 额外携带的数据，如果你要携带额外的数据，可以把你的数据放在这里
     */
    private Object extraData;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getRequesterIP() {
        return requesterIP;
    }

    public void setRequesterIP(String requesterIP) {
        this.requesterIP = requesterIP;
    }


    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("调用上下文信息{");
        sb.append("登录用户名='").append(userName).append('\'');
        sb.append(", 客户端类型='").append(clientType).append('\'');
        sb.append(", 客户端IP='").append(requesterIP).append('\'');
        sb.append(", locale信息=").append(locale);
        sb.append('}');
        return sb.toString();
    }

    public Object getExtraData() {
        return extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }
}
