package kyPkg.atoms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import globals.ResControlWeb;
import kyPkg.converter.CnvMap;
import kyPkg.converter.MultiPlusNa;
import kyPkg.etc.Poi4MashUps;
import kyPkg.filter.CommonClojure;
import kyPkg.filter.Inf_BaseClojure;
import kyPkg.rez.BlockHeads;
import kyPkg.rez.UQMatrix;

//-----------------------------------------------------------------------------
//　コンバータ適用バージョン
//-----------------------------------------------------------------------------
public class Map2MatrixDx implements Inf_BaseClojure {
	// XXX ブロックごとの合計値、平均値、標準偏差などストックしておく・・・
	// XXX Excel出力 openOffice用のxml（ODFファイル）ってどんな仕様なのか？？
	// ODF Toolkit http://odf4j.wordpress.com/
	// http://odftoolkit.org/
	// SDK ( Software Development Kits )
	// http://odftoolkit.org/projects/odftoolkit/pages/ODFDOM
	// XXX 集計時にMapReduceする必要のあるケースについて考える 購入率など・・・
	// XXX 集計結果に名前をつけて、比較を行ったり、相関をとったりする
	private String[] option;

	private int current;

	private int valOcc;

	private BlockHeads[][] blocks = null;

	private UQMatrix[][] uMatrx = null;

	protected String wLF = System.getProperty("line.separator");

	private int counter = -1;

	private String outPath = "";

	private String delimiter = "\t";

	private Atomics atomix = null;

	private int[] dimV = null;

	private int[] dimH = null;

	private int[] val = null;

	private String[] title_V = null;

	private String[] title_H = null;

	private MultiPlusNa converter;

	public int getCounter() {
		return counter;
	}

	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}

	public static void main(String[] args) {
		test01();
	}

	public static void test01() {
		String outPath = "c:/sample/Map2Matrix.json";
		String atxPath = "c:/sample/AtomicsMixer";
		System.out.println("20121004debug @atxPath:" + atxPath);
		System.out.println("# checkpoint 009 #20130401@ of Atomics() caller");

		Atomics atom = new Atomics(atxPath);
		int[] dim1 = { 0 }; // 表側に並ぶ項目
		int[] dim2 = { 1 }; // 表頭に並ぶ項目
		int[] val = { 0 };
		String[] options = { "", "", "", "" };
		Map2MatrixDx closure = new Map2MatrixDx(atom, dim1, dim2, val, ",",
				options);
		// Map2Matrix closure = new Map2Matrix (atom, dim1, dim2, val,",");
		closure.setOutPath(outPath);
		new CommonClojure().incore(closure, atxPath + ".txt", true);
		System.out.println("テスト完了");
	}

	public static void test02() {
		String outPath = "c:/sample/Map2Matrix.json";
		String atxPath = "c:/sample/sqlRes2FileWithKeyXX";
		System.out.println("20121004debug @atxPath:" + atxPath);
		System.out.println("# checkpoint 010 #20130401@ of Atomics() caller");

		Atomics insAtomics = new Atomics(atxPath);
		int[] dim1 = { 0 };
		int[] dim2 = { 4 };
		// int[] dim1 = { 8,9,8,9,8,9 };
		// int[] dim2 = { 9,8,9,8,9,8 };
		int[] val = { 0 };
		String[] options = { "", "", "", "" };
		Map2MatrixDx closure = new Map2MatrixDx(insAtomics, dim1, dim2, val,
				options);
		closure.setOutPath(outPath);
		new CommonClojure().incore(closure, atxPath + ".txt", true);
		System.out.println("テスト完了");
	}

	public static void test03() {
		String outPath = ResControlWeb.getD_Resources_Templates("test.json");
		String atxPath = ResControlWeb.getD_Resources_Templates("sample");
		System.out.println("20121004debug @atxPath:" + atxPath);
		System.out.println("# checkpoint 011 #20130401@ of Atomics() caller");

		Atomics insAtomics = new Atomics(atxPath);
		int[] dim1 = { 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 };
		int[] dim2 = { 1, 0, 1, 0 };
		int[] val = { 1 };
		String[] options = { "", "", "", "" };
		Map2MatrixDx closure = new Map2MatrixDx(insAtomics, dim1, dim2, val,
				options);
		closure.setOutPath(outPath);
		new CommonClojure().incore(closure, atxPath + ".txt", true);
	}

	// -------------------------------------------------------------------------
	// コンストラクタ（メタデータ処理）
	// -------------------------------------------------------------------------
	private Map2MatrixDx(Atomics atomix, int[] dim1, int[] dim2, int[] val,
			String[] option) {
		this(atomix, dim1, dim2, val, "\t", option);
	}

	public Map2MatrixDx(Atomics atomix, int[] dim1, int[] dim2, int[] val,
			String delimiter, String[] option) {
		converter = new MultiPlusNa();
		this.atomix = atomix;
		this.dimV = dim1;
		this.dimH = dim2;
		this.val = val;
		this.delimiter = delimiter;
		this.option = option;
		// gradeV = new int[dimV.length];
		// gradeH = new int[dimH.length];
		// init();
	}

	// -------------------------------------------------------------------------
	// init() parse
	// -------------------------------------------------------------------------
	@Override
	public void init() {
		System.out.println("　 Map2MatrixDx  <<init>>");
		counter = 0;
		current = 0;
		int[] gradeV = new int[dimV.length];
		int[] gradeH = new int[dimH.length];
		int max_V = 0;
		int max_H = 0;
		atomix.setOtherName("ＯＴＨＥＲ");// 区分にその他を追加する・・・ XXX しない場合は０を無視する処理が必要
		for (int i = 0; i < dimV.length; i++) {
			if (i > 0)
				gradeV[i] = max_V;
			max_V += atomix.getSize(dimV[i]);
		}
		for (int i = 0; i < dimH.length; i++) {
			if (i > 0)
				gradeH[i] = max_H;
			max_H += atomix.getSize(dimH[i]);
		}
		blocks = new BlockHeads[dimV.length][dimH.length];
		uMatrx = new UQMatrix[dimV.length][dimH.length];
		valOcc = val.length;
		for (int h = 0; h < gradeH.length; h++) {
			int grade_H = gradeH[h];
			for (int v = 0; v < gradeV.length; v++) {
				int grade_V = gradeV[v];
				blocks[v][h] = new BlockHeads(valOcc, atomix.getSize(dimV[v]),
						atomix.getSize(dimH[h]), grade_V, grade_H);
				uMatrx[v][h] = new UQMatrix(); // ユニークカウントクラス
			}
		}
		String[] options = getOptions(option);
		for (int i = 0; i < options.length; i++) {
			System.out.println("* options[" + i + "]:" + options[i]);
		}
		System.out.println("* max_V:" + max_V);
		System.out.println("* max_H:" + max_H);
		title_V = new String[max_V + (options.length * dimV.length)]; 
		// この辺だよ!!
		// 2008/12/16
		title_H = new String[max_H + (options.length * dimH.length)];
		System.out.println("* (options.length*dimV.length):"
				+ (options.length * dimV.length));
		System.out.println("* (options.length*dimH.length):"
				+ (options.length * dimH.length));
		// ---------------------------------------------------------------------
		// Side
		// ---------------------------------------------------------------------
		for (int i = 0; i < dimV.length; i++) {
			String[] array = atomix.getTagNa(dimV[i]);
			for (int n = 0; n < options.length; n++) {
				title_V[gradeV[i] + n] = options[n];
			}
			for (int j = 0; j < array.length; j++) {
				title_V[j + gradeV[i] + (options.length * (i + 1))] = array[j];
			}
		}

		// ---------------------------------------------------------------------
		// Top
		// ---------------------------------------------------------------------
		for (int i = 0; i < dimH.length; i++) {
			String[] array = atomix.getTagNa(dimH[i]);
			for (int n = 0; n < options.length; n++) {
				title_H[gradeH[i] + n] = options[n];
			}
			for (int j = 0; j < array.length; j++) {
				title_H[j + gradeH[i] + (options.length * (i + 1))] = array[j];
			}
		}
		for (int i = 0; i < title_V.length; i++) {
			System.out.println("* title_V[" + i + "]:" + title_V[i]);
		}
		for (int i = 0; i < title_H.length; i++) {
			System.out.println("* title_H[" + i + "]:" + title_H[i]);
		}

	}

	public String[] getOptions(String[] option) {
		ArrayList<String> list = new ArrayList();
		if (option[0].equals("1")) {
			list.add("合計");
		} // sum
		if (option[1].equals("1")) {
			list.add("平均");
		} // avg
		if (option[2].equals("1")) {
			list.add("標準偏差");
		} // std
		if (option[3].equals("1")) {
			list.add("合計シェア");
		}// shr
		return (String[]) list.toArray(new String[list.size()]);
	}

	// -------------------------------------------------------------------------
	// 集計処理(データを読み込んでマトリックス展開・・・)
	// -------------------------------------------------------------------------
	// XXX 縦横の交換はむづかしいか?
	// -------------------------------------------------------------------------
	@Override
	public void execute(String rec) {
		counter++;
		if (!rec.equals("")) {
			String[] array = rec.split(delimiter);
			execute(array);
		}
	}

	@Override
	public void execute(String[] array) {
		int y = 0;
		int occ = 0;
		String wkStr = "";
		for (int bkV = 0; bkV < dimV.length; bkV++) {
			int v = atomix.getMapElement(dimV[bkV]);
			// ConvertMap cnvX = atomix.getMapConv(dim1[i]);
			// XXX for test
			CnvMap cnv_V = CnvMap.shiftConverter(atomix.getTagNa(dimV[bkV]), 1);
			cnv_V = null; // @@@
			if (array.length > v) {
				occ = atomix.getMapOcc(dimV[bkV]);
				if (occ == 1) { // single answer
					try {
						if (cnv_V != null)
							array[v] = cnv_V.convert(array[v]);// @@@大丈夫か？？？
						y = Integer.parseInt(array[v]);
					} catch (java.lang.NumberFormatException e) {
						y = 0;// TODO 外れ値とする
					}
					loopH(array, bkV, y);
				} else { // multi answer XXX NAをどうしようか？
					if (!array[v].equals("")) {
						converter.setSize(occ);
						wkStr = converter.convert(array[v]);
						char[] cArrX = wkStr.toCharArray();
						// System.out.println("wCel[xx]:"+wkStr);
						for (int n = 0; n < cArrX.length; n++) {
							if (cArrX[n] == '1') {
								y = n; // XXX 0からでｏｋだっけ？
								loopH(array, bkV, y);
							}
						}
					} else {
						// XXX ??? 0とするのが正しいかな
					}
				}
			} else {
			}
		}
	}

	private void loopH(String[] wCel, int bkV, int y) {
		int x = 0;
		int occ = 0;
		String wkStr = "";
		for (int bkH = 0; bkH < dimH.length; bkH++) {
			int h = atomix.getMapElement(dimH[bkH]);
			// ConvertMap cnvY = atomix.getMapConv(dim2[j]);
			// XXX for test
			CnvMap cnv_H = CnvMap.shiftConverter(atomix.getTagNa(dimH[bkH]), 1);
			cnv_H = null;// @@@
			if (wCel.length > h) {
				occ = atomix.getMapOcc(dimH[bkH]);
				if (occ == 1) { // single answer
					try {

						if (cnv_H != null)
							wCel[h] = cnv_H.convert(wCel[h]);// @@@大丈夫か？？？

						x = Integer.parseInt(wCel[h]);
					} catch (java.lang.NumberFormatException e) {
						x = 0;// TODO 外れ値とする
					}
					calcIt(wCel, bkV, bkH, y, x);
				} else { // multi answer XXX NAをどうしようか？
					if (!wCel[h].equals("")) {
						converter.setSize(occ);
						wkStr = converter.convert(wCel[h]);
						char[] cArrY = wkStr.toCharArray();
						// System.out.println("wCel[yy]:"+wkStr);
						for (int m = 0; m < cArrY.length; m++) {
							if (cArrY[m] == '1') {
								x = m; // XXX 0からでｏｋだっけ？
								calcIt(wCel, bkV, bkH, y, x);
							}
						}
					} else {
						// XXX ??? 0とするのが正しいかな
					}
				}
			} else {
			}
		}
	}

	private void calcIt(String[] wCel, int bkV, int bkH, int y, int x) {
		String gUnit = "000";
		// 集計ベースが異なるものを複数集計したい場合ここで回すのが一番効率が良い
		// XXX val[0]固定だが・・・計算オブジェクトをval.lengthで回す必要がある・・重くないか？
		for (int current = 0; current < val.length; current++) {
			// ユニークなサンプル数をカウントするロジック
			if (gUnit.equals("999")) {
				if (!uMatrx[bkV][bkH].isExists(y, x, wCel[0])) {// wCel[0]はID
					blocks[bkV][bkH].addCalcObj(y, x, 1, current);
				}
			} else {
				// 通常のサマリー
				int position = atomix.getNumElement(val[current]);
				if (wCel.length > position) {
					try {
						int wNum = Integer.parseInt(wCel[position]);
						blocks[bkV][bkH].addCalcObj(y, x, wNum, current);
					} catch (java.lang.NumberFormatException e) {
					}
				} else {
					System.out.println("#error wCel.length <= z:" + position);
				}
			}
		}
	}

	public void blockCalc(int sampleCount) {
		System.out.println("　blockCalc <<start>> sampleCount:" + sampleCount);
		for (int bkV = 0; bkV < blocks.length; bkV++) {
			for (int bｋH = 0; bｋH < blocks[0].length; bｋH++) {
				blocks[bkV][bｋH].calcIt(sampleCount);
			}
		}
	}

	// -------------------------------------------------------------------------
	// XXX 結果をExcel出力する
	// -------------------------------------------------------------------------
	public void createExcel(int type, String outPath, String[] option) {
		System.out.println("## createExcel outPath:" + outPath);
		String xlsTmp = ResControlWeb.getD_Resources_Templates("QPRBASE02.xls");

		// String sys200 = resDir + "templates/QPRMASHUP.xls";
		String[] resource = { "品目,コーラなど", "対象期間,２００８年1月１日から１０月１０日", "天気,雨のち曇り" };
		// int[][] matrix = { { 1, 2, 3, 4, 5, 6 }, { 1, 2, 3, 4, 5, 6 },
		// { 1, 2, 3, 4, 5, 6 }, { 6, 5, 4, 3, 2, 1 },
		// { 6, 5, 4, 3, 2, 1 }, { 6, 5, 4, 3, 2, 1 } };
		// String[][] smatrix=getMatrix(current,type);
		Poi4MashUps poiMatrix = new Poi4MashUps(outPath, xlsTmp);
		poiMatrix.setResource(resource);
		String sheetName = "sheet";
		// -----------------------------------------------------------------
		// data
		// -----------------------------------------------------------------
		for (int i = 0; i < 6; i++) {
			type = i;
			switch (i) {
			case 0:
				sheetName = "Original";
				break;
			case 1:
				sheetName = "hRatio";
				break;
			case 2:
				sheetName = "H_Share";
				break;
			case 3:
				sheetName = "V_Share";
				break;
			case 4:
				sheetName = "H_Pow2";
				break;
			case 5:
				sheetName = "V_Pow2";
				break;
			default:
				sheetName = "xxxx";
				break;
			}
//			poiMatrix.plot2Headers((sheetName), null, null);
//			poiMatrix.plot2SideHeaders((sheetName), null);
			for (int bkV = 0; bkV < blocks.length; bkV++) {
				for (int bkH = 0; bkH < blocks[0].length; bkH++) {
					System.out.println("V:" + bkV + " H:" + bkH);
					// String[][] smatrix =
					// blocks[bkV][bkH].getMatrix(current,type);
					String[][] smatrix = blocks[bkV][bkH].getMatrix(current,
							type, option);
					int gradeV = blocks[bkV][bkH].getGradeV();
					int gradeH = blocks[bkV][bkH].getGradeH();
					poiMatrix.strMatrix2Sheet(gradeV, gradeH, (sheetName),
							smatrix);
				}
			}
		}

		poiMatrix.fin();
		System.out.println("## createExcel end ##");

	}

	// -------------------------------------------------------------------------
	// 結果をＪＳＯＮ出力する
	// -------------------------------------------------------------------------
	public String getJSON(int type, String suffix, String[] option) {
		String lf = "";
		// XXX サンプル数で割る!!
		counter = -1;
		StringBuffer writer = new StringBuffer();
		StringBuffer buff = new StringBuffer();
		// -----------------------------------------------------------------
		// meta
		// -----------------------------------------------------------------
		if (suffix.equals(""))
			writer.append("{");
		writer.append("meta" + suffix);
		writer.append(":{cells:[[");
		writer.append(lf);
		writer.append("{name: ' ', styles: 'text-align: left;', width: 15},");
		writer.append(lf);
		writer.append("{name: '" + title_H[0]
				+ "', styles: 'text-align: right;', width: 6}");
		for (int i = 1; i < title_H.length; i++) {
			writer.append(",");
			// buffWriter.append("{name: '" + titleTop[i] + "', styles:
			// 'text-align: right;', width: 'auto'},");
			writer.append("{name: '" + title_H[i]
					+ "', styles: 'text-align: right;', width: 6}");
			// buff.append("{name: '伊藤園', styles: 'text-align: right;', width:
			// 'auto'},");
			writer.append(lf);
		}
		// buffWriter.append("],]},");
		writer.append("]]},");
		writer.append(lf);
		writer.append("data" + suffix);
		writer.append(":[");
		writer.append(lf);
		// -----------------------------------------------------------------
		// data
		// -----------------------------------------------------------------
		// あらかじめマトリックスをずらす
		// -----------------------------------------------------------------
		for (int bkV = 0; bkV < blocks.length; bkV++) {
			for (int bkH = 0; bkH < blocks[0].length; bkH++) {
				blocks[bkV][bkH].setMatrix(current, type, option);// ??
			}
		}
		for (int bkV = 0; bkV < blocks.length; bkV++) {
			for (int y = blocks[bkV][0].getGradeV(); y < blocks[bkV][0]
					.getEndV(); y++) {
				if (y > 0)
					writer.append(",");
				buff.delete(0, buff.length());
				// buff.append("['贈答 ','0.00','0.00',0.22,'0.00'],");
				buff.append("['" + title_V[y] + "',");

				int v = y - blocks[bkV][0].getGradeV();
				// buff.append(blocks[bkV][0].getBlockStr(v, current,type));
				buff.append(blocks[bkV][0].getBlockStr(v, current));
				for (int bkH = 1; bkH < blocks[0].length; bkH++) {
					buff.append(",");
					// buff.append(blocks[bkV][bkH].getBlockStr(v,
					// current,type));
					// buff.append(blocks[bkV][bkH].getBlockStr(v,
					// current,type,option));
					buff.append(blocks[bkV][bkH].getBlockStr(v, current));
				}
				writer.append(buff.toString());
				writer.append("]");
				writer.append(lf);
				counter++;
			}
		}
		writer.append("]");
		if (suffix.equals(""))
			writer.append("}");
		writer.append(lf);
		return writer.toString();
	}

	private int saveAsJSON(String outPath) {
		System.out.println("　　saveAsJSON <<start>> outPath:" + outPath);

		if (outPath.equals(""))
			return -1;
		File file = new File(outPath);
		if (file.isFile() && !file.canWrite()) {
			System.out.println("#Error MapReducer@saveAs File can't Write:"
					+ outPath);
			return -1;
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outPath));
			int type = 0; 
			String json = getJSON(type, "", new String[] { "", "", "", "" });
			bw.write(json);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return counter;
	}

	@Override
	public void write() {
		int sampleCount = 5000;
		blockCalc(sampleCount);
		saveAsJSON(outPath);
		System.out.println("Map2Matrix <<fin>> counter:" + counter);
	}

	// var json1 =
	// {meta:{cells:[[
	// {name: ' ', styles: 'text-align: left;', width: 15},
	// {name: '伊藤園 カテキン緑茶 ', styles: 'text-align: right;', width: 'auto'},
	// {name: '伊藤園 緑茶習慣 ', styles: 'text-align: right;', width: 'auto'},
	// {name: '伊藤園 おいお茶 ', styles: 'text-align: right;', width: 'auto'},
	// {name: '伊藤園 おいお茶 濃い味', styles: 'text-align: right;', width: 'auto'}
	// ],]},
	// data:[
	// ["贈答 ","0.00","0.00",0.22,"0.00"],
	// ["スーパー ",0.12,"0.00",3.04,1.26],
	// ["コンビニエンスストア",0.10,"0.00",1.71,1.12],
	// ["一般小売店 ","0.00","0.00",0.27,0.08],
	// ["百貨店 ","0.00","0.00",0.06,0.04],
	// ["薬粧店 ",0.06,"0.00",0.27,0.20],
	// ["自動販売機 ",0.02,"0.00",0.63,0.35],
	// ["通信販売 ","0.00","0.00",0.02,"0.00"],
	// ["訪問販売・宅配 ","0.00","0.00","0.00","0.00"],
	// ["その他店舗 ",0.02,"0.00",0.78,0.25]
	// ]};
}
