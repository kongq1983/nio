package com.kq.nio.keepalive;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * KeepAliveServer
 * 服务器
 * 每次发固定长度10
 * @author kq
 * @date 2019-06-17
 */
public class KeepAliveServer {

    public static int port = 8302;

    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        serverSocketChannel.bind(new InetSocketAddress(port));
        System.out.println("服务启动成功！ 监听端口:"+port);

        while (true) {
            //阻塞，直到事件通知才会返回
            selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("收到新连接：" + socketChannel);
                } else if (key.isReadable()) {// 客户端连接有数据可以读时触发
                    try {
                        SocketChannel socketChannel = (SocketChannel) key.channel();

                        ByteBuffer requestBuffer = ByteBuffer.allocate(10);
                        while (socketChannel.isOpen() && socketChannel.read(requestBuffer) != -1) {
                            // 长连接情况下
                            read(requestBuffer,socketChannel);

                        }
                        if (requestBuffer.position() == 0) continue; // 如果没数据了, 则不继续后面的处理

                        read(requestBuffer,socketChannel);

                    } catch (Exception e) {
                        e.printStackTrace();
                        key.cancel();
                    }
                }


            }
        }

    }

    private static void read(ByteBuffer requestBuffer,SocketChannel socketChannel) throws Exception{
        requestBuffer.flip();
        byte[] content = new byte[requestBuffer.remaining()];
        requestBuffer.get(content);
        System.out.println(new String(content));

        String contentstr = new String(content);

        System.out.println("收到数据："+contentstr+"，来自：" + socketChannel.getRemoteAddress());
    }


}
