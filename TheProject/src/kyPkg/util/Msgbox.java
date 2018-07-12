package kyPkg.util;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.*;

//-----+---------+---------+---------+---------+---------+---------+---------+
// Msgbox class
// �s�g�p��t
//		JOptionPane.showMessageDialog((Component)null,"�Ώۃt�@�C�����I�΂�Ă��܂���"
//			,"Warning...",JOptionPane.WARNING_MESSAGE);					return;
//-----+---------+---------+---------+---------+---------+---------+---------+
public class Msgbox {
	Component parentComponent;

	public Msgbox(Object pParentComponent) {
		parentComponent = (Component) pParentComponent;
	}

	// -+---------+---------+---------+---------+---------+---------+---------+
	// ���_�C�A���O��\��
	// @param message ��񃁃b�Z�[�W
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

	// �g������ new Msgbox(null).info("�t�@�C������ł�");

	public void info(String message) {
		JOptionPane.showMessageDialog(parentComponent, new JLabel(message),
				"Information...", JOptionPane.INFORMATION_MESSAGE);
	}

	// -+---------+---------+---------+---------+---------+---------+---------+
	// �x���_�C�A���O��\��
	// @param message �x�����b�Z�[�W
	// �g������ new Msgbox(null).warn("�t�@�C������ł�");
	// -+---------+---------+---------+---------+---------+---------+---------+
	public void warn(String message) {
		JOptionPane.showMessageDialog(parentComponent, new JLabel(message),
				"Warning...", JOptionPane.WARNING_MESSAGE);
	}

	// -+---------+---------+---------+---------+---------+---------+---------+
	// �G���[�_�C�A���O��\��
	// @param message �G���[���b�Z�[�W
	// -+---------+---------+---------+---------+---------+---------+---------+
	public void error(String message) {
		JOptionPane.showMessageDialog(parentComponent, new JLabel(message),
				"Error...", JOptionPane.ERROR_MESSAGE);
	}

	// -+---------+---------+---------+---------+---------+---------+---------+
	// Q?��\��
	// -+---------+---------+---------+---------+---------+---------+---------+
	public void question(String message) {
		JOptionPane.showMessageDialog(parentComponent, new JLabel(message),
				"?!", JOptionPane.QUESTION_MESSAGE);
	}

	// -+---------+---------+---------+---------+---------+---------+---------+
	// Q?��\��
	// -+---------+---------+---------+---------+---------+---------+---------+
	public void plain(String message) {
		JOptionPane.showMessageDialog(parentComponent, new JLabel(message),
				"?!", JOptionPane.PLAIN_MESSAGE);
	}

	// -+---------+---------+---------+---------+---------+---------+---------+
	// �m�F�_�C�A���O��\��
	// @param message �G���[���b�Z�[�W
	// -+---------+---------+---------+---------+---------+---------+---------+
	public boolean confirmYN(String message) {
		int ans = JOptionPane.showConfirmDialog((Component) null, message,
				"�m�F", JOptionPane.YES_NO_OPTION);
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
		int ans = JOptionPane.showConfirmDialog((Component) null, txArea, "�m�F",
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
				+ "yes,no,Cancel?", "�m�F", JOptionPane.YES_NO_CANCEL_OPTION);
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
	// �_�C�A���O��\������
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
		dialog.setLocationRelativeTo(null);// �Z���^�����O�E�E
		InputMap imap = dialog.getRootPane().getInputMap(
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close-it");
		dialog.getRootPane().getActionMap().put("close-it", act);
		dialog.setVisible(true);
	}

	public static void test01() {
		// new Msgbox(null).warn("�x���ł�");
		// new Msgbox(null).info("���m�点�ł�");
		// new Msgbox(null).error("�G���[�������I");
		// new Msgbox(null).confirmYN("confirm");
		java.util.List<String> info = new ArrayList();
		info.add("message1");
		info.add("message2");
		info.add("message3");
		new Msgbox(null).info(info);
	}

}
