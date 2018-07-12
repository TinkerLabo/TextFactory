package kyPkg.tools;

import java.util.ArrayList;
import java.util.List;

public class CnvListSource {
	// ------------------------------------------------------------------------
	// 文字列からリストのソースに書き換える
	// ------------------------------------------------------------------------
	public static List<String> execute(List<String> list) {
		List<String> listDst = new ArrayList();
		listDst.add("List<String> list = new ArrayList();");
		for (String element : list) {
			listDst.add("list.add(\"" + element + "\");");
		}
		return listDst;
	}
}
