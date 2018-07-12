package kyPkg.filter;

import static kyPkg.uFile.FileUtil.getBufferedReader;
import static kyPkg.util.Faker.getDummy;
import static kyPkg.util.KUtil.getIntArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import globals.ResControl;
import kyPkg.task.Abs_ProgressTask;
import kyPkg.uFile.File49ers;
import kyPkg.util.KUtil;

//　マスター（多）対トランザクションファイル（多）のマッチング処理、出力はクロージャを指定
//　	XXX あとで・・カラム指定が無い場合,フルカラム処理という仕様にしておく
//	XXX リーダーもクロージャにできないか？？パフォーマンスを鑑みながら修正！！
// last update 2009-11-12 yuasa 
public class MultiMatch extends Abs_ProgressTask {
	private boolean Order = true;// "MT";
	private kyPkg.tools.Elapse elapse;
	private boolean sameSource = false;
	private char funC = 'I'; // Default = Inner Join
	private long writeCount = 0;
	private HashMap<String, ElementsLR_Plus> mixerMap;
	private Set<String> markerset;
	private HashMap<String, Set> setMap;
	private HashMap<String, Inf_BaseClojure> outClosureMap;
	private Inf_BaseClojure defaultClosure;
	private String pathM = "";// Master
	private String pathT = "";// Tran
	private int keyM = 0; // Master側のキーカラム
	private int keyT = 0; // Tran側のキーカラム
	private int[] dimM;
	private int[] dimT;
	private String dmyM = "DummyM";
	private String dmyT = "DummyT";
	private int maxcolM;
	private int maxcolT;
	private String delimiter = null;
	private String delimM = "\t";
	private String delimT = "\t";
	private boolean elapseCheck = false;

	public boolean isElapseCheck() {
		return elapseCheck;
	}

	public void setElapseCheck(boolean elapseCheck) {
		this.elapseCheck = elapseCheck;
	}

	public int getMaxcolL() {
		return maxcolM;
	}

	public int getMaxcolR() {
		return maxcolT;
	}

	public void setKeyL(int keyL) {
		this.keyM = keyL;
	}

	public void setKeyR(int keyR) {
		this.keyT = keyR;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter; // 出力ファイルのデリミタ
		this.delimM = delimiter;// 入力ファイルのデリミタ
		this.delimT = delimiter;// 入力ファイルのデリミタ
	}

	// -------------------------------------------------------------------------
	// 内部クラス
	// -------------------------------------------------------------------------
	class ElementsLR_Plus {
		// マスター側の項目だけリストとなっている（同じキーごとスタックされる）
		private ArrayList<String> list = null;
		private boolean done = false;

		public boolean isDone() {
			return done;
		}

		public void setDone() {
			this.done = true;
		}

		public ElementsLR_Plus() {
			list = new ArrayList();
			done = false;
		}

		public ArrayList getList() {
			return this.list;
		}

		public void setList(String left) {
			this.list.clear();
			this.list.add(left);
		}

		public void addList(String left) {
			this.list.add(left);
		}
	}

	// 同じキーの値が存在したらスタックする
	private ElementsLR_Plus modElementVal(String key, String value) {
		ElementsLR_Plus element = (ElementsLR_Plus) mixerMap.get(key);
		if (element == null) {// NotFound
			element = new ElementsLR_Plus();
		}
		element.addList(value); // 同じキーであれば値はスタックされていく
		mixerMap.put(key, element);
		return element;
	}

	// 同じキーの値が存在したら上書きする（スタックしない）
	private ElementsLR_Plus setElementVal(String key, String value) {
		ElementsLR_Plus element = (ElementsLR_Plus) mixerMap.get(key);
		if (element == null) {// NotFound
			element = new ElementsLR_Plus();
		}
		element.setList(value); // スタックしない！
		mixerMap.put(key, element);
		return element;
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	// マッチングした結果をマスターにモッドする場合用
	public MultiMatch(String pathM, String func, String pathT,
			String delimiter) {
		this(pathM, func, pathM, (int[]) null, pathT, (int[]) null, true);
		this.setDelimiter(delimiter);
	}

	public MultiMatch(String outPath, String func, String pathM, String pathT,
			boolean order) {
		this(outPath, func, pathM, (int[]) null, pathT, (int[]) null, order);
	}

	public MultiMatch(String outPath, String func, String pathM, String dimM,
			String pathT, String dimT, boolean order) {
		this(outPath, func, pathM, KUtil.str2intArray(dimM), pathT,
				KUtil.str2intArray(dimT), order);
	}

	public MultiMatch(String outPath, String func, String pathM, int[] dimM,
			String pathT, int[] dimT, boolean order) {
		this(func, pathM, dimM, pathT, dimT, order);
		this.setOutClosure(outPath);
	}

	public MultiMatch(String func, String pathM, int[] dimM, String pathT,
			int[] dimT, boolean Order) {
		super();
		this.markerset = null;
		this.setMap = null;
		this.outClosureMap = null;
		this.mixerMap = new HashMap();
		this.funC = (func.toUpperCase()).charAt(0); // L,R,I,Fのいずれか
		this.pathM = pathM;
		this.pathT = pathT;
		this.dimM = dimM;
		this.dimT = dimT;
		this.Order = Order;// True:MT false:TM
	}

	// -------------------------------------------------------------------------
	// ID(key)による振り分け処理を行う為の識別マーカーの設定（Ｉｄによるネガポジ等、種類分け処理）
	// idにしるしをつけて、それに対応したクロージャを実行させることができる
	// ※何もマッピングしていない場合はスペースをマーカーに割り当てる
	// -------------------------------------------------------------------------
	public void setOutClosure(String outPath) {
		this.setOutClosure("", null, new EzWriter(outPath));
	}

	public void setOutClosure(String marker, HashSet set,
			Inf_BaseClojure closure) {
		if (marker.equals("")) {
			defaultClosure = closure;
		} else {
			if (this.markerset == null)
				this.markerset = new HashSet();
			if (this.setMap == null)
				this.setMap = new HashMap();
			if (this.outClosureMap == null)
				this.outClosureMap = new HashMap();
			this.markerset.add(marker);
			this.setMap.put(marker, set);
			this.outClosureMap.put(marker, closure);
		}
	}

	@Override
	public void execute() {
		super.start("MultiMatch", 2048);
		if (elapseCheck) {
			elapse = new kyPkg.tools.Elapse("MultiMatch");
			elapse.start();
		}
		closureInit();// 出力クロージャopen処理
		dmyM = "";
		dmyT = "";
		if (pathM.equals(pathT)) {
			// 同じソースの場合、大量のデータを想定すると・・インコアさせてたくない
			sameSource = true;
			File49ers f49R = new File49ers(pathT);// TRAN
			// System.out.println("　master/tran => sameSource");
			// if (delimT == null)
			delimT = f49R.getDelimiter();
			maxcolT = f49R.getMaxColCount();
			maxcolM = maxcolT;
			if (delimiter == null) {
				delimiter = delimT;
			}
		} else {
			sameSource = false;
			File49ers f49M = new File49ers(pathM);// Master
			File49ers f49T = new File49ers(pathT);// TRAN

			// System.out.println("　pathM =>" + pathM);
			// System.out.println("　pathT =>" + pathT);

			// if (delimM == null)
			delimM = f49M.getDelimiter();
			// if (delimT == null)
			delimT = f49T.getDelimiter();

			// System.out.println("　delimM => " + delimM);
			// System.out.println("　delimT => " + delimT);

			maxcolM = f49M.getMaxColCount();
			maxcolT = f49T.getMaxColCount();
			// 入力データの区切りが同じならそれを使用する、違っていたらタブをデフォルトとする
			if (delimiter == null) {
				if (delimM.equals(delimT)) {
					delimiter = delimT;
				} else {
					delimiter = "\t";
				}
			}
			// ダミーセルおよびダミーパラメータ（全カラム出力）を作る
			if (dimM == null) {
				dimM = getIntArray(1, maxcolM);
				dmyM = getDummy(maxcolM - 1, delimiter);
			} else {
				dmyM = getDummy(dimM.length, delimiter);
			}
			if (dimT == null) {
				dimT = getIntArray(1, maxcolT);
				dmyT = getDummy(maxcolT - 1, delimiter);
			} else {
				dmyT = getDummy(dimT.length, delimiter);
			}
			// --------------------------------------------------------------------
			// マスターINCORE
			// --------------------------------------------------------------------
			masterIncore(pathM, delimM, keyM, dimM);
		}
		writeCount = 1;
		// -----------------------------------------------------------------
		// tranLoop
		// -----------------------------------------------------------------
		tranLoop(pathT, delimT, keyT, dimT, dimM);
		// -----------------------------------------------------------------
		// masterOnly
		// -----------------------------------------------------------------
		long cnt_M = masterOnly();
		// System.out.println("# masterOnly Count:" + cnt_M);
		stop();// 正常終了
		closureFin();
		if (elapseCheck)
			elapse.stop();

		super.stop();// 正常終了
	}

	// -------------------------------------------------------------------------
	// incore
	// -------------------------------------------------------------------------
	private void masterIncore(String path, String delim, int keyCol,
			int[] dim) {
		long counter = 0;
		String key = "";
		String rec = "";
		StringBuffer buf = new StringBuffer();
		try {
			// System.out.println("@masterIncore	master==>>" + path);
			BufferedReader reader = getBufferedReader(path);
			while ((rec = reader.readLine()) != null) {
				counter++;
				buf.delete(0, buf.length());
				String[] splited = rec.split(delim);
				if (splited != null && splited.length > 0) {
					key = splited[keyCol]; // XXX Key位置はゼロでよいのか？オプションを追加するか？？
					// System.out.println("key==>>" + key);

					for (int i = 0; i < dim.length; i++) {
						if (i >= 1)
							buf.append(delimiter);
						int pos = dim[i];
						if (pos < splited.length) {
							buf.append(splited[pos]);
						}
					}
					// if (key.equals("73225580")) {
					// System.out.println("==>>" + buf.toString());
					// }
					// 同じキーのマスターデータはスタックされる
					modElementVal(key, buf.toString());
				}
			}
			reader.close();
			// System.out.println("masterIncoreCount:" + counter);
		} catch (IOException ie) {
			ie.printStackTrace();
			abend();
		} catch (Exception e) {
			e.printStackTrace();
			abend();
		}
	}

	// -------------------------------------------------------------------------
	// tranLoop(Tran Data)
	// -------------------------------------------------------------------------
	private void tranLoop(String path, String delim, int keyCol, int[] dimT,
			int[] dimM) {
		long counter = 0;
		// System.out.println("MultiMatch@tranLoop  path:" + path);
		String key = "";
		String rec = "";
		String val_M = null;
		String val_T = null;
		ElementsLR_Plus masterElement = null;
		StringBuffer buffT = new StringBuffer();
		StringBuffer buffM = new StringBuffer();
		try {
			BufferedReader reader = getBufferedReader(path);
			while ((rec = reader.readLine()) != null) {
				// System.out.println("debug>>>" + rec);
				counter++;
				String[] splited = rec.split(delim);
				if (splited != null && splited.length > 0) {
					key = splited[keyCol]; // XXX Key位置はゼロでよいのか？オプションを追加するか？？
					buffT.delete(0, buffT.length());
					for (int i = 0; i < dimT.length; i++) {
						if (i >= 1)
							buffT.append(delimiter);
						int pos = dimT[i];
						if (pos < splited.length) {
							buffT.append(splited[pos]);
						}
					}
					// 同じソースの場合マスターインコアさせないので、ここでつくっている
					if (sameSource) {
						buffM.delete(0, buffM.length());
						for (int i = 0; i < dimM.length; i++) {
							if (i >= 1)
								buffM.append(delimiter);
							int pos = dimM[i];
							if (pos < splited.length) {
								buffM.append(splited[pos]);
							}
						}
						masterElement = setElementVal(key, buffM.toString());
					} else {
						masterElement = (ElementsLR_Plus) mixerMap.get(key);
					}
					if (masterElement != null) {
						val_M = null;
						val_T = buffT.toString();
						masterElement.setDone();// 書き込み済みという意味
						// スタックされているマスターエレメント分ループ処理を行う
						ArrayList list = masterElement.getList();
						for (Iterator iter = list.iterator(); iter.hasNext();) {
							val_M = (String) iter.next();
							write(key, val_M, val_T);
						}
					} else {
						// Master Not Found(TranOnly!)
						val_M = null;
						write(key, val_M, val_T);
					}
				}
			}
			reader.close();
		} catch (IOException ie) {
			ie.printStackTrace();
			abend();
		} catch (Exception e) {
			e.printStackTrace();
			abend();
		}
		// System.out.println("MultiMatch@tranLoop  end:counter:" + counter);
	}

	// -------------------------------------------------------------------------
	// masterOnly
	// -------------------------------------------------------------------------
	private long masterOnly() {
		long counter = -1;
		String val_M = null;
		Set set = mixerMap.entrySet(); // 直接iteratorを呼べないので一旦SETを取得する
		Iterator it = set.iterator();
		while (it.hasNext()) {
			setCurrent((int) writeCount);
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
			String key = (String) ent.getKey();
			ElementsLR_Plus masterElement = (ElementsLR_Plus) ent.getValue();
			val_M = null;
			// 書き込み済みではなかったら
			if (masterElement != null && !masterElement.isDone()) {
				ArrayList list = masterElement.getList();
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					val_M = (String) iter.next();
					write(key, val_M, null);
				}
			}
		}
		return counter;
	}

	// -------------------------------------------------------------------------
	// write
	// -------------------------------------------------------------------------
	public void write(String key, String val_M, String val_T) {
		StringBuffer buf = new StringBuffer();
		boolean wSw = false;
		switch (funC) {
		case 'I':
			if (val_M != null && val_T != null) {
				wSw = true;
			}
			break;
		case 'L':
			if (val_M != null) {
				if (val_T == null)
					val_T = dmyT;
				wSw = true;
			}
			break;
		case 'R':
			if (val_T != null) {
				if (val_M == null)
					val_M = dmyM;
				wSw = true;
			}
			break;
		default: // Full Join
			if (val_M == null)
				val_M = dmyM;
			if (val_T == null)
				val_T = dmyT;
			wSw = true;
			break;
		}
		if (wSw) {
			buf.delete(0, buf.length());
			buf.append(key);
			buf.append(delimiter);
			// -----------------------------------------------------------------
			if (Order) {// Master_Tranの順で接合する場合
				if (dimM != null) {
					buf.append(val_M);
					if (dimT != null)
						buf.append(delimiter);
				}
				if (dimT != null) {
					buf.append(val_T);
				}
			} else {
				if (dimT != null) {
					buf.append(val_T);
					if (dimM != null)
						buf.append(delimiter);
				}
				if (dimM != null) {
					buf.append(val_M);
				}

			}
			// -----------------------------------------------------------------

			// どれかに当てはまればよい場合はブレークすれば効率が良い・・・
			// どれにも当てはまらなかったらデフォルトという流れにはなっていない・・・
			Inf_BaseClojure outClosure = null;
			if (this.markerset == null) {
				outClosure = defaultClosure;
				if (outClosure != null) {
					outClosure.execute(buf.toString());
					writeCount++;
				}
			} else {
				for (Iterator iter = markerset.iterator(); iter.hasNext();) {
					String marker = (String) iter.next();
					Set set = setMap.get(marker);
					if (set != null && set.contains(key)) {
						outClosure = outClosureMap.get(marker);
					}
					if (outClosure != null) {
						outClosure.execute(buf.toString());
						writeCount++;
					}
				}

			}
		}
	}

	// -------------------------------------------------------------------------
	// closure.init();
	// -------------------------------------------------------------------------
	private void closureInit() {
		if (defaultClosure != null) {
			defaultClosure.init();
		}
		if (this.outClosureMap != null) {
			Set set = outClosureMap.entrySet();
			Iterator it = set.iterator();
			while (it.hasNext()) {
				java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
				Inf_BaseClojure closure = (Inf_BaseClojure) ent.getValue();
				if (closure != null)
					closure.init();
			}
		}
	}

	// -------------------------------------------------------------------------
	// closure.fin();
	// -------------------------------------------------------------------------
	private void closureFin() {
		if (defaultClosure != null) {
			defaultClosure.write();
		}
		if (this.outClosureMap != null) {
			Set set = outClosureMap.entrySet();
			Iterator it = set.iterator();
			while (it.hasNext()) {
				java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
				Inf_BaseClojure closure = (Inf_BaseClojure) ent.getValue();
				if (closure != null)
					closure.write();
			}
		}
	}

	// -------------------------------------------------------------------------
	// 使用例＞
	// new Filters().Flt_Mixer(JoinType,"mix.txt","\t","Left.txt","Right.txt");
	// <<JoinType>>
	// Left 左側のファイルを軸（左側にしか存在しない場合ダミーセルが発生する）
	// Right 右側のファイルを軸（右側にしか存在しない場合ダミーセルが発生する）
	// Inner 左右の両方に存在するキーを軸（ダミーセルは発生しない）
	// Full 左右のどちらかにに存在するキーを軸（どちらかに存在しない場合ダミーセルが発生する）
	// -------------------------------------------------------------------------
	public static void test1105() {
		// それぞれ必要なカラムを指定した場合
		String pathM = "c:/sample/masterX.txt";
		String pathT = "c:/sample/tranX.txt";
		String dest = "c:/sample/MultiMatchTest.txt";
		int[] dimM = { 1, 2 };
		int[] dimT = { 1, 2, 3, 4, 5, 6 };
		MultiMatch mixer = new MultiMatch(dest, "R", pathM, dimM, pathT, dimT,
				true);
		mixer.setElapseCheck(true);
		mixer.execute();
	}

	public static void testFull() {
		// fullジョインの例（例が良くないかんじ・・・）
		String atomDir = ResControl.getAtomDir();
		String pathM = atomDir + "伊藤園抹茶分析用.txt";
		String pathT = atomDir + "伊藤園抹茶分析用.txt";
		String dest = "c:/sample/MultiMatchTest.txt";
		MultiMatch mixer = new MultiMatch(dest, "full", pathM, "9,1", pathT,
				"8", true);
		mixer.setElapseCheck(true);
		mixer.execute();
	}

	public static void test1020() {
		// カラムを指定しない場合は全部のカラムを連結する
		String pathM = "c:/sample/masterX.txt";
		String pathT = "c:/sample/tranX.txt";
		String dest = "c:/sample/MultiMatchTest.txt";
		MultiMatch mixer = new MultiMatch(dest, "R", pathM, pathT, true);
		mixer.setElapseCheck(true);
		mixer.execute();
	}

	// public static void test0826() {
	// // -----------------------------------------------------------------
	// // データ部合成　　たぶん区切り文字の指定がおかしくなってしまっているのだと思う！
	// // -----------------------------------------------------------------
	// System.out.println("#　データ部合成　#");
	// String userDir = kyPkg.uFile.ResControl.getQPRHome();
	// String outPath = ResControl.getCurrentPath();
	// String atomDir = ResControl.getAtomDir();
	// String targetPath = atomDir + "ＱＰＲアンケート/属性・メディア編/2010/q36.txt";
	// String modPath = atomDir + "ＱＰＲアンケート/属性・メディア編/2009/q36.txt";
	// MultiMatch mixer = new MultiMatch(outPath, "full", targetPath, modPath,
	// true);
	// mixer.setDelimiter(",");
	// mixer.execute();
	//
	// }

	// public static void test110711() {
	// // ---------------------------------------------------------------------
	// // アンケートとの連結
	// // アンケート項目とキーマッチングをして、購買データのお尻に、アンケートデータをくっつけたものを出力する
	// // ---------------------------------------------------------------------
	// System.out.println("### MultiMatch ###");
	// String userDir = kyPkg.uFile.ResControl.getQPRHome();
	// String enqMix1 = userDir + "828111000507/calc/enqMix1.txt";
	// String enqPath = ResControl.getCurrentPath();
	// String mapRed1 = userDir + "828111000507/calc/MapReduce1.txt";
	// MultiMatch mixer = new MultiMatch(enqMix1, "R", enqPath, mapRed1, false);
	// mixer.execute();
	//
	// int relPosL = mixer.getMaxcolL();// アンケートデータのカラム数
	// int relPosR = mixer.getMaxcolR();// 購買データのカラム数
	// System.out.println("  maxcolL:" + relPosL);
	// System.out.println("  maxcolR:" + relPosR);
	//
	// }

	public static void main(String[] args) {
		// test1020();
		// testfull();
		// tester();
		// test0826();
		// test110711();
	}

	// public static void tester() {
	// String userDir = kyPkg.uFile.ResControl.getQPRHome();
	// String outPath = userDir + "MapReduce1.txt";
	// String func = "R";
	// String pathM = ResControl.getCurrentPath();
	// String pathT = userDir + "MapReduce0.txt";
	// new MultiMatch(outPath, func, pathM, pathT, true).execute();
	// }

	// フィルタを使ったテストも書いておく・・・さて属性を購買データにくっつけてみるか？！
	// メタデータから、リストボックスにパラメータを吐き出させる
}
