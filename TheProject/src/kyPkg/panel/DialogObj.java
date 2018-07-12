package kyPkg.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import kyPkg.etc.StackCtrl;
import kyPkg.util.Banner;

public class DialogObj {
	private JDialog dialog;
	private static StackCtrl stackCtrl = new StackCtrl();

	public DialogObj() {
		super();
	}

	// ------------------------------------------------------------------------
	// Dialog util
	// ------------------------------------------------------------------------
	public void showDialog(Component comp, String title) {
		Object owner = stackCtrl.peek();
		if (owner instanceof Dialog) {
			System.out.println("## showDialog<1> ##" + title);
			dialog = new JDialog((Dialog) owner, title, true);
		} else if (owner instanceof Frame) {
			System.err.println("## showDialog<2> ?!##" + title);
			dialog = new JDialog((Frame) owner, title, true);
		} else {
			System.out.println("## showDialog<3> ##" + title);
			dialog = new JDialog((Frame) null, title, true);
		}
		//---------------------------------------------------------------------
		//	Image im = Toolkit.getDefaultToolkit().getImage("./image/cool.png");
		//	dialog.setIconImage(im);
		// System.out.println("pushOwner<" + title + ">:" + dialog.hashCode());
		//---------------------------------------------------------------------
		stackCtrl.push(dialog, title);
		dialog.setSize(comp.getSize());

		Container container = dialog.getContentPane();
		container.setLayout(new BorderLayout());
		container.add(comp, BorderLayout.CENTER);
		dialog.setLocationRelativeTo(null);// センタリング
		//---------------------------------------------------------------------
		// エスケープキーが押されたら閉じる
		//---------------------------------------------------------------------
		AbstractAction action = new AbstractAction("close-it") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		};
		InputMap imap = dialog.getRootPane()
				.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close-it");
		dialog.getRootPane().getActionMap().put("close-it", action);
		// dialog.getDefaultCloseOperation();
		// dialog.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		//		dialog.addWindowListener(new WindowListener() {
		//			@Override
		//			public void windowOpened(WindowEvent arg0) {
		//			}
		//
		//			@Override
		//			public void windowIconified(WindowEvent arg0) {
		//			}
		//
		//			@Override
		//			public void windowDeiconified(WindowEvent arg0) {
		//			}
		//
		//			@Override
		//			public void windowDeactivated(WindowEvent arg0) {
		//			}
		//
		//			@Override
		//			public void windowClosing(WindowEvent arg0) {
		//			}
		//
		//			@Override
		//			//---------------------------------------------------------------------
		//			// ダイアログを閉じた場合の動作
		//			//---------------------------------------------------------------------
		//			public void windowClosed(WindowEvent arg0) {
		//				stackCtrl.pop();
		//			}
		//
		//			@Override
		//			public void windowActivated(WindowEvent arg0) {
		//			}
		//		});
		dialog.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				//TODO	ダイアログオブジェクトを除去する
				System.out.println(
						"################################# ダイアログオブジェクトを除去する?");
				closeDialog();
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
			}
		});
		dialog.setVisible(true);
	}

	// ------------------------------------------------------------------------
	//	ダイアログを全部閉じる
	// ------------------------------------------------------------------------
	public static void destroyAllDialogs() {
		Banner.bannerPrint(30, "destroyAllDialogs");
		while (closeDialog())
			;
	}

	public static boolean closeDialog() {
		Dialog dialog = (Dialog) stackCtrl.peek();
		if (dialog != null) {
			System.err.println("dispose:" + dialog.hashCode());
			dialog.setVisible(false);
			dialog.dispose();
			dialog = null;
		} else {
			System.err.println("dialog == null????!!!");
		}
		Object object = stackCtrl.pop();
		if (object instanceof Dialog) {
			System.err.println("◆◆◆Dialog:");
			dialog = (JDialog) object;
			return true;
		} else if (object instanceof Frame) {
			System.err.println("◆◆◆Frame:");
			return false;
		} else {
			System.err.println("◆◆◆Other?!:");
			return false;
		}

	}

	// ------------------------------------------------------------------------
	// アクセッサ
	// ------------------------------------------------------------------------
	public void dispose() {
		if (dialog != null)
			dialog.dispose();
	}

	public void close() {
		dialog.setVisible(false);
	}

	public void setVisible(boolean stat) {
		dialog.setVisible(stat);
	}
}
