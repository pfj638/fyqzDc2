package com.fyqz.dc.common.performance;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/5/8 11:38
 * Description:
 * 这其实就是一个标识位，主要是作性能分析统计的时候使用
 */
@Target({METHOD, TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BusinessDesc {
    /**
     * 模块或者方法的中文描述，必须写
     * 主要写业务含义
     *
     * @return
     */
    String value();

    /**
     * 一个分类属性，用于以后扩展使用，
     * 默认是 1,
     * 以后可以添其它的值，例如0 代表不显示，
     * 方便ＳＱＬ生成报表时使用
     * @return
     */
    int category() default 1;

}
