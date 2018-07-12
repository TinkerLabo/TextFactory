package kyPkg.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kyPkg.uFile.DosEmu;
import kyPkg.uFile.File49ers;
import kyPkg.uFile.FileUtil;

//-----------------------------------------------------------------------------
//指定されたファイルを横方向に連結出力する
//-----------------------------------------------------------------------------
public class TexMerge_H {
	private static final String DUMMY = "";
	private kyPkg.filter.EzWriter writer;
	private String outPath;
	private String path_L = "";
	private String path_R = "";
	private String delimiter = "\t";// 出力用区切り文字
	private String delimiter_L;
	private String delimiter_R;
	private List<Integer> list_L = null;
	private List<Integer> list_R = null;

	// ------------------------------------------------------------------------
	// 配列からリストに変換する
	// ------------------------------------------------------------------------
	private List array2List(Integer[] array) {
		return java.util.Arrays.asList(array);
	}

	public void setArray_L(Integer[] array_L) {
		this.list_L = array2List(array_L);
	}

	public void setArray_R(Integer[] array_R) {
		this.list_R = array2List(array_R);
	}

	public void setList_L(List<Integer> list_L) {
		this.list_L = list_L;
	}

	public void setList_R(List<Integer> list_R) {
		this.list_R = list_R;
	}

	private int bef_L = 0;// ※bef,aftは0から始まるインデックで指定する
	private int bef_R = 0;
	private int aft_L = 0;
	private int aft_R = 0;

	public void setBef_L(int bef_L) {
		this.bef_L = bef_L;
	}

	public void setAft_L(int aft_L) {
		this.aft_L = aft_L;
	}

	public void setBef_R(int bef_R) {
		this.bef_R = bef_R;
	}

	public void setAft_R(int aft_R) {
		this.aft_R = aft_R;
	}

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public TexMerge_H(String outPath, String path_L, String path_R) {
		this.outPath = outPath;
		this.path_L = path_L;
		this.path_R = path_R;
	}

	private List<Integer> parse(List<Integer> list, int bef, int aft, int max) {
		int from = 0;
		int to = 0;
		if (list == null) {
			if (aft == 0)
				aft = max;
			if (bef < aft) {
				from = bef;
				to = aft;
			} else {
				from = aft;
				to = bef;
			}
			System.out.println("##  from:" + from + " to:" + to);
			list = new ArrayList();
			for (int i = from; i <= to; i++) {
				// System.out.println("col=>" + i);
				list.add(i);
			}
		}
		return list;
	}

	public int execute() {
		String[] arrays_L = null;
		String[] arrays_R = null;
		// --------------------------------------------------------------------
		// check left
		// --------------------------------------------------------------------
		File49ers f49_L = new File49ers(path_L);
		delimiter_L = f49_L.getDelimiter();
		list_L = parse(list_L, bef_L, aft_L, f49_L.getMaxColCount() - 1);
		// --------------------------------------------------------------------
		// check right
		// --------------------------------------------------------------------
		File49ers f49_R = new File49ers(path_R);
		delimiter_R = f49_R.getDelimiter();
		list_R = parse(list_R, bef_R, aft_R, f49_R.getMaxColCount() - 1);
		// --------------------------------------------------------------------
		StringBuffer buf = new StringBuffer();
		writer = new kyPkg.filter.EzWriter(outPath);
		writer.open();
		System.out.println("path_L=>" + path_L);
		System.out.println("path_R=>" + path_R);
		try {
			BufferedReader br_L = FileUtil.getBufferedReader(path_L);
			BufferedReader br_R = FileUtil.getBufferedReader(path_R);
//			BufferedReader br_L = new BufferedReader(new FileReader(path_L));
//			BufferedReader br_R = new BufferedReader(new FileReader(path_R));
			String rec_L = br_L.readLine();
			String rec_R = br_R.readLine();
			while (rec_L != null || rec_R != null) {
				buf.delete(0, buf.length());
				arrays_L = null;
				arrays_R = null;
				if (rec_L != null)
					arrays_L = rec_L.split(delimiter_L);
				// ------------------------------------------------------------
				// 左側のファイルを書き出す
				// ------------------------------------------------------------
				for (Integer col : list_L) {
					if (arrays_L != null && arrays_L.length > col) {
						buf.append(arrays_L[col]);
					} else {
						buf.append(DUMMY);
					}
					buf.append(delimiter);
				}
				if (rec_R != null)
					arrays_R = rec_R.split(delimiter_R);
				// ------------------------------------------------------------
				// 右側のファイルを書き出す
				// ------------------------------------------------------------
				for (Integer col : list_R) {
					if (arrays_R != null && arrays_R.length > col) {
						buf.append(arrays_R[col]);
					} else {
						buf.append(DUMMY);
					}
					buf.append(delimiter);
				}
				writer.write(buf.toString());
				rec_L = br_L.readLine();
				rec_R = br_R.readLine();
			}
			br_L.close();
			br_R.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.close();
		return 0;
	}

	// ------------------------------------------------------------------------
	// main
	// ------------------------------------------------------------------------
	public static void main(String[] args) {
		test00();
	}

	// ------------------------------------------------------------------------
	// Q どちらかのファイルのレコード数が少ない場合どうなるか⇒長いほうに合わせて出力される（ダミーセル出力あり）
	// リスト（または配列）の指定が優先的に使用される（開始終了カラムの指定は無視される）
	// ------------------------------------------------------------------------
	// 20141107 ファイルを横方向にbindする・・・
	// ------------------------------------------------------------------------
	public static void test00() {
		// XXX 連結するファイルの並びをソート順にになるようにしておく（SEQを振る）
		String outPath = "C:/@qpr/home/238881000301/calc/Mixed.txt";
		String regex = "C:/@qpr/home/238881000301/calc/#004_sorted_AMT_*.txt";
		int modCol = 1;
		TexMerge_H.modByRegex(outPath, regex, modCol);
	}

	// ------------------------------------------------------------------------
	// 最初のファイルはすべての項目が出力される・・
	// regex　当該ディレクトリ以下のパターンにマッチするファイル一覧を連結する
	// modCol　2番目のファイルは、このカラム以降のカラムを連結
	// ------------------------------------------------------------------------
	public static void modByRegex(String outPath, String regex, int modCol) {
		List<String> list = DosEmu.dir(regex, false);
		Collections.sort(list);

		modWithList(outPath, list, modCol);

		// String path_L = "";
		// String path_R = "";
		// String prevPath = "";
		// for (String element : list) {
		// System.out.println("---------------------------------------------");
		// if (prevPath.equals("")) {
		// prevPath = element;
		// } else {
		// path_L = prevPath;
		// path_R = element;
		// TexMerge_H merger = new TexMerge_H(outPath, path_L, path_R);
		// merger.setBef_R(modCol);
		// merger.execute();
		// prevPath = outPath;
		// }
		// }

	}

	public static void modWithList(String outPath, List<String> list, int skip) {
		// 連結する対象が１つの場合
		if (list.size() == 1) {
			String inPath = list.get(0);
			FileUtil.fileCopy(outPath, inPath);
		} else {
			String path_L = "";
			String path_R = "";
			String prevPath = "";
			for (String element : list) {
				System.out.println("----------------------------------------");
				if (prevPath.equals("")) {
					prevPath = element;
				} else {
					path_L = prevPath;
					path_R = element;
					TexMerge_H merger = new TexMerge_H(outPath, path_L, path_R);
					merger.setBef_R(skip);
					merger.execute();
					prevPath = outPath;
				}
			}
		}
	}

	// 20141219 単純に横につなげるだけではだめなので、特定の列をkeyに連結するように書き換える（ハッシュマップを使うということだろうね・・・）

	public static void test01() {
		// まず、単純に横につなげる
		String outPath = "C:/@qpr/home/238881000301/calc/Mixed.txt";
		String path_L = "C:/@qpr/home/238881000301/calc/#004_sorted_AMT_年代（５歳区分）.txt";
		String path_R = "C:/@qpr/home/238881000301/calc/#004_sorted_AMT_購入先（大区分）.txt";
		TexMerge_H merger = new TexMerge_H(outPath, path_L, path_R);
		merger.setBef_L(1);// 左側の読み込み開始カラム（０スタート）、指定されていない場合０カラムから
		merger.setAft_L(1);// 左側の読み込み終了カラム（０スタート）、指定されていない場合最終カラムまで
		merger.setArray_L(new Integer[] { 1, 0 });// 左側の読み込み対象カラム・・・これを指定すると開始終了カラムは無視される
		merger.setArray_R(new Integer[] { 1, 0 });// 右側の読み込み対象カラム
		merger.execute();
	}

}
