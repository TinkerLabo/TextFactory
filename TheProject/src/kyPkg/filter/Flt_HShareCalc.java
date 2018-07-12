package kyPkg.filter;

import static kyPkg.etc.CalcUtil.calcShareS;

import kyPkg.converter.Inf_ArrayCnv;

public class Flt_HShareCalc implements Inf_ArrayCnv {
	private int motherCol = 1;
	private int[] targetCols;
	private int[] outputSeq;
	private int dot = 2;

	// private int outMax;

	// -------------------------------------------------------------------------
	// ※motherColを母数にしてtargetColsの位置の値の横比を計算する
	// outputSeqによって出力するセル、および出力順を調整する
	// パラメータ：
	// -------------------------------------------------------------------------
	public Flt_HShareCalc(int[] outputSeq, int[] targetCols, int motherCol) {
		super();
		this.motherCol = motherCol;
		this.targetCols = targetCols;
		this.outputSeq = outputSeq;

		// outMax = outputSeq.length;
		// outMax = -1;
		// for (int seq : outputSeq) {
		// if(outMax<seq)outMax=seq;
		// }
		// System.out.println("# Flt_ShareCalc # ");
	}

	// ------------------------------------------------------------------------
	// init
	// ------------------------------------------------------------------------
	@Override
	public void init() {
		// System.out.println("# init # ");
	}

	// ------------------------------------------------------------------------
	// Convert
	// ------------------------------------------------------------------------
	@Override
	public String[] convert(String[] rec, int stat) {
		// System.out.print("# convert # ");
		// 指定されたカラムの値を母数にシェアを計算して元のせるに戻している
		if (rec.length > motherCol) {
			Double mother = Double.parseDouble(rec[motherCol]);
			// System.out.println("## mother:" + mother);
			for (int target : targetCols) {
				Double tval = 0.0;
				try {
					tval = Double.parseDouble(rec[target]);
				} catch (Exception e) {
				}
				String share = calcShareS(tval, mother, dot);
				// System.out.print(" <" + target + "> "+ share);
				// System.out.print(" <" + target + "> val:" + tval +
				// " => share:"+ share);
				rec[target] = share;
			}
		}
		// 出力指定セルのみ出力する
		// String[] outrec = new String[rec.length];
		String[] outrec = new String[outputSeq.length];
		for (int col : outputSeq) {
			outrec[col] = rec[col];
		}
		// System.out.println("");
		return outrec;
	}

	// ------------------------------------------------------------------------
	// fin
	// ------------------------------------------------------------------------
	@Override
	public void fin() {
		// System.out.println("# fin # ");

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
		String inPath = "C:/@qpr/home/123620000036/calc/sorted.txt";
		String outPath = "C:/@qpr/home/123620000036/calc/HShare.txt";
		int motherCol = 1;
		int[] targetCols = new int[] { 1, 2, 3, 4, 5, 6, 7, 8 };
		int[] outSeq = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
		Inf_ArrayCnv cnv = new Flt_HShareCalc(outSeq, targetCols, motherCol);
		EzReader reader = new EzReader(inPath, cnv);
		new Common_IO(outPath, reader).execute();
	}
}
