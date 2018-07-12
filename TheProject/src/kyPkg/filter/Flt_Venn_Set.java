package kyPkg.filter;

import static kyPkg.util.KUtil.int2flags;

import java.util.HashSet;
import java.util.Set;

import kyPkg.task.Abs_ProgressTask;
import kyPkg.uFile.ListArrayUtil;

// -------------------------------------------------------------------------
//２つのファイルのそれぞれのキー項目を比較して、共通するもの、および一方にしか存在しないものをSetに振り分ける
// ベン図のどの部分が必要なのかを指定する　　
// （L（I）R）の、それぞれの重みが（4（2）1）として、必要な部分の値を足した値をwriteOptに指定する
// ※注意！！tranに指定したものがinnerに出力される（masterは捨てられる）
// ・・・tran中のマスターに含まれている要素を取り出したい為そういう仕様となっている
// -------------------------------------------------------------------------
public class Flt_Venn_Set extends Abs_ProgressTask {
	public static final int RIGHT_ONLY = 1;// tran
	public static final int INNER_JOIN = 2;
	public static final int LEFT_ONLY = 4; // master
	public static final int FULL = LEFT_ONLY + INNER_JOIN + RIGHT_ONLY;
	private boolean trim = false;
	private int keyCol_L = 0; // マスター側のKeyカラム
	private int keyCol_R = 0; // tran側のKeyカラム
	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private Inf_iClosure inProc_L = null;
	private Inf_iClosure inProc_R = null;
	private long readCount_L;
	private long readCount_R;
	// ------------------------------------------------------------------------
	private Set<String> leftOnly_Set;
	private Set<String> innerJoin_Set;
	private Set<String> rightOnly_Set;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Flt_Venn_Set(String left, String right) {
		this(new EzReader(left), new EzReader(right));
	}

	public Flt_Venn_Set(String path_L, String path_R, int keyCol_L,
			int keyCol_R) {
		this(new EzReader(path_L), new EzReader(path_R));
		//#createTester--------------------------------------------------
//		System.out.println("public static void testFlt_Venn_Set() {");
//		System.out.println("    String path_L = \"" + path_L + "\";");
//		System.out.println("    String path_R = \"" + path_R + "\";");
//		System.out.println("    int keyCol_L = " + keyCol_L + ";");
//		System.out.println("    int keyCol_R = " + keyCol_R + ";");
//		System.out.println(
//				"    Flt_Venn_Set ins = new Flt_Venn_Set(path_L,path_R,keyCol_L,keyCol_R);");
//		System.out.println("}");
		//--------------------------------------------------

		setLeftKeyCol(keyCol_L);
		setRightKeyCol(keyCol_R);
	}

	private Flt_Venn_Set(Inf_iClosure left, Inf_iClosure right) {
		super();
		this.inProc_L = left; // M:Master
		this.inProc_R = right; // L:Tran
		leftOnly_Set = new HashSet();
		innerJoin_Set = new HashSet();
		rightOnly_Set = new HashSet();
	}

	public void setLeftKeyCol(int keyCol_L) {
		this.keyCol_L = keyCol_L;
	}

	public void setRightKeyCol(int keyCol_R) {
		this.keyCol_R = keyCol_R;
	}

	public void setTrimOpt(boolean trimOpt) {
		this.trim = trimOpt;
	}

	// -------------------------------------------------------------------------
	// 20121219 yuasa
	// ※注意、これはexecuteの直前にセットしないといけない
	// -------------------------------------------------------------------------
	public void setDelimiter(String delimiter) {
		this.inProc_R.setDelimiter(delimiter);
		this.inProc_L.setDelimiter(delimiter);
	}

	public long getTranReadCount() {
		return readCount_R;
	}

	public long getMasterReadCount() {
		return readCount_L;
	}

	@Override
	// -------------------------------------------------------------------------
	// execute
	// -------------------------------------------------------------------------
	public void execute() {
		super.start("Flt_Venn", 2048);
		incore();
//		for (String element : leftOnly_Set) {
//			System.err.println("leftOnly_Set:" + element);
//		}

		loop();
//		for (String element : rightOnly_Set) {
//			System.err.println("rightOnly_Set:" + element);
//		}
//		for (String element : innerJoin_Set) {
//			System.err.println("innerJoin_Set:" + element);
//		}

		stop();
		super.stop();
	}

	// -------------------------------------------------------------------------
	// incore
	// -------------------------------------------------------------------------
	private void incore() {
		readCount_L = 0;
		String key = "";
		inProc_L.open();
		String[] cells = null;
		while ((cells = inProc_L.readSplited()) != null) {
			readCount_L++;
			if (cells != null && cells.length > keyCol_L) {
				key = cells[keyCol_L]; // マスター側のKey
				if (trim)
					key = key.trim();
//				if (!leftOnly_Set.contains(key))
//					System.out.println("key:" + key);
				leftOnly_Set.add(key);
			}
		}
		inProc_L.close();
	}

	// -------------------------------------------------------------------------
	// loop
	// -------------------------------------------------------------------------
	private void loop() {
		readCount_R = 0;
		String key = "";
		inProc_R.open();
		String[] cells = null;
		while ((cells = inProc_R.readSplited()) != null) {
			readCount_R++;
			if (cells != null && cells.length > keyCol_R) {
				key = cells[keyCol_R];
				if (trim)
					key = key.trim();
//				if (key.equals("D0012: オリヒロ広島Ｃ蒟蒻ゼリー")){
//					System.err.println("key:"+key);
//				}
				if (innerJoin_Set.contains(key)) {
				} else if (leftOnly_Set.contains(key)) {
					innerJoin_Set.add(key);
					leftOnly_Set.remove(key);
				} else {
					rightOnly_Set.add(key);
				}
			}
		}
		inProc_R.close();
	}

	// ------------------------------------------------------------------------
	// double Math.log(double a)
	// ※２を低とする３２の対数は５
	// ２の何乗かということ（つまり何オカレンスめにフラグがたつかということを表現したい）
	// 定数との関連付けされているロジックを表したかったがほかに良いアイデアが出てこなかった
	// 分かりにくいのであまりよくない実装だろう
	// ------------------------------------------------------------------------
	public static int log2(int i) {
		return (int) (Math.log(i) / Math.log(2.0));
	}

	public Set<String> getSet(int opt) {
		boolean[] optFlags = int2flags(opt, 3);//合成パラメータを分解
		Set<String> resultSet = new HashSet();
		if (optFlags[0])
			resultSet.addAll(rightOnly_Set);//RIGHT_ONLY
		if (optFlags[1])
			resultSet.addAll(innerJoin_Set);//INNER_JOIN
		if (optFlags[2])
			resultSet.addAll(leftOnly_Set);//LEFT_ONLY
		return resultSet;
	}

	// -------------------------------------------------------------------------
	// 使用例＞
	// ベン図のどの部分が必要なのかを指定する　　
	// （L（I）R）の、それぞれの重みが（4（2）1）として、必要な部分の値を足した値をwriteOptに指定する
	// -------------------------------------------------------------------------
	//	public static void test20151119() {
	//		String path_L = "C:/@qpr/home/828111000507/tran/20140801_20141031.trv";
	//		String path_R = "C:/@qpr/home/828111000507/tran/20150801_20151031.trv"; // innerに出力させるのがこちらなので・・・
	//		Flt_Venn_Set venSet = new Flt_Venn_Set(path_L, path_R, 3, 3);
	//		venSet.execute();
	//		Set<String> set_LEFT_ONLY = venSet.getSet(Flt_Venn_Set.LEFT_ONLY);
	//		Set<String> set_RIGHT_ONLY = venSet.getSet(Flt_Venn_Set.RIGHT_ONLY);
	//		for (String element : set_LEFT_ONLY) {
	//			System.out.println("LEFT_ONLY:" + element);
	//		}
	//	}
	public static void testFlt_Venn_Set20170523() {
		String path_L = "C:/@qpr/home/556845000012/calc/#000_modItp1.txt~inner";
//		String path_L = "C:/@qpr/home/556845000012/calc/#001_modItp2.txt~inner";
		String path_R = "C:/@qpr/home/556845000012/calc/#001_modItp2.txt~inner";
		int keyCol_L = 3;
		int keyCol_R = 3;
		Flt_Venn_Set venSet = new Flt_Venn_Set(path_L, path_R, keyCol_L,
				keyCol_R);
		venSet.execute();
		Set<String> DummyKeys1 = venSet.getSet(Flt_Venn_Set.LEFT_ONLY);
		Set<String> DummyKeys2 = venSet.getSet(Flt_Venn_Set.RIGHT_ONLY);
		ListArrayUtil.set2File("c:/DummyKeys1.txt", DummyKeys1);
		ListArrayUtil.set2File("c:/DummyKeys2.txt", DummyKeys2);
	}

	// -------------------------------------------------------------------------
	// 使用例＞
	// ベン図のどの部分が必要なのかを指定する　　
	// （L（I）R）の、それぞれの重みが（4（2）1）として、必要な部分の値を足した値をwriteOptに指定する
	// public static final int MASTER = 4; // master
	// public static final int INNER = 2;
	// public static final int TRAN = 1;// tran
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		testFlt_Venn_Set20170523();
		//		test20151119();
		//		testFlt_Venn();
		//		testFlt_Venn20150928();
	}
}
