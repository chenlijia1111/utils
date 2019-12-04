package com.github.chenlijia1111.utils.core.enums;

/**
 * 系统参数
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/12/3 0003 下午 2:24
 **/
public enum SystemPropertyEnum {


    JAVA_VERSION("java.version"), //运行时环境版本
    JAVA_HOME("java.home"), // Java 安装目录
    JAVA_IO_TEMP_DIR("java.io.tmpdir"), // 默认的临时文件路径
    OS_NAME("os.name"), //操作系统的名称
    OS_ARCH("os.arch"), //操作系统的架构
    OS_VERSION("os.version"), //操作系统的版本
    FILE_SEPARATOR("file.separator"), //文件分隔符
    PATH_SEPARATOR("path.separator"), //路径分隔符
    LINE_SEPARATOR("line.separator"), //行分隔符
    USER_NAME("user.name"), //用户的账户名称
    USER_HOME("user.home"), //用户的主目录
    USER_DIR("user.dir"), //用户的当前工作目录
    ;

    SystemPropertyEnum(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}
