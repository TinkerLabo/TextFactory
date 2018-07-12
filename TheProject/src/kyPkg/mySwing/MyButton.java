package kyPkg.mySwing;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class MyButton extends JButton {
	private static final long serialVersionUID = 1L;

	public MyButton(int x,int y,int width ,int height,String text) {
		super(text);
		setBounds(x, y, width, height);	
	}

	public MyButton() {
	}

	public MyButton(Icon icon) {
		super(icon);
	}

	public MyButton(String text) {
		super(text);
	}

	public MyButton(Action a) {
		super(a);
	}

	public MyButton(String text, Icon icon) {
		super(text, icon);
	}

}
