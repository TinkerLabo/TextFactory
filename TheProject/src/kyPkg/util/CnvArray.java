package kyPkg.util;

import static kyPkg.util.Joint.join;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kyPkg.converter.Inf_ArrayCnv;

//----------------------------------------------------------------------------
//	文字配列をカラム別に辞書変換する
//----------------------------------------------------------------------------
public class CnvArray implements Inf_ArrayCnv {
	private String notDef = "";
	private HashMap<Integer, HashMap<String, String>> converter = null;
	private String ans;

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public CnvArray(String notDef) {
		this.notDef = notDef;
		converter = new HashMap();
	}

	// ------------------------------------------------------------------------
	// 変換処理
	// ------------------------------------------------------------------------
	/*
	 * (非 Javadoc)
	 * 
	 * @see kyPkg.util.inf_ArrayCnv#convert(java.lang.String[])
	 */
	@Override
	public String[] convert(String[] rec, int stat) {
		List<Integer> keylist = new ArrayList(converter.keySet());
		for (Integer col : keylist) {
			if (rec.length > col) {
				String cel = rec[col];
				ans = converter.get(col).get(cel);
				if (ans != null) {
					rec[col] = ans;
				} else {
					if (notDef.equals("")) {
						rec[col] = "";
					} else {
						rec[col] = notDef + rec[col];
					}
				}
			}
		}
		return rec;
	}

	// ------------------------------------------------------------------------
	// カラムごとに変換辞書を登録する
	// ------------------------------------------------------------------------
	public void setDict(int col, HashMap<String, String> dict) {
		converter.put(new Integer(col), dict);
	}

	// ------------------------------------------------------------------------
	// 動作確認用
	// ------------------------------------------------------------------------
	public void debug(String[] rec) {
		String bef = join(rec, ",");
		String[] array = convert(rec, 0);
		String aft = join(array, ",");
		System.out.println(bef + " => " + aft);
	}

	public static void main(String[] argv) {
		test01();
	}

	public static void test01() {
		HashMap<String, String> dict0 = new HashMap();
		dict0.put("0", "♂");
		dict0.put("1", "♀");

		HashMap<String, String> dict1 = new HashMap();
		dict1.put("A", "1");
		dict1.put("B", "2");
		dict1.put("C", "3");
		dict1.put("D", "4");
		dict1.put("E", "5");
		dict1.put("F", "6");

		HashMap<String, String> dict2 = new HashMap();
		dict2.put("A", "い");
		dict2.put("B", "ろ");
		dict2.put("C", "は");
		dict2.put("D", "に");
		dict2.put("E", "ほ");
		dict2.put("F", "へ");

		CnvArray cnv = new CnvArray("NotDefined:");
		cnv.setDict(0, dict0);// カラム０に対して変換を行う
		cnv.setDict(1, dict1);// カラム１に対して変換を行う
		cnv.setDict(2, dict2);// カラム０に対して変換を行う

		cnv.debug("0,A,G".split(","));
		cnv.debug(new String[] { "0", "B", "A" });
		cnv.debug(new String[] { "0", "C", "B" });
		cnv.debug(new String[] { "1", "D", "C" });
		cnv.debug(new String[] { "0", "E", "D" });
		cnv.debug(new String[] { "1", "F", "E" });
		cnv.debug(new String[] { "0", "G", "F" });

	}

	public static void test02() {
		// 変換辞書を用意する
		HashMap<String, String> dict0 = new HashMap();
		dict0.put("0", "♂");
		dict0.put("1", "♀");

		CnvArray cnv = new CnvArray("NotDefined:");
		cnv.setDict(0, dict0);// カラム０に対して変換を登録する

		// ループ内などで変換する
		String[] rec = new String[] { "0", "B", "A" };

		if (cnv != null)
			rec = cnv.convert(rec, 0);

	}

	@Override
	public void init() {
	}

	@Override
	public void fin() {
	}
//
	//	selectedFields	モニターＩＤ,購入日付,購入時間,JanCode,購入金額Ｘ数量,購入金額,購入数量,購入先（業態）,購入先（詳細）,アイテム名称,マスタ価格,品目,品目名,モニター種別,世帯主,世帯特性…主婦年代,世帯主年代,世帯特性…家族人数,子供（１８才以下）の同居有無,乳幼児の同居有無,小学生（低学年）の同居有無,小学生（高学年の同居有無,中学生の同居有無,高校生の同居有無,老人の同居有無,住居形態,個人年収,世帯年収,年度年齢,性別,未既婚,世帯特性…自身の子供の有無,職業,学生種別,性・年代（１０歳区分）,性・年代（５歳区分）,性・年代（メディア区分）,年代（１０歳区分）,年代（５歳区分）,年代（メディア区分）,住居（都道府県）,住居（エリア）,職業タイプ
//	sourceDir	Z:/S2/rx/enquetes/NQ//03_属性・性年代編/2016/

	
}
