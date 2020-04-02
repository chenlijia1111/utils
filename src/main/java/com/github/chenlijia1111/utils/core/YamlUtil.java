package com.github.chenlijia1111.utils.core;

import com.github.chenlijia1111.utils.list.Lists;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Chen LiJia
 * @since 2020/4/2
 */
public class YamlUtil {

    //存放每一行数据
    private class YamlNode {

        //前面空格的数量，以空格数量来判断上级下级
        private Integer spaceCount;

        private String key;

        private String value;
    }


    /**
     * 读取yaml文件
     *
     * @param inputStream
     * @return
     */
    public Properties yamlToProperties(InputStream inputStream) {

        Properties properties = new Properties();
        if (null != inputStream) {
            List<YamlNode> yamlNodeList = new ArrayList<>();

            String s = IOUtil.readToString(inputStream);
            //开始读取每一行
            List<String> spiltStrToList = StringUtils.spiltStrToList(s, "\r\n");
            if (Lists.isNotEmpty(spiltStrToList)) {
                for (String lineStr : spiltStrToList) {
                    String[] split = lineStr.split(":");
                    if (split.length >= 1 && lineStr.contains(":")) {
                        YamlNode yamlNode = new YamlNode();
                        String key = split[0];
                        //计算前面的空格数量
                        Integer countStrPrefixSpace = countStrPrefixSpace(key);
                        yamlNode.key = key.trim();
                        yamlNode.spaceCount = countStrPrefixSpace;
                        if (split.length > 1) {
                            yamlNode.value = lineStr.substring(key.length() + 2);
                        }
                        yamlNodeList.add(yamlNode);
                    }else {
                        //如果是以 -开头的就是数组
                        String trim = lineStr.trim();
                        if(trim.startsWith("-") && yamlNodeList.size() > 0){
                            //是数组，放到他的上一行里面去
                            YamlNode lastYamlNode = yamlNodeList.get(yamlNodeList.size() - 1);
                            String value = lastYamlNode.value;
                            if(Objects.isNull(value)){
                                //说明他是第一个元素
                                lastYamlNode.value = JSONUtil.objToStr(Lists.asList(trim.substring(1)));
                            }else {
                                //说明不是一个元素
                                List<String> originList = JSONUtil.strToList(lastYamlNode.value, ArrayList.class, String.class);
                                originList.add(trim.substring(1));
                                lastYamlNode.value = JSONUtil.objToStr(originList);
                            }
                        }
                    }

                }
            }

            //开始处理每行数据 yamlNodeList
            if (Lists.isNotEmpty(yamlNodeList)) {
                for (int i = 0; i < yamlNodeList.size(); i++) {
                    YamlNode yamlNode = yamlNodeList.get(i);
                    if (i > 0) {
                        //从后往前看找上级
                        for (int j = i - 1; j >= 0; j--) {
                            YamlNode preYamlNode = yamlNodeList.get(j);
                            if (Objects.equals(preYamlNode.spaceCount, yamlNode.spaceCount - 2)) {
                                yamlNode.key = preYamlNode.key + "." + yamlNode.key;
                                break;
                            }
                        }
                    }
                }
            }

            //转为properties
            for (int i = 0; i < yamlNodeList.size(); i++) {
                YamlNode yamlNode = yamlNodeList.get(i);
                if (Objects.nonNull(yamlNode.value)) {
                    properties.setProperty(yamlNode.key, yamlNode.value);
                }
            }
        }
        return properties;
    }

    /**
     * 读取yaml文件
     *
     * @param file
     * @return
     */
    public Properties yamlToProperties(File file) {

        if (Objects.nonNull(file) && file.exists()) {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                return yamlToProperties(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Properties();
    }

    /**
     * 计算字符串前面有多少个空格
     *
     * @param str
     * @return
     */
    private Integer countStrPrefixSpace(String str) {
        //计算出的数量，初始化0
        Integer count = 0;
        if (null != str) {
            char[] chars = str.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char aChar = chars[i];
                if (' ' == aChar) {
                    count++;
                } else {
                    break;
                }
            }
        }
        return count;
    }

}
