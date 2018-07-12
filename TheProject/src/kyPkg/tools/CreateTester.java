package kyPkg.tools;

import static kyPkg.util.Joint.join;
import static kyPkg.util.KUtil.list2strArray;
import java.util.ArrayList;

public class CreateTester extends Tool {
	//	-----------------------------------------------------------------------
	// テスト用雛形自動生成
	// ※生成されたものをソースコードに貼り付けて使用する
	// TODO 引数にHashMapが指定された場合のロジックも必要
	//	-----------------------------------------------------------------------
	public static String execute(String str) {
		ArrayList<String> list = new ArrayList();
		ArrayList<String> bufList = new ArrayList();
		int pos1 = str.indexOf('(');
		int pos2 = str.indexOf(')');
		if (pos1 < 0 || pos2 < 0)
			return "";
		String[] func = str.substring(0, pos1).split(" ");
		String className = func[func.length - 1];
		String ans = str.substring(pos1 + 1, pos2);
		ans = ans.replaceAll("<String, String>", "<String:String>");
		//このような形式のものも対応したいがどうすればよいだろう
		//		"<String, String>"
		//		"<String, Integer>"

		if ((pos1 > 0) && (pos1 < pos2)) {
			bufList.add(
					"\t//#createTester--------------------------------------------------");
			bufList.add("public static void test" + className + "() {");
			String[] splited = ans.split(",");
			if (splited.length > 0) {
				for (int i = 0; i < splited.length; i++) {
					String[] tuple = splited[i].trim().split(" ");
					if (tuple.length > 1) {
						list.add(tuple[1].trim());
						System.out.println("tuple[0]:" + tuple[0].trim()
								+ "    tuple[1]:" + tuple[1].trim());
						if (tuple[0].trim().equals("String")) {
							//-------------------------------------------------
							//	String
							//-------------------------------------------------
							bufList.add("    " + tuple[0].trim() + " "
									+ tuple[1].trim() + " = \\\"\" + "
									+ tuple[1].trim() + " + \"\\\";");

						} else if (tuple[0].trim().equals("List<String[]>")) {
							System.out.println("■■　ＨＥＬＬＯ　■■");
							//-------------------------------------------------
							//	List<String[]>
							//-------------------------------------------------
							//								System.out.println("    List<String[]> limitList = new ArrayList();");
							//								for (String[] array : limitList) {
							//									System.out.println("    limitList.add(" + kyPkg.util.KUtil.join(array) + ");");
							//								}

							String clsName = tuple[0].trim();
							String varName = tuple[1].trim();
							bufList.add("    List<String[]> " + varName+ " = new ArrayList();");
							bufList.add("\tfor (String[] array : " + varName+ ") {");
							bufList.add("        " + varName+ ".add(\" + kyPkg.util.KUtil.join(array) + \");");
							bufList.add("\t}");

						} else if (tuple[0].trim().equals("List<String>")) {
							//-------------------------------------------------
							//	List<String>
							//-------------------------------------------------
							bufList.add("    " + tuple[0].trim() + " "
									+ tuple[1].trim() + " = new ArrayList();");
							bufList.add("\tfor (String element : "
									+ tuple[1].trim() + ") {");
							bufList.add("    " + tuple[1].trim()
									+ ".add(\\\"\"+element+\"\\\");");
							bufList.add("\t}");
						} else if (tuple[0].trim().equals("List<Integer>")) {
							//-------------------------------------------------
							//	List<Integer>
							//-------------------------------------------------
							bufList.add("    " + tuple[0].trim() + " "
									+ tuple[1].trim() + " = new ArrayList();");
							bufList.add("\tfor (Integer element : "
									+ tuple[1].trim() + ") {");
							bufList.add("    " + tuple[1].trim()
									+ ".add(\"+element+\");");
							bufList.add("\t}");
						} else if (tuple[0].trim().equals("Set<String>")) {
							//-------------------------------------------------
							//	HashSet<String>
							//-------------------------------------------------
							bufList.add("    " + tuple[0].trim() + " "
									+ tuple[1].trim() + " = new HashSet();");
							bufList.add("\tfor (String element : "
									+ tuple[1].trim() + ") {");
							bufList.add("    " + tuple[1].trim()
									+ ".add(\\\"\"+element+\"\\\");");
							bufList.add("\t}");
						} else if (tuple[0].trim().equals("Set<Integer>")) {
							//-------------------------------------------------
							//	HashSet<String>
							//-------------------------------------------------
							bufList.add("    " + tuple[0].trim() + " "
									+ tuple[1].trim() + " = new HashSet();");
							bufList.add("\tfor (Integer element : "
									+ tuple[1].trim() + ") {");
							bufList.add("    " + tuple[1].trim()
									+ ".add(\"+element+\");");
							bufList.add("\t}");
						} else if (tuple[0].trim().equals("HashSet<String>")) {
							//-------------------------------------------------
							//	HashSet<String>
							//-------------------------------------------------
							bufList.add("    " + tuple[0].trim() + " "
									+ tuple[1].trim() + " = new HashSet();");
							bufList.add("\tfor (String element : "
									+ tuple[1].trim() + ") {");
							bufList.add("    " + tuple[1].trim()
									+ ".add(\\\"\"+element+\"\\\");");
							bufList.add("\t}");

						} else
							if (tuple[0].trim().equals("Map<String:String>")) {
							//-------------------------------------------------
							//	Map<String, String>
							//-------------------------------------------------
							String clsName = tuple[0].trim();
							String varName = tuple[1].trim();
							bufList.add("    HashMap<String, String> " + varName
									+ " = new HashMap();");
							bufList.add(
									"\tList<String> keyList = new ArrayList("
											+ varName + ".keySet());");
							bufList.add("\tfor (String key : keyList) {");
							bufList.add(
									"\tString val = " + varName + ".get(key);");
							bufList.add("    " + varName + ".put(\\\"\" + key + \"\\\", \\\"\" + val + \"\\\");");
							bufList.add("\t}");

						} else if (tuple[0].trim().equals("HashSet<Integer>")) {
							//-------------------------------------------------
							//	HashSet<String>
							//-------------------------------------------------
							bufList.add("    " + tuple[0].trim() + " "
									+ tuple[1].trim() + " = new HashSet();");
							bufList.add("\tfor (Integer element : "
									+ tuple[1].trim() + ") {");
							bufList.add("    " + tuple[1].trim()
									+ ".add(\"+element+\");");
							bufList.add("\t}");
						} else {
							if (tuple[0].indexOf('[') > 0
									&& tuple[0].indexOf(']') > 0) {
								bufList.add("    " + tuple[0].trim() + " "
										+ tuple[1].trim()
										+ " = {\" + kyPkg.util.KUtil.join("
										+ tuple[1].trim() + ") + \"};");
							} else {
								bufList.add("    " + tuple[0].trim() + " "
										+ tuple[1].trim() + " = \" + "
										+ tuple[1].trim() + " + \";");
							}
						}
					}
				}
			}
			String args = join(list2strArray(list), ",");
			bufList.add("    " + className + " ins = new " + className + "("
					+ args + ");");
			bufList.add("}");
			bufList.add(
					"\t//--------------------------------------------------");
		}

		String prefix = "\tSystem.out.println(\"";
		String suffix = "\");\n";
		String LF = "\n";
		StringBuffer buff = new StringBuffer();
		for (String val : bufList) {
			if (val.startsWith("\t")) {
				buff.append(val + LF);
			} else {
				buff.append(prefix + val + suffix);
			}
		}
		return buff.toString();
	}

	public static void main(String[] argv) {
		String str = "	public HttpClient(String requestMethod, String url) {";
		System.out.println(kyPkg.tools.CreateTester.execute(str));
	}
}
