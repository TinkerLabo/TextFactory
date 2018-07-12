package kyPkg.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TemplateOfClojure implements Inf_BaseClojure {
	private String delimiter = "\t";
	private HashMap<String, String> hmap;
	private List<String> list;

	// -------------------------------------------------------------------------
	// 2013-07-09 yuasa （コピーして雛形として使用する）
	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public TemplateOfClojure() {
	}

	// -------------------------------------------------------------------------
	// 初期処理
	// -------------------------------------------------------------------------
	@Override
	public void init() {
		System.out.println("TemplateOfClojure初期処理");
		list = new ArrayList();
		hmap = new HashMap();
	}

	@Override
	public void execute(String rec) {
		execute(rec.split(delimiter));
	}

	@Override
	public void execute(String[] rec) {
		if (rec.length >= 2) {
			list.add(rec[1]);
			hmap.put(rec[0], rec[1]);
		}
	}

	// -------------------------------------------------------------------------
	// 終了処理
	// -------------------------------------------------------------------------
	@Override
	public void write() {
		System.out.println("TemplateOfClojure終了処理");
		for (String element : list) {
			System.out.println("list>" + element);
		}
		emunit(hmap);
	}
	private void emunit(HashMap<String, String> hmap) {
		List<String> keyList = new ArrayList(hmap.keySet());
		Collections.sort(keyList);
		for (String key : keyList) {
			System.out.println("key>" + key + " val:" + hmap.get(key));
		}
	}


	// -------------------------------------------------------------------------
	// main
	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		test01();
	}

	public static void test01() {
		Inf_BaseClojure clojure = new TemplateOfClojure();
		clojure.init();
		clojure.execute("002	world");
		clojure.execute("001	hello");
		clojure.write();
	}

	public static void test02() {
		String srcFile = "templates/temp01.txt";
		TemplateOfClojure closure = new TemplateOfClojure();
		new CommonClojure().incore(closure, srcFile, true);
	}
}
