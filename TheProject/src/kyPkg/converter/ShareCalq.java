package kyPkg.converter;

import java.util.ArrayList;
import java.util.List;

import kyPkg.Sorts.MixIt;

public class ShareCalq extends ShareBasic {
	static final int KEY_COL = 1;
	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public ShareCalq(List<Integer> shareCellL) {
		super(KEY_COL, shareCellL);
	}
	// ------------------------------------------------------------------------
	// 実装
	// ------------------------------------------------------------------------
	@Override
	public String[] convert(String[] recs, int lineNumber) {
		if (motherMap == null)
			recs = super.incoreMotherVal(recs);// 一行目が総合計（トータル行）であるという前提・・・・
		if (shareCols != null && shareCols.size() > 0) {
			for (Integer intObj : shareCols) {
				recs = setShare(recs, intObj);
			}
		}
		return recs;
	}

	public static void testMixIt() {
		int shareCol = 4;//4
		List shareCellL = new ArrayList();
		if (shareCol > 0) {
			shareCellL.add(shareCol);// ｼｴｱ計算するセル位置（固定）
		}
//		Inf_LineConverter cnvRank = new ShareCalq(shareCellL);
		String bodyPath = "C:/@qpr/home/123620000049/calc/#004_MXD_PKG__000_2015年06月01日～30日_期間002.txt";
		List<String> iPaths = new ArrayList();
		iPaths.add("C:/@qpr/home/123620000049/calc/#003_MR2_PKG__000_2015年06月01日～30日_期間002_lev0.txt");
		iPaths.add("C:/@qpr/home/123620000049/calc/#003_MR2_PKG__000_2015年06月01日～30日_期間002_lev1.txt");
		MixIt ins = new MixIt(bodyPath, iPaths,shareCellL);
	}

	public static void main(String[] argv) {

	}
}
