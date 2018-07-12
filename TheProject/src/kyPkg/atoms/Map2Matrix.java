package kyPkg.atoms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import globals.ResControlWeb;
import kyPkg.converter.MultiPlusNa;
import kyPkg.filter.CommonClojure;
import kyPkg.filter.Inf_BaseClojure;

public class Map2Matrix implements Inf_BaseClojure {
	//XXX Excel出力　openOffice用のxml（ODFファイル）ってどんな仕様なのか？？
	//ODF Toolkit http://odf4j.wordpress.com/
	//http://odftoolkit.org/
	//SDK ( Software Development Kits ) 
	//http://odftoolkit.org/projects/odftoolkit/pages/ODFDOM
	//XXX 集計時にMapReduceする必要のあるケースについて考える 購入率など・・・
	//XXX 集計結果に名前をつけて、比較を行ったり、相関をとったりする
	protected String wLF = System.getProperty("line.separator");
	private int counter = -1;
	private String outPath = "";
	private String delimiter = "\t";
	private Atomics atomix = null;
	private int[] dim1 = null;
	private int[] dim2 = null;
	private int[] val = null;
	private int[] weight1 = null;
	private int[] weight2 = null;
	private String[] titleSide = null;
	private String[] titleTop = null;
	private Integer[][][] calcObj = null;
	private int dim0Max;
	private int dim1Max;
	private int dim2Max;
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
		System.out.println("# checkpoint 006 #20130401@ of Atomics() caller");

		Atomics insAtomics = new Atomics(atxPath);
		int[] dim1 = { 0, 1 }; //表側に並ぶ項目
		int[] dim2 = { 3, 4 }; //表頭に並ぶ項目
		int[] val = { 0 };
		Map2Matrix closure = new Map2Matrix(insAtomics, dim1, dim2, val, ",");
		// Map2Matrix closure = new Map2Matrix(insAtomics, dim1, dim2, val,",");
		closure.setOutPath(outPath);
		new CommonClojure().incore(closure, atxPath + ".txt", true);
		System.out.println("テスト完了");
	}

	public static void test01x() {
		String outPath = "c:/sample/Map2Matrix.json";
		String atxPath = "c:/sample/sqlRes2FileWithKeyXX";
		System.out.println("20121004debug @atxPath:" + atxPath);
		System.out.println("# checkpoint 007 #20130401@ of Atomics() caller");

		Atomics insAtomics = new Atomics(atxPath);
		int[] dim1 = { 0 };
		int[] dim2 = { 4 };
		//		int[] dim1 = { 8,9,8,9,8,9 };
		//		int[] dim2 = { 9,8,9,8,9,8 };
		int[] val = { 0 };
		Map2Matrix closure = new Map2Matrix(insAtomics, dim1, dim2, val);
		// Map2Matrix closure = new Map2Matrix(insAtomics, dim1, dim2, val,",");
		closure.setOutPath(outPath);
		new CommonClojure().incore(closure, atxPath + ".txt", true);
		System.out.println("テスト完了");
	}

	public static void test02() {
		String outPath = ResControlWeb.getD_Resources_Templates("test.json");
		String atxPath = ResControlWeb.getD_Resources_Templates("sample");
		System.out.println("20121004debug @atxPath:" + atxPath);
		System.out.println("# checkpoint 008 #20130401@ of Atomics() caller");

		Atomics insAtomics = new Atomics(atxPath);
		int[] dim1 = { 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 };
		int[] dim2 = { 1, 0, 1, 0 };
		int[] val = { 1 };
		Map2Matrix closure = new Map2Matrix(insAtomics, dim1, dim2, val);
		closure.setOutPath(outPath);
		new CommonClojure().incore(closure, atxPath + ".txt", true);
	}

	// -------------------------------------------------------------------------
	// コンストラクタ（メタデータ処理）
	// -------------------------------------------------------------------------
	public Map2Matrix(Atomics atomix, int[] dim1, int[] dim2, int[] val) {
		this(atomix, dim1, dim2, val, "\t");
	}

	public Map2Matrix(Atomics atomix, int[] dim1, int[] dim2, int[] val,
			String delimiter) {
		converter = new MultiPlusNa();
		this.atomix = atomix;
		this.dim1 = dim1;
		this.dim2 = dim2;
		this.val = val;
		this.delimiter = delimiter;
		weight1 = new int[dim1.length];
		weight2 = new int[dim2.length];
		// init();
	}

	// -------------------------------------------------------------------------
	// parse
	// -------------------------------------------------------------------------
	@Override
	public void init() {
		counter = 0;
		System.out.println("　Map2Matrix <<init>>");
		dim0Max = val.length;
		dim1Max = 0;
		dim2Max = 0;

		atomix.setOtherName("ＯＴＨＥＲ");//区分にその他を追加する・・・ XXX しない場合は０を無視する処理が必要

		for (int i = 0; i < dim1.length; i++) {
			if (i > 0)
				weight1[i] = dim1Max;
			System.out.println(
					"atomix.getSize(dim1[i]:" + atomix.getSize(dim1[i]));
			dim1Max = dim1Max + atomix.getSize(dim1[i]);
		}
		for (int i = 0; i < dim2.length; i++) {
			if (i > 0)
				weight2[i] = dim2Max;
			System.out.println(
					"atomix.getSize(dim2[i]:" + atomix.getSize(dim2[i]));
			dim2Max = dim2Max + atomix.getSize(dim2[i]);
		}
		System.out.println("* dim1Max:" + dim1Max);
		System.out.println("* dim2Max:" + dim2Max);
		for (int i = 0; i < weight1.length; i++) {
			System.out.println("* weight1[" + i + "]:" + weight1[i]);
		}
		for (int i = 0; i < weight2.length; i++) {
			System.out.println("* weight2[" + i + "]:" + weight2[i]);
		}
		titleSide = new String[dim1Max];
		titleTop = new String[dim2Max];
		//---------------------------------------------------------------------
		// titleSide
		//---------------------------------------------------------------------
		System.out.println("*** title Side ***");
		for (int i = 0; i < dim1.length; i++) {
			String[] array = atomix.getTagNa(dim1[i]);
			System.out.println("array.length:" + array.length);
			System.out.println("weight1[" + i + "]:" + weight1[i]);
			for (int j = 0; j < array.length; j++) {
				int k = j + weight1[i];
				// System.out.println("k=>"+k);
				titleSide[k] = array[j];
			}
		}
		//---------------------------------------------------------------------
		// titleTop
		//---------------------------------------------------------------------
		System.out.println("*** title Top   ***");
		for (int i = 0; i < dim2.length; i++) {
			String[] array = atomix.getTagNa(dim2[i]);
			System.out.println("array.length:" + array.length);
			for (int j = 0; j < array.length; j++) {
				titleTop[j + weight2[i]] = array[j];
			}
		}
		System.out.println("dim0Max:" + dim0Max);
		System.out.println("dim1Max:" + dim1Max);
		System.out.println("dim2Max:" + dim2Max);
		System.out.println("delimiter=>" + delimiter);
		calcObj = new Integer[dim0Max][dim1Max][dim2Max];
	}

	// -------------------------------------------------------------------------
	// 集計処理(データを読み込んでマトリックス展開・・・)
	// -------------------------------------------------------------------------
	// XXX 縦横の交換はむづかしいかどうか
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
		int x = 0;
		int occ = 0;
		String wkStr = "";
		for (int i = 0; i < dim1.length; i++) {
			int xx = atomix.getMapElement(dim1[i]);
			if (array.length > xx) {
				occ = atomix.getMapOcc(dim1[i]);
				if (occ == 1) { // single answer
					try {
						x = Integer.parseInt(array[xx]) + weight1[i];
					} catch (java.lang.NumberFormatException e) {
						x = 0 + weight1[i];//TODO 外れ値とする
					}
					loopY(array, x);
				} else { // multi answer XXX NAをどうしようか？
					if (!array[xx].equals("")) {
						converter.setSize(occ);
						wkStr = converter.convert(array[xx]);
						char[] cArrX = wkStr.toCharArray();
						//System.out.println("wCel[xx]:"+wkStr);
						for (int n = 0; n < cArrX.length; n++) {
							if (cArrX[n] == '1') {
								x = n + weight1[i]; // XXX 0からでｏｋだっけ？
								loopY(array, x);
							}
						}
					} else {
						// XXX ??? 0とするのが正しいかな
					}
				}
			} else {
				//System.out.println("なんか変だぞ wCel.length("+wCel.length+") > xx:" + xx);
			}
		}
	}

	private void loopY(String[] wCel, int x) {
		int y = 0;
		int occ = 0;
		String wkStr = "";
		for (int j = 0; j < dim2.length; j++) {
			int yy = atomix.getMapElement(dim2[j]);
			if (wCel.length > yy) {
				occ = atomix.getMapOcc(dim2[j]);
				if (occ == 1) { // single answer
					try {
						y = Integer.parseInt(wCel[yy]) + weight2[j];
					} catch (java.lang.NumberFormatException e) {
						y = 0 + weight2[j];//TODO 外れ値とする
					}
					calcIt(wCel, x, y);
				} else { // multi answer XXX NAをどうしようか？
					if (!wCel[yy].equals("")) {
						converter.setSize(occ);
						wkStr = converter.convert(wCel[yy]);
						char[] cArrY = wkStr.toCharArray();
						//System.out.println("wCel[yy]:"+wkStr);
						for (int m = 0; m < cArrY.length; m++) {
							if (cArrY[m] == '1') {
								y = m + weight2[j]; // XXX 0からでｏｋだっけ？
								calcIt(wCel, x, y);
							}
						}
					} else {
						// XXX ??? 0とするのが正しいかな
					}
				}
			} else {
				//System.out.println("なんか変だぞ wCel.length("+wCel.length+") > yy:" + yy);
			}
		}
	}

	private void calcIt(String[] wCel, int x, int y) {
		// XXX 購入比率カウントロジック
		//		UQMatrix uqMatrix = new UQMatrix();
		//		if (uqMatrix.isExists(x, y, monitorId)){
		//			
		//		}
		int z = 0; // GLOBALの方が軽いのかも知れない
		int wNum = 0; // GLOBALの方が軽いのかも知れない
		// XXX val[0]固定だが・・・計算オブジェクトをval.lengthで回す必要がある・・重くないか？
		for (int k = 0; k < val.length; k++) {
			z = atomix.getNumElement(val[k]);
			if (wCel.length > z) {
				try {
					wNum = Integer.parseInt(wCel[z]);
				} catch (java.lang.NumberFormatException e) {
					wNum = 0;
					e.printStackTrace();
				}
			} else {
				System.out.println("#error wCel.length <= z:" + z);
			}
			//System.out.println("x:" + x + " y:" + y + " >:" + wNum);
			if (dim1Max > x && dim2Max > y) {
				if (calcObj[k][x][y] == null) {
					calcObj[k][x][y] = new Integer(0);
				}
				calcObj[k][x][y] = calcObj[k][x][y].intValue() + wNum;
			} else {
				System.out.println("#error ");
				System.out.println("x:" + x + " y:" + y + " >:" + wNum);
			}
		}
	}

	// -------------------------------------------------------------------------
	// 結果をＪＳＯＮ出力する
	// -------------------------------------------------------------------------
	public String getJSON(int k) {
		String lf = "";
		// XXX サンプル数で割る!!
		counter = -1;
		StringBuffer buffWriter = new StringBuffer();
		StringBuffer buff = new StringBuffer();
		// -----------------------------------------------------------------
		// meta
		// -----------------------------------------------------------------
		buffWriter.append("{meta:{cells:[[");
		buffWriter.append(lf);
		buffWriter
				.append("{name: ' ', styles: 'text-align: left;', width: 15},");
		buffWriter.append(lf);
		buffWriter.append("{name: '" + titleTop[0]
				+ "', styles: 'text-align: right;', width: 3}");
		for (int i = 1; i < titleTop.length; i++) {
			buffWriter.append(",");
			// buffWriter.append("{name: '" + titleTop[i] + "', styles: 'text-align: right;', width: 'auto'},");
			buffWriter.append("{name: '" + titleTop[i]
					+ "', styles: 'text-align: right;', width: 3}");
			// buff.append("{name: '伊藤園', styles: 'text-align: right;', width: 'auto'},");
			buffWriter.append(lf);
		}
		//buffWriter.append("],]},");
		buffWriter.append("]]},");
		buffWriter.append(lf);
		buffWriter.append("data:[");
		buffWriter.append(lf);
		// -----------------------------------------------------------------
		// data
		// -----------------------------------------------------------------
		for (int i = 0; i < dim1Max; i++) {
			if (i > 0)
				buffWriter.append(","); // XXX IE対応しておこう・・・
			buff.delete(0, buff.length());
			buff.append("['" + titleSide[i] + "'");
			for (int j = 0; j < dim2Max; j++) {
				String wVal = "0.00";
				wVal = "0";
				if (calcObj[k][i][j] != null) {
					wVal = calcObj[k][i][j].toString();
				}
				buff.append(",");
				// buff.append("['贈答 ','0.00','0.00',0.22,'0.00'],");
				buff.append(wVal);
			}
			buffWriter.append(buff.toString());
			buffWriter.append("]");
			//			buffWriter.append(","); // XXX IE対応しておこう・・・
			buffWriter.append(lf);
			counter++;
		}
		buffWriter.append("]}");
		buffWriter.append(lf);
		return buffWriter.toString();
	}

	public int saveAsJSON(String outPath, int k) {
		if (outPath.equals(""))
			return -1;
		File file = new File(outPath);
		if (file.isFile() && !file.canWrite()) {
			System.out.println(
					"#Error MapReducer@saveAs File can't Write:" + outPath);
			return -1;
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outPath));
			String json = getJSON(k);
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
		System.out.println("　Map2Matrix <<fin>> counter:" + counter);
		saveAsJSON(outPath, 0);
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
