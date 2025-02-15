package kyPkg.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kyPkg.uFile.File49ers;
import kyPkg.uFile.FileUtil;

//-----------------------------------------------------------------------------
//指定されたカラムの値をキーにファイルを横方向に連結＆出力する
//	対前年比バージョン	Year_on_year 2015-01-27 yuasa
//-----------------------------------------------------------------------------
public class Year_on_year_org {
	java.text.DecimalFormat exFormat = new java.text.DecimalFormat(
			"000000000.000");
	private static final String DUMMY_VAL = "0.00";
	//	String dummy = "0.00";
	private List<Integer> keyCols;
	private int skip = 0;
	private String outPath = "";
	private List<String> pathListBef;
	private List<String> pathListAft;
	private HashMap<Integer, Integer> widthMap = null;// セルに格納されているリストのサイズ
	private String delimiter = "\t";
	private int maxCol = 0;
	boolean optDebug = false;
	private List<Integer> avoidCols;
	private StringBuffer keyBuf = null;

	private Set<String> commonKeys;//期間１、２を通しての、共通のキーセット

	public void setAvoidCols(List<Integer> avoidCols) {
		this.avoidCols = avoidCols;
	}

	// ------------------------------------------------------------------------
	// アクセッサ
	// ------------------------------------------------------------------------
	public void setSkip(int skip) {
		this.skip = skip;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public Year_on_year_org(String outPath, List<String> pathList, int keyCol,
			boolean optDebug) {
		this(outPath, pathList, getKeyCols(keyCol, 1), optDebug);
	}

	private Year_on_year_org(String outPath, List<String> pathList,
			List<Integer> keyCols, boolean optDebug) {
		//#createTester--------------------------------------------------
//		System.out.println("public static void testYear_on_year() {");
//		System.out.println("    String outPath = \"" + outPath + "\";");
//		System.out.println("    List<String> pathList = new ArrayList();");
//		for (String element : pathList) {
//			System.out.println("    pathList.add(\"" + element + "\");");
//		}
//		System.out.println("    List<Integer> keyCols = new ArrayList();");
//		for (Integer element : keyCols) {
//			System.out.println("    keyCols.add(" + element + ");");
//		}
//		System.out.println("    String dummy = \"" + DUMMY_VAL + "\";");
//		System.out.println("    boolean optDebug = " + optDebug + ";");
//		System.out
//				.println("    Year_on_year ins = new Year_on_year(outPath,pathList,keyCols,dummy,optDebug);");
//		System.out.println("}");
		//--------------------------------------------------
		this.optDebug = optDebug;
		this.outPath = outPath;
		this.pathListBef = new ArrayList<String>();
		this.pathListAft = new ArrayList<String>();
		// まず、パスリストを前後２つに振り分ける？
		//対前年設定されている場合、ファイルは必ず前後期それぞれに分けられる
		int maxCol = pathList.size() / 2;
//		System.out.println("#20150728debug# maxCol：" + maxCol);
		for (String path : pathList) {
			if (pathListBef.size() < maxCol) {
				pathListBef.add(path);
			} else {
				pathListAft.add(path);
			}
		}
		this.keyCols = keyCols;
//		System.out.println("#20150728debug# keyCols：" + keyCols);

	}

	private static List<Integer> getKeyCols(int keyFrom, int occ) {
		List<Integer> list = new ArrayList();
		int cnt = 1;
		for (int val = keyFrom; cnt <= occ; val++) {
			list.add(val);
			cnt++;
		}
		return list;
	}

	public int execute() {
		commonKeys = new HashSet();
		widthMap = new HashMap<Integer, Integer>();

		int current = 0;// 現在何セルめ（何シートめ）を連結しているか
		maxCol = pathListBef.size();
		HashMap<String, List<List<String>>> mapBef = new HashMap();
		for (String path : pathListBef) {
			mapBef = file2Map(mapBef, path, current, keyCols);
			current++;
		}

		current = 0;// 現在何セルめ（何シートめ）を連結しているか
		maxCol = pathListAft.size();
		HashMap<String, List<List<String>>> mapAft = new HashMap();
		for (String path : pathListAft) {
			mapAft = file2Map(mapAft, path, current, keyCols);
			current++;
		}

		map2File(outPath, mapBef, mapAft, delimiter, true, false);
		return 0;
	}

	// ----------------------------------------------------------------
	// ファイルを読み込みマップ上でデータを格納（連結）する
	// ----------------------------------------------------------------
	private HashMap<String, List<List<String>>> file2Map(
			HashMap<String, List<List<String>>> map, String path, int current,
			List<Integer> keyCols) {

		StringBuffer keyBuf = new StringBuffer();
		String key = "";
		String[] recs = null;
		// --------------------------------------------------------------------
		File49ers f49 = new File49ers(path);
		String delimiter_L = f49.getDelimiter();

		// --------------------------------------------------------------------
		//skip?要解説か・・・
		// --------------------------------------------------------------------
		int xFrom = 0 + skip;
		int xTo = f49.getMaxColCount() - 1;
		List<Integer> colList = paramGen(xFrom, xTo);// どのカラムが必要かを指定するパラメータ
		// カラム指定がなければ全体を指すパラメータを生成する
//		System.out.println("#20150728debug# From:" + xFrom + " To:" + xTo
//				+ " path=" + path);
		// --------------------------------------------------------------------
		widthMap.put(current, colList.size());// セルごとの幅（リストのサイズ）を記録する・・ダミー生成時に使用する
		// --------------------------------------------------------------------
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			String rec = br.readLine();
			while (rec != null) {
				recs = rec.split(delimiter_L, -1);
				key = getKey(recs);
				// ------------------------------------------------------------
				List<String> curList = new ArrayList();
				for (Integer col : colList) {
					if (recs != null && recs.length > col) {
						curList.add(recs[col]);
					} else {
						curList.add(DUMMY_VAL);
					}
				}
				// ------------------------------------------------------------
				//	同じキーのものについて対象カラムの値のリストを連結する
				// ------------------------------------------------------------
				List<List<String>> cells = map.get(key);
				if (cells == null) {
					cells = new ArrayList<List<String>>();
					// --------------------------------------------------------
					// オリジナルの並び順を記録する為のリスト
					// --------------------------------------------------------
					commonKeys.add(key);
				}
				// ------------------------------------------------------------
				//	直前期間が存在しない場合はnullを追加しておく
				// ------------------------------------------------------------
				while (cells.size() < current)
					cells.add(null);
				if (cells.size() == current)
					cells.add(curList);
				map.put(key, cells);
				// ------------------------------------------------------------
				rec = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	// ------------------------------------------------------------------------
	// key部分を抜き出す
	// ------------------------------------------------------------------------
	private String getKey(String[] recs) {
		if (keyBuf == null)
			keyBuf = new StringBuffer();
		keyBuf.delete(0, keyBuf.length());
		for (Integer keyCol : keyCols) {
			if (recs != null && recs.length > keyCol) {
				if (keyBuf.length() > 0)
					keyBuf.append(delimiter);
				keyBuf.append(recs[keyCol]);
			}
		}
		return keyBuf.toString();
	}

	// ------------------------------------------------------------------------
	// ハッシュマップからファイルに書き出す（ダミーを補てんする）
	// ------------------------------------------------------------------------
	public void map2File(String outPath,
			HashMap<String, List<List<String>>> mapBef,
			HashMap<String, List<List<String>>> mapAft, String delimiter,
			boolean keyOpt, boolean seqOpt) {

		if (mapBef == null) {
			System.out.println("#ERROR@map2File mapBef==null");
			return;
		}
		if (mapAft == null) {
			System.out.println("#ERROR@map2File mapAft==null");
			return;
		}
		long seq = 0;
		EzWriter writer = new EzWriter(outPath);
		writer.open();
		for (String key : commonKeys) {
			// System.out.println("key:" + key);
			List<List<String>> cellsBef = mapBef.get(key);
			List<List<String>> cellsAft = mapAft.get(key);
			if (cellsBef != null && cellsAft != null) {
				StringBuffer buf = new StringBuffer();
				// ----------------------------------------------------------------
				if (seqOpt) {//Seqを出力する
					buf.append(String.valueOf(seq++));
					buf.append(delimiter);
				}
				// ----------------------------------------------------------------
				if (keyOpt) {//key部分を出力する
					buf.append(key);
					buf.append(delimiter);
				}
				// ----------------------------------------------------------------
				// 後方補填処理
				// ----------------------------------------------------------------
				while (cellsBef.size() < this.maxCol)
					cellsBef.add(null);
				while (cellsAft.size() < this.maxCol)
					cellsAft.add(null);
				// ----------------------------------------------------------------
				for (int xCol = 0; xCol < this.maxCol; xCol++) {
					int width = widthMap.get(xCol);
					List<String> listBef = getList(cellsBef, xCol, width);
					List<String> listAft = getList(cellsAft, xCol, width);
					for (int i = 0; i < width; i++) {
						String elementBef = listBef.get(i);
						String elementAft = listAft.get(i);
						String val;
						if (xCol == 0 && avoidCols.contains(i)) {
//							System.out.println("contain:" + i);
							val = elementAft;
						} else {
							val = calcIt(elementBef, elementAft);
						}
						buf.append(val);

						buf.append(delimiter);
					}
				}
				String rec = buf.toString();
				// System.out.println("#Year_on_year # rec:" + rec);
				writer.write(rec);
			}
		}
		writer.close();
	}

	private String calcIt(String elementBef, String elementAft) {
		String result = "";
		String debug = "(100 * " + elementAft + ")/" + elementBef;//  デバッグ用に文字列にしてある
		if (elementBef.equals("0") || elementAft.equals("0")) {
			result = "0";
		} else {
			// 対前年比１　(（今年−前年）*100)÷前年
			// 対前年比２　(今年*100)÷前年
			if (optDebug) {
				result = debug;
			} else {
				try {
					// それぞれの値をdoubleにして計算する・・・
					Double aft = Double.parseDouble(elementAft);
					Double bef = Double.parseDouble(elementBef);
					result = exFormat.format((100.0 * aft) / bef);
				} catch (Exception e) {
					result = "#ERROR @対前年計算=> " + debug;
				}
			}
		}
		return result;
	}

	// ------------------------------------------------------------------------
	// 当該セルにあたる部分のデータがなければ０を補てんする
	// ------------------------------------------------------------------------
	private List<String> getList(List<List<String>> cells, int col, int width) {
		List<String> list = null;
		if (cells.size() > col)
			list = cells.get(col);
		if (list == null)
			list = new ArrayList<String>();
		while (list.size() < width) {
			//			list.add("0");
			list.add(DUMMY_VAL);
		}
		//
		return list;
	}

	// ------------------------------------------------------------------------
	// 開始終了パラメータから位置パラメータを生成する
	// ------------------------------------------------------------------------
	public static List<Integer> paramGen(int bef, int aft) {
		int from = 0;
		int to = 0;
		if (bef < aft) {
			from = bef;
			to = aft;
		} else {
			from = aft;
			to = bef;
		}
		List list = new ArrayList();
		for (int i = from; i <= to; i++) {
			list.add(i);
		}
		return list;
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		testYear_on_year();
	}

	public static void testYear_on_year() {
		String outPath = "C:/@qpr/home/123620000049/calc/数量ベース.txt";
		List<String> pathList = new ArrayList();
		pathList.add("C:/@qpr/home/123620000049/calc/#004_MXD_QTY_0002014年05月01日〜30日_001.txt");
		pathList.add("C:/@qpr/home/123620000049/calc/#004_MXD_QTY_0012014年05月01日〜30日_001.txt");
		pathList.add("C:/@qpr/home/123620000049/calc/#004_MXD_QTY_0002015年05月01日〜30日_002.txt");
		pathList.add("C:/@qpr/home/123620000049/calc/#004_MXD_QTY_0012015年05月01日〜30日_002.txt");
		List<Integer> keyCols = new ArrayList();
		keyCols.add(0);

		boolean optDebug = false;
		Year_on_year_org ins = new Year_on_year_org(outPath, pathList, keyCols,
				optDebug);
		//		ins.setSkip(2); /???
		ins.execute();
	}

}
