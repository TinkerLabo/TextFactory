package kyPkg.Sorts;

import java.util.ArrayList;
import java.util.List;

import kyPkg.converter.Inf_LineConverter;
import kyPkg.converter.ShareCalq;
import kyPkg.filter.EzReader;
import kyPkg.filter.EzWriter;
import kyPkg.filter.Inf_iClosure;
import kyPkg.filter.Inf_oClosure;
import kyPkg.task.Abs_BaseTask;

// ----------------------------------------------------------------------------
//　
// ----------------------------------------------------------------------------
public class MixIt extends Abs_BaseTask {
	private String delimiter = "\t";
	private Inf_oClosure writer = null;
	private List<Inf_iClosure> readers;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public MixIt(String bodyPath, List<String> iPaths,
			List<Integer> shareCellL) {
		//#createTester--------------------------------------------------
		//		System.out.println("public static void testMixIt() {");
		//		System.out.println("    String bodyPath = \"" + bodyPath + "\";");
		//		System.out.println("    List<String> iPaths = new ArrayList();");
		//		for (String element : iPaths) {
		//		System.out.println("    iPaths.add(\""+element+"\");");
		//		}
		//		System.out.println("    List<Integer> shareCellL = new ArrayList();");
		//		for (Integer element : shareCellL) {
		//		System.out.println("    shareCellL.add("+element+");");
		//		}
		//		System.out.println("    MixIt ins = new MixIt(bodyPath,iPaths,shareCellL);");
		//		System.out.println("}");
		//--------------------------------------------------

		Inf_LineConverter cnvRank = new ShareCalq(shareCellL);
		Inf_oClosure writer = new EzWriter(bodyPath, cnvRank);
		readers = new ArrayList();
		for (String path : iPaths) {
			readers.add(new EzReader(path));
		}
		this.writer = writer;
	}

	public long getWriteCount() {
		return this.writer.getWriteCount();
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("MIX start");
		String[] array = null;
		writer.setDelimiter(delimiter);
		writer.open();
		for (Inf_iClosure reader : readers) {
			reader.open();
			while ((array = reader.readSplited()) != null) {
				if (array.length > 0)
					writer.write(array, 1);
			}
			reader.close();
		}
		writer.close();
	}

	public static void testMixIt() {
		String bodyPath = "C:/@qpr/home/828111000630/calc/#004_MX1_金額_000_2016年07月25日～31日_期間001.txt";
		List<String> iPaths = new ArrayList();
		iPaths.add(
				"C:/@qpr/home/828111000630/calc/#003_MR2_金額_000_2016年07月25日～31日_期間001_lev0.txt");
		iPaths.add(
				"C:/@qpr/home/828111000630/calc/#003_MR2_金額_000_2016年07月25日～31日_期間001_lev1.txt");
		List<Integer> shareCellL = new ArrayList();
		MixIt ins = new MixIt(bodyPath, iPaths, shareCellL);
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
	}

}
