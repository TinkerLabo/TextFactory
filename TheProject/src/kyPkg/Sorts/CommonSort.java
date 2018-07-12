package kyPkg.Sorts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CommonSort {
	public static void tester01() {
		List<String> list = new ArrayList();
		list.add("Julia");
		list.add("Getting Better");
		list.add("Drive My Car");
		list.add("Fool On The Hill ,The");
		list.add("Here, There And Everywhere");
		list.add("Blackbird");
		list.add("Across The Universe");
		list.add("Eleanor Rigby");
		list.add("Kansas City");
		list.add("I Will");
		list.add("Come Together");
		kyPkg.Sorts.CommonSort.sortIt(list, -1);// 降順ソート
		for (String value : list) {
			System.out.println(value);
		}
	}

	public static void main(String[] args) {
		tester01();
	}

	// sortIt　汎用ソート -1:降順ソート,1昇順ソート
	// kyPkg.Sorts.CommonSort.sortIt(list, order);
	public static List sortIt(List list, int order) {
		Comparator comparator = null;
		if (order < 0) {
			comparator = new StringComparator(StringComparator.DESC);
		} else {
			comparator = new StringComparator(StringComparator.ASC);
		}
		Collections.sort(list, comparator);
		return list;
	}

}

// Comparator実装クラス
class StringComparator implements Comparator {
	public static final int ASC = 1; // 昇順
	public static final int DESC = -1; // 降順
	private int direction = ASC; // デフォルトは昇順

	public StringComparator() {
	}

	/**
	 * @param sort
	 *            StringComparator.ASC | StringComparator.DESC。昇順や降順を指定します。
	 */
	public StringComparator(int sort) {
		if (sort == DESC) {
			this.direction = DESC;
		} else {
			this.direction = ASC;
		}
	}

	@Override
	public int compare(Object arg0, Object arg1) {
		if (!(arg0 instanceof Comparable) || !(arg1 instanceof Comparable)) {
			throw new IllegalArgumentException(
					"arg0 & arg1 must implements interface of java.lang.Comparable.");
		}
		if (arg0 == null && arg1 == null) {
			return 0; // arg0 = arg1
		} else if (arg0 == null) {
			return 1 * direction; // arg1 > arg2
		} else if (arg1 == null) {
			return -1 * direction; // arg1 < arg2
		}
		return ((Comparable) arg0).compareTo((Comparable) arg1) * direction;
	}

}