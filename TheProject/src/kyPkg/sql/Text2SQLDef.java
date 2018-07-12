package kyPkg.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import globals.ResControl;
import kyPkg.uFile.ListArrayUtil;

//毎度毎度ｃｓｖからテーブル定義帯やら、インポート文、とそのフォーマット定義などを作る時間が勿体無い（面倒くさい！）ので一気に片付ける
//データを一旦substrGUIなどでフォーマットしてから、このプログラムを動かしてみた・・・
//テキストファイルから、CreateTableSql、importBatch ,Formatを生成する。
public class Text2SQLDef {
	// private static final String QPR_DB = "QPR.Db";
	 private static final String DBNAME = "QPRDB1";
	private static final String SQLITE = "SQLITE";
	private static final String SQL_SERVER = "SQL_SERVER";
	private static final String USE = "USE ";
	private static final String GO = "GO";
	private static final String FILL = "\t";
	private static final String IMPORT_DIR = "IMPORT/";
	private static final String FORMAT_DIR = "fmt/";
	private static final String CREATE_TABLE = "SQL/CREATE/TABLE/";
	private static final String DROP_TABLE = "SQL/DROP/TABLE/";
	private static final String TRUNC_TABLE = "SQL/TRUNCATE/TABLE/";
	private static final String CREATE_INDEX = "SQL/CREATE/INDEX/";

	private static final String TAB = "\"\\t\"";
	private static final String CRLF = "\"\\r\\n\"";
	private static final String LF = "\n";
	private static String INT = "INT     ";
	private static String FLOAT = "FLOAT   ";
	private static String DATETIME = "DATETIME";
	private static String CHAR = "CHAR    ";
	private static String VARCHAR = "VARCHAR ";
	// AS SQL_SERVER TYPE
	private static final String SQL_CHAR = "CHAR    ";
	private static final String SQL_VARCHAR = "VARCHAR ";
	private static String SEPARATOR = "";
	// ---------------------------------------------------------------------
	// テキストファイルを読み込み
	// CREATE TABLE　SQL
	// CREATE INDEX　SQL
	// DROP TABLE　SQL
	// TRUNCATE TABLE　SQL
	// BCP　バッチ
	// BCP　バッチ用フォーマットファイル
	// を生成する
	// ---------------------------------------------------------------------
	// forceCharを指定するとフィールドをすべてCharとみなして処理を行う
	// ---------------------------------------------------------------------
	private static HashMap<Integer, Boolean> hmapNul = new HashMap();// Null値を許すか
	private static HashMap<Integer, Boolean> hmapFix = new HashMap();// 長さが一定か
	private static HashMap<Integer, Integer> hmapLen = new HashMap();// カラムごとの長さを調べる
	private static HashMap<Integer, String> hmapType = new HashMap();// カラムごとの型を調べる

	private static void analyze(String inPath, boolean forceChar,
			String delimiter) {
		List<String> lines = ListArrayUtil.file2List(inPath, 256);
		for (String line : lines) {
			// System.out.println("line=>" + line);
			String[] array = line.split(delimiter);
			for (int i = 0; i < array.length; i++) {
				String val = array[i];
				Integer len = hmapLen.get(i);
				if (len == null || len < val.length()) {
					hmapLen.put(i, val.length());// 一番長いものを調べている
				}
				if (len != null && len != val.length()) {
					hmapFix.put(i, false);
				}
				// null値が1つ以上含まれているか？
				if (val == null || val.equals(""))
					hmapNul.put(i, true);

				// 全部文字列とする
				if (forceChar) {
					hmapType.put(i, CHAR);
				} else {
					// String type = hmapType.get(i);
					// 半角文字列か？
					if (kyPkg.uRegex.Regex.isHalfWidthString(val)) {
						// 一つでもストリングがあれば文字列とみなす
						if (kyPkg.uRegex.Regex.isInteger(val)) {
							if (kyPkg.uRegex.Regex.isDouble(val)) {
								hmapType.put(i, FLOAT);
							} else {
								hmapType.put(i, INT);
							}
						} else {
							if (kyPkg.uRegex.Regex.isTimeStamp(val)) {
								hmapType.put(i, DATETIME);
							} else {
								hmapType.put(i, CHAR);
							}
						}
					} else {
						// "全角";
						if (kyPkg.uRegex.Regex.isFullWidthString(val)) {
							hmapType.put(i, VARCHAR);
						} else {
							// ?空値とか？
						}
					}

				}
			}
		}

	}

	public static void analyzeAndWrite(String base_dir, String inPath) {
		scaffold(DBNAME, base_dir, inPath, false, SQL_SERVER);
	}

	public static void scaffold(String dbName, String baseDir,
			String inPath, boolean forceChar, String mode) {

		mode = mode.trim().toUpperCase();
		if (mode.equals(""))
			mode = SQL_SERVER;
		if (mode.equals(SQLITE)) {
			INT = "int     ";
			FLOAT = "real    ";
			DATETIME = "text    ";
			CHAR = "text    ";
			VARCHAR = "text    ";
			// SQLiteで利用可能なデータ型
			// NULL NULL値
			// INTEGER 符号付整数。1, 2, 3, 4, 6, or 8 バイトで格納
			// REAL 浮動小数点数。8バイトで格納
			// TEXT テキスト。UTF-8, UTF-16BE or UTF-16-LEのいずれかで格納
			// BLOB Binary Large OBject。入力データをそのまま格納
			// テーブルを定義する時にカラム毎にデータ型を指定しなくても構いません。その為、同じカラムに異なったデータ型の値が格納されることもあります。
		}
		String delimiter = "\t";

		analyze(inPath, forceChar, delimiter);

		String name = kyPkg.uFile.FileUtil.getFirstName2(inPath);
		// String tName = name.toUpperCase();
		String tName = name;

		// << create Table sql file >>
		List<String> listCreate = new ArrayList();
		List<String> listDrop = new ArrayList();
		List<String> listTrunc = new ArrayList();
		List<String> listIndex = new ArrayList();
		List<String> listFormat = new ArrayList();
		String line1 = "";
		String line2 = "";
		// --------------------------------------------------------------------
		if (mode.equals(SQL_SERVER))
			listTrunc.add(USE + dbName + LF + GO);
		listTrunc.add("TRUNCATE TABLE " + tName + ";");
		if (mode.equals(SQL_SERVER))
			listTrunc.add(GO);
		// --------------------------------------------------------------------
		if (mode.equals(SQL_SERVER))
			listDrop.add(USE + dbName + LF + GO);
		listDrop.add("DROP TABLE " + tName + ";");
		if (mode.equals(SQL_SERVER))
			listDrop.add(GO);
		// --------------------------------------------------------------------
		listCreate.add("DROP TABLE if exists " + tName + ";");
		if (mode.equals(SQL_SERVER))
			listCreate.add(GO);
		if (mode.equals(SQL_SERVER))
			listCreate.add(USE + dbName + LF + GO);
		// drop table if exists current;
		listCreate.add("CREATE TABLE " + tName + "");
		listCreate.add("(");
		// << format file >>
		List keys = new ArrayList(hmapLen.keySet());
		Collections.sort(keys);
		listFormat.add("6.0.  ");
		listFormat.add(keys.size() + "      ");
		for (Object key : keys) {
			int seq = Integer.valueOf(key.toString());
			if (seq == 0) {
				if (mode.equals(SQL_SERVER)) {
					listIndex.add(USE + dbName + LF + GO);
				}
				listIndex.add("CREATE INDEX X_FLD" + seq + " ON " + tName
						+ " (FLD" + seq + ") ;");
				if (mode.equals(SQL_SERVER))
					listIndex.add(GO);
			}
			String type = hmapType.get(key);
			int len = hmapLen.get(key);
			if (type == null) {
				type = CHAR;
				len = 1;
			}
			// 数値で固定長ならcharと判定・・・どうなんだろうね
			if (type.equals(INT) && hmapFix.get(key) == null)
				type = CHAR;
			if (type.equals(SQL_CHAR) || type.equals(SQL_VARCHAR)) {
				line1 = "FLD" + seq + " " + type + " (" + len + ")";
			} else {
				line1 = "FLD" + seq + " " + type;
			}
			Boolean nulEx = hmapNul.get(key);
			if (nulEx != null && nulEx == true) {
				line1 = line1 + " NULL";
			}

			// 最終行なら改行する
			if ((seq + 1) == keys.size()) {
				SEPARATOR = CRLF;
			} else {
				SEPARATOR = TAB;
				line1 = line1 + " ,";
			}
			listCreate.add(line1);
			line2 = "" + (seq + 1) + FILL + "SQL" + type.toUpperCase() + FILL
					+ "0" + FILL + len + FILL + SEPARATOR + FILL + (seq + 1)
					+ FILL + "FLD" + seq + FILL + "";
			listFormat.add(line2);
		}
		// for (String element : lines1) {
		// System.out.println("=>" + element);
		// }
		// for (String element : lines2) {
		// System.out.println("=>" + element);
		// }
		listCreate.add(");");
		if (mode.equals(SQL_SERVER))
			listCreate.add(GO);

		String createPath = baseDir + CREATE_TABLE + tName + ".sql";
		String dropPath = baseDir + DROP_TABLE + tName + ".sql";
		String truncPath = baseDir + TRUNC_TABLE + tName + ".sql";
		String indexPath = baseDir + CREATE_INDEX + tName + ".sql";

		if (mode.equals(SQLITE)) {
			// 入力ファイルのデリミタとｄｂ名が　間に合わせなので・・・あとで要修正かな
			String importSQLPath = baseDir + IMPORT_DIR + tName + ".sql";
			List<String> listImportSql = new ArrayList();
			listImportSql.add(".separator \"" + delimiter + "\"");
			listImportSql.add(".import \"" + inPath + "\" " + tName + " ");
			ListArrayUtil.list2File(importSQLPath, listImportSql);

			String importBatPath = baseDir + IMPORT_DIR + tName + ".bat";
			String command1 = "sqlite3 \"" + baseDir + dbName + "\" \".read '"
					+ createPath + "'\"";
			String command2 = "sqlite3 \"" + baseDir + dbName + "\" \".read '"
					+ importSQLPath + "'\"";
			System.out.println("=>" + command1);
			System.out.println("=>" + command2);
			List<String> listBcpBat = new ArrayList();
			listBcpBat.add(command1);
			listBcpBat.add(command2);
			ListArrayUtil.list2File(importBatPath, listBcpBat);

		}
		if (mode.equals(SQL_SERVER)) {
			String formatPath = baseDir + FORMAT_DIR + tName + ".FMT";
			ListArrayUtil.list2File(formatPath, listFormat);
			String bcpBatPath = baseDir + IMPORT_DIR + tName + ".bat";
			String lineBcp = "bcp QPRDB1.." + tName + " in " + inPath
					+ " /m 512 /f " + formatPath + " /e " + baseDir + "error/"
					+ tName + ".err " + " /o " + baseDir + "log/" + tName
					+ ".log /c /t " + TAB + " -Uqpr /P /S" + ServerConnecter.CURRENT_SERVER;
			System.out.println("=>" + lineBcp);
			List<String> listBcpBat = new ArrayList();
			listBcpBat.add(lineBcp);
			ListArrayUtil.list2File(bcpBatPath, listBcpBat);
		}

		ListArrayUtil.list2File(createPath, listCreate);
		ListArrayUtil.list2File(dropPath, listDrop);
		ListArrayUtil.list2File(truncPath, listTrunc);
		ListArrayUtil.list2File(indexPath, listIndex);

	}

	public static void main(String[] argv) {
		test20130416();
	}

	// テキストファイルを解析して・・・一連のｓｑｌスクリプト等を作成
	public static void test20130416() {
		String inPath = ResControl.D_DAT+"MonitorCheck.txt";
		Text2SQLDef.analyzeAndWrite(ResControl.D_QPR, inPath);
	}
}
