package com.github.chenlijia1111.utils.database;

import com.github.chenlijia1111.utils.core.reflect.PropertyUtil;
import com.github.chenlijia1111.utils.list.Lists;
import com.mysql.jdbc.Driver;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/11/14 0014 下午 1:19
 **/
public class CommonSqlUtil {

    /**
     * 单例模式创建连接
     *
     * @since 上午 9:45 2019/11/15 0015
     **/
    private static Connection connection;

    /**
     * 创建mysql连接  单例
     *
     * @param url      1
     * @param userName 2
     * @param password 3
     * @return java.sql.Connection
     * @since 下午 1:19 2019/11/14 0014
     **/
    public static Connection createSingleConnection(String url, String userName, String password) {
        if (Objects.isNull(connection)) {
            try {
                Class.forName(Driver.class.getName());
                connection = DriverManager.getConnection(url, userName, password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * 创建mysql连接
     *
     * @param url      1
     * @param userName 2
     * @param password 3
     * @return java.sql.Connection
     * @since 下午 1:19 2019/11/14 0014
     **/
    public static Connection createConnection(String url, String userName, String password) {
        try {
            Class.forName(Driver.class.getName());
            Connection connection = DriverManager.getConnection(url, userName, password);
            return connection;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * resultSet 映射为对象
     *
     * @param resultSet 1
     * @param objClass  2
     * @return T
     * @since 上午 8:57 2019/11/15 0015
     **/
    public static <T> T resultSetToObject(ResultSet resultSet, Class<T> objClass) {

        String name = objClass.getName();
        if (name.startsWith("java")) {
            //表示转化的是基本类型
            //直接转化即可
            return resultSetToBaseClass(resultSet, objClass);
        }

        //获取class的所有属性
        List<Field> allFields = PropertyUtil.getAllFields(objClass);
        try {
            T t = objClass.newInstance();
            if (resultSet.next()) {
                //开始处理数据
                for (Field field : allFields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Class<?> fieldType = field.getType();
                    //赋值
                    Object value = resultSet.getObject(fieldName, fieldType);
                    field.set(t, value);
                }
            }
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * resultSet 映射为基础对象 如Integer,Double,String,Date,BigDecimal等
     *
     * @param resultSet 1
     * @param objClass  2
     * @return T
     * @since 上午 8:57 2019/11/15 0015
     **/
    private static <T> T resultSetToBaseClass(ResultSet resultSet, Class<T> objClass) {

        try {
            if (resultSet.next()) {
                //开始处理数据
                T object = resultSet.getObject(0, objClass);
                return object;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * resultSet 映射为集合
     *
     * @param resultSet 1
     * @param objClass  2
     * @return T
     * @since 上午 8:57 2019/11/15 0015
     **/
    public static <T> List<T> resultSetToList(ResultSet resultSet, Class<T> objClass) {

        //获取class的所有属性
        List<Field> allFields = PropertyUtil.getAllFields(objClass);
        try {
            List<T> list = Lists.newArrayList();
            while (resultSet.next()) {
                //开始处理数据
                T t = objClass.newInstance();
                for (Field field : allFields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Class<?> fieldType = field.getType();
                    //赋值
                    Object value = resultSet.getObject(fieldName, fieldType);
                    field.set(t, value);
                }
                list.add(t);
            }
            return list;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


}
