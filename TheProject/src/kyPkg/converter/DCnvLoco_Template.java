package kyPkg.converter;

import static kyPkg.util.Joint.join;

import kyPkg.filter.Inf_DualConverter;

public class DCnvLoco_Template implements Inf_DualConverter {
	String delimiter = "\t";

	@Override
	public String convert(String[] recLs, String[] recRs, int stat) {
		String recL;
		String recR;
		String result;
		switch (stat) {
		case 1:// L > R
			// 本年の値が無いので無視か？
			recR = join(recRs, delimiter);
			result = "R:" + recR;
			break;
		case -1:// L < R
			// 前年の値が無い場合処理
			recL = join(recLs, delimiter);
			result = "L:" + recL;
			break;
		default:// L == R
			// 対前年処理
			recL = join(recLs, delimiter);
			recR = join(recRs, delimiter);
			result = "=:" + recL + " recR:" + recR;
			break;
		}
		System.out.println(result);
		return result;
	}

	@Override
	public void init() {
	}
	@Override
	public void fin() {
	};

}
