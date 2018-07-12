package kyPkg.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import kyPkg.uFile.FileUtil;

// 《sqliteのテスト》 2009-08-31 yuasa　
//
//	sqlitejdbc-v056.jarをクラスパスに追加しただけでOK（ｓqliteを別途インストールする必要はない）
//　　テキストファイルからのインポートは？？どうするのか？？
//　	jet、hsqlDBとの代替は可能か？？（更新系は要注意！！）
public class SqliteTest {
	private Connection conn;

	public SqliteTest(String dbpath) {
		connect(dbpath);
	}

	private void connect(String dbpath) {
		conn = null;
		// Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbpath);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void execute(String sql) {
		executeUpdate(sql);
	}

	public void executeUpdate(String sql) {
		try {
			Statement stat = conn.createStatement();
			stat.executeUpdate(sql);
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void executeBatch(String sql, List<List> paramList) {
		try {
			PreparedStatement prep = conn.prepareStatement(sql);
			for (Iterator iter = paramList.iterator(); iter.hasNext();) {
				List params = (List) iter.next();
				for (int i = 0; i < params.size(); i++) {
					prep.setString((i + 1), (String) params.get(i));
				}
				prep.addBatch();
			}
			// prep.setString(1, "Gandhi");
			// prep.setString(2, "politics");
			// prep.addBatch();
			// prep.setString(1, "Turing");
			// prep.setString(2, "computers");
			// prep.addBatch();
			// prep.setString(1, "Wittgenstein");
			// prep.setString(2, "smartypants");
			// prep.addBatch();
			conn.setAutoCommit(false);
			prep.executeBatch();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void executeQuery(String sql) {
		System.out.println("<<executeQuery>> start:" + sql);
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(sql);
			while (rs.next()) {
				System.out.println("name       = " + rs.getString("name"));
				System.out
						.println("occupation = " + rs.getString("occupation"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("<<executeQuery>> end");
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		test01();
	}
	
	//XXX ロイヤルティの中間ファイルをデータベースに変換する
	//　テーブル定義はどんなか

	// ------------------------------------------------------------------------
	// 適当なディレクトリにsqliteデータベースを作って、イロイロコマンドをたたいてみる
	// ------------------------------------------------------------------------
	public static void test01() {
		String dir = "c:/sqTest/";
		FileUtil.mkdir(dir);
		String dbName ="testDB";//"test.Db";
		String dbpath = dir + dbName;
		String jUrl = "jdbc:sqlite:" + dbpath;
		String user = "";
		String pass = "";
		Inf_JDBC dbIns = new kyPkg.sql.JDBC(jUrl, user, pass);
		// ------------------------------------------------------------------------
		// テーブルを消す
		// ------------------------------------------------------------------------
		dbIns.executeUpdate("drop table if exists people;");
		// ------------------------------------------------------------------------
		// テーブルを定義する
		// ------------------------------------------------------------------------
		dbIns.executeUpdate("create table people (name, occupation);");

		// ------------------------------------------------------------------------
		// データを登録する
		// ------------------------------------------------------------------------
		List<List> paramList = new ArrayList();
		paramList.add(Arrays.asList(new String[] { "Gandhi", "politics" }));
		paramList.add(Arrays.asList(new String[] { "Turing", "computers" }));
		paramList.add(Arrays.asList(new String[] { "Wittgenstein",
				"smartypants" }));
		dbIns.executeBatch("insert into people values (?, ?);", paramList);

		// ------------------------------------------------------------------------
		// データを検索する
		// ------------------------------------------------------------------------
		List<List> matrix = dbIns.query2Matrix("select * from people;");
		StringBuffer buff = new StringBuffer();
		for (Iterator iter = matrix.iterator(); iter.hasNext();) {
			List row = (List) iter.next();
			buff.delete(0, buff.length());
			for (Iterator iterator = row.iterator(); iterator.hasNext();) {
				String element = (String) iterator.next();
				buff.append(element);
				buff.append(" - ");
			}
			System.out.println("=>" + buff.toString());
		}

		// dbIns.executeQuery("PRAGMA table_info(people);");

		// ------------------------------------------------------------------------
		// テーブルを消す
		// ------------------------------------------------------------------------
		dbIns.execute("drop table if exists JAN;");
		// ------------------------------------------------------------------------
		// テーブルを定義する
		// ------------------------------------------------------------------------
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("create table JAN (");
		sqlBuf.append("kCode text not null,");
		sqlBuf.append("kMK text not null,");
		sqlBuf.append("kK1 text not null,");
		sqlBuf.append("kK2 text not null,");
		sqlBuf.append("kK3 text not null,");
		sqlBuf.append("kK4 text not null,");
		sqlBuf.append("kK5 text not null,");
		sqlBuf.append("kK6 text not null,");
		sqlBuf.append("kPRC Integer not null,");
		sqlBuf.append("kAMT Integer not null,");
		sqlBuf.append("kName text not null,");
		sqlBuf.append("kTerm text not null");
		sqlBuf.append(");");
		dbIns.execute(sqlBuf.toString());
		dbIns.execute("VACUUM");// VACUUM
		dbIns.close();
	}

	// private static void test00() {
	// String dir = ResControl.getUsersItpDir() +"504191000002/";
	// String dbpath = dir + "QPR.Db";
	// String jUrl = "jdbc:sqlite:" + dbpath;
	// String user = "";
	// String pass = "";
	// Inf_JDBC dbIns = new kyPkg.sql.JDBC(jUrl, user, pass);
	//
	// String sql = "";
	// List<List> matrix = null;
	//
	// ITP_SqlFactory itp_sql = new ITP_SqlFactory();
	// String xWhere = new CndBuilder().getCondJan(null, null,
	// CndBuilder.OR, CndBuilder.OR);
	//
	// sql = ITP_SqlFactory.jan_and_category("JAN_MK", xWhere,-1, true);
	// System.out.println("Bef:" + sql);
	// sql = sql.replaceAll("#TXT", ""); // ISAM用から変換する
	// System.out.println("Aft:" + sql);
	// matrix = dbIns.query2Matrix(sql);
	// // SELECT t1.kCode,t1.kName,t2.kCode,t2.kName,kPRC,kAMT FROM JAN as t1
	// StringBuffer buff = new StringBuffer();
	// for (Iterator iter = matrix.iterator(); iter.hasNext();) {
	// List row = (List) iter.next();
	// buff.delete(0, buff.length());
	// for (Iterator iterator = row.iterator(); iterator.hasNext();) {
	// Object element = iterator.next();
	// if (element != null) {
	// buff.append(element.toString());
	// } else {
	// buff.append("");
	// }
	// buff.append(" - ");
	// }
	// System.out.println("=>" + buff.toString());
	// }
	// sql = ITP_SqlFactory.createSql2("MK", xWhere, "code");
	// System.out.println("Bef:" + sql);
	// sql = sql.replaceAll("#TXT", ""); // ISAM用から変換する
	// System.out.println("Aft:" + sql);
	// matrix = dbIns.query2Matrix(sql);
	// buff = new StringBuffer();
	// for (Iterator iter = matrix.iterator(); iter.hasNext();) {
	// List row = (List) iter.next();
	// buff.delete(0, buff.length());
	// for (Iterator iterator = row.iterator(); iterator.hasNext();) {
	// Object element = iterator.next();
	// if (element != null) {
	// buff.append(element.toString());
	// } else {
	// buff.append("");
	// }
	// buff.append(" - ");
	// }
	// System.out.println("=>" + buff.toString());
	// }
	// dbIns.execute("VACUUM");// VACUUM
	// dbIns.close();
	// }

}
