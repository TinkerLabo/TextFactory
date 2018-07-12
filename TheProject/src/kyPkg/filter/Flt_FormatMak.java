package kyPkg.filter;

import static kyPkg.util.Joint.join;

import java.util.ArrayList;
import java.util.List;

import globals.ResControl;
import kyPkg.converter.DefaultConverter;
import kyPkg.task.Abs_BaseTask;

// 2009-07-16 yuasa
public class Flt_FormatMak extends Abs_BaseTask {
	private static final String SL = "／";// Slash
	private static final String US = "_";// underscore

	// ------------------------------------------------------------------------
	// 入力クロージャ
	// ------------------------------------------------------------------------
	private Inf_iClosure reader = null;

	private Inf_oClosure writer = null;

	private List<String> nameList;

	private List<String> codeList;

	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public Flt_FormatMak(String outPath, String inPath) {
		reader = new EzReader(inPath);
		writer = new EzWriter(outPath, new LocoConverter());
	}

	private String getConName(String delimiter) {
		if (nameList != null) {
			return join(nameList, delimiter);
		} else {
			return "";
		}
	}

	private String getConCode(String delimiter) {
		if (codeList != null) {
			return join(codeList, delimiter);
		} else {
			return "";
		}
	}

	public String getFName(String cCode, String cName) {
		String conName = getConName("＆");
		String conCode = getConCode("+");
		return cCode + conCode + US + cName + SL + conName;
	}

	// -------------------------------------------------------------------------
	// 実行
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.setMessage("ＸＸ開始");

		nameList = new ArrayList();
		codeList = new ArrayList();
		reader.open();
		writer.open();
		while (writer.write(reader))
			;
		reader.close();
		writer.close();
	}

	public class LocoConverter extends DefaultConverter {
		// public String convert2Str(String str) {
		// return convert2Str(new String[] { str }, 0);
		// };
		@Override
		public String[] convert(String[] splited, int stat) {
			List<String> list = new ArrayList();
			if (splited == null)
				return null;
			if (!splited[0].equals("")) {
				nameList.add(splited[1].replaceAll("　", "").trim());
				codeList.add(splited[0]);
				list.add(splited[0].substring(0, 5)); // Code
				list.add(splited[1]); // Name
			}
			String[] array = (String[]) list.toArray(new String[list.size()]);
			return array;
		}

		// @Override
		// public String convert2Str(String[] splited, int stat) {
		// splited = convert(splited, stat);
		// return join(splited, delimiter);
		// };

		// public String convert2Str(List list, int stat) {
		// String[] splited = (String[]) list.toArray(new String[list.size()]);
		// return convert2Str(splited, stat);
		// }

		@Override
		public void init() {
		}

		@Override
		public void fin() {
		};
	}

	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test1();
	}

	public static void test1() {

		String targetPath3 = ResControl.D_DRV + "resources/temp/EJQP7/CAT.txt";
		String targetPath4 = ResControl.D_DRV + "resources/temp/EJQP7/K1.txt";
		Flt_FormatMak catFormater = new Flt_FormatMak(targetPath4, targetPath3);// 002キログラム
		catFormater.execute();
		String folderName = catFormater.getFName("340018", "ＱＰＲ標準指定品目");
		System.out.println(folderName);
	}

}
