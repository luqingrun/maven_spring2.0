package com.gongsibao.common.util.security;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	public static final String CHARSET = "UTF-8";
	public static final String PUB_KEY = "1QA2Z%TGB";
	/**
	 * 加密
	 * @param content
	 * @param key
	 * @return
	 */
	public static String encode(String content, String key) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(key.getBytes(CHARSET));
			byte[] raw = md.digest();
			byte[] result = encrypt(raw, content.getBytes(CHARSET));
			return new String(Base64.encode(result));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解密
	 * @param content
	 * @param key
	 * @return
	 */
	public static String decode(String content, String key) {
		try {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(key.getBytes(CHARSET));
				byte[] raw = md.digest();
//				byte[] enc = Base64.decode(content.getBytes(CHARSET));
				byte[] enc = Base64.decode(content);
				byte[] result = decrypt(raw, enc);
				return new String(result,CHARSET);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decode(String content) {
		return decode(content, PUB_KEY);
	}

	public static String encode(String content) {
		return encode(content, PUB_KEY);
	}
	
	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	
	
	public static void main(String[] args) throws Throwable {
		//String content = "{\"coupon_id\":[\"1\",\"2\"]}";
		//String content = "[{\"coupon_id\":\"1\",\"view_count\":\"3\",\"down_count\":\"3\"},{\"coupon_id\":2,\"view_count\":2323,\"down_count\":233232}]";
		//String key = "aSf$%9*11D13Q0U8";
		//String content = "[{\"coupon_id\":\"20\",\"view_count\":\"33\",\"down_count\":\"18\"},{\"coupon_id\":20,\"view_count\":323,\"down_count\":88}]";
		//String content = "{\"coordinate_type\":\"bd09\",\"longitude\":116.55565454,\"latitude\":29.4546548756555}";
		String content="{\"count\":5,\"longitude\":116.449112,\"coordinate_type\":\"bd09\",\"latitude\":39.923789}";
		String key= "aSf$%9*11D13Q0U8";
		content = AES.encode(content, key);
		System.out.println(content);
		System.out.println(AES.decode(content, key));
		
	}
	

}
