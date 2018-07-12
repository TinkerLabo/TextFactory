package kyPkg.sql;

import java.sql.Connection;
import java.util.List;

import kyPkg.filter.Inf_oClosure;

import org.apache.commons.logging.Log;

public interface Inf_JDBC {

	public abstract void setLog(Log log);

	//�ǉ��f�[�^�̃L�[�ɂ�����f�[�^�����炩���߃}�X�^�[����폜���āA�ǉ��f�[�^���C���T�[�g����
	public abstract boolean tableMerge(String dstTable, String dstKey,
			String dstFields, String srcTable, String srcKey, String srcFields);

	public abstract boolean tableCopy(String dstTable, String fields,
			String srcTable, Boolean killOption);

	public abstract int rowCount(String dstTable);

	public abstract boolean dropTable(String table);

	public abstract boolean truncate(String table);

	public abstract int query2File(String path, String sql);

	// ---------------------------------------------------------------
	// �Ō�ɁI���ׂẴR�l�N�V�������J��
	// ---------------------------------------------------------------
	public abstract void releaseAll();

	// ---------------------------------------------------------------
	// (�g���I�����)�R�l�N�V������ҋ@�L���[�ɖ߂�
	// ---------------------------------------------------------------
	public abstract void freeConnection();

	public abstract void freeConnection(Connection con);

	// ---------------------------------------------------------------
	// �R�l�N�V�������擾
	// ---------------------------------------------------------------
	public abstract Connection getConnection(String jURL, String user,
			String password);

	public abstract Connection getConnection();

	// ---------------------------------------------------------------
	// �ڑ����N���[�Y
	// ---------------------------------------------------------------
	public abstract void close();

	// ---------------------------------------------------------------
	// �R�l�N�V���������
	// ---------------------------------------------------------------
	public abstract void closeConnection(Connection con);

	public abstract void executeBatch(String sql, List<List> paramList);

	// ---+---------+---------+---------+---------+---------+---------+---------+
	// �������������P
	// ������������ use for SQL commands CREATE, DROP, INSERT and UPDATE
	// ---+---------+---------+---------+---------+---------+---------+---------+
	public abstract void execute(String sql);

	// prepStmt = conn.prepareStatement(
	// "INSERT INTO MyTable(MyColumn) values (?)");
	public abstract boolean openPrep(String hash, String sql);

	public abstract void closePrep(String hash);

	public abstract boolean prepUpdate(String hash, String[] array);

	public abstract boolean prepUpdate(String hash, String[] array, int max);

	public abstract List<List> prepQuery2Matrix(String hash,
			String[] array);

	public abstract List<List> prepQuery2Matrix(String hash, int limit,
			String[] array);

	// XXX �{���͉e����^����ꂽ�f�[�^�̌������Ԃ�̂��]�܂���
	public abstract boolean executeUpdate(String sql);
	public abstract int executeUpdate(List<String> sql);

	public abstract List<List> query2Matrix(String sql);

	public abstract List<List> query2Matrix(String sql, boolean headerOpt, int limit);

	public abstract List<String> query2List(String sql, boolean headerOpt, int limit);

	// �P��̒l�����Ԃ�Ȃ����Ƃ����炩���߂킩���Ă���ꍇ�Ɏg�p����
	public abstract String queryOne(String sql);

	public abstract int query2Writer(Inf_oClosure writer, String sql);

	// -------------------------------------------------------------------------
	// �X�L�[�}�ꗗ��list�ŕԂ�
	// -------------------------------------------------------------------------
	public abstract List getSchemeList();

	// -------------------------------------------------------------------------
	// DB�J�^���O�ꗗ��list�ŕԂ�
	// -------------------------------------------------------------------------
	public abstract List getCatalogList();

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
	public abstract int getColumnCount(String tableName);

	public abstract String getField(String tableName, int seq);

	//�擪����A�w�肵���J�����̃t�B�[���h�����J���}�ŘA�����ĕԂ��i���̐����w�肳�ꂽ�ꍇ�͌�납�炻�̂Ԃ���j
	public abstract String getFields(String tableName,int n);
	
	public abstract List getFieldsList(String tableName);
	
	public abstract String getFields(String tableName);

	public abstract List getFieldsList(String tableName, int kindOf);

	public abstract List getFieldsDefs(String tableName);

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
	public abstract List getTableNameList(String[] patStr);

	public abstract List getTableNameList(String[] patStr, int[] seq,
			String delimiter);

	// ---------------------------------------------------------------
	// isExistTbl �Y���e�[�u�������݂��邩�ǂ���
	// ��g�p��� instance.isExist("�e�[�u����");
	// ---------------------------------------------------------------
	public abstract boolean isExist(String tableName);

	public abstract boolean isTableExist(String pTblName, boolean toUppercase);

	// -------------------------------------------------------------------------
	// �N�G���̌��ʂ��P��̒l�ł���Ƃ�
	// -------------------------------------------------------------------------
//	public abstract String query2Str(String sql);

	// ---------------------------------------------------------------
	// �������������Q use for SQL command SELECT
	// ---------------------------------------------------------------
//	public abstract List query2Matrix(String sql);

//	public abstract List query2Matrix(String sql, boolean headerOpt, int limit);

}