package kyPkg.vbMods;

import java.io.File;

import static kyPkg.sql.ISAM.CHARACTER_SET_OEM;
import static kyPkg.sql.ISAM.COL_NAME_HEADER_FALSE;
import static kyPkg.sql.ISAM.FORMAT_CSV_DELIMITED;
import static kyPkg.sql.ISAM.MAX_SCAN_ROWS_0;
import static kyPkg.sql.ISAM.SCHEMA_INI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import globals.ResControl;
import kyPkg.uFile.FileUtil;

//import kyPkg.vbMods.Schemes.*;

public class CrossMod extends VB_Base {
	public String CreateCrossSql(String[] pArray, String pTbl1, String pTgt1,
			String pTbl2, String pTgt2) {
		String CreateCrossSql = null;
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// 概要 :クロス集計（単項目間）を行う
		// 引数
		// 戻り値 :
		// 使用例 :
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		int wMax = 0;
		String sNew = "";
		String sOld = "";
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String xJoin = "";
		String xOnx = "";
		String xWhere = "";
		// int i = 0;
		// int Rtn = 0;
		String xSql = "";
		// String xTgt = "";
		// String xF01 = "";
		// String xF02 = "";
		String xTB1 = "";
		String xTB2 = "";
		// String sJoin = "";
		String wTB1 = "";
		String wTB2 = "";
		wTB1 = Left(pTbl1, 3) + "#TXT";
		wTB2 = Left(pTbl2, 3) + "#TXT";
		CreateCrossSql = "";
		xWhere = "";
		xJoin = "";
		xOnx = "";
		// ***< Table >***********************************************
		xTB1 = "[" + pTbl1 + "#TXT]";
		xTB2 = "[" + pTbl2 + "#TXT]";
		String sFlds = "";
		sFlds = xTB1 + ".[ID] as ID ," + xTB1 + ".[" + pTgt1 + "] AS F01,"
				+ xTB2 + ".[" + pTgt2 + "] AS F02," + "1 AS CNT";
		// 2011-01-07 yuasa mod from here;
		String sFld1 = "";
		String sFld2 = "";
		if (pTgt1.equals("FIXX")) {
			sFld1 = "1 AS F01,";
		} else {
			sFld1 = xTB1 + ".[" + pTgt1 + "] AS F01,";
		} // end_if;
		if (pTgt2.equals("FIXX")) {
			sFld2 = "1 AS F02,";
		} else {
			sFld2 = xTB2 + ".[" + pTgt2 + "] AS F02,";
		} // end_if;
		sFlds = xTB1 + ".[ID] as ID ," + sFld1 + sFld2 + "1 AS CNT";
		// 2011-01-07 yuasa mod to here;
		// ***< fields >**********************************************
		sOld = xTB1;
		sNew = xTB2;
		if (xTB1 != xTB2) {
			xOnx = " ON " + xTB2 + ".[ID] = " + xTB1 + ".[ID]";
			if (!xJoin.equals(""))
				xJoin = xJoin + "(";
			xJoin = xJoin + sOld + " INNER JOIN ";
		} // end_if;
		wMax = UBound(pArray);
		for (int i = 1; i <= wMax; i++) {
			if (pArray[i] != wTB1 && pArray[i] != wTB2) {
				// ***< Table >***********************************************
				sOld = sNew;
				sNew = "[" + pArray[i] + "]";
				// ***< Joint >***********************************************
				if (!xJoin.equals(""))
					xJoin = xJoin + "(";
				xJoin = xJoin + sOld + " INNER JOIN ";
				// ***< Compare fields >**************************************
				if (!xOnx.equals(""))
					xOnx = ")" + xOnx;
				xOnx = " ON " + sOld + ".[ID] = " + sNew + ".[ID]" + xOnx;
			} // end_if;
		} // Next i;
		xJoin = xJoin + sNew;
		// ***< SQL >*************************************************
		xSql = "SELECT DISTINCT " + sFlds + " FROM " + xJoin + xOnx + xWhere;
		// System.out.println("xSql:" + xSQL)
		CreateCrossSql = xSql;
		return CreateCrossSql;
	} // <function>;

	public String CreateTotalSql(String[] pArray, String pTbl1, String pTgt1) {
		String CreateTotalSql = null;
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// 概要 :合計行用のｓｑｌを作成する！
		// 引数
		// 戻り値 :
		// 使用例 :
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// 違タイプのテーブル間（sqlサーバー＆mdbなど）で使用不可！！！→ＢＵＧ？！
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		int wMax = 0;
		String xTbl = "";
		// String xAlias = "";
		String sOld = "";
		String xSav = "";
		String xJoin = "";
		String xOnx = "";
		String xWhere = "";
		String xGroup = "";
		// int i = 0;
		String xSql = "";
		String sFlds = "";
		// String xCnn = "";
		String wTbl = "";
		if (pTgt1.equals("FIXX")) {
			CreateTotalSql = "SELECT 1,COUNT(*) AS Total  FROM [T01#TXT];";
			return CreateTotalSql; // Exit Function;
		} // end_if;
			// xCnn = "";
		CreateTotalSql = "";
		wMax = UBound(pArray);
		wTbl = Left(pTbl1, 3) + "#TXT";
		for (int i = 1; i <= wMax; i++) {
			// ***< Table >***********************************************
			if (pArray[i] == wTbl) {
				xTbl = "[" + pTbl1 + "#TXT" + "]";
			} else {
				xTbl = "[" + pArray[i] + "]";
			} // end_if;
				// ***< Joint >***********************************************
			if (!xJoin.equals("")) {
				if (i == wMax) {
					xJoin = xJoin + " INNER JOIN " + xTbl;
				} else {
					xJoin = xJoin + " INNER JOIN (" + xTbl;
				} // end_if;
			} else {
				xJoin = xTbl;
			} // end_if;
				// ***< Compare fields >**************************************
			if (!sOld.equals(""))
				xSav = " ON" + sOld + ".[ID] ";
			sOld = " " + xTbl;
			if (!xSav.equals(""))
				xSav = xSav + "=" + sOld + ".[ID] ";
			if (!xOnx.equals("")) {
				xOnx = xSav + ")" + xOnx;
			} else {
				xOnx = xSav;
			} // end_if;
				// -+---------+---------+---------+---------+---------+---------+---------+---------+---------+
			if (pArray[i] == wTbl) {
				xGroup = " GROUP BY " + xTbl + ".[" + pTgt1 + "]";
				sFlds = xTbl + ".[" + pTgt1 + "],COUNT(*) AS Total ";
			} // end_if;
				// -+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		} // Next i;
		if (!xWhere.equals(""))
			xWhere = " WHERE" + xWhere;
		// ***< SQL >*************************************************
		xSql = "SELECT " + sFlds + " FROM " + xJoin + xOnx + xWhere + xGroup
				+ ";";
		// System.out.println("xSql:" + xSQL)
		CreateTotalSql = xSql;
		return CreateTotalSql;
	} // <function>;

	public void Dimension_Load2(String pPath, List outlet, String pLimit,
			String pComm) {
		if (IsMissing(pLimit))
			pLimit = "";
		if (IsMissing(pComm))
			pComm = "";
		outlet.clear();
		if (!new File(pPath + "/" + SCHEMA_INI).isFile()) {
			MsgBox("カテゴリーデータが存在しませんでした" + vbCrLf + "他のカテゴリーを選んでください。");
			return; // Exit Sub;
		} // end_if;
			// ------------------------------------------------------------------------------
			// int i = 0;
			// int j = 0;
		String xComm = "";
		String xParm = "";
		// ------------------------------------------------------------------------------
		List<List> xVector;// Collection2 ? Variant;
		xVector = new ArrayList();
		int xmax = 0;
		// ------------------------------------------------------------------------------
		String xNam = "";
		String xKey = "";
		String xTbl = "";
		String xTyp = "";
		String xFld = "";
		String[] wOpts;// Collection2 ? String;
		String wOpt = "";
		String wProxy = "";
		if (new File(pPath + "\\ALIAS.TXT").isFile()) {
			xTbl = "ALIAS";
			wProxy = "$";
		} else {
			wProxy = "";
			if (pLimit.equals("M01")) {
				xTbl = "RCL";
			} else if (pLimit.equals("Q01")) {
				xTbl = "RCL";
			} else {
				xTbl = "ASM";
			} // end_if;
		} // end_if;
			// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String xCnn = "";
		String xSql = "";
		if (pLimit.equals("M01")) {
			xSql = "SELECT Nam,Key,Typ,Opt FROM RTB1#TXT ORDER BY Srt";
		} else if (pLimit.equals("Q01")) {
			xSql = "SELECT Nam,Key,Typ,Opt FROM RTB1#TXT ORDER BY Srt";
		} else {
			xSql = "SELECT Nam,Key,Typ,Opt FROM QTB1#TXT WHERE (Typ = 'SINGLE' OR Typ = 'MULTI') AND Mot = 'ROOT' ORDER BY Srt";
		} // end_if;
		xCnn = "DRIVER={Microsoft Text Driver (*.txt; *.csv)};DefaultDir="
				+ pPath
				+ ";DriverId=27;FIL=text;MaxBufferSize=16384;PageTimeout=5;";
		xmax = ODBC_SQL2VECTOR(xCnn, xSql, xVector);
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		if (xmax > 0) {
			for (int i = 0; i <= xVector.size() - 1; i++) {
				String xSelect = (String) ((List) xVector.get(i)).get(1);
				if (xSelect.equals("")) {
				} else if (xSelect.equals("ID")) {
				} else if (xSelect.equals("SAMPLENUMBER")) {
				} else {
					List<String> wList = xVector.get(i);

					xNam = wList.get(0);
					xKey = wList.get(1);
					xTyp = wList.get(2);
					wOpt = wList.get(3);
					wOpts = wOpt.split(":");
					xFld = wProxy;
					if (UBound(wOpts) >= 0) {
						if (!Trim(wOpts[0]).equals(""))
							xFld = wOpts[0];
					} // end_if;
					xParm = vbTab + pPath + vbTab + xTbl + vbTab + xKey + vbTab
							+ xFld + vbTab + xTyp;
					// 　comm 設定;
					if (!pLimit.equals("")) {
						if (InStr(xKey, "@") > 0) {
							if (InStr(xKey, pLimit) > 0) {
								xComm = Trim(pComm) + " " + Trim(xNam);
							} else {
								xComm = "";
							} // end_if;
						} else {
							xComm = Trim(xNam) + " ";
						} // end_if;
					} else {
						xComm = Trim(xNam) + " ";
					} // end_if;
					if (!xComm.equals("")) {
						outlet.add(xComm + "\t" + xParm);
					} // end_if;
				} // end select;
			} // Next i;
		} // end_if;
	} // <Sub>;

	public void Add2pCtr(String pPrm1, String pPrm2, List<String> pCtr,
			String pCLQ, String pComm) {
		String[] wChunk;// Collection2 ? String;
		// String xPth = "";
		// String xTbl = "";
		// String xDat = "";
		// String xKey = "";
		String xVal = "";
		String xTyp = "";
		// String xCol = "";
		// String xLen = "";
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String xCm1 = "";
		String xCm2 = "";
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		xCm1 = "『" + Trim(pComm) + "』";
		xCm2 = "『" + Trim(pPrm1) + "』";
		wChunk = pPrm2.split(vbTab);
		if (UBound(wChunk) == 0)
			return; // Exit Sub;
		// xPth = wChunk[0];
		// xTbl = wChunk[1];
		// xDat = wChunk[2];
		// xKey = wChunk[3];
		xVal = wChunk[4];
		xTyp = wChunk[5];
		// xCol = wChunk[6];
		// xLen = wChunk[7];
		// ◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇◇
		String xComm = "";
		String xParm = "";
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String xCnd = "";
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String Msg = "";
		int Style;
		// Object Rtn;
		// xPth xTBL xkey xCnd xTyp;
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+

		if (xTyp.equals("INTEGER") || xTyp.equals("REAL")
				|| xTyp.equals("COUNT")) {
			if (IsNumeric(xVal) == false) {
				Msg = "比較する値が数値ではありません！" + Chr(13) + "数値を指定して下さい。" + Chr(13);
				Style = vbOKOnly + vbCritical;
				MsgBox(Msg, Style, "警告!");
				return; // Exit Sub;
			} // end_if;
			if (pCLQ.equals("Like") || pCLQ.equals("Like XXX?")
					|| pCLQ.equals("Like ?XXX")) {
				Msg = "比較する値が数値なのでLike演算子は使えません！" + Chr(13) + "他の演算子を指定して下さい。"
						+ Chr(13);
				Style = vbOKOnly + vbCritical;
				MsgBox(Msg, Style, "警告!");
				return; // Exit Sub;
			} // end_if;
		} else if (xTyp.equals("MULTI") || xTyp.equals("SINGLE")
				|| xTyp.equals("ALPHA")) {
			if (pCLQ.equals("Like")) {
				xVal = Chr(39) + "*" + Trim(StripChar(xVal, Chr(39))) + "*"
						+ Chr(39);
			} else if (pCLQ.equals("Like XXX?")) {
				xVal = Chr(39) + Trim(StripChar(xVal, Chr(39))) + "*" + Chr(39);
			} else if (pCLQ.equals("Like ?XXX")) {
				xVal = Chr(39) + "*" + Trim(StripChar(xVal, Chr(39))) + Chr(39);
			} else {
				if (xVal.equals(""))
					xVal = "NULL";
				if (UCase(xVal).equals("NULL")) {
					xVal = "NULL";
				} else {
					xVal = Chr(39) + StripChar(xVal, Chr(39)) + Chr(39);
				} // end_if;
			} // end_if;
		} else {
			return; // Exit Sub;
		} // end select;
		xCnd = "";
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String xSelect = pCLQ;
		if (xSelect.equals("Like")) {
			xCnd = " Like " + xVal;
			xComm = xCm1 + "に" + xCm2 + "という文字が含まれるもの";
		} else if (xSelect.equals("Like XXX?")) {
			xCnd = " Like " + xVal;
			xComm = xCm1 + "が、" + xCm2 + "という文字で始まるもの";
		} else if (xSelect.equals("Like ?XXX")) {
			xCnd = " Like " + xVal;
			xComm = xCm1 + "が、" + xCm2 + "という文字で終わるもの";
		} else if (xSelect.equals("IN")) {
			xCnd = " IN (" + xVal + ") ";
			xComm = xCm1 + "が、" + xCm2 + " のうちどれか";
		} else if (xSelect.equals("NOT IN")) {
			xCnd = " NOT IN (" + xVal + ") ";
			xComm = xCm1 + "が、" + xCm2 + "のうちどれでもないもの";
		} else if (xSelect.equals("=")) {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + "が、" + xCm2 + "であるもの";
		} else if (xSelect.equals("<>")) {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + "が、" + xCm2 + "ではないもの";
		} else if (xSelect.equals(">")) {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + "が、" + xCm2 + "より大きいもの";
		} else if (xSelect.equals(">=")) {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + "が、" + xCm2 + "以上であるもの";
		} else if (xSelect.equals("<")) {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + " が、" + xCm2 + "より小さいもの";
		} else if (xSelect.equals("<=")) {
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + " が、" + xCm2 + "以下であるもの";
		} else { // ;
			xCnd = " " + pCLQ + " " + xVal;
			xComm = xCm1 + " " + pCLQ + " " + xCm2 + "であるもの";
		} // end select;
		xParm = pPrm2 + vbTab + xCnd;
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// int i = 0;
		boolean xFlg = false;
		xFlg = false;
		for (int i = 0; i <= pCtr.size(); i++) {
			if (pCtr.get(i).startsWith(xComm)) {
				xFlg = true;
				return;
			} // end_if;
		} // Next i;
		if (xFlg == false) {
			pCtr.add(xComm + "\t" + xParm);
			// this.LabInfo2 = "条件を追加しました。"
		} else {
			// this.LabInfo2 = "すでに同じ項目が指定されています。"
		} // end_if;
	} // <Sub>;

	public void ListDetails(String pVal, List outlet) {
		String pPath = "";
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String xNam = "";
		String xKey = "";
		String xTbl = "";
		String xTyp = "";
		String xFld = "";
		String xCol = "";
		String xLen = "";
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String wProxy = "";
		String[] wChunk;// Collection2 ? String;
		wChunk = pVal.split(vbTab);
		xNam = Trim(wChunk[0]);
		pPath = wChunk[1];
		xTbl = wChunk[2];
		xKey = wChunk[3];
		wProxy = wChunk[4];
		System.out.println(
				"+---------+---------+---------+---------+---------+-");
		System.out.println("Comment wChunk[0]:" + wChunk[0]);
		System.out.println("Path    wChunk[1]:" + wChunk[1]);
		System.out.println("Table   wChunk[2]:" + wChunk[2]);
		System.out.println("Key     wChunk[3]:" + wChunk[3]);
		System.out.println("Field   wChunk[4]:" + wChunk[4]);
		System.out.println("Type    wChunk[5]:" + wChunk[5]);
		System.out.println(
				"+---------+---------+---------+---------+---------+-");
		if (!new File(pPath + "/" + SCHEMA_INI).isFile()) {
			MsgBox("カテゴリーデータが存在しませんでした" + vbCrLf + "他のカテゴリーを選んでください。");
			return; // Exit Sub;
		} // end_if;
		String wQtb = "";
		if (new File(pPath + "/" + "QTB1.TXT").isFile())
			wQtb = "QTB1#TXT";
		if (new File(pPath + "/" + "QTB2.TXT").isFile())
			wQtb = "QTB2#TXT";
		if (wQtb.equals("")) {
			MsgBox("インデックスファイルが見つかりませんでした。");
			return; // Exit Sub;
		} // end_if;
			// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String xCnn = "";
		String xSql = "";
		xSql = "SELECT Nam,Key,Val,Typ,Col,Len,Opt FROM " + wQtb
				+ " WHERE Mot = '" + xKey + "' ORDER BY Srt";
		xCnn = "DRIVER={Microsoft Text Driver (*.txt; *.csv)};DefaultDir="
				+ pPath
				+ ";DriverId=27;FIL=text;MaxBufferSize=16384;PageTimeout=5;";
		List<List> xVector;// Collection2 ? Variant;
		xVector = new ArrayList();
		int xmax = 0;
		// int i= 0;int j=0;
		String xRec1 = "";
		String xRec2 = "";
		String xVal = "";
		String xSub = "";
		String xWid = "";
		String[] wOpts;// Collection2 ? String;
		String wOpt = "";
		xmax = ODBC_SQL2VECTOR(xCnn, xSql, xVector);
		if (xmax > 0) {
			for (int i = 0; i <= (xVector.size()); i++) {
				List<String> wList = xVector.get(i);
				xKey = wList.get(1);
				xVal = wList.get(2);
				xTyp = wList.get(3);
				xCol = wList.get(4);
				xLen = wList.get(5);
				// 2008 3 26;
				wOpt = wList.get(6);
				wOpts = wOpt.split(":");
				xFld = wProxy;
				if (UBound(wOpts) >= 0) {
					if (!Trim(wOpts[0]).equals(""))
						xFld = wOpts[0];
				} // end_if;
					// 2008 3 26;
				if (!xFld.equals("")) {
					xSub = "SUBSTRING(" + xFld + "," + xCol + "," + xLen + ")";
					xWid = "Char     Width " + xLen;
				} else {
					xSub = "";
					xWid = "";
				} // end_if;
				xRec1 = "【" + xNam + "】  " + vbTab + Trim(wList.get(0));
				// (Path,Tabl) 1:KEY 2:VAL 3:TYP 4:Sub 5:Width;
				xRec2 = vbTab + pPath + vbTab + xTbl + vbTab + xKey + vbTab
						+ xVal + vbTab + xTyp + vbTab + xSub + vbTab + xWid;
				outlet.add(xRec1 + "\t" + xRec2);
			} // Next i;
			if (xVector.size() <= 90) {
				xRec1 = "【" + xNam + "】  " + vbTab + "ＮＡ";
				xRec2 = vbTab + pPath + vbTab + xTbl + vbTab + xKey + vbTab
						+ "NA" + vbTab + xTyp + vbTab + "" + vbTab + "";
				outlet.add(xRec1 + "\t" + xRec2);
			} // end_if;
		} // end_if;
	} // <Sub>;

	public static void testParseAlias() {
		HashMap hmap = ParseAlias(
				ResControl.ENQ_DIR + "NQ/属性・性年代編/2010/alias.txt");
		System.out.println("Cnn:" + hmap.get("Cnn"));
		System.out.println("Tbl:" + hmap.get("Tbl"));
		System.out.println("Fld:" + hmap.get("Fld"));
		System.out.println("Key:" + hmap.get("Key"));
		System.out.println("Cnd:" + hmap.get("Cnd"));
	}

	// 検証済み2011-07-05　完璧に検証したわけではない
	public static HashMap ParseAlias(String pPath) {
		HashMap<String, String> hmap = new HashMap();
		String cnn = "";
		String tbl = "ASM.TXT";
		String fld = "";
		String key = "";
		String cnd = "";
		hmap.put("Cnn", cnn);
		hmap.put("Tbl", tbl);
		hmap.put("Fld", fld);
		hmap.put("Key", key);
		hmap.put("Cnd", cnd);
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// 概要 :パラメータファイルを読み込む
		// 引数 pFile 入力ファイルのパス
		// 使用例 :
		// Rtn = ParseAlias(pFile.....パラメータ群)
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// ※グローバルなのでパラメータでなくてもｏｋではある？？？？変更するか悩むところ・・・
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String pDir = Left(pPath, (Len(pPath) - 10));
		if (!new File(pPath).isFile()) {
			return hmap; // Exit Function;
		}
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// List xAry;// Collection2 ? String;
		// int i = 0;
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String path = "";
		String xRec = "";
		int xLct = 0;
		try {
			path = GetShortName(Trim(pPath));
			if (path.equals(""))
				return null; // Exit Function;
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			if (br == null)
				return null; // Exit Function;
			xLct = 0;
			while (br.ready()) {
				xRec = br.readLine();
				xLct = xLct + 1;
				if (Trim(xRec).equals("")) {
				} else if (UCase(Left(xRec, 7)).equals("CONNECT")) {
					cnn = Trim(Mid(xRec, 9));
					cnn = CnvChr(cnn, "$", pDir);
				} else if (UCase(Left(xRec, 5)).equals("TABLE")) {
					tbl = Trim(Mid(xRec, 7));
				} else if (UCase(Left(xRec, 5)).equals("FIELD")) {
					fld = Trim(Mid(xRec, 7));
				} else if (UCase(Left(xRec, 3)).equals("KEY")) {
					key = Trim(Mid(xRec, 5));
				} else if (UCase(Left(xRec, 4)).equals("COND")) {
					cnd = Trim(Mid(xRec, 6));
				} else {
				} // end_if;
			}
			// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
			// System.out.println(" gd_Cnn:" + gd_Cnn)
			// System.out.println(" gd_Tbl:" + gd_Tbl)
			// System.out.println(" gd_Fld:" + gd_Fld)
			// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
			// System.out.println(" gd_Key:" + gd_Key)
			// System.out.println(" gd_Cnd:" + gd_Cnd)
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		hmap.put("Cnn", cnn);
		hmap.put("Tbl", tbl);
		hmap.put("Fld", fld);
		hmap.put("Key", key);
		hmap.put("Cnd", cnd);
		return hmap;
	} // <function>;

	public static ParamObj ParmAnalyze(List<String> pCtrl, FountainHead objFH) {
		boolean neo = true;
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+--------
		// ParmAnalyze
		// ヘッダー、ユニークテーブルおよびオリジナル値等のマトリックスを作成する
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+--------
		String[] wChunk;// Collection2 ? String;
		String sBrk = "";
		String wVal = "";
		int wSeq = 0;
		// int k = 0;
		int SSQ = 0;
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+--------;
		String wMother = "";
		String wSQL = "";
		String wOth = "";
		// int wMsq = 0;
		int wMlx = 0;
		// int xMct = 0;
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+--------;
		List pTbl = new ArrayList(); // ReDim pTbl[0];
		List pTgt = new ArrayList(); // ReDim pTgt[0];
		List pAns = new ArrayList(); // ReDim pAns[0];
		List pHed;// Collection2 ? Variant;
		List pMsq = new ArrayList(); // ReDim pMsq[0];
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+--------;
		List wCm1 = new ArrayList(); // ReDim wCm1[0];
		List wCm2 = new ArrayList(); // ReDim wCm2[0];
		List wTbl = new ArrayList(); // ReDim wTbl[0];
		int ixx = -1;
		int jxx = -1;
		wCm1.add("");
		wCm2.add("");
		wTbl.add("");
		for (int i = 0; i <= pCtrl.size(); i++) {
			// if (pCtrl.Selected[i] == true ) {
			if (true) {
				// -------+---------+---------+---------+---------+---------+---------+---------+--------;
				// 選択された選択肢のみを wXXX に保存ブレークを取り pXXX へ返す;
				// -------+---------+---------+---------+---------+---------+---------+---------+--------;
				ixx = ixx + 1;
				wCm1.add(""); // Preserve ixx + 1);
				wCm2.add(""); // Preserve ixx + 1);
				wTbl.add(""); // Preserve ixx + 1);
				pAns.add(""); // Preserve ixx);
				// -------+---------+---------+---------+---------+---------+---------+---------+--------;
				// 0:Q_Comm 1:A_Comm 2:Path 3:Table 4:Key 5:AnsVal 6:Typ
				// 7:Substring 8:Width;
				// -------+---------+---------+---------+---------+---------+---------+---------+--------;
				// wChunk= pCtrl.list(i, 0) + pCtrl.list(i, 1).split(vbTab);
				wChunk = pCtrl.get(i).split(vbTab);
				String qx_Ttl = "";
				String qx_Ans = "";
				String qx_Pth = "";
				String qx_Tbl = "";
				String qx_Key = "";
				String qx_Val = "";
				String qx_Typ = "";
				String qx_Sub = "";
				String qx_Wid = "";

				qx_Ttl = wChunk[0];
				qx_Ans = wChunk[1];
				qx_Pth = wChunk[2];
				qx_Tbl = wChunk[3];
				qx_Key = wChunk[4];
				qx_Val = wChunk[5];
				qx_Typ = wChunk[6];
				qx_Sub = wChunk[7];
				qx_Wid = wChunk[8];
				// -------+---------+---------+---------+---------+---------+---------+---------+--------;
				if (qx_Tbl.equals("ALIAS")) {
					qx_Pth = qx_Pth + "\\" + qx_Tbl + ".txt";
				} else {
					qx_Pth = qx_Pth + "\\" + qx_Tbl;
				} // end_if;
				if (!qx_Sub.equals("")) {
					wVal = qx_Sub + vbTab + qx_Key + vbTab + qx_Wid;
				} else {
					wVal = "";
				} // end_if;
					// -------+---------+---------+---------+---------+---------+---------+---------+--------;
					// ■ UniqueTbl2 ■ fountainhead;
					// -------+---------+---------+---------+---------+---------+---------+---------+--------;
					// wSeq = UniqueTbl2(qx_Pth, wVal, pUqt, pSub)
				wSeq = objFH.regist(qx_Pth, wVal);
				wCm1.add("");
				wCm2.add(qx_Ans);
				// ---------------------------------------------------;
				wTbl.add("T" + Right("00" + wSeq, 2));
				wMother = Left(qx_Key, (InStr(qx_Key + "_", "_")) - 1);
				// ---------------------------------------------------;
				pAns.add(qx_Val);
				if (sBrk != (qx_Pth + wMother)) {
					wOth = "";
					SSQ = 0;
					jxx = jxx + 1;
					wCm1.add(qx_Ttl);
					if (qx_Typ.equals("MULTI")) {
						pTbl.add(wTbl.get(ixx) + wMother);
					} else {
						pTbl.add(wTbl.get(ixx));
					} // end_if;
					pTgt.add(wMother);
					sBrk = qx_Pth + wMother;
				} // end_if;
					// ---------------------------------------------------;
				if (qx_Typ.equals("MULTI")) {
					SSQ = SSQ + 1;
					if (!qx_Val.equals("NA")) {
						wMlx = wMlx + 1;
						pMsq.add(""); // Preserve wMlx);
						wSQL = "SELECT ID,'" + SSQ + "' AS " + wMother
								+ " FROM " + wTbl.get(ixx) + "#TXT" + " WHERE "
								+ qx_Key + " = '" + qx_Val + "'";
						// mod@2011_0118;
						if (neo == true) {
							wSQL = "SELECT ID,'" + SSQ + "' FROM "
									+ wTbl.get(ixx) + "#TXT"
									+ " WHERE SUBSTRING(" + wMother + "," + SSQ
									+ ",1)" + " = '1'";
						} // end_if;
						if (!wOth.equals(""))
							wOth = wOth + " and ";
						wOth = wOth + qx_Key + " is null ";
						// System.out.println(wSQL);
						pMsq.add(
								pTbl.get(jxx) + vbTab + wMother + vbTab + wSQL);
						pAns.add(SSQ);
					} else {
						wMlx = wMlx + 1;
						pMsq.add(""); // Preserve wMlx);
						wSQL = "SELECT ID,'NA' AS " + wMother + " FROM "
								+ wTbl.get(ixx) + "#TXT" + " WHERE " + wOth;
						// mod@2011_0118;
						if (neo == true) {
							wSQL = "SELECT ID,'NA' AS " + wMother + " FROM "
									+ wTbl.get(ixx) + "#TXT" + " WHERE "
									+ wMother + " is NULL";
						} // end_if;
							// System.out.println(wSQL);
						pMsq.add(
								pTbl.get(jxx) + vbTab + wMother + vbTab + wSQL);
						pAns.add("NA");
					} // end_if;
						// System.out.println("pMsq(wMlx):" + pMsq(wMlx));
				} else {
					pAns.add(qx_Val);
				} // end_if;
			} // end_if;
		} // Next i;
		pHed = new ArrayList(); // ReDim pHed(2);
		pHed.add(wTbl);
		pHed.add(wCm1);
		pHed.add(wCm2);
		ParamObj pObj = new ParamObj();
		pObj.push("Tbl", pTbl);
		pObj.push("Tgt", pTgt);
		pObj.push("Ans", pAns);
		pObj.push("Hed", pHed);
		pObj.push("Msq", pMsq);
		return pObj;
	} // <Sub>;

	public static void testSQL2CSV_COPY() {
	}

	// 2011-07-07　検証中・・・
	public static List SQL2CSV_COPY(String pDir, String pPfx, Object outlet,
			FountainHead objFH) {
		List pUqt = new ArrayList();// Collection2 ? Variant;
		int seq = 0;
		if (Trim(pPfx).equals(""))
			pPfx = "T";
		// For Each wTable In objFH.keyList;
		for (String aliasPath : objFH.keyList) {
			outlet = "サーバーよりデータ取得中・・・";
			seq = seq + 1;
			String tableName = pPfx + Right("00" + seq, 2);
			// outPath ex> c:\\USERID\\TEMP\\T01.TXT;
			String outPath = pDir + "\\" + tableName + ".TXT";
			System.out.println("サーバーよりデータ取得中・・・@SQL2CSV_COPY");
			System.out.println("【" + seq + "】 " + aliasPath + "   ");
			if (InStr(aliasPath, "\\ALIAS") > 0) {
				List xFld = objFH.getArray(aliasPath);
				CpyFromDB(aliasPath, outPath, xFld);
			} else {
				// ISAMの場合フィールドを加工しない;
				Schemes.SchemeCpy(aliasPath, outPath);
			} // end_if;
				// ※此処で書き換えている;
			pUqt.add(tableName + "#TXT"); // Preserve i);
			System.out.println(" ");
			// System.out.println("table:" + wTable)
			// Set list = hashVal(wTable)
			// For Each wVal In list
			// System.out.println("    field:" + wVal)
			// } // Next
		} // Next;
		return pUqt;
	} // <function>;

	public static void testCpyFromDB() {
		String rootDir = globals.ResControl.getQprRootDir();
		String aliasPath = ResControl.ENQ_DIR + "NQ/属性・性年代編/2010/alias.txt";
		String outPath = rootDir + "kentest/testCpyFromDB";
		List xFld = new ArrayList();
		CpyFromDB(aliasPath, outPath, xFld);
	}

	// 検証中　2011-07-05　 フィールドが空の場合のみチェックした
	public static int CpyFromDB(String aliasPath, String outPath, List xFld) {
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// 概要: 入力パス上のスキーマファイルから指定されたファイルの情報のみを抜き出し出力先に追い書きする
		// （※該当ファイルのコピーも同時に行う）
		// 引数: xInput :入力パス
		// xOutput :出力パス
		// 戻り値: 出力ライン数
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// ※出力パスおよび出力先のスキーマが存在しない場合は作成されます
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// 使用例: ? CpyFromDB("INPUT","C:\\" + gUSER + "\\@QBR\\TEMPS\\T01.TXT")
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String DQ = "\"";
		int xLct = 0;
		int xPoz = InStr_R(outPath, "\\");
		if (xPoz == 0)
			return -1; // Exit Function;
		String outDir = Left(outPath, xPoz - 1);
		String xName = Mid(outPath, xPoz + 1);
		String schema = outDir + "/" + SCHEMA_INI;//schema
		// ########################################################################################
		// # Schemes.SchemeRmv
		// ########################################################################################
		Schemes.SchemeRmv(outPath, true);
		if (!new File(outDir).isDirectory())
			MakeDir(outDir, false);
		String wFields = "";
		String xSql = "";
		// -------+---------+---------+---------+---------+---------+---------+---------+---------+
		// ParseAlias エイリアスファイルを読み込む;
		// -------+---------+---------+---------+---------+---------+---------+---------+---------+
		if (!new File(aliasPath).isFile()) {
			MsgBox("エイリアスが見つかりませんでした：" + aliasPath);
			return -1; // Exit Function;
		}
		// ########################################################################################
		// # ParseAlias
		// ########################################################################################
		HashMap<String, String> hmap = ParseAlias(aliasPath);
		if (hmap == null) {
			System.out.println("Error @ParseAlias");
			return -1;
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(schema, true));
			if (bw == null)
				return -1; // Exit Function;
			bw.write("[" + xName + "]" + LF);
			bw.write(COL_NAME_HEADER_FALSE + LF);
			bw.write(FORMAT_CSV_DELIMITED + LF);
			bw.write(MAX_SCAN_ROWS_0 + LF);
			bw.write(CHARACTER_SET_OEM + LF);
			xLct = 1;
			bw.write("Col" + xLct + "=" + DQ + "ID" + DQ
					+ " Char     Width 15" + LF);
			xSql = " FROM " + hmap.get("Tbl") + " AS BASETABLE";
			wFields = "BASETABLE." + hmap.get("Key") + " as ID";
			// -<<LOOP>>-------------------------------------------------------------------------------;
			System.out.println("UBound(xFld):" + xFld.size());
			for (Iterator it = xFld.iterator(); it.hasNext();) {
				String wStr = (String) it.next();
				if (wStr != null)
					wStr = Trim(wStr);
				System.out.println("wStr:" + wStr);
				if (wStr != null && !wStr.equals("")) {
					xLct = xLct + 1;
					String[] wChunk = wStr.split(vbTab);
					if (!wFields.equals(""))
						wFields = wFields + ",";
					if (wChunk[1].equals("FIXX")) {
						wFields = wFields + " 1 AS FIXX";
						wChunk[2] = "Char Width 1";
					} else {
						wFields = wFields + wChunk[0] + " AS " + wChunk[1];
					}
					bw.write("Col" + xLct + "=" + DQ + wChunk[1] + DQ + " "
							+ wChunk[2] + LF);
					// XXX ISAMのフィールド数の上限をチェックする;
				}
			}
			// for (int idx = 0; idx <= UBound(xFld); idx++) {
			// String wStr = xFld[idx];
			// if ( wStr!=null) wStr=Trim(wStr);
			// System.out.println("wStr:" + wStr);
			// if (wStr!=null && !wStr.equals("")) {
			// xLct = xLct + 1;
			// String[] wChunk = wStr.split(vbTab);
			// if (!wFields.equals(""))
			// wFields = wFields + ",";
			// if (wChunk[1].equals("FIXX")) {
			// wFields = wFields + " 1 AS FIXX";
			// wChunk[2] = "Char Width 1";
			// } else {
			// wFields = wFields + wChunk[0] + " AS " + wChunk[1];
			// }
			// xFn2.write("Col" + xLct + "=" + DQ + wChunk[1] + DQ + " " +
			// wChunk[2] + LF);
			// // XXX ISAMのフィールド数の上限をチェックする;
			// }
			// }
			xSql = "SELECT " + wFields + xSql;
			xSql = CnvChr(xSql, "$", hmap.get("Fld")) + " " + hmap.get("Cnd");
			System.out.println("AfterCnv xSQL:" + xSql);

			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		if (new File(outPath).isFile())
			Kill(outPath);
		if (xLct > 0) {
			System.out.println(" xCnn:" + hmap.get("Cnn"));
			System.out.println(" xSQL:" + xSql);
			// XXX 効率が悪い感じなので別のモジュールを用意してみる;
			boolean trimFlag = false;
			// trimFlag = true 2011_0118 yuasa...
			// 問題ないだろうか？引き数が問題ないか？？？？？？
			ODBC_SQL2TEX(hmap.get("Cnn"), xSql, outPath, false, trimFlag, true,
					null);
			return (xLct + 5);
		} else {
			return -1;
		}
		// VBのエラー処理の残骸　----+---------+---------+---------+---------+---------+---------+---------+
		// CpyFromDB_Err:
		// String xSelect = Err;
		// if(false){ ;
		// } else if ( xSelect.equals(53: MsgBox("エラー 53) ){ ファイルが見つかりません。");
		// } else { // : MsgBox("on ooRDO_Err " + Err + " が発生しました" +
		// Error(Err));
		// }// end select;
		// return rtnVal;
	}

	// public void SQL2CSV_COPY_orgxxx(String pPath, String[] gUqt, String[]
	// xSub,
	// String pPfx, Object outlet) {
	// // int i = 0;
	// String tableName = "";
	// String outPath = "";
	// if (Trim(pPfx).equals(""))
	// pPfx = "T";
	// for (int i = 1; i <= UBound(xSub); i++) {
	// outlet = "サーバーよりデータ取得中・・・";
	// // DoEvents;
	// tableName = pPfx + Right("00" + i, 2);
	// outPath = pPath + "\\" + tableName + ".TXT";
	// System.out.println("【" + i + "】 " + gUqt[i] + "   ");
	// if (InStr(gUqt[i], "\\ALIAS") > 0) {
	// List xFld;// Collection2 ? Variant;
	// xFld = new ArrayList();
	// xFld.add(xSub[i]);
	// CpyFromDB(gUqt[i], outPath, xFld);
	// } else {
	// // ISAMの場合フィールドを加工しない;
	// Schemes.SchemeCpy(gUqt[i], outPath);
	// } // end_if;
	// gUqt[i] = tableName + "#TXT";
	// System.out.println(" ");
	// } // Next i;
	// } // <Sub>;

	public static void main(String[] argv) {
		// testParseAlias();
		testCpyFromDB();
	}

} // end of class
