package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;

//-----------------------------------------------------------------------------
//�w�肳�ꂽ�t�@�C�����������ɘA���o�͂���
//-----------------------------------------------------------------------------
public class ArrayMerge_org {
	private static final String DUMMY = "";
	private List<Integer> colsL = null;// �K�v�ȃJ����
	private List<Integer> colsR = null;// �K�v�ȃJ����
	private int bef_L = 0;// ��bef,aft��0����n�܂�C���f�b�N�Ŏw�肷��
	private int aft_L = 0;
	private int aft_R = 0;
	private int bef_R = 0;
	private String[] arrays_L = null;
	private String[] arrays_R = null;
	private List<String> mergedList;

	public List<String> getResultAsList() {
		return mergedList;
	}

	public String[] getResultAsArray() {
		String[] array = (String[]) mergedList.toArray(new String[mergedList
				.size()]);
		return array;
	}

	// ------------------------------------------------------------------------
	// �z�񂩂烊�X�g�ɕϊ�����
	// ------------------------------------------------------------------------
	private List array2List(Integer[] array) {
		return java.util.Arrays.asList(array);
	}

	public void setArray_L(Integer[] array_L) {
		this.colsL = array2List(array_L);
	}

	public void setArray_R(Integer[] array_R) {
		this.colsR = array2List(array_R);
	}

	public void setList_L(List<Integer> list_L) {
		this.colsL = list_L;
	}

	public void setList_R(List<Integer> list_R) {
		this.colsR = list_R;
	}

	public void setBef_L(int bef_L) {
		this.bef_L = bef_L;
	}

	public void setAft_L(int aft_L) {
		this.aft_L = aft_L;
	}

	public void setBef_R(int bef_R) {
		this.bef_R = bef_R;
	}

	public void setAft_R(int aft_R) {
		this.aft_R = aft_R;
	}

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public ArrayMerge_org(String[] arrays_L, String[] arrays_R) {
		this.arrays_L = arrays_L;
		this.arrays_R = arrays_R;
	}

	// ------------------------------------------------------------------------
	// �J�n�ʒu�I���ʒu������΁A����ɉ������΂��悤�J�����p�����[�^�����o��
	// �^�[�Q�b�g�J�������w�肳��Ă���ꍇ�͂����炪�D�悳���
	// ------------------------------------------------------------------------
	private List<Integer> parse(List<Integer> colList, int bef, int aft, int max) {
		if (colList != null)
			return colList;
		int from = 0;
		int to = 0;
		if (aft == 0)
			aft = max;
		if (bef < aft) {
			from = bef;
			to = aft;
		} else {
			from = aft;
			to = bef;
		}
		// System.out.println("##  from:" + from + " to:" + to);
		colList = new ArrayList();
		for (int i = from; i <= to; i++) {
			colList.add(i);
		}
		return colList;
	}

	public int execute() {
		mergedList = new ArrayList();
		// ------------------------------------------------------------------------
		// �w�肳�ꂽ�z��̒����Ɉˑ�����̂ł����őΏۃJ�����p�����[�^�𒲐�����
		// ------------------------------------------------------------------------
		colsL = parse(colsL, bef_L, aft_L, arrays_L.length - 1);
		colsR = parse(colsR, bef_R, aft_R, arrays_R.length - 1);
		// ------------------------------------------------------------
		// �����̔z��������o��
		// ------------------------------------------------------------
		for (Integer col : colsL) {
			if (arrays_L != null && arrays_L.length > col) {
				mergedList.add(arrays_L[col]);
			} else {
				mergedList.add(DUMMY);
			}
		}
		// ------------------------------------------------------------
		// �E���̔z��������o��
		// ------------------------------------------------------------
		for (Integer col : colsR) {
			if (arrays_R != null && arrays_R.length > col) {
				mergedList.add(arrays_R[col]);
			} else {
				mergedList.add(DUMMY);
			}
		}
		return 0;
	}

	// ------------------------------------------------------------------------
	// �ŏ��̃t�@�C���͂��ׂĂ̍��ڂ��o�͂����E�E
	// regex�@���Y�f�B���N�g���ȉ��̃p�^�[���Ƀ}�b�`����t�@�C���ꗗ��A������
	// modCol�@2�Ԗڂ̃t�@�C���́A���̃J�����ȍ~�̃J������A��
	// ------------------------------------------------------------------------
	public static String[] modByList(List<String[]> listArray, int skip) {
		List<String> list = null;
		if (listArray == null)
			return null;
		// �A������z�񂪂P�̏ꍇ
		if (listArray.size() == 1) {
			return listArray.get(0);
		} else {
			ArrayMerge_org merger = null;
			String[] arrays_L = null;
			String[] arrays_R = null;
			String[] prevPath = null;
			for (String[] element : listArray) {
				if (prevPath == null) {
					prevPath = element;
				} else {
					arrays_L = prevPath;
					arrays_R = element;
					merger = new ArrayMerge_org(arrays_L, arrays_R);
					merger.setBef_R(skip);
					merger.execute();
					prevPath = merger.getResultAsArray();
				}
			}
			list = merger.getResultAsList();
		}
		if (list == null)
			return null;
		String[] array = (String[]) list.toArray(new String[list.size()]);
		return array;
	}

	
	
	public List<String> listMerge(List<String> list_L, List<String> list_R, int skip) {
		int cnt = 0;
		List<String> result = new ArrayList();
		for (String element : list_L) {
			result.add(element);
		}
		for (String element : list_R) {
			cnt++;
			if (cnt > skip)
				result.add(element);
		}
		return null;
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		test00();
	}

	// ------------------------------------------------------------------------
	// Q �ǂ��炩�̃t�@�C���̃��R�[�h�������Ȃ��ꍇ�ǂ��Ȃ邩�˒����ق��ɍ��킹�ďo�͂����i�_�~�[�Z���o�͂���j
	// ���X�g�i�܂��͔z��j�̎w�肪�D��I�Ɏg�p�����i�J�n�I���J�����̎w��͖��������j
	// ------------------------------------------------------------------------
	// 20141107 �t�@�C������������bind����E�E�E
	// ------------------------------------------------------------------------
	public static void test00() {
		List<String[]> list = new ArrayList();
		list.add(new String[] { "A1", "A2", "A3" });
		list.add(new String[] { "B1", "B2", "B3", "B4", "B5", "B6", "B7" });
		list.add(new String[] { "C1", "C2", "C3", "C4", "C5" });
		int skip = 1;
		String[] result = ArrayMerge_org.modByList(list, skip);
		for (String element : result) {
			System.out.println("test00 element:" + element);
		}
	}
	public static void test01() {
		List<String[]> list = new ArrayList();
		list.add(new String[] { "A1", "A2", "A3" });
		list.add(new String[] { "B1", "B2", "B3", "B4", "B5", "B6", "B7" });
		list.add(new String[] { "C1", "C2", "C3", "C4", "C5" });
		int skip = 1;
		String[] result = ArrayMerge_org.modByList(list, skip);
		for (String element : result) {
			System.out.println("element:" + element);
		}
	}
}
