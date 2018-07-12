package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import globals.ResControl;
import kyPkg.sql.ServerConnecter;
import kyPkg.task.Inf_ProgressTask;

public class Guess4Sqlite extends Guess {
	// ---------------------------------------------------------------------
	// 最頻、変動、標準偏差のあたりを計算するのに使用したいので・・・その辺を考える
	// sqliteには標準偏差を計算する関数がないので・・・
	// １.全体の平均を求める
	// sqlite> select avg(val1) from LOY1_HEAD;
	// 191.248341232228
	// ２．全体の平均との差の２乗の平方根をとる
	// select key,(val1-191.248341232228) from LOY1_HEAD group by key;
	// ・・・やはり大変そうなので、とりあえずＩＳＡＭで対応しておくか・・
	// ---------------------------------------------------------------------
	private static final String Mysterious = "Mysterious";
	private static final String HALF_STRING = "ﾊﾝｶｸ";
	private static final String WIDE_STRING = "全角";
	private static final String NUMERIC = "数字";
	private static final String DOTNUMERIC = "小数点値";

	// ------------------------------------------------------------------------
	// コンストラクター
	// ------------------------------------------------------------------------
	public Guess4Sqlite() {
		super();
	}

	// ---------------------------------------------------------------------
	// analyzeIt
	// ---------------------------------------------------------------------
	@Override
	public void analyzeIt(String path, boolean headOpt) {
		Vector<Vector> matrix = super.getMatrix(path, 0, 100);
		if (matrix == null || matrix.size() == 0) {
			System.out.println("ERROR?! Empty Data?:" + path);
			return;
		}
		if (headOpt)
			headOpt = isHeaderExist(matrix);
		// ---------------------------------------------------------------------
		// それぞれの列について型判定を行う
		// ---------------------------------------------------------------------
		autoDetect(matrix, headOpt);

		// ---------------------------------------------------------------------
		// 雛形ＳＱＬをつくる
		// ---------------------------------------------------------------------
		create_Table_sql = create_Table_sql(names, typeFigures, tableName);
		create_Index_sql = create_Index_sql(names, typeFigures, tableName);
		drop_Table_sql = drop_Table_sql(names, typeFigures, tableName);
		drop_Index_sql = drop_Index_sql(names, typeFigures, tableName);
		truncate_Table_sql = truncate_Table_sql(names, typeFigures, tableName);

		i_List = insert_sql(names, tableName);
		r_List = select_sql(names, tableName);
		u_List = update_sql(names, tableName);
		d_List = delete_sql(names, tableName);

		// ---------------------------------------------------------------------
		// bcpパラメータをつくる
		// ---------------------------------------------------------------------
		bcp_param = bcpParam(names, types, figures, delimiter);
		// ---------------------------------------------------------------------
		// bcpバッチをつくる
		// ---------------------------------------------------------------------
		String dbName = "QPRDB1";
		String user = "qpr";
		String pass = "";
		bcp_bat = bcp_batch(ServerConnecter.CURRENT_SERVER, dbName, ResControl.D_QPR, user, pass, tableName,
				delimiter);

		// ---------------------------------------------------------------------
		// bcpパラメータをつくる
		// ---------------------------------------------------------------------
		sampleCodeList = sampleCode(getR_Sql());// Select 文を拾ってくる
		// ---------------------------------------------------------------------
		// (SQLITE独自)
		// ---------------------------------------------------------------------
		String dirName = parentDir + "/" + tableName + "/";
		// ---------------------------------------------------------------------
		// import用ＳＱＬ＆バッチを作ってファイル出力する
		// ---------------------------------------------------------------------
		String importPath = dirName + "/" + tableName + "_Import.sql";
		EzWriter.list2File(importPath, create_Import(create_Table_sql));
		String batPath = dirName + "/" + tableName + "_Import.bat";
		EzWriter.list2File(batPath, create_batch(importPath));
		// ---------------------------------------------------------------------
		// 簡易更新処理のバッチシステムを自動生成する
		// ---------------------------------------------------------------------
		String trnPath = dirName + "/" + tableName + "_Transcation.sql";
		EzWriter.list2File(trnPath, create_Transaction(tableName));
		String trnBatPath = dirName + "/" + tableName + "_Transcation.bat";
		EzWriter.list2File(trnBatPath, create_batch(trnPath));
		// ---------------------------------------------------------------------
		// 簡易更新処理のバッチシステムを自動生成する(更新のリカバー)
		// ---------------------------------------------------------------------
		String rcvPath = dirName + "/" + tableName + "_Recover.sql";
		EzWriter.list2File(rcvPath, create_Recover(tableName));
		String rcvBatPath = dirName + "/" + tableName + "_Recover.bat";
		EzWriter.list2File(rcvBatPath, create_batch(rcvPath));
		// ---------------------------------------------------------------------
		// 確認用に出力しているが不要かも・・
		// ---------------------------------------------------------------------
		String fileName = tableName + ".sql";
		EzWriter.list2File(parentDir + "/SQL/CREATE/TABLE/" + fileName,
				create_Table_sql);
		EzWriter.list2File(parentDir + "/SQL/CREATE/INDEX/" + fileName,
				create_Index_sql);
		EzWriter.list2File(parentDir + "/SQL/DROP/TABLE/" + fileName,
				drop_Table_sql);
		EzWriter.list2File(parentDir + "/SQL/DROP/INDEX/" + fileName,
				drop_Index_sql);
		EzWriter.list2File(parentDir + "/SQL/TRUNCATE/TABLE/" + fileName,
				truncate_Table_sql);

		EzWriter.list2File(parentDir + "/" + tableName + "_bcp.bat", bcp_bat);
		EzWriter.list2File(parentDir + "/FMT/" + tableName + ".fmt", bcp_param);

		EzWriter.list2File(dirName + "/insert/" + fileName, i_List);
		EzWriter.list2File(dirName + "/select/" + fileName, r_List);
		EzWriter.list2File(dirName + "/update/" + fileName, u_List);
		EzWriter.list2File(dirName + "/delete/" + fileName, d_List);
	}

	// ------------------------------------------------------------------------
	// autoDetect:マトリックスを分析して、sqliteのファイル定義およびimport文等を作る
	// ------------------------------------------------------------------------
	private void autoDetect(Vector<Vector> matrix, boolean headerOpt) {
		if (matrix == null || matrix.size() <= 0)
			return;
		// Header enumerate
		List rows = matrix.get(0);
		int maxCol = rows.size();
		String heads[] = new String[maxCol];
		String guess[] = new String[maxCol];
		Integer maxVal[] = new Integer[maxCol];
		Integer maxLen[] = new Integer[maxCol];
		Integer minLen[] = new Integer[maxCol];
		// --------------------------------------------------------------------
		// ヘッダー名称の処理
		// --------------------------------------------------------------------
		if (headerOpt) {
			// 先頭行をヘッダーとする場合
			for (int col = 0; col < maxCol; col++) {
				heads[col] = rows.get(col).toString();
			}
		} else {
			// デフォルトのヘッダー名を生成
			for (int col = 0; col < maxCol; col++) {
				heads[col] = PREFIX + String.valueOf(col + 1);
			}
		}
		// --------------------------------------------------------------------
		int skip = 1;
		Object obj;
		for (int col = 0; col < maxCol; col++) {
			guess[col] = Mysterious;
			maxVal[col] = -1;
			maxLen[col] = -1;
			minLen[col] = Integer.MAX_VALUE;
			int line = 0;
			for (List rowObj : matrix) {
				line++;
				if (line > skip && rowObj != null) {
					if (rowObj.size() > col) {
					}
					obj = rowObj.get(col);
					if (obj != null) {
						// String val = obj.toString().trim();// スペースを含まない長さとなる
						String val = obj.toString();
						int iVal = getMaxVal(val);
						if (maxVal[col] < iVal)
							maxVal[col] = iVal;
						int curLen = val.length();
						// if(col==1){
						// System.out.println("val:>"+val+"< curLen:"+curLen);
						// }
						if (maxLen[col] < curLen)
							maxLen[col] = curLen;
						if (minLen[col] > curLen)
							minLen[col] = curLen;
						// 半角文字列か？
						if (kyPkg.uRegex.Regex.isHalfWidthString(val)) {
							if (kyPkg.uRegex.Regex.isNumeric(val)) {
								guess[col] = NUMERIC;// 数値
							} else if (kyPkg.uRegex.Regex.isDotNumeric(val)) {
								guess[col] = DOTNUMERIC;// 小数点値
							} else {
								guess[col] = HALF_STRING;// 半角文字列
							}
						} else {
							if (kyPkg.uRegex.Regex.isFullWidthString(val)) {
								guess[col] = WIDE_STRING;// 漢字文字列
							} else {
								guess[col] = Mysterious;// 全角半角混在
							}
						}
					}
				}
			}
			String type = "";// フィールドの型
			if (guess[col].equals(WIDE_STRING)) {
				type = VARCHAR;
				// figure = String.valueOf(maxLen[col]);
			} else {
				if (maxLen[col] == minLen[col]) {
					if (maxLen[col] == 0) {
						// すべて空っぽなのだと思う（開始位置、長さを指定していない）
						type = VARCHAR;
					} else {
						type = CHAR;
						// figure = String.valueOf(maxLen[col]);
					}
				} else {
					if (guess[col].equals(NUMERIC)) {
						type = INT;
					} else if (guess[col].equals(DOTNUMERIC)) {
						type = FLOAT;
					} else {
						// 半角全角混在の場合など
						type = VARCHAR;
						// figure = String.valueOf(maxLen[col]);
					}
				}
			}
			String figure = String.valueOf(maxLen[col]);// 桁数 20140723 for Bcp
			String name = heads[col];
			String typeFigure = "";
			names.add(name);
			types.add(type);
			if (nSet.contains(type)) {
				if (type.equals(VARCHAR)) {
					Integer iFigure = Integer.parseInt(figure);
					figure = String.valueOf((iFigure * 2));
				}
				typeFigure = type + "(" + figure + ")";
			} else {
				typeFigure = type;
			}
			figures.add(figure);
			typeFigures.add(typeFigure);
		}

		// VARCHARが指定されていたら桁を倍にする
		for (int i = 0; i < types.size(); i++) {
			String type = types.get(i);
		}
	}

	// ---------------------------------------------------------------------
	// create_batch （SQLITE）
	// ---------------------------------------------------------------------
	private List<String> create_batch(String sqlPath) {
		List<String> list = new ArrayList();
		String dbName = "test.Db";
		list.add("sqlite3 " + dbName + " \".read " + sqlPath + "\"");
		debug(list);
		return list;
	}

	// ---------------------------------------------------------------------
	// create_Import　（SQLITE）
	// ---------------------------------------------------------------------
	private List<String> create_Import(List<String> c_List) {
		List<String> list = new ArrayList(c_List);
		// ---------------------------------------------------------------------
		// 20141212 トランザクション用のテーブルも自動生成してみた・・・
		// ---------------------------------------------------------------------
		List<String> create_Table_trn_sql;// 更新＆追加用データ一時格納用テーブル
		List<String> create_Table_pre_sql;// 更新前情報格納用テーブル
		create_Table_trn_sql = create_Table_sql(names, typeFigures, tableName
				+ "_trn");
		create_Table_pre_sql = create_Table_sql(names, typeFigures, tableName
				+ "_pre");
		list.addAll(create_Table_trn_sql);
		list.addAll(create_Table_pre_sql);
		// ---------------------------------------------------------------------
		// import文を作る
		// ---------------------------------------------------------------------
		list.add(".separator \"" + delimiter + "\"");
		list.add(".import " + path + " " + tableName);
		return list;
	}

	private List<String> create_Transaction(String name) {
		List<String> list = new ArrayList();
		// importする
		list.add(".separator \"" + delimiter + "\"");
		list.add(".import " + path + " " + tableName + "_trn");
		// <1>ログを空にする
		list.add("delete from " + name + "_pre;");
		// <2>ログに吐き出す
		list.add("insert into " + name + "_pre select * from " + name
				+ " where " + name + ".fld_1 in (select Fld_1 from " + name
				+ "_trn);");
		// <3>消す
		list.add("delete from " + name + " where " + name
				+ ".fld_1 in (select Fld_1 from " + name + "_trn);");
		// <4>追加する
		list.add("insert into " + name + " select * from " + name + "_trn;");
		// <5>結果を出力する
		list.add(".output result.txt");
		list.add("select * from " + name + ";");
		return list;
	}
	private List<String> create_Recover(String name) {
		List<String> list = new ArrayList();
		//<1>ログと同じ内容のものを消す
		list.add("delete from " + name + " where " + name + ".fld_1 in (select Fld_1 from " + name + "_pre);");
		//<2>ログから戻す
		list.add("insert into " + name + " select * from " + name + "_pre;");
		// <5>結果を出力する
		list.add(".output result.txt");
		list.add("select * from " + name + ";");
		return list;
	}

	// ---------------------------------------------------------------------
	// Main
	// ---------------------------------------------------------------------
	public static void main(String[] argv) {
		test01();
	}

	public void template() {
		String dbDir = "C:/";
		String sql = list2String(r_List, "\n");
		String resultPath = dbDir + "result.txt";
		Inf_ProgressTask task = kyPkg.etc.CommonMethods.queryIsam2File(resultPath,
				sql, dbDir);
		new kyPkg.task.TaskWatcherNoGUI(task).execute();
	}

	public static void test01() {
		String inPath = "C:/loy1_Head.txt";
		inPath = "C:/Documents and Settings/EJQP7/デスクトップ/zapp/K6.txt";
		inPath = "C:/Documents and Settings/EJQP7/デスクトップ/zapp/loy1_Head.txt";
		boolean headerOption = false;
		new Guess4Sqlite().analyzeIt(inPath, headerOption);
	}
}
