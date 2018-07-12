package globals;

import kyPkg.globals.StaticKeyValue;

public class MM_SERVER extends StaticKeyValue {
	private static StaticKeyValue instance;
	// ------------------------------------------------------------------------
	// key
	// ------------------------------------------------------------------------
	private static final String USER = "USER";
	private static final String PASS = "PASS";
	// ------------------------------------------------------------------------
	// default value ( from 2011/01/06 )
	// ------------------------------------------------------------------------
	private static final String DEFAULT_USER = "qprftp@210.158.199.104";
	private static final String DEFAULT_PASS = "qprftp";

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public MM_SERVER() {
		super();
		super.setName(getClass().getSimpleName());
		super.put(USER, DEFAULT_USER);
		super.put(PASS, DEFAULT_PASS);
		super.save(false);// ymlの設定を優先させるのでfalse
	}

	// ------------------------------------------------------------------------
	// アクセッサ
	// ------------------------------------------------------------------------
	public static void setUser(String val) {
		putIt(USER, val);
	}

	public static void setPass(String val) {
		putIt(PASS, val);
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
			instance = new MM_SERVER();
		return instance;
	}

}
