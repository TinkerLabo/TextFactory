package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;

//-----------------------------------------------------------------------------
//指定されたファイルを横方向に連結出力する
//-----------------------------------------------------------------------------
public class ArrayMerge {
	public static List<String[]> getHalfofList(List<String[]> listArray,
			boolean aft) {
		List<String[]> listBef = new ArrayList();
		List<String[]> listAft = new ArrayList();
		int maxCol = listArray.size() / 2;
		for (String[] element : listArray) {
			if (listBef.size() < maxCol) {
				listBef.add(element);
			} else {
				listAft.add(element);
			}
		}
		if (aft) {
			return listAft;
		} else {
			return listBef;
		}
	}

	public static List<String> modByListHalf(List<String[]> listArray, int skip) {
		return modByList(getHalfofList(listArray, true), skip);
	}

	// ------------------------------------------------------------------------
	// 最初のファイルはすべての項目が出力される・・
	// regex　当該ディレクトリ以下のパターンにマッチするファイル一覧を連結する
	// modCol　2番目のファイルは、このカラム以降のカラムを連結
	// ------------------------------------------------------------------------
	public static List<String> modByList(List<String[]> listArray, int skip) {
		if (listArray == null)
			return null;
		List<String> list_L = null;
		List<String> list_R = null;
		List<String> merged = null;
		int prefix = 0;
		for (String[] array : listArray) {
			if (merged == null) {
				merged = prefix(array, prefix);
			}
			list_L = merged;
			list_R = array2List(array);
			merged = listMerge(list_L, list_R, skip);
		}
		if (merged == null)
			return null;
		return merged;
	}

	public static String[] list2Array(List<String> merged) {
		return (String[]) merged.toArray(new String[merged.size()]);
	}

	// ------------------------------------------------------------------------
	// 配列からリストに変換する
	// ------------------------------------------------------------------------
	private static List array2List(String[] array) {
		return java.util.Arrays.asList(array);
	}

	public static List<String> listMerge(String[] arrayL, String[] arrayR,
			int skip) {
		List<String> result = listMerge(array2List(arrayL), array2List(arrayR),
				skip);
		return result;
	}

	public static List<String> prefix(String[] array, int prefix) {
		List<String> result = new ArrayList();
		if (array != null) {
			if (array.length > prefix)
				result.add(array[prefix]);
		}
		return result;
	}

	public static List<String> listMerge(List<String> list_L,
			List<String> list_R, int skip) {
		int cnt = 0;
		List<String> result = new ArrayList();
		if (list_L != null) {
			for (String element : list_L) {
				result.add(element);
			}
		}
		if (list_R != null) {
			for (String element : list_R) {
				cnt++;
				if (cnt > skip)
					result.add(element);
			}
		}
		return result;
	}

	public static List<String> insertList(List<String> mother,
			List<String> chile, int targetCol) {
		List<String> result = new ArrayList<String>();
		int col = 0;
		for (String element : mother) {
			if (targetCol == col++) {
				for (String insert : chile) {
					result.add(insert);
				}
			}
			result.add(element);
		}
		return result;
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		// test00();
		test01();
	}

	// ------------------------------------------------------------------------
	// Q どちらかのファイルのレコード数が少ない場合どうなるか⇒長いほうに合わせて出力される（ダミーセル出力あり）
	// リスト（または配列）の指定が優先的に使用される（開始終了カラムの指定は無視される）
	// ------------------------------------------------------------------------
	// 20141107 ファイルを横方向にbindする・・・
	// ------------------------------------------------------------------------
	public static void test01() {
		List<String> mother = new ArrayList<String>();
		List<String> chile = new ArrayList<String>();
		mother.add("A");
		mother.add("B");
		mother.add("C");
		mother.add("D");
		mother.add("E");
		mother.add("F");
		mother.add("G");
		chile.add("a");
		chile.add("b");
		chile.add("c");
		chile.add("d");
		List<String> result = ArrayMerge.insertList(mother, chile, 2);
		for (String element : result) {
			System.out.println("element:" + element);
		}
	}

	public static void test00() {
		List<String[]> list = new ArrayList();
		list.add(new String[] { "A1", "A2", "A3" });
		list.add(new String[] { "B1", "B2", "B3", "B4", "B5", "B6", "B7" });
		list.add(new String[] { "C1", "C2", "C3", "C4", "C5" });
		int skip = 1;
		List<String> result = ArrayMerge.modByList(list, skip);
		for (String element : result) {
			System.out.println("element:" + element);
		}
	}
}
