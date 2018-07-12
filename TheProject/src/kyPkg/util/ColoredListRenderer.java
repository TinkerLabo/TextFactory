package kyPkg.util;
import java.awt.*;
import javax.swing.*;
//-----------------------------------------------------------------------------
// 縞縞リストレンダラー
// 《使用例》
// ColoredListRenderer renderer = new ColoredListRenderer();
// list.setCellRenderer(renderer);
//-----------------------------------------------------------------------------
// ColoredListRenderer renderer = new ColoredListRenderer(new Color(252,212,212)); // うすい朱色 
// list.setCellRenderer(renderer);
//-----------------------------------------------------------------------------
public class ColoredListRenderer extends JLabel implements ListCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7534857539500623823L;
	//-------------------------------------------------------------------------
	Color wBk0 = new Color( 51,  0,153);  // ぐんじょういろ 
	Color wBk1 = new Color(204,255,255);  // みずいろ 偶数行
	Color wBk2 = Color.WHITE;             // 白       奇数行
	//-------------------------------------------------------------------------
	Color wFr0 = Color.WHITE;            // 白
	Color wFr1 = Color.BLACK;            // 黒
	//-------------------------------------------------------------------------
	//Color.RED;              // 赤       選択行??
	//-------------------------------------------------------------------------
	public ColoredListRenderer(Color pBk1){
		this();
		wBk1 = pBk1;
	}
	public ColoredListRenderer(){
		setOpaque(true);
	}
	@Override
	public Component getListCellRendererComponent(JList list,Object value,
						int index,boolean isSelected,boolean cellHasFocus){
		setText(value.toString());
		if ((index%2)==1){
			setBackground(isSelected ? wBk0 : wBk1);
		}else{
			setBackground(isSelected ? wBk0 : wBk2);
		}
		setForeground(isSelected ? wFr0 : wFr1);
		return this;
	}
}
