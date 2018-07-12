package kyPkg.tools;

import java.util.ArrayList;

public class TesterMaker_org {
	public static void main(String[] argv) {
		String str = "";
		str = "	public CalcWLev(String outPath, int monCount, List<ResDict> resDicts,int whichBase, int calcType, boolean debug, String outPath2) {";
str="	private String createValidTran(String trvPath, String tranPath,String movPath) {";
		System.out.println(kyPkg.tools.TesterMaker_org.createTester(str));
	}

	// ￥使用箇所をエスケープに書き換える
	public static String cnvEsc(String str) {
		str = str.replaceAll("\\\\", "\\\\\\\\");
		str = str.replaceAll("\"", "\\\\\"");
		return str;
	}

	// テスト用雛形自動生成
	// ※生成されたものをソースコードに貼り付けて使用する
	// TODO 引数にHashMapが指定された場合のロジックも必要
	public static String createTester(String str) {
		ArrayList<String> list = new ArrayList();
		ArrayList<String> bufList1 = new ArrayList();
		ArrayList<String> bufList2 = new ArrayList();
		String prefix = "";
		String suffix = "\n";
		int pos1 = str.indexOf('(');
		int pos2 = str.indexOf(')');
		if (pos1 < 0 || pos2 < 0)
			return "";
		String[] func = str.substring(0, pos1).split(" ");
		String ans = str.substring(pos1 + 1, pos2);
		if ((pos1 > 0) && (pos1 < pos2)) {
			bufList1.add("//--------------------------------------------------");
			String[] splited = ans.split(",");
			if (splited.length > 0) {
				for (int i = 0; i < splited.length; i++) {
					String[] tuple = splited[i].trim().split(" ");
					if (tuple.length > 1) {
						list.add(tuple[1].trim());
						String type = tuple[0].trim();
						String name = tuple[1].trim();
						bufList1.add("private " + type + " " + name + ";");
						bufList2.add("this." + name + " = " + name + ";");
					}
				}
			}
			bufList1.add("//--------------------------------------------------");
		}
		StringBuffer buff = new StringBuffer();
		for (String val : bufList1) {
			buff.append(prefix + val + suffix);
		}
		for (String val : bufList2) {
			buff.append(prefix + val + suffix);
		}
		return buff.toString();
	}
}
