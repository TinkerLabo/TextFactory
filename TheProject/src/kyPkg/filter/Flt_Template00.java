package kyPkg.filter;

import globals.ResControl;
import kyPkg.task.Abs_BaseTask;

// 2016-11-01 yuasa
public class Flt_Template00 extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private Inf_iClosure reader = null;
	private Inf_oClosure writer = null;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Flt_Template00(String outPath, String inPath) {
		reader = new EzReader(inPath);
		writer = new EzWriter(outPath);
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
		StringBuffer buf = new StringBuffer();
		String rec = "";
		while ((rec = reader.readLine()) != null) {
			buf.delete(0, buf.length());
			buf.append(rec);
			writer.write(buf.toString());
		}
		reader.close();
		writer.close();
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test();
	}

	public static void test() {
		String itpCode = "238881000300";
		String inPath = ResControl.getItpPath_Local(itpCode);
		String outPath = "C:/result";
		new Flt_Template00(outPath, inPath).execute();
	}

}
