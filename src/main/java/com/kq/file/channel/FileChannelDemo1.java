package com.kq.file.channel;

import com.kq.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelDemo1 {

    public static void main(String[] args) throws Exception {
        FileUtil.ensureDirOK("c:\\log");
        // ababcde
        try(
            FileOutputStream outputStream = new FileOutputStream(new File("c:\\log\\helloworld.txt"));
            FileChannel fileChannel = outputStream.getChannel();
        ){
            ByteBuffer buffer = ByteBuffer.wrap("abcde".getBytes());
            System.out.println("1. fileChannel.position="+fileChannel.position());
            System.out.println("write() 1 返回值:"+fileChannel.write(buffer));// abcde
            System.out.println("2. fileChannel.position="+fileChannel.position());
            fileChannel.position(2); // ab
            buffer.rewind(); //buffer还原position=0
            System.out.println("write() 2 返回值:"+fileChannel.write(buffer)); // ababcde
            System.out.println("3. fileChannel.position="+fileChannel.position()); //7

        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

}
