package kyPkg.gridUtil;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

//--------------------------------------------------------------------------
// カスタム　テーブルセルレンダラー《ラベル版》
// 《使用例》
//	jTblRez.setDefaultRenderer(Object.class, new TCRendLabel() );
// イメージアイコン自体オカレンスを持てれば良いのだと思う
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
	// コンストラクタ
	// ----------------------------------------------------------------------
	public CellRenderer() {
		setOpaque(true);
	}

	public CellRenderer(Color activeBack) {
		this();
		setActiveBack(activeBack);
	}

	// ----------------------------------------------------------------------
	// アイコンを設定
	// ----------------------------------------------------------------------
	public void setIcon(ImageIcon pIcon) {
		gIcon = pIcon;
	}

	// ----------------------------------------------------------------------
	// 描画コンポーネント
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
