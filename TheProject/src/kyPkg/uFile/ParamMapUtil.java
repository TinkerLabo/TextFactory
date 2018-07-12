package kyPkg.uFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParamMapUtil {
	//2017-02-01
	public static String getAsString(HashMap<String, String> hmap, String key,
			String defaultVal) {
		if (hmap == null)
			return defaultVal;
		String str = hmap.get(key);
		if (str != null) {
			try {
				return String.valueOf(str);
			} catch (NumberFormatException e) {
				hmap.put(key, String.valueOf(defaultVal));// 副作用あり
			}
		}
		return defaultVal;
	}

	//2017-02-01
	public static int getAsInt(HashMap<String, String> hmap, String key,
			int defaultVal) {
		if (hmap == null)
			return defaultVal;
		String str = hmap.get(key);
		if (str != null) {
			try {
				return Integer.valueOf(str);
			} catch (NumberFormatException e) {
				hmap.put(key, String.valueOf(defaultVal));// 副作用あり
			}
		}
		return defaultVal;
	}

	//2017-02-01
	public static long getAsLong(HashMap<String, String> hmap, String key,
			long defaultVal) {
		if (hmap == null)
			hmap = new HashMap();//意味があるので消さないこと
		String str = hmap.get(key);
		if (str != null) {
			try {
				return Long.valueOf(str);
			} catch (NumberFormatException e) {
				hmap.put(key, String.valueOf(defaultVal));// 副作用あり
			}
		}
		return defaultVal;
	}

	//2017-02-01
	public static float getAsFloat(HashMap<String, String> hmap, String key,
			float defaultVal) {
		if (hmap == null)
			hmap = new HashMap();//意味があるので消さないこと
		String str = hmap.get(key);
		if (str != null) {
			try {
				return Float.valueOf(str);
			} catch (NumberFormatException e) {
				//hmap.put(key, String.valueOf(defaultVal));// NG!
			}
		}
		return defaultVal;
	}

	//2017-02-01
	public static boolean getAsBoolean(HashMap<String, String> hmap, String key,
			boolean defaultVal) {
		if (hmap == null)
			return defaultVal;
		String str = hmap.get(key);
		if (str != null) {
			if (str.trim().toUpperCase().startsWith("T")) {
				return true;
			} else {
				return false;
			}
		}
		return defaultVal;
	}

	private static HashMap<String, String> getTestData() {
		HashMap<String, String> hmap = new HashMap();
		hmap.put("-123", "-123");
		hmap.put("123", "123");
		hmap.put("1.23", "1.23");
		hmap.put("true", "true");
		hmap.put("false", "false");
		hmap.put("T", "T");
		hmap.put("F", "F");
		return hmap;
	}

	public static void test_getAsString() {
		HashMap<String, String> hmap = getTestData();
		List<String> keyList = new ArrayList(hmap.keySet());
		for (String key : keyList) {
			System.out.println("getAsString key:" + key + " => "
					+ ParamMapUtil.getAsString(hmap, key, "NO"));
		}
	}

	public static void test_getAsInt() {
		HashMap<String, String> hmap = getTestData();
		List<String> keyList = new ArrayList(hmap.keySet());
		for (String key : keyList) {
			System.out.println("getAsInt     key:" + key + " => "
					+ ParamMapUtil.getAsInt(hmap, key, -1));
		}
	}

	public static void test_getAsBoolean() {
		HashMap<String, String> hmap = getTestData();
		List<String> keyList = new ArrayList(hmap.keySet());
		for (String key : keyList) {
			System.out.println("getAsBoolean key:" + key + " => "
					+ ParamMapUtil.getAsBoolean(hmap, key, true));
		}
	}

	public static void test_getAsLong() {
		HashMap<String, String> hmap = getTestData();
		List<String> keyList = new ArrayList(hmap.keySet());
		for (String key : keyList) {
			System.out.println("getAsLong     key:" + key + " => "
					+ getAsLong(hmap, key, Long.MIN_VALUE));
		}
	}

	public static void test_getAsFloat() {
		HashMap<String, String> hmap = getTestData();
		List<String> keyList = new ArrayList(hmap.keySet());
		for (String key : keyList) {
			System.out.println("getAsFloat     key:" + key + " => "
					+ ParamMapUtil.getAsFloat(hmap, key, Float.MIN_VALUE));
		}
	}

	public static void test_20170201() {
		test_getAsString();
		test_getAsInt();
		test_getAsBoolean();
		test_getAsLong();
		test_getAsFloat();
	}

	//TODO アサーションを作っておきたいが
	public static void main(String[] args) {
		test_20170201();
	}
}
