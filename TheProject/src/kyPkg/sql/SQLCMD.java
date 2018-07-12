package kyPkg.sql;

import kyPkg.atoms.AtomDB;

//JDBCクラスをバッチで使用するためのユーティリティクラス
//(※JDBCクラスが色々なクラスに依存しないようにする為に別クラスにした)
public class SQLCMD {
	private static final String JURL = "jURL";
	private static final String USER = "user";
	private static final String PASS = "pass";
	private static final String PATH = "path";
	private static final String SQL = "sql";
	private static final String DELIMITER = "delimiter";
	private static final String ERR_SQLCMD = "ERROR@SQLCMD";
	private static final String TAB = "\t";
	private String message = "";
	private String jUrl;
	private String user;
	private String pass;
	private String comment1;
	private String comment2;
	private String outPath;
	private String sql;
	private String delimiter;
	private kyPkg.util.ArgsMap argMap;

	public String getjUrl() {
		return jUrl;
	}

	public void setjUrl(String jUrl) {
		this.jUrl = jUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getComment1() {
		return comment1;
	}

	public void setComment1(String comment1) {
		this.comment1 = comment1;
	}

	public String getComment2() {
		return comment2;
	}

	public void setComment2(String comment2) {
		this.comment2 = comment2;
	}

	public String getOutPath() {
		return outPath;
	}

	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}


	public String getMessage() {
		return message;
	}

	//-------------------------------------------------------------------------
	// コンストラクタ (第一引数はYML、第二引数は出力先パス)
	//-------------------------------------------------------------------------
	public SQLCMD(String[] args) {
		super();
		parse(args);
	}

	private void parse(String[] args) {
		if (args.length >= 1) {
			argMap = new kyPkg.util.ArgsMap(args[0]);
			if (args.length >= 2)
				argMap.put(PATH, args[1]); // 第２引数が指定されていたら、出力先pathとして扱う
			execute();
		}
	}

	// ------------------------------------------------------------------------
	// execute
	// ------------------------------------------------------------------------
	public void execute() {
		jUrl = argMap.get(JURL);
		user = argMap.get(USER);
		pass = argMap.get(PASS);
//		System.out.println("jUrl:" + jUrl);
//		System.out.println("user:" + user);
//		System.out.println("pass:" + pass);
		comment1 = argMap.get(AtomDB.COMMENT1);
		comment2 = argMap.get(AtomDB.COMMENT2);

		JDBC jdbcIns = new JDBC(jUrl, user, pass);
		// ------------------------------------------------------------------------
		// XXX 実行前に出力先が存在していたら、そのファイルを削除する。?
		// ------------------------------------------------------------------------
		int count = -1;
		if (jdbcIns != null) {
			outPath = argMap.get(PATH);
			sql = argMap.get(SQL);
			delimiter = argMap.get(DELIMITER, TAB);
			System.out.println("path:" + outPath);
			System.out.println("sql:" + sql);
			System.out.println("delimiter:" + delimiter);
			count = jdbcIns.query2File(outPath, sql, delimiter);
			message += " 出力先:" + argMap.get(PATH) + "\n";
			message += " カウント:" + count + "";
		} else {
			System.out.println(ERR_SQLCMD + "Can't Connect to DB");
		}
	}

	// ------------------------------------------------------------------------
	// XXX デフォルトのjurl、ユーザー、パスワードを環境変数にしても良いかもしれない
	// XXX パラメーターのチェック（verify）をした方が良いかも知れない
	// ------------------------------------------------------------------------
	// for Batch起動（isql=>osql=>sqlcmdが使えない場合などの代替・・・かな）
	// String jURL, String user, String passについてyamlなどに入れる？
	// ------------------------------------------------------------------------
	// ※外部から直接起動する場合を想定しているのでmainを書き換えてはいけない
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("SQLCMD Elapse");
		elapse.start();

		SQLCMD ins = new SQLCMD(args);
		ins.execute();

		String comment = "";
		elapse.setComment(comment);
		elapse.stop();
	}

}
