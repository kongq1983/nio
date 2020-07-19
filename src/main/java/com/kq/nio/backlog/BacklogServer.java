package com.kq.nio.backlog;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class BacklogServer {

    public static void main(String[] args) throws Exception{
        InetAddress inetAddress = InetAddress.getLocalHost();
        ServerSocket serverSocket = new ServerSocket(BacklogServerChanel.PORT,10,inetAddress);

        Thread.sleep(5000);

        for(int i=0;i<100;i++) {
            System.out.println("accept begin "+(i+1));
            Socket socket = serverSocket.accept();
            System.out.println("accept end "+(i+1));
        }
        serverSocket.close();

    }

}
