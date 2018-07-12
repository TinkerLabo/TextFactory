package kyPkg.converter;

import java.text.DecimalFormat;
import java.util.*;

public class UnitCnv {

	private static UnitCnv unitCnv;
	private DecimalFormat df;

	// ----------------------------------------------------------------
	public UnitCnv() {
		df = new DecimalFormat("########.###");
		incore();
	}

	public static UnitCnv getInstance() {
		if (unitCnv == null) {
			unitCnv = new UnitCnv();
		}
		return unitCnv;
	}

	// ----------------------------------------------------------------
	// ＱＰＲ版 convert 単位の割戻し処理
	// 引数：変換先単位コード、変換元単位コード、変換する値
	// 《使用例》 ※"001"はｇ、"002"は㎏
	// UnitCnv wUnitCnv = null;
	// if (wUnitCnv==null) wUnitCnv = new UnitCnv();
	// wUnitCnv.convert("002","001", 30);
	// ----------------------------------------------------------------
	Object[][] data = { { "000", "？", new Integer(1000) },
			{ "001", "g", new Integer(1000) },
			{ "002", "kg", new Integer(1000000) },
			{ "003", "mg", new Integer(1) },
			{ "004", "pnd", new Integer(373240) },
			{ "005", "oz", new Integer(31103) },
			{ "101", "ml", new Integer(1) }, { "102", "L", new Integer(1000) },
			{ "103", "kL", new Integer(1000000) },
			{ "104", "㏄", new Integer(1) },
			{ "105", "ｶﾞﾛﾝ  ", new Integer(3785) },
			{ "106", "CM3", new Integer(1) },
			{ "107", "M3", new Integer(1000000) },
			{ "201", "cm", new Integer(1000) },
			{ "202", "m", new Integer(100000) },
			{ "203", "km", new Integer(100000000) },
			{ "204", "mm", new Integer(100) },
			{ "205", "ﾔ-ﾄﾞ", new Integer(91440) },
			{ "206", "ｲﾝﾁ", new Integer(2540) },
			{ "207", "ﾌｲ-ﾄ", new Integer(30480) },
			{ "301", "CM2", new Integer(1000) },
			{ "302", "M2", new Integer(10000000) },
			{ "303", "MM2", new Integer(10) },
			{ "399", "標準", new Integer(1000) },
			{ "501", "枚", new Integer(1000) },
			{ "502", "個", new Integer(1000) },
			{ "503", "本", new Integer(1000) },
			{ "504", "冊", new Integer(1000) },
			{ "505", "頁", new Integer(1000) },
			{ "506", "束", new Integer(1000) },
			{ "507", "袋", new Integer(1000) },
			{ "508", "粒", new Integer(1000) },
			{ "509", "錠", new Integer(1000) },
			{ "510", "巻", new Integer(1000) },
			{ "511", "包", new Integer(1000) },
			{ "512", "組", new Integer(1000) },
			{ "513", "箱", new Integer(1000) },
			{ "514", "台", new Integer(1000) },
			{ "515", "丁", new Integer(1000) },
			{ "516", "足", new Integer(1000) },
			{ "517", "ｶﾌﾟｾﾙ", new Integer(1000) },
			{ "518", "膳", new Integer(1000) },
			{ "519", "人前", new Integer(1000) },
			{ "520", "食", new Integer(1000) },
			{ "521", "斤", new Integer(1000) },
			{ "522", "反", new Integer(1000) },
			{ "523", "かせ", new Integer(1000) },
			{ "524", "双", new Integer(1000) },
			{ "525", "帖", new Integer(1000) },
			{ "526", "切", new Integer(1000) },
			{ "527", "株", new Integer(1000) },
			{ "528", "ﾀﾞｰｽ", new Integer(1000) },
			{ "529", "片", new Integer(1000) } };
	HashMap hashCodeNam = new java.util.HashMap();
	HashMap hashCodeVal = new java.util.HashMap();

	// ----------------------------------------------------------------
	// convert 単位の割戻し処理
	// 引数：変換先単位コード、変換元単位コード、変換する値
	// 《使用例》 ※"001"はｇ、"002"は㎏
	// UnitCnv wUnitCnv = null;
	// if (wUnitCnv==null) wUnitCnv = new UnitCnv();
	// wUnitCnv.convert("002","001", 30);
	// ----------------------------------------------------------------
	public float convertPlus(String unitTo, String unitFrom, int unitVal) {
		//		System.out.println("@UnitCnv.unitTo To:" + unitTo + " From:" + unitFrom + " val:"
		//				+ unitVal);
		float capa = convert(unitTo, unitFrom, unitVal);
		// ---------------------------------------------
		// DB上の値は*1000となっているので・・割る必要がある
		// ---------------------------------------------
		if (capa > 0) {
			capa = capa / 1000.0f;
		} else {
			capa = 0;
		}
		//		System.out.println("=> 容量:" + capa);
		return capa;
	}

	public float convert(String unitTo, String unitFrom, int unitVal) {
		float iRtn = -1;// original 変換不能なら負の値
		// float iRtn = 0;//20130730 変換不能ならｚｅｒｏ
		String wPrefix1 = unitTo.substring(0, 1);
		String wPrefix2 = unitFrom.substring(0, 1);
		if (wPrefix1.equals(wPrefix2)) {
			if (unitVal != 0) {
				int wVal1 = getVal(unitTo);
				int wVal2 = getVal(unitFrom);
				iRtn = (float) (Math.round(unitVal * wVal2 * 100.0 / wVal1)
						/ 100.0);
			}
			// System.out.println("■" + iRtn + getName(pUnitTo) + " ← " + pValue
			// + " " + getName(pUnitFrom));
		}
		return iRtn;
	}

	public String convert(String pUnitTo, String pUnitFrom, String cel) {
		float f = 0;
		cel = cel.trim();
		if (cel.equals("")) {
			cel = "0";
		}
		int iVal = Integer.valueOf(cel);
		if (iVal > 0) {
			f = convert(pUnitTo, pUnitFrom, iVal) / 1000;
		}
		if (f >= 0.0) {
			// あとで単位記号と分離する場合があるのでスペースを挟んでいる
//			return df.format(f) + ".0~ " + getName(pUnitTo);//20170314 +"★" 
			return df.format(f) + " " + getName(pUnitTo);//20170314 +"★" 
		} else {
			if(!pUnitFrom.trim().equals("")){
				//#createTester--------------------------------------------------
				//				System.out.println("public static void testconvert() {");
				//				System.out.println("    String pUnitTo = \"" + pUnitTo + "\";");
				//				System.out.println("    String pUnitFrom = \"" + pUnitFrom + "\";");
				//				System.out.println("    String cel = \"" + cel + "\";");
				//				System.out.println(
				//						"    convert ins = new convert(pUnitTo,pUnitFrom,cel);");
				//				System.out.println("}");
				//--------------------------------------------------
			}
			return "NotDefined (" + cel + ")";//20170124
		}
	}

	// ----------------------------------------------------------------
	// incore 割戻しテーブルをハッシュテーブル化
	// ----------------------------------------------------------------
	public void incore() {
		// System.out.println("### UnitCnv incore ###");
		for (int i = 0; i < data.length; i++) {
			if (data[i].length == 3) {
				// System.out.println("## " + data[i][1]);
				hashCodeNam.put(data[i][0], data[i][2]);
				hashCodeVal.put(data[i][0], data[i][1]);
			}
		}
	}

	// ----------------------------------------------------------------
	// 割戻し値をハッシュテーブルから取り出す
	// ----------------------------------------------------------------
	public int getVal(String pKey) {
		int wRtn = -1;
		Object wObj = hashCodeNam.get(pKey);
		if (wObj != null) {
			if (wObj instanceof Integer) {
				wRtn = ((Integer) wObj).intValue();
			}
		}
		return wRtn;
	}

	// ----------------------------------------------------------------
	// 単位名をハッシュテーブルから取り出す
	// ----------------------------------------------------------------
	public String getName(String pKey) {
		if (pKey == null) {
			return null;
		}
		pKey = pKey.trim();
		if (!pKey.equals("")) {
			Object wObj = hashCodeVal.get(pKey);
			if (wObj != null) {
				if (wObj instanceof String) {
					return wObj.toString();
				}
			}
		}
		return "";
	}

	// ----------------------------------------------------------------
	public static void main(String[] argv) {
		UnitCnv wUnitCnv = new UnitCnv();
		wUnitCnv.convert("002", "001", 3);
		wUnitCnv.convert("002", "001", 30);
		wUnitCnv.convert("002", "001", 3000);
		wUnitCnv.convert("001", "002", 3);
	}

}
