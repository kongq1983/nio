package com.kq.util;

import java.nio.ByteBuffer;

/**
 * @author kq
 * @date 2020-07-20 19:39
 * @since 2020-0630
 */
public class ByteBufferUtil {


    public static String toString(ByteBuffer readBuffer) throws Exception{
        readBuffer.flip();
        byte[] bytes = new byte[readBuffer.remaining()];
        //获取缓冲区并写入字节数组中
        readBuffer.get(bytes);
        //将字节数组转换为String类型
        String body = new String(bytes,"utf8");
        return body;
    }

    /**
     * 注意返回合并后的ByteBuffer，一开始不需要调用flip
     * @param byteBuffer1
     * @param byteBuffer2
     * @return
     */
    public static ByteBuffer byteBufferJoin(ByteBuffer byteBuffer1,ByteBuffer byteBuffer2) {

        if(byteBuffer2==null){
            throw new RuntimeException("byteBuffer2不能为空！");
        }

        if(byteBuffer1==null){
            return byteBuffer2;
        }


        int limit1 = byteBuffer1.limit();
        int limit2 = byteBuffer2.limit();

        byte[] bytes = new byte[limit1+limit2];

        System.arraycopy(byteBuffer1.array(),0,bytes,0,limit1);
        System.arraycopy(byteBuffer2.array(),0,bytes,limit1,limit2);

        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

        return byteBuffer;

    }

    public static ByteBuffer positionToLimitByteBuffer(ByteBuffer byteBuffer){
        int position = byteBuffer.position();
        int limit = byteBuffer.limit();

        return ByteBuffer.wrap(byteBuffer.array(),position,limit);

    }

    /**
     * 根据position和limit之间的数据，新生成1个ByteBuffer
     * @param byteBuffer
     */
    public static ByteBuffer newByteBufferByPosition(ByteBuffer byteBuffer) {

        if(byteBuffer==null) {
            return byteBuffer;
        }

        int length = byteBuffer.limit()-byteBuffer.position();

        if(length==0){
            return null;
        }

        byte[] data = new byte[length];

        System.arraycopy(byteBuffer.array(),byteBuffer.position(),data,0,length);

        ByteBuffer result = ByteBuffer.wrap(data);

        return result;

    }

}
