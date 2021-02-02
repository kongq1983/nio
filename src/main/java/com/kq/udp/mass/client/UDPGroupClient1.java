package com.kq.udp.mass.client;


import com.kq.udp.mass.server.UDPGroupServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;

/**
 * @author kq
 * @date 2021-02-02 10:36
 * @since 2020-0630
 */
public class UDPGroupClient1 {

    private static final int BUFFER_SIZE = 1024;
    private static final String SERVER_IP = "localhost";


    public static void main(String[] args) throws IOException {
        SocketAddress server = new InetSocketAddress(SERVER_IP, UDPGroupServer.SERVER_PORT);
        DatagramSocket ds = new DatagramSocket();
        String str = "getIp";
        byte buff[] = str.getBytes();
        byte buffer[] = new byte[8 * 1024];
        DatagramPacket dp = new DatagramPacket(buff, 0, buff.length, server);
        Scanner sc = new Scanner(System.in);
        ds.send(dp);//发送信息到服务器
        dp.setData(buffer, 0, 8 * 1024);
        ds.receive(dp);
        String message = new String(dp.getData(), 0, dp.getLength());
        System.out.println("client本机端口和IP信息:" + message);
        int clientListenPort = Integer.valueOf(message.split(":")[1]);
        System.out.println(clientListenPort);
        new Thread(new ReceiveMsgThread(ds)).start();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String msg = br.readLine();
                if ("shutdown".equals(msg)) {
                    System.out.println("客户端已退出！");
                    System.exit(-1);
                }
                sendToServer(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    /**
     * 用来监听服务器端发送的信息
     */
    private static class ReceiveMsgThread implements Runnable {
        DatagramSocket ds = null;
        DatagramPacket dp = null;
        String message = "";

        private ReceiveMsgThread(DatagramSocket ds) {
            this.ds = ds;
        }

        @Override
        public void run() {
            //System.out.println("客户端已经启动，监听端口："+clientListenPort);
            try {
                byte buffer[] = new byte[1024];
                dp = new DatagramPacket(buffer, 0, buffer.length);
                byte buff[] = new byte[1024];
                while (true) {
                    dp.setData(buff, 0, 1024);
                    System.out.println("client准备接收");
                    ds.receive(dp);
                    message = new String(dp.getData(), 0, dp.getLength());
                    System.out.println("client接收到:" + message);
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
