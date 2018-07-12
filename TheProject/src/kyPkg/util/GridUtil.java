package kyPkg.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.table.TableModel;

public class GridUtil {
	// ------------------------------------------------------------------------
	// TableModel����}�g���b�N�X�����o��
	// ex) List<List<Object>>  matrix = kyPkg.util.GridUtil.cnvTableModel2Matrix(��Model)
	// ------------------------------------------------------------------------
	public static Vector<Vector> cnvTableModel2Vector(TableModel ��Model) {
		int rowCount = ��Model.getRowCount();
		int colCount = ��Model.getColumnCount();
		Vector<Vector> matrix = new Vector();
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
			Vector<Object> list = new Vector();
			for (int columnIndex = 0; columnIndex < colCount; columnIndex++) {
				list.add(��Model.getValueAt(rowIndex, columnIndex));
			}
			matrix.add(list);
		}
		return matrix;
	}
	public static List<List<Object>> cnvTableModel2Matrix(TableModel ��Model) {
		int rowCount = ��Model.getRowCount();
		int colCount = ��Model.getColumnCount();
		List<List<Object>> matrix = new ArrayList();
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
			List<Object> list = new ArrayList();
			for (int columnIndex = 0; columnIndex < colCount; columnIndex++) {
				list.add(��Model.getValueAt(rowIndex, columnIndex));
			}
			matrix.add(list);
		}
		return matrix;
	}

	// ------------------------------------------------------------------------
	// TableModel����}�g���b�N�X(List<String[]>)�����o��
	// �၄ List<String[]> matrix =
	// kyPkg.util.GridUtil.tModel2Matrix(grid.getModel());
	// ------------------------------------------------------------------------
	public static List<String[]> cnvTModel2ListOfArray(TableModel ��Model) {
		int rowCount = ��Model.getRowCount();
		int colCount = ��Model.getColumnCount();
		List<String[]> matrix = new ArrayList();
		for (int v = 0; v < rowCount; v++) {
			List<Object> recList = new ArrayList();
			// ��s����z��ɔ����o��
			for (int h = 0; h < colCount; h++) {
				Object obj = ��Model.getValueAt(v, h);
				if (obj != null) {
					recList.add(obj.toString());
				}
			}
			// ���X�g��z��ɕό`���ă}�g���b�N�X�ɒǉ�����
			if (recList.size() > 0) {
				String[] array = (String[]) recList.toArray(new String[recList
						.size()]);
				matrix.add(array);
			} else {
				System.out.println("#DEBUG#@GridUtil list.size():"
						+ recList.size());
			}
		}
		return matrix;
	}

	//�e�[�u�����f���̃f�[�^���}�g���b�N�X�Ƃ��Ď��o��
	public static Vector<Vector> cnvTModel2Vector(TableModel ��Model) {
		int rowCount = ��Model.getRowCount();
		int colCount = ��Model.getColumnCount();
		Vector<Vector> matrix = new Vector();
		for (int v = 0; v < rowCount; v++) {
			Vector recList = new Vector();
			for (int h = 0; h < colCount; h++) {
				recList.add(��Model.getValueAt(v, h));
			}
			matrix.add(recList);
		}
		return matrix;
	}
	public static List<List> cnvTModel2Matrix(TableModel ��Model) {
		int rowCount = ��Model.getRowCount();
		int colCount = ��Model.getColumnCount();
		List<List> matrix = new ArrayList();
		for (int v = 0; v < rowCount; v++) {
			List recList = new ArrayList();
			for (int h = 0; h < colCount; h++) {
				recList.add(��Model.getValueAt(v, h));
			}
			matrix.add(recList);
		}
		return matrix;
	}

	// �w�肳�ꂽ�J���������X�g�ŕԂ�
	public static List<String> cnvTModel2ListByCol(TableModel ��Model, int col) {
		int rowCount = ��Model.getRowCount();
		int colCount = ��Model.getColumnCount();
		List<String> list = new ArrayList();
		if (colCount <= col)
			return null;
		for (int v = 0; v < rowCount; v++) {
			Object obj = ��Model.getValueAt(v, col);
			list.add(obj.toString());
		}
		return list;
	}

	// �w�肳�ꂽ�J���������j�[�N�ȃ��X�g�ŕԂ�
	public static List<String> cnvTModel2UQList(TableModel ��Model, int col) {
		int rowCount = ��Model.getRowCount();
		int colCount = ��Model.getColumnCount();
		if (colCount <= col)
			return null;
		Set uqSet = new HashSet();
		for (int v = 0; v < rowCount; v++) {
			Object obj = ��Model.getValueAt(v, col);
			uqSet.add(obj.toString());
		}
		List<String> list = new ArrayList(uqSet);
		Collections.sort(list);
		return list;
	}

}
