package com.github.chenlijia1111.util.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chenlijia1111.utils.core.JSONUtil;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试结果：
 * 序列化花费的时间大概序列化一次 需要一百多毫秒
 * 反序列化花费的时间很短 一次只需要 几十毫秒  20毫秒的样子
 *
 * @author Chen LiJia
 * @since 2020/9/18
 */
public class TestJsonWriterAndReader {

    //转json
    //测试 1次 152
    //测试 1000次 185
    //测试 10000次 195
    //测试 10000次 302
    @Test
    public void test0() {




        ArrayList<UserInfo> userInfos = new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            userInfos.add(new UserInfo("name" + i, "age" + i));
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
//        for (int i = 0; i < 1; i++) {
//            String s = JSONUtil.objToStr(userInfos);
//        }
//        String s = JSONUtil.objToStr(userInfos);
        try {
            String s = new ObjectMapper().writer().writeValueAsString(userInfos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());


    }


    //转 list
    //测试转化一次 22毫秒
    //测试 1000 次简单对象  244 ms
    //测试 10000 次简单对象  278 ms
    //测试 100000 次简单对象  635 ms
    @Test
    public void test1() {


        ArrayList<UserInfo> userInfos = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            userInfos.add(new UserInfo("name" + i, "age" + i));
        }

        String s = JSONUtil.objToStr(userInfos);
        System.out.println(s);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < 1; i++) {
            List<UserInfo> list = JSONUtil.strToList(s, ArrayList.class, UserInfo.class);
        }

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());


    }

    //转对象
    //测试 1000 次简单对象  222 ms
    //测试 10000 次简单对象  286 ms
    //测试 100000 次简单对象  875 ms
    @Test
    public void test2() {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        UserInfo userInfo = new UserInfo("name", "age");
        String s = JSONUtil.objToStr(userInfo);
        System.out.println(s);
        for (int i = 0; i < 1000; i++) {
            UserInfo userInfo1 = JSONUtil.strToObj(s, UserInfo.class);
            System.out.println(userInfo1);
        }

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    //转 jsonNode
    //测试 1000 次简单对象  196 ms
    //测试 10000 次简单对象  261 ms
    //测试 100000 次简单对象  759 ms
    @Test
    public void test3() {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        UserInfo userInfo = new UserInfo("name", "age");
        String s = JSONUtil.objToStr(userInfo);
        System.out.println(s);
        for (int i = 0; i < 100000; i++) {
            JsonNode jsonNode = JSONUtil.strToJsonNode(s);
            System.out.println(jsonNode);
        }

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());


    }

    //转 map
    //测试 1000 次简单对象  215 ms
    //测试 10000 次简单对象  295 ms
    //测试 100000 次简单对象  901 ms
    @Test
    public void test4() {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        UserInfo userInfo = new UserInfo("name", "age");
        String s = JSONUtil.objToStr(userInfo);
        System.out.println(s);
        for (int i = 0; i < 1000; i++) {
            Map<String, Object> map = JSONUtil.strToMap(s, HashMap.class, String.class, Object.class);
            System.out.println(map);
        }

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());


    }

    //转 map
    //测试 1000 次简单对象  242 ms
    //测试 10000 次简单对象  363 ms
    //测试 100000 次简单对象  962 ms
    @Test
    public void test5() {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < 1000; i++) {
            UserInfo userInfo = new UserInfo("name", "age");
            Map<String, Object> map = JSONUtil.objToMap(userInfo, HashMap.class);
            System.out.println(map);
        }

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());

    }

}
