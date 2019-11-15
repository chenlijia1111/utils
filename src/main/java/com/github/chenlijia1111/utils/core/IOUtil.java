package com.github.chenlijia1111.utils.core;

import com.github.chenlijia1111.utils.common.AssertUtil;

import java.io.*;
import java.util.Properties;

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

        try {
            writeInputStream(new FileInputStream(file), outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出文件
     *
     * @param file
     * @param destFile
     */
    public static void writeFile(File file, File destFile) {
        AssertUtil.isTrue(null != file && file.exists(), "输出文件为空");
        AssertUtil.isTrue(null != destFile, "目标文件为空");

        //判断目标文件目录是否存在
        File parentFile = destFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        try {
            writeInputStream(new FileInputStream(file), new FileOutputStream(destFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将输入流的数据输出到输出流
     *
     * @param inputStream  1
     * @param outputStream 2
     * @return void
     * @since 下午 3:36 2019/10/15 0015
     **/
    public static void writeInputStream(InputStream inputStream, OutputStream outputStream) {

        AssertUtil.isTrue(null != inputStream, "输入流为空");
        AssertUtil.isTrue(null != outputStream, "输出流为空");

        try {
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
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 通过类加载器进行加载 相对于classpath 路径下的文件
     * 前面不能加 /
     *
     * @param relativePath 相对于 classPath 下的路径
     * @return java.io.InputStream
     * @since 下午 1:24 2019/9/25 0025
     **/
    public static InputStream inputStreamToBaseProject(String relativePath) {

        relativePath = relativePath.startsWith("/") ? relativePath.substring(1) : relativePath;
        InputStream inputStream = IOUtil.class.getClassLoader().getResourceAsStream(relativePath);
        return inputStream;
    }


    /**
     * 读取文件内容
     *
     * @param file 1
     * @return java.lang.String
     * @since 下午 7:52 2019/9/25 0025
     **/
    public static String readToString(File file) {
        AssertUtil.isTrue(null != file && file.exists(), "文件不合法");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            StringBuilder sb = new StringBuilder();
            String s = reader.readLine();
            while (s != null) {
                sb.append(reader.readLine());
                sb.append("\r\n");
                s = reader.readLine();
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 读取输入流内容
     *
     * @param inputStream 1
     * @return java.lang.String
     * @since 下午 7:52 2019/9/25 0025
     **/
    public static String readToString(InputStream inputStream) {
        AssertUtil.isTrue(null != inputStream, "输入流为空");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder sb = new StringBuilder();
            String s = reader.readLine();
            while (s != null) {
                sb.append(reader.readLine());
                sb.append("\r\n");
                s = reader.readLine();
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 读取输入流到 {@link Properties} 里面
     *
     * @param inputStream 1
     * @return java.util.Properties
     * @since 上午 11:13 2019/9/27 0027
     **/
    public static Properties readToProperties(InputStream inputStream) {

        AssertUtil.isTrue(null != inputStream, "输入流为空");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 读取文件到 {@link Properties} 里面
     *
     * @param file 读取的文件 如 application.properties
     * @return java.util.Properties
     * @since 上午 11:13 2019/9/27 0027
     **/
    public static Properties readToProperties(File file) {

        AssertUtil.isTrue(null != file && file.exists(), "文件不存在");
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return readToProperties(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 关闭流
     *
     * @param closeable 1
     * @return void
     * @since 上午 9:38 2019/10/24 0024
     **/
    public static void close(Closeable... closeable) {
        if (null != closeable && closeable.length > 0) {
            for (int i = 0; i < closeable.length; i++) {
                if (null != closeable[i]) {
                    try {
                        closeable[i].close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * 消费输入流
     *
     * @since 下午 5:25 2019/10/23 0023
     **/
    public static class ConsumeInputStream extends Thread {

        public ConsumeInputStream() {
        }

        public ConsumeInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        private InputStream inputStream;

        public InputStream getInputStream() {
            return inputStream;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    //判断是否被外部中断了
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
