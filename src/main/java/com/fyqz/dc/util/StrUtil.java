package com.fyqz.dc.util;

import com.fyqz.dc.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrUtil {
    private static final Logger log = LoggerFactory.getLogger(StrUtil.class.getName());

    /**
	 * 截取制定长度中英文字符串（宽度一样）<br>
	 * @param str 要截取的字符串<br>
	 * @param length 截取长度，中文字符长度<br>
	 * @return 截取后的字符串
	 */
	public static String bSubstring(String str, int length){
		byte[] bytes=null;
        if (str == null) {
            return "";
        }
        str=str.trim();
		int len = length * 2;
		int j = 0 , k = 0;
		try{
			for(int i=0;i<str.length();i++)
			{
                bytes=(str.charAt(i)+"").getBytes(Constants.Common.GB2312);
				if(bytes.length==2)
				{
					j += 2;
				}
				else
				{
					j++;
				}
				if(j <= len)
				{
					k += 1;
				}
				if(j >= len)
				{
					return str.substring(0,k) + "...";
				}
			}
		}catch(Exception e){
            log.error("出现错误",e);
		}
		return str;
	}
}
