package kyPkg.util;
import java.awt.*;
import javax.swing.*;
//-----------------------------------------------------------------------------
// �Ȏȃ��X�g�����_���[
// �s�g�p��t
// ColoredListRenderer renderer = new ColoredListRenderer();
// list.setCellRenderer(renderer);
//-----------------------------------------------------------------------------
// ColoredListRenderer renderer = new ColoredListRenderer(new Color(252,212,212)); // ��������F 
// list.setCellRenderer(renderer);
//-----------------------------------------------------------------------------
public class ColoredListRenderer extends JLabel implements ListCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7534857539500623823L;
	//-------------------------------------------------------------------------
	Color wBk0 = new Color( 51,  0,153);  // ���񂶂傤���� 
	Color wBk1 = new Color(204,255,255);  // �݂����� �����s
	Color wBk2 = Color.WHITE;             // ��       ��s
	//-------------------------------------------------------------------------
	Color wFr0 = Color.WHITE;            // ��
	Color wFr1 = Color.BLACK;            // ��
	//-------------------------------------------------------------------------
	//Color.RED;              // ��       �I���s??
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
