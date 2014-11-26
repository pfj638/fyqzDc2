package com.fyqz.dc.dto;

import com.fyqz.dc.common.BaseForm;
import com.fyqz.dc.intrercepter.OperRetCodeConstant;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     14-4-4 下午4:12
 * Description:
 * 这个类用于页面返回操作结果，多用于ajax
 * 1 如果你有额外数据要返回到页面，请放到data
 * 2 页面自己去判断timeout的值
 *
 */
public class OperResult extends BaseForm{

    private static final long serialVersionUID = -3365630013539536882L;


    /**
     * 操作是否成功,默认是操作是成功的
     */
    private boolean success = true;

     /**
     * 操作码 默认为成功
     * 具体含义可以参见　com.fyqz.dc.intrercepter.OperRetCodeConstant
     */
    private int code = OperRetCodeConstant.RET_SUCCESS;

    /**
     * 操作结果的描述
     */
    private String desc;


    /**
     * 额外的操作数据,例如，如果是查询的话，你把数据可以扔在这里
     */
    private Object data;

    /**
     * 额外的数据,目前使用在超时时会在这个里面填登录的url
     */
    private Object extraData;


    public boolean isTimeout() {
        return code == OperRetCodeConstant.RET_TIME_OUT;
    }

    public void setTimeout(boolean timeout) {
        if (timeout) {
            success = false;//操作失败
            desc = OperRetCodeConstant.RET_TIME_OUT_DESC;
            code = OperRetCodeConstant.RET_TIME_OUT;
        }
    }

    public String getDesc() {
        return this.desc;
    }


    /**
     * 操作成功时请不要调用此方法
     * 设置错误的描述，同时也会设置操作结果为失败
     *
     * @param desc 错误的描述（）
     */
    public void setErrorDesc(String desc) {
        this.success = false;
        setDesc(desc);
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
        if (success) {
            this.code = OperRetCodeConstant.RET_SUCCESS;
        } else {
            //说明这里还是默认的code，需要设置一下cod
            if (this.code == OperRetCodeConstant.RET_SUCCESS) {
                this.code = OperRetCodeConstant.RET_UNKNOWN_ERR;
            }
        }
    }


    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getExtraData() {
        return this.extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }
}
