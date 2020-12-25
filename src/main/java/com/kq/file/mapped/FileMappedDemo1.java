package com.kq.file.mapped;

import com.kq.util.FileUtil;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FileMappedDemo1 {

    public static void main(String[] args) {
        File file = new File("D:\\myprotocol\\mapper1");

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
            buffer.put((byte)'o');
            buffer.put((byte)'p');
            buffer.put((byte)'q');
            buffer.put((byte)'r');
            buffer.put((byte)'s');


        }catch (Exception e) {
            e.printStackTrace();
        }

    }

//    private void fillPreBlank(final MappedByteBuffer buffer, final long untilWhere) {
//        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
//        byteBuffer.putLong(0L); // 填充0
//        byteBuffer.putInt(Integer.MAX_VALUE);
//        byteBuffer.putLong(0L); // 填充0
//
//        int until = (int) (untilWhere % this.mappedFileQueue.getMappedFileSize());
//        for (int i = 0; i < until; i += CQ_STORE_UNIT_SIZE) {
//            mappedFile.appendMessage(byteBuffer.array());
//        }
//    }

}
