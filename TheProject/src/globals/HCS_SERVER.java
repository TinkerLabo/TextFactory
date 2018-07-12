package globals;

import kyPkg.globals.StaticKeyValue;

public class HCS_SERVER extends StaticKeyValue {
	private static StaticKeyValue instance;
	// ------------------------------------------------------------------------
	// key
	// ------------------------------------------------------------------------
	private static final String USER = "USER";
	private static final String PASS = "PASS";
	private static final String SERVERDIR = "SERVERDIR";
	private static final String REGEX = "REGEX";
	private static final String PATTERN = "PATTERN";

	// ------------------------------------------------------------------------
	// default value
	// ------------------------------------------------------------------------
	private static final String DEFAULT_USER = "S001ftp@202.253.106.33";
	private static final String DEFAULT_PASS = "R85VMCG6";
	private static final String DEFAULT_SERVERDIR = "./";
	private static final String DEFAULT_REGEX = "DIS00060\\d*STD\\.*.*";
	private static final String DEFAULT_PATTERN = "DIS00060*";

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public HCS_SERVER() {
		super();
		super.setName(getClass().getSimpleName());
		super.put(USER, DEFAULT_USER);
		super.put(PASS, DEFAULT_PASS);
		super.put(SERVERDIR, DEFAULT_SERVERDIR);
		super.put(REGEX, DEFAULT_REGEX);
		super.put(PATTERN, DEFAULT_PATTERN);
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

	public static void setServerdir(String val) {
		putIt(SERVERDIR, val);
	}
	public static void setRegex(String val) {
		putIt(REGEX, val);
	}
	public static void setPattern(String val) {
		putIt(PATTERN, val);
	}

	public static String[] getServerdirs() {
		String val = getIt(SERVERDIR);
		if (val == null)
			return new String[] { DEFAULT_SERVERDIR };
		return val.split(",", -1);
	}
	public static String[] getRegexes() {
		String val = getIt(REGEX);
		if (val == null)
			return new String[] { DEFAULT_REGEX };
		return val.split(",", -1);
	}
	public static String[] getPatterns() {
		String val = getIt(PATTERN);
		if (val == null)
			return new String[] { DEFAULT_PATTERN };
		return val.split(",", -1);
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
			instance = new HCS_SERVER();
		return instance;
	}
}
