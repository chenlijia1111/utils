package com.github.chenlijia1111.utils.image;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.FileUtils;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 压缩图片工具
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/10/10 0010 下午 2:14
 **/
public class ReduceImageUtil {

    /**
     * 原理,将图片读取到 BufferedImage 中
     * 通过 getScaledInstance 进行压缩
     * 然后写到目标文件中去
     * 建议调用者调用本方法时,使用多线程异步执行,因为这个操作耗时较长,一般在3秒上下
     *
     * @param originalImage 源文件
     * @param destImage     目标文件
     * @return void
     * @since 下午 2:24 2019/10/10 0010
     **/
    public static void reduceImage(File originalImage, File destImage) {
        //校验参数
        AssertUtil.isTrue(null != originalImage && originalImage.exists(), "压缩文件不存在");
        AssertUtil.isTrue(null != destImage, "目标文件为空");
        //确保目标文件夹存在
        FileUtils.checkDirectory(destImage.getParent());
        try {
            BufferedImage read = ImageIO.read(originalImage);
            //创建一个跟源文件长宽相等的bufferImage
            BufferedImage bufferedImage = new BufferedImage(read.getWidth(), read.getHeight(), BufferedImage.TYPE_INT_RGB);
            //获取画笔
            Graphics graphics = bufferedImage.getGraphics();
            //Image.SCALE_SMOOTH 压缩策略  还有其他的可以尝试
            graphics.drawImage(read.getScaledInstance(read.getWidth(), read.getHeight(), Image.SCALE_SMOOTH), 0, 0, null);

            FileOutputStream fileOutputStream = new FileOutputStream(destImage);
            //使用jpeg在压缩一次
            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(fileOutputStream);
            jpegEncoder.encode(bufferedImage);

            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 原理,将图片读取到 BufferedImage 中
     * 通过 getScaledInstance 进行压缩
     * 然后写到目标文件中去
     * 建议调用者调用本方法时,使用多线程异步执行,因为这个操作耗时较长,一般在3秒上下
     *
     * @param originalImage 源文件
     * @return void
     * @since 下午 2:24 2019/10/10 0010
     **/
    public static void reduceImage(File originalImage) {
        reduceImage(originalImage, originalImage);
    }


}
