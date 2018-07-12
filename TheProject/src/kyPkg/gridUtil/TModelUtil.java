package kyPkg.gridUtil;

import static kyPkg.uFile.FileUtil.LF;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import kyPkg.converter.Inf_ArrayCnv;
import kyPkg.gridModels.DefaultTableModelMod;
import kyPkg.uFile.File2Matrix;
import kyPkg.uFile.FileUtil;

public class TModelUtil {
	// ------------------------------------------------------------------------
	// ※ カラム名称をテーブルモデルより取得する
	// ------------------------------------------------------------------------
	// グリッドの幅を自動調整する
	// ------------------------------------------------------------------------
	public static void adjustColWidth(JTable grid, TableModel tableModel) {
		Graphics graphics = grid.getGraphics();
		String[] colNames = getColNames(tableModel);
		int[] colWidths = getColWidths(graphics, tableModel);
		adjustColWidth(grid, colNames, colWidths);
	}

	public static String[] getColNames(TableModel tableModel) {
		if (tableModel == null)
			return null;
		int colCount = tableModel.getColumnCount();
		String[] colNames = new String[colCount];
		for (int col = 0; col < colCount; col++) {
			colNames[col] = tableModel.getColumnName(col);
		}
		return colNames;
	}

	public static int tModel2File(String outPath, TableModel tableModel,
			String delimiter, List<Integer> colList) {
		int colMax = tableModel.getColumnCount();
		int rowMax = tableModel.getRowCount();
		// colListがnullなら全カラム出力する）
		if (colList == null) {
			colList = new ArrayList();
			for (int col = 0; col < colMax; col++) {
				colList.add(col);
			}
		}
		int row = 0;
		try {
			OutputStreamWriter writer = FileUtil.getWriter(outPath);
			if (writer == null)
				return -1;
			StringBuffer buf = new StringBuffer();
			for (row = 0; row < rowMax; row++) {
				buf.delete(0, buf.length());
				for (Integer col : colList) {
					Object obj = tableModel.getValueAt(row, col);
					if (obj != null && obj instanceof String)
						buf.append((String) obj);
					buf.append(delimiter);
					writer.write(buf.toString(), 0, buf.length());
					writer.write(LF, 0, LF.length()); // 改行コード
					// System.out.println("tModel2File>" + buf.toString());
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return row;
	}

	private static int getMatrixColCount(Vector<Vector> matrix) {
		if (matrix != null && matrix.size() > 0) {
			Vector row = matrix.get(0);
			if (row != null)
				return row.size();
		}
		return -1;
	}

	// ------------------------------------------------------------------------
	// ファイルからデータモデルを生成する
	// ------------------------------------------------------------------------
	public static DefaultTableModelMod file2Tmodel_A_org(
			List<String> orgHeadList, String path, Inf_ArrayCnv cnv) {
		// ファイルが空なら・・・?
		Vector<Vector> matrix = File2Matrix.extract(path, cnv);
		int colCount = getMatrixColCount(matrix);
		Vector columnIdentifiers = new Vector();
		for (int seq = 0; seq < colCount; seq++) {
			if (orgHeadList.size() > seq) {
				columnIdentifiers.add(orgHeadList.get(seq));
			} else {
				columnIdentifiers.add("Cel_" + seq);
			}
		}
		DefaultTableModelMod tableModel = new DefaultTableModelMod();
		tableModel.setDataVector(matrix, columnIdentifiers); // テーブルモデルに値を設定
		return tableModel;
	}

	public static DefaultTableModelMod matrix2TModel(List<String> headList,
			Vector<Vector> matrix) {
		int colCount = getMatrixColCount(matrix);
		Vector columnIdentifiers = new Vector();
		for (int seq = 0; seq < colCount; seq++) {
			if (headList.size() > seq) {
				columnIdentifiers.add(headList.get(seq));
			} else {
				columnIdentifiers.add("Cel_" + seq);
			}
		}
		DefaultTableModelMod tableModel = new DefaultTableModelMod();
		tableModel.setDataVector(matrix, columnIdentifiers); // テーブルモデルに値を設定
		return tableModel;
	}

	// ------------------------------------------------------------------------
	// ※ カラム幅を自動計算
	// ------------------------------------------------------------------------
	public static int[] getColWidths(Graphics graphics, TableModel tableModel) {
		FontMetrics metrics = null;
		if (graphics != null) {
			System.out.println("◆◆◆カラム幅を自動計算 ◆◆◆graphics != null (^.^)/");
			metrics = graphics.getFontMetrics();
		} else {
			System.out.println("◆◆◆カラム幅を自動計算 ◆◆◆graphics == ヌル (T_T)");
		}
		return getColWidths(metrics, tableModel, 20);
	}

	// ------------------------------------------------------------------------
	// ※ カラム幅を調整
	// ------------------------------------------------------------------------
	public static void adjustColWidth(JTable jTable, String[] names,
			int[] widths) {
		// System.out.println("◆◆◆グリッドの幅を調整する");
		for (int col = 0; col < widths.length; col++) {
			TableColumn column = jTable.getColumn(names[col]);
			column.setPreferredWidth((int) (widths[col] * 1.3));// 微調整している
		}
	}

	// ------------------------------------------------------------------------
	// カラム幅の自動調整
	// ------------------------------------------------------------------------
	public static int[] getColWidths(FontMetrics metrics, TableModel tableModel,
			int checkCount) {
		// System.out.println("### カラム幅の自動調整<getColWidths> ############");

		// int baseWidth = fom.stringWidth("字"); //
		// 基本とする文字の幅としている"字"→12ぐらい
		int rowCount = tableModel.getRowCount();
		int colCount = tableModel.getColumnCount();
		String[] colNames = new String[colCount];
		int[] colWidths = new int[colCount];
		// セル幅を推測(※ヘッダーが収まる幅を調べる)
		for (int col = 0; col < colCount; col++) {
			colNames[col] = tableModel.getColumnName(col);
			colWidths[col] = (metrics == null)
					? colNames[col].length() * 10 + 10
					: metrics.stringWidth(colNames[col]);
			// colWidths[col] = metrics.stringWidth(colNames[col]);
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
					int width = (metrics == null) ? str.length() * 10 + 10
							: metrics.stringWidth(str);
					// int width = metrics.stringWidth(str);
					if (colWidths[col] < width)
						colWidths[col] = width;
				}
			}
		}
		return colWidths;
	}

	// ------------------------------------------------------------------------
	// 新しいテーブルモデルをつくって、そこに限定をかけたMatrixをアサインしている
	// またアサインした行カウントも返す
	// ------------------------------------------------------------------------
	public static int extractIt(JTable grid,
			HashMap<Integer, Set<String>> limiter, Vector colNames,
			Vector<Vector> matrix) {
		DefaultTableModelMod newTModel = new DefaultTableModelMod();
		Vector<Vector> newMatrix = extractor(matrix, limiter);
		newTModel.setDataVector(newMatrix, colNames);
		attachSortableGrid(grid, newTModel);
		return newMatrix.size();

	}

	// ------------------------------------------------------------------------
	// matrixに限定をかける（and条件しか設定できないが・・・or条件の要望はあるか？？）
	// ------------------------------------------------------------------------
	private static Vector<Vector> extractor(Vector<Vector> matrix,
			HashMap<Integer, Set<String>> limiter) {
		if (limiter == null)
			return matrix;
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
				if (set.contains(obj)) {
					// System.out.println("col<" + col + ">:" + obj.toString()
					// + "=>contains!!");
					condCount++;
				} else {
					// System.out.println("col<" + col + ">:" + obj.toString()
					// + "=> NotContains...");
				}
			}
			if (condCount == condMax) {
				outMatrix.add(rows);
			}
		}
		return outMatrix;
	}

	public static void attachSortableGrid(JTable grid, TableModel tableModel) {
		TableSorter sorter = new TableSorter(tableModel);
		grid.setModel(sorter);
		sorter.setTableHeader(grid.getTableHeader());
	}

	// matrix2Vector
	public static Vector<Vector> matrix2Vector(List matrix, long limit) {
		long cnt = 0;
		if (limit <= 0)
			limit = Long.MAX_VALUE;
		Vector dataVector = new Vector();
		for (Object rowObj : matrix) {
			cnt++;
			if (cnt > limit)
				break;
			if (rowObj instanceof List) {
				dataVector.add(new Vector((List) rowObj));
			} else {
				dataVector.add(rowObj);
			}
		}
		return dataVector;
	}
}
