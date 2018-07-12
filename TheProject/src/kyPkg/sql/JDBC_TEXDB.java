package kyPkg.sql;

import static kyPkg.util.Joint.join;
import kyPkg.uFile.File49ers;

//public  class JDBC_TEXDB extends JDBC implements Inf_TexDb {
public abstract class JDBC_TEXDB extends JDBC implements Inf_TexDb {
	// public static Log log =
	// LogFactory.getLog("kyPkg.sql.TexTableUtil.class");
	// データベースを作成する場所
	protected String dbDir = "";
	protected String dbPath = "";

	// テキストテーブルを配置する場所（ 簡単のため固定名"data"とした）
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

	// innerjoin　とunionを使ったコピー

	// -------------------------------------------------------------------------
	// copyTable(dbObj,"CLONE","qpr_monitor_base");
	// -------------------------------------------------------------------------
	@Override
	public boolean copyTable(String dstTable, String srcTable) {
		// コピー先が存在すれば消しておく　
		if (isExist(dstTable))
			dropTable(dstTable);
		if (isExist(srcTable)) {
			if (log != null)
				log.info("　　" + srcTable + "をコピーして、" + dstTable + "として保存");
			String sql = "";
			// ※注意！データベースの種類によってＳＱＬが異なる
			if (getKind().equals("SQLITE")) {
				sql = "CREATE TABLE " + dstTable + " AS SELECT * FROM "
						+ srcTable;
			} else {
				sql = "SELECT * INTO " + dstTable + " FROM " + srcTable;
			}
			return executeUpdate(sql);
		} else {
			System.out.println("コピー元となるテーブルが存在しません→" + srcTable);
			return false;
		}
	}

	// -------------------------------------------------------------------------
	// <<memo>>
	// ＩＤでマッチングした場合マスタ側のフィールド（開始日付）をあてがい
	// それ以外の（マッチングしなかった）場合のレコードとunion　allで合成出力する
	// -------------------------------------------------------------------------
	// 苦し紛れだ・・・・2011/02/23
	// 以下のようなＳＱＬを実行したかったのだが
	// sql = "UPDATE qpr_monitor_base "
	// + "SET qpr_monitor_base.fld3 = STARTCONV.fld1 "
	// + "INNER JOIN STARTCONV ON qpr_monitor_base.fld0 = STARTCONV.fld0 ";
	// ==> You can't. SQLite doesn't support JOINs in UPDATE statements.
	// ということで泣く泣く・・・UNIONallを使った形式でヤッツケました。@2011/02/23
	// -------------------------------------------------------------------------
	// マスターファイルを利用して更新（合成）した結果を別テーブルに出力する　
	// パラメータ　
	// String dstTable 出力先（テーブル名）
	// String master 変換用マスタ（テーブル名）
	// String tran 変換元（テーブル名）
	// int tCol 入れ替え先フィールド位置（0~n）
	// int mCol マスター側フィールド位置（0~n）
	// ※簡単の為キーは0カラムめとした（必要があれば改造可能）
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
				log.info("　　　トランザクションフィールドが空なので処理を中断しました");
			return false;
		}
		String mFlds[] = getFieldsArray(master);
		if (mFlds != null && mFlds.length > 0) {
			for (int i = 0; i < mFlds.length; i++) {
				mFlds[i] = master + "." + mFlds[i];
			}
		} else {
			if (log != null)
				log.info("　　　コンバータフィールドが空なので処理を中断しました");
			return false;
		}
		String srcFld = join(tFlds, ",");
		String tKey = tFlds[0];
		String mKey = mFlds[0];
		// 変更フィールドには元の名前を付けてあげる・・・
		tFlds[tCol] = mFlds[mCol] + " AS " + asFld;
		String cnvFld = join(tFlds, ",");
		String sql1 = "SELECT " + cnvFld + " FROM " + tran + " INNER JOIN "
				+ master + " ON " + tKey + " = " + mKey + " ";
		String sql2 = "SELECT " + srcFld + " FROM " + tran + " LEFT JOIN "
				+ master + " ON " + tKey + " = " + mKey + " WHERE (" + mKey
				+ " IS NULL)";
		// コピー先が存在すれば消しておく　
		if (isExist(dstTable))
			dropTable(dstTable);
		String sql = "";
		// 確認出力したときに見やすいように改行やスペースが入れてあります。
		String subQuery = "\n   (" + sql1 + " \n   union all \n   " + sql2
				+ "\n   )";
		// ※注意！データベースの種類によってＳＱＬが異なる
		if (getKind().equals("SQLITE")) {
			sql = "CREATE TABLE " + dstTable + " AS SELECT * FROM " + subQuery;
		} else {
			sql = "SELECT * INTO " + dstTable + " FROM " + subQuery;
		}
		// System.out.println("subQuery:\n" + subQuery);
		// System.out.println("sql\n" + sql);
		if (log != null)
			log.info("　　　マスターファイルを利用して更新（合成）した結果を別テーブルに出力 \n\n" + sql + "\n");
		return executeUpdate(sql);
	}

	// // 取りあえず作ったが・・・お蔵入りバージョン
	// private boolean convertIt() {
	// // You can't. SQLite doesn't support JOINs in UPDATE statements. だと？！
	// // sqliteがUPDATEとINNERを使ったクエリーに対応していないので、あきらめる
	// // （ＪＥＴでは下のバージョンで動く→確認済み、ＭＹＳＱＬの場合上段で動くようだ→未確認）
	// // そのうち動く日がくるさ・・・ぼそ（涙）
	// log.info("コンバータにより日付変換を行う");
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
	// 永続的にインポートする(テキストテーブルとしてではなく)
	// 使用例＞ importAS("CLONE","cnvMonitorCnv.txt");
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
	// テーブルをファイルに紐付ける（ファイルはデータベースフォルダのDATA_DIRフォルダに配置されているもの）
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
	// テーブルをテキストテーブルにコピーすることによるダンプ
	// テキストテーブル作成＜ファイルに紐づけられている＞（同名ファイルが存在する場合一度消す）
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
	// XXX　ファイルが存在しない場合も考慮する＝＞その旨表示すべき
	// XXX　ファイルが空の場合も同様
	// テキストからレストアする( コピー先のテーブルを一旦削除する場合はkillOptionをtrue)
	// sqliteの場合、テンポラリを用意する必要は無い・・・いきなり流し込めるはずダガ・・・
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
		// texTable.dropTable(); //ここでこれを動かすと、コピー先の件数がおかしくなる
		int count1 = rowCount(tmpTable); // 件数確認（コピー元）
		int count2 = rowCount(dstTable); // 件数確認（コピー先）
		if (log != null) {
			if (count1 == count2) {
				log.info("テーブル:" + tmpTable + "　より、" + dstTable + "　へ　"
						+ count1 + " 件のデータをロードしました");
			} else {
				log.fatal("テーブル:" + tmpTable + "　より、" + dstTable
						+ "へ　データをロードしようとしましたが、" + count1 + " 件中 " + count2
						+ " 件 しかロードできませんでした");
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