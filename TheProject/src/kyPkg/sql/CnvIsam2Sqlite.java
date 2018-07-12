package kyPkg.sql;

import static kyPkg.sql.ISAM.SCHEMA_INI;
import static kyPkg.sql.ISAM.TAB_DELIMITED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import kyPkg.filter.EzReader;
import kyPkg.filter.Inf_iClosure;
import kyPkg.task.Abs_BaseTask;

// 2009-09-01 Schema.iniを読みｓｑｌ（sqlite用）に変換する
// (sqlite用)スキーマ定義と同じテーブルをcreateするｓｑｌ文を生成
public class CnvIsam2Sqlite extends Abs_BaseTask {

	private static final String COMMA = ",";
	private static final String TAB = "\t";
	private String schemaPath;
	private String createPath;
	private String importPath;

	// -------------------------------------------------------------------------
	// コンストラクタ  ","
	// -------------------------------------------------------------------------
	public CnvIsam2Sqlite(String dir, String createPath, String importPath,
			String schemaPath) {
		kyPkg.uFile.FileUtil.makedir(dir);
		this.schemaPath = dir + schemaPath;
		this.createPath = dir + createPath;
		this.importPath = dir + importPath;
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("Schema2Sql開始");
		gen(schemaPath);
	}

	// ---------------------------------------------------------------------
	// スクリプトを出力する
	// ---------------------------------------------------------------------
	private void gen(String schemaPath) {
		HashMap<String, List<String>> schemaMap = new HashMap();
		//		HashMap<String, List<String>> schemaMap = schema2Hmap(resultMap,schemaPath);
		HashMap<String, String> delimiters = schema2Map(schemaMap, schemaPath);
		if (schemaMap.size() <= 0)
			return;
		List<String> tabeleNames = new ArrayList(schemaMap.keySet());
		//		Set set = schemaMap.entrySet();
		//		List<String> tableList = new ArrayList();
		//		for (Iterator iter = set.iterator(); iter.hasNext();) {
		//			Map.Entry<String, ArrayList> element = (Map.Entry) iter.next();
		//			tableList.add(element.getKey());
		//		}
		Collections.sort(tabeleNames);
		//		for (String key : tabeleNames) {
		//			System.out.println("#debug210150811# key:" + key);
		//		}
		JDBC_Sqlite.writeCreateTableSQL(createPath, tabeleNames, schemaMap);
		JDBC_Sqlite.writeImportFromTXT(importPath, tabeleNames, delimiters);
	}

	// ---------------------------------------------------------------------
	// incore (schema.ini => hashMap)
	// <tableName,colList>
	// ---------------------------------------------------------------------
	private static HashMap<String, String> schema2Map(
			HashMap<String, List<String>> resultMap, String schemaPath) {
		HashMap<String, String> separators = new HashMap();
		resultMap.clear();
		Inf_iClosure reader = new EzReader(schemaPath);
		String[] array = null;
		List<String> colList = null;
		String tableName = "";
		String colName;
		String colType;
		reader.setDelimiter("=");
		reader.open();
		while (reader.readLine() != null) {
			int wStat = reader.getStat();
			if (wStat >= 0) {
				array = reader.getSplited();
				array[0] = array[0].trim();
				if (array[0].startsWith("[")) {
					tableName = array[0].substring(1, (array[0].length() - 5))
							.trim();
					// tableName=tableName + "#TXT";
					// ISAM使用時と同じSQLを利用したいので変更2009/09/08=>NGテーブル名に＃を使えない
					colList = new ArrayList();
					resultMap.put(tableName, colList);
				}
				//タブ区切りorカンマ区切り
				if (array.length >= 2) {
					if (array[0].startsWith("ColNameHeader")) {
					} else if (array[0].startsWith("Format")) {
						if (array[1].trim().equals(TAB_DELIMITED)) {
							separators.put(tableName, TAB);
						} else {
							separators.put(tableName, COMMA);
						}
					} else if (array[0].startsWith("Col")) {
						String[] chunk1 = array[1].split("\"");
						if (chunk1.length >= 3) {
							colName = (chunk1[1]).trim();
							colType = (chunk1[2]).trim();
							if (colType.startsWith("Char")) {
								colType = " text not null";
							} else if (colType.startsWith("Integer")) {
								colType = " Integer not null";
							}
							if (colList != null) {
								colList.add((colName + colType));
							}
						}
					}
				}
			}
		}
		reader.close();
		return separators;
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		testCnvIsam2Sqlite();
	}

	public static void testCnvIsam2Sqlite() {
		System.out.println("## Test Start ##");
		String dir = "C:/test/";
		new CnvIsam2Sqlite(dir, "create.sql", "import.sql", SCHEMA_INI)
				.execute();
		System.out.println("## Test End ##");
	}

	public static void testCreateDBandImportWithScheme() {
		String dir = "C:/test/";
		String dbName = "QPR";
		JDBC_Sqlite dbObj = new JDBC_Sqlite(dbName, dir);
		dbObj.importFromIsam();
	}
	public static void testMonitorCheck_old() {
		String dir = "C:/test/";
		testCreateDBandImportWithScheme() ;
		String inPath = "C:/test/MonitorCheck.txt";
		Text2SQLDef.scaffold("QPR.Db", dir, inPath, false, "SQLITE");
	}
}
