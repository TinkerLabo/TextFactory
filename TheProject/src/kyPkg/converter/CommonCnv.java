package kyPkg.converter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kyPkg.uCodecs.CharConv;

public class CommonCnv implements Inf_ListArrayConverter {
	private CharConv charConv;
	private Map<Integer, Integer> lenMap;
	private Map<Integer, DecimalFormat> formatMap;
	private Map<Integer, Integer> typeMap;
	private Set<Integer> keySet;

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public CommonCnv() {
		super();
		charConv = CharConv.getInstance();
	}
	public CommonCnv(Map<Integer, String> paramMap) {
		this();
		parseIt(paramMap);
	}

	// ------------------------------------------------------------------------
	// 初期化
	// ------------------------------------------------------------------------
	private void reset() {
		this.lenMap = new HashMap();
		this.formatMap = new HashMap();
		this.typeMap = new HashMap();
		this.keySet = new HashSet();// Dummy
	}

	// ------------------------------------------------------------------------
	// パラメータをパース
	// ------------------------------------------------------------------------
	public void parseIt(Map<Integer, String> paramMap) {
		reset();
		this.keySet = paramMap.keySet();
		for (Integer col : keySet) {
			String param = paramMap.get(col);
			if (param != null) {
				String[] params = param.split(",");
				int len = -1;
				int type = 1;
				if (params.length >= 1) {
					String whichType = params[0].toLowerCase();
					if (whichType.startsWith("t")) {
						type = 0;// trim
					} else if (whichType.startsWith("n")) {
						type = 1;// narrow
						if (params.length >= 2)
							len = parseInt(params[1], -1);
					} else if (whichType.startsWith("w")) {
						type = 2;// wide
						if (params.length >= 2)
							len = parseInt(params[1], -1);
					} else if (whichType.startsWith("z")) {
						type = 3;// FIX_LEADING_ZERO
						if (params.length >= 2)
							len = parseInt(params[1], -1);
					} else if (whichType.startsWith("d")) {
						type = 4;// Decimal Format
						if (params.length >= 2) {
							String format = params[1];// "000000.000"
							DecimalFormat decFormat = new DecimalFormat(format);
							formatMap.put(col, decFormat);
						}
					}
				}
				lenMap.put(col, len);
				typeMap.put(col, type);
			}
		}
	}

	private int parseInt(String val, int defaultValue) {
		int num = defaultValue;
		try {
			num = Integer.parseInt(val);
		} catch (Exception e) {
		}
		return num;

	}

	private String convert(String cel, Integer col) {
		Integer type = typeMap.get(col);
		if (type == 0) {
			return cel.trim();
		} else if (type == 1) {
			return cnv2NarrowFixedLen(cel, lenMap.get(col));
		} else if (type == 2) {
			return cnv2WideFixedLen(cel, lenMap.get(col));
		} else if (type == 3) {
			return cnv2LeadingZero(cel, lenMap.get(col));
		} else if (type == 4) {
			return cnv2decimalFormated(cel, formatMap.get(col));
		}
		return cel;
	}

	// ------------------------------------------------------------------------
	// 固定長半角
	// ------------------------------------------------------------------------
	public String cnv2NarrowFixedLen(String cel, int len) {
		return charConv.cnvFixHalf(cel, len);
	}

	// ------------------------------------------------------------------------
	// 固定長全角Plus
	// ------------------------------------------------------------------------
	public String cnv2WideFixedLen(String cel, int len) {
		return charConv.cnvFixWide2(cel, len);
	}

	// ------------------------------------------------------------------------
	// 前ゼロ半角
	// ------------------------------------------------------------------------
	public String cnv2LeadingZero(String cel, int len) {
		return CharConv.fixRight(cel.trim(), len, '0');
	}

	// ------------------------------------------------------------------------
	// フォーマット出力
	// ------------------------------------------------------------------------
	public String cnv2decimalFormated(String cel, DecimalFormat decFormat) {
		try {
			return decFormat.format(Double.parseDouble(cel));
		} catch (Exception e) {
			System.out.println("ERROR @" + cel);
			return cel;
		}
	}

	@Override
	public String[] convert(String[] array) {
		for (Integer col : keySet) {
			String val = array[col];
			array[col] = convert(val, col);
		}
		return array;
	}

	@Override
	public List convert(List list) {
		for (Integer col : keySet) {
			if (list.size() > col) {
				Object element = list.get(col);
				String val = element.toString();
				// if(element instanceof String){
				// val = (String) element;
				// }else if(element instanceof Integer){
				// val = String.valueOf(element) ;
				// }else {
				// val = element.toString();
				//
				// }
				list.set(col, convert(val, col));
			}
		}
		return list;
	}

	// 2014-02-24 yuasa
	private static void testIt() {
		Map<Integer, String> paramMap = new HashMap();
		// 0番めの要素を、全角10文字に変換する
		paramMap.put(0, "wide,25");
		// 2番めの要素を、trim
		paramMap.put(3, "trim");
		// 3番めの要素を、全角25文字に変換する
		paramMap.put(4, "narrow,3");
		Inf_ListArrayConverter ins = new CommonCnv(paramMap);
		// テストデータ
		List<String> testData = new ArrayList();
		testData.add("0_abc     ");
		testData.add("1_abc     ");
		testData.add("2_abc     ");
		testData.add("3_abc     ");
		testData.add("4_abc     ");
		testData = ins.convert(testData);
		for (String element : testData) {
			System.out.println("@testIt element:" + element);
		}
	}

	public static void main(String[] argv) {
		testIt();
	}

}
