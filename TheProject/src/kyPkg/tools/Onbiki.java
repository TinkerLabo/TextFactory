package kyPkg.tools;

import static kyPkg.uFile.FileUtil.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import kyPkg.uFile.ListArrayUtil;

public class Onbiki {
	public Onbiki() {
	}
	/**
	 * �������A���������̌����ɂȂ镶�����A���������Ȃ������ɒu�����܂��B
	 * @param str
	 * @param encoding �O���o�͗\��̕����R�[�h(���̒l�ɂ��u���e�[�u��������܂�)
	 * @return
	 */
	//	public static java.lang.String replaceEach(java.lang.String text, java.lang.String[] searchList, java.lang.String[] replacementList)
	public static String cnv2Similar(String str, String encoding) {
		if (str == null || encoding == null) {
			return str;
		}
		//		encoding = encoding.toLowerCase();
		if (windowsDecoding.equalsIgnoreCase(encoding)) {
			return StringUtils.replaceEach(str, MS932_FROM, MS932_TO);
		} else if (MS932.equalsIgnoreCase(encoding)) {
			return StringUtils.replaceEach(str, MS932_FROM, MS932_TO);
		} else if (SHIFT_JIS.equalsIgnoreCase(encoding)) {
			return StringUtils.replaceEach(str, SJIS_FROM, SJIS_TO);
		} else if (EUC_JP.equalsIgnoreCase(encoding)) {
			return StringUtils.replaceEach(str, EUCJP_FROM, EUCJP_TO);
		} else if (ISO_2022_JP.equalsIgnoreCase(encoding)) {
			return StringUtils.replaceEach(str, ISO2022JP_FROM, ISO2022JP_TO);
		}
		return str;
	}

	//���ʒu���e�[�u��
	private static final String[] COMMON_FROM = new String[] { //
			"\u00AD", //SOFT HYPHEN
			"\u2011", //NON-BREAKING HYPHEN
			"\u2012", //FIGURE DASH
			"\u2013", //EN DASH
			"\u2043", //HYPHEN BULLET
			"\uFE63", //SMALL HYPHEN-MINUS
			"\u223C", //TILDE OPERATOR�@�`
			"\u223E", //INVERTED LAZY S
			"\u22EF", //MIDLINE HORIZONTAL ELLIPSIS�@3�_
			"\u00B7", //MIDDLE DOT
			"\u2022", //BULLET
			"\u2219", //BULLET OPERATOR	
			"\u22C5" //DOT OPERATOR���p���_
	};
	private static final String[] COMMON_TO = new String[] { //
			"\u002D", //
			"\u002D", //
			"\u002D", //
			"\u002D", //
			"\u002D", //
			"\u002D", //���p�n�C�t��
			"\u007E", //
			"\u007E", //���p�g�������p�`���_
			"\u2026", //3�_
			"\uFF65", //
			"\uFF65", //
			"\uFF65", //
			"\uFF65" //���p���_
	};
	//�G���R�[�f�B���O�ʒu���e�[�u��
	private static final String[] SJIS_FROM;
	private static final String[] SJIS_TO;
	private static final String[] MS932_FROM;
	private static final String[] MS932_TO;
	private static final String[] EUCJP_FROM;
	private static final String[] EUCJP_TO;
	private static final String[] ISO2022JP_FROM;
	private static final String[] ISO2022JP_TO;

	static {
		SJIS_FROM = (String[]) ArrayUtils.addAll(COMMON_FROM,
				new String[] { //
						"\uFF0D", // �S�p�}�C�i�X 
						"\u00AF", // �������� 
						"\u2015", // �������p 
						"\u3030", // 
						"\uFF5E" // �g�� 
		});
		SJIS_TO = (String[]) ArrayUtils.addAll(COMMON_TO,
				new String[] { //
						"\u2212", // �S�p�}�C�i�X
						"\uFFE3", // ��������
						"\u2014", // �������p
						"\u301C", // ?
						"\u301C" // ? �g��
		});

		MS932_FROM = (String[]) ArrayUtils.addAll(COMMON_FROM,
				new String[] { //
						"\u2212", // �S�p�}�C�i�X
						"\u2014", // �������p 
						"\u3030", // 
						"\u301C" // �g�� 
		});
		MS932_TO = (String[]) ArrayUtils.addAll(COMMON_TO,
				new String[] { //
						"\uFF0D", // �S�p�}�C�i�X
						"\u2015", // �������p
						"\uFF5E", // 
						"\uFF5E" // �g��
		});
		ISO2022JP_FROM = (String[]) ArrayUtils.addAll(COMMON_FROM,
				new String[] { //
						"\uFF0D", // �S�p�}�C�i�X
						"\u00AF", // ��������
						"\u2015", // �������p
						"\u3030", // 
						"\uFF5E" // �g�� 
		});
		ISO2022JP_TO = (String[]) ArrayUtils.addAll(COMMON_TO,
				new String[] { //
						"\u2212", // �S�p�}�C�i�X
						"\uFFE3", // ��������
						"\u2014", // �������p
						"\u301C", // 
						"\u301C" // �g��
		});

		EUCJP_FROM = (String[]) ArrayUtils.addAll(COMMON_FROM,
				new String[] { //
						"\uFF0D", // �S�p�}�C�i�X
						"\u2015", // �������p
						"\u3030" // �g��
		});
		EUCJP_TO = (String[]) ArrayUtils.addAll(COMMON_TO,
				new String[] { // 
						"\u2212", // �S�p�}�C�i�X
						"\u2014", // �������p
						"\uFF5E" // �g��
		});
	}

	/**
	 * Unicode������ɕϊ�����("��" -> "\u3042")
	 * @param original
	 * @return
	 */
	public static String convertToUnicode(String original) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < original.length(); i++) {
			sb.append(String.format("\\u%04X",
					Character.codePointAt(original, i)));
		}
		String unicode = sb.toString();
		return unicode;
	}

	/**
	 * Unicode�����񂩂猳�̕�����ɕϊ����� ("\u3042" -> "��")
	 * @param unicode
	 * @return
	 */
	public static String convertToOiginal(String unicode) {
		String[] codeStrs = unicode.split("\\\\u");
		int[] codePoints = new int[codeStrs.length - 1]; // �ŏ����󕶎��Ȃ̂ł���𔲂���
		for (int i = 0; i < codePoints.length; i++) {
			codePoints[i] = Integer.parseInt(codeStrs[i + 1], 16);
		}
		String encodedText = new String(codePoints, 0, codePoints.length);
		return encodedText;
	}

	public static void main(String[] args) {
		test01();
	}

	public static void test01() {
		String iPath = "c:/onbikiIn.txt";
		String oPath = "c:/onbikiOut.txt";
		List<String> srcList = ListArrayUtil.file2List(iPath);
		List<String> dstList = new ArrayList();
		boolean flag = false;
		for (String rec : srcList) {
			System.out.println("in  rec:" + rec);
			String debug = convertToUnicode(rec);
			System.out.println("debug:" + debug);
			//			rec = Onbiki.cnv2Similar(rec, WINDOWS_31J);
			//			rec = Onbiki.cnv2Similar(rec, SHIFT_JIS);
			dstList.add(rec);
		}
		ListArrayUtil.list2File(oPath, dstList, defaultEncoding2);
	}
	//
	//	�` \uFF5E
//	�| \uFF0D
//	�� \uFFE0
//	�� \uFFE1
//	�� \uFFE2
//	�] \u2010
//	�k \u3014

}
//�Q�l	http://d.hatena.ne.jp/y-kawaz/20101112/1289554290