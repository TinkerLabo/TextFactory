package kyPkg.uFile;

import java.io.*;
import java.nio.*;
import java.nio.charset.*;

public class EncodeTest {
	/*
	 * ���C�������i�Ăяo�����j
	 */

	public static void test01() {
		try {
			// ==============================//
			// �ǂݍ��� //
			// ==============================//
			File f = new File("test.txt");
			FileInputStream fi = new FileInputStream(f);

			// �ǂݍ���
			int len = (int) f.length();
			byte[] data = new byte[len];
			fi.read(data);
			fi.close();

			// ==============================//
			// �ϊ� //
			// ==============================//

			// �r�i�h�r�����t�s�e�W
			Charset charset1 = Charset.forName("SJIS");
			CharsetDecoder decoder = charset1.newDecoder();
			ByteBuffer bb1 = ByteBuffer.wrap(data);
			CharBuffer cb = decoder.decode(bb1);

			// UTF8=���d�t�b
			Charset charset2 = Charset.forName("EUC_JP");
			CharsetEncoder encoder = charset2.newEncoder();
			ByteBuffer bb2 = encoder.encode(cb);
			byte[] outdata = bb2.array();

			// ==============================//
			// �����o�� //
			// ==============================//
			File f2 = new File("testEUC.txt");
			FileOutputStream fo = new FileOutputStream(f2);

			// �����o��
			fo.write(outdata);
			fo.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		test01();
	}

}
