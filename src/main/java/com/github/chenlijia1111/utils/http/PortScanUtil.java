package com.github.chenlijia1111.utils.http;

import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.list.Lists;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 端口扫描工具
 * 扫描服务器端口是否开启
 *
 * @author Chen LiJia
 * @since 2020/3/17
 */
public class PortScanUtil {


    /**
     * 扫描端口
     *
     * @param host         主机ip
     * @param scanPortList 要扫描的端口
     * @return
     */
    public static List<Integer> scanPort(String host, List<Integer> scanPortList) throws IllegalAccessException {
        //返回开启的端口
        List<Integer> resultPortList = new ArrayList<>();
        if (StringUtils.isNotEmpty(host) && Lists.isNotEmpty(scanPortList)) {
            if(scanPortList.size() > 10){
                throw new IllegalAccessException("一次性最多扫描10个端口,避免消耗时间过长");
            }
            for (int i = 0; i < scanPortList.size(); i++) {
                Integer port = scanPortList.get(i);
                try {
                    Socket socket = new Socket(host, port);
                    //连接成功
                    //说明开启了
                    socket.close();
                    resultPortList.add(port);
                } catch (IOException e) {
                    //说明没开启
                    //略过
                }
            }
        }
        return resultPortList;
    }

}
