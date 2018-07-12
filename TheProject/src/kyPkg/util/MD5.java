package kyPkg.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	// ３２ケタのハッシュコードが帰る
	public static String getMD5(String key) {
		byte[] hash = null;
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.reset();
			md5.update(key.getBytes());
			hash = md5.digest();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("error@getMD5");
		}
		return byteArray2Hex(hash);
	}

	// 16進数にする
	private static String byteArray2Hex(byte[] bytes) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			if ((0xff & bytes[i]) < 0x10) {
				buf.append("0" + Integer.toHexString((0xFF & bytes[i])));
			} else {
				buf.append(Integer.toHexString(0xFF & bytes[i]));
			}
		}
		return buf.toString();
	}

	public static void main(String[] argv) {
		System.out.println("test1:" + MD5.getMD5("select * from table1;"));
		System.out.println("test2:" + MD5.getMD5("select * from table1;"));
		System.out.println("test3:" + MD5.getMD5("select * from table2;"));

	}
}
