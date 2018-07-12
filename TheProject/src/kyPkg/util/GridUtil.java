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
	// TableModelからマトリックスを取り出す
	// ex) List<List<Object>>  matrix = kyPkg.util.GridUtil.cnvTableModel2Matrix(ｔModel)
	// ------------------------------------------------------------------------
	public static Vector<Vector> cnvTableModel2Vector(TableModel ｔModel) {
		int rowCount = ｔModel.getRowCount();
		int colCount = ｔModel.getColumnCount();
		Vector<Vector> matrix = new Vector();
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
			Vector<Object> list = new Vector();
			for (int columnIndex = 0; columnIndex < colCount; columnIndex++) {
				list.add(ｔModel.getValueAt(rowIndex, columnIndex));
			}
			matrix.add(list);
		}
		return matrix;
	}
	public static List<List<Object>> cnvTableModel2Matrix(TableModel ｔModel) {
		int rowCount = ｔModel.getRowCount();
		int colCount = ｔModel.getColumnCount();
		List<List<Object>> matrix = new ArrayList();
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
			List<Object> list = new ArrayList();
			for (int columnIndex = 0; columnIndex < colCount; columnIndex++) {
				list.add(ｔModel.getValueAt(rowIndex, columnIndex));
			}
			matrix.add(list);
		}
		return matrix;
	}

	// ------------------------------------------------------------------------
	// TableModelからマトリックス(List<String[]>)を取り出す
	// 例＞ List<String[]> matrix =
	// kyPkg.util.GridUtil.tModel2Matrix(grid.getModel());
	// ------------------------------------------------------------------------
	public static List<String[]> cnvTModel2ListOfArray(TableModel ｔModel) {
		int rowCount = ｔModel.getRowCount();
		int colCount = ｔModel.getColumnCount();
		List<String[]> matrix = new ArrayList();
		for (int v = 0; v < rowCount; v++) {
			List<Object> recList = new ArrayList();
			// 一行分を配列に抜き出す
			for (int h = 0; h < colCount; h++) {
				Object obj = ｔModel.getValueAt(v, h);
				if (obj != null) {
					recList.add(obj.toString());
				}
			}
			// リストを配列に変形してマトリックスに追加する
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

	//テーブルモデルのデータをマトリックスとして取り出す
	public static Vector<Vector> cnvTModel2Vector(TableModel ｔModel) {
		int rowCount = ｔModel.getRowCount();
		int colCount = ｔModel.getColumnCount();
		Vector<Vector> matrix = new Vector();
		for (int v = 0; v < rowCount; v++) {
			Vector recList = new Vector();
			for (int h = 0; h < colCount; h++) {
				recList.add(ｔModel.getValueAt(v, h));
			}
			matrix.add(recList);
		}
		return matrix;
	}
	public static List<List> cnvTModel2Matrix(TableModel ｔModel) {
		int rowCount = ｔModel.getRowCount();
		int colCount = ｔModel.getColumnCount();
		List<List> matrix = new ArrayList();
		for (int v = 0; v < rowCount; v++) {
			List recList = new ArrayList();
			for (int h = 0; h < colCount; h++) {
				recList.add(ｔModel.getValueAt(v, h));
			}
			matrix.add(recList);
		}
		return matrix;
	}

	// 指定されたカラムをリストで返す
	public static List<String> cnvTModel2ListByCol(TableModel ｔModel, int col) {
		int rowCount = ｔModel.getRowCount();
		int colCount = ｔModel.getColumnCount();
		List<String> list = new ArrayList();
		if (colCount <= col)
			return null;
		for (int v = 0; v < rowCount; v++) {
			Object obj = ｔModel.getValueAt(v, col);
			list.add(obj.toString());
		}
		return list;
	}

	// 指定されたカラムをユニークなリストで返す
	public static List<String> cnvTModel2UQList(TableModel ｔModel, int col) {
		int rowCount = ｔModel.getRowCount();
		int colCount = ｔModel.getColumnCount();
		if (colCount <= col)
			return null;
		Set uqSet = new HashSet();
		for (int v = 0; v < rowCount; v++) {
			Object obj = ｔModel.getValueAt(v, col);
			uqSet.add(obj.toString());
		}
		List<String> list = new ArrayList(uqSet);
		Collections.sort(list);
		return list;
	}

}
