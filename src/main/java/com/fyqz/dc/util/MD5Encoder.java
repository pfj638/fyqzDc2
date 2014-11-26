package com.fyqz.dc.util;

import com.fyqz.dc.common.Constants;

import java.security.MessageDigest;

/**  
*@description:[MD5加密工具类]
*@fileName：com.fyqz.wg.dao.MD5Encoder.java 
*@createTime: 2014-3-13下午4:05:33
*@creater:[赵佳丽]
*@editTime：2014-3-13下午4:05:33
*@modifier:[赵佳丽]  
*/
public class MD5Encoder{

	public static String encode(String sourceString)
	{

		String resultString = null;
		try {
			resultString = sourceString;
			MessageDigest md = MessageDigest.getInstance(Constants.Common.MD5);
            //change by hshihaiyang for multi-platform encodiing issue
			byte[] b = md.digest(resultString.getBytes(Constants.Common.UTF_ENCODING));
			resultString = byte2hexString(b);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return resultString;
	}

	private static final String byte2hexString(byte[] bytes)
	{

		StringBuilder buf = new StringBuilder(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString();
	}
}
