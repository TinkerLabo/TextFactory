package kyPkg.sql;

import kyPkg.atoms.AtomDB;

//JDBC�N���X���o�b�`�Ŏg�p���邽�߂̃��[�e�B���e�B�N���X
//(��JDBC�N���X���F�X�ȃN���X�Ɉˑ����Ȃ��悤�ɂ���ׂɕʃN���X�ɂ���)
public class SQLCMD {
	private static final String JURL = "jURL";
	private static final String USER = "user";
	private static final String PASS = "pass";
	private static final String PATH = "path";
	private static final String SQL = "sql";
	private static final String DELIMITER = "delimiter";
	private static final String ERR_SQLCMD = "ERROR@SQLCMD";
	private static final String TAB = "\t";
	private String message = "";
	private String jUrl;
	private String user;
	private String pass;
	private String comment1;
	private String comment2;
	private String outPath;
	private String sql;
	private String delimiter;
	private kyPkg.util.ArgsMap argMap;

	public String getjUrl() {
		return jUrl;
	}

	public void setjUrl(String jUrl) {
		this.jUrl = jUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getComment1() {
		return comment1;
	}

	public void setComment1(String comment1) {
		this.comment1 = comment1;
	}

	public String getComment2() {
		return comment2;
	}

	public void setComment2(String comment2) {
		this.comment2 = comment2;
	}

	public String getOutPath() {
		return outPath;
	}

	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}


	public String getMessage() {
		return message;
	}

	//-------------------------------------------------------------------------
	// �R���X�g���N�^ (��������YML�A�������͏o�͐�p�X)
	//-------------------------------------------------------------------------
	public SQLCMD(String[] args) {
		super();
		parse(args);
	}

	private void parse(String[] args) {
		if (args.length >= 1) {
			argMap = new kyPkg.util.ArgsMap(args[0]);
			if (args.length >= 2)
				argMap.put(PATH, args[1]); // ��Q�������w�肳��Ă�����A�o�͐�path�Ƃ��Ĉ���
			execute();
		}
	}

	// ------------------------------------------------------------------------
	// execute
	// ------------------------------------------------------------------------
	public void execute() {
		jUrl = argMap.get(JURL);
		user = argMap.get(USER);
		pass = argMap.get(PASS);
//		System.out.println("jUrl:" + jUrl);
//		System.out.println("user:" + user);
//		System.out.println("pass:" + pass);
		comment1 = argMap.get(AtomDB.COMMENT1);
		comment2 = argMap.get(AtomDB.COMMENT2);

		JDBC jdbcIns = new JDBC(jUrl, user, pass);
		// ------------------------------------------------------------------------
		// XXX ���s�O�ɏo�͐悪���݂��Ă�����A���̃t�@�C�����폜����B?
		// ------------------------------------------------------------------------
		int count = -1;
		if (jdbcIns != null) {
			outPath = argMap.get(PATH);
			sql = argMap.get(SQL);
			delimiter = argMap.get(DELIMITER, TAB);
			System.out.println("path:" + outPath);
			System.out.println("sql:" + sql);
			System.out.println("delimiter:" + delimiter);
			count = jdbcIns.query2File(outPath, sql, delimiter);
			message += " �o�͐�:" + argMap.get(PATH) + "\n";
			message += " �J�E���g:" + count + "";
		} else {
			System.out.println(ERR_SQLCMD + "Can't Connect to DB");
		}
	}

	// ------------------------------------------------------------------------
	// XXX �f�t�H���g��jurl�A���[�U�[�A�p�X���[�h�����ϐ��ɂ��Ă��ǂ���������Ȃ�
	// XXX �p�����[�^�[�̃`�F�b�N�iverify�j�����������ǂ������m��Ȃ�
	// ------------------------------------------------------------------------
	// for Batch�N���iisql=>osql=>sqlcmd���g���Ȃ��ꍇ�Ȃǂ̑�ցE�E�E���ȁj
	// String jURL, String user, String pass�ɂ���yaml�Ȃǂɓ����H
	// ------------------------------------------------------------------------
	// ���O�����璼�ڋN������ꍇ��z�肵�Ă���̂�main�����������Ă͂����Ȃ�
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("SQLCMD Elapse");
		elapse.start();

		SQLCMD ins = new SQLCMD(args);
		ins.execute();

		String comment = "";
		elapse.setComment(comment);
		elapse.stop();
	}

}
