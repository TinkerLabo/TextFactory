package kyPkg.pmodel;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import globals.ResControlWeb;
import kyPkg.uFile.DosEmu;

public class JsonU {
	private static final String wCRLF = "\r\n"; // XXX for DEBUG



	public static String list2JSON(String name, List list) {
		if (list == null) {
			list = new ArrayList();
			list.add("Error");
		}
		StringBuffer buff = null;
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			if (buff == null) {
				buff = new StringBuffer(wCRLF + "{\"" + name + "\":[");
			} else {
				buff.append(",");
			}
			buff.append(wCRLF + "\"" + element + "\"");
		}
		buff.append(wCRLF + "]}");
		return buff.toString();
	}

	public static String ｓｔｒ2JSON(String name, String str) {
		if (str == null)
			str = "";
		str = str.trim();
		if (str.equals(""))
			str = "NULL";
		StringBuffer buff = new StringBuffer("{" + name + ":{" + str + "}}");
		return buff.toString();
	}

	public static String array2JSON(String name, String[] array) {
		if (array == null)
			array = new String[] { "NG" };
		String enclosure = "\"";// "\""
		if (array == null || array.length == 0) {
			array = new String[] { "NULL" };
		}
		StringBuffer buff = new StringBuffer(wCRLF + "{" + name + ":[");
		for (int i = 0; i < array.length; i++) {
			if (i > 0)
				buff.append(",");
			buff.append(wCRLF + enclosure + array[i] + enclosure);
		}
		buff.append(wCRLF + "]}");
		return buff.toString();
	}

	private static String openFile(String fileName) throws IOException {
		File file = new File(fileName);
		int size = (int) file.length();
		int chars_read = 0;
		FileReader in = new FileReader(file);
		char[] data = new char[size];
		chars_read = in.read(data, 0, size);
		in.close();
		return new String(data, 0, chars_read);
	}

	// （key tab 値１,値2） → {key1:値１,key2:値2...keyN:値N},と変換する
	// ファイルの内容をＪｓｏｎ化
	public static String file2Json(String name, String path) {
		StringBuffer buffX = new StringBuffer("{'" + name + "':{" + wCRLF);
		StringBuffer buffY = new StringBuffer();
		String[] splited1 = null;
		String[] splited2 = null;
		String[] lines = null;
		int lineCnt = 0;
		if (new File(path).isFile()) {
			try {
				// 対象ファイルは小さいと仮定
				String text = openFile(path);
				lines = text.split("\n");// ヌルポの可能性あり・・・
				for (int ndx = 0; ndx < lines.length; ndx++) {
					splited1 = lines[ndx].trim().split("\t");
					if (splited1.length > 1) {
						String key = splited1[0];
						buffY.delete(0, buffY.length());
						splited2 = splited1[1].split(",");
						buffY.append("'");
						buffY.append(splited2[0]);
						buffY.append("'");
						for (int i = 1; i < splited2.length; i++) {
							buffY.append(",");
							buffY.append("'");
							buffY.append(splited2[i]);
							buffY.append("'");
						}
						if (lineCnt > 0) {
							buffX.append(",");
							buffX.append(wCRLF);
						}
						lineCnt++;
						buffX.append(key);
						buffX.append(":[");
						buffX.append(buffY.toString());
						buffX.append("]");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		buffX.append(wCRLF + "}}");
		return buffX.toString();
	}

	public static HashMap<String, String> file2hash(String path) {
		return file2hash(path, "\t", true);
	}

	public static String[] file2array(List list) {
		if (list != null) {
			return (String[]) list.toArray(new String[list.size()]);
		}
		return new String[] { "Notfound" };
	}

	public static List<String> file2List(String path) {
		try {
			String wRec = "";
			String[] lines = null;
			List<String> list = new ArrayList();
			if (new File(path).isFile()) {
				String text = openFile(path);
				lines = text.split("\n");
				for (int idx = 0; idx < lines.length; idx++) {
					wRec = lines[idx].trim();
					if (!wRec.equals(""))
						list.add(wRec);
				}
			}
			return list;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static HashMap<String, String> file2hash(String path,
			String delimiter, boolean trimOption) {
		HashMap<String, String> hmap = new HashMap();
		String wRec = "";
		if (new File(path).isFile()) {
			try {
				String text = openFile(path);
				String[] lines = text.split("\n");
				for (int ndx = 0; ndx < lines.length; ndx++) {
					wRec = lines[ndx].trim();
					// System.out.println("@file2hash wRec:" + wRec);
					if (!wRec.equals("")) {
						String[] array = wRec.split(delimiter);
						if (array.length >= 2) {
							if (trimOption) {
								array[0] = array[0].trim().replaceAll("　", "");
								// System.out.println("array[0] >>" +
								// array[0]+"<<");
								// System.out.println("array[1] >>" +
								// array[1]+"<<");
							}
							hmap.put(array[0], array[1]);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("@file2hash not a file :" + path);
		}
		// System.out.println("---------------------------------------------------");
		// java.util.Set set = hmap.entrySet(); // 直接iteratorを呼べないので一旦SETを取得する
		// java.util.Iterator it = set.iterator();
		// while (it.hasNext()) {
		// java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
		// String key = (String) ent.getKey();
		// String val = (String) ent.getValue();
		// System.out.println("key<" + key + "> val:" + val);
		// }
		// System.out.println("---------------------------------------------------");

		return hmap;
	}

	public static void test01x() {
		String[] array;
		String itpDir = globals.ResControlWeb.getD_Resources_USER_ITP_COMMON();
		String pattern = "*";
		// ファイル名の一覧
		// array = DosEmu.fileList2StrArray(targetPath, pattern);
		// ディレクトリ名の一覧
		array = DosEmu.dirList2Array(itpDir, pattern);
		// ファイルの内容を配列に
		// array = file2array(targetPath);
		// for (int i = 0; i < array.length; i++) {
		// System.out.println("test01=>"+array[i]) ;
		// }
		System.out.println("array2JSON=>" + array2JSON("jsonRes", array));
	}

	public static void test01() {
		String path = ResControlWeb.getD_Resources_ATOM("freakout01.atx");
		String json = file2Json("list", path);
		// for (int i = 0; i < array.length; i++) {
		// System.out.println("=>"+array[i]) ;
		// }
		System.out.println("result=>" + json);
	}

	public static void main(String[] argv) {
		test01();
	}

}