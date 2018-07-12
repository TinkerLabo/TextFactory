package kyPkg.filter;

import kyPkg.task.Abs_ProgressTask;

public class Flt_Base extends Abs_ProgressTask {
	protected Inf_iClosure inClosure = null; // 入力クロージャ
	protected Inf_oClosure outClosure = null; // 出力クロージャ

	// -------------------------------------------------------------------------
	// 2011-10-05 yuasa
	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Flt_Base(String outPath, String inPath) {
		super();
		this.inClosure = new EzReader(inPath);
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
		super.start("Flt_Base",2048);
		outClosure.open();
		loop();// loop
		outClosure.close();
		super.stop();// 正常終了
	}

	// -------------------------------------------------------------------------
	// tranLoop
	// -------------------------------------------------------------------------
	private long loop() {
		long lCount = -1;
		inClosure.open();
		String[] cells = null;
		while ((cells = inClosure.readSplited()) != null) {
			lCount++;
			outClosure.write("outrec" + cells[0]);
		}
		inClosure.close();
		return lCount;
	}

	// -------------------------------------------------------------------------
	// 使用例＞
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		// tester();
	}

	// test
	// public static void tester() {
	// // アンケートデータを期間内有効モニターに限定する
	// String userDir = kyPkg.uFile.ResControl.getQPRHome();
	// String inPath = ResControl.getCurrentPath();
	// String outPath = userDir+"828111000507/calc/current.txt";
	// Flt_Base venn = new Flt_Base(outPath, inPath);
	// venn.execute();
	// }

}
