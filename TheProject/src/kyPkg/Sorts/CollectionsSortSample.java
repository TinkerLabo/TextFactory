package kyPkg.Sorts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Collections#Sort sample
 */
public class CollectionsSortSample implements Comparator<String[]> {
	private List<Comparator> list = null;

	public CollectionsSortSample(Comparator comparator) {
		super();
		this.list = new ArrayList();
		this.list.add(comparator);
	}

	public void addComparator(Comparator comparator) {
		this.list.add(comparator);
	}

	@Override
	public int compare(String[] o1, String[] o2) {
		int comp = 0;
		for (Comparator comparator : list) {
			if (comparator != null) {
				comp = comparator.compare(o1, o2);
				if (comp != 0)
					return comp;
			} else
				return 0;
		}
		return 0;
	}

	public static void test01() {
		List<String[]> arraylist = new ArrayList();
		java.util.Collections.sort(arraylist, new CollectionsSortSample(null));
	}

	public static void main(String[] argv) {
		test01();
	}
}
