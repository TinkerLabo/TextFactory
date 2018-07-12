package kyPkg.gridModels;

import javax.swing.table.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

//-----------------------------------------------------------------------------
// 2007-07-10 ken yuasa
//-----------------------------------------------------------------------------
public class DefaultTableModelMod_org extends DefaultTableModel {
	private static final long serialVersionUID = 7644929846772436518L;
	// スプレッドシートの規約を使用して、列のデフォルト名 (A、B、C、...Z、AA、AB など) を返します。
	private boolean useDefaultName = true;

	public void setUseDefaultName(boolean useDefaultName) {
		this.useDefaultName = useDefaultName;
	}

	private Class colClass[];

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public DefaultTableModelMod_org() {
		this(array2Matrix(new Object[][] { { "" } }), Arrays
				.asList(new String[] { "" }));
	}

	public DefaultTableModelMod_org(List<List> matrix) {
		this(matrix, null);
	}

	public DefaultTableModelMod_org(List<List> matrix, List header) {
		this(matrix, header, -1);
	}

	public DefaultTableModelMod_org(List<List> matrix, List header, int limit) {
		super();

		// debug
		// String[][] tabledata = { { "日本", "3勝", "0敗", "1分" },
		// { "クロアチア", "3勝", "1敗", "0分" }, { "ブラジル", "1勝", "2敗", "1分" },
		// { "オーストラリア", "2勝", "2敗", "0分" } };
		// String[] columnNames = { "COUNTRY", "WIN", "LOST", "EVEN" };
		// DefaultTableModel debugModel = new
		// DefaultTableModel(tabledata,columnNames);
		// DefaultTableModel tModel = debugModel;

		columnIdentifiers = nonNullVector(header);
		dataVector = new Vector();
		if (matrix == null)
			return;
		if (limit < 0) {
			for (List rowObj : matrix) {
				dataVector.add(new Vector(rowObj));
			}
		} else {
			for (int i = 0; i < matrix.size(); i++) {
				if (i < limit)
					dataVector.add(new Vector(matrix.get(i)));
			}
		}

		// System.out.println("<### debug2013-01-29_1155 ###>");
		// for (Object obj : dataVector) {
		// if (obj instanceof Vector) {
		// Vector cols = (Vector) obj;
		// for (Object val : cols) {
		// System.out.print("@@<" + val);
		// }
		// System.out.println("@@>" );
		// }
		// }
		this.colClass = checkClasses(dataVector, columnIdentifiers.size());
		setDataVector(dataVector, columnIdentifiers);
	}

	private static Vector nonNullVector(List list) {
		return (list != null) ? new Vector(list) : new Vector();
	}

	
	
	
	
	@Override
	public void setDataVector(Vector dataVector, Vector columnIdentifiers) {
		this.useDefaultName = true;
		this.colClass = checkClasses(dataVector, columnIdentifiers.size());
		super.setDataVector(dataVector, columnIdentifiers);
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
		System.out.println("@chkColClassxx columnIdentifiers.size():" + maxCol);

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
			System.out.println("gClass[" + col + "]:" + wClass);
		}
		// }
		return colClass;
	}

	private static List<List> array2Matrix(Object[][] matrix) {
		List<List> rowlist = new ArrayList();
		for (int i = 0; i < matrix.length; i++) {
			Object[] rowArray = matrix[i];
			List collist = new ArrayList();
			for (int j = 0; j < rowArray.length; j++) {
				collist.add(rowArray[j]);
			}
			rowlist.add(collist);
		}
		return rowlist;
	}

}
// -------------------------------------------------------------------------
// ２次元のオブジェクト配列を２次元のベクターに変換する（一次元版も使用）
// <必須？！>
// -------------------------------------------------------------------------
// private static Vector array2Vector(Object[][] anArray) {
// if (anArray == null)
// return null;
// Vector v = new Vector(anArray.length);
// for (int i = 0; i < anArray.length; i++) {
// v.addElement(array2List(anArray[i]));
// }
// return v;
// }
// private static List array2List(Object[] array) {
// return Arrays.asList(array);
// }
// -------------------------------------------------------------------------
// セルの値をセット
// -------------------------------------------------------------------------
// public void setValueAt(Object aValue, int row, int column) {
// super.setValueAt(aValue, row, column);
// }

// -------------------------------------------------------------------------
// 《必須》 セルの値を返す。※範囲をチェックしなくてもいいのだろうか？
// -------------------------------------------------------------------------
// public Object getValueAt(int row, int column) {
// if (dataVector != null && row >= 0 && dataVector.size() > row) {
// Object rowObj = dataVector.get(row);
// if (rowObj instanceof Vector) {
// Vector rowList = (Vector) rowObj;
// if (rowList != null && column >= 0 && rowList.size() > column) {
// Object colObj = rowList.get(column);
// if (colObj != null)
// return colObj;
// }
// }
// }
// return "";
// }
// public Object getValueAt(int row, int column) {
// Vector rowVector = (Vector) dataVector.elementAt(row);
// return rowVector.elementAt(column);
// }
