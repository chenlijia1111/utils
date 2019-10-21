package com.github.chenlijia1111.utils.core.reflect;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.FileUtils;
import com.github.chenlijia1111.utils.core.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类反射工具
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/24 0024 下午 4:58
 **/
public class ClassUtil {

    public static ClassLoader CLASS_LOADER = ClassUtil.class.getClassLoader();

    /**
     * 获取对象的类的完整路径加名字
     * 如 com.github.chenlijia1111.utils.core.reflect.ClassUtil
     *
     * @param obj 1
     * @return java.lang.String
     * @since 下午 5:00 2019/9/24 0024
     **/
    public static String classPathName(Object obj) {
        AssertUtil.isTrue(null != obj, "对象为空");
        return obj.getClass().getName();
    }

    /**
     * 获取对象的类的名称
     * 如 ClassUtil
     *
     * @param obj 1
     * @return java.lang.String
     * @since 下午 5:00 2019/9/24 0024
     **/
    public static String className(Object obj) {
        AssertUtil.isTrue(null != obj, "对象为空");
        return obj.getClass().getSimpleName();
    }


    /**
     * 扫描指定包下的所有class
     *
     * @param packagePath     包名
     * @param annotationClass 注解class  指定只扫描有此注解的class
     * @param parentClass     继承的父类 class
     * @param parentInterface 实现的接口 class
     * @return java.util.List<java.lang.Class>
     * @author chenlijia
     * @since 上午 9:06 2019/6/18 0018
     **/
    public static List<Class> doScan(String packagePath, Class annotationClass, Class parentClass, Class parentInterface) {

        if (StringUtils.isEmpty(packagePath))
            throw new RuntimeException("扫描的包名不能为空");

        ArrayList<Class> arrayList = new ArrayList<>();
        packagePath = packagePath.replaceAll("\\.", "/");
        URL resource = CLASS_LOADER.getResource(packagePath);
        if (resource.getProtocol().toLowerCase().equals("jar")) {
            findJarClass(packagePath, annotationClass, parentClass, parentInterface, arrayList);
        } else {
            String filePath = resource.getFile();
            File file = new File(filePath);
            findChildClass(file, packagePath, annotationClass, parentClass, parentInterface, arrayList);
        }
        return arrayList;
    }


    private static void findJarClass(String packagePath, Class annotationClass, Class parentClass, Class parentInterface, List<Class> list) {
        URL resource = CLASS_LOADER.getResource(packagePath);
        if (resource != null && resource.getProtocol().toLowerCase().equals("jar")) {
            try {
                JarURLConnection urlConnection = (JarURLConnection) resource.openConnection();
                if (null != urlConnection) {
                    JarFile jarFile = urlConnection.getJarFile();
                    if (jarFile != null) {
                        Enumeration<JarEntry> entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry jarEntry = entries.nextElement();
                            String name = jarEntry.getName();
                            if (name.contains(".class") & name.startsWith(packagePath)) {
                                String className = name.substring(0, name.lastIndexOf(".")).replaceAll("/", ".");
                                Class<?> aClass = Class.forName(className);
                                list.add(aClass);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private static void findChildClass(File parentFile, String packagePath, Class annotationClass, Class parentClass, Class parentInterface, List<Class> list) {
        if (parentFile.exists()) {
            //是否是文件夹
            boolean directory = parentFile.isDirectory();
            if (directory) {
                File[] files = parentFile.listFiles();
                if (Objects.nonNull(files) && files.length > 0) {
                    for (File file : files) {
                        findChildClass(file, packagePath, annotationClass, parentClass, parentInterface, list);
                    }
                }
            } else {
                Class aClass = createClass(parentFile, packagePath, annotationClass, parentClass, parentInterface);
                if (Objects.nonNull(aClass))
                    list.add(aClass);
            }

        }
    }


    /**
     * 将 file 转为class
     *
     * @param file            文件
     * @param packagePath     所选取的包名
     * @param annotationClass 需要判断该class是否有这个注解
     * @return java.lang.Class
     * @author chenlijia
     * @since 上午 9:51 2019/6/18 0018
     **/
    private static Class createClass(File file, String packagePath, Class annotationClass, Class parentClass, Class parentInterface) {
        if (file.exists() && FileUtils.getFileSuffix(file).equals(".class")) {
            String path = file.getPath().replaceAll("\\\\", "/");
            packagePath = packagePath.replaceAll("\\\\", "/").replaceAll("\\.", "/");
            int i = path.indexOf(packagePath);
            if (i != -1) {
                String classPath = path.substring(i, path.length() - 6).replaceAll("/", "\\.");
                try {
                    Class<?> aClass = CLASS_LOADER.loadClass(classPath);
                    if (Objects.nonNull(aClass)
                            && (Objects.isNull(annotationClass) || Objects.nonNull(aClass.getAnnotation(annotationClass)))
                            && (Objects.isNull(parentClass) || aClass.getSuperclass().equals(parentClass))
                            && (Objects.isNull(parentInterface) || hasParentInterface(aClass, parentInterface)))
                        return aClass;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * 判断class 是否实现了某个接口
     *
     * @param aclass         1
     * @param interfaceClass 2
     * @return boolean
     * @author chenlijia
     * @since 上午 11:24 2019/6/18 0018
     **/
    private static boolean hasParentInterface(Class aclass, Class interfaceClass) {
        Class[] interfaces = aclass.getInterfaces();
        if (Objects.nonNull(interfaceClass) && interfaces.length > 0) {
            for (Class anInterface : interfaces) {
                if (anInterface.equals(interfaceClass))
                    return true;
            }
        }
        return false;
    }

}
