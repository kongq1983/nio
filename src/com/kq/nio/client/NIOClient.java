package com.kq.nio.client;

import com.kq.nio.server.NIOServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOClient {
    private Selector selector;
    private BufferedReader clientInput = new BufferedReader(new InputStreamReader(System.in));

    public void init() throws IOException {
        this.selector = Selector.open();
        SocketChannel channel = SocketChannel.open(); //创建SocketChannel
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("127.0.0.1",NIOServer.SERVER_PORT)); //链接服务器
        channel.register(selector,SelectionKey.OP_CONNECT); //注册connetct事件
    }

    public void start() throws IOException {
        while(true) {
            selector.select(); //此方法会阻塞，直到至少有一个已注册的事件发生
            Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator(); //获取发生事件的selectionKey对象集合

            while(ite.hasNext()) {
                SelectionKey key = (SelectionKey)ite.next();
                ite.remove();
                if(key.isConnectable()) { //链接事件

                } else if(key.isReadable()) {

                }
            }
        }
    }

    public void connect(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel)key.channel();
        if(channel.isConnectionPending()) { //如果正在链接
            if(channel.finishConnect()) { //完成链接
                channel.configureBlocking(false);
                channel.register(this.selector,SelectionKey.OP_READ); //注册读事件
                String request = clientInput.readLine(); //输入客户端请求
                channel.write(ByteBuffer.wrap(request.getBytes())); //发送到服务端
            }else {
                key.channel();
            }
        }
    }

    private void read(SelectionKey key) throws Exception {
        SocketChannel channe = (SocketChannel)key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channe.read(buffer);
        String response = new String(buffer.array()).trim();
        System.out.println("服务端响应:"+response);
        String nextRequest = clientInput.readLine(); //读取客户端请求输入
        ByteBuffer outBuffer = ByteBuffer.wrap(nextRequest.getBytes());
        channe.write(outBuffer); //将请求发送到服务端
    }

    public static void main(String[] args) throws IOException {
        NIOClient client = new NIOClient();
        client.init();
        client.start();
    }
}
