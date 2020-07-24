package com.kq.nio.myreactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * 接收新的连接
 * @author kq
 * @date 2020-07-23 15:09
 * @since 2020-0630
 */
public class AcceptThread extends Thread{

    private Selector selector;
    private AcceptIOThread ioThread;

    public static final String INCOMING_MSG = "the new incoming accetp request ! client-port=%s, client-ip=%s ";

    public AcceptThread(AcceptIOThread ioThread) throws IOException{
        super("accept-thread-0");
        this.ioThread = ioThread;
        this.init();
    }

    public void init() throws IOException {
        selector = Selector.open();
    }

    @Override
    public void run() {
        while(true) {
            try {
                this.selector.select();

                Iterator<SelectionKey> iter = this.selector.selectedKeys().iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if(key.isAcceptable()) {
                        SocketChannel socketChannel = ((ServerSocketChannel)key.channel()).accept();

                        InetSocketAddress clientSocketAddress = (InetSocketAddress)socketChannel.getRemoteAddress();
                        String msg = String.format(INCOMING_MSG,clientSocketAddress.getPort(),clientSocketAddress.getHostName());
                        System.out.println(msg);

                        this.ioThread.register(socketChannel);
                    }

                }

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void register(ServerSocketChannel serverSocketChannel) throws ClosedChannelException {
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

}
