package com.kq.nio.reactor.demo1;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * SubReactor
 *
 * @author kq
 * @date 2019-06-20
 */
public class SubReactor extends Thread{

    private Selector selector;
    private volatile boolean running = false;

    public SubReactor() throws IOException{
        selector = Selector.open();
    }


    @Override
    public synchronized void start() {
        if (!running) {
            running = true;
            super.start();
        }
    }


    @Override
    public void run() {

        while (running) {
            try{

                selector.select();// 阻塞，直到事件通知才会返回
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if(key.isReadable()) {
                        try{
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            new Handler(socketChannel);
                        }catch (Exception e) {

                            e.printStackTrace();
                            key.cancel();
                        }
                    }

                }

            }catch(Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 将客户端channel注册到selector中，注册OP_READ事件
     * @param socketChannel
     * @throws ClosedChannelException
     */
    public void register(SocketChannel socketChannel) throws ClosedChannelException {
        socketChannel.register(selector, SelectionKey.OP_READ);
    }


}
