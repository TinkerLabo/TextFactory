package kyPkg.tools;

import static kyPkg.uFile.FileUtil.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//���̃��W�b�N�̑���Ɉȉ��̃��W�b�N�ɕύX����
//wRec = Onbiki.cnv2Similar(wRec, FileUtil.defaultEncoding);
public class StringEncoder {

	//�u�`�a�\�|�����ʁv��7�����������������N�����Ă��܂��̂ł��̑Ή�
	/**
	 * �����̕�����(Shift_JIS)���AUTF-8�ɃG���R�[�h����B
	 * 
	 * @param value �ϊ��Ώۂ̕�����
	 * @return �G���R�[�h���ꂽ������
	 */
	public static String sjisToUtf8(String value)
			throws UnsupportedEncodingException {
		byte[] srcStream = value.getBytes(SJIS);
		byte[] destStream = (new String(srcStream, SJIS)).getBytes(UTF_8);
		value = new String(destStream, UTF_8);
		value = StringEncoder.convert(value, SJIS, UTF_8);
		return value;
	}

	/**
	 * �����̕�����(UTF-8)���AShift_JIS�ɃG���R�[�h����B
	 * 
	 * @param value �ϊ��Ώۂ̕�����
	 * @return �G���R�[�h���ꂽ������
	 */
	public static String utf8ToSjis(String value)
			throws UnsupportedEncodingException {
		byte[] srcStream = value.getBytes(UTF_8);
		value = convert(new String(srcStream, UTF_8), UTF_8, SJIS);
		byte[] destStream = value.getBytes(SJIS);
		value = new String(destStream, SJIS);
		return value;
	}

	/**
	 * �����̕�������A�G���R�[�h����B
	 * 
	 * @param value �ϊ��Ώۂ̕�����
	 * @param src �ϊ��O�̕����R�[�h
	 * @param dest �ϊ���̕����R�[�h
	 * @return �G���R�[�h���ꂽ������
	 */
	private static String convert(String value, String src, String dest)
			throws UnsupportedEncodingException {
		Map<String, String> conversion = createConversionMap(src, dest);
		char oldChar;
		char newChar;
		String key;
		for (Iterator<String> itr = conversion.keySet().iterator(); itr
				.hasNext();) {
			key = itr.next();
			oldChar = toChar(key);
			newChar = toChar(conversion.get(key));
			value = value.replace(oldChar, newChar);
		}
		return value;
	}

	/**
	 * �G���R�[�h�����쐬����
	 * 
	 * @param src �ϊ��O�̕����R�[�h
	 * @param dest �ϊ���̕����R�[�h
	 * @return �G���R�[�h���ꂽ������
	 */
	private static Map<String, String> createConversionMap(String src,
			String dest) throws UnsupportedEncodingException {
		Map<String, String> conversion = new HashMap<String, String>();
		if ((src.equals(UTF_8)) && (dest.equals(SJIS))) {
			// �|�i�S�p�}�C�i�X�j
			conversion.put("U+FF0D", "U+2212");
			// �`�i�S�p�`���_�j
			conversion.put("U+FF5E", "U+301C");
			// ���i�Z���g�j
			conversion.put("U+FFE0", "U+00A2");
			// ���i�|���h�j
			conversion.put("U+FFE1", "U+00A3");
			// �ʁi�m�b�g�j
			conversion.put("U+FFE2", "U+00AC");
			// �\�i�S�p�}�C�i�X��菭�����̂��镶���j
			conversion.put("U+2015", "U+2014");
			// �a�i���p�p�C�v��2���񂾂悤�ȕ����j
			conversion.put("U+2225", "U+2016");

		} else if ((src.equals(SJIS)) && (dest.equals(UTF_8))) {
			// �|�i�S�p�}�C�i�X�j
			conversion.put("U+2212", "U+FF0D");
			// �`�i�S�p�`���_�j
			conversion.put("U+301C", "U+FF5E");
			// ���i�Z���g�j
			conversion.put("U+00A2", "U+FFE0");
			// ���i�|���h�j
			conversion.put("U+00A3", "U+FFE1");
			// �ʁi�m�b�g�j
			conversion.put("U+00AC", "U+FFE2");
			// �\�i�S�p�}�C�i�X��菭�����̂��镶���j
			conversion.put("U+2014", "U+2015");
			// �a�i���p�p�C�v��2���񂾂悤�ȕ����j
			conversion.put("U+2016", "U+2225");

		} else {
			throw new UnsupportedEncodingException(
					"���̕����R�[�h�̓T�|�[�g���Ă��܂���B\n�Esrc=" + src + ",dest=" + dest);
		}
		return conversion;
	}

	/**
	 * 16�i�\�L�̕������擾����B
	 * 
	 * @param value �ϊ��Ώۂ̕�����
	 * @return 16�i�\�L�̕���
	 */
	private static char toChar(String value) {
		return (char) Integer.parseInt(value.trim().substring("U+".length()),
				16);
	}
}