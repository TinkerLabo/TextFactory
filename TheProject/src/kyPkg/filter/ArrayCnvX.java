package kyPkg.filter;

import kyPkg.converter.Inf_ArrayCnv;

public class ArrayCnvX implements Inf_ArrayCnv {
	private int[] targetCol;
	private String[] outrec = new String[1];

	// -------------------------------------------------------------------------
	// éwíËÇµÇΩÉJÉâÉÄÇÃÇ›ÇèoóÕÇ∑ÇÈ
	// -------------------------------------------------------------------------
	public ArrayCnvX() {
		this(new int[] { 0 });
	}

	public ArrayCnvX(int[] targetCol) {
		super();
		this.targetCol = targetCol;
		this.outrec = new String[this.targetCol.length];
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
		for (int col = 0; col < targetCol.length; col++) {
			outrec[col] = rec[targetCol[col]];
		}
		return outrec;
	}

	// ------------------------------------------------------------------------
	// fin
	// ------------------------------------------------------------------------
	@Override
	public void fin() {
	}

}
