package com.kq.nio.answerserver;

import com.kq.util.Constant;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

    private void read1() throws Exception{
        while(true) {
            //服务器返回需要加\n
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("等待读数据");
            String line = null;
            while((line=bufferedReader.readLine())!=null) {
                System.out.println("AnswerBIOClient read server data =" + line);
            }

        }

    }

    private void read() throws Exception{

        while(true) {
            System.out.println("等待读数据");

            byte[] bytes = new byte[512];
            int readLength = socket.getInputStream().read(bytes);
            System.out.println("readLength="+readLength);
            while (readLength!=-1) {
                String result = new String(bytes,0,readLength);
                if(!isConnectionOk){
                    isConnectionOk = result.equals(AnswerServer.RES_CONNECTION_OK);
                    System.out.println("result="+result+" isConnectionOk="+isConnectionOk+" o:"+result.equals(AnswerServer.RES_CONNECTION_OK));
                }
            }

        }

    }

    private void write() throws Exception{

        while (true) {
            if (isConnectionOk) {
                TimeUnit.SECONDS.sleep(5);
                Long questionId = atomicLong.incrementAndGet();
                System.out.println("客户端开始发送 questionId=" + questionId);
                socket.getOutputStream().write(String.valueOf(questionId).getBytes());
                socket.getOutputStream().flush();
            }
        }

    }

    public static void main(String[] args) throws Exception{

        new AnswerBIOClient().start();

    }

}
