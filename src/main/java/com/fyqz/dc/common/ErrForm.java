package com.fyqz.dc.common;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/5/9 10:49
 * Description:  
 */
public class ErrForm extends BaseForm {
	private static final long serialVersionUID = 6235501611125405910L;
    /**
     * 错误代码
     */
    public int errCode;
    /**
     * 错误描述
     */
    public String errDesc;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }
}
