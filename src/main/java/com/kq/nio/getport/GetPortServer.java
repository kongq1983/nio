package com.kq.nio.getport;

import java.net.ServerSocket;
import java.net.Socket;

public class GetPortServer {

    public static final int PORT = 11000;

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("wait for new connection !");
        Socket socket = serverSocket.accept();
        System.out.println("GetPortServer Print Info");
        System.out.println("Server Port ="+socket.getLocalPort());
        System.out.println("Client Port ="+socket.getPort());
        socket.close();
        serverSocket.close();
        System.out.println("server end.");

    }

}
