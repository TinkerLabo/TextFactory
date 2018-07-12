package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

// -------------------------------------------------------------------------
// インナークラス ParmsObj substr用のパラメータ
// -------------------------------------------------------------------------
public class RegChecker {
	private List<FilterParam> filterList = null;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public RegChecker() {
		filterList = new ArrayList();
	}

	public RegChecker(int col, int pos, int len, String regex, int stat) {
		this();
		addFilter(col, pos, len, regex, stat);
	}

	public RegChecker(int col, int pos, int len, String regex, int stat,
			boolean flag) {
		this();
		addFilter(col, pos, len, regex, stat, flag);
	}

	// ------------------------------------------------------------------------
	// . 任意の文字を表わす（行末記号とマッチする場合もあるんだそう・・）
	// .* なにか文字がいくつか続く（・・スペースもｏｋだっけ？）
	// \\( 文字(を表わす
	// \\) 文字)を表わす
	// []や() それから*などは特殊な文字なので気を付ける！！
	//
	// ● 文字クラス 構文 マッチ対象
	// [ABC] A,B,Cのいずれか1文字
	// [A-Z] AからZまでのいずれか1文字
	// [A-Za-z0-9] AからZ, aからz, 0から9までのいずれか1文字
	// [^ABC] A,B.C以外の文字
	// [^A-Z] AからZ以外の文字
	//
	// 例＞Janコードかどうか
	// \s*[0-9]+\s* jan?
	//
	// \w 英数文字。[a-zA-Z0-9]と同様
	// \W \w以外の文字
	// \d 数値文字。[0-9]と同等
	// \D \d以外の文字
	// \s 空白文字
	// \S \s以外の文字
	// \n 改行文字
	//
	// ● 繰り返し 構文 マッチ対象
	// A+ 1個以上連続したA(A, AA, AAA, ...)
	// A* 0個以上連続したA( , A, AA, AAA, ...)
	// A? 0または1つの任意文字( , A, B, C, ...)
	// A{5} 5回繰り返し。 AAAAAと同じ
	// A{3,} 3回以上繰り返し。 AAA+と同じ
	// A{3,5} 3回以上5回以下繰り返し。 AAAA?A?と同じ
	//
	// ● 位置指定 構文 マッチ対象
	// ^ 行の先頭
	// $ 行の末尾
	// ------------------------------------------------------------------------
	// カラム、開始位置、長さ、ブール値、設定ステータス値、レジックスパターン
	// ------------------------------------------------------------------------
	public void addFilter(int col, int pos, int len, String regex, int stat) {
		filterList.add(new FilterParam(col, pos, len, regex, stat, true));
	}

	public void addFilter(int col, int pos, int len, String regex, int stat,
			boolean flag) {
		filterList.add(new FilterParam(col, pos, len, regex, stat, flag));
	}

	// -------------------------------------------------------------------------
	// テキストのみでパラメータ設定したい場合を考慮する
	// -------------------------------------------------------------------------
	private class FilterParam {
		private int col = 0; // 対象カラム

		private int start = 0; // 開始位置

		private int len = 0; // 長さ

		private Pattern pattern = null;// 文字列検査パターン

		private boolean flag = true; // 検査文字にマッチする場合statusをセットするならならTrueもしくは"T"

		private int stat = 0; // 1,2,4,8,16,32,64など分解可能な値

		// ---------------------------------------------------------------------
		// コンストラクタ
		// regex にカンマを含ませたい場合はデリミターを引数に持たせる
		// ---------------------------------------------------------------------
		private FilterParam(int col, int pos, int len, String regex, int stat,
				boolean flag) {
			this.col = col;
			this.start = pos;
			this.len = len;
			this.flag = flag;
			this.stat = stat;
			pattern = Pattern.compile(regex);
		}

		// ---------------------------------------------------------------------
		// アクセッサ
		// ---------------------------------------------------------------------
		public int getCol() {
			return col;
		}

		public int getStart() {
			return start;
		}

		public int getEnd() {
			return start + len;
		}

		public Pattern getPattern() {
			return pattern;
		}

		public boolean getFlag() {
			return flag;
		}

		public int getStat() {
			return stat;
		}

	} // end of class ParmsObj

	// ------------------------------------------------------------------------
	// パターンにマッチした評価値（statの合計）を返す
	// ------------------------------------------------------------------------
	public int checkIt(String rec, String delimiter) {
		return checkIt(rec.split(delimiter));
	}

	public int checkIt(String[] array) {
		String wCel = "";
		String wStr = "";
		int status = 0;
		for (FilterParam filter : filterList) {
			int col = filter.getCol();
			int startPos = filter.getStart();
			int endPos = filter.getEnd();
			Pattern pattern = filter.getPattern();
			// String regex = filter.getRegex();
			if (filter != null && array.length > col) {
				wCel = array[col];
				if (wCel.length() >= startPos) {
					if (endPos > 0 && wCel.length() > endPos) {
						wStr = wCel.substring(startPos, endPos);
					} else {
						wStr = wCel.substring(startPos);
					}
					// if (wStr.matches(filter.getRegex()) == filter.getFlag())
					if (pattern.matcher(wStr).matches() == filter.getFlag()) {
						status += filter.getStat();
					}
				}
			}
		}
		return status;// 1,2,4,8,16
	}

	// ########################################################################
	// main
	// ########################################################################
	public static void main(String[] argv) {
		test01();
		test02();
	}

	// ------------------------------------------------------------------------
	// test01
	// ------------------------------------------------------------------------
	public static void test01() {
		String[] rec = { " * DATA BASE REPORT                ",
				" * CONTENTS OF DATABASE            ",
				" * FILE OPTIONS                    ",
				" * FILE SPACE ALLOCATIONS          ",
				" * PHYSICAL LAYOUT OF THE DATABASE ", " OTHER ", };

		RegChecker checker = new RegChecker();
		checker.addFilter(0, 0, 0, "^\\s\\* DATA BASE REPORT .*", 16);
		checker.addFilter(0, 0, 0, "^\\s\\* CONTENTS OF DATABASE .*", 8);
		checker.addFilter(0, 0, 0, "^\\s\\* FILE OPTIONS .*", 4);
		checker.addFilter(0, 0, 0, "^\\s\\* FILE SPACE ALLOCATIONS .*", 2);
		checker.addFilter(0, 0, 0, "^\\s\\* PHYSICAL LAYOUT OF THE DATABASE .*",
				1);
		// filter.addFilter(0, 0, 0, ".*", 32);
		// filter.addFilter(0, 0, 0, "^1\\s*", 0);
		// FilterParams ins = new FilterParams(filter);
		for (int i = 0; i < rec.length; i++) {
			System.out.println(rec[i] + "=>" + checker.checkIt(rec[i], "\t"));
		}
	}

	// ------------------------------------------------------------------------
	// test02
	// ------------------------------------------------------------------------
	public static void test02() {
		String[] rec = {
				"71211886	200711269999123101054000000101080909122170121253151081400",
				"71226695	200711269999123120303110000105010705222130111133232042200",
				"71226710	200711269999123101034100100111060606122050141439131051300",
				"71227570	200711269999123120304110010102010206222130111138232052300",
				"71230060	200711269999123100004000000115020406111140131335131051300",
				"71230648	200711269999123120303110000105010605222130141430232042201",
				"71233302	200711269999123101035111000101030406122020111137131051300",
				"71234127	200711269999123101052000000111030309111070131352151081400",
				"71241123	200711269999123111051000000105030310231070242455252092400",
				"71244109	200711269999123120403100010105010807222130111144242062300", };

		RegChecker checker = new RegChecker();
		// カラム、開始位置（０より）、長さ、ブール値、設定ステータス値、レジックスパターン
		checker.addFilter(0, 0, 1, "^7.*", 16);
		checker.addFilter(1, 54, 1, "3", 1);

		for (int i = 0; i < rec.length; i++) {
			System.out.println(rec[i] + " stat=> "
					+ checker.checkIt(rec[i], "\t"));
		}
	}

}
