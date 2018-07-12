package kyPkg.vbMods;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Calc extends VB_Base {
	String gPath_Tmp = "";
	Object Lev01;
	Object Lev02;
	Object Lev03;
	Object Lev04;
	Object Lev05;
	Object Lev06;
	Object Lev07;
	Object Lev08;
	Object Lev09;
	Object Lev10;
	String gRepository = "";
	static boolean gDIRTY = false;
	int gCndx = 0;
	// --------+---------+---------+---------+---------+---------+---------+---------+---------+---------
	List gConds;// List2 ? String;
	List gHed1;// List2 ? Variant;
	List gTbl1;// List2 ? String;
	List gTgt1;// List2 ? String;
	List gAns1;// List2 ? Variant;
	List gHed2;// List2 ? Variant;
	List gTbl2;// List2 ? String;
	List gTgt2;// List2 ? String;
	List gAns2;// List2 ? Variant;
	List gUqt;// List2 ? Variant;

	public boolean CalcIt(Object outlet, List<String> obj1, List<String> obj2,
			List<String> obj3) {
		// List xSub = new ArrayList(); // ReDim xSub[0];
		gUqt = new ArrayList(); // ReDim gUqt[0];
		// ----+---------+---------+---------+---------+---------+---------+---------+---------+---------;
		// 抽出条件（コメント）;
		// ----+---------+---------+---------+---------+---------+---------+---------+---------+---------;
		gConds = new ArrayList(); // ReDim gConds(obj3.ListCount);
		for (int i = 0; i <= obj3.size() - 1; i++) {
			String wStr = obj3.get(i);
			String[] wChunk = wStr.split(vbTab);
			gConds.add(wChunk[0]);
		} // Next i;
		// ----+---------+---------+---------+---------+---------+---------+---------+---------+---------;
		// パラメータ解析１ 【表\頭】;
		// ----+---------+---------+---------+---------+---------+---------+---------+---------+---------;
		outlet = "パラメータ解析中(1)・・・";
		// DoEvents;
		// List xUqm = new ArrayList(); // ReDim xUqm[0];
		FountainHead objFH = new FountainHead();
		ParamObj pObj1 = CrossMod.ParmAnalyze(obj1, objFH);
		System.out
				.println("----+---------+---------+---------+---------+---------+---------+---------+---------+---------");
		gTbl1 = pObj1.get("Tbl");
		gTgt1 = pObj1.get("Tgt");
		gAns1 = pObj1.get("Ans");
		gHed1 = pObj1.get("Hed");
		List xMsq1 = pObj1.get("Msq");
		// ----+---------+---------+---------+---------+---------+---------+---------+---------+---------;
		// パラメータ解析２ 【表\側】;
		// ----+---------+---------+---------+---------+---------+---------+---------+---------+---------;
		outlet = "パラメータ解析中(2)・・・";
		// DoEvents;
		ParamObj pObj2 = CrossMod.ParmAnalyze(obj2, objFH);
		System.out
				.println("----+---------+---------+---------+---------+---------+---------+---------+---------+---------");
		gTbl2 = pObj2.get("Tbl");
		gTgt2 = pObj2.get("Tgt");
		gAns2 = pObj2.get("Ans");
		gHed2 = pObj2.get("Hed");
		List xMsq2 = pObj2.get("Msq");
		// ----+---------+---------+---------+---------+---------+---------+---------+---------+---------;
		// ※ベクター配列の縦横を反転する;
		// ----+---------+---------+---------+---------+---------+---------+---------+---------+---------;
		RevertVector(gHed2);
		// --------------------------------------------------------------------------------------------------
		// ★肝★【SchemeCpy】オリジナルデータをコピーしてローカルに展開 スキーマも作成する
		// --------------------------------------------------------------------------------------------------
		outlet = "データ抽出中・・・";
		// DoEvents;
		// ----+---------+---------+---------+---------+---------+---------+---------+---------+---------;
		// 源泉からデータをローカルにコピーする;
		// ----+---------+---------+---------+---------+---------+---------+---------+---------+---------;
		objFH.fieldConvert();
		// for debug;
		// objFH.enumurateIt();
		// gUqt = objFH.getKeyArray();
		gUqt = CrossMod.SQL2CSV_COPY(gPath_Tmp, "T", outlet, objFH);
		// ----+---------+---------+---------+---------+---------+---------+---------+---------+---------;
		// マルチ項目を再構\成;
		// ----+---------+---------+---------+---------+---------+---------+---------+---------+---------;
		outlet = "表頭を再構成・・・";
		// DoEvents;
		Multi2Single(xMsq1, gPath_Tmp, outlet);
		outlet = "表側を再構成・・・";
		// DoEvents;
		Multi2Single(xMsq2, gPath_Tmp, outlet);
		// ----+---------+---------+---------+---------+---------+---------+---------+---------+---------;
		// テーブルの合成についても考慮する;
		// ----+---------+---------+---------+---------+---------+---------+---------+---------+---------;
		// 抽出条件データ作成;
		// ----+---------+---------+---------+---------+---------+---------+---------+---------+---------;
		if (gDIRTY == true) {
			if (gCndx <= 0) {
				MsgBox("条件に該当するデータがありませんでした。");
				return false; // Exit Function;
			} // end_if;
			gUqt.add("AX#TXT");
		} // end_if;
		// ----+---------+---------+---------+---------+---------+---------+---------+---------+---------;
		SetHourglass(true);
		outlet = "作表開始";
		// DoEvents;
		// ----+---------+---------+---------+---------+---------+---------+---------+---------+---------;

		return true;
	} // <function>;

	public void ResetAx() {
		String wFile0 = "";
		String wFile1 = "";
		String wFile2 = "";
		wFile0 = gPath_Tmp + "\\AX.TXT";
		wFile1 = gPath_Tmp + "\\AX1.TXT";
		wFile2 = gPath_Tmp + "\\AX2.TXT";
		if (new File(wFile0).isFile())
			Kill(wFile0);
		if (new File(wFile1).isFile())
			Kill(wFile1);
		if (new File(wFile2).isFile())
			Kill(wFile2);
	} // <Sub>;

	public void ComJaminMod2(Object outlet, boolean relation) {
		outlet = "";
		String xSql = "";
		// String wFile0 = "";
		String wFile1 = "";
		String wFile2 = "";
		// wFile0 = gPath_Tmp + "\\AX.TXT";
		wFile1 = gPath_Tmp + "\\AX1.TXT";
		wFile2 = gPath_Tmp + "\\AX2.TXT";
		if (!new File(wFile1).equals("") && new File(wFile2).isFile()) {
			if (relation == true) {
				xSql = "SELECT [AX1#TXT].[ID] FROM [AX1#TXT] INNER JOIN [AX2#TXT] ON [AX1#TXT].[ID] = [AX2#TXT].[ID]";
			} else {
				xSql = "SELECT * FROM [AX1#TXT] union SELECT * FROM [AX2#TXT]";
			} // end_if;
		} else if (new File(wFile1).isFile()) {
			xComand("ren  " + wFile1 + " AX.TXT");
			gDIRTY = true;
			// DoEvents;
		} else if (new File(wFile2).isFile()) {
			xComand("ren  " + wFile2 + " AX.TXT");
			gDIRTY = true;
			// DoEvents;
		} else {
			xSql = "";
		} // end_if;
		if (xSql.equals(""))
			return; // Exit Sub;
		outlet = "データを抽出中・・・しばらくお待ちください。";
		// DoEvents;
		int rtnVal = 0;
		// String xRel = "";
		String xCnn = "";
		// int xmax = 0;
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// CreateAxSql;
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		Schemes.SchemaAx(gPath_Tmp);
		xCnn = "DRIVER={Microsoft Text Driver (*.txt; *.csv)};DefaultDir="
				+ gPath_Tmp
				+ ";DriverId=27;FIL=text;MaxBufferSize=16384;PageTimeout=5;";
		rtnVal = CrossMod.ODBC_SQL2TEX(xCnn, xSql, gPath_Tmp + "\\AX.TXT",
				false, false, false, ",");
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		if (rtnVal <= 0) {
			outlet = "条件に当てはまるデータがありませんでした。";
			// DoEvents;
		} else {
			gCndx = rtnVal;
			outlet = "該当データが" + rtnVal + "件ありました。";
			gDIRTY = true;
			// DoEvents;
		} // end_if;
	} // <Sub>;

	public void ComMrg(boolean relation, boolean relation_a,
			boolean relation_b, List outlet, List<List> outlet_a,
			List<List> outlet_b) {
		// int i = 0;
		String wRel = "";
		// outlet.Clear();
		wRel = "条件Ａ　";
		wRel = "";
		for (int i = 0; i <= outlet_a.size() - 1; i++) {
			String p1 = wRel + outlet_a.get(i).get(0);
			String p2 = (String) outlet_a.get(i).get(1);
			outlet.add(p1 + "\t" + p2);
			if (relation_a == true) {
				wRel = "ａｎｄ　";
			} else {
				wRel = "ｏｒ　　";
			} // end_if;
		} // Next i;
		if (outlet_b.size() > 0) {
			if (relation == true) {
				wRel = "ａｎｄ　";
			} else {
				wRel = "ｏｒ　　";
			} // end_if;
			String p1 = wRel;
			String p2 = "";
			outlet.add(p1 + "\t" + p2);
		} // end_if;
		wRel = "条件Ｂ　";
		wRel = "";
		for (int i = 0; i <= outlet_b.size() - 1; i++) {
			String p1 = wRel;
			String p2 = (String) outlet_b.get(i).get(1);
			outlet.add(p1 + "\t" + p2);
			if (relation_b == true) {
				wRel = "ａｎｄ　";
			} else {
				wRel = "ｏｒ　　";
			} // end_if;
		} // Next i;
	} // <Sub>;

	public static void main(String[] argv) {
		testComJamin_Plus();
	}

	public static void testComJamin_Plus() {
		System.out.println("#testComJamin_Plus# start");
		String userDir = globals.ResControl.getQPRHome();
		String gPath_Tmp = userDir+"TEMP";
		String pFile = "AX1.txt";
		boolean pAnd = false;
		String outlet = "";
		List<String> CondList = new ArrayList();
		CondList.add("Z:\\s2\\rx\\Enquetes\\NQ\\属性・性年代編\\2010\tALIAS\t$\tA22\t1\tSINGLE\t50\t1\t= '1'");
		CondList.add("Z:\\s2\\rx\\Enquetes\\NQ\\属性・性年代編\\2010\tALIAS\t$\tA22\t2\tSINGLE\t50\t1\t= '2'");
		CondList.add("Z:\\s2\\rx\\Enquetes\\NQ\\属性・性年代編\\2010\tALIAS\t$\tA22\t3\tSINGLE\t50\t1\t= '3'");
		CondList.add("Z:\\s2\\rx\\Enquetes\\NQ\\属性・メディア編\\2009\tALIAS\t$\tq14\t1\tSINGLE\t338\t1\t= '1'");
		// CondList.add("Z:\\s2\\rx\\Enquetes\\NQ\\属性・性年代編\\2010	ALIAS	$	A22	1	SINGLE	50	1	= '1'");
		// CondList.add("Z:\\s2\\rx\\Enquetes\\NQ\\属性・性年代編\\2010	ALIAS	$	A22	2	SINGLE	50	1	= '2'");
		// CondList.add("Z:\\s2\\rx\\Enquetes\\NQ\\属性・性年代編\\2010	ALIAS	$	A22	3	SINGLE	50	1	= '3'");
		// CondList.add("Z:\\s2\\rx\\Enquetes\\NQ\\属性・メディア編\\2009	ALIAS	$	q14	1	SINGLE	338	1	= '1'");
		int cnt = ComJamin_Plus(CondList, gPath_Tmp, pFile, pAnd, outlet);
		System.out.println("#testComJamin_Plus# cnt=>" + cnt);
	}

	// 2011-07-08 検証中・・・ほぼｏｋそう
	public static int ComJamin_Plus(List<String> CondList, String gPath_Tmp,
			String pFile, boolean pAnd, String outlet) {
		FountainHead objFH = new FountainHead();
		HashMap<String, List> hmap = new HashMap();
		List wTnm = new ArrayList();
		for (int i = 0; i <= CondList.size() - 1; i++) {
			String wStr = (String) CondList.get(i);
			String[] wChunk = wStr.split(vbTab);
			if (UBound(wChunk) == 0 || UBound(wChunk) != 8) {
				MsgBox("Error!! UBound(wChunk):" + UBound(wChunk));
				return -1; // Exit Function;
			}
			String xPth = wChunk[0];
			String xTbl = wChunk[1];
			String xDat = wChunk[2];
			String xKey = wChunk[3];
			// String xVal = wChunk[4];
			// String xTyp = wChunk[5];
			String xCol = wChunk[6];
			String xLen = wChunk[7];
			String xCnd = wChunk[8];
			// -+---------+---------+---------+---------+---------+---------+---------+---------+---------+
			String qx_Pth = xPth + "\\" + xTbl + ".TXT";
			// -+---------+---------+---------+---------+---------+---------+---------+---------+---------+
			// UniqueTbl!!;
			// -+---------+---------+---------+---------+---------+---------+---------+---------+---------+
			String xSub = "";
			String xWid = "";
			String wVal = "";
			if (!xDat.equals("")) {
				xSub = "SUBSTRING(" + xDat + "," + xCol + "," + xLen + ")";
				xWid = " Char Width " + xLen;
				wVal = xSub + vbTab + xKey + vbTab + xWid;
			} else {
				wVal = "";
			}
			int wSeq = objFH.regist(qx_Pth, wVal);
			// -+---------+---------+---------+---------+---------+---------+---------+---------+---------+
			String wName = "X" + Right("00" + wSeq, 2);
			List list = hmap.get(wName);
			if (list == null) {
				wTnm.add(wName);
				list = new ArrayList();
			}
			list.add("[" + xKey + "]" + xCnd);
			hmap.put(wName, list);
		}
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		List<List> xMatrix = new ArrayList();
		List wTnmx = new ArrayList();
		for (Iterator it = wTnm.iterator(); it.hasNext();) {
			String wName = (String) it.next();
			System.out.println("dbg wName:" + wName);

			xMatrix.add(hmap.get(wName));
			CrossMod.SQL2CSV_COPY(gPath_Tmp, "X", outlet, objFH);
			// 名前を変更している wTnm[i] = wTnm[i] + "#TXT";
			wTnmx.add(wName + "#TXT");
		}
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		outlet = "データを抽出中・・・しばらくお待ちください。";
		String xRel = "";
		if (pAnd == true) {
			xRel = "AND";
		} else {
			xRel = "OR";
		}
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// CreateAxSql;
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String xSql = LocalSqlMod.CreateAxSql(wTnmx, xMatrix, xRel);
		System.out.println("CreateAxSql>" + xSql);
		String xCnn = "DRIVER={Microsoft Text Driver (*.txt; *.csv)};DefaultDir="
				+ gPath_Tmp
				+ ";DriverId=27;FIL=text;MaxBufferSize=16384;PageTimeout=5;";
		int resultCount = CrossMod.ODBC_SQL2TEX(xCnn, xSql, gPath_Tmp + "\\"
				+ pFile, false, false, false, ",");
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		if (resultCount < 0) {
			outlet = "条件に当てはまるデータがありませんでした。";
		} else {
			Schemes.SchemaAx(gPath_Tmp);
			outlet = "該当データが" + resultCount + "件ありました。";
			gDIRTY = true;
		}
		return resultCount;
	}
}
