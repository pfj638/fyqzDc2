package com.fyqz.dc.util;

import java.util.Map;

/**
 * @description:[一些集合的判断使用方法]
 * @fileName：com.fyqz.dc.util.CollectionUtil
 * @createTime: 2014-3-24下午4:13:34
 * @creater:[shihaiyang]
 * @editTime：2014-3-24下午4:13:34
 */
public class CollectionUtil {
    public static boolean isEmpty(java.util.Collection<?> collect) {
        return null == collect || collect.isEmpty();
    }

    public static boolean isNotEmpty(java.util.Collection<?> collect) {
        return !isEmpty(collect);
    }

    public static boolean isNotEmpty(Object[] objs) {
        return !isEmpty(objs);
    }

    public static boolean isEmpty(Object[] objs) {
        return null == objs || objs.length < 1;
    }

    public static boolean isEmpty(Map<Object,Object> map) {
        return null == map || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<Object,Object> map) {
        return !isEmpty(map);
    }


}
