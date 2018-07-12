package kyPkg.atoms;

import static kyPkg.sql.ISAM.CSV_DELIMITED;
import static kyPkg.sql.ISAM.TAB_DELIMITED;
import static kyPkg.util.KUtil.ModArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComboBox;

import globals.ResControl;
import globals.ResControlWeb;
import kyPkg.converter.CnvMap;
import kyPkg.filter.CommonClojure;
import kyPkg.uFile.File49ers;
import kyPkg.uFile.FileUtil;
import kyPkg.util.*;

public class Atomics {
	private static final String KEY = "KEY";
	private static final String MAP = "MAP";
	private static final String NUM = "NUM";
	private static final String MAP_NAMES = "MAP_NAMES";
	private static final String NUM_NAMES = "NUM_NAMES";
	private static final String UNIT = "UNIT";
	private static final String MAP_OCC = "MAP_OCC";
	private static final String TAB = "\t";
	private static final String ATM = "atm";
	private static final String TAG_VER1 = "T_";
	private static final String TAG_VER0 = "TAG_";
	private static final String wLF = System.getProperty("line.separator");
	private static final String naName = "NA"; // NAにつける名称

	protected static String getEXT() {
		return ATM;
	}

	private String otherName = ""; // otherにつける名称
	private HashMap<String, String> hashMap = null;

	// シリアライズできない・・・プロパテイ タグはどうしよう？ ※シングルアンサーしか対応していない！！
	// -------------------------------------------------------------------------
	// 以下のプロパティは対応するElementのlengthにフェーズを合わせる
	// -------------------------------------------------------------------------
	private String[] version = null; // version
	private int[] keyElement = null; // Ｋｅｙエレメント
	private int[] mapElement = null; // Ｍａｐエレメント
	private int[] numElement = null; // Ｎｕｍエレメント
	private int[] mapOcc = null; // 各Ｍａｐエレメントのオカレンス（１ならシングルアンサー）
	private String[] mapTitle = null; // 各Ｍａｐエレメントの名前
	private String[] numTitle = null; // 各Ｎｕｍエレメントの名前
	private String[] numUnit = null; // 各Ｎｕｍエレメントの単位
	private ArrayList<String[]> tagName = null; // タグ名リスト（各Ｍａｐエレメントに対応）
	private CnvMap[] mapConv = null; // マップの値を変換する装置
	private String name = null;
	private String dirPath = null;
	private String format = CSV_DELIMITED;//default

	protected String getDirPath() {
		return dirPath;
	}

	// --------------------------------------------------------------
	// コンストラクタ
	// --------------------------------------------------------------
	public Atomics() {
		this.hashMap = new HashMap();
		this.tagName = new ArrayList();
	}

	// --------------------------------------------------------------
	// モニター属性情報（アンケート等に含まれる要素）をローカルに生成する
	// --------------------------------------------------------------
	public Atomics(String dataPath) {
		this();
		// dataPathが未設定ならカレントを指定する =>C:/@qpr/home/Personal/MonSets/current.txt
		if (dataPath == null || dataPath.trim().equals(""))
			dataPath = ResControl.getCurrentPath();
		incore(dataPath);
	}

	private void incore(String path) {
		System.out.println("#################################################");
		System.out.println("### Atomics コンストラクタ  Path:" + path);
		System.out.println("#################################################");
		String delimiter = new File49ers(path).getDelimiter();
		if (delimiter.equals(TAB)) {
			format = TAB_DELIMITED;
		} else {
			format = CSV_DELIMITED;
		}
		if (FileUtil.isExists(path)) {
			HashMap<String, String> pHmap = incoreMeta(path);
			mapIncore(pHmap);
		} else {
			System.out.println("@Atomics file not exists:" + path);
		}
	}

	// --------------------------------------------------------------
	// ファイルから読み込む
	// --------------------------------------------------------------
	private HashMap incoreMeta(String path) {
		String encode = FileUtil.windowsDecoding;
		name = FileUtil.getFirstName2(path);
		dirPath = FileUtil.getParent2(path, true);
		String metaPath = FileUtil.changeExt(path, ATM);
		//		System.out.println("## Atomics incoreMeta  metaPath:" + metaPath);
		return file2HashMap(metaPath, encode);
	}

	public String getFormat() {
		return format;
	}

	protected String getName() {
		return name;
	}

	protected int[] getKeyElement() {
		return keyElement;
	}

	protected int[] getMapElement() {
		return mapElement;
	}

	protected int[] getNumElement() {
		return numElement;
	}

	// --------------------------------------------------------------
	// 一番最後のmapカラム
	// --------------------------------------------------------------
	protected int getLastmapCol() {
		int lastmapCol = -1;
		for (int i = 0; i < mapElement.length; i++) {
			if (lastmapCol < mapElement[i]) {
				lastmapCol = mapElement[i];
			}
		}
		return lastmapCol;
	}

	private void mapIncore(HashMap<String, String> pHmap) {
		if (pHmap != null) {
			this.hashMap = pHmap;
			parseIt();
		}
	}

	// --------------------------------------------------------------
	// ファイルから読み取ったハッシュを内部変数に書き込む
	// --------------------------------------------------------------
	private void parseIt() {
		keyElement = KUtil.split2Int(hashMap.get(KEY));
		mapElement = KUtil.split2Int(hashMap.get(MAP));
		numElement = KUtil.split2Int(hashMap.get(NUM));
		// System.out.println("@parse mapElement.length:"+mapElement.length);
		if (mapElement != null) {
			mapConv = new CnvMap[mapElement.length];
			//
			mapOcc = KUtil.split2Int_Lim(hashMap.get(MAP_OCC),
					mapElement.length, 1);
			mapTitle = KUtil.split2Str_Lim(hashMap.get(MAP_NAMES),
					mapElement.length);
			// ----------------------------------------------------------------
			// 名前をトリミング
			// ----------------------------------------------------------------
			mapTitle = spaceTrim(mapTitle);
			// // 名前をトリミング・・・2009/10/20
			// for (int i = 0; i < mapTitle.length; i++) {
			// mapTitle[i] = mapTitle[i].replaceAll("　", " ");// 全角SPACE半角化
			// mapTitle[i] = mapTitle[i].trim();
			// mapTitle[i] = mapTitle[i].replaceAll(" ", "　");// 半角SPACE全角化
			// // System.out.println("@parse mapTitle[i]:"+mapTitle[i]);
			// }
		}
		if (numElement != null) {
			numTitle = KUtil.split2Str_Lim(hashMap.get(NUM_NAMES),
					numElement.length);
			numUnit = KUtil.split2Str_Lim(hashMap.get(UNIT), numElement.length,
					"000");
		}
		if (hashMap.containsKey("VER")) {
			version = (hashMap.get("VER")).split(",");
		} else {
			version = null;
		}
		tagName = new ArrayList();
		if (version == null) {
			for (int i = 0; i < mapElement.length; i++) {
				String[] array = KUtil.splitStr(hashMap.get(TAG_VER0 + i));
				tagName.add(array);
			}
		} else {
			for (int i = 0; i < mapElement.length; i++) {
				int col = mapElement[i];
				String[] array = KUtil.splitStr(hashMap.get(TAG_VER1 + col));
				tagName.add(array);
			}
		}
	}

	// ---------------------------------------------------------------------
	// 文字列の後部に連続する連続スペースを取り去る
	// ---------------------------------------------------------------------
	public static String[] spaceTrim(String[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = spaceTrim(array[i]);
		}
		return array;
	}

	public static String spaceTrim(String inStr) {
		String outStr = inStr.replaceAll("　", " ");// 全角スペースを半角に
		outStr = outStr.trim();
		return outStr.replaceAll(" ", "　");// 半角スペースを全角人
	}

	// --------------------------------------------------------------
	// 保存する
	// --------------------------------------------------------------
	// KEY 0
	// MAP 2,3
	// NUM 4,5,6
	// MAP_NAMES 区分１,区分２
	// NUM_NAMES 数量,金額､サンプル数
	// UNIT 000,001,000
	// TAG_0 其の１,其の２,其の３,其の４,其の５,其の６,其の７,其の８,其の９,其の１０
	// TAG_1 その１,その２,その３,その４,その５,その６,その７,その８,その９,その１０
	// --------------------------------------------------------------

	// XXX　サーバー上に一般的な属性データをバッチで生成しておき・・・これを毎日コピーする？！
	// ## Atomics saveAs Path=>C:/Documents and
	// Settings/Administrator.AGC/QPR/Personal/MonSets/current.txt
	// ## Atomics saveAs metaPath=>C:/Documents and
	// Settings/Administrator.AGC/QPR/Personal/MonSets/current.atm
	public int saveAsATM(String path) {
		String metaPath = kyPkg.uFile.FileUtil.changeExt(path, ATM);
		// System.out.println("#saveAsATM# Atomics saveAs      Path=>" + path);
		System.out.println("#saveAsATM# メタデータを出力する  metaPath=>" + metaPath);
		if (mapElement == null) {
			System.out.println(
					"#saveAsATM# Atomics saveAs      mapElement==null");
			return -1;
		}
		// String metaPath = destPathxx + EXT;
		// XXX VERSION情報を持たせたい・・・どうしようか？？
		// 内部項目をハッシュマップに書き戻す
		// 余計なスペースを取り払う
		mapTitle = spaceTrim(mapTitle);
		hashMap.put(KEY, KUtil.join(keyElement));
		hashMap.put(MAP, KUtil.join(mapElement));
		hashMap.put(NUM, KUtil.join(numElement));
		hashMap.put(MAP_NAMES, KUtil.join(mapTitle));
		hashMap.put(NUM_NAMES, KUtil.join(numTitle));
		hashMap.put(UNIT, KUtil.join(numUnit));
		hashMap.put(MAP_OCC, KUtil.join(mapOcc));

		// マップコンバータはシリアライズできない・・・アドホックだ
		if (version == null) {
			// version = new String[]{"1.0"};
			// hashMap.put("VER", KUtil.joinStr(version));
			for (int i = 0; i < mapElement.length; i++) {
				String[] array = tagName.get(i);

				if (array != null) {
					// 余計なスペースを取り払う
					array = spaceTrim(array);

					// for (int j = 0; j < array.length; j++) {
					// array[j] = (array[j].replaceAll("　", " ")).trim();
					// }
					String tag = TAG_VER0 + i;
					//					System.out.println("tag:"+tag +" =>"+KUtil.join(array));
					hashMap.put(tag, KUtil.join(array));
				}
			}
		} else {
			hashMap.put("VER", KUtil.join(version));
			for (int i = 0; i < mapElement.length; i++) {
				String[] array = tagName.get(i);
				if (array != null) {
					// 余計なスペースを取り払う
					array = spaceTrim(array);

					// for (int j = 0; j < array.length; j++) {
					// array[j] = (array[j].replaceAll("　", " ")).trim();
					// }
					String tag = TAG_VER1 + mapElement[i];
					hashMap.put(tag, KUtil.join(array));
				}
			}
		}
		System.out.println(
				"#####################################################");
		return hashMap2File(metaPath, hashMap);
	}

	// --------------------------------------------------------------
	// 2列のタブ区切りテキストを読み込み、０列目をキーに1列目を値としてハッシュマップにputしてこれを返す
	// --------------------------------------------------------------
	// Sample
	// --------------------------------------------------------------
	// KEY 0
	// MAP 1,2
	// MAP_NAMES 世帯特性…主婦年代,世帯主年代
	// MAP_OCC 1,1
	// NUM
	// NUM_NAMES
	// TAG_0 非該当,～２９才,～３９才,～４９才,～５９才,６０才～
	// TAG_1 非該当,～２９才,～３９才,～４９才,～５９才,６０才～
	// UNIT
	// --------------------------------------------------------------
	private HashMap file2HashMap(String path, String encode) {
		HashMap<String, String> hashMap = new HashMap();
		if (new File(path).exists() == false) {
			System.out.println("# ERROR # text2HashMap FileNotFound:" + path);
			return null;
			// return hashMap;
		}
		try {
			String rec;
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(path), encode));
			while (br.ready()) {
				rec = br.readLine();
				if (rec != null && (!rec.equals(""))) {
					String[] splited = rec.split(TAB);
					if (splited.length >= 2) {
						// System.out.println("0:"+splited[0].trim()+" 1:"+splited[1]);
						hashMap.put(splited[0].trim(), splited[1]);
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return hashMap;
	}

	// --------------------------------------------------------------------
	// ハッシュマップよりファイルに書き出す
	// --------------------------------------------------------------------
	int hashMap2File(String metaPath, HashMap<String, String> hashMap) {
		int count = 0;
		try {
			kyPkg.uFile.FileUtil.makeParents(metaPath);
			BufferedWriter bw = new BufferedWriter(new FileWriter(metaPath));
			List<String> keyList = new ArrayList(hashMap.keySet());
			Collections.sort(keyList);
			for (String key : keyList) {
				String val = hashMap.get(key);
				bw.write(key);
				bw.write(TAB);
				bw.write(val);
				bw.write(wLF);
				//				System.out.println("#hashMap2File key:"+key+" val:"+val);
				count++;
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			// return -1;
			System.exit(0);
		}
		return count;
	}

	protected void addTagName(int col, String title, List list) {
		// System.out.println("##addTagName##");
		if (col <= 0)
			return;
		if (title == null)
			return;
		if (list == null)
			return;
		mapElement = ModArray(mapElement, col);
		mapTitle = ModArray(mapTitle, title);
		String[] tagArray = (String[]) list.toArray(new String[list.size()]);
		tagName.add(tagArray);
	}

	public void modTagName(String value, String delimiter) {
		// System.out.println("##modTagName##");
		if (value == null)
			return;
		this.tagName.add(value.split(delimiter));
	}

	protected void setTagName(ArrayList<String[]> tagName) {
		// System.out.println("##setTagName##");
		if (tagName == null)
			return;
		this.tagName = tagName;
	}

	public String[] getTag(int seq) {
		String[] array = null;
		CnvMap mapConv = getMapConv(seq);
		if (mapConv != null) {
			array = mapConv.getTagArray();// XXX 代替名・・・大丈夫か？？@@@
		} else {
			if (tagName.size() >= seq) {
				array = tagName.get(seq);
			} else {
				System.out.println("#ERROR @getTag seq:" + seq);
				System.out.println(
						"#ERROR @getTag tagName.size():" + tagName.size());
				array = null;
			}
		}
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				array[i] = (array[i].replaceAll("　", " ")).trim();
			}
		}
		return array;
	}

	public List getTagList(int seq) {
		String[] array = getTag(seq);
		if (array == null)
			return null;
		List list = Arrays.asList(array);
		return list;
	}

	// -------------------------------------------------------------------------
	// その他を付け加える場合を考慮・・・・
	// XXX その他＆NA処理はクラスベースではないほうがよさそうなので要修正かな・・・
	// -------------------------------------------------------------------------
	public String[] getTagNa(int seq) {
		String[] array = getTag(seq);
		if (otherName.equals("")) {
			return array;
		} else {
			String[] rtnArray = new String[array.length + 1];
			if (mapOcc[seq] == 1) {
				rtnArray[0] = otherName;
			} else {
				rtnArray[0] = naName;
			}
			for (int i = 0; i < array.length; i++) {
				rtnArray[i + 1] = array[i];
			}
			return rtnArray;
		}
	}

	// -------------------------------------------------------------------------
	// 該当ＭＡＰのタグのサイズを返す(その他を付け加える場合を考慮されている・・・・)
	// -------------------------------------------------------------------------
	public int getSize(int seq) {
		String[] array = getTagNa(seq);
		if (array == null)
			return 0;// XXX これでいいのかどうか疑問 、-1では誤動作したときややこしいので
		return array.length;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	// -------------------------------------------------------------------------
	// private String dataPath=""; // 対応するデータのパス
	// あとでに追加しておきたいパラメータ
	// VER バージョンナンバー
	// NAME データの名前（省略可）
	// TARGET 対象データのパス（省略可）
	// OWNER オーナー
	// CATEGORY 対象品目
	// TERM 対象期間
	// SAMPLE サンプル数
	// COMMENT 抽出条件など・・・・
	// -------------------------------------------------------------------------
	// 位置情報（ゼロから始まる）コンテナなのでそれなりのアクセッサも用意する
	// -------------------------------------------------------------------------
	protected void setMapConv(CnvMap[] mapConv) {
		this.mapConv = mapConv;
	}

	protected void setMapConv(int seq, CnvMap pMapConv) {
		if (mapConv == null)
			mapConv = new CnvMap[mapElement.length]; // TODO 怪しい！
		this.mapConv[seq] = pMapConv;
	}

	protected CnvMap getMapConv(int seq) {
		if (mapConv == null)
			return null;
		// System.out.println("debug@getMapConv mapConv.length:"+mapConv.length);
		// System.out.println("debug@getMapConv seq:"+seq);
		// String bug20100826;
		if (mapConv.length <= seq)
			return null;
		return mapConv[seq];
	}

	// --------------------------------------------------------------
	// 列名はユニークでなければならない・・・ことにしよう！！あらら
	// --------------------------------------------------------------
	protected int getColSeq(String title) {
		for (int i = 0; i < mapTitle.length; i++) {
			if (title.equals(mapTitle[i])) {
				return mapElement[i];
			}
		}
		return -1;
	}

	// -------------------------------------------------------------------------
	// 該当ＭＡＰのタグ情報を返す <Original Version>
	// -------------------------------------------------------------------------
	public String[] getTagsByName(String title) {
		for (int i = 0; i < mapTitle.length; i++) {
			if (title.equals(mapTitle[i])) {
				return tagName.get(i);
				// String[] array = KUtil.splitStr(hashMap.get(TAG_VER0 + i));
				// return array;
			}
		}
		return null;
	}

	public void setKeyElement(List keyList) {
		this.keyElement = KUtil.list2intArray(keyList);
	}

	public void setMapElement(List mapList) {
		this.mapElement = KUtil.list2intArray(mapList);
	}

	public void setNumElement(List numList) {
		this.numElement = KUtil.list2intArray(numList);
	}

	public void setKeyElement(int[] keyElement) {
		this.keyElement = keyElement;
	}

	public void setMapElement(int[] mapElement) {
		this.mapElement = mapElement;
	}

	public void setMapOcc(int[] mapOcc) {
		this.mapOcc = mapOcc;
	}

	public void setMapTitle(String[] mapTitle) {
		this.mapTitle = mapTitle;
	}

	public void setNumElement(int[] numElement) {
		this.numElement = numElement;
	}

	public void setNumTitle(String[] numTitle) {
		this.numTitle = numTitle;
	}

	public void setNumUnit(String[] numUnit) {
		this.numUnit = numUnit;
	}

	// --------------------------------------------------------------
	// オカレンスが１ならシングル項目、それ以上ならマルチアンサーとする（乱暴だが・・・簡単の為）
	// --------------------------------------------------------------
	public int getMapOcc(int seq) {
		if (mapOcc.length <= seq)
			return -1; // error ...1でも良いかも
		return mapOcc[seq];
	}

	public int getKeyElement(int seq) {
		if (keyElement.length <= seq)
			return -1;
		return keyElement[seq];
	}

	public int getMapElement(int seq) {
		if (mapElement.length <= seq)
			return -1;
		return mapElement[seq];
	}

	public int getNumElement(int seq) {
		if (numElement.length <= seq)
			return -1;
		return numElement[seq];
	}

	public String getMapTitle(int seq) {
		if (mapTitle.length <= seq)
			return null;
		return mapTitle[seq];
	}

	public String[] getParmArray() {
		return mapTitle;
	}

	public String getNumTitle(int seq) {
		if (numTitle.length <= seq)
			return null;
		return numTitle[seq];
	}

	public String[] getNumTitle() {
		return numTitle;
	}

	public String getNumUnit(int seq) {
		if (numUnit.length <= seq)
			return null;
		return numUnit[seq];
	}

	public List<String> getChileList(int mapCol, String delim) {
		List<String> list = new ArrayList();
		String mapName = getMapTitle(mapCol);
		String[] tagNames = getTag(mapCol);
		int mapOcc = getMapOcc(mapCol);
		for (int i = 0; i < tagNames.length; i++) {
			// 　タグ名称=回答選択肢、回答値、マップ名＝設問、マップシーケンス＝マップカラム、オカレンス
			list.add(tagNames[i] + delim + (i + 1) + delim + mapName + delim
					+ mapCol + delim + mapOcc);
		}
		return list;
	}

	public void mapTitles2Combo(JComboBox comboBox) {
		copyArray2Combo(comboBox, getParmArray());
		return;
	}

	public void copyArray2Combo(JComboBox comboBox, String[] array) {
		// 順序に意味を持たせたあるのでソート等をしてはいけない
		if (array == null)
			return;
		comboBox.removeAllItems();
		List<String> list = Arrays.asList(array);
		if (list == null)
			return;
		for (String element : list) {
			comboBox.addItem(element);
		}
		comboBox.setSelectedIndex(0);
	}

	// ------------------------------------------------------------------------
	// XXX マルチアンサー項目なのか、シングルアンサー項目なのか？それぞれどう使うのか？
	// XXX 数値項目を持つ場合、持たない場合・・・拡張子は変える？それともすべて数値項目を持たせるか？
	// XXX 購買データなのかどうか、判定させる必要はあるのかも知れない、軸にしたいのだから・・・atxとする？
	// 合成をする場合・・・METAデータの合成を行わなければならないが、タイトルはユニークにしなければならない
	// 理由：機械的に項目が同じもの（同種）であるか判定するのに使いたい 例＞対期間比較などを行う場合など
	// 内部項目をハッシュマップに書き戻す
	// --------------------------------------------------------------
	// Ｍａｐを除去する
	// --------------------------------------------------------------
	public void remove(int seq) {
	};

	// --------------------------------------------------------------
	// Ｍａｐを合成 （ｘ-１）＋ｙ??によって合成する（名票も同時に合成する）
	// --------------------------------------------------------------
	public void composeIt(int seq1, int seq2) {
	};

	// --------------------------------------------------------------
	// エンティティ間の連結＆統合
	// 単純化のためＮｕｍを持っているエンティティは左辺（軸）にのみ指定可とする
	// Ｎｕｍ項目を連結すると→金額１、金額２などと命名したり話がややこしくなる
	// 数値間の比較を指定させる場合などもゴテゴテしてダサイくなる可能性が高い
	// （しかい、Ｎｕｍを持っているエンティティかどうかどうやって判定するの？）
	// 兎に角、自由度が下がらないように注意しよう・・・
	// --------------------------------------------------------------
	public void synthesize(String path1, String path2, int[] element1,
			int[] element2) {
	};

	// --------------------------------------------------------------
	// Ｍａｐの構成を変更（カテゴリー変換）3時間おきに変換とか・・そういうの
	// --------------------------------------------------------------
	public void filtering(int seq, CnvMap cnvMap) {
	};

	public static void main(String[] args) {
		//		test00();
	}

	// ------------------------------------------------------------------------
	//	20150811 for debug
	// ------------------------------------------------------------------------

	public static void test00() {
		int[] cols = { 1, 2 };// 欲しいカラムの情報
		// String zappa = "20091105";
		String userDir = globals.ResControl.getQPRHome();
		String enqPath = userDir + "current";
		System.out.println("20121004debug @test00 dstPath:" + enqPath);
		System.out.println("# checkpoint 002 #20130401@ of Atomics() caller");

		Atomics insAtomics = new Atomics(enqPath);
		// ------------------------------------------------------
		// そのカラムのデータ値に対応するタグ名称がほしい・・・
		// ------------------------------------------------------
		HashMap<String, String>[] mapArray = new HashMap[cols.length];
		for (int i = 0; i < mapArray.length; i++) {
			mapArray[i] = new HashMap<String, String>();
			String[] tags = insAtomics.getTag(i);
			for (int j = 0; j < tags.length; j++) {
				String tag = tags[j];
				String key = String.valueOf(j);
				System.out.println("key>>" + key + " tag>>" + tag);
				mapArray[i].put(key, tag);
			}
		}
		int i = 1;
		System.out.println("tag 1:" + mapArray[i].get("1"));
		System.out.println("tag 2:" + mapArray[i].get("2"));
		System.out.println("tag 3:" + mapArray[i].get("3"));
		System.out.println("tag 4:" + mapArray[i].get("4"));
		System.out.println("tag 5:" + mapArray[i].get("5"));
	}

	public static void test01() {
		String atmPath = ResControlWeb
				.getD_Resources_PUBLIC("commonstock/elements/freakoutAtm.atx");
		System.out.println("20121004debug @test00 dstPath:" + atmPath);
		System.out.println("# checkpoint 003 #20130401@ of Atomics() caller");

		Atomics insAtomics = new Atomics(atmPath);
		int[] dim1 = { 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 };
		int[] dim2 = { 1, 0, 1, 0 };
		int[] val = { 1 };
		Map2Matrix closure = new Map2Matrix(insAtomics, dim1, dim2, val);
		closure.setOutPath(ResControlWeb.getD_Resources_PUBLIC(
				"commonstock/elements/freakoutAtm.json"));
		String srcFile = ResControlWeb
				.getD_Resources_PUBLIC("commonstock/elements/freakoutAtm.txt");
		new CommonClojure().incore(closure, srcFile, true);
	}
}
