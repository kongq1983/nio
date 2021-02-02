package com.kq.udp.demo1.server;

/**
 * @author kq
 * @date 2021-02-02 10:10
 * @since 2020-0630
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {
    public static void main(String[] args) {
        DatagramSocket ds = null;
        try {
            //创建UDP服务器端的对象，必须指定端口
            ds = new DatagramSocket(10005);
            //定义接收数据的字节数组
            byte[] bs = new byte[1024];
            //定义接收的数据包
            DatagramPacket dp = new DatagramPacket(bs,bs.length);
            System.out.println("服务器已经启动 监听端口:"+ds.getLocalPort());
            // 这里会阻塞
            ds.receive(dp);
            System.out.println("已通过receive====================");
            //获得发送端的IP
            InetAddress ia = dp.getAddress();
            //获取数据包中的数据
            byte[] bs1 = dp.getData();
            //获得接收数据的长度(实际接收的长度)
            int len = dp.getLength();
            //组装接收的数据
            String data = new String(bs1,0,len);
            System.out.println(ia.getHostAddress()+"接收数据="+data);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(ds!=null) {
                ds.close();
            }
        }


    }
}