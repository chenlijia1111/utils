package com.github.chenlijia1111.utils.core;

import com.github.chenlijia1111.utils.common.AssertUtil;

import java.io.*;

/**
 * 输入输出流工具类
 *
 * @author 陈礼佳
 * @since 2019/9/17 23:15
 */
public class IOUtil {


    /**
     * 输出文件
     *
     * @param file
     * @param outputStream
     */
    public static void writeFile(File file, OutputStream outputStream) {
        AssertUtil.isTrue(null != file && file.exists(), "输出文件为空");
        AssertUtil.isTrue(null != outputStream, "输出流为空");

        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            byte[] bytes = new byte[4096];
            int read;
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
