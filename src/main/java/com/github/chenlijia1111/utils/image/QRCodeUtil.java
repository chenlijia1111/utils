package com.github.chenlijia1111.utils.image;

import com.github.chenlijia1111.utils.core.IOUtil;
import com.github.chenlijia1111.utils.core.RandomUtil;
import com.github.chenlijia1111.utils.core.enums.CharSetType;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Objects;

/**
 * 二维码工具类
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/7/12 0012 下午 3:25
 **/
public class QRCodeUtil {

    private static final String format = "png";

    private BitMatrix bitMatrix;

    //二维码宽
    private Integer width = 300;

    //二维码高
    private Integer height = 300;

    /**
     * 容错率
     * 容错率最高，越容易识别
     * 但是可能会不清晰
     */
    private ErrorCorrectionLevel errorCorrectionLevel;

    public QRCodeUtil(Integer width, Integer height) {
        this.width = width;
        this.height = height;
    }

    public QRCodeUtil() {
    }

    /**
     * 生成二维码
     *
     * @param msg 信息
     */
    private void createQRCode(String msg) throws WriterException {
        HashMap<EncodeHintType, Object> hashMap = new HashMap<>();
        hashMap.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //纠错等级
        if(Objects.nonNull(errorCorrectionLevel)){
            hashMap.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
        }else {
            hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        }
        //图片边距
        hashMap.put(EncodeHintType.MARGIN, 2);
        bitMatrix = new MultiFormatWriter().encode(msg, BarcodeFormat.QR_CODE, width, height, hashMap);
    }

    /**
     * 将二维码输出到文件
     *
     * @param content 二维码内容
     * @param file    输出文件
     * @return
     */
    public boolean output(String content, File file) {
        try {
            createQRCode(content);
            MatrixToImageWriter.writeToPath(bitMatrix, format, file.toPath());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将二维码输出到文件
     * 带logo
     *
     * @param content 二维码内容
     * @param file    输出文件
     * @return
     */
    public boolean outputWithLogo(String content, File file, File logoFile) {
        try {
            createQRCode(content);
            MatrixToImageWriter.writeToPath(bitMatrix, format, file.toPath());
            //进行合并图片
            ImageMergeUtil.mergeImage(logoFile, file, file, 120, 120, 60, 60);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 将二维码输出到输出流
     *
     * @param content      二维码内容
     * @param outputStream 输出流
     */
    public void output(String content, OutputStream outputStream) {
        try {
            createQRCode(content);
            MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将二维码输出到输出流
     *
     * @param content      二维码内容
     * @param outputStream 输出流
     * @param logoFile     logo文件
     */
    public void outputWithLogo(String content, OutputStream outputStream, File logoFile) {
        try {
            createQRCode(content);
            //创建一个临时文件用于装载二维码
            File tempFile = File.createTempFile(RandomUtil.createUUID(), ".png");
            outputWithLogo(content, tempFile, logoFile);

            //再将临时文件输出到输出流中
            IOUtil.writeFile(tempFile, outputStream);
            tempFile.deleteOnExit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析二维码
     *
     * @param file 二维码图片文件
     * @return 二维码包含信息
     */
    public String decodeQRCodeFile(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            if (Objects.nonNull(image)) {
                BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                HashMap<DecodeHintType, Object> hints = new HashMap<>();
                hints.put(DecodeHintType.CHARACTER_SET, CharSetType.UTF8.getType());
                Result decode = new MultiFormatReader().decode(bitmap, hints);
                String text = decode.getText();
                return text;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解析二维码
     *
     * @param fileInputStream 二维码图片文件输入流
     * @return 二维码包含信息
     */
    public String decodeQRCodeFile(InputStream fileInputStream) {
        try {
            BufferedImage image = ImageIO.read(fileInputStream);
            if (Objects.nonNull(image)) {
                BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                HashMap<DecodeHintType, Object> hints = new HashMap<>();
                hints.put(DecodeHintType.CHARACTER_SET, CharSetType.UTF8.getType());
                Result decode = new MultiFormatReader().decode(bitmap, hints);
                String text = decode.getText();
                return text;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 设置容错率
     * @param errorCorrectionLevel
     */
    public QRCodeUtil setErrorCorrectionLevel(ErrorCorrectionLevel errorCorrectionLevel) {
        this.errorCorrectionLevel = errorCorrectionLevel;
        return this;
    }
}
