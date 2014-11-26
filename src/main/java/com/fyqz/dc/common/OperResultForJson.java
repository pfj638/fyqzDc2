package com.fyqz.dc.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/5/8 11:38
 * Description:
 * 这其实就是一个标识位，标识你将要使用Opersutl来返回json数据，
 * 前台你也将使用OperResult.js来处理这个问题
 */
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperResultForJson {

}
