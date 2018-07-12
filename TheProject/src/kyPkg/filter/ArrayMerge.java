package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;

//-----------------------------------------------------------------------------
//�w�肳�ꂽ�t�@�C�����������ɘA���o�͂���
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
	// �ŏ��̃t�@�C���͂��ׂĂ̍��ڂ��o�͂����E�E
	// regex�@���Y�f�B���N�g���ȉ��̃p�^�[���Ƀ}�b�`����t�@�C���ꗗ��A������
	// modCol�@2�Ԗڂ̃t�@�C���́A���̃J�����ȍ~�̃J������A��
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
	// �z�񂩂烊�X�g�ɕϊ�����
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
	// Q �ǂ��炩�̃t�@�C���̃��R�[�h�������Ȃ��ꍇ�ǂ��Ȃ邩�˒����ق��ɍ��킹�ďo�͂����i�_�~�[�Z���o�͂���j
	// ���X�g�i�܂��͔z��j�̎w�肪�D��I�Ɏg�p�����i�J�n�I���J�����̎w��͖��������j
	// ------------------------------------------------------------------------
	// 20141107 �t�@�C������������bind����E�E�E
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
