package kyPkg.atoms;

import globals.ResControl;

public class Atom_DB_Test {

	public Atom_DB_Test() {
	}

	public static void main(String[] args) {
		// test();
		// test110726_b();
		// testNull();
		// test110729();
		// testQuery2filex();
	}

	public static void test110729() {
		String sql = "SELECT MAP_0,COUNT(*) FROM current GROUP BY MAP_0 ORDER BY MAP_0";
		sql = "SELECT * FROM current ";
		String userDir = globals.ResControl.getQPRHome();
		String outPath = userDir + "828111000507/tran/ans0729.txt";
		AtomDB atomDB = new AtomDB();
		int count = atomDB.query2file(outPath, sql);
		if (count == 0) {
			System.out.println("T-R-O-U-B-L-E!");
		}
	}

	public static void testgen() {
		String sql = AtomDB.multiSqlGen("MAP_4", 22);
		String userDir = ResControl.getQPRHome();
		String outPath = userDir + "ans.txt";
		AtomDB atomDB = new AtomDB();
		atomDB.query2file(outPath, sql);
	}

	public static void test() {
		// 　抽出条件に名前をつけて保存したいが・・・・
		// これを,limitPathに設定して限定処理としたい
		String sql = "select key_0 from current where MAP_0 = 2;";
		String userDir = ResControl.getQPRHome();
		String outPath = userDir + "limit.txt";
		AtomDB atomDB = new AtomDB();
		atomDB.query2file(outPath, sql);
	}

	public static void testNull() {
		// sqlite上でのｓｑｌ文の挙動確認のため・・・
		String sql = "";
		sql = "SELECT MAP_4 FROM current";
		sql = "SELECT substr(MAP_4,1,5) FROM current";
		sql = "SELECT 1,count(*) FROM current where substr(MAP_4,1,1)='1'";
		sql = "SELECT 'NA',count(*) FROM current where MAP_4 ='                      '";
		sql = "SELECT 'NA',count(*) FROM current where MAP_4 =''";
		sql = "SELECT 'NA',count(*) FROM current where MAP_4 is NULL";
		sql = "SELECT 'NA',count(*) FROM current where MAP_4 is null";

		sql = "SELECT 1,count(*) FROM current where substr(MAP_4,1,1)='1'";
		sql += " union ";
		sql += "SELECT 2,count(*) FROM current where substr(MAP_4,2,1)='1'";
		sql += " union ";
		sql += "SELECT 'NA',count(*) FROM current where MAP_4 =''";

		sql = "SELECT 'NA',count(*) FROM current where MAP_4 ='' union SELECT 1,count(*) FROM current where substr(MAP_4,1,1)='1' union SELECT 2,count(*) FROM current where substr(MAP_4,2,1)='1' union SELECT 3,count(*) FROM current where substr(MAP_4,3,1)='1' union SELECT 4,count(*) FROM current where substr(MAP_4,4,1)='1' union SELECT 5,count(*) FROM current where substr(MAP_4,5,1)='1' union SELECT 6,count(*) FROM current where substr(MAP_4,6,1)='1' union SELECT 7,count(*) FROM current where substr(MAP_4,7,1)='1' union SELECT 8,count(*) FROM current where substr(MAP_4,8,1)='1' union SELECT 9,count(*) FROM current where substr(MAP_4,9,1)='1' union SELECT 10,count(*) FROM current where substr(MAP_4,10,1)='1' union SELECT 11,count(*) FROM current where substr(MAP_4,11,1)='1' union SELECT 12,count(*) FROM current where substr(MAP_4,12,1)='1' union SELECT 13,count(*) FROM current where substr(MAP_4,13,1)='1' union SELECT 14,count(*) FROM current where substr(MAP_4,14,1)='1' union SELECT 15,count(*) FROM current where substr(MAP_4,15,1)='1' union SELECT 16,count(*) FROM current where substr(MAP_4,16,1)='1' union SELECT 17,count(*) FROM current where substr(MAP_4,17,1)='1' union SELECT 18,count(*) FROM current where substr(MAP_4,18,1)='1' union SELECT 19,count(*) FROM current where substr(MAP_4,19,1)='1' union SELECT 20,count(*) FROM current where substr(MAP_4,20,1)='1' union SELECT 21,count(*) FROM current where substr(MAP_4,21,1)='1' union SELECT 22,count(*) FROM current where substr(MAP_4,22,1)='1' union SELECT 23,count(*) FROM current where substr(MAP_4,23,1)='1' union SELECT 24,count(*) FROM current where substr(MAP_4,24,1)='1' union SELECT 25,count(*) FROM current where substr(MAP_4,25,1)='1' union SELECT 26,count(*) FROM current where substr(MAP_4,26,1)='1' union SELECT 27,count(*) FROM current where substr(MAP_4,27,1)='1' union SELECT 28,count(*) FROM current where substr(MAP_4,28,1)='1' union SELECT 29,count(*) FROM current where substr(MAP_4,29,1)='1' union SELECT 30,count(*) FROM current where substr(MAP_4,30,1)='1' union SELECT 31,count(*) FROM current where substr(MAP_4,31,1)='1' union SELECT 32,count(*) FROM current where substr(MAP_4,32,1)='1' union SELECT 33,count(*) FROM current where substr(MAP_4,33,1)='1' union SELECT 34,count(*) FROM current where substr(MAP_4,34,1)='1' union SELECT 35,count(*) FROM current where substr(MAP_4,35,1)='1' union SELECT 36,count(*) FROM current where substr(MAP_4,36,1)='1' union SELECT 37,count(*) FROM current where substr(MAP_4,37,1)='1' union SELECT 38,count(*) FROM current where substr(MAP_4,38,1)='1' union SELECT 39,count(*) FROM current where substr(MAP_4,39,1)='1' union SELECT 40,count(*) FROM current where substr(MAP_4,40,1)='1' union SELECT 41,count(*) FROM current where substr(MAP_4,41,1)='1' union SELECT 42,count(*) FROM current where substr(MAP_4,42,1)='1' union SELECT 43,count(*) FROM current where substr(MAP_4,43,1)='1' union SELECT 44,count(*) FROM current where substr(MAP_4,44,1)='1' union SELECT 45,count(*) FROM current where substr(MAP_4,45,1)='1' union SELECT 46,count(*) FROM current where substr(MAP_4,46,1)='1' union SELECT 47,count(*) FROM current where substr(MAP_4,47,1)='1' union SELECT 48,count(*) FROM current where substr(MAP_4,48,1)='1'";

		String userDir = ResControl.getQPRHome();
		String outPath = userDir + "ans.txt";
		AtomDB atomDB = new AtomDB();
		atomDB.query2file(outPath, sql);
	}
}
