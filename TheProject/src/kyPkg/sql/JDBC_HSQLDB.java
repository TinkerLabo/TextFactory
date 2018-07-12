package kyPkg.sql;

import java.io.*;
import java.util.List;
import kyPkg.uFile.FileUtil;

import static kyPkg.uFile.FileUtil.*;

//-----------------------------------------------------------------------------
//エクスポートする際の改行コードがCRLF　（普通）
//-----------------------------------------------------------------------------
// ファイルの出力先には。”C:\dataBase\”が先頭に付けられたパスとなるので、相対パス以外不可ということ
// 遅い・・・ヒープが足りなくなる・・・あんまり良くない気がしてきた　　
// もしかしたらsqliteを使ったほうが良いのかも(あちらはencodeの問題あり)
// jdbc:hsqldb:file:/c:/database/dbname
// JRE に　-Xmx512Mを指定しないとOUTOFMEMORYとなってしまう
//-----------------------------------------------------------------------------
//http://hsqldb.org/doc/guide/index.html
//http://hsqldb.sourceforge.net/
//http://www.wakhok.ac.jp/~tomoharu/tokuron2003/hsqldb/
//-----------------------------------------------------------------------------
// http://hsqldb.org/
// http://hsqldb.org/doc/guide/ch01.html
// ";shutdown=true"という追加属性をつけておくと、終了時にSHUTDOWNもしてくれる
// 参考=>	http://www50.tok2.com/home/oppama/hsqldb-start.html
// 要するに・・これをするまでは何かのタイミングでファイルに保存されているがそれまではインメモリで管理されているということ
//General
//Closing the Database
//All databases running in different modes can be closed with the SHUTDOWN command, 
//issued as an SQL query. 
//From version 1.7.2, in-process databases are no longer closed when the last connection 
//to the database is explicitly closed via JDBC, a SHUTDOWN is required. 
//In 1.8.0, a connection property, shutdown=true, can be specified on the first connection
//to the database (the connection that opens the database) to force a shutdown when the last connection closes.
//
//When SHUTDOWN is issued, all active transactions are rolled back. 
//A special form of closing the database is via the SHUTDOWN COMPACT command. 
//This command rewrites the .data file that contains the information stored in CACHED tables and
//compacts it to size. This command should be issued periodically, especially when lots of inserts,
//updates or deletes have been performed on the cached tables. 
//Changes to the structure of the database, such as dropping or modifying populated CACHED tables or 
//indexes also create large amounts of unused file space that can be reclaimed using this command.
//-----------------------------------------------------------------------------
public class JDBC_HSQLDB extends JDBC_TEXDB {
	private static final String DATA_DIR = "./data/";
	private static final String DBNAME = "dbname"; // 簡単のため固定名
	//Execute SQL in FILENAME
	@Override
	public String getPath(String path) {
		String fileName = kyPkg.uFile.FileUtil.getFileName(path);
		String dbDir = getDbDir();
		return dbDir + DATA_DIR.substring(2) + fileName;
	}

	// -------------------------------------------------------------------------
	// 若干スタイルが異なるので、オーバーライドしている
	// -------------------------------------------------------------------------
	@Override
	public boolean createTable(String table, String fDefs) {
		dropTable(table);
		String sql = "CREATE TEXT TABLE " + table + " (" + fDefs + ")";
		return executeUpdate(sql);
	}

	// -------------------------------------------------------------------------
	// テキストテーブルの作成とアサイン
	// CREATE TEXT TABLE xTable(XA1 VARCHAR,XB1 VARCHAR,XC1 VARCHAR)
	// SET TABLE xTable SOURCE \"qpr_monitor_info.csv;fs=\\t;ignore_fist=true\"
	// 区切り文字指定 fs=\\t; 一番最初の列を無視する ignore_fist=true\"
	// SET TABLE xTable SOURCE off; ディスコネクトする
	// -------------------------------------------------------------------------
	// テキストテーブルで追加更新を行うと空の行がたくさんできてしまう・・・
	// -------------------------------------------------------------------------
	// text table はメモリ上のみのＤＢでは作成できない
	// conn = DriverManager.getConnection("jdbc:hsqldb:mem:/kenTEST" ,"sa","");
	// conn = DriverManager.getConnection("jdbc:hsqldb:file:/" + dbPath
	// ,"sa","");
	// -------------------------------------------------------------------------
	public JDBC_HSQLDB(String dir) {
		if (dir == null || dir.equals("")) {
			if (log != null)
				log.fatal("hsqlDb<Text> dirが指定されていません");
			return;
		}
		// データベースを作成する場所
		this.dbDir= FileUtil.mkdir(dir);
		// テキストテーブルを配置する場所（ 簡単のため固定名"data"とした）
		this.dataDir = FileUtil.mkdir(getDbDir() + "data");
		new File(dataDir).mkdirs();
		openDB();
	}

	public JDBC_HSQLDB() {
		this(System.getProperty("user.dir").trim() + "/DB");
	}

	// -------------------------------------------------------------------------
	// テーブルをファイルに紐付ける（ファイルはデータベースフォルダのDATA_DIRフォルダに配置されているもの）
	// -------------------------------------------------------------------------
	@Override
	public boolean assignIt(String table, String path, String delimiter,
			String encoding) {
		String fileName = kyPkg.uFile.FileUtil.getFileName(path);
		// ---------------------------------------------------------------------
		// separator
		// ---------------------------------------------------------------------
		String mode = "\\t"; // defaultはタブ区切り
		if (delimiter == null || delimiter.equals("\t"))
			mode = "\\t";
		else if (delimiter.equals(";"))
			mode = "\\semi";
		else if (delimiter.equals(" "))
			mode = "\\space";
		else if (delimiter.equals("'"))
			mode = "\\apos";
		else
			mode = delimiter;
		// ---------------------------------------------------------------------
		// encode
		// ---------------------------------------------------------------------
		if (delimiter.equals(UTF_8))
			encoding = ";encoding=UTF-8";
		else if (delimiter.equals(SHIFT_JIS))
			encoding = ";encoding=Shift_JIS";
		else if (delimiter.equals(ASCII))
			encoding = ";encoding=ASCII";
		else
			encoding = "";// default=>ASCIIとなる
		String dataPath = DATA_DIR + fileName;
		String tmpSql = "SET TABLE " + table + " SOURCE \"" + dataPath + ";fs="
				+ mode + encoding + ";ignore_fist=false\"";
		return executeUpdate(tmpSql);
	}

	// -------------------------------------------------------------------------
	// 一時的にテキストテーブルを作成して、オリジナルのコピーにより出力を行う
	// 他に方法がないだろうか？？
	// -------------------------------------------------------------------------
	// 《パラメータ》
	// String outPath 出力先
	// String table 対象となるテーブル
	// String orderBy 出力順を指定したい場合に使う
	// -------------------------------------------------------------------------
	@Override
	public void exportIt(String outPath, String delimiter, String table,
			String key, String fields, String orderBy, String where) {
		if (fields.trim().equals(""))
			fields = "*";
		if (log != null)
			log.info("　　テーブル（" + table + "）をファイルに出力=>" + outPath);
		if (!isExist(table)) {
			if (log != null)
				log.info("　　#ERROR!!　Table not Exist!!:" + table);
			return;
		}
		// 削除する
		String dataPath = getPath(outPath);
		kyPkg.uFile.FileUtil.killIt(dataPath);
		String tmpTable = "TMP_TABLE";
		String wWhere = "";
		if (!key.equals("")) {
			wWhere = " WHERE ( " + key + " <> '' ) ";
		}
		orderBy = orderBy.trim();
		if (!orderBy.equals("")) {
			orderBy = " order by " + orderBy;
		}
		List list = getFieldsList(table);
		if (list.size() > 0) {
			// 桃沢さんのオーダー=>子供年齢云々用のマージ処理を考える
			String fDefs = "";
			if (fields.equals("*")) {
				List deflist = getFieldsDefs(table);
				fDefs = kyPkg.util.Joint.join(deflist, ",");
			} else {
				//ややヤッツケ仕事
				StringBuffer buf = new StringBuffer();
				String array[] = fields.split(",");
				for (int i = 0; i < array.length; i++) {
					if (i > 0)
						buf.append(",");
					buf.append(array[i]);
					buf.append(" VARCHAR");
				}
				fDefs = buf.toString();
			}
			//System.out.println("fDefs::::" + fDefs);
			createTable(tmpTable, fDefs);
//			assignIt(tmpTable, outPath, delimiter, SHIFT_JIS);
			assignIt(tmpTable, outPath, delimiter, defaultEncoding2);//210161222
			String sql = "INSERT INTO " + tmpTable + " SELECT DISTINCT "
					+ fields + " FROM " + table + wWhere + orderBy + ";";
			// INSERT INTO qpr_monitor_out select distinct * from
			// QPR_MONITOR_BASE order by FLD0;
			executeUpdate(sql);
			dropTable(tmpTable);
		} else {
			System.out.println("#ERROR!! JDBC_HSQLDB@exportit fieldSize =< 0");
		}
	}

	// いまのところ使っていないので取りあえずコメントアウトしてある
	// -------------------------------------------------------------------------
	// // Disconnecting Text Tables
	// -------------------------------------------------------------------------
	// public boolean setTableOff(String table) {
	// executeUpdate("SET TABLE " + table + " SOURCE OFF");
	// return true;
	// }
	// -------------------------------------------------------------------------
	// // ReConnecting Text Tables
	// -------------------------------------------------------------------------
	// public boolean setTableON(String table) {
	// executeUpdate("SET TABLE " + table + " SOURCE ON");
	// return true;
	// }
	@Override
	public TContainer importTable(String table, String srcPath) {
		// 当該テーブルが存在するなら消しておく
		if (!isExist(table))
			dropTable(table);
		// -------------------------------------------------------------
		// ファイルからテキストテーブルを生成する
		// -------------------------------------------------------------
		String path = getPath(srcPath);
		TContainer container = new TContainer(table, path, 0);
		String iDefs = container.getfDefs();
		if (createTable(table, iDefs)) {
			// ---------------------------------------------------------
			// ファイルをテーブルにアサイン
			// ---------------------------------------------------------
			assignIt(table, srcPath);
			return container;
		} else {
			return null;
		}
	}

	protected void openDB() {
		if (log != null)
			log.info("hsqlDb　openDB");
		String jUrl = "jdbc:hsqldb:file:/" + getDbDir() + DBNAME
				+ ";shutdown=true";
		String user = "sa";
		String pass = "";
		System.out.println("# openDB jUrl:" + jUrl);
		if (super.getConnection(jUrl, user, pass) == null) {
			if (log != null)
				log.fatal("hsqlDb<Text> 初期化失敗");
		}
	}

	@Override
	public void close() {
		super.close();
	}

	@Override
	public String getKind() {
		return "HSQLDB";
	}

	@Override
	public void executeSQL(String sql) {
	}
}
