package kyPkg.filter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import globals.ResControl;
import kyPkg.Sorts.IncoreSort;
import kyPkg.converter.DefaultConverter;
import kyPkg.converter.Inf_LineConverter;
import kyPkg.task.Abs_BaseTask;

public class Flt_Regex extends Abs_BaseTask {
	private Inf_iClosure reader = null; // 入力クロージャ

	private Inf_oClosure writer = null; // 出力クロージャ

	private HashMap<String, Inf_oClosure> writerMap = null;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	// public Flt_Regex(String outPath, String inPath) {
	// this(outPath, inPath, null);
	// }
	// public Flt_Regex(String outPath, String inPath, RegFilter filter) {
	// reader = new EasyReader(inPath, filter);
	// writer = new EasyWriter(outPath);
	// }
	public Flt_Regex(String outPath, String inPath, RegChecker filter,
			Inf_LineConverter converter) {
		reader = new EzReader(inPath, filter, 0);
		writer = new EzWriter(outPath, converter);
	}

	public Flt_Regex(Inf_oClosure writer, Inf_iClosure reader) {
		this.writer = writer;
		this.reader = reader;
	}

	public Flt_Regex(Inf_iClosure reader) {
		this.reader = reader;
	}

	// -------------------------------------------------------------------------
	// 出力クロージャをマップにセット
	// -------------------------------------------------------------------------
	public void setWriter(Inf_oClosure outClosure) {
		this.writer = outClosure;
	}

	public void setWriter(String key, Inf_oClosure outClosure) {
		if (writerMap == null)
			writerMap = new HashMap();
		this.writerMap.put(key, outClosure);
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("ＸＸ開始");

		writerOpen();
		long wCnt = 0;
		reader.open();
		while (reader.readLine() != null) {
			int wStat = reader.getStat();
			// System.out.println(">>"+reader.getCurrent());
			// System.out.println("wStat:"+wStat);
			// Statusに対応する出力クロージャを呼び出す
			if (writerMap != null)
				writer = writerMap.get(String.valueOf(wStat));
			if (writer != null) {
				writer.write(reader.getSplited(), wStat);
			}
			wCnt++;
		}
		reader.close();
		writerClose();
	}

	// -------------------------------------------------------------------------
	// 出力クロージャOPEN
	// -------------------------------------------------------------------------
	private void writerOpen() {
		if (writerMap != null) {
			Set set = writerMap.entrySet();
			Iterator it = set.iterator();
			while (it.hasNext()) {
				java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
				Inf_oClosure closure = (Inf_oClosure) ent.getValue();
				if (closure != null)
					closure.open();
			}
		} else {
			if (writer != null) {
				writer.open();
			}
		}
	}

	// -------------------------------------------------------------------------
	// 出力クロージャCLOSE
	// -------------------------------------------------------------------------
	private void writerClose() {
		if (writerMap != null) {
			Set set = writerMap.entrySet();
			Iterator it = set.iterator();
			while (it.hasNext()) {
				java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
				Inf_oClosure closure = (Inf_oClosure) ent.getValue();
				if (closure != null)
					closure.close();
			}
		} else {
			if (writer != null) {
				writer.close();
			}
		}
	}

	// -------------------------------------------------------------------------
	// 差分に数字が入ってしまっているものをピックアップして、これを元にダミーデータを作成する
	// -------------------------------------------------------------------------
	public static void dummyPkUp() {
		// フィルタ
		DefaultConverter converter = new DefaultConverter() {
			@Override
			public String[] convert(String[] splited, int lineNumber) {
				splited[(17 - 1)] = ""; // 住居形態その他にお化け文字が入っているのでクリア
				splited[(35 - 1)] = "0"; // 開始日時補正日数
				return splited;
			}
			//			@Override
			//			public String convert2Str(String[] splited, int stat) {
			//				splited = convert(splited, stat);
			//				return join(splited, "\t");
			//			};
		};
		String iPath = ResControl.D_QPR + "qpr_monitor_out.txt";
		String oPath = ResControl.D_QPR + "qpr_monitor_info_20090231.txt";
		RegChecker filter = new RegChecker();
		filter.addFilter((35 - 1), 0, 1, "0", 1, false); // 開始日付補正日数が０以外のもの
		Flt_Regex reggae = new Flt_Regex(oPath, iPath, filter, converter);
		reggae.execute();
	}

	// #########################################################################
	// ##  m a i n
	// #########################################################################
	public static void main(String[] args) {
		// dummyPkUp();
		test0610();
	}

	// -------------------------------------------------------------------------
	// cond_m.add("1, 64,1,True,1,1");
	// -------------------------------------------------------------------------
	public static void test00() {
		String m_path = ResControl.D_DAT + "NQFACE.DAT";
		String st_path = ResControl.D_DAT + "STFACE.DAT";
		RegChecker filter = new RegChecker();
		filter.addFilter(1, 55, 1, "0", 4);
		filter.addFilter(1, 55, 1, "0", 4);
		// filter.addFilter(1, 0,0,true,2,"^99991231.*");
		Flt_Regex reggae = new Flt_Regex(new EzReader(m_path, filter, 0));
		reggae.setWriter("2", new EzWriter(ResControl.D_DRV + "zyppie1.txt"));
		reggae.setWriter("4", new EzWriter(st_path));
		reggae.execute();
	}

	// -------------------------------------------------------------------------
	// test01
	// -------------------------------------------------------------------------
	public static void test02() {
		RegChecker filter = new RegChecker();
		filter.addFilter(0, 0, 1, "^7.*", 16);
		filter.addFilter(1, 54, 1, "3", 1);

		Flt_Regex reggae = new Flt_Regex(
				new EzReader(ResControl.D_DRV + "NQFACE.DAT", filter, 0));
		reggae.setWriter("16", new EzWriter(ResControl.D_DRV + "stat_16.txt"));
		reggae.setWriter("1", new EzWriter(ResControl.D_DRV + "stat_1.txt"));
		reggae.execute();
	}

	// -------------------------------------------------------------------------
	// test0610
	// -------------------------------------------------------------------------
	public static void test0610() {
		// 　対象地区のモニターのみを出力する（セル、カラム、レングス、ステータス）
		RegChecker filter = new RegChecker();
		String pattern = "35      ";
		filter.addFilter(1, 59, 1, "[" + pattern.trim() + "]", 1);
		String inPath1 = ResControl.D_DAT + "STFACE.DAT";
		String outPath1 = ResControl.D_DAT + "test0610.txt";
		new IncoreSort("0,String,asc", new EzWriter(outPath1),
				new EzReader(inPath1, filter, 1)).execute();

	}

}
