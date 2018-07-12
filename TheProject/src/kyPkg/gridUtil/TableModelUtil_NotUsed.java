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
		// jTable��tableModel���Ђ��Â�
		// -------------------------------------------------------------
		setTableModel(grid, tableModel);

		FontMetrics fom = grid.getGraphics().getFontMetrics();

		colNames = getColNames(tableModel);
		colWidths = getColWidths(fom, tableModel, 10);

		adjustColWidth(grid, colNames, colWidths);
		// �e�[�u���̓��e�͕ҏW�\�Ƃ���̂��A����Ƃ��s�Ƃ���̂��H
		// ���l���ڂ͉E�񂹂Ƃ���̂��A�ǂ������ꍇ�𐔒l���ڂƔ��肷��̂��H
	}

	// ------------------------------------------------------------------------
	// �O���b�h�Ƀf�[�^���Z�b�g����i�O���b�h�̕�����������j
	// ------------------------------------------------------------------------
	private DefaultTableModel createTmodelFromFile(String path, String[] head,
			Inf_ArrayCnv cnv) {
		List<String> headList = new ArrayList();
		if (head != null)
			headList = Arrays.asList(head);

		dataVector = File2Matrix.extract(path, cnv);
		if (dataVector.size() > 0) {
			Vector row = dataVector.get(0);
			// �w�b�_�[�����݂��Ȃ������ꍇ�擪�s�̃J�����𐔂��ĉ��̃w�b�_�[�����
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
		tableModel.setDataVector(dataVector, columnIdentifiers); // �e�[�u�����f���ɒl��ݒ�
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
	// �J�������̎�������
	// -------------------------------------------------------------
	private int[] getColWidths(FontMetrics fom, DefaultTableModel tableModel,
			int checkCount) {
		// int baseWidth = fom.stringWidth("��"); //
		// ��{�Ƃ��镶���̕��Ƃ��Ă���"��"��12���炢
		int rowCount = tableModel.getRowCount();
		int colCount = tableModel.getColumnCount();
		String[] colNames = new String[colCount];
		int[] colWidths = new int[colCount];
		// �Z�����𐄑�(���w�b�_�[�����܂镝�𒲂ׂ�)
		for (int col = 0; col < colCount; col++) {
			colNames[col] = tableModel.getColumnName(col);
			colWidths[col] = fom.stringWidth(colNames[col]);
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
					int width = fom.stringWidth(str);
					if (colWidths[col] < width)
						colWidths[col] = width;
				}
			}
		}
		return colWidths;
	}

	// �� �J�������𒲐�
	private void adjustColWidth(Grid jTable, String[] names, int[] widths) {
		for (int col = 0; col < widths.length; col++) {
			TableColumn column = jTable.getColumn(names[col]);
			column.setPreferredWidth((int) (widths[col] * 1.3));// ���������Ă���
		}
	}

	public void extractIt(HashMap<Integer, Set<String>> limiter) {
		tableModel.setDataVector(extractor(dataVector, limiter, true),
				columnIdentifiers);
		setTableModel(grid, tableModel);
	}

	
	//and�@���������ݒ�ł��Ȃ����E�E�Eor�̗v�]�͂��邩�H�H
	private static Vector<Vector> extractor(Vector<Vector> matrix,
			HashMap<Integer, Set<String>> limiter, boolean positivex) {
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
