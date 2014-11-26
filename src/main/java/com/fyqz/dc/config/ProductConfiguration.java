package com.fyqz.dc.config;

import com.fyqz.dc.common.BaseConfiguration;
import org.apache.commons.lang.SystemUtils;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/4/10 14:36
 * Description:  
 */
public class ProductConfiguration extends BaseConfiguration {

    private static ProductConfiguration configuration = null;

    public static ProductConfiguration instance() {
            synchronized (ProductConfiguration.class) {
                if (configuration == null) {
                    configuration = new ProductConfiguration();

                }
        }
        return configuration;
    }

    private ProductConfiguration() {
        super("productConfig.properties");
    }


    /**
     * 案例背景知识导入时的上传的目录
     *
     * @return
     */
    public String getCaseDocFolder() {
        return SystemUtils.IS_OS_WINDOWS ? getString("case.windows.folder") : getString("case.linux.folder");
    }


    public String getKnowledgeDocFolder() {
        return SystemUtils.IS_OS_WINDOWS ? getString("knowledge.windows.folder") : getString("knowledge.linux.folder");
    }

    public String getProductName(){
        return getString("product.title");
    }

}
