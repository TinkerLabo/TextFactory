package kyPkg.filter;

import kyPkg.converter.Inf_ArrayCnv;
public class BasicCnv implements Inf_ArrayCnv {
	// -------------------------------------------------------------------------
	// ※雛形
	// パラメータ：
	// -------------------------------------------------------------------------

	public BasicCnv() {
		super();
	}

	// ------------------------------------------------------------------------
	// init
	// ------------------------------------------------------------------------
	@Override
	public void init() {
	}

	// ------------------------------------------------------------------------
	// Convert
	// ------------------------------------------------------------------------
	@Override
	public String[] convert(String[] rec, int stat) {

		return rec;
	}

	// ------------------------------------------------------------------------
	// fin
	// ------------------------------------------------------------------------
	@Override
	public void fin() {
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		testForHVShare();
	}

	// -------------------------------------------------------------------------
	// 単体テスト　
	// -------------------------------------------------------------------------
	public static void testForHVShare() {
		String inPath = "C:/@qpr/home/123620000036/calc/loyHead.txt";
		String outPath = "C:/@qpr/home/123620000036/calc/HVShare.txt";
		Inf_ArrayCnv cnv = new BasicCnv();
		EzReader reader = new EzReader(inPath, cnv);
		new Common_IO(outPath, reader).execute();
	}

}
