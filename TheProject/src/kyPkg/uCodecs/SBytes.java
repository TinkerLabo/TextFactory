package kyPkg.uCodecs;

import java.util.ArrayList;
import java.util.List;

public class SBytes {
	
	// /** 全角記号開始位置 */
	// private static final int DOUBLE_BYTE_SYMBOL_START = 0xFF01;
	// private static final int DOUBLE_BYTE_SYMBOL_END = 0xFF5E;
	// /** 全角ｶﾀｶﾅ開始位置 */
	// private static final int DOUBLE_BYTE_KATAKANA_START = 0x30A0;
	// private static final int DOUBLE_BYTE_KATAKANA_END = 0x30FF;
	// /** 全角スペース */
	// private static final int DOUBLE_BYTE_SPACE_END = 0x3000;
	// /** 半角スペース */
	// private static final int SINGLE_BYTE_SPACE_END = 0x0020;

	/** 半角記号開始位置 */
	private static final int SYMBOL_START = 0x0020;
	private static final int SYMBOL_END = 0x007E;

	/** 半角ｶﾀｶﾅ名開始位置 */
	private static final int KATAKANA_START = 0xFF61;
	private static final int KATAKANA_END = 0xFF9F;

	/**
	 * 半角文字判定
	 * 
	 * @return 判定結果 true:半角記号
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
	 * 半角"記号"判定
	 * 
	 * @return 判定結果 true:半角記号
	 */
	public static boolean isSymbol(final char c) {
		return (SYMBOL_START <= c) && (c <= SYMBOL_END) && !isAlphabet(c)
				&& !isDigit(c);
	}

	/**
	 * 半角記号判定
	 * 
	 * @return 判定結果 true:半角記号
	 */
	public static boolean isKana(final char c) {
		return (KATAKANA_START <= c) && (c <= KATAKANA_END);
	}

	/**
	 * 半角数字判定(正規表現でもいいのではないか？)
	 * 
	 * @return 判定結果 true:半角数字
	 */
	public static boolean isDigit(final char c) {
		return ('0' <= c) && (c <= '9');
	}

	/**
	 * 半角英字判定(正規表現でもいいのではないか？)
	 * 
	 * @return 判定結果 true:半角英字
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
		System.out.println("'ﾞ'>" + (int) 'ﾞ');
		System.out.println("'ﾟ'>" + (int) 'ﾟ');
		System.out.println("'ｦ'>" + (int) 'ｦ');
		System.out.println("'ｱ'>" + (int) 'ｱ');
		System.out.println("'ﾝ'>" + (int) 'ﾝ');
		// for (char i = '｡'; i <= 'ﾟ'; i++) {
		for (char i = KATAKANA_START; i <= KATAKANA_END; i++) {
			System.out.println(String.valueOf((int) i) + ">" + i);
		}
	}

	public static void enumAN() {
		System.out.println("------------------------------------------------");
		System.out.println("'ｱ'>" + (int) 'ｱ');
		System.out.println("'ﾝ'>" + (int) 'ﾝ');
		for (char i = 'ｱ'; i <= 'ﾝ'; i++) {
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
