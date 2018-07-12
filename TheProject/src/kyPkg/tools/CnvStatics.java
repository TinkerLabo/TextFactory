package kyPkg.tools;

import java.util.ArrayList;
import java.util.List;

public class CnvStatics extends Tool {
	// ------------------------------------------------------------------------
	// ｖｂのソースを利用して定数を生成するためのツール
	// 文字列はコロン（：）で区切られているものとする(以下のようなスタイルを期待している)
	// メーカー　　　　　　　　　　　:MKR
	// 区分１　　　　　　　　　　　　:KB1
	// 区分２　　　　　　　　　　　　:KB2
	// 区分３　　　　　　　　　　　　:KB3
	// 区分４　　　　　　　　　　　　:KB4
	// 区分５　　　　　　　　　　　　:KB5
	// 区分６　　　　　　　　　　　　:KB6
	// アイテム　　　　　　　　　　　:ITM
	// 品目名 　　　　　　　　　　　:HN4
	// ------------------------------------------------------------------------
	public static List<String> execute(List<String> list) {
		List<String> listA = new ArrayList();
		List<String> listB = new ArrayList();
		for (String element : list) {
			String[] array = element.split(":");
			if (array.length >= 2) {
				listA.add(array[0]);
				listB.add(array[1].trim());
			}
		}
		List<String> listRes = new ArrayList();
		for (int i = 0; i < listA.size(); i++) {
			listRes.add("public static final String " + listB.get(i) + " = \""
					+ listA.get(i) + "\";");
		}
		listRes.add("public static String[] arrayList_X = {");
		for (int i = 0; i < listA.size(); i++) {
			listRes.add("\t" + listB.get(i) + ",");
		}
		listRes.add("};");
		listRes.add("");
		listRes.add("HashMap map_X;");
		listRes.add("map_X = new HashMap();");
		for (int i = 0; i < listA.size(); i++) {
			listRes.add("map_x.put(RepDict." + listB.get(i) + ", \""
					+ listB.get(i) + "\");");
		}
		listRes.add("");
		listRes.add("//●リストの場合（カスタムコードに指定する）");
		listRes.add(
				"ListModel listModelX new javax.swing.AbstractListModel() {");
		listRes.add(
				"    public int getSize() { return RepDict.arrayList_X.length; }");
		listRes.add(
				"    public Object getElementAt(int i) { return RepDict.arrayList_X[i]; }");
		listRes.add("};");
		listRes.add("");
		listRes.add("//●コンボボックスの場合（カスタムコードに指定する）");
		listRes.add(
				"ComboBoxModel cmbModelX = new javax.swing.DefaultComboBoxModel(RepDict.arrayList_X);");
		return listRes;
	}

}
