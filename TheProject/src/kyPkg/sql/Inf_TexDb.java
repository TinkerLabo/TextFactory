package kyPkg.sql;

public interface Inf_TexDb extends Inf_JDBC {
	public abstract String getKind();

	// データベースに付随したパス（テキストファイル）に変更する
	public abstract String getPath(String path);

	// テキストテーブルにアサイン可能な場所が限定されている場合があるので
	public abstract String getDataDir();

	public abstract String getDbDir();

	// -------------------------------------------------------------------------
	// テーブルをファイルに紐付ける（ファイルはデータベースフォルダのDATA_DIRフォルダに配置されているもの）
	public abstract boolean assignIt(String table, String fileName);

	public abstract boolean assignIt(String table, String path,
			String delimiter, String encoding);

	// -------------------------------------------------------------------------
	// テーブルを定義する
	public abstract boolean createTable(String table, String fDefs);

	// -------------------------------------------------------------------------
	// テーブルをコピーする
	public abstract boolean copyTable(String dstTable, String srcTable);

	public abstract boolean covWithMaster(String dstTable, String mTable,String tTable, int tCol, int mCol);

	// sqlを 実行する
	public abstract void executeSQL(String sql) ;

	// -------------------------------------------------------------------------
	// テーブルをファイル出力する
	public void unloadIt(String filePath, String tableName);

	public abstract void reloadIt(String dstTable, String fileName,
			boolean killOption);

	public abstract boolean importAS(String dstTable, String path);

	// -------------------------------------------------------------------------
	// テキストファイルをテーブルとして取り込む（テキストファイルをテーブルにアサインするということ）
	public abstract TContainer importTable(String iTable, String srcPath);

	// -------------------------------------------------------------------------
	// 《パラメータ》
	// String outPath 出力先
	// String table 対象となるテーブル
	// String orderBy 出力順を指定したい場合に使う
	// -------------------------------------------------------------------------
	public abstract void exportIt(String outPath, String delimiter, String ｔable,
			String key,String fields, String order, String where);

}