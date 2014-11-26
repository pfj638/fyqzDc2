package com.fyqz.dc.common;

/*
 * Copyright (C), 2002-2013
 * FileName:  
 * Author:   Shi Hai Yang
 * Date:     2014/8/19 15:05
 * Description:  
 */
public class Constants {

    /**
     * 日期的格式
     */
    public static class DateFormat {
        /**
         * 默认日期格式.
         */
        public static final String DEFALT_DATE_FORMAT = "yyyy-MM-dd";

        /**
         * 默认时间格式.
         */
        public static final String DEFUALT_TIMESTAMPE_FORMAT = "yyyy-MM-dd hh:mm:ss sss";

        /**
         * 时间格式yyyyMMddHHmmssSSS.
         */
        public static final String DATE_FORMATE_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

        /**
         * 时间格式yyyy-MM-dd HH:mm:ss.
         */
        public static final String DATE_FORMATE_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

        /**
         * 时间格式yyMMdd.
         */
        public static final String DATE_FORMATE_YYMMDD = "yyMMdd";
    }


    public static class ClientType {
        /**
         * 上下中的类型
         */
        public static final String CLIENT_TYPE_WEB = "FYQZ_WEB_CLIENT";
    }
    
    /**
     * 案例的各个状态
     * @author Administrator
     *
     */
	public static class DiscussStatus {

		public static final String DISCUSS_STATUS_APPLY = "申请";
		public static final String DISCUSS_STATUS_REFUSE = "驳回";
		public static final String DISCUSS_STATUS_READY = "筹备";
		public static final String DISCUSS_STATUS_INIT_FINISH = "初始化完毕";
		public static final String DISCUSS_STATUS_BEGIN = "开始";
		public static final String DISCUSS_STATUS_HALT = "暂停";
		public static final String DISCUSS_STATUS_TALK_END = "讨论结束";
		public static final String DISCUSS_STATUS_END = "研讨结束";
	}

    public static class ProductConfig{
        public static final String PRODUCT_NAME_KEY = "PRODUCT_NAME";
    }

    public static class  WebInitConfigConstant {
        /**
         * 用于是否校验登录
         */
        public static final String IGNORE_AJAX_CHECK = "ignoreAjax";


        /**
         * 使用资源时需要检查登录的的action
         */
        public static final String LOGIN_CHECK_PATTERN = "loginCheckPatterns";

        /**
         * 使用资源时不检查登录的的action
         */
        public static final String LOGIN_IGNORE_PATTERN = "loginIgnorePatterns";


        public static final String USER_IN_SESSION_NAME = "user";


    }

    public static class Common{
        public static final  String UTF_ENCODING="UTF-8";

        public static final String MD5="MD5";

        public static final String GB2312="gb2312";

    }

}
