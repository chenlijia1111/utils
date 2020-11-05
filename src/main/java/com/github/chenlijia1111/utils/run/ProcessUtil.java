package com.github.chenlijia1111.utils.run;

import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.core.enums.SystemPropertyEnum;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Objects;

/**
 * 进程工具
 * 使用场景：jar 包部署项目的时候，每次都需要先杀死之前的进程id，然后再启动。
 * {@code
 *      public static void main(String[] args) {
 *
 *         File file = new File("D://saveProcessId.txt");
 *         //杀死之前的进程
 *         Integer processId = ProcessUtil.loadProcessId(file);
 *         if (Objects.nonNull(processId)) {
 *             ProcessUtil.killProcessId(processId);
 *         }
 *
 *         SpringApplication.run(GoldRepurchaseApplication.class, args);
 *
 *         //保存当前项目的进程
 *         ProcessUtil.saveProcessId(file);
 *     }
 * }
 *
 * @author Chen LiJia
 * @since 2020/11/2
 */
public class ProcessUtil {

    /**
     * 获取进程id
     *
     * @return
     */
    public static int getProcessId() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        //进程名字 1820@ChenLijia 前面是进程id 后面是用户名
        String name = runtimeMXBean.getName();
        String[] split = name.split("@");
        String processStr = split[0];
        return Integer.valueOf(processStr);
    }

    /**
     * 将进程id存储到文件中
     *
     * @param file
     */
    public static void saveProcessId(File file) {
        if (Objects.nonNull(file)) {
            //判断父文件夹是否存在，不存在就创建
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            //开始写入信息
            try (PrintWriter printWriter = new PrintWriter(new FileWriter(file))) {
                //将当前进程id 写入
                printWriter.print(getProcessId());
                printWriter.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从文件中读取进程id
     *
     * @param file
     * @return
     */
    public static Integer loadProcessId(File file) {
        if (Objects.nonNull(file) && file.exists()) {

            //开始读取信息
            try (LineNumberReader reader = new LineNumberReader(new FileReader(file))) {
                String line = reader.readLine();
                if (StringUtils.isInt(line)) {
                    return Integer.valueOf(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 杀死进程
     *
     * @param processId
     */
    public static void killProcessId(int processId) {
        //判断当前系统
        String osName = System.getProperty(SystemPropertyEnum.OS_NAME.getName()).toLowerCase();
        //命令
        String command;
        if (osName.startsWith("win")) {
            //windows 系统
            command = "cmd.exe /c taskkill -pid " + processId + " -f";
        } else {
            //linux 系统
            command = "kill -9 " + processId;
        }

        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
