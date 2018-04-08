package com.niudantg.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5 {

	public static String getMd5(byte[] buffer) throws NoSuchAlgorithmException {
		String s = null;
		char hexDigist[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(buffer);
		byte[] datas = md.digest(); // 16个字节的长整数
		char[] str = new char[2 * 16];
		int k = 0;
		for (int i = 0; i < 16; i++) {
			byte b = datas[i];
			str[k++] = hexDigist[b >>> 4 & 0xf];// 高4位
			str[k++] = hexDigist[b & 0xf];// 低4位
		}
		s = new String(str);
		return s;
	}

	public static String getmd5(byte[] buffer) throws NoSuchAlgorithmException {
		String s = null;
		char hexDigist[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(buffer);
		byte[] datas = md.digest(); // 16个字节的长整数
		char[] str = new char[2 * 16];
		int k = 0;
		for (int i = 0; i < 16; i++) {
			byte b = datas[i];
			str[k++] = hexDigist[b >>> 4 & 0xf];// 高4位
			str[k++] = hexDigist[b & 0xf];// 低4位
		}
		s = new String(str);
		return s;
	}
}