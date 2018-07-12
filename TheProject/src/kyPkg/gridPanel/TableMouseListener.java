package kyPkg.gridPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

public class TableMouseListener implements MouseListener {
	private int pressedRow; // �}�E�X�������ꂽ�s

	private int pressedCol; // �}�E�X�������ꂽ��

	protected int whichButton;
	protected int clickCount;
	protected boolean shiftDown;
	protected boolean controlDown;
	protected boolean metaDown;

	public int getWhichButton() {
		return whichButton;
	}

	public int getClickCount() {
		return clickCount;
	}

	public boolean isShiftDown() {
		return shiftDown;
	}

	public boolean isControlDown() {
		return controlDown;
	}

	public boolean isMetaDown() {
		return metaDown;
	}

	// -------------------------------------------------------------------------
	// �w�b�h��N���b�N
	// -------------------------------------------------------------------------
	public void headClickHandle(int columnIndex) {
		System.out.println("�w�b�_�[�Ń}�E�X���N���b�N���ꂽ����:" + columnIndex);
	}

	// -------------------------------------------------------------------------
	// �}�E�X�{�^����������
	// -------------------------------------------------------------------------
	public void mousePressedHandle(MouseEvent evt, int row, int col) {
		whichButton = evt.getButton();
		clickCount = evt.getClickCount();
		shiftDown = (evt.isShiftDown()) ? true : false;
		controlDown = (evt.isControlDown()) ? true : false;
		metaDown = (evt.isMetaDown()) ? true : false;
		if (whichButton == MouseEvent.BUTTON1) {
//			System.out.println("���{�^���N���b�N");
		} else if (whichButton == MouseEvent.BUTTON2) {
//			System.out.println("���{�^���N���b�N");
		} else if (whichButton == MouseEvent.BUTTON3) {
//			System.out.println("�E�{�^���N���b�N");
		}
		pressedRow = row;
		pressedCol = col;
		System.out
				.println("mousePressed @ �s:" + pressedRow + " ��:" + pressedCol);
	}

	// -------------------------------------------------------------------------
	// �}�E�X�{�^���������
	// -------------------------------------------------------------------------
	public void mouseReleasedHandle(MouseEvent evt, int releasedRow, int releasedCol) {
		System.out.println("@TableMouseListener_mouseReleased @ �s:"
				+ releasedRow + " ��:" + releasedCol);
	}

	// ------------------------------------------------------------------
	// �}�E�X�̃{�^���������ꂽ
	// ------------------------------------------------------------------
	@Override
	public void mousePressed(MouseEvent evt) {
		Object evtSource = evt.getSource(); // �C���X�^���X�𒲂ׂ�
		if ((evtSource != null) && (evtSource instanceof JTable)) {
			//			System.out.println("��mousePressed��");
			JTable jTable = (JTable) evtSource;
			whichButton = evt.getButton();
			clickCount = evt.getClickCount();
			shiftDown = (evt.isShiftDown()) ? true : false;
			controlDown = (evt.isControlDown()) ? true : false;
			metaDown = (evt.isMetaDown()) ? true : false;
			//			System.out.println("@TableMouseListener whichButton:" + whichButton);
			int row = jTable.rowAtPoint(evt.getPoint());
			int col = jTable.columnAtPoint(evt.getPoint());
			// �n���h�����Ă�
			mousePressedHandle(evt, row, col);
		} else if ((evtSource != null) && (evtSource instanceof JTableHeader)) {
			JTableHeader header = (JTableHeader) evtSource;
			int columnIndex = header.columnAtPoint(evt.getPoint());
			// System.out.println("mousePressed=>columnIndex:" + columnIndex);
			// �n���h�����Ă�
			headClickHandle(columnIndex);
		}
	}

	// ------------------------------------------------------------------
	// �}�E�X�̃{�^���������ꂽ
	// ------------------------------------------------------------------
	@Override
	public void mouseReleased(MouseEvent evt) {
		Object wObj = evt.getSource(); // �C���X�^���X�𒲂ׂ�
		if ((wObj != null) && (wObj instanceof JTable)) {
			//			System.out.println("��mouseReleased��");
			JTable table = (JTable) wObj;
			try {
				int row = table.rowAtPoint(evt.getPoint());
				int col = table.columnAtPoint(evt.getPoint());
				// �n���h�����Ă�
				mouseReleasedHandle(evt, row, col);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public int getPressedCol() {
		return pressedCol;
	}

	public void setPressedCol(int pressedCol) {
		if (pressedCol >= 0)
			this.pressedCol = pressedCol;
	}

	public int getPressedRow() {
		return pressedRow;
	}

	public void setPressedRow(int pressedRow) {
		if (pressedRow >= 0)
			this.pressedRow = pressedRow;
	}

	// private int releasedRow; // �}�E�X�������ꂽ�s
	// private int releasedCol; // �}�E�X�������ꂽ��
	// public int getReleasedCol() {
	// return releasedCol;
	// }
	// public void setReleasedCol(int releasedCol) {
	// if (releasedCol >= 0)
	// this.releasedCol = releasedCol;
	// }
	// public int getReleasedRow() {
	// return releasedRow;
	// }
	// public void setReleasedRow(int releasedRow) {
	// if (releasedRow >= 0)
	// this.releasedRow = releasedRow;
	// }
}
