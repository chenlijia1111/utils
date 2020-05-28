package com.github.chenlijia1111.utils.image;

import com.github.chenlijia1111.utils.common.Result;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * 视频截取工具类
 * 截取视频第一帧
 * <p>
 * 依赖 javacv opencv ffmpeg
 *
 * 博客资料连接 <a href = "https://www.cnblogs.com/zhaoyanhaoBlog/p/11465516.html"></a>
 * 是通过这个博客看的
 *
 * @author Chen LiJia
 * @since 2020/5/20
 */
public class VideoCaptureUtil {

    /**
     * 截取视频第一帧
     *
     * @param videoFileUrl
     * @param captureImageDirectory
     * @param captureImageFileName
     */
    public static Result capture(String videoFileUrl, String captureImageDirectory, String captureImageFileName) {
        try {
            //创建视频帧抓取工具
            FFmpegFrameGrabber fabeFrameGrabber = FFmpegFrameGrabber.createDefault(videoFileUrl);
            fabeFrameGrabber.start();
            //获取旋转角度信息(90度) 如果不旋转，截出来的是角度不对的
            String rotate = fabeFrameGrabber.getVideoMetadata("rotate");
            //帧
            Frame frame = null;
            //取第五帧好了，防止前面黑屏
            int i = 0;
            while (i < 5) {
                //一帧一帧去抓取图片 grabImage调用一次就取下一帧
                Frame grabImage = fabeFrameGrabber.grabImage();
                if (Objects.isNull(grabImage)) {
                    //没有获取到帧
                    break;
                }
                frame = grabImage;
                i++;
            }

            //如果是手机拍的图片，需要旋转
            if (null != rotate && rotate.length() > 1) {
                OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
                opencv_core.IplImage iplImage = converter.convert(frame);
                frame = converter.convert(rotate(iplImage, Integer.valueOf(rotate)));
            }
            //把帧转为图片
            doExecuteFrame(frame, captureImageDirectory, captureImageFileName);

            //停止
            fabeFrameGrabber.stop();

            return Result.success("操作成功");
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
        return Result.failure("操作失败");
    }


    /**
     * 旋转角度
     */
    private static opencv_core.IplImage rotate(opencv_core.IplImage src, int angle) {
        opencv_core.IplImage img = opencv_core.IplImage.create(src.height(), src.width(), src.depth(), src.nChannels());
        opencv_core.cvTranspose(src, img);
        opencv_core.cvFlip(img, img, angle);
        return img;
    }

    /**
     * 将帧转为图片
     *
     * @param f
     * @param targetFilePath
     * @param targetFileName 文件名，不需要带后缀，默认png
     */
    private static void doExecuteFrame(Frame f, String targetFilePath, String targetFileName) {

        if (null == f || null == f.image) {
            return;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        String imageMat = "png";
        String FileName = targetFilePath + File.separator + targetFileName + "." + imageMat;
        BufferedImage bi = converter.getBufferedImage(f);
        File output = new File(FileName);
        try {
            //判断文件夹是否存在
            File directory = new File(targetFilePath);
            if(!directory.exists()){
                directory.mkdirs();
            }

            ImageIO.write(bi, imageMat, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
