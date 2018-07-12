package kyPkg.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Joint {
	private static StringBuffer buff;

	// -------------------------------------------------------------------------
	// 使用例＞ kyPkg.util.Joint.join(list);
	// -------------------------------------------------------------------------
	public static String join(Set set, String delimiter) {
		return join(set, delimiter, "");
	}

	// -------------------------------------------------------------------------
	// 使用例＞ kyPkg.util.Joint.join(list);
	// -------------------------------------------------------------------------
	public static String join(List list, String delimiter) {
		return join(list, delimiter, "", 0,null);
	}

	public static String join(List list, String delimiter, int skip) {
		return join(list, delimiter, "", skip,null);
	}

	public static String join(List list, String delimiter, String literal) {
		return join(list, delimiter, literal, 0,null);
	}
	

	
	public static String joinIN(List list) {
		return join(list, ",", "'", 0,"");
	}

	// -------------------------------------------------------------------------
	// 使用例＞ kyPkg.util.Joint.join(array,",");
	// -------------------------------------------------------------------------
	public static String join(String[] array, String delimiter) {
		return join(array, delimiter, "", 0);
	}

	public static String join(String[] array, String delimiter,
			String literal) {
		return join(array, delimiter, literal, 0);
	}

	public static String join(String[] array, String delimiter, int skip) {
		return join(array, delimiter, "", skip);
	}

	// -------------------------------------------------------------------------
	// 使用例＞
	// String str = kyPkg.util.Joint.join(array);
	// System.out.println("array:"+kyPkg.util.KUtil.join(array));
	// -------------------------------------------------------------------------
	public static String join(int[] array, String delimiter) {
		return join(int2StrArray(array), delimiter);
	}

	public static String join(long[] array, String delimiter) {
		return join(long2StrArray(array), delimiter);
	}

	public static String join(float[] array, String delimiter) {
		return join(float2StrArray(array), delimiter);
	}

	// -------------------------------------------------------------------------
	// 使用例＞ kyPkg.util.KUtil.join(list,",","'");
	// -------------------------------------------------------------------------
	private static String join(List list, String delimiter, String literal,
			int skip,String defaultValue) {
		if (list == null || list.size() == 0)
			return defaultValue;
		if (delimiter == null)
			delimiter = "";
		if (buff == null)
			buff = new StringBuffer();
		buff.delete(0, buff.length());
		for (int i = skip; i < list.size(); i++) {
			if (i > skip)
				buff.append(delimiter);
			Object obj = list.get(i);
			if (obj == null)
				obj = "";
			if (obj instanceof String) {
				buff.append(literal);
				buff.append((String) obj);
				buff.append(literal);
			} else {
				buff.append(String.valueOf(obj));
			}
		}
		return buff.toString();
	}

//	private static String join(HashSet<String> set, String delimiter,
//			String literal) {
//		if (set == null || set.size() == 0)
//			return null;
//		if (delimiter == null)
//			delimiter = "";
//		if (buff == null)
//			buff = new StringBuffer();
//		int x = 0;
//		buff.delete(0, buff.length());
//		for (String element : set) {
//			if (x > 0)
//				buff.append(delimiter);
//			if (element == null)
//				element = "";
//			if (element instanceof String) {
//				buff.append(literal);
//				buff.append((String) element);
//				buff.append(literal);
//			}
//			x++;
//		}
//		return buff.toString();
//	}

	// -------------------------------------------------------------------------
	// 使用例＞ kyPkg.util.KUtil.join(set,",","'");
	// -------------------------------------------------------------------------
	public static String join(Set set, String delimiter,String literal) {
		if (set == null || set.size() == 0)
			return null;
		if (delimiter == null)
			delimiter = "";
		if (buff == null)
			buff = new StringBuffer();
		buff.delete(0, buff.length());
		for (Object element : set) {
			if (buff.length() > 0)
				buff.append(delimiter);
			buff.append(literal);
			buff.append((String) element);
			buff.append(literal);
		}
		return buff.toString();
	}

	public static String join(String[] array, String delimiter, String literal,
			int skip) {
		if (array == null || array.length == 0)
			return null;
		if (delimiter == null)
			delimiter = "";
		if (buff == null)
			buff = new StringBuffer();
		buff.delete(0, buff.length());
		for (int i = skip; i < array.length; i++) {
			if (i > skip)
				buff.append(delimiter);
			if (array[i] == null)
				array[i] = "";
			buff.append(literal);
			buff.append(array[i]);
			buff.append(literal);
		}
		return buff.toString();
	}

	// -------------------------------------------------------------------------
	private static String[] int2StrArray(int[] iArray) {
		if (iArray == null)
			return null;
		String[] sArray = new String[iArray.length];
		for (int i = 0; i < iArray.length; i++) {
			sArray[i] = String.valueOf(iArray[i]);
		}
		return sArray;
	}

	private static String[] long2StrArray(long[] lArray) {
		if (lArray == null)
			return null;
		String[] sArray = new String[lArray.length];
		for (int i = 0; i < lArray.length; i++) {
			sArray[i] = String.valueOf(lArray[i]);
		}
		return sArray;
	}

	private static String[] float2StrArray(float[] fArray) {
		if (fArray == null)
			return null;
		String[] sArray = new String[fArray.length];
		for (int i = 0; i < fArray.length; i++) {
			sArray[i] = String.valueOf(fArray[i]);
		}
		return sArray;
	}

	// ########################################################################
	// main
	// ########################################################################
	public static void main(String[] argv) {
		//		range2ArrayTest1();
		testJoin();
	}

	public static void testJoin() {
		List<String> list = new ArrayList();
		list.add("-1");
		list.add("0");
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		String res = kyPkg.util.Joint.join(list, ",");
		System.out.println("res:" + res);
	}

	public static void testJoin2() {
		String[] array = { "-1", "0", "1", "2", "3", "4" };
		String res = kyPkg.util.Joint.join(array, ",");
		System.out.println("res:" + res);
	}

	public static void range2ArrayTest1() {
		String[] tag = { "1", "2", "3", "4", "5", "6" };
		String[] array = kyPkg.util.KUtil.range2Array(0, tag.length);
		for (int i = 0; i < array.length; i++) {
			System.out.println("[" + i + "]" + array[i]);
		}
		System.out.println("join=>" + join(array, ","));
	}

}
