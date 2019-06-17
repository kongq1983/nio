package com.kq.nio.myhttpserver;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * MyHttpServer
 *
 * @author kq
 * @date 2019-06-17
 */
public class MyHttpServer {

    static final int port = 8310;

    public static void main(String[] args) throws Exception{
        // 1. 创建服务端的channel对象
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 2. 创建Selector
        Selector selector = Selector.open();

        SelectionKey selectionKey = serverSocketChannel.register(selector,0);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);

        // 4. 绑定端口，启动服务
        serverSocketChannel.socket().bind(new InetSocketAddress(port)); // 绑定端口
        System.out.println("服务启动成功 监听端口:"+port);

        while (true) {
            // 5. 启动selector（管家）
            selector.select();// 阻塞，直到事件通知才会返回

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if(key.isAcceptable()) {
                    SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("收到新连接：" + socketChannel);

                }else if (key.isReadable()) {// 有可读数据
                    try {
                        SocketChannel socketChannel = (SocketChannel) key.channel();

                        ByteBuffer requestBuffer = ByteBuffer.allocate(1024);
                        while (socketChannel.isOpen() && socketChannel.read(requestBuffer) != -1) {
                            // 长连接情况下,需要手动判断数据有没有读取结束 (此处做一个简单的判断: 超过0字节就认为请求结束了)
                            if (requestBuffer.position() > 0) break;
                        }
                        if (requestBuffer.position() == 0) continue; // 如果没数据了, 则不继续后面的处理
                        requestBuffer.flip();
                        byte[] content = new byte[requestBuffer.remaining()];
                        requestBuffer.get(content);
                        System.out.println(new String(content));
                        System.out.println("收到数据,来自：" + socketChannel.getRemoteAddress());

                        // 响应结果 200
                        String response = "HTTP/1.1 200 OK\r\n" +
                                "Content-Length: 11\r\n\r\n" +
                                "Hello World";
                        ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
                        while (buffer.hasRemaining()) {
                            socketChannel.write(buffer);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        key.cancel();
                    }

                }

            }


        }



    }

}
