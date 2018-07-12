package kyPkg.uFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import globals.ResControl;
import kyPkg.uDateTime.DateCalc;

//QTBファイルをベースにフィールド定義、値⇒ラベル変換辞書の生成を行う
public class Tran_SourceObj extends SourceObj {
	private String defaultSourceDir;
	// ------------------------------------------------------------------------
	// XXX　sourceDirが実際に存在するか、そしてqtbとかaliasが存在するかどうか確認する
	// ------------------------------------------------------------------------
	// FIXME WHOLE = "全体";
	//	public static final String WHOLE = "全体";
	// public static final int WHOLE_COL = -1; // 全体（漫然と全体を指すのに使用）
	// ------------------------------------------------------------------------
	private static final String SOURCE_DIR = "sourceDir";
	private static final String SELECTED_FIELDS = "selectedFields";
	private static final String PREVIOUS_SETTING_PATH = ResControl
			.getQPRHomeQprRes("defaultFields.txt");
	// ------------------------------------------------------------------------
	public static final String WEEK = "曜日";
	public static final String TIME30 = "30分刻み";
	private static final String FLAG2 = "Flag2";
	private static final String FLAG1 = "Flag1";
	private static final String FLAG3 = "Flag3";
	// ------------------------------------------------------------------------
	public static final String MONITORID = "モニターＩＤ";
	public static final String MBEF = "開始";
	public static final String MAFT = "終了";
	public static final String TRAN_YMD = "購入日付";
	public static final String TRAN_HOUR = "購入時間";
	public static final String TRAN_PRICE = "購入金額";
	public static final String TRAN_PRICExCOUNT = "購入金額Ｘ数量";
	public static final String TRAN_COUNT = "購入数量";
	public static final String CHANNEL_L = "購入先（業態）";
	public static final String CHANNEL_S = "購入先（詳細）";
	// ------------------------------------------------------------------------
	public static final String JAN_CODE = "JanCode";
	public static final String JAN_CODEX = "JANコード";
	public static final String JAN_NAME = "アイテム名称";
	public static final String JAN_PRICE = "マスタ価格";
	public static final String JAN_KANA = "カナ名称";
	// ------------------------------------------------------------------------
	public static final String CATEGORY = "品目";
	public static final String CATEGORY_NAME = "品目名";
	public static final String MAKER = "メーカー";
	public static final String MAKER_NAME = "メーカー名";
	public static final String SAL_YMD = "発売日";
	public static final String REG_YMD = "登録日";
	public static final String UPD_YMD = "更新日";
	// ------------------------------------------------------------------------
	public static final String SHAPE = "容器形態";
	public static final String STD1 = "規格（単位）";
	public static final String STD2 = "規格（容量）";
	public static final String UNIT = "容量";
	// ------------------------------------------------------------------------
	//V_ITEM_Daoでフィールドとの対応を併せておく
	public static final String[] JAN_NAME_CODE_KANA_MAKER_CATEGORY = new String[] { JAN_NAME,
			JAN_CODEX, JAN_KANA ,MAKER,CATEGORY};
//	public static final String[] JAN_NAME_CODE_KANA = new String[] { JAN_NAME,
//			JAN_CODEX, JAN_KANA };

	private static String[] pattern2 = new String[] { MONITORID,
			TRAN_PRICExCOUNT, TRAN_PRICE, TRAN_COUNT, JAN_CODE, CHANNEL_S,
			TRAN_YMD, FLAG3, FLAG1, FLAG2, CHANNEL_L, "YM", TRAN_HOUR, TIME30,
			WEEK };

	public static String[] getHeadPattern2() {
		return pattern2;
	}

	public void setBrowseMode() {
		super.setSelectedFields(pattern2);
	}



	// ------------------------------------------------------------------------
	// 表示名によるフィールド一覧（順序制御のためリストとなっている）
	// ------------------------------------------------------------------------
	// FIXME ここらへんに　【全体】機能を挿入したい・・・
	private void setDefaultFieldList() {
		ArrayList list = new ArrayList();
		list.add(MONITORID);// 0
		list.add(TRAN_YMD);// 1
		list.add(TRAN_HOUR);// 2
		list.add(JAN_CODE);// 3
		list.add(TRAN_PRICExCOUNT);// 4
		list.add(TRAN_PRICE);// 4
		list.add(TRAN_COUNT);// 5
		list.add(CHANNEL_L);// 6
		list.add(CHANNEL_S);// 6
		list.add(JAN_NAME);// 7
		list.add(JAN_PRICE);// 8
		list.add(CATEGORY);// 9
		list.add(CATEGORY_NAME);
		super.setFieldList(list);
	}

	public List<String> getDefaultList() {
		List list = new ArrayList();
		list.add(MONITORID);
		list.add(TRAN_YMD);
		list.add(TRAN_HOUR);
		list.add(JAN_CODE);
		list.add(TRAN_PRICExCOUNT);
		list.add(TRAN_PRICE);
		list.add(TRAN_COUNT);
		// list.add(CHANNEL_L);
		list.add(CHANNEL_S);
		list.add(JAN_NAME);
		list.add(JAN_PRICE);
		list.add(CATEGORY);
		return list;
	}

	// ------------------------------------------------------------------------
	// 表示名とデータベース内でのフィールド名とデータベースの識別フィールドの対応表
	// ------------------------------------------------------------------------
	private void setDefaultFieldMap() {
		HashMap hmap = new HashMap();
		hmap.put(MONITORID, "TRN.Monitor");
		hmap.put(TRAN_YMD, "TRN.Accept");
		hmap.put(TRAN_HOUR, "TRN.hh as Hour");
		hmap.put(JAN_CODE, "'#'+TRN.JanCode");
		hmap.put(TRAN_PRICExCOUNT, "TRN.Price * TRN.Count");
		hmap.put(TRAN_PRICE, "TRN.Price");
		hmap.put(TRAN_COUNT, "TRN.Count");
		hmap.put(CHANNEL_L, "TRN.Shop2 as shop2");
		hmap.put(CHANNEL_S, "TRN.Shop1 as shop1");
		hmap.put(JAN_NAME, "ITM.NAME");
		hmap.put(JAN_PRICE, "ITM.PRICE");
		hmap.put(CATEGORY, "ITM.CAT");
		hmap.put(CATEGORY_NAME, "CATEGORY.XB1");
		super.setFieldMap(hmap);
	}

	// ------------------------------------------------------------------------
	// 表示名とその列の値に対する変換辞書の対応
	// ------------------------------------------------------------------------
	private void setDefaultDictionary() {
		// ------------------------------------------------------------------------
		// System.out.println("#<<Base_Source.setDictionary()>>#");
		// resDir=>./qpr/res/
		// ※検索用タグ：購入先名称、チャネル名称
		// ------------------------------------------------------------------------
		String resDir = globals.ResControl.getQPRHomeQprRes("");
		HashMap<String, String> channelDic2 = HashMapUtil.file2HashMap(resDir + "channel.dic",
				1);
		HashMap dict = new HashMap();
		dict.put(CHANNEL_L, channelDic2);
		dict.put(CHANNEL_S, channelDic2);
		dict.put(WEEK, HashMapUtil.file2HashMap(resDir + WEEK + ".txt", 1));
		dict.put(TIME30, HashMapUtil.file2HashMap(resDir + "30分刻み.txt", 1));
		super.setDictionary(dict);
	}

	// ------------------------------------------------------------------------
	// 属性フィールドの一覧をユーザー設定ファイルから読み込む
	// DEFAULT_FIELDS："c:/@qpr/qpr/res/defaultFields.txt"
	// ------------------------------------------------------------------------
	private void previousSetting(String path) {
		System.out.println("#################################################");
		System.out.println("## previousSetting:" + path);
		System.out.println("#################################################");
		HashMapObj hmapObj = new HashMapObj(path);
		//---------------------------------------------------------------------
		//　PREVIOUS_SETTING_PATH => C:/@qpr/home/qpr/res/defaultFields.txt    の内容の例↓
		//	selectedFields	モニターＩＤ,購入日付,購入時間,JanCode,購入金額Ｘ数量,購入金額,購入数量,購入先（業態）,購入先（詳細）,アイテム名称,マスタ価格,品目,品目名,モニター種別,世帯主,世帯特性…主婦年代,世帯主年代,世帯特性…家族人数,子供（１８才以下）の同居有無,乳幼児の同居有無,小学生（低学年）の同居有無,小学生（高学年の同居有無,中学生の同居有無,高校生の同居有無,老人の同居有無,住居形態,個人年収,世帯年収,年度年齢,性別,未既婚,世帯特性…自身の子供の有無,職業,学生種別,性・年代（１０歳区分）,性・年代（５歳区分）,性・年代（メディア区分）,年代（１０歳区分）,年代（５歳区分）,年代（メディア区分）,住居（都道府県）,住居（エリア）,職業タイプ
		//	sourceDir	Z:/S2/rx/enquetes/NQ//03_属性・性年代編/2016/
		//---------------------------------------------------------------------
		// 設定ファイルのSOURCE_DIRがnullならデフォルトソースのパスをSOURCE_DIRとする
		//---------------------------------------------------------------------
		setSourceDir(hmapObj.getByString(SOURCE_DIR, defaultSourceDir));
		//---------------------------------------------------------------------
		// 設定ファイルのSELECTED_FIELDSがnullならデフォルトフィールドを設定
		//---------------------------------------------------------------------
		setSelectedFields(hmapObj.getByList(SELECTED_FIELDS, getDefaultList()));
		System.out.println("##<previousSettings end>##");
	}

	public void saveSettings(List<String> fields, String sourceDir) {
		System.out.println("#################################################");
		System.out.println("## saveSettings");
		System.out.println("#################################################");
		HashMapObj hmapObj = new HashMapObj();
		hmapObj.put(SOURCE_DIR, sourceDir);
		// listがnullならデフォルトのリストを設定する
		if (fields == null)
			fields = getDefaultList();
		hmapObj.set(SELECTED_FIELDS, fields);
		// hashMapをファイルに保存？
		hmapObj.save(PREVIOUS_SETTING_PATH);
	}

	// ------------------------------------------------------------------------
	// コンストラクタ(初回および前回の続き版)
	// ------------------------------------------------------------------------
	public Tran_SourceObj() {
		System.out.println("<< Base_Source コンストラクタ >>");
		defaultSourceDir = ResControl.getQtbDir( DateCalc.getThisYear());//ex=> Z:/S2/rx/enquetes/NQ//03_属性・性年代編/2016/
		previousSetting(PREVIOUS_SETTING_PATH);
		setDefault();
		incore(getSourceDir());// 20140922
	}

	public void setDefault() {
		System.out.println(" << setDefault() >>");
		setDefaultFieldList();
		setDefaultFieldMap();
		setDefaultDictionary();
	}

	// XXX 20140922 Src_Buyでコールしている・・・どんな場合に必要なのか検証しておく
	public void loadPreSettings() {
		System.out.println("#debug20140922#<< loadPreSettings() >>#");
		defaultSourceDir = super.getSourceDir();
		previousSetting(PREVIOUS_SETTING_PATH);
	}

}
