package kyPkg.filter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import kyPkg.task.Abs_ProgressTask;

public class Flt_Rotation extends Abs_ProgressTask {
	private String dummyVal = "";
	private Inf_iClosure inClosure = null; // 入力クロージャ
	private Inf_oClosure outClosure = null; // 出力クロージャ
	private String delimiter = "\t";
	private List<Queue<String>> matrix = null;

	// -------------------------------------------------------------------------
	// 2011-10-05 yuasa
	// CSVデータのＸ，Ｙ軸を交換する
	// ｜Ａ｜Ｂ｜Ｃ｜Ｄ｜
	// ｜↓｜↓｜↓｜↓｜
	// という並びのデータを
	// ｜Ａ→
	// ｜Ｂ→
	// ｜Ｃ→
	// ｜Ｄ→
	// という並びのでーたに変換する
	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Flt_Rotation(String outPath, String inPath) {
		super();
		this.inClosure = new EzReader(inPath); // R:Tran
		this.outClosure = new EzWriter(outPath);
	}

	public long getWriteCount() {
		return outClosure.getWriteCount();
	}

	public void setDelimiter(String delimiter) {
		this.outClosure.setDelimiter(delimiter);
	}

	@Override
	public void execute() {
		super.start("Flt_Rotation", 2048);
		outClosure.open();
		loop();// loop
		output();
		outClosure.close();
		super.stop();// 正常終了
	}

	// -------------------------------------------------------------------------
	// tranLoop
	// -------------------------------------------------------------------------
	private long loop() {
		long iCount = -1;
		inClosure.open();
		String[] cells = null;
		while ((cells = inClosure.readSplited()) != null) {
			iCount++;
			// 先頭行分のリストを用意する（2行目以降のセル数が多い場合捨てられる）
			if (matrix == null) {
				matrix = new ArrayList();
				for (int i = 0; i < cells.length; i++) {
					matrix.add(new LinkedList());
				}
			}
			for (int i = 0; i < matrix.size(); i++) {
				Queue que = matrix.get(i);
				if (cells.length > i) {
					que.add(cells[i]);
				} else {
					que.add(dummyVal);// DummyCell
				}
			}
		}
		inClosure.close();
		return iCount;
	}

	private void output() {
		long oCount = -1;
		StringBuffer buf = new StringBuffer();
		for (Queue<String> que : matrix) {
			buf.delete(0, buf.length());
			buf.append(que.remove());
			while (!que.isEmpty()) {
				buf.append(delimiter);
				buf.append(que.remove());
			}
			oCount++;
			outClosure.write(buf.toString());
		}
	}

	// -------------------------------------------------------------------------
	// 使用例＞
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		tester();
	}

	// test
	public static void tester() {
		// abc.txt　=　qw｛
		// a b c d e f g h i j k l m n o
		// 1 2 3 4 5 6 7 8 9 0
		// a b c d e f g h i j
		// 1 2 3 4 5
		// }
		String rootDir = globals.ResControl.getQprRootDir();
		String abcPath = rootDir + "abc.txt";
		String cbaPath = rootDir + "cba.txt";
		new Flt_Rotation(cbaPath, abcPath).execute();
		// ※変換したファイルを入力にして、再変換すると元の並びに戻る
		String cbaRPath = rootDir + "cbaR.txt";// 但し、ダミーセルが追加される
		new Flt_Rotation(cbaRPath, cbaPath).execute();
	}

	public static void tester2() {
		String rootDir = globals.ResControl.getQprRootDir();
		String abcPath = rootDir + "relMeta.txt";
		String cbaPath = rootDir + "relMetaR.txt";
		new Flt_Rotation(cbaPath, abcPath).execute();
		// ※変換したファイルを入力にして、再変換すると元の並びに戻る
		String cbaRPath = rootDir + "relMetaRR.txt";// 但し、ダミーセルが追加される
		new Flt_Rotation(cbaRPath, cbaPath).execute();
	}

}
