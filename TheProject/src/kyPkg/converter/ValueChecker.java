package kyPkg.converter;

import java.text.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import kyPkg.uCodecs.CharConv;

//kyPkg.util.ValueChecker
public class ValueChecker {
	private static final String ID_SEVEN = "7"; //モニターIDの先頭 //TODO monitorID7
	private static Pattern ptnSP = null;
	private static Pattern ptnZERO = null;
	private static Pattern ptnKJSPC = null;
	private static Pattern ptnDate000 = null;
	private static Pattern ptnDate001 = null;
	private static Pattern ptnDate002 = null;
	private static Pattern ptnDate003 = null;
	private static CharConv charConv = null;

	// 固定長半角に変換する (2013-07-09)
	public static String cnvToNarrow(String cel, int len) {
		if (charConv == null)
			charConv = CharConv.getInstance();
		cel = charConv.cnvNarrow(cel);
		return CharConv.fixStr(cel, len);
	}

	// 固定長全角に変換する (2013-07-09)
	public static String cnvToWide(String cel, int len) {
		if (charConv == null)
			charConv = CharConv.getInstance();
		cel = CharConv.fixStr(cel, len);
		cel = charConv.cnvWide(cel);
		return charConv.cnvCKJ(cel);// debug　ホスト漢字処理・・・とほほ
	}

	// 連続する空白文字
	public static boolean isSPC(String str) {
		if (ptnSP == null)
			ptnSP = Pattern.compile("[\\s+]");
		return (ptnSP.matcher(str).matches());
	}

	// 連続する0
	//Converter.ValueChecker.isZERO(data)
	public static boolean isZERO(String str) {
		if (ptnZERO == null)
			ptnZERO = Pattern.compile("[0]+");
		return (ptnZERO.matcher(str).matches());
	}

	// 連続する漢字空白文字
	public static boolean isKJSPC(String str) {
		if (ptnKJSPC == null)
			ptnKJSPC = Pattern.compile("[　]+");
		return (ptnKJSPC.matcher(str).matches());
	}

	//isDate000(data)
	private static boolean isDate000(String str) {
		if (ptnDate000 == null)
			ptnDate000 = Pattern.compile("[0-9]{8}+.+");
		return (ptnDate000.matcher(str).matches());
	}

	private static boolean isDate001(String str) {
		if (ptnDate001 == null)
			ptnDate001 = Pattern.compile("[0-9]+/[0-9]+/[0-9]+.+");
		return (ptnDate001.matcher(str).matches());
	}

	private static boolean isDate002(String str) {
		if (ptnDate002 == null)
			ptnDate002 = Pattern.compile("[0-9]+\\.[0-9]+\\.[0-9]+.+");
		return (ptnDate002.matcher(str).matches());
	}

	private static boolean isDate003(String str) {
		if (ptnDate003 == null)
			ptnDate003 = Pattern.compile("[0-9]+-[0-9]+-[0-9]+.+");
		return (ptnDate003.matcher(str).matches());
	}

	// -------------------------------------------------------------------------
	// マルチアンサーをフラグ形式に変換する
	// String val 検査対象文字列
	// int maxLen 出力文字列長
	// String delim 区切り文字
	// char defChar　　フラグがたたない場合の文字
	// -------------------------------------------------------------------------
	// 使用例
	// String ans = EnqChk.cnvMultiAns("'1','3','5','7','09','10','11',a,,",10);
	// System.out.println("■ans =>"+ans);
	// -------------------------------------------------------------------------
	public static String cnvMultiAns(String val, int width) {
		return cnvMultiAns(val, width, ",");
	}

	public static String cnvMultiAns2(String val, int width) {
		// 20121102
		return "\"" + cnvMultiAns(val, width, ",") + "\"";
	}

	private static String cnvMultiAns(String val, int width, String delim) {
		if (width <= 0)
			return "Multi cnv Length Error";
		val = val.replaceAll("\'", "");
		val = val.replaceAll("\"", "");
		val = val.trim();
		// System.out.println("maxLen:" + maxLen);
		// System.out.println("cnvMultiAns in :" + val);
		String[] array = val.split(delim);
		return cnvMultiAns(width, array);
	}

	private static String cnvMultiAns(int width, String[] array) {
		char defChar = ' ';
		char[] cArray = new char[width];
		for (int i = 0; i < width; i++) {
			cArray[i] = defChar;
		}
		int idx = -1;
		for (int i = 0; i < array.length; i++) {
			if (!array[i].equals("")) {
				try {
					// System.out.print(">>" + array[i]);
					idx = Integer.parseInt(array[i]);
					if (idx > 0 && idx <= cArray.length) {
						// System.out.println("ok>>"+(idx-1));
						cArray[idx - 1] = '1';
					} else {
						// System.out.println("NG!!");
					}
				} catch (NumberFormatException e) {
					System.out.println("@cnvMultiAns　NumberFormatException!!>"
							+ array[i] + "<");
				}
			}
		}
		// String wDebug = new String(rtn);
		// System.out.println("cnvMultiAns out:" + wDebug);
		return new String(cArray);
	}

	// -------------------------------------------------------------------------
	// 使用例
	// int width=10;
	// List<Integer> list= new ArrayList();
	// String ans = EnqChk.cnvMultiAns(width,list);
	// System.out.println("■ans =>"+ans);
	// -------------------------------------------------------------------------
	public static String cnvMultiAns(int width, HashSet<Integer> set) {
		char defChar = ' ';
		char[] cArray = new char[width];
		for (int i = 0; i < width; i++) {
			cArray[i] = defChar;
		}
		for (Integer idx : set) {
			if (idx != null) {
				if (idx > 0 && idx <= cArray.length) {
					cArray[idx - 1] = '1';
				} else {
					// System.out.println("NG!!");
				}
			}

		}
		return new String(cArray);
	}

	// -------------------------------------------------------------------------
	// 半角スペースが２文字連続している場合、全角スペース一文字に変換する
	// ※指定位置より前に半角が混在する漢字エリアがある場合、使用不可！
	// ※スペースが奇数個の場合はズレるはず・・・
	// ■注意■開始位置は０から始まる！（substringの仕様にあわせた・・・）
	// String pStr1 =
	// "12345678１２３４５６７８９０１２３４５2999000001001 001                    E";
	// String pStr2 =
	// "ARI     猫の飼育匹数区分　　　　　　　2999000001001 001                    E";
	// String pStr3 =
	// "ARI1    ５匹以上                  　　X                                    E";
	// String pStr4 =
	// "12345678                              X                                    E";
	// String pStr5 =
	// "ARI     猫 の 飼 育 匹 数 区 分 　　　2999000001001 001                    E";
	// wAns = EnqChk.spKjCnv(pStr1,8,15);
	// System.out.println("■spKjCnv1=>"+wAns+"<=");
	// wAns = EnqChk.spKjCnv(pStr2,8,15);
	// System.out.println("■spKjCnv2=>"+wAns+"<=");
	// wAns = EnqChk.spKjCnv(pStr3,8,15);
	// System.out.println("■spKjCnv3=>"+wAns+"<=");
	// wAns = EnqChk.spKjCnv(pStr4,8,15);
	// System.out.println("■spKjCnv4=>"+wAns+"<=");
	// wAns = EnqChk.spKjCnv(pStr5,8,15);
	// System.out.println("■spKjCnv5=>"+wAns+"<=");
	// -------------------------------------------------------------------------
	public static String spKjCnv(String pStr, int pPos, int pLen) {
		if (pStr.length() < (pPos + pLen)) {
			System.out.println("parameterがデータサイズを超えています");
			return "";
		}
		// ----------------------------------------------------------------------
		int wCnv = 0; // 変換した文字数
		int wCnt = 0; // 半角カウンタ
		// pPos = pPos - 1;
		char[] cSrc = pStr.toCharArray(); // キャラクター配列にする
		char[] cDes = new char[pStr.length()]; // 出力バッファ
		for (int i = 0; i < pPos; i++) {
			cDes[i] = cSrc[i];
		} // 直前まで
		for (int i = pPos, j = pPos; (i < cSrc.length); i++) {
			if (pLen > 0) {
				if (cSrc[i] == ' ') {
					wCnt++;
					if (wCnt == 2) {
						cDes[j] = '　';
						pLen--;
						j++;
						wCnv++;
						wCnt = 0;
					}
				} else {
					cDes[j] = cSrc[i];
					pLen--;
					j++;
				}
			} else {
				cDes[j] = cSrc[i];
				j++;
			}
		}
		String wAns = new String(cDes, 0, (pStr.length() - wCnv));
		return wAns;
	}

	// ------------------------------------------------------------------------------
	// 右詰めに文字列をコピーする（元の文字列、出力文字数）
	// 《使用例》
	// wAns = EnqChk.fixNum("0001",9);
	// System.out.println("■fixNum 0001,9=>"+wAns);
	// wAns = EnqChk.fixNum("0002",8);
	// System.out.println("■fixNum 0002,8=>"+wAns);
	// wAns = EnqChk.fixNum("7777",7);
	// System.out.println("■fixNum 7777,7=>"+wAns);
	// wAns = EnqChk.fixNum("9876",6);
	// System.out.println("■fixNum 9876,6=>"+wAns);
	// wAns = EnqChk.fixNum("8765",5);
	// System.out.println("■fixNum 8765,5=>"+wAns);
	// wAns = EnqChk.fixNum("7654",4);
	// System.out.println("■fixNum 7654,4=>"+wAns);
	// wAns = EnqChk.fixNum("6543",3);
	// System.out.println("■fixNum 6543,3=>"+wAns);
	// wAns = EnqChk.fixNum("5432",2);
	// System.out.println("■fixNum 5432,2=>"+wAns);
	// wAns = EnqChk.fixNum("4321",1);
	// System.out.println("■fixNum 4321,1=>"+wAns);
	// 0001,9=>000000001
	// 0002,8=>00000002
	// 7777,7=>0007777
	// 9876,6=>009876
	// 8765,5=>08765
	// 7654,4=>7654
	// 6543,3=>543
	// 5432,2=>32
	// 4321,1=>1
	// ------------------------------------------------------------------------------
	public static String fixNum(int val, int pLen) {
		return fixNum(String.valueOf(val), pLen);
	}

	public static String fixNum(String pStr, int pLen) {
		char wStf = '0';
		return CharConv.fixRight(pStr.trim(), pLen, wStf);
	}

	// ------------------------------------------------------------------------------
	// 固定長のsetに変更する
	// ------------------------------------------------------------------------------
	public static Set<String> cnv2FixSet(Set<String> set, int len) {
		Set<String> newSet = new HashSet();
		for (String element : set) {
			element = CharConv.fixStr(element, len);
			newSet.add(element);
		}
		return newSet;
	}

	public static Set<String> cnv2TrimSet(Set<String> set) {
		Set<String> newSet = new HashSet();
		for (String element : set) {
			element = element.trim();
			newSet.add(element);
		}
		return newSet;
	}

	// ------------------------------------------------------------------------------
	// 文字列がレジックスパターンにマッチしたら指定された長さに加工して返す
	// param レジックスパターン,長さ・・・不可なら空文字を返す
	// 《使用例》
	// パターンが"\\d+"の場合のみ右寄せ,それ以外左寄せ
	// wAns = EnqChk.regFix("123","\\d+",5);
	// System.out.println("■123,\\d+,5          =>"+wAns);
	// wAns = EnqChk.regFix("Ab1","[A-Za-z0-9]+",8);
	// System.out.println("■Ab1,[A-Za-z0-9]+,8 =>"+wAns);
	// wAns = EnqChk.regFix("49011101","\\s*[0-9]+\\s*",13);
	// System.out.println("■49011101,\\s*[0-9]+\\s*,13 =>"+wAns);
	// ------------------------------------------------------------------------------
	public static String regFix(String pStr, String pReg, int pLen) {
		String wRtn = "";
		if (pStr.matches(pReg)) {
			if (pReg.equals("\\d+")) {
				wRtn = CharConv.fixRight(pStr.trim(), pLen, '0');
			} else {
				wRtn = CharConv.fixLeft(pStr.trim(), pLen, ' ');
			}
		}
		return wRtn;
	}

	// ------------------------------------------------------------------------------
	// アンケートの猫に関する質問用の処理
	// 飼育数（２桁）を受け取り・・・
	// 存在フラグx、飼育数区分y、飼育数z → xyzzの４桁固定で返す
	// 《使用例》
	// wAns = EnqChk.catCnv("  "); System.out.println("■catCnv SP=>"+wAns);
	// wAns = EnqChk.catCnv("01"); System.out.println("■catCnv 01=>"+wAns);
	// wAns = EnqChk.catCnv("02"); System.out.println("■catCnv 02=>"+wAns);
	// wAns = EnqChk.catCnv("03"); System.out.println("■catCnv 03=>"+wAns);
	// wAns = EnqChk.catCnv("04"); System.out.println("■catCnv 04=>"+wAns);
	// wAns = EnqChk.catCnv("05"); System.out.println("■catCnv 05=>"+wAns);
	// wAns = EnqChk.catCnv("06"); System.out.println("■catCnv 06=>"+wAns);
	// wAns = EnqChk.catCnv("07"); System.out.println("■catCnv 07=>"+wAns);
	// ■catCnv SP=> 600
	// ■catCnv 01=>1501
	// ■catCnv 02=>1402
	// ■catCnv 03=>1303
	// ■catCnv 04=>1204
	// ■catCnv 05=>1105
	// ■catCnv 06=>1106
	// ■catCnv 07=>1107
	// ------------------------------------------------------------------------------
	public static String catCnv2(String pVal, String delim) {
		String wExist = " ";
		String wClass = " ";
		String wCount = "00";
		int wCls = 6;
		try {
			int wInt = Integer.parseInt(pVal);
			if (wInt <= 0) {
				wCls = 6;
			} else if (wInt >= 5) {
				wCls = 1;
			} else if (5 > wInt) {
				wCls = 6 - wInt;
			}
		} catch (NumberFormatException ne) {
		}
		if (wCls != 6) {
			wExist = "1";
		} else {
			wExist = " ";
		}
		wCount = fixNum(pVal, 2); // 右詰２文字化
		wClass = fixNum(Integer.toString(wCls), 1);
		return (wExist + delim + wClass + delim + wCount);
	}

	public static String catCnv(String pVal) {
		String wExist = " ";
		String wClass = " ";
		String wCount = "";
		int wCls = 6;
		try {
			int wInt = Integer.parseInt(pVal);
			if (wInt <= 0) {
				wCls = 6;
			} else if (wInt >= 5) {
				wCls = 1;
			} else if (5 > wInt) {
				wCls = 6 - wInt;
			}
		} catch (NumberFormatException ne) {
		}
		if (wCls != 6) {
			wExist = "1";
		} else {
			wExist = " ";
		}
		wCount = fixNum(pVal, 2); // 右詰２文字化
		wClass = fixNum(Integer.toString(wCls), 1);
		return (wExist + wClass + wCount);
	}

	// ------------------------------------------------------------------------------
	// アンケートの犬に関する質問用の処理
	// 飼育数（２桁）を受け取り・・・
	// 存在フラグx、飼育数区分y、飼育数z → xyzzの４桁固定で返す
	// 《使用例》
	// wAns = EnqChk.dogCnv("  "); System.out.println("■dogCnv SP=>"+wAns);
	// wAns = EnqChk.dogCnv("01"); System.out.println("■dogCnv 01=>"+wAns);
	// wAns = EnqChk.dogCnv("02"); System.out.println("■dogCnv 02=>"+wAns);
	// wAns = EnqChk.dogCnv("03"); System.out.println("■dogCnv 03=>"+wAns);
	// wAns = EnqChk.dogCnv("04"); System.out.println("■dogCnv 04=>"+wAns);
	// wAns = EnqChk.dogCnv("05"); System.out.println("■dogCnv 05=>"+wAns);
	// wAns = EnqChk.dogCnv("06"); System.out.println("■dogCnv 06=>"+wAns);
	// wAns = EnqChk.dogCnv("07"); System.out.println("■dogCnv 07=>"+wAns);
	// ■dogCnv SP=> 500
	// ■dogCnv 01=>1401
	// ■dogCnv 02=>1302
	// ■dogCnv 03=>1203
	// ■dogCnv 04=>1104
	// ■dogCnv 05=>1105
	// ■dogCnv 06=>1106
	// ■dogCnv 07=>1107
	// ------------------------------------------------------------------------------
	public static String dogCnv(String pVal) {
		String wExist = " ";
		String wClass = " ";
		String wCount = "";
		int wCls = 5;
		try {
			int wInt = Integer.parseInt(pVal);
			if (wInt <= 0) {
				wCls = 5;
			} else if (wInt >= 4) {
				wCls = 1;
			} else if (4 > wInt) {
				wCls = 5 - wInt;
			}
		} catch (NumberFormatException ne) {
		}
		if (wCls != 5) {
			wExist = "1";
		} else {
			wExist = " ";
		}
		wCount = fixNum(pVal, 2); // 右詰２文字化
		wClass = fixNum(Integer.toString(wCls), 1);
		return (wExist + wClass + wCount);
	}

	// ------------------------------------------------------------------------------
	// 文字列を前ゼロ数字文字列として返す
	// 《使用例》
	// wAns = num("  0  0 2 0  56"); System.out.println("■num =>"+wAns);
	// >00000000002056
	// ------------------------------------------------------------------------------
	public static String num(String pStr) {
		// System.out.println("■in:"+pStr);
		int wVal = 0;
		char[] cSrc = pStr.toCharArray(); // キャラクター配列にする
		for (int i = 0; i < cSrc.length; i++) {
			if (Character.isDigit(cSrc[i])) {
				wVal = wVal * 10 + Character.getNumericValue(cSrc[i]);
			}
			cSrc[i] = '0';
		}
		String wFmt = new String(cSrc);
		DecimalFormat df = new DecimalFormat(wFmt);
		String wAns = df.format(wVal);
		return wAns;
	}

	// ------------------------------------------------------------------------------
	// 文字列の変換 01から26→AからZ
	// pStr 入力文字列
	// 《使用例》
	// String wAns = EnqChk.xcx("11");
	// >K
	// ------------------------------------------------------------------------------
	public static String xcx(String pXX) {
		String wAns = " ";
		if (pXX.equals("01")) {
			wAns = "A";
		} else if (pXX.equals("02")) {
			wAns = "B";
		} else if (pXX.equals("03")) {
			wAns = "C";
		} else if (pXX.equals("04")) {
			wAns = "D";
		} else if (pXX.equals("05")) {
			wAns = "E";
		} else if (pXX.equals("06")) {
			wAns = "F";
		} else if (pXX.equals("07")) {
			wAns = "G";
		} else if (pXX.equals("08")) {
			wAns = "H";
		} else if (pXX.equals("09")) {
			wAns = "I";
		} else if (pXX.equals("10")) {
			wAns = "J";
		} else if (pXX.equals("11")) {
			wAns = "K";
		} else if (pXX.equals("12")) {
			wAns = "L";
		} else if (pXX.equals("13")) {
			wAns = "M";
		} else if (pXX.equals("14")) {
			wAns = "N";
		} else if (pXX.equals("15")) {
			wAns = "O";
		} else if (pXX.equals("16")) {
			wAns = "P";
		} else if (pXX.equals("17")) {
			wAns = "Q";
		} else if (pXX.equals("18")) {
			wAns = "R";
		} else if (pXX.equals("19")) {
			wAns = "S";
		} else if (pXX.equals("20")) {
			wAns = "T";
		} else if (pXX.equals("21")) {
			wAns = "U";
		} else if (pXX.equals("22")) {
			wAns = "V";
		} else if (pXX.equals("23")) {
			wAns = "W";
		} else if (pXX.equals("24")) {
			wAns = "X";
		} else if (pXX.equals("25")) {
			wAns = "Y";
		} else if (pXX.equals("26")) {
			wAns = "Z";
		}
		return wAns;
	}

	// ------------------------------------------------------------------------------
	// 選択された値の該当箇所にフラグを立てて指定された固定長さで返す(#TSX #TSL)
	// pStr 入力文字列
	// pLen 出力長
	// 《使用例》※マルチアンサー項目に使用する
	// wAns = tsl("1356",10);
	// wAns = tsl(pStr,20); System.out.println("■wAns:"+wAns);
	// wAns = tsl("abcdefghij",20); System.out.println("■wAns:"+wAns);
	// wAns = tsl("bdfhj",20); System.out.println("■wAns:"+wAns);
	// String wAns = EnqChk.tsl("1356",10);
	// >1 1 11
	// ------------------------------------------------------------------------------
	public static String tsl(String pStr, int pLen) {
		// System.out.println("■in:"+pStr);
		int wIdx = 0;
		pStr = pStr.toUpperCase();
		char[] cSrc = pStr.toCharArray(); // キャラクター配列にする
		char[] cDes = new char[pLen];
		for (int i = 0; i < cDes.length; i++)
			cDes[i] = ' ';
		for (int i = 0; i < cSrc.length; i++) {
			switch (cSrc[i]) {
			case ('1'): {
				wIdx = 1;
				break;
			}
			case ('2'): {
				wIdx = 2;
				break;
			}
			case ('3'): {
				wIdx = 3;
				break;
			}
			case ('4'): {
				wIdx = 4;
				break;
			}
			case ('5'): {
				wIdx = 5;
				break;
			}
			case ('6'): {
				wIdx = 6;
				break;
			}
			case ('7'): {
				wIdx = 7;
				break;
			}
			case ('8'): {
				wIdx = 8;
				break;
			}
			case ('9'): {
				wIdx = 9;
				break;
			}
			case ('0'): {
				wIdx = 10;
				break;
			}
			case ('A'): {
				wIdx = 11;
				break;
			}
			case ('B'): {
				wIdx = 12;
				break;
			}
			case ('C'): {
				wIdx = 13;
				break;
			}
			case ('D'): {
				wIdx = 14;
				break;
			}
			case ('E'): {
				wIdx = 15;
				break;
			}
			case ('F'): {
				wIdx = 16;
				break;
			}
			case ('G'): {
				wIdx = 17;
				break;
			}
			case ('H'): {
				wIdx = 18;
				break;
			}
			case ('I'): {
				wIdx = 19;
				break;
			}
			case ('J'): {
				wIdx = 20;
				break;
			}
			default: {
				wIdx = 0;
				break;
			}
			}
			if (wIdx > 0 && cDes.length >= wIdx)
				cDes[wIdx - 1] = '1';
		}
		String wAns = new String(cDes);
		/*-----------------------------------------------------------
		byte[] bArray = wAns.getBytes();          // バイト配列にする
		for(int i =0;i< bArray.length;i++){
			System.out.print("_"+bArray[i]);
		}
		------------------------------------------------------------*/
		return wAns;
	}

	// ------------------------------------------------------------------------------
	// 選択された値の該当箇所に文字を配置する(#anm)
	// pStr 入力文字列
	// pLen 出力長
	// 《使用例》
	// String wAns = anm("ACEF",10);
	// >A C EF
	// ------------------------------------------------------------------------------
	public static String anm(String pStr, int pLen) {
		// System.out.println("■in:"+pStr);
		int wIdx = 0;
		pStr = pStr.toUpperCase();
		char[] cSrc = pStr.toCharArray(); // キャラクター配列にする
		char[] cDes = new char[pLen];
		for (int i = 0; i < cDes.length; i++)
			cDes[i] = ' ';
		for (int i = 0; i < cSrc.length; i++) {
			switch (cSrc[i]) {
			case ('A'): {
				wIdx = 1;
				break;
			}
			case ('B'): {
				wIdx = 2;
				break;
			}
			case ('C'): {
				wIdx = 3;
				break;
			}
			case ('D'): {
				wIdx = 4;
				break;
			}
			case ('E'): {
				wIdx = 5;
				break;
			}
			case ('F'): {
				wIdx = 6;
				break;
			}
			case ('G'): {
				wIdx = 7;
				break;
			}
			case ('H'): {
				wIdx = 8;
				break;
			}
			case ('I'): {
				wIdx = 9;
				break;
			}
			case ('J'): {
				wIdx = 10;
				break;
			}
			case ('K'): {
				wIdx = 11;
				break;
			}
			case ('L'): {
				wIdx = 12;
				break;
			}
			case ('M'): {
				wIdx = 13;
				break;
			}
			case ('N'): {
				wIdx = 14;
				break;
			}
			default: {
				wIdx = 0;
				break;
			}
			}
			if (wIdx > 0 && cDes.length >= wIdx)
				cDes[wIdx - 1] = cSrc[i];
		}
		String wAns = new String(cDes);
		/*-----------------------------------------------------------
		byte[] bArray = wAns.getBytes();          // バイト配列にする
		for(int i =0;i< bArray.length;i++){
			System.out.print("_"+bArray[i]);
		}
		------------------------------------------------------------*/
		return wAns;
	}

	// ------------------------------------------------------------------------------
	// 文字列中の、第２パラメータにあたる文字に第３パラメータに置く
	// （ ※ SUBSTR(pStr,pIdx,1)= pChar のようなイメージで使用・・・）
	// 《使用例》
	// wAns = overLay("##########","03",'X');
	// System.out.println("■overLay 03=>"+wAns);
	// wAns = overLay("##########","04",'X');
	// System.out.println("■overLay 04=>"+wAns);
	// ------------------------------------------------------------------------------
	public static String overLay(String pStr, String pIdx, char pChar) {
		char[] cSrc = pStr.toCharArray();
		try {
			int wIdx = Integer.parseInt(pIdx);
			if (wIdx > 0 && cSrc.length >= wIdx)
				cSrc[wIdx - 1] = pChar;
		} catch (NumberFormatException ne) {
		}
		return new String(cSrc);
	}

	// ------------------------------------------------------------------------------
	// 選択された値の該当箇所に文字を配置する(#translate)
	// pStr 入力文字列
	// pLen 出力長
	// 《使用例》
	// String wAns = translate("OHDA","IJKLMNOPQRST",
	// "ABCDEFGHHGFE");
	// >OPLI
	// 《動作検証》
	// char[] cArr = "1234567890ABC".toCharArray(); // キャラクター配列にする
	// for(int q = 0;q<cArr.length;q++){
	// String incC = String.valueOf(cArr[q]);
	// String incCX = EnqChk.translate(incC,"ABCDEFGHIJKLM",
	// "1234567890ABC");
	// System.out.println("■translate "+incC+"=>"+incCX);
	// }
	// ------------------------------------------------------------------------------
	public static String translate(String pStr, String pAft, String pBef) {
		char[] cBef = pBef.toCharArray();
		char[] cAft = pAft.toCharArray();
		char[] cSrc = pStr.toCharArray();
		int wMax = cBef.length;
		if (wMax > cAft.length)
			wMax = cAft.length;
		outerL: for (int i = 0; i < cSrc.length; i++) {
			for (int j = 0; j < wMax; j++) {
				if (cSrc[i] == cBef[j]) {
					cSrc[i] = cAft[j];
					continue outerL; // 内側のループのみブレーク
				}
			}
		}
		String wAns = new String(cSrc);
		// System.out.println("translate■out:"+wAns);
		/*-----------------------------------------------------------
		byte[] bArray = wAns.getBytes();          // バイト配列にする
		for(int i =0;i< bArray.length;i++){
			System.out.print("_"+bArray[i]);
		}
		------------------------------------------------------------*/
		return wAns;
	}

	// ------------------------------------------------------------------------------
	// 文字列中の、第２パラメータにあたる文字に第３パラメータに置く
	// （ ※ SUBSTR(pStr,pIdx,1)= pChar のようなイメージで使用・・・）
	// 《使用例》
	// String wAns = EnqChk.classify("1",6);
	// wAns = EnqChk.classify("01",6);
	// System.out.println("■classify 01=>"+wAns);
	// wAns = EnqChk.classify("02",6);
	// System.out.println("■classify 02=>"+wAns);
	// wAns = EnqChk.classify("03",6);
	// System.out.println("■classify 03=>"+wAns);
	// ------------------------------------------------------------------------------
	public static String classify(String pVal, int pMax) {
		int wRtn = pMax; // 未回答ならmaxを返す
		try {
			int wIdx = Integer.parseInt(pVal);
			if (wIdx <= 0) {
				wRtn = pMax;
			} else if (wIdx >= pMax) {
				wRtn = 1;
			} else if (pMax > wIdx) {
				wRtn = pMax - wIdx;
			}
		} catch (NumberFormatException ne) {
		}
		return Integer.toString(wRtn);
	}

	// ------------------------------------------------------------------------------
	// 海外旅行回数を区分コードに変更する
	// 《使用例》※前ゼロ変換編集済みであることが前提
	// String wQ = "";
	// for(int i =0;i<12;i++){
	// wQ = "0"+ i;
	// wAns = EnqChk.classifyTravel(wQ); System.out.println("■"+wQ+" =>"+wAns);;
	// }
	// ------------------------------------------------------------------------------
	public static String classifyTravel(String pVal) {
		String wRtn = " ";
		try {
			int wVal = Integer.parseInt(pVal);
			if (wVal >= 10) {
				wRtn = "J";
			} else if (wVal >= 9) {
				wRtn = "I";
			} else if (wVal >= 8) {
				wRtn = "H";
			} else if (wVal >= 7) {
				wRtn = "G";
			} else if (wVal >= 6) {
				wRtn = "F";
			} else if (wVal >= 5) {
				wRtn = "E";
			} else if (wVal >= 4) {
				wRtn = "D";
			} else if (wVal >= 3) {
				wRtn = "C";
			} else if (wVal >= 2) {
				wRtn = "B";
			} else if (wVal >= 1) {
				wRtn = "A";
			} else {
				wRtn = " ";
			}
		} catch (NumberFormatException ne) {
		}
		return wRtn;
	}

	// ------------------------------------------------------------------------------
	// 主婦かどうか
	// ------------------------------------------------------------------------------
	// public static String isHousewife(String pMarried,String pGender,String
	// pMainPurchase){
	// String wRtn = "0";
	// if (pMarried.equals("2")){
	// if (pGender.equals("2")){
	// if (pMainPurchase.equals("1")){
	// wRtn = "1";
	// }
	// }
	// }
	// return wRtn;
	// }
	// ------------------------------------------------------------------------------
	// モニター種別(単身世帯・主婦・単身＆主婦以外に振り分ける)
	// ------------------------------------------------------------------------------
	// 性別(pGender)
	// 1 男性
	// 2 女性
	// ------------------------------------------------------------------------------
	// 未既婚(pMarried)
	// 1 未婚
	// 2 既婚
	// 3 離死別
	// ------------------------------------------------------------------------------
	public static String getMonitorType(String memberCount, String pGender,
			String pMarried, String pMainPurchase) {
		String wRtn = "0"; // その他
		if (memberCount.equals("1")) {
			wRtn = "1"; // 単身世帯
		} else {
			if (pGender.equals("2")) {
				if (pMarried.equals("2") || pMarried.equals("3")) {
					if (pMainPurchase.equals("1")) {
						wRtn = "2"; // 主婦
					}
				}
			}
		}
		return wRtn;
	}

	// ------------------------------------------------------------------------------
	// 年齢を区分コードに変換する
	// 《使用例》※前ゼロ変換編集済みであることが前提
	// String wQ = "";
	// for(int i =0;i<99;i++){
	// wQ = "0"+ i;
	// wAns = EnqChk.classifyOld(wQ); System.out.println("■"+wQ+" =>"+wAns);
	// }
	// ------------------------------------------------------------------------------
	public static String classifyOld(String wVal) {
		String wRtn = "   ";
		String wKB1 = " ";
		String wKB2 = "  ";
		try {
			int wOld = Integer.parseInt(wVal);
			if (wOld > 59) {
				wKB1 = "6";
			} else if (wOld > 49) {
				wKB1 = "5";
			} else if (wOld > 39) {
				wKB1 = "4";
			} else if (wOld > 29) {
				wKB1 = "3";
			} else if (wOld > 19) {
				wKB1 = "2";
			} else if (wOld > 9) {
				wKB1 = "1";
			} else if (wOld > 0) {
				wKB1 = "0";
			} else {
				wKB1 = "0";
			}
			if (wOld > 59) {
				wKB2 = "10";
			} else if (wOld > 49) {
				wKB2 = "09";
			} else if (wOld > 39) {
				wKB2 = "08";
			} else if (wOld > 29) {
				wKB2 = "07";
			} else if (wOld > 22) {
				wKB2 = "06";
			} else if (wOld > 17) {
				wKB2 = "05";
			} else if (wOld > 14) {
				wKB2 = "04";
			} else if (wOld > 11) {
				wKB2 = "03";
			} else if (wOld > 5) {
				wKB2 = "02";
			} else if (wOld > 0) {
				wKB2 = "01";
			} else {
				wKB2 = "00";
			}
			wRtn = wKB1 + wKB2;
		} catch (NumberFormatException ne) {
		}
		return wRtn;
	}

	public static String classifyOld1(String wVal) {
		String wKB1 = " ";
		try {
			int wOld = Integer.parseInt(wVal);
			if (wOld > 59) {
				wKB1 = "6";
			} else if (wOld > 49) {
				wKB1 = "5";
			} else if (wOld > 39) {
				wKB1 = "4";
			} else if (wOld > 29) {
				wKB1 = "3";
			} else if (wOld > 19) {
				wKB1 = "2";
			} else if (wOld > 9) {
				wKB1 = "1";
			} else if (wOld > 0) {
				wKB1 = "0";
			} else {
				wKB1 = "0";
			}
		} catch (NumberFormatException ne) {
		}
		return wKB1;
	}

	public static String classifyOld2(String wVal) {
		String wKB2 = "  ";
		try {
			int wOld = Integer.parseInt(wVal);
			if (wOld > 59) {
				wKB2 = "10";
			} else if (wOld > 49) {
				wKB2 = "09";
			} else if (wOld > 39) {
				wKB2 = "08";
			} else if (wOld > 29) {
				wKB2 = "07";
			} else if (wOld > 22) {
				wKB2 = "06";
			} else if (wOld > 17) {
				wKB2 = "05";
			} else if (wOld > 14) {
				wKB2 = "04";
			} else if (wOld > 11) {
				wKB2 = "03";
			} else if (wOld > 5) {
				wKB2 = "02";
			} else if (wOld > 0) {
				wKB2 = "01";
			} else {
				wKB2 = "00";
			}
		} catch (NumberFormatException ne) {
		}
		return wKB2;
	}

	// ------------------------------------------------------------------------------
	// 年齢を区分コードに変換する	=>	年代（10歳区分）
	// 10代_20代_30代_40代_50代_60代
	// ------------------------------------------------------------------------------
	public static String getOLD_10(int wOld) {
		String wKB1 = " ";
		if (wOld > 59) {
			wKB1 = "6";
		} else if (wOld > 49) {
			wKB1 = "5";
		} else if (wOld > 39) {
			wKB1 = "4";
		} else if (wOld > 29) {
			wKB1 = "3";
		} else if (wOld > 19) {
			wKB1 = "2";
		} else if (wOld > 9) {
			wKB1 = "1";
		} else if (wOld > 0) {
			wKB1 = "0";
		} else {
			wKB1 = "0";
		}
		return wKB1;
	}

	// ------------------------------------------------------------------------------
	// 年齢を区分コードに変換する	=>	年代（5歳区分）
	// 15..才_20..才_25..才_30..才_35..才_40..才_45..才_50..才_55..才_60..才_65..才
	// ------------------------------------------------------------------------------
	public static String getOLD_5(int wOld) {
		String wKB2 = " ";
		if (wOld >= 65) {
			wKB2 = "11";
		} else if (wOld >= 60) {
			wKB2 = "10";
		} else if (wOld >= 55) {
			wKB2 = "09";
		} else if (wOld >= 50) {
			wKB2 = "08";
		} else if (wOld >= 45) {
			wKB2 = "07";
		} else if (wOld >= 40) {
			wKB2 = "06";
		} else if (wOld >= 35) {
			wKB2 = "05";
		} else if (wOld >= 30) {
			wKB2 = "04";
		} else if (wOld >= 25) {
			wKB2 = "03";
		} else if (wOld >= 20) {
			wKB2 = "02";
		} else if (wOld >= 15) {
			wKB2 = "01";
		} else {
			wKB2 = "00";
		}
		return wKB2;
	}
	
	// ------------------------------------------------------------------------------
	// 年度年齢コード(マクロミルスタイル)
	// ------------------------------------------------------------------------------
	public static String getNendoOLD(int wOld) {
		String wKB2 = " ";
		if (wOld >= 60) {
			wKB2 = "11";
		} else if (wOld >= 55) {
			wKB2 = "10";
		} else if (wOld >= 50) {
			wKB2 = "09";
		} else if (wOld >= 45) {
			wKB2 = "08";
		} else if (wOld >= 40) {
			wKB2 = "07";
		} else if (wOld >= 35) {
			wKB2 = "06";
		} else if (wOld >= 30) {
			wKB2 = "05";
		} else if (wOld >= 25) {
			wKB2 = "04";
		} else if (wOld >= 20) {
			wKB2 = "03";
		} else if (wOld >= 12) {
			wKB2 = "02";
		} else {
			wKB2 = "01";
		}
		return wKB2;
	}

	// ------------------------------------------------------------------------------
	// 年齢を区分コードに変換する	=>	年代（メディア区分）
	// 15..才_20..才_35..才_50才以上
	// ------------------------------------------------------------------------------
	public static String getOLD_MEDIA(int wOld) {
		String wKB3 = " ";
		if (wOld >= 50) {
			wKB3 = "4";
		} else if (wOld >= 35) {
			wKB3 = "3";
		} else if (wOld >= 20) {
			wKB3 = "2";
		} else if (wOld >= 15) {
			wKB3 = "1";
		} else {
			wKB3 = "0";
		}
		return wKB3;
	}

	// ------------------------------------------------------------------------------
	// 年齢を区分コードに変換する
	// 年代（10歳区切り大人版）
	// 20代_30代_40代_50代_60代
	// ------------------------------------------------------------------------------
	public static String classifyAdultOld(String val) {
		String wRtn = "0";
		try {
			int wOld = Integer.parseInt(val);
			if (wOld >= 60) {
				wRtn = "6";
			} else if (wOld >= 50) {
				wRtn = "5";
			} else if (wOld >= 40) {
				wRtn = "4";
			} else if (wOld >= 30) {
				wRtn = "3";
			} else if (wOld >= 0) {
				wRtn = "2";
			} else {
				wRtn = "0";
			}
		} catch (NumberFormatException ne) {
		}
		return wRtn;
	}

	// ------------------------------------------------------------------------------
	// 文字列が正規化パターンにマッチしたらpMatch値を、それ以外ならpUnMatch値を返す。
	// 《使用例》　パラメータ　[123],1,0 1or2or3=> 1 other => 0
	// System.out.println("■ans regcheknCnv0=>"+kyPkg.util.EnqChk.regcheknCnv("0","[123]","1","0"));
	// System.out.println("■ans regcheknCnv1=>"+kyPkg.util.EnqChk.regcheknCnv("1","[123]","1","0"));
	// System.out.println("■ans regcheknCnv2=>"+kyPkg.util.EnqChk.regcheknCnv("2","[123]","1","0"));
	// System.out.println("■ans regcheknCnv2=>"+kyPkg.util.EnqChk.regcheknCnv("3","[123]","1","0"));
	// System.out.println("■ans regcheknCnv2=>"+kyPkg.util.EnqChk.regcheknCnv("4","[123]","1","0"));
	// ------------------------------------------------------------------------------
	public static String regcheknCnv(String pStr, String pReg, String pMatch,
			String pUnMatch) {
		return pStr.matches(pReg) ? pMatch : pUnMatch; // 三項条件演算子
	}

	// ------------------------------------------------------------------------------
	// 構成人数を区分コードに変換する
	// ------------------------------------------------------------------------------
	public static String classifyNumberOfPeople(String wVal) {
		String wNP = " ";
		try {
			int wCnt = Integer.parseInt(wVal);
			if (wCnt >= 5) {
				wNP = "5";
			} else if (wCnt >= 4) {
				wNP = "4";
			} else if (wCnt >= 3) {
				wNP = "3";
			} else if (wCnt >= 2) {
				wNP = "2";
			} else {
				wNP = "1";
			}
		} catch (NumberFormatException ne) {
		}
		return wNP;
	}

	// ------------------------------------------------------------------------------
	// 年齢を区分コードに変換する
	// ------------------------------------------------------------------------------
	// 性・年代（10歳区分）
	// 　01 　男１０代　　　　　　　　　　　　　　　　
	// 　02 　男２０代　　　　　　　　　　　　　　　　
	// 　03 　男３０代　　　　　　　　　　　　　　　　
	// 　04 　男４０代　　　　　　　　　　　　　　　　
	// 　05 　男５０代　　　　　　　　　　　　　　　　
	// 　06 　男６０代　　　　　　　　　　　　　　　　
	// 　07 　女１０代　　　　　　　　　　　　　　　　
	// 　08 　女２０代　　　　　　　　　　　　　　　　
	// 　09 　女３０代　　　　　　　　　　　　　　　　
	// 　10 　女４０代　　　　　　　　　　　　　　　　
	// 　11 　女５０代　　　　　　　　　　　　　　　　
	// 　12 　女６０代　　　　　　　　　　　　　　　　
	// ------------------------------------------------------------------------------
	// 性・年代（5歳区分）
	// 　01 　男１５から１９才　　　　　　　　　　　　　
	// 　02 　男２０から２４才　　　　　　　　　　　　　
	// 　03 　男２５から２９才　　　　　　　　　　　　　
	// 　04 　男３０から３４才　　　　　　　　　　　　　
	// 　05 　男３５から３９才　　　　　　　　　　　　　
	// 　06 　男４０から４４才　　　　　　　　　　　　　
	// 　07 　男４５から４９才　　　　　　　　　　　　　
	// 　08 　男５０から５４才　　　　　　　　　　　　　
	// 　09 　男５５から５９才　　　　　　　　　　　　　
	// 　10 　男６０から６４才　　　　　　　　　　　　　
	// 　11 　男６５から６９才　　　　　　　　　　　　　
	// 　12 　女１５から１９才　　　　　　　　　　　　　
	// 　13 　女２０から２４才　　　　　　　　　　　　　
	// 　14 　女２５から２９才　　　　　　　　　　　　　
	// 　15 　女３０から３４才　　　　　　　　　　　　　
	// 　16 　女３５から３９才　　　　　　　　　　　　　
	// 　17 　女４０から４４才　　　　　　　　　　　　　
	// 　18 　女４５から４９才　　　　　　　　　　　　　
	// 　19 　女５０から５４才　　　　　　　　　　　　　
	// 　20 　女５５から５９才　　　　　　　　　　　　　
	// 　21 　女６０から６４才　　　　　　　　　　　　　
	// 　22 　女６５から６９才　　　　　　　　　　　　　
	// ------------------------------------------------------------------------------
	// 性・年代（メディア区分）
	// 　1 　男１５から１９才　　　　　　　　　　　　　
	// 　2 　男２０から３４才　　　　　　　　　　　　　
	// 　3 　男３５から４９才　　　　　　　　　　　　　
	// 　4 　男５０才以上　　　　　　　　　　　　　　
	// 　5 　女１５から１９才　　　　　　　　　　　　　
	// 　6 　女２０から３４才　　　　　　　　　　　　　
	// 　7 　女３５から４９才　　　　　　　　　　　　　
	// 　8 　女５０才以上　　　　　　　　　　　　　　
	// ------------------------------------------------------------------------------
	public static String classifyOld3(String val, String gender, String delim) {
		String wKB1 = " ";
		String wKB2 = "  ";
		String wKB3 = " ";
		// System.out.println("classifyOld3 val:"+val+" gender:"+gender);
		StringBuffer buf = new StringBuffer();
		try {
			gender = gender.trim();
			int wOld = Integer.parseInt(val);
			if (gender.equals("1")) {
				// 10代_20代_30代_40代_50代_60代
				if (wOld > 59) {
					wKB1 = "06";
				} else if (wOld > 49) {
					wKB1 = "05";
				} else if (wOld > 39) {
					wKB1 = "04";
				} else if (wOld > 29) {
					wKB1 = "03";
				} else if (wOld > 19) {
					wKB1 = "02";
				} else if (wOld > 9) {
					wKB1 = "01";
				} else {
					wKB1 = "  ";
				}
				// 15..才_20..才_25..才_30..才_35..才_40..才_
				// 45..才_50..才_55..才_60..才_65..才
				if (wOld >= 65) {
					wKB2 = "11";
				} else if (wOld >= 60) {
					wKB2 = "10";
				} else if (wOld >= 55) {
					wKB2 = "09";
				} else if (wOld >= 50) {
					wKB2 = "08";
				} else if (wOld >= 45) {
					wKB2 = "07";
				} else if (wOld >= 40) {
					wKB2 = "06";
				} else if (wOld >= 35) {
					wKB2 = "05";
				} else if (wOld >= 30) {
					wKB2 = "04";
				} else if (wOld >= 25) {
					wKB2 = "03";
				} else if (wOld >= 20) {
					wKB2 = "02";
				} else if (wOld >= 15) {
					wKB2 = "01";
				} else {
					wKB2 = "  ";
				}
				// 15..才_20..才_35..才_50才以上
				if (wOld >= 50) {
					wKB3 = "4";
				} else if (wOld >= 35) {
					wKB3 = "3";
				} else if (wOld >= 20) {
					wKB3 = "2";
				} else if (wOld >= 15) {
					wKB3 = "1";
				} else {
					wKB3 = " ";
				}
			} else if (gender.equals("2")) {
				// 10代_20代_30代_40代_50代_60代
				if (wOld > 59) {
					wKB1 = "12";
				} else if (wOld > 49) {
					wKB1 = "11";
				} else if (wOld > 39) {
					wKB1 = "10";
				} else if (wOld > 29) {
					wKB1 = "09";
				} else if (wOld > 19) {
					wKB1 = "08";
				} else if (wOld > 9) {
					wKB1 = "07";
				} else {
					wKB1 = "  ";
				}
				// 15..才_20..才_25..才_30..才_35..才_40..才_
				// 45..才_50..才_55..才_60..才_65..才
				if (wOld >= 65) {
					wKB2 = "22";
				} else if (wOld >= 60) {
					wKB2 = "21";
				} else if (wOld >= 55) {
					wKB2 = "20";
				} else if (wOld >= 50) {
					wKB2 = "19";
				} else if (wOld >= 45) {
					wKB2 = "18";
				} else if (wOld >= 40) {
					wKB2 = "17";
				} else if (wOld >= 35) {
					wKB2 = "16";
				} else if (wOld >= 30) {
					wKB2 = "15";
				} else if (wOld >= 25) {
					wKB2 = "14";
				} else if (wOld >= 20) {
					wKB2 = "13";
				} else if (wOld >= 15) {
					wKB2 = "12";
				} else {
					wKB2 = "  ";
				}
				// 15..才_20..才_35..才_50才以上
				if (wOld >= 50) {
					wKB3 = "8";
				} else if (wOld >= 35) {
					wKB3 = "7";
				} else if (wOld >= 20) {
					wKB3 = "6";
				} else if (wOld >= 15) {
					wKB3 = "5";
				} else {
					wKB3 = " ";
				}
			}
			buf.append(wKB1);
			buf.append(delim);
			buf.append(wKB2);
			buf.append(delim);
			buf.append(wKB3);
		} catch (NumberFormatException ne) {
			buf.append("  ");
			buf.append(delim);
			buf.append("  ");
			buf.append(delim);
			buf.append(" ");
		}
		// System.out.println("ans:"+buf.toString());
		return buf.toString();
	}

	public static String classifyQPROld(String val, String gender,
			String delim) {
		String wGender = " ";
		String wKB1 = " ";
		String wKB2 = "  ";
		String wKB3 = " ";
		StringBuffer buf = new StringBuffer();
		try {
			gender = gender.trim();
			if (gender.equals("1") | gender.equals("2")) {
				wGender = gender;
			}
			int wOld = Integer.parseInt(val);
			wKB1 = getOLD_10(wOld); // 年代（10歳区分）
			wKB2 = getOLD_5(wOld); // 年代（5歳区分）
			wKB3 = getOLD_MEDIA(wOld); // 年代（メディア区分）
			buf.append(wGender);
			buf.append(wKB1);
			buf.append(delim);
			buf.append(wGender);
			buf.append(wKB2);
			buf.append(delim);
			buf.append(wGender);
			buf.append(wKB3);
		} catch (NumberFormatException ne) {
			buf.append("  ");
			buf.append(delim);
			buf.append("   ");
			buf.append(delim);
			buf.append("  ");
		}
		return buf.toString();
	}

	// -------------------------------------------------------------------
	// 世帯収入関連
	// String wAns = EnqChk.incomeCnv("10","01","02");
	// 《検証用》
	// String qA = ""; // 主婦年収
	// String qB = ""; // 生活費
	// String qC = ""; // 世帯年収
	// for(int i =0;i<=16;i++){
	// if (i<10){ qA = "0"+ i; }else{ qA = "" + i;}
	// for(int j =0;j<=13;j++){
	// if (j<10){ qB = "0"+ j; }else{ qB = "" + j;}
	// for(int k =0;k<=13;k++){
	// if (k<10){ qC = "0"+ k; }else{ qC = "" + k;}
	// wAns = EnqChk.incomeCnv(qA,qB,qC);
	// System.out.println("■"+qA+"_"+qB+"_"+qC+" =>"+wAns);;
	// }
	// }
	// }
	// -------------------------------------------------------------------
	public static String incomeCnv(String pA, String pB, String pC) {
		String wRtn = "       ";
		String incA = " "; // 主婦年収 (1から90ABCDEF)
		String incAX = " "; // 主婦年収区分 (1から4)
		String incB = " "; // 生活費 (1から90ABC)
		String incBX = " "; // 生活費区分 (1から4)
		String incC = " "; // 世帯年収 (1から90ABC)
		String incCX = " "; // 世帯年収区分１(AからM)
		String incCY = " "; // 世帯年収区分２(1から4)
		// 其々未回答の場合の補正はこれでｏｋかどうか・・・いんちきなので後で直しておこう！
		if (pA.equals("  "))
			pA = "00";
		if (pB.equals("  "))
			pB = "00";
		if (pC.equals("  "))
			pC = "00";
		// try{
		// }catch(NumberFormatException ne){
		// }
		int iA = Integer.parseInt(pA);
		int iB = Integer.parseInt(pB);
		int iC = Integer.parseInt(pC);
		if (iA > 0 && iC > 0) {
			if (iA > 9 & iC <= 1) {
				pA = "09";
			} else if (iA > 11 & iC <= 2) {
				pA = "11";
			} else if (iA > 12 & iC <= 3) {
				pA = "12";
			} else if (iA > 13 & iC <= 4) {
				pA = "13";
			} else if (iA > 14 & iC <= 5) {
				pA = "14";
			} else if (iA > 15 & iC <= 9) {
				pA = "15";
			}
		}
		if (iB > 0 && iC > 0) {
			if (iB > 2 & iC <= 1) {
				pB = "02";
			} else if (iB > 5 & iC <= 2) {
				pB = "05";
			} else if (iB > 7 & iC <= 3) {
				pB = "07";
			} else if (iB > 9 & iC <= 4) {
				pB = "09";
			} else if (iB > 10 & iC <= 5) {
				pB = "10";
			} else if (iB > 11 & iC <= 7) {
				pB = "11";
			} else if (iB > 12 & iC <= 8) {
				pB = "12";
			}
		}
		if (iA > 0) {
			incA = ValueChecker.xch(pA);
			incAX = ValueChecker.translate(incA, "1222223344444444",
					"1234567890ABCDEF");
		}
		if (iB > 0) {
			incB = ValueChecker.xch(pB);
			incBX = ValueChecker.translate(incB, "1111222233444",
					"1234567890ABC");
		}
		if (iC > 0) {
			incC = ValueChecker.xch(pC);
			incCX = ValueChecker.translate(incC, "ABCDEFGHIJKLM",
					"1234567890ABC");
			incCY = ValueChecker.translate(incC, "1111223334444",
					"1234567890ABC");
		}
		wRtn = incA + incAX + incB + incBX + incC + incCX + incCY;
		return wRtn; // ７文字返る
	}

	// ------------------------------------------------------------------------------
	// 文字列の変換 25 → 21
	// pStr 入力文字列
	// 《使用例》
	// String wAns = EnqChk.regionCnv("11");
	// ------------------------------------------------------------------------------
	public static String cnvAreaCode(String param) {
		// 旧地域コード変換・・・このフィールドは現在使用されていない（過渡期用）
		String ans = "00";
		if (param.equals("11")) {
			ans = "11";
		} else if (param.equals("12")) {
			ans = "12";
		} else if (param.equals("13")) {
			ans = "13";
		} else if (param.equals("14")) {
			ans = "14";
		} else if (param.equals("25")) {
			ans = "21";
		} else if (param.equals("26")) {
			ans = "22";
		} else if (param.equals("27")) {
			ans = "23";
		} else if (param.equals("28")) {
			ans = "24";
		} else if (param.equals("29")) {
			ans = "25";
		} else if (param.equals("30")) {
			ans = "26";
		}
		return ans;
	}

	// ------------------------------------------------------------------------------
	// 日付文字列の変換
	// pStr 入力文字列　yyyy/mm/dd hh:mmのような形
	// 《使用例》
	// String wAns = EnqChk.cnvYmd("2007/11/18 11:25:12);
	// ※regixをチェックしたほうがよいか？
	// ------------------------------------------------------------------------------
	public static String cnvYmd6(String data) {
		String wk = cnvYmd(data);
		return wk.substring(2);
	}

	//-------------------------------------------------------------------------
	// yyyymmdd => yyyy年MM月DD日
	//-------------------------------------------------------------------------
	public static String ymdKJEdit(String pYmd) {
		String sYY = "0000";
		String sMM = "00";
		String sDD = "00";
		if (pYmd.length() >= 8) {
			sYY = pYmd.substring(0, 4);
			sMM = pYmd.substring(4, 6);
			sDD = pYmd.substring(6, 8);
		}
		return sYY + "年" + sMM + "月" + sDD + "日";
	}

	//---------------------------------------------------------------
	//cnvYmd
	//15/01/15,2015-1-8,のような文字列を固定長のYYYYMMDDにフォーマットするロジック
	//---------------------------------------------------------------
	public static String cnvYmd(String data) {
		data = data.trim();
		data = data.replaceAll("[年月日]", "-"); //20160901
		if (data.equals("0") || data.equals("000000")
				|| data.equals("00000000")) {
			return "00000000";
		} else if (data.length() == 6) {
			data = String.format("%s/%s/%s", data.substring(0, 2),
					data.substring(2, 4), data.substring(4));
		}
		data = data + " "; // yyyy/mm/dd でも大丈夫なように・・・変更
		StringBuffer buf = new StringBuffer();
		// XXX パターンをあらかじめコンパイルする
		// if
		// (data.matches("[0-9]+/[0-9]+/[0-9]+.+")||data.matches("[0-9]+\\.[0-9]+\\.[0-9]+.+")||data.matches("[0-9]+-[0-9]+-[0-9]+.+"))
		// {
		if (isDate001(data) || isDate002(data) || isDate003(data)) {
			String arry1[] = data.split(" ");
			String arry2[] = arry1[0].split("[-/\\.]");
			String wY = arry2[0];
			if (wY.length() == 2) {
				int yy = Integer.parseInt(wY);
				if (yy < 80) {
					wY = "20" + wY;
				} else {
					wY = "19" + wY;
				}
			}
			wY = CharConv.fixRight(wY, 4, '0');
			String wM = CharConv.fixRight(arry2[1].trim(), 2, '0');
			String wD = CharConv.fixRight(arry2[2].trim(), 2, '0');
			buf.append(wY);
			buf.append(wM);
			buf.append(wD);
			// } else if (data.matches("[0-9]{8}+.+")) {
		} else if (isDate000(data)) {
			buf.append(data.substring(0, 8));
		} else {
			buf.append("00000000");
		}
		return buf.toString();
	}

	// ------------------------------------------------------------------------------
	// 文字列の変換 01から16 → 1からF
	// pStr 入力文字列
	// 《使用例》
	// String wAns = EnqChk.xch("11");
	// >A
	// ※範囲外の文字はスペースで大丈夫か？
	// ------------------------------------------------------------------------------
	private static String xch(String pXX) {
		String wAns = " ";
		int x = Integer.parseInt(pXX);
		switch (x) {
		case 0:
			wAns = "0";
			break;
		case 1:
			wAns = "1";
			break;
		case 2:
			wAns = "2";
			break;
		case 3:
			wAns = "3";
			break;
		case 4:
			wAns = "4";
			break;
		case 5:
			wAns = "5";
			break;
		case 6:
			wAns = "6";
			break;
		case 7:
			wAns = "7";
			break;
		case 8:
			wAns = "8";
			break;
		case 9:
			wAns = "9";
			break;
		case 10:
			wAns = "A";
			break;
		case 11:
			wAns = "B";
			break;
		case 12:
			wAns = "C";
			break;
		case 13:
			wAns = "D";
			break;
		case 14:
			wAns = "E";
			break;
		case 15:
			wAns = "F";
			break;
		case 16:
			wAns = "G";
			break;
		case 17:
			wAns = "H";
			break;
		case 18:
			wAns = "I";
			break;
		case 19:
			wAns = "J";
			break;
		case 20:
			wAns = "K";
			break;
		case 21:
			wAns = "L";
			break;
		case 22:
			wAns = "M";
			break;
		case 23:
			wAns = "N";
			break;
		case 24:
			wAns = "O";
			break;
		case 25:
			wAns = "P";
			break;
		case 26:
			wAns = "Q";
			break;
		case 27:
			wAns = "R";
			break;
		case 28:
			wAns = "S";
			break;
		case 29:
			wAns = "T";
			break;
		case 30:
			wAns = "U";
			break;
		case 31:
			wAns = "V";
			break;
		case 32:
			wAns = "W";
			break;
		case 33:
			wAns = "X";
			break;
		case 34:
			wAns = "Y";
			break;
		case 35:
			wAns = "Z";
			break;
		default:
			wAns = "?";
			break;
		}
		return wAns;
	}

	// ------------------------------------------------------------------------------
	//	kyPkg.util.ValueChecker.idFormatter("1")
	//20160513 マクロミルのIDが7ケタを超えた場合７を付加しない
	//TODO	8で始まる場合旧QPRのIDとかぶってしまう・・・その場合？先頭一文字をアルファベット等に置き換えてはどうか？
	//TODO 	そもそも8ケタを超えるような場合にも問題がある
	// ------------------------------------------------------------------------------
	public static String idFormatter(String pStr) {
		//		System.out.println("idFormatter in :"+pStr);
		pStr = pStr.trim();
		String id = CharConv.fixRight(pStr, 8, '0');//前ゼロ8ケタにする
		//		System.out.println("idFormatter ---:"+id);
		if (id.startsWith("0")) {
			id = ID_SEVEN + id.substring(1);//先頭がゼロの場合のみ”７”に置き換える
		}
		//		System.out.println("idFormatter out:"+id);
		return id;//先頭がゼロ以外の場合はそのままとする
	}

	public static void main(String[] argv) {
		//		testCnvMultiAns();
		testIdFormatter();
	}

	//-------------------------------------------------------------------------
	// 20160909
	//-------------------------------------------------------------------------
	public static void testIdFormatter() {
		System.out.println(">" + idFormatter("1"));
		System.out.println(">" + idFormatter("12"));
		System.out.println(">" + idFormatter("123"));
		System.out.println(">" + idFormatter("1234"));
		System.out.println(">" + idFormatter("12345"));
		System.out.println(">" + idFormatter("123456"));
		System.out.println(">" + idFormatter("1234567"));
		System.out.println(">" + idFormatter("12345678"));
		System.out.println(">" + idFormatter("22345678"));
		System.out.println(">" + idFormatter("32345678"));
		System.out.println(">" + idFormatter("42345678"));
		System.out.println(">" + idFormatter("52345678"));
		System.out.println(">" + idFormatter("62345678"));
		System.out.println(">" + idFormatter("72345678"));
		System.out.println(">" + idFormatter("82345678"));
		System.out.println(">" + idFormatter("92345678"));
		System.out.println(">" + idFormatter("123456789"));//このばあい下８桁となる
	}

	//###################################################################
	public static void tester() {
		// memberCount, Gender,String pMarried,String pMainPurchase)
		// System.out.println("■ans 1_2_2_1=>"+kyPkg.util.EnqChk.getMonitorType("1","2","2","0"));
		// System.out.println("■ans 1_2_2_1=>"+kyPkg.util.EnqChk.getMonitorType("2","2","2","0"));
		// System.out.println("■ans 1_2_2_1=>"+kyPkg.util.EnqChk.getMonitorType("3","2","3","0"));
		// System.out.println("■ans 1_2_2_1=>"+kyPkg.util.EnqChk.getMonitorType("4","2","3","0"));
		// System.out.println("■ans 1_2_2_1=>"+kyPkg.util.EnqChk.getMonitorType("5","2","3","0"));

		// regcheknCnv(String pStr,String pReg,String pMatch,String pUnMatch)
		System.out.println("■ans regcheknCnv0=>"
				+ kyPkg.converter.ValueChecker.regcheknCnv("0", "[123]", "1", "0"));
		System.out.println("■ans regcheknCnv1=>"
				+ kyPkg.converter.ValueChecker.regcheknCnv("1", "[123]", "1", "0"));
		System.out.println("■ans regcheknCnv2=>"
				+ kyPkg.converter.ValueChecker.regcheknCnv("2", "[123]", "1", "0"));
		System.out.println("■ans regcheknCnv2=>"
				+ kyPkg.converter.ValueChecker.regcheknCnv("3", "[123]", "1", "0"));
		System.out.println("■ans regcheknCnv2=>"
				+ kyPkg.converter.ValueChecker.regcheknCnv("4", "[123]", "1", "0"));

		// String ans = kyPkg.util.EnqChk.classifyOld3("10","1","@");
		// System.out.println("■ans =>"+ans);

		// String ans =
		// EnqChk.cnvMultiAns("'1','3','5','7','09','10','11',a,,",15);
		// System.out.println("■ans =>"+ans);

		// GregorianCalendar calendar = new GregorianCalendar();
		// SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		// String wToday = sf.format(calendar.getTime());
		// System.out.println("Today:"+ wToday );
		//
		// System.out.println("■1 =>"+EnqChk.cnvYmd("2007/11/18 11:25:12"));
		// System.out.println("■2 =>"+EnqChk.cnvYmd("2007/11/18"));
		// System.out.println("■3 =>"+EnqChk.cnvYmd("20071118"));

		/*
		 * String wAns = "";
		 * 
		 * wAns = EnqChk.regFix("123","\\d+",5); System.out.println(
		 * "■123,\\d+,5          =>"+wAns); wAns =
		 * EnqChk.regFix("Ab1","[A-Za-z0-9]+",8); System.out.println(
		 * "■Ab1,[A-Za-z0-9]+,8 =>"+wAns); wAns =
		 * EnqChk.regFix("49011101","\\s*[0-9]+\\s*",13); System.out.println(
		 * "■49011101,\\s*[0-9]+\\s*,13 =>"+wAns);
		 * 
		 * String pStr1 =
		 * "12345678１２３４５６７８９０１２３４５ABBBCCCDDDEEE FFF*                    ";
		 * String pStr2 =
		 * "AQAxxxxx普段利用するスーパー　　　　　2195089001036 002*                    ";
		 * String pStr3 =
		 * "AQA     普段利用するスーパー　　　　　2195089001036 002*                    ";
		 * String pStr4 =
		 * "12345678                              X                                    E"
		 * ; String pStr5 =
		 * "ARI     猫 の 飼 育 匹 数 区 分 　　　2999000001001 001                    E";
		 * wAns = EnqChk.spKjCnv(pStr1,8,15);
		 * System.out.println("■spKjCnv1=>"+wAns+"<="); wAns =
		 * EnqChk.spKjCnv(pStr2,8,15);
		 * System.out.println("■spKjCnv2=>"+wAns+"<="); wAns =
		 * EnqChk.spKjCnv(pStr3,8,15);
		 * System.out.println("■spKjCnv3=>"+wAns+"<="); wAns =
		 * EnqChk.spKjCnv(pStr4,8,15);
		 * System.out.println("■spKjCnv4=>"+wAns+"<="); wAns =
		 * EnqChk.spKjCnv(pStr5,8,15);
		 * System.out.println("■spKjCnv5=>"+wAns+"<=");
		 * System.out.println("----------------------------------------------");
		 * wAns = EnqChk.dogCnv("  "); System.out.println("■dogCnv SP=>"+wAns);
		 * wAns = EnqChk.dogCnv("01"); System.out.println("■dogCnv 01=>"+wAns);
		 * wAns = EnqChk.dogCnv("02"); System.out.println("■dogCnv 02=>"+wAns);
		 * wAns = EnqChk.dogCnv("03"); System.out.println("■dogCnv 03=>"+wAns);
		 * wAns = EnqChk.dogCnv("04"); System.out.println("■dogCnv 04=>"+wAns);
		 * wAns = EnqChk.dogCnv("05"); System.out.println("■dogCnv 05=>"+wAns);
		 * wAns = EnqChk.dogCnv("06"); System.out.println("■dogCnv 06=>"+wAns);
		 * wAns = EnqChk.dogCnv("07"); System.out.println("■dogCnv 07=>"+wAns);
		 * System.out.println("----------------------------------------------");
		 * wAns = EnqChk.catCnv("  "); System.out.println("■catCnv SP=>"+wAns);
		 * wAns = EnqChk.catCnv("01"); System.out.println("■catCnv 01=>"+wAns);
		 * wAns = EnqChk.catCnv("02"); System.out.println("■catCnv 02=>"+wAns);
		 * wAns = EnqChk.catCnv("03"); System.out.println("■catCnv 03=>"+wAns);
		 * wAns = EnqChk.catCnv("04"); System.out.println("■catCnv 04=>"+wAns);
		 * wAns = EnqChk.catCnv("05"); System.out.println("■catCnv 05=>"+wAns);
		 * wAns = EnqChk.catCnv("06"); System.out.println("■catCnv 06=>"+wAns);
		 * wAns = EnqChk.catCnv("07"); System.out.println("■catCnv 07=>"+wAns);
		 * System.out.println("----------------------------------------------");
		 */
	}

	public static void testCnvMultiAns() {
		int width = 10;
		HashSet<Integer> set = new HashSet();
		set.add(1);
		set.add(2);
		set.add(3);
		set.add(9);
		set.add(10);
		String ans = kyPkg.converter.ValueChecker.cnvMultiAns(width, set);
		System.out.println("■ans =>" + ans);
	}
}
