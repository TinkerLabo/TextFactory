package kyPkg.mySwing;
import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JLabel;

public class MyLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	public MyLabel(int x,int y,int width ,int height,String text) {
		super(text);
		//setOpaque(false);   //•s“§–¾
		setBounds(x, y, width, height);		
	}

	public MyLabel() {
		super();
	}

	public MyLabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
	}

	public MyLabel(Icon image) {
		super(image);
	}

	public MyLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
	}

	public MyLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
	}

	public MyLabel(String text) {
		super(text);
	}
	public void setColor(Color foreColor,Color backColor){
		setForeground(foreColor);
		setBackground(backColor);
		setOpaque(true);
	}
}
