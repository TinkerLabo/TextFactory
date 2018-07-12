package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import kyPkg.task.Abs_BaseTask;

// 2009-06-16 yuasa
public class Flt_Template0 extends Abs_BaseTask {
	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private Inf_iClosure reader = null;

	private Inf_oClosure writer = null;

	private String delimiter = null;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Flt_Template0(String outPath, String inPath) {
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
		if (delimiter == null)
			delimiter = reader.getDelimiter();
		String[] splited = reader.readSplited();
		while (splited != null) {
			System.out.println("splited" + splited[0]);
			writer.write(join(splited, delimiter));
			wCnt++;
			splited = reader.readSplited();
		}
		reader.close();
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
		String inPath = "C:/qpr_monitor_info_ALL.txt";
		String outPath = "C:/qpr_monitor_info_ALL.OUT";
		new Flt_Template0(outPath, inPath).execute();
	}

	public static void testIoBase() {
		String oPath = "";
		String iPath = "";
		ioBase(oPath, iPath);
	}

	// -------------------------------------------------------------------------
	// I/O Base
	// -------------------------------------------------------------------------
	private static boolean ioBase(String oPath, String iPath) {
		EzReader reader = new EzReader(iPath);
		EzWriter writer = new EzWriter(oPath);
		reader.open();
		writer.open();
		StringBuffer buf = new StringBuffer();
		String[] array;
		while (reader.readLine() != null) {
			int wStat = reader.getStat();
			if (wStat >= 0) {
				array = reader.getSplited();
				buf.delete(0, buf.length());
				buf.append(array[0]);
				writer.write(buf.toString());
			}
		}
		reader.close();
		writer.close();
		return true;// XXX ERROR時はfalseが返るようにする
	}

	public static boolean mapIt(String oPath, String iPath) {
		System.out.println("## start cnv ##");

		HashMap<String, String[]> singleMap = new HashMap();
		EzReader reader = new EzReader(iPath);
		reader.open();
		StringBuffer buf = new StringBuffer();
		String[] array;
		while (reader.readLine() != null) {
			int wStat = reader.getStat();
			if (wStat >= 0) {
				array = reader.getSplited();
				String id = array[0];
				String[] obj = singleMap.get(id);
				if (obj == null) {
					obj = new String[] {};
				}
				obj = array;
				System.out.println("reg id:" + id);
				singleMap.put(id, obj);
				buf.delete(0, buf.length());
				buf.append(array[0]);
			}
		}
		reader.close();
		List<String> keylist = new ArrayList(singleMap.keySet());
		Collections.sort(keylist);
		for (String key : keylist) {
			System.out.println("mapIt key:" + key);
		}

		System.out.println("## end cnv ##");

		// EzWriter writer = new EzWriter(oPath);
		// writer.open();
		// writer.write(buf.toString());
		// writer.close();
		return true;// XXX ERROR時はfalseが返るようにする
	}

}
