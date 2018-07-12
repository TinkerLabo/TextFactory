package kyPkg.frame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class BaseFrame extends JFrame {
	private static final long serialVersionUID = -5798707280988524112L;

	private Container container;
	private String icon = ""; // "image/xDead.gif";

	public BaseFrame(JPanel panel, int width, int height) {
		super();
		container = this.getContentPane();
		container.setLayout(new BorderLayout());
		container.add(panel, BorderLayout.CENTER);
		setLookAndFeel();
		setICON();
		pack();
		this.setSize(width, height);
		centering();
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	// ---------------------------------------------------------------------
	// Look �� Feel�𒲐�
	// ---------------------------------------------------------------------
	private void setLookAndFeel() {

		// import javax.swing.UIManager.*;

		String wOsName = System.getProperty("os.name");
		System.out.println("wOsName:" + wOsName);
		if (wOsName.startsWith("Windows")) {
			try {
				UIManager
						.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception e) {
				// e.printStackTrace();
				JFrame.setDefaultLookAndFeelDecorated(true);
			}
		} else {
			JFrame.setDefaultLookAndFeelDecorated(true);
		}
	}

	// ---------------------------------------------------------------------
	// ���̃t�H�[���Ŏg�p����A�C�R��
	// ---------------------------------------------------------------------
	private void setICON() {
		if (!icon.equals("")) {
			Image im = Toolkit.getDefaultToolkit().getImage(icon);
			this.setIconImage(im);
		}
	}

	// ---------------------------------------------------------------------
	// �E�B���h�E�𒆉��ɔz�u
	// ---------------------------------------------------------------------
	private void centering() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		this.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
	}

	public static void main(String args[]) {
		// �K���ȃp�l����p�ӂ���
		final JPanel testP = new JPanel(new BorderLayout());
		final JTextField txtField = new JTextField("????");
		final JButton btnTest = new JButton("Test");
		testP.add(txtField, BorderLayout.CENTER);
		testP.add(btnTest, BorderLayout.SOUTH);
		btnTest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// new DialogTemplate("tester", 200, 100, txtField);
			}
		});
		// -----------------------------�����܂Ńp�l��

		System.setErr(System.out);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new BaseFrame(testP, 640, 480);
			}
		});
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
