package kyPkg.sql;

import static kyPkg.util.Joint.join;
import kyPkg.uFile.File49ers;

//public  class JDBC_TEXDB extends JDBC implements Inf_TexDb {
public abstract class JDBC_TEXDB extends JDBC implements Inf_TexDb {
	// public static Log log =
	// LogFactory.getLog("kyPkg.sql.TexTableUtil.class");
	// �f�[�^�x�[�X���쐬����ꏊ
	protected String dbDir = "";
	protected String dbPath = "";

	// �e�L�X�g�e�[�u����z�u����ꏊ�i �ȒP�̂��ߌŒ薼"data"�Ƃ����j
	protected String dataDir = "";

	@Override
	public String getDataDir() {
		return dataDir;
	}

	@Override
	public String getDbDir() {
		return dbDir;
	}

	public String getDbPath() {
		return dbPath;
	}

	// public JDBC_TEXDB() {
	// super();
	// }
	// public JDBC_TEXDB(String jURL, String user, String password) {
	// super(jURL, user, password);
	// }

	// innerjoin�@��union���g�����R�s�[

	// -------------------------------------------------------------------------
	// copyTable(dbObj,"CLONE","qpr_monitor_base");
	// -------------------------------------------------------------------------
	@Override
	public boolean copyTable(String dstTable, String srcTable) {
		// �R�s�[�悪���݂���Ώ����Ă����@
		if (isExist(dstTable))
			dropTable(dstTable);
		if (isExist(srcTable)) {
			if (log != null)
				log.info("�@�@" + srcTable + "���R�s�[���āA" + dstTable + "�Ƃ��ĕۑ�");
			String sql = "";
			// �����ӁI�f�[�^�x�[�X�̎�ނɂ���Ăr�p�k���قȂ�
			if (getKind().equals("SQLITE")) {
				sql = "CREATE TABLE " + dstTable + " AS SELECT * FROM "
						+ srcTable;
			} else {
				sql = "SELECT * INTO " + dstTable + " FROM " + srcTable;
			}
			return executeUpdate(sql);
		} else {
			System.out.println("�R�s�[���ƂȂ�e�[�u�������݂��܂���" + srcTable);
			return false;
		}
	}

	// -------------------------------------------------------------------------
	// <<memo>>
	// �h�c�Ń}�b�`���O�����ꍇ�}�X�^���̃t�B�[���h�i�J�n���t�j�����Ă���
	// ����ȊO�́i�}�b�`���O���Ȃ������j�ꍇ�̃��R�[�h��union�@all�ō����o�͂���
	// -------------------------------------------------------------------------
	// �ꂵ���ꂾ�E�E�E�E2011/02/23
	// �ȉ��̂悤�Ȃr�p�k�����s�����������̂���
	// sql = "UPDATE qpr_monitor_base "
	// + "SET qpr_monitor_base.fld3 = STARTCONV.fld1 "
	// + "INNER JOIN STARTCONV ON qpr_monitor_base.fld0 = STARTCONV.fld0 ";
	// ==> You can't. SQLite doesn't support JOINs in UPDATE statements.
	// �Ƃ������Ƃŋ��������E�E�EUNIONall���g�����`���Ń��b�c�P�܂����B@2011/02/23
	// -------------------------------------------------------------------------
	// �}�X�^�[�t�@�C���𗘗p���čX�V�i�����j�������ʂ�ʃe�[�u���ɏo�͂���@
	// �p�����[�^�@
	// String dstTable �o�͐�i�e�[�u�����j
	// String master �ϊ��p�}�X�^�i�e�[�u�����j
	// String tran �ϊ����i�e�[�u�����j
	// int tCol ����ւ���t�B�[���h�ʒu�i0~n�j
	// int mCol �}�X�^�[���t�B�[���h�ʒu�i0~n�j
	// ���ȒP�̈׃L�[��0�J�����߂Ƃ����i�K�v������Ή����\�j
	// -------------------------------------------------------------------------
	@Override
	public boolean covWithMaster(String dstTable, String master, String tran,
			int tCol, int mCol) {
		String tFlds[] = getFieldsArray(tran);
		String asFld = tFlds[tCol];
		if (tFlds != null && tFlds.length > 0) {
			for (int i = 0; i < tFlds.length; i++) {
				tFlds[i] = tran + "." + tFlds[i];
			}
		} else {
			if (log != null)
				log.info("�@�@�@�g�����U�N�V�����t�B�[���h����Ȃ̂ŏ����𒆒f���܂���");
			return false;
		}
		String mFlds[] = getFieldsArray(master);
		if (mFlds != null && mFlds.length > 0) {
			for (int i = 0; i < mFlds.length; i++) {
				mFlds[i] = master + "." + mFlds[i];
			}
		} else {
			if (log != null)
				log.info("�@�@�@�R���o�[�^�t�B�[���h����Ȃ̂ŏ����𒆒f���܂���");
			return false;
		}
		String srcFld = join(tFlds, ",");
		String tKey = tFlds[0];
		String mKey = mFlds[0];
		// �ύX�t�B�[���h�ɂ͌��̖��O��t���Ă�����E�E�E
		tFlds[tCol] = mFlds[mCol] + " AS " + asFld;
		String cnvFld = join(tFlds, ",");
		String sql1 = "SELECT " + cnvFld + " FROM " + tran + " INNER JOIN "
				+ master + " ON " + tKey + " = " + mKey + " ";
		String sql2 = "SELECT " + srcFld + " FROM " + tran + " LEFT JOIN "
				+ master + " ON " + tKey + " = " + mKey + " WHERE (" + mKey
				+ " IS NULL)";
		// �R�s�[�悪���݂���Ώ����Ă����@
		if (isExist(dstTable))
			dropTable(dstTable);
		String sql = "";
		// �m�F�o�͂����Ƃ��Ɍ��₷���悤�ɉ��s��X�y�[�X������Ă���܂��B
		String subQuery = "\n   (" + sql1 + " \n   union all \n   " + sql2
				+ "\n   )";
		// �����ӁI�f�[�^�x�[�X�̎�ނɂ���Ăr�p�k���قȂ�
		if (getKind().equals("SQLITE")) {
			sql = "CREATE TABLE " + dstTable + " AS SELECT * FROM " + subQuery;
		} else {
			sql = "SELECT * INTO " + dstTable + " FROM " + subQuery;
		}
		// System.out.println("subQuery:\n" + subQuery);
		// System.out.println("sql\n" + sql);
		if (log != null)
			log.info("�@�@�@�}�X�^�[�t�@�C���𗘗p���čX�V�i�����j�������ʂ�ʃe�[�u���ɏo�� \n\n" + sql + "\n");
		return executeUpdate(sql);
	}

	// // ��肠������������E�E�E��������o�[�W����
	// private boolean convertIt() {
	// // You can't. SQLite doesn't support JOINs in UPDATE statements. ���ƁH�I
	// // sqlite��UPDATE��INNER���g�����N�G���[�ɑΉ����Ă��Ȃ��̂ŁA������߂�
	// // �i�i�d�s�ł͉��̃o�[�W�����œ������m�F�ς݁A�l�x�r�p�k�̏ꍇ��i�œ����悤�������m�F�j
	// // ���̂��������������邳�E�E�E�ڂ��i�܁j
	// log.info("�R���o�[�^�ɂ����t�ϊ����s��");
	// String sql = "";
	// if (!getKind().equals("SQLITE")) {
	// sql = "UPDATE qpr_monitor_base "
	// + "SET qpr_monitor_base.fld3 = STARTCONV.fld1 "
	// + "INNER JOIN STARTCONV ON qpr_monitor_base.fld0 = STARTCONV.fld0 ";
	// } else {
	// sql = "UPDATE qpr_monitor_base "
	// + "INNER JOIN STARTCONV ON qpr_monitor_base.fld0 = STARTCONV.fld0 "
	// + "SET qpr_monitor_base.fld3 = STARTCONV.fld1 ";
	// }
	// return executeUpdate(sql);
	// }

	// -------------------------------------------------------------------------
	// �i���I�ɃC���|�[�g����(�e�L�X�g�e�[�u���Ƃ��Ăł͂Ȃ�)
	// �g�p�၄ importAS("CLONE","cnvMonitorCnv.txt");
	// -------------------------------------------------------------------------
	@Override
	public boolean importAS(String dstTable, String path) {
		String srcPath = getPath(path);
		String tmpTable = "tmpTex";
		if (isExist(tmpTable))
			dropTable(tmpTable);
		TContainer contener = importTable(tmpTable, srcPath);
		if (contener != null) {
			if (copyTable(dstTable, tmpTable)) {
				if (isExist(tmpTable))
					dropTable(tmpTable);
				return true;
			}
		}
		return false;
	}

	// -------------------------------------------------------------------------
	// �e�[�u�����t�@�C���ɕR�t����i�t�@�C���̓f�[�^�x�[�X�t�H���_��DATA_DIR�t�H���_�ɔz�u����Ă�����́j
	// -------------------------------------------------------------------------
	@Override
	public boolean assignIt(String table, String path) {
		File49ers f49_L = new File49ers(path);
		String delimiter = f49_L.getDelimiter();
		// int maxCol = f49_L.getMaxColm();
		String encoding = f49_L.getEncoding();
		return assignIt(table, path, delimiter, encoding);
		// return assignIt(table, path, delimiter, "");
	}

	// -------------------------------------------------------------------------
	// �e�[�u�����e�L�X�g�e�[�u���ɃR�s�[���邱�Ƃɂ��_���v
	// �e�L�X�g�e�[�u���쐬���t�@�C���ɕR�Â����Ă��遄�i�����t�@�C�������݂���ꍇ��x�����j
	// -------------------------------------------------------------------------
	@Override
	public void unloadIt(String filePath, String tableName) {
		if (log != null)
			log.info("# unloadIt START #");
		exportIt(filePath, "\t", tableName, "", "*", "", "");
		close();
		if (log != null) {
			log.info("	unload table(" + tableName + ") to " + filePath);
			log.info("# unloadIt End   #");
		}
	}

	// -------------------------------------------------------------------------
	// XXX�@�t�@�C�������݂��Ȃ��ꍇ���l�����遁�����̎|�\�����ׂ�
	// XXX�@�t�@�C������̏ꍇ�����l
	// �e�L�X�g���烌�X�g�A����( �R�s�[��̃e�[�u������U�폜����ꍇ��killOption��true)
	// sqlite�̏ꍇ�A�e���|������p�ӂ���K�v�͖����E�E�E�����Ȃ藬�����߂�͂��_�K�E�E�E
	// -------------------------------------------------------------------------
	@Override
	public void reloadIt(String dstTable, String fileName, boolean killOption) {
		if (log != null)
			log.info("# reloadIt START #");
		String tmpTable = "TMP_TABLE";

		TContainer contener = new TContainer(tmpTable, getPath(fileName), 0);
		createTable(tmpTable, contener.getfDefs());
		assignIt(tmpTable, contener.getDataPath());

		String fields = contener.getfDefs();
		tableCopy(dstTable, fields, tmpTable, killOption);
		// texTable.dropTable(); //�����ł���𓮂����ƁA�R�s�[��̌��������������Ȃ�
		int count1 = rowCount(tmpTable); // �����m�F�i�R�s�[���j
		int count2 = rowCount(dstTable); // �����m�F�i�R�s�[��j
		if (log != null) {
			if (count1 == count2) {
				log.info("�e�[�u��:" + tmpTable + "�@���A" + dstTable + "�@�ց@"
						+ count1 + " ���̃f�[�^�����[�h���܂���");
			} else {
				log.fatal("�e�[�u��:" + tmpTable + "�@���A" + dstTable
						+ "�ց@�f�[�^�����[�h���悤�Ƃ��܂������A" + count1 + " ���� " + count2
						+ " �� �������[�h�ł��܂���ł���");
			}

		}
		close();
		log.info("# reloadIt END #");
	}

	@Override
	public boolean createTable(String table, String fDefs) {
		if (!isExist(table))
			dropTable(table);
		String sql = "CREATE TABLE " + table + " (" + fDefs + ")";
		return executeUpdate(sql);
	}

}