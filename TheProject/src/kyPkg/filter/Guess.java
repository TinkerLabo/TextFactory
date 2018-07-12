package kyPkg.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import kyPkg.uCodecs.CharConv;
import kyPkg.uFile.File2Matrix;
import kyPkg.uFile.FileUtil;

public abstract class Guess {

	public abstract void analyzeIt(String inPath, boolean headOpt);

	// ------------------------------------------------------------------------
	// SQLのデータ型
	// ------------------------------------------------------------------------
	public static final String BINARY = "binary";
	public static final String VARBINARY = "varbinary";
	public static final String CHAR = "char";
	public static final String VARCHAR = "varchar";
	public static final String DATETIME = "datetime";
	public static final String DECIMAL = "decimal";
	public static final String NUMERIC = "numeric";
	public static final String FLOAT = "float";
	public static final String REAL = "real";
	public static final String INT = "int";
	public static final String SMALLINT = "smallint";
	public static final String TINYINT = "tinyint";
	public static final String MONEY = "money";
	public static final String BIT = "bit";
	public static final String TIMESTAMP = "timestamp";
	public static final String TEXT = "text";
	public static final String IMAGE = "image";
	private static final String NCHAR = "nchar";
	private static final String NVARCHAR = "nvarchar";
	private static final String NTEXT = "ntext";
	private static final String BIGINT = "bigint";

	// public static final String BINARY = "BINARY";
	// public static final String VARBINARY = "VARBINARY";
	// public static final String CHAR = "CHAR";
	// public static final String VARCHAR = "VARCHAR";
	// public static final String DATETIME = "DATETIME";
	// public static final String DECIMAL = "DECIMAL";
	// public static final String NUMERIC = "NUMERIC";
	// public static final String FLOAT = "FLOAT";
	// public static final String REAL = "REAL";
	// public static final String INT = "INT";
	// public static final String SMALLINT = "SMALLINT";
	// public static final String TINYINT = "TINYINT";
	// public static final String MONEY = "MONEY";
	// public static final String BIT = "BIT";
	// public static final String TIMESTAMP = "TIMESTAMP";
	// public static final String TEXT = "TEXT";
	// public static final String IMAGE = "IMAGE";
	// private static final String NCHAR = "NCHAR";
	// private static final String NVARCHAR = "NVARCHAR";
	// private static final String NTEXT = "NTEXT";
	// private static final String BIGINT = "BIGINT";
	// ------------------------------------------------------------------------
	// host file data type
	// ------------------------------------------------------------------------
	private static final String SQLBIT = "SQLBIT";
	private static final String SQLMONEY = "SQLMONEY";
	private static final String SQLTINYINT = "SQLTINYINT";
	private static final String SQLSMALLINT = "SQLSMALLINT";
	private static final String SQLBIGINT = "SQLBIGINT";
	private static final String SQLINT = "SQLINT";
	private static final String SQLFLT4 = "SQLFLT4";
	private static final String SQLFLT8 = "SQLFLT8";
	private static final String SQLNUMERIC = "SQLNUMERIC";
	private static final String SQLDECIMAL = "SQLDECIMAL";
	private static final String SQLDATETIME = "SQLDATETIME";
	private static final String SQLBINARY = "SQLBINARY";
	private static final String SQLNCHAR = "SQLNCHAR";
	private static final String SQLCHAR = "SQLCHAR";
	public static final String NOT = "NOT";
	public static final String NULL = "NULL";

	protected static final String PREFIX = "Fld_";

	protected String delimiter;
	protected String path;
	protected String parentDir;
	protected String preExt;
	protected String tableName;

	protected List<String> names;// フィールド名
	protected List<String> types;// 型名
	protected List<String> typeFigures;// 型名
	protected List<String> figures;// 桁数

	protected List<String> create_Table_sql;
	protected List<String> create_Index_sql;
	protected List<String> drop_Table_sql;
	protected List<String> drop_Index_sql;
	protected List<String> truncate_Table_sql;


	
	protected List<String> r_List;// read
	protected List<String> u_List;// update
	protected List<String> d_List;// delete
	protected List<String> i_List;
	protected List<String> bcp_param;// bcp parameter
	protected List<String> bcp_bat;// bcp parameter
	protected List<String> sampleCodeList;
	protected Set<String> nSet;// (n)形式で桁数オプションがある型かどうかを判定する

	public static HashMap<String, String> getStorageType() {
		// ファイルストレージ型ホストファイルデータ型
		HashMap<String, String> map = new HashMap();
		map.put(CHAR, SQLCHAR);
		map.put(VARCHAR, SQLCHAR);
		map.put(NCHAR, SQLNCHAR);
		map.put(NVARCHAR, SQLNCHAR);
		map.put(TEXT, SQLCHAR);
		map.put(NTEXT, SQLNCHAR);
		map.put(BINARY, SQLBINARY);
		map.put(VARBINARY, SQLBINARY);
		map.put(IMAGE, SQLBINARY);
		map.put(DATETIME, SQLDATETIME);
		map.put(DECIMAL, SQLDECIMAL);
		map.put(NUMERIC, SQLNUMERIC);
		map.put(FLOAT, SQLFLT8);
		map.put(REAL, SQLFLT4);
		map.put(INT, SQLINT);
		map.put(BIGINT, SQLBIGINT);
		map.put(SMALLINT, SQLSMALLINT);
		map.put(TINYINT, SQLTINYINT);
		map.put(MONEY, SQLMONEY);
		map.put(BIT, SQLBIT);
		map.put(TIMESTAMP, SQLBINARY);
		return map;
	}

	// ---------------------------------------------------------------------
	// アクセッサ
	// ---------------------------------------------------------------------
	public List<String> getNames() {
		return names;
	}

	public List<String> getTypes() {
		return types;
	}

	public List<String> getFigures() {
		return figures;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public String getPath() {
		return path;
	}

	// ---------------------------------------------------------------------
	// コンストラクタ
	// ---------------------------------------------------------------------
	public Guess() {
		super();
		names = new ArrayList();
		types = new ArrayList();
		figures = new ArrayList();
		typeFigures = new ArrayList();
		// 桁数(n)を付ける型
		nSet = new HashSet();
		nSet.add(BINARY);
		nSet.add(VARBINARY);
		nSet.add(CHAR);
		nSet.add(VARCHAR);
	}

	// ------------------------------------------------------------------------
	// マトリックスの先頭がヘッダー名称かどうか判定する
	// ------------------------------------------------------------------------
	protected boolean isHeaderExist(Vector<Vector> matrix) {
		if (matrix == null || matrix.size() <= 0)
			return false;
		List rows = matrix.get(0);
		int cnt = 0;
		for (Object object : rows) {
			String str = object.toString();
			// 半角英字で始まる半角英数文字列ならフィールド名を認識
			if (kyPkg.uRegex.Regex.isFieldName(str)) {
				cnt++;
			}
		}
		if (rows.size() == cnt)
			return true;
		return false;
	}

	protected Vector<Vector> getMatrix(String path, int skip, int limit) {
		path = FileUtil.normarizeIt(path);
		this.path = path;
		this.parentDir = FileUtil.getParent2(path, true);
		this.preExt = FileUtil.getPreExt(path);
		this.tableName = FileUtil.getFirstName2(path);
		this.tableName = tableName.toUpperCase();

		// Vector<Vector> matrix = MatrixUtil.file2VectorMatrix(path,limit);

		File2Matrix insF2M = new File2Matrix(path);
		insF2M.setLimit(limit);
		insF2M.setSkip(skip);
		insF2M.execute();
		this.delimiter = insF2M.getDelimiter();
		Vector<Vector> matrix = insF2M.getMatrix();
		if (matrix == null || matrix.size() == 0) {
			System.out.println("ERROR?! Empty Data?:" + path);
			return null;
		}
		// File2Matrix.debugTheMatrix(matrix);// for Debug
		return matrix;
	}

	protected String list2String(List<String> list, String remove) {
		StringBuffer buf = new StringBuffer();
		for (String element : list) {
			if (remove != null)
				element = element.replace(remove, "");
			buf.append(element);
		}
		return buf.toString();
	}

	protected static int getMaxVal(String val) {
		int rtn = -1;
		if (kyPkg.uRegex.Regex.isAlphaNumeric(val)) {
			try {
				rtn = Integer.valueOf(val);
			} catch (Exception e) {
				rtn = -1;
			}
		} else {
			if (kyPkg.uRegex.Regex.isMuliAns(val)) {
				val = val.replaceAll("\"", "");
				String[] array = val.split(",");
				for (int i = 0; i < array.length; i++) {
					int iVal = Integer.valueOf(array[i]);
					if (rtn < iVal)
						rtn = iVal;
				}
			}
		}
		return rtn;
	}

	protected static void debug(List<String> list) {
		boolean debug = true;
		if (debug == false)
			return;
		for (String element : list) {
			System.out.println("debug@Guess>" + element);
		}
	}

	// ---------------------------------------------------------------------
	// Insert into Table
	// ---------------------------------------------------------------------
	protected List<String> insert_sql(List<String> fields, String name) {
		List<String> list = new ArrayList();
		StringBuffer buf = new StringBuffer();
		String comma = ",";
		for (int i = 0; i < fields.size(); i++) {
			if (i == (fields.size() - 1))
				comma = "";
			buf.append(fields.get(i) + comma);
		}
		list.add("INSERT INTO " + name + ";");
		debug(list);
		return list;
	}

	// ---------------------------------------------------------------------
	// Create Table
	// ---------------------------------------------------------------------
	protected List<String> create_Table_sql(List<String> fields,
			List<String> types, String name) {
		List<String> list = new ArrayList();
		list.add("CREATE TABLE " + name + "(");
		String comma = ",";
		for (int i = 0; i < fields.size(); i++) {
			if (i == (fields.size() - 1))
				comma = "";
			list.add("    " + fields.get(i) + "  " + types.get(i) + comma);
		}
		list.add(");");
		debug(list);
		return list;
	}

	// ---------------------------------------------------------------------
	// Drop Table
	// ---------------------------------------------------------------------
	protected List<String> drop_Table_sql(List<String> fields,
			List<String> types, String name) {
		List<String> list = new ArrayList();
		list.add("DROP TABLE " + name);
		debug(list);
		return list;
	}

	// ---------------------------------------------------------------------
	// Truncate Table
	// ---------------------------------------------------------------------
	protected List<String> truncate_Table_sql(List<String> fields,
			List<String> types, String name) {
		List<String> list = new ArrayList();
		list.add("TRUNCATE  TABLE " + name);
		debug(list);
		return list;
	}

	// ---------------------------------------------------------------------
	// Drop Index
	// ---------------------------------------------------------------------
	protected List<String> drop_Index_sql(List<String> fields,
			List<String> types, String name) {
		List<String> list = new ArrayList();
		String field = fields.get(0);
		list.add("DROP INDEX  " + name + ".X_" + field);
		debug(list);
		return list;
	}

	// ---------------------------------------------------------------------
	// Create Index
	// ---------------------------------------------------------------------
	protected List<String> create_Index_sql(List<String> fields,
			List<String> types, String name) {
		List<String> list = new ArrayList();
		String field = fields.get(0);
		list.add("CREATE UNIQUE INDEX X_" + field + " ON " + name + " ("
				+ field + ")  ");
		debug(list);
		return list;
	}

	// ---------------------------------------------------------------------
	// Read Table
	// ---------------------------------------------------------------------
	protected List<String> select_sql(List<String> fields, String name) {
		List<String> list = new ArrayList();
		StringBuffer buf = new StringBuffer();
		String comma = ",";
		for (int i = 0; i < fields.size(); i++) {
			if (i == (fields.size() - 1))
				comma = "";
			buf.append(fields.get(i) + comma);
		}
		list.add("SELECT " + buf.toString() + " FROM " + name + "#TXT;");
		debug(list);
		return list;
	}

	// ---------------------------------------------------------------------
	// Update Table
	// ---------------------------------------------------------------------
	protected List<String> update_sql(List<String> fields, String name) {
		List<String> list = new ArrayList();
		StringBuffer buf = new StringBuffer();
		String comma = ",";
		for (int i = 0; i < fields.size(); i++) {
			if (i == (fields.size() - 1))
				comma = "";
			buf.append(fields.get(i) + " = '' " + comma);
		}
		list.add("UPDATE " + name + " SET " + buf.toString()
				+ " WHERE id IS NOT NULL " + ";");
		debug(list);
		return list;
	}

	// ---------------------------------------------------------------------
	// Delete Table
	// ---------------------------------------------------------------------
	protected List<String> delete_sql(List<String> fields, String name) {
		List<String> list = new ArrayList();
		StringBuffer buf = new StringBuffer();
		String relation = " OR ";
		for (int i = 0; i < fields.size(); i++) {
			if (i == (fields.size() - 1))
				relation = "";
			buf.append(fields.get(i) + " <> '' " + relation);
		}
		list.add("DELETE FROM " + name + " WHERE (" + buf.toString() + ");");
		debug(list);
		return list;
	}

	// ---------------------------------------------------------------------
	// BCP batch （SQL_SERVER）
	// ---------------------------------------------------------------------
	public List<String> bcp_batch(String serverName, String dbName, String dir,
			String user, String pass, String name, String delimiter) {
		String delimStr = getDelimString(delimiter);
		String inPath = dir + "dat/" + name + ".dat";
		String fmtPath = dir + "fmt/" + name + ".fmt";
		String errPath = dir + "error/" + name + ".err";
		String logPath = dir + "log/" + name + ".log";
		List<String> list = new ArrayList();
		list.add("bcp " + dbName + ".." + name + " in " + inPath
				+ " /m 512 /f " + fmtPath + " /e " + errPath + " /o " + logPath
				+ " /c /t \"" + delimStr + "\" -U" + user + " /P" + pass
				+ " /S" + serverName + "");
		debug(list);
		return list;
	}

	// ---------------------------------------------------------------------
	// 区切り文字のエスケープ文字列を返す
	// ---------------------------------------------------------------------
	public static String getDelimString(String delimiter) {
		if (delimiter.equals("\t"))
			delimiter = "\\t";
		return delimiter;
	}

	// ---------------------------------------------------------------------
	// BCP Parameter （SQL_SERVER）
	// ---------------------------------------------------------------------
	public static List<String> bcpParam(List<String> fieldNames,
			List<String> fieldTypes, List<String> figures, String delimiter) {
		String END = "\\r\\n";
		String element = "";
		HashMap<String, String> map = Guess.getStorageType();
		List<String> list = new ArrayList();
		list.add("6.0");
		list.add("" + fieldNames.size());
		for (int index = 0; index < fieldNames.size(); index++) {
			String type = map.get(fieldTypes.get(index));
			String figure = figures.get(index);
			String fieldName = fieldNames.get(index);
			int seq = (index + 1);
			String delimStr = getDelimString(delimiter);
			if (seq == fieldNames.size())
				delimStr = END;// 最後
			element = CharConv.cnvFixLength(seq, 10)
					+ CharConv.cnvFixLength(type, 10)
					+ CharConv.cnvFixLength("0", 10)
					+ CharConv.cnvFixLength(figure, 10)
					+ CharConv.cnvFixLength("\""+delimStr+"\"", 10)
					+ CharConv.cnvFixLength(seq, 10)
					+ CharConv.cnvFixLength(fieldName, 10);
			list.add(element);
		}
		debug(list);
		return list;
	}

	// ---------------------------------------------------------------------
	// SampleCode(of ISAM)
	// ---------------------------------------------------------------------
	protected List<String> sampleCode(String sql) {
		String dbDir = parentDir;// ファイルの親ディレクトリ
		List<String> list = new ArrayList();
		list.add("private static void sample() {\n");
		list.add("        String dbDir = \"" + dbDir + "\";\n");
		list.add("        String sql = \"" + sql + "\";\n");
		list.add("        String resultPath = dbDir +\"result.txt\";\n");
		list.add("        kyPkg.task.Inf_BaseTask task = qpr.logic.CommonMethods.queryIsam2File(resultPath,sql, dbDir);\n");
		list.add("        new kyPkg.task.TaskWatcherNoGUI(task).execute();\n");
		list.add("}\n");
		debug(list);
		return list;
	}

	public String getI_Sql() {
		return list2String(i_List, "\n");
	}

	public String getC_Sql() {
		return list2String(create_Table_sql, "\n");
	}

	public String getR_Sql() {
		return list2String(r_List, "\n");
	}

	public String getU_Sql() {
		return list2String(u_List, "\n");
	}

	public String getD_Sql() {
		return list2String(d_List, "\n");
	}

	public String getSqmpleCode() {
		return list2String(sampleCodeList, null);
	}
}