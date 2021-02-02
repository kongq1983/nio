package com.kq.udp.demo1.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

/**
 * @author kq
 * @date 2021-02-02 10:13
 * @since 2020-0630
 */
public class UDPClient1 {



    public static void main(String[] args) throws Exception{
        DatagramSocket ds = null;
        try {
            // 创建客户端的套接字对象
            ds = new DatagramSocket();
            // 定义一个发送的内容
//            字符串.getBytes()将字符串转换为字节数组
            byte[] bs = "你好".getBytes();
            // 创建要发送的目的的IP地址也可以是本机的名字也可以是192.168.1.102(本人电脑)
//            InetAddress is = InetAddress.getByName("MSI");
            InetAddress is = InetAddress.getByName("localhost");
            // 打数据包
            DatagramPacket dp = new DatagramPacket(bs, bs.length, is, 10005);
            // 发送数据
            ds.send(dp);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ds != null) {
                ds.close();
            }
        }

        TimeUnit.SECONDS.sleep(5);

    }

}
