package kyPkg.mySwing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kyPkg.sql.IsamConnector;
import kyPkg.uFile.FileUtil;

public class EnqRes {
	protected static final String vbTab = "\t";
	protected static final String VTAB = "" + '\u000b'; // êÇíºÉ^Éu

	private static List<List> ODBC_SQL2VECTOR(String xSql, String iPath) {
		List<List> matrix = null;
		if (FileUtil.isExists(iPath)) {
			System.out.println("xSql:" + xSql);
			matrix = new IsamConnector(iPath).query2Matrix(xSql);
		}
		return matrix;
	}

	public static void Dimension_Load2(String pPath, InfListPanel outlet,
			String pLimit, String pComm) {
 
		outlet.clear();
		String xNam;
		String xKey;
		String xTbl;
		String xTyp;
		String xFld;
		String[] wOpts;
		String wOpt;
		String wProxy;
		xTbl = "";
		if (FileUtil.isExists(pPath + "/ALIAS.TXT")) {
			xTbl = "ALIAS";
			wProxy = "$"; // $$$
		} else {
			wProxy = "";
			if (pLimit.equals("M01")) {
				xTbl = "RCL";
			} else if (pLimit.equals("Q01")) {
				xTbl = "RCL";
			} else {
				xTbl = "ASM";
			}
		}
		String xComm;
		String xParm;
		// ---------------------------------------------------------------------
		int xmax;
		// String xCnn;
		String xSql;
		if (pLimit.equals("M01")) {
			xSql = "SELECT Nam,Key,Typ,Opt FROM RTB1#TXT ORDER BY Srt";
		} else if (pLimit.equals("Q01")) {
			xSql = "SELECT Nam,Key,Typ,Opt FROM RTB1#TXT ORDER BY Srt";
		} else {
			xSql = "SELECT Nam,Key,Typ,Opt FROM QTB1#TXT WHERE (Typ = 'SINGLE' OR Typ = 'MULTI') AND Mot = 'ROOT' ORDER BY Srt";
		}
		List xVector = ODBC_SQL2VECTOR(xSql, pPath);
		if (xVector != null && xVector.size() > 0) {
			xmax = xVector.size();
		} else {
			System.out.println("#error @SQL");
			return;
		}
		List listData = new ArrayList();

		if (xmax > 0) {
			for (Iterator iterator = xVector.iterator(); iterator.hasNext();) {
				List list = (List) iterator.next();
				String val = (String) list.get(0);
				if (val.equals("")) {
				} else if (val.equals("ID")) {
				} else if (val.equals("SAMPLENUMBER")) {
				} else {
					xFld = wProxy;
					xNam = (String) list.get(0);
					xKey = (String) list.get(1);
					xTyp = (String) list.get(2);
					wOpt = (String) list.get(3);
					if (wOpt != null) {
						wOpts = wOpt.split(":");
						if (wOpts.length > 0) {
							if (!wOpts[0].trim().equals(""))
								xFld = wOpts[0];
						}
					}
					xParm = vbTab + pPath + vbTab + xTbl + vbTab + xKey + vbTab
							+ xFld + vbTab + xTyp;
					// comm ê›íË
					if (!pLimit.equals("")) {
						if (xKey.indexOf("@") > 0) {
							if (xKey.indexOf(pLimit) > 0) {
								xComm = pComm.trim() + " " + xNam.trim();
							} else {
								xComm = "";
							}
						} else {
							xComm = xNam.trim() + " ";
						}
					} else {
						xComm = xNam.trim() + " ";
					}
					if (!xComm.equals("")) {
						// System.out.println("xComm:" + xComm+ " xParm:" +
						// xParm);
						listData.add(xComm + VTAB + xParm);
						// outlet.AddItem
						// outlet.List(outlet.ListCount - 1, 0) = xComm
						// outlet.List(outlet.ListCount - 1, 1) = xParm
					}
				}
			}
			outlet.setListData(listData);
		}
	}

}
