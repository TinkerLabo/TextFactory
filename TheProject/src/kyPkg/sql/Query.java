package kyPkg.sql;

import java.util.HashMap;

//JDBC�N���X���o�b�`�Ŏg�p���邽�߂̃��[�e�B���e�B�N���X
public class Query {
	private static final String SQL = "sql";
	private static final String JURL = "jURL";
	private static final String USER = "user";
	private static final String PSWD = "pass";
	private static HashMap<String, String> map;
	private static JDBC jdbcIns;

	// ------------------------------------------------------------------------
	// �R���X�g���N�^
	// ------------------------------------------------------------------------
	public Query() {
		super();
		String jUrl = ServerConnecter.getDEF_JURL() ;
		String user = ServerConnecter.getDEF_USER();
		String pswd = ServerConnecter.getDEF_PASS();
		map = new HashMap();
		map.put(JURL,jUrl);
		map.put(USER, user);
		map.put(PSWD, pswd);
		map.put(SQL, "");
		init();
	}

	// ------------------------------------------------------------------------
	// �������i�ڑ��j
	// ------------------------------------------------------------------------
	public void init() {
		jdbcIns = new JDBC(map.get(JURL), map.get(USER), map.get(PSWD));
	};

	// ------------------------------------------------------------------------
	// �I������
	// ------------------------------------------------------------------------
	public void fin() {
		jdbcIns.close();
	};

	public void executeUpdate(String sql) {
		if (jdbcIns != null) {
			jdbcIns.executeUpdate(sql);
		} else {
			System.out.println("Can't Connect to DB");
		}
	}

	// ------------------------------------------------------------------------
	//
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("SQLCMD Elapse");
		elapse.start();
		test20150316();
		elapse.stop();
	}

	// ------------------------------------------------------------------------
	// JICFS�Ŗ��̕ϊ������֘A
	// ------------------------------------------------------------------------
	public static void test20150316() {
		Query query = new Query();
		query.executeUpdate("update ITEM Set xc2 = '�\�m�^���j�@�}�����f�C�L���������k�K�[�P�T�O�f�@�@' where xc2 = '' and   xa1 = '8710486118251'");
		query.fin();
	}

}
