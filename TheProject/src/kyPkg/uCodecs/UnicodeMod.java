package kyPkg.uCodecs;

import java.io.UnsupportedEncodingException;

public class UnicodeMod {
	// �v���b�g�t�H�[���̃L�����N�^�[�Z�b�g�ɕϊ�����E�E�E�܂�(unicode => windows-31j)
	// �g�p�၄
	// �@itpName = UnicodeMod.charsetConv(itpName);
	public static String charsetConv(String src) {
		String charsetName = java.nio.charset.Charset.defaultCharset().name();
		// System.out.println("@UnicodeMod charsetName:"+charsetName); //
		// windows-31j
		// String dest = src + "<UnsupportedEncoding>";
		String dest = src;
		try {
			dest = new String(src.getBytes(), charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return dest;
	}
}
