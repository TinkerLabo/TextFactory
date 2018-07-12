package kyPkg.uFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import globals.ResControl;

public class HashMapObj {
	private List<String> keylist;// keyÇÃèáèòÇãLò^Ç≥ÇπÇÈ
	private HashMap<String, String> hmap;
	private static final String TAB = "\t";
	private static final String COMMA = ",";

	public HashMapObj() {
		super();
		keylist = new ArrayList();
		hmap = new HashMap();
	}

	public HashMapObj(String path) {
		this();
		load(path);
	}

	private void load(String path) {
		hmap.clear();
		keylist.clear();
		if (!new File(path).exists())
			return;
		List<String> list = ListArrayUtil.file2List(path);
		for (String element : list) {
			String[] array = element.split(TAB);
			if (array.length >= 2) {
				keylist.add(array[0]);
				hmap.put(array[0], array[1]);
			}
		}
	}

	public void save(String path) {
		HashMapUtil.hashMap2File(path, hmap);
	}

	public List<String> getKeylist() {
		return keylist;
	}

	public int getByInt(String key) {
		int n = Integer.MIN_VALUE;
		String str = getByString(key);
		if (str == null)
			return n;
		try {
			n = Integer.parseInt(str.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return n;
	}

	public String getByString(String key, String defaultValue) {
		String val = hmap.get(key);
		if (val == null) {
			System.out.println("#@getByString# Could not find key:" + key);
			System.out.println(
					"                 => use  defaultValue:" + defaultValue);
			if (defaultValue != null)
				hmap.put(key, defaultValue);
			return defaultValue;
		} else {
			return val;
		}
	}

	public String getByString(String key) {
		return getByString(key, null);
	}

	private String[] getByArray(String key) {
		String str = getByString(key);
		if (str == null)
			return null;
		return str.split(COMMA);
	}

	public List<String> getByList(String key, List<String> defaultValue) {
		String[] array = getByArray(key);
		if (array == null) {
			System.out.println("#@getByList# Could not find key:" + key);
			System.out.println(
					"                 => use  defaultValue:" + defaultValue);
			StringBuffer buf = new StringBuffer();
			buf.append(defaultValue.get(0));
			for (int i = 1; i < defaultValue.size(); i++) {
				buf.append(COMMA);
				buf.append(defaultValue.get(i));
			}
			hmap.put(key, buf.toString());
			return defaultValue;
		}
		return java.util.Arrays.asList(array);

	}

	// private List<String> getByList(String key) {
	// String[] array = getByArray(key);
	// if (array == null)
	// return null;
	// return java.util.Arrays.asList(array);
	// }

	private void addKey(String key) {
		if (!keylist.contains(key))
			keylist.add(key);
	}

	public void put(String key, String val) {
		hmap.put(key, val);
		addKey(key);
	}

	public void set(String key, String[] array) {
		StringBuffer buf = new StringBuffer();
		for (String element : array) {
			buf.append(element);
			buf.append(COMMA);
		}
		buf.deleteCharAt(buf.length());
		hmap.put(key, buf.toString());
		addKey(key);
	}

	public void set(String key, List<String> list) {
		if (list == null || list.size() == 0)
			return;
		StringBuffer buf = new StringBuffer();
		buf.append(list.get(0));
		for (int i = 1; i < list.size(); i++) {
			buf.append(COMMA);
			buf.append(list.get(i));
		}
		System.out.println("20141017 set:key" + key + " buf:" + buf.toString());
		hmap.put(key, buf.toString());
		addKey(key);
	}

	public void debug() {
		for (String key : keylist) {
			System.out.println("key:" + key + "  val:" + hmap.get(key));
		}
	}

	// ########################################################################
	public static void test01() {
		String path = ResControl.SVREPOSITORY;
		HashMapObj hmapObj = new HashMapObj(path);
		// ins.debug();
		System.out.println("----------------------------------------------");
		List<String> level1 = hmapObj.getKeylist();
		for (String element : level1) {
			System.out.println("level1:" + element);
		}
		System.out.println("----------------------------------------------");
		String child1 = hmapObj.getByString(level1.get(0));
		System.out.println("child1:" + child1);
		List<String> level2 = kyPkg.uFile.FileUtil.getDirList(child1);
		for (String element : level2) {
			System.out.println("level2:" + element);
		}
		String child2 = child1 + "/" + level2.get(0);
		System.out.println("----------------------------------------------");
		System.out.println("child2:" + child2);
		List<String> level3 = kyPkg.uFile.FileUtil.getDirList(child2);
		for (String element : level3) {
			System.out.println("level3:" + element);
		}
	}

	public static void main(String[] argv) {
		test01();
	}
}
