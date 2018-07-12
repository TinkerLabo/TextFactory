package kyPkg.uFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import globals.ResControl;
import kyPkg.filter.EzReader;
import kyPkg.filter.Inf_iClosure;
import kyPkg.rez.HashList;

public class HashMapUtil {
	private static final String TAB = "\t";
	static final String LF = System.getProperty("line.separator");
	private static final String DEFAULT_ENCODE = "shift-jis";
	private static boolean DEBUG = false;
	//	private static final String TAB = TAB;

	public static Boolean getAsBoolean(HashMap<String, Object> hmap,
			String key) {
		return getAsBoolean(hmap, key, false);
	}

	public static Boolean getAsBoolean(HashMap<String, Object> hmap, String key,
			Boolean defaultValue) {
		if (hmap == null)
			hmap = new HashMap();//意味があるので消さないこと
		Object val = hmap.get(key);
		if (val != null || val instanceof Boolean) {
			return (Boolean) val;
		} else {
			hmap.put(key, defaultValue);//※副作用あり
			return defaultValue;
		}
	}

	public static String getAsString(HashMap<String, Object> hmap, String key) {
		return getAsString(hmap, key, "");
	}

	public static String getAsString(HashMap<String, Object> hmap, String key,
			String defaultValue) {
		if (hmap == null)
			hmap = new HashMap();//意味があるので消さないこと
		Object val = hmap.get(key);
		if (val != null || val instanceof String) {
			return (String) val;
		} else {
			hmap.put(key, defaultValue);//※副作用あり
			return defaultValue;
		}
	}

	public static String getKeyValue(HashMap<String, String> hMap, String key) {
		return getKeyValue(hMap, key, "");
	}

	public static String getKeyValue(HashMap<String, String> hMap, String key,
			String defVal) {
		String val = defVal;
		if (hMap != null) {
			val = hMap.get(key);
		}
		return val;
	}

	public static String getExtra(HashMap<String, String> extra, String key,
			String defaultVal) {
		if (extra != null) {
			String val = extra.get(key);
			if (val != null)
				return val;
		}
		return defaultVal;
	}

	// kyPkg.uFile.HashMapUtil.hashMap2File(path,infoMap);
	public static int hashMap2File(String path,
			HashMap<String, String> infoMap) {
		return hashMap2File(path, infoMap, TAB);
	}

	public static int hashMap2File(String path, HashMap<String, String> hMap,
			String delimiter) {
		// キーの重複が考えられていないので..append = falseとした
		if (hMap == null)
			return -1;
		boolean append = false;
		int count = 0;
		FileUtil.makeParents(path); // 親パスが無ければ作る
		StringBuffer buff = new StringBuffer();
		try {
			String wRec = "";
			BufferedWriter bw = new BufferedWriter(new FileWriter(path, append));
			List<String> keylist = new ArrayList(hMap.keySet());
			Collections.sort(keylist);
			for (String key : keylist) {
				buff.delete(0, buff.length()); // バッファをクリア
				buff.append(key);
				buff.append(delimiter);
				buff.append(hMap.get(key));
				buff.append(LF); // 改行文字
				wRec = buff.toString();
				bw.write(wRec, 0, wRec.length());
				count++;
			}
			bw.close();
		} catch (IOException ie) {
			ie.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return count;
	}

	// ハッシュマップの値の部分のみを出力する(2013-07-09)
	public static int hashMapValue2File(String path,
			HashMap<String, String> hMap) {
		boolean append = false;
		int count = 0;
		FileUtil.makeParents(path); // 親パスが無ければ作る
		StringBuffer buff = new StringBuffer();
		try {
			String wRec = "";
			BufferedWriter bw = new BufferedWriter(new FileWriter(path, append));
			List<String> keylist = new ArrayList(hMap.keySet());
			Collections.sort(keylist);
			for (String key : keylist) {
				buff.delete(0, buff.length()); // バッファをクリア
				buff.append(hMap.get(key));

				buff.append(LF); // 改行文字
				wRec = buff.toString();
//				System.err.println("#20170424#  key>>"+key+" val:"+wRec);
				bw.write(wRec, 0, wRec.length());
				count++;
			}
			bw.close();
		} catch (IOException ie) {
			ie.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return count;
	}

	public static HashMap<String, String> modHashWithList(
			HashMap<String, String> hashMap, List<String> list,
			String delimiter) {
		if (hashMap == null) {
			hashMap = new HashMap<String, String>();
		} else {
			// hashMap = clone???の方が副作用を考えなくていいのではないか？
		}
		if (list == null) {
			System.out.println("Error!!@FileUtil.modList2hash list == null");
			return hashMap;
		}
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			String[] splited = element.split(delimiter);
			if (splited.length >= 2)
				hashMap.put(splited[0], splited[1]);
		}
		return hashMap;
	}

	public static HashMap file2HashMapX(String pPath) {
		return file2HashMapX(pPath, false);
	}

	// キーをすべて大文字に変換する場合（処理を簡単にするため・・・）
	public static HashMap file2HashMapX(String pPath, boolean uCaseOpt) {
		HashMap<String, String> hMap = new HashMap();
		if (file2HashMapX(hMap, pPath, 0, 1, TAB, uCaseOpt)) {
			return hMap;
		} else {
			return null;
		}
	}

	public static boolean file2HashMapX(HashMap wHtbl, String pPath,
			int pKeyCol) {
		return file2HashMapX(wHtbl, pPath, pKeyCol, -1, TAB, false);
	}

	public static boolean file2HashMapX(HashMap hash, String path, int keyCol,
			int targetCol, String delimiter, boolean ucaseOpt) {
		String wRec;
		File fl = new File(path);
		if (fl.exists() == false)
			return false;
		hash.clear();
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				wRec = br.readLine();
				if (wRec != null) {
					String[] wArray = wRec.split(delimiter);
					if (wArray.length > keyCol) {
						String wKey = wArray[keyCol].trim();

						wKey = wKey.replace("　", "");// 暫定的なフィルタ処理
						// キーはすべて大文字に変換する（オプション）
						if (ucaseOpt)
							wKey = wKey.toUpperCase();
						String wVal = "";
						if (targetCol == -1) {
							// targetCol が -1　なら全体をタブで区切ったものとする
							wVal = ListArrayUtil.array2String(wArray, TAB);
						} else {
							if (wArray.length > targetCol)
								wVal = wArray[targetCol];
						}

						if (!wKey.equals(""))
							// System.out.println("wKey:" + wKey+" wVal:" +
							// wVal);
							hash.put(wKey, wVal);
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		// System.out.println("map1:"+hash.get("16888"));

		return true;
	}

	public static boolean file2HashMapType2(HashMap hash, String path,
			int keyCol, String delimiter, String fixedVal) {
		String wRec;
		File fl = new File(path);
		if (fl.exists() == false)
			return false;
		// hash.clear();
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				wRec = br.readLine();
				if (wRec != null) {
					String[] wArray = wRec.split(delimiter);
					if (wArray.length > keyCol) {
						String wKey = wArray[keyCol].trim();
						wKey = wKey.replace("　", "");// 暫定的なフィルタ処理
						if (!wKey.equals(""))
							hash.put(wKey, fixedVal);
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return true;
	}

	public static boolean file2HashMomo(HashMap<String, String> wHtbl,
			String path) {
		String wRec;
		String wKey;
		String wVal;
		File fl = new File(path);
		if (fl.exists() == false) {
			System.out.println("■FileNotFound:" + path);
			return false;
		}
		System.out.println("■■■FileUtil.file2HashMomo:" + path);
		wHtbl.clear();
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				wRec = br.readLine();
				// System.out.println("■wRec:" + wRec);
				if (wRec != null) {
					String[] wArray = wRec.split(",");
					System.out.println("■wArray.length:" + wArray.length);
					if (wArray.length >= 3) {
						wKey = "T" + wArray[0] + "\\B" + wArray[1];
						wVal = wArray[2];
						System.out.println("  ■Key:" + wKey + " ■Val:" + wVal);
						wHtbl.put(wVal, wKey);
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		for (String key : wHtbl.keySet()) {
			System.out.println("◎wEnum:" + key);
		}
		for (String key : wHtbl.keySet()) {
			System.out.println("▲wEnum:" + wHtbl.get(key));
		}
		return true;
	}

	public List sortegkeyList(HashMap hmap) {
		List keys = new ArrayList(hmap.keySet());
		return keys;
	}

	// -------------------------------------------------------------------------
	// テキストファイルの
	// keyColカラム目をキーに
	// valColカラムめを値
	// としたハッシュマップ（辞書）を生成する
	// -------------------------------------------------------------------------
	public static HashList file2HashList(String path, int keyCol, int valCol) {
		String encode = null;
		String delimiter = TAB;
		return file2HashList(path, delimiter, keyCol, valCol, encode);
	}

	public static HashList file2HashList(String path, String delimiter,
			int keyCol, int valCol, String encode) {
		HashList hashList = new HashList();
		if (encode == null)
			encode = DEFAULT_ENCODE;
		File file = new File(path);
		if (file.exists() == false) {
			System.out.println("HashListRes@　FileNotFound>" + path + "<");
			return null;
		}
		try {
			int counter = -1;
			String element;
			InputStreamReader isr = new InputStreamReader(
					new FileInputStream(file), encode);
			BufferedReader br = new BufferedReader(isr);
			while (br.ready()) {
				element = br.readLine();
				if (element != null && (!element.equals(""))) {
					counter++;
					String[] splited = element.split(delimiter);
					if (splited.length >= 2) {
						hashList.put(splited[keyCol], splited[valCol]);
					} else if (splited.length == 1) {
						// １セルしか持たない場合、その位置をキーとして、ゼロ番目が値となる
						// 位置→ZEROから始まる？？
						hashList.put(String.valueOf(counter), splited[0]);
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return hashList;
	}

	// -------------------------------------------------------------------------
	// テキストファイルの先頭のセルをキー、それ以降をlist値としたハッシュマップを生成する
	// -<使用例>-----------------------------------------------------------------
	// HashMap<String, List<String>> map =
	// Dictionary.file2Hmap("c:/TANTOOB.TXT");
	// List<String> keyList = new ArrayList(map.keySet());
	// for (String key : keyList) {
	// List<String> valList = map.get(key);
	// System.out.println("key:" + key + " val:" + valList.get(2));
	// }
	// -------------------------------------------------------------------------
	public static HashMap<String, List<String>> file2HmapL(String path) {
		return file2HmapL(path, TAB, null);
	}

	private static HashMap<String, List<String>> file2HmapL(String path,
			String delimiter, String encode) {
		HashMap<String, List<String>> hmap = new HashMap();
		if (encode == null)
			encode = DEFAULT_ENCODE;
		int counter = -1;
		File file = new File(path);
		if (file.exists() == false) {
			System.out.println("file2Hmap@　FileNotFound>" + path + "<");
			System.exit(99999);
			return hmap;
		}
		try {
			InputStreamReader isr = new InputStreamReader(
					new FileInputStream(file), encode);
			BufferedReader br = new BufferedReader(isr);
			String rec;
			while (br.ready()) {
				rec = br.readLine();
				if (rec != null && (!rec.equals(""))) {
					counter++;
					String[] splited = rec.split(delimiter);
					String key = splited[0].trim();
					List<String> list = new ArrayList<String>();
					for (int i = 1; i < splited.length; i++) {
						list.add(splited[i]);
					}
					hmap.put(key, list);

				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return hmap;
	}

	// -------------------------------------------------------------------------
	// ファイルをハッシュマップにする　ID→クラス（H,M,Lどのクラスか）
	// -------------------------------------------------------------------------
	public static HashMap<String, String> file2HashMap0(String path,
			int valCol) {
		HashMap<String, String> hMap = new HashMap();
		Inf_iClosure reader = new EzReader(path);
		reader.open();
		String[] array = null;
		while (reader.readLine() != null) {
			int wStat = reader.getStat();
			if (wStat >= 0) {
				array = reader.getSplited();
				if (array.length > 0) {
					String id = array[0];// ゼロカラム目をIDとする
					String val = array[valCol];// H、M、Lの区分が入っているカラム
					hMap.put(id, val);
				}
			}
		}
		reader.close();
		return hMap;
	}

	// -------------------------------------------------------------------------
	// Incore from File
	// -------------------------------------------------------------------------
	public static HashMap<String, String> file2HashMap(String path, int keyCol,
			int valCol) {
		return file2HashMap(path, keyCol, valCol, TAB, null);
	}

	public static HashMap<String, String> file2HashMap(String path,
			int valCol) {
		return file2HashMap(path, 0, valCol, TAB, null);
	}

	public static HashMap<String, String> file2HashMap(String path) {
		return file2HashMap(path, 0, 1, TAB, null);
	}

	private static HashMap<String, String> file2HashMap(String path, int keyCol,
			int valCol, String delimiter, String encode) {
		HashMap<String, String> hmap = new HashMap();
		if (encode == null)
			encode = DEFAULT_ENCODE;
		int counter = -1;
		File file = new File(path);
		if (file.exists() == false) {
			System.out.println("file2Hmap@　FileNotFound>" + path + "<");
			System.exit(99999);
			return hmap;
		}
		try {
			InputStreamReader isr = new InputStreamReader(
					new FileInputStream(file), encode);
			BufferedReader br = new BufferedReader(isr);
			String rec;
			while (br.ready()) {
				rec = br.readLine();
				if (rec != null && (!rec.equals(""))) {
					counter++;
					String[] splited = rec.split(delimiter);
					if (splited.length >= 2) {
						if (splited.length > valCol) {
							hmap.put(splited[keyCol].trim(), splited[valCol]);
						} else {
							System.out.println(
									"@kyPkg.converter.Dictionary length:"
											+ splited.length);
							System.out.println(
									"keyCol:" + keyCol + " valCol:" + valCol);
							hmap.put(splited[keyCol].trim(), "DictERROR");
						}
						if (DEBUG) {
							System.out
									.println("file2Hmap@　key:" + splited[keyCol]
											+ " val:" + splited[valCol]);
						}
					} else if (splited.length == 1) {
						// １セルしか持たない場合、その位置をキーとして、ゼロ番目が値となる
						// 位置→ZEROから始まる？？
						hmap.put(String.valueOf(counter), splited[0]);
						if (DEBUG) {
							System.out.println(
									"file2Hmap@　key:" + String.valueOf(counter)
											+ " val:" + splited[0]);
						}
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return hmap;
	}

	// #######################################################################
	public static void testFile2HashMapX() {
		String path = ResControl.ENQ_DIR + "NQ/01_属性・メディア編/2012/alias.txt";
		HashMap<String, String> hMap = HashMapUtil.file2HashMapX(path);
		if (hMap != null) {
			for (String key : hMap.keySet()) {
				String val = hMap.get(key);
				System.out.println("key:" + key + " Val:" + val);
			}
		}
	}

	public static void testFile2HashMapType2() {
		String path = "C:/20121107鈴木さんトヨタ＃２/121107トヨタ3_1.csv";
		HashMap<String, String> hMap = new HashMap();
		file2HashMapType2(hMap, path, 0, ",", "1");
		file2HashMapType2(hMap, path, 1, ",", "2");
		for (String key : hMap.keySet()) {
			String val = hMap.get(key);
			System.out.println("key:" + key + " Val:" + val);
		}
		hashMap2File("c:/zuzuki20121107_A.txt", hMap);
	}

	public static void testHashMap2File20121112() {
		//---------------------------------------------------------------------
		String path = "C:/121112フォルクスワーゲン1.csv";
		HashMap<String, String> hMap = null;
		hMap = new HashMap();
		file2HashMapType2(hMap, path, 0, ",", "1");
		file2HashMapType2(hMap, path, 1, ",", "2");
		for (String key : hMap.keySet()) {
			String val = hMap.get(key);
			System.out.println("key:" + key + " Val:" + val);
		}
		hashMap2File("c:/suzuki2012_1112_A.txt", hMap);
		//---------------------------------------------------------------------
		path = "C:/121112フォルクスワーゲン2.csv";
		hMap = new HashMap();
		file2HashMapType2(hMap, path, 0, ",", "1");
		file2HashMapType2(hMap, path, 1, ",", "2");
		for (String key : hMap.keySet()) {
			String val = hMap.get(key);
			System.out.println("key:" + key + " Val:" + val);
		}
		hashMap2File("c:/suzuki2012_1112_B.txt", hMap);
	}

	// ------------------------------------------------------------------------
	// テキストファイルの先頭のセルをキー、それ以降をlist値としたハッシュマップを生成する
	// 担当者コードから担当者のIDを辞書引きする例
	// ------------------------------------------------------------------------
	public static void testFile2HmapL20141001() {
		HashMap<String, List<String>> map = HashMapUtil
				.file2HmapL("c:/TANTOOB.TXT");
		List<String> keyList = new ArrayList(map.keySet());
		for (String key : keyList) {
			List<String> valList = map.get(key);
			System.out.println(
					"test20141001 key:" + key + " val:" + valList.get(2));
		}
	}

	public static void dumpIt(HashMap<String, String> mmMap) {
		if (mmMap != null) {
			List<String> keyList = new ArrayList(mmMap.keySet());
			Collections.sort(keyList);
			for (String key : keyList) {
				String val = mmMap.get(key);
				System.out.println("key:" + key + " Val:" + val);
			}
		}
	}





}
