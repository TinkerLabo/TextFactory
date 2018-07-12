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
			// �{�N�̒l�������̂Ŗ������H
			recR = join(recRs, delimiter);
			result = "R:" + recR;
			break;
		case -1:// L < R
			// �O�N�̒l�������ꍇ����
			recL = join(recLs, delimiter);
			result = "L:" + recL;
			break;
		default:// L == R
			// �ΑO�N����
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
