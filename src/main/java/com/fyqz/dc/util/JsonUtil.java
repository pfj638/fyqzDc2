package com.fyqz.dc.util;

import com.fyqz.dc.common.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class JsonUtil {

	// 将一个Object对象转化为json 并输出到客户端浏览器
	public static void printJson(Object obj, HttpServletResponse response)
			throws Exception {
		PrintWriter out = null;
		try {
			response.setContentType("application/json; charset=utf-8");
			response.setHeader("prama", "no-cache");
			response.setHeader("cache-control", "no-cache");
			out = response.getWriter();
			Gson gson = new Gson();
			if (obj != null) {
				String str = gson.toJson(obj);
				out.print(str);
			} else {
				out.print("");
			}
		} finally {
            FileUtil.closeOsWithFlush(out);

        }
    }

	// 输出文本到客户端
	public static void printText(String text, HttpServletResponse response)
			throws Exception {
		PrintWriter out = null;
		try {
			response.setContentType("text/html; charset=utf-8");
			response.setHeader("pragma", "no-cache");
			response.setHeader("cache-control", "no-cache");
			out = response.getWriter();
			out.print(text);
		} finally {
            FileUtil.closeOsWithFlush(out);
		}
	}

    public static String toJson(Object obj) {
        final Gson gson = new GsonBuilder().setDateFormat(Constants.DateFormat.DATE_FORMATE_YYYYMMDDHHMMSS).create();
        return gson.toJson(obj);
    }

}
