package globals;

import kyPkg.globals.StaticKeyValue;

public class GWHOST extends StaticKeyValue {
	private static StaticKeyValue instance;
	// ------------------------------------------------------------------------
	// key
	// ------------------------------------------------------------------------
	private static final String HOST = "HOST";
	private static final String USER = "USER";
	private static final String PASS = "PASS";
	// ------------------------------------------------------------------------
	// default value
	// ------------------------------------------------------------------------
	public static final String DEFAULT_LOCAL = "local";//ホストに接続しない
	public static final String DEFAULT_HOSTNAME = "gwhost";
	private static final String DEFAULT_USER = "EJFTP";
	private static final String DEFAULT_PASS = "QPRFTP";

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public GWHOST() {
		super();
		super.setName(getClass().getSimpleName());
		super.put(HOST, DEFAULT_HOSTNAME);
		super.put(USER, DEFAULT_USER);
		super.put(PASS, DEFAULT_PASS);
		super.save(false);// ymlの設定を優先させるのでfalse
	}

	// ------------------------------------------------------------------------
	// アクセッサ
	// ------------------------------------------------------------------------
	public static void setHost(String val) {
		putIt(HOST, val);
	}

	public static void setUser(String val) {
		putIt(USER, val);
	}

	public static void setPass(String val) {
		putIt(PASS, val);
	}

	public static String getHost() {
		String sHost = getIt(HOST);
		return sHost;
	}

	public static String getUser() {
		return getIt(USER);
	}

	public static String getPass() {
		return getIt(PASS);
	}

	private static void putIt(String key, String val) {
		getInstance().put(key, val);
	}

	private static String getIt(String key) {
		return getInstance().get(key);
	}

	public static void save() {
		getInstance().save(true);
	}

	private static StaticKeyValue getInstance() {
		if (instance == null)
			instance = new GWHOST();
		return instance;
	}

}
