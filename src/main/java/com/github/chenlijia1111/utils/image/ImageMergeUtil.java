package com.github.chenlijia1111.utils.image;

import com.github.chenlijia1111.utils.common.AssertUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * 图片合并工具类
 *
 * @author 陈礼佳
 * @since 2019/9/17 22:28
 */
public class ImageMergeUtil {


    /**
     * 把 图片aboveFile 写道 缓存图片belowFile 上面去
     *
     * @param aboveFile  合并的图片
     * @param belowFile  合并的图片背景
     * @param outputFile 输出的文件
     * @param x          相对于背景图片左上角的x坐标 填正数
     * @param y          相对于背景图片左上角的y坐标 填正数
     * @return
     */
    public static void mergeImage(File aboveFile, File belowFile, File outputFile,
                                  int x, int y, int width, int height) {

        AssertUtil.isTrue(aboveFile.exists(), "合并的图片不能为空");
        AssertUtil.isTrue(belowFile.exists(), "合并的图片背景不能为空");
        AssertUtil.isTrue(Objects.nonNull(outputFile), "输出的文件不能为空");

        //判断输出的文件是否存在,不存在则创建
        File parentFile = outputFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {

            BufferedImage aboveBufferedImage = ImageIO.read(aboveFile);
            BufferedImage belowBufferedImage = ImageIO.read(belowFile);

            //创建背景图片的画板
            Graphics2D graphics = belowBufferedImage.createGraphics();
            graphics.drawImage(aboveBufferedImage, x, y, width, height, null);
            graphics.dispose();

            //将合并之后的缓存图片写道要输出的文件里面去
            ImageIO.write(belowBufferedImage, "png", outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 把 图片aboveFile 写道 缓存图片belowFile 上面去
     *
     * @param aboveFile    合并的图片
     * @param belowFile    合并的图片背景
     * @param x            相对于背景图片左上角的x坐标
     * @param y            相对于背景图片左上角的y坐标
     * @param outputStream 将合并的图片输出到输出流
     * @return
     */
    public static void mergeImage(File aboveFile, InputStream belowFile, OutputStream outputStream,
                                  int x, int y, int width, int height) {

        try {

            BufferedImage aboveBufferedImage = ImageIO.read(aboveFile);
            BufferedImage belowBufferedImage = ImageIO.read(belowFile);

            //创建背景图片的画板
            Graphics2D graphics = belowBufferedImage.createGraphics();
            graphics.drawImage(aboveBufferedImage, x, y, width, height, null);
            graphics.dispose();

            //将合并之后的缓存图片写道要输出的文件里面去
            ImageIO.write(belowBufferedImage, "png", outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
