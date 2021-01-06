package com.github.chenlijia1111.utils.core;


import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.common.Result;
import com.github.chenlijia1111.utils.list.Lists;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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
    private static final Logger log = new LogUtil(FileUtils.class);

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
        commonDownLoadContentType.put(".mp4", "video/mp4");
        commonDownLoadContentType.put(".ppt", "application/x-ppt");
        commonDownLoadContentType.put(".apk", "application/vnd.android.package-archive");
        commonDownLoadContentType.put(".css", "text/css");
        commonDownLoadContentType.put(".dll", "application/x-msdownload");
        commonDownLoadContentType.put(".htm", "text/html");
        commonDownLoadContentType.put(".mp3", "audio/mpeg");
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
     * 根据contentType获取文件后缀
     *
     * @param contentType
     * @return
     */
    public static String findFileSuffixWithContentType(String contentType) {
        if (StringUtils.isNotEmpty(contentType)) {
            contentType = contentType.toLowerCase();
            Set<Map.Entry<String, String>> entries = commonDownLoadContentType.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                if (Objects.equals(entry.getValue(), contentType)) {
                    return entry.getKey();
                }
            }
        }
        return null;
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
        IOUtil.writeFile(inputFile, outPutFile);

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
            log.error("传入的文件不是图片");
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
                int read;
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

    /**
     * 解压压缩包
     * 只能解压 zip 文件
     * 调用此方法前先调用 {@link #checkZipEncoding(InputStream, Charset)} 判断编码
     * 输入流需要重新获取，输入流用于判断之后就不能重复用了
     * <p>
     * 测试代码如下：
     * {@code
     * String zipFileName = "D:\\公司资料\\企业办公信息化\\批量导入票据资料.zip";
     * try {
     * FileInputStream fileInputStream = new FileInputStream(zipFileName);
     * Charset charset = Charset.forName("UTF-8");
     * boolean b = checkZipEncoding(fileInputStream, charset);
     * if (!b){
     * charset = Charset.forName("GBK");
     * }
     * fileInputStream = new FileInputStream(zipFileName);
     * unzip(fileInputStream,charset,"D:\\公司资料\\企业办公信息化\\批量导入票据资料2");
     * } catch (FileNotFoundException e) {
     * e.printStackTrace();
     * }
     * }
     *
     * @param inputStream   输入流
     * @param charset       编码
     * @param destDirectory 解压路径
     * @return
     */
    public static Result unzip(InputStream inputStream, Charset charset, String destDirectory) {

        if (Objects.isNull(inputStream)) {
            return Result.failure("输入流为空");
        }
        if (Objects.isNull(charset)) {
            return Result.failure("编码为空");
        }
        if (StringUtils.isEmpty(destDirectory)) {
            return Result.failure("解压路径为空");
        }

        byte[] bytes = IOUtil.readToBytes(inputStream);
        SeekableInMemoryByteChannel memoryByteChannel = new SeekableInMemoryByteChannel(bytes);
        try {
            ZipFile zipFile = new ZipFile(memoryByteChannel, charset.name());
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry zipArchiveEntry = entries.nextElement();
                if (zipArchiveEntry.isDirectory()) {
                    // 是文件夹
                    // 文件夹，直接忽略就行了，因为文件是带了上级文件夹的名称的，
                    // 到时候判断一下上级是否存在，不存在就创建就行了
                    continue;
                } else {
                    // 是文件
                    String name = zipArchiveEntry.getName();
                    File file = new File(destDirectory + "/" + name);
                    // 判断上级文件夹是否存在
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }

                    // 输出文件
                    InputStream fileItemInputStream = zipFile.getInputStream(zipArchiveEntry);
                    FileOutputStream outputStream = new FileOutputStream(file);
                    IOUtil.writeInputStream(fileItemInputStream, outputStream);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.failure("解压失败");
        }
        return Result.success();
    }

    /**
     * 测试压缩文件编码
     * 源编码 测试编码参数
     * GBK -> GBK true
     * GBK -> UTF-8 false  使用这个来进行判断
     * UTF-8 -> UTF-8 true
     * UTF-8 -> UTF-8 true
     * <p>
     * 因为输入流是只能读取一次的，所以输入流要做好控制
     * 如果是直接从文件获取的，其他地方就需要再获取一次
     * 如果是通过 上传文件获取的，可以直接重新 get 一次输入流
     *
     * @param charset
     * @return
     */
    public static boolean checkZipEncoding(InputStream fis, Charset charset) {

        BufferedInputStream bis = new BufferedInputStream(fis);
        ZipInputStream zis = new ZipInputStream(bis, charset);
        try {
            while (zis.getNextEntry() != null) {
                // do nothing
            }
        } catch (Exception e) {
            return false;
        } finally {
            IOUtil.close(zis, bis, fis);
        }
        return true;
    }

    /**
     * 查询文件夹下的所有文件列表
     * 递归
     *
     * @param directoryPath
     * @return
     */
    public static List<File> listAllFileByDirectory(String directoryPath) {
        List<File> fileList = new ArrayList<>();
        if (StringUtils.isNotEmpty(directoryPath)) {
            File file = new File(directoryPath);
            if (file.exists() && file.isDirectory()) {
                // 开始处理
                // 查询所有下级文件
                File[] files = file.listFiles();
                if (null != files && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        File childFile = files[i];
                        if (childFile.isDirectory()) {
                            // 是文件夹，递归处理
                            List<File> childFileList = listAllFileByDirectory(childFile.getAbsolutePath());
                            if (Lists.isNotEmpty(childFileList)) {
                                fileList.addAll(childFileList);
                            }
                        } else {
                            // 是文件
                            fileList.add(childFile);
                        }
                    }
                }
            }
        }
        return fileList;
    }

    /**
     * 删除文件或文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (Objects.nonNull(file) && file.exists()) {
            if (file.isDirectory()) {
                // 是文件夹，先删除下级
                File[] files = file.listFiles();
                if (Objects.nonNull(files) && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        File childFile = files[i];
                        deleteFile(childFile);
                    }
                }
            }
            // 删除
            file.delete();
        }
    }

}
