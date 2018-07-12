package kyPkg.gridPanel;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import kyPkg.external.ColorControl;

public class StripeTableRenderer extends DefaultTableCellRenderer {
	private ImageIcon gIcon;

	// 画面に表示されたものしか表示しない・・・
	// グリッドではなくデータモデルそのものを書き換えた方が良いだろう
	// private HashMap<Object, Integer> key2row = new HashMap();

	// jancode と　行番号の辞書引き　（外部化する）
	private ColorMapCtrlMod colorMapCtrl;

	public void setColorMapList(ColorMapCtrlMod colorMapCtrl) {
		this.colorMapCtrl = colorMapCtrl;
	}

	// ----------------------------------------------------------------------
	// アイコンを設定
	// ----------------------------------------------------------------------
	public void setIcon(ImageIcon pIcon) {
		gIcon = pIcon;
	}

	
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);

		// if (column == 0) {
		// System.out.println("## key:" + value + " size:" + key2row.size());
		// key2row.put(value, row);
		// }

		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(ColorControl.getActivebackColor());
		} else {
			Color color = null;
			// 色を調べる
			if (colorMapCtrl != null)
				color = colorMapCtrl.peekColor(row, column);
			if (color == null) {
				setForeground(table.getForeground());
				setBackground((row % 2 == 0) ? ColorControl.getEvenColor()
						: table.getBackground());
			} else {
				Color foreColor = ColorControl.XorColor(color);
//				Color backColor = ColorControl.XorColor(color);
				setForeground(foreColor);
				setBackground(color);
			}
		}

		setHorizontalAlignment((value instanceof Number) ? RIGHT : LEFT);
		// if (value instanceof Number){
		// System.out.println("###getTableCellRendererComponent##"+value);
		// }

		if (gIcon != null)
			setIcon(gIcon);
		// if (value != null) {
		// setText(value.toString());
		// } else {
		// setText("");
		// }

		return this;
	}

	public StripeTableRenderer() {
		super();

	}
}
// ----------------------------------------------------------------------
// 描画コンポーネント
// ----------------------------------------------------------------------
// public Component getTableCellRendererComponent(JTable table, Object data,
// boolean isSelected, boolean hasFocus, int row, int column) {
// if (isSelected == true) {
// setBackground(activeBack);
// } else {
// setBackground(deActiveBack);
// }
// if (gIcon != null)
// setIcon(gIcon);
// if (data != null) {
// setText(data.toString());
// } else {
// setText("");
// }
// return this;
// }
