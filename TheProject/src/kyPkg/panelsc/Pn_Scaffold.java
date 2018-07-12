package kyPkg.panelsc;

import java.awt.*;

/**
 * �s�v���O���X�o�[�̗��p���`�t ���ȉ��̓�̃N���X�Ɉˑ����Ă��܂� kyPkg.util.TskXXX kyPkg.util.SwingWorker
 */
public class Pn_Scaffold extends BorderPanel {
	private static final long serialVersionUID = -1775170390761227057L;
	protected BorderPanel pnlSouth;
	// -------------------------------------------------------------------------
	// �J�[�\����ύX
	// -------------------------------------------------------------------------
	public void setHourglass(boolean flag) {
		if (flag) {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		} else {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
	public Pn_Scaffold(int width, int height) {
		super();
		initGUI(width, height);
	}

	// -------------------------------------------------------------------------
	// init GUI
	// -------------------------------------------------------------------------
	private void initGUI(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(new Dimension(width, height));
		pnlSouth = new BorderPanel();
		add(pnlSouth, BorderLayout.SOUTH);
		// this.setOpaque(true);
	}

	// -------------------------------------------------------------------------
	// /** ActionListener�C���^�[�t�F�[�X�̃��\�b�h���` */
	// public void actionPerformed(ActionEvent evt) {
	// JButton button = (JButton) evt.getSource();
	// if (button == btnExe) {
	// task = new TskXXX(new String[] { ".", commonRes.getPath(), "" });
	// task.execute(); // ���������N������
	// }
	// }

	// -------------------------------------------------------------------------
	// ���[���[�\��
	// -------------------------------------------------------------------------
	// public void paintComponent(Graphics g) {
	// super.paintComponent(g);
	// Ruler.drawRuler(g, this.getSize().width, this.getSize().height,
	// new Color(250, 187, 244, 128));
	// }
	public static void main(String[] argv) {
		standAlone(new Pn_Scaffold(640, 480), "scaffold");
	}

}
