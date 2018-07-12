package kyPkg.uFile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import globals.ResControl;
import kyPkg.converter.Inf_ArrayCnv;

public class TextAnalyzer {
	// ---+---------+---------+---------+---------+---------+---------+---------+
	public static final String CHAR = "CHAR";
	public static final String VARCHAR = "VARCHAR";
	public static final String INTEGER = "INTEGER";
	public static final String DOUBLE = "DOUBLE";
	// ---+---------+---------+---------+---------+---------+---------+---------+
	public static final String TYPE = "type";
	public static final String MIN_WIDTH = "min_width";
	public static final String MAX_WIDTH = "max_width";
	public static final String SAMPLE = "sample";
	public static final String ENCODE = "encode";
	public static final String DELIM = "delimiter";
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// テキストファイルのデータを分析する
	// ---+---------+---------+---------+---------+---------+---------+---------+
	public static HashMap<String, List<Object>> analyzeIt(String path,
			int checkCount) {
		// System.out.println("(2)debug1017 path:" + path);
		File49ers insF49 = new File49ers(path);
		String encoding = insF49.getEncoding();
		String delimiter = insF49.getDelimiter();
		return analyzeIt(path, encoding, delimiter, checkCount, null);
	}

	// ------------------------------------------------------------------------
	// 20130222ココを読んだあとでファイルをロックしている？
	// ------------------------------------------------------------------------
	public static HashMap<String, List<Object>> analyzeIt(String path,
			int checkCount, Inf_ArrayCnv cnv) {
		// System.out.println("(2)debug1017 path:" + path);
		// FileUtil_.isLocked(path, "<211>");
		File49ers insF49 = new File49ers(path);
		String encoding = insF49.getEncoding();
		String delimiter = insF49.getDelimiter();
		// FileUtil_.isLocked(path, "<212>");
		return analyzeIt(path, encoding, delimiter, checkCount, cnv);
	}

	public static HashMap<String, List<Object>> analyzeIt(String path,
			String encoding, String delimiter, int checkCount, Inf_ArrayCnv cnv) {
		// System.out.println("<<<analyzeIt start>>>(3)debug1017 path:" + path);
		// FileUtil_.isLocked(path, "<301>");

		int cnt = 0;
		if (encoding.equals(""))
			encoding = System.getProperty("file.encoding");
		String[] array;
		HashMap<String, List<Object>> map = new HashMap();
		map.put(TYPE, new ArrayList());
		map.put(MIN_WIDTH, new ArrayList());
		map.put(MAX_WIDTH, new ArrayList());
		map.put(SAMPLE, new ArrayList());
		map.put(ENCODE, new ArrayList());
		map.get(ENCODE).add(encoding);
		map.put(DELIM, new ArrayList());
		map.get(DELIM).add(delimiter);

		List<Object> types = map.get(TYPE);
		List<Object> maxWidths = map.get(MAX_WIDTH);// maxWidth
		List<Object> minWidths = map.get(MIN_WIDTH);// minWidth
		List<Object> samples = map.get(SAMPLE);

		String wRec = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(path), encoding));
			while ((wRec = br.readLine()) != null) {
				cnt++;
				if (cnt > checkCount)
					break;
				array = wRec.split(delimiter);

				// カラム辞書が存在するなら変換する(2012-01-16)
				if (cnv != null)
					array = cnv.convert(array, 0);
				// ファイルの中身を調べる
				String type = "";
				Integer maxWidth = 0;
				Integer minWidth = 0;
				String cel = "";
				// XXX カラムが固定長なら数値ではなく文字列とした方が良い！
				// XXX 固定長かどうか判定する

				for (int col = 0; col < array.length; col++) {
					cel = array[col];
					if (types.size() <= col) {
						types.add(INTEGER);
						maxWidths.add(Integer.MIN_VALUE);
						minWidths.add(Integer.MAX_VALUE);
						// System.out.println("col:" + col + " #cel#" + cel);
						samples.add(cel);
					}
					type = (String) types.get(col);
					int width = cel.length();
					maxWidth = (Integer) maxWidths.get(col);
					minWidth = (Integer) minWidths.get(col);
					if (maxWidth < width) {
						maxWidth = width;
						maxWidths.set(col, maxWidth);
					}
					if (minWidth > width) {
						minWidth = width;
						minWidths.set(col, minWidth);
					}
					if (type.equals(INTEGER) | type.equals(DOUBLE)) {
						if (isNumeric(cel)) {
							if (type.equals(INTEGER) && isInt(cel)) {
								types.set(col, INTEGER);
							} else {
								types.set(col, DOUBLE);
							}
						} else {
							if (!type.equals(VARCHAR)) {
								if (isAlphaNum(cel)) {
									types.set(col, CHAR);
								} else {
									types.set(col, VARCHAR);
								}
							}
						}
					}
					// if (col == 14) {
					// System.out.println("#col#" + col + "#type#" + type
					// + " #maxWidth:" + maxWidth + " #minWidth:"
					// + minWidth);
					// }
				}
			}
			br.close();
			//
			for (int col = 0; col < types.size(); col++) {
				String type = (String) types.get(col);
				int maxWidth = (Integer) maxWidths.get(col);
				int minWidth = (Integer) minWidths.get(col);
				// System.out.println("#col#" + col + "#type#" + type
				// + " #maxWidth:" + maxWidth + " #minWidth:" + minWidth);
				if (type.equals(INTEGER) | type.equals(DOUBLE)) {
					if (maxWidth == minWidth) {
						// 数値項目でなおかつすべての長さが同じなら文字列とみなす・・・
						types.set(col, CHAR);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		// System.out.println("<<<analyzeIt end>>>");
		// FileUtil_.isLocked(path, "<304>");

		return map;
	}

	/**
	 * 半角数字文字列かどうか
	 * 
	 * @param pStr
	 *            検査文字列
	 * @return 半角数字のみの文字列ならtrue
	 */
	private static boolean isNumeric(String pStr) {
		// System.out.println("チェック結果：半角数字のみの文字列です。");
		// if (pStr.length() >= 8)
		// return false;
		if (pStr.matches("[+-]*\\d+[.]*\\d*")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isInt(String pStr) {
		// 便宜上・・・8桁以下にした（本当はInteger.MAX_VALUE=>2147483647）
//		if (pStr.length() >= 8)
//		if (pStr.length() >= 10)
//			return false;
		if (pStr.matches("\\d+")) {
			return true;
		} else {
			return false;
		}
	}

	// 半角英数かどうか
	private static boolean isAlphaNum(String pStr) {
		if (pStr.matches("[a-zA-Z0-9]*")) {
			return true;
		} else {
			return false;
		}
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] argv) {
		// test01();
		// test02();
		tester20130222();
	}

	// ------------------------------------------------------------------------
	// 20130222単体テスト・・・問題なさそう・・・ふむ
	// ------------------------------------------------------------------------
	public static void tester20130222() {
		String path = ResControl.getQPRHome() + "janResult.txt";
		int checkCount = 100;
		Inf_ArrayCnv cnv = null;
		analyzeIt(path, checkCount, cnv);
	}

	public static void test01() {
		String path = "C:/@QPR/zappa.txt";
		int checkCount = 20;
		String encoding = "";
		String delimiter = "\t";
		HashMap<String, List<Object>> map = kyPkg.uFile.TextAnalyzer.analyzeIt(
				path, encoding, delimiter, checkCount, null);
		// ---------------------------------------------------------------------
		List<Object> typeList = map.get(TYPE);
		List<Object> widthList = map.get(MAX_WIDTH);
		List<Object> sampleList = map.get(SAMPLE);
		// ---------------------------------------------------------------------
		for (int index = 0; index < typeList.size(); index++) {
			System.out.println(" type:" + typeList.get(index) + " width:"
					+ (Integer) widthList.get(index) + " sample:"
					+ (String) sampleList.get(index));
		}
	}

	public static void test02() {
		String path = "C:/@QPR/zappa.txt";
		int checkCount = 20;
		HashMap<String, List<Object>> map = kyPkg.uFile.TextAnalyzer.analyzeIt(
				path, checkCount);
		// ---------------------------------------------------------------------
		List<Object> typeList = map.get(TextAnalyzer.TYPE);
		List<Object> widthList = map.get(TextAnalyzer.MAX_WIDTH);
		List<Object> sampleList = map.get(TextAnalyzer.SAMPLE);
		String encoding = (String) map.get(TextAnalyzer.ENCODE).get(0);
		String delimiter = (String) map.get(TextAnalyzer.DELIM).get(0);
		// ---------------------------------------------------------------------
		System.out.println(" encode:" + encoding);
		System.out.println(" delim:" + delimiter);
		for (int index = 0; index < typeList.size(); index++) {
			System.out.println(" type:" + typeList.get(index) + " width:"
					+ (Integer) widthList.get(index) + " sample:"
					+ (String) sampleList.get(index));
		}
	}
}
