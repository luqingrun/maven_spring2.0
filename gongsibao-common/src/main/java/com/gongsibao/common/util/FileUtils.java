package com.gongsibao.common.util;

import org.apache.commons.collections.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Collection;

/**
 * Created by wk on 2016/4/22.
 */
public class FileUtils {

    /**
     * 本地临时存放路径
     */
    public final static String      LOCAL_SAVE_PATH              = PropertiesReader.getValue("project", "local_save_path");

    /**
     * 获取文件后缀
     *
     * @param fileName
     * @return
     */
    public static String getExtName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return "";
        }

        int index = fileName.lastIndexOf(".");
        if (-1 == index) {
            return "";
        }
        return fileName.substring(index);
    }

    /**
     * 下载本地文件
     *
     * @param request
     * @param response
     * @param path
     * @param fileName
     */
    public static void downLoacl(HttpServletRequest request, HttpServletResponse response, String path, String fileName) {
        InputStream is = null;
        BufferedInputStream bs = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(new File(path));
            bs = new BufferedInputStream(is);

            // 清除首部空白行,必须reset，否则会出现文件不完整
            response.reset();
            // 设置响应头类型，响应被提交到输出流之前必须调用这个方法
            response.setContentType("application/x-msdownload;charset=GBK");
            response.setCharacterEncoding("UTF-8");
            // firefox浏览器
            if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") != -1) {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
            // IE浏览器
            else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                // 其他浏览器
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }

            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            int len = 0;
            byte[] b = new byte[1024];
            os = response.getOutputStream();
            while ((len = bs.read(b)) > 0) {
                os.write(b, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != bs) {
                    bs.close();
                }
                if (null != is) {
                    is.close();
                }

                if (!StringUtils.isBlank(path)) {
                    File file = new File(path);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件
     *
     * @param file
     */
    public static void removeLocal(File file) {
        if (null == file) {
            return;
        }

        file.delete();
    }

    /**
     * 删除文件(批量)
     *
     * @param files
     */
    public static void removeLocal(Collection<File> files) {
        if (CollectionUtils.isNotEmpty(files)) {
            return;
        }

        for (File file : files) {
            removeLocal(file);
        }
    }
}
