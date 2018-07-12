package kyPkg.sql;

import java.util.HashMap;

//JDBCクラスをバッチで使用するためのユーティリティクラス
public class Query {
	private static final String SQL = "sql";
	private static final String JURL = "jURL";
	private static final String USER = "user";
	private static final String PSWD = "pass";
	private static HashMap<String, String> map;
	private static JDBC jdbcIns;

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public Query() {
		super();
		String jUrl = ServerConnecter.getDEF_JURL() ;
		String user = ServerConnecter.getDEF_USER();
		String pswd = ServerConnecter.getDEF_PASS();
		map = new HashMap();
		map.put(JURL,jUrl);
		map.put(USER, user);
		map.put(PSWD, pswd);
		map.put(SQL, "");
		init();
	}

	// ------------------------------------------------------------------------
	// 初期化（接続）
	// ------------------------------------------------------------------------
	public void init() {
		jdbcIns = new JDBC(map.get(JURL), map.get(USER), map.get(PSWD));
	};

	// ------------------------------------------------------------------------
	// 終了処理
	// ------------------------------------------------------------------------
	public void fin() {
		jdbcIns.close();
	};

	public void executeUpdate(String sql) {
		if (jdbcIns != null) {
			jdbcIns.executeUpdate(sql);
		} else {
			System.out.println("Can't Connect to DB");
		}
	}

	// ------------------------------------------------------------------------
	//
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("SQLCMD Elapse");
		elapse.start();
		test20150316();
		elapse.stop();
	}

	// ------------------------------------------------------------------------
	// JICFSｶﾅ名称変換処理関連
	// ------------------------------------------------------------------------
	public static void test20150316() {
		Query query = new Query();
		query.executeUpdate("update ITEM Set xc2 = 'ソノタユニ　マランデイキヤラメルヌガー１５０Ｇ　　' where xc2 = '' and   xa1 = '8710486118251'");
		query.fin();
	}

}
