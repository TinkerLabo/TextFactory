package kyPkg.sql;

public class SQLCMD_TEST {
	// ------------------------------------------------------------------------
	// テスト (実際は下記のようにymlファイルを引数に使うようなイメージ)・・・第二引数は出力先
	// ------------------------------------------------------------------------
	// java -jar T:\QPR\SQLCMD.jar "T:\QPR\ITEM_MM_Export.yml" 
	// "T:\QPR\dat\MM_JAN.VAR"
	// 　上記でパラメータに使用している　"T:\QPR\ITEM_MM_Export.yml"　の内容は以下の通り
	// -<ここから>----------------------------------------------
	// sql SELECT 'D2','01',' ','1',' ',' ',' ',XA1,XA2,XA3,XC3,'
	// ',XC4,XC2,XD2,XF1,XH1,XH2,XF2,XF3,XF4,XF5,XJ1,XJ2,XM1,XM2,XM3,XM4,'0','0','
	// ','0000'
	// FROM ITEM_MM;
	// -<ここまで>----------------------------------------------
	// Eclipse上から同じ処理をする場合は下記のとおり（同内容のyml、SQLCMD_TEST.ymlを用意して実行する）
	// ------------------------------------------------------------------------
	public static void test20150313() {
		String ymlPath = "c:/SQLCMD_TEST.yml";
		String outPath = "c:/SQLCMD_RESULT.txt";
		SQLCMD ins = new SQLCMD(new String[] { ymlPath, outPath });
		ins.execute();
	}
	public static void test20160610() {
		String ymlPath = "C:/samples/murakamiSelmon/「性・年代（１０歳区分）」が「男１０代」.YML";
		String outPath = "c:/SQLCMD_RESULT.txt";
		SQLCMD ins = new SQLCMD(new String[] { ymlPath, outPath });
		ins.execute();
	}

	public static void main(String[] args) {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("SQLCMD Elapse");
		elapse.start();

//		test20150313();
		test20160610();

		String comment = "";
		elapse.setComment(comment);
		elapse.stop();
	}
}
