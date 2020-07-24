package com.kq.nio.myreactor;

import com.kq.util.Constant;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kq
 * @date 2020-07-23 16:31
 * @since 2020-0630
 */
public class ReadThread extends Thread{

    private Selector selector;

    private WriteThread writeThread;

    private AtomicInteger readInteger = new AtomicInteger(0);

    public ReadThread(int index) throws Exception{
        super("read-thread-"+index);
        this.init();
    }

    private void init() throws IOException{
        this.selector = Selector.open();
    }

    @Override
    public void run() {

        while (true) {
            try {
                Iterator<SelectionKey> iter = this.selector.selectedKeys().iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    SocketChannel socketChannel = (SocketChannel)key.channel();

                    try{
                        this.doRead(key,socketChannel);
                    }catch (Exception e){
                        key.cancel();
                        socketChannel.socket().close();
                        socketChannel.close();
                        e.printStackTrace();
                    }

                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    private void doRead(SelectionKey key, SocketChannel socketChannel) {

        try {
            this.read(key,socketChannel);
            writeThread.register(socketChannel);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void read(SelectionKey key,SocketChannel socketChannel) {

        System.out.println("SimpleProtocolServer开始读数据");

        int index = readInteger.getAndIncrement();
        try{
            //数据结构
            //以0xdabb开头
            //数据长度
            //具体的value
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            socketChannel.read(byteBuffer);
            byteBuffer.flip();

            while (true && byteBuffer.hasRemaining()) {
                short magicNum = byteBuffer.getShort(); //position+2
                //无符号
                int magic = Short.toUnsignedInt(magicNum);
                if(magic!= Constant.MAGIC_NUMBER) {
                    System.out.println("index="+index+",无效的Magic数字 magicNum=" + Constant.MAGIC_NUMBER + " magic=" + magic);
                    break;
                }
                // data的长度
                int length = byteBuffer.getInt();
                System.out.println("index=" + index + "  length=" + length);

                byte[] datas = new byte[length];

                byteBuffer.get(datas);

                String request = new String(datas).trim();
                System.out.println("index=" + index + " 客户端请求:" + request);

            }


        }catch (Exception e){
            System.out.println("读事件报错"+e.getMessage());
            key.cancel();
            try {
                socketChannel.socket().close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public void setWriteThread(WriteThread writeThread) {
        this.writeThread = writeThread;
    }

    public void register(SocketChannel socketChannel) throws ClosedChannelException {
        socketChannel.register(this.selector, SelectionKey.OP_READ);
    }

}
