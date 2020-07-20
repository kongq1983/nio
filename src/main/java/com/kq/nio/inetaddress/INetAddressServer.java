package com.kq.nio.inetaddress;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class INetAddressServer {

    public static final int PORT = 11000;

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket socket = serverSocket.accept();

        InetAddress inetAddress = socket.getLocalAddress();
        InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getLocalSocketAddress();

        byte[] bytes = inetAddress.getAddress();
        System.out.println("服务端的IP地址为:");
        for(int i=0;i<bytes.length;i++) {
            System.out.println(bytes[i]+" ");
        }
        System.out.println("hostaddress:"+inetAddress.getHostAddress());
        System.out.println("hostname:"+inetAddress.getHostName());
        System.out.println();
        System.out.println("服务端的端口为:"+inetSocketAddress.getPort());
        socket.close();
        serverSocket.close();

    }

}
