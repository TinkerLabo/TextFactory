package kyPkg.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Divide Equally�F�����Z�����ߕs�������������čČ�������
public class DivideEQ {
	private HashMap<Integer, Integer> colMap;
	private String delimiter;
	private String marker;
	private List<Integer> keyList;

	// �J�����͂O����w�肷��
	public DivideEQ(HashMap<Integer, Integer> colMap, String delimiter,
			String marker) {
		super();
		this.colMap = colMap;
		keyList = new ArrayList(colMap.keySet());
		this.delimiter = delimiter;
		this.marker = marker;
	}

	public String divideIt(String val) {
		String[] array = val.split(delimiter);
		for (Integer col : keyList) {
			if (array.length > col) {
				array[col] = divIt(array[col], delimiter, marker, colMap
						.get(col));
			} else {
				// ���Y�J�����͑��݂��܂���̃G���[���b�Z�[�W�͕K�v���H
			}
		}
		StringBuffer buff = new StringBuffer();
		for (int idx = 0; idx < array.length; idx++) {
			buff.append(array[idx]);
			buff.append(delimiter);
		}
		buff.deleteCharAt(buff.length() - 1);// delimiter�͈ꕶ���Ƃ���i�������傩��char�ɂ���Ⴂ���񂶂�Ȃ��́H�j
		return buff.toString();
	}

	public static String divIt(String cel, String delimiter, String marker,
			int max) {
		String[] array2 = cel.split(marker);
		// �����ŉߕs���𒲐�����
		StringBuffer buf2 = new StringBuffer();
		if (buf2.length() > 0)
			buf2.delete(0, buf2.length() - 1);
		for (int idx = 0; idx < max; idx++) {
			if (array2.length > idx) {
				buf2.append(array2[idx]);
			}
			buf2.append(delimiter);
		}
		buf2.deleteCharAt(buf2.length() - 1);
		return buf2.toString();
	}

	public static void testDivIt() {
		String ans = divIt("a:b:c:d:e:", "@", ":", 5);
		System.out.println("ans:" + ans);
	}

	public static void main(String[] argv) {
		HashMap<Integer, Integer> colMap = new HashMap();
		colMap.put(2, 10);
		DivideEQ ins = new DivideEQ(colMap, ",", ":");
		String ans2 = ins.divideIt("001,002,a:b:c:d:e:,004,005,006");
		System.out.println("ans:" + ans2);
	}
}
