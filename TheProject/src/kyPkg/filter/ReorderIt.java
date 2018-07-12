package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;

import kyPkg.converter.Inf_ArrayCnv;

public class ReorderIt implements Inf_ArrayCnv {
	// -------------------------------------------------------------------------
	// データ列の並びを変更する（指定した順序にならびかえる）
	// 負値、数値以外およびセル範囲外の場合ダミーセルとする
	// -------------------------------------------------------------------------
	private List<Integer> order;
	private String[] outRec;

	public ReorderIt(String param) {
		this(param.split(","));
	}

	public ReorderIt(String[] array) {
		this(paramConv(array));
	}

	public ReorderIt(List<Integer> order) {
		super();
//		System.out.println("■■■ ### ReorderIt ### ■■■ ");
		this.order = order;
	}

	private static List<Integer> paramConv(String[] array) {
		List<Integer> order = new ArrayList();
		for (String seq : array) {
			int i = -1;
			try {
				i = Integer.valueOf(seq);
			} catch (Exception e) {
				i = -1;
			}
			order.add(i);
		}
		return order;
	}

	// ------------------------------------------------------------------------
	// init
	// ------------------------------------------------------------------------
	@Override
	public void init() {
		// System.out.println("■■■ ### ReorderIt ### ■■■ init()");
		// outRec = new String[order.size()];
	}

	// ------------------------------------------------------------------------
	// Convert
	// ------------------------------------------------------------------------
	@Override
	public String[] convert(String[] rec, int stat) {
		// System.out.println("■■■ ### convert ### ■■■ ");
		outRec = new String[order.size()];
		int idx = 0;
		for (Integer seq : order) {
			if (rec.length > seq && seq >= 0) {
				outRec[idx++] = rec[seq];
			} else {
				outRec[idx++] = "";
			}
		}
		return outRec;
	}

	// ------------------------------------------------------------------------
	// fin
	// ------------------------------------------------------------------------
	@Override
	public void fin() {
		// System.out.println("■■■ ### ReorderIt ### ■■■  fin()");
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		testReorderIt();
	}

	// -------------------------------------------------------------------------
	// 単体テスト　
	// -------------------------------------------------------------------------
	public static void testReorderIt() {
		String inPath = "C:/@qpr/home/123620000036/calc/loyHead_MIX.txt";
		String outPath = "C:/@qpr/home/123620000036/calc/reordered.txt";
		Inf_ArrayCnv reorderIt = new ReorderIt(
				"0,1,2,3,4,5,6,7,12,13,14,15,16,17,18,8,9,20");
		EzReader reader = new EzReader(inPath, reorderIt);
		new Common_IO(outPath, reader).execute();
	}

	public static void testReorderIt2() {
		String inPath = "C:/@qpr/home/123620000036/calc/loyHead_MIX.txt";
		String outPath = "C:/@qpr/home/123620000036/calc/reordered.txt";

		Inf_ArrayCnv reorderIt = new ReorderIt("dummy,dummy,0,1,0,1");
		EzReader reader = new EzReader(inPath, reorderIt);
		new Common_IO(outPath, reader).execute();
	}

	public static void testReorderIt1() {
		String inPath = "C:/@qpr/home/123620000036/calc/loyHead_MIX.txt";
		String outPath = "C:/@qpr/home/123620000036/calc/reordered.txt";

		List<Integer> order = new ArrayList();
		order.add(-1);
		order.add(-1);
		order.add(-1);
		order.add(-1);
		order.add(0);
		order.add(1);
		order.add(99);
		Inf_ArrayCnv reorderIt = new ReorderIt(order);
		EzReader reader = new EzReader(inPath, reorderIt);
		new Common_IO(outPath, reader).execute();
	}

}
