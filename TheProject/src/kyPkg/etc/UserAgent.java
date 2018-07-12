package kyPkg.etc;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.*;

public class UserAgent extends JFrame {
	private JTextField txField;
	private JButton btnMove;
	private JEditorPane htmlPane;

	public UserAgent() {
		super("Open Sessame");
		setLookAndFeelSuitable();
		JLabel label = new JLabel("�A�h���X(D):");
		txField = new JTextField();
		txField.setFocusAccelerator('d');
		// txField.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// String url = txField.getText().trim();
		// useEditorPane(url);
		// }
		// });

		btnMove = new JButton("Agent(A)");
		btnMove.setMnemonic('m');
		btnMove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String path = txField.getText().trim();
				UserAgent.openWithUA(path);
			}
		});
		htmlPane = new JEditorPane();
		htmlPane.setEditable(false);
		htmlPane.setContentType("text/html");

		Container container = getContentPane();
		container.setLayout(new BorderLayout());

		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(label, BorderLayout.WEST);
		northPanel.add(txField, BorderLayout.CENTER);
		northPanel.add(btnMove, BorderLayout.EAST);

		JScrollPane scroll = new JScrollPane(htmlPane);
		scroll.setPreferredSize(new Dimension(400, 400));
		scroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		container.add(northPanel, BorderLayout.NORTH);
		container.add(scroll, BorderLayout.CENTER);

		pack();

		setCenterLocation(this);
	}

	/**
	 * JTextField�ɋL�q���ꂽ�t�q�k��JEditorPane�ɕ\������B
	 */
	public void useEditorPane(String url) {
		if (url.length() > 0) {
			try {
				htmlPane.setPage(url);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "�ǂݍ��݃G���[���������܂����B",
						"�ǂݍ��݃G���[", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// IE�ŕ\������
	static public void openWithIE(String path) {
		runtimeKick("C:/Program Files/Internet Explorer/iexplore.exe", path);
	}

	// explorer�Ńt�H���_��\������
	static public void openFolder(String path) {
		runtimeKick("explorer", path);
	}

	// explorer�ɔC���Ă݂�
	static public void openWithExplorer(String path) {
		runtimeKick("explorer", path);
	}

	static public void openWithUA_org(String path) {
		path = path.toLowerCase();
		if (path.endsWith(".pdf")) {
			openWithAcrobat(path);
		} else if (path.endsWith(".xls")) {
			openWithExcel(path);
		} else {
			openWithExplorer(path);
		}
	}

	static public void openWithUA(String path) {
		path = path.replaceAll("\\\\", "/");// �@�f/�f�@�ŋ�؂��Ă��Ȃ���openWithUA�������Ȃ��E�E�E
		System.out.println("#@openWithUA path:" + path);
		openWithUA("browse", path);
	}

	public static void openWithUA(String method, String location) {
		Desktop desktop = Desktop.getDesktop();
		try {
			if (method.equals("browse")) {
				desktop.browse(new URI(location));
			} else if (method.equals("mail")) {
				desktop.mail(new URI(location));
			} else if (method.equals("edit")) {
				desktop.edit(new File(location));
			} else if (method.equals("open")) {
				desktop.open(new File(location));
			} else if (method.equals("print")) {
				desktop.print(new File(location));
			}
		} catch (URISyntaxException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private static String whichAcrobat() {
		String acroRd = "C:/Program Files/Adobe/Reader 10.0/Reader/AcroRd32.exe";
		acroRd = "C:/Program Files/Adobe/Reader 10.0/Reader/AcroRd32.exe";
		acroRd = "C:/Program Files (x86)/Adobe/Reader 10.0/Reader/AcroRd32.exe";
		return acroRd;
	}

	// pdf������
	static public void openWithAcrobat(String path) {
		String reader = whichAcrobat();
		runtimeKick(reader, path);
	}

	// Excel�ŊJ��
	static public void openWithExcel(String path) {
		String app =  whichExcel();
		runtimeKick(app, path);
	}
	//TODO ��`�t�@�C������E���悤�ɂ�����
	private static String whichExcel() {
		String app = "";
		app = "C:/Program Files/Microsoft Office/Office14/EXCEL.EXE";
		if (!new File(app).isFile()) {
			app = "C:/Program Files/Microsoft Office/Office10/EXCEL.EXE";
			if (!new File(app).isFile()) {
				app = "explorer";
			}
		}
		return app;
	}

	
	static boolean runtimeKick(String app, String url) {
		try {
			Runtime.getRuntime().exec(new String[] { app, url });
		} catch (IOException exc) {
			exc.printStackTrace();
		}
		return true;
	}

	/**
	 * Look&Feel��OS�̂��̂ɕύX����B
	 */
	private void setLookAndFeelSuitable() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Component���f�B�X�v���C�̒��S�ɔz�u����B
	 * 
	 * @param component
	 *            Component
	 */
	private void setCenterLocation(Component component) {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (d.width - component.getWidth()) / 2;
		int y = (d.height - component.getHeight()) / 2;
		component.setLocation(x, y);
	}

	public static void testOpenWithUA() {
		String path = "C:/@qpr/home/238881000300/�w������_�y�A�C�e���z���y�w����i��敪�j�z_2012�N08��13���`2012�N08��13��_�u���ѓ����c��w�N��v �� �u�`�R�X�ˁvor �u�`�S�X�ˁv�B�܂��́u���j�^�[��ʁv �� �u�P�g�ҁvor �u��w�v�B.xls";
		path = "C:/@qpr/home/238881000300/�w������_�y�A�C�e���z���y�w����i��敪�j�z_2012�N08��13���`2012�N08��13��_�u���ѓ����c��w�N��v���u�`�R�X�ˁvor�u�`�S�X�ˁv�B�܂��́u���j�^�[��ʁv���u�P�g�ҁvor�u��w�v�B.xls";
		path = "C:/@qpr/home/238881000300/"
				+ "�w������_�y�A�C�e���z���y�w����i��敪�j�z_2012�N08��13���`2012�N08��13��_�u���ѓ����c��w�N��v���u�`�R�X�ˁvor�u�`�S�X�ˁv�B�܂��́u���j�^�[��ʁv���u�P�g�ҁvor�u��w�v�B.xls";
		UserAgent.openWithUA(path);
	}

	public static void test() {
		JFrame frame = new UserAgent();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// test();
		testOpenWithUA();
	}

}
