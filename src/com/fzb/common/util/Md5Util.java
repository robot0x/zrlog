package com.fzb.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;

public class Md5Util {
	public static final String MD5(String pwd) {
		if (pwd != null) {
			char[] md5String = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
					'9', 'A', 'B', 'C', 'D', 'E', 'F' };
			try {
				byte[] btInput = pwd.getBytes();

				MessageDigest mdInst = MessageDigest.getInstance("MD5");

				mdInst.update(btInput);

				byte[] md = mdInst.digest();

				int j = md.length;

				char[] str = new char[j * 2];
				int k = 0;
				for (int i = 0; i < j; i++) {
					byte byte0 = md[i];
					str[(k++)] = md5String[(byte0 >>> 4 & 0xF)];
					str[(k++)] = md5String[(byte0 & 0xF)];
				}

				return new String(str).toLowerCase();
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	public static final String MD5(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		byte b[] = new byte[(int) file.length()];
		in.read(b);
		in.close();
		if (b.length > 0) {

			{
				char[] md5String = { '0', '1', '2', '3', '4', '5', '6', '7',
						'8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
				try {
					byte[] btInput = b;

					MessageDigest mdInst = MessageDigest.getInstance("SHA-1");

					mdInst.update(btInput);

					byte[] md = mdInst.digest();
					System.out.println(md.length);
					int j = md.length;

					char[] str = new char[j * 2];
					int k = 0;
					for (int i = 0; i < j; i++) {
						byte byte0 = md[i];
						str[(k++)] = md5String[(byte0 >>> 4 & 0xF)];
						str[(k++)] = md5String[(byte0 & 0xF)];
					}

					return new String(str).toLowerCase();
				} catch (Exception e) {
					return null; 
				}
			}
		}
		return null;
	}

	public static void main(String[] args) throws IOException {
		System.out.println(MD5("123456"));
		/*System.out.println(System.currentTimeMillis());
		System.out.println(MD5(UUID.randomUUID().toString()));
		System.out.println(System.currentTimeMillis());*/
	}
}
