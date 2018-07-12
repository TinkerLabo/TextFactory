package kyPkg.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import kyPkg.uFile.FileUtil;
import kyPkg.uFile.ListArrayUtil;

public class Text2Schema {
	private static final String SCHEMA_INI = "/schema.ini";
	private static final String INTEGER = "Integer";
	private static final String DOUBLE = "Double";
	private static final String CHAR = "Char";

	// ---------------------------------------------------------------------
	// ISAMのデータ型は
	// Bit、
	// Byte、
	// Short、
	// Long、
	// Currency、
	// Single、
	// Double、
	// DateTime、
	// Text、
	// Memo
	// ---------------------------------------------------------------------
	// テキストを読み込みschema.iniを生成する
	public static void analyze(String path, boolean forceChar) {
		String delimiter = "\t";
		HashMap<Integer, Integer> hmapLen = new HashMap();// カラムごとの長さを調べる
		HashMap<Integer, String> hmapType = new HashMap();// カラムごとの型を調べる
		List<String> lines = ListArrayUtil.file2List(path, 256);
		for (String line : lines) {
			String[] array = line.split(delimiter);
			for (int i = 0; i < array.length; i++) {
				Integer len = hmapLen.get(i);
				if (len == null || len < array[i].length())
					hmapLen.put(i, array[i].length());// 一番長いものを調べている
				// 全部文字列とする
				if (forceChar) {
					hmapType.put(i, CHAR);
				} else {
					// 一つでもストリングがあれば文字列とみなす
					String type = hmapType.get(i);
					if (type == null || !type.equals(CHAR)) {
						if (kyPkg.uRegex.Regex.isInteger(array[i])) {
							if (type == null || !type.equals(DOUBLE))
								hmapType.put(i, INTEGER);
						} else if (kyPkg.uRegex.Regex.isDouble(array[i])) {
							hmapType.put(i, DOUBLE);
						} else {
							hmapType.put(i, CHAR);
						}
					}
				}
			}
		}
		String name = FileUtil.getName(path);
		String schema = FileUtil.getParent(path) + SCHEMA_INI;
		List<String> keys = new ArrayList(hmapLen.keySet());
		Collections.sort(keys);
		List<String> recs = new ArrayList();
		recs.add("[" + name + "]");
		recs.add("ColNameHeader=False");
		recs.add("Format=TABDelimited");
		recs.add("MaxScanRows=0");
		recs.add("CharacterSet=OEM");
		for (Object key : keys) {
			int seq = Integer.valueOf(key.toString());
			int col = seq + 1;
			if (hmapType.get(key).equals(CHAR)) {
				recs.add("Col" + col + "=\"fld" + seq + "\" "
						+ hmapType.get(key) + " Width " + hmapLen.get(key));
			} else {
				recs.add("Col" + col + "=\"fld" + seq + "\" "
						+ hmapType.get(key));
			}
		}
		ListArrayUtil.list2File(schema, recs);
	}

	public static void main(String[] argv) {
		String path = "c:/@qpr/monitorTest.txt";
		Text2Schema.analyze(path, false);
	}
}
