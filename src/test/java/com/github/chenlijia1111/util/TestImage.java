package com.github.chenlijia1111.util;

import com.github.chenlijia1111.utils.image.*;
import net.coobird.thumbnailator.Thumbnails;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author 陈礼佳
 * @since 2019/9/17 22:47
 */
public class TestImage {

    @Test
    public void test1() {
        ImageMergeUtil.mergeImage(new File("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\index.jpg"),
                new File("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\index1.jpg"),
                new File("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\index2.jpg"),
                1, 100, 100, 100
        );
    }

    @Test
    public void test2() {
        QRCodeUtil qrCodeUtil = new QRCodeUtil();
        File file = new File("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\qrcode.jpg");
        File logo = new File("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\index1.jpg");
        qrCodeUtil.outputWithLogo("http://www.baidu.com", file, logo);
    }

    @Test
    public void test3() throws FileNotFoundException {
        QRCodeUtil qrCodeUtil = new QRCodeUtil();
        File file = new File("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\qrcode1.jpg");
        File logo = new File("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\index.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        qrCodeUtil.outputWithLogo("http://www.baidu.com", fileOutputStream, logo);
    }

    @Test
    public void test4() {
        for (int i = 0; i < 100; i++) {
            String validCode = ValidateImageUtil.createValidCode(4);
            ValidateImageUtil.writeValidCode(validCode, new File("C:\\Users\\Administrator\\Desktop\\picture\\chen" + i + ".png"));

        }
    }

    @Test
    public void test5() {
        OneDimensionalCodeUtil codeUtil = new OneDimensionalCodeUtil();
        codeUtil.output("5675757657", new File("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\onecode1.jpg"));
    }

    /**
     * 压缩图片测试
     * 压缩的时间比较长，建议用多线程处理
     *
     * @return void
     * @since 下午 2:38 2019/10/10 0010
     **/
    @Test
    public void test6() {
        long l = System.currentTimeMillis();
        File file = new File("E:\\公司资料\\我的图片\\IMG_20191002_081044.gif");
        ReduceImageUtil.reduceImage(file);
        System.out.println(System.currentTimeMillis() - l);
    }

    //判断是否是图片
    @Test
    public void test7() {
        File file = new File("E:\\公司资料\\笔记\\mysql\\mysql 语法笔记.md");
        try {
            BufferedImage read = ImageIO.read(file);
            System.out.println(read);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test8() {
        File file = new File("E:\\公司资料\\我的图片\\IMG_20191027_090423.jpg");
        File file1 = new File("E:\\公司资料\\我的图片\\IMG_test1.jpg");
        try {
            Thumbnails.of(file1).scale(1f).outputQuality(0.25).toFile(new File("E:\\公司资料\\我的图片\\IMG_test2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
