package kyPkg.gridUtil;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

//--------------------------------------------------------------------------
// �J�X�^���@�e�[�u���Z�������_���[�s���x���Łt
// �s�g�p��t
//	jTblRez.setDefaultRenderer(Object.class, new TCRendLabel() );
// �C���[�W�A�C�R�����̃I�J�����X�����Ă�Ηǂ��̂��Ǝv��
//--------------------------------------------------------------------------
public class CellRenderer extends JLabel implements TableCellRenderer{
	private static final long serialVersionUID = -2465767291255095697L;
	private ImageIcon gIcon;
	private Color activeBack = Color.PINK;
	private Color deActiveBack = Color.WHITE;

	public void setActiveBack(Color activeBack) {
		this.activeBack = activeBack;
	}

	public void setDeActiveBack(Color deActiveBack) {
		this.deActiveBack = deActiveBack;
	}

	// ----------------------------------------------------------------------
	// �R���X�g���N�^
	// ----------------------------------------------------------------------
	public CellRenderer() {
		setOpaque(true);
	}

	public CellRenderer(Color activeBack) {
		this();
		setActiveBack(activeBack);
	}

	// ----------------------------------------------------------------------
	// �A�C�R����ݒ�
	// ----------------------------------------------------------------------
	public void setIcon(ImageIcon pIcon) {
		gIcon = pIcon;
	}

	// ----------------------------------------------------------------------
	// �`��R���|�[�l���g
	// ----------------------------------------------------------------------
	@Override
	public Component getTableCellRendererComponent(JTable table, Object data,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected == true) {
			setBackground(activeBack);
		} else {
			setBackground(deActiveBack);
		}
		if (gIcon != null)
			setIcon(gIcon);
		if (data != null) {
			setText(data.toString());
		} else {
			setText("");
		}
		return this;
	}
}
