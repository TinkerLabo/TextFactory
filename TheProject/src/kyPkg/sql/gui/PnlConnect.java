package kyPkg.sql.gui;

import static kyPkg.sql.ISAM.SCHEMA_INI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;

import kyPkg.uFile.FileUtil;
import kyPkg.uFile.HashMapUtil;
import kyPkg.util.DnDAdapter;

//############################################################
// 接続
//############################################################
public class PnlConnect extends JPanel implements InfDBHandler {
	private static final String CONNECT = "Connect";
	private static final String ALIAS_TXT = "ALIAS.TXT";

	public PnlSQL pnlSql;
	public JTabbedPane tabPane;
	public List<JComboBox> cmbList;
	protected JDBC_GUI insJDBC;

	@Override
	public JDBC_GUI getInsJDBC() {
		insJDBC = reConnectGui();
		return insJDBC;
	}

	private String recommendTable = "";
	public JTextField txUser;
	public JTextField txPass;
	public JTextField txUrl;
	public JButton btnConnect;
	public JTextField txfPath;
	public JComboBox cmbScheme;
	public JComboBox cmbCatalog;
	public JComboBox cmbUrl;
	public static String rootDir;
	protected List<String> cnnList;

	public String getSql() {
		return pnlSql.getSql(); // SQL
	}

	public JDBC_GUI getJDBC_GUI() {
		return new JDBC_GUI(getJurl(), getUser(), getPass());
	}

	public String getUser() {
		return txUser.getText();
	}

	public String getPass() {
		return txPass.getText();
	}

	public String getJurl() {
		return txUrl.getText();
	}

	// ############################################################################
	public PnlConnect(PnlSQL pnlSql, JTabbedPane tabP,
			List<JComboBox> cmbList) {
		super();
		initRes();
		this.pnlSql = pnlSql;
		this.tabPane = tabP;
		this.cmbList = cmbList;
		setLayout(null);
		// ************************************************
		int top = 5;
		int left = 40;
		int width = 500;
		JLabel labUser = new JLabel("User:");
		labUser.setBounds(left, top, 50, 20);
		add(labUser);
		left += 50 + 10;
		// ------------------------------------------------
		txUser = new JTextField("sa");
		txUser.setBounds(left, top, 100, 20);
		add(txUser);
		left += 100 + 10;
		// ------------------------------------------------
		JLabel jLabPass = new JLabel("Pw:");
		jLabPass.setBounds(left, top, 30, 20);
		add(jLabPass);
		left += 30 + 10;
		// ------------------------------------------------
		txPass = new JTextField("");
		txPass.setBounds(left, top, 100, 20);
		add(txPass);

		left += 100 + 50;
		// ------------------------------------------------
		btnConnect = new JButton(CONNECT);
		btnConnect.setBounds(left, top, 200, 20);
		add(btnConnect);

		// ************************************************
		top += 30;
		left = 40;
		// ------------------------------------------------
		JLabel labJURL = new JLabel("jUrl:");
		labJURL.setBounds(left, top, 40, 20);
		add(labJURL);
		left += 40;
		// ------------------------------------------------
		left = 100;
		txUrl = new JTextField(cnnList.get(0));
		txUrl.setBounds(left, top, width, 20);
		add(txUrl);

		// ************************************************
		top += 30;
		left = 40;
		// ------------------------------------------------
		JLabel labDnD = new JLabel("D&D:");
		labDnD.setBounds(left, top, 50, 20);
		add(labDnD);
		left += 50;
		// ------------------------------------------------
		left = 100;
		txfPath = new JTextField(
				"D&D => Schema.ini xxx.MDB sqlite.Db ALIAS.TXT  etc...");
		txfPath.setBounds(left, top, width, 20);
		txfPath.setBackground(Color.yellow);
		add(txfPath);

		// ************************************************
		top += 30 + 10;
		left = 40;
		// ------------------------------------------------
		JLabel labEx = new JLabel("example:");
		labEx.setBounds(left, top, 60, 20);
		add(labEx);
		left += 60;
		// ------------------------------------------------
		left = 100;
		cmbUrl = new JComboBox();
		cmbUrl.setBackground(Color.lightGray);
		for (String element : cnnList) {
			cmbUrl.addItem(element);
		}
		cmbUrl.setBounds(left, top, width, 20);
		add(cmbUrl);

		// ************************************************
		top += 30;
		left = 40;
		JLabel labCat = new JLabel("Catalog:");
		labCat.setBounds(left, top, 60, 20);
		add(labCat);
		left += 60;
		// ------------------------------------------------
		left = 100;
		cmbCatalog = new JComboBox();
		cmbCatalog.setBackground(Color.lightGray);
		cmbCatalog.setBounds(left, top, 200, 20);
		add(cmbCatalog);
		left += 200 + 10 + 20;
		// ------------------------------------------------
		JLabel labScheme = new JLabel("Scheme:");
		labScheme.setBounds(left, top, 60, 20);
		add(labScheme);
		left += 60 + 10;
		// ------------------------------------------------
		cmbScheme = new JComboBox();
		cmbScheme.setBackground(Color.lightGray);
		cmbScheme.setBounds(left, top, 200, 20);
		add(cmbScheme);

		// ---------------------------------------------------------------------
		// ファイルドラッグドロップの仕掛け
		// ---------------------------------------------------------------------
		new java.awt.dnd.DropTarget(txfPath, new DnDAdapter(txfPath) {
			// オーバーライドしている
			@Override
			public void setTargetText(String path) {
				recommendTable = "";
				txfPath.setText(path);
				String fileName = FileUtil.getFileName(path).toUpperCase();
				String parentPath = FileUtil.getParent(path);
				// System.out.println("fileName:" + fileName);
				if (fileName.equals(SCHEMA_INI)) {
					String url = "JDBC:ODBC:DRIVER={Microsoft Text Driver (*.txt; *.csv)};DefaultDir="
							+ parentPath
							+ ";DriverId=27;FIL=text;MaxBufferSize=2048;PageTimeout=5;";
					txUrl.setText(url);

					connect(tabPane); // ■ 接続 ＆ etc

				} else if (fileName.equals(ALIAS_TXT)) {
					HashMap<String, String> hMap = HashMapUtil
							.file2HashMapX(path);
					if (hMap != null) {
						String url = hMap.get("connect");
						String field = hMap.get("field");//TODO
						String key = hMap.get("key");//TODO
						recommendTable = hMap.get("table");
						if (url != null) {
							txUrl.setText("JDBC:ODBC:" + url);
						}
					}
					connect(tabPane); // ■ 接続 ＆ etc

				} else if (fileName.endsWith(".MDB")) {
					String url = "jdbc:odbc:DRIVER=Microsoft Access Driver (*.mdb);DBQ="
							+ path;
					txUrl.setText(url);
					connect(tabPane); // ■ 接続 ＆ etc
				} else {
					if (FileUtil.readHeader(path, "SQLite ")) {
						String url = "jdbc:sqlite:" + path;
						txUrl.setText(url);
					}

				}
			}
		});
		// ---------------------------------------------------------------------
		// コネクトボタン：DB接続 テーブル名一覧等を拾う
		// ---------------------------------------------------------------------
		btnConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Thread th1 = new Thread() {
					@Override
					public void run() {
						btnConnect.setEnabled(false);
						connect(tabPane); // ■ 接続 ＆ etc
						btnConnect.setEnabled(true);
					}
				};
				th1.start();
			}
		});
		// ---------------------------------------------------------------------
		// データベースURLを変更
		// ---------------------------------------------------------------------
		cmbUrl.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String wJUrl = (String) cmbUrl.getSelectedItem();
				txUrl.setText(wJUrl);
				// AIRS3
				// if (wJUrl.equals(jUrl1)){ jTxfUser.setText("sa");
				// } else if (wJUrl.equals(jUrl0)){
				// jTxfUser.setText("sa");
				// } else if (wJUrl.equals(jUrl9)){
				// jTxfUser.setText("AIRS3");
				// jTxfPass.setText("AIRS3");
				// } else if (wJUrl.equals(jUrl11)){
				// jTxfUser.setText("postgres");
				// } else if (wJUrl.equals(jUrl2)){
				// jTxfUser.setText("QPR");
				// } else if (wJUrl.equals(jUrl3)){
				// jTxfUser.setText("QPR");
				// } else if (wJUrl.equals(jUrl4)){
				// jTxfUser.setText("QPR");
				// } else if (wJUrl.equals(jUrl6)){
				// jTxfUser.setText("root");
				// jTxfPass.setText("fatmanri2m");
				// } else if (wJUrl.equals(jUrl7)){
				// jTxfUser.setText("");
				// jTxfPass.setText("");
				// } else { jTxfUser.setText("sa");
				// }
			}
		});
	}

	// #########################################################################
	// ## ＤＢに接続＆コンボボックスの内容を更新する
	// #########################################################################
	public void connect(JTabbedPane tabPane) {
		cmbScheme.setEnabled(false);
		cmbCatalog.setEnabled(false);
		if (cmbList != null) {
			for (JComboBox jComboBox : cmbList) {
				jComboBox.setEnabled(false);
			}
		}
		// cmbTable_X.setEnabled(false);// xx
		// cmbTable_L.setEnabled(false);// LL
		// cmbTable_R.setEnabled(false);// RR

		// String[] patStr = {"TABLE","VIEW","SYSTEM TABLE"};
		String[] patStr = { "TABLE", "VIEW" };
		JDBC_GUI jDbc = getJDBC_GUI();
		if (jDbc.getConnection() != null) {
			jDbc.popCatCombo(cmbCatalog); // カタログ情報
			jDbc.popSchCombo(cmbScheme); // 使用可能なスキーマ名
			if (cmbList != null) {
				for (JComboBox jComboBox : cmbList) {
					jDbc.popTblCombo(jComboBox, patStr); // xx テーブル名一覧
				}
			}

			// jDbc.popTblCombo(cmbTable_X, patStr); // xx テーブル名一覧
			// jDbc.popTblCombo(cmbTable_L, patStr); // LL テーブル名一覧
			// jDbc.popTblCombo(cmbTable_R, patStr); // RR テーブル名一覧
			jDbc.close();
			// alias.txtを使ってコネクトした場合に設定される
			if (cmbList != null) {
				if (!recommendTable.equals(""))
					cmbList.get(0).setSelectedItem(recommendTable);
			}
			cmbScheme.setEnabled(true);
			cmbCatalog.setEnabled(true);
			if (cmbList != null) {
				for (JComboBox jComboBox : cmbList) {
					jComboBox.setEnabled(true);
				}
			}
			// cmbTable_X.setEnabled(true);// xx
			// cmbTable_L.setEnabled(true);// LL
			// cmbTable_R.setEnabled(true);// RR
			tabPane.setSelectedIndex(1);
		} else {
			JOptionPane.showMessageDialog(null,
					"Login Error!?:" + jDbc.getEmsg());
		}
	}

	private void initRes() {
		rootDir = getRoot();
		cnnList = getConnectList();
	}

	public static List<String> getConnectList() {
		String path = "C:/@qpr/home/qpr/res/connectSample.txt";
		String pathX = "c:/connectSample.txt";
		if (new File(pathX).isFile() == false) {
			if (new File(path).isFile() == false) {
				writeConnectList(path);
			}
		} else {
			path = pathX;
		}
		List<String> list = kyPkg.uFile.ListArrayUtil.file2List(path);
		return list;
	}

	public static void writeConnectList(String path) {
		List<String> cnnList = new ArrayList();
		cnnList.add(
				"jdbc:odbc:DRIVER=Microsoft Access Driver (*.mdb);DBQ=C:/BLANKBOOK.MDB");
		kyPkg.uFile.ListArrayUtil.list2File(path, cnnList);
	}

	// -------------------------------------------------------------------------
	// プロパティのリセット ≪jdbcの再接続≫
	// -------------------------------------------------------------------------
	private JDBC_GUI reConnectGui() {
		insJDBC = getJDBC_GUI();

		// String jURL = getJurl(); // JURL
		// String user = getUser(); // ユーザ名
		// String pass = getPass(); // パスワード
		// JDBC_GUI insJDBC = new JDBC_GUI(jURL, user, pass);

		if (insJDBC.getConnection() == null) {
			JOptionPane.showMessageDialog(null,
					"@JDBC_GUI Error!:" + insJDBC.getEmsg());
			insJDBC = null;
		}
		return insJDBC;
	}

	@Override
	public void createSample() {
		JDBC_GUI jDbc = getJDBC_GUI();
		// JDBC_GUI jDbc = new JDBC_GUI(getJurl(), getUser(), getPass());
		if (jDbc.getConnection() != null) {
			jDbc.createSample();
			jDbc.close();
		} else {
			System.out.println(jDbc.getEmsg());
			JOptionPane.showMessageDialog(null,
					"Loginp2 Error!:" + jDbc.getEmsg());
		}
	}

	@Override
	public ComboBoxModel query2Cmodel() {
		String sql = pnlSql.getSql(); // SQL
		ComboBoxModel cmbModel = null;
		insJDBC = reConnectGui();
		if (insJDBC != null) {
			cmbModel = insJDBC.query2ComboModel(sql);
			insJDBC.close();
		}
		return cmbModel;
	}

	// -------------------------------------------------------------------------
	@Override
	public boolean queryExUpdate() {
		String sql = pnlSql.getSql(); // SQL
		insJDBC = reConnectGui();
		if (insJDBC != null) {
			try {
				if (insJDBC.executeUpdate(sql) == false) {
//					System.out.println("Error  sql:" + sql);
					return false;
				} else {
					connect(tabPane);
				}
			} catch (Exception se) {
				se.printStackTrace();
			}
			insJDBC.close();
		}
		tabPane.setSelectedIndex(0);
		return true;
	}

	@Override
	public ListModel query2Lmodel() {
		ListModel listModel = null;
		String sql = pnlSql.getSql();
		insJDBC = reConnectGui();
		if (insJDBC != null) {
			listModel = insJDBC.query2Listmodel(sql);
			insJDBC.close();
		}
		return listModel;
	}
	//	public boolean query2Grid(Jp_SortableGrid pnlGrid) {
	//		String sql = pnlSql.getSql(); // SQL
	//		insJDBC = reConnectGui();
	//		return  insJDBC.query2Grid( pnlGrid, sql);
	//	}
	//	public boolean query2Grid(Jp_SortableGrid pnlGrid) {
	//		DefaultTableModelMod tableModel = null;
	//		String sql = pnlSql.getSql(); // SQL
	//		insJDBC = reConnectGui();
	//		
	//		if (insJDBC != null) {
	//			HashMap<String, List<List>> map = insJDBC.query2HMap(sql);
	//			insJDBC.close();
	//			tableModel = new DefaultTableModelMod(map.get("matrix"),
	//					map.get("headers"));
	//		}
	//		if (tableModel != null) {
	//			pnlGrid.setTableModel(tableModel);
	//			return true;
	//		} else {
	//			return false;
	//		}
	//	}

	@Override
	public List<List> query2Matrix() {
		List<List> matrix = null;
		String sql = pnlSql.getSql(); // SQL
		insJDBC = reConnectGui();
		if (insJDBC != null) {
			matrix = insJDBC.query2Matrix(sql, true, -1);
			insJDBC.close();
		}
		return matrix;
	}

	@Override
	public String isExist(String wTnm) {
		JDBC_GUI jDbcG = reConnectGui();
		System.out.println("isExit:" + wTnm);
		String ans = "";
		if (jDbcG.isExist(wTnm)) {
			ans = wTnm + "is Exist";
		} else {
			ans = wTnm + "is Not Exist";
		}
		return ans;
	}

	@Override
	public void importFromText(String texPath, String table, String delimiter,
			boolean optHeader) {
		insJDBC = reConnectGui();
		if (insJDBC != null) {
			insJDBC.importFromText(texPath, table, delimiter, optHeader);
			insJDBC.close();
		}
		connect(tabPane);
		tabPane.setSelectedIndex(0);
	}

	// -------------------------------------------------------------------------
	// データベースファイルのルート
	// -------------------------------------------------------------------------
	private static String getRoot() {
		String pRoot = "";
		String wOsName = System.getProperty("os.name");
		if (wOsName.startsWith("Windows")) {
			String rootDir = globals.ResControl.getQprRootDir();
			pRoot = rootDir + "testDataBase/";
			new File(pRoot).mkdirs();
		} else if (wOsName.startsWith("Mac osx")) { // 仮
			pRoot = "/Users/ken/testDatabase/"; // 仮
			new File(pRoot).mkdirs();
		} else {
			System.out.println("unknown os name =>" + wOsName);
		}
		return pRoot;
	}

	@Override
	public void repaint() {
		super.repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// kyPkg.util.Ruler.drawRuler(g, this.getSize().width,
		// this.getSize().height, Color.white);
		// g.setColor(Color.BLACK);
	}

	public static void main(String[] args) {

	}
}
