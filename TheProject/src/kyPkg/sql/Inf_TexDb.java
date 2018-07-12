package kyPkg.sql;

public interface Inf_TexDb extends Inf_JDBC {
	public abstract String getKind();

	// �f�[�^�x�[�X�ɕt�������p�X�i�e�L�X�g�t�@�C���j�ɕύX����
	public abstract String getPath(String path);

	// �e�L�X�g�e�[�u���ɃA�T�C���\�ȏꏊ�����肳��Ă���ꍇ������̂�
	public abstract String getDataDir();

	public abstract String getDbDir();

	// -------------------------------------------------------------------------
	// �e�[�u�����t�@�C���ɕR�t����i�t�@�C���̓f�[�^�x�[�X�t�H���_��DATA_DIR�t�H���_�ɔz�u����Ă�����́j
	public abstract boolean assignIt(String table, String fileName);

	public abstract boolean assignIt(String table, String path,
			String delimiter, String encoding);

	// -------------------------------------------------------------------------
	// �e�[�u�����`����
	public abstract boolean createTable(String table, String fDefs);

	// -------------------------------------------------------------------------
	// �e�[�u�����R�s�[����
	public abstract boolean copyTable(String dstTable, String srcTable);

	public abstract boolean covWithMaster(String dstTable, String mTable,String tTable, int tCol, int mCol);

	// sql�� ���s����
	public abstract void executeSQL(String sql) ;

	// -------------------------------------------------------------------------
	// �e�[�u�����t�@�C���o�͂���
	public void unloadIt(String filePath, String tableName);

	public abstract void reloadIt(String dstTable, String fileName,
			boolean killOption);

	public abstract boolean importAS(String dstTable, String path);

	// -------------------------------------------------------------------------
	// �e�L�X�g�t�@�C�����e�[�u���Ƃ��Ď�荞�ށi�e�L�X�g�t�@�C�����e�[�u���ɃA�T�C������Ƃ������Ɓj
	public abstract TContainer importTable(String iTable, String srcPath);

	// -------------------------------------------------------------------------
	// �s�p�����[�^�t
	// String outPath �o�͐�
	// String table �ΏۂƂȂ�e�[�u��
	// String orderBy �o�͏����w�肵�����ꍇ�Ɏg��
	// -------------------------------------------------------------------------
	public abstract void exportIt(String outPath, String delimiter, String ��able,
			String key,String fields, String order, String where);

}