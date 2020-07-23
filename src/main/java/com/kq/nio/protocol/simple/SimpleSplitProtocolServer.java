package com.kq.nio.protocol.simple;

import com.kq.common.MyRunnable;
import com.kq.util.ByteBufferUtil;
import com.kq.util.Constant;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 接收客户端多个包，组成1个完整的数据
 * @author kq
 * @date 2020-07-21 18:38
 * @since 2020-0630
 */
public class SimpleSplitProtocolServer implements MyRunnable {

    Selector selector;

    private AtomicInteger readInteger = new AtomicInteger(0);
    private AtomicInteger writeInteger = new AtomicInteger(0);

    public SimpleSplitProtocolServer() throws Exception {
        this.init();
    }

    @Override
    public void init() throws Exception {
        selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(Constant.SERVER.IP,Constant.SERVER.PORT));
//        serverSocketChannel.bind(new InetSocketAddress(Constant.SERVER.IP,Constant.SERVER.PORT));

        System.out.println("server listen port : "+serverSocketChannel.socket().getLocalPort());

        serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);

    }

    @Override
    public void start() throws Exception {
        while (true) {
            this.selector.select();
            System.out.println("the coming keys="+this.selector.selectedKeys());

            Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if(key.isValid() && key.isAcceptable()) {
                    this.accept(key);
                } else if(key.isValid() && key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel)key.channel();
                    try {
                        this.read(key,socketChannel);
                    }catch (Exception e){
                        key.cancel();
                        socketChannel.socket().close();
                        socketChannel.close();
                        e.printStackTrace();
                    }
                }else if(key.isValid() && key.isWritable()) {

                }

            }

        }

    }

    private void accept(SelectionKey key) throws Exception{
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(this.selector,SelectionKey.OP_READ);
        System.out.println("注册完读事件");

    }

    private void read(SelectionKey key,SocketChannel socketChannel) {

        System.out.println("SimpleProtocolServer开始读数据");

        ByteBuffer cacheByteBuffer = (ByteBuffer)key.attachment();




        int index = readInteger.getAndIncrement();
        try{
            //数据结构
            //以0xdabb开头
            //数据长度
            //具体的value
            ByteBuffer thisReadByteBuffer = ByteBuffer.allocate(1024);
            socketChannel.read(thisReadByteBuffer);
            thisReadByteBuffer.flip();
            System.out.println(index+". 服务端byteBuffer="+thisReadByteBuffer);

            ByteBuffer byteBuffer = ByteBufferUtil.byteBufferJoin(cacheByteBuffer,thisReadByteBuffer);
            System.out.println(index+". 附件cacheByteBuffer="+cacheByteBuffer);
            System.out.println(index+". 合并的byteBuffer="+byteBuffer);
            System.out.println(index+". start do bussiness");

            while (true && byteBuffer.hasRemaining()) {
                System.out.println(index+". start do read data");
                short magicNum = byteBuffer.getShort(); //position+2
                //无符号
                int magic = Short.toUnsignedInt(magicNum);
                if(magic!=Constant.MAGIC_NUMBER) {
                    System.out.println("index="+index+",无效的Magic数字 magicNum=" + Constant.MAGIC_NUMBER + " magic=" + magic);

                    byteBuffer.position(0);
                    ByteBuffer newByteBuffer = ByteBufferUtil.newByteBufferByPosition(byteBuffer);
                    System.out.println("attach0="+newByteBuffer);
                    key.attach(newByteBuffer);
                    break;
                }
                // data的长度
                int length = byteBuffer.getInt();
                System.out.println("index=" + index + "  length=" + length);
                System.out.println("2. 服务端byteBuffer="+byteBuffer);
                int remainSize = byteBuffer.limit()-byteBuffer.position();
                System.out.println("remainSize="+remainSize);
                if(remainSize>=length) { //这里只读1次完整的数据，如果有多条完整的数据，这里也只读1次,留着下次读
                    // 读取完  ， 把读取的删除掉
                    // 拷贝poisiston 至 limit的数据
                    byte[] datas = new byte[length];

                    byteBuffer.get(datas);

                    String request = new String(datas).trim();
                    System.out.println("index=" + index + " 客户端请求:" + request);

                    ByteBuffer newByteBuffer = ByteBufferUtil.newByteBufferByPosition(byteBuffer);
                    System.out.println("attach1="+newByteBuffer);
                    key.attach(newByteBuffer);
                    break;

                }else {
                    key.attach(byteBuffer);
                    System.out.println("attach2="+byteBuffer);
                    break;
                }

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

    private void write(SelectionKey key) throws Exception{

    }

    public static void main(String[] args) throws Exception{
        new SimpleSplitProtocolServer().start();
    }
}
