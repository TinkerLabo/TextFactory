package kyPkg.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
//import java.util.Vector;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import kyPkg.uFile.FileUtil;

//import java.util.*;

//kyPkg.util.KUtil.
public class KUtil {
	// public static final String LF = System.getProperty("line.separator");
	// -------------------------------------------------------------------------
	//右から指定文字分取り出す（vbのright関数のような処理）
	//指定文字数に満たない場合は元の文字を返す　20150928
	// -------------------------------------------------------------------------
	public static String right(String _str, int width) {
		int _len = _str.length();
		if (_len > width) {
			return _str.substring(_len - width, _len);
		}
		return _str;
	}
//	public static String right(String str, int width) {
//		if (width > str.length()) {
//			// 足りなければそのままであればStringUtils.rightを使えばよい
//			// width = str.length();
//			// 足りない場合はそのぶんスペースをコンカチ
//			str = StringUtils.rightPad(str, width);
//		}
//		str = str.substring(0, width);
//		return str;
//	}

	
	public static void main(String[] args) {
		// testInt2flags();
		// testTrim();
		testSeqString();
		testRight();
		testLeft();
	}

	public static void testSeqString() {
		for (int i = 0; i <= 256; i++) {
			System.out.println("seqString=>" + seqString(i, 7));
		}
	}

	public static void testRight() {
		for (int i = 0; i <= 30; i++) {
			System.out.println(
					"right=>" + right("Hello this is a test String.", i));
		}
	}

	public static void testLeft() {
		for (int i = 0; i <= 30; i++) {
			System.out.println(
					"Left=>" + left("Hello this is a test String.", i));
		}
	}

	// http://www.jajakarta.org/commons/lang-1.0.1/ja/withPrimary/org/apache/commons/lang/StringUtils.html
	// 　前ゼロの文字列シーケンスを返す
	// ※10桁以上欲しいことはないだろうという見込みで作ってある（intなので・・-2147483648～2147483647）
	public static String seqString(int seq, int width) {
		return left("0000000000" + seq, width);
	}


	public static String left(String str, int width) {
		if (width > str.length()) {
			// width = str.length();
			str = StringUtils.leftPad(str, width);
		}
		str = str.substring(str.length() - width, str.length());
		return str;
	}

	public static void testTrim() {
		List<String> testList = new ArrayList();
		testList.add("          this    is          ");
		testList.add("\"  hello   hello   hello\"");
		for (String str : testList) {
			System.out.println(
					"rtrim>" + str + ">>" + kyPkg.util.KUtil.rTrim(str) + "<<");
			System.out.println(
					"ltrim>" + str + ">>" + kyPkg.util.KUtil.lTrim(str) + "<<");
		}

	}

	public static String rTrim(String str) {
		if (str.charAt(0) == ' ') {
			str = "#" + str;
			str = str.trim();
			return str.substring(1);
		} else {
			return str.trim();
		}
	}

	public static String lTrim(String str) {
		str = str + "#";
		str = str.trim();
		return str.substring(0, str.length() - 1);
	}

	// DRY　kyPkg.util.KUtil.getSortedKeyList()
	// -------------------------------------------------------------------------
	// HashMapのキー部をソートしたリストで返す
	// 例＞List<String> keyList = kyPkg.util.KUtil.getKeyList(hmap);
	// -------------------------------------------------------------------------
	public static List<String> getKeyList(HashMap<String, String> hmap) {
		if (hmap == null)
			return null;
		List<String> keylist = new ArrayList(hmap.keySet());
		Collections.sort(keylist);
		return keylist;
	}

	public static List<String> getKeyList_HSH(
			HashMap<String, HashMap<String, String>> hmap) {
		if (hmap == null)
			return null;
		List<String> keylist = new ArrayList(hmap.keySet());
		Collections.sort(keylist);
		return keylist;
	}

	public static List<Integer> getKeyList_HIH(
			HashMap<Integer, HashMap<String, String>> hmap) {
		if (hmap == null)
			return null;
		List<Integer> keylist = new ArrayList(hmap.keySet());
		Collections.sort(keylist);
		return keylist;
	}

	// public static List<String> getKeyList(HashMap<String, String> hmap) {
	// List<String> keylist = new ArrayList(hmap.keySet());
	// Collections.sort(keylist);
	// return keylist;
	// }

	public static void testInt2flags() {
		boolean[] flag = kyPkg.util.KUtil.int2flags(2 + 8, 8);
		for (int i = 0; i < flag.length; i++) {
			System.out.print(" <" + i + ">" + flag[i]);
			System.out.println("");
		}
	}

	// 数字（合成パラメータ）をフラグに分解する (合成パラメータを分解)
	public static boolean[] int2flags(int option, int maxCol) {
		boolean[] flag = new boolean[maxCol];
		for (int i = flag.length - 1; i >= 0; i--) {
			if (option >= Math.pow(2, i)) {
				option -= Math.pow(2, i);
				flag[i] = true;
			} else {
				flag[i] = false;
			}
		}
		return flag;
	}

	public static void testGetTimeStamp() {
		System.out.println(kyPkg.uDateTime.DateCalc.getTimeStamp());
	}

	// kyPkg.util.KUtil.enumHashMap(HashMap hmap)
	public static void enumHashMap(HashMap hmap) {
		System.out.println("debug@enumHashMap-<start>----------------");
		java.util.Set set = hmap.entrySet(); // 直接iteratorを呼べないので一旦SETを取得する
		java.util.Iterator it = set.iterator();
		while (it.hasNext()) {
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
			String key = (String) ent.getKey();
			String val = (String) ent.getValue();
			System.out.println("enum=> key:" + key + "  val:" + val);
		}
		System.out.println("debug@enumHashMap-<end>------------------");
	}

	public static List mapKey2List(HashMap hmap) {
		List<String> list = new ArrayList();
		java.util.Set set = hmap.entrySet(); // 直接iteratorを呼べないので一旦SETを取得する
		java.util.Iterator it = set.iterator();
		while (it.hasNext()) {
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
			list.add((String) ent.getKey());
			// String key = (String)ent.getKey();
			// String val = (String)ent.getValue();
			// System.out.println("key:"+key + "  val:"+val);
		}
		return list;
	}

	public static List<String> hmap2list(HashMap<String, String> infoMap) {
		String delimiter = "\t";
		if (infoMap == null)
			return null;
		List<String> keylist = new ArrayList(infoMap.keySet());
		Collections.sort(keylist);

		List<String> list = new ArrayList();
		StringBuffer buff = new StringBuffer();
		for (String key : keylist) {
			buff.delete(0, buff.length());
			buff.append(key);
			buff.append(delimiter);
			buff.append(infoMap.get(key));
			list.add(buff.toString());
		}
		return list;
	}

	// public static List<String> hmap2list_org(HashMap<String, String> infoMap,
	// String delimiter) {
	// if (infoMap == null)
	// return null;
	// List<String> list = new ArrayList();
	// Set set = infoMap.entrySet();
	// for (Iterator iterator = set.iterator(); iterator.hasNext();) {
	// Map.Entry entry = (Map.Entry) iterator.next();
	// String key = (String) entry.getKey();
	// String val = (String) entry.getValue();
	// list.add(key + delimiter + val);
	// }
	// Collections.sort(list);
	// return list;
	// }

	public static HashMap<String, String> list2map(List list,
			String delimiter) {
		HashMap<String, String> hmap = new HashMap();
		if (list != null) {
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				String line = (String) iterator.next();
				String[] splited = line.split(delimiter);
				if (splited.length >= 2) {
					hmap.put(splited[0], splited[1]);
				}
			}
		}
		return hmap;
	}

	// 　例＞ int[] array = kyPkg.util.KUtil.getIntArray(7);
	public static int[] getIntArray(int maxCol) {
		return getIntArray(0, maxCol);
	}

	public static int[] getIntArray(int from, int maxCol) {
		int[] rtn = null;
		if (from < maxCol) {
			rtn = new int[maxCol - from];
			// System.out.println("rtn.length>>" + rtn.length);
			int c = 0;
			for (int i = from; i < maxCol; i++) {
				// System.out.println("getIntArray>>"+i);
				rtn[c] = i;
				c++;
			}
		} else {
			System.out.println("#ERROR!! @getIntArray from>>" + from
					+ " maxCol>>" + maxCol);
		}
		return rtn;
	}

	// 　例＞ int[] array = kyPkg.util.KUtil.str2intArray("a,b,c");
	public static int[] str2intArray(String field) {
		String[] splited = field.split(",");
		int[] rtn = new int[splited.length];
		for (int i = 0; i < splited.length; i++) {
			rtn[i] = Integer.parseInt(splited[i]);
		}
		return rtn;
	}

	public static List listReplaceAll(List<String> list, String regex,
			String replacement) {
		for (int i = 0; i < list.size(); i++) {
			list.set(i, list.get(i).replaceAll(regex, replacement));
		}
		return list;
	}

	public static List listModz(List<String> original, List<String> option) {
		int from = 0;
		int len = original.size();
		return listModz(original, from, len, option);
	}

	public static List listModz(List<String> original, int from, int len,
			List<String> option) {
		ArrayList<String> rtnList = new ArrayList();
		for (int i = from; i < len; i++) {
			if (original.size() > i)
				rtnList.add(original.get(i));
		}
		if (rtnList.size() < len) {
			while (rtnList.size() < len) {
				rtnList.add("");
			}
		}
		if (option != null) {
			for (int i = 0; i < option.size(); i++) {
				rtnList.add(option.get(i));
			}
		}
		return rtnList;
	}

	public static String[] arrayModz(String[] original, int from, int len,
			String[] option) {
		List originalList = null;
		List optionList = null;
		if (original != null)
			originalList = Arrays.asList(original);
		if (option != null)
			optionList = Arrays.asList(option);

		List<String> list = listModz(originalList, from, len, optionList);
		return list.toArray(new String[list.size()]);
	}

	// ---+---------+---------+---------+---------+---------+---------+---------+
	// ListをStringに書き出す
	// pStr 書き出すString
	// pVec 読み込み元となるVector
	// pDlm 各フィールドを区切るデリミタ文字
	// ---+---------+---------+---------+---------+---------+---------+---------+
	public static String matrix2String(List<List> list, String delimiter) {
		StringBuffer buff = new StringBuffer();
		for (Object wObj : list) {
			if (wObj instanceof Vector) {
				for (int j = 0; j < ((Vector) wObj).size(); j++) {
					if (j > 0)
						buff.append(delimiter);
					buff.append(((Vector) wObj).get(j));
				}
			} else {
				if (buff.length() != 0)
					buff.append(delimiter);
				buff.append(wObj);
			}
			buff.append(FileUtil.LF);

		}
		return (buff.toString());
	}

	// public static String list2String(List<String> list) {
	// return list2String(list, "");
	// }
	public static String list2StringLF(List<String> list, String delimiter) {
		return list2String(list, delimiter, FileUtil.LF);
	}

	public static String list2String(List<String> list, String delimiter) {
		return list2String(list, delimiter, "");
	}

	public static String list2String(List<String> list, String delimiter,
			String eol) {
		if (list == null || list.size() == 0)
			return null;
		StringBuffer buff = new StringBuffer();
		buff.delete(0, buff.length());
		for (String element : list) {
			if (buff.length() != 0)
				buff.append(delimiter);
			buff.append(element);
			buff.append(eol);
		}
		return (buff.toString());
	}

	public static String array2StringLF(String[] array, String delimiter) {
		return array2StringLF(array, delimiter, FileUtil.LF);
	}

	public static String array2String(String[] array, String delimiter) {
		return array2StringLF(array, delimiter, "");
	}

	//20151215 bugfix
	public static String array2StringLF(String[] array, String delimiter,
			String eol) {
		if (array == null || array.length == 0)
			return null;
		StringBuffer buff = new StringBuffer();
		buff.delete(0, buff.length());
		for (int col = 0; col < array.length; col++) {
			if (col > 0)
				buff.append(delimiter);
			buff.append(array[col]);
			buff.append(eol);

		}
		//		for (String element : array) {
		//			if (buff.length() != 0)
		//				buff.append(delimiter);
		//			buff.append(element);
		//			buff.append(eol);
		//		}
		return (buff.toString());
	}

	public static int[] str2IntArray(String[] sArray) {
		if (sArray == null)
			return null;
		int[] iArray = new int[sArray.length];
		for (int i = 0; i < sArray.length; i++) {
			iArray[i] = Integer.parseInt(sArray[i]);
		}
		return iArray;
	}

	public static String[] ModArray(String[] array, String val) {
		// List list = Arrays.asList(array);
		// list.add(val);
		// return (String[]) list.toArray(new String[list.size()]);
		String[] rtnArray = new String[array.length + 1];
		for (int index = 0; index < array.length; index++) {
			rtnArray[index] = array[index];
		}
		rtnArray[rtnArray.length - 1] = val;
		return rtnArray;
	}

	// 使用例＞ array = kyPkg.util.KUtil.ModArray(array,val);
	public static int[] ModArray(int[] array, int val) {
		int[] rtnArray = new int[array.length + 1];
		for (int index = 0; index < array.length; index++) {
			rtnArray[index] = array[index];
		}
		rtnArray[rtnArray.length - 1] = val;
		return rtnArray;
	}

	public static String[] range2Array(int from, int size) {
		String[] array = new String[size];
		for (int i = 0; i < size; i++) {
			array[i] = String.valueOf(from + i);
		}
		return array;
	}

	public static void arrayModtest1() {
		String[] array1 = { "T", "O", "K", "Y", "U" };
		String[] array2 = { "-", "A", "G", "C" };
		String[] array3 = arrayMod(array1, array2);
		for (int i = 0; i < array3.length; i++) {
			System.out.println("array3[" + i + "]" + array3[i]);
		}
	}

	public static void arrayModtest2() {
		int[] array1 = { 1, 3, 5, 7, 9 };
		int[] array2 = { 2, 4, 6, 8 };
		int[] array3 = kyPkg.util.KUtil.arrayMod(array1, array2);
		for (int i = 0; i < array3.length; i++) {
			System.out.println("array3[" + i + "]" + array3[i]);
		}
	}

	// nullかどうかは見ていない・・・
	public static String[] arrayMod(String[] array_R, String[] array_L) {
		int idx = 0;
		String[] array = new String[array_R.length + array_L.length];
		for (int i = 0; i < array_R.length; i++) {
			array[idx++] = array_R[i];
		}
		for (int i = 0; i < array_L.length; i++) {
			array[idx++] = array_L[i];
		}
		return array;
	}

	public static int[] arrayMod(int[] array_R, int[] array_L) {
		int idx = 0;
		int[] array = new int[array_R.length + array_L.length];
		for (int i = 0; i < array_R.length; i++) {
			array[idx++] = array_R[i];
		}
		for (int i = 0; i < array_L.length; i++) {
			array[idx++] = array_L[i];
		}
		return array;
	}

	// obj.split でヌルポになるのを避ける為・・・
	public static String[] splitStr(String str) {
		if (str == null)
			return null;
		return str.split(",");
	}

	// パラメータの数がリミット数に補正（補完）される
	public static String[] split2Str_Lim(String str, int limit) {
		return split2Str_Lim(str, limit, "");
	}

	public static String[] split2Str_Lim(String str, int limit, String defVal) {
		// System.out.println("@splitLimited chkCount:" + limit);
		String[] array = new String[limit];
		for (int i = 0; i < array.length; i++) {
			if (defVal.equals("")) {
				array[i] = "No." + i; // 仮の名前 unitの場合まずい
			} else {
				array[i] = defVal;
			}
		}
		if (str == null)
			return array;
		String[] splited = str.split(",");
		for (int i = 0; i < splited.length; i++) {
			// System.out.println("splited[" + i + "]:" + splited[i]);
			array[i] = splited[i];
		}
		return array;
	}

	public static int[] split2Int_Lim(String str, int chkCount, int defVal) {
		int[] intArray = new int[chkCount];
		for (int i = 0; i < intArray.length; i++) {
			intArray[i] = defVal;
		}
		if (str == null)
			return intArray;
		String[] splited = str.split(",");
		for (int i = 0; i < splited.length; i++) {
			try {
				intArray[i] = Integer.parseInt(splited[i].trim());
			} catch (java.lang.NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return intArray;
	}

	// 文字を数値配列に変換する
	public static int[] split2Int(String str) {
		if (str == null)
			return null;
		str = str.trim();
		if (str.equals(""))
			return null;
		// パラメータを信頼してチェックしていない、あしからず！!
		String[] splited = str.split(",");
		int[] intArray = new int[splited.length];
		for (int i = 0; i < splited.length; i++) {
			try {
				intArray[i] = Integer.parseInt(splited[i].trim());
			} catch (java.lang.NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return intArray;
	}

	public static String join(int[] intArray) {
		return join(intArray, ",");
	}

	public static String join(String[] strArray) {
		return join(strArray, ",","");
	}
	public static String join(String[] strArray,String delimiter) {
		return join(strArray, delimiter, "");
	}

	public static String join(int[] intArray, String delimiter) {
		StringBuffer buff = new StringBuffer();
		if (intArray != null && intArray.length > 0) {
			buff.append(intArray[0]);
			for (int i = 1; i < intArray.length; i++) {
				buff.append(delimiter);
				buff.append(intArray[i]);
			}
		}
		return buff.toString();
	}

	public static String join(String[] strArray, String delimiter,String literal) {
		return join(strArray, delimiter, false,literal);
	}

	public static String join(String[] strArray, String delimiter,
			boolean opt,String literal) {
		StringBuffer buff = new StringBuffer();
		if (strArray != null && strArray.length > 0) {
			buff.append(literal);//20170525
			buff.append(strArray[0]);
			buff.append(literal);//20170525
			for (int i = 1; i < strArray.length; i++) {
				buff.append(delimiter);
				buff.append(literal);//20170525
				buff.append(strArray[i]);
				buff.append(literal);//20170525
			}
			if (opt)
				buff.append(delimiter);
		}
		return buff.toString();
	}

	// public static Integer[] list2IntegerArray(List<String> list) {
	// String[] strArray = (String[]) list.toArray(new String[list.size()]);
	// Integer[] intArray = new Integer[strArray.length];
	// for (int i = 0; i < strArray.length; i++) {
	// intArray[i] = new Integer(Integer.parseInt(strArray[i]));
	// }
	// return intArray;
	// }

	public static int[] list2intArray(List<String> list) {
		// return (int[]) list.toArray(new int[list.size()]); うーむ
		String[] strArray = (String[]) list.toArray(new String[list.size()]);
		int[] intArray = new int[strArray.length];
		for (int i = 0; i < strArray.length; i++) {
			intArray[i] = Integer.parseInt(strArray[i]);
		}
		return intArray;
	}

	// listから配列に変換（未テスト）
	// List list = new ArrayList();
	// list.add("boo");list.add("foo");list.add("woo");
	// String[] array = kyPkg.util.KUtil.list2strArray(list);
	public static String[] list2strArray(List list) {
		return (String[]) list.toArray(new String[list.size()]);
	}

	// 配列からlistに変換（未テスト）
	// String[] array={"boo","foo","woo"};
	// List list = kyPkg.util.KUtil.array2list(array);
	public static List<String> array2list(String[] array) {
		return Arrays.asList(array);
	}

	public static List<Integer> intArray2List(int[] array) {
		List list = new ArrayList();
		for (int i = 0; i < array.length; i++) {
			list.add(new Integer(array[i]));
		}
		return list;
	}

	/**
	 * 半角数字文字列かどうか
	 * 
	 * @param pStr
	 *            検査文字列
	 * @return 半角数字のみの文字列ならtrue
	 */
	public static boolean isNumeric(String pStr) {
		// System.out.println("isNumeric in :" + pStr);
		boolean wRtn = false;
		if (pStr.matches("[+-]*\\d+[.]*\\d*")) {
			// System.out.println("チェック結果：半角数字のみの文字列です。");
			wRtn = true;
		} else {
			// System.out.println("チェック結果：半角数字以外の文字が含まれます。");
			wRtn = false;
		}
		// System.out.println("isNumeric Ans:" + wRtn);
		return wRtn;
	}

	// ---+---------+---------+---------+---------+---------+---------+---------+
	// isNumeric 文字列が数値とし認識できるか
	// @param str チェックする文字列
	// @return
	// ---+---------+---------+---------+---------+---------+---------+---------+
	// 例》if ( kUtil.isNumeric("./RC.log") ){}
	// ---+---------+---------+---------+---------+---------+---------+---------+
	public static boolean isNumericxx(String str) {
		try {
			// もしstrがInteger.MAX_VALUEを超える数値だった場合これはどんな値を返すだろう？
			// 予防線として長さでもってなんとかできないだろうか？
			System.out.println("isNumeric:" + str);
			if (str.length() <= 9) {
				int i = Integer.parseInt(str);
				System.out.println(" parse=>" + i);
				return true;
			}
			return false;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

}
