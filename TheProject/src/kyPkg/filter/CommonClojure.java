package kyPkg.filter;

import java.io.BufferedReader;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import globals.ResControlWeb;
import kyPkg.converter.Inf_StrConverter;
import kyPkg.task.Abs_BaseTask;
import kyPkg.uFile.FileUtil;

public class CommonClojure extends Abs_BaseTask {
	private Inf_BaseClojure clojure;
	private String inputPath;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public CommonClojure() {
	}

	public CommonClojure(Inf_BaseClojure closure, String inputPath) {
		this.clojure = closure;
		this.inputPath = inputPath;
	}

	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public void setClosure(Inf_BaseClojure closure) {
		this.clojure = closure;
	}

	// -------------------------------------------------------------------------
	// スレッドで順序制御したい場合
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("CommonIO start");
		incore(clojure, inputPath, true);
		super.stop();
		System.out.println("#CommonIO end");
	}

	// -------------------------------------------------------------------------
	// Incore 直接うごかしてもOK
	// -------------------------------------------------------------------------
	public void incore(Inf_BaseClojure closure, Inf_iClosure reader) {
		closure.init();
		reader.open();
		String[] splited = reader.readSplited();
		while (splited != null) {
			// if (splited[0].equals("71458211"))
			// System.out.println("@incore>>>" + splited[0]);
			if (splited.length > 0)
				closure.execute(splited);
			splited = reader.readSplited();
		}
		reader.close();
		closure.write();
	}

	public void incore(Inf_BaseClojure closure, String inPath) {
		Inf_iClosure reader = new EzReader(inPath);
		closure.init();
		reader.open();
		String[] splited = reader.readSplited();
		while (splited != null) {
			if (splited.length > 0)
				closure.execute(splited);
			splited = reader.readSplited();
		}
		reader.close();
		closure.write();
	}

	// データがスプリットされないバージョン・・・使用されているので残した
	public void incore(Inf_BaseClojure closure, String path,
			boolean notSplited) {
		closure.init();
		File fl = new File(path);
		if (fl.exists() == false) {
			System.out.println("#ERROR CommonIO@incore FileNotFound:" + path);
			return;
		}
		String rec;
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				rec = br.readLine();
//				System.out.println("20170413>>"+rec);
				if (rec != null && (!rec.equals(""))) {
					// System.out.println(wRec);
					closure.execute(rec);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		closure.write();
	}

	// 使われていないようなのでコメントアウトした2010/02/09
	// public void incore(Inf_BaseClosure closure, Inf_iClosure
	// reader1,Inf_iClosure reader2) {
	// closure.init();
	// reader1.open();
	// String[] splited = reader1.readSplited();
	// while (splited != null) {
	// if (splited.length > 0)
	// closure.execute(splited);
	// splited = reader1.readSplited();
	// }
	// reader1.close();
	// closure.fin();
	// }

	public void incore(Inf_BaseClojure closure, String inputPath, int index,
			Inf_StrConverter converter) {
		EzReader reader = new EzReader(inputPath);
		reader.addConverter(index, converter);
		closure.init();
		reader.open();
		String[] splited = reader.readSplited();
		while (splited != null) {
			if (splited.length > 0)
				closure.execute(splited);
			splited = reader.readSplited();
		}
		reader.close();
		closure.write();
	}

	public void incore(Inf_BaseClojure closure, String[] array) {
		closure.init();
		String wRec;
		for (int i = 0; i < array.length; i++) {
			wRec = array[i];
			if (wRec != null && (!wRec.equals(""))) {
				closure.execute(wRec);
			}
		}
		closure.write();
	}

	public void incore(Inf_BaseClojure closure, List<String> list) {
		closure.init();
		String wRec;
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			wRec = (String) iter.next();
			if (wRec != null && (!wRec.equals(""))) {
				closure.execute(wRec);
			}
		}
		closure.write();
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] argv) {
		test02();
	}

	// -------------------------------------------------------------------------
	// クロージャの雛形あり→TemplateOfClojure()
	// -------------------------------------------------------------------------
	public static void test_TemplateOfClojure() {
		String srcFile = "templates/temp01.txt";
		TemplateOfClojure closure = new TemplateOfClojure();
		new CommonClojure().incore(closure, srcFile, true);
	}

	// -------------------------------------------------------------------------
	public static void test01() {
		String srcFile = "templates/temp01.txt";
		PoiClosure closure = new PoiClosure();
		closure.setTemplate(ResControlWeb.getD_Resources_Templates("QPRBASE01.xls"));
		closure.setOutPath(ResControlWeb.getD_Resources_PUBLIC("workbook.xls"));
		closure.setSheet("data");
		closure.setBaseYX(17, 2);
		new CommonClojure().incore(closure, srcFile, true);
	}

	public static void test02() {
		String[] array = { "A1	B2	C3	D4	E5	F6", "A2	B3	C4	D5	E6	F7", };
		String[] header = { "Head1", "Head2", "Head3", "Head4", "Head5",
				"Head6" };
		PoiClosure closure = new PoiClosure();
		closure.setTemplate(ResControlWeb.getD_Resources_Templates("QPRSSHOT.xls"));
		closure.setOutPath(ResControlWeb.getD_Resources_PUBLIC("snapShot.xls"));
		closure.setSheet("data");
		closure.setBaseYX(7, 2);
		closure.setHeader(header);
		new CommonClojure().incore(closure, array);
	}

}
