package kyPkg.etc;

import globals.ResControl;
import globals.ResControlWeb;

public class AliasRes_Test {

	public AliasRes_Test() {
		// TODO Auto-generated constructor stub
	}
	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] argv) {
//		test_CreateCurrentData();
	}

	// -------------------------------------------------------------------------
	// test
	// -------------------------------------------------------------------------
//	public static void test00_batchConvert() {
//		String aliasDir = "";
//		aliasDir = "G:/s2/rx/Enquetes/NQ/お酒調査（個人）/2009";
//		System.out.println("outDir:" + batchConvert(aliasDir));
//		aliasDir = "G:/s2/rx/Enquetes/NQ/お酒調査（世帯）/2009";
//		System.out.println("outDir:" + batchConvert(aliasDir));
//	}

	public static void test01_saveAsAtomics() {
		String aliasDir = ResControl.ENQ_DIR + "NQ/属性・性年代編/2007";
		String[] fields = { "A23", "A10", "A22" }; // 項目キーの名前
		String outDir = ResControlWeb.getD_Resources_ATOM(""); // 出力ディレクトリパス
		String outName = "属性ｘ1201_1264";
		// --------------------------------------------------------------
		System.out.println("#20130402#checkpoint 011");

		AliasRes aliasRes = new AliasRes(aliasDir);
		aliasRes.saveAsAtomics(outDir + outName, fields);
	}

	public static void test02_saveAsAtomics() {
		// XXX メタデータを作るにはどうしたらいいか考える
		String aliasDir = "Z:/s2/rx/Idxc/Enquete/属性・ライフスタイル編/2007";
		String[] fields = { "q1", "q2", "q3", "q4", "q5", "q6", "q7", "q47",
				"q48", "q49", "q50", "cluster" }; // 項目キーの名前
		String outDir = globals.ResControlWeb.getD_Resources_ATOM(""); // 出力ディレクトリパス
		String outName = "bonvoyage";
		System.out.println("#20130402#checkpoint 012");

		AliasRes aliasRes = new AliasRes(aliasDir);
		aliasRes.saveAsAtomics(outDir + outName, fields);
	}

//	public static void test1019_saveAsAtomicsWrap() {
//		String outname1 = AliasRes.saveAsAtomicsWrap("./atom/",
//				"NQ/属性・性年代編/2007/", "A23,A10,A22");
//		System.out.println("outname1:" + outname1);
//	}
//
//	public static void testSaveAsAtomicsWrap() {
//		String atomDir = ResControl.getAtomDir();
//		String levs = "ＱＰＲアンケート/属性・性年代編/2009/";
//		String field = "A01,A02";
//		String outname1 = AliasRes.saveAsAtomicsWrap(atomDir, levs, field);
//		System.out.println("outname1:" + outname1);
//	}

	// ------------------------------------------------------------------------
	// 集計期間に同期させるために集計対象期間の終了年をベースに
	// 『currentP.txt』⇒『currentP2014.txt』へと内容を書き換え生成する
	// ------------------------------------------------------------------------
//	public static void test_CreateCurrentData() {
//	String pPath = "C:/@qpr/home/Personal/MonSets/currentP2009.txt";
//		createCurrentData(pPath);
//	}
	public static void testxxx() {
		// G:/s2/rx/Enquetes/NQ/お酒調査（世帯）/2008
		// G:/s2/rx/Enquetes/NQ/お酒調査（個人）/2008

		String aliasDir = "";
		String outDir = "";// 出力ディレクトリパス

		aliasDir = "G:/s2/rx/Enquetes/NQ/お酒調査（世帯）/2008";
		outDir = ResControl.getUsersEnqDir() + "お酒調査（世帯）/2008/";

		// aliasDir = "G:/s2/rx/Enquetes/NQ/お酒調査（個人）/2008";
		// outDir = ResControl.getUsersEnqDir() + "お酒調査（個人）/2008/";

		// aliasDir = "G:/s2/rx/Enquetes/common/ブランドユーザー（ビール系酒）/2008(10-12)";
		// outDir = ResControl.getUsersEnqDir() +
		// "ブランドユーザー（ビール系酒）/2008(10-12)/";
		//
		// aliasDir = "G:/s2/rx/Enquetes/common/ブランドユーザー（ビール系酒）/2008(07-09)";
		// outDir = ResControl.getUsersEnqDir() +
		// "ブランドユーザー（ビール系酒）/2008(07-09)/";
		//
		// aliasDir = "G:/s2/rx/Enquetes/common/ブランドユーザー（ビール系酒）/2008(04-06)";
		// outDir = ResControl.getUsersEnqDir() +
		// "ブランドユーザー（ビール系酒）/2008(04-06)/";
		//
		// aliasDir = "G:/s2/rx/Enquetes/common/ブランドユーザー（ビール系酒）/2008(01-03)";
		// outDir = ResControl.getUsersEnqDir() +
		// "ブランドユーザー（ビール系酒）/2008(01-03)/";
		//
		// aliasDir = "G:/s2/rx/Enquetes/NQ/お茶調査１/2008";
		// outDir = ResControl.getUsersEnqDir() + "お茶調査１/2008/";
		//
		// aliasDir = "G:/s2/rx/Enquetes/NQ/お茶調査２/2008";
		// outDir = ResControl.getUsersEnqDir() + "お茶調査２/2008/";
		//
		// aliasDir = "G:/s2/rx/Enquetes/NQ/お茶調査３/2008";
		// outDir = ResControl.getUsersEnqDir() + "お茶調査３/2008/";
		//
		// aliasDir = "G:/s2/rx/Enquetes/NQ/お茶調査４/2008";
		// outDir = ResControl.getUsersEnqDir() + "お茶調査４/2008/";
		System.out.println("#20130402#checkpoint 013");

		AliasRes aliasRes = new AliasRes(aliasDir);
		aliasRes.saveAsStandAlone(outDir);
	}
	
}
