package kyPkg.sql;

import java.io.*;
import java.util.List;
import kyPkg.uFile.FileUtil;

import static kyPkg.uFile.FileUtil.*;

//-----------------------------------------------------------------------------
//�G�N�X�|�[�g����ۂ̉��s�R�[�h��CRLF�@�i���ʁj
//-----------------------------------------------------------------------------
// �t�@�C���̏o�͐�ɂ́B�hC:\dataBase\�h���擪�ɕt����ꂽ�p�X�ƂȂ�̂ŁA���΃p�X�ȊO�s�Ƃ�������
// �x���E�E�E�q�[�v������Ȃ��Ȃ�E�E�E����܂�ǂ��Ȃ��C�����Ă����@�@
// ������������sqlite���g�����ق����ǂ��̂���(�������encode�̖�肠��)
// jdbc:hsqldb:file:/c:/database/dbname
// JRE �Ɂ@-Xmx512M���w�肵�Ȃ���OUTOFMEMORY�ƂȂ��Ă��܂�
//-----------------------------------------------------------------------------
//http://hsqldb.org/doc/guide/index.html
//http://hsqldb.sourceforge.net/
//http://www.wakhok.ac.jp/~tomoharu/tokuron2003/hsqldb/
//-----------------------------------------------------------------------------
// http://hsqldb.org/
// http://hsqldb.org/doc/guide/ch01.html
// ";shutdown=true"�Ƃ����ǉ����������Ă����ƁA�I������SHUTDOWN�����Ă����
// �Q�l=>	http://www50.tok2.com/home/oppama/hsqldb-start.html
// �v����ɁE�E���������܂ł͉����̃^�C�~���O�Ńt�@�C���ɕۑ�����Ă��邪����܂ł̓C���������ŊǗ�����Ă���Ƃ�������
//General
//Closing the Database
//All databases running in different modes can be closed with the SHUTDOWN command, 
//issued as an SQL query. 
//From version 1.7.2, in-process databases are no longer closed when the last connection 
//to the database is explicitly closed via JDBC, a SHUTDOWN is required. 
//In 1.8.0, a connection property, shutdown=true, can be specified on the first connection
//to the database (the connection that opens the database) to force a shutdown when the last connection closes.
//
//When SHUTDOWN is issued, all active transactions are rolled back. 
//A special form of closing the database is via the SHUTDOWN COMPACT command. 
//This command rewrites the .data file that contains the information stored in CACHED tables and
//compacts it to size. This command should be issued periodically, especially when lots of inserts,
//updates or deletes have been performed on the cached tables. 
//Changes to the structure of the database, such as dropping or modifying populated CACHED tables or 
//indexes also create large amounts of unused file space that can be reclaimed using this command.
//-----------------------------------------------------------------------------
public class JDBC_HSQLDB extends JDBC_TEXDB {
	private static final String DATA_DIR = "./data/";
	private static final String DBNAME = "dbname"; // �ȒP�̂��ߌŒ薼
	//Execute SQL in FILENAME
	@Override
	public String getPath(String path) {
		String fileName = kyPkg.uFile.FileUtil.getFileName(path);
		String dbDir = getDbDir();
		return dbDir + DATA_DIR.substring(2) + fileName;
	}

	// -------------------------------------------------------------------------
	// �኱�X�^�C�����قȂ�̂ŁA�I�[�o�[���C�h���Ă���
	// -------------------------------------------------------------------------
	@Override
	public boolean createTable(String table, String fDefs) {
		dropTable(table);
		String sql = "CREATE TEXT TABLE " + table + " (" + fDefs + ")";
		return executeUpdate(sql);
	}

	// -------------------------------------------------------------------------
	// �e�L�X�g�e�[�u���̍쐬�ƃA�T�C��
	// CREATE TEXT TABLE xTable(XA1 VARCHAR,XB1 VARCHAR,XC1 VARCHAR)
	// SET TABLE xTable SOURCE \"qpr_monitor_info.csv;fs=\\t;ignore_fist=true\"
	// ��؂蕶���w�� fs=\\t; ��ԍŏ��̗�𖳎����� ignore_fist=true\"
	// SET TABLE xTable SOURCE off; �f�B�X�R�l�N�g����
	// -------------------------------------------------------------------------
	// �e�L�X�g�e�[�u���Œǉ��X�V���s���Ƌ�̍s����������ł��Ă��܂��E�E�E
	// -------------------------------------------------------------------------
	// text table �̓�������݂̂̂c�a�ł͍쐬�ł��Ȃ�
	// conn = DriverManager.getConnection("jdbc:hsqldb:mem:/kenTEST" ,"sa","");
	// conn = DriverManager.getConnection("jdbc:hsqldb:file:/" + dbPath
	// ,"sa","");
	// -------------------------------------------------------------------------
	public JDBC_HSQLDB(String dir) {
		if (dir == null || dir.equals("")) {
			if (log != null)
				log.fatal("hsqlDb<Text> dir���w�肳��Ă��܂���");
			return;
		}
		// �f�[�^�x�[�X���쐬����ꏊ
		this.dbDir= FileUtil.mkdir(dir);
		// �e�L�X�g�e�[�u����z�u����ꏊ�i �ȒP�̂��ߌŒ薼"data"�Ƃ����j
		this.dataDir = FileUtil.mkdir(getDbDir() + "data");
		new File(dataDir).mkdirs();
		openDB();
	}

	public JDBC_HSQLDB() {
		this(System.getProperty("user.dir").trim() + "/DB");
	}

	// -------------------------------------------------------------------------
	// �e�[�u�����t�@�C���ɕR�t����i�t�@�C���̓f�[�^�x�[�X�t�H���_��DATA_DIR�t�H���_�ɔz�u����Ă�����́j
	// -------------------------------------------------------------------------
	@Override
	public boolean assignIt(String table, String path, String delimiter,
			String encoding) {
		String fileName = kyPkg.uFile.FileUtil.getFileName(path);
		// ---------------------------------------------------------------------
		// separator
		// ---------------------------------------------------------------------
		String mode = "\\t"; // default�̓^�u��؂�
		if (delimiter == null || delimiter.equals("\t"))
			mode = "\\t";
		else if (delimiter.equals(";"))
			mode = "\\semi";
		else if (delimiter.equals(" "))
			mode = "\\space";
		else if (delimiter.equals("'"))
			mode = "\\apos";
		else
			mode = delimiter;
		// ---------------------------------------------------------------------
		// encode
		// ---------------------------------------------------------------------
		if (delimiter.equals(UTF_8))
			encoding = ";encoding=UTF-8";
		else if (delimiter.equals(SHIFT_JIS))
			encoding = ";encoding=Shift_JIS";
		else if (delimiter.equals(ASCII))
			encoding = ";encoding=ASCII";
		else
			encoding = "";// default=>ASCII�ƂȂ�
		String dataPath = DATA_DIR + fileName;
		String tmpSql = "SET TABLE " + table + " SOURCE \"" + dataPath + ";fs="
				+ mode + encoding + ";ignore_fist=false\"";
		return executeUpdate(tmpSql);
	}

	// -------------------------------------------------------------------------
	// �ꎞ�I�Ƀe�L�X�g�e�[�u�����쐬���āA�I���W�i���̃R�s�[�ɂ��o�͂��s��
	// ���ɕ��@���Ȃ����낤���H�H
	// -------------------------------------------------------------------------
	// �s�p�����[�^�t
	// String outPath �o�͐�
	// String table �ΏۂƂȂ�e�[�u��
	// String orderBy �o�͏����w�肵�����ꍇ�Ɏg��
	// -------------------------------------------------------------------------
	@Override
	public void exportIt(String outPath, String delimiter, String table,
			String key, String fields, String orderBy, String where) {
		if (fields.trim().equals(""))
			fields = "*";
		if (log != null)
			log.info("�@�@�e�[�u���i" + table + "�j���t�@�C���ɏo��=>" + outPath);
		if (!isExist(table)) {
			if (log != null)
				log.info("�@�@#ERROR!!�@Table not Exist!!:" + table);
			return;
		}
		// �폜����
		String dataPath = getPath(outPath);
		kyPkg.uFile.FileUtil.killIt(dataPath);
		String tmpTable = "TMP_TABLE";
		String wWhere = "";
		if (!key.equals("")) {
			wWhere = " WHERE ( " + key + " <> '' ) ";
		}
		orderBy = orderBy.trim();
		if (!orderBy.equals("")) {
			orderBy = " order by " + orderBy;
		}
		List list = getFieldsList(table);
		if (list.size() > 0) {
			// ���򂳂�̃I�[�_�[=>�q���N��]�X�p�̃}�[�W�������l����
			String fDefs = "";
			if (fields.equals("*")) {
				List deflist = getFieldsDefs(table);
				fDefs = kyPkg.util.Joint.join(deflist, ",");
			} else {
				//��⃄�b�c�P�d��
				StringBuffer buf = new StringBuffer();
				String array[] = fields.split(",");
				for (int i = 0; i < array.length; i++) {
					if (i > 0)
						buf.append(",");
					buf.append(array[i]);
					buf.append(" VARCHAR");
				}
				fDefs = buf.toString();
			}
			//System.out.println("fDefs::::" + fDefs);
			createTable(tmpTable, fDefs);
//			assignIt(tmpTable, outPath, delimiter, SHIFT_JIS);
			assignIt(tmpTable, outPath, delimiter, defaultEncoding2);//210161222
			String sql = "INSERT INTO " + tmpTable + " SELECT DISTINCT "
					+ fields + " FROM " + table + wWhere + orderBy + ";";
			// INSERT INTO qpr_monitor_out select distinct * from
			// QPR_MONITOR_BASE order by FLD0;
			executeUpdate(sql);
			dropTable(tmpTable);
		} else {
			System.out.println("#ERROR!! JDBC_HSQLDB@exportit fieldSize =< 0");
		}
	}

	// ���܂̂Ƃ���g���Ă��Ȃ��̂Ŏ�肠�����R�����g�A�E�g���Ă���
	// -------------------------------------------------------------------------
	// // Disconnecting Text Tables
	// -------------------------------------------------------------------------
	// public boolean setTableOff(String table) {
	// executeUpdate("SET TABLE " + table + " SOURCE OFF");
	// return true;
	// }
	// -------------------------------------------------------------------------
	// // ReConnecting Text Tables
	// -------------------------------------------------------------------------
	// public boolean setTableON(String table) {
	// executeUpdate("SET TABLE " + table + " SOURCE ON");
	// return true;
	// }
	@Override
	public TContainer importTable(String table, String srcPath) {
		// ���Y�e�[�u�������݂���Ȃ�����Ă���
		if (!isExist(table))
			dropTable(table);
		// -------------------------------------------------------------
		// �t�@�C������e�L�X�g�e�[�u���𐶐�����
		// -------------------------------------------------------------
		String path = getPath(srcPath);
		TContainer container = new TContainer(table, path, 0);
		String iDefs = container.getfDefs();
		if (createTable(table, iDefs)) {
			// ---------------------------------------------------------
			// �t�@�C�����e�[�u���ɃA�T�C��
			// ---------------------------------------------------------
			assignIt(table, srcPath);
			return container;
		} else {
			return null;
		}
	}

	protected void openDB() {
		if (log != null)
			log.info("hsqlDb�@openDB");
		String jUrl = "jdbc:hsqldb:file:/" + getDbDir() + DBNAME
				+ ";shutdown=true";
		String user = "sa";
		String pass = "";
		System.out.println("# openDB jUrl:" + jUrl);
		if (super.getConnection(jUrl, user, pass) == null) {
			if (log != null)
				log.fatal("hsqlDb<Text> ���������s");
		}
	}

	@Override
	public void close() {
		super.close();
	}

	@Override
	public String getKind() {
		return "HSQLDB";
	}

	@Override
	public void executeSQL(String sql) {
	}
}
