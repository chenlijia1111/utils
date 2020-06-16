package com.github.chenlijia1111.utils.core;

import com.github.chenlijia1111.utils.common.Result;
import com.github.chenlijia1111.utils.dateTime.DateTimeConvertUtil;
import com.github.chenlijia1111.utils.http.HttpClientUtils;
import com.github.chenlijia1111.utils.http.HttpUtils;
import com.github.chenlijia1111.utils.image.ReduceImageUtil;
import com.github.chenlijia1111.utils.list.Lists;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * web文件工具类
 * <p>
 * 使用
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/10/10 0010 下午 2:35
 **/
public class WebFileUtil {

    //log 日志
    private static final Logger log = new LogUtil(WebFileUtil.class);

    /**
     * 线程池
     */
    private static volatile Executor threadPool = null;

    /**
     * 下载视频分片下载每片大小
     * 单位 byte 字节  默认8M
     * 优化客户端请求
     */
    public static long DOWN_LOAD_VIDEO_PEER_SIZE = 8 * 1024 * 1024L;

    /**
     * 获取线程池 用于压缩图片
     *
     * @return
     */
    public static Executor getThreadPool() {
        if (Objects.isNull(threadPool)) {
            synchronized (FileUtils.class) {
                if (Objects.isNull(threadPool)) {
                    threadPool = Executors.newFixedThreadPool(4);
                }
            }
        }
        return threadPool;
    }

    /**
     * 上传文件之后返回相对接口路径,不返回绝对url 方便迁移文件
     *
     * @param file
     * @param savePath        保存文件的路径
     * @param downLoadApiPath 下载的api路径
     * @param isFileName      是否加文件名参数
     * @return
     */
    public static Result saveFile(MultipartFile file, String savePath, String downLoadApiPath, boolean isFileName, String fileType) {
        if (file == null) {
            return Result.failure("上传文件为空");
        }
        if (StringUtils.isEmpty(savePath)) {
            return Result.failure("上传路径为空");
        }
        //随机名称
        String newFileName = UUID.randomUUID().toString().replace("-", "");
        String originalFilename = file.getOriginalFilename();
        //后缀
        int i = originalFilename.lastIndexOf(".");
        String suffixName = "";
        if (i != -1)
            suffixName = originalFilename.substring(i);

        if (StringUtils.isEmpty(suffixName) || suffixName.equals("."))
            return Result.failure("文件没有后缀名");

        //以天为文件夹
        String yyyyMMdd = DateTimeConvertUtil.dateToStr(new Date(), "yyyyMMdd");
        savePath = savePath + "/" + yyyyMMdd;
        FileUtils.checkDirectory(savePath);
        File destFile = new File(savePath + "/" + newFileName + suffixName);

        try {
            file.transferTo(destFile);

            //如果是图片的话,判断图片是否合法
            if (Objects.equals(fileType, "img")) {
                boolean image = FileUtils.isImage(destFile);
                if (!image) {
                    return Result.failure("图片不合法");
                }
            }

            //如果是图片,对图片进行压缩
            //判断图片大小,如果大于1M就进行压缩
            if (Lists.asList(".jpg", ".png", ".gif").contains(suffixName) && destFile.length() > 1 * 1024 * 1024) {
                //多线程执行,这个操作耗时比较长
                getThreadPool().execute(() -> ReduceImageUtil.reduceImage(destFile));
            }
        } catch (IOException e) {
            log.error("文件上传失败");
            e.printStackTrace();
            return Result.failure("上传失败");
        }
        //拼接最终的url请求路径,不带绝对前缀  ------ system/downLoad?filePath=filePath&fileType=file&fileName=fileName
        StringBuilder pathStringBuilder = new StringBuilder();
        pathStringBuilder.append(downLoadApiPath);
        pathStringBuilder.append("?filePath=");
        pathStringBuilder.append(yyyyMMdd);
        pathStringBuilder.append("/");
        pathStringBuilder.append(newFileName);
        pathStringBuilder.append(suffixName);
        pathStringBuilder.append("&fileType=");
        pathStringBuilder.append(fileType);
        //凡是文件的都需要源文件名
        if (isFileName) {
            pathStringBuilder.append("&fileName=");
            pathStringBuilder.append(originalFilename);
        }
        Result result = Result.success("上传成功");
        result.setData(pathStringBuilder.toString());
        return result;
    }


    /**
     * @param filePath 文件绝对路径
     * @param fileName 文件名称
     * @param isDelete 下载完成之后是否删除文件
     * @param response
     */
    public static void downloadFile(String filePath, String fileName, boolean isDelete, HttpServletResponse response, HttpServletRequest request) {
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
            Long startPosition = findDownLoadStartPosition(request);
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
//            e.printStackTrace();
            //可能出现的原因，客户端断开连接
            //如加载视频时，浏览器会先发送一个请求，但是不会获取数据，只是看下整体文件大小
            //然后通过range的形式来请求数据。
        } finally {
            if (isDelete)
                file.delete();
        }

    }


    /**
     * 处理断点续传
     * 返回状态 206
     * 新增 header Content-Range=bytes 2000070-106786027/106786028
     *
     * @param file     1
     * @param request  2
     * @param response 3
     * @return void
     * @author chenlijia
     * @since 下午 4:39 2019/7/23 0023
     **/
    private static void checkGoingDown(File file, HttpServletRequest request, HttpServletResponse response) {
        long length = file.length();
        String range = request.getHeader("range");
        if (StringUtils.isNotEmpty(range)) {
            Long start = findDownLoadStartPosition(request);
            //分片下载 8M为1片
            Long end = start + DOWN_LOAD_VIDEO_PEER_SIZE;
            //最大下标为length-1 判断是否超出
            if (end > length - 1) {
                end = length - 1;
            }
            response.setHeader("Content-Range", "bytes " + start + "-" + (end > length ? length - 1 : end) + "/" + length);
            response.setStatus(206);
            //文件大小
            response.setContentLengthLong(end - start + 1);
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
     * @author chenlijia
     * @since 下午 4:41 2019/7/23 0023
     **/
    public static Long findDownLoadStartPosition(HttpServletRequest request) {

        long start = 0;
        String range = request.getHeader("range"); //Range: bytes=2001-4932
        if (StringUtils.isNotEmpty(range)) {
            String[] split = range.split("=");
            String s = split[1]; //2001-4932
            if (StringUtils.isNotEmpty(s)) {
                String[] split1 = s.split("-");
                if (null != split1 && split1.length > 0) {
                    start = Long.valueOf(split1[0]);
                }
            }
        }
        return start;
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
     * 将网络图片保存到本地
     *
     * @param imageUrl
     * @param saveLocalImageDirectory 保存到本地的文件夹
     * @return 返回文件名
     */
    public static Result httpImageToLocalImage(String imageUrl, String saveLocalImageDirectory) {

        if (StringUtils.isEmpty(imageUrl)) {
            return Result.failure("网络图片URL为空");
        }
        if (StringUtils.isEmpty(saveLocalImageDirectory)) {
            return Result.failure("保存文件夹为空");
        }

        //进行请求
        try {
            HttpResponse response = HttpClientUtils.getInstance().
                    doGet(imageUrl).toResponse();
            InputStream content = response.getEntity().getContent();

            //判断文件夹是否存在，不存在就创建
            File saveLocalImageDirectoryFile = new File(saveLocalImageDirectory);
            if (!saveLocalImageDirectoryFile.exists()) {
                saveLocalImageDirectoryFile.mkdirs();
            }

            //判断文件的后缀
            //默认后缀设置为.png
            String fileSuffix = ".png";
            Header contentType = response.getFirstHeader("Content-Type");
            if (Objects.nonNull(contentType)) {
                String fileSuffixWithContentType = FileUtils.findFileSuffixWithContentType(contentType.getValue());
                if (StringUtils.isNotEmpty(fileSuffixWithContentType)) {
                    fileSuffix = fileSuffixWithContentType;
                }
            }
            //生成文件名
            String fileName = RandomUtil.createRandomName() + fileSuffix;
            File file = new File(saveLocalImageDirectory + "/" + fileName);
            //判断input
            IOUtil.writeInputStream(content, new FileOutputStream(file));

            //返回文件名
            return Result.success("操作成功", fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.failure("操作失败");
    }

}
