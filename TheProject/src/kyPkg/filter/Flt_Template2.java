package kyPkg.filter;

import globals.ResControl;
import kyPkg.converter.DefaultConverter;
import kyPkg.task.Abs_BaseTask;

// 2009-07-14 yuasa
public class Flt_Template2 extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private Inf_iClosure reader = null;

	private Inf_oClosure writer = null;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Flt_Template2(String outPath, String inPath) {

		reader = new EzReader(inPath);
		writer = new EzWriter(outPath, new LocoConverter());
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("## Start ##");
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("# template #");
		elapse.start();

		reader.open();
		writer.open();
		// if (delimiter == null)
		// delimiter = reader.getDelimiter();
		while (writer.write(reader))
			;
		reader.close();
		writer.close();
		elapse.stop();

	}

	public class LocoConverter extends DefaultConverter {
		@Override
		public String[] convert(String[] splited, int stat) {
			if (splited == null)
				return null;
			System.out.println(">" + splited[0]);
			if (splited[0] != null && splited[0].equals("")) {
				splited[0] = "★";
			}
			return splited;
		};
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
	}

	public static void test1() {
		String inPath = ResControl.D_DAT + "NQFACE.DAT";
		String outPath = ResControl.D_DAT + "sorted.txt";
		new Flt_Template2(outPath, inPath).execute();
	}

	public static void sameLogic() {

		String inPath = "";
		String outPath = "";
		Inf_iClosure reader = new EzReader(inPath);
		Inf_oClosure writer = new EzWriter(outPath);
		// Inf_oClosure writer = new EzWriter(outPath, new Flt_Template2.new
		// LocoConverter());
		reader.open();
		writer.open();
		while (writer.write(reader))
			;
		reader.close();
		writer.close();
	}

}
