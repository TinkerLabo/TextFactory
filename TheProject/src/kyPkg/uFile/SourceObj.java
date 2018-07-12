package kyPkg.uFile;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kyPkg.sql.ServerConnecter;
import kyPkg.util.CnvArray;

abstract public class SourceObj implements INF_FieldSource {
	protected static final String TABLE = "table";
	protected static final String FIELD = "field";
	protected static final String KEY = "key";
	// ------------------------------------------------------------------------
	// DEFAULT_FIELDS=>C:/@qpr/home/qpr/res/defaultFields.txt
	// ------------------------------------------------------------------------
	// ユーザー固有の属性設定リストが、以下のような形式で格納されている
	// selectedFields<TAB>モニターＩＤ,購入日付,購入時間,JanCode,購入金額Ｘ数量,購入金額,購入数量,購入先（業態）,購入先（詳細）,アイテム名称
	// sourceDir<TAB>Z:/S2/rx/enquetes/NQ/03_属性・性年代編/2014/
	// ------------------------------------------------------------------------
	// 年の要素をどうにか持たせたい・・・target：2014のようにすればいいだろうか・・・現在名称で持っているので名称が変わってしまうとイケナイ
	// ------------------------------------------------------------------------
	// XXX 20140922 それぞれの要素がわかりにくいので例示する必要あり
	// ------------------------------------------------------------------------
	private String sourceDir = "";
	private String[] splitteds;
	private String key = "";// "XA1";

	protected String table = "";// 対象SQLテーブル名称
	protected List<String> selectedFields = null; // フィールドの名称（ラベル）( 例＞"モニターＩＤ","購入日付",)
	private List<String> allFields; // フィールド名(例＞"モニターＩＤ","購入日付","購入金額","購入先（詳細）")
	private HashMap<String, String> fieldMap;// フィールド名と識別フィールドの対応表(例＞put(MONITORID,"TRN.Monitor");)
	private HashMap<String, HashMap<String, String>> dictionary;

	protected static final String CONNECT = "connect";
	private HashMap<String, String> connectMap;

	// ------------------------------------------------------------------------
	// QTB関連
	// ------------------------------------------------------------------------
	// フィールド値の値とそれが指す名称（内容）を変換するマップ
	// 例＞１：月曜日、２：火曜日・・・・
	// ------------------------------------------------------------------------
	private static final String QTB1_TXT = "Qtb1.TXT";
	private static final String ALIAS_TXT = "alias.txt";
	private HashMap<String, String> typMap = new HashMap();
	private HashMap<String, String> key2nam = new HashMap();
	//	private String alterName = "T1";//"MON";//
	private String alterName = "mon";//20150831  属性検索時のエラー対応　これが正しいのかどうかわからないが・・・

	public String getTableAlterName() {
		return alterName;
	}

	private String KEY_NAME = "KEY";

	public void setKeyName(String key_name) {
		KEY_NAME = key_name;
	}

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public SourceObj() {
		super();
		selectedFields = null;
		allFields = new ArrayList();
		typMap = new HashMap();
		key2nam = new HashMap();
		fieldMap = new HashMap();
		dictionary = new HashMap();
		connectMap = new HashMap();
	}

	//	public String getAlterName() {
	//		return alterName;
	//	}
	//
	//	public void setAlterName(String alterName) {
	//		this.alterName = alterName;
	//	}

	// ------------------------------------------------------------------------
	// getConnectVal("SERVER");
	// getConnectVal("DATABASE");
	// ------------------------------------------------------------------------
	public String getConnectVal(String key) {
		String rtnVal = "";
		if (connectMap == null)
			return rtnVal;
		rtnVal = connectMap.get(key);
		if (rtnVal == null)
			rtnVal = "";
		return rtnVal;
	}

	public String getServer() {
		return getConnectVal("SERVER").toUpperCase();
	}

	public String getDataBase() {
		return getConnectVal("DATABASE");
	}

	// ------------------------------------------------------------------------
	//
	// ------------------------------------------------------------------------
	public void setConnect(String connect) {
		// System.out.println("setConnect:" + connect);
		connectMap = parseConnect(connect);
	}

	// ------------------------------------------------------------------------
	// connect DRIVER={SQL
	// Server};SERVER=ks1s003;UID=sa;PWD=;DATABASE=qprdb;Trusted_Connection=true
	// ------------------------------------------------------------------------
	private HashMap<String, String> parseConnect(String connect) {
		HashMap<String, String> connectMap = new HashMap<String, String>();
		String[] ar1 = connect.split(";");
		for (int i = 0; i < ar1.length; i++) {
			String[] ar2 = ar1[i].split("=");
			if (ar2.length >= 2) {
				connectMap.put(ar2[0].toUpperCase(), ar2[1]);
			}
		}
		return connectMap;
	}

	public void setDictionary(
			HashMap<String, HashMap<String, String>> dictionary) {
		this.dictionary = dictionary;
	}

	// ------------------------------------------------------------------------
	// フィールド名およびそれに対応した変換辞書の組
	// ------------------------------------------------------------------------
	public void dictionary_put(String key, HashMap<String, String> cnv) {
		// XXX keyがnullなら追加しない！！
		dictionary.put(key, cnv);
	}

	public HashMap<String, String> dictionary_get(String nam) {
		HashMap<String, String> cnv = dictionary.get(nam);
		if (cnv == null)
			cnv = new HashMap();
		return cnv;
	}

	public HashMap<String, HashMap<String, String>> getDictionary() {
		return dictionary;
	}

	// ------------------------------------------------------------------------
	// 当該テーブルの主キー
	// 例＞ setKey:XA1
	// ------------------------------------------------------------------------
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	// ------------------------------------------------------------------------
	// アクセッサ
	// ------------------------------------------------------------------------
	public void setSourceDir(String sourceDir) {
		splitteds = sourceDir.split("/");
		// 集計対象期間に合わせて末端を対象年度に変更したい
		// 末端の正規表現が1984以降2099年以下の正規表現に当てはまるかどうか
		// （[12]あとは3ケタの数字）で判定かな
		// ・・・・集計ロジックでこのオブジェクトをどんなふうに参照しているか確認する　参照時に当該年度をパラメータで渡す？
		String joined = kyPkg.util.KUtil.join(splitteds, "/") + "/";
		this.sourceDir = joined;
	}

	public String getSourceDir() {
		return sourceDir;
	}

	public void setFieldList(List<String> fieldList) {
		this.allFields = fieldList;
	}

	// ------------------------------------------------------------------------
	// keyと存在するすべてのフィールドを選択状態にする
	// ------------------------------------------------------------------------
	public void selectAllFields() {
		selectedFields = new ArrayList();
		selectedFields.clear();
		selectedFields.add(KEY_NAME);
		selectedFields.addAll(allFields);
	}

	// ------------------------------------------------------------------------
	// Debug
	// ------------------------------------------------------------------------
	public static void enumIt(String message, List<String> fieldList) {
		System.out.println("#debug# " + message);
		for (String element : fieldList) {
			System.out.println("@sOURCEoBJ    element:" + element);
		}
	}

	public void setFieldMap(HashMap<String, String> fieldMap) {
		this.fieldMap = fieldMap;
	}

	// ------------------------------------------------------------------------
	// 対象テーブルが持っている、全フィールド名リスト
	// ------------------------------------------------------------------------
	public void addAllFields(String nam) {
		allFields.add(nam);
	}

	// ------------------------------------------------------------------------
	// フィールドの（ラベル名⇒識別子）の組
	// ------------------------------------------------------------------------
	public void putFieldMap(String key, String val) {
		// XXX keyがnullなら追加しない！！
		// System.out.println("fieldMap_put >" + key + " >" + val);
		fieldMap.put(key, val);
	}

	// ------------------------------------------------------------------------
	// ラベル名⇒識別子（辞書引き）
	// ------------------------------------------------------------------------
	public String getField(String nam) {
		String fld = fieldMap.get(nam);
		if (fld == null) {
			return null;// スペースの方がよいだろうか？
		} else {
			return fld;
		}
	}

	public HashMap<String, String> getFieldMap() {
		return fieldMap;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public void setSelectedFields(List<String> selectedFields) {
		//TODO nullだったらどうする？
		this.selectedFields = selectedFields;
	}

	public void setSelectedFields(String[] array) {
		this.selectedFields = java.util.Arrays.asList(array);
	}

	@Override
	public List<String> getFieldList() {
		return allFields;
	}

	@Override
	public String getFields() {
		return concatFields(fieldMap, getSelectedFields());
	}

	@Override
	public List<String> getSelectedFields() {
		return selectedFields;
	}

	//-------------------------------------------------------------------------
	// 使用例＞saveSelectedFields("c:/selectedFields.txt")
	//-------------------------------------------------------------------------
	public void saveSelectedFields(String path) {
		kyPkg.uFile.ListArrayUtil.list2File(path, selectedFields);
	}

	//-------------------------------------------------------------------------
	// 使用例＞loadSelectedFields("c:/selectedFields.txt")
	//-------------------------------------------------------------------------
	public void loadSelectedFields(String path) {
		List list = kyPkg.uFile.ListArrayUtil.file2List(path);
		if (list == null)
			return;//file is not exists
		setSelectedFields(list);
	}

	//選択された項目名一覧からＤＢのフィールド名をコンカチしたものを返す
	private String concatFields(HashMap<String, String> fldMap,
			List<String> keyList) {
		if (keyList == null || keyList.size() == 0) {
			// System.out
			// .println("@concatFields........keyList == null || keyList.size() == 0");
			return "";
		}
		StringBuffer buf = new StringBuffer();
		for (String key : keyList) {
			String val = fldMap.get(key);
			// System.out.println("#20140917#  key:" + key + " ===> val:" +
			// val);
			//存在しないフィールドが指定されてもエラーとしない（メッセージは出力しようか？）
			if (val != null) {
				val = val.trim();
				if (!val.equals("")) {
					if (buf.length() > 0)
						buf.append(",");
					buf.append(val);
				}
			} else {
				//モニターＩＤ
				System.out.println(
						"#ERROR SourceObj@concatFields 指定されたフィールドは見つかりませんでした⇒"
								+ key);
			}
		}
		return buf.toString();
	}

	// ------------------------------------------------------------------------
	// for Debug
	// ------------------------------------------------------------------------
	public void enumurate() {
		for (String nam : getFieldList()) {
			System.out.println("nam:" + nam);
			String fld = getField(nam);
			String typ = typMap.get(nam);
			System.out.println("    fld:" + fld);
			System.out.println("    typ:" + typ);
			HashMap<String, String> cnv = dictionary_get(nam);
			List<String> keyList = new ArrayList(cnv.keySet());
			for (String element : keyList) {
				System.out.println("       element :" + element + " =>"
						+ cnv.get(element));
			}
		}
	}

	// ------------------------------------------------------------------------
	// QTBファイルを読み込んで・・・フィールド名と対応フィールドの変換辞書を生成する
	// path:Z:\S2\rx\enquetes\NQ\03_属性・性年代編\2014\Qtb1.TXT
	// monolith:
	// ※注意：fieldは一つのフィールド（一枚岩）を想定している、複数連結の場合ロジックを修正しなければならない
	// ※対象テーブルが（キー、ヴァリュー）の2つのフィールドでできていることを想定している
	// ------------------------------------------------------------------------
	public boolean qtb2HashMap(String path, String monolith) {
		// System.out.println("qtbPath：" + pPath);
		// System.out.println("monolith：" + monolith);
		int motCol = 1;
		int keyCol = 2;
		int valCol = 3;
		int namCol = 4;
		int maxCol = 5;
		int occCol = 6;
		int typCol = 7;
		int colCol = 8;
		int lenCol = 9;
		String delimiter = "\t";
		File49ers insF49 = new File49ers(path);
		if (!insF49.isExists()) {
			System.out
					.println("#err @qtb2HashMap file is not existed:" + path);
			return false;
		}
		// System.out.println("delimiter:" + insF49.getDelimiter());
		// System.out.println("maxColumn:" + insF49.getMaxColCount());
		delimiter = insF49.getDelimiter();
		String wRec;
		typMap.clear();
		key2nam.clear();

		// キーのラベル名をあらかじめ登録しておく・・・
		putFieldMap(KEY_NAME, key);
		try {
			BufferedReader br = FileUtil.getBufferedReader(path);
//			BufferedReader br = new BufferedReader(new FileReader(path));
			while (br.ready()) {
				wRec = br.readLine();
				if (wRec != null) {
					String[] array = wRec.split(delimiter);
					if (array.length > motCol) {
						String mot = array[motCol].trim();
						if (mot.equals("ROOT")) {
							// 親キーを表す行の場合
							String key = array[keyCol];// qtbのキーコード
							String val = array[valCol];// データ値
							String nam = array[namCol];// データ値が指す名称（設問名称）
							String typ = array[typCol];// アンサー（説も円）タイプ　SINGLE/MULTI
							String col = array[colCol];// 何カラム目からか
							String len = array[lenCol];// 長さ

							String fld = "SUBSTRING(" + alterName + "."
									+ monolith + "," + col + "," + len + ")";

							// fld：sqlで表現する回答の対象域
							if (typ.toUpperCase().equals("SINGLE")) {
								nam = nam.replaceAll("　", "");// 全角スペースを取り払う
								addAllFields(nam);// 対象フィールドの漢字名称

								// System.out.println("key:" + key + " val:" +
								// val
								// + " nam:" + nam + " typ:" + typ
								// + " fld:" + fld);

								putFieldMap(nam, fld);// 漢字名称：対応する、sqlフィールドの対
								key2nam.put(key, nam);// キーから名称の逆引き用
								typMap.put(nam, typ);// Multi or Single
							}
						} else {
							// 選択肢を表す行の場合
							String val = array[valCol];// データ値
							String nam = array[namCol];// データ値が指す名称（選択肢名称）
							String max = array[maxCol];// 最大
							String occ = array[occCol];// 繰り返し
							String typ = array[typCol];// タイプ
							String col = array[colCol];// 何カラム目からか
							String len = array[lenCol];// 長さ
							String motherName = key2nam.get(mot);
							HashMap<String, String> cnv = dictionary_get(
									motherName);
							nam = nam.replaceAll("　", "");// 全角スペースを取り払う
							// 値変換用辞書
							cnv.put(val, nam);
							if (motherName != null)
								dictionary_put(motherName, cnv);
						}
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return true;
	}

	// ------------------------------------------------------------------------
	// コンバーターの対応カラムに変換辞書を仕込んで返す
	// ------------------------------------------------------------------------
	@Override
	public CnvArray getConverter() {
		CnvArray cnv = new CnvArray("");// 変換辞書
		List<String> selectedFields = getSelectedFields();
		if (selectedFields != null) {
			HashMap<String, HashMap<String, String>> dicts = getDictionary();
			for (int col = 0; col < selectedFields.size(); col++) {
				HashMap<String, String> map = dicts
						.get(selectedFields.get(col));
				if (map != null)
					cnv.setDict(col, map);
			}
		}
		return cnv;
	}

	// @Override
	// abstract public void incore(String sourceDir, boolean initOpt);

	// ------------------------------------------------------------------------
	// 20141015
	// ------------------------------------------------------------------------
	public void incore(String sourceDir) {
		String jUrl = ServerConnecter.getDEF_JURL();
		String user = ServerConnecter.getDEF_USER();
		String pswd = ServerConnecter.getDEF_PASS();
		// --------------------------------------------------------------------
		// ALIAS_TXTを読み込み要素をハッシュマップに格納する
		// 例＞ aliasPath：　Z:/S2/rx/enquetes/NQ/03_属性・性年代編/2014/alias.txt
		// ※ファイルの内容は以下のようになっている
		// --------------------------------------------------------------------
		// connect<TAB>DRIVER={SQLServer};SERVER=xxxx;UID=xxx;PWD=xxx;DATABASE=xxx;Trusted_Connection=true
		// table<TAB>TABLE_NAME
		// field<TAB>XB1
		// key<TAB>XA1
		// Cond<TAB>
		// --------------------------------------------------------------------
		String aliasPath = sourceDir + ALIAS_TXT;
		// System.out.println("　　　　#debug20141015# aliasPath：" + aliasPath);
		HashMap<String, String> hMap = HashMapUtil.file2HashMapX(aliasPath);
		if (hMap != null) {
			// 20140922 ここでユーザー設定を上書きしているが・・・よいのか？疑問だ
			// ユーザー設定ではコネクトは持っていないと思う・・・ユーザー設定持たせた方がよいのではないか？考える（急きょ別ソースに変更された場合は現状の方が良い）
			//			System.out.println("TABLE:" + hMap.get(TABLE));
			//			System.out.println("KEY:" + hMap.get(KEY));
			//			System.out.println("CONNECT:" + hMap.get(CONNECT));
			//			System.out.println("FIELD:" + hMap.get(FIELD));
			setTable(hMap.get(TABLE));
			setKey(hMap.get(KEY));
			setConnect(hMap.get(CONNECT));
			String qtbPath = sourceDir + QTB1_TXT;
			// QTBファイルを読み込んで・・・フィールド名と対応フィールドの変換辞書を生成
			qtb2HashMap(qtbPath, hMap.get(FIELD));
		}
		//		System.out.println("#QTB_Source.incore end # ");
	}

	// @Override
	// public String getTable() {
	// return table;
	// }
	@Override
	public String getTable() {
		String dataBase = getDataBase();
		String server = getServer();

		//		if (!server.equals(ServerConnecter.QPRSERVER)) {
		if (!server.equalsIgnoreCase(ServerConnecter.CURRENT_SERVER)) {
			// 自サーバー以外なら　リンクサーバーのテーブルとみなす
			// リンクサーバーはあらかじめ定義しておかなければならない
			return server + "." + dataBase + ".dbo." + table;
		}
		return table;
	}

}