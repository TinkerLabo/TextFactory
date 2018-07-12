package kyPkg.sql;

import java.util.Collections;
import java.util.List;

import kyPkg.uFile.DosEmu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TexTableUtil {
//	public static Log log = LogFactory.getLog("kyPkg.sql.TexTableUtil.class");
	public static Log log = LogFactory.getLog(TContainer.APPENDA_CLASS);

	// -------------------------------------------------------------------------
	// テキストデータの先頭セルをキーとして累積更新する
	// ※レイアウトは問わないがマスターのセル幅に合わせられる
	// ※マスターが存在しない場合は一番先頭のファイルの幅のテーブルが生成される
	// -------------------------------------------------------------------------
	// パラメータ概要
	// 例＞TexTableUtil.mergeTable(outPath, dbObj, "qpr_monitor_base", pathList);
	// outPath 結果出力先ディレクトリ
	// TexDb dbObj データベースインタフェース
	// dstTable マスターとするテーブル
	// pathList 入力データパスのリスト
	// -------------------------------------------------------------------------
	// public static TContener importTable(Inf_TexDb dbObj, String iTable,String
	// srcPath) {
	// TContener contener = null;
	// // -------------------------------------------------------------
	// // ファイルからテキストテーブルを生成する
	// // -------------------------------------------------------------
	// String path = dbObj.getPath(srcPath);
	// contener = new TContener(iTable, path, 0);
	// String iDefs = contener.getfDefs();
	// dbObj.createTable(iTable, iDefs);
	// // -------------------------------------------------------------
	// // ファイルをテーブルにアサイン
	// // -------------------------------------------------------------
	// log.info("ファイルをテーブルにアサイン");
	// dbObj.assignIt(iTable, srcPath);
	// return contener;
	// }


	private static void test_mmMerge_Shop() {
		String dbDir = "c:/shops";
		Inf_TexDb dbObj = new JDBC_Sqlite("QPR", dbDir);
		dbObj.setLog(log); // for Debug
		String dataDir = dbObj.getDataDir();
		System.out.println("dataDir:" + dataDir);
		List pathList = DosEmu.dir(dataDir + "Shop*.CSV", false);
		if (pathList != null)
			Collections.sort(pathList);
		String outPath = dataDir + "Shop_out.txt";
		String oTable = "Shop_master";
		log.info("#test_mmMerge_Shop START #");
		log.info("■　情報をマスターにマージ");
		if (mergeShopTable(dbObj, oTable, pathList)) {
			log.info("■　テーブルをファイルにエクスポートする:" + outPath);
			String oKey = dbObj.getField(oTable, 0);
			dbObj.exportIt(dbObj.getPath(outPath), "\t", oTable, oKey, "*", oKey, "");
		} else {
			log.info(" データは存在しませんでした");
		}
		dbObj.close();
		log.info("#End test_mmMerge_Shop    #");

	}

	private static boolean mergeShopTable(Inf_TexDb dbObj, String oTable,
			List<String> pathList) {
		int oCol = 0;
		int iCol = 0;
		String sql = "";
		String iTable = "tmpMerge";
		String oKey = "";// fld0
		String iKey = "";// fld0
		String iFlds = "*";
		String oFlds = "*";
		if (pathList.size() > 0) {
			for (int i = 0; i < pathList.size(); i++) {
				String srcPath = pathList.get(i);
				log.info("　　テーブル'" + iTable + "'に【入力データ】を割り当てる");
				// -------------------------------------------------------------
				// 差分データをインポート
				TContainer contener = dbObj.importTable(iTable, srcPath);
				String iDefs = contener.getfDefs();
				iKey = dbObj.getField(iTable, 0);// 一番先頭のカラムをＩＤ(キー)とする
				// -------------------------------------------------------------
				// マスターテーブルに関する処理（存在しなければ生成）
				if (i == 0) {
					if (!dbObj.isExist(oTable)) {
						log.info("　　【マスタテーブル】" + oTable + "を生成");
						sql = "CREATE TABLE " + oTable + " (" + iDefs + ")";
						dbObj.executeUpdate(sql);
					}
					oKey = dbObj.getField(oTable, 0);
					oCol = dbObj.getColumnCount(oTable);
					oFlds = dbObj.getFields(oTable);
					log.info("　　※マスタ ： " + oCol + "カラム");
					// log.info("　　※マスタ Flds:" + oFlds);
				}
				iCol = contener.getMaxCol();
				if (iCol > oCol) {
					log.fatal("　　●~*　データのカラム数ERROR！！ マスター＜入力（" + iCol + "カラム）");
					System.exit(999);// ん~リカバーしにくくないか？？
				} else {
					log.info("　　※入力　： " + iCol + "カラム（セル幅が小さいときはダミー（null）で補う）");
					iFlds = contener.getFields(oCol);
					// log.info("　　※入力Flds:" + iFlds);
					// -------------------------------------------------------------
					// マスターにマージする
					log.info("　　マスターイメージに'" + iTable + "'をマージ");
					dbObj.tableMerge(oTable, oKey, oFlds, iTable, iKey, iFlds);
				}
				// ファイルを削除
				dbObj.dropTable(iTable);
			}
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] agrv) {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("TexTableUtil");
		elapse.start();
		System.out.println("- start!　 -");

		test_mmMerge_Shop();
		// testHsqlTexTable();
		// testSqliteTexTable();

		System.out.println("- finish! -");
		elapse.stop();
	}

	public static void testHsqlTexTable() {
		Inf_TexDb dbObj = new JDBC_HSQLDB("C:/dataBase");
		dbObj.reloadIt("QPR_MONITOR_BASE", "qpr_monitor_out0716.dat", true);
		dbObj.unloadIt("unloadIt0716.txt", "QPR_MONITOR_BASE");
	}

	public static void testSqliteTexTable() {
		Inf_TexDb dbObj = new JDBC_Sqlite("QPR", "C:/dataBase");
		dbObj.reloadIt("QPR_MONITOR_BASE", "unloadIt.txt", true);
		dbObj.unloadIt("C:/dataBase/unloadIt.txt", "QPR_MONITOR_BASE");
	}
}