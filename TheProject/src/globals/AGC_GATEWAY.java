package globals;

import kyPkg.globals.StaticKeyValue;

public class AGC_GATEWAY extends StaticKeyValue {
	private static StaticKeyValue instance;
	// ------------------------------------------------------------------------
	// key
	// ------------------------------------------------------------------------
	private static final String HOST = "HOST";
	private static final String PORT = "PORT";
	// ------------------------------------------------------------------------
	// default value
	// ------------------------------------------------------------------------
	private static final String DEFAULT_PORT = "8021";
	private static final String DEFAULT_HOSTNAME = "agcisvw3";

	// =>20130331まで "agcisvw";,それ以降"agcisvw3";

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public AGC_GATEWAY() {
		super();
		super.setName(getClass().getSimpleName());
		super.put(HOST, DEFAULT_HOSTNAME);
		super.put(PORT, DEFAULT_PORT);
		super.save(false);// ymlの設定を優先させるのでfalse
	}

	// ------------------------------------------------------------------------
	// アクセッサ
	// ------------------------------------------------------------------------
	public static void setHost(String val) {
		putIt(HOST, val);
	}

	public static void setPort(int val) {
		setPort(String.valueOf(val));
	}

	public static void setPort(String val) {
		putIt(PORT, val);
	}

	public static String getHost() {
		String sHost = getIt(HOST);
		return sHost;
	}

	public static int getPort() {
		String sPort = getIt(PORT);
		try {
			return Integer.valueOf(sPort);
		} catch (Exception e) {
			System.out.println("#error @getPort:" + sPort);
			return -1;
		}
	}

	public static void save() {
		getInstance().save(true);
	}

	private static void putIt(String key, String val) {
		getInstance().put(key, val);
	}

	private static String getIt(String key) {
		return getInstance().get(key);
	}

	private static StaticKeyValue getInstance() {
		if (instance == null)
			instance = new AGC_GATEWAY();
		return instance;
	}

}
