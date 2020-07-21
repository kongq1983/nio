package com.kq.util;

import java.nio.ByteBuffer;

/**
 * @author kq
 * @date 2020-07-20 19:39
 * @since 2020-0630
 */
public class ByteBufferUtil {


    public static String toString(ByteBuffer readBuffer) {
        readBuffer.flip();
        byte[] bytes = new byte[readBuffer.remaining()];
        //获取缓冲区并写入字节数组中
        readBuffer.get(bytes);
        //将字节数组转换为String类型
        String body = new String(bytes);
        return body;
    }

}
