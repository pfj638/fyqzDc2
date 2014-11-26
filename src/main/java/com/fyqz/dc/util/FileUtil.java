package com.fyqz.dc.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.fyqz.dc.config.ProductConfiguration;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fyqz.dc.common.Constants;

/**
 * @file:   com.fyqz.dc.util.FileUtil.java 
 * @brief:  文件上传 -- struts2 图片上传
 * @author:   林明阳
 * @date: 2014-8-22下午4:03:30
 * @version: 大成1.0
 */
public class FileUtil {

	protected static final Logger log = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 
	 * show: struts2 FIle上传
	 * @date 2014-8-22下午4:48:01
	 * @author   林明阳
	 * @version 大成1.0
	 * @param file
	 * @param filename
	 * @return     服务器根目录/模块/
	 * @throws Exception
	 * @exception
	 */
	public static String uploadFileaUpdate(File file, String filename) throws Exception {

		try {
			String rootpath = ProductConfiguration.instance().getCaseDocFolder() + getDatePath()
					+ randStr(8) + File.separatorChar;
			File newfile = new File(rootpath);
			if (!newfile.exists())//判断路径是否存在，如果不存在就创建此路径
			{
				boolean isCreate= newfile.mkdirs();
				if(isCreate){
					log.info("已经创建目录PATH：{}",rootpath);
				}
			}
			File newf = new File(newfile, filename);
			FileUtils.copyFile(file, newf);//将文件拷贝到此目录下
			return rootpath + File.separatorChar + filename;
		} catch (Exception e) {
			log.error("文件上传失败！", e);
			return null;
		}
	}

	/**
	 * 
	 * show: 201101/01格式
	 * @date 2014-8-22下午4:48:26
	 * @author   林明阳
	 * @version 大成1.0
	 * @return
	 * @exception
	 */
	public static StringBuffer getDatePath() {

		StringBuffer path = new StringBuffer(DateUtil.formatDateByDateFormate(new Date(),
				Constants.DateFormat.DATE_FORMATE_YYMMDD));
		return new StringBuffer(path.substring(0, 6) + File.separatorChar + path.substring(6, path.length()));
	}

	/**
	 * 
	 * show: 获得随机长度字符串
	 * @date 2014-8-22下午4:48:36
	 * @author   林明阳
	 * @version 大成1.0
	 * @param n
	 * @return
	 * @exception
	 */
	public static String randStr(int n) {

		char[] ca = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
				'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
				'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		char[] cr = new char[n];
		Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < n; i++) {
			int x = random.nextInt(36);
			cr[i] = ca[x];
		}
		return (new String(cr));
	}
	
	/**
	 * 
	 * show: 文件下载
	 * @date 2014-8-22下午4:48:48
	 * @author   林明阳
	 * @version 大成1.0
	 * @param response
	 * @param path
	 * @param fileName
	 * @throws IOException
	 * @exception
	 */
	public static void downloadXls(HttpServletResponse response ,String path,String fileName) throws IOException {
        File xls = new File(path);
        FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(xls);
            outputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[2048];
            int iLength = 0;
            while ((iLength = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, iLength);
            }
            try {
                fileName = new String(fileName.getBytes("gb2312"), "ISO-8859-1");
            } catch (Exception e1) {
                log.error("编码转换错误", e1);
            }
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentType("application/octet-stream");
            response.setContentLength(outputStream.size());
            ServletOutputStream out = response.getOutputStream();
            outputStream.writeTo(out);
        } catch (IOException e) {
            log.error("文件下载时出错", e);
            throw e;
        } finally {
            closeOsWithFlush(outputStream);
            closeIs(inputStream);
        }

    }

    /**
     * 关闭输入流，忽略异常
     * @param is
     */
    public static void closeIs(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {
                log.error("关闭输入流时出错",e);
            }
        }
    }

    /**
     * 关闭输出流，忽略异常
     *
     * @param os
     */
    public static void closeOs(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (Exception e) {
                log.error("关闭输出流时出错", e);
            }
        }
    }


    /**
     * flush输出流
     * 关闭输出流，忽略异常,
     *
     * @param os
     */
    public static void closeOsWithFlush(OutputStream os) {
        if (os != null) {
            try {
                os.flush();
                closeOs(os);
            } catch (Exception e) {
                log.error("Flush输出流时出错", e);
            }
        }
    }

    /**
     * flush输出流
     * 关闭输出流，忽略异常,
     *
     * @param os
     */
    public static void closeOsWithFlush(PrintWriter os) {
        if (os != null) {
            try {
                os.flush();
                closeOs(os);
            } catch (Exception e) {
                log.error("Flush输出流时出错", e);
            }
        }
    }

    /**
     * 关闭输出流，忽略异常
     *
     * @param os
     */
    public static void closeOs(PrintWriter os) {
        if (os != null) {
            try {
                os.close();
            } catch (Exception e) {
                log.error("关闭输出流时出错", e);
            }
        }
    }


}
