package com.kq.nio.myreactor;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author kq
 * @date 2020-07-23 16:31
 * @since 2020-0630
 */
public class WriteThread extends Thread{

    private Selector selector;

    private ReadThread readThread;

    public WriteThread(int index) throws Exception{
        super("write-thread-"+index);
        this.init();
    }

    private void init() throws IOException {
        this.selector = Selector.open();
    }

    @Override
    public void run() {

        while (true){
            Iterator<SelectionKey> iter = this.selector.selectedKeys().iterator();

            while(iter.hasNext()) {
                SelectionKey  selectionKey = iter.next();
                iter.remove();

            }

        }

    }



    public void setReadThread(ReadThread readThread) {
        this.readThread = readThread;
    }

    public void register(SocketChannel socketChannel) throws ClosedChannelException {
        socketChannel.register(this.selector, SelectionKey.OP_WRITE);
    }
}
