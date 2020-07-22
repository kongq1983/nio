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

    public static ByteBuffer byteBufferJoin(ByteBuffer byteBuffer1,ByteBuffer byteBuffer2) {

        if(byteBuffer1==null){
            return byteBuffer2;
        }

        if(byteBuffer2==null){
            throw new RuntimeException("byteBuffer2不能为空！");
        }


        int limit1 = byteBuffer1.limit();
        int limit2 = byteBuffer2.limit();

        byte[] bytes = new byte[limit1+limit2];

        System.arraycopy(byteBuffer1.array(),0,bytes,0,limit1);
        System.arraycopy(byteBuffer2.array(),0,bytes,limit1,limit2);

        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

        return byteBuffer;

    }

}
