package kyPkg.filter;

import kyPkg.converter.Inf_ArrayCnv;

public class PickUpTermData implements Inf_ArrayCnv {
	// -------------------------------------------------------------------------
	// 指定期間のデータのみを出力する
	// パラメータ：
	// -------------------------------------------------------------------------
	private int dateCol = 5;
	private int bef = 0;
	private int aft = 0;

	public PickUpTermData(int dateCol, String bef, String aft) {
		super();
		this.dateCol = dateCol;
		this.bef = str2int(bef);
		this.aft = str2int(aft);
		//		System.out.println("PickUpTermData dateCol=>" + dateCol);
		//		System.out.println("bef=>" + bef);
		//		System.out.println("aft=>" + aft);
	}

	// ------------------------------------------------------------------------
	// init
	// ------------------------------------------------------------------------
	@Override
	public void init() {
		//		System.out.println("## init() ##");
	}

	// ------------------------------------------------------------------------
	// fin
	// ------------------------------------------------------------------------
	@Override
	public void fin() {
		//		System.out.println("## fin() ##");
	}

	private int str2int(String val) {
		try {
			return Integer.parseInt(val);
		} catch (Exception e) {
			return -1;
		}
	}

	// ------------------------------------------------------------------------
	// Convert
	// ------------------------------------------------------------------------
	@Override
	public String[] convert(String[] rec, int stat) {
		if (rec.length <= dateCol)
			return null;
		int intVal = str2int(rec[dateCol]);
		if (bef <= intVal && intVal <= aft) {
			//			  System.out.println("ok=>" + intVal);
			return rec;
		}
		return null;
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test_PickUpTermData();
	}

	// -------------------------------------------------------------------------
	// 単体テスト　
	// -------------------------------------------------------------------------
	public static void test_PickUpTermData() {
		// bef:20130901
		// aft:20141031
		// oPath:C:/@qpr/home/238881000301/tran/20130901_20141031.trv
		// mPath:C:/@qpr/home/238881000301/tran/20130901_20141031.mov
		// iPath:C:/@qpr/home/238881000301/tran/20130901_20141031.trn
		String inPath = "C:/@qpr/home/238881000301/tran/20130901_20141031.trn";
		String outPath = "C:/@qpr/home/238881000301/tran/20130901_201410930pk.trn";
		String bef = "20130901";
		String aft = "20130903";
		Inf_ArrayCnv cnv = new PickUpTermData(5, bef, aft);
		EzReader reader = new EzReader(inPath);
		EzWriter writer = new EzWriter(outPath, cnv);
		new Common_IO(writer, reader).execute();
	}

}
