package kyPkg.Sorts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

//import java.util.Set;

// これのリストバージョンおよびhashバージョンを作る
public class MatrixComparator implements Comparator<List<Object>> {

	private List<Comparator> comparators = null;

	public MatrixComparator(String sortParam) {
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
			char type = 0;
			if (sType.startsWith("str")) {// String
				type = 1;
			} else if (sType.startsWith("i")) {// integer
				type = 2;
			} else {
				type = 0;
			}
			// instanceofで確認できるのでもはや型の指定は無視する
			String sOrder = splited[(i * 3) + 2].trim().toLowerCase();
			int order = 1; // 並び順
			if (sOrder.startsWith("d")) {
				order = -1; // -1は降順
			} else {
				order = 1; // 1なら昇順
			}
			addComparator(new CompObj(whichCol, type, order));
		}
	}

	public void addComparator(Comparator comparator) {
		this.comparators.add(comparator);
	}

	@Override
	public int compare(List<Object> o1, List<Object> o2) {
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
	// Object
	// ------------------------------------------------------------------------
	private class CompObj implements Comparator<List<Object>> {
		private int whichCol = 0; // このカラムの値を比較対象とする
		private char type = 0; // 型
		private int order = 1; // 1なら昇順-1なら降順

		public CompObj(int whichCol, char type, int order) {
			super();
			this.whichCol = whichCol;
			this.type = type;
			this.order = order;
		}

		// 毎回instanceofで型を聞くのはどうかな・・・どうしたらオーバーヘッドを回避できるか後で考えてみる(とりあえず動くものを作っておく)
		@Override
		public int compare(List<Object> o1, List<Object> o2) {
			Object obj1 = getColValue(o1, whichCol, type);
			Object obj2 = getColValue(o2, whichCol, type);
			if (obj1 instanceof String && obj2 instanceof String) {
				String val1 = (String) obj1;
				String val2 = (String) obj2;
				return (val1.compareTo(val2)) * order;
			} else if (obj1 instanceof Integer && obj2 instanceof Integer) {
				Integer val1 = (Integer) obj1;
				Integer val2 = (Integer) obj2;
				return (int) ((val1 - val2) * order);
			} else if (obj1 instanceof Long && obj2 instanceof Long) {
				Long val1 = (Long) obj1;
				Long val2 = (Long) obj2;
				return (int) ((val1 - val2) * order);
			} else if (obj1 instanceof Float && obj2 instanceof Float) {
				Float val1 = (Float) obj1;
				Float val2 = (Float) obj2;
				return (int) ((val1 - val2) * order);
			} else if (obj1 instanceof Double && obj2 instanceof Double) {
				Double val1 = (Double) obj1;
				Double val2 = (Double) obj2;
				return (int) ((val1 - val2) * order);
			} else if (obj1 instanceof Character && obj2 instanceof Character) {
				Character val1 = (Character) obj1;
				Character val2 = (Character) obj2;
				return (int) ((val1 - val2) * order);
			} else if (obj1 instanceof Byte && obj2 instanceof Byte) {
				Byte val1 = (Byte) obj1;
				Byte val2 = (Byte) obj2;
				return (int) ((val1 - val2) * order);
			} else if (obj1 instanceof Short && obj2 instanceof Short) {
				Short val1 = (Short) obj1;
				Short val2 = (Short) obj2;
				return (int) ((val1 - val2) * order);
			}
			return 0;
		}

		private Object cnvToInteger(Object obj) {
			if (obj instanceof String) {
				return Integer.valueOf((String) obj);
			} else if (obj instanceof Integer) {
				return obj;
			} else if (obj instanceof Long) {
				return Integer.valueOf(String.valueOf(obj));
			} else if (obj instanceof Float) {
				return ((Float) obj).intValue();
			} else if (obj instanceof Double) {
				return ((Double) obj).intValue();
			} else if (obj instanceof Character) {
				return Integer.valueOf(String.valueOf(obj));
			} else if (obj instanceof Byte) {
				return ((Byte) obj).intValue();
			} else if (obj instanceof Short) {
				return ((Short) obj).intValue();
			} else {
				return Integer.MIN_VALUE;
			}
		}

		private Object getColValue(List<Object> list, int col, char type) {
			if (list.size() <= col)
				return null;
			Object obj = list.get(col);
			switch (type) {
			case 1:// to String
				return String.valueOf(obj);
			case 2:// to Integer
				return cnvToInteger(obj);
			default:
				obj = list.get(col);
				return obj;
			}
		}

	}

	// -------------------------------------------------------------------------
	// 文字列配列をオブジェクトリストに書き換える
	// -------------------------------------------------------------------------
	public static List<Object> array2List(String[] array) {
		List<String> strList = java.util.Arrays.asList(array);
		List<Object> objList = new ArrayList();
		for (Object element : strList) {
			objList.add(element);
		}
		return objList;
	}

	public static List<Object> array2List(Integer[] array) {
		List<Integer> strList = java.util.Arrays.asList(array);
		List<Object> objList = new ArrayList();
		for (Object element : strList) {
			objList.add(element);
		}
		return objList;
	}

	// -------------------------------------------------------------------------
	// 配列をリストに書き換える(リスト内包版)
	// -------------------------------------------------------------------------
	public static List<List<Object>> array2objList(List<String[]> arrayList) {
		List<List<Object>> list = new ArrayList();
		for (String[] array : arrayList) {
			List<Object> objList = array2List(array);
			list.add(objList);
		}
		return list;
	}

	private static List<List<Object>> hmap2objListI(
			HashMap<String, Integer> hmap) {
		List<String> keyList = new ArrayList(hmap.keySet());
		List<List<Object>> list = new ArrayList();
		for (String key : keyList) {
			List<Object> rec = new ArrayList();
			rec.add(key);
			rec.add(hmap.get(key));
			list.add(rec);
		}
		return list;
	}

	public static List<List<Object>> hmap2objListD(HashMap<String, Double> hmap) {
		if (hmap == null)
			return null;
		List<String> keyList = new ArrayList(hmap.keySet());
		List<List<Object>> list = new ArrayList();
		for (String key : keyList) {
			List<Object> rec = new ArrayList();
			rec.add(key);
			rec.add(hmap.get(key));
			list.add(rec);
		}
		return list;
	}

	// ---------------------------------------------------------------------
	// ※ｈａｓｈマップの数値部分で降順ソートする
	// ＜文字、数値＞型のハッシュマップの数値の降順にソートした結果をリスト形式で返す
	// ---------------------------------------------------------------------
	public static List<List<Object>> hmap2sortedListI(
			HashMap<String, Integer> hmap) {
		List<List<Object>> list = hmap2objListI(hmap);
		if (list != null) {
			// 数値の降順＆文字の昇順
			String sortParam = "1,Integer,dsc,0,String,asc";
			Comparator<List<Object>> comparator = new MatrixComparator(
					sortParam);
			Collections.sort(list, comparator);
			return list;
		}
		return null;
	}

//	private static List<List<Object>> hmap2sortedListD(
//			HashMap<String, Double> hmap) {
//		List<List<Object>> list = hmap2objListD(hmap);
//		if (list != null) {
//			// 数値の降順＆文字の昇順
//			String sortParam = "1,Integer,dsc,0,String,asc";
//			Comparator<List<Object>> comparator = new MatrixComparator(
//					sortParam);
//			Collections.sort(list, comparator);
//			return list;
//		}
//		return null;
//	}

	public static List<List<Object>> objMatrixSort(List<List<Object>> list,String sortParam) {
		if (list != null) {
			// 数値の降順＆文字の昇順
			Comparator<List<Object>> comparator = new MatrixComparator(
					sortParam);
			Collections.sort(list, comparator);
			return list;
		}
		return null;
	}

	// ---------------------------------------------------------------------
	// ※ 上位ｎ件のキーだけ取り出す
	// List<List<Object>>の上位ｎ件のキーだけ取り出して,Set<String>で返す
	// ---------------------------------------------------------------------
	// public static Set<String> eliminateN(List<List<Object>> list, int n) {
	// Set<String> set = new HashSet();
	// if (list == null)
	// return null;
	// int cnt = 0;
	// for (List<Object> chile : list) {
	// cnt++;
	// if (cnt <= n) {
	// System.out.println(">> key:" + (String) chile.get(0) + " val:"
	// + (String) String.valueOf(chile.get(1)));
	// set.add((String) chile.get(0));
	// }
	// }
	// return set;
	// }

	// ---------------------------------------------------------------------
	// 何番目かを記録したマップを返す
	// ---------------------------------------------------------------------
	public static HashMap<String, Integer> eliminateNX(List<List<Object>> list,
			int n) {
		HashMap<String, Integer> rankMap = new HashMap();
		if (list == null)
			return null;
		int rank = 0;
		for (List<Object> chile : list) {
			rank++;
			if (rank <= n) {
				// System.out.println(">> key:" + (String) chile.get(0) +
				// " val:" + (String) String.valueOf(chile.get(1)));
				rankMap.put((String) chile.get(0), new Integer(rank));
			}
		}
		return rankMap;
	}

	// ---------------------------------------------------------------------
	// 何番目かを記録したマップを返す
	// ---------------------------------------------------------------------
	public static List<String> eliminateNR(List<List<Object>> list, int n) {
		List<String> rankList = new ArrayList();
		if (list == null)
			return null;
		int rank = 0;
		for (List<Object> chile : list) {
			rank++;
			if (rank <= n) {
				rankList.add((String) chile.get(0));
			}
		}
		return rankList;
	}

	public static void test_2011_1111_15() {
		// ---------------------------------------------------------------------
		// 脚きり処理（cardinalty調整）
		// ---------------------------------------------------------------------
		HashMap<String, Integer> cdtMap = new HashMap();
		cdtMap.put("Ea", new Integer(300));
		cdtMap.put("Aa", new Integer(700));
		cdtMap.put("Da", new Integer(400));
		cdtMap.put("Fa", new Integer(200));
		cdtMap.put("Ba", new Integer(600));
		cdtMap.put("Ga", new Integer(100));
		cdtMap.put("Ca", new Integer(500));
		cdtMap.put("Eb", new Integer(300));
		cdtMap.put("Ab", new Integer(700));
		cdtMap.put("Db", new Integer(400));
		cdtMap.put("Fb", new Integer(200));
		cdtMap.put("Bb", new Integer(600));
		cdtMap.put("Gb", new Integer(100));
		cdtMap.put("Cb", new Integer(500));
		// ---------------------------------------------------------------------
		// hmap => List<List<Object>> に変換する
		// ---------------------------------------------------------------------
		List<List<Object>> cdtList = MatrixComparator.hmap2sortedListI(cdtMap);
		MatrixComparator.eliminateNX(cdtList, 5);

		// set.contains(o)で調べて、Dictionaryから削除する・・・という考え
		// return set

	}

	private static List<List<Object>> createTestData() {
		List<String[]> arrayList = new ArrayList();
		arrayList.add("E,1,1".split(","));
		arrayList.add("D,2,2".split(","));
		arrayList.add("C,3,3".split(","));
		arrayList.add("B,4,4".split(","));
		arrayList.add("A,3,5".split(","));
		arrayList.add("A,2,6".split(","));
		arrayList.add("A,,".split(",", 3));
		return array2objList(arrayList);
	}

	// 使用例
	public static void test_2011_1111() {
		List<List<Object>> objMatrix = createTestData();
		String sortParam = "1,Integer,asc,0,String,asc";
		Comparator<List<Object>> comparator = new kyPkg.Sorts.MatrixComparator(
				sortParam);
		Collections.sort(objMatrix, comparator);
		// 確認
		for (List<Object> row : objMatrix) {
			System.out.println("0:" + row.get(0) + " 1:" + row.get(1) + " 2:"
					+ row.get(2));
		}
	}

	public static void main(String[] argv) {
		test_2011_1111_15();
	}

}
