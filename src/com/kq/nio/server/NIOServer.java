package com.kq.nio.server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer {

    private Selector selector;
    public static final int SERVER_PORT = 8200;

    public void init() throws  Exception {
        this.selector = Selector.open();
        //创建ServerSocketChannel
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        ServerSocket serverSocket = channel.socket();
        InetSocketAddress address = new InetSocketAddress(SERVER_PORT);
        serverSocket.bind(address);
        System.out.println("Server listen port : "+SERVER_PORT);
        System.out.println("Server Channel Wait Client Connect .");
        channel.register(this.selector,SelectionKey.OP_ACCEPT);
        System.out.println("init end----------------------");

    }

    public void start() throws Exception{
        System.out.println("start method ---------------------");
        while(true) {
            this.selector.select(); //此方法会阻塞，直到至少有一个已注册的事件发生
            System.out.println("event is coming , keys="+this.selector.selectedKeys());
            Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();

            while (ite.hasNext()) {
                SelectionKey key = (SelectionKey)ite.next();
                ite.remove(); //从集合中移除即将处理的SelectionKey，避免重复处理
                if(key.isAcceptable()){ //客户端请求链接事件
                    System.out.println("处理链接事件");
                    this.accept(key);
                }else if(key.isReadable()) { //读事件
                    System.out.println("处理读事件");
                    this.read(key);
                }
            }

        }
    }

    private void accept(SelectionKey key) throws Exception {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel channel = server.accept(); //接收链接
        channel.configureBlocking(false);//设置为非阻塞
        System.out.println("开始注册读事件");
        channel.register(this.selector,SelectionKey.OP_READ);//为通道注册读事件
    }

    private void read(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024); //创建读取的缓冲区
        channel.read(buffer); //读取数据
        String request = new String(buffer.array()).trim();
        System.out.println("客户端请求:"+request);
        ByteBuffer outBuffer = ByteBuffer.wrap("请求收到".getBytes());
        channel.write(outBuffer); //将消息会送给客户端
    }

    public static void main(String[] args) throws Exception {
        NIOServer server = new NIOServer();
        server.init();
        server.start();
    }

}
