package com.fyqz.dc.dto;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/21 8:45
 * Description:
 * 主要用于js框架及拦截器去设置是否成功
 * 　例如，没有权限，没有登录，出现异常，都会将sysSuccess设置为false
 * 　这样js会跳转到错误页面
 */
public class ExtOperResult extends OperResult {
    private boolean sysSuccess=true;

    public boolean isSysSuccess() {
        return sysSuccess;
    }

    public void setSysSuccess(boolean sysSuccess) {
        this.sysSuccess = sysSuccess;
    }
}
