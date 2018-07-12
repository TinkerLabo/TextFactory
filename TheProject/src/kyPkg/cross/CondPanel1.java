package kyPkg.cross;

import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kyPkg.mySwing.ListPanel;
import kyPkg.mySwing.MyButton;
import kyPkg.mySwing.MyCheckBox;
import kyPkg.mySwing.MyLabel;
import kyPkg.mySwing.MyPanel;

public class CondPanel1 extends MyPanel {
	private static final long serialVersionUID = 1L;
	private static HashMap<String, List> xUqt = new HashMap();
	// private static String[] xUqt;
	// private static String[] xSub;
	static int gCndx = 0;
	private static Color darkRed = new Color(128, 0, 0);
	private MyCheckBox optionRel1a;
	private MyCheckBox optionRel2a;
	private ListPanel listQtb3a;
	private MyLabel LabInfo_a;
	private MyButton comJaminA;
	private boolean gDIRTY = false;

	public boolean isgDIRTY() {
		return gDIRTY;
	}

	public CondPanel1(int x, int y, int width, int height, LayoutManager layout) {
		super(x, y, width, height, layout);
		this.setGrid(true);
		listQtb3a = new ListPanel(new Rectangle(0, 0, 500, 80), false, false);
		MyButton comClr_a = new MyButton(500, 0, 150, 20, "条件をクリア");
		comJaminA = new MyButton(500, 20, 150, 20, "件数確認");
		optionRel1a = new MyCheckBox(510, 50, 100, 20, "ＡＮＤ 条件");
		optionRel2a = new MyCheckBox(510, 70, 100, 20, "ＯＲ   条件");

		LabInfo_a = new MyLabel(0, 80, 500, 20, "条件設定後検索ボタンを押してください!!");
		LabInfo_a.setColor(Color.YELLOW, darkRed);
		this.add(LabInfo_a);

		this.add(listQtb3a);
		this.add(comClr_a);
		this.add(comJaminA);
		this.add(optionRel1a);
		this.add(optionRel2a);
		comClr_a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comClr_a_Click();
				repaint();
			}
		});
		comJaminA.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comJamin_a_Click();
				repaint();
			}
		});

	}

	public ListPanel getListPanel() {
		return listQtb3a;
	}

	public void comClr_a_Click() {
		LabInfo_a.setText("");
		// LabInfo_ca.setText("");
		listQtb3a.clear();
	}

	public void comJamin_a_Click() {
		// System.out.println("@ #comJamin_a_Click");
		comJaminA.setEnabled(false);
		LabInfo_a.setText("");
		// LabInfo_ca.setText("");
		if (listQtb3a.listCount() >= 1) {
			String gPath_Tmp = "";
			gCndx = gCndx
					+ ComJamin_Plus(listQtb3a, gPath_Tmp, "AX1.txt",
							optionRel1a.isSelected(), LabInfo_a);
			// LabInfo_ca.setText(LabInfo_a.getText());
		}
		comJaminA.setEnabled(true);
	}

	private int UniqueTbl2(String pKey, String pVal) {
		System.out.println("@UniqueTbl2 pKey:" + pKey);
		System.out.println("            pVal:" + pVal);
		// -----------------------------------------------------------------------------------------------------
		// pKeyでxUqt()を検索し、発見した場合そのインデックスを、無い場合は追加してそのインデックスを返す
		// ※注意！インデックスは１から始まる
		// -----------------------------------------------------------------------------------------------------
		List<String> list = xUqt.get(pKey);
		if (list == null) {
			list = new ArrayList();
		}
		list.add(pVal);
		xUqt.put(pKey, list);
		return xUqt.size();
	}

	// Function UniqueTbl2(pKey As String, pVal As String, ByRef xUqt() As
	// String, ByRef xSub() As Variant) As Integer
	// Dim i As Integer
	// Dim j As Integer
	// Dim wSq1 As Integer
	// Dim wSq2 As Integer
	// Dim xOcc() As Variant
	// '-------------------------------------------------------------------------------------------------

	// If wSq1 = -1 Then 'NOT FOUND!
	// wSq1 = UBound(xUqt) + 1
	// ReDim Preserve xUqt(wSq1)
	// ReDim Preserve xSub(wSq1)
	// xUqt(wSq1) = pKey
	// ReDim xOcc(1)
	// xOcc(1) = pVal
	// xSub(wSq1) = xOcc()
	// Else 'FOUND!
	// ReDim xOcc(UBound(xSub(wSq1)))
	// wSq2 = -1
	// For j = 1 To UBound(xSub(wSq1))
	// xOcc(j) = xSub(i)(j)
	// If xSub(i)(j) = pVal Then
	// wSq2 = j
	// Exit For
	// End If
	// Next j
	// If wSq2 = -1 Then
	// wSq2 = UBound(xOcc) + 1
	// ReDim Preserve xOcc(wSq2)
	// xOcc(wSq2) = pVal
	// xSub(wSq1) = xOcc()
	// End If
	// End If
	// UniqueTbl2 = wSq1
	// End Function

	public int ComJamin_Plus(ListPanel wCtrl, String gPath_Tmp, String pFile,
			boolean pAnd, MyLabel outlet) {
		List<String> wTnm = new ArrayList();
		List<List> wCnd = new ArrayList();

		System.out.println("@ #ComJamin_Plus wCtrl.listCount():"
				+ wCtrl.listCount());
		for (int i = 0; i < wCtrl.listCount(); i++) {
			// array_type array_element = array[i];
			// wStr = wCtrl.List(i, 1)
			String wStr = (String) wCtrl.getValueX(i, 1);
			System.out.println("wStr:" + wStr);
			String vbTab = "\t";
			String[] wChunk = wStr.split(vbTab);
			System.out.println("wChunk.length:" + wChunk.length);

			if (wChunk.length == 0)
				return 0;
			if (wChunk.length < 8) {
				// MsgBox "Error!!"
				return 0;
			}
			String xPth = wChunk[0];
			String xTbl = wChunk[1];
			String xDat = wChunk[2];
			String xKey = wChunk[3];
			String xVal = wChunk[4];
			String xTyp = wChunk[5];
			String xCol = wChunk[6];
			String xLen = wChunk[7];
			String xCnd = wChunk[8];
			String wPath = xPth + "/" + xTbl + ".TXT";
			System.out.println("xPth  :" + xPth);
			System.out.println("xTbl  :" + xTbl);
			System.out.println("xDat  :" + xDat);
			System.out.println("xKey  :" + xKey);
			System.out.println("xVal  :" + xVal);
			System.out.println("xTyp  :" + xTyp);
			System.out.println("xCol  :" + xCol);
			System.out.println("xLen  :" + xLen);
			System.out.println("xCnd  :" + xCnd);
			System.out.println("wPath :" + wPath);
			// -+---------+---------+---------+---------+---------+---------+---------+---------+---------+
			// UniqueTbl!!
			// -+---------+---------+---------+---------+---------+---------+---------+---------+---------+
			String wVal = "";
			if (!xDat.equals("")) {
				wVal = "SUBSTRING(" + xDat + "," + xCol + "," + xLen + ")"
						+ vbTab + xKey + vbTab + " Char Width " + xLen;
			}
			int wSeq = UniqueTbl2(wPath, wVal); // ■ UniqueTbl2 ■
			// '-+---------+---------+---------+---------+---------+---------+---------+---------+---------+
			// ReDim Preserve wTnm(wSeq)
			// ReDim Preserve wCnd(wSeq)
			// wTnm(wSeq) = "X" & Right("00" & wSeq, 2)
			// If wCnd(wSeq) <> "" Then wCnd(wSeq) = wCnd(wSeq) & vbTab
			// wCnd(wSeq) = wCnd(wSeq) & "[" & xKey & "]" & xCnd '[KEY] + COND


			String val = "X" + ((String) "00" + wSeq).substring(0, 2);
			System.out.println("wSeq :" + wSeq);
			System.out.println("val  :" + val);
			wTnm.set(wSeq, val);
			List<String> cndList = wCnd.get(wSeq);
			if (cndList == null) {
				cndList = new ArrayList();
				wCnd.set(wSeq, cndList);
			}
			cndList.add("[" + xKey + "]" + xCnd);
		}
		List<List> xMatrix = new ArrayList<List>();
		xMatrix = wCnd;
		String xFile_I;
		String xFile_O;

		for (int i = 0; i < wTnm.size(); i++) {
			// xFile_I = pUqt(i)
			xFile_O = gPath_Tmp + "/" + wTnm.get(i) + ".TXT"; // output
			// -+---------+---------+---------+---------+---------+---------+---------+---------+---------+
			// SchemeCpy
			// -+---------+---------+---------+---------+---------+---------+---------+---------+---------+
			// Call SQL2CSV_COPY(gPath_Tmp, pUqt(), pSub(), "X", outlet)
			wTnm.set(i, wTnm.get(i) + "#TXT");
		}
		String wMsgx = "データを抽出中・・・しばらくお待ちください。";
		String xRel = "";
		if (pAnd == true) {
			xRel = "AND";
		} else {
			xRel = "OR";
		}
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// CreateAxSql
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String xSql = CreateAxSql(wTnm, xMatrix, xRel);
		String xCnn = "DRIVER={Microsoft Text Driver (*.txt; *.csv)};DefaultDir="
				+ gPath_Tmp
				+ ";DriverId=27;FIL=text;MaxBufferSize=16384;PageTimeout=5;";
		List<List> rtnVal = ODBC_SQL2VECTOR(xSql, gPath_Tmp);
		if (rtnVal == null || rtnVal.size() == 0) {
			wMsgx = "条件に当てはまるデータがありませんでした。";
		} else {
			// Call SchemaAx(gPath_Tmp);
			wMsgx = "該当データが" + rtnVal.size() + "件ありました。";
			gDIRTY = true;
		}
		return rtnVal.size();
	}

	// XXX テストする！！・・・20101216　
	public String CreateAxSql(List pTbl, List<List> pMatrix, String pRel) {
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// 概要 : テーブル合成#2
		// ※条件設定、使用テーブル等、軸（モニターｉｄ）を限定するのに使用;
		// 引数 pTbl() 合成するテーブル
		//
		// pMatrix() 限定条件を二次元配列化したもの
		// 戻り値 : ＳＱＬ文
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String xSql; // SQL;
		String xFld; // 比較用FIELD（左）;
		String xWhere; // Where;
		String xOrder; // Order By;
		String xAlias;
		String xTbl;
		String xJoin;
		String xOns;
		String xOnx;
		String xKey;
		// ---------+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		String xCond;
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		if (UBound(pTbl) > 30) {
			MsgBox("３０を超える項目の結合はできません！");
			return "";
		}
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		xSql = "";
		xFld = "";
		xWhere = "";
		xOrder = "";
		xAlias = "";
		xTbl = "";
		xJoin = "";
		xOns = "";
		xOnx = "";
		xKey = "";
		// ***< Where >***********************************************
		xJoin = "";
		xOnx = "";
		xWhere = "";
		for (int i = 0; i < UBound(pTbl); i++) {
			// System.out.plintln( pTbl(1)
			if (!pTbl.get(i).equals("")) {
				// ***< Table >***********************************************
				xAlias = "$" + Right("000" + i, 3);
				xTbl = "[" + pTbl.get(i) + "] as [" + xAlias + "]";
				// ***< Joint >***********************************************
				if (xJoin != "") {
					if (i == UBound(pTbl)) {
						xJoin = xJoin + " INNER JOIN " + xTbl;
					} else {
						xJoin = xJoin + " INNER JOIN (" + xTbl;
					}
				} else {
					xJoin = xTbl;
				}
				// ***< Compare fields >**************************************
				if (xFld != "")
					xOns = " ON" + xFld;
				xFld = " [" + xAlias + "].[ID] ";
				if (xKey.equals(""))
					xKey = xFld + "as ID "; // ←■■■■
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
				for (int k = 0; k < UBound(pMatrix); k++) {
					if (pMatrix.get(i).get(k) != "") {
						if (xCond != "") {
							xCond = xCond + " " + pRel + " [" + xAlias + "]."
									+ pMatrix.get(i).get(k);
						} else {
							xCond = "[" + xAlias + "]." + pMatrix.get(i).get(k);
						}
					}
				} // Next k;
				if (xCond != "") {
					if (UBound(pMatrix.get(i)) >= 1)
						xCond = "(" + xCond + ")";
					if (xWhere != "")
						xWhere = xWhere + " " + pRel;
					xWhere = xWhere + " " + xCond;
				}
				// -------+---------+---------+---------+---------+---------+---------+---------+---------+

			}
		} // Next i;
		if (!xWhere.equals(""))
			xWhere = " WHERE" + xWhere;
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		// SQL;
		// -----+---------+---------+---------+---------+---------+---------+---------+---------+---------+
		xSql = "SELECT distinct " + xKey + " FROM " + xJoin + xOnx + xWhere
				+ xOrder;
		return xSql;
	} // End Function;

	// Public Function ComJamin_Plus( _
	// Dim pSub() As Variant
	// Dim wCtrl As control
	// Dim wMsgx As control
	// Set wCtrl = CondList
	// Set wMsgx = outlet
	// Dim rtnVal As Integer
	// rtnVal = 0
	// Dim pUqt() As String
	//
	// Dim wChunk() As String
	// Dim wPath As String
	// Dim wStr As String
	// Dim wSeq As Integer
	//
	// Dim xPth As String
	// Dim xTbl As String
	// Dim xDat As String
	// Dim xKey As String
	// Dim xVal As String
	// Dim xTyp As String
	// Dim xCol As String
	// Dim xLen As String
	// Dim xCnd As String
	//
	// Dim wVal As String
	// Dim i As Integer

	// Dim xSql As String
	// Dim xCnn As String
	// Dim xmax As Integer
	// End Function

}
