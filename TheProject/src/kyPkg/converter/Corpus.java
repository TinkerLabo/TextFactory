package kyPkg.converter;

public class Corpus {
	//TODO	20160525　varidationを行いデータ補正を行うフィルターを作りたい
	//TODO	フィルターは特定のインターフェースをもったクラスファイル（jar）を所定のフォルダ下に配置することによりプラグインとして任意のフィルタを追加できるようにしたい
	//-------------------------------------------------------------------------
	//一般的なもの
	//-------------------------------------------------------------------------
//	public static final String DICT_CONVERT = "dictconvert";
//	public static final String REPLACE = "replace";
//	public static final String TRANSLATE = "translate";
	public static final String PATTERN_MATCH = "パターンマッチ変換";
	public static final String TO_WIDE = "全角化";
	public static final String TO_HALF = "半角化";
	public static final String FIX_WIDE = "固定長全角";
	public static final String FIX_HALF = "固定長半角";
	public static final String FIX_LEADING_SPACE = "固定前スペース";
	public static final String FIX_LEADING_ZERO = "固定前ゼロ";
	public static final String FIX_LEN = "固定長";
	public static final String TRIM = "Trim";
	public static final String LOWER_CASE = "LowerCase";
	public static final String UPPER_CASE_EX = "UpperCaseEx";
	public static final String UPPER_CASE = "UpperCase";

	public static final String DATE_CNV = "日付変換";
	public static final String DATE_CNV6 = "日付変換6";
	public static final String MULTI_ANS_TO_FLAG = "マルチフラグ化";
	public static final String MULTI_ANS_TO_FLAG2 = "マルチフラグ化２";

	public Corpus() {
		super();
	}

}