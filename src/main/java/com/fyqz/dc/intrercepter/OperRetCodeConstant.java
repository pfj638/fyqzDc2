package com.fyqz.dc.intrercepter;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/5/9 9:23
 * Description:  服务器响应返回码常量
 */
public interface OperRetCodeConstant {

    /**
     * 成功
     */
    public static final int RET_SUCCESS = 0;


    /**
     * 未登录,等同于超时，即会话中没发现登录信息
     */
    public static final int RET_TIME_OUT = -10;

    /**
     * timeout的描述
     */
    public static final String RET_TIME_OUT_DESC = "系统超时，请重新登录!";



    /**
     * 未授权,即没有权限
     */
    public static final int RET_NOT_AUTHORITY = -20;



    /**
     * 系统错误,主要用于ajax中你没有捕获异常
     */
    public static final int RET_SYSTEM_ERR = 10;


    /**
     * 未知错误，一般用于设置一个初始值
     */
    public static final int RET_UNKNOWN_ERR = 20;


}
