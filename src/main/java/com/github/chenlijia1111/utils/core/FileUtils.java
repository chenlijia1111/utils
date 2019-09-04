package com.github.chenlijia1111.utils.core;


import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件工具类
 *
 * @since 下午 7:30 2019/9/4 0004
 **/
public class FileUtils {


    /**
     * 常用下载类型   contentType  MIME Type
     */
    public static Map<String, String> commonDownLoadContentType = new HashMap<>();

    static {
        commonDownLoadContentType.put(".html", "text/html");
        commonDownLoadContentType.put(".img", "application/x-img");
        commonDownLoadContentType.put(".jpeg", "image/jpeg");
        commonDownLoadContentType.put(".jpe", "image/jpeg");
        commonDownLoadContentType.put(".jpg", "application/x-jpg");
        commonDownLoadContentType.put(".mp4", "video/mpeg4");
        commonDownLoadContentType.put(".png", "application/x-png");
        commonDownLoadContentType.put(".ppt", "application/x-ppt");
        commonDownLoadContentType.put(".apk", "application/vnd.android.package-archive");
        commonDownLoadContentType.put(".css", "text/css");
        commonDownLoadContentType.put(".dll", "application/x-msdownload");
        commonDownLoadContentType.put(".htm", "text/html");
        commonDownLoadContentType.put(".mp3", "audio/mp3");
        commonDownLoadContentType.put(".pdf", "application/pdf");
        commonDownLoadContentType.put(".png", "image/png");
        commonDownLoadContentType.put(".txt", "text/plain");
        commonDownLoadContentType.put(".xls", "application/vnd.ms-excel");
        commonDownLoadContentType.put(".doc", "application/msword");
        commonDownLoadContentType.put(".pdf", "application/pdf");
        commonDownLoadContentType.put(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        commonDownLoadContentType.put(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }


    /**
     * 返回项目根目录
     *
     * @param request
     * @return
     */
    public static String getServerPath(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        String url = scheme + "://" + serverName + ":" + serverPort + contextPath;
        return url;
    }


    /**
     * 检测文件夹是否存在，不存在则创建
     *
     * @param path
     */
    public static void checkDirectory(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    /**
     * 检验文件后缀,判断是否是suffixs的后缀，有一个满足则返回true
     *
     * @param filePath    文件地址
     * @param suffixArray 文件后缀
     * @return boolean
     * @author chenlijia
     * @since 下午 3:51 2019/5/28 0028
     **/
    public static boolean checkFileSuffix(String filePath, String... suffixArray) {

        if (null == suffixArray || suffixArray.length == 0)
            return false;

        for (String suffix : suffixArray) {
            boolean b = filePath.toLowerCase().endsWith(suffix.toLowerCase());
            if (b)
                return true;
            else
                continue;
        }
        return false;
    }

    /**
     * 返回文件后缀名  .docx
     *
     * @param file 1
     * @return java.lang.String
     * @author chenlijia
     * @since 上午 9:59 2019/6/14 0014
     **/
    public static String getFileSuffix(File file) {

        if (file.exists()) {
            String name = file.getName();
            int i = name.lastIndexOf(".");
            if (i != -1)
                return name.substring(i);
        }
        return null;
    }


}
