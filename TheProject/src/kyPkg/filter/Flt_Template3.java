package kyPkg.filter;

import globals.ResControl;
import kyPkg.converter.DefaultConverter;
import kyPkg.task.Abs_BaseTask;

// 2009-10-15 yuasa　マージ？！
public class Flt_Template3 extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private Inf_iClosure reader = null;

	private Inf_oClosure writer = null;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Flt_Template3(String outPath, String inPath) {
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
		while (writer.write(reader))
			;
		reader.close();
		writer.close();
		elapse.stop();

	}

	public class LocoConverter extends DefaultConverter {
		@Override
		public String[] convert(String[] splited, int stat) {
			if (splited[0].equals("")) {
				splited[0] = "space";
			}
			return splited;
		};
//		@Override
//		public String convert2Str(String[] splited, int stat) {
//			splited = convert(splited, stat);
//			return join(splited, delimiter);
//		};
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
	}

	public static void test1() {
		String inPath = ResControl.D_DAT + "NQFACE.DAT";
		String outPath = ResControl.D_DAT + "sorted.txt";
		new Flt_Template3(outPath, inPath).execute();
	}

}
