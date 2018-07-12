package kyPkg.Sorts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

// これのリストバージョンおよびhashバージョンを作る
public class MatrixComparator_Str implements Comparator<List<String>> {

	private List<Comparator> comparators = null;

	public MatrixComparator_Str(String sortParam) {
		super();
		this.comparators = new ArrayList();
		parse(sortParam);
	}

	// ソートパラメータを解釈
	// ex　String sortParam = " 2,ch,Asc,5,int,desc";
	private void parse(String sortParam) {
		sortParam = sortParam.toLowerCase();
		String[] splited = sortParam.split(",");
		int max = splited.length / 3;
		for (int i = 0; i < max; i++) {
			String sCol = splited[(i * 3) + 0].trim();// 対象とするカラム
			int whichCol = Integer.parseInt(sCol);
			String sType = splited[(i * 3) + 1].trim().toLowerCase();
			String sOrder = splited[(i * 3) + 2].trim().toLowerCase();
			int order = 1; // 並び順
			if (sOrder.startsWith("d")) {
				order = -1; // -1は降順
			} else {
				order = 1; // 1なら昇順
			}
			// System.out.println("debug C:" + sCol + " T:" + sType + " o:" +
			// order);
			if (sType.startsWith("i")) {
				// ｉｎｔｅｇｅｒ
				addComparator(new CompInt(whichCol, order));
			} else if (sType.startsWith("d")) {
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
	public int compare(List<String> o1, List<String> o2) {
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
	// コンパレータクラス
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// String
	// ------------------------------------------------------------------------
	class CompStr implements Comparator<List<String>> {
		private int whichCol = 0; // このカラムの値を比較対象とする
		private int order = 1; // 1なら昇順-1なら降順

		public CompStr(int whichCol, int order) {
			super();
			this.whichCol = whichCol;
			this.order = order;
		}

		@Override
		public int compare(List<String> o1, List<String> o2) {
			String val1 = "";
			String val2 = "";
			if (o1.size() > whichCol)
				val1 = o1.get(whichCol);
			if (o2.size() > whichCol)
				val2 = o2.get(whichCol);
			return (val1.compareTo(val2)) * order;
		}
	}

	// ------------------------------------------------------------------------
	// Int
	// ------------------------------------------------------------------------
	class CompInt implements Comparator<List<String>> {
		private int whichCol = 0; // このカラムの値を比較対象とする
		private int order = 1; // 1なら昇順-1なら降順

		public CompInt(int whichCol, int order) {
			super();
			this.whichCol = whichCol;
			this.order = order;
		}

		@Override
		public int compare(List<String> o1, List<String> o2) {
			int val1 = cnvIntValue(o1, whichCol);
			int val2 = cnvIntValue(o2, whichCol);
			return (val1 - val2) * order;
		}

		private int cnvIntValue(List<String> list, int col) {
			int iValue = Integer.MIN_VALUE;
			if (list.size() > col) {
				String str = list.get(col);
				if (str == null || str.trim().equals(""))
					return iValue;
				try {
					iValue = Integer.valueOf(str);
				} catch (NumberFormatException e) {
					System.out.println("Exception" + e.toString());
					System.out.println("col:" + col);
					System.out.println("List<String>:" + str);
					return 0;
				}
			}
			return iValue;
		}

	}

	// ------------------------------------------------------------------------
	// Double
	// ------------------------------------------------------------------------
	class CompDouble implements Comparator<List<String>> {
		private int whichCol = 0; // このカラムの値を比較対象とする
		private int order = 1; // 1なら昇順-1なら降順

		public CompDouble(int whichCol, int order) {
			super();
			this.whichCol = whichCol;
			this.order = order;
		}

		@Override
		public int compare(List<String> o1, List<String> o2) {
			double val1 = cnvDoubleValue(o1, whichCol);
			double val2 = cnvDoubleValue(o2, whichCol);
			return (int) ((val1 - val2) * order);
		}

		private double cnvDoubleValue(List<String> list, int col) {
			double dValue = Double.MIN_VALUE;
			if (list.size() > col) {
				String str = list.get(col);
				if (str == null || str.trim().equals(""))
					return dValue;
				try {
					dValue = Double.valueOf(str);
				} catch (NumberFormatException e) {
					System.out.println("Exception" + e.toString());
					System.out.println("col:" + col);
					System.out.println("List<String>:" + str);
					return 0;
				}
			}
			return dValue;
		}
	}

	public static void main(String[] argv) {
		test_2011_1111();
	}

	// -------------------------------------------------------------------------
	// 配列をリストに書き換える
	// -------------------------------------------------------------------------
	public static List<String> array2List(String[] array) {
		return java.util.Arrays.asList(array);
	}

	// -------------------------------------------------------------------------
	// 配列をリストに書き換える(リスト内包版)
	// -------------------------------------------------------------------------
	public static List<List<String>> array2List(List<String[]> arrayList) {
		List<List<String>> list = new ArrayList();
		for (String[] array : arrayList) {
			list.add(java.util.Arrays.asList(array));
		}
		return list;
	}

	private static List<List<String>> createTestData() {
		List<String[]> arrayList = new ArrayList();
		arrayList.add("E,1,1".split(","));
		arrayList.add("D,2,2".split(","));
		arrayList.add("C,3,3".split(","));
		arrayList.add("B,4,4".split(","));
		arrayList.add("A,3,5".split(","));
		arrayList.add("A,2,6".split(","));
		arrayList.add("A,,".split(",", 3));
		return array2List(arrayList);
	}
	public static void test_2011_1111() {
		List<List<String>> strMatrix = createTestData();
		String sortParam = "0,String,A,1,int,D";
		Comparator<List<String>> comparator = new kyPkg.Sorts.MatrixComparator_Str(sortParam);
		Collections.sort(strMatrix, comparator);
		//確認
		for (List<String> row : strMatrix) {
			System.out.println("0:" + row.get(0) + " 1:" + row.get(1)
					+ " 2:" + row.get(2));
		}
	}
}
