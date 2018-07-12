package kyPkg.converter;

import java.util.ArrayList;
import java.util.List;

import kyPkg.Sorts.MixIt;

public class ShareCalq extends ShareBasic {
	static final int KEY_COL = 1;
	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public ShareCalq(List<Integer> shareCellL) {
		super(KEY_COL, shareCellL);
	}
	// ------------------------------------------------------------------------
	// ����
	// ------------------------------------------------------------------------
	@Override
	public String[] convert(String[] recs, int lineNumber) {
		if (motherMap == null)
			recs = super.incoreMotherVal(recs);// ��s�ڂ������v�i�g�[�^���s�j�ł���Ƃ����O��E�E�E�E
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
			shareCellL.add(shareCol);// ����v�Z����Z���ʒu�i�Œ�j
		}
//		Inf_LineConverter cnvRank = new ShareCalq(shareCellL);
		String bodyPath = "C:/@qpr/home/123620000049/calc/#004_MXD_PKG__000_2015�N06��01���`30��_����002.txt";
		List<String> iPaths = new ArrayList();
		iPaths.add("C:/@qpr/home/123620000049/calc/#003_MR2_PKG__000_2015�N06��01���`30��_����002_lev0.txt");
		iPaths.add("C:/@qpr/home/123620000049/calc/#003_MR2_PKG__000_2015�N06��01���`30��_����002_lev1.txt");
		MixIt ins = new MixIt(bodyPath, iPaths,shareCellL);
	}

	public static void main(String[] argv) {

	}
}
