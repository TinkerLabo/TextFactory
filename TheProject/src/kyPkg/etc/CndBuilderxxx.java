package kyPkg.etc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//20170719  区分名称でアイテム限定できるようにする為のヴァージョン
public class CndBuilderxxx {
	// ------------------------------------------------------------------------
	public static final String CONTAINS = "が含まれる";
	private static final String BEGINWITH = "で始まる";
	private static final String ENDWITH = "で終わる";
	public static final String EQUALS = "と一致する";
	public static final String NOT_CONTAINS = "を含まない";
	public static final String NOT_EQUALS = "と一致しない";
	public static final String[] CONDITIONS = new String[] { CONTAINS,
			BEGINWITH, EQUALS, NOT_CONTAINS, NOT_EQUALS };

	public static String getPattern() {
		return "(" + CndBuilderxxx.CONTAINS + "|" + CndBuilderxxx.ENDWITH + "|"
				+ CndBuilderxxx.BEGINWITH + "|" + CndBuilderxxx.EQUALS + ")";
	}

	// ------------------------------------------------------------------------
	public static final String AND = "1";
	public static final String OR = "0";
	// ------------------------------------------------------------------------
	private String prefix = "";
	private HashMap<String, String> dictionary = null;

	// ------------------------------------------------------------------------
	// コンストラクタ
	// ------------------------------------------------------------------------
	public CndBuilderxxx(String debug, String prefix,
			HashMap<String, String> dictionary) {
		super();
		//		System.out.println("#DEBUG#CndBuilder：" + debug);
		prefix = prefix.trim();
		if (!prefix.equals("")) {
			prefix = prefix + ".";
		}
		this.prefix = prefix;
		this.dictionary = dictionary;
	}

	public static HashMap<String, String> array2hashMapU(String[] array,
			String delimiter) {
		// key部分は大文字に変換します。
		HashMap<String, String> dictionary = new HashMap();
		for (int i = 0; i < array.length; i++) {
			String[] splited = array[i].split(delimiter);
			if (splited.length > 1) {
				dictionary.put(splited[0].toUpperCase(), splited[1]);
			}
		}
		return dictionary;
	}

	public String getCondJan(List<String> condList1, List<String> condList2,
			String rel1, String rel2, String rel3, boolean debug) {
		if (condList1 == null)
			condList1 = new ArrayList(); //20170718
		if (condList2 == null)
			condList2 = new ArrayList(); //20170718
		if (condList1.size() == 0 && condList2.size() == 0)
			return "";
		return getCond(condList1, condList2, rel1, rel2, rel3);
	}

	public String getCond(List<String> condList1, List<String> condList2,
			String rel1, String rel2, String rel3) {
		// dictionary => フィールドとDB上のフィールド名の対
		// ここで辞書に入れるものがおかしいのｶﾓしれない
		String xWhere = "";
		String xWhere1 = parseIt(rel1, condList1);
		String xWhere2 = parseIt(rel2, condList2);
		if ((!xWhere1.equals("")) && (!xWhere2.equals(""))) {
			if (rel3.equals(AND)) {
				xWhere = "(" + xWhere1 + ") AND (" + xWhere2 + ")";
			} else {
				xWhere = "(" + xWhere1 + ") OR (" + xWhere2 + ")";
			}
		} else {
			xWhere = xWhere1 + xWhere2;
		}
		return xWhere;
	}

	private String parseIt(String relType, List<String> condList) {
		if (condList == null || condList.size() == 0)
			return "";
		String xWhere = "";
		String relation = " OR ";
		if (relType.equals(AND))
			relation = " AND ";
		for (String element : condList) {
			//TODO	作りたい条件文
			//kMK IN (SELECT MK.kCode FROM MK where( kName Like '%アイ%' )) 

			String[] array = element.split("\t");
			if (array != null && array.length >= 3 && !array[1].equals("")) {
				String fieldTyp = array[0];
				String value = array[1];
				String ope = array[2];
//				System.out.println("fieldTyp:" + fieldTyp);
//				System.out.println("value:" + value);
//				System.out.println("ope:" + ope);

				//ここを修正
				//				String field = dictionary.get(fieldTyp);
				//				fieldTyp　アイテム名、アイテム、メーカー、メーカー名
				if (fieldTyp.startsWith("アイテム")) {
					prefix = "JAN.";
				}
				if (fieldTyp.startsWith("メーカー")) {
					prefix = "MK.";
				}
				if (fieldTyp.startsWith("区分１")) {
					prefix = "K1.";
				}
				if (fieldTyp.startsWith("区分２")) {
					prefix = "K2.";
				}
				if (fieldTyp.startsWith("区分３")) {
					prefix = "K3.";
				}
				if (fieldTyp.startsWith("区分４")) {
					prefix = "K4.";
				}
				if (fieldTyp.startsWith("区分５")) {
					prefix = "K5.";
				}
				if (fieldTyp.startsWith("区分６")) {
					prefix = "K6.";
				}

				//				fieldTyp　アイテム名、アイテム、メーカー、メーカー名
				String field = "kCode";
				if (fieldTyp.endsWith("名")) {
					field = "kName";
				}

				if (field == null) {
					// ここで辞書引きできていない・・なぜか？
					System.out.println(
							"#ERROR# fields Not Found=>" + fieldTyp + "<=");
					System.out.println("#=> 以下の選択肢に含まれていなければならない");
					List<String> keyList = new ArrayList(dictionary.keySet());
					for (String key : keyList) {
						System.out.println("# field:" + key);
					}
				}
				field = prefix + field;
				String condition = parseCond(field, value, ope);

				if (fieldTyp.startsWith("アイテム")) {
					condition = "JAN.kCode IN (SELECT JAN.kCode FROM JAN where( "
							+ condition + " )) ";
				}
				if (fieldTyp.startsWith("メーカー")) {
					condition = "JAN.kMK   IN (SELECT MK.kCode  FROM MK  where( "
							+ condition + " )) ";
				}
				if (fieldTyp.startsWith("区分１")) {
					condition = "JAN.kK1   IN (SELECT K1.kCode  FROM K1  where( "
							+ condition + " )) ";
				}
				if (fieldTyp.startsWith("区分２")) {
					condition = "JAN.kK2   IN (SELECT K2.kCode  FROM K2  where( "
							+ condition + " )) ";
				}
				if (fieldTyp.startsWith("区分３")) {
					condition = "JAN.kK3   IN (SELECT K3.kCode  FROM K3  where( "
							+ condition + " )) ";
				}
				if (fieldTyp.startsWith("区分４")) {
					condition = "JAN.kK4   IN (SELECT K4.kCode  FROM K4  where( "
							+ condition + " )) ";
				}
				if (fieldTyp.startsWith("区分５")) {
					condition = "JAN.kK5   IN (SELECT K5.kCode  FROM K5  where( "
							+ condition + " )) ";
				}
				if (fieldTyp.startsWith("区分６")) {
					condition = "JAN.kK6   IN (SELECT K6.kCode  FROM K6  where( "
							+ condition + " )) ";
				}

//				System.out.println("condition:" + condition);

				if (!condition.equals("")) {
					if (!xWhere.equals(""))
						xWhere += relation;
					xWhere += "(" + condition + ")";
				}
			}
		}
		//		System.out.println("xWhere:" + xWhere);
		return xWhere;
	}

	// -------------------------------------------------------------------------
	// リストには”NAME_アサヒ_1”のような形式の値がはいる
	// => t1.kName like '%アサヒ%'
	// -------------------------------------------------------------------------
	// TODO 20150515 opeのGUI項目とここでの判定が一致しない可能性があるので修正する
	private String parseCond(String field, String value, String ope) {
		//	System.out.println("■parseCond■　field>>" + field + " value>>" + value
		//	+ " ope>>" + ope);
		value = kyPkg.rez.Normalizer.removeInjection(value);
		String condition = "";
		if (field != null && !field.equals("")) {
			if (ope.equals("")) {
			} else if (ope.equals("1")) {
				condition = field + " like '%" + value + "%'";// が含まれる
			} else if (ope.equals("2")) {
				condition = field + " like '" + value + "%'";// で始まる
			} else if (ope.equals("3")) {
				condition = field + " = '" + value + "'";// と一致する
			} else if (ope.equals("4")) {
				condition = field + " not like '%" + value + "%'";// が含まれない
			} else if (ope.equals("5")) {
				condition = field + " <> '" + value + "'";// と一致しない
			} else if (ope.equals(CONTAINS)) {
				condition = field + " like '%" + value + "%'";// が含まれる
			} else if (ope.equals(BEGINWITH)) {
				condition = field + " like '" + value + "%'";// で始まる
			} else if (ope.equals(EQUALS)) {
				condition = field + " = '" + value + "'";// と一致する
			} else if (ope.equals(NOT_CONTAINS)) {
				condition = field + " not like '%" + value + "%'";// が含まれない
			} else if (ope.equals(NOT_EQUALS)) {
				condition = field + " <> '" + value + "'";// と一致しない
			}
		}
		//		System.out.println("■　parseCond　ope:" + ope + ">>:" + condition);
		return condition;
	}
}
