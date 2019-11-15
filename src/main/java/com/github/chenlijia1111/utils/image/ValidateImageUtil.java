package com.github.chenlijia1111.utils.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

/**
 * 验证码图片工具
 * 利用画板画出图片输出
 * <p>
 * 关于前后端分离 验证码验证的问题
 * 后台生成验证码图片的时候，生成一个验证码的Id 然后跟验证码文本对应上，
 * 返回前端验证码图片和验证码Id
 * 前端请求的时候提交验证码Id 和验证码内容给后台
 * 后台进行判断验证码是否正确
 * <p>
 * 过期处理可以再加一列位验证码生成时间，做判断即可
 *
 * @author 陈礼佳
 * @since 2019/9/22 9:27
 */
public class ValidateImageUtil {


    //验证码内容,如果需要覆盖验证码内容,直接覆盖此数组即可
    public static char[] commonCode = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    //验证码字体用比较显眼的颜色
    public static Color[] fontColor = {Color.RED, Color.BLUE, Color.GREEN};

    /**
     * 创建验证码
     *
     * @param length
     * @return
     */
    public static String createValidCode(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int nextInt = random.nextInt(commonCode.length);
            sb.append(commonCode[nextInt]);
        }
        return sb.toString();
    }

    /**
     * 输出验证码
     *
     * @param code 验证码内容 {@link #createValidCode(int)}
     * @param file 输出到的文件
     */
    public static void writeValidCode(String code, File file) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            writeValidCode(code, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 输出验证码
     *
     * @param code         验证码内容  {@link #createValidCode(int)}
     * @param outputStream 输出验证码的输出流
     */
    public static void writeValidCode(String code, OutputStream outputStream) {
        // 设置图片宽度和高度
        int width = 400;
        int height = 200;
        // 干扰线条数
        int lines = 10;
        Random r = new Random();
        BufferedImage b = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) b.getGraphics();
        g.setFont(new Font("宋体", Font.BOLD, 150));
        g.setBackground(new Color(255, 255, 255));
        g.clearRect(0, 0, width, height);//通过使用当前绘图表面的背景色进行填充来清除指定的矩形。
        g.drawOval(0, 0, width, height);

        for (int i = 0; i < code.length(); i++) {
            int y = 120 + r.nextInt(60);// y坐标范围
            // 随机颜色
            Color c = fontColor[r.nextInt(fontColor.length)];
            g.setColor(c);
            g.drawString(code.charAt(i) + "", width / code.length() / 16 + i * width / code.length(), y);
        }

        for (int i = 0; i < lines; i++) {
            // 设置干扰线颜色
            Color c = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            g.setColor(c);
            g.drawLine(r.nextInt(width), r.nextInt(height), r.nextInt(width),
                    r.nextInt(height));
        }

        //扭曲图片
        shear(g, width, height, Color.WHITE);

        try {
            // 清空缓冲
            g.dispose();
            ImageIO.write(b, "png", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 扭曲图片
     *
     * @param g
     * @param w1
     * @param h1
     * @param color
     */
    private static void shear(Graphics g, int w1, int h1, Color color) {
        shearX(g, w1, h1, color);
        shearY(g, w1, h1, color);
    }

    /**
     * 修剪X
     *
     * @param g
     * @param w1
     * @param h1
     * @param color
     */
    private static void shearX(Graphics g, int w1, int h1, Color color) {
        Random random = new Random();
        int period = random.nextInt(2);
        boolean borderGap = true;
        int frames = 1;
        int phase = random.nextInt(2);
        for (int i = 0; i < h1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            if (borderGap) {
                g.setColor(color);
                g.drawLine((int) d, i, 0, i);
                g.drawLine((int) d + w1, i, w1, i);
            }
        }
    }

    /**
     * 修剪Y
     *
     * @param g
     * @param w1
     * @param h1
     * @param color
     */
    private static void shearY(Graphics g, int w1, int h1, Color color) {
        Random random = new Random();
        int period = random.nextInt(40) + 10; // 50;
        boolean borderGap = true;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if (borderGap) {
                g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int) d + h1, i, h1);
            }

        }

    }


}
