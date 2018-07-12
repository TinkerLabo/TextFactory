package kyPkg.util;

import java.awt.*;

import javax.swing.*;
//--------------------------------------------------------------------
// リストセルレンダラー《ラベル版》シマシマ版
//--------------------------------------------------------------------
//《使用例》
//  LCRendLabel listRenderer = new LCRendLabel();
//  jList01.setCellRenderer(listRenderer);
//--------------------------------------------------------------------

import kyPkg.external.ColorControl;

public class LCRendLabel extends JLabel implements ListCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7142370505506592069L;
	private Color gSelForeColor = Color.WHITE;
	private Color gSelBackColor = new Color(153, 204, 255);

	private Color gOddForeColor = Color.BLACK;
	// private Color gOddBackColor = new Color(191,191,242);
	private Color gOddBackColor = ColorControl.getEvenColor();

	// private Color gSelForeColor = Color.BLACK;
	// private Color gSelBackColor = new Color(191,191,242);
	//
	// private Color gOddForeColor = Color.BLACK;
	// private Color gOddBackColor = new Color(153,204,255);

	private Color gEveForeColor = Color.BLACK;
	private Color gEveBackColor = Color.WHITE;

	// ----------------------------------------------------------------
	// アクセッサ
	// ----------------------------------------------------------------
	public void setSelForeColor(Color pColor) {
		gSelForeColor = pColor;
	}

	public void setOddForeColor(Color pColor) {
		gOddForeColor = pColor;
	}

	public void setEveForeColor(Color pColor) {
		gEveForeColor = pColor;
	}

	// ----------------------------------------------------------------
	public void setSelBackColor(Color pColor) {
		gSelBackColor = pColor;
	}

	public void setOddBackColor(Color pColor) {
		gOddBackColor = pColor;
	}

	public void setEveBackColor(Color pColor) {
		gEveBackColor = pColor;
	}

	// ----------------------------------------------------------------
	// コンストラクタ
	// ----------------------------------------------------------------
	public LCRendLabel() {
		super();
		setOpaque(true);

		Font font = new Font("Monospaced", Font.PLAIN, 14);
		setFont(font);
		// new Font("Monospaced", Font.PLAIN, 10));//
		// いい感じ！！
		// new Font("Dialog", Font.PLAIN, 12));
		// new Font("MS Gothic", Font.PLAIN, 10));
		// new Font("MS Mincho", Font.PLAIN, 10));
		// new Font("SansSerif", Font.PLAIN, 12));
		// new Font("Serif", Font.PLAIN, 10));
		// new Font("HGSGyoshotai", Font.PLAIN, 12));
		// new Font("Arial Unicode MS", Font.PLAIN,12));
		// new Font("HGMaruGothicMPRO", Font.PLAIN,10));
		// new Font("HGPSoeiKakugothicUB", Font.PLAIN,12));

	}

	// ----------------------------------------------------------------
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		String tmp = value.toString();
		setText(tmp);

		if (isSelected) {
			setBackground(gSelBackColor);
			setForeground(gSelForeColor);
		} else {
			if ((index % 2) == 0) {
				setForeground(gEveForeColor);
				setBackground(gEveBackColor);
			} else {
				setForeground(gOddForeColor);
				setBackground(gOddBackColor);
			}
		}
		return this;
	}
}
