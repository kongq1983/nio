package com.kq.nio.reactor.demo1;

/**
 * MainReactor
 *
 * @author kq
 * @date 2019-06-20
 */

import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class MainReactor extends Thread {
    private Selector selector;
    private AtomicInteger incr = new AtomicInteger(0);

    public MainReactor() throws IOException {
        selector = Selector.open();
    }

    @Override
    public void run() {
        while (true) {
            try {
                //启动管理者
                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    if(selectionKey.isAcceptable()) {
                        SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();

                        // 将客户端连接给到acceptor
                        new Acceptor(socketChannel);
                    }

                }


            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Acceptor {

        public Acceptor(SocketChannel socketChannel) throws IOException {
            socketChannel.configureBlocking(false);
            int index = incr.getAndIncrement() % NIOReactorDemo.subReactors.length;
            SubReactor subReactor = NIOReactorDemo.subReactors[index];
            subReactor.register(socketChannel);
            subReactor.start();
            System.out.println("收到新连接：" + socketChannel);
        }

    }

    /**
     * 将服务端channel注册到selector中，注册OP_ACCEPT事件
     * @param channel
     * @throws ClosedChannelException
     */
    public void register(ServerSocketChannel channel) throws ClosedChannelException {
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

}
