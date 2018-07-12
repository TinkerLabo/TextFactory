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
	 * 音引き、文字化けの原因になる文字を、文字化けない文字に置換します。
	 * @param str
	 * @param encoding 外部出力予定の文字コード(この値により置換テーブルが代わります)
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

	//共通置換テーブル
	private static final String[] COMMON_FROM = new String[] { //
			"\u00AD", //SOFT HYPHEN
			"\u2011", //NON-BREAKING HYPHEN
			"\u2012", //FIGURE DASH
			"\u2013", //EN DASH
			"\u2043", //HYPHEN BULLET
			"\uFE63", //SMALL HYPHEN-MINUS
			"\u223C", //TILDE OPERATOR　～
			"\u223E", //INVERTED LAZY S
			"\u22EF", //MIDLINE HORIZONTAL ELLIPSIS　3点
			"\u00B7", //MIDDLE DOT
			"\u2022", //BULLET
			"\u2219", //BULLET OPERATOR	
			"\u22C5" //DOT OPERATOR半角中点
	};
	private static final String[] COMMON_TO = new String[] { //
			"\u002D", //
			"\u002D", //
			"\u002D", //
			"\u002D", //
			"\u002D", //
			"\u002D", //半角ハイフン
			"\u007E", //
			"\u007E", //半角波線→半角チルダ
			"\u2026", //3点
			"\uFF65", //
			"\uFF65", //
			"\uFF65", //
			"\uFF65" //半角中点
	};
	//エンコーディング別置換テーブル
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
						"\uFF0D", // 全角マイナス 
						"\u00AF", // 長音符号 
						"\u2015", // 強調引用 
						"\u3030", // 
						"\uFF5E" // 波線 
		});
		SJIS_TO = (String[]) ArrayUtils.addAll(COMMON_TO,
				new String[] { //
						"\u2212", // 全角マイナス
						"\uFFE3", // 長音符号
						"\u2014", // 強調引用
						"\u301C", // ?
						"\u301C" // ? 波線
		});

		MS932_FROM = (String[]) ArrayUtils.addAll(COMMON_FROM,
				new String[] { //
						"\u2212", // 全角マイナス
						"\u2014", // 強調引用 
						"\u3030", // 
						"\u301C" // 波線 
		});
		MS932_TO = (String[]) ArrayUtils.addAll(COMMON_TO,
				new String[] { //
						"\uFF0D", // 全角マイナス
						"\u2015", // 強調引用
						"\uFF5E", // 
						"\uFF5E" // 波線
		});
		ISO2022JP_FROM = (String[]) ArrayUtils.addAll(COMMON_FROM,
				new String[] { //
						"\uFF0D", // 全角マイナス
						"\u00AF", // 長音符号
						"\u2015", // 強調引用
						"\u3030", // 
						"\uFF5E" // 波線 
		});
		ISO2022JP_TO = (String[]) ArrayUtils.addAll(COMMON_TO,
				new String[] { //
						"\u2212", // 全角マイナス
						"\uFFE3", // 長音符号
						"\u2014", // 強調引用
						"\u301C", // 
						"\u301C" // 波線
		});

		EUCJP_FROM = (String[]) ArrayUtils.addAll(COMMON_FROM,
				new String[] { //
						"\uFF0D", // 全角マイナス
						"\u2015", // 強調引用
						"\u3030" // 波線
		});
		EUCJP_TO = (String[]) ArrayUtils.addAll(COMMON_TO,
				new String[] { // 
						"\u2212", // 全角マイナス
						"\u2014", // 強調引用
						"\uFF5E" // 波線
		});
	}

	/**
	 * Unicode文字列に変換する("あ" -> "\u3042")
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
	 * Unicode文字列から元の文字列に変換する ("\u3042" -> "あ")
	 * @param unicode
	 * @return
	 */
	public static String convertToOiginal(String unicode) {
		String[] codeStrs = unicode.split("\\\\u");
		int[] codePoints = new int[codeStrs.length - 1]; // 最初が空文字なのでそれを抜かす
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
	//	～ \uFF5E
//	－ \uFF0D
//	￠ \uFFE0
//	￡ \uFFE1
//	￢ \uFFE2
//	‐ \u2010
//	〔 \u3014

}
//参考	http://d.hatena.ne.jp/y-kawaz/20101112/1289554290