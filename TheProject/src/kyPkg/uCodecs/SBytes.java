package kyPkg.uCodecs;

import java.util.ArrayList;
import java.util.List;

public class SBytes {
	
	// /** �S�p�L���J�n�ʒu */
	// private static final int DOUBLE_BYTE_SYMBOL_START = 0xFF01;
	// private static final int DOUBLE_BYTE_SYMBOL_END = 0xFF5E;
	// /** �S�p���ŊJ�n�ʒu */
	// private static final int DOUBLE_BYTE_KATAKANA_START = 0x30A0;
	// private static final int DOUBLE_BYTE_KATAKANA_END = 0x30FF;
	// /** �S�p�X�y�[�X */
	// private static final int DOUBLE_BYTE_SPACE_END = 0x3000;
	// /** ���p�X�y�[�X */
	// private static final int SINGLE_BYTE_SPACE_END = 0x0020;

	/** ���p�L���J�n�ʒu */
	private static final int SYMBOL_START = 0x0020;
	private static final int SYMBOL_END = 0x007E;

	/** ���p���Ŗ��J�n�ʒu */
	private static final int KATAKANA_START = 0xFF61;
	private static final int KATAKANA_END = 0xFF9F;

	/**
	 * ���p��������
	 * 
	 * @return ���茋�� true:���p�L��
	 */
	public static boolean isNarrow(String str) {
		boolean flag = true;
		while (flag) {
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				flag = isNarrow(c);
			}
		}
		return flag;
	}

	public static boolean isNarrow(final char c) {
		return (SYMBOL_START <= c) && (c <= SYMBOL_END) || isKana(c);
	}

	/**
	 * ���p"�L��"����
	 * 
	 * @return ���茋�� true:���p�L��
	 */
	public static boolean isSymbol(final char c) {
		return (SYMBOL_START <= c) && (c <= SYMBOL_END) && !isAlphabet(c)
				&& !isDigit(c);
	}

	/**
	 * ���p�L������
	 * 
	 * @return ���茋�� true:���p�L��
	 */
	public static boolean isKana(final char c) {
		return (KATAKANA_START <= c) && (c <= KATAKANA_END);
	}

	/**
	 * ���p��������(���K�\���ł������̂ł͂Ȃ����H)
	 * 
	 * @return ���茋�� true:���p����
	 */
	public static boolean isDigit(final char c) {
		return ('0' <= c) && (c <= '9');
	}

	/**
	 * ���p�p������(���K�\���ł������̂ł͂Ȃ����H)
	 * 
	 * @return ���茋�� true:���p�p��
	 */
	public static boolean isAlphabet(final char c) {
		return (('a' <= c) && (c <= 'z')) || (('A' <= c) && (c <= 'Z'));
	}

	// -------------------------------------------------------------------------
	public static List getKanaList() {
		List<String> list = new ArrayList();
		for (char i = KATAKANA_START; i <= KATAKANA_END; i++) {
			list.add(String.valueOf(i));
		}
		return list;
	}

	public static List getSymbolList() {
		List<String> list = new ArrayList();
		for (char i = SYMBOL_START; i <= SYMBOL_END; i++) {
			list.add(String.valueOf(i));
		}
		return list;
	}

	public static List getDigitList() {
		List<String> list = new ArrayList();
		for (char i = '0'; i <= '9'; i++) {
			list.add(String.valueOf(i));
		}
		return list;
	}

	public static List getAlphabetList() {
		List<String> list = new ArrayList();
		for (char i = 'A'; i <= 'Z'; i++) {
			list.add(String.valueOf(i));
		}
		for (char i = 'a'; i <= 'z'; i++) {
			list.add(String.valueOf(i));
		}
		return list;
	}

	// -------------------------------------------------------------------------
	public static void enumKATAKANA() {
		System.out.println("KATAKANA_START>" + KATAKANA_START);
		System.out.println("KATAKANA_END  >" + KATAKANA_END);
		System.out.println("enumKATAKANA()-----------------------------------");
		System.out.println("'�'>" + (int) '�');
		System.out.println("'�'>" + (int) '�');
		System.out.println("'�'>" + (int) '�');
		System.out.println("'�'>" + (int) '�');
		System.out.println("'�'>" + (int) '�');
		// for (char i = '�'; i <= '�'; i++) {
		for (char i = KATAKANA_START; i <= KATAKANA_END; i++) {
			System.out.println(String.valueOf((int) i) + ">" + i);
		}
	}

	public static void enumAN() {
		System.out.println("------------------------------------------------");
		System.out.println("'�'>" + (int) '�');
		System.out.println("'�'>" + (int) '�');
		for (char i = '�'; i <= '�'; i++) {
			System.out.println(String.valueOf((int) i) + ">" + i);
		}
	}

	public static void enumDigit() {
		System.out.println("------------------------------------------------");
		System.out.println("'0'>" + (int) '0');
		System.out.println("'9'>" + (int) '9');
		for (char i = '0'; i <= '9'; i++) {
			System.out.println(String.valueOf((int) i) + ">" + i);
		}
	}

	public static void enumAlpha() {
		System.out.println("------------------------------------------------");
		System.out.println("'A'>" + (int) 'A');
		System.out.println("'Z'>" + (int) 'Z');
		for (char i = 'A'; i <= 'Z'; i++) {
			System.out.println(String.valueOf((int) i) + ">" + i);
		}
		System.out.println("'a'>" + (int) 'a');
		System.out.println("'z'>" + (int) 'z');
		for (char i = 'a'; i <= 'z'; i++) {
			System.out.println(String.valueOf((int) i) + ">" + i);
		}
	}

	public static void enumSymbol() {
		System.out.println("------------------------------------------------");
		System.out.println("SYMBOL_START>" + SYMBOL_START);
		System.out.println("SYMBOL_END  >" + SYMBOL_END);
		for (char i = SYMBOL_START; i <= SYMBOL_END; i++) {
			System.out.println(String.valueOf((int) i) + ">" + i);
		}
	}
}
