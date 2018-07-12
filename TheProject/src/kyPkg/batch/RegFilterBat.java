package kyPkg.batch;

import static kyPkg.util.KUtil.arrayModz;

import globals.ResControl;
import kyPkg.converter.DefaultConverter;
import kyPkg.filter.EzReader;
import kyPkg.filter.EzWriter;
import kyPkg.filter.RegChecker;
import kyPkg.filter.Flt_Regex;

public class RegFilterBat {
	public static void main(String[] argv) {
		System.out.println("<<RegFilter>> qtb_Convert start");
		String inPath = ResControl.ENQ_DIR + "NQ/属性・性年代編/2009/Qtb1.TXT";
		String outPath = ResControl.D_DRV + "regex.txt";
		new RegFilterBat().qtb_Convert(outPath, inPath);
		System.out.println("<<RegFilter>> qtb_Convert end");
	}

	public void qtb_Convert(String outPath, String inPath) {
		RegChecker filterx = new RegChecker(1, 0, 0, "ROOT", 1, false);
		EzReader ezReader = new EzReader(inPath, filterx, 0);
		EzWriter ezWriter = new EzWriter(outPath, new LocoConverter());
		Flt_Regex loopCtrl = new Flt_Regex(ezWriter, ezReader);
		loopCtrl.execute();
	}

	// -------------------------------------------------------------------------
	// ROOTならフィールドをくっつける
	// ROOTではないならそのまま出力
	// -------------------------------------------------------------------------
	public class LocoConverter extends DefaultConverter {
		{
			delimiter=",";
		}
		@Override
		public String[] convert(String[] splited, int stat) {
			String[] modified = null;
			switch ((int) stat) {
			case 1:// ROOT以外の場合、最後のセルに項目フィールドを追加する・・・・
				if (splited[7].equals("SINGLE")) {
					modified = arrayModz(splited, 0, 8,
							new String[] { "1", splited[9], "X" + splited[1] });
				} else {
					String[] wkArray = splited[2].split("_");
					modified = arrayModz(splited, 0, 8,
							new String[] { wkArray[1], "1", "X" + splited[1] });
				}
				return modified;
			case 0:// ROOTの処理
				if (splited[7].equals("SINGLE")) {
					modified = arrayModz(splited, 0, 8,
							new String[] {});
				} else {
					modified = arrayModz(splited, 0, 8,
							new String[] {});
				}
				return modified;
			default:
				return null;// "UnExpected Data";
			}
		}

//		@Override
//		public String convert2Str(String[] splited, int stat) {
//			splited = convert(splited, stat);
//			return join(splited, ",");
//		};

		@Override
		public void init() {
		}

		@Override
		public void fin() {
		};
	}

	// public String[] arrayModz(String[] array, int from, int len,String[]
	// option) {
	// ArrayList<String> list = new ArrayList();
	// for (int i = from; i < array.length; i++) {
	// list.add(array[i]);
	// }
	// if (list.size() < len) {
	// while (list.size() < len) {
	// list.add("");
	// }
	// }
	// if (option != null) {
	// for (int i = 0; i < option.length; i++) {
	// list.add(option[i]);
	// }
	// }
	// return list.toArray(new String[list.size()]);
	// }
}
