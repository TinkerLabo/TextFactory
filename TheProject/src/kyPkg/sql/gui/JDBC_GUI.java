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
	// コンストラクタ
	// -----+---------+---------+---------+---------+---------+---------+---------+
	public JDBC_GUI() {
		super.getConnection(getDEF_JURL(), getDEF_USER(), getDEF_PASS());
	}

	public JDBC_GUI(String jURL, String user, String password) {
		super.getConnection(jURL, user, password);
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// queryCombo クエリー結果をComboに流し込む
	// -----+---------+---------+---------+---------+---------+---------+---------+
	public synchronized ComboBoxModel query2ComboModel(String sql) {
		DefaultComboBoxModel pDmdl = new DefaultComboBoxModel();
		try {
			Statement stmt = createStatement(); // ステートメント生成
			ResultSet rSet = stmt.executeQuery(sql); // クエリー実行
			rs2ComboModel(pDmdl, rSet);
			stmt.close();
		} catch (Exception e) {
			eMsg = e.toString();
			e.printStackTrace();
		}
		return pDmdl;
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// ｑｕｅｒｙＬｉｓｔ クエリー結果をListに流し込む
	// -----+---------+---------+---------+---------+---------+---------+---------+
	public synchronized DefaultListModel query2Listmodel(String sql) {
		return query2Listmodel(sql, "");
	}

	public synchronized DefaultListModel query2Listmodel(String sql,
			String delimiter) {
		DefaultListModel model = new DefaultListModel();
		try {
			Statement stmt = createStatement(); // ステートメント生成
			ResultSet rSet = stmt.executeQuery(sql); // クエリー実行
			rs2ListModel(model, rSet, delimiter);
			stmt.close();
		} catch (Exception e) {
			eMsg = e.toString();
			e.printStackTrace();
		}
		return model;
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// queryTable クエリー結果をTableに流し込む。 ※"util.ProtTModelクラス"に依存
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
	// Statement stmt = createStatement(); // ステートメント生成
	//
	// // トランザクション開始
	// if (getDriverClassName().contains("sqlite")) {
	// // sqliteのパフォーマンスを上げたい・・・defaultは2000
	// stmt.executeUpdate("PRAGMA cache_size = 8000");
	// }
	// rSet = stmt.executeQuery(sql); // クエリー実行
	// // System.out.println("## queryTable ##");
	// model = new DefaultTableModelMod(rSet, true);
	// // ProtTModelはAbstractTableModelをExtendしていて・・
	// // getColumnClassはデータを格納してあるVector中のクラスを返す
	// // よって・・それに対応した
	// stmt.close();
	// } catch (Exception e) {
	// eMsg = e.toString();
	// e.printStackTrace();
	// }
	// return model;
	// }

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// rs2Combo RESULTをComboに流し込む
	// -----+---------+---------+---------+---------+---------+---------+---------+
	int rs2ComboModel(DefaultComboBoxModel model, ResultSet rSet)
			throws SQLException {
		int lcnt = 0;
		ResultSetMetaData rsMeta = rSet.getMetaData();
		int colmax = rsMeta.getColumnCount(); // 結果セットの列数
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
		int colmax = rsMeta.getColumnCount(); // 結果セットの列数
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
	// ｃｒｅａｔｅＳａｍｐｌｅ サンプルデータ作成
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
				"INSERT INTO sample_table(str_col,num_col) VALUES('Ｇｉｂｓｏｎ　　',20000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('Ｔｒａｖｉｓ　　',17000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('Ｍａｒｔｉｎ　　',15000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('Ｆｅｎｄｅｒ　　',10000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('Ｇｒｅｔｓｃｈ　', 9000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('Ｅｐｉｐｈｏｎｅ', 8000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('Ｎａｔｉｏｎａｌ', 7000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('Ｄｏｂｒｏ　　　', 6000 )");
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
				"INSERT INTO sample_table(str_col,num_col) VALUES('ぎぶそん　　　　',20000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('トラビス　　　　',17000 )");
		executeUpdate(
				"INSERT INTO sample_table(str_col,num_col) VALUES('マーチン　　　　',15000 )");
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
	// popCatCombo カタログ情報をコンボボックスに表示
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
	// popSchCombo 使用可能なスキーマ名をコンボボックスに表示
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
	// popTblString テーブル名一覧を文字列として返す
	// -----+---------+---------+---------+---------+---------+---------+---------+
	// 第二引数には以下のようなテーブルの種別を表すテーブルを渡す
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
	// popTblCombo テーブル名一覧をコンボボックスに表示
	// -----+---------+---------+---------+---------+---------+---------+---------+
	// ≪使用例≫
	// 第二引数には以下のようなテーブルの種別を表すテーブルを渡す
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
	// popFldCombo フィールド名をコンボボックスに一覧表示
	// -----+---------+---------+---------+---------+---------+---------+---------+
	protected void popFldCombo(JComboBox pBox, String pTable) {
		List list = getFieldsList(pTable, 4); // 4.COLUMN_NAME String => 列名
		if (list != null) {
			pBox.setModel(new DefaultComboBoxModel(new Vector(list)));
			pBox.setEnabled(list.size() > 0);
		} else {
			pBox.setEnabled(false);
		}
	}

	// -----+---------+---------+---------+---------+---------+---------+---------+
	// popFldList フィールド名をリストボックスに一覧表示
	// -----+---------+---------+---------+---------+---------+---------+---------+
	protected void popFldList(JList pBox, String pTable) {
		List list = getFieldsList(pTable, 4); // 4.COLUMN_NAME String => 列名
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
	// エラーメッセージを返す
	// ---+---------+---------+---------+---------+---------+---------+---------+
	public String getEmsg() {
		return eMsg;
	}

	// 1行目にヘッダー情報がある場合 => headOption=true
	public long importFromText(String path, String table, String delimiter,
			boolean headOption) {
		long count = 0;
		String wRec = "";
		try {

			File49ers insF49 = new File49ers(path);
			String encoding = insF49.getEncoding();

			// データの先読みをしてフィールド型を設定する。
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

			// <<DROP>> テーブルがすでに存在する場合消す
			if (isExist(table)) {
				String dropSql = "DROP TABLE " + table;
				System.out.println(
						"table:" + table + "is existed!!\nSQL:" + dropSql);
				executeUpdate(dropSql);
			}

			// <<CREATE>> テーブルを定義する
			String createSql = "CREATE TABLE " + table + " (" + defs.toString()
					+ ")";
			System.out.println("createSql:" + createSql);
			executeUpdate(createSql);

			// プリペアドステートメント準備
			String insertSql = "INSERT INTO " + table + " (" + flds.toString()
					+ ") VALUES(" + dums.toString() + ")";
			System.out.println("insertSql:" + insertSql);

			if (openPrep("anyHash", insertSql) == false) {
				System.out.println("#Error プリペアドステートメント定義失敗");
			}

			// トランザクション開始
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
				// TODO セル幅が合わない場合に問題がある・・・不足分のセルを補えないだろうか？？
				if (xCel.length != maxCol) {
					System.out
							.println("#DBG# #ERROR xCel.length:" + xCel.length);
				}
				// XXX 更新前と更新後にコミットすればよいのかもよ
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
		// TODO 処理後に追加したテーブル名をコンボに落とそうか・・・それともリロードしようか？？

		// コミットしてトランザクション終了
		if (getDriverClassName().contains("sqlite")) {
			executeUpdate("COMMIT");
		}
		return count;
	}

	// プリペアドステートメントの例
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
	// ｍａｉｎ
	// -----+---------+---------+---------+---------+---------+---------+---------+
	public static void main(String[] argv) {

	}

	public static void tester(String[] argv) {
		String jURL = getDEF_JURL();
		String user = getDEF_USER(); // ユーザ名
		String pass = getDEF_PASS(); // パスワード
		String sql = "select　 top 100 * from  TRAN04;";
		if (argv.length >= 1) {
			sql = argv[0];
			jURL = "jdbc:hsqldb:LocoDB";
			user = "sa"; // ユーザ名
			pass = ""; // パスワード
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
	// ｑｕｅｒｙ２Ｆｉｌｅ use for SQL command SELECT
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// public int query2File(OutputStreamWriter writer, String pSql,boolean
	// headOpt,String suffix) {
	// jdbcIns.setSuffix(suffix);
	// jdbcIns.setHeadOpt(headOpt);
	// return jdbcIns.query2File(writer,pSql);
	// }
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// // info_Catalog カタログ情報表示（他のデータベース名）
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
	// // info_Tables テーブル名一覧表示
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
	// // info_Columns 指定されたテーブルのカラム情報一覧
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
	// // info_Index 指定されたテーブルのインデックスと統計情報に関する記述を取得
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
	// // ｄｕｍｐ あらかじめResultsetのカウントを拾う方法はないのか？？調べよう！
	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// private int dump(ResultSet rSet) throws SQLException {
	// int lcnt = 0;
	// ResultSetMetaData rsMeta = rSet.getMetaData();
	// int colmax = rsMeta.getColumnCount();
	// System.out.println("colmax:" + colmax);
	// // ---------+---------+---------+---------+---------+---------+---------+
	// // Header（列名等処理）
	// // ---------+---------+---------+---------+---------+---------+---------+
	// for (int j = 0; j < colmax; j++) {
	// System.out.print("#ColumnName(" + (j + 1) + "):");
	// System.out.print(rsMeta.getColumnName(j + 1) + CRLF);
	// }
	// // ---------+---------+---------+---------+---------+---------+---------+
	// // Body（データ処理）
	// // ---------+---------+---------+---------+---------+---------+---------+
	// for (; rSet.next();) {
	// lcnt++;
	// System.out.print(" #" + lcnt);
	// for (int i = 0; i < colmax; i++) {
	// System.out.print(" <" + (i + 1) + ">");
	// System.out.print(rSet.getString(i + 1));
	// // jTa.append(obj.toString() + "\t");
	// // ※最初のカラムはゼロじゃなくて１なので注意する
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
	// int[] colSiz = new int[meta.getColumnCount()]; // 結果セットの列幅
	// List datVec = new ArrayList(0);
	// List cnmVec = new ArrayList(0);
	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// // Header（列名等処理）
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
	// // Body（データ処理）
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
	// // ｒｓ２Ｌｉｓｔ RESULT Set to List
	// //
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// private int rs2ListModel(DefaultListModel model, ResultSet rSet)
	// throws SQLException {
	// return rs2ListModel(model, rSet, " |\t");
	// }
}
