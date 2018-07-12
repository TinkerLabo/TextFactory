package kyPkg.util;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.*;

//-----+---------+---------+---------+---------+---------+---------+---------+
// Msgbox class
// 《使用例》
//		JOptionPane.showMessageDialog((Component)null,"対象ファイルが選ばれていません"
//			,"Warning...",JOptionPane.WARNING_MESSAGE);					return;
//-----+---------+---------+---------+---------+---------+---------+---------+
public class Msgbox {
	Component parentComponent;

	public Msgbox(Object pParentComponent) {
		parentComponent = (Component) pParentComponent;
	}

	// -+---------+---------+---------+---------+---------+---------+---------+
	// 情報ダイアログを表示
	// @param message 情報メッセージ
	// -+---------+---------+---------+---------+---------+---------+---------+
	public void info(java.util.List<String> info) {
		String messages = "";
		for (String message : info) {
			messages += message + "\n";
		}
		JOptionPane.showMessageDialog(parentComponent,
				new JTextArea(messages.trim()), "Information...",
				JOptionPane.INFORMATION_MESSAGE);
	}

	// 使い方＞ new Msgbox(null).info("ファイルが空です");

	public void info(String message) {
		JOptionPane.showMessageDialog(parentComponent, new JLabel(message),
				"Information...", JOptionPane.INFORMATION_MESSAGE);
	}

	// -+---------+---------+---------+---------+---------+---------+---------+
	// 警告ダイアログを表示
	// @param message 警告メッセージ
	// 使い方＞ new Msgbox(null).warn("ファイルが空です");
	// -+---------+---------+---------+---------+---------+---------+---------+
	public void warn(String message) {
		JOptionPane.showMessageDialog(parentComponent, new JLabel(message),
				"Warning...", JOptionPane.WARNING_MESSAGE);
	}

	// -+---------+---------+---------+---------+---------+---------+---------+
	// エラーダイアログを表示
	// @param message エラーメッセージ
	// -+---------+---------+---------+---------+---------+---------+---------+
	public void error(String message) {
		JOptionPane.showMessageDialog(parentComponent, new JLabel(message),
				"Error...", JOptionPane.ERROR_MESSAGE);
	}

	// -+---------+---------+---------+---------+---------+---------+---------+
	// Q?を表示
	// -+---------+---------+---------+---------+---------+---------+---------+
	public void question(String message) {
		JOptionPane.showMessageDialog(parentComponent, new JLabel(message),
				"?!", JOptionPane.QUESTION_MESSAGE);
	}

	// -+---------+---------+---------+---------+---------+---------+---------+
	// Q?を表示
	// -+---------+---------+---------+---------+---------+---------+---------+
	public void plain(String message) {
		JOptionPane.showMessageDialog(parentComponent, new JLabel(message),
				"?!", JOptionPane.PLAIN_MESSAGE);
	}

	// -+---------+---------+---------+---------+---------+---------+---------+
	// 確認ダイアログを表示
	// @param message エラーメッセージ
	// -+---------+---------+---------+---------+---------+---------+---------+
	public boolean confirmYN(String message) {
		int ans = JOptionPane.showConfirmDialog((Component) null, message,
				"確認", JOptionPane.YES_NO_OPTION);
		if (ans == JOptionPane.YES_OPTION) {
//			System.out.println("confirmYN @Msgbox.java yes!");
			return true;
		} else {
//			System.out.println("confirmYN @Msgbox.java NO!!");
			return false;
		}
	}

	public boolean confirmYN(java.util.List<String> info) {
		String messages = "";
		for (String message : info) {
			messages += message + "\n";
		}
		JTextArea txArea = new JTextArea(messages.trim());
		txArea.setOpaque(false);
		int ans = JOptionPane.showConfirmDialog((Component) null, txArea, "確認",
				JOptionPane.YES_NO_OPTION);
		if (ans == JOptionPane.YES_OPTION) {
//			System.out.println("confirmYN @Msgbox.java yes!");
			return true;
		} else {
//			System.out.println("confirmYN @Msgbox.java NO!!");
			return false;
		}
	}

	public int confirmYNC(String message) {
		int ans = JOptionPane.showConfirmDialog((Component) null, message
				+ "yes,no,Cancel?", "確認", JOptionPane.YES_NO_CANCEL_OPTION);
		if (ans == JOptionPane.YES_OPTION) {
			System.out.println("confirmYNC @Msgbox.java  yes!");
			return 1;
		} else if (ans == JOptionPane.NO_OPTION) {
			System.out.println("confirmYNC @Msgbox.java  NO!!");
			return -1;
		} else {
			return 0;
		}
	}

	// ########################################################################
	// ## main
	// ########################################################################
	public static void main(String[] argv) {
		test01();
	}

	// ---------------------------------------------------------------
	// ダイアログを表示する
	// ex> kyPkg.util.Msgbox.showDialog(comp,"title");
	// ---------------------------------------------------------------
	public static void testShowDialog(Component comp, String title) {
		final JDialog dialog = new JDialog((JFrame) null, title, true);
		dialog.setSize(comp.getSize());
		Container cnt = dialog.getContentPane();
		cnt.setLayout(new BorderLayout());
		cnt.add(comp, BorderLayout.CENTER);
		AbstractAction act = new AbstractAction("close-it") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		};
		dialog.setLocationRelativeTo(null);// センタリング・・
		InputMap imap = dialog.getRootPane().getInputMap(
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close-it");
		dialog.getRootPane().getActionMap().put("close-it", act);
		dialog.setVisible(true);
	}

	public static void test01() {
		// new Msgbox(null).warn("警告です");
		// new Msgbox(null).info("お知らせです");
		// new Msgbox(null).error("エラーが発生！");
		// new Msgbox(null).confirmYN("confirm");
		java.util.List<String> info = new ArrayList();
		info.add("message1");
		info.add("message2");
		info.add("message3");
		new Msgbox(null).info(info);
	}

}
