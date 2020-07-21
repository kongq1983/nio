package com.kq.nio.getport;

import java.net.InetSocketAddress;
import java.net.Socket;

public class GetPortClient {

    public static void main(String[] args) throws Exception{
        Socket socket = new Socket();
        // 客户端通过bind指定具体端口 不指定自动分配
        socket.bind(new InetSocketAddress("localhost",7777));
        socket.connect(new InetSocketAddress("localhost",GetPortServer.PORT));
        System.out.println("GetPortClient Print Info");

        System.out.println("Client Port ="+socket.getLocalPort());
        System.out.println("Server Port ="+socket.getPort());
        socket.close();
    }

}
