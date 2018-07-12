package kyPkg.gridPanel;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import kyPkg.gridModels.DefaultTableModelMod;

public class Grid_Panel_old extends JPanel {
	private static final long serialVersionUID = 1L;
	private Grid grid;
	private JScrollPane scrollPane;
	protected JLabel labInfo;// ����������܂������̕\���Ɏg�p

	public JLabel getLabInfo() {
		return labInfo;
	}

	// -------------------------------------------------------------------------
	// �R���X�g���N�^
	// -------------------------------------------------------------------------
	public Grid_Panel_old() {
		super(new BorderLayout());
		this.setOpaque(false);
		grid = new Grid(false);
		scrollPane = new JScrollPane();
		this.add(scrollPane, BorderLayout.CENTER);
		showGrid();
	}

	public void showGrid() {
		scrollPane.setViewportView(grid);
	}

	public Grid getGrid() {
		return grid;
	}

	public void enableIt(boolean flag) {
	}

	@Override
	public void removeAll() {
		grid.removeAll();
	}

	public void setTableModel(DefaultTableModelMod tableModel) {
		grid.setDefModel(tableModel);
	}

	public void resetColwidth(String param) {
		grid.resetColwidth(param);
	}

	// �ق��̃R���|�[�l���g��\������
	public void setCmponent(Component cmp) {
		scrollPane.setViewportView(cmp);
	}
}
