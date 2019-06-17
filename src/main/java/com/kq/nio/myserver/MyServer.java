package com.kq.nio.myserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 以0xdabb开头  16 bits
 * data length   32 bits
 * data
 */
public class MyServer {

    private Selector selector;
    public static final int SERVER_PORT = 8300;

    public static final int MAGIC_NUMBER = 55995;

    private AtomicInteger atomicInteger = new AtomicInteger(1);

    public void init() throws Exception {
        this.selector = Selector.open();
        //创建ServerSocketChannel
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        ServerSocket serverSocket = channel.socket();
        InetSocketAddress address = new InetSocketAddress(SERVER_PORT);
        serverSocket.bind(address);
        System.out.println("Server listen port : " + SERVER_PORT);
        System.out.println("Server Channel Wait Client Connect .");
        channel.register(this.selector, SelectionKey.OP_ACCEPT);
        System.out.println("init end----------------------");

    }

    public void start() throws Exception {
        System.out.println("start method ---------------------");
        while (true) {
            this.selector.select(); //此方法会阻塞，直到至少有一个已注册的事件发生
            System.out.println("event is coming , keys=" + this.selector.selectedKeys());
            Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();

            while (ite.hasNext()) {
                SelectionKey key = (SelectionKey) ite.next();
                ite.remove(); //从集合中移除即将处理的SelectionKey，避免重复处理
                if (key.isAcceptable()) { //客户端请求链接事件
                    System.out.println("处理链接事件");
                    this.accept(key);
                } else if (key.isValid() && key.isReadable()) { //读事件
                    System.out.println("处理读事件");
                    SocketChannel channel = (SocketChannel) key.channel();
                    try{
                        this.read(channel,key);
                    }catch (IOException e){
                        System.out.println("----------------read exception---------------key=---"+key);
                        e.printStackTrace();

                        key.cancel();
                        channel.socket().close();
                        channel.close();

                    }
                } else if (key.isValid() && key.isWritable()) {
                    System.out.println("writable--------------");
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
//        ByteBuffer outBuffer = ByteBuffer.wrap("hi , accept success!".getBytes());
//        channel.write(outBuffer); //写信息
        this.sayHello(channel);
        System.out.println("----------aaaaaa-------------");
//        channel.register(this.selector, SelectionKey.OP_WRITE);//为通道注册读事件

    }

    private void read(SocketChannel channel,SelectionKey key) throws Exception {

        int index = atomicInteger.getAndIncrement();

        ByteBuffer buffer = ByteBuffer.allocate(1024); //创建读取的缓冲区
        channel.read(buffer); //读取数据
        buffer.flip();

        while(true && buffer.hasRemaining()) {
            short magicShort = buffer.getShort();
            int magic = Short.toUnsignedInt(magicShort);

            if (magic != MAGIC_NUMBER) {
                System.out.println("index=" + index + " 无效的Magic数字 magicShort=" + magicShort + " magic=" + magic);
//                return;
                break;
            }

            System.out.println("index=" + index + "  magic=" + magic);

            int length = buffer.getInt();
            System.out.println("index=" + index + "length=" + length);

            byte[] data = new byte[length];
            buffer.get(data);

            buffer.get(length);

//        byte[] bs = buffer.array();
//        for(byte b : bs){
//            System.out.println("接收客户字节数 b="+b);
//        }

            String request = new String(data).trim();
            System.out.println("index=" + index + " 客户端请求:" + request);
            ByteBuffer outBuffer = ByteBuffer.wrap("请求收到".getBytes());
            channel.write(outBuffer); //将消息会送给客户端
        }


    }

    /**
     * Spew a greeting to the incoming client connection. * * @param channel * The newly connected SocketChannel to say hello to.
     */
    private void sayHello(SocketChannel channel) throws Exception {
        ByteBuffer outBuffer = ByteBuffer.wrap("hi , accept success!".getBytes());
        channel.write(outBuffer);
//        buffer.clear();
//        buffer.put("Hi there!\r\n".getBytes());
//        outBuffer.flip();
//        channel.write(outBuffer);
    }

    public static void main(String[] args) throws Exception {
        MyServer server = new MyServer();
        server.init();
        server.start();
    }

}
