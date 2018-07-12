package kyPkg.uFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class YamlControl {
	private HashMap<String, String> itpInfo;
	public static final String YML = "yml";
	private String yamlPath = "";

	public String getYamlPath() {
		return yamlPath;
	}

	public static String getExt() {
		return "." + YML.toLowerCase();
	}

	private void setInfoMap(HashMap<String, String> map) {
		if (map == null) {
			this.itpInfo = new HashMap<String, String>();
		} else {
			this.itpInfo = map;
		}
	}

	// -------------------------------------------------------------------------
	// コンストラクタ 引数なし
	// -------------------------------------------------------------------------
	public YamlControl() {
		super();
		setInfoMap(null);
	}

	// -------------------------------------------------------------------------
	// コンストラクタ ハッシュマップで初期化する場合
	// -------------------------------------------------------------------------
	public YamlControl(HashMap<String, String> infoMap) {
		super();
		setInfoMap(infoMap);
	}

	// -------------------------------------------------------------------------
	// コンストラクタ 　　　　ファイルより初期化する場合
	// -------------------------------------------------------------------------
	public YamlControl(String path) {
		super();
		this.yamlPath = getYmlPath(path);
		// System.out.println("# @YamlControl　コンストラクタ　path:" + yamlPath);
		List<String> infoList = ListArrayUtil.file2List(yamlPath);
		setInfoMap(cnvList2Map(infoList));
	}

	// -------------------------------------------------------------------------
	// コンストラクタ リストで初期化する場合
	// -------------------------------------------------------------------------
	public YamlControl(List<String> infoList) {
		super();
		setInfoMap(cnvList2Map(infoList));
	}

	// -------------------------------------------------------------------------
	// リストをハッシュマップに変換
	// -------------------------------------------------------------------------
	private HashMap<String, String> cnvList2Map(List<String> infoList) {
		HashMap<String, String> map = null;
		if (infoList != null)
			map = kyPkg.util.KUtil.list2map(infoList, "\t");
		return map;
	}

	// -------------------------------------------------------------------------
	// #拡張子を”.yml”に書き換える
	// -------------------------------------------------------------------------
	public static String getYmlPath(String dataPath) {
		return FileUtil.getPreExt(dataPath) + "." + YML;
	}

	// -------------------------------------------------------------------------
	// #ハッシュマップを返す
	// -------------------------------------------------------------------------
	public HashMap<String, String> getInfoMap() {
		return itpInfo;
	}

	// -------------------------------------------------------------------------
	// #デバッグ用
	// -------------------------------------------------------------------------
	public static void debug_enumIt(HashMap<String, String> infoMap) {
		System.out
				.println("<< debug_enumIt start >>----------------------------");
		List<String> keylist = new ArrayList(infoMap.keySet());
		Collections.sort(keylist);
		for (String key : keylist) {
			System.out.println("key:" + key + " => val:" + infoMap.get(key));
		}
		System.out
				.println("<< debug_enumIt end   >>----------------------------");
	}

	// -------------------------------------------------------------------------
	// メタデータを読み込んでハッシュに追加する
	// TODO 要テスト・・・
	// -------------------------------------------------------------------------
	public void modYml(String modPath) {
		List modList = ListArrayUtil.file2List(modPath);
		itpInfo = HashMapUtil.modHashWithList(itpInfo, modList, "\t");
	}

	// yamlを合成する
	// private void readModYml(String originalPath, String modPath) {
	// HashMap<String, String> infoMap = getMap(originalPath);
	// readModYml(infoMap, modPath);
	// }

	// ---------------------------------------------------------------
	// write
	// ---------------------------------------------------------------
	public void saveAs(String path) {
		String metaPath = getYmlPath(path);// 拡張子を強引に書き換える
		HashMapUtil.hashMap2File(metaPath, itpInfo);
	}

	public void save() {
		String metaPath = getYmlPath(yamlPath);
		HashMapUtil.hashMap2File(metaPath, itpInfo);
	}

	// private List<String> getList(String path) {
	// return kyPkg.util.KUtil.hmap2list(infoMap);
	// // FileUtil.list2File(metaPath, list);
	// }

	// ---------------------------------------------------------------
	// ラッパーメソッド
	// ---------------------------------------------------------------
	// XXX 重要な箇所で使用しているので慎重にテストすること
	// このパターンのコンストラクタを作っておくか・・・・パスとデフォルトマップとフラグ・・・
	public static HashMap<String, String> yaml2Map(String yamlPath) {
		return yaml2Map(yamlPath, null, false);
	}

	public static HashMap<String, String> yaml2Map(String yamlPath,
			HashMap<String, String> defaultMap, boolean forceDefault) {
		// ファイルが無ければ、デフォルト値を使用するという意味・・・・で使用している
		YamlControl ymlCtrl = null;
		if (forceDefault == false && new File(yamlPath).isFile())
			ymlCtrl = new YamlControl(yamlPath);
		if (ymlCtrl == null && defaultMap != null) {
			ymlCtrl = new YamlControl(defaultMap);
			ymlCtrl.saveAs(yamlPath);
		}
		if (ymlCtrl != null) {
			return ymlCtrl.getInfoMap();
		} else {
			return null;
		}
	}

	// 辞書引き
	public String get(String key) {
		return HashMapUtil.getExtra(itpInfo, key, "");// NotFoundは止める
	}

	// 2012-05-31 mod
	public String getExtra(String key, String defaultVal) {
		return HashMapUtil.getExtra(itpInfo, key, defaultVal);
	}

	// public String get(String key) {
	// if (infoMap == null)
	// return null;
	// String str = infoMap.get(key);
	// if (str == null) {
	// str = "notDefined";
	// }
	// return String.valueOf(str);
	// }

	// 辞書登録
	public void put(String key, String value) {
		// if (infoMap == null)
		// return;
		// System.out.println("Yaml Put key:" + key + " val:" + value);
		itpInfo.put(key, value);
	}

	public void putAll(HashMap<String, String> map) {
		if (map == null)
			return;
		itpInfo.putAll(map);

		List<String> keyList = new ArrayList(itpInfo.keySet());
//		for (String key : keyList) {
//			System.out.println("putAll>> key:" + key + " val:" + itpInfo.get(key));
//		}

	}

	// 辞書引き（数値で返す版）　※小数点があるとダメなので気をつける
	public int getAsInt(String key, int defaultVal) {
		String str = itpInfo.get(key);
		if (str == null)
			return defaultVal;
		try {
			return Integer.valueOf(str);
		} catch (NumberFormatException e) {
			return defaultVal;
		}
	}

	// ########################################################################

	public static void tester() {
		String path1 = "c:/swamp.trn";
		YamlControl ins1 = new YamlControl();
		ins1.put("Eric", "Clapton");
		ins1.put("Duane", "Allman");
		ins1.put("Jesse", "Davis");
		ins1.put("num", "1234567890");
		ins1.put("π", "3.14");
		// ins1.debug_enumIt();
		ins1.saveAs(path1);// ※拡張子はymlに書き換えられる

		YamlControl ins2 = new YamlControl(path1);
		YamlControl.debug_enumIt(ins2.getInfoMap());
		int i = ins2.getAsInt("π", 0);
		int j = ins2.getAsInt("ε", 0);
		int k = ins2.getAsInt("num", 0);
		String s = ins2.get("π");
		System.out.println("π=>" + i);
		System.out.println("ε=>" + j);
		System.out.println("k=>" + k);
		System.out.println("s=>" + s);
	}

	public static void tester2() {
		String inPath = "c:/swamp.trn";
		String outPath = "c:/Rock.trn";
		YamlControl ymlCtrl = new YamlControl(inPath);
		ymlCtrl.saveAs(outPath);

	}

	// ------------------------------------------------------------------------
	// test for MTA (mta.ymlファイルが存在しなければデフォルト値で生成する)
	// ------------------------------------------------------------------------
	public static void test20150309() {
		String curDir = globals.ResControl.getCurDir();
		String paramPath = curDir + "test."+YamlControl.YML;
		System.out.println("paramPath:" + paramPath);
		HashMap<String, String> pMap = new HashMap<String, String>();
		pMap.put("SERVER", "redmineserver");
		pMap.put("PORT", "8080");
		pMap = kyPkg.uFile.YamlControl.yaml2Map(paramPath, pMap, false);
		YamlControl.debug_enumIt(pMap);
	}
	public static void test20151225() {
		String curDir = globals.ResControl.getCurDir();
		String paramPath = curDir + "connect."+YamlControl.YML;
		System.out.println("paramPath:" + paramPath);
		HashMap<String, String> pMap = new HashMap<String, String>();
		pMap.put("SERVER", "redmineserver");
		pMap.put("PORT", "8080");
		pMap = kyPkg.uFile.YamlControl.yaml2Map(paramPath, pMap, false);
		YamlControl.debug_enumIt(pMap);
	}

	// ########################################################################
	// main
	// ########################################################################
	public static void main(String[] argv) {
		// tester();
		// tester2();
		// tester3();
		test20150309();
	}

}
