package com.kq.file;

import com.kq.util.ByteBufferUtil;
import com.kq.util.FileUtil;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * 只保存1条数据
 * @author kq
 * @date 2020-12-24 9:20
 * @since 2020-0630
 */
public class OneMyProtocolDemo {

    // 开始 daa320a7
    public final static int MESSAGE_MAGIC_CODE = -626843481;
    // End of file empty MAGIC CODE cbd43194  cbd43194
    protected final static int BLANK_MAGIC_CODE = -875286124;

    public static final int max = 100;

    // 协议: length MESSAGE_MAGIC_CODE开始  body
    public static void main(String[] args) throws Exception{
        String path = "D:\\myprotocol";
        FileUtil.ensureDirOK(path);
//        System.out.println(Integer.toHexString(MESSAGE_MAGIC_CODE));
//        System.out.println(Integer.toHexString(BLANK_MAGIC_CODE));

        write(path);
        read(path);


    }

    private static void write(String path) throws Exception {
        int size = 28;
        ByteBuffer buffer =ByteBuffer.allocate(size);
        buffer.putInt(size); // 4
        buffer.putInt(MESSAGE_MAGIC_CODE); //4
        buffer.put("1234567890abcdefghij".getBytes());
        buffer.flip();

        try(
                RandomAccessFile toFile = new RandomAccessFile(path + "\\protocol", "rw");
        ) {
            toFile.getChannel().write(buffer);
        }
    }

    private static void read(String path) throws Exception {
        RandomAccessFile file = new RandomAccessFile(path + "\\protocol", "rw");
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        // 先读8个字节
        file.getChannel().read(byteBuffer);

        byteBuffer.flip();

        int len = byteBuffer.getInt();
        System.out.println("length="+len);
        int l_message_magic_code = byteBuffer.getInt();
        if(MESSAGE_MAGIC_CODE!=l_message_magic_code) {
            System.out.println("无效的魔幻数字！");
        }
        ByteBuffer bodyBuffer = ByteBuffer.allocate(len-4-4);
        file.getChannel().read(bodyBuffer);
        System.out.println(ByteBufferUtil.toString(bodyBuffer));

    }

}
