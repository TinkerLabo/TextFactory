package kyPkg.Sorts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// ����̃��X�g�o�[�W���������hash�o�[�W���������
public class ArrayComparator implements Comparator<String[]> {
	private List<Comparator> comparators = null;

	public ArrayComparator(String sortParam) {
		super();
		this.comparators = new ArrayList();
		parse(sortParam);
	}

	// �\�[�g�p�����[�^������
	// ex�@String sortParam = " 2,ch,Asc,5,int,desc";
	private void parse(String sortParam) {
		sortParam = sortParam.toLowerCase();
		String[] splited = sortParam.split(",");
		int max = splited.length / 3;
		for (int i = 0; i < max; i++) {
			String sCol = splited[(i * 3) + 0].trim();// �ΏۂƂ���J����
			int whichCol = Integer.parseInt(sCol);
			String sType = splited[(i * 3) + 1].trim().toLowerCase();
			String sOrder = splited[(i * 3) + 2].trim().toLowerCase();
			int order = 1; // ���я�
			if (sOrder.startsWith("d")) {// double
				order = -1; // -1�͍~��
			} else {
				order = 1; // 1�Ȃ珸��
			}
			// System.out.println("debug C:" + sCol + " T:" + sType + " o:" +
			// order);
			if (sType.startsWith("i")) {// integer
				// ��������������
				addComparator(new CompInt(whichCol, order));
			} else if (sType.startsWith("d")) {// double
				// double
				addComparator(new CompDouble(whichCol, order));
			} else {
				// String
				addComparator(new CompStr(whichCol, order));
			}
		}
	}

	public void addComparator(Comparator comparator) {
		this.comparators.add(comparator);
	}

	@Override
	public int compare(String[] o1, String[] o2) {
		int comp = 0;
		for (Iterator iter = comparators.iterator(); iter.hasNext();) {
			Comparator comparator = (Comparator) iter.next();
			comp = comparator.compare(o1, o2);
			if (comp != 0) {
				return comp;
			}
		}
		return 0;
	}

	// ------------------------------------------------------------------------
	// �R���p���[�^�N���X
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// String
	// "A" > "0" > " " �̏��ԂɂȂ��Ă���E�E�E�z�X�g�ł͐������p�����O�ɂȂ�
	// ------------------------------------------------------------------------
	class CompStr implements Comparator<String[]> {
		private int whichCol = 0; // ���̃J�����̒l���r�ΏۂƂ���
		private int order = 1; // 1�Ȃ珸��-1�Ȃ�~��

		public CompStr(int whichCol, int order) {
			super();
			this.whichCol = whichCol;
			this.order = order;
		}

		@Override
		public int compare(String[] o1, String[] o2) {
			String val1 = "";
			String val2 = "";
			if (o1.length > whichCol)
				val1 = o1[whichCol];
			if (o2.length > whichCol)
				val2 = o2[whichCol];
			return (val1.compareTo(val2)) * order;
		}
	}

	// ------------------------------------------------------------------------
	// Int
	// ------------------------------------------------------------------------
	class CompInt implements Comparator<String[]> {
		private int whichCol = 0; // ���̃J�����̒l���r�ΏۂƂ���
		private int order = 1; // 1�Ȃ珸��-1�Ȃ�~��

		public CompInt(int whichCol, int order) {
			super();
			this.whichCol = whichCol;
			this.order = order;
		}

		@Override
		public int compare(String[] o1, String[] o2) {
			int val1 = cnvIntValue(o1, whichCol);
			int val2 = cnvIntValue(o2, whichCol);
			if (val1 > val2) {
				return 1 * order;
			} else if (val1 < val2) {
				return -1 * order;
			} else {
				return 0;
			}
		}

		private int cnvIntValue(String[] array, int col) {
			int iValue = Integer.MIN_VALUE;
			// iValue=-100;
			// if(order<0)
			// iValue = Integer.MAX_VALUE;
			if (array.length > col) {
				if (array[col].trim().equals(""))
					return iValue;
				try {
					iValue = Integer.valueOf(array[col]);
				} catch (NumberFormatException e) {
					System.out.println("Exception" + e.toString());
					System.out.println("col:" + col);
					System.out.println("array[col]:" + array[col]);
					return 0;
				}
			}
			return iValue;
		}

	}

	// ------------------------------------------------------------------------
	// Double
	// ------------------------------------------------------------------------
	class CompDouble implements Comparator<String[]> {
		private int whichCol = 0; // ���̃J�����̒l���r�ΏۂƂ���
		private int order = 1; // 1�Ȃ珸��-1�Ȃ�~��

		public CompDouble(int whichCol, int order) {
			super();
			this.whichCol = whichCol;
			this.order = order;
		}

		@Override
		public int compare(String[] o1, String[] o2) {
			double val1 = cnvDoubleValue(o1, whichCol);
			double val2 = cnvDoubleValue(o2, whichCol);
			if (val1 > val2) {
				return 1 * order;
			} else if (val1 < val2) {
				return -1 * order;
			} else {
				return 0;
			}
			// double ans = ((val1 - val2) * order);
			// if (ans > 0) {
			// return 1;
			// } else if (ans < 0) {
			// return -1;
			// } else {
			// return 0;
			// }
		}

		private double cnvDoubleValue(String[] array, int col) {
			double dValue = Double.MIN_VALUE;
			// if(order>=0)
			// dValue = Double.MAX_VALUE;
			if (array.length > col) {
				if (array[col].trim().equals(""))
					return dValue;
				try {
					dValue = Double.valueOf(array[col]);

				} catch (NumberFormatException e) {
					System.out.println("Exception" + e.toString());
					System.out.println("col:" + col);
					System.out.println("array[col]:" + array[col]);
					return 0;
				}
			}
			return dValue;
		}
	}

	// ########################################################################
	// m a i n
	// ########################################################################
	public static void main(String[] argv) {
		// test_2011_1111();
		// test_20140304();
		test2();
	}

	// ----------------------------------------------------------------------------
	// �\�[�g�������̂��@hashMap<key,rank>�̃}�b�v�ɂ������E�E�E
	// �܂�A�\�[�g�ς݂̍��ڂɏ��ʕt�������ĉ��ʃ��x���̃��R�[�h�����ʋ敪�̏��ʂ��Q�Ƃ���̂Ɏg�p������
	// ----------------------------------------------------------------------------
	public static Map<String, Integer> list2RankMap(List<String[]> list) {
		// System.out.println("---------------------------------------");
		Map<String, Integer> rankMap = new HashMap();
		int ranking = 0;
		for (String[] array : list) {
			ranking++;
			// System.out.println("0:" + array[0] + " 1:" + array[1]);
			rankMap.put(array[0], ranking);
		}
		return rankMap;
	}

	public static void test2() {
		// new
		// String[]{"Coors","100"}�̑���ɁA<String,double>���^�v���Ƃ��Ă��N���X�𗘗p�ł���ƃp�t�H�[�}���X���҂���͂�
		// --------------------------------------------------------------------
		// test data
		// --------------------------------------------------------------------
		List<String[]> rankingList = new ArrayList();
		rankingList.add("SUNTRY,60.1".split(","));
		rankingList.add("ASAHI,990.02".split(","));
		rankingList.add("KIRIN,887.3".split(","));
		rankingList.add("EBISU,770.004".split(","));
		rankingList.add("Budweiser,5.5".split(","));
		rankingList.add("Red Stripe,0.0006".split(","));
		rankingList.add("LowenBrau,".split(",", 3));
		rankingList.add(new String[] { "Coors", "0.100" });
		// --------------------------------------------------------------------
		// 2�ځi�C���f�b�N�X���ƂP�j�̒l�ō~���\�[�g����
		// --------------------------------------------------------------------
		String sortParam = "1,double,desc";
		Comparator<String[]> comparator = new ArrayComparator(sortParam);
		Collections.sort(rankingList, comparator);

		// --------------------------------------------------------------------
		// �����N�t���i�\�[�g�j�������X�g����A���ʂ��Q�Ƃ���}�b�v��Ԃ�
		// --------------------------------------------------------------------
		Map<String, Integer> rankMap = list2RankMap(rankingList);

		// --------------------------------------------------------------------
		// �m�F
		// --------------------------------------------------------------------
		System.out.println("TESTaRRAYcOMPARATOR---------------------------------------");
		List<String> keyList = new ArrayList(rankMap.keySet());
		for (String key : keyList) {
			System.out.println("test2 key:" + key + " rank:" + rankMap.get(key));
		}
	}

	public static void test_20140304() {
		List<String[]> list = new ArrayList();
		list.add("E,1,0.1".split(","));
		list.add("D,2,0.02".split(","));
		list.add("C,3,1.3".split(","));
		list.add("B,4,0.004".split(","));
		list.add("A,3,5.5".split(","));
		list.add("A,2,0.0006".split(","));
		list.add("A,,".split(",", 3));
		String sortParam = "2,double,asc";
		Comparator<String[]> comparator = new ArrayComparator(sortParam);
		Collections.sort(list, comparator);
		for (String[] array : list) {
			System.out.println("0:" + array[0] + " 1:" + array[1] + " 2:"
					+ array[2]);
		}
	}

	public static void test_2011_1111() {
		List<String[]> list = new ArrayList();
		list.add("E,1,1".split(","));
		list.add("E,2,2".split(","));
		list.add("B,4,3".split(","));
		list.add("B,3,4".split(","));
		list.add("A,6,5".split(","));
		list.add("A,5,6".split(","));
		list.add("A,,".split(",", 3));

		String sortParam = "0,String,A,1,int,a";
		Comparator<String[]> comparator = new ArrayComparator(sortParam);
		Collections.sort(list, comparator);
		for (String[] array : list) {
			System.out.println("0:" + array[0] + " 1:" + array[1] + " 2:"
					+ array[2]);
		}

	}

}
