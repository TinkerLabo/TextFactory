package kyPkg.sql;

import java.sql.Connection;
import java.util.List;

import kyPkg.filter.Inf_oClosure;

import org.apache.commons.logging.Log;

public interface Inf_JDBC {

	public abstract void setLog(Log log);

	//追加データのキーにあたるデータをあらかじめマスターから削除して、追加データをインサートする
	public abstract boolean tableMerge(String dstTable, String dstKey,
			String dstFields, String srcTable, String srcKey, String srcFields);

	public abstract boolean tableCopy(String dstTable, String fields,
			String srcTable, Boolean killOption);

	public abstract int rowCount(String dstTable);

	public abstract boolean dropTable(String table);

	public abstract boolean truncate(String table);

	public abstract int query2File(String path, String sql);

	// ---------------------------------------------------------------
	// 最後に！すべてのコネクションを開放
	// ---------------------------------------------------------------
	public abstract void releaseAll();

	// ---------------------------------------------------------------
	// (使い終わった)コネクションを待機キューに戻す
	// ---------------------------------------------------------------
	public abstract void freeConnection();

	public abstract void freeConnection(Connection con);

	// ---------------------------------------------------------------
	// コネクションを取得
	// ---------------------------------------------------------------
	public abstract Connection getConnection(String jURL, String user,
			String password);

	public abstract Connection getConnection();

	// ---------------------------------------------------------------
	// 接続をクローズ
	// ---------------------------------------------------------------
	public abstract void close();

	// ---------------------------------------------------------------
	// コネクションを閉じる
	// ---------------------------------------------------------------
	public abstract void closeConnection(Connection con);

	public abstract void executeBatch(String sql, List<List> paramList);

	// ---+---------+---------+---------+---------+---------+---------+---------+
	// ｑｕｅｒｙ＃１
	// ｕｐｄａｔｅ use for SQL commands CREATE, DROP, INSERT and UPDATE
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

	// XXX 本当は影響を与えられたデータの件数が返るのが望ましい
	public abstract boolean executeUpdate(String sql);
	public abstract int executeUpdate(List<String> sql);

	public abstract List<List> query2Matrix(String sql);

	public abstract List<List> query2Matrix(String sql, boolean headerOpt, int limit);

	public abstract List<String> query2List(String sql, boolean headerOpt, int limit);

	// 単一の値しか返らないことがあらかじめわかっている場合に使用する
	public abstract String queryOne(String sql);

	public abstract int query2Writer(Inf_oClosure writer, String sql);

	// -------------------------------------------------------------------------
	// スキーマ一覧をlistで返す
	// -------------------------------------------------------------------------
	public abstract List getSchemeList();

	// -------------------------------------------------------------------------
	// DBカタログ一覧をlistで返す
	// -------------------------------------------------------------------------
	public abstract List getCatalogList();

	// -------------------------------------------------------------------------
	// フィールド名一覧をlistで返す
	// -------------------------------------------------------------------------
	// 必要な項目を番号（type）に指定する
	// 1.TABLE_CAT String => テーブルカタログ (null の可能性がある)
	// 2.TABLE_SCHEM String => テーブルスキーマ (null の可能性がある)
	// 3.TABLE_NAME String => テーブル名
	// >4.COLUMN_NAME String => 列名
	// 5.DATA_TYPE short => java.sql.Types からの SQL の型
	// >6.TYPE_NAME String => データソース依存の型名。UDT の場合、型名は完全指定
	// 7.COLUMN_SIZE int => 列サイズ。
	// char や date の型については最大文字数、numeric や decimal の型については精度
	// -------------------------------------------------------------------------
	public abstract int getColumnCount(String tableName);

	public abstract String getField(String tableName, int seq);

	//先頭から、指定したカラムのフィールド名をカンマで連結して返す（負の数が指定された場合は後ろからそのぶん削る）
	public abstract String getFields(String tableName,int n);
	
	public abstract List getFieldsList(String tableName);
	
	public abstract String getFields(String tableName);

	public abstract List getFieldsList(String tableName, int kindOf);

	public abstract List getFieldsDefs(String tableName);

	// -------------------------------------------------------------------------
	// テーブル名一覧をlistで返す
	// -------------------------------------------------------------------------
	// ※ＭＥＭＯ getTablesの返す列について（各String）
	// 1.TABLE_CAT => テーブルカタログ名 ex.Pubs
	// 2.TABLE_SCHEM => テーブルスキーマ ex.dbo
	// 3.TABLE_NAME => テーブル名 !! ex.
	// 4.TABLE_TYPE => テーブルの型。!! "TABLE","VIEW","SYSTEM TABLE"
	// 5.REMARKS => テーブルに関する説明文 !!
	// 6.TYPE_CAT => の型のカタログ
	// 7.TYPE_SCHEM => の型のスキーマ
	// 8.TYPE_NAME => の型名
	// 9.SELF_REFERENCING_COL_NAME => 型付きテーブルの指定された「識別子」列の名前
	// 10.REF_GENERATION => "SYSTEM"、"USER"、"DERIVED"
	// -------------------------------------------------------------------------
	// ≪使用例≫
	// 引数には以下のようなテーブルの種別を表すテーブルを渡す
	// String[] patStr = {"TABLE","VIEW","SYSTEM TABLE",
	// "GLOBAL TEMPORARY","LOCAL TEMPORARY","ALIAS","SYNONYM"};
	// instance.popTblString(wCombo,patStr);
	// -------------------------------------------------------------------------
	public abstract List getTableNameList(String[] patStr);

	public abstract List getTableNameList(String[] patStr, int[] seq,
			String delimiter);

	// ---------------------------------------------------------------
	// isExistTbl 該当テーブルが存在するかどうか
	// ≪使用例≫ instance.isExist("テーブル名");
	// ---------------------------------------------------------------
	public abstract boolean isExist(String tableName);

	public abstract boolean isTableExist(String pTblName, boolean toUppercase);

	// -------------------------------------------------------------------------
	// クエリの結果が単一の値であるとき
	// -------------------------------------------------------------------------
//	public abstract String query2Str(String sql);

	// ---------------------------------------------------------------
	// ｑｕｅｒｙ＃２ use for SQL command SELECT
	// ---------------------------------------------------------------
//	public abstract List query2Matrix(String sql);

//	public abstract List query2Matrix(String sql, boolean headerOpt, int limit);

}