package kyPkg.filter;

import static kyPkg.etc.CalcUtil.calcShareS;

import kyPkg.converter.Inf_ArrayCnv;

public class Flt_VShareCalc implements Inf_ArrayCnv {
	private int[] targetCols;
	private int[] outputSeq;
	private int dot = 2;
	private Double[] mothers;

	// -------------------------------------------------------------------------
	// ��motherCol��ꐔ�ɂ���targetCols�̈ʒu�̒l�̉�����v�Z����
	// outputSeq�ɂ���ďo�͂���Z���A����яo�͏��𒲐�����
	// �p�����[�^�F
	// -------------------------------------------------------------------------
	public Flt_VShareCalc(int[] outputSeq, int[] targetCols) {
		super();
		this.targetCols = targetCols;
		this.outputSeq = outputSeq;
		System.out.println("# Flt_ShareCalc # ");
	}

	// ------------------------------------------------------------------------
	// init
	// ------------------------------------------------------------------------
	@Override
	public void init() {
		System.out.println("# init # ");
	}

	// ------------------------------------------------------------------------
	// Convert
	// ------------------------------------------------------------------------
	@Override
	public String[] convert(String[] rec, int stat) {
		// �擪�����v�s�Ƃ݂Ȃ�
		if (mothers == null) {
			mothers = new Double[rec.length];
			for (int target : targetCols) {
				mothers[target] = 0.0;
				try {
					mothers[target] = Double.parseDouble(rec[target]);
				} catch (Exception e) {
				}
			}
		}
		// System.out.print("# convert # ");
		// �w�肳�ꂽ�J�����̒l��ꐔ�ɃV�F�A���v�Z���Č��̂���ɖ߂��Ă���
		// System.out.println("## mother:" + mother);
		for (int target : targetCols) {
			Double tval = 0.0;
			try {
				tval = Double.parseDouble(rec[target]);
			} catch (Exception e) {
			}
			String share = calcShareS(tval, mothers[target], dot);
			// System.out.print(" <" + target + "> "+ share);
			// System.out.print(" <" + target + "> val:" + tval +
			// " => share:"+ share);
			rec[target] = share;
		}
		// �o�͎w��Z���̂ݏo�͂���

		// String[] outrec = new String[rec.length];
		String[] outrec = new String[outputSeq.length];
		for (int col : outputSeq) {
			outrec[col] = rec[col];
		}
		// System.out.println("debugdebug rec.length   :"+rec.length);
		// System.out.println("debugdebug outrec.length:"+outrec.length);
		return outrec;
	}

	// ------------------------------------------------------------------------
	// fin
	// ------------------------------------------------------------------------
	@Override
	public void fin() {
		System.out.println("# fin # ");
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		testForHVShare();
	}

	// -------------------------------------------------------------------------
	// �P�̃e�X�g�@
	// -------------------------------------------------------------------------
	public static void testForHVShare() {
		String sortedPath = "C:/@qpr/home/123620000036/calc/sorted.txt";
		String outPath = "C:/@qpr/home/123620000036/calc/VShare.txt";
		int[] targetCols = new int[] { 1, 2, 3, 4, 5, 6, 7, 8 };
		int[] outSeq = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
		Inf_ArrayCnv cnv = new Flt_VShareCalc(outSeq, targetCols);
		new Common_IO(outPath, new EzReader(sortedPath, cnv)).execute();
	}
}
