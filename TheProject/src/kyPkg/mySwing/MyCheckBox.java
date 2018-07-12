package kyPkg.mySwing;

 

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;

public class MyCheckBox extends JCheckBox {
	private static final long serialVersionUID = 1L;
	public MyCheckBox(int x,int y,int width ,int height,String text) {
		super(text);
		setOpaque(false);
		//setFocusPainted(false);
		//addFocusListener();
		setBounds(x, y, width, height);	
	}

	public MyCheckBox() {
	}

	public MyCheckBox(Icon icon) {
		super(icon);
	}

	public MyCheckBox(String text) {
		super(text);
	}

	public MyCheckBox(Action a) {
		super(a);
	}

	public MyCheckBox(Icon icon, boolean selected) {
		super(icon, selected);
	}

	public MyCheckBox(String text, boolean selected) {
		super(text, selected);
	}

	public MyCheckBox(String text, Icon icon) {
		super(text, icon);
	}

	public MyCheckBox(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
	}
}
