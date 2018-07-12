package kyPkg.util;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//-----------------------------------------------------------------------------
public class InputDialog extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7536379210486624530L;
	JButton btnOK;
	JButton btnNG;
	JTextField txtField;
	String original;
	int status = 0; // okなら１が返る

	public String getValue() {
		return original;
	}

	public int getStatus() {
		return status;
	}

	// -------------------------------------------------------------------------
	public InputDialog(Frame parent, String title, boolean modal,
			String message, String defaultVal) {
		super(parent, title, modal);
		this.setLayout(new BorderLayout());
		setSize(300, 100);
		JTextArea textArea = new JTextArea(message);
		textArea.setOpaque(false);
		textArea.setEditable(false);
		this.add(textArea, BorderLayout.CENTER);

		System.out.println("pVal:" + defaultVal);
		original = defaultVal;
		txtField = new JTextField(defaultVal, defaultVal.length());
		JPanel btnPanel = new JPanel();
		btnOK = new JButton("OK");
		btnNG = new JButton("Cancel");
		btnPanel.add(btnOK);
		// btnPanel.add(btnNG);

		JPanel jp2 = new JPanel();
		jp2.setLayout(new BorderLayout());
		jp2.add(txtField, BorderLayout.NORTH);
		jp2.add(btnPanel, BorderLayout.SOUTH);
		this.add(jp2, BorderLayout.SOUTH);
		btnOK.addActionListener(this);
		btnNG.addActionListener(this);
		// pack();
		// ---------------------------------------------------------------------
		// ウィンドウを中央に配置
		// ---------------------------------------------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dlgSize = this.getSize();
		if (dlgSize.height > screenSize.height)
			dlgSize.height = screenSize.height;
		if (dlgSize.width > screenSize.width)
			dlgSize.width = screenSize.width;
		this.setSize(dlgSize.width, dlgSize.height);
		this.setLocation((screenSize.width - dlgSize.width) / 2,
				(screenSize.height - dlgSize.height) / 2);
	}

	// -------------------------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnOK) {
			status = 1;
			String wInput = txtField.getText();
			char[] cSrc = wInput.toCharArray();
			char[] cDes = original.toCharArray();
			for (int i = 0; i < cDes.length; i++) {
				if (i < cSrc.length) {
					cDes[i] = cSrc[i];
				} else {
					cDes[i] = ' ';
				}
			}
			original = new String(cDes);
		}
		setVisible(false);
	}

	// -------------------------------------------------------------------------
	public static void test() {
		String title = "ダイアログテスト";
		String msg = "タイトルを入力してください・・";
		String def = "1234567890ABC";
		kyPkg.util.InputDialog dialog = new kyPkg.util.InputDialog(null, title, true, msg, def);
		dialog.setVisible(true);
		System.out.println("val:" + dialog.getValue());
	}

	public static void main(String[] argv) {
		// System.setErr(System.out); // 標準エラー出力を標準出力へリダイレクト
		test();
	}
}
