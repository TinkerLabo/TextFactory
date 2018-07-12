package kyPkg.converter;

import java.util.HashMap;

import kyPkg.util.ChannelCnv;

//	チャネルコード・店コード（検索用キーワード）　2010-05-11
//XXX　ｍｙｓｑｌの場合アルファベットの大文字小文字は区別されるのかどうか、確認しておきたい
public class Channel {
	private static HashMap<String, String> map2to1;
	private static HashMap<String, String> map1to2;

	public static String channelCnv(String channel) {
		if (channel.equals("698"))
			channel = "608"; // その他屋内の自販機
		if (channel.equals("699"))
			channel = "609"; // その他屋外の自販機
		String wCn1 = channel.substring(0, 1); // １桁目
		String wCn2 = channel.substring(1, 3); // 残り２桁
		String wCn3 = Channel.encode(wCn2); // EX 09=>9 10=>A に変換
		if (wCn3.equals("?"))
			wCn3 = "9";
		return wCn1 + wCn3;
	}

	public static String encode(String wStr) {
		if (map2to1 == null)
			map2to1 = incore2to1();
		return convert(map2to1, wStr, "9");
	}

	public static String decode(String wStr) {
		if (map1to2 == null)
			map1to2 = incore1to2();
		return convert(map1to2, wStr, "99");
	}

	private static String convert(HashMap<String, String> map, String wStr,
			String defval) {
		String ans = defval;
		if (map != null) {
			ans = map.get(wStr);
			if (ans == null) {
				ans = defval;
			}
		}
		return ans;
	}

	private static HashMap<String, String> incore1to2() {
		HashMap<String, String> map = new HashMap();
		map.put("0", "00");
		map.put("1", "01");
		map.put("2", "02");
		map.put("3", "03");
		map.put("4", "04");
		map.put("5", "05");
		map.put("6", "06");
		map.put("7", "07");
		map.put("8", "08");
		map.put("9", "09");
		map.put("A", "10");
		map.put("B", "11");
		map.put("C", "12");
		map.put("D", "13");
		map.put("E", "14");
		map.put("F", "15");
		map.put("G", "16");
		map.put("H", "17");
		map.put("I", "18");
		map.put("J", "19");
		map.put("K", "20");
		map.put("L", "21");
		map.put("M", "22");
		map.put("N", "23");
		map.put("O", "24");
		map.put("P", "25");
		map.put("Q", "26");
		map.put("R", "27");
		map.put("S", "28");
		map.put("T", "29");
		map.put("U", "30");
		map.put("V", "31");
		map.put("W", "32");
		map.put("X", "33");
		map.put("Y", "34");
		map.put("Z", "35");
		map.put("ｱ", "36");
		map.put("ｲ", "37");
		map.put("ｳ", "38");
		map.put("ｴ", "39");
		map.put("ｵ", "40");
		map.put("ｶ", "41");
		map.put("ｷ", "42");
		map.put("ｸ", "43");
		map.put("ｹ", "44");
		map.put("ｺ", "45");
		map.put("ｻ", "46");
		map.put("ｼ", "47");
		map.put("ｽ", "48");
		map.put("ｾ", "49");
		map.put("ｿ", "50");
		map.put("ﾀ", "51");
		map.put("ﾁ", "52");
		map.put("ﾂ", "53");
		map.put("ﾃ", "54");
		map.put("ﾄ", "55");
		map.put("ﾅ", "56");
		map.put("ﾆ", "57");
		map.put("ﾇ", "58");
		map.put("ﾈ", "59");
		map.put("ﾉ", "60");
		map.put("ﾊ", "61");
		map.put("ﾋ", "62");
		map.put("ﾌ", "63");
		map.put("ﾍ", "64");
		map.put("ﾎ", "65");
		map.put("ﾏ", "66");
		map.put("ﾐ", "67");
		map.put("ﾑ", "68");
		map.put("ﾒ", "69");
		map.put("ﾓ", "70");
		map.put("ﾔ", "71");
		map.put("ﾕ", "72");
		map.put("ﾖ", "73");
		map.put("a", "74");
		map.put("b", "75");
		map.put("c", "76");
		map.put("d", "77");
		map.put("e", "78");
		map.put("f", "79");
		map.put("g", "80");
		map.put("h", "81");
		map.put("i", "82");
		map.put("j", "83");
		map.put("k", "84");
		map.put("l", "85");
		map.put("m", "86");
		map.put("n", "87");
		map.put("o", "88");
		map.put("p", "89");
		map.put("q", "90");
		map.put("r", "91");
		map.put("s", "92");
		map.put("t", "93");
		map.put("u", "94");
		map.put("v", "95");
		map.put("w", "96");
		map.put("x", "97");
		map.put("y", "98");
		// map.put("9", "99");
		return map;
	}

	private static HashMap<String, String> incore2to1() {
		HashMap<String, String> map = new HashMap();
		map.put("00", "0");
		map.put("01", "1");
		map.put("02", "2");
		map.put("03", "3");
		map.put("04", "4");
		map.put("05", "5");
		map.put("06", "6");
		map.put("07", "7");
		map.put("08", "8");
		map.put("09", "9");
		map.put("10", "A");
		map.put("11", "B");
		map.put("12", "C");
		map.put("13", "D");
		map.put("14", "E");
		map.put("15", "F");
		map.put("16", "G");
		map.put("17", "H");
		map.put("18", "I");
		map.put("19", "J");
		map.put("20", "K");
		map.put("21", "L");
		map.put("22", "M");
		map.put("23", "N");
		map.put("24", "O");
		map.put("25", "P");
		map.put("26", "Q");
		map.put("27", "R");
		map.put("28", "S");
		map.put("29", "T");
		map.put("30", "U");
		map.put("31", "V");
		map.put("32", "W");
		map.put("33", "X");
		map.put("34", "Y");
		map.put("35", "Z");
		map.put("36", "ｱ");
		map.put("37", "ｲ");
		map.put("38", "ｳ");
		map.put("39", "ｴ");
		map.put("40", "ｵ");
		map.put("41", "ｶ");
		map.put("42", "ｷ");
		map.put("43", "ｸ");
		map.put("44", "ｹ");
		map.put("45", "ｺ");
		map.put("46", "ｻ");
		map.put("47", "ｼ");
		map.put("48", "ｽ");
		map.put("49", "ｾ");
		map.put("50", "ｿ");
		map.put("51", "ﾀ");
		map.put("52", "ﾁ");
		map.put("53", "ﾂ");
		map.put("54", "ﾃ");
		map.put("55", "ﾄ");
		map.put("56", "ﾅ");
		map.put("57", "ﾆ");
		map.put("58", "ﾇ");
		map.put("59", "ﾈ");
		map.put("60", "ﾉ");
		map.put("61", "ﾊ");
		map.put("62", "ﾋ");
		map.put("63", "ﾌ");
		map.put("64", "ﾍ");
		map.put("65", "ﾎ");
		map.put("66", "ﾏ");
		map.put("67", "ﾐ");
		map.put("68", "ﾑ");
		map.put("69", "ﾒ");
		map.put("70", "ﾓ");
		map.put("71", "ﾔ");
		map.put("72", "ﾕ");
		map.put("73", "ﾖ");
		map.put("74", "a");
		map.put("75", "b");
		map.put("76", "c");
		map.put("77", "d");
		map.put("78", "e");
		map.put("79", "f");
		map.put("80", "g");
		map.put("81", "h");
		map.put("82", "i");
		map.put("83", "j");
		map.put("84", "k");
		map.put("85", "l");
		map.put("86", "m");
		map.put("87", "n");
		map.put("88", "o");
		map.put("89", "p");
		map.put("90", "q");
		map.put("91", "r");
		map.put("92", "s");
		map.put("93", "t");
		map.put("94", "u");
		map.put("95", "v");
		map.put("96", "w");
		map.put("97", "x");
		map.put("98", "y");
		map.put("99", "9");
		return map;
	}

	// ---------------------------------------------------------------
	// 文字列の変換(３桁→２桁)
	// 《使用例》
	// String wAns = EnqChk.xch("11");
	// ---------------------------------------------------------------
	public static String cnv2to3(String wCnl) {
		String param_A = wCnl.substring(0, 1);
		String param_B = wCnl.substring(1, 2);
		String wAns = param_A + decode(param_B);
		// 付番ミスを修正
		if (wAns.equals("608"))
			wAns = "698"; // その他屋内の自販機
		if (wAns.equals("609"))
			wAns = "699"; // その他屋外の自販機
		return wAns;
	}

	// ---------------------------------------------------------------
	// 文字列の変換(２桁→３桁)
	// ---------------------------------------------------------------
	public static String cnv3to2(String wCnl) {
		// 付番ミスを修正
		if (wCnl.equals("698"))
			wCnl = "608"; // その他屋内の自販機
		if (wCnl.equals("699"))
			wCnl = "609"; // その他屋外の自販機
		StringBuffer buf = new StringBuffer();
		buf.append(wCnl.substring(0, 1));
		buf.append(Channel.encode(wCnl.substring(1, 3)));
		return buf.toString();
	}

	// XXX 実際にマッピングされている店名に変換するロジックもほしい
	// XXX substrのトリムするフィルターを作りたい
	// ---------------------------------------------------------------
	// 店コード変換関係のコードを集めてみた・・・・
	// ---------------------------------------------------------------
	public static void main(String[] argv) {
		//		testChannelCnv();
		test();
	}

	//TODO 20160913 エラー対策をする
	//	public static void testChannelCnv2() {
	//		String orginalChannel = Channel.channelCnv(mmChannel3Col);
	//
	//		// 確認用（新旧の変換結果を比較）
	//		if (!newChannel.equals(orginalChannel)) {
	//			System.out.println(
	//					"●~* #Channel Not Equal!! code:"
	//							+ mmChannel3Col + " new=>"
	//							+ newChannel + " org =>"
	//							+ orginalChannel);
	//		}
	//		
	//	}
	public static void test() {
		kyPkg.converter.CnvDictionary cnvDict = ChannelCnv.setUpConverter();
	}

	public static void testChannelCnv() {
		System.out.println("698=>" + channelCnv("698"));
		// 11/02/28 追加分中、変更対象のものについての割り当て
		// 　ｍｍコード中変換対象外の番号は飛ばすので・・中間コードを振りそれを変換する。
		System.out.println("149=>" + channelCnv("149"));
		System.out.println("225=>" + channelCnv("225"));
		System.out.println("226=>" + channelCnv("226"));
		System.out.println("227=>" + channelCnv("227"));
		System.out.println("228=>" + channelCnv("228"));
		System.out.println("151=>" + channelCnv("151"));
		System.out.println("152=>" + channelCnv("152"));
		System.out.println("153=>" + channelCnv("153"));
		System.out.println("610=>" + channelCnv("610"));
		System.out.println("611=>" + channelCnv("611"));
		System.out.println("612=>" + channelCnv("612"));
		System.out.println("613=>" + channelCnv("613"));
		System.out.println("614=>" + channelCnv("614"));
		System.out.println("615=>" + channelCnv("615"));
		System.out.println("616=>" + channelCnv("616"));
		System.out.println("617=>" + channelCnv("617"));
		System.out.println("618=>" + channelCnv("618"));
		System.out.println("619=>" + channelCnv("619"));
		System.out.println("620=>" + channelCnv("620"));
		System.out.println("941=>" + channelCnv("941"));
		System.out.println("942=>" + channelCnv("942"));

	}

}
