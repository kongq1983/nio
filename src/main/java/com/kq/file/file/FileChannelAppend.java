package com.kq.file.file;

import com.kq.util.FileUtil;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * FileChannel追加
 * @author kq
 * @date 2020-12-24 10:35
 * @since 2020-0630
 */
public class FileChannelAppend {


    private static int filesize = 0;

    // 协议: length MESSAGE_MAGIC_CODE开始  body
    public static void main(String[] args) throws Exception{
        String path = "D:\\myprotocol";
        FileUtil.ensureDirOK(path);
//        System.out.println(Integer.toHexString(MESSAGE_MAGIC_CODE));
//        System.out.println(Integer.toHexString(BLANK_MAGIC_CODE));

        writeNumber(path,1);
        writeNumber(path,2);
        writeNumber(path,3);


    }
    // filechannel 的方法write(ByteBuffer src,long position)，position为从文件的位置开始写入
    private static void writeNumber(String path,int type) throws Exception {
        int size = 12;
        ByteBuffer buffer =ByteBuffer.allocate(size);
        if(type==1){
            buffer.put("1234567890\r\n".getBytes());
        }else if(type==2) {
            buffer.put("abcde12345\r\n".getBytes());
        } else {
            buffer.put("67890aaaaa\r\n".getBytes());
        }

        buffer.flip();

        try(
                RandomAccessFile toFile = new RandomAccessFile(path + "\\protocol", "rw");
        ) {
            toFile.getChannel().write(buffer,filesize);
            filesize = filesize+size;
        }
    }



}
