package kyPkg.converter;

import static kyPkg.util.KUtil.range2Array;

import java.util.HashMap;

public class CnvMap implements Inf_StrConverter {
	private String otherName = "Other"; // その他、非該当項目につける名前
	private HashMap<String, String> map;
	private String[] tagNames;

	// その他、非該当項目につける名前
	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	// -------------------------------------------------------------------------
	// 計算による文字レンジ変換、
	// ０から始まるものを１から始まる値に変換
	// （タグ名、オリジナルの値、変換先SEQ）のマップに変換
	// -------------------------------------------------------------------------
	public static CnvMap shiftConverter(String[] tag, int nShift) {
		CnvMap cnvMap = null;
		if (tag != null) {
			String[] srcArray = range2Array(0, tag.length);
			String[] forwardA = range2Array(nShift, tag.length + nShift);
			cnvMap = new CnvMap(tag, srcArray, forwardA);
		} else {
			System.out.println("#ERROR @shiftConverter tag=null");
		}
		return cnvMap;
	}

	// ｎ区切りにシーケンスをまとめる方法
	public static int[] nConverter(int[] iArray, int n) {
		return nConverter(iArray, n, false);
	}

	public static int[] nConverter(int[] iArray, int n, boolean zero) {
		int[] ｒArray = new int[iArray.length];
		int grade = 1;
		if (zero)
			grade = 0; // zero を含む場合
		for (int i = 0; i < iArray.length; i++) {
			ｒArray[i] = (iArray[i] + (n - grade)) / n;
			System.out.println(" val:" + iArray[i] + " ans:" + ｒArray[i]);
		}
		return ｒArray;
	}

	public CnvMap(String tagStr, String valStr, String forward,
			String delimiter) {
		this((tagStr != null) ? tagStr.split(delimiter) : null,
				valStr.split(delimiter),
				(forward != null) ? forward.split(delimiter) : null);
	}

	public CnvMap(String[] tags, String[] sources, String[] forWards) {
		if (sources == null) {
			System.out.println("");
			return;// varArrayは必須項目
		}
		// 位置に充てる名前を設定する（0番目は、その他用）
		if (tags == null) {
			// nullの場合は、入力値を1から順に割り当てる
			tagNames = new String[sources.length + 1];
			for (int i = 0; i < sources.length; i++) {
				tagNames[i + 1] = sources[i];
			}
		} else {
			tagNames = new String[tags.length + 1];
			for (int i = 0; i < tags.length; i++) {
				tagNames[i + 1] = tags[i];
			}
		}
		tagNames[0] = otherName;// デフォルト名は”other”
		// 値を振り替える場合は、当該位置に行く先のシーケンスを入れておく
		int forward = 0;
		map = new HashMap();
		for (int i = 0; i < sources.length; i++) {
			String key = sources[i];
			if (forWards != null && forWards.length > i) {
				forward = Integer.parseInt(forWards[i]);
			} else {
				// 行く先指定が無い場合はそのまま
				forward = i + 1;
			}
			// ※注意 0があるので1要素多い
			if (forward >= tagNames.length)
				forward = 0; // out of rangeなら矛盾なので・・
			map.put(key, String.valueOf(forward));
		}
	}

	public String[] getTagArray() {
		return tagNames;
	}

	public String getTagName(int seq) {
		return tagNames[seq];
	}

	public String getTagName(String strSeq) {
		int seq = Integer.parseInt(strSeq);
		return getTagName(seq);
	}

	public int getSeq(String key) {
		// XXX 使用頻度が高いようなら数値用のマップをあらかじめ用意しておく
		String intObj = map.get(key);
		if (intObj != null) {
			return Integer.parseInt(intObj);
		}
		return 0;
	}

	@Override
	public String convert(String val) {
		String wkObj = map.get(val);
		if (wkObj == null)
			return "0";// 外れ値は０となる
		return wkObj;
	}

	public static void testShiftConverter() {
		String[] tag = { "1", "2", "3", "4", "5", "6" };
		CnvMap cnvMap = shiftConverter(tag, 1);
		String[] forDebug = range2Array(0, tag.length);
		for (int i = 0; i < forDebug.length; i++) {
			String debug = forDebug[i];
			System.out.println(
					"(" + debug + ") convert=>" + cnvMap.convert(debug));
		}
	}

	public static void testNConverter() {
		int[] iArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		int[] rArray = nConverter(iArray, 3, true);
		for (int i = 0; i < rArray.length; i++) {
			System.out.println("  ｒArray:" + rArray[i]);
		}
	}

	public static void test00() {
		// 計算による文字レンジ変換、
		// ex ０から始まるものを１から始まるに変換・・・・ 幅は？元幅＋１かな
		String sourceVal = "0,1,2,3,4,5,6,7"; // 元
		String forward = "1,2,3,4,5,6,7,8"; // 変換先
		String tagArray = "a,b,c,d,e,f,g,h"; // 変換先名称
		CnvMap cnvMap = new CnvMap(tagArray, sourceVal, forward, ",");
		String[] names = cnvMap.getTagArray();
		for (int i = 0; i < names.length; i++) {
			System.out.println("names[" + i + "]:" + names[i]);
		}
		String[] dArray = sourceVal.split(",");
		String debug = "";
		for (int i = 0; i < dArray.length; i++) {
			debug = dArray[i];
			System.out
					.print("(" + debug + ") convert=>" + cnvMap.convert(debug));
			System.out.println("          =>" + names[cnvMap.getSeq(debug)]);
		}
	}

	public static void test02() {
		// ---------------------------------------------------------------------
		// 時間の変換、
		// １時間おき→３時間おき
		// １時間おき→変則時間おき
		// XXX 3時間おきコンバータを作る
		// ---------------------------------------------------------------------
		// 5:00 - 5:59
		// 6:00 - 6:59
		// 7:00 - 7:59
		// 8:00 - 8:59
		// 9:00 - 9:59
		// 10:00 - 10:59
		// 11:00 - 11:59
		// 12:00 - 12:59
		// 13:00 - 13:59
		// 14:00 - 14:59
		// 15:00 - 15:59
		// 16:00 - 16:59
		// 17:00 - 17:59
		// 18:00 - 18:59
		// 19:00 - 19:59
		// 20:00 - 20:59
		// 21:00 - 21:59
		// 22:00 - 22:59
		// 23:00 - 23:59
		// 0:00 - 0:59
		// 1:00 - 1:59
		// 2:00 - 2:59
		// 3:00 - 3:59
		// 4:00 - 4:59
		// ----------------------------------------------------------------------
		String sourceVal = "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23"; // 元
		String forward = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24"; // 変換先
		String tagArray = "a,b,c,d,e,f,g,h"; // 変換先名称
		CnvMap cnvMap = new CnvMap(tagArray, sourceVal, forward, ",");
		String[] names = cnvMap.getTagArray();
		for (int i = 0; i < names.length; i++) {
			System.out.println("names[" + i + "]:" + names[i]);
		}
		String[] dArray = sourceVal.split(",");
		String debug = "";
		for (int i = 0; i < dArray.length; i++) {
			debug = dArray[i];
			System.out
					.print("(" + debug + ") convert=>" + cnvMap.convert(debug));
			System.out.println("          =>" + names[cnvMap.getSeq(debug)]);
		}
	}

	// ---------------------------------------------------------------------
	// 時間の変換、
	// １時間おき→３時間おき
	// １時間おき→変則時間おき
	// ---------------------------------------------------------------------
	// 計算による文字レンジ変換、
	// ex ０から始まるものを１から始まるに変換・・・・ 幅は？元幅＋１かな

	// -------------------------------------------------------------------------
	// XXX ファイルからも生成できるようにしておく
	// XXX varArrayに正規表現は使えないだろうか？？ちょっと考えてみる ,hashのキーにregix？！混乱しそう
	// XXX → → → 具体的にやってみよう！マッチしなかったらregixパターンマッチング、対応するキーを落とし込むかな
	// XXX → → → regixかどうかどうやって判定するのだ？？
	// -------------------------------------------------------------------------
	public static void main(String[] argv) {
		// testShiftConverter();
		test01();
	}

	public static void test01() {
		// ---------------------------------------------------------------------
		// sourceValとforwardをハッシュマップにいれて、値で辞書引き、ｓｅｑを返す（名前も引ける）
		// つまり、入力値を1から始まるシーケンスに置き換える為のフィルターとして使用する
		// コンテキストは違うが、シーケンスの代わりに振り替え先コードを入れても良いかな・・・
		// ---------------------------------------------------------------------
		// ※非該当はインデックス０とした（これを利用するかしないかについては実装次第）
		// sourcesはコード等を想定していたが、文字列ならなんでもいい（ＦＡも名寄せすれば使用可能であろう）
		// forwardsは振り替え先の位置情報、省略時はsourcesの出現順となる
		// tagNamesは振り替え先名称、省略時はsourcesそのものにできる
		// ---------------------------------------------------------------------
		String sources = "Zappa,Captain,Lowell,Jerry,Phil,Roy"; // 元
		String forwards = null; // 変換先シーケンス
		String tagNames = null; // 変換先名称
		forwards = "1,1,2,2,3,3";
		tagNames = "Freaks,Guiter,Base";
		// tagArray = "Frank,Beefheart,George,Garchia,Lesh,Estrada"; // 変換先名称
		CnvMap cnvMap = new CnvMap(tagNames, sources, forwards, ",");
		String[] names = cnvMap.getTagArray();
		for (int i = 0; i < names.length; i++) {
			System.out.println("names[" + i + "]:" + names[i]);
		}
		String[] dArray = sources.split(",");
		String debug = "";
		for (int i = 0; i < dArray.length; i++) {
			debug = dArray[i];
			System.out
					.print("(" + debug + ") convert=>" + cnvMap.convert(debug));
			System.out.println("          =>" + names[cnvMap.getSeq(debug)]);
		}
		debug = "Eric";
		System.out.print("(" + debug + ") convert=>" + cnvMap.convert(debug));
		System.out.println("          =>" + names[cnvMap.getSeq(debug)]);
		debug = "Duane";
		System.out.print("(" + debug + ") convert=>" + cnvMap.convert(debug));
		System.out.println("          =>" + names[cnvMap.getSeq(debug)]);
	}

}
