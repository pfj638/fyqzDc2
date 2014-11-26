package com.fyqz.dc.util;

import com.fyqz.dc.common.OperResultForJson;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Collection;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/5/8 14:02
 * Description:Annotation的一些工具类,主要用struts2的扩展中
   *
 */
public class AnnotationUtil {


    /**
     * 检查方法上是否存在注解
     *
     * @param invocation
     * @param action
     * @return
     */
    @SuppressWarnings("unchecked")
	public static boolean checkAnnotationExist(ActionInvocation invocation, Object action) {
        try {
            final Method method = getActionMethod(action.getClass(), invocation.getProxy().getMethod());
            final Collection<Method> methods = AnnotationUtils.getAnnotatedMethods(invocation.getAction().getClass(), OperResultForJson.class);
            return methods.contains(method);
        } catch (Exception e) {
            return false;
        }


    }

    /**
     * 这用来在struts2中寻找相应的method
     *
     * @param actionClass
     * @param methodName
     * @return
     * @throws NoSuchMethodException
     */
    @SuppressWarnings("unchecked")
	private static Method getActionMethod(@SuppressWarnings("rawtypes") Class actionClass, String methodName) throws NoSuchMethodException {
        Method method;
        try {
            method = actionClass.getMethod(methodName, new Class[0]);
        } catch (NoSuchMethodException e) {
            try {
                String altMethodName = "do" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
                method = actionClass.getMethod(altMethodName, new Class[0]);
            } catch (NoSuchMethodException e1) {
                throw e;
            }
        }
        return method;
    }

}
