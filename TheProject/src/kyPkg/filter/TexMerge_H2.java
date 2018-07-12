package kyPkg.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kyPkg.uFile.File49ers;
import kyPkg.uFile.FileUtil;

//-----------------------------------------------------------------------------
//指定されたカラムの値をキーにファイルを横方向に連結＆出力する
//-----------------------------------------------------------------------------
public class TexMerge_H2 {
	private String DOT_ZERO = "0.00";
	private List<Integer> keyCols;
	private int skip = 0;
	private String outPath = "";
	private int maxCol = 0;
	private List<String> pathList;
	private List<String> keyList;// データの並び順を記録
	private HashMap<String, List<List<String>>> map = null;// (key,Value)
	private HashMap<Integer, Integer> widthMap = null;// セルに格納されているリストのサイズ
	private String delimiter = "\t";
	private int width;

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public TexMerge_H2(String outPath, List<String> pathList, int keyCol,int skip) {
		//#createTester--------------------------------------------------
//		System.out.println("public static void testTexMerge_H2() {");
//		System.out.println("    String outPath = \"" + outPath + "\";");
//		System.out.println("    List<String> pathList = new ArrayList();");
//		for (String element : pathList) {
//		System.out.println("    pathList.add(\""+element+"\");");
//		}
//		System.out.println("    int keyCol = " + keyCol + ";");
//		System.out.println("    int skip = " + skip + ";");
//		System.out.println("    TexMerge_H2 ins = new TexMerge_H2(outPath,pathList,keyCol,skip);");
//		System.out.println("    ins.execute();");
//		System.out.println("}");
		//--------------------------------------------------
		this.outPath = outPath;
		this.pathList = pathList;
		this.keyCols = getKeyCols(keyCol, 1);
		this.maxCol = pathList.size();
		this.skip = skip;
		this.width = 2;//ファイルが持っていると期待しているカラム数（空のファイルが存在するため）
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
		keyList = null;
		map = null;
		widthMap = null;
		int current = 0;// 現在何セルめ（何シートめ）を連結しているか
		for (String path : pathList) {
			file2Map(current, keyCols, path);
			current++;
		}
		map2File(outPath, delimiter, true, false);
		return 0;
	}

	// ----------------------------------------------------------------
	// ファイルからハッシュマップにデータを格納（連結）する
	// ----------------------------------------------------------------
	public int file2Map(int current, List<Integer> keyCols, String path) {
//		System.out.println("path:" + path);
		// --------------------------------------------------------------------
		File49ers f49 = new File49ers(path);
		String delimiter = f49.getDelimiter();
		int bef = 0 + skip;
		int maxCol = f49.getMaxColCount();
		// --------------------------------------------------------------------
//		System.out.println("maxCol:" + maxCol);
		if (maxCol <= 0)
			maxCol = width;
		mapit(current, bef, maxCol, path, delimiter);
		return 0;
	}

	private void mapit(int current, int startCol, int endCol, String path,
			String delimiter) {
		StringBuffer keyBuf = new StringBuffer();
		endCol = endCol - 1;
		String[] array = null;
		String key = "";
		List<Integer> colList = paramGen(startCol, endCol);// カラム指定がなければ全体を指すパラメータを生成する
		// --------------------------------------------------------------------
		if (keyList == null)
			keyList = new ArrayList<String>();
		if (map == null)
			map = new HashMap<String, List<List<String>>>();
		// --------------------------------------------------------------------
		if (widthMap == null)
			widthMap = new HashMap<Integer, Integer>();
		widthMap.put(current, colList.size());// セルごとの幅（リストのサイズ）を記録する・・ダミー生成時に使用する
		// --------------------------------------------------------------------
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			String rec = br.readLine();
			while (rec != null) {
				// List<String> curList = new ArrayList<String>();//20150213
				if (rec != null) {
					array = rec.split(delimiter, -1);
				} else {
					array = null;
				}
				// --------------------------------------------------------
				// getKey （key部を組み立てる）
				// --------------------------------------------------------
				keyBuf.delete(0, keyBuf.length());
				for (Integer keyCol : keyCols) {
					// System.out.println("### keyCol:" + keyCol);
					if (array != null && array.length > keyCol) {
						if (keyBuf.length() > 0)
							keyBuf.append(delimiter);
						keyBuf.append(array[keyCol]);
					}
				}
				key = keyBuf.toString();
				// --------------------------------------------------------
				// System.out.println("### key:" + key);
				// colListにあるカラムのみでデータ部を組み立てる
				// --------------------------------------------------------
				List<String> curList = new ArrayList<String>();// 20150213　
				for (Integer col : colList) {
					if (array != null && array.length > col) {
						curList.add(array[col]);
					} else {
						curList.add(DOT_ZERO);
					}
				}
				List<List<String>> cells = map.get(key);
				if (cells == null)
					cells = new ArrayList<List<String>>();
				while (cells.size() < current)
					cells.add(null);
				// --------------------------------------------------------
				// System.out.println("### cells.size():" + cells.size());
				// --------------------------------------------------------
				if (cells.size() == current)
					cells.add(curList);
				// --------------------------------------------------------
				// オリジナルの並び順を記録する為のリスト（不要か？）
				// --------------------------------------------------------
				if (!keyList.contains(key))
					keyList.add(key);
				map.put(key, cells);
				rec = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------
	// ハッシュマップからファイルに書き出す（ダミーを補てんする）
	// ------------------------------------------------------------------------
	public void map2File(String outPath, String delimiter, boolean keyOpt,
			boolean seqOpt) {
		if (map == null) {
			System.out.println("#ERROR map==null");
			return;
		}
		long seq = 0;
		EzWriter writer = new EzWriter(outPath);
		writer.open();
		// --------------------------------------------------------
		// オリジナルの並び順を記録したいので、別にキーのリストを用意した=>keyList
		// --------------------------------------------------------
		for (String key : keyList) {
			// System.out.println("key:" + key);
			StringBuffer buf = new StringBuffer();
			// ----------------------------------------------------------------
			if (seqOpt) {
				buf.append(String.valueOf(seq++));
				buf.append(delimiter);
			}
			// ----------------------------------------------------------------
			if (keyOpt) {
				buf.append(key);
				buf.append(delimiter);
			}
			List<List<String>> cells = map.get(key);
			// 20150126 後方補填処理・・
			while (cells.size() < this.maxCol)
				cells.add(null);
			int col = 0;
			for (List<String> list : cells) {
				if (list != null) {
					for (String element : list) {
						buf.append(element);
						buf.append(delimiter);
					}
				} else {
					// nullなら同じセル位置のものを参照して同じ幅のダミーデータを生成する
					Integer width = widthMap.get(col);
					if (width == null) {

					}
					for (int i = 0; i < width; i++) {
						buf.append(DOT_ZERO);
						buf.append(delimiter);
					}
				}
				col++;
			}
			writer.write(buf.toString());
		}
		writer.close();
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

	//20161005 for debug
	public static void testTexMerge_H2() {
	    String outPath = "C:/@qpr/home/828111000630/calc/#005_MX2_金額ベース.txt";
	    List<String> pathList = new ArrayList();
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2015年09月28日～2016年07月31日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2015年09月28日～10月04日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2015年10月05日～11日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2015年10月12日～18日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2015年10月19日～25日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2015年10月26日～11月01日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2015年11月02日～08日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2015年11月09日～15日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2015年11月16日～22日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2015年11月23日～29日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2015年11月30日～12月06日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2015年12月07日～13日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2015年12月14日～20日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2015年12月21日～27日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2015年12月28日～2016年01月03日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年01月04日～10日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年01月11日～17日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年01月18日～24日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年01月25日～31日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年02月01日～07日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年02月08日～14日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年02月15日～21日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年02月22日～28日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年02月29日～03月06日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年03月07日～13日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年03月14日～20日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年03月21日～27日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年03月28日～04月03日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年04月04日～10日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年04月11日～17日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年04月18日～24日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年04月25日～05月01日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年05月02日～08日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年05月09日～15日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年05月16日～22日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年05月23日～29日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年05月30日～06月05日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年06月06日～12日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年06月13日～19日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年06月20日～26日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年06月27日～07月03日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年07月04日～10日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年07月11日～17日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年07月18日～24日_期間001.txt");
	    pathList.add("C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年07月25日～31日_期間001.txt");
	    int keyCol = 0;
	    int skip = 1;
	    TexMerge_H2 ins = new TexMerge_H2(outPath,pathList,keyCol,skip);
	    ins.execute();
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		testTexMerge_H2();
	}
}
