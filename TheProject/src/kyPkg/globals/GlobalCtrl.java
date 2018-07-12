package kyPkg.globals;

import java.util.HashMap;

public class GlobalCtrl {
	protected static final HashMap hashMap = new HashMap();

	public static void put(String key, boolean flag) {
		if (key == null)
			return;
		if (flag) {
			GlobalCtrl.hashMap.put(key, key);
		} else {
			GlobalCtrl.hashMap.put(key, "");
		}
	}

	public static void put(String key, String value) {
		if (key == null)
			return;
		GlobalCtrl.hashMap.put(key, value);
	}

	// public static Object getObject(String key) {
	// return GlobalCtrl.hashMap.get(key);
	// }

	private static String getString(String key) {
		Object obj = GlobalCtrl.hashMap.get(key);
		return (String) obj;
	}

	public static boolean isNotBlank(String key) {
		return (!getValue(key, "").equals(""));
	}

	public static String getValue(String key) {
		return getValue(key, "");
	}

	public static String getValue(String key, String defaultVal) {
		Object obj = GlobalCtrl.hashMap.get(key);
		if (obj == null) {
			return defaultVal;
		} else {
			return (String) obj;
		}
	}

	public static boolean equals(String key, String checkVal) {
		String setVal = getString(key);
		if (setVal != null && setVal.equals(checkVal)) {
			return true;
		} else {
			return false;
		}
	}

	public static int getInt(String key) {
		int rtn = Integer.parseInt(getString(key));
		return rtn;
	}

	public static void main(String[] argv) {
		test20130313();
	}

	public static void test20130313() {
		System.out.println("pre getComponent:"
				+ kyPkg.globals.GlobalCtrl.getValue("Theme", "default"));

		// 半固定パラメーターはシングルトンクラスに格納する（外部パラメータ値が存在すれば変更する、無ければデフォルトを使用する）
		kyPkg.globals.GlobalCtrl.put("Theme", "DarknessTheme");

		System.out.println("------------------------------------------------");
		System.out.println("aft getComponent:"
				+ kyPkg.globals.GlobalCtrl.getValue("Theme", "default"));

	}

}