package com.kq.nio.answerserver;

import com.kq.util.Constant;
import org.apache.commons.io.IOUtils;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author kq
 * @date 2020-07-21 9:37
 * @since 2020-0630
 */
public class AnswerBIOClient {

    Socket socket = new Socket();

    private AtomicLong atomicLong = new AtomicLong();

    private boolean isConnectionOk = false;

    public AnswerBIOClient(){

        try{
            init();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void init() throws Exception{
        socket.connect(new InetSocketAddress(Constant.SERVER.IP,Constant.SERVER.PORT));
        if(socket.isConnected()) {
            System.out.println("AnswerBIOClient连接成功! 服务端端口="+socket.getPort()+" 客户端端口="+socket.getLocalPort());
        }
    }

    public void start() throws Exception{
        Runnable readRunnable = ()->{
            try {
                read();
            }catch (Exception e){
                System.out.println("readRunnable exception "+e.getMessage());
                e.printStackTrace();
            }
        };

        Runnable writeRunnable = ()->{
            try {
                write();
            }catch (Exception e){
                System.out.println("readRunnable exception "+e.getMessage());
                e.printStackTrace();
            }
        };

        Thread readThread = new Thread(readRunnable,"readThread");
        Thread writeThread = new Thread(writeRunnable,"writeThread");

        readThread.start();
        writeThread.start();

    }

    private void read() throws Exception{

        while(true) {
            System.out.println("等待读数据");
            String res = IOUtils.toString(socket.getInputStream(), "utf-8");
            System.out.println("AnswerBIOClient read server data =" + res);

            if (res != null && AnswerServer.RES_CONNECTION_OK.endsWith(res.trim())) {
                System.out.println("AnswerBIOClient set isConnectionOk = true");
                isConnectionOk = true;
            }
        }

    }

    private void write() throws Exception{

        while (true) {
//            if (isConnectionOk) {
                TimeUnit.SECONDS.sleep(5);
                Long questionId = atomicLong.incrementAndGet();
                System.out.println("客户端开始发送 questionId=" + questionId);
                socket.getOutputStream().write(String.valueOf(questionId).getBytes());
                socket.getOutputStream().flush();
//            }
        }

    }

    public static void main(String[] args) throws Exception{

        new AnswerBIOClient().start();

    }

}
