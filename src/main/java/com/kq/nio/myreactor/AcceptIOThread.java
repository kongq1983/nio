package com.kq.nio.myreactor;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.Random;

/**
 * @author kq
 * @date 2020-07-23 16:31
 * @since 2020-0630
 */
public class AcceptIOThread extends Thread{

    private ReadThread[] readThreads = new ReadThread[5];
    private WriteThread[] writeThreads = new WriteThread[5];
    private Random random = new Random();

    public AcceptIOThread() throws Exception{
        super("io-thread-0");
        this.init();
    }


    public void init() throws Exception{

        for(int i=0;i<readThreads.length;i++) {
            readThreads[i] = new ReadThread(i);
            readThreads[i].start();
        }

        for(int i=0;i<writeThreads.length;i++) {
            writeThreads[i] = new WriteThread(i);
            writeThreads[i].start();
        }
    }





    public void register(SocketChannel socketChannel) throws ClosedChannelException {
        int readThreadIndex = random.nextInt(readThreads.length);
        int writeThreadIndex = random.nextInt(writeThreads.length);

        readThreads[readThreadIndex].register(socketChannel);
        writeThreads[writeThreadIndex].register(socketChannel);

        readThreads[readThreadIndex].setWriteThread(writeThreads[writeThreadIndex]);
        writeThreads[writeThreadIndex].setReadThread(readThreads[readThreadIndex]);

    }




}
