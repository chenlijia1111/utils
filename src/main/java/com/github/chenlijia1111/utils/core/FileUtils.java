package com.github.chenlijia1111.utils.core;


import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 * 主要是封装一些对对文件的基本操作
 *
 * @author 陈礼佳
 * @since 下午 7:30 2019/9/4 0004
 **/
public class FileUtils {

    //log 日志
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 常用下载类型   contentType  MIME Type
     */
    public static Map<String, String> commonDownLoadContentType = new HashMap<>();

    static {
        commonDownLoadContentType.put(".html", "text/html");
        commonDownLoadContentType.put(".img", "application/x-img");
        commonDownLoadContentType.put(".jpeg", "image/jpeg");
        commonDownLoadContentType.put(".jpg", "image/jpeg");
        commonDownLoadContentType.put(".jpe", "image/jpeg");
        commonDownLoadContentType.put(".mp4", "video/mpeg4");
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
            return getFileSuffix(name);
        }
        return null;
    }

    /**
     * 返回文件后缀名  .docx
     *
     * @param fileName 1
     * @return java.lang.String
     * @author chenlijia
     * @since 上午 9:59 2019/6/14 0014
     **/
    public static String getFileSuffix(String fileName) {

        if (StringUtils.isNotEmpty(fileName)) {
            int i = fileName.lastIndexOf(".");
            if (i != -1)
                return fileName.substring(i);
        }
        return null;
    }

    /**
     * 返回文件后缀名  docx
     * 不包含前面的 .
     *
     * @param file 1
     * @return java.lang.String
     * @author chenlijia
     * @since 上午 9:59 2019/6/14 0014
     **/
    public static String getFileSuffixNotDot(File file) {

        if (file.exists()) {
            String name = file.getName();
            return getFileSuffixNotDot(name);
        }
        return null;
    }

    /**
     * 返回文件后缀名  docx
     * 不包含前面的 .
     *
     * @param fileName 1
     * @return java.lang.String
     * @author chenlijia
     * @since 上午 9:59 2019/6/14 0014
     **/
    public static String getFileSuffixNotDot(String fileName) {

        if (StringUtils.isNotEmpty(fileName)) {
            int i = fileName.lastIndexOf(".");
            if (i != -1)
                return fileName.substring(i + 1);
        }
        return null;
    }


    /**
     * 文件复制
     *
     * @param inputFile  被复制文件
     * @param outPutFile
     * @return void
     * @since 下午 6:25 2019/9/9 0009
     **/
    public static void copyFile(File inputFile, File outPutFile) throws FileNotFoundException {
        if (!inputFile.exists()) {
            throw new FileNotFoundException("文件不存在");
        }

        AssertUtil.isTrue(null != outPutFile, "输出文件为空");

        //判断输出文件夹是否存在
        File parentFile = outPutFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        //开始复制
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outPutFile))) {
            int read;
            byte[] bufferedBytes = new byte[4096];
            while ((read = inputStream.read(bufferedBytes)) != -1) {
                outputStream.write(bufferedBytes, 0, read);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 判断是否是图片
     *
     * @param file 1
     * @return boolean
     * @since 下午 4:05 2019/10/10 0010
     **/
    public static boolean isImage(File file) {
        try {
            BufferedImage read = ImageIO.read(file);
            return read != null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 压缩文件夹
     *
     * @param sourceFilePath  要压缩的文件夹
     * @param destZipFilePath 压缩包存放路径
     * @param destFileName    压缩包名
     * @return
     */
    public static Result fileToZip(String sourceFilePath, String destZipFilePath, String destFileName) {
        File sourceFiles = new File(sourceFilePath);
        if (!sourceFiles.exists()) {
            //文件夹不存在
            return Result.failure("文件夹不存在");
        }

        File[] files = sourceFiles.listFiles();
        if (files == null || files.length == 0) {
            return Result.failure("文件夹为空");
        }

        File file1 = new File(destZipFilePath);
        if (!file1.exists()) {
            file1.mkdirs();
        }

        File destFile = new File(destZipFilePath + "/" + destFileName + ".zip");

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destFile)))) {

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                //创建zip实体，并添加进压缩包
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zipOutputStream.putNextEntry(zipEntry);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                byte[] bs = new byte[4096];
                int read = -1;
                while ((read = bufferedInputStream.read(bs, 0, 4096)) != -1) {
                    zipOutputStream.write(bs, 0, read);
                }
            }
            Result success = Result.success("压缩成功");
            success.setData(destZipFilePath + "/" + destFileName + ".zip");
            return success;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.failure("压缩失败");
    }


}
