package kyPkg.sql;

import static kyPkg.util.Joint.join;

import kyPkg.filter.EzReader;
import kyPkg.filter.EzWriter;
import kyPkg.filter.Inf_iClosure;
import kyPkg.filter.Inf_oClosure;
import kyPkg.task.Abs_BaseTask;

// 20160615 yuasa
//　生協データをマスターデータとマッチングして・・・云々　
//TODO	マクロミルに転送するデータもここで作ってしまう
public class Matching_Nsei extends Abs_BaseTask {
	private int keyCol = 0;
	private String key_L = null;
	private String key_R = null;
	private Inf_iClosure reader_T = null; // 入力クロージャ
	private Inf_iClosure reader_M = null; // 入力クロージャ
	private Inf_oClosure writer = null;
	private String delimiter = null;
	private String[] splited_T;
	private String[] splited_M;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Matching_Nsei(String outPath, String inPath_Tran, String inPath_Master) {
		reader_T = new EzReader(inPath_Tran);
		reader_M = new EzReader(inPath_Master);
		writer = new EzWriter(outPath);
	}

	// -------------------------------------------------------------------------
	// read_L
	// -------------------------------------------------------------------------
	private String read_T() {
		splited_T = reader_T.readSplited();
		if (splited_T != null && splited_T.length > keyCol) {
			return splited_T[keyCol].trim();
		}
		return null;
	}

	// -------------------------------------------------------------------------
	// read_R
	// -------------------------------------------------------------------------
	private String read_M() {
		splited_M = reader_M.readSplited();
		if (splited_M != null && splited_M.length > keyCol) {
			return splited_M[keyCol].trim();
		}
		return null;
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("<<START>>");
		long cnt_EQ = 0;
		long cnt_L = 0;
		long cnt_R = 0;
		reader_T.open();
		reader_M.open();
		writer.open();
		if (delimiter == null)
			delimiter = reader_T.getDelimiter();
		key_L = read_T();
		key_R = read_M();
		int cmp = 0;
		while (key_L != null && key_R != null) {
			cmp = key_L.compareTo(key_R);
			// System.out.println("=> L:" + key_L + " R:" + key_R);
			if (cmp < 0) {
				writer.write(join(splited_T, delimiter));
				cnt_L++;//L only
				key_L = read_T();
			} else if (cmp > 0) {
				writer.write(join(splited_M, delimiter));
				cnt_R++;//R only
				key_R = read_M();
			} else {
				//	System.out.println("=> L:" + key_L + " R:" + key_R);
//				if (key_L.equals("4515445811310")) {
//					System.out.println("Helllo");
//				}

				for (int idx = 0; idx < splited_T.length; idx++) {
					if (idx != keyCol) { //キーのカラムは処理対象外 
						String val_T = splited_T[idx];//new
						String val_M = splited_M[idx];//master
						//Rが現状のマスタの内容、・Lは新規更新データイメージ
						//空白あるいは空値以外の時にLの値で上書きする
						if (val_T.trim().equals("")) {
						} else if (kyPkg.converter.ValueChecker.isSPC(val_T)) {//連続する空白文字
						} else if (kyPkg.converter.ValueChecker.isZERO(val_T)) {//連続する0
						} else if (kyPkg.converter.ValueChecker.isKJSPC(val_T)) {//連続する漢字空白文字
						} else if (val_M.equals(val_T)) {//変更があるかどうか・・・
						} else {
//							splited_T[idx] = val_M;//どれにも当てはまらない場合のみ上書きをする
							splited_M[idx] = val_T;//どれにも当てはまらない場合のみ上書きをする
						}
					}
				}
				writer.write(join(splited_M, delimiter));
//				writer.write(join(splited_T, delimiter));//20170720 ここ違うんじゃないか？？
				cnt_EQ++;
				key_L = read_T();
				key_R = read_M();
			}
		}
		reader_T.close();
		reader_M.close();
		writer.close();
		System.out.println("L only:" + cnt_L);
		System.out.println("R only:" + cnt_R);
		System.out.println("EQ    :" + cnt_EQ);
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		testMatching();
	}

	public static void testMatching() {
		String wkDir = "C:/samples/生協テスト/";
		String inPath_L = wkDir + "formated.txt";
		String inPath_R = wkDir + "masterImage.txt";
		String outPath = wkDir + "xxxxx.OUT";
		new Matching_Nsei(outPath, inPath_L, inPath_R).execute();
	}
}
