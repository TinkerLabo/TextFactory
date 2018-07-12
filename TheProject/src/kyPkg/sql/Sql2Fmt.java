package kyPkg.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import globals.ResControl;
import kyPkg.filter.EzReader;
import kyPkg.filter.EzWriter;
import kyPkg.filter.Inf_iClosure;
import kyPkg.filter.Inf_oClosure;
import kyPkg.task.Abs_BaseTask;

// 2010-06-24 create.sqlを読みbcp用フォーマット(ver6.0)に変換する
// ついでに・・・以下のような・・・雛形となるｓｑｌを自動生成する

//copy T:\QPR\SQL\TRUNCATE\TABLE\ITEM_SUB.sql  T:\QPR\SQL\TRUNCATE\TABLE\JICFS_A1.SQL  
//copy T:\QPR\SQL\DROP\INDEX\ITEM_SUB.sql      T:\QPR\SQL\DROP\INDEX\JICFS_A1.SQL      
//copy T:\QPR\SQL\CREATE\TABLE\ITEM_SUB.sql    T:\QPR\SQL\CREATE\TABLE\JICFS_A1.SQL    
//copy T:\QPR\SQL\CREATE\INDEX\ITEM_SUB.sql    T:\QPR\SQL\CREATE\INDEX\JICFS_A1.SQL    

//　あと村上君のオーダーも忘れないようにする＞＞品目別集計　　および・・・神戸生協

public class Sql2Fmt extends Abs_BaseTask {
	private HashMap<String, ArrayList> hMap1;

	// ------------------------------------------------------------------------
	// 入出力クロージャ
	// ------------------------------------------------------------------------
	private String dir = "";

	private Inf_iClosure reader = null;

	private Inf_oClosure writer1 = null;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	// 　スキーマ定義と同じテーブルcreateするｓｑｌ文を生成する(sqlite用)
	public Sql2Fmt(String dir, String outPath1, String inPath) {
		this.dir = kyPkg.uFile.FileUtil.makedir(dir);
		reader = new EzReader(dir + inPath); 
		writer1 = new EzWriter(dir + outPath1); // "create.sql"
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("ＸＸ開始");
		System.out.println("<<Schema2Sql>> start");
		long wCnt = 0;
		// reader.setDelimiter("\n");
		reader.open();
		String tableName;
		List colName = new ArrayList();
		List colType = new ArrayList();
		List colLen = new ArrayList();
		// ---------------------------------------------------------------------
		// incore
		// ---------------------------------------------------------------------
		StringBuffer buff = new StringBuffer();
		hMap1 = new HashMap();
		while (reader.readLine() != null) {
			int wStat = reader.getStat();
			if (wStat >= 0) {
				String rec = reader.getCurrent();
				rec = rec.trim().toUpperCase();
				if (rec.equals("GO")) {
					String sql = buff.toString();
					int pos1 = sql.indexOf("(");
					int pos2 = sql.lastIndexOf(")");
					if (pos1 > 0 && pos2 > 0) {
						String part1 = sql.substring(0, pos1);
						String[] splited0 = part1.split("\\s+");
						if (splited0.length >= 3
							&& splited0[0].equals("CREATE")
							&& splited0[1].equals("TABLE")) {
							tableName = splited0[2];
							System.out.println("tableName:" + tableName);
						}
						String part2 = sql.substring(pos1 + 1, pos2);
						// System.out.println("pos1:" + pos1);
						// System.out.println("pos2:" + pos2);
						// System.out.println("part1:" + part1);
						// System.out.println("part2:" + part2);
						String[] splited1 = part2.split(",");
						for (int i = 0; i < splited1.length; i++) {
							String string1 = splited1[i];
							String[] splited2 = string1.split("[()\\s]+");
							if (splited2.length > 1) {
								colName.add(splited2[0]);
								colType.add(splited2[1]);
							}
							if (splited2.length > 2) {
								colLen.add(splited2[2]);
							} else {
								colLen.add("");
							}
						}
					}
					buff.delete(0, buff.length());
				} else {
					buff.append(rec);
				}
			}
			//TODO 固定長にしてコンカチする必要あり・・・・どうすればよいのだっけね？？
			for (int index = 0; index < colName.size(); index++) {
				System.out.println("<"+index+"> Name:" + colName.get(index) + " Type:"
						+ colType.get(index) + " Len:" + colLen.get(index));
			}
		}
		reader.close();
		// ---------------------------------------------------------------------
		// output
		// ---------------------------------------------------------------------
		writer1.setLF("");
		writer1.open();
		if (wCnt > 0) {
			hmapWrite(hMap1, writer1, dir);
		}
		writer1.close();
		System.out.println("<<Schema2Sql>> end");
	}

	public void hmapWrite(HashMap<String, ArrayList> hMap,
			Inf_oClosure writer1, String dir) {
		ArrayList<String> keys = new ArrayList();
		Set set = hMap.entrySet();
		for (Iterator iter = set.iterator(); iter.hasNext();) {
			Map.Entry<String, ArrayList> element = (Map.Entry) iter.next();
			// System.out.print("key>"+element.getKey());
			keys.add(element.getKey());
		}
		Collections.sort(keys);
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			String tableName = (String) iter.next();
			ArrayList<String> list = hMap.get(tableName);
			writer1.write("drop table if exists " + tableName + "; \n");
			writer1.write("create table " + tableName + " (\n");
			writer1.write("     " + list.get(0));
			if (list != null && list.size() > 0) {
				for (int i = 1; i < list.size(); i++) {
					writer1.write(",\n");
					writer1.write("     " + list.get(i));
				}
			}
			writer1.write("\n);\n");
		}
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test1();
	}

	public static void test1() {
		String dir = ResControl.DSKTOP + "JICFS関連/";
		new Sql2Fmt(dir, "create.sql", "JICFS_A1.SQL").execute();
	}
}
