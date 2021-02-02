package com.kq.udp.mass.send;


import com.kq.udp.mass.server.UDPGroupServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author kq
 * @date 2021-02-02 10:36
 * @since 2020-0630
 */
public class UDPClientSendData {

    private static final int BUFFER_SIZE = 1024;
    private static final String SERVER_IP = "localhost";


    public static void main(String[] args) throws Exception {

        int index = 0;
        while (index<10) {
            sendToServer("current index="+index);
            TimeUnit.SECONDS.sleep(5);
            index++;
        }

    }

    /**
     * 客户端向服务端发送消息
     *
     * @param msg
     * @throws IOException
     */
    private static void sendToServer(String msg) throws IOException {
        SocketAddress server = new InetSocketAddress(SERVER_IP, UDPGroupServer.SERVER_PORT);
        DatagramSocket ds = new DatagramSocket();
        byte[] bytes = msg.getBytes();
        ds.send(new DatagramPacket(bytes, bytes.length, server));
        ds.close();
    }


}
