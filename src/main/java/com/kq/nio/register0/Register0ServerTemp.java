package com.kq.nio.register0;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * KeepAliveServer
 * 服务器
 * 每次发固定长度10
 *
 * @author kq
 * @date 2019-06-17
 */
public class Register0ServerTemp {

    public static int port = 8303;

    private static AtomicLong atomicLong = new AtomicLong();

    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
//        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        serverSocketChannel.register(selector, 0);

        serverSocketChannel.bind(new InetSocketAddress(port));
        System.out.println("服务启动成功！ 监听端口:" + port);

        while (true) {
            //阻塞，直到事件通知才会返回
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            if (atomicLong.incrementAndGet() > 10000) {
                break;
            }

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                System.out.println("selectedKey = " + key);
                iterator.remove();

                if (key.isAcceptable()) {
                    SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("收到新连接：" + socketChannel);
                } else if (key.isReadable()) {// 客户端连接有数据可以读时触发
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    try {

                        ByteBuffer requestBuffer = ByteBuffer.allocate(10);
                        int isEnd = -1;
                        while (socketChannel.isOpen() && (isEnd = socketChannel.read(requestBuffer)) != -1) {
                            // 长连接情况下
                            read(requestBuffer, socketChannel);
                            //  // 长连接情况下,需要手动判断数据有没有读取结束 (此处做一个简单的判断: 超过0字节就认为请求结束了)
                            if (requestBuffer.position() > 0) break;

                        }

                        if (isEnd == -1) {
                            // mac
                            // Process finished with exit code 130 (interrupted by signal 2: SIGNINT)
                            key.cancel();
                            System.out.println("---------------break--------------");
                            break;
                        }

                        System.out.println("********************************");
//                        if (requestBuffer.position() == 0) {
//
//                            continue; // 如果没数据了, 则不继续后面的处理
//                        }

//                        read(requestBuffer,socketChannel);

                    } catch (Exception e) {
                        e.printStackTrace();
                        key.cancel();
                        socketChannel.socket().close();
                        socketChannel.close();

                    }
                }


            }
        }

        System.out.println("atomicLong="+atomicLong.get());
        serverSocketChannel.socket().close();
        serverSocketChannel.close();

    }

    private static void read(ByteBuffer requestBuffer, SocketChannel socketChannel) throws Exception {
        requestBuffer.flip();
        byte[] content = new byte[requestBuffer.remaining()];
        requestBuffer.get(content);

        System.out.println(new String(content));

        String contentstr = new String(content);

        System.out.println("limit=" + requestBuffer.limit() + " pos=" + requestBuffer.position());

        System.out.println("收到数据：" + contentstr + "，来自：" + socketChannel.getRemoteAddress());
    }


}
