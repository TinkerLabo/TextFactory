package kyPkg.gridUtil;

import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import kyPkg.converter.Inf_ArrayCnv;
import kyPkg.gridModels.DefaultTableModelMod;
import kyPkg.gridPanel.Grid;
import kyPkg.uFile.File2Matrix;


public class TableModelUtil_NotUsed {
	private Grid grid;
	private String path;
	private String[] heads;
	private Inf_ArrayCnv cnv;

	private int[] colWidths;
	private String[] colNames;
	private Vector<Vector> dataVector;

	// public Vector<Vector> getDataVector() {
	// return dataVector;
	// }

	private Vector columnIdentifiers;
	private DefaultTableModelMod tableModel;

	// public DefaultTableModelMod getTableModel() {
	// return tableModel;
	// }

	public int[] getColWidths() {
		return colWidths;
	}

	public String[] getColNames() {
		return colNames;
	}

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	public TableModelUtil_NotUsed(Grid grid, String[] heads, String path,
			Inf_ArrayCnv cnv) {
		super();
		this.grid = grid;
		this.heads = heads;
		this.path = path;
		this.cnv = cnv;
	}

	public void setTableModel(Grid grid, DefaultTableModel tableModel) {

		TableSorter sorter = new TableSorter(tableModel);
		grid.setModel(sorter);
		sorter.setTableHeader(grid.getTableHeader());
	}

	public void execute() {
		System.out.println("###<execute>###");
		DefaultTableModel tableModel = createTmodelFromFile(path, heads, cnv);
		// -------------------------------------------------------------
		// jTableにtableModelをひもづけ
		// -------------------------------------------------------------
		setTableModel(grid, tableModel);

		FontMetrics fom = grid.getGraphics().getFontMetrics();

		colNames = getColNames(tableModel);
		colWidths = getColWidths(fom, tableModel, 10);

		adjustColWidth(grid, colNames, colWidths);
		// テーブルの内容は編集可能とするのか、それとも不可とするのか？
		// 数値項目は右寄せとするのか、どういう場合を数値項目と判定するのか？
	}

	// ------------------------------------------------------------------------
	// グリッドにデータをセットする（グリッドの幅も調整する）
	// ------------------------------------------------------------------------
	private DefaultTableModel createTmodelFromFile(String path, String[] head,
			Inf_ArrayCnv cnv) {
		List<String> headList = new ArrayList();
		if (head != null)
			headList = Arrays.asList(head);

		dataVector = File2Matrix.extract(path, cnv);
		if (dataVector.size() > 0) {
			Vector row = dataVector.get(0);
			// ヘッダーが存在しなかった場合先頭行のカラムを数えて仮のヘッダーを作る
			if (headList.size() == 0 && row != null) {
				for (int i = 1; i <= row.size(); i++)
					headList.add("Cel_" + i);
			}
		}

		tableModel = new DefaultTableModelMod();

		columnIdentifiers = new Vector();
		for (String element : headList) {
			columnIdentifiers.add(element);
		}
		// System.out.println("### @DefaultTableModel columnIdentifiers.size():"
		// + columnIdentifiers.size());
		tableModel.setDataVector(dataVector, columnIdentifiers); // テーブルモデルに値を設定
		return tableModel;
	}

	private String[] getColNames(DefaultTableModel tableModel) {
		int colCount = tableModel.getColumnCount();
		String[] colNames = new String[colCount];
		for (int col = 0; col < colCount; col++) {
			colNames[col] = tableModel.getColumnName(col);
		}
		return colNames;
	}

	// -------------------------------------------------------------
	// カラム幅の自動調整
	// -------------------------------------------------------------
	private int[] getColWidths(FontMetrics fom, DefaultTableModel tableModel,
			int checkCount) {
		// int baseWidth = fom.stringWidth("字"); //
		// 基本とする文字の幅としている"字"→12ぐらい
		int rowCount = tableModel.getRowCount();
		int colCount = tableModel.getColumnCount();
		String[] colNames = new String[colCount];
		int[] colWidths = new int[colCount];
		// セル幅を推測(※ヘッダーが収まる幅を調べる)
		for (int col = 0; col < colCount; col++) {
			colNames[col] = tableModel.getColumnName(col);
			colWidths[col] = fom.stringWidth(colNames[col]);
		}
		// セル幅を推測(※データから列の幅を測ってみる)
		if (checkCount > rowCount)
			checkCount = rowCount;
		for (int row = 0; row < checkCount; row++) {
			for (int col = 0; col < colCount; col++) {
				Object obj = tableModel.getValueAt(row, col);
				if (obj != null) {
					String str = (String) obj.toString();
					// System.out.println("getColWidths debug str=>" + str +
					// "<<=");
					int width = fom.stringWidth(str);
					if (colWidths[col] < width)
						colWidths[col] = width;
				}
			}
		}
		return colWidths;
	}

	// ※ カラム幅を調整
	private void adjustColWidth(Grid jTable, String[] names, int[] widths) {
		for (int col = 0; col < widths.length; col++) {
			TableColumn column = jTable.getColumn(names[col]);
			column.setPreferredWidth((int) (widths[col] * 1.3));// 微調整している
		}
	}

	public void extractIt(HashMap<Integer, Set<String>> limiter) {
		tableModel.setDataVector(extractor(dataVector, limiter, true),
				columnIdentifiers);
		setTableModel(grid, tableModel);
	}

	
	//and　条件しか設定できないが・・・orの要望はあるか？？
	private static Vector<Vector> extractor(Vector<Vector> matrix,
			HashMap<Integer, Set<String>> limiter, boolean positivex) {
		Vector<Vector> outMatrix = new Vector();
		List<Integer> keyList = new ArrayList(limiter.keySet());
		int condMax = keyList.size();
		int condCount = 0;
		if (condMax == 0)
			return matrix;// 条件が無ければそのまま返す
		for (Vector rows : matrix) {
			condCount = 0;
			for (Integer col : keyList) {
				Set set = limiter.get(col);
				Object obj = rows.get(col);
				System.out.println("col<" + col + ">:" + obj.toString());
				if (set.contains(obj)) {
					System.out.println("contains!!");
					condCount++;
				}
			}
			if (condCount == condMax) {
				outMatrix.add(rows);
			}
		}
		return outMatrix;
	}

}
