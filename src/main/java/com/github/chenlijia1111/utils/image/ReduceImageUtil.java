package com.github.chenlijia1111.utils.image;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.FileUtils;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
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
            //scale 表示相对原图的大小 在0-1之间   outputQuality表示输出的质量 在0-1之间
            //经测试 0.25的图片质量还是可以的 6M的照片压缩在700k左右
            Thumbnails.of(originalImage).scale(1).outputQuality(0.25).toFile(destImage);
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
