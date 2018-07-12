package kyPkg.gridModels;

import javax.swing.table.*;

//import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

//-----------------------------------------------------------------------------
//2007-07-10 ken yuasa
//-----------------------------------------------------------------------------
public class DefaultTableModelMod extends DefaultTableModel {
	// private Class colClass[];
	private boolean useDefaultName = true;
	// private boolean headerOption = false;
	private Class colClass[];

	public void setUseDefaultName(boolean useDefaultName) {
		this.useDefaultName = useDefaultName;
	}

	// -----------------------------------------------------------------------------
	// コンストラクタ
	// -----------------------------------------------------------------------------
	public DefaultTableModelMod() {
		super();
	}

	public DefaultTableModelMod(Vector<Vector> dataVector) {
		this(dataVector, createHeaderFromVector(dataVector), -1);
	}

	public DefaultTableModelMod(Vector<Vector> dataVector, List headerList) {
		this(dataVector, headerList, -1);
	}

	public DefaultTableModelMod(Vector<Vector> dataVector, String header,
			int limit) {
		this(dataVector, Arrays.asList(header.split(",")), limit);
	}

	public DefaultTableModelMod(Vector<Vector> dataVector, List headerList,
			int limit) {
		super();
		setDataVector(dataVector, headerList);
	}

	// @Override
	private void setDataVector(Vector<Vector> matrix, List headerList) {
		if (matrix == null || matrix.size() == 0)
			return;
		this.dataVector = matrix;
		// Vector dataVector = matrix2Vector(matrix,limit);
		if (headerList == null)
			headerList = createHeaderFromVector(dataVector);
		if (headerList == null)
			return;
		columnIdentifiers = new Vector(headerList);
		this.useDefaultName = true;
		this.colClass = checkClasses(dataVector, columnIdentifiers.size());
		super.setDataVector(dataVector, columnIdentifiers);
	}

	// public void setDataVector(Vector dataVector, Vector columnIdentifiers) {
	// super.setDataVector(dataVector, columnIdentifiers);
	// }

	private static Vector createHeaderFromVector(Vector dataVec) {
		boolean headerOption = false;
		if (dataVec == null || dataVec.size() == 0)
			return null;
		System.out.println("debug   vec.size=>" + dataVec.size());
		Vector heads = new Vector();
		Object obj = dataVec.get(0);
		if (obj instanceof List) {
			List list = (List) obj;
			for (int i = 0; i < list.size(); i++) {
				if (headerOption) {
					heads.add(list.get(i));
				} else {
					heads.add(String.valueOf(i + 1));
				}
			}
		} else {
			if (headerOption) {
				heads.add(obj);
			} else {
				heads.add(String.valueOf(1));
			}
		}
		return heads;
	}

	// -------------------------------------------------------------------------
	// 指定された位置にある列の名前を返す。
	// ※スーパークラスはどんな名前を返すんだい？？
	// -------------------------------------------------------------------------
	@Override
	public String getColumnName(int column) {
		if (this.useDefaultName == false)
			return Integer.toString(column + 1);
		return super.getColumnName(column);
	}

	// -------------------------------------------------------------------------
	// セルが編集可能かどうかを設定
	// -------------------------------------------------------------------------
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	// #########################################################################
	// -------------------------------------------------------------------------
	// 列のセル値の最も明確なスーパークラスを返します。
	// DEFAULTではオブジェクトクラスが返される、当該クラスに変更することにより
	// そのクラスの標準レンダラーおよびエディタが選択される
	// (EX BOOLならCheckBoxが表示されるナド)
	// ※コメントアウトにより動作確認されたし
	// ---------------------------------------------<インタフェース TableModel>-
	@Override
	@SuppressWarnings("unchecked")
	public Class getColumnClass(int columnIndex) {
		Class dataType;
		if (colClass == null) {
			dataType = super.getColumnClass(columnIndex);
		} else {
			if (colClass.length > columnIndex) {
				dataType = colClass[columnIndex];
			} else {
				dataType = super.getColumnClass(columnIndex);
				// dataType = "".getClass();
			}
		}
		return dataType;
	}
	// -------------------------------------------------------------------------
	// 各列のクラスを配列に格納しておく・・処理を軽くするため
	// -------------------------------------------------------------------------
	private Class[] checkClasses(Vector<Vector> vMatrix, int maxCol) {
		Class[] colClass = null;
		if (vMatrix == null)
			return null;
		// if (columnIdentifiers != null) {
		// int maxCol = columnIdentifiers.size();
		// System.out.println("@chkColClassxx columnIdentifiers.size():" +
		// maxCol);
		colClass = new Class[maxCol];
		for (int col = 0; col < maxCol; col++) {
			Class wClass = null;
			for (int row = 0; (wClass == null && row < vMatrix.size()); row++) {
				Vector rowData = vMatrix.get(row);
				try {
					if (rowData.size() > col) {
						Object obj = rowData.get(col);
						if (obj != null)
							wClass = obj.getClass();
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print("#Error @checkClasses Index:" + col);
					wClass = Object.class;
				}
			}
			if (wClass == null)
				wClass = new String().getClass();
			colClass[col] = wClass;
			// System.out.println("gClass[" + col + "]:" + wClass);
		}
		// }
		return colClass;
	}
	// private static List<List> array2Matrix(Object[][] matrix) {
	// List<List> rowlist = new ArrayList();
	// for (int i = 0; i < matrix.length; i++) {
	// Object[] rowArray = matrix[i];
	// List collist = new ArrayList();
	// for (int j = 0; j < rowArray.length; j++) {
	// collist.add(rowArray[j]);
	// }
	// rowlist.add(collist);
	// }
	// return rowlist;
	// }
	// public DefaultTableModelMod(int arg0, int arg1) {
	// super(arg0, arg1);
	// }
	// public DefaultTableModelMod(Object[] arg0, int arg1) {
	// super(arg0, arg1);
	// }
	// public DefaultTableModelMod(Object[][] arg0, Object[] arg1) {
	// super(arg0, arg1);
	// }
	// public DefaultTableModelMod(List dataList) {
	// super();
	// setDataVector(dataList, null, -1);
	// }
	// public DefaultTableModelMod(List dataList, List headerList) {
	// super();
	// setDataVector(dataList, headerList, -1);
	// }
	// public DefaultTableModelMod(List dataList, String header) {
	// super();
	// setDataVector(dataList, Arrays.asList(header.split(",")), -1);
	// }
	// public DefaultTableModelMod(List dataList, List headerList, int limit) {
	// super();
	// setDataVector(dataList, headerList, limit);
	// }
	// private void setDataVector(List matrix, List headerList, int limit) {
	// if (matrix == null || matrix.size() == 0)
	// return;
	// Vector dataVector = matrix2Vector(matrix, limit);
	// if (headerList == null)
	// headerList = getHeads(dataVector);
	// if (headerList == null)
	// return;
	// columnIdentifiers = new Vector(headerList);
	// setDataVector(dataVector, columnIdentifiers);
	// }
	// private static Vector<Vector> matrix2Vector(List matrix, long limit) {
	// long cnt = 0;
	// if (limit <= 0)
	// limit = Long.MAX_VALUE;
	// Vector dataVector = new Vector();
	// for (Object rowObj : matrix) {
	// cnt++;
	// if (cnt > limit)
	// break;
	// if (rowObj instanceof List) {
	// dataVector.add(new Vector((List) rowObj));
	// } else {
	// dataVector.add(rowObj);
	// }
	// }
	// return dataVector;
	// }
}
