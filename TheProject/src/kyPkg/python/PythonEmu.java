package kyPkg.python;

import java.io.BufferedReader;
import java.io.File;
import java.util.HashSet;

import kyPkg.filter.CommonWriter;
import kyPkg.filter.Inf_BaseClojure;
import kyPkg.uFile.File49ers;
import kyPkg.uFile.FileUtil;

public class PythonEmu {
	// PYTHONの内包をjavaで実装してみる試み・・・・
	// PYTHONのDictionaryを読み込む・・書き出す
	// PYTHONのDictionaryをJSONへ相互変換
	// XXX このクラスを使ってユークリッド距離による相関を計算するロジックを作る
	// XXX 上記ができたら次はピアソンを作る
	// XXX さらに上記を組み合わせて、協調フィルタリングによる推薦モデルを作成する
	public static void main(String[] argv) {
		test0();
	}

	// public static int map2file(String outPath, HashMap<String, Object> map) {
	// CommonWriter closure = new CommonWriter(outPath);
	// closure.init();
	// int count = mapIterator(map, closure);
	// closure.fin();
	// return count;
	// }
	public static int set2file(String outPath, HashSet<String> set) {
		CommonWriter closure = new CommonWriter(outPath);
		closure.init();
		int count = setIterator(set, closure);
		closure.write();
		return count;
	}

	// private static int mapIterator(HashMap<String, Object> map,
	// Inf_BaseClosure closure) {
	// int count = 0;
	// StringBuffer buff = new StringBuffer();
	// Set set = map.entrySet();
	// java.util.Iterator it = set.iterator();
	// while (it.hasNext()) {
	// buff.delete(0, buff.length());
	// java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
	// String wKey = (String) ent.getKey();
	// String wVal = (String) ent.getValue();
	// buff.append(wKey);
	// buff.append(wVal);
	// closure.execute(buff.toString());
	// count++;
	// //System.out.println("key:" + wKey + " val:" + wVal);
	// }
	// return count;
	// }
	public static int setIterator(HashSet<String> set,
			Inf_BaseClojure closure) {
		int count = 0;
		StringBuffer buff = new StringBuffer();
		java.util.Iterator it = set.iterator();
		while (it.hasNext()) {
			buff.delete(0, buff.length());
			java.util.Map.Entry ent = (java.util.Map.Entry) it.next();
			String wKey = (String) ent.getKey();
			String wVal = (String) ent.getValue();
			buff.append(wKey);
			buff.append(wVal);
			closure.execute(buff.toString());
			count++;
			// System.out.println("key:" + wKey + " val:" + wVal);
		}
		return count;
	}

	public static HashSet<String> file2Set(String path) {
		File49ers f49ins = new File49ers(path);
		String delimiter = f49ins.getDelimiter();
		HashSet<String> set = new HashSet();
		String wRec;
		File fl = new File(path);
		if (fl.exists() == false) {
			System.out.println("PythonEmu@incore FileNotFound:" + path);
			return null;
		}
		try {
			System.out.println("@incore File:" + path);
			BufferedReader br = FileUtil.getBufferedReader(path);
			//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				wRec = br.readLine();
				if (wRec != null && (!wRec.equals(""))) {
					int pos = wRec.indexOf(delimiter); // "\t"
					if (pos > 0) {
						String key = wRec.substring(0, pos);
						set.add(key);
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		if (set.size() > 0) {
			System.out.println("set.size()>" + set.size());
			return set;
		} else {
			return null;
		}
	}

	public static void test0() {
		// 購入有り・無しのマッピング取り出し機構・・・・
		String parmMaster = "c:/sample/masterX.txt";
		String paramTran = "c:/sample/TranX.txt";
		HashSet<String> set1 = file2Set(parmMaster);
		HashSet<String> set2 = file2Set(paramTran);
		HashSet positive = new HashSet();
		HashSet negative = new HashSet();
		vennMix(set1, set2, positive, negative);
		System.out.println("positive count:" + positive.size());
		System.out.println("negative count:" + negative.size());
		System.out
				.println("target   count:" + positive.size() + negative.size());
		String positivePath = "c:/sample/positive.map";
		String negativePath = "c:/sample/negative.map";
		set2file(positivePath, positive);
		set2file(negativePath, negative);
	}

	// expression for 変数 in リスト if 条件
	public static HashSet<String> vennMix(HashSet<String> set1,
			HashSet<String> set2) {
		HashSet positive = new HashSet();
		vennMix(set1, set2, positive, null);
		return positive;
	}

	public static void vennMix(HashSet<String> set1, HashSet<String> set2,
			HashSet positive) {
		vennMix(set1, set2, positive, null);
	}

	// public static HashSet<String> vennMix(HashSet<String>
	// set1,HashSet<String> set2, HashSet positive, HashSet negative) {
	public static void vennMix(HashSet<String> set1, HashSet<String> set2,
			HashSet positive, HashSet negative) {
		if (set1 == null)
			return;
		if (set2 == null)
			return;
		java.util.Iterator itr = set1.iterator();
		while (itr.hasNext()) {
			String key = (String) itr.next();
			// java.util.Map.Entry ent = (java.util.Map.Entry) itMaster.next();
			// String wItem = (String) ent.getKey();
			if (set2.contains(key)) {
				// positive
				if (positive != null)
					positive.add(key);
			} else {
				// negative
				if (negative != null)
					negative.add(key);
			}
		}
		return;
	}

	// private HashMap<String, HashMap> hashMap = null;
	//
	// public PythonEmu() {
	// hashMap = new HashMap();
	// }
	// public static void test01() {
	// String key = "Lisa Rose";
	// String chile = "Lady in the Water";
	// Object val = new Float(2.5);
	// PythonEmu ins = new PythonEmu();
	// ins.pythonPut(key, chile, val);
	// Object obj = ins.pythonGet(key, chile);
	// System.out.println("obj:" + obj.toString());
	// }
	//
	// public HashMap<String, Object> pythonGetMap(String key) {
	// return hashMap.get(key);
	// }
	// public List pythonGetList(String key) {
	// return hmap2List(hashMap.get(key));
	// }
	//
	// public Object pythonGet(String key, String chile) {
	// HashMap<String, Object> wkMap = hashMap.get(key);
	// if (wkMap != null) {
	// Object val = wkMap.get(chile);
	// return val;
	// } else {
	// return null;
	// }
	// }
	//
	// public void pythonPut(String key, String chile, Object val) {
	// HashMap<String, Object> wkMap = hashMap.get(key);
	// if (wkMap == null)
	// wkMap = new HashMap();
	// wkMap.put(chile, val);
	// hashMap.put(key, wkMap);
	// }

}
