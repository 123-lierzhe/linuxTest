package com.liez.test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	public static String encodeToMD5(String str) {
		StringBuffer sb = new StringBuffer();
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] bytes = digest.digest(str.getBytes("UTF-8"));
			for (int i = 0; i < bytes.length; i++) {
				String s = Integer.toHexString(0xFF & bytes[i]);
				if (s.length() == 1) {
					sb.append("0" + s);
				} else {
					sb.append(s);
				}
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException("加密失败");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
