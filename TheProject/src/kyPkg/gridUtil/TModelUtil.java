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
	// �� �J�������̂��e�[�u�����f�����擾����
	// ------------------------------------------------------------------------
	// �O���b�h�̕���������������
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
		// colList��null�Ȃ�S�J�����o�͂���j
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
					writer.write(LF, 0, LF.length()); // ���s�R�[�h
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
	// �t�@�C������f�[�^���f���𐶐�����
	// ------------------------------------------------------------------------
	public static DefaultTableModelMod file2Tmodel_A_org(
			List<String> orgHeadList, String path, Inf_ArrayCnv cnv) {
		// �t�@�C������Ȃ�E�E�E?
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
		tableModel.setDataVector(matrix, columnIdentifiers); // �e�[�u�����f���ɒl��ݒ�
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
		tableModel.setDataVector(matrix, columnIdentifiers); // �e�[�u�����f���ɒl��ݒ�
		return tableModel;
	}

	// ------------------------------------------------------------------------
	// �� �J�������������v�Z
	// ------------------------------------------------------------------------
	public static int[] getColWidths(Graphics graphics, TableModel tableModel) {
		FontMetrics metrics = null;
		if (graphics != null) {
			System.out.println("�������J�������������v�Z ������graphics != null (^.^)/");
			metrics = graphics.getFontMetrics();
		} else {
			System.out.println("�������J�������������v�Z ������graphics == �k�� (T_T)");
		}
		return getColWidths(metrics, tableModel, 20);
	}

	// ------------------------------------------------------------------------
	// �� �J�������𒲐�
	// ------------------------------------------------------------------------
	public static void adjustColWidth(JTable jTable, String[] names,
			int[] widths) {
		// System.out.println("�������O���b�h�̕��𒲐�����");
		for (int col = 0; col < widths.length; col++) {
			TableColumn column = jTable.getColumn(names[col]);
			column.setPreferredWidth((int) (widths[col] * 1.3));// ���������Ă���
		}
	}

	// ------------------------------------------------------------------------
	// �J�������̎�������
	// ------------------------------------------------------------------------
	public static int[] getColWidths(FontMetrics metrics, TableModel tableModel,
			int checkCount) {
		// System.out.println("### �J�������̎�������<getColWidths> ############");

		// int baseWidth = fom.stringWidth("��"); //
		// ��{�Ƃ��镶���̕��Ƃ��Ă���"��"��12���炢
		int rowCount = tableModel.getRowCount();
		int colCount = tableModel.getColumnCount();
		String[] colNames = new String[colCount];
		int[] colWidths = new int[colCount];
		// �Z�����𐄑�(���w�b�_�[�����܂镝�𒲂ׂ�)
		for (int col = 0; col < colCount; col++) {
			colNames[col] = tableModel.getColumnName(col);
			colWidths[col] = (metrics == null)
					? colNames[col].length() * 10 + 10
					: metrics.stringWidth(colNames[col]);
			// colWidths[col] = metrics.stringWidth(colNames[col]);
		}
		// �Z�����𐄑�(���f�[�^�����̕��𑪂��Ă݂�)
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
	// �V�����e�[�u�����f���������āA�����Ɍ����������Matrix���A�T�C�����Ă���
	// �܂��A�T�C�������s�J�E���g���Ԃ�
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
	// matrix�Ɍ����������iand���������ݒ�ł��Ȃ����E�E�Eor�����̗v�]�͂��邩�H�H�j
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
			return matrix;// ������������΂��̂܂ܕԂ�
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
