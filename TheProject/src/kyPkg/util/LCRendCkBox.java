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
public class LCRendCkBox extends JCheckBox implements ListCellRenderer {
	private static final long serialVersionUID = -7142370505506592069L;
	private Color gSelForeColor = Color.WHITE;
	private Color gSelBackColor = new Color(153,204,255);
	 
	private Color gOddForeColor = Color.BLACK;
//	private Color gOddBackColor = Color.LIGHT_GRAY;
	private Color gOddBackColor = new Color(191,191,242);
	private Color gEveForeColor = Color.BLACK;
	private Color gEveBackColor = Color.WHITE;
	//----------------------------------------------------------------
	// アクセッサ
	//----------------------------------------------------------------
	public void setSelForeColor(Color pColor){
		gSelForeColor = pColor;
	}
	public void setOddForeColor(Color pColor){
		gOddForeColor = pColor;
	}
	public void setEveForeColor(Color pColor){
		gEveForeColor = pColor;
	}
	//----------------------------------------------------------------
	public void setSelBackColor(Color pColor){
		gSelBackColor = pColor;
	}
	public void setOddBackColor(Color pColor){
		gOddBackColor = pColor;
	}
	public void setEveBackColor(Color pColor){
		gEveBackColor = pColor;
	}
	//----------------------------------------------------------------
	// コンストラクタ
	//----------------------------------------------------------------
	public LCRendCkBox(){
		super();
		setOpaque(true);
	}
	//----------------------------------------------------------------
	@Override
	public Component getListCellRendererComponent(JList list,
			Object value, int index, boolean isSelected,
			boolean cellHasFocus){
		String tmp = value.toString();
		setText(tmp);
		if (isSelected){
			setBackground(gSelBackColor);
			setForeground(gSelForeColor);
			setSelected(true);
		}else{
			setSelected(false);
			if ((index % 2) == 0){
				setForeground(gEveForeColor);
				setBackground(gEveBackColor);
			}else{
				setForeground(gOddForeColor);
				setBackground(gOddBackColor);
			}
		}
		return this;
	}
}
