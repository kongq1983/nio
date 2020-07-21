package com.kq.nio.myserver1;

import com.kq.util.ByteBufferUtil;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author kq
 * @date 2020-07-17 17:00
 * @since 2020-0630
 */
public class MyServer1 {

    private Selector selector;

    public static final int PORT = 18300;

    private static AtomicLong atomicLong = new AtomicLong();

    public MyServer1(){
        try {
            this.init();
        }catch (Exception e) {
            System.out.println("MyServer1 初始化失败！");
            e.printStackTrace();
        }
    }

    public void init() throws Exception{
        //打开一个多路复用器
        selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.socket().bind(new InetSocketAddress("localhost",PORT),1024);

        System.out.println("Server listen port : " + PORT);

        //对于服务端来说，一定要先注册一个OP_ACCEPT事件用来响应客户端的请求连接
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void start() throws Exception{
        System.out.println("start method ---------------------");

        while (true) {
            this.selector.select();
            System.out.println("the coming keys = "+this.selector.selectedKeys());

            Iterator<SelectionKey> iterable = this.selector.selectedKeys().iterator();

            while (iterable.hasNext()) {
                SelectionKey selectionKey = iterable.next();
                iterable.remove();

                if(selectionKey.isAcceptable()) {
                    System.out.println("有新的Acceptable连接进来 selectionKey="+selectionKey);
                    this.accept(selectionKey);
                }else if(selectionKey.isValid() && selectionKey.isReadable()) { //读事件
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    try {
                        this.read(socketChannel, selectionKey);
                    }catch (Exception e) {
                        System.out.println("读事件报错 "+e.getMessage());
                        e.printStackTrace();

                        selectionKey.cancel();
                        socketChannel.socket().close();
                        socketChannel.close();
                    }

                }
//                else if(selectionKey.isConnectable()){
//                    System.out.println("有新的Connectable连接进来 selectionKey="+selectionKey);
//                }

            }

        }

    }

    private void read(SocketChannel channel,SelectionKey key) throws Exception {
        System.out.println(atomicLong.incrementAndGet()+",开始读数据 key="+key);
        ByteBuffer buffer = ByteBuffer.allocate(100); //创建读取的缓冲区
//        channel.read(buffer); //读取数据

        while (channel.isOpen() && channel.read(buffer) != -1) {
            // 长连接情况下,需要手动判断数据有没有读取结束 (此处做一个简单的判断: 超过0字节就认为请求结束了)
            if (buffer.position() > 0) {
                System.out.println("数据读完了-----------------------position="+buffer.position());
                break;
            }
        }
        if (buffer.position() == 0) {
            System.out.println("没有数据了-----------------------");
            return; // 如果没数据了, 则不继续后面的处理
        }


        System.out.println("========read stirng :" + ByteBufferUtil.toString(buffer));


        ByteBuffer responseBuffer = ByteBuffer.allocate(20);
        responseBuffer.put("ok".getBytes());
        channel.write(responseBuffer);
    }


    private void accept(SelectionKey key) throws Exception {

        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
        //接收链接
        SocketChannel socketChannel = serverSocketChannel.accept();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        System.out.println("开始注册读事件");
        socketChannel.register(selector,SelectionKey.OP_READ);
        //say ok
        this.sayOk(socketChannel);

    }

    private void sayOk(SocketChannel channel) throws Exception {

        ByteBuffer outBuffer = ByteBuffer.wrap("connection is ok!".getBytes());
        channel.write(outBuffer);

    }


    public static void main(String[] args) throws Exception{

        new MyServer1().start();

    }

}
