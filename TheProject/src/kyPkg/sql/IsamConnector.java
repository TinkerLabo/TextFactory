package kyPkg.sql;

import java.io.File;
import java.util.List;
import kyPkg.uFile.FileUtil;

//-------+---------+---------+---------+---------+---------+---------+---------+
/**
 * IsamConnection
 * 
 * @author Ken Yuasa
 * @version 1.00 05/09/06
 * @since 1.3 Copyright 2004 Ken Yuasa. All rights reserved.
 */
// -------+---------+---------+---------+---------+---------+---------+---------+
public class IsamConnector implements Connector {
	private String dbDir = "C:/@QPR/hsqldb/TEMP";
	private String jURL = "JDBC:ODBC:DRIVER={Microsoft Text Driver (*.txt; *.csv)};"
		+ "DefaultDir="
		+ dbDir
		+ ";"
		+ "DriverId=27;FIL=text;MaxBufferSize=2048;PageTimeout=5;";
	private String user = "sa"; // ユーザ名
	private String pass = ""; // パスワード
	private JDBC jdbcIns = null;
	public IsamConnector() {
		getConnection();
	}
	public IsamConnector(String jURL, String user, String pass) {
		this.jURL = jURL;
		this.user = user;
		this.pass = pass;
		getConnection();
	}

	public IsamConnector(String dbDir) {
		setDbDir(dbDir);
		getConnection();
	}

	public String getJURL() {
		return jURL;
	}
	@Override
	public List<List> query2Matrix(String sql) {
		getConnection();
		if (jdbcIns != null) {
			return jdbcIns.query2Matrix(sql);
		}
		return null;
	}
	@Override
	public String query2Str(String sql) {
		getConnection();
		if (jdbcIns != null) {
			return jdbcIns.queryOne(sql);
		}
		return null;
	}
	@Override
	public JDBC getConnection() {
		if (jdbcIns == null) {
			jdbcIns = connectToLocalDB();
		}
		return jdbcIns;
	}
	@Override
	public void close() {
		if (jdbcIns != null)
			jdbcIns.close();
	}
	// -------------------------------------------------------------------------
	// Privates
	// -------------------------------------------------------------------------
	private void setDbDir(String dbDir) {
		close();
		jdbcIns = null;
		File tmp = new File(dbDir);
		if (tmp.exists() == false)
			FileUtil.makeParents(dbDir);
		this.dbDir = dbDir;
		this.jURL = "JDBC:ODBC:DRIVER={Microsoft Text Driver (*.txt; *.csv)};"
				+ "DefaultDir=" + dbDir + ";"
				+ "DriverId=27;FIL=text;MaxBufferSize=2048;PageTimeout=5;";
//		System.out.println("@setDbDir=>"+jURL);
	}

	// -------------------------------------------------------------------------
	// Connect to Host ≪DBの再接続≫
	// -------------------------------------------------------------------------
	private JDBC connectToLocalDB() {
		JDBC jdbcIns = new JDBC(jURL, user, pass);
		if (jdbcIns.getConnection() == null) {
			jdbcIns = null;
		}
		return jdbcIns;
	}

	public String getDbDir() {
		return dbDir;
	}
}
