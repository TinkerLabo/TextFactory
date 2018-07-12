package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;

import kyPkg.converter.Inf_ArrayCnv;

public class ShareCnv implements Inf_ArrayCnv {
	// -------------------------------------------------------------------------
	// 一行目をトータル行としてシェアを計算する
	// -------------------------------------------------------------------------
	java.text.DecimalFormat exFormat2 = new java.text.DecimalFormat("0.00");
	private long lCount = 0;
	private Integer[] targetCols = { 1, 2, 3, 4 };
	private List<Long> mothers;

	public ShareCnv() {
		super();
	}

	// ------------------------------------------------------------------------
	// init
	// ------------------------------------------------------------------------
	@Override
	public void init() {
	}

	// ------------------------------------------------------------------------
	// Convert
	/*
	<br>
	<br><hr>入出力ファイル　【サンプル】　
	<br>
	<table border='1'>
	<tr bgcolor='DeepSkyBlue'><td>#0</td><td>#1</td><td>#2</td><td>#3</td><td>#4</td></tr>
	<tr><td>    Total</td><td>55131697</td><td>401063</td><td>264553274000</td><td>92</td></tr>
	<tr><td>L0000: 炭酸飲料</td><td>8765013</td><td>70738</td><td>49544253000</td><td>92</td></tr>
	<tr><td>H0000: コーヒー飲料</td><td>7215592</td><td>61627</td><td>21407190000</td><td>92</td></tr>
	<tr><td>O0000: 乳性・乳酸菌</td><td>8246712</td><td>58881</td><td>14874327000</td><td>92</td></tr>
	<tr><td>A0000: 日本茶飲料</td><td>6258059</td><td>43529</td><td>38145280000</td><td>92</td></tr>
	</table>
	*/
	// ------------------------------------------------------------------------
	@Override
	public String[] convert(String[] rec, int stat) {
		if (lCount == 0) {
			mothers = new ArrayList();
			for (Integer col : targetCols) {
				mothers.add(Long.valueOf(rec[col]));// Total
			}
			for (Long element : mothers) {
				System.out.println(" m>>"+element);
			}
		}
		for (int seq = 0; seq < targetCols.length; seq++) {
			int col = targetCols[seq];
			//TODO mothers is null  etc
			Long chile = Long.valueOf(rec[col]);
			Long mother = mothers.get(seq);
			System.out.println("chile:" + chile);
			System.out.println("mother:" + mother);
			Double share = chile * 100.0 / mother;// each values
			rec[col] = String.valueOf(exFormat2.format(share));//暫定版　20160303
		}

		lCount++;
		return rec;
	}

	// ------------------------------------------------------------------------
	// fin
	// ------------------------------------------------------------------------
	@Override
	public void fin() {
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		testForHVShare();
	}

	// -------------------------------------------------------------------------
	// 単体テスト　
	// -------------------------------------------------------------------------
	public static void testForHVShare() {
		String inPath = "C:/@qpr/home/123620000036/calc/loyHead.txt";
		String outPath = "C:/@qpr/home/123620000036/calc/HVShare.txt";
		Inf_ArrayCnv cnv = new ShareCnv();
		EzReader reader = new EzReader(inPath, cnv);
		new Common_IO(outPath, reader).execute();
	}

}
