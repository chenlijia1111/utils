package com.github.chenlijia1111.util;

import com.github.chenlijia1111.utils.image.ImageMergeUtil;
import com.github.chenlijia1111.utils.image.QRCodeUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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
    public void test2(){
        QRCodeUtil qrCodeUtil = new QRCodeUtil();
        File file = new File("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\qrcode.jpg");
        File logo = new File("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\index1.jpg");
        qrCodeUtil.outputWithLogo("http://www.baidu.com",file,logo);
    }

    @Test
    public void test3() throws FileNotFoundException {
        QRCodeUtil qrCodeUtil = new QRCodeUtil();
        File file = new File("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\qrcode1.jpg");
        File logo = new File("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\index.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        qrCodeUtil.outputWithLogo("http://www.baidu.com",fileOutputStream,logo);
    }

}
