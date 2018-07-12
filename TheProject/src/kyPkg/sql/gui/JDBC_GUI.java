package kyPkg.sql.gui;

import kyPkg.filter.Inf_oClosure;
import kyPkg.sql.JDBC;
import kyPkg.sql.Query2FileThread;
import kyPkg.uFile.File49ers;
import kyPkg.uFile.FileUtil;

import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.*; //import javax.swing.table.*;

import static kyPkg.sql.ServerConnecter.getDEF_JURL;
import static kyPkg.sql.ServerConnecter.getDEF_PASS;
import static kyPkg.sql.ServerConnecter.getDEF_USER;

import java.io.*;

public class JDBC_GUI extends JDBC {

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// �R���X�g���N�^
	// -----+---------+---------+---------+---------+---------+---------+---------+
	public JDBC_GUI() {
		super.getConnection(getDEF_JURL(), getDEF_USER(), getDEF_PASS());
	}

	public JDBC_GUI(String jURL, String user, String password) {
		super.getConnection(jURL, user, password);
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// queryCombo �N�G���[���ʂ�Combo�ɗ�������
	// -----+---------+---------+---------+---------+---------+---------+---------+
	public synchronized ComboBoxModel query2ComboModel(String sql) {
		DefaultComboBoxModel pDmdl = new DefaultComboBoxModel();
		try {
			Statement stmt = createStatement(); // �X�e�[�g�����g����
			ResultSet rSet = stmt.executeQuery(sql); // �N�G���[���s
			rs2ComboModel(pDmdl, rSet);
			stmt.close();
		} catch (Exception e) {
			eMsg = e.toString();
			e.printStackTrace();
		}
		return pDmdl;
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// �����������k������ �N�G���[���ʂ�List�ɗ�������
	// -----+---------+---------+---------+---------+---------+---------+---------+
	public synchronized DefaultListModel query2Listmodel(String sql) {
		return query2Listmodel(sql, "");
	}

	public synchronized DefaultListModel query2Listmodel(String sql,
			String delimiter) {
		DefaultListModel model = new DefaultListModel();
		try {
			Statement stmt = createStatement(); // �X�e�[�g�����g����
			ResultSet rSet = stmt.executeQuery(sql); // �N�G���[���s
			rs2ListModel(model, rSet, delimiter);
			stmt.close();
		} catch (Exception e) {
			eMsg = e.toString();
			e.printStackTrace();
		}
		return model;
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// queryTable �N�G���[���ʂ�Table�ɗ������ށB ��"util.ProtTModel�N���X"�Ɉˑ�
	// -----+---------+---------+---------+---------+---------+---------+---------+
	// public synchronized AbstractTableModel query2Tmodel(String sql) {
	// AbstractTableModel model = null;
	//	private HashMap<String, List<List>> query2HMap( String sql) {
	//		HashMap<String, List<List>> map = query2HMap(sql);
	//		close();
	//		return map;
	//	}

	//	private boolean query2Grid(Jp_SortableGrid pnlGrid, String sql) {
	//		DefaultTableModelMod tableModel = null;
	//		HashMap<String, List<List>> map = query2HMap(sql);
	//		tableModel = new DefaultTableModelMod(map.get("matrix"),
	//				map.get("headers"));
	//		if (tableModel != null) {
	//			pnlGrid.setTableModel(tableModel);
	//			return true;
	//		} else {
	//			return false;
	//		}
	//	}

	// public synchronized DefaultTableModelMod query2Tmodel(String sql) {
	// HashMap<String, List<List>> map = query2HMap(sql);
	// return new DefaultTableModelMod(map.get("matrix"), map.get("headers"));
	// }

	// public synchronized DefaultTableModelMod query2Tmodel_org(String sql) {
	// DefaultTableModelMod model = null;
	// ResultSet rSet = null;
	// try {
	// Statement stmt = createStatement(); // �X�e�[�g�����g����
	//
	// // �g�����U�N�V�����J�n
	// if (getDriverClassName().contains("sqlite")) {
	// // sqlite�̃p�t�H�[�}���X���グ�����E�E�Edefault��2000
	// stmt.executeUpdate("PRAGMA cache_size = 8000");
	// }
	// rSet = stmt.executeQuery(sql); // �N�G���[���s
	// // System.out.println("## queryTable ##");
	// model = new DefaultTableModelMod(rSet, true);
	// // ProtTModel��AbstractTableModel��Extend���Ă��āE�E
	// // getColumnClass�̓f�[�^���i�[���Ă���Vector���̃N���X��Ԃ�
	// // ����āE�E����ɑΉ�����
	// stmt.close();
	// } catch (Exception e) {
	// eMsg = e.toString();
	// e.printStackTrace();
	// }
	// return model;
	// }

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// rs2Combo RESULT��Combo�ɗ�������
	// -----+---------+---------+---------+---------+---------+---------+---------+
	int rs2ComboModel(DefaultComboBoxModel model, ResultSet rSet)
			throws SQLException {
		int lcnt = 0;
		ResultSetMetaData rsMeta = rSet.getMetaData();
		int colmax = rsMeta.getColumnCount(); // ���ʃZ�b�g�̗�
		while (rSet.next()) {
			lcnt++;
			String wRec = "";
			for (int i = 0; i < colmax; i++) {
				wRec = wRec + rSet.getString(i + 1) + " \t";
			}
			model.addElement(wRec);
		}
		return lcnt;
	}

	private int rs2ListModel(DefaultListModel model, ResultSet rSet,
			String delimiter) throws SQLException {
		int lcnt = 0;
		ResultSetMetaData rsMeta = rSet.getMetaData();
		int colmax = rsMeta.getColumnCount(); // ���ʃZ�b�g�̗�
		while (rSet.next()) {
			lcnt++;
			String wRec = "";
			for (int i = 0; i < colmax; i++) {
				wRec = wRec + rSet.getString(i + 1) + delimiter;
			}
			model.addElement(wRec);
		}
		return lcnt;
	}

	// ---+---------+---------+---------+---------+---------+---------+---------+
	// �������������r���������� �T���v���f�[�^�쐬
	// http://archive.guitarplayer.com/archive/gear/setup.shtml
	// ---+---------+---------+---------+---------+---------+---------+---------+
	void createSample() {
		// make an empty table
		// by declaring the id column IDENTITY, the db will automatically
		// generate unique values for new rows- useful for row keys
		executeUpdate("CREATE TABLE sample_table "
				+ "( id INTEGER IDENTITY, str_col VARCHAR(256), num_col INTEGER)");
		// add some rows - will create duplicates if run more then once
		// the id column is automatically generated
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('�f�����������@�@',20000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('�s�����������@�@',17000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('�l�����������@�@',15000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('�e�����������@�@',10000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('�f�������������@', 9000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('�d��������������', 8000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('�m��������������', 7000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('�c���������@�@�@', 6000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('Rickenbacker', 5000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('Stromberg   ', 3000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('Danelectro  ', 2000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('YAMAHA      ',  100 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('HOFNER      ',  200 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('���Ԃ���@�@�@�@',20000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('�g���r�X�@�@�@�@',17000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('�}�[�`���@�@�@�@',15000 )");
	}

	public int query2File(Inf_oClosure writer, String pSql, boolean headOpt,
			String suffix, boolean trimOption) {
		Query2FileThread thread = new Query2FileThread(this, writer, pSql, pSql,
				trimOption);
		thread.setSuffix(suffix);
		thread.setHeadOpt(headOpt);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int counter = thread.getCounter();
		return counter;
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// popCatCombo �J�^���O�����R���{�{�b�N�X�ɕ\��
	// -----+---------+---------+---------+---------+---------+---------+---------+
	protected void popCatCombo(JComboBox pBox) {
		try {
			List list = getCatalogList();
			pBox.setModel(new DefaultComboBoxModel(new Vector(list)));
			pBox.setSelectedItem(getCatalog());
			pBox.setEnabled(list.size() > 0);
		} catch (Exception e) {
			pBox.setEnabled(false);
		}
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// popSchCombo �g�p�\�ȃX�L�[�}�����R���{�{�b�N�X�ɕ\��
	// -----+---------+---------+---------+---------+---------+---------+---------+
	protected void popSchCombo(JComboBox pBox) {
		// System.out.println("#debug# popSchCombo!");
		List list = getSchemeList();
		if (list != null) {
			pBox.setModel(new DefaultComboBoxModel(new Vector(list)));
			pBox.setEnabled(list.size() > 0);
		} else {
			pBox.setModel(new DefaultComboBoxModel(new Vector()));
			pBox.setEnabled(false);
		}
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// popTblString �e�[�u�����ꗗ�𕶎���Ƃ��ĕԂ�
	// -----+---------+---------+---------+---------+---------+---------+---------+
	// �������ɂ͈ȉ��̂悤�ȃe�[�u���̎�ʂ�\���e�[�u����n��
	// String[] patStr = {"TABLE","VIEW","SYSTEM TABLE",
	// "GLOBAL TEMPORARY","LOCAL TEMPORARY","ALIAS","SYNONYM"};
	// instance.popTblString(wCombo,patStr);
	// -----+---------+---------+---------+---------+---------+---------+---------+
	public String popTblString(String delimiter, String[] patStr) {
		List<String> list = getTableNameList(patStr,
				new int[] { 1, 2, 3, 4, 5 }, delimiter);
		if (list != null) {
			return kyPkg.util.KUtil.list2StringLF(list, FileUtil.LF);
		} else
			return null;
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// popTblCombo �e�[�u�����ꗗ���R���{�{�b�N�X�ɕ\��
	// -----+---------+---------+---------+---------+---------+---------+---------+
	// ��g�p���
	// �������ɂ͈ȉ��̂悤�ȃe�[�u���̎�ʂ�\���e�[�u����n��
	// String[] patStr = {"TABLE","VIEW","SYSTEM TABLE",
	// "GLOBAL TEMPORARY","LOCAL TEMPORARY","ALIAS","SYNONYM"};
	// instance.popTblCombo(wCombo,patStr);
	// -----+---------+---------+---------+---------+---------+---------+---------+
	protected void popTblCombo(JComboBox pBox, String[] pattern) {
		// System.out.println("#debug# popTblCombo!");
		List list = getTableNameList(pattern);
		pBox.setEnabled(false);
		if (list != null) {
			pBox.setModel(new DefaultComboBoxModel(new Vector(list)));
			if (list.size() > 0) {
				pBox.setEnabled(true);
				pBox.setSelectedIndex(0);
			}
		}
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// popFldCombo �t�B�[���h�����R���{�{�b�N�X�Ɉꗗ�\��
	// -----+---------+---------+---------+---------+---------+---------+---------+
	protected void popFldCombo(JComboBox pBox, String pTable) {
		List list = getFieldsList(pTable, 4); // 4.COLUMN_NAME String => ��
		if (list != null) {
			pBox.setModel(new DefaultComboBoxModel(new Vector(list)));
			pBox.setEnabled(list.size() > 0);
		} else {
			pBox.setEnabled(false);
		}
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// popFldList �t�B�[���h�������X�g�{�b�N�X�Ɉꗗ�\��
	// -----+---------+---------+---------+---------+---------+---------+---------+
	protected void popFldList(JList pBox, String pTable) {
		List list = getFieldsList(pTable, 4); // 4.COLUMN_NAME String => ��
		if (list != null) {
			DefaultListModel values = new DefaultListModel();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				values.addElement(iter.next());
			}
			pBox.setModel(values);
			pBox.setEnabled(values.size() > 0);
		} else {
			pBox.setEnabled(false);
			System.out.println("# Error on popFldList");
		}
	}

	// ---+---------+---------+---------+---------+---------+---------+---------+
	// �G���[���b�Z�[�W��Ԃ�
	// ---+---------+---------+---------+---------+---------+---------+---------+
	public String getEmsg() {
		return eMsg;
	}

	// 1�s�ڂɃw�b�_�[��񂪂���ꍇ => headOption=true
	public long importFromText(String path, String table, String delimiter,
			boolean headOption) {
		long count = 0;
		String wRec = "";
		try {

			File49ers insF49 = new File49ers(path);
			String encoding = insF49.getEncoding();

			// �f�[�^�̐�ǂ݂����ăt�B�[���h�^��ݒ肷��B
			int checkCount = 5;
			HashMap<String, List<Object>> map = AnalyzeText.analyzeIt(path,
					encoding, delimiter, checkCount, headOption);

			StringBuffer defs = new StringBuffer();
			StringBuffer flds = new StringBuffer();
			StringBuffer dums = new StringBuffer();
			for (int j = 0; j < map.get(AnalyzeText.NAME).size(); j++) {
				if (j > 0) {
					defs.append(",");
					flds.append(",");
					dums.append(",");
				}
				defs.append(map.get(AnalyzeText.NAME).get(j) + " "
						+ map.get(AnalyzeText.TYPE).get(j));
				flds.append(map.get(AnalyzeText.NAME).get(j));
				dums.append("?");
			}

			// <<DROP>> �e�[�u�������łɑ��݂���ꍇ����
			if (isExist(table)) {
				String dropSql = "DROP TABLE " + table;
				System.out.println(
						"table:" + table + "is existed!!\nSQL:" + dropSql);
				executeUpdate(dropSql);
			}

			// <<CREATE>> �e�[�u�����`����
			String createSql = "CREATE TABLE " + table + " (" + defs.toString()
					+ ")";
			System.out.println("createSql:" + createSql);
			executeUpdate(createSql);

			// �v���y�A�h�X�e�[�g�����g����
			String insertSql = "INSERT INTO " + table + " (" + flds.toString()
					+ ") VALUES(" + dums.toString() + ")";
			System.out.println("insertSql:" + insertSql);

			if (openPrep("anyHash", insertSql) == false) {
				System.out.println("#Error �v���y�A�h�X�e�[�g�����g��`���s");
			}

			// �g�����U�N�V�����J�n
			if (getDriverClassName().contains("sqlite")) {
				executeUpdate("BEGIN");
			}
			int maxCol = map.get(AnalyzeText.NAME).size();
			System.out.println("#DBG# maxCol:" + maxCol);

			// -+---------+---------+---------+---------+---------+---------+
			// BODY
			// -+---------+---------+---------+---------+---------+---------+
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(path), encoding));
			while ((wRec = br.readLine()) != null) {
				String[] xCel = wRec.split(delimiter, maxCol);
				// TODO �Z����������Ȃ��ꍇ�ɖ�肪����E�E�E�s�����̃Z����₦�Ȃ����낤���H�H
				if (xCel.length != maxCol) {
					System.out
							.println("#DBG# #ERROR xCel.length:" + xCel.length);
				}
				// XXX �X�V�O�ƍX�V��ɃR�~�b�g����΂悢�̂�����
				if (prepUpdate("anyHash", xCel) == false) {
					System.out.println("#Error ");
				} else {
					count++;
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Finish!!! ");
		// TODO ������ɒǉ������e�[�u�������R���{�ɗ��Ƃ������E�E�E����Ƃ������[�h���悤���H�H

		// �R�~�b�g���ăg�����U�N�V�����I��
		if (getDriverClassName().contains("sqlite")) {
			executeUpdate("COMMIT");
		}
		return count;
	}

	// �v���y�A�h�X�e�[�g�����g�̗�
	public void ex_Prep() {
		String table = "tableName";
		String sql = "INSERT INTO " + table + " (XA1,XBA,XBB) "
				+ " values(?,?,?);";
		if (openPrep("anyHash", sql) == false) {
			System.out.println("#Error ");
		}
		while (true) {
			String[] array = { "a", "b", "c" };
			if (prepUpdate("anyHash", array) == false) {
				System.out.println("#Error ");
			}
		}
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// ��������
	// -----+---------+---------+---------+---------+---------+---------+---------+
	public static void main(String[] argv) {

	}

	public static void tester(String[] argv) {
		String jURL = getDEF_JURL();
		String user = getDEF_USER(); // ���[�U��
		String pass = getDEF_PASS(); // �p�X���[�h
		String sql = "select�@ top 100 * from  TRAN04;";
		if (argv.length >= 1) {
			sql = argv[0];
			jURL = "jdbc:hsqldb:LocoDB";
			user = "sa"; // ���[�U��
			pass = ""; // �p�X���[�h
			sql = "SELECT * FROM sample_table";
		}
		System.out.println(sql);
		JDBC_GUI jDbc = new JDBC_GUI(jURL, user, pass);
		if (jDbc.getConnection() != null) {
			if (user.equals("sa") == true)
				jDbc.createSample();
			try {
				// XXX jDbc.info_Catalog();
				// XXX jDbc.info_Tables();
			} catch (Exception e) {
				e.printStackTrace();
			}
			jDbc.close();
		}
	}
	// public boolean isExistTbl(String pTblName) {
	// return jdbcIns.isExistTbl(pTblName);
	// }
	// public String getCatalog() {
	// return jdbcIns.getCatalog();
	// }
	// private DatabaseMetaData getMetaData() {
	// return jdbcIns.getMetaData();
	// }
	// protected Statement createStatement() {
	// return jdbcIns.createStatement();
	// }
	// public boolean executeUpdate(String expression) throws SQLException {
	// return jdbcIns.executeUpdate(expression);
	// }
	// public synchronized Vector query2Vector(String sql) {
	// return jdbcIns.query2Vector(sql, false);
	// }
	// public synchronized Vector query2Vector(String sql, boolean flag) {
	// return jdbcIns.query2Vector(sql, flag);
	// }
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// �����������Q�e������ use for SQL command SELECT
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// public int query2File(OutputStreamWriter writer, String pSql,boolean
	// headOpt,String suffix) {
	// jdbcIns.setSuffix(suffix);
	// jdbcIns.setHeadOpt(headOpt);
	// return jdbcIns.query2File(writer,pSql);
	// }
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// // info_Catalog �J�^���O���\���i���̃f�[�^�x�[�X���j
	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// private void info_Catalog() throws SQLException {
	// System.out.println("****************************************");
	// System.out.println("** Catalog ");
	// System.out.println("****************************************");
	// ResultSet rSet = dbMeta.getCatalogs();
	// dump(rSet);
	// }
	//
	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// // info_Tables �e�[�u�����ꗗ�\��
	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// private void info_Tables() throws SQLException {
	// System.out.println("****************************************");
	// System.out.println("** Tables ");
	// System.out.println("****************************************");
	// // ---------+---------+---------+---------+---------+---------+---------+
	// // String[] patStr
	// // = {"TABLE","VIEW","SYSTEM TABLE",
	// // "GLOBAL TEMPORARY","LOCAL TEMPORARY","ALIAS","SYNONYM"};
	// // ---------+---------+---------+---------+---------+---------+---------+
	// String[] patStr = { "TABLE" };
	// ResultSet rSet = dbMeta.getTables(null, null, null, patStr);
	// dump(rSet);
	// }

	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// // info_Columns �w�肳�ꂽ�e�[�u���̃J�������ꗗ
	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// private void info_Columns(String pTable) throws SQLException {
	// System.out.println("****************************************");
	// System.out.println("** Columns ");
	// System.out.println("****************************************");
	// ResultSet rSet = dbMeta.getColumns(null, null, pTable, null);
	// dump(rSet);
	// }
	//
	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// // info_Index �w�肳�ꂽ�e�[�u���̃C���f�b�N�X�Ɠ��v���Ɋւ���L�q���擾
	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// private void info_Index(String pTable) throws SQLException {
	// System.out.println("****************************************");
	// System.out.println("** getIndexInfo ");
	// System.out.println("****************************************");
	// ResultSet rSet = dbMeta.getIndexInfo(null, null, pTable, false, false);
	// dump(rSet);
	// }

	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// // �������� ���炩����Resultset�̃J�E���g���E�����@�͂Ȃ��̂��H�H���ׂ悤�I
	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// private int dump(ResultSet rSet) throws SQLException {
	// int lcnt = 0;
	// ResultSetMetaData rsMeta = rSet.getMetaData();
	// int colmax = rsMeta.getColumnCount();
	// System.out.println("colmax:" + colmax);
	// // ---------+---------+---------+---------+---------+---------+---------+
	// // Header�i�񖼓������j
	// // ---------+---------+---------+---------+---------+---------+---------+
	// for (int j = 0; j < colmax; j++) {
	// System.out.print("#ColumnName(" + (j + 1) + "):");
	// System.out.print(rsMeta.getColumnName(j + 1) + CRLF);
	// }
	// // ---------+---------+---------+---------+---------+---------+---------+
	// // Body�i�f�[�^�����j
	// // ---------+---------+---------+---------+---------+---------+---------+
	// for (; rSet.next();) {
	// lcnt++;
	// System.out.print(" #" + lcnt);
	// for (int i = 0; i < colmax; i++) {
	// System.out.print(" <" + (i + 1) + ">");
	// System.out.print(rSet.getString(i + 1));
	// // jTa.append(obj.toString() + "\t");
	// // ���ŏ��̃J�����̓[������Ȃ��ĂP�Ȃ̂Œ��ӂ���
	// // obj = rs.getObject(i + 1);
	// // if (obj==null){
	// // System.out.print("null?" + "\t");
	// // }else{
	// // System.out.print(obj.toString() + "\t");
	// // }
	// }
	// System.out.print(CRLF);
	// }
	// return lcnt;
	// }
	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// // rs2List
	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// private List rs2List(ResultSet rSet, boolean pHead) throws SQLException {
	// int lcnt = 0;
	// ResultSetMetaData meta = rSet.getMetaData();
	// int[] colSiz = new int[meta.getColumnCount()]; // ���ʃZ�b�g�̗�
	// List datVec = new ArrayList(0);
	// List cnmVec = new ArrayList(0);
	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// // Header�i�񖼓������j
	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// if (pHead == true) {
	// for (int j = 0; j < colSiz.length; j++) {
	// colSiz[j] = meta.getColumnDisplaySize(j + 1);
	// cnmVec.add(meta.getColumnName(j + 1));
	// }
	// datVec.add(cnmVec);
	// }
	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// // Body�i�f�[�^�����j
	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// while (rSet.next()) {
	// lcnt++;
	// List rowList = new ArrayList(0);
	// for (int i = 0; i < colSiz.length; i++) {
	// rowList.add(rSet.getObject(i + 1));
	// }
	// datVec.add(rowList);
	// }
	// return datVec;
	// }
	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// // �����Q�k������ RESULT Set to List
	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// private int rs2ListModel(DefaultListModel model, ResultSet rSet)
	// throws SQLException {
	// return rs2ListModel(model, rSet, " |\t");
	// }
}
