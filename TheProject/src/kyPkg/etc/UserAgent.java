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
		JLabel label = new JLabel("アドレス(D):");
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
	 * JTextFieldに記述されたＵＲＬをJEditorPaneに表示する。
	 */
	public void useEditorPane(String url) {
		if (url.length() > 0) {
			try {
				htmlPane.setPage(url);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "読み込みエラーが発生しました。",
						"読み込みエラー", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// IEで表示する
	static public void openWithIE(String path) {
		runtimeKick("C:/Program Files/Internet Explorer/iexplore.exe", path);
	}

	// explorerでフォルダを表示する
	static public void openFolder(String path) {
		runtimeKick("explorer", path);
	}

	// explorerに任せてみる
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
		path = path.replaceAll("\\\\", "/");// 　’/’　で区切っていないとopenWithUAが動かない・・・
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

	// pdfを見る
	static public void openWithAcrobat(String path) {
		String reader = whichAcrobat();
		runtimeKick(reader, path);
	}

	// Excelで開く
	static public void openWithExcel(String path) {
		String app =  whichExcel();
		runtimeKick(app, path);
	}
	//TODO 定義ファイルから拾うようにしたい
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
	 * Look&FeelをOSのものに変更する。
	 */
	private void setLookAndFeelSuitable() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Componentをディスプレイの中心に配置する。
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
		String path = "C:/@qpr/home/238881000300/購入水準_【アイテム】ｘ【購入先（大区分）】_2012年08月13日～2012年08月13日_「世帯特性…主婦年代」 が 「～３９才」or 「～４９才」。または「モニター種別」 が 「単身者」or 「主婦」。.xls";
		path = "C:/@qpr/home/238881000300/購入水準_【アイテム】ｘ【購入先（大区分）】_2012年08月13日～2012年08月13日_「世帯特性…主婦年代」が「～３９才」or「～４９才」。または「モニター種別」が「単身者」or「主婦」。.xls";
		path = "C:/@qpr/home/238881000300/"
				+ "購入水準_【アイテム】ｘ【購入先（大区分）】_2012年08月13日～2012年08月13日_「世帯特性…主婦年代」が「～３９才」or「～４９才」。または「モニター種別」が「単身者」or「主婦」。.xls";
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
