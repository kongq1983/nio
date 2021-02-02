package com.kq.udp.mass.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;

/**
 * @author kq
 * @date 2021-02-02 10:23
 * @since 2020-0630
 */
public class UDPGroupServer {

    public static final int SERVER_PORT=1234;//服务器端口
    private static final int BUFFER_SIZE=1024;//缓冲区字节
    private static final List<InetSocketAddress> clientAddressList=new LinkedList<>();//用于存放客户端的ip和端口

    public static void main(String[] args) throws IOException {
        System.out.println("服务器已经启动，监听端口："+SERVER_PORT);
        int clientPort=0;
        String ip="";
        String str="";
        @SuppressWarnings("resource")
        DatagramSocket socket=new DatagramSocket(SERVER_PORT);
        while(true){
            //从客户端接收到的内容（客户端请求）
            DatagramPacket packet=new DatagramPacket(new byte[BUFFER_SIZE],0, BUFFER_SIZE);
            SocketAddress socketAddress=null;
            InetSocketAddress inetSocketAddress=null;
            socket.receive(packet);//接收客户端数据包
            String msg=new String(packet.getData(),0,packet.getLength());//数据包数据转为字符串
            System.out.println("收到客户端消息："+msg);
            socketAddress=packet.getSocketAddress();//由连接的客户端数据包来获得客户端socket地址
            inetSocketAddress=(InetSocketAddress) socketAddress;
            clientPort=inetSocketAddress.getPort();//获得连接的客户端端口号
            ip=inetSocketAddress.getAddress().getHostAddress();//获得连接的客户端ip地址
            if(msg.startsWith("getIp")){//利用接收到getIp字符串来获取客户端ip和端口
                clientAddressList.add(new InetSocketAddress(ip,clientPort));//将客户端ip和端口放到一个list集合，为后面做转发准备
                str=ip+":"+clientPort;
                byte buff[]=str.getBytes();
                packet.setData(buff,0,buff.length);
                socket.send(packet);//把连接的客户端ip和端口号发送回给客户端，表示两者联通
                System.out.println("getip= "+ip+":"+clientPort);
            }
            if("shutdown server".equals(msg)){
                System.out.println("服务器已经被关闭！");//客户端输入shutdown server来关闭服务器
                break;
            }else{
                System.out.println("=====================================");
                //clientAddressList.remove(new InetSocketAddress(ip,clientPort));
                sendToClients(msg, packet.getSocketAddress());
            }

        }
    }
    //服务器向所有客户端群发消息
    private static void sendToClients(String msg,SocketAddress fromAddress) throws IOException{
        DatagramSocket socket=new DatagramSocket();
        if(!msg.equals("getIp")){
            byte[] bytes=msg.getBytes();
            for(InetSocketAddress clientAddress:clientAddressList){//遍历客户端，然后转发消息
                System.out.println(clientAddress);
                DatagramPacket packet=new DatagramPacket(bytes, 0,bytes.length,clientAddress);
                //可以进行判断是否是发送者，若是，可以跳过转发，根据ip地址判断
                socket.send(packet);
            }
        }
        socket.close();
    }

}
