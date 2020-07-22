package com.kq.binary;

/**
 * @author kq
 * @date 2020-07-22 8:38
 * @since 2020-0630
 */
public class BinaryDemo {

    public static void main(String[] args) {
        short num = (short)65535;
        System.out.println("65535 binary = "+Integer.toBinaryString(num));
        System.out.println(num);

        int unsignedInt = Short.toUnsignedInt(num);
        System.out.println("short无符号 65535 unsignedInt="+unsignedInt);
        System.out.println("short有符号 65535="+num);

    }

}
