package kyPkg.gridPanel;

import kyPkg.gridModels.DefaultTableModelMod;
import kyPkg.gridUtil.TModelUtil;
import kyPkg.util.GridUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.awt.*;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

//------------------------------------------------------------------------------
// TablePanel <���i >
// �C�x���g�̎��񂵂�S��
//------------------------------------------------------------------------------
// ���@�\�[�X�f�[�^�ݒ�i����t����l�j
// ���@�h���b�O�h���b�v
// ���@�t������
// �����R�[�h�̒ǉ��A�폜
// ���ꊇ�ǉ��E�E�E
// ���N���A
// ���A�C�R���̏����͊O���ɁE�E�E
// ���R���X�g���N�^�E�E�E
// ��ʃf�[�^��Ώۂɂ���ꍇ�͂ǂ����E�E�E
// �c�a�Ƃ̃R�l�N�V�������l���Ă݂�
//------------------------------------------------------------------------------
public class Grid_Panel extends JPanel {
	private static final long serialVersionUID = -6539486059219321035L;
	private int colWidth = 21;//autoFit���̊�{�J������
	private Grid grid;
	private JScrollPane scrollPane;
 
	protected JLabel labInfo;// ����������܂������̕\���Ɏg�p

	public void setLabInfo(JLabel labInfo) {
		this.labInfo = labInfo;
	}

	public JLabel getLabInfo() {
		return labInfo;
	}

	private HashMap<Integer, Set<String>> limiter = new HashMap();// �\���s�������肷��

	public HashMap<Integer, Set<String>> getLimiter() {
		return limiter;
	}

	public void extractIt(HashMap<Integer, Set<String>> limiter) {
		// Vector<Vector> dataVector =
		// GridUtil.cnvTModel2Vector(this.getModel());// ???
		this.limiter = limiter;
		int cnt = grid.getModel().getColumnCount();
		Vector colNames = new Vector();
		for (int i = 0; i < cnt; i++) {
			colNames.add(grid.getModel().getColumnName(i));
		}
		int count=TModelUtil.extractIt(grid, limiter, colNames, grid.getDataVector());
		setInfo();
		repaint();
	}

	// --------------------------------------------------------------------------
	// �R���X�g���N�^
	// --------------------------------------------------------------------------
	public Grid_Panel() {
		this(null);
	}

	public Grid_Panel(DefaultTableModelMod tableModel) {
		super(new BorderLayout());
		this.setOpaque(false);
		grid = new Grid(false);
		scrollPane = new JScrollPane(grid);
		this.add("Center", scrollPane);
		if (tableModel != null)
			setDefModel(tableModel);
		showGrid();
	}

	public void showGrid() {
		scrollPane.setViewportView(grid);
	}

	public List<String> getListByCol(int col) {
		return GridUtil.cnvTModel2ListByCol(grid.getModel(), 0);
	}

	// 20120905�@yuasa
	public int tModel2File(String path) {
		return TModelUtil.tModel2File(path, grid.getModel(), "\t", null);
	}

	public int saveAsFile(String path, int col) {
		List colList = new ArrayList();
		colList.add(col);
		return TModelUtil.tModel2File(path, grid.getModel(), "\t", colList);
	}

	// public List getDataList() {
	// TableModel tableModel = grid.getModel();
	// if (tableModel != null) {
	// if (tableModel instanceof DefaultTableModel)
	// return ((DefaultTableModel) tableModel).getDataVector();xxxx
	// }
	// return null;
	// }

	public Vector<Vector> getDataVector() {
		return grid.getDataVector();
	}

	public String getDataValue(int row, int col) {
		Vector<Vector> matrix = grid.getDataVector();
		Vector vector = matrix.get(row);
		if (vector != null) {
			Object obj = vector.get(col);
			return String.valueOf(obj);
		}
		return "";
	}

	public void removeTableMouseListener(MouseListener mouseListener) {
		grid.getTableHeader().removeMouseListener(mouseListener);
		grid.removeMouseListener(mouseListener);
	}

	public void setTableMouseListener(MouseListener mouseListener) {
		if (mouseListener == null)
			return;
		createColorMap();
		grid.getTableHeader().addMouseListener(mouseListener);
		grid.addMouseListener(mouseListener);
	}

	public ColorMapCtrlMod createColorMap() {
		return grid.createColorMap(10);// ok
	}

	public void autoFit() {

		grid.autoFit(colWidth, getDataVector());
	}

	// --------------------------------------------------------------------------
	// ���[�U����w�b�_���h���b�O���ė�̏�����ς����邩�ǂ�����ݒ肵�܂��B
	// --------------------------------------------------------------------------
	protected void setReorderingAllowed(boolean flag) {
		JTableHeader jTHead = grid.getTableHeader();
		jTHead.setReorderingAllowed(flag); // false:��̏�����ς����Ȃ�
	}

	// --------------------------------------------------------------------------
	// ���b�p�[
	// --------------------------------------------------------------------------
	// �e�[�u���̃T�C�Y�ύX���Ƀe�[�u���̎����T�C�Y�ύX���[�h��ݒ肵�܂��B
	// --------------------------------------------------------------------------
	protected void setAutoResizeMode() {
		grid.setAutoResizeMode(Grid.AUTO_RESIZE_OFF);
	}

	// tableHeader ��Ԃ��܂��B
	public JTableHeader getTableHeader() {
		return grid.getTableHeader();
	}

	// --------------------------------------------------------------------------
	// �I������
	// --------------------------------------------------------------------------
	// public void clearSelection() {
	// grid.clearSelection();// �A�N�e�B�u�͈͂������i���̕����̃��y�C���g������邪�E�E�j
	// grid.repaint();// �m���Ƀ��y�C���g����i�������肷��j
	// }

	// --------------------------------------------------------------------------
	// �`��
	// --------------------------------------------------------------------------
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	// --------------------------------------------------------------------------
	// ���\�[�X�i�A�C�R���A�摜�Ȃǁj��ǂݍ��݂܂�
	// --------------------------------------------------------------------------
	ImageIcon createImageIcon(String path, String description) {
		ClassLoader ld = this.getClass().getClassLoader();
		java.net.URL imgURL = ld.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		}
		System.err.println("Couldn't find file: " + path);
		return null;
	}

	// �ق��̃R���|�[�l���g��\������
	public void setCmponent(Component cmp) {
		scrollPane.setViewportView(cmp);
	}

	public void enableIt(boolean flag) {
	}

	public void setInfo() {
		if (labInfo == null)
			return;
//		int dataCount = grid.getRowDataCount();
		int dataCount = grid.getRowCount();
		System.out.println("################### setInfo() #################################"+dataCount);
		if (dataCount == 0) {
			labInfo.setText("Data Not Found!               ");
			labInfo.setForeground(Color.RED);
		} else {
			labInfo.setText("RowCount : " + dataCount + "               ");
			labInfo.setForeground(Color.BLUE);
		}
	}

	// wrapper
	public void fixFit(int width) {
		grid.fixFit(width);
	}

	// wrapper
	public int getColumnCount() {
		return grid.getColumnCount();
	}

	// wrapper
	public void resetColWidth(String colWidth) {
		grid.resetColWidth(colWidth);
	}

	// wrapper
	@Override
	public void removeAll() {
		grid.removeAll();
	}

	public Grid getGrid() {
		return grid;
	}

	public TableModel getTableModel() {
		return grid.getModel();
	}

	public void setDefModel(DefaultTableModelMod tableModel) {
		grid.setDefModel(tableModel);
	}

}
