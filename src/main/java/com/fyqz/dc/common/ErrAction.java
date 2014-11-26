package com.fyqz.dc.common;

import com.fyqz.dc.common.BaseAction;
import com.fyqz.dc.intrercepter.OperRetCodeConstant;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/5/9 10:47
 * Description:  一个专门用于处理各种错误的action
 */
public class ErrAction extends BaseAction {
	private static final long serialVersionUID = 8866505681324465910L;
    private ErrForm errForm;

    /**
     * 展示错误页面
     *
     * @return
     */
    public String show() {
        int code = errForm.getErrCode();
        if (OperRetCodeConstant.RET_TIME_OUT == code) {
            return "timeout";
        } else if (OperRetCodeConstant.RET_SYSTEM_ERR == code) {
            return "sysErr";
        }
        return "timeout";
    }


    public ErrForm getErrForm() {
        return errForm;
    }

    public void setErrForm(ErrForm errForm) {
        this.errForm = errForm;
    }
}
