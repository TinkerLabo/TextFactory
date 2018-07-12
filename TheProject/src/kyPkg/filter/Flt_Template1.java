package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.util.ArrayList;

import globals.ResControl;
import kyPkg.task.Abs_BaseTask;

// 2009-06-16 yuasa
public class Flt_Template1 extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private Inf_iClosure reader = null;

	private Inf_oClosure writer = null;

	private String delimiter = null;

	private ArrayList<String[]> list;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Flt_Template1(String outPath, String inPath) {
		reader = new EzReader(inPath);
		writer = new EzWriter(outPath);
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("## Start ##");
		kyPkg.tools.Elapse elapse = new kyPkg.tools.Elapse("# template #");
		elapse.start();

		list = new ArrayList();
		long wCnt = 0;
		reader.open();
		if (delimiter == null)
			delimiter = reader.getDelimiter();
		String[] splited = null;
		// ---------------------------------------------------------------------
		// incore
		// ---------------------------------------------------------------------
		while (reader.readLine() != null) {
			int wStat = reader.getStat();
			if (wStat >= 0) {
				splited = reader.getSplited();
				list.add(splited);
				wCnt++;
			}
		}
		reader.close();
		// ---------------------------------------------------------------------
		// output
		// ---------------------------------------------------------------------
		writer.open();
		if (wCnt > 0) {
			for (String[] data : list) {
				writer.write(join(data, delimiter));
			}
		}
		writer.close();
		elapse.stop();

	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test1();
	}

	public static void test1() {
		String inPath = ResControl.D_DAT + "NQFACE.DAT";
		String outPath = ResControl.D_DAT + "sorted.txt";
		new Flt_Template1(outPath, inPath).execute();
	}

}
