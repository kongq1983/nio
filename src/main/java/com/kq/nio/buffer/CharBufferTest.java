package com.kq.nio.buffer;

import org.apache.commons.lang3.StringUtils;

import java.nio.CharBuffer;

public class CharBufferTest {

    private CharBuffer buffe;

    public static void main(String[] args) {
        CharBuffer buffe = CharBuffer.allocate(20);
        String str = "welcome to you!";
        for (int i=0;i<str.length();i++) {
            String s = str.substring(i,i+1);
//            System.out.println(str.substring(i,i+1));
            buffe.put(s);

        }
       buffe.flip();

        while (buffe.hasRemaining()){
            char s = buffe.get();
            System.out.println(s);
        }

        System.out.println("-----------------------------------------");
    }

}
