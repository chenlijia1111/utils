package com.github.chenlijia1111.utils.core;

import com.github.chenlijia1111.utils.common.AssertUtil;

import java.io.*;
import java.net.URL;
import java.util.Objects;

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


    /**
     * 获取相对项目目录下的文件
     * 如果是在编译器运行的文件
     * 相对项目目录下的路径是在编译路径的classPath下
     * 而如果是打成jar之后运行的项目
     * 就是相对于jar的最外层目录了
     * 所以一般项目在打包之后路径就不一样了，一般我们之前在resource目录下的文件都是打包在class目录下的
     * <p>
     * 所以在获取的时候 先判断当前运行的方式
     *
     * @param relativePath 相对于 classPath 下的路径
     * @return java.io.InputStream
     * @since 下午 1:24 2019/9/25 0025
     **/
    public static InputStream inputStreamToBaseProject(String relativePath) {

        relativePath = (relativePath.startsWith("/") ? "" : "/") + relativePath;

        //先判断当前的运行方式
        //根据文件协议进行判断 如果是jar:/ 就是在jar包中 如果是 file:/就是非jar中
        //获取项目根目录的URL
        URL resource = IOUtil.class.getResource("/");
        String protocol = resource.getProtocol();
        if (protocol.equals("jar")) {
            //如果是springBoot打包的形式的话,最外层路径是BOOT-INF/classes  里面才是classes目录
            //如果是普通的jar的话,最外层就是顶级目录,不用特别处理
            URL resource1 = IOUtil.class.getResource("/BOOT-INF/classes");
            if (Objects.nonNull(resource1)) {
                //说明是springBoot打包形式的jar包
                relativePath = "/BOOT-INF/classes" + relativePath;
            }
        }
        InputStream inputStream = IOUtil.class.getResourceAsStream(relativePath);
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
            while (reader.ready()) {
                sb.append(reader.readLine());
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
            while (reader.ready()) {
                sb.append(reader.readLine());
                sb.append("\r\n");
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
