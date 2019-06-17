package com.kq.util;

/**
 * 数字与字节数组转换
 * <p>
 * short 2字节数组
 * </p>
 * <p>
 * int 4字节
 * </p>
 * <p>
 * long 8字节
 * </p>
 * 
 *
 */
public class ByteUtil {
	/** short2字节数组 */
	public static byte[] short2bytes(short v) {
//		byte[] b = new byte[4];
		byte[] b = new byte[2];
		b[1] = (byte) v;
		b[0] = (byte) (v >>> 8);
		return b;
	}

	/** int4字节数组 */
	public static byte[] int2bytes(int v) {
		byte[] b = new byte[4];
		b[3] = (byte) v;
		b[2] = (byte) (v >>> 8);
		b[1] = (byte) (v >>> 16);
		b[0] = (byte) (v >>> 24);
		return b;
	}

	/** long8字节数组 */
	public static byte[] long2bytes(long v) {
		byte[] b = new byte[8];
		b[7] = (byte) v;
		b[6] = (byte) (v >>> 8);
		b[5] = (byte) (v >>> 16);
		b[4] = (byte) (v >>> 24);
		b[3] = (byte) (v >>> 32);
		b[2] = (byte) (v >>> 40);
		b[1] = (byte) (v >>> 48);
		b[0] = (byte) (v >>> 56);
		return b;
	}

	/** 字节数组转字符串 */
	public static String bytesToHexString(byte[] bs) {
		if (bs == null || bs.length == 0) {
			return null;
		}

		StringBuffer sb = new StringBuffer();
		String tmp = null;
		for (byte b : bs) {
			tmp = Integer.toHexString(Byte.toUnsignedInt(b));
			if (tmp.length() < 2) {
				sb.append(0);
			}
			sb.append(tmp);
		}
		return sb.toString();
	}


	private static byte[] intToBytes(int val){
		byte[] b = new byte[4];
		b[0] = (byte)(val & 0xff);
		b[1] = (byte)((val >> 8) & 0xff);
		b[2] = (byte)((val >> 16) & 0xff);
		b[3] = (byte)((val >> 24) & 0xff);

		return b;
	}

	public static void main(String[] args) {
		byte[] bs = int2bytes(1111);

		for(byte b : bs) {
			System.out.println(b);
		}

		System.out.println("--------------------");

		byte[] bs1 = intToBytes(1111);

		for(byte b : bs1) {
			System.out.println(b);
		}

		System.out.println(bytesToHexString(bs));
		System.out.println(bytesToHexString(bs1));

	}
}
