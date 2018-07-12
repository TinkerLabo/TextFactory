package kyPkg.mySwing;

import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

public class MyComboBox extends JComboBox {
	@Override
	public String getSelectedItem() {
		return (String) dataModel.getSelectedItem();
	}

	private static final long serialVersionUID = 1L;

	public MyComboBox(int x, int y, int width, int height) {
		super();
		setBounds(x, y, width, height);
	}

	public MyComboBox() {
	}

	public MyComboBox(ComboBoxModel aModel) {
		super(aModel);
	}

	public MyComboBox(Object[] items) {
		super(items);

	}

	public MyComboBox(Vector<?> items) {
		super(items);

	}

}
