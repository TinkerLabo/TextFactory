package kyPkg.mySwing;

import javax.swing.JTextField;
import javax.swing.text.Document;

public class MyTextField extends JTextField {
	private static final long serialVersionUID = 1L;

	public MyTextField(int x,int y,int width ,int height,String text) {
		super(text);
		setBounds(x, y, width, height);	
	}

	public MyTextField() {
	}

	public MyTextField(String text) {
		super(text);
	}

	public MyTextField(int columns) {
		super(columns);
	}

	public MyTextField(String text, int columns) {
		super(text, columns);
	}

	public MyTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
	}
}
