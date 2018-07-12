package kyPkg.Sorts;

import java.util.ArrayList;
import java.util.List;

import kyPkg.filter.EzReader;
import kyPkg.filter.Inf_iClosure;
import kyPkg.filter.Inf_oClosure;
import kyPkg.task.Abs_BaseTask;

// ----------------------------------------------------------------------------
//　
// ----------------------------------------------------------------------------
public class MixIt_org extends Abs_BaseTask {
	private String delimiter = "\t";
	private Inf_oClosure writer = null;
	private List<Inf_iClosure> readers;
	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public MixIt_org(Inf_oClosure writer, List<String> iPaths,boolean debug) {
		//#createTester--------------------------------------------------
		System.out.println("public static void testMixIt() {");
		System.out.println("    Inf_oClosure writer = " + writer + ";");
		System.out.println("    List<String> iPaths = new ArrayList();");
		for (String element : iPaths) {
			System.out.println("    iPaths.add(\"" + element + "\");");
		}
		System.out.println("    MixIt ins = new MixIt(writer,iPaths);");
		System.out.println("}");
		//--------------------------------------------------

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

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
	}

}
