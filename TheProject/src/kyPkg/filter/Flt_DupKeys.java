package kyPkg.filter;

import kyPkg.task.Abs_BaseTask;

// 2016-06-01 重複するキーを出力する
//スタンドアローン化しておけば　バッチで自在に使える・・・
public class Flt_DupKeys extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private Inf_iClosure reader = null;
	private Inf_oClosure writer = null;
	private String delimiter = null;
	private int keyCol = 0;
	private String preKey = null;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Flt_DupKeys(String outPath, String inPath, int keyCol) {
		this.reader = new EzReader(inPath);
		this.writer = new EzWriter(outPath);
		this.keyCol = keyCol;
		this.preKey = null;
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("<<START>>");
		long wCnt = 0;
		reader.open();
		writer.open();
		if (delimiter == null)
			delimiter = reader.getDelimiter();
		String[] splited = reader.readSplited();
		while (splited != null) {
			if (splited.length > keyCol) {
				String key = splited[keyCol];
				if (preKey != null && key.equals(preKey)) {
					System.out.println("duplicate:" + preKey);
					writer.write(preKey);
					wCnt++;
				}
				preKey = key;
			}
			splited = reader.readSplited();
		}
		reader.close();
		writer.close();
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test01();
		test02();
	}

	public static void test01() {
		//---------------------------------------------------------------------
		//	重複しているキー（ＪＡＮコードなど）をファイルに書き出す
		//---------------------------------------------------------------------
		String userDir = globals.ResControl.getQPRHome();
		String inPath = "c:/Users/EJQP7/hoge.txt";
		String outPath = "c:/Users/EJQP7/dupKeys.txt";
		int keyCol = 0;
		new Flt_DupKeys(outPath, inPath, keyCol).execute();
	}

	public static void test02() {
		//---------------------------------------------------------------------
		//	キー情報（重複しているＪＡＮコード）に一致するものとしないもの（ユニークなデータ）に振り分け出力を行う
		//---------------------------------------------------------------------
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("# template #");
		elapse.start();
		String path_M = "c:/Users/EJQP7/dupKeys.txt";
		String path_T = "c:/Users/EJQP7/hoge.txt";
		String path_O = "c:/Users/EJQP7/notDuplicated.txt";
		String path_D = "c:/Users/EJQP7/Duplicated.txt";
		Flt_Venn venn = new Flt_Venn(path_O, path_M, path_T,
				Flt_Venn.RIGHT_ONLY);//重複は含まない
		venn.setOutPath_I(path_D);
		//それぞれ取り出したいのだが・・・インナーとトランオンリーのそれぞれを別々に取り出したい
		//		venn.setMasterKeyCol(0);
		//		venn.setTranKeyCol(0);
		//		venn.setDelimiter(",");
		venn.execute();
		elapse.stop();
	//---------------------------------------------------------------------

	
	}

}
