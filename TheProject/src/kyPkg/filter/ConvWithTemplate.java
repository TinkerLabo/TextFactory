package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;

import kyPkg.converter.Inf_ArrayCnv;

//----------------------------------------------------------------------------
//2015-03-13 雛形文字列を使って、テキストデータを編集するプログラム
//----------------------------------------------------------------------------
public class ConvWithTemplate implements Inf_ArrayCnv {
	private String template = "";
	private List<Integer> colList;
	private String[] outRecs;
	private List<Object> argList;

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public ConvWithTemplate(String template, String targetCols) {
		super();
		this.template = template;
		String[] cols = targetCols.split(",", -1);
		colList = new ArrayList();
		for (int i = 0; i < cols.length; i++) {
			Integer col = Integer.valueOf(cols[i]);
			if (col != null)
				colList.add(col);
		}
		outRecs = new String[1];
		argList = new ArrayList();
	}

	@Override
	public void init() {
	}

	@Override
	public void fin() {
	}

	@Override
	// ------------------------------------------------------------------------
	// convert
	// ------------------------------------------------------------------------
	public String[] convert(String[] rec, int stat) {
		argList.clear();
		for (Integer col : colList) {
			argList.add(rec[col]);
		}
		outRecs[0] = String.format(template, list2array(argList));
		System.out.println("debug:" + outRecs[0]);
		return outRecs;
	}

	private Object[] list2array(List<Object> argList) {
		return (Object[]) argList.toArray(new String[argList.size()]);

	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] argv) {
		System.out.println("Start");
		testJicfsKanji2SQL();
		System.out.println("end");
	}

	// ------------------------------------------------------------------------
	// test
	//　JICFSｶﾅ漢字変換　関連20150313
	// ------------------------------------------------------------------------
	public static void testJicfsKanji2SQL() {
		String sqlTemplate = "update ITEM Set xc2 = '%s' where xc2 = '' and   xa1 = '%s'";// 雛形
		String targetCols = "1,0";// ひな型に埋め込むデータのカラム位置
		ConvWithTemplate flt = new ConvWithTemplate(sqlTemplate, targetCols);
		String inPath = "c:/jicfsKCNV.txt";
		String outPath = "c:/jicfsKCNV_sql.txt";
		EzReader reader = new EzReader(inPath);
		EzWriter writer = new EzWriter(outPath, flt);
		new Basic_IO(writer, reader).execute();
	}

}
