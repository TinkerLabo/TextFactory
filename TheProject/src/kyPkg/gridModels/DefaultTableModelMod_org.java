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
	// �X�v���b�h�V�[�g�̋K����g�p���āA��̃f�t�H���g�� (A�AB�AC�A...Z�AAA�AAB �Ȃ�) ��Ԃ��܂��B
	private boolean useDefaultName = true;

	public void setUseDefaultName(boolean useDefaultName) {
		this.useDefaultName = useDefaultName;
	}

	private Class colClass[];

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
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
		// String[][] tabledata = { { "���{", "3��", "0�s", "1��" },
		// { "�N���A�`�A", "3��", "1�s", "0��" }, { "�u���W��", "1��", "2�s", "1��" },
		// { "�I�[�X�g�����A", "2��", "2�s", "0��" } };
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
	// �w�肳�ꂽ�ʒu�ɂ����̖��O��Ԃ��B
	// ���X�[�p�[�N���X�͂ǂ�Ȗ��O��Ԃ��񂾂��H�H
	// -------------------------------------------------------------------------
	@Override
	public String getColumnName(int column) {
		if (this.useDefaultName == false)
			return Integer.toString(column + 1);
		return super.getColumnName(column);
	}

	// -------------------------------------------------------------------------
	// �Z�����ҏW�\���ǂ�����ݒ�
	// -------------------------------------------------------------------------
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	// #########################################################################
	// -------------------------------------------------------------------------
	// ��̃Z���l�̍ł����m�ȃX�[�p�[�N���X��Ԃ��܂��B
	// DEFAULT�ł̓I�u�W�F�N�g�N���X���Ԃ����A���Y�N���X�ɕύX���邱�Ƃɂ��
	// ���̃N���X�̕W�������_���[����уG�f�B�^���I�������
	// (EX BOOL�Ȃ�CheckBox���\�������i�h)
	// ���R�����g�A�E�g�ɂ�蓮��m�F���ꂽ��
	// ---------------------------------------------<�C���^�t�F�[�X TableModel>-
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
	// �e��̃N���X��z��Ɋi�[���Ă����E�E�������y�����邽��
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
// �Q�����̃I�u�W�F�N�g�z����Q�����̃x�N�^�[�ɕϊ�����i�ꎟ���ł��g�p�j
// <�K�{�H�I>
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
// �Z���̒l���Z�b�g
// -------------------------------------------------------------------------
// public void setValueAt(Object aValue, int row, int column) {
// super.setValueAt(aValue, row, column);
// }

// -------------------------------------------------------------------------
// �s�K�{�t �Z���̒l��Ԃ��B���͈͂��`�F�b�N���Ȃ��Ă������̂��낤���H
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
