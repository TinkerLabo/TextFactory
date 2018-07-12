package kyPkg.tools;

import java.util.ArrayList;
import java.util.List;

public class CnvArraySource extends Tool {
	// ------------------------------------------------------------------------
	// 文字列から配列のソースに書き換える
	// ------------------------------------------------------------------------
	public static List<String> execute(List<String> list) {
		int i = 0;
		List<String> listDst = new ArrayList();
		for (String element : list) {
			i++;
			String array[] = element.split("\t");
			listDst.add("String[] array_" + i + " = new String[]{");
			for (String val : array) {
				listDst.add("\"" + val + "\",");
			}
			listDst.add("};");
		}
		return listDst;
	}
}
