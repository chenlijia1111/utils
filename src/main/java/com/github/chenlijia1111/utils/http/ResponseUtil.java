package com.github.chenlijia1111.utils.http;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * response 工具类
 * 用于 利用输出流 输出数据
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/11 0011 下午 8:13
 **/
public class ResponseUtil {

    /**
     * 输出 json 信息
     *
     * @param object   1
     * @param response 2
     * @author chenlijia
     * @since 上午 11:57 2019/7/25 0025
     **/
    public static void printRest(Object object, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(object);
            writer.write(s);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
