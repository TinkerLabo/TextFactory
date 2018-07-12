package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

/**************************************************************************
 * TeeClojure				
 * @author	ken yuasa
 * @version	1.0
 * <br>検査対象カラムの値がセットに含まれているかどうかにより、レコードを振り分ける。
 * <br>(totalOptがtrueなら、当該カラムが　rec[col] = TOTAL;　となる）
 **************************************************************************/
public class TeeClojure implements Inf_BaseClojure {
	private static final String TARGET = "target";
	private static final String TOTAL = "total";
	private String delimiter = "\t";
	private static final String LF = System.getProperty("line.separator");
	private String oFile_T;
	private String oFile_F;
	private int col;
	private Set<String> hashSet;
	private BufferedWriter bw_T;
	private BufferedWriter bw_F;
	private int counter_T = 0;
	private int counter_F = 0;

	private boolean otherOpt = false;
	private boolean totalOpt = false;
	private boolean targetOpt = false;

	public void setOtherOpt(boolean otherOpt) {
		this.otherOpt = otherOpt;
	}

	public void setTotalOpt(boolean totalOpt) {
		this.totalOpt = totalOpt;
	}

	public void setTargetOpt(boolean targetOpt) {
		this.targetOpt = targetOpt;
	}

	private String stat = "";

	public int getCounter1() {
		return counter_T;
	}

	public int getCounter2() {
		return counter_F;
	}

	//	String tgt_Path = calcDir + _PRF1 + "trrTrue.txt";	//target
	//	String ttl_Path = calcDir + _PRF2 + "trrFalse.txt";	//total
	/**************************************************************************
	 * TeeClojure				
	 * <br>検査対象カラムの値がセットに含まれているかどうかにより、レコードを振り分ける。
	 * <br>(totalOptがtrueなら、当該カラムが　rec[col] = TOTAL;　となる）
	 * @param oFile_T	出力ファイルパス１（検査対象カラムの値がリストに含まれていた場合の出力）	trrTrue.txt	 
	 * @param oFile_F	出力ファイルパス２（検査対象カラムの値がリストに含まれない場合の出力）	trrFalse.txt	 
	 * @param col		検査対象カラム	 
	 * @param hashSet	ハッシュセット（検査対象カラムの内容がコレに一致するかどうか）
	 * 
	<br>
	<br><hr>入力ファイル【※重要】
	<br>
	<table border='1'>
	<tr bgcolor='DeepSkyBlue'><td>カラム</td><td>項目</td></tr>
	<tr><td> 0</td><td>Monitor モニターID</td></tr>
	<tr><td> 1</td><td>Price 購入価格</td></tr>
	<tr><td> 2</td><td>Count 購入数量</td></tr>
	<tr ><td> 3</td><td>break　指定品目より合成した区分</td></tr>
	<tr><td> 4</td><td>Shop1 購入先</td></tr>
	<tr><td> 5</td><td>Accept 購入日付</td></tr>
	<tr><td> 6</td><td>Flag3 フラグ３</td></tr>
	<tr><td> 7</td><td>Flag1 フラグ１</td></tr>
	<tr><td> 8</td><td>Flag2 フラグ２</td></tr>
	<tr><td> 9</td><td>Shop2 購入先(大店区分）</td></tr>
	<tr><td> 10</td><td>YM 年月</td></tr>
	<tr><td> 11</td><td>hh 購入時間</td></tr>
	<tr><td> 12</td><td>idx 購入時間３０分刻み</td></tr>
	<tr><td> 13</td><td>week 購入曜日</td></tr>
	<tr><td> 14</td><td>capa　容量</td></tr>
	</table>
	<br>
	<br><hr>入力ファイル　【サンプル】　
	<br>
	<table border='1'>
	<tr bgcolor='DeepSkyBlue'><td>#0_Id</td><td>#1_Price</td><td>#2_Count</td><td>#3_Break</td><td>#4_Shop1</td><td>#5_AcceptDate</td><td>#6_Flg3</td><td>#7_Flg1</td><td>#8_Flg2</td><td>#9_Shop2</td><td>#10_ym</td><td>#11_hh</td><td>#12_Idx</td><td>#13_Week</td><td>#14_Capa</td></tr>
	<tr><td>73302423</td><td>359</td><td>1</td><td>00002: 生ラーメン</td><td>1V</td><td>20141018</td><td> </td><td> </td><td> </td><td>1</td><td>1410</td><td>15</td><td>30</td><td>6</td><td>336000</td></tr>
	<tr><td>71205401</td><td>321</td><td>1</td><td>00002: 生ラーメン</td><td>71</td><td>20140924</td><td> </td><td> </td><td> </td><td>7</td><td>1409</td><td>18</td><td>37</td><td>3</td><td>336000</td></tr>
	<tr><td>74136616</td><td>420</td><td>1</td><td>00004: 生うどん</td><td>10</td><td>20141224</td><td> </td><td> </td><td> </td><td>1</td><td>1412</td><td>13</td><td>27</td><td>3</td><td>424000</td></tr>
	<tr><td>74136616</td><td>418</td><td>1</td><td>00004: 生うどん</td><td>10</td><td>20141001</td><td> </td><td> </td><td> </td><td>1</td><td>1410</td><td>13</td><td>27</td><td>3</td><td>424000</td></tr>
	<tr><td>71596376</td><td>409</td><td>2</td><td>00004: 生うどん</td><td>10</td><td>20141219</td><td> </td><td> </td><td> </td><td>1</td><td>1412</td><td>16</td><td>33</td><td>5</td><td>424000</td></tr>
	</table>		 
	<br>
	<br><hr>出力ファイル１　【サンプル】trrTrue.txt　　『00004: 生うどん』が対象の場合
	<br>
	<table border='1'>
	<tr bgcolor='DeepSkyBlue'><td>#0</td><td>#1</td><td>#2</td><td>#3</td><td>#4</td><td>#5</td><td>#6</td><td>#7</td><td>#8</td><td>#9</td><td>#10</td><td>#11</td><td>#12</td><td>#13</td><td>#14</td></tr>
	<tr><td>74136616</td><td>420</td><td>1</td><td>00004: 生うどん</td><td>10</td><td>20141224</td><td> </td><td> </td><td> </td><td>1</td><td>1412</td><td>13</td><td>27</td><td>3</td><td>424000</td></tr>
	<tr><td>74136616</td><td>418</td><td>1</td><td>00004: 生うどん</td><td>10</td><td>20141001</td><td> </td><td> </td><td> </td><td>1</td><td>1410</td><td>13</td><td>27</td><td>3</td><td>424000</td></tr>
	<tr><td>71596376</td><td>409</td><td>2</td><td>00004: 生うどん</td><td>10</td><td>20141219</td><td> </td><td> </td><td> </td><td>1</td><td>1412</td><td>16</td><td>33</td><td>5</td><td>424000</td></tr>
	<tr><td>76835104</td><td>408</td><td>1</td><td>00004: 生うどん</td><td>10</td><td>20141009</td><td> </td><td> </td><td> </td><td>1</td><td>1410</td><td>11</td><td>23</td><td>4</td><td>424000</td></tr>
	<tr><td>73521373</td><td>408</td><td>1</td><td>00004: 生うどん</td><td>10</td><td>20140905</td><td> </td><td> </td><td> </td><td>1</td><td>1409</td><td>20</td><td>41</td><td>5</td><td>424000</td></tr>
	</table>
	<br>
	<br><hr>出力ファイル２　【サンプル】trrFalse.txt　
	<br>
	<table border='1'>
	<tr bgcolor='DeepSkyBlue'><td>#0</td><td>#1</td><td>#2</td><td>#3</td><td>#4</td><td>#5</td><td>#6</td><td>#7</td><td>#8</td><td>#9</td><td>#10</td><td>#11</td><td>#12</td><td>#13</td><td>#14</td></tr>
	<tr><td>73624673</td><td>410</td><td>1</td><td>total</td><td>14</td><td>20141122</td><td> </td><td> </td><td> </td><td>1</td><td>1411</td><td>10</td><td>20</td><td>6</td><td>336000</td></tr>
	<tr><td>74077743</td><td>410</td><td>1</td><td>total</td><td>19</td><td>20141207</td><td> </td><td> </td><td> </td><td>1</td><td>1412</td><td>16</td><td>33</td><td>0</td><td>336000</td></tr>
	<tr><td>77841267</td><td>409</td><td>1</td><td>total</td><td>19</td><td>20141121</td><td> </td><td> </td><td> </td><td>1</td><td>1411</td><td>11</td><td>23</td><td>5</td><td>336000</td></tr>
	<tr><td>70278857</td><td>367</td><td>3</td><td>total</td><td>19</td><td>20141124</td><td> </td><td> </td><td> </td><td>1</td><td>1411</td><td>13</td><td>26</td><td>1</td><td>336000</td></tr>
	<tr><td>73010796</td><td>355</td><td>1</td><td>total</td><td>19</td><td>20141102</td><td> </td><td> </td><td> </td><td>1</td><td>1411</td><td>20</td><td>40</td><td>0</td><td>336000</td></tr>
	</table>
	
	 **************************************************************************/
	public TeeClojure(String oFile_T, String oFile_F, int col,
			Set<String> hashSet) {
		this.counter_T = 0;
		this.counter_F = 0;
		this.oFile_T = oFile_T;
		this.oFile_F = oFile_F;
		this.col = col;
		this.hashSet = hashSet;
		//		for (String element : hashSet) {
		//			System.out.println("■@TeeClojure set:" + element);
		//		}
	}

	public String getStat() {
		return stat;
	}

	public void write_T(String[] rec) {
		if (targetOpt)
			rec[col] = TARGET;
		String xRec = join(rec, delimiter);
		try {
			bw_T.write(xRec);
			bw_T.write(LF);
			counter_T++;
		} catch (Exception e) {
			System.out.println("#Error @TeeClojure.write_T():" + e.toString());
			e.printStackTrace();
		}
	}

	public void write_F(String[] rec) {
		if (totalOpt)
			rec[col] = TOTAL;
		String xrec = join(rec, delimiter);
		try {
			bw_F.write(xrec);
			bw_F.write(LF);
			counter_F++;
		} catch (Exception e) {
			System.out.println("#Error @TeeClojure.write_F{}:" + e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		// open
		try {
			bw_T = new BufferedWriter(new FileWriter(oFile_T));
			bw_F = new BufferedWriter(new FileWriter(oFile_F));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public void execute(String rec) {
		execute(rec.split(delimiter));
	}

	@Override
	public void execute(String[] rec) {
		if (rec.length > col) {
			if (hashSet.contains(rec[col])) {
				//				System.out.println("@@@TeeClojure rec[col]:" + rec[col]);
				write_T(rec);
				if (otherOpt) {
					write_F(rec);
				}
			} else {
				write_F(rec);
			}
		}
	}

	// ------------------------------------------------------------------------
	// counter1が０なら「一件もヒットしなかった」ということになる
	// ------------------------------------------------------------------------
	@Override
	public void write() {
		try {
			bw_T.close();
			bw_F.close();
		} catch (Exception e) {
			System.out.println("#Error @TeeClojure:" + e.toString());
		}
		stat = "";
		if (counter_T == 0) {
			stat = "#ERROR　軸にマッチする項目が存在しませんでした！";
		}
	}

	public static void main(String[] argv) {
		teeClojureTest();
	}

	public static void teeClojureTest() {
		String userDir = globals.ResControl.getQPRHome();
		System.out.println("userDir:" + userDir);
		String otPath = userDir + "828111000507/calc/true.txt";
		String ofPath = userDir + "828111000507/calc/false.txt";
		String inPath = userDir + "828111000507/calc/trrModItp.txt";
		int col = 3;
		HashSet<String> hashSet = new HashSet();
		hashSet.add("[40691] 発泡酒");
		hashSet.add("[40641] ビール");
		// [40691] 発泡酒
		TeeClojure closure = new TeeClojure(otPath, ofPath, col, hashSet);
		new CommonClojure().incore(closure, inPath, true);
	}
}
