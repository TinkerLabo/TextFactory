package kyPkg.sql;

import java.awt.Component;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;

import kyPkg.converter.Inf_ArrayCnv;
import kyPkg.converter.Inf_ListArrayConverter;
import kyPkg.filter.EzWriter;
import kyPkg.filter.Inf_oClosure;
import kyPkg.globals.GuiControl;
import kyPkg.uFile.MatrixUtil;
import kyPkg.util.MD5;
import kyPkg.util.Shell;

//�@2010/03/09 connectionpool�����E�E�E
//-------------------------------------------------------------------------------
/**
 * Title: JDBC Description: DB�A�N�Z�X�v���O�����̐��` Author: ken yuasa
 */
// -------------------------------------------------------------------------------
// java URL�̕\�L�t�H�[�}�b�g�͈�ʂɉ��L�̂Ƃ���E�E�T�u�v���g�R���ȉ��̓o���o��
// jdbc:subprotocol ://hostname:port/database?name=value&...
// -------------------------------------------------------------------------------
// HSQLDB�̏ꍇ�i�������Ȃ��Ꮯ��ɍ���邻���E�E�j
// jURL = "jdbc:hsqldb:LocoDB";
// -------------------------------------------------------------------------------
// sql2000�̏ꍇ
// jURL = "jdbc:microsoft:sqlserver://AGCQBR:1433;DatabaseName=Northwind";
// -------------------------------------------------------------------------------
// ODBC�R���g���[���p�l���ɓo�^���Ă���ꍇ
// jURL = "jdbc:odbc:QPRDB";
// -------------------------------------------------------------------------------
public class JDBC implements Inf_JDBC {

	private static boolean DEBUG = false;
	private int timeOut = 0;// �b�Ŏw�肷��i0�Ȃ琧���Ȃ��j
	//class GuiControl ��	private static final int DEFAULT_TIMEOUT = 60 * 4;//�^�C���A�E�g��ݒ�
	private Component component;

	// gui��œ����Ă��ăG���[���b�Z�[�W���_�C�A���O�\�������Ĉʏꍇ�ɃZ�b�g����ƁE�E�E�悢����
	public void setComponent(Component component) {
		this.component = component;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	protected String eMsg;

	// logg.fatal("fatal���b�Z�[�W");
	// logg.error("error���b�Z�[�W");
	// logg.warn ("warn ���b�Z�[�W");
	// logg.info ("info ���b�Z�[�W");
	// logg.debug("debug���b�Z�[�W");
	// logg.trace("trace���b�Z�[�W");
	protected Log log = null;
	private String jURL; // �t�q�k�@�@�@�@
	private String user; // ���[�U�[���@�@
	private String password; // �p�X���[�h�@�@
	// private int maxConnection = 20; // �ő�ڑ����@�@
	// private int checkedOut; // �v�[�����鐔�@
	private List<Connection> cnnPool = new ArrayList();

	protected Connection currentCon = null;
	private DatabaseMetaData currentMeta = null;
	private HashMap<String, PreparedStatement> prepMap = null;
	private String driverClassName;

	public String getDriverClassName() {

		// System.out.println("#DBG# getDriverClassName :"+driverClassName);
		return driverClassName;
	}

	/*
	 * (�� Javadoc)
	 * 
	 * @see kyPkg.sql.Inf_JDBC#setLog(org.apache.commons.logging.Log)
	 */
	@Override
	public void setLog(Log log) {
		this.log = log;
	}

	public static void testCnn2Hmap() {
		HashMap hmap = cnn2Hmap(ServerConnecter.getDEF_JURL());
		System.out.println("UID:" + hmap.get("UID"));
		System.out.println("PWD:" + hmap.get("PWD"));
	}

	public static HashMap cnn2Hmap(String cnn) {
		String[] array = cnn.split(";");
		HashMap<String, String> hmap = new HashMap();
		for (int i = 0; i < array.length; i++) {
			String statement = array[i];
			int pos = statement.indexOf("=");
			if (pos > 0) {
				String key = statement.substring(0, pos);
				String value = statement.substring(pos + 1);
				// System.out.println("key:" + key + " value:" + value);
				hmap.put(key, value);
			}
		}
		return hmap;
	}

	// ---------------------------------------------------------------
	// �R���X�g���N�^
	// ---------------------------------------------------------------
	protected JDBC() {
		component = GuiControl.getComponent();
		timeOut = GuiControl.getTimeOut();//�^�C���A�E�g��ݒ�
		// System.out.println("###�@Hello20130312 ###�@timeout:" + timeOut);
	}

	// 2011-07-05 ���e�X�g
	public JDBC(String cnn) {
		this();
		jURL = "JDBC:ODBC:" + cnn;
		HashMap<String, String> hmap = cnn2Hmap(cnn);
		user = hmap.get("UID");
		password = hmap.get("PWD");
		if (user == null)
			user = "";
		if (password == null)
			password = "";
		getConnection(jURL, user, password);
	}

	public JDBC(String jURL, String user, String password) {
		this();
		getConnection(jURL, user, password);
	}

	// �ǉ��f�[�^�̃L�[�ɂ�����f�[�^�����炩���߃}�X�^�[����폜���āA�ǉ��f�[�^���C���T�[�g����

	@Override
	public boolean tableMerge(String oTable, String oKey, String oFlds,
			String iTable, String iKey, String iFlds) {
		String sql = "";
		// -------------------------------------------------------------
		// �}�X�^�[����ǉ��f�[�^�Ɠ����L�[�̃f�[�^����������
		// DELETE FROM qpr_monitor_base where�@qpr_monitor_base.fld0
		// in (select qpr_monitor_info.fld0 FROM qpr_monitor_info)
		// -------------------------------------------------------------
		sql = "DELETE FROM " + oTable + " where " + oTable + "." + iKey
				+ " in (select " + iTable + "." + iKey + " FROM " + iTable
				+ ")";
		if (log != null)
			log.info("�@�@�����L�[�̃f�[�^�������F" + sql);
		// System.out.println("tableMerge sql:"+);
		executeUpdate(sql);
		// -------------------------------------------------------------
		// �}�X�^�[�Ƀf�[�^��ǉ�����
		// INSERT INTO qpr_monitor_base SELECT * FROM�@qpr_monitor_info;
		// -------------------------------------------------------------
		// sql = "INSERT INTO " + oTable + " SELECT " + iFlds+ " FROM " + iTable
		// + ";";
		sql = "INSERT INTO " + oTable + "(" + oFlds + ")" + " SELECT " + iFlds
				+ " FROM " + iTable + ";";
		if (log != null)
			log.info("�@�@�f�[�^��ǉ��F" + sql);
		// System.out.println("tableMerge sql:" + sql);
		executeUpdate(sql);
		// int count1 = dbObj.rowCount(wMASTER); // �����m�F
		// int count2 = dbObj.rowCount(wMonINF); // �����m�F
		// System.out.println("wMASTER count1:"+count1);
		// System.out.println("wMonINF count2:"+count2);
		return true;
	}

	@Override
	public boolean tableCopy(String dstTable, String fields, String srcTable,
			Boolean killOption) {
		String sql = "";
		int oMaxCol = 0;
		String[] array = fields.split(",");
		int iMaxCol = array.length;
		if (killOption)
			dropTable(dstTable);
		if (!isExist(dstTable)) {
			sql = "CREATE TABLE " + dstTable + " (" + fields + ")";
			executeUpdate(sql);
		}
		List list = getFieldsList(dstTable);
		oMaxCol = list.size();
		if (iMaxCol != oMaxCol) {
			System.out.println("#ERROR�@���͌�(" + iMaxCol + ")�Əo�͐�(" + oMaxCol
					+ ")�̃J���������Ⴂ�܂�");
			return false;
		} else {
			// �e�[�u������ɂ���
			sql = "DELETE from " + dstTable + ";";
			executeUpdate(sql);
			// �ǉ������i���ꂼ��̃t�B�[���h�̌^�`�F�b�N�͂��Ă��Ȃ��j
			sql = "INSERT INTO " + dstTable + " SELECT * FROM " + srcTable
					+ ";";
			executeUpdate(sql);
			return true;
		}
	}

	@Override
	public int rowCount(String dstTable) {
		// �����m�F�i�R�s�[��j
		String sql = "select count(*) from " + dstTable + ";";
		String ans1 = queryOne(sql);
		int count = Integer.parseInt(ans1);
		return count;
	}

	@Override
	public boolean dropTable(String table) {
		executeUpdate("DROP        TABLE IF EXISTS " + table + " ;");
		return true;
	}

	@Override
	public boolean truncate(String table) {
		executeUpdate("DELETE from " + table + " ;");
		return true;
	}

	// ---------------------------------------------------------------
	// �Ō�ɁI���ׂẴR�l�N�V�������J��
	// ---------------------------------------------------------------
	/*
	 * (�� Javadoc)
	 * 
	 * @see kyPkg.sql.Inf_JDBC#releaseAll()
	 */
	@Override
	public synchronized void releaseAll() {

		for (Iterator iterator = cnnPool.iterator(); iterator.hasNext();) {
			Connection con = (Connection) iterator.next();
			closeConnection(con);
		}
		cnnPool.clear();
	}

	// ---------------------------------------------------------------
	// (�g���I�����)�R�l�N�V������ҋ@�L���[�ɖ߂�
	// ---------------------------------------------------------------

	@Override
	public synchronized void freeConnection() {
		freeConnection(currentCon);
	}

	@Override
	public synchronized void freeConnection(Connection con) {
		cnnPool.add(con);
		// TODO�@��肠��
		System.out.println("�R�l�N�V�������L���E�Ɋi�[�E�E�E�E:" + cnnPool.size());
		notifyAll();
	}

	// ---------------------------------------------------------------
	// �h���C�o�����[�h����
	// ---------------------------------------------------------------
	private Connection openDB() {
		if (DEBUG)
			System.out.println("#debug @openDB  jURL:" + jURL);
		driverClassName = "";
		if (jURL == null || user == null || password == null) {
			System.err.println("#Error jURL || user || password == null");
			System.err.println("jURL:" + jURL);
			System.err.println("user:" + user);
			System.err.println("password:" + password);
			return null;
		}
		try {
			// if (driverClassName.trim().equals("")) {
			driverClassName = getDriverClassName(jURL);
			// }
			// System.out.println("driverClassName:" + driverClassName);
			classLoader(driverClassName);
			return DriverManager.getConnection(jURL, user, password);
		} catch (Exception e) {
			if (log != null) {
				log.fatal("#ERROR @JDBC.openDB");
				log.fatal("ERROR:" + e.toString());
				showErrorDialog(e.toString());
			}
			System.err.println("Error@openDB className:" + driverClassName);
			System.err.println("             jURL    :" + jURL);
			System.err.println("             user    :" + user);
			System.err.println("             password:" + password);
			e.printStackTrace();
			return null;
		}
	}

	// ---------------------------------------------------------------
	// �R�l�N�V�������擾
	// ---------------------------------------------------------------
	@Override
	public Connection getConnection(String jURL, String user, String password) {
		this.jURL = jURL;
		this.user = user;
		this.password = password;
		// this.maxConnection = 10;
		// driverClassName = getDriverClassName(jURL);
		return getConnection();
	}

	@Override
	public synchronized Connection getConnection() {
		return getValidConnection(null);
	}

	private synchronized Connection getValidConnection(Connection connection) {
		try {
			if (connection != null) {
				if (!connection.isClosed()) {
					currentCon = connection;
					return currentCon;
				} else {
					System.out.println("Connection is Closed...");
				}
			}
			while (cnnPool.size() > 0 && currentCon == null) {
				System.out.println(
						"�R�l�N�V���������o���E�E�E�E#Pool.size():" + cnnPool.size());
				currentCon = (Connection) cnnPool.get(0);
				cnnPool.remove(0);
				if (currentCon != null && !currentCon.isClosed())
					return currentCon;
			}
		} catch (SQLException e) {
		}
		if (DEBUG)
			System.out.println(
					"@getValidConnection Connection is null....reconnect!!");
		currentCon = openDB();
		if (currentCon != null) {
			try {
				currentMeta = currentCon.getMetaData();
			} catch (SQLException e) {
				if (log != null) {
					log.fatal("#ERROR @JDBC.getConnection");
					log.fatal("ERROR:" + e.toString());
					showErrorDialog(e.toString());
				}
				e.printStackTrace();
			}
		}
		return currentCon;
	}

	// ---------------------------------------------------------------
	// �ڑ����N���[�Y
	// ---------------------------------------------------------------
	@Override
	public void close() {
		closeConnection(currentCon);
		currentMeta = null;
	}

	// ---------------------------------------------------------------
	// �R�l�N�V���������
	// ---------------------------------------------------------------
	@Override
	public synchronized void closeConnection(Connection con) {
		try {
			if (con != null)
				con.close();
		} catch (SQLException e) {
			if (log != null) {
				log.fatal("#ERROR @JDBC.closeConnection");
				log.fatal("ERROR:" + e.toString());
				showErrorDialog(e.toString());
			}
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		con = null;
	}

	public void showErrorDialog(String errorMessage) {
		if (component != null) {
			// javax.swing.JOptionPane.showMessageDialog(component, new
			// javax.swing.JLabel(errorMessage), "SQL Error...",
			// javax.swing.JOptionPane.ERROR_MESSAGE);
			javax.swing.JOptionPane.showMessageDialog(component,
					new javax.swing.JTextArea(errorMessage), "SQL Error...",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	protected void finalize() {
		// XXX close�����H
	}

	private String getDriverClassName(String jURL) {
		jURL = jURL.toLowerCase();
		String driverClassName = "";
		// SQL2000�h���C�o�̏ꍇ
		if (jURL.startsWith("jdbc:microsoft:sqlserver:") == true) {
			driverClassName = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
		} else if (jURL.startsWith("jdbc:mysql:") == true) {
			// "org.gjt.mm.mysql.Driver"
			driverClassName = "com.mysql.jdbc.Driver";
		} else if (jURL.startsWith("jdbc:hsqldb:") == true) {
			driverClassName = "org.hsqldb.jdbcDriver";
		} else if (jURL.startsWith("jdbc:sqlite:") == true) {
			driverClassName = "org.sqlite.JDBC";
		} else if (jURL.startsWith("jdbc:postgresql:") == true) {
			driverClassName = "org.postgresql.Driver";
		} else if (jURL.startsWith("jdbc:firebirdsql:") == true) {
			driverClassName = "org.firebirdsql.jdbc.FBDriver";
		} else if (jURL.startsWith("jdbc:derby:") == true) {
			driverClassName = "org.apache.derby.jdbc.EmbeddedDriver";
		} else if (jURL.startsWith("jdbc:oracle:") == true) {
			driverClassName = "oracle.jdbc.OracleDriver";
		} else {
			driverClassName = "sun.jdbc.odbc.JdbcOdbcDriver";
		}
		// System.out.println("@getDriverClassName:"+driverClassName+"<=jurl:"+jURL);
		return driverClassName;
	}

	// ---------------------------------------------------------------
	// JDBC-ODBC bridge �h���C�o�N���X�����[�h
	// ---------------------------------------------------------------
	// <<����>> postgres�̃A�N�Z�X�������w�肷��ɂ́E�E�E
	// /usr/local/pgsql/data/pg_hba.conf ��
	// host all 10.6.1.40 255.255.255.255 trust
	// �ƁA�\�ߏ�������ł����Ȃ��ƃG���[�ƂȂ�
	// ---------------------------------------------------------------
	private void classLoader(String driverClassName) throws Exception {
		// System.out.print("@classLoader ClassName�F" + driverClassName);
		Class c = Class.forName(driverClassName); // �h���C�o�N���X�����[�h
		if (DEBUG)
			System.out.println(" ==> ClassLoaded:" + c.toString());
	}

	protected PreparedStatement createprepareStatement(String phrase) {
		currentCon = getValidConnection(currentCon);
		try {
			return currentCon.prepareStatement(phrase);
		} catch (SQLException e) {
			if (log != null) {
				log.fatal("#ERROR @createStatement");
				log.fatal("ERROR:" + e.toString());
				showErrorDialog(e.toString());
			}
			System.out.println("Error!!@JDBC.createStatement:" + e.toString());
			e.printStackTrace();
			return null;
		}
	}

	protected Statement createStatement() {
		currentCon = getValidConnection(currentCon);
		try {
			// ���Ȃ݂ɁE�E�E
			// Statement �ÓI�ȃX�e�[�g�����g
			// PreparedStatement �v���R���p�C���X�e�[�g�����g
			// CallableStatement �X�g�A�h�v���V�W�����s
			if (currentCon == null) {
				if (log != null) {
					log.fatal("#ERROR @createStatement...currentCon == null");
				} else {
					System.out.println(
							"#ERROR @createStatement...currentCon == null");
				}
			}
			Statement stmt = currentCon.createStatement();
			// �^�C���A�E�g��ݒ肵�Ă���i�e�L�X�g�c�a�Ȃǂł͐ݒ�ł��Ȃ��悤���j
			try {
				//				System.out.println("�������������������������������������������������������������������@timeout=>"
				//						+ timeOut);
				//				�h���C�o�� Statement �I�u�W�F�N�g�̎��s��҂b�����A�w�肳�ꂽ�b���ɐݒ肵�܂��B
				//				�V�����N�G���[�^�C���A�E�g�̐����l�̕b���B�[���͖��������Ӗ�����
				stmt.setQueryTimeout(timeOut);
			} catch (Exception e) {
			}
			// System.out.println("#############################################");
			// System.out.println("## JDBC setQueryTimeout:" + timeOut);
			// System.out.println("#############################################");
			return stmt;
		} catch (SQLException e) {
			if (log != null) {
				log.fatal("#ERROR @createStatement");
				log.fatal("ERROR:" + e.toString());
				showErrorDialog(e.toString());
			}
			System.out.println("Error!!@JDBC.createStatement:" + e.toString());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void executeBatch(String sql, List<List> paramList) {
		try {
			PreparedStatement prep = currentCon.prepareStatement(sql);
			for (Iterator iter = paramList.iterator(); iter.hasNext();) {
				List params = (List) iter.next();
				for (int i = 0; i < params.size(); i++) {
					prep.setString((i + 1), (String) params.get(i));
				}
				prep.addBatch();
			}
			currentCon.setAutoCommit(false);
			prep.executeBatch();
			currentCon.setAutoCommit(true);
			prep.close();
		} catch (SQLException e) {
			if (log != null) {
				log.fatal("#ERROR @JDBC.executeBatch\nsql:" + sql);
				log.fatal("ERROR:" + e.toString());
				showErrorDialog(e.toString());
			}
			e.printStackTrace();
		}
	}

	// ---+---------+---------+---------+---------+---------+---------+---------+
	// �������������P
	// ������������ use for SQL commands CREATE, DROP, INSERT and UPDATE
	// ---+---------+---------+---------+---------+---------+---------+---------+
	@Override
	public void execute(String sql) {
		executeUpdate(sql);
	}

	// ���̓��̓p�����[�^�l�����蓖�Ă܂��B
	// ���̃R�[�h�́A������p�����[�^�������܂��B
	// String newValue;
	// prepStmt.setStringParameter(1, newValue);
	// �������s���܂��B
	// �߂�l�́A���ɉe�����ꂽ���[�̐��������܂��B
	// long rowsInserted = prepStmt.executeStatement();

	// prepStmt = conn.prepareStatement(
	// "INSERT INTO MyTable(MyColumn) values (?)");

	// //�v���y�A�h�X�e�[�g�����g�̎g�p��
	// public void ex_Prep() {
	// String table ="tableName";
	// String sql = "INSERT INTO "
	// + table
	// + " (XA1,XBA,XBB) "
	// + " values(?,?,?);";
	// if (openPrep("anyHash", sql) == false) {
	// System.out.println("#Error ");
	// }
	// while (true) {
	// String[] array ={"a","b","c"};
	// if (prepUpdate("anyHash", array) == false) {
	// System.out.println("#Error ");
	// }
	// }
	// }
	@Override
	public synchronized boolean openPrep(String hash, String sql) {
		if (prepMap == null)
			prepMap = new HashMap();
		try {
			PreparedStatement prepStmt = createprepareStatement(sql);
			prepMap.put(hash, prepStmt);
		} catch (Exception e) {
			if (log != null) {
				log.fatal("#ERROR @JDBC.openPrep");
				log.fatal("openPrep sql:" + sql);
				log.fatal("=>" + e.toString());
				showErrorDialog(e.toString());

			}
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public synchronized void closePrep(String hash) {
		if (prepMap == null)
			prepMap = new HashMap();
		try {
			PreparedStatement prepStmt = prepMap.get(hash);
			if (prepStmt == null) {
				log.fatal("#ERROR @closePrep prepStmt==null");
				return;
			}
			prepStmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected String getCatalog() {
		if (currentCon == null)
			return null;
		try {
			return currentCon.getCatalog();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// private int rs2Stream(String format, Inf_oClosure writer, ResultSet rs) {
	// return rs2Stream(format, writer, rs,null);
	// }

	private int rs2Stream(String format, Inf_oClosure writer, ResultSet rs,
			Inf_ListArrayConverter cnv) {
		int cnt = 0;
		try {
			ResultSetMetaData rsMeta = rs.getMetaData();
			int maxCol = rsMeta.getColumnCount(); // ���ʃZ�b�g�̗�
			List dataRow = null;
			while (rs.next()) {
				dataRow = new ArrayList();
				// �����Ӂ�ResultSet�̍ŏ��̃J�����̓[������Ȃ��ĂP
				for (int j = 0; j < maxCol; j++) {
					dataRow.add(rs.getObject(j + 1));
				}

				// 2014-02-24 yuasa
				if (cnv != null) {
					dataRow = cnv.convert(dataRow);
				}

				cnt++;
				String rec = MatrixUtil.formatIt(format, dataRow);
				//20170425 �������G���[
				//				if (rec.startsWith("E 04-YY")) {
				//					rec = Onbiki.cnv2Similar(rec, FileUtil.SHIFT_JIS);
				//					System.err.println("#20170425# rec:" + rec);
				//				}
				writer.write(rec);
			}

		} catch (SQLException e) {
			if (log != null) {
				log.fatal("#ERROR @JDBC.r2Stream");
				log.fatal("ERROR:" + e.toString());
				showErrorDialog(e.toString());

			}

			e.printStackTrace();
		}
		return cnt;
	}

	private int rs2Stream(Inf_oClosure writer, ResultSet rs) {
		int cnt = 0;
		try {
			ResultSetMetaData rsMeta = rs.getMetaData();
			int maxCol = rsMeta.getColumnCount(); // ���ʃZ�b�g�̗�
			List dataRow = null;
			while (rs.next()) {
				dataRow = new ArrayList();
				// �����Ӂ�ResultSet�̍ŏ��̃J�����̓[������Ȃ��ĂP
				for (int j = 0; j < maxCol; j++) {
					dataRow.add(rs.getObject(j + 1));
				}
				cnt++;
				writer.write(dataRow, cnt);
			}

		} catch (SQLException e) {
			if (log != null) {
				log.fatal("#ERROR @JDBC.r2Stream");
				log.fatal("ERROR:" + e.toString());
				showErrorDialog(e.toString());

			}

			e.printStackTrace();
		}
		return cnt;
	}

	private int rs2Closure(Inf_Closure closure, ResultSet rs) {
		int cnt = 0;
		if (closure == null)
			return -1;
		closure.init();
		try {
			ResultSetMetaData rsMeta = rs.getMetaData();
			int maxCol = rsMeta.getColumnCount(); // ���ʃZ�b�g�̗�
			List dataRow = null;
			while (rs.next()) {
				dataRow = new ArrayList();
				// �����Ӂ�ResultSet�̍ŏ��̃J�����̓[������Ȃ��ĂP
				for (int j = 0; j < maxCol; j++) {
					Object obj = rs.getObject(j + 1);
					//					if(j==0){
					//						System.out.println("debug "+obj);
					//					}
					dataRow.add(obj);
				}
				closure.execute(dataRow);
				cnt++;
			}
		} catch (SQLException e) {
			if (log != null) {
				log.fatal("#ERROR @JDBC.r2Stream");
				log.fatal("ERROR:" + e.toString());
				showErrorDialog(e.toString());
			}
			e.printStackTrace();
		}
		closure.fin();
		return cnt;
	}

	// for debug
	public static void enumMatrix(List<List> matrix) {
		StringBuffer buff = new StringBuffer();
		for (Iterator iter = matrix.iterator(); iter.hasNext();) {
			List row = (List) iter.next();
			buff.delete(0, buff.length());
			for (Iterator iterator = row.iterator(); iterator.hasNext();) {
				Object element = iterator.next();
				buff.append(element.toString());
				buff.append(" - ");
			}
			System.out.println("=>" + buff.toString());
		}
	}

	private List<List> rs2Matrix(ResultSet rs, boolean headerOpt, int limit) {
		int counter = 0;
		if (limit == -1)
			limit = Integer.MAX_VALUE;
		List matrix = new ArrayList();
		try {
			ResultSetMetaData rsMeta = rs.getMetaData();
			int maxCol = rsMeta.getColumnCount(); // ���ʃZ�b�g�̗�
			List dataRow = null;
			// -------------------------------------------------------
			// Header�i�񖼓������j
			// -------------------------------------------------------
			if (headerOpt == true) {
				dataRow = new ArrayList();
				for (int j = 0; j < maxCol; j++) {
					// colSiz[j] = rsMeta.getColumnDisplaySize(j + 1);
					dataRow.add(rsMeta.getColumnName(j + 1));
				}
				matrix.add(dataRow);
			}
			// -------------------------------------------------------
			// Body�i�f�[�^�����j
			// -------------------------------------------------------
			while (rs.next()) {
				dataRow = new ArrayList();
				// �����Ӂ�ResultSet�̍ŏ��̃J�����̓[������Ȃ��ĂP
				for (int j = 0; j < maxCol; j++) {
					dataRow.add(rs.getObject(j + 1));
				}
				counter++;
				matrix.add(dataRow);
				if (counter > limit)
					break;
			}

		} catch (SQLException e) {
			if (log != null) {
				log.fatal("#ERROR @JDBC.rs2List");
				log.fatal("ERROR:" + e.toString());
				showErrorDialog(e.toString());

			}

			System.out.println("#ERROR @JDBC.rs2List limit:" + limit);
			e.printStackTrace();
		}
		return matrix;
	}

	private List<String> rs2List(ResultSet rs, boolean headerOpt, int limit) {
		int counter = 0;
		if (limit == -1)
			limit = Integer.MAX_VALUE;
		List<String> list = new ArrayList();
		try {
			ResultSetMetaData rsMeta = rs.getMetaData();
			int maxCol = rsMeta.getColumnCount(); // ���ʃZ�b�g�̗�
			StringBuffer dataRow = new StringBuffer();
			// -------------------------------------------------------
			// Header�i�񖼓������j
			// -------------------------------------------------------
			if (headerOpt == true) {
				dataRow.delete(0, dataRow.length());
				for (int j = 0; j < maxCol; j++) {
					dataRow.append(rsMeta.getColumnName(j + 1).toString());
				}
				list.add(dataRow.toString());
			}
			// -------------------------------------------------------
			// Body�i�f�[�^�����j
			// -------------------------------------------------------
			while (rs.next()) {
				dataRow.delete(0, dataRow.length());
				// �����Ӂ�ResultSet�̍ŏ��̃J�����̓[������Ȃ��ĂP
				for (int j = 0; j < maxCol; j++) {
					dataRow.append(rs.getObject(j + 1).toString());
				}
				counter++;
				list.add(dataRow.toString());
				if (counter > limit)
					break;
			}

		} catch (SQLException e) {
			if (log != null) {
				log.fatal("#ERROR @JDBC.rs2List");
				log.fatal("ERROR:" + e.toString());
				showErrorDialog(e.toString());
			}

			System.out.println("#ERROR @JDBC.rs2List limit:" + limit);
			e.printStackTrace();
		}
		return list;
	}

	private HashMap<String, String> rs2HashMap(ResultSet rs, String delimiter) {
		if (delimiter == null)
			delimiter = "";
		HashMap<String, String> hashMap = new HashMap();
		StringBuffer buf = new StringBuffer();
		try {
			ResultSetMetaData rsMeta = rs.getMetaData();
			int maxCol = rsMeta.getColumnCount(); // ���ʃZ�b�g�̗�
			// -------------------------------------------------------
			// Body�i�f�[�^�����j
			// -------------------------------------------------------
			while (rs.next()) {
				if (maxCol >= 1) {
					String key = rs.getObject(1).toString();
					buf.delete(0, buf.length());
					buf.append(rs.getObject(2).toString());
					for (int i = 3; i <= maxCol; i++) {
						buf.append(delimiter);
						buf.append(rs.getObject(i).toString());
					}
					// String val = rs.getObject(2).toString();
					// hashMap.put(key, val);
					hashMap.put(key, buf.toString());
				}
			}

		} catch (SQLException e) {
			if (log != null) {
				log.fatal("#ERROR @JDBC.rs2List");
				log.fatal("ERROR:" + e.toString());
				showErrorDialog(e.toString());
			}

			e.printStackTrace();
		}
		return hashMap;
	}

	// -------------------------------------------------------------------------
	// �X�L�[�}�ꗗ��list�ŕԂ�
	// -------------------------------------------------------------------------
	/*
	 * (�� Javadoc)
	 * 
	 * @see kyPkg.sql.Inf_JDBC#getSchemeList()
	 */
	@Override
	public List getSchemeList() {
		if (currentMeta == null)
			return null;
		List list = new ArrayList();
		try {
			ResultSet rSet = currentMeta.getSchemas();
			while (rSet.next()) {
				list.add(rSet.getString(1));
			}
			rSet.close();
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	// -------------------------------------------------------------------------
	// DB�J�^���O�ꗗ��list�ŕԂ�
	// -------------------------------------------------------------------------
	/*
	 * (�� Javadoc)
	 * 
	 * @see kyPkg.sql.Inf_JDBC#getCatalogList()
	 */
	@Override
	public List getCatalogList() {
		// System.out.println("#debug# getCatalogList!");
		if (currentMeta == null)
			return null;
		try {
			ResultSet rSet = currentMeta.getCatalogs();
			List list = new ArrayList();
			while (rSet != null && rSet.next()) {
				list.add(rSet.getString(1));
			}
			rSet.close();
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	// -------------------------------------------------------------------------
	// �t�B�[���h���ꗗ��list�ŕԂ�
	// -------------------------------------------------------------------------
	// �K�v�ȍ��ڂ�ԍ��itype�j�Ɏw�肷��
	// 1.TABLE_CAT String => �e�[�u���J�^���O (null �̉\��������)
	// 2.TABLE_SCHEM String => �e�[�u���X�L�[�} (null �̉\��������)
	// 3.TABLE_NAME String => �e�[�u����
	// >4.COLUMN_NAME String => ��
	// 5.DATA_TYPE short => java.sql.Types ����� SQL �̌^
	// >6.TYPE_NAME String => �f�[�^�\�[�X�ˑ��̌^���BUDT �̏ꍇ�A�^���͊��S�w��
	// 7.COLUMN_SIZE int => ��T�C�Y�B
	// char �� date �̌^�ɂ��Ă͍ő啶�����Anumeric �� decimal �̌^�ɂ��Ă͐��x
	// -------------------------------------------------------------------------
	/*
	 * (�� Javadoc)
	 * 
	 * @see kyPkg.sql.Inf_JDBC#getColumnCount(java.lang.String)
	 */
	@Override
	public int getColumnCount(String tableName) {
		List list = getFieldsList(tableName);
		if (list != null) {
			return list.size();
		}
		return -1;
	}

	/*
	 * (�� Javadoc)
	 * 
	 * @see kyPkg.sql.Inf_JDBC#getField(java.lang.String, int)
	 */
	@Override
	public String getField(String tableName, int seq) {
		List list = getFieldsList(tableName);
		if (list != null && list.size() > seq) {
			Object val = list.get(seq);
			return val.toString();
		}
		return null;
	}

	/*
	 * (�� Javadoc)
	 * 
	 * @see kyPkg.sql.Inf_JDBC#getFieldsList(java.lang.String)
	 */
	@Override
	public List getFieldsList(String tableName) {
		return getFieldsList(tableName, 4);// 4:��
	}

	@Override
	public String getFields(String tableName) {
		StringBuffer buf = new StringBuffer();
		List list = getFieldsList(tableName);
		for (Iterator it = list.iterator(); it.hasNext();) {
			String fld = (String) it.next();
			if (buf.length() > 0)
				buf.append(",");
			buf.append(fld);
		}
		return buf.toString();
	}

	// �擪����A�w�肵���J�����̃t�B�[���h�����J���}�ŘA�����ĕԂ��i���̐����w�肳�ꂽ�ꍇ�͌�납�炻�̂Ԃ���j
	// �@�e�X�g�p�^�[���͈ȉ��̒ʂ�E�E�E�e�X�g�p�^�[�����ǂ̂悤�Ɏc�����H�H����I�I
	// String wFld1 = dbObj.getFields(masterTable, 0);
	// String wFld2 = dbObj.getFields(masterTable, 5);
	// String wFld3 = dbObj.getFields(masterTable, -5);
	// String wFld4 = dbObj.getFields(masterTable, 80);
	// String wFld5 = dbObj.getFields(masterTable, -80);
	// System.out.println("wFld1:"+wFld1);
	// System.out.println("wFld2:"+wFld2);
	// System.out.println("wFld3:"+wFld3);
	// System.out.println("wFld4:"+wFld4);
	// System.out.println("wFld5:"+wFld5);
	@Override
	public String getFields(String tableName, int n) {
		StringBuffer buf = new StringBuffer();
		List list = getFieldsList(tableName);
		int max = 0;
		if (n <= 0) {
			max = list.size() + n;
			// ���̐��Ȃ̂Ŏ��ۂ͈�����܂�
			// max��0�`���ł���΃X�y�[�X���Ԃ�ƍl���܂�
		} else {
			max = n;
		}
		if (max >= list.size()) {
			max = list.size();
			System.out.println("���ۂ̃t�B�[���h�����z�����J���������w�肳�ꂽ�̂Ŗ�������");
		}
		for (int i = 0; i < max; i++) {
			String fld = (String) list.get(i);
			if (buf.length() > 0)
				buf.append(",");
			buf.append(fld);
		}
		// for (Iterator it = list.iterator(); it.hasNext();) {
		// String fld = (String) it.next();
		// if (buf.length() > 0)
		// buf.append(",");
		// buf.append(fld);
		// }
		return buf.toString();
	}

	public String[] getFieldsArray(String tableName) {
		List list = getFieldsList(tableName);
		if (list != null) {
			return (String[]) list.toArray(new String[list.size()]);
		} else {
			return null;
		}
	}

	/*
	 * (�� Javadoc)
	 * 
	 * @see kyPkg.sql.Inf_JDBC#getFieldsList(java.lang.String, int)
	 */
	// public List<String> getFieldsListz(String pTable, int pIdx) {
	// return getFieldsList(pTable, pIdx);
	// if (list != null) {
	// return new Vector(list);
	// } else {
	// return null;
	// }
	// }
	// -----+---------+---------+---------+---------+---------+---------+---------+
	// 1.�e�[�u���J�^���O => TABLE_CAT String (null �̉\��������)
	// 2.�e�[�u���X�L�[�} => TABLE_SCHEM String (null �̉\��������)
	// 3.�e�[�u���� => TABLE_NAME String
	// 4.�� => COLUMN_NAME String
	// 5.SQL �̌^ => DATA_TYPE short (java.sql.Types �����)
	// 6.�f�[�^�\�[�X�ˑ��̌^��=>TYPE_NAME String (UDT �̏ꍇ�A�^���͊��S�w��)
	// 7.��T�C�Y=>COLUMN_SIZE int
	// ��char �� date �̌^�ɂ��Ă͍ő啶�����Anumeric �� decimal �̌^�ɂ��Ă͐��x
	// -----+---------+---------+---------+---------+---------+---------+---------+
	public HashMap<String, Integer> getTypeMap(String tableName) {
		if (tableName == null)
			return null;
		tableName = tableName.toUpperCase(); // debug
		if (currentMeta == null) {
			System.out.println("#ERROR@getFieldMaps => currentMeta is null");
			return null;
		}
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();
		try {
			ResultSet rSet = currentMeta.getColumns(null, null, tableName,
					null);
			while (rSet != null && rSet.next()) {
				String key = rSet.getString(4);
				int value = rSet.getInt(5);
				hmap.put(key, value);
			}
			rSet.close();
			return hmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getColumnNames(String tableName) {
		if (tableName == null)
			return null;
		return getFieldsList(tableName, 4); // �񖼁@4.COLUMN_NAME
	}

	@Override
	public List<String> getFieldsList(String tableName, int kindOf) {
		tableName = tableName.toUpperCase(); // debug
		if (currentMeta == null) {
			System.out.println("#ERROR currentMeta == null");
			return null;
		}
		// System.out.println("#check0714<1> tableName:"+tableName);
		List<String> list = new ArrayList();
		try {
			ResultSet rSet = currentMeta.getColumns(null, null, tableName,
					null);
			while (rSet != null && rSet.next()) {
				// System.out.println("#check0714<2> loop:"+rSet.getString(kindOf));
				list.add(rSet.getString(kindOf));
			}
			rSet.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List getFieldsDefs(String tableName) {
		tableName = tableName.toUpperCase(); // debug
		String defStr = "";
		if (currentMeta == null)
			return null;
		List list = new ArrayList();
		try {
			ResultSet rSet = currentMeta.getColumns(null, null, tableName,
					null);
			while (rSet != null && rSet.next()) {
				defStr = rSet.getString(4) + " " + rSet.getString(6);
				list.add(defStr);
			}
			rSet.close();
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	// -------------------------------------------------------------------------
	// �e�[�u�����ꗗ��list�ŕԂ�
	// -------------------------------------------------------------------------
	// ���l�d�l�n getTables�̕Ԃ���ɂ��āi�eString�j
	// 1.TABLE_CAT => �e�[�u���J�^���O�� ex.Pubs
	// 2.TABLE_SCHEM => �e�[�u���X�L�[�} ex.dbo
	// 3.TABLE_NAME => �e�[�u���� !! ex.
	// 4.TABLE_TYPE => �e�[�u���̌^�B!! "TABLE","VIEW","SYSTEM TABLE"
	// 5.REMARKS => �e�[�u���Ɋւ�������� !!
	// 6.TYPE_CAT => �̌^�̃J�^���O
	// 7.TYPE_SCHEM => �̌^�̃X�L�[�}
	// 8.TYPE_NAME => �̌^��
	// 9.SELF_REFERENCING_COL_NAME => �^�t���e�[�u���̎w�肳�ꂽ�u���ʎq�v��̖��O
	// 10.REF_GENERATION => "SYSTEM"�A"USER"�A"DERIVED"
	// -------------------------------------------------------------------------
	// ��g�p���
	// �����ɂ͈ȉ��̂悤�ȃe�[�u���̎�ʂ�\���e�[�u����n��
	// String[] patStr = {"TABLE","VIEW","SYSTEM TABLE",
	// "GLOBAL TEMPORARY","LOCAL TEMPORARY","ALIAS","SYNONYM"};
	// instance.popTblString(wCombo,patStr);
	// -------------------------------------------------------------------------
	@Override
	public List getTableNameList(String[] patStr) {
		return getTableNameList(patStr, new int[] { 3 }, "");
	}

	@Override
	public List<String> getTableNameList(String[] patStr, int[] seq,
			String delimiter) {
		if (currentMeta == null)
			return null;
		StringBuffer buff = new StringBuffer();
		List<String> list = new ArrayList();
		try {
			ResultSet rSet = currentMeta.getTables(null, null, null, patStr);
			while (rSet != null && rSet.next()) {
				buff.delete(0, buff.length());
				if (seq.length > 0) {
					buff.append(rSet.getString(seq[0]));
					for (int i = 1; i < seq.length; i++) {
						buff.append(delimiter);
						buff.append(rSet.getString(seq[i]));
					}
				}
				list.add(buff.toString());
			}
			rSet.close();
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	// ---------------------------------------------------------------
	// isExistTbl �Y���e�[�u�������݂��邩�ǂ���
	// ��g�p��� instance.isExist("�e�[�u����");
	// ---------------------------------------------------------------
	@Override
	public boolean isExist(String tableName) {
		return isTableExist(tableName, true);
	}

	@Override
	public boolean isTableExist(String pTblName, boolean toUppercase) {
		if (currentMeta == null)
			return false;
		if (toUppercase)
			pTblName = pTblName.toUpperCase();
		String[] patStr = { "TABLE" };
		List<String> tables = getTableNameList(patStr);
		for (String table : tables) {
			// System.out.println("@isTableExist table>>" + table + "<<");
			if (toUppercase)
				table = table.toUpperCase();
			if (table.equals(pTblName)) {
				// System.out.println("@isTableExist Exist=>"+pTblName);
				return true;
			}
		}
		return false;
	}

	private List<List> rs2Matrix(ResultSet rs, boolean headerOpt) {
		List<List> matrix = new ArrayList();
		try {
			ResultSetMetaData rsMeta = rs.getMetaData();
			int maxCol = rsMeta.getColumnCount(); // ���ʃZ�b�g�̗�
			List dataRow = null;
			// -------------------------------------------------------
			// Header�i�񖼓������j
			// -------------------------------------------------------
			if (headerOpt == true) {
				dataRow = new ArrayList();

				for (int j = 0; j < maxCol; j++) {
					// colSiz[j] = rsMeta.getColumnDisplaySize(j + 1);
					dataRow.add(rsMeta.getColumnName(j + 1));
				}
				matrix.add(dataRow);
			}
			// -------------------------------------------------------
			// Body�i�f�[�^�����j
			// -------------------------------------------------------
			while (rs.next()) {
				dataRow = new ArrayList();

				// �����Ӂ�ResultSet�̍ŏ��̃J�����̓[������Ȃ��ĂP
				for (int i = 0; i < maxCol; i++) {
					Object obj = rs.getObject(i + 1);
					dataRow.add(obj);
				}
				matrix.add(dataRow);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return matrix;
	}

	private Vector<Vector> rs2VectorMatrix(ResultSet rs, boolean headerOpt) {
		Vector<Vector> matrix = new Vector();
		try {
			ResultSetMetaData rsMeta = rs.getMetaData();
			int maxCol = rsMeta.getColumnCount(); // ���ʃZ�b�g�̗�
			Vector dataRow = null;
			// -------------------------------------------------------
			// Header�i�񖼓������j
			// -------------------------------------------------------
			if (headerOpt == true) {
				dataRow = new Vector();

				for (int j = 0; j < maxCol; j++) {
					// colSiz[j] = rsMeta.getColumnDisplaySize(j + 1);
					dataRow.add(rsMeta.getColumnName(j + 1));
				}
				matrix.add(dataRow);
			}
			// -------------------------------------------------------
			// Body�i�f�[�^�����j
			// -------------------------------------------------------
			while (rs.next()) {
				dataRow = new Vector();

				// �����Ӂ�ResultSet�̍ŏ��̃J�����̓[������Ȃ��ĂP
				for (int i = 0; i < maxCol; i++) {
					Object obj = rs.getObject(i + 1);
					dataRow.add(obj);
				}
				matrix.add(dataRow);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return matrix;
	}

	private List<HashMap> rs2HashMap(ResultSet rs, boolean headerOpt,
			List<String> fields) {
		List<HashMap> matrix = new ArrayList();
		try {
			ResultSetMetaData rsMeta = rs.getMetaData();
			int maxCol = rsMeta.getColumnCount(); // ���ʃZ�b�g�̗�
			if (fields == null)
				fields = new ArrayList();
			if (fields.size() < maxCol) {
				for (int j = fields.size(); j < maxCol; j++) {
					fields.add("f" + j);
				}
			}
			HashMap dataRow = null;
			// -------------------------------------------------------
			// Header�i�񖼓������j
			// -------------------------------------------------------
			if (headerOpt == true) {
				dataRow = new HashMap();
				for (int j = 0; j < maxCol; j++) {
					dataRow.put("h" + j, rsMeta.getColumnName(j + 1));
				}
				matrix.add(dataRow);
			}
			// -------------------------------------------------------
			// Body�i�f�[�^�����j
			// -------------------------------------------------------
			while (rs.next()) {
				dataRow = new HashMap();
				// �����Ӂ�ResultSet�̍ŏ��̃J�����̓[������Ȃ��ĂP
				for (int seq = 0; seq < maxCol; seq++) {
					Object obj = rs.getObject(seq + 1);
					dataRow.put(fields.get(seq), obj);
				}
				matrix.add(dataRow);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return matrix;
	}

	protected static List<List> rset2Header(ResultSet rSet) {
		List columnIdentifiers = null;
		try {
			ResultSetMetaData rMeta = rSet.getMetaData();
			int colCount = rMeta.getColumnCount();
			// Header
			columnIdentifiers = new ArrayList();
			for (int i = 1; i <= colCount; i++) {
				columnIdentifiers.add(rMeta.getColumnName(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return columnIdentifiers;
	}

	protected static List<List> rset2Matrix(ResultSet rSet) {
		List<List> matrix = null;
		try {
			ResultSetMetaData rMeta = rSet.getMetaData();
			int colCount = rMeta.getColumnCount();
			// ��������
			Object colObj;
			List rowData;
			matrix = new ArrayList();
			while (rSet.next()) {
				// if(colCount == -1){
				// //for debug(sqlite bug)
				// rMeta = rSet.getMetaData();
				// colCount = rMeta.getColumnCount();
				// }
				rowData = new ArrayList(colCount);
				for (int i = 1; i <= colCount; i++) {
					colObj = rSet.getObject(i);

					// System.out.print("<" + i + ">");
					switch (rMeta.getColumnType(i)) {
					case java.sql.Types.BIT: // -7
					case java.sql.Types.DOUBLE: // 8
					case java.sql.Types.FLOAT: // 6
					case java.sql.Types.SMALLINT: // 5
					case java.sql.Types.INTEGER: // 4
					case java.sql.Types.REAL: // 7
					case java.sql.Types.NUMERIC: // 2
					case java.sql.Types.DECIMAL: // 3
						if (colObj != null) {
							rowData.add(colObj);
						} else {
							rowData.add(0);
						}
						break;
					case java.sql.Types.DATE: // 91
						if (colObj != null) {
							rowData.add(colObj);
						} else {
							rowData.add(0);
						}
						break;
					case java.sql.Types.CHAR: // 1
					case java.sql.Types.VARCHAR: // 12
					case java.sql.Types.LONGVARCHAR: // -1
					case java.sql.Types.TIME: // 92
					case java.sql.Types.TIMESTAMP: // 93
					case java.sql.Types.BINARY: // -2
					case java.sql.Types.VARBINARY: // -3
					case java.sql.Types.LONGVARBINARY: // -4
						if (colObj != null) {
							rowData.add(colObj);
						} else {
							rowData.add(0);
						}
						break;
					default:
						if (colObj != null) {
							rowData.add(colObj.toString());
						} else {
							rowData.add("");
						}
					}
				}
				matrix.add(rowData);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return matrix;
	}

	// Batch�����s������@20130517 �����؁E�E�E�h�r�p�k�Ȃǂ����s��������
	public static String kickBatch(String batchPath) {

		// todo �o�b�`�̂���f�B���N�g���Ɉړ����Ă�����s�ł���΃x�X�g�ł͂Ȃ����Q�H
		String command = batchPath;
		// if (log != null)
		// log.info("�@�@�y�V�F��������s�z��" + command);
		String stat = new Shell().execute(command);
		return stat;
	}

	// #########################################################################
	// Query
	// #########################################################################
	// -------------------------------------------------------------------------
	// to File
	// -------------------------------------------------------------------------
	// �����������̌��ʂ������ł���Ƃ킩���Ă���ꍇ�ɃL���b�V���𗘗p�������ꍇ
	// �t�@�C���ł͂Ȃ��A�I�u�W�F�N�g�������T���L�[�ɂ��ăn�b�V���}�b�v�ɂ��Ă�������������Ȃ��i�������Ɉˑ����邪�E�E�j

	public String query2Temp(String sql, boolean dirty) {
		String md5 = MD5.getMD5(sql);
		String md5Path = System.getProperty("java.io.tmpdir") + md5;
		System.out.println("md5Path:" + md5Path);
		md5Path = "";// �L���b�V�����g��Ȃ��E�E�E���Ƃōl����
		dirty = true;// �L���b�V�����g��Ȃ��E�E�E���Ƃōl����
		if (dirty || !new File(md5Path).isFile()) {
			System.out.println("** not existed query it **");
			query2File(md5Path, sql, "\t");
		}
		return md5Path;
	}

	@Override
	public int query2File(String path, String sql) {
		return query2File(path, sql, "\t");
	}

	public int query2File(String path, String sql, String delimiter) {
		if (delimiter == null)
			delimiter = "\t";
		EzWriter writer = new EzWriter(path);
		writer.setDelimiter(delimiter);
		writer.open();
		int count = query2Writer(writer, sql);
		writer.close();
		return count;
	}

	public int query2File(String path, String sql, String delimiter,
			String[] header, Inf_ArrayCnv cnv) {
		if (delimiter == null)
			delimiter = "\t";
		EzWriter writer = new EzWriter(path, cnv);
		writer.setDelimiter(delimiter);
		writer.setHeader(header, delimiter);
		writer.open();
		int count = query2Writer(writer, sql);
		writer.close();
		return count;
	}

	// -------------------------------------------------------------------------
	// to List
	// -------------------------------------------------------------------------
	public synchronized List<String> query2List(String sql) {
		return query2List(sql, false, -1);
	}

	@Override
	public synchronized List<String> query2List(String sql, boolean headerOpt,
			int limit) {
		List<String> list = null;
		try {
			// Statement stmt = currentCon.createStatement();
			Statement stmt = createStatement(); // �X�e�[�g�����g����
			ResultSet rs = stmt.executeQuery(sql);
			list = rs2List(rs, false, limit);
			// rs.close();
			stmt.close(); // ���֘A����ResultSet�������ɃN���[�Y�����I�I
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	// -------------------------------------------------------------------------
	// query2HashMap
	// -------------------------------------------------------------------------
	// 2�t�B�[���h�̕Ԓl��1�߂̃t�B�[���h���L�[�ɂQ�߂�l�ɂ����A�n�b�V���}�b�v��Ԃ�
	public synchronized HashMap<String, String> query2HashMap(String sql) {
		return query2HashMap(sql, "");
	}

	public synchronized HashMap<String, String> query2HashMap(String sql,
			String delimiter) {
		HashMap<String, String> hashMap = null;
		try {
			Statement stmt = createStatement(); // �X�e�[�g�����g����
			ResultSet rs = stmt.executeQuery(sql);
			hashMap = rs2HashMap(rs, delimiter);
			stmt.close(); // ���֘A����ResultSet�������ɃN���[�Y�����I�I
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hashMap;
	}

	// -------------------------------------------------------------------------
	// query2MappedList�@�w�b�_�[�ƃ}�g���b�N�X��2��ނ̕Ԓl���n�b�V���}�b�v�ɂ̂��ĕԂ�
	// -------------------------------------------------------------------------
	// XXX grid�������ɂ��ăN�G���[���񂷕��@�͂ǂ����낤�H
	// XXX �N���[�W���������ɂ������A�����낤�E�E�E�E
	public synchronized HashMap<String, List<List>> query2MappedList(
			String sql) {
		HashMap<String, List<List>> map = new HashMap();
		List<List> matrix = null;
		List<List> headers = null;
		try {
			Statement stmt = createStatement(); // �X�e�[�g�����g����
			// �g�����U�N�V�����J�n
			if (getDriverClassName().contains("sqlite")) {
				// sqlite�̃p�t�H�[�}���X���グ�����E�E�Edefault��2000
				stmt.executeUpdate("PRAGMA cache_size = 8000");
			}
			ResultSet rSet = stmt.executeQuery(sql); // �N�G���[���s
			// System.out.println("## queryTable ##");
			// model = new DefaultTableModelMod(rSet);
			headers = rset2Header(rSet);// ���Ԃ�ς����瓮�����E�E�E��������Ƃɂł��Ȃ��E�E�Ȃ����H
			matrix = rset2Matrix(rSet);
			map.put("matrix", matrix);
			map.put("headers", headers);
			// ProtTModel��AbstractTableModel��Extend���Ă��āE�E
			// getColumnClass�̓f�[�^���i�[���Ă���Vector���̃N���X��Ԃ�
			// ����āE�E����ɑΉ�����
			stmt.close();
		} catch (Exception e) {
			eMsg = e.toString();
			e.printStackTrace();
		}
		close();
		return map;
	}

	// -------------------------------------------------------------------------
	// to Matrix
	// -------------------------------------------------------------------------
	@Override
	public List<List> query2Matrix(String sql) {
		return query2Matrix(sql, false, -1);
	}

	@Override
	public List<List> query2Matrix(String sql, boolean headerOpt, int limit) {
		// System.out.println("@query2Matrix sql:"+sql);
		List<List> matrix = null;
		try {
			Statement stmt = createStatement(); // �X�e�[�g�����g����
			ResultSet rs = stmt.executeQuery(sql); // �N�G���[���s
			matrix = rs2Matrix(rs, headerOpt);
			stmt.close(); // ���֘A����ResultSet�������ɃN���[�Y�����I�I
		} catch (Exception e) {
			System.out.println("## DB Error!!@query2Matrix\n" + e.toString());
		}
		return matrix;
	}

	public Vector<Vector> query2VectorMatrix(String sql) {
		return query2VectorMatrix(sql, false, -1);
	}

	public Vector<Vector> query2VectorMatrix(String sql, boolean headerOpt,
			int limit) {
		Vector<Vector> matrix = null;
		try {
			Statement stmt = createStatement(); // �X�e�[�g�����g����
			ResultSet rs = stmt.executeQuery(sql); // �N�G���[���s
			matrix = rs2VectorMatrix(rs, headerOpt);
			stmt.close(); // ���֘A����ResultSet�������ɃN���[�Y�����I�I
		} catch (Exception e) {
			System.out.println("## DB Error!!@query2Matrix\n" + e.toString());
			//			System.out.println("query2VectorMatrix sql:" + sql);
		}
		return matrix;
	}

	private List string2List(String value) {
		String[] array = value.split(",");
		return java.util.Arrays.asList(array);
	}

	public List<HashMap> query2HashMapList(String sql, boolean headerOpt,
			int limit, String fields) {
		return query2HashMapList(sql, headerOpt, limit, string2List(fields));
	}

	private List<HashMap> query2HashMapList(String sql, boolean headerOpt,
			int limit, List<String> fields) {
		List<HashMap> mapList = null;
		try {
			Statement stmt = createStatement(); // �X�e�[�g�����g����
			ResultSet rs = stmt.executeQuery(sql); // �N�G���[���s
			mapList = rs2HashMap(rs, headerOpt, fields);
			stmt.close(); // ���֘A����ResultSet�������ɃN���[�Y�����I�I
		} catch (Exception e) {
			System.out.println("## DB Error!!@query2Matrix\n" + e.toString());
		}
		return mapList;
	}

	@Override
	public List<List> prepQuery2Matrix(String hash, String[] array) {
		return prepQuery2Matrix(hash, -1, array);
	}

	@Override
	public List<List> prepQuery2Matrix(String hash, int limit, String[] array) {
		List<List> matrix = null;
		if (prepMap == null)
			prepMap = new HashMap();
		PreparedStatement prepStmt = prepMap.get(hash);
		if (prepStmt == null) {
			log.fatal("#ERROR @executePrepQueryMatrix prepStmt==null");
			return null;
		}
		try {
			for (int i = 0; i < array.length; i++) {
				// System.out.println("array[i]>>>>"+array[i]);
				prepStmt.setString(i + 1, array[i]);
			}
			try {
				ResultSet rs = prepStmt.executeQuery();
				matrix = rs2Matrix(rs, false, limit);
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			if (log != null) {
				log.fatal("#ERROR @JDBC.executeUpdate:");
				log.fatal("ERROR:" + e.toString());
				showErrorDialog(e.toString());

			}
			e.printStackTrace();
			return null;
		}
		return matrix;
	}

	// -------------------------------------------------------------------------
	// �N�G���̌��ʂ��P��̒l�ł���ꍇ�Ɏg�p����
	// -------------------------------------------------------------------------
	@Override
	public String queryOne(String sql) {
		List<String> list = query2List(sql, false, 1);
		if (list == null) {
			return null;
		} else {
			if (list.size() < 1)
				return null;
			return list.get(0);
		}
	}

	// #########################################################################
	// Update
	// #########################################################################
	@Override
	public synchronized boolean prepUpdate(String hash, String[] array) {
		return prepUpdate(hash, array, array.length);
	}

	@Override
	public synchronized boolean prepUpdate(String hash, String[] array,
			int max) {
		if (prepMap == null)
			prepMap = new HashMap();
		PreparedStatement prepStmt = prepMap.get(hash);
		if (prepStmt == null) {
			log.fatal("#ERROR @prepUpdate prepStmt==null");
			return false;
		}
		try {
			if (max > array.length)
				max = array.length;
			for (int i = 0; i < max; i++) {
				// System.out.println("prepStmt.setString array["+i+"]:"+array[i]);
				prepStmt.setString(i + 1, array[i]);
			}
			int i = prepStmt.executeUpdate(); // run the query
			if (i == -1) {
				System.out.println("Error! @executeUpdate:");
				prepStmt.close();
				return false;
			}
		} catch (SQLException e) {
			if (log != null) {
				log.fatal("#ERROR @JDBC.executeUpdate:");
				log.fatal("ERROR:" + e.toString());
				showErrorDialog(e.toString());

			}
			e.printStackTrace();
			System.exit(999);
			return false;
		}
		return true;
	}

	// #########################################################################
	// writer
	// #########################################################################
	// �T���₷���悤�ɁA�ʖ�������
	public int query2Stream(Inf_oClosure writer, String sql) {
		return query2Writer(writer, sql);
	}

	@Override
	public int query2Writer(Inf_oClosure writer, String sql) {
		int writeCount = 0;
		try {
			Statement stat = currentCon.createStatement();
			ResultSet rs = stat.executeQuery(sql);
			writeCount = rs2Stream(writer, rs);
			rs.close();
		} catch (SQLException e) {
			if (log != null) {
				log.fatal("#ERROR @JDBC.query2Writer");
				log.fatal("ERROR:" + e.toString());
				showErrorDialog(e.toString());
			}
			e.printStackTrace();
		}
		return writeCount;
	}

	public int query2Closure(Inf_Closure closure, String sql) {
		int writeCount = 0;
		// System.out.println("<<executeQuery2Writer>> sql:" + sql);
		try {
			Statement stat = currentCon.createStatement();
			ResultSet rs = stat.executeQuery(sql);
			writeCount = rs2Closure(closure, rs);
			rs.close();
		} catch (SQLException e) {
			if (log != null) {
				log.fatal("#ERROR @JDBC.query2Writer");
				log.fatal("ERROR:" + e.toString());
				showErrorDialog(e.toString());

			}

			e.printStackTrace();
		}
		return writeCount;
	}

	// �R���o�[�^�[����
	public int query2Writer(String format, Inf_oClosure writer, String sql) {
		return query2Writer(format, writer, sql, null);
	}

	public int query2Writer(String format, Inf_oClosure writer, String sql,
			Inf_ListArrayConverter cnv) {
		int writeCount = 0;
		// System.out.println("<<executeQuery2Writer>> sql:" + sql);
		try {
			Statement stat = currentCon.createStatement();
			ResultSet rs = stat.executeQuery(sql);
			writeCount = rs2Stream(format, writer, rs, cnv);
			rs.close();
		} catch (SQLException e) {
			if (log != null) {
				log.fatal("#ERROR @JDBC.query2Writer");
				log.fatal("ERROR:" + e.toString());
				showErrorDialog(e.toString());
			}
			e.printStackTrace();
		}
		return writeCount;
	}

	// XXX �{���͉e����^����ꂽ�f�[�^�̌������Ԃ�̂��]�܂���
	@Override
	public synchronized boolean executeUpdate(String sql) {
		List<String> sqls = new ArrayList();
		sqls.add(sql);
		int result = executeUpdate(sqls);
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	// ------------------------------------------------------------------------
	// �߂�l:
	// (1) SQL �f�[�^���쌾�� (DML) ���̏ꍇ�͍s���A(2) �����Ԃ��Ȃ� SQL ���̏ꍇ�� 0
	// ------------------------------------------------------------------------
	public synchronized int executeUpdate(List<String> sqls) {
		int result = 0;
		if (log != null) {
			log.info("executeUpdate sql:" + sqls);
		}
		//		System.out.println("executeUpdate sqls:"+sqls.get(0));
		try {
			if (sqls.size() > 1)
				currentCon.setAutoCommit(false);
			Statement stmt = createStatement(); // statements
			for (String sql : sqls) {
				sql = sql.trim();
				if (!sql.equals("")) {
					//					System.out.println("@executeUpdate sql:" + sql);
					int rtn = stmt.executeUpdate(sql); // run the query
					if (rtn == -1) {
						// �߂�l:
						// INSERT ���AUPDATE ���ADELETE ���̏ꍇ�͍s���B�����Ԃ��Ȃ� SQL ���̏ꍇ�� 0
						System.out.println("@executeUpdate Error?! rtn:" + rtn);
						System.out.println(" sql=>" + sqls);
						stmt.close();
						return -1;
					}
					result++;
				}
			}
			if (sqls.size() > 1)
				currentCon.commit();
			stmt.close();
		} catch (SQLException e) {
			try {
				if (sqls.size() > 1)
					currentCon.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			if (log != null) {
				log.fatal("#ERROR @JDBC.executeUpdate\nsql:" + sqls);
				log.fatal("ERROR:" + e.toString());
				showErrorDialog(e.toString());
			}
			System.out.println("#ERROR @JDBC.executeUpdate\nsql:" + sqls);

			e.printStackTrace();
			System.exit(999);
			return -1;
		}
		return result;
	}

}
// System.out.println("#JURL(meta):" + dbMeta.getURL());
// System.out.println("#Catalog :" + conn.getCatalog());
// ******************************************************************************
// �r�t�m�̊֘A
// �� http://java.sun.com/j2se/1.3/ja/docs/ja/guide/jdbc/getstart/bridge.doc.html
// �T���v���̏���
// �� http://support.microsoft.com/default.aspx?scid=kb;en-us;191932
// �֘A�y�[�W��T��
// ��
// http://search.microsoft.com/search/results.aspx?view=msdn&st=b&na=83&qu=JDBC+ODBC&s=1
// ******************************************************************************
// �Q�l��
// http://www.mars.dti.ne.jp/~torao/program/jdbc/program.html
// http://support.microsoft.com/default.aspx?scid=kb;ja;313100
// ******************************************************************************
// MySQL�̏ꍇ�́A�ujdbc:mysql://�z�X�g��/�f�[�^�x�[�X��?useUnicode=true&characterEncoding=SJIS�v
// Class.forName("com.mysql.jdbc.Driver");
// ���z�X�g���Ȃ�ȗ��\
// ******************************************************************************
// Oracle�̏ꍇ�́A
// String url = "jdbc:oracle:thin:@localhost:1521:ORCL";
// Connection con = DriverManager.getConnection(url,"system","manager");
// url�̎w��́ujdbc:oracle:thin:@�z�X�g��:�|�[�g�ԍ�:SID���ʎq�v
// ******************************************************************************
// public synchronized List<List> query2Matrix(String sql) {
// return executeQueryMatrix(sql, false, -1);
// }
// public synchronized List<List> query2Matrix(String sql, boolean
// headerOpt, int limit) {
// List<List> matrix = null;
// try {
// Statement stmt = createStatement(); // �X�e�[�g�����g����
// ResultSet rs = stmt.executeQuery(sql); // �N�G���[���s
// matrix = rs2Matrix(rs, headerOpt);
// stmt.close(); // ���֘A����ResultSet�������ɃN���[�Y�����I�I
// } catch (Exception e) {
// System.out.println("## DB Error!!@query2Matrix\n" + e.toString());
// }
// return matrix;
// }
// public List<List> executeQueryMatrix(String sql, boolean headerOpt, int
// limit) {
// List<List> matrix = null;
// try {
// Statement stat = currentCon.createStatement();
// ResultSet rs = stat.executeQuery(sql);
// matrix = rs2Matrix(rs, headerOpt, limit);
// rs.close();
// } catch (SQLException e) {
// e.printStackTrace();
// }
// return matrix;
// }
// private synchronized List<String> query2List(String sql, boolean
// headerOpt) {
// List list = null;
// try {
// Statement stmt = createStatement(); // �X�e�[�g�����g����
// ResultSet rs = stmt.executeQuery(sql); // �N�G���[���s
// list = rs2List(rs, headerOpt);
// stmt.close(); // ���֘A����ResultSet�������ɃN���[�Y�����I�I
// } catch (Exception e) {
// System.out.println("## DB Error!!@query2Matrix\n" + e.toString());
// }
// return list;
// }
// private synchronized List<String> query2List(String sql, boolean
// headerOpt) {
// List list = null;
// try {
// Statement stmt = createStatement(); // �X�e�[�g�����g����
// ResultSet rs = stmt.executeQuery(sql); // �N�G���[���s
// list = rs2List(rs, headerOpt);
// stmt.close(); // ���֘A����ResultSet�������ɃN���[�Y�����I�I
// } catch (Exception e) {
// System.out.println("## DB Error!!@query2Matrix\n" + e.toString());
// }
// return list;
// }
// private synchronized List<String> query2List(String sql, boolean
// headerOpt) {
// List list = null;
// try {
// Statement stmt = createStatement(); // �X�e�[�g�����g����
// ResultSet rs = stmt.executeQuery(sql); // �N�G���[���s
// list = rs2List(rs, headerOpt);
// stmt.close(); // ���֘A����ResultSet�������ɃN���[�Y�����I�I
// } catch (Exception e) {
// System.out.println("## DB Error!!@query2Matrix\n" + e.toString());
// }
// return list;
// }
// private synchronized List<String> query2List(String sql, boolean
// headerOpt) {
// List list = null;
// try {
// Statement stmt = createStatement(); // �X�e�[�g�����g����
// ResultSet rs = stmt.executeQuery(sql); // �N�G���[���s
// list = rs2List(rs, headerOpt);
// stmt.close(); // ���֘A����ResultSet�������ɃN���[�Y�����I�I
// } catch (Exception e) {
// System.out.println("## DB Error!!@query2Matrix\n" + e.toString());
// }
// return list;
// }
//
// private List<List> rs2List(ResultSet rs, boolean headerOpt) {
// return rs2List(rs, headerOpt,-1);
// }
// // 20130326
// private List<String> rs2List(ResultSet rs, boolean headerOpt) {
// List<String> list = new ArrayList();
// String delimiter = "\t";
// try {
// ResultSetMetaData rsMeta = rs.getMetaData();
// int maxCol = rsMeta.getColumnCount(); // ���ʃZ�b�g�̗�
// StringBuffer dataRow = null;
// // -------------------------------------------------------
// // Header�i�񖼓������j
// // -------------------------------------------------------
// if (headerOpt == true) {
// dataRow = new StringBuffer();
// for (int j = 0; j < maxCol; j++) {
// dataRow.append(rsMeta.getColumnName(j + 1));
// dataRow.append(delimiter);
// }
// dataRow.deleteCharAt(dataRow.length() - 1);
// list.add(dataRow.toString());
// }
// // -------------------------------------------------------
// // Body�i�f�[�^�����j
// // -------------------------------------------------------
// while (rs.next()) {
// dataRow = new StringBuffer();
// // �����Ӂ�ResultSet�̍ŏ��̃J�����̓[������Ȃ��ĂP
// for (int i = 0; i < maxCol; i++) {
// Object obj = rs.getObject(i + 1);
// dataRow.append(obj);
// dataRow.append(delimiter);
// }
// dataRow.deleteCharAt(dataRow.length() - 1);
// list.add(dataRow.toString());
// }
//
// } catch (SQLException e) {
// e.printStackTrace();
// }
// return list;
// }
// -------------------------------------------------------------------------
// �N�G���̌��ʂ��P��̒l�ł���Ƃ�
// -------------------------------------------------------------------------
// public String query2Str(String sql) {
// System.out.println("0310@query2Str of ##JDBC## ???");
// List list = executeQueryMatrix(sql);
// if (list != null && list.size() >= 1) {
// List cell = (List) list.get(0);
// if (cell != null) {
// return cell.get(0).toString();
// }
// }
// return null;
// }
