package kyPkg.vbMods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LocalSqlMod extends VB_Base {
	public static String CreateAxSql(List<String> pTbl, List<List> pMatrixx,
			String pRel) {

		for (Iterator it = pMatrixx.iterator(); it.hasNext();) {
			List list = (List) it.next();
			for (Iterator it2 = list.iterator(); it2.hasNext();) {
				String member = (String) it2.next();
				System.out.println("dbg member:" + member);
			}
		}

		String CreateAxSql = null;
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// 概要 : テーブル合成#2
		// ※条件設定、使用テーブル等、軸（モニターｉｄ）を限定するのに使用
		// 引数 pTbl() 合成するテーブル
		//
		// pMatrix() 限定条件を二次元配列化したもの
		// 戻り値 : ＳＱＬ文
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		CreateAxSql = "";
		if (pTbl.size() > 30) {
			MsgBox("３０を超える項目の結合はできません！");
			return CreateAxSql; // Exit Function;
		} // end_if;
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String xSql = "";
		String xFld = "";
		String xWhere = "";
		String xOrder = "";
		String xAlias = "";
		String xTbl = "";
		String xJoin = "";
		String xOns = "";
		String xOnx = "";
		String xKey = "";
		String xCond = "";
		// ***< Where >***********************************************
		for (int i = 1; i < pTbl.size(); i++) {
			// System.out.println(pTbl(1));
			if (!pTbl.get(i).equals("")) {
				// ***< Table >***********************************************
				xAlias = "$" + Right("000" + i, 3);
				xTbl = "[" + pTbl.get(i) + "] as [" + xAlias + "]";
				// ***< Joint >***********************************************
				if (!xJoin.equals("")) {
					if (i == pTbl.size()) {
						xJoin = xJoin + " INNER JOIN " + xTbl;
					} else {
						xJoin = xJoin + " INNER JOIN (" + xTbl;
					} // end_if;
				} else {
					xJoin = xTbl;
				} // end_if;
				// ***< Compare fields >**************************************
				if (!xFld.equals(""))
					xOns = " ON" + xFld;
				xFld = " [" + xAlias + "].[ID] ";
				// if ( xKey.equals("") ) xKey = xFld +
				// "as ID,'f00002' as dTARGET " '←■■ ＷＡＴＣＨＯＵＴ！！ ■■
				if (xKey.equals(""))
					xKey = xFld + "as ID ";
				if (xOrder.equals(""))
					xOrder = " ORDER BY" + xFld;
				if (!xOns.equals(""))
					xOns = xOns + "=" + xFld;
				if (!xOnx.equals("")) {
					xOnx = xOns + ")" + xOnx;
				} else {
					xOnx = xOns + xOnx;
				} 
				// -------+---------+---------+---------+---------+---------+---------+---------+---------+
				xCond = "";
				List listC = pMatrixx.get(i);
				for (int k = 0; k < listC.size(); k++) {
					String valc = (String) listC.get(k);
					System.out.println("(" + i + "," + k + ") valc=>" +valc);
					if (!valc.equals("")) {
						if (!xCond.equals("")) {
							xCond = xCond + " " + pRel + " [" + xAlias + "]."+ valc;
						} else {
							xCond = "[" + xAlias + "]." + valc;
						} 
					} 
				} 
				if (!xCond.equals("")) {
					if (pMatrixx.get(i).size() >= 1)
						xCond = "(" + xCond + ")";
					if (!xWhere.equals(""))
						xWhere = xWhere + " " + pRel;
					xWhere = xWhere + " " + xCond;
				} 
				// -------+---------+---------+---------+---------+---------+---------+---------+---------+
			} 
		} 
		if (!xWhere.equals(""))
			xWhere = " WHERE" + xWhere;
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// SQL;
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		xSql = "SELECT distinct " + xKey + " FROM " + xJoin + xOnx + xWhere
				+ xOrder;
		// MsgBox("xSql" + xSql)
		// System.out.println("■xSql:" + xSql)
		CreateAxSql = xSql;
		// System.out.println("■xSql:" + xSQL)
		// DoEvents;
		return CreateAxSql;
	} 

//	public void RevertVector(List<List> pVector_I) {
//		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
//		// FOR 二次元版 縦と横を取り替えます
//		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
//		List<List> pVector_O;// List2 ? Variant;
//		int i_max = 0;
//		int j_max = 0;
//		List wArray;// List2 ? Variant;
//		i_max = pVector_I.size();
//		j_max = pVector_I.get(0).size();
//		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
//		pVector_O = new ArrayList(); // ReDim pVector_O(j_max);
//		wArray = new ArrayList(); // ReDim wArray(i_max);
//		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
//		for (int j = 0; j <= j_max; j++) {
//			pVector_O.set(j, wArray);
//		} // Next j;
//		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
//		for (int i = 0; i <= i_max; i++) {
//			for (int j = 0; j <= j_max; j++) {
//				pVector_O.get(j).set(i, (pVector_I.get(i).get(j)));
//			} // Next j;
//		} // Next i;
//		pVector_I = pVector_O;
//	} // <Sub>;

	public String GetSampleCount(List pArray) {
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// 概要 :指定されたアレイに示されたテーブルの合成カウントを求めるＳＱＬを返す
		// ※照合フィールドはID
		// 引数 テーブル名が入っている配列
		// 戻り値 :SQL文
		// 使用例 :
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		int wMax = 0;
		String xTbl = "";
		String xAlias = "";
		String xFld = "";
		String xSav = "";
		String xJoin = "";
		String xOnx = "";
		String xWhere = "";
		String xSql = "";
		wMax = pArray.size();
		if (wMax == 0) {
			MsgBox("Error on GetSampleCount!");
			return ""; // Exit Function;
		} // end_if;
		for (int i = 1; i <= wMax; i++) {
			// ***< Table >***********************************************
			xAlias = "$" + Right("000" + i, 3);
			xTbl = "[" + pArray.get(i) + "] as [" + xAlias + "]";
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
			if (!xFld.equals(""))
				xSav = " ON" + xFld;
			xFld = " [" + xAlias + "].[ID] ";
			if (!xSav.equals(""))
				xSav = xSav + "=" + xFld;
			if (!xOnx.equals("")) {
				xOnx = xSav + ")" + xOnx;
			} else {
				xOnx = xSav + xOnx;
			} // end_if;
		} // Next i;
		if (!xWhere.equals(""))
			xWhere = " WHERE" + xWhere;
		xSql = "SELECT count(*) as Sample FROM " + xJoin + xOnx + xWhere;
		System.out.println("xSql:" + xSql);
		return xSql;
	} // <function>;

	public void GetSampleCount_Drv() {
		List xArry = new ArrayList(); // ReDim xArry(1);
		xArry.add("TB1");
		String xSql = GetSampleCount(xArry);
		System.out.println(xSql);
	} // <Sub>;

	public void CreateSqlxdrv() {
		String xSql = "";
		List pVal = new ArrayList(); // ReDim pVal(1);
		pVal.add("ejqp7_030417_11451");
		pVal.add("ejqp7_030417_11462");
		// pVal(2) = "ejqp7_030417_11463"
		xSql = CreateSqlx("CEL2", "[BODY#TXT]", "CEL1", pVal);
		System.out.println(xSql);
	} // <Sub>;

	public String CreateSqlx(String pFls, String pTbl, String pWhf, List pVal) {
		String CreateSqlx = null;
		// ----------------------------------------------------------------------------------------------
		// 自己参照＆インナージョインＳＱＬ作成
		// ----------------------------------------------------------------------------------------------
		// pFls 出力および軸とするフィールド
		// pTbl 処理対象となるテーブル
		// pWhf 抽出条件（対象フィールド）
		// pVal() 抽出条件（値）
		// ----------------------------------------------------------------------------------------------
		// <SAMPLE>
		// SELECT [$01].[FIELD] FROM
		// [■] as [$01] INNER JOIN (
		// [■] as [$02] INNER JOIN [■] as [$03]
		// ON [$02].[FIELD] = [$03].[FIELD]
		// ) ON [$01].[FIELD] = [$02].[FIELD]
		// WHERE [$01].[△] = '□1' AND [$02].[△] = '□2' AND [$03].[△] = '□3';
		// ----------------------------------------------------------------------------------------------
		// ＜＜使用例＞＞
		// Dim pTbl As String
		// Dim pFld As String
		// Dim pVal(3) As String
		// Dim i As Integer
		// Dim xSql As String
		// for (int i = 0 ; i <= UBound(pVal); i ++) {
		// pVal[i] = "□" + i
		// } // Next i
		// pTbl = "[■]"
		// pFld = "△"
		// xSql = CreateSqlx("FIELD", pTbl, pFld, pVal())
		// System.out.println("xSql:" + xSql)
		// ----------------------------------------------------------------------------------------------
		int wMax = 0;
		String xTbl = "";
		String xAlias = "";
		String xFld = "";
		String xSav = "";
		String xJoin = "";
		String xOnx = "";
		String xWhere = "";
		String xGroup = "";
		// int i = 0;
		String xSql = "";
		String xFlds = "";
		CreateSqlx = "";
		wMax = pVal.size();
		for (int i = 0; i <= wMax; i++) {
			// ***< Table >***********************************************
			xAlias = "$" + Right("00" + i, 2);
			xTbl = "" + pTbl + " as [" + xAlias + "]";
			if (i == 0)
				xFlds = "[" + xAlias + "].[" + pFls + "]";
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
			if (!xFld.equals(""))
				xSav = " ON" + xFld;
			xFld = " [" + xAlias + "].[" + pFls + "] ";
			if (!xSav.equals(""))
				xSav = xSav + "=" + xFld;
			if (!xOnx.equals("")) {
				xOnx = xSav + ")" + xOnx;
			} else {
				xOnx = xSav;
			} // end_if;
			if (!xWhere.equals(""))
				xWhere = xWhere + " AND";
			// -------------------------------------------------------------------------------------;
			xWhere = xWhere + " [" + xAlias + "].[" + pWhf + "] = '"
					+ pVal.get(i) + "'";
			// -------------------------------------------------------------------------------------;
		} // Next i;
		if (!xWhere.equals(""))
			xWhere = " WHERE" + xWhere;
		// ***< SQL >*************************************************
		xSql = "SELECT " + xFlds + " FROM " + xJoin + xOnx + xWhere + xGroup
				+ ";";
		CreateSqlx = xSql;
		return CreateSqlx;
	} // <function>;

	public void CreateSqlZdrv() {
		String[] pTbl = new String[3];
		String[] pFld = new String[3];
		String[] pVal = new String[3];
		String xSql = "";
		for (int i = 0; i <= UBound(pTbl); i++) {
			pTbl[i] = "[■" + i + "]";
			pFld[i] = "△" + i;
			pVal[i] = "□" + i;
		}
		xSql = CreateSqlZ("FIELD", pTbl, pFld, pVal);
		System.out.println("xSql:" + xSql);
	}

	public String CreateSqlZ(String pFls, String[] pTbl, String[] pFld,
			String[] pVal) {
		// ----------------------------------------------------------------------------------------------
		// 複数のテーブルをインナージョインする
		// ----------------------------------------------------------------------------------------------
		int wMax = 0;
		String xTbl = "";
		String xAlias = "";
		String xFld = "";
		String xSav = "";
		String xJoin = "";
		String xOnx = "";
		String xWhere = "";
		String xGroup = "";
		// int i = 0;
		String xSql = "";
		String xFlds = "";
		// String rtnVal = "";
		wMax = UBound(pTbl);
		for (int i = 1; i <= wMax; i++) {
			// ***< Table >***********************************************
			xAlias = "$" + Right("00" + i, 2);
			xTbl = "" + pTbl[i] + " as [" + xAlias + "]";
			if (i == 1)
				xFlds = "[" + xAlias + "].[" + pFls + "]";
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
			if (!xFld.equals(""))
				xSav = " ON" + xFld;
			xFld = " [" + xAlias + "].[" + pFls + "] ";
			if (!xSav.equals(""))
				xSav = xSav + "=" + xFld;
			if (!xOnx.equals("")) {
				xOnx = xSav + ")" + xOnx;
			} else {
				xOnx = xSav;
			} // end_if;
			if (!xWhere.equals(""))
				xWhere = xWhere + " AND";
			// -------------------------------------------------------------------------------------;
			xWhere = xWhere + " [" + xAlias + "].[" + pFld[i] + "] = '"
					+ pVal[i] + "'";
			// -------------------------------------------------------------------------------------;
		} // Next i;
		if (!xWhere.equals(""))
			xWhere = "WHERE" + xWhere;
		// ***< SQL >*************************************************
		xSql = "SELECT " + xFlds + " FROM " + xJoin + xOnx + xWhere + xGroup
				+ ";";
		return xSql;
	} // <function>;
}
