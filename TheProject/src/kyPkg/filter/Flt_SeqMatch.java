package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;

import kyPkg.converter.DCnvLoco_Template;
import kyPkg.task.Abs_BaseTask;

// 2011-06-03 二つの入力をシーケンシャルにマッチする
public class Flt_SeqMatch extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private Inf_iClosure reader_R = null;
	private Inf_iClosure reader_L = null;
	private List<EzWriterDual> writers;
	private String[] recRs;
	private String[] recLs;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Flt_SeqMatch(EzWriterDual writer, String inPath_L, String inPath_R) {
		this.reader_L = new EzReader(inPath_L);
		this.reader_R = new EzReader(inPath_R);
		writers = new ArrayList();
		writers.add(writer);
	}

	public Flt_SeqMatch(List<EzWriterDual> writers, String inPath_L,
			String inPath_R) {
		this.reader_L = new EzReader(inPath_L);
		this.reader_R = new EzReader(inPath_R);
		this.writers = writers;
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("Flt_SeqMatch start");
		reader_L.open();
		reader_R.open();

		for (EzWriterDual writer : writers)
			writer.open();
		recRs = reader_R.readSplited();
		recLs = reader_L.readSplited();
		while (reader_L.notEOF() && reader_R.notEOF()) {
			int stat = 0;
			stat = recLs[0].compareTo(recRs[0]);
			if (stat > 0) {
				for (EzWriterDual writer : writers)
					writer.write(null, recRs, 1);// L > R
				recRs = reader_R.readSplited();
			} else if (stat < 0) {
				for (EzWriterDual writer : writers)
					writer.write(recLs, null, -1);// L < R
				recLs = reader_L.readSplited();
			} else {
				for (EzWriterDual writer : writers)
					writer.write(recLs, recRs, 0);// L == R
				recRs = reader_R.readSplited();
				recLs = reader_L.readSplited();
			}
		}
		while (reader_L.notEOF()) {
			for (EzWriterDual writer : writers)
				writer.write(recLs, null, -1);// L < R
			recLs = reader_L.readSplited();
		}
		while (reader_R.notEOF()) {
			for (EzWriterDual writer : writers)
				writer.write(null, recRs, 1);// L > R
			recRs = reader_R.readSplited();
		}
		reader_R.close();
		reader_L.close();
		for (Inf_oClosure writer : writers)
			writer.close();
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test1();
	}

	public static void test1() {
		String userDir = globals.ResControl.getQPRHome();
		String path_R = userDir+"828111000507/calc/R_sorted.txt";
		String path_L = userDir+"828111000507/calc/L_sorted.txt";
		String outPath = userDir+"828111000507/calc/RESULT.txt";
		EzWriterDual writer = new EzWriterDual(outPath,
				new DCnvLoco_Template());
		new Flt_SeqMatch(writer, path_L, path_R).execute();
	}



}
