package kyPkg.filter;

import globals.ResControl;
import globals.ResControlWeb;
import kyPkg.converter.Corpus;

public class TextFactory_Test {
	// -------------------------------------------------------------------------
	// バッチ起動する場合の例
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		// testerY();
		// test0717();
		jicfsConvert();
	}

	// -------------------------------------------------------------------------
	// パラメータファイルを利用する例
	// -------------------------------------------------------------------------
	public static void testerZ() {
		String sys100 = ResControlWeb
				.getD_Resources_QPR("qpr_monitor_info_2008XXXX.txt");
		String sys200 = ResControlWeb.getD_Resources_Templates("MONNew.txt");
		String sys110 = ResControlWeb.getD_Resources_QPR("Monp.csv");
		kyPkg.converter.SubstrCnv converter = new kyPkg.converter.SubstrCnv(sys110);
		TextFactory ins = new TextFactory(sys200, sys100, converter);
		ins.execute();
	}

	// -------------------------------------------------------------------------
	// csvを固定長ファイルに変換する例（パラメータは文字配列）
	// -------------------------------------------------------------------------
	public static void testerX() {
		String sys300 = ResControlWeb.getD_Resources_Templates("clusterMIX.csv");
		String sys400 = ResControlWeb.getD_Resources_Templates("clusterFIX.TXT");

		String[] paramArray = { "1,1,8," + Corpus.FIX_HALF + ",\tモニターＩＤ",
				"2,1,1," + Corpus.FIX_HALF + ",\t２００７年度ライフスタイルクラスタ",
				"3,1,1," + Corpus.FIX_HALF + ",\t２００８年度ライフスタイルクラスタ",
				"4,1,1," + Corpus.FIX_HALF + ",\t",
				"5,1,1," + Corpus.FIX_HALF + ",\t",
				"6,1,1," + Corpus.FIX_HALF + ",\t",
				"7,1,1," + Corpus.FIX_HALF + ",\t",
				"8,1,1," + Corpus.FIX_HALF + ",\t",
				"9,1,1," + Corpus.FIX_HALF + ",\t",
				"10,1,1," + Corpus.FIX_HALF + ",\t",
				"11,1,1," + Corpus.FIX_HALF + ",\t",
				"12,1,1," + Corpus.FIX_HALF + ",\t",
				"13,1,1," + Corpus.FIX_HALF + ",\t",
				"15,1,60," + Corpus.FIX_HALF + ",\tＤＵＭＭＹ", };
		kyPkg.converter.SubstrCnv converter = new kyPkg.converter.SubstrCnv(paramArray);

		TextFactory substr = new TextFactory(sys400, sys300, converter);
		substr.setRedersDelimiter(",");
		substr.execute();
	}

	// -------------------------------------------------------------------------
	// 固定長からタブ区切りテキストに変換する例（パラメータは文字配列）
	// -------------------------------------------------------------------------
	public static void testerY() {
		String sys100 = ResControlWeb.getD_Resources_QPR("MONNew.txt");
		String sys201 = ResControlWeb.getD_Resources_Templates("testerY.txt");

		String[] paramArray = { "1,1,8,固定前ゼロ,\tモニタID", "D,カンマ\t",
				"1,1,1," + Corpus.FIX_HALF + ",\tライフスタイルクラスタ1", "D,タブ\t",
				"1,2,2," + Corpus.FIX_HALF + ",\tライフスタイルクラスタ2", "D,カンマ\t",
				"1,3,3," + Corpus.FIX_HALF + ",\tライフスタイルクラスタ3", "D,タブ\t",
				"1,4,4," + Corpus.FIX_HALF + ",\tライフスタイルクラスタ4", "D,カンマ\t",
				"1,5,5," + Corpus.FIX_HALF + ",\tライフスタイルクラスタ5", "D,タブ\t",
				"1,6,6," + Corpus.FIX_HALF + ",\tライフスタイルクラスタ6", "D,カンマ\t",
				"1,7,7," + Corpus.FIX_HALF + ",\tライフスタイルクラスタ7", "D,タブ\t",
				"1,8,8," + Corpus.FIX_HALF + ",\tライフスタイルクラスタ8", };
		kyPkg.converter.SubstrCnv converter = new kyPkg.converter.SubstrCnv(paramArray);

		TextFactory substr = new TextFactory(sys201, sys100, converter);
		// 1セルめの、0文字めから、1文字の長さが、2で始まるならにマッチしたら、ステータスに１６を加える
		// RegFilter filter = new RegFilter(1, 0, 1, 16, "^2", true);
		// 0セルめの、0文字めから、1文字の長さが、7で始まるならにマッチしたら、ステータスに１６を加える
		RegChecker filter = new RegChecker(0, 0, 1, "^7", 16);
		substr.setFilter(filter, 16); // ステータス値の合計が１６以上なら出力する
		substr.execute();

		// <<動作テスト>> 以前の処理結果と同じかどうかの確認
		String sys202 = ResControlWeb.getD_Resources_Templates("testerY.org");
		String msg = new kyPkg.tools.Compare(sys201, sys202, 10, false)
				.compareAndGetStatRez();
		if (!msg.equals(""))
			System.out.println(msg);
	}

	public static void test0717() {
		String enqData = "N:/datas/Lotte09Spring/8001.csv";
		String parmPath = "N:/PowerBX/Lotte09Spring/T8001_ロッテガム/subParm.csv";
		String outPath = "N:/PowerBX/Lotte09Spring/T8001_ロッテガム/ASM.TXT";
		System.out.println(" enqData:" + enqData);
		System.out.println(" parmPath:" + parmPath);
		System.out.println(" outPath:" + outPath);
		// パラメータファイルの作成がうまくできたか？などなど確認後・・等々の処理をここに書く 
		TextFactory insSub = new TextFactory(outPath, enqData,
				new kyPkg.converter.SubstrCnv(parmPath));
		// insSub.setRedersDelimiter(COMMA);
		insSub.headerOption(true);
		insSub.execute();
	}

	// 2010/08/31 JICFS_New_Format_Data convert to
	// Jicfs_Classic_Format_Data(HOST)
	public static void jicfsConvert() {
		String userDir = globals.ResControl.getQPRHome();
		String iPathJ = userDir + "デスクトップ/JMAK変換資料/JAN/EA1.txt";
		String pPathJ = userDir + "デスクトップ/JMAK変換資料/JAN/JanCnvP.txt";
		String oPathJ = ResControl.D_DAT + "JICFS_JAN.DAT";
		System.out.println(" iPath:" + iPathJ);
		System.out.println(" pPath:" + pPathJ);
		System.out.println(" oPath:" + oPathJ);
		TextFactory insSubJ = new TextFactory(oPathJ, iPathJ,
				new kyPkg.converter.SubstrCnv(pPathJ));
		insSubJ.execute();

		String iPathS = userDir + "デスクトップ/JMAK変換資料/短縮/ED12.txt";
		String pPathS = userDir + "デスクトップ/JMAK変換資料/短縮/makCnvP_Short.txt";
		String oPathS = ResControl.D_DAT + "JICFS_MAK_SHORT.DAT";
		System.out.println(" iPath:" + iPathS);
		System.out.println(" pPath:" + pPathS);
		System.out.println(" oPath:" + oPathS);
		TextFactory insSubS = new TextFactory(oPathS, iPathS,
				new kyPkg.converter.SubstrCnv(pPathS));
		insSubS.execute();
		String iPathN = userDir + "デスクトップ/JMAK変換資料/標準/ED11.txt";
		String pPathN = userDir + "デスクトップ/JMAK変換資料/標準/makCnvP_Stdrd.txt";
		String oPathN = ResControl.D_DAT + "JICFS_MAK_STDRD.DAT";
		System.out.println(" iPath:" + iPathN);
		System.out.println(" pPath:" + pPathN);
		System.out.println(" oPath:" + oPathN);
		TextFactory insSubN = new TextFactory(oPathN, iPathN,
				new kyPkg.converter.SubstrCnv(pPathN));
		insSubN.execute();
	}
}
