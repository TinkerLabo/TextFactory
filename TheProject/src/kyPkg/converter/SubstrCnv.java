package kyPkg.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import globals.ResControl;
import kyPkg.filter.ParmsObj;
import kyPkg.filter.TextFactory;
import kyPkg.uCodecs.CharConv;
import kyPkg.uDateTime.DateCalc;
import kyPkg.uDateTime.Holidays;
import kyPkg.uFile.File2Matrix;

// python PIL 
// encode
//	XXX 同じパラメータを指定できないバグを修正
//	XXX パラメーターを挿入したい
//	XXX フィルターをパイプ処理できるとうれしいね・・・うーん
//	XXX 処理を別スレッド化
//	XXX 処理をプラグイン化（外部クラス化）
//	XXX 同じ拡張子のデータについて一括変換できるようにしたいが！？（鈴木さんが苦しんでいたので・・・）
//	XXX フィルタを分割したい（個々のフィルタをプラグインとして扱えるように書き換えたい）
//	XXX 正規表現をぷリコンパイルして効率を上げたい

//2009/03/17 yuasa
//2015/05/25 yuasa フィルタのインスタンスをマップ化？（インスタンスの初期化に関する問題がないか気になるが・・・それぞれのフィルタに初期化ルーチンを設けるか？）
public class SubstrCnv extends Corpus implements Inf_LineConverter {
	private String[] array = null;
	private List list = null;
	private StringBuffer buf;
	private String cel;
	private String prefix = "";
	private String suffix = "";
	private CharConv charConv;
	private String today;
	private String firstSunday;
//	private DecimalFormat df00 = new DecimalFormat("00");
//	private SimpleDateFormat dformat;

	private List<ParmsObj> params;
	private HashMap<String, Inf_Converter> cnvMap = new HashMap();
	private HashMap<String, Inf_FilterFactory> filterMap;//20170626

	private int getLength() {
		if (list == null) {
			return array.length;
		} else {
			return list.size();
		}
	}

	@Override
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public SubstrCnv() {
		init();
	}

	public SubstrCnv(List paramList) {
		this();
		parseParam(paramList);
	}

	public SubstrCnv(String[] paramArray) {
		this();
		parseParam(Arrays.asList(paramArray));
	}

	public SubstrCnv(String paramPath) {
		this();
		if (paramPath.indexOf(",") > 0) {
			// ダイレクトにパラメータが指定されている
			parseParam(Arrays.asList(new String[] { paramPath }));
		} else {
			// パラメータにパスが指定されている
			parseParam(File2Matrix.extract(paramPath, (String) null));
		}
	}

	// -------------------------------------------------------------------------
	// 初期化
	// -------------------------------------------------------------------------
	@Override
	public void init() {
		today = DateCalc.getToday();
		firstSunday = Holidays.getThisFirstSunday();
//		dformat = kyPkg.uDateTime.DateUtil.getSimpleDateFormat("yyyyMMdd");
		buf = new StringBuffer();
		charConv = CharConv.getInstance();
		// --------------------------------------------------------------------
		//20170628
		// --------------------------------------------------------------------
		filterMap = new HashMap();
		filterMap.put(DictconvertFactory.TITLE, new DictconvertFactory());
		filterMap.put(ReplaceFactory.TITLE, new ReplaceFactory());
		filterMap.put(TranslateFactory.TITLE, new TranslateFactory());
		filterMap.put(RangeConvertFactory.TITLE, new RangeConvertFactory());
	}

	public void parseParam(List paramList) {
		cnvMap = new HashMap();
		params = new ArrayList();
		for (int i = 0; i < paramList.size(); i++) {
			ParmsObj pObj = new ParmsObj(paramList.get(i));
			String filter = pObj.getFilterName(true);
			String param = pObj.getParam();
			String signature = pObj.getSignature();
			Inf_FilterFactory factory = filterMap.get(filter);
			if (factory != null)
				cnvMap.put(signature, factory.getConverter(param));
			params.add(pObj);
		}
	}

	@Override
	public String getHeader() {
		buf.delete(0, buf.length());
		buf.append(prefix);
		for (int i = 0; i < params.size(); i++) {
			ParmsObj pObj = params.get(i);
			if (pObj.getFilterName(true).equals("D")) {
				cel = pObj.getParam();
			} else {
				cel = pObj.getComment(true);
				cel = cel.replaceAll(":", "\t");//TODO タブで切ってある・・・問題あり？！複数のセルを発生する場合、複数のヘッダーを｜で区切る？
				if (cel.equals("")) {
					cel = "#col" + (i + 1);
				}
			}
			buf.append(cel);
		}
		buf.append(suffix);
		return buf.toString();
	}

	private void setList(List list) {
		this.list = list;
		this.array = null;
	}

	private void setArray(String[] array) {
		this.array = array;
		this.list = null;
	}

	@Override
	public String[] convert(String[] pArray, int lineNumber) {
		if (pArray == null)
			return null;
		setArray(pArray);
		return new String[] { convert(lineNumber) };
	}

	@Override
	public String convert2Str(String[] pArray, int lineNumber) {
		if (pArray == null)
			return null;
		setArray(pArray);
		return convert(lineNumber);
	}

	@Override
	public String convert2Str(List list, int lineNumber) {
		if (list == null)
			return null;
		setList(list);
		return convert(lineNumber);
	}

	private String colGet(int col) {
		if (list == null) {
			return array[col];
		} else {
			return (String) list.get(col);
		}
	}

	private String convert(int lineNumber) {
		cel = "";
		buf.delete(0, buf.length());
		buf.append(prefix);
		for (int i = 0; i < params.size(); i++) {
			cel = "";
			ParmsObj pObj = params.get(i);
			String filter = pObj.getFilterName(true);
			//			String parm = pObj.getParms(0);
			String parm = pObj.getParam();
			if (filter.equals("D")) { // 区切り文字
				cel = parm;
			} else if (filter.equals("@")) {// 固定文字列
				parm = parm.toUpperCase();
				if (parm.equals("@SEQ")) {
					cel = String.valueOf(lineNumber + 1);
				} else if (parm.equals("@TODAY")) {
					cel = today;
				} else if (parm.equals("@FIRSTSUNDAY")) {
					cel = firstSunday;
				} else {
					cel = parm;
				}
			} else {
				// substring処理
				int col = pObj.getCol();
				if (0 <= col && col < getLength()) {
					String data = colGet(col);
					int start = pObj.getStart();
					int end = start + pObj.getLen();
					if (start < 0) {
						cel = data;
					} else {
						if (start < data.length()) {
							if (end >= 0) {
								// 寸足らずだったら・・・そのままの長さ
								if (end > data.length())
									end = data.length();
								cel = data.substring(start, end);
							} else {
								cel = data.substring(start);
							}
						}
					}
				}
				// フィルター処理
				cel = convertIT(pObj, cel);
			}
			// 該当パラメータが存在しない場合はそのままそのセルが出力されるのか？？
			buf.append(cel);
		}
		buf.append(suffix);
		return buf.toString();
	}

	// XXX ここはif文ではなくハッシュマップで処理する
	private String convertIT(ParmsObj pObj, String cel) {
		String filter = pObj.getFilterName(true);
		int len = pObj.getLen();
		String signature = pObj.getSignature();
		if (filter.equals("")) {
			return cel;
		} else if (filter.equalsIgnoreCase(TRIM)) {
			cel = cel.trim();
		} else if (filter.equalsIgnoreCase(UPPER_CASE)) {
			cel = cel.toUpperCase();
		} else if (filter.equalsIgnoreCase(LOWER_CASE)) {
			cel = cel.toLowerCase();
		} else if (filter.equalsIgnoreCase(TO_WIDE)) {
			cel = charConv.cnvWide(cel);
		} else if (filter.equalsIgnoreCase(TO_HALF)) {
			cel = charConv.cnvNarrow(cel);
		} else if (filter.equalsIgnoreCase(FIX_LEN)) {
			cel = CharConv.fixStr(cel, len);
		} else if (filter.equalsIgnoreCase(UPPER_CASE_EX)) {
			cel = cel.toUpperCase();
			cel = charConv.cnvK2N(cel); // ｶﾅをhost用にノーマライズする
			cel = CharConv.fixStr(cel, len);
		} else if (filter.equalsIgnoreCase(FIX_HALF)) { // 固定長半角
			cel = charConv.cnvFixHalf(cel, len);
		} else if (filter.equalsIgnoreCase(FIX_WIDE)) { // 固定長全角
			cel = charConv.cnvFixWide2(cel, len);
		} else if (filter.equalsIgnoreCase(FIX_LEADING_ZERO)) {
			cel = CharConv.fixRight(cel.trim(), len, '0');
		} else if (filter.equalsIgnoreCase(FIX_LEADING_SPACE)) {
			cel = CharConv.fixRight(cel.trim(), len, ' ');
		} else if (filter.equalsIgnoreCase(MULTI_ANS_TO_FLAG)) {
			cel = ValueChecker.cnvMultiAns(cel, pObj.getParmi(0) + 1);
		} else if (filter.equalsIgnoreCase(MULTI_ANS_TO_FLAG2)) {
			cel = ValueChecker.cnvMultiAns(cel, pObj.getParmi(0) + 1);
		} else if (filter.equalsIgnoreCase(DATE_CNV)) {
			cel = ValueChecker.cnvYmd(cel);
		} else if (filter.equalsIgnoreCase(DATE_CNV6)) {
			cel = ValueChecker.cnvYmd6(cel);
		} else if (filter.equalsIgnoreCase(PATTERN_MATCH)) {
			// パラメータ regix :match返値:Unmatch返値
			// 例> [123]:1:0 1or2or3=> 1 other => 0
			// String otherParm = pObj.getOtherParm();
			cel = ValueChecker.regcheknCnv(cel, pObj.getParm(0),
					pObj.getParm(1), pObj.getParm(2));
		} else {
			Inf_Converter cnv = cnvMap.get(signature);
			if (cnv != null)
				cel = cnv.convert(cel, null);
			//-----------------------------------------------------------------
			// XXX　2010-08-03 reflectorを使って外部クラスを指定できるようにしたい
			// XXX　クラス名 パターン をファイルから読み込む
			// XXX　メソッドのシグニチャをもう一回考え直す
			//-----------------------------------------------------------------
		}
		return cel;
	}

	@Override
	public String convert2Str(String str) {
		return convert2Str(new String[] { str }, 0);
	}

	@Override
	public void fin() {
	}

	public static void util() {
	}

	public List<String> getFilters() {
		List<String> filterNames = new ArrayList();
		// 駱駝のインデントもほしいね・・・
		filterNames.add("");
		filterNames.add(TRIM);
		filterNames.add(FIX_LEN);
		filterNames.add(FIX_HALF);
		filterNames.add(FIX_WIDE);
		filterNames.add(TO_HALF);
		filterNames.add(TO_WIDE);
		filterNames.add(UPPER_CASE);
		filterNames.add(UPPER_CASE_EX);
		filterNames.add(LOWER_CASE);
		filterNames.add(FIX_LEADING_ZERO);
		filterNames.add(MULTI_ANS_TO_FLAG);
		filterNames.add(MULTI_ANS_TO_FLAG2);
		filterNames.add(DATE_CNV);
		filterNames.add(PATTERN_MATCH); // パターンマッチ変換 2008-01-18
		filterNames.add(DATE_CNV6);
		// filterNames.add(OLD_CATEGORIZE1);
		// filterNames.add(OLD_CATEGORIZE2);
		// --------------------------------------------------------------------
		List<String> filterNames2 = new ArrayList(filterMap.keySet());// 20170628
		for (String filterName : filterNames2) {
			filterNames.add(filterName);
		}
		return filterNames;
	}

	public String getSample(String filter) {
		String sample = "";
		if (filter.equals(Corpus.PATTERN_MATCH)) {
			// 例> [123]:1:0 1or2or3=> 1 other => 0
			//		} else if (filter.equals(Translate.TITLE)) {
			//			txtOther.setText(Translate.SAMPLE);
		} else {
			Inf_FilterFactory factory = filterMap.get(filter);
			if (factory != null)
				sample = factory.getSample();
		}
		return sample;
	}

	//TODO	フィルターオブジェクトごとに解説を持たせておきたい
	public String getExplain(String filter) {
		String explain = "";
		if (filter.equals(TRIM)) {
			explain = "項目の前後の空白を取り除きます";
		} else if (filter.equals(UPPER_CASE)) {
			explain = "大文字に変換します";
		} else if (filter.equals(LOWER_CASE)) {
			explain = "小文字に変換します";
		} else if (filter.equals(TO_WIDE)) {
			explain = "全角文字に変換します";
		} else if (filter.equals(TO_HALF)) {
			explain = "可能な限り半角文字に変換します";
		} else if (filter.equals(FIX_LEN)) {
			explain = "固定長文字列に変換します、開始終了位置により文字の長さを決めます";
		} else if (filter.equals(FIX_HALF)) {
			explain = "固定長半角文字列に変換します、開始終了位置により文字の長さを決めます";
		} else if (filter.equals(FIX_WIDE)) {
			explain = "固定長半角文字列に変換します、開始終了位置により文字の長さを決めます";
		} else if (filter.equals(FIX_LEADING_ZERO)) {
			explain = "半角文字列の先頭に0を埋め込んで固定長に、開始終了位置により文字の長さを決めます";
		} else if (filter.equals(MULTI_ANS_TO_FLAG)) {
			explain = "マルチアンサー項目を1orスペースに変換します、otherに出力長さを指定します";
		} else if (filter.equals(MULTI_ANS_TO_FLAG2)) {
			explain = "マルチアンサー項目を1orスペースに変換します、otherに出力長さを指定します。結果はクオーテーションで囲まれます";
		} else if (filter.equals(PATTERN_MATCH)) {
			explain = "引数１にパターンマッチしたものに引数２を、それ以外は引数３をアサインする　例>　[123]:1:0   1or2or3=> 1 other => 0";
		} else if (filter.equals(DATE_CNV)) {
			explain = "YYYYMMDD型に変換します、カラム位置のみ指定する";
		} else {
			Inf_FilterFactory factory = filterMap.get(filter);
			if (factory != null)
				explain = factory.getExplain();
		}
		return explain;
	}

	@Override
	public List convert(List<String> list, int stat) {
		return null;
	}

	//-------------------------------------------------------------------------
	// main
	//-------------------------------------------------------------------------
	public static void main(String[] argv) {
		test02();
	}

	//-------------------------------------------------------------------------
	// 　マルチフラグ化のテスト（これは量的なテストにもなる）
	//-------------------------------------------------------------------------
	public static void test02() {
		String param = ResControl.D_DRV + "workspace/gotoTest/Parm.txt";
		String iPath = ResControl.D_DRV
				+ "workspace/gotoTest/54232_rawdata.tsv";
		String oPath2 = ResControl.D_DRV + "workspace/gotoTest/ans2.txt";
		TextFactory substr_B = new TextFactory(oPath2, iPath,
				new SubstrCnv(param));
		substr_B.execute();
	}
}
