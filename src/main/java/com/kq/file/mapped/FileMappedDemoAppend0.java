package com.kq.file.mapped;

import com.kq.util.FileUtil;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FileMappedDemoAppend0 {

    public static void main(String[] args) {
        File file = new File("D:\\myprotocol\\mapper2");

        FileUtil.ensureDirOK(file.getParent());

        try(RandomAccessFile r = new RandomAccessFile(file,"rw");
            FileChannel fileChannel = r.getChannel();
        ){

            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE,0,1000);

            System.out.println((char)buffer.get()+" position="+buffer.position());
            System.out.println((char)buffer.get()+" position="+buffer.position());
            System.out.println((char)buffer.get()+" position="+buffer.position());
            System.out.println((char)buffer.get()+" position="+buffer.position());
            System.out.println((char)buffer.get()+" position="+buffer.position());

            buffer.position(0);
            fillPreBlank(buffer);
//            buffer.put((byte)'o');
//            buffer.put((byte)'p');
//            buffer.put((byte)'q');
//            buffer.put((byte)'r');
//            buffer.put((byte)'s');


        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 如果没有文件，直接调用这个也会生成1K文件
     * @param args
     */
    public static void main2(String[] args) {
        File file = new File("D:\\myprotocol\\mapper2");

        FileUtil.ensureDirOK(file.getParent());

        try(RandomAccessFile r = new RandomAccessFile(file,"rw");
            FileChannel fileChannel = r.getChannel();
        ){

            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE,0,1000);
            for(int i=0;i<50;i++) {
                Long first = buffer.getLong();
                Integer maxData = buffer.getInt();
                Long last = buffer.getLong();

                System.out.println("first="+first+",maxData="+maxData+",last="+last+",maxData="+Integer.MAX_VALUE);
            }

//            System.out.println((char)buffer.get()+" position="+buffer.position());


        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 用0填充
     * @param buffer
     */
    private static void fillPreBlank(final MappedByteBuffer buffer) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
        byteBuffer.putLong(0L); // 填充0
        byteBuffer.putInt(Integer.MAX_VALUE);
        byteBuffer.putLong(0L); // 填充0

        System.out.println("limit="+buffer.limit());

        for (int i = 0; i < buffer.limit(); i += 20) {
            byteBuffer.flip();
            System.out.println("position="+byteBuffer.position()+",limit="+byteBuffer.limit());
            buffer.put(byteBuffer);
        }
    }

}
