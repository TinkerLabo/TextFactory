package kyPkg.filter;

import static kyPkg.util.KUtil.getKeyList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import kyPkg.task.Abs_ProgressTask;

public class Flt_Mixin extends Abs_ProgressTask {
	private String delimiter = "\t";
	private Inf_iClosure inClosure = null; // 入力クロージャ
	private Inf_oClosure outClosure = null; // 出力クロージャ
	private List<String> inList;
	private HashMap<String, String> catMap;
	private List<String> pCodeList;// property Code
	private List<String> pNameList;
	private List<String> gCodeList;// Group Code
	private List<String> gNameList;
	private int loopCount = 0;
	private Map<Integer, String> map;

	// -------------------------------------------------------------------------
	// 2011-10-05 yuasa
	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Flt_Mixin(String outPath, List<String> inList) {
		super();
		this.inList = inList;
		this.outClosure = new EzWriter(outPath);
	}

	public long getWriteCount() {
		return outClosure.getWriteCount();
	}

	public void setDelimiter(String delimiter) {
		this.outClosure.setDelimiter(delimiter);
	}

	@Override
	public void execute() {
		super.start("Flt_Mixin",2048);
		map = new HashMap() {
			private static final long serialVersionUID = 1L;
			{
				put(1, "A");
				put(2, "B");
				put(3, "C");
				put(4, "D");
				put(5, "E");
				put(6, "F");
				put(7, "G");
				put(8, "H");
				put(9, "I");
			}
		};

		catMap = new HashMap();
		pCodeList = new ArrayList();
		pNameList = new ArrayList();
		gCodeList = new ArrayList();
		gNameList = new ArrayList();

		for (String inPath : inList) {
			loop(inPath);// loop
		}
		outClosure.open();
		writeOut();
		outClosure.close();
		super.stop();// 正常終了
	}

	// -------------------------------------------------------------------------
	// tranLoop
	// -------------------------------------------------------------------------
	private long loop(String inPath) {
		loopCount++;
		gCodeList.add(map.get(loopCount));

		this.inClosure = new EzReader(inPath);
		int lCount = -1;
		String[] keyArray = null;
		inClosure.open();
		String[] cells = null;
		while ((cells = inClosure.readSplited()) != null) {
			lCount++;
			switch (lCount) {
			case 0:
				keyArray = cells;
				break;
			case 1:
				for (int i = 0; i < keyArray.length; i++) {
					keyArray[i] = keyArray[i].trim();
					if (!keyArray[i].equals("")) {
						if (cells.length > 0) {
							catMap.put(keyArray[i], cells[i]);
						} else {
							catMap.put(keyArray[i], "notDefined");
						}
					}
				}
				break;
			case 2:
				for (int i = 0; i < cells.length; i++) {
					// ※patternは（プログラムの開始部分で）あらかじめコンパイルしておくと良い。
					Pattern pattern = Pattern.compile("([A-Z])([\\d][\\d]*)");
					List<String> ans = kyPkg.uRegex.Regex.parseIt(pattern,
							cells[i]);
					if (ans != null && ans.size() >= 2) {
						pCodeList.add(map.get(loopCount) + ans.get(1));// key
						// System.out.println("Line3 key> " + ans.get(0) +
						// " value>" + ans.get(1));
					} else {
						// other pattern
					}
				}
				break;
			case 3:
				for (int i = 0; i < cells.length; i++) {
					cells[i] = cells[i].trim();
					// 空白以外なら処理対象とする　match("\w")の方が良いかな？
					if (!cells[i].equals("")) {
						pNameList.add(cells[i]);
					} else {
						pNameList.add("cell_" + i);
					}
				}
				break;
			case 5:
				if (cells.length > 1) {
					cells[0] = cells[0].trim();
					if (!cells[0].equals("")) {
						gNameList.add(cells[0]);
					} else {
						gNameList.add("Data_#" + loopCount);
					}
				}
				break;
			default:
				break;
			}

		}
		inClosure.close();

		return lCount;
	}

	private void writeOut() {
		// write
		List<String> keylist = getKeyList(catMap);

		writeFromListH(outClosure, keylist, delimiter);

		StringBuffer buf = new StringBuffer();
		for (String val : keylist) {
			// System.out.println("Line1&2 key> " + val + " value>"+
			// hmap.get(val));
			if (buf.length() > 0)
				buf.append(delimiter);// overHead?
			buf.append(catMap.get(val));
		}
		outClosure.write(buf.toString());
		buf.delete(0, buf.length());

		writeFromListH(outClosure, pCodeList, delimiter);
		writeFromListH(outClosure, pNameList, delimiter);
		writeFromListH(outClosure, gCodeList, delimiter);
		writeFromListH(outClosure, gNameList, delimiter);

		List<String> optList = new ArrayList();
		optList.add("C");
		writeFromListH(outClosure, optList, delimiter);

	}

	private void writeFromListH(Inf_oClosure outClosure, List<String> list,
			String delimiter) {
		StringBuffer buf = new StringBuffer();
		for (String val : list) {
			if (buf.length() > 0)
				buf.append(delimiter);
			buf.append(val);
		}
		outClosure.write(buf.toString());
		buf.delete(0, buf.length());
	}

	// -------------------------------------------------------------------------
	// 使用例＞
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		tester();
	}

	// test
	public static void tester() {
		List<String> list = new ArrayList();
		String rootDir = globals.ResControl.getQprRootDir();
		String inPath = rootDir + "relMetaR.txt";
		String outPath = rootDir + "mixin.txt";
		list.add(inPath);
		list.add(inPath);
		list.add(inPath);
		Flt_Mixin venn = new Flt_Mixin(outPath, list);
		venn.execute();
	}

	public static void tester2() {
		List<String> list = new ArrayList();
		String rootDir = globals.ResControl.getQprRootDir();
		String inPath = rootDir + "relMetaR.txt";
		String outPath = rootDir + "mixin.txt";
		list.add(inPath);
		list.add(inPath);
		list.add(inPath);
		Flt_Mixin venn = new Flt_Mixin(outPath, list);
		venn.execute();
	}

}
