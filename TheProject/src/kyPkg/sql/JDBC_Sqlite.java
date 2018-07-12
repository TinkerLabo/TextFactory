package kyPkg.sql;

import static kyPkg.sql.ISAM.SCHEMA_INI;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kyPkg.filter.EzWriter;
import kyPkg.filter.Inf_oClosure;
import kyPkg.uFile.FileUtil;
import kyPkg.util.Shell;

// ��sqlite�͕����R�[�h�̐���������
//�G�N�X�|�[�g����ۂ̉��s�R�[�h��LF�̂݁E�E�ECR�͂��Ȃ������Ȃ����H�I
//-----------------------------------------------------------------------------
//SQLite�W���̃C���|�[�g�E�G�N�X�|�[�g�͈ȉ��̂悤�ɍs���܂��B
//# �W���ňȉ��̂悤�ɃC���|�[�g�E�G�N�X�|�[�g�ł��邪�A�Z�p���[�^����肭�p�[�X����Ȃ�
//.mode csv table_name
//# �G�N�X�|�[�g
//SELECT * FROM table_name
//# �C���|�[�g
//.import sample.csv table_name
//-----------------------------------------------------------------------------
//�C���|�[�g����e�L�X�g�͂t�s�e�����ʖڂȂ񂾂낤���Q�H
//% sqlite3 qpr.db �� �R���\�[���Ŏ��s����ꍇ
//Command Line Shell For SQLite 
//http://www.sqlite.org/sqlite.html
//.import FILE TABLE     Import data from FILE into TABLE
//.separator STRING      Change separator used by output mode and .import
//.mode MODE ?TABLE?     Set output mode where MODE is one of:
//csv      Comma-separated values
//column   Left-aligned columns.  (See .width)
//html     HTML <table> code
//insert   SQL insert statements for TABLE
//line     One value per line
//list     Values delimited by .separator string
//tabs     Tab-separated values
//tcl      TCL list elements
//-----------------------------------------------------------------------------
public class JDBC_Sqlite extends JDBC_TEXDB {
	//	-----------------------------------------------------------------------
	//	SQLITE �́@�^
	//	-----------------------------------------------------------------------
	private static final String TEXT = "text";
	//	private static final String NONE = "none";
	//	private static final String REAL = "real";
	private static final String NUMERIC = "numeric";
	private static final String INTEGER = "Integer";
	//	-----------------------------------------------------------------------
	public static final String IMPORT_SQL = "import.sql";
	public static final String CREATE_SQL = "create.sql";
	private static final String LF = " \n";
	private static final String CSV = "csv";
	private static final String TABS = "tabs";

	//	-----------------------------------------------------------------------
	//	Constructor
	//	-----------------------------------------------------------------------
	public JDBC_Sqlite(String dbName, String dbDir) {
		super();
		this.dbDir = kyPkg.uFile.FileUtil.mkdir(dbDir);
		this.dbPath = getDbDir() + dbName + ".Db";//ex QPR.Db
		this.dataDir = FileUtil.mkdir(getDbDir() + "data");
		new File(dataDir).mkdirs();
		openDB();
	}

	@Override
	public String getPath(String path) {
		String fileName = kyPkg.uFile.FileUtil.getFileName(path);
		return dataDir + fileName;
	}

	@Override
	public TContainer importTable(String table, String srcPath) {
		// ���Y�e�[�u�������݂���Ȃ�����Ă���
		if (isExist(table))
			dropTable(table);
		// -------------------------------------------------------------
		// �t�@�C������e�L�X�g�e�[�u���𐶐�����
		// -------------------------------------------------------------
		// String path = getPath(srcPath);
		// TContainer container = new TContainer(table, path, 0);
		TContainer container = new TContainer(table, srcPath, 0);

		String iDefs = container.getfDefs();
		if (createTable(table, iDefs)) {
			// ---------------------------------------------------------
			// �t�@�C�����e�[�u���ɃA�T�C��
			// ---------------------------------------------------------
			assignIt(table, srcPath);
			return container;
		} else {
			log.info("@JDBC_Sqlite.importTable #�t�@�C�����e�[�u���ɃA�T�C���@NG");
			return null;
		}
	}

	protected void openDB() {
		// System.out.println("at openDB()");
		if (log != null)
			log.info("sqlite�@openDB");
		String jUrl = "jdbc:sqlite:" + dbPath;
		String user = "anonymous";
		String pass = "";
		// System.out.println("# SqliteCtrl.openDB jUrl:" + jUrl);
		if (super.getConnection(jUrl, user, pass) == null) {
			if (log != null)
				log.fatal("hsqlDb<Text> ���������s");
		}
	}

	// -------------------------------------------------------------------------
	// sqlite�Ǝ�(posgre�ɂ�����P�h)
	// -------------------------------------------------------------------------
	public void vacuum() {
		execute("VACUUM");// VACUUM
	}

	// Execute SQL in FILENAME
	@Override
	public void executeSQL(String sql) {
		execute(sql);// .read xxx.sql
	}

	@Override
	public boolean assignIt(String table, String path, String delimiter,
			String encoding) {
		// XXX�@encoding���w�肳�ꂽ�ꍇ�@�����utf-8�̃t�@�C���Ɉ�U�ϊ����Ȃ���΂Ȃ�Ȃ��Ǝv���@
		// sqlite��utf-8�����Ή����ĂȂ������Ȃ̂ŁE�E�E
		File file = new File(path);
		String absPath = file.getAbsolutePath();
		if (log != null)
			log.info("�@�@�e�[�u��" + table + "����ɂ���");
		truncate(table);
		String dbDir = getDbDir();
		String dbPath = getDbPath();
		String scriptPath = dbDir + IMPORT_SQL;
		if (log != null)
			log.info("�@�@�X�N���v�g���쐬���o��:" + scriptPath);
		writeImportScript(scriptPath, table, absPath, "\t");
		exeScript(dbPath, scriptPath);
		return true;
	}

	@Override
	public void exportIt(String outPath, String delimiter, String table,
			String key, String fields, String order, String where) {
		if (log != null)
			log.info("�@�@�e�[�u���i" + table + "�j���t�@�C���ɏo��=>" + outPath);
		if (!isExist(table)) {
			if (log != null)
				log.info("�@�@#ERROR!!�@Table not Exist!!:" + table);
			return;
		}
		String dbDir = getDbDir();
		String dbPath = getDbPath();
		// �X�N���v�g���o��
		String firstName = FileUtil.getFirstName(outPath);
		String scriptPath = dbDir + "export_" + firstName + ".sql";
		writeExportScript(scriptPath, outPath, delimiter, table, key, fields,
				where, order);
		// �X�N���v�g�����s
		exeScript(dbPath, scriptPath);
	}

	// ----------------------------------------------------------------------
	// sqlite�@DB ���쐬���ăf�[�^�C���|�[�g���s��
	// ���ӁF�h�r�`�l���璼�ڃC���|�[�g���Ă���̂ł͂Ȃ��X�L�[�}�𗘗p���Ă��邾��
	// ----------------------------------------------------------------------
	public void importFromIsam() {
		// Schema�D���������iSQL���𐶐��j create.sql import.sql
		String separator = ",";
		String dir = getDbDir();
		new CnvIsam2Sqlite(dir, CREATE_SQL, IMPORT_SQL, SCHEMA_INI).execute();
		//DB�ɃC���|�[�g���Ă���
		exeCreateAndImport();
		vacuum();
	}

	public void exeCreateAndImport() {
		String scriptPath1 = getDbDir() + CREATE_SQL;
		String scriptPath2 = getDbDir() + IMPORT_SQL;
		exeScript(dbPath, scriptPath1);
		exeScript(dbPath, scriptPath2);
	}

	// sqlite3�ɃX�N���v�g�����s������
	private String exeScript(String dbPath, String scriptPath) {
		// String command = "sqlite3   " + dbPath + "   \".read " + scriptPath +
		// "\"";
		String command = "sqlite3 \"" + dbPath + "\" \".read '" + scriptPath
				+ "'\"";
		if (log != null)
			log.info("�@�@�y�V�F��������s�z��" + command);
		String stat = new Shell().execute(command);
		return stat;
	}

	// �e�L�X�g�t�@�C���ɏ����o���X�N���v�g�𐶐�
	private static void writeExportScript(String scriptPath, String outPath,
			String delimiter, String table, String key, String fields,
			String where, String order) {
		if (fields.trim().equals(""))
			fields = "*";
		String mode = TABS;// default�̓^�u��؂�
		if (delimiter == null || delimiter.equals("\t"))
			mode = TABS; // Tab-separated values
		else if (delimiter.equals(","))
			mode = CSV; // Comma-separated values
		else
			mode = TABS;
		order = order.trim();
		if (!order.equals(""))
			order = " order by " + order;
		// ---------------------------------------------------------------------
		// ���ʂ�BOM�Ȃ���Utf-8�ŏo��
		// ���ӁF�t�@�C���̃Z�p���[�^��\�ł�sqlite����������̂�"/"�ɕϊ�����I�I
		outPath = FileUtil.normarizeIt(outPath.trim());
		// ---------------------------------------------------------------------
		List list = new ArrayList();
		list.add(".mode " + mode + LF); // �o�͂��^�u��؂�ɕύX
		list.add(".output " + outPath + LF); // �o�͐���w��t�@�C���ɕύX
		list.add("select " + fields + " from " + " " + table + where + order
				+ ";" + LF);
		writeFromList(scriptPath, list);
	}

	// �e�L�X�g�t�@�C�����e�[�u���ɓǂݍ��ރX�N���v�g�𐶐�
	private static void writeImportScript(String scriptPath, String table,
			String inPath, String separator) {
		// ���ӁF�t�@�C���̃Z�p���[�^��\�ł�sqlite����������̂�"/"�ɕϊ�����I�I
		inPath = FileUtil.normarizeIt(inPath.trim());
		List list = new ArrayList();
		list.add(".separator \"" + separator + "\"" + LF); // ��؂蕶�����w�肷��
		list.add(".import \"" + inPath + "\" " + table + LF);// �e�L�X�g�t�@�C����荞��
		writeFromList(scriptPath, list);
	}

	// ���̕�����sqlite�ȊO�ł����p�ł���͂��E�E�E
	public static void writeCreateTableSQL(String createPath, List<String> keys,
			HashMap<String, List<String>> hMap) {
		List listx = new ArrayList();
		for (String tableName : keys) {
			List<String> list = hMap.get(tableName);
			listx.add("drop table if exists " + tableName + "; \n");
			listx.add("create table " + tableName + " (\n");
			listx.add("     " + list.get(0));
			if (list != null && list.size() > 0) {
				for (int i = 1; i < list.size(); i++) {
					listx.add(",\n");
					listx.add("     " + list.get(i));
				}
			}
			listx.add("\n);\n");
		}
		writeFromList(createPath, listx);
	}

	//
	public static void writeImportFromTXT(String importPath,
			List<String> tabeleNames, HashMap<String, String> separatorMap) {
		Inf_oClosure writer2 = new EzWriter(importPath); // "import.sql"
		String path = writer2.getPath();
		String parent = FileUtil.getParent2(path, true);
		List list = new ArrayList();
		for (String tableName : tabeleNames) {
			String separator = separatorMap.get(tableName);
			list.add(".separator \"" + separator + "\"\n");
			String filePath = "\"" + parent + tableName.trim() + ".txt" + "\"";
			list.add(".import " + filePath + " " + tableName + " \n");
		}
		writeFromList(importPath, list);
	}

	private static void writeFromList(String scriptPath, List<String> list) {
		Inf_oClosure writer = new EzWriter(scriptPath);
		writer.setLF("");
		writer.open();
		for (String line : list) {
			writer.write(line); // ��؂蕶�����w�肷��
		}
		writer.close();
	}

	@Override
	public String getKind() {
		return "SQLITE";
	}

	public void runSQL(String sql) {
	}

	//-------------------------------------------------------------------------
	//	String[] types=>�z�񂩂�stmt�Ƀo�C���h���鎞�̌^���w�肷�� 
	//	ex: String[] TYPES = { "text", "Integer", "text", "text", "Integer","Integer", "Integer" };
	//	String sqlTemplate=>"INSERT INTO ItpLog_ (" + FIELDS + ") VALUES(?,?,?,?,?,?,?)";
	//-------------------------------------------------------------------------
	public synchronized int executeUpdatePrep(List<String> sqls, String[] types,
			String sqlTemplate) {
		int result = 0;
		try {
			if (sqls.size() > 1)
				currentCon.setAutoCommit(false);
			PreparedStatement stmt = currentCon.prepareStatement(sqlTemplate);
			int ival = 0;
			for (String sql : sqls) {
				sql = sql.trim();
				if (!sql.equals("")) {
					//				System.out.println("#20150930# addItpLog  new version!!sql>" + sql);
					String[] array = cnvValue2Array(sql);
					//				for (String element : array) {
					//					System.out.println("element>" + element);
					//				}
					for (int i = 0; i < types.length; i++) {
						if (types[i].equals(TEXT)) {
							stmt.setString((i + 1), array[i]);
						} else if (types[i].equals(INTEGER)
								|| types[i].equals(NUMERIC)) {
							try {
								ival = Integer.valueOf(ival);
							} catch (Exception e) {
								ival = 0;
							}
							stmt.setInt((i + 1), Integer.valueOf(array[i]));
						} else {
							//���̃t�B�[���h�͖��Ή�
							stmt.setNull((i + 1), 0);
						}
						//					} else if (types[i].equals(REAL)) {
						//					} else if (types[i].equals(NONE)) {
					}
					int rtn = stmt.executeUpdate();
					//				int rtn = 0;
					if (rtn == -1) {
						// �߂�l:
						// INSERT ���AUPDATE ���ADELETE ���̏ꍇ�͍s���B�����Ԃ��Ȃ� SQL ���̏ꍇ�� 0
						System.out.println(
								"@executeUpdate Error?! sql:" + sqlTemplate);
						System.out.println("@executeUpdate Error?! rtn:" + rtn);
						stmt.close();
						return -1;
					}
					result++;
				}
			}
			stmt.close();
			if (sqls.size() > 1)
				currentCon.commit();
//			System.out.println("#debug 20150930# stmt.close");
		} catch (SQLException e) {
			try {
				System.out.println("#Error rollbacked=>" + e.toString());
				if (sqls.size() > 1)
					currentCon.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			if (log != null) {
				log.fatal("#ERROR @JDBC.executeUpdate\nsql:" + sqlTemplate);
				log.fatal("ERROR:" + e.toString());
				showErrorDialog(e.toString());
			}
			System.out
					.println("#ERROR @JDBC.executeUpdate\nsql:" + sqlTemplate);

			e.printStackTrace();
			System.exit(999);
			return -1;
		}
		return result;
	}

	public static String[] cnvValue2Array(String sql) {
		String val = getEncloseBy(sql, "VALUES(", ")");
		val = csv2tsv(val);
		//		System.out.println("res>" + val);
		val = val.replaceAll("'", "");
		val = val.replaceAll("\"", "");
		return val.split("\t");
	}

	//-------------------------------------------------------------------------
	//	���e�����̊O�ɂ���J���}���^�u�ɕϊ�����
	// 	������'�̒��Ɂh������q�ɂȂ�悤�ȃP�[�X�A����уG�X�P�[�v�V�[�P���X�͔�Ή�
	//-------------------------------------------------------------------------
	private static String csv2tsv(String val) {
		boolean flag = false;//���e���������̕����Ȃ�true
		char ch[] = val.toCharArray();
		StringBuffer buf = new StringBuffer();
		for (char c : ch) {
			if (c == '\'' || c == '\"')
				flag = !flag;
			if (c == ',' && flag == false)
				c = '\t';// �^�u�ɕύX
			buf.append(c);
		}
		return buf.toString();

	}

	//-------------------------------------------------------------------------
	//�w�肵��������ň͂܂ꂽ���������o��
	//-------------------------------------------------------------------------
	public static String getEncloseBy(String str, String start, String end) {
		String res = "";
		str = str.trim();
		if (str.equals(""))
			return res;
		//		int from_ = str.toUpperCase().indexOf(start);
		int from_ = str.indexOf(start);
		if (from_ > 0) {
			res = str.substring(from_ + 7);
			int to_ = res.indexOf(end);
			if (to_ > 0) {
				res = res.substring(0, to_);
			}
		}
		return res;
	}



	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	//	public static void main(String[] args) {
	//		// testCreateDBandImportWithScheme();
	//		//		scaffold();
	//		test20150604();
	//	}

	public static void test20150604() {
		//�w���ʑw����sqlite�̃��[�J��DB�𐶐�����
		System.out.println("## test20150604 ##");
		JDBC_Sqlite dbObj = new JDBC_Sqlite("monSel", "C:/temp/");
		dbObj.exeCreateAndImport();
	}

	// �e�L�X�g�t�@�C�������`�ѓ�(����ƂȂ����)�𐶐�����
	public static void scaffold() {
		String dir = "C:/test/";
		String inPath = "C:/test/MonitorCheck.txt";
		String dbName = "QPR";
		new JDBC_Sqlite(dbName, dir);
		Text2SQLDef.scaffold("QPR.Db", dir, inPath, true, "SQLITE");
		// sqlite�ł�import�������E�E�E�C���|�[�g���Ă��܂�����
	}

}