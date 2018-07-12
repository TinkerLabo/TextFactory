package kyPkg.gridPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import kyPkg.converter.Inf_ArrayCnv;
import kyPkg.gridModels.DefaultTableModelMod;
import kyPkg.gridUtil.TModelUtil;
import kyPkg.gridUtil.Assign2Grid;
import kyPkg.gridUtil.TableSorter;
import kyPkg.uFile.File2Matrix;
import kyPkg.uFile.FileUtil;
import kyPkg.util.FontUtility;
import kyPkg.util.GridUtil;

public class Grid extends JTable {

	private int currentCol;
	private int currentRow;
	private Object currentVal;
	private Grid thisGrid;
	private HashMap<Integer, HashMap> toolTipDicts;

	private Vector<Vector> dataVector;

	public Vector<Vector> getDataVector() {
		return dataVector;
	}

	protected MouseListener mouseListener = null; // マウスリスナー

	private boolean sortable = true;
	private StripeTableRenderer tableRenderer;

	private ColorMapCtrlMod colorMapCtrl;

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public Grid(boolean reorderingAllowed) {
		super();
		thisGrid = this;

		// setFont(new Font("Courier", Font.PLAIN, 10));
		// FontUtility.setFont(new Font("Monospaced", Font.PLAIN, 12));
		FontUtility.setFont(new Font("Courier", Font.PLAIN, 10),false);

		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		setRowSelectionAllowed(true); // 行 選択

		// setColumnSelectionAllowed(true); // 列 選択
		// setCellSelectionEnabled(true); // セル選択
		setReorderingAllowed(reorderingAllowed);

		//        System.out.println("###< setDefaultRenderer >#################");
		// TableCellRenderer tableRenderer = new StripeTableRenderer();

		setBackground(Color.WHITE);

		tableRenderer = new StripeTableRenderer();
		setDefaultRenderer(Object.class, tableRenderer);
		setDefaultRenderer(Integer.class, tableRenderer);

		setAutoResizeMode(Grid.AUTO_RESIZE_OFF); // セル幅自動調整
		// setCellSelectionEnabled(true); // セル選択

		// getModel().addTableModelListener(new TableModelListener() {
		// // ------------------------------------------------------------------
		// // テーブルに変更が発生
		// // ------------------------------------------------------------------
		// public void tableChanged(TableModelEvent e) {
		// int row = e.getFirstRow();
		// int column = e.getColumn();
		// TableModel model = (TableModel) e.getSource();
		// // String columnName = model.getColumnName(column);
		// Object data = model.getValueAt(row, column);
		// System.out.print(" 行：" + row);
		// System.out.print(" 列：" + column);
		// System.out.println(" 変更値：" + data);
		// }
		// });
	}

	public void addToolTipDicts(Integer col, HashMap<String, String> dicts) {
		if (toolTipDicts == null) {
			toolTipDicts = new HashMap();
		}
		this.toolTipDicts.put(col, dicts);
	}

	private String toolTipTranslate(Integer col, String key) {
		// System.out.println("col:"+col+" key:"+key);
		if (toolTipDicts == null) {
			toolTipDicts = new HashMap();
		}
		HashMap<String, String> dict = toolTipDicts.get(col);
		if (dict != null) {
			// System.out.println(" dict != null ");
			// System.out.println(" dict.get(key)=> "+dict.get(key));
			return dict.get(key);
		}
		return null;
	}

	protected HashMap<String, String> metaMap = new HashMap();// メタデータ

	public HashMap<String, String> getInfoMap() {
		return metaMap;
	}

	public void setInfoMap(HashMap<String, String> metaMap) {
		this.metaMap = metaMap;
	}

	private HashMap<String, Object> memMap;

	public Object getMemo(String key) {
		if (memMap == null) {
			return null;
		}
		return memMap.get(key);
	}

	public void putMemo(String key, Object val) {
		if (memMap == null) {
			memMap = new HashMap();
		}
		memMap.put(key, val);
	}

	public boolean patternEq(String pattern) {
		String header = getHeader();
		if (header != null && header.equals(pattern)) {
			return true;
		}
		return false;
	}

	private String getHeader() {
		return (String) getMemo("header");
	}

	// private HashMap<String, Object> memMap;
	// private Object getMemo(String key) {
	// if (memMap == null)
	// return null;
	// return memMap.get(key);
	// }
	//
	// public void putMemo(String key, Object val) {
	// if (memMap == null)
	// memMap = new HashMap();
	// memMap.put(key, val);
	// }
	public ColorMapCtrlMod createColorMap(int count) {
		//        System.out.println("### createColorMap ###########################");
		colorMapCtrl = new ColorMapCtrlMod(this, 10);
		tableRenderer.setColorMapList(colorMapCtrl);
		return colorMapCtrl;
	}

	public void pushColor(String key, int column, Color color) {
		colorMapCtrl.pushColor(key, column, color);
	}

	public void popColor(String key, int column) {
		colorMapCtrl.popColor(key, column);
	}

	public void setValueAt(int row, int column, String val) {
		if (val == null) {
			val = "";
		}
		// 値に変更があれば色を変えるか？　カラム数文のカラーマップが必要　
		super.setValueAt(val, row, column);
	}

	// --------------------------------------------------------------------------
	// 選択解除
	// --------------------------------------------------------------------------
	@Override
	public void clearSelection() {
//		System.out.println("#20150930# clearSelection");
		super.clearSelection();// アクティブ範囲を解除（その部分のリペイントもされるが・・）
//		System.out.println("#20150930# repaint");
		super.repaint();// 確実にリペイントする（すっきりする）
//		System.out.println("#20150930# repaint end");
	}

	public String getString(int row, int col) {
		// System.out.println("@Grid_Panle getValue(" + row + "," + col + ")");
		int maxCol = getColumnCount();
		int maxRow = getRowCount();
		if (col >= maxCol || row >= maxRow) {
			return null;
		}
		Object obj = getValueAt(row, col);
		if (obj != null && obj instanceof String) {
			return (String) obj;
		} else {
			return null;
		}
	}

	public void setMouseListener(MouseListener mouseListener) {
		this.mouseListener = mouseListener;
	}

	// 指定したカラムの値をリストで返す
	public HashSet getSet(int col, boolean trim) {
		return new HashSet(getList(col, trim));
	}

	public Object getCurrentVal() {
		return currentVal;
	}

	public int getCurrentCol() {
		return currentCol;
	}

	public void setCurrentCol(int currentCol) {
		this.currentCol = currentCol;
	}

	public int getCurrentRow() {
		return currentRow;
	}

	public void setCurrentRow(int currentRow) {
		this.currentRow = currentRow;
	}

	// 列の順序を変えられない設定。=>false
	protected void setReorderingAllowed(boolean reorderingAllowed) {
		JTableHeader jTHead = getTableHeader();
		jTHead.setReorderingAllowed(reorderingAllowed);
	}

	public void resetColwidth(String param) {
		// System.out.println("debug0331@resetColwidth:" + param);
		String[] array = param.split(",");
		// カラム幅の調節 TableColumnModelが前提・・・・
		TableColumnModel columnModel = (DefaultTableColumnModel) getColumnModel();
		setVisible(false);
		TableColumn column = null;
		for (int i = 0; i < columnModel.getColumnCount(); i++) {
			column = columnModel.getColumn(i);
			int width = 10;
			if (array.length > i) {
				width = Integer.parseInt(array[i]);
			}

			column.setPreferredWidth(width);
		}
		setVisible(true);
		repaint();
	}

	// --------------- --------------- --------------- ---------------
	// private DefaultTableColumnModel getColumnModel() {
	// return (DefaultTableColumnModel) grid.getColumnModel();
	// }
	@Override
	public int getColumnCount() {
		// if (tableModel == null)
		// return -1;
		TableColumnModel columnModel = getColumnModel();
		if (columnModel == null) {
			return -1;
		}
		return columnModel.getColumnCount();
	}

	public void fixFit(int width) {
		// カラム幅の調節 TableColumnModelが前提・・・・
		TableColumnModel columnModel = getColumnModel();
		TableColumn column = null;
		for (int i = 0; i < columnModel.getColumnCount(); i++) {
			column = columnModel.getColumn(i);
			column.setPreferredWidth(width);
		}
		repaint();
	}

	public void resetColWidth(String param) {
		String[] array = param.split(",");
		// カラム幅の調節 TableColumnModelが前提・・・・
		TableColumnModel columnModel = getColumnModel();
		TableColumn column = null;
		for (int i = 0; i < columnModel.getColumnCount(); i++) {
			if (array.length > i) {
				int width = Integer.parseInt(array[i]);
				column = columnModel.getColumn(i);
				column.setPreferredWidth(width);
			}
		}
		repaint();
	}

	public void autoFit(int limit, Vector<Vector> dataList) {
		int defaultCount = 3;
		int baseSize = 7;// pointおよびフォントサイズによるだろう
		// カラXSム幅の調節 TableColumnModelが前提・・・・
		// DefaultTableColumnModel columnModel = getColumnModel();
		TableColumnModel columnModel = getColumnModel();
		// System.out.println("@autoFit limit:" + limit);
		if (dataList == null) {
			return;
		}
		int[] aWidth = new int[columnModel.getColumnCount()];
		for (int j = 0; j < aWidth.length; j++) {
			aWidth[j] = defaultCount;
		}
		Object obj;
		for (int i = 0; i < dataList.size(); i++) {
			Vector rList = dataList.get(i);
			if (rList != null) {
				for (int j = 0; j < rList.size(); j++) {
					obj = rList.get(j);
					if (obj != null) {
						int wLen = (obj.toString()).length();
						if (aWidth.length > j) { // 2009/04/08
							// System.out.println("val:" +
							// rList.get(j).toString()+" Len:" + wLen);
							if (aWidth[j] < wLen) {
								aWidth[j] = wLen;
							}
						}

					}
				}
			}
			if (i > limit) {
				i = dataList.size();
			}
		}
		TableColumn column = null;
		for (int j = 0; j < columnModel.getColumnCount(); j++) {
			column = columnModel.getColumn(j);
			// System.out.println("set width["+j+"]:" + aWidth[j]);
			column.setPreferredWidth(aWidth[j] * baseSize);
		}
		repaint();
	}

	public String getColName() {
		String[] colNames = getColNames();
		return colNames[currentCol];
	}

	// -----------------------------------------------------------------------------
	// getModel()を使用するメソッド群
	// -----------------------------------------------------------------------------
	public void saveAs(String wPath) {
		new FileUtil().tmdl2file(wPath, getModel(), true);
	}

	public void assign(List<String> headList, String path, Inf_ArrayCnv cnv) {
		Assign2Grid assign2grid = new Assign2Grid(thisGrid, headList, new File2Matrix(path, cnv));
		assign2grid.execute();
	}

	public int[] getColWidths(Graphics graphics) {
		return TModelUtil.getColWidths(graphics, getModel());
	}

	public String[] getColNames() {
		return TModelUtil.getColNames(getModel());
	}

	//表示されている行のカウントなので・・・
	public int getRowDataCount() {
		TableModel model = getModel();
		return model.getRowCount();
	}

	//	public int getDataCount() {
	//		return dataVector.size();
	//	}

	// クリックされた位置から、カラム、行、値を判定（設定）する
	public Object assignCurrent(Point point) {
		int col = columnAtPoint(point);
		int row = rowAtPoint(point);
		setCurrentCol(col);
		setCurrentRow(row);
		if (col < 0 || row < 0) {
			return null;
		}
		TableModel ｔModel = this.getModel();
		currentVal = ｔModel.getValueAt(row, col);
		// System.out.println("■ colName:" + getColName() + "　Col:" + col +
		// " Row:"
		// + row + " value→" + currentVal);
		return currentVal;
	}

	public List<Object> getList(int col, boolean trim) {
		TableModel model = this.getModel();
		int maxCol = model.getColumnCount();
		int maxRow = model.getRowCount();
		List list = new ArrayList();
		if (col > maxCol) {
			return list;
		}
		String str;
		for (int row = 0; row < maxRow; row++) {
			if (trim) {
				str = (String) model.getValueAt(row, col);
				// System.out.println("#debug@getList#:::::" + str);
				if (str != null) {
					// jancodeなど前ゼロの関係で＃がついている場合があり、これを除去している
					// また、短縮コードなどはコードにスペースがパデイングされている場合がある
					str = str.replace("#", "");
					list.add(str.trim());
				}

			} else {
				list.add(model.getValueAt(row, col));
			}
		}
		return list;
	}

	public boolean setData(HashMap<String, List<List>> map) {
		return setData(map.get("matrix"), map.get("headers"));
	}

	private boolean setData(List<List> matrix, List header) {
		dataVector = TModelUtil.matrix2Vector(matrix, -1);
		DefaultTableModelMod tableModel = new DefaultTableModelMod(dataVector,
				header);
		if (tableModel != null) {
			super.setModel(tableModel);
			return true;
		} else {
			return false;
		}
	}

	// 直接setModelをコールさせてはいけない
	public void setDefModel(DefaultTableModelMod tModel) {
		if (tModel == null) {
			return;
		}
		dataVector = tModel.getDataVector();
		if (sortable) {
			TableSorter sorter = new TableSorter(tModel);
			super.setModel(sorter);
			sorter.setTableHeader(getTableHeader());
		} else {
			super.setModel(tModel);
		}
		doLayout();
	}

	public List<String> getListByCol(int col) {
		if (col < 0) {
			col = this.getCurrentCol();
		}
		return GridUtil.cnvTModel2ListByCol(this.getModel(), col);
	}

	public List<List> getMatrix() {
		return GridUtil.cnvTModel2Matrix(this.getModel());
	}

	// getToolTipText()をオーバーライド
	@Override
	public String getToolTipText(MouseEvent evt) {
		// イベントからマウス位置を取得し、テーブル内のセルを割り出す
		// ここでキャストえくせぷしょんが起きるぜ
		try {
			int row = rowAtPoint(evt.getPoint());
			int col = columnAtPoint(evt.getPoint());
			String key = (String) getModel().getValueAt(row, col);
			return toolTipTranslate(col, key);
			// return "col:"+col+" key:"+key;
		} catch (java.lang.ClassCastException e) {
			return null;
		}
	}
}
