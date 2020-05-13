package com.zhou.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5FileUtil {
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private  MessageDigest messagedigest = null;
	
	public MessageDigest getMessagedigest() {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsaex) {
			System.err.println(Md5FileUtil.class.getName()
					+ "初始化失败，MessageDigest不支持MD5Util。");
			nsaex.printStackTrace();
		}
		return messagedigest;
	}

	public void setMessagedigest(MessageDigest messagedigest) {
		this.messagedigest = messagedigest;
	}

	public Md5FileUtil() {
		super();
		getMessagedigest();
	}

	public String getMD5String(String s) {
		return getMD5String(s.getBytes());
	}

	public String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

	private String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

}
