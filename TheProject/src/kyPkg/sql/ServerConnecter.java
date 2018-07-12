package kyPkg.sql;

import java.util.List;

import kyPkg.converter.Inf_ArrayCnv;

public class ServerConnecter implements Connector {
//TODO この辺を外部化する・・・暗号化するか・・・うむ　PGPかな
	public static String CURRENT_SERVER = "ks1s014";
	private static String CURRENT_USER = "qpr";
	private static String CURRENT_PASS = "";
	//	NEWSERVER
//	public static String CURRENT_SERVER = "ag6s022";
//	private static String CURRENT_USER = "qpr";
//	private static String CURRENT_PASS = "lovelovelove";

	public static String getDEF_JURL() {
		return "JDBC:ODBC:DRIVER={SQL Server};SERVER=" + CURRENT_SERVER
				+ ";UID=" + CURRENT_USER + ";PWD=" + CURRENT_PASS
				+ ";DATABASE=QPRDB1";
	}

	public static String getDEF_USER() {
		return CURRENT_USER;
	}

	public static String getDEF_PASS() {
		return CURRENT_PASS;
	}

	private static String jURL = getDEF_JURL();
	private static String user = getDEF_USER(); // ユーザ名
	private static String pass = getDEF_PASS(); // パスワード

	private JDBC jdbcIns = null;

	@Override
	public JDBC getConnection() {
		if (jdbcIns == null) {
			jdbcIns = connectToServer();
		}
		return jdbcIns;
	}

	public ServerConnecter() {
		getConnection();
	}

	public ServerConnecter(String jURL, String user, String pass) {
		ServerConnecter.jURL = jURL;
		ServerConnecter.user = user;
		ServerConnecter.pass = pass;
		jdbcIns = getConnection();
	}

	public String getJURL() {
		return jURL;
	}

	@Override
	public List<List> query2Matrix(String sql) {
		getConnection();
		if (jdbcIns != null) {
			return jdbcIns.query2Matrix(sql);
		}
		return null;
	}

	public boolean executeUpdate(String sql) {
		getConnection();
		if (jdbcIns != null) {
			return jdbcIns.executeUpdate(sql);
		}
		return false;
	}

	public List query2List(String sql) {
		getConnection();
		if (jdbcIns != null) {
			return jdbcIns.query2List(sql);
		}
		return null;
	}

	// list = kyPkg.uFile.ListArrayUtil.file2List(path);
	public int query2File(String path, String sql) {
		getConnection();
		if (jdbcIns != null) {
			return jdbcIns.query2File(path, sql);
		}
		return -1;
	}

	public int query2File(String path, String sql, String delimiter,
			String[] header, Inf_ArrayCnv cnv) {
		getConnection();
		if (jdbcIns != null) {
			return jdbcIns.query2File(path, sql, delimiter, header, cnv);
		}
		return -1;
	}

	public int query2Closure(Inf_Closure closure, String sql) {
		getConnection();
		if (jdbcIns != null) {
			return jdbcIns.query2Closure(closure, sql);
		}
		return -1;
	}

	@Override
	public String query2Str(String sql) {
		getConnection();
		if (jdbcIns != null) {
			return jdbcIns.queryOne(sql);
		}
		return null;
	}

	@Override
	public void close() {
		if (jdbcIns != null)
			jdbcIns.close();
	}

	// -------------------------------------------------------------------------
	// Connect to Host ≪DBの再接続≫
	// -------------------------------------------------------------------------
	private JDBC connectToServer() {
		JDBC object = new JDBC(jURL, user, pass);
		if (object.getConnection() == null) {
			object = null;
		}
		return object;
	}

}
