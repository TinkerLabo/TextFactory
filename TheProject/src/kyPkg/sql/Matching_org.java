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
public class Matching_org extends Abs_BaseTask {
	private int keyCol = 0;
	private String key_L = null;
	private String key_R = null;
	private Inf_iClosure reader_L = null; // 入力クロージャ
	private Inf_iClosure reader_R = null; // 入力クロージャ
	private Inf_oClosure writer = null;
	private String delimiter = null;
	private String[] splited_L;
	private String[] splited_R;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Matching_org(String outPath, String inPath_L, String inPath_R) {
		reader_L = new EzReader(inPath_L);
		reader_R = new EzReader(inPath_R);
		writer = new EzWriter(outPath);
	}

	// -------------------------------------------------------------------------
	// read_L
	// -------------------------------------------------------------------------
	private String read_L() {
		splited_L = reader_L.readSplited();
		if (splited_L != null && splited_L.length > keyCol) {
			return splited_L[keyCol].trim();
		}
		return null;
	}

	// -------------------------------------------------------------------------
	// read_R
	// -------------------------------------------------------------------------
	private String read_R() {
		splited_R = reader_R.readSplited();
		if (splited_R != null && splited_R.length > keyCol) {
			return splited_R[keyCol].trim();
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
		reader_L.open();
		reader_R.open();
		writer.open();
		if (delimiter == null)
			delimiter = reader_L.getDelimiter();
		key_L = read_L();
		key_R = read_R();
		int cmp = 0;
		while (key_L != null && key_R != null) {
			cmp = key_L.compareTo(key_R);
			// System.out.println("=> L:" + key_L + " R:" + key_R);
			if (cmp < 0) {
				writer.write(join(splited_L, delimiter));
				cnt_L++;//L only
				key_L = read_L();
			} else if (cmp > 0) {
				writer.write(join(splited_R, delimiter));
				cnt_R++;//R only
				key_R = read_R();
			} else {
				//	System.out.println("=> L:" + key_L + " R:" + key_R);
//				if (key_L.equals("4515445811310")) {
//					System.out.println("Helllo");
//				}

				for (int idx = 0; idx < splited_L.length; idx++) {
					if (idx != keyCol) { //キーのカラムは処理対象外 
						String val_L = splited_L[idx];//new
						String val_R = splited_R[idx];//master
//						System.out.println("val_L:" + val_L);
//						System.out.println("val_R:" + val_R);
						//Rが現状のマスタの内容、・Lは新規更新データイメージ
						//空白あるいは空値以外の時にLの値で上書きする
						if (val_L.equals("")) {
						} else if (kyPkg.converter.ValueChecker.isSPC(val_L)) {//連続する空白文字
						} else if (kyPkg.converter.ValueChecker.isZERO(val_L)) {//連続する0
						} else if (kyPkg.converter.ValueChecker.isKJSPC(val_L)) {//連続する漢字空白文字
						} else if (val_R.equals(val_L)) {//変更があるかどうか・・・
						} else {
							//　splited_R[idx] = "☆" + val_L;
//							splited_R[idx] = val_L;//どれにも当てはまらない場合のみ上書きをする
							splited_L[idx] = val_R;//どれにも当てはまらない場合のみ上書きをする
						}
					}
				}
				writer.write(join(splited_R, delimiter));
				cnt_EQ++;
				key_L = read_L();
				key_R = read_R();
			}
		}
		reader_L.close();
		reader_R.close();
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
		new Matching_org(outPath, inPath_L, inPath_R).execute();
	}
}
