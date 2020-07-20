package com.kq.nio.inetaddress;


import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class INetAddressServer1 {

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(INetAddressServer.PORT);
        Socket socket = serverSocket.accept();
        InetAddress inetAddress = socket.getInetAddress();
        InetSocketAddress inetSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();

        byte[] bytes = inetAddress.getAddress();
        System.out.println("客户端的IP地址为:");
        for(int i=0;i<bytes.length;i++) {
            System.out.println(bytes[i]+" ");
        }

        System.out.println("hostaddress:"+inetAddress.getHostAddress());
        System.out.println("客户端的端口为:"+inetSocketAddress.getPort());
        socket.close();
        serverSocket.close();

    }

}
