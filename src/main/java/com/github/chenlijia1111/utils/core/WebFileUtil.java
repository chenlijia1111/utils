package com.github.chenlijia1111.utils.core;

import com.github.chenlijia1111.utils.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 文件工具类
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/10/10 0010 下午 2:35
 **/
public class WebFileUtil {

    //log 日志
    private static final Logger log = LoggerFactory.getLogger(WebFileUtil.class);

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
     * @param filePath 文件绝对路径 C:/demo/demo.txt
     * @param fileName 文件名称 下载返回显示的文件名称
     * @param response
     */
    public static void downloadFile(String filePath, String fileName, HttpServletResponse response, HttpServletRequest request) {
        File file = new File(filePath);
        if (!file.exists())
            //文件不存在
            return;

        //判断文件类型，赋予MIME Type 浏览器可以根据这个判断是什么类型的文件
        String fileSuffix = FileUtils.getFileSuffix(file).toLowerCase();
        String s = FileUtils.commonDownLoadContentType.get(fileSuffix);
        response.setContentType(StringUtils.isNotEmpty(s) ? s : "APPLICATION/OCTET-STREAM");
        //检测是否断点续传
        checkGoingDown(file, request, response);

        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
             OutputStream outputStream = new BufferedOutputStream(response.getOutputStream())) {
            if (StringUtils.isNotEmpty(fileName)) {
                fileName = URLDecoder.decode(fileName, "UTF-8");
                if (HttpUtils.isIE(request)) {
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                } else {
                    fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
                }
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            }
            byte[] buffer = new byte[2048];
            //起始读取位置
            Integer startPosition = findDownLoadStartPosition(request);
            inputStream.skip(startPosition);
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
        } catch (FileNotFoundException e) {
            log.error("文件未找到:" + filePath);
        } catch (IOException e) {
            log.error("文件下载异常:" + e.getMessage());
        }

    }


    /**
     * 处理断点续传
     * 返回状态 206
     * 新增 Header Content-Range=bytes 2000070-106786027/106786028
     *
     * @param file     1
     * @param request  2
     * @param response 3
     * @return void
     * @since 下午 4:39 2019/7/23 0023
     **/
    private static void checkGoingDown(File file, HttpServletRequest request, HttpServletResponse response) {
        long length = file.length();
        String range = request.getHeader("range");
        if (StringUtils.isNotEmpty(range)) {
            Integer start = findDownLoadStartPosition(request);
            response.setHeader("Content-Range", "bytes " + start + "-" + length + "/" + length);
            response.setStatus(206);
            //文件大小
            response.setContentLengthLong(file.length() - start);
        } else {
            //文件大小
            response.setContentLengthLong(file.length());
        }
    }


    /**
     * 获取下载起始位置
     *
     * @param request 1
     * @return java.lang.Integer
     * @since 下午 4:41 2019/7/23 0023
     **/
    private static Integer findDownLoadStartPosition(HttpServletRequest request) {

        int start = 0;
        String range = request.getHeader("range");
        if (StringUtils.isNotEmpty(range)) {
            String[] split = range.split("=");
            String s = split[1];
            s = s.substring(0, s.length() - 1);
            start = Integer.valueOf(s);
        }
        return start;
    }

}
