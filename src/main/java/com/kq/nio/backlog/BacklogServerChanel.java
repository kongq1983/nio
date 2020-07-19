package com.kq.nio.backlog;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

public class BacklogServerChanel {

    public static final int PORT = 8083;

    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //backlog来限制客户端连接数量
        serverSocketChannel.bind(new InetSocketAddress("localhost",PORT),30);

        ServerSocket serverSocket = serverSocketChannel.socket();
        Thread.sleep(5000);

        boolean isRun = true;

        while(isRun) {
            Socket socket = serverSocket.accept();
//            socket.close();
        }

        Thread.sleep(8000);

        serverSocket.close();
        serverSocketChannel.close();

    }

}
