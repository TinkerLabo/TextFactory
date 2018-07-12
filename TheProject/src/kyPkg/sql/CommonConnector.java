package kyPkg.sql;

import java.io.File;
import java.util.List;

import kyPkg.etc.AliasRes;
import kyPkg.uFile.FileUtil;

//-------+---------+---------+---------+---------+---------+---------+---------+
/**
 * IsamConnection
 * 
 * @author Ken Yuasa
 * @version 1.00 08/10/20
 * @since 1.5 Copyright 2008 Ken Yuasa. All rights reserved.
 */
// -------+---------+---------+---------+---------+---------+---------+---------+
public class CommonConnector implements Connector {
	private String dbDir = "";
	private String jURL = "";
	private String user = "";	// ユーザ名
	private String pass = "";	// パスワード
	private JDBC jdbcIns = null;
	public CommonConnector(String jURL, String user, String pass) {
		this.jURL = jURL;
		this.user = user;
		this.pass = pass;
		getConnection();
	}
	
	public CommonConnector(AliasRes aliasRes) {
		this.jURL = aliasRes.getJURL();
		this.user = aliasRes.getUser();
		this.pass = aliasRes.getPass();
		getConnection();
	}

	public CommonConnector(String dbDir) {
		setDbDir(dbDir);
		getConnection();
	}

	public String getJURL() {
		return jURL;
	}
	@Override
	public List query2Matrix(String sql) {
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
		jURL = jURL.trim();
		if (jURL.equals("")) return null;
		if (jdbcIns == null) {
			jdbcIns = new JDBC(jURL, user, pass);
			if (jdbcIns.getConnection() == null) {
				jdbcIns = null;
			}
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
		close();//念のためクローズ・・?!
		jdbcIns = null;
		File tmp = new File(dbDir);
		if (tmp.exists() == false)
			FileUtil.makeParents(dbDir);
		this.dbDir = dbDir;
		String aliasPath = "Z:/s2/rx/Idxc/Enquete/属性・ライフスタイル編/2008/alias.txt";
		aliasPath = dbDir + "/alias.txt";
		File alias = new File(aliasPath);
		if (alias.exists() == true){
			//当該ディレクトリにalias.txtがあればその内容よりコネクト文を作成する
			kyPkg.rez.HashRes hRes = new kyPkg.rez.HashRes(aliasPath, "\t");
			System.out.println("connect=>"+hRes.getValue("connect"));
			System.out.println("table=>"+hRes.getValue("table"));
			System.out.println("field=>"+hRes.getValue("field"));
			System.out.println("key=>"+hRes.getValue("key"));
			System.out.println("Cond=>"+hRes.getValue("cond"));

//			connect	DRIVER={SQL Server};SERVER=KS1S003;UID=sa;PWD=;DATABASE=qprdb;Trusted_Connection=true
//			table	TBL_NQMON2008
//			field	CH_DAT
//			key	CH_ID
//			Cond	

			this.jURL = hRes.getValue("connect");
			this.user = "sa";	// ユーザ名
			this.pass = ""; 	// パスワード

		}else{
			this.jURL = "JDBC:ODBC:DRIVER={Microsoft Text Driver (*.txt; *.csv)};"
				+ "DefaultDir=" + dbDir + ";"
				+ "DriverId=27;FIL=text;MaxBufferSize=2048;PageTimeout=5;";
			this.user = "sa";	// ユーザ名
			this.pass = ""; 	// パスワード
		}
	}

	public String getDbDir() {
		return dbDir;
	}
}
