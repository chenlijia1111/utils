package com.github.chenlijia1111.utils.database;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/11/14 0014 下午 1:19
 **/
public class CommonSqlUtil {

    private Connection connection;

    /**
     * 创建mysql连接
     *
     * @param url      1
     * @param userName 2
     * @param password 3
     * @return java.sql.Connection
     * @since 下午 1:19 2019/11/14 0014
     **/
    public Connection createConnection(String url, String userName, String password) {
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


}
