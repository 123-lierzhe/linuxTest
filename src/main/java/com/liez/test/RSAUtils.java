package com.liez.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;



public class RSAUtils {
	public static String checkSignAndDecrypt(RequestResponseBody requestJsonBody, String signKey, String privateKey,
			String charset, boolean isDecrypt, boolean isCheckSign) throws Exception {
		if (charset == null || "".equalsIgnoreCase(charset))
			charset = "UTF-8";
		if (isCheckSign) {
			if (requestJsonBody.getHead() == null)
				throw new RuntimeException("验签失败");
			if (!SignBuildUtils.sign(requestJsonBody, signKey).equalsIgnoreCase(requestJsonBody.getHead().getSign()))
				throw new RuntimeException("验签失败");
		}
		if (isDecrypt)
			return URLDecoder.decode(rsaDecrypt(requestJsonBody.getBody(), privateKey, charset), "UTF-8");
		return requestJsonBody.getBody();
	}

	public static Map<String, String> encryptAndSign(Head head, String body, String signKey, String publicKey,
			String charset, boolean isEncrypt, boolean isSign) throws Exception {
		if (charset == null || "".equalsIgnoreCase(charset))
			charset = "UTF-8";
		body = URLEncoder.encode(body, "UTF-8");
		Map<String, String> map = new HashMap<String, String>();
		if (isEncrypt) {
			String encrypted = rsaEncrypt(body, publicKey, charset);
			map.put("CONTENT", encrypted);
			if (isSign) {
				String sign = SignBuildUtils.sign(head, encrypted, signKey);
				map.put("SIGN", sign);
			}
		} else if (isSign) {
			map.put("CONTENT", body);
			String sign = SignBuildUtils.sign(head, body, signKey);
			map.put("SIGN", sign);
		}
		return map;
	}

	public static String rsaEncrypt(String content, String publicKey, String charset) throws Exception {
		PublicKey pubKey = getPublicKeyFromX509("RSA", new ByteArrayInputStream(publicKey.getBytes()));
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(1, pubKey);
		byte[] data = isEmpty(charset) ? content.getBytes() : content.getBytes(charset);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		int i = 0;
		while (inputLen - offSet > 0) {
			byte[] cache;
			if (inputLen - offSet > 117) {
				cache = cipher.doFinal(data, offSet, 117);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * 117;
		}
		byte[] encryptedData = Base64.encodeBase64(out.toByteArray());
		out.close();
		return isEmpty(charset) ? new String(encryptedData) : new String(encryptedData, charset);
	}

	public static String rsaDecrypt(String content, String privateKey, String charset) throws Exception {
		try {
			PrivateKey priKey = getPrivateKeyFromPKCS8("RSA", new ByteArrayInputStream(privateKey.getBytes()));
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(2, priKey);
			byte[] encryptedData = isEmpty(charset) ? Base64.decodeBase64(content.getBytes())
					: Base64.decodeBase64(content.getBytes(charset));
			int inputLen = encryptedData.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			int i = 0;
			while (inputLen - offSet > 0) {
				byte[] cache;
				if (inputLen - offSet > 128) {
					cache = cipher.doFinal(encryptedData, offSet, 128);
				} else {
					cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * 128;
			}
			byte[] decryptedData = out.toByteArray();
			out.close();
			return isEmpty(charset) ? new String(decryptedData) : new String(decryptedData, charset);
		} catch (Exception e) {
			throw new Exception("EncodeContent = " + content + ",charset = " + charset, e);
		}
	}

	public static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		StringWriter writer = new StringWriter();
		StreamUtils.io(new InputStreamReader(ins), writer);
		byte[] encodedKey = writer.toString().getBytes();
		encodedKey = Base64.decodeBase64(encodedKey);
		return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
	}

	public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
		if (ins == null || isEmpty(algorithm))
			return null;
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		byte[] encodedKey = StreamUtils.readText(ins).getBytes();
		encodedKey = Base64.decodeBase64(encodedKey);
		return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
	}

	public static boolean isEmpty(String value) {
		return (null == value || "".equals(value.trim()));
	}
}
