package kyPkg.filter;

import static kyPkg.util.KUtil.array2String;
import static kyPkg.util.KUtil.list2String;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import kyPkg.sql.JDBC_Sqlite;
import kyPkg.task.Abs_ProgressTask;

public class HML2Sqlite extends Abs_ProgressTask {
	public static final String TABLE_NAME = "WORK";
	public static final String DB_NAME = "monDB";
	public static final String MONSEL_DIR = "monsel";
	//-------------------------------------------------------------------------
	private static final String regexNum = "^[-]?[0-9]*[.]?[0-9]+$";//正規表現、数値文字列かどうか
	private static final String delimiter = "\t";
	private static final String SEQ = "＊順位";
	private static final String ASTA = "＊";
	private String dbDir = "c:/temp/";
	private String tableName = TABLE_NAME;
	private Inf_iClosure inClosure = null; // 	入力クロージャ
	private Inf_oClosure outClosure = null; // 	出力クロージャ
	private List<String> headerList; //	帳票のヘッダー部分（参考表示か）
	private List<String> nameList;

	public List<String> getNameList() {
		return nameList;
	}

	public List<String> getHeaderList() {
		return headerList;
	}

	public String getHeaderString() {
		StringBuffer buf = new StringBuffer();
		for (String element : headerList) {
			buf.append(element);
			buf.append("\n");
		}
		return buf.toString();
	}

	// -------------------------------------------------------------------------
	// コンストラクタ                                                                                                                            2015-06-03 yuasa
	// -------------------------------------------------------------------------
	//TODO sqlite3にパスが通っていないとスクリプトが実行できないのではないか？フルパスで指定する必要があるかも知れない
	// -------------------------------------------------------------------------
	public HML2Sqlite(String dbDir, String tableName, String inPath) {
		//	public HML2Sqlite(String tableName, String inPath) {
		super();
		this.dbDir = dbDir;
		this.tableName = tableName;
		this.inClosure = new EzReader(inPath);
		String outPath = dbDir + tableName + ".txt";
		this.outClosure = new EzWriter(outPath);
	}

	public long getWriteCount() {
		return outClosure.getWriteCount();
	}

	public void setDelimiter(String delimiter) {
		this.outClosure.setDelimiter(delimiter);
	}

	@Override
	public void execute() {
		super.start("Flt_Base", 2048);
		loop();// loop
		super.stop();// 正常終了
	}

	// -------------------------------------------------------------------------
	// tranLoop
	// -------------------------------------------------------------------------
	private long loop() {
		long lCount = -1;
		inClosure.open();
		inClosure.setDelimiter(delimiter);
		String[] cells = null;
		char[] ch;
		HashMap<Integer, String> colNameMap = new HashMap();
		headerList = new ArrayList();

		while ((cells = inClosure.readSplited()) != null) {
			lCount++;
			ch = cells[0].toCharArray();
			switch (ch[0]) {
			case '@':
				System.out.println("@@" + cells[0]);
				colNameMap = new HashMap();
				headerList = new ArrayList();
				outClosure.close();
				outClosure.open();
				break;
			case 'A':
				String head = array2String(cells, delimiter);
				headerList.add(head);
				//tab区切りのStringのリストで良いと思う・・・
				break;
			case 'B':
				for (int col = 1; col < cells.length; col++) {
					String name = colNameMap.get(col);
					if (name == null)
						name = "";
					//　文字列変換・・・
					name = name.replaceAll("ﾕｳｺｳｾﾀｲｽｳ", "有効個人数");
					name = name.replaceAll("ｷﾝｶﾞｸ_100ｾﾀｲｱﾀﾘ", "百個人当り購入金額");
					name = name.replaceAll("ﾖｳﾘｮｳ_100ｾﾀｲｱﾀﾘ", "百個人当り購入容量");
					name = name.replaceAll("ｽｳﾘｮｳ_100ｾﾀｲｱﾀﾘ", "百個人当り購入数量");
					name = name.replaceAll("ｺｳﾆｭｳｾﾀｲﾋﾘﾂ", "購入率　");
					name = name.replaceAll("ｷﾝｶﾞｸ", "購入金額");
					name = name.replaceAll("ﾖｳﾘｮｳ", "購入容量");
					name = name.replaceAll("ｽｳﾘｮｳ", "購入数量");
					name = name.replaceAll("ｺｳﾆｭｳｾﾀｲｽｳ", "購入個人数");
					name = name.replaceAll("ｶｲｽｳ", "購入回数");
					name = name.replaceAll("SEQ", SEQ);
					name = (name + cells[col]).trim();
					if (!name.equals("")) {
						colNameMap.put(col, name);
					}
				}
				//map
				break;
			case 'C':
				boolean writeFlag = true;
				List<String> recList = new ArrayList();
				for (int col = 1; col < cells.length; col++) {
					String colName = colNameMap.get(col);
					String cell = cells[col].trim();
					if (colName == null) {
						colName = "";
					}
					if (!colName.equals("")) {
						if (colName.startsWith(ASTA)) {
							if (colName.equals(SEQ)) {
								//								System.out.println("SEQ =>" + cell);
								if (cell.trim().equals("TOTAL")) {
									System.out
											.println("reject TOTAL =>" + cell);
									writeFlag = false; //値がTOTALならこの行を出力しない
								}
							}
							if (!cell.matches(regexNum)) {
								cell = "0";//数値でなければ０とする
							}
						} else {
							//	文字列行
						}
						recList.add(cell);
					}
				}
				if (writeFlag) {
					String rec = list2String(recList, delimiter);
					outClosure.write(rec);
				}
				break;
			default:
				break;
			}
		}
		inClosure.close();
		outClosure.close();
		List<Integer> keys = new ArrayList(colNameMap.keySet());
		nameList = new ArrayList();//漢字名称
		List<String> createSqlList = new ArrayList();//
		List<String> importSqlList = new ArrayList();//
		//---------------------------------------------------------------------
		Collections.sort(keys);
		String comma = "";
		String dataPath = dbDir + tableName + ".txt";
		//---------------------------------------------------------------------
		importSqlList.add(".separator \"\t\"");
		importSqlList.add(".import \"" + dataPath + "\" " + tableName + "");
		//---------------------------------------------------------------------
		createSqlList.add("drop table if exists " + tableName + ";");
		createSqlList.add("create table " + tableName + " (");
		for (int seq = 0; seq < keys.size(); seq++) {
			Integer key = keys.get(seq);
			String colName = colNameMap.get(key);
			nameList.add(colName);
			String fld = "    fld" + String.valueOf(key);
			if ((seq + 1) == keys.size()) {
				comma = "";
			} else {
				comma = ",";
			}
			if (colName.startsWith(ASTA)) {
				createSqlList.add(fld + " Integer " + comma);
			} else {
				createSqlList.add(fld + " text not null" + comma);
			}
		}
		createSqlList.add(");");
		//---------------------------------------------------------------------
		//		for (String head : headerList) {
		//			System.out.println("head:" + head);
		//		}
		//---------------------------------------------------------------------
		//create.sqlを出力する
		//---------------------------------------------------------------------
		EzWriter.list2File(dbDir + JDBC_Sqlite.CREATE_SQL, createSqlList);
		//---------------------------------------------------------------------
		//import.sqlを出力する
		//---------------------------------------------------------------------
		EzWriter.list2File(dbDir + JDBC_Sqlite.IMPORT_SQL, importSqlList);
		//---------------------------------------------------------------------
		//フィールド名代替情報
		//---------------------------------------------------------------------
		EzWriter.list2File(dbDir + "name.txt", nameList);
		//		for (String element : nameList) {
		//			System.out.println("##" + element);
		//		}
		return lCount;
	}

	// ------------------------------------------------------------------------
	// 使用例＞
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		String inPath = "C:/temp/QUOHML.PRN";
		genHML2Sqlite(inPath);
	}

	// ------------------------------------------------------------------------
	// アンケートデータを期間内有効モニターに限定する
	// ------------------------------------------------------------------------
	public static JDBC_Sqlite genHML2Sqlite(String inPath) {
		// --------------------------------------------------------------------
		// 購入量層の出力データをパースしてDBの生成とインポート用のスクリプトを生成する
		// --------------------------------------------------------------------
		System.out.println("購入量層の出力データをパースしてDBの生成とインポート用のスクリプトを生成する");
		String dbDir = globals.ResControl.getQPRHome(MONSEL_DIR);//=>C:/@qpr/home/monsel/
		//		System.out.println("userDir:" + dbDir);
		String dbName = DB_NAME;
		String tableName = TABLE_NAME;
		HML2Sqlite ins = new HML2Sqlite(dbDir, tableName, inPath);
		ins.execute();
		List<String> headerList = ins.getHeaderList();
		// --------------------------------------------------------------------
		//　生成したスクリプトからsqLiteのローカルDBを生成する
		// --------------------------------------------------------------------
		System.out.println("生成したスクリプトからsqLiteのローカルDBを生成する");
		JDBC_Sqlite dbObj = new JDBC_Sqlite(dbName, dbDir);
		dbObj.exeCreateAndImport();
		return dbObj;
	}

}