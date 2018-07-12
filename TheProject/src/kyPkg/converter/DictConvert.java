package kyPkg.converter;

import java.util.HashMap;
import java.util.List;

import kyPkg.uFile.MapUtil;

public class DictConvert implements Inf_Converter {

	private HashMap<String, List<String>> map;
	private int valCol = 0;
	private String errorVal = "";

	// -------------------------------------------------------------------------
	// コンストラクタ	20170623
	// -------------------------------------------------------------------------
	public DictConvert(String dictPath) {
		this(MapUtil.file2Dict(dictPath));//20170731この部分ををカスタマイズする
	}

	public DictConvert(HashMap<String, List<String>> map) {
		super();
		this.map = map;
	}

	// ------------------------------------------------------------------------
	// Convert
	// ------------------------------------------------------------------------
	@Override
	public String convert(String cell, String[] list) {
		List<String> mapVals = map.get(cell);
		if (mapVals != null && mapVals.size() > valCol)
			return mapVals.get(valCol);
		else
			return errorVal;
	}

	// ------------------------------------------------------------------------
	//	ファイル名やフォルダー名に使用出来ない文字でパスをスプリットして、valueのカラムを指定させるか・・・
	//	\　/　:　*　?　"　<　>　|
	// ------------------------------------------------------------------------
	public static void test00() {
		String dictPath = "C:/@QPR/home/qpr/res/channel.dic";
		Inf_Converter translate = new DictConvert(dictPath);
		System.out.println("# test=>" + translate.convert("0", null));
		System.out.println("# test=>" + translate.convert("1", null));
		System.out.println("# test=>" + translate.convert("2", null));
		System.out.println("# test=>" + translate.convert("3", null));
		System.out.println("# test=>" + translate.convert("4", null));
		System.out.println("# test=>" + translate.convert("5", null));
		System.out.println("# test=>" + translate.convert("6", null));
		System.out.println("# test=>" + translate.convert("7", null));
		System.out.println("# test=>" + translate.convert("8", null));
		System.out.println("# test=>" + translate.convert("9", null));
		System.out.println("# test=>" + translate.convert("20", null));
		System.out.println("# test=>" + translate.convert("21", null));
		System.out.println("# test=>" + translate.convert("22", null));
		System.out.println("# test=>" + translate.convert("XX", null));
		System.out.println("# test=>" + translate.convert("", null));
		System.out.println("# test=>" + translate.convert(null, null));
	}

	public static void test20170731() {
		String dictPath = "C:/samples/dict2file/dict.txt";
		HashMap<String, List<String>> map = MapUtil.file2Dict(dictPath,
				new int[] { 0 }, new int[] { 1, 2, 3, 4, 5 });
//		Inf_Converter translate = new DictConvert(map);
		List<String> attr = map.get("#4987306038622");
		System.err.println("<0>" + attr.get(0));
		System.err.println("<1>" + attr.get(1));
		System.err.println("<2>" + attr.get(2));
		System.err.println("<3>" + attr.get(3));
		System.err.println("<4>" + attr.get(4));


	}

	public static void main(String[] argv) {
		//		test00();
		test20170731();
	}

}
