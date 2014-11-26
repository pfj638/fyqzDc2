package com.fyqz.dc.common;

import com.fyqz.dc.common.performance.BusinessDesc;
import com.fyqz.dc.common.performance.ClassPerformanceTag;
import com.fyqz.dc.common.performance.MethodPerformanceTag;
import com.fyqz.dc.service.PerformanceService;
import com.fyqz.dc.util.CollectionUtil;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.util.AnnotationUtils;
import com.opensymphony.xwork2.util.ClassLoaderUtil;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterConfig;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/25 15:31
 * Description:
  *  该类主要是为了将struts2配置文件中的关于
  *  action及方法上的注解+真实的类名/方法名写到数据库中
 */
public class FyqzStrutsPrepareAndExecuteFilter extends StrutsPrepareAndExecuteFilter {
    private static final Logger log = LoggerFactory.getLogger(FyqzStrutsPrepareAndExecuteFilter.class.getName());

    @Override
    protected void postInit(Dispatcher dispatcher, FilterConfig filterConfig) {
        final List<ClassPerformanceTag> classPerformanceTags = new ArrayList<ClassPerformanceTag>();
        final Map<String, PackageConfig> packageConfigMap = dispatcher.getConfigurationManager().getConfiguration().getPackageConfigs();
        for (PackageConfig packageConfig : packageConfigMap.values()) {
            Map<String, ActionConfig> actionConfigMap = packageConfig.getActionConfigs();
            if (actionConfigMap != null && !actionConfigMap.isEmpty()) {
                for (ActionConfig config : actionConfigMap.values()) {
                    String name = config.getClassName();
                    try {
                        final Class<?> aClass = ClassLoaderUtil.loadClass(name, this.getClass());
                        ClassPerformanceTag classPerformanceTag = processOneClass(aClass, config.getName());
                        if (classPerformanceTag != null) {
                            classPerformanceTags.add(classPerformanceTag);
                        }
                    } catch (Exception e) {
                        log.error("初始化tag中出现异常",e);
                    }
                }
            }
        }

        try {
            if (!classPerformanceTags.isEmpty()) {
                saveTag(filterConfig, classPerformanceTags);
            }
        } catch (Exception e) {
            log.error("保存初始化tag表时出错", e);
        }
    }

    /**
     * 保存tag到数据库中
     * @param filterConfig
     * @param classPerformanceTags
     */
    private void saveTag(FilterConfig filterConfig, List<ClassPerformanceTag> classPerformanceTags) {
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
        PerformanceService performanceService = ctx.getBean(PerformanceService.class);
        performanceService.initTagTable(classPerformanceTags);
    }


    /**
     * @param aClass
     * @param classMappingPrefix 这是在struts2中配置的前缀名称
     * @return
     */
    private ClassPerformanceTag processOneClass(Class<?> aClass, String classMappingPrefix) {
        final BusinessDesc businessDesc = aClass.getAnnotation(BusinessDesc.class);
        if (businessDesc != null) {
            final ClassPerformanceTag classPerformanceTag = new ClassPerformanceTag();
            classPerformanceTag.setName(aClass.getName());
            classPerformanceTag.setDesc(businessDesc.value());
            classPerformanceTag.setMappingName(classMappingPrefix);
            classPerformanceTag.setCategory(businessDesc.category());
            final List<MethodPerformanceTag> methodPerformanceTags = new ArrayList<MethodPerformanceTag>();
            final Collection<Method> allMethodWithAnnotation = AnnotationUtils.getAnnotatedMethods(aClass, BusinessDesc.class);
            if (CollectionUtil.isNotEmpty(allMethodWithAnnotation)) {
                final String prefix = classMappingPrefix.replace("*", "");
                for (Method method : allMethodWithAnnotation) {
                    final BusinessDesc desc = method.getAnnotation(BusinessDesc.class);
                    //这里不可能为空
                    final MethodPerformanceTag methodPerformanceTag = new MethodPerformanceTag();
                    final String methodName = method.getName();
                    methodPerformanceTag.setName(methodName);
                    methodPerformanceTag.setDesc(desc.value());
                    methodPerformanceTag.setMappingName(prefix + methodName);
                    methodPerformanceTag.setCategory(desc.category());
                    methodPerformanceTags.add(methodPerformanceTag);
                }
            }
            classPerformanceTag.setMethodPerformanceTags(methodPerformanceTags);
            return classPerformanceTag;
        }
        return null;
    }
}
