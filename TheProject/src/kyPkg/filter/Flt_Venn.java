package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import globals.ResControl;
import kyPkg.converter.DCnvConcatinate;
import kyPkg.converter.DCnvDefault;
import kyPkg.converter.DCnvKeyOnly;
import kyPkg.task.Abs_ProgressTask;

public class Flt_Venn extends Abs_ProgressTask {
	public static final String CONCATINATE = "Concatinate";
	public static final String KEY_ONLY = "Key Only";
	public static final String DEFAULT = "Default";
	// -------------------------------------------------------------------------
	// ベン図のどの部分が必要なのかを指定する　　
	// （L（I）R）の、それぞれの重みが（4（2）1）として、必要な部分の値を足した値をwriteOptに指定する
	// ※注意！！tranに指定したものがinnerに出力される（masterは捨てられる）
	// ・・・tran中のマスターに含まれている要素を取り出したい為そういう仕様となっている
	// -------------------------------------------------------------------------
	public static final int LEFT_ONLY = 4; // MASTER_ONLY
	public static final int INNER_JOIN = 2;//INNER_JOIN
	public static final int RIGHT_ONLY = 1;// TRAN_ONLY
	public static final int FULL = LEFT_ONLY + INNER_JOIN + RIGHT_ONLY;

	public static final String LEFT_ONLY_COUNT = "LEFT_ONLY_COUNT";
	public static final String RIGHT_ONLY_COUNT = "RIGHT_ONLY_COUNT";
	public static final String INNER_JOIN_COUNT = "INNER_JOIN_COUNT";
	public static final String LEFT_READ_COUNT = "LEFT_READ_COUNT";
	public static final String RIGHT_READ_COUNT = "RIGHT_READ_COUNT";

	private boolean headerOpt = false;//１レコード目をヘッダーとしてとっておき、先頭に出力する場合

	public void setHeaderOpt(boolean headerOpt) {
		this.headerOpt = headerOpt;
	}

	private boolean trimOpt = false;
	private boolean append = false; // 出力を追書きする場合はtrue
	private int keyCol_M = 0; // マスター側のKeyカラム
	private int keyCol_T = 0; // tran側のKeyカラム
	private HashMap<String, MasterElem> mixerMap;
	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private Inf_iClosure reader_M = null;
	private Inf_iClosure reader_T = null;
	// ------------------------------------------------------------------------
	// 出力クロージャ
	// ------------------------------------------------------------------------
	private EzWriterDual writer_M = null;
	private EzWriterDual writer_I = null;
	private EzWriterDual writer_T = null;

	private long read_cnt_L;
	private long read_cnt_R;
	private long join_cnt;
	private long rOnly_cnt;
	private long lOnly_cnt;
	private String[] masterHeads;
	private String[] tranHeads;
	private List<String[]> modMaster;
	private EzWriterDual outClosure;

	private String delimiter = "\t";

	public String getDelimiter() {
		return delimiter;
	}

	public void setDualConverter(Inf_DualConverter dualConverter) {
		if (outClosure != null)
			outClosure.setDualConverter(dualConverter);
	}

	// ------------------------------------------------------------------------
	//マスターにしか存在しないデータを別出力する場合
	// ------------------------------------------------------------------------
	public void setOutPath_M(String path) {
		if (!path.trim().equals(""))
			this.writer_M = new EzWriterDual(path);
	}

	// ------------------------------------------------------------------------
	//マスターにマッチしたデータを別出力する場合
	// ------------------------------------------------------------------------
	public void setOutPath_I(String path) {
		if (!path.trim().equals(""))
			this.writer_I = new EzWriterDual(path);
	}

	// ------------------------------------------------------------------------
	//Tranのみに存在するデータを別出力する場合
	// ------------------------------------------------------------------------
	public void setOutOutPath_T(String path) {
		if (!path.trim().equals(""))
			this.writer_T = new EzWriterDual(path);
	}

	// ------------------------------------------------------------------------
	//Master側の１レコード目を配列で返す
	// ------------------------------------------------------------------------
	public String[] getMasterHeads() {
		return masterHeads;
	}

	public String getMasterHead(String delimiter, int skip) {
		return join(masterHeads, delimiter, skip);
	}

	// ------------------------------------------------------------------------
	//Tran側の１レコード目を配列で返す
	// ------------------------------------------------------------------------
	public String[] getTranHeads() {
		return tranHeads;
	}

	public String getTranHead(String delimiter) {
		return join(tranHeads, delimiter);
	}

	public void setModMaster(String val) {
		List<String[]> modMaster = new ArrayList();
		modMaster.add(new String[] { val });
		setModMaster(modMaster);
	}

	public void setModMaster(List<String[]> modMaster) {
		this.modMaster = modMaster;
	}

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Flt_Venn(String masterPath, String tranPath) {
		this(tranPath, masterPath, tranPath, INNER_JOIN); // ※出力処理完了後に出力パスを入力パスにリネームする場合
	}

	public Flt_Venn(String outPath, String masterPath, String tranPath) {
		this(outPath, masterPath, tranPath, INNER_JOIN); // ※インナージョインのみを出力する
	}

	public Flt_Venn(String out, String master, String tran, int which) {
		this(new EzWriterDual(out), new EzReader(master), new EzReader(tran),
				which);
	}

	//20170726　コンバータを指定可能なタイプ
	//TODO　これをバッチ化したい・・・つまいmainから引数で動作させたいということ・・・・・うむ
	public Flt_Venn(String out, String master, String tran, int which,
			String cnvType) {
		this(new EzWriterDual(out), new EzReader(master), new EzReader(tran),
				which);
//		//#createTester--------------------------------------------------
//		System.out.println("public static void testFlt_Venn() {");
//		System.out.println("    String out = \"" + out + "\";");
//		System.out.println("    String master = \"" + master + "\";");
//		System.out.println("    String tran = \"" + tran + "\";");
//		System.out.println("    int which = " + which + ";");
//		System.out.println("    String cnvType = \"" + cnvType + "\";");
//		System.out.println(
//				"    Flt_Venn ins = new Flt_Venn(out,master,tran,which,cnvType);");
//		System.out.println("}");
//		//--------------------------------------------------

		if (cnvType.equalsIgnoreCase(CONCATINATE)) {
			setDualConverter(new DCnvConcatinate(delimiter));
		} else if (cnvType.equalsIgnoreCase(KEY_ONLY)) {
			setDualConverter(new DCnvKeyOnly());
		} else {
			setDualConverter(new DCnvDefault(delimiter));
		}
	}

	public Flt_Venn(EzWriterDual outClosure, Inf_iClosure master,
			Inf_iClosure tran, int which) {
		super();
		this.reader_T = tran; // Tran
		this.reader_M = master; // Master
		this.outClosure = outClosure;
		if ((which & LEFT_ONLY) == LEFT_ONLY)
			this.writer_M = outClosure;// 左側のみの出力先を指定
		if ((which & INNER_JOIN) == INNER_JOIN)
			this.writer_I = outClosure;// 中央のみの出力先を指定
		if ((which & RIGHT_ONLY) == RIGHT_ONLY)
			this.writer_T = outClosure;// 右側のみの出力先を指定
	}

	//	private void writeOpt(EzWriterDual outClosure, int which) {
	//		if ((which & LEFT_ONLY) == LEFT_ONLY)
	//			setOutProc(LEFT_ONLY, outClosure);// 左側のみの出力先を指定
	//		if ((which & INNER_JOIN) == INNER_JOIN)
	//			setOutProc(INNER_JOIN, outClosure);// 中央のみの出力先を指定
	//		if ((which & RIGHT_ONLY) == RIGHT_ONLY)
	//			setOutProc(RIGHT_ONLY, outClosure);// 右側のみの出力先を指定
	//	}
	//	private void setOutProc(int which, EzWriterDual outClosure) {
	//		switch (which) {
	//		case LEFT_ONLY:
	//			this.writer_M = outClosure;
	//			break;
	//		case RIGHT_ONLY:
	//			this.writer_T = outClosure;
	//			break;
	//		case INNER_JOIN:
	//			this.writer_I = outClosure;
	//			break;
	//		default:
	//			break;
	//		}
	//	}

	public void setAppendMode(boolean append) {
		this.append = append;
	}

	public void setTrimOpt(boolean trimOpt) {
		this.trimOpt = trimOpt;
	}

	// -------------------------------------------------------------------------
	// 20121219 yuasa
	// ※注意、これはexecuteの直前にセットしないといけない
	// -------------------------------------------------------------------------
	public void setDelimiter(String delimiter) {
		setInDelimiter(delimiter);
		setOutDelimiter(delimiter);
	}

	public void setInDelimiter(String inDelimiter) {
		this.reader_T.setDelimiter(inDelimiter);
		this.reader_M.setDelimiter(inDelimiter);
	}

	// -------------------------------------------------------------------------
	// 20110728 yuasa
	// -------------------------------------------------------------------------
	public void setOutDelimiter(String outDelimiter) {
		this.delimiter = outDelimiter;
		if (this.writer_M != null)
			this.writer_M.setDelimiter(outDelimiter);
		if (this.writer_T != null)
			this.writer_T.setDelimiter(outDelimiter);
		if (this.writer_I != null)
			this.writer_I.setDelimiter(outDelimiter);
	}

	public void setMasterKeyCol(int masterKeyCol) {
		this.keyCol_M = masterKeyCol;
	}

	public void setTranKeyCol(int tranKeyCol) {
		this.keyCol_T = tranKeyCol;
	}

	// ------------------------------------------------------------------------
	// i/oのサマリーをハッシュマップで返す
	// ------------------------------------------------------------------------
	public HashMap<String, Long> summary() {
		HashMap<String, Long> map = new HashMap();
		map.put(LEFT_READ_COUNT, read_cnt_L);
		map.put(RIGHT_READ_COUNT, read_cnt_R);
		map.put(INNER_JOIN_COUNT, join_cnt);
		map.put(RIGHT_ONLY_COUNT, rOnly_cnt);
		map.put(LEFT_ONLY_COUNT, lOnly_cnt);
		return map;
	}

	public long getTranReadCount() {
		return read_cnt_R;
	}

	public long getMasterReadCount() {
		return read_cnt_L;
	}

	public long getWriteCount() {
		return writer_I.getWriteCount();
	}

	// ------------------------------------------------------------------------
	// Mapマッチング用内部クラス
	// ------------------------------------------------------------------------
	private class MasterElem {
		private String[] values = null;
		private String mark = ""; // master only = ""

		public String getMarker() {
			return mark;
		}

		public void setMark(String mark) {
			this.mark = mark;
		}

		public String[] getValues() {
			return values;
		}

		public void setValues(String[] values) {
			this.values = values;
		}
	}

	@Override
	public void execute() {
		super.start("Flt_Venn", 2048);
		if (writer_M != null)
			writer_M.open(append);
		if (writer_I != null)
			writer_I.open(append);
		if (writer_T != null)
			writer_T.open(append);
		mixerMap = new HashMap();
		// -----------------------------------------------------------------
		masterIncore();// マスター読み込み
		// -----------------------------------------------------------------
		tranLoop();// 変換データ読み込み
		// -----------------------------------------------------------------
		masterOnly();
		// -----------------------------------------------------------------
		stop();// 正常終了
		if (writer_M != null)
			writer_M.close();
		if (writer_I != null)
			writer_I.close();
		if (writer_T != null)
			writer_T.close();
		// System.out.println("## Flt_Venn end ##");
		super.stop();// 正常終了
	}

	// -------------------------------------------------------------------------
	// incore
	// -------------------------------------------------------------------------
	private void masterIncore() {
		read_cnt_L = 0;
		if (modMaster != null)
			mod2Master();
		reader_M.open();
		String[] cells = null;
		while ((cells = reader_M.readSplited()) != null) {
			read_cnt_L++;
			if (read_cnt_L == 1) {
				masterHeads = cells;
			}
			add2MixerMap(cells);
		}
		reader_M.close();
	}

	private void add2MixerMap(String[] cells) {
		if (cells != null && cells.length > keyCol_M) {
			String key = cells[keyCol_M]; // マスター側のKey
			if (trimOpt)
				key = key.trim();
			MasterElem element = (MasterElem) mixerMap.get(key);
			if (element == null)
				element = new MasterElem();
			element.setValues(cells);
			mixerMap.put(key, element);
		}
	}

	//入力とはべつにマスターの値を注入する場合に使用する＜20160506＞
	private void mod2Master() {
		for (String[] cells : modMaster) {
			add2MixerMap(cells);
		}
	}

	// -------------------------------------------------------------------------
	// tranLoop(Tran Data)
	// -------------------------------------------------------------------------
	private void tranLoop() {
		read_cnt_R = 0;
		join_cnt = 0;
		rOnly_cnt = 0;
		String key = "";
		MasterElem element = null;
		reader_T.open();
		String[] cells = null;
		while ((cells = reader_T.readSplited()) != null) {
			// System.out.println("cells.length:" + cells.length);
			read_cnt_R++;
			if (read_cnt_R == 1) {
				tranHeads = cells;
			}
			if (cells != null && cells.length > keyCol_T) {
				key = cells[keyCol_T]; // tran側のKey
				if (trimOpt)
					key = key.trim();
				element = (MasterElem) mixerMap.get(key);
				if (element != null) {
					// Inner Join
					join_cnt++;
					if (writer_I != null) {
						if (headerOpt) {
							writer_I.write(masterHeads, tranHeads, INNER_JOIN);//# Header out #
							headerOpt = false;
						}
						writer_I.write(element.getValues(), cells, INNER_JOIN);
					}
					element.setMark("Dirty"); // 処理済のマーキングを行う
					mixerMap.put(key, element); // 必要があるのか？？同じ参照なのかどうか確認しておく・・・
				} else {
					rOnly_cnt++;
					if (writer_T != null) {
						if (headerOpt) {
							writer_T.write(null, tranHeads, LEFT_ONLY);//# Header out # どうなるんだ？
							headerOpt = false;
						}
						writer_T.write(null, cells, LEFT_ONLY);
					}
				}
			}
		}
		reader_T.close();
	}

	// -------------------------------------------------------------------------
	// masterOnly
	// -------------------------------------------------------------------------
	private void masterOnly() {
		lOnly_cnt = 0;//このカウンタがおかしい?・・・20150928
		List<String> keyList = new ArrayList(mixerMap.keySet());
		System.out.println("## masterOnly keyList.size:" + keyList.size());
		for (String key : keyList) {
			MasterElem element = mixerMap.get(key);
			if (element != null) {
				if (element.getMarker().equals("")) {
					if (writer_M != null) {
						if (headerOpt) {
							writer_M.write(masterHeads, null, RIGHT_ONLY);//# Header out # どうなるんだ？
							headerOpt = false;
						}
						writer_M.write(element.getValues(), null, RIGHT_ONLY);
					}
					lOnly_cnt++;
					setCurrent((int) lOnly_cnt);
				}
			} else {
				System.out.println("ERROR ## masterOnly key:" + key
						+ "## element==null?!");
			}
		}
	}

	public void setConverter(Inf_DualConverter dualConverter) {
		if (this.writer_M != null)
			this.writer_M.setDualConverter(dualConverter);
		if (this.writer_T != null)
			this.writer_T.setDualConverter(dualConverter);
		if (this.writer_I != null)
			this.writer_I.setDualConverter(dualConverter);
	}

	public static void janMoonParts() {
		String path_M = "C:/merge/F_org.txt";
		String path_T = "C:/merge/F_mod.txt"; // innerに出力させるのがこちらなので・・・
		String path_O = "C:/merge/F_Moon.txt";
		Flt_Venn venn = new Flt_Venn(path_O, path_M, path_T,
				Flt_Venn.RIGHT_ONLY);
		venn.setDelimiter("=");
		venn.execute();
	}

	// カテゴリーはオリジナルを優先させて単純に合成する
	// ※注意tran側がinnerに出力されるのでorgをｔに指定している
	public static void categoryMix() {
		String path_M = "C:/merge/E_mod.txt";
		String path_T = "C:/merge/E_org.txt";
		String path_O = "C:/merge/E_mix.txt";
		Flt_Venn venn = new Flt_Venn(path_O, path_M, path_T, Flt_Venn.FULL);
		venn.setDelimiter("=");
		venn.execute();
	}

	public static void testFlt_Venn() {
		String outPath = "C:/@qpr/home/123620000058/calc/mixed.mon";
		String masterPath = "C:/@qpr/home/123620000058/tran/20150401_20150630.mon";
		String tranPath = "C:/@qpr/home/123620000058/tran/20150401_20150630.mon";
		Flt_Venn ins = new Flt_Venn(outPath, masterPath, tranPath);
		System.out.println("fin=>" + ins.getWriteCount());
	}

	public static void testFlt_Venn20150928() {
		String mergeDir = ResControl.getUSERS_ITPBANKS_MERGE_forTest();
		String out = mergeDir + "F_Moon.txt";
		String master = mergeDir + "F_Org.txt";
		String tran = mergeDir + "F_Mod.txt";
		int option = 1;
		Flt_Venn venn = new Flt_Venn(out, master, tran, option);
		venn.setDelimiter("=");
		venn.execute();
		HashMap<String, Long> map = venn.summary();
		List<String> keyList = new ArrayList(map.keySet());
		for (String key : keyList) {
			System.out.println(
					"testFlt_Venn20150928 key:" + key + " val:" + map.get(key));
		}
	}

	// -------------------------------------------------------------------------
	// 使用例＞
	// ベン図のどの部分が必要なのかを指定する　　
	// （L（I）R）の、それぞれの重みが（4（2）1）として、必要な部分の値を足した値をwriteOptに指定する
	// -------------------------------------------------------------------------
	public static void test20151119() {
		String path_M = "C:/@qpr/home/828111000507/tran/20140801_20141031.trv";
		String path_T = "C:/@qpr/home/828111000507/tran/20150801_20151031.trv"; // innerに出力させるのがこちらなので・・・
		String path_O = "C:/@qpr/home/828111000507/tran/L_Only.txt";
		Flt_Venn venn = new Flt_Venn(path_O, path_M, path_T,
				Flt_Venn.LEFT_ONLY);
		venn.setMasterKeyCol(3);
		venn.setTranKeyCol(3);
		//		venn.setDelimiter("=");
		venn.execute();
	}

	// -------------------------------------------------------------------------
	//	20160105	ターゲットファインダー用データ
	// -------------------------------------------------------------------------
	public static void venn_1() {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("# template #");
		elapse.start();
		String path_M = "P:/20151225_yuasa/対象ID.csv";
		String path_T = "P:/20151225_yuasa/stdType1Head.csv"; // innerに出力させるのがこちらなので・・・
		String path_O = "P:/20151225_yuasa/stdType1HeadLimited.csv";
		Flt_Venn venn = new Flt_Venn(path_O, path_M, path_T,
				Flt_Venn.INNER_JOIN);
		venn.setMasterKeyCol(0);
		venn.setTranKeyCol(0);
		venn.setDelimiter(",");
		venn.execute();
		elapse.stop();
	}

	public static void venn_2() {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("# template #");
		elapse.start();
		String path_M = "P:/20151225_yuasa/対象ID.csv";
		String path_T = "P:/20151225_yuasa/stdType2Head.csv"; // innerに出力させるのがこちらなので・・・
		String path_O = "P:/20151225_yuasa/stdType2HeadLimited.csv";
		Flt_Venn venn = new Flt_Venn(path_O, path_M, path_T,
				Flt_Venn.INNER_JOIN);
		venn.setMasterKeyCol(0);
		venn.setTranKeyCol(0);
		venn.setDelimiter(",");
		venn.execute();
		elapse.stop();
	}

	public static void tester() {
		//よく考えたら限定を先にかけてから処理をすりゃよかったんだ！！失敗		
		System.out.println("type2");
		venn_2();
		System.out.println("type1");
		venn_1();
		System.out.println("finish");
	}

	public static void test20170615() {
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("# template #");
		elapse.start();
		// -----------------------------------------------------------------
		// 贈答を除外する　20170615
		// -----------------------------------------------------------------
		String dstPath = ResControl.getCnlSets();
		String giftCnl = dstPath + ResControl.GIFTS;
		String limitedTranPath = "C:/@qpr/home/110098000009/tran/20160901_20170228.trv";
		Flt_Venn venn = new Flt_Venn(limitedTranPath, giftCnl, limitedTranPath,
				Flt_Venn.RIGHT_ONLY);
		venn.setInDelimiter("\t");
		venn.setTranKeyCol(4);
		venn.setMasterKeyCol(0);
		venn.setTrimOpt(true);
		venn.execute();
		// -----------------------------------------------------------------
		elapse.stop();
	}

	// -------------------------------------------------------------------------
	// 使用例＞
	// ベン図のどの部分が必要なのかを指定する　　
	// （L（I）R）の、それぞれの重みが（4（2）1）として、必要な部分の値を足した値をwriteOptに指定する
	// public static final int MASTER = 4; // master
	// public static final int INNER = 2;
	// public static final int TRAN = 1;// tran
	// -------------------------------------------------------------------------
	// * e-trade ? 200,000

	public static void batch(String[] args) {
		if (args.length == 5) {
			String out = args[0];
			String master = args[1];
			String tran = args[2];
			int which = Integer.valueOf(args[3]);
			String cnvType = args[4];
			Flt_Venn ins = new Flt_Venn(out, master, tran, which, cnvType);
			ins.execute();
		} else {
			System.err.println("usage....");
			//もしくはGUI起動とか・・・
		}
	}

	public static void testBatch() {
		String[] args = { "C:/samples/venn/FullJoin.TXT",
				"C:/samples/venn/left.txt", "C:/samples/venn/right.txt", "7",
				"Concatinate" };
		Flt_Venn.batch(args);
	}

	public static void main(String[] args) {
		test20170615();
	}
}
