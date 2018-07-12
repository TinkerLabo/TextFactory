package kyPkg.util;
public class Channel_Classic {
	// ---------------------------------------------------------------
	// 古いバージョン（使っていない！！）　念のため残してある 2010/01/22 yuasa
	// ---------------------------------------------------------------
	// 店コード変換関係のコードを集めてみた・・・・
	// ---------------------------------------------------------------
	public static String encode(String wStr) {
		String wAns = " ";
		try {
			int x = Integer.parseInt(wStr);
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
			case 36:
				wAns = "a";
				break;
			case 37:
				wAns = "b";
				break;
			case 38:
				wAns = "c";
				break;
			case 39:
				wAns = "d";
				break;
			case 40:
				wAns = "e";
				break;
			case 41:
				wAns = "f";
				break;
			case 42:
				wAns = "g";
				break;
			case 43:
				wAns = "h";
				break;
			case 44:
				wAns = "i";
				break;
			case 45:
				wAns = "j";
				break;
			case 46:
				wAns = "k";
				break;
			case 47:
				wAns = "l";
				break;
			case 48:
				wAns = "m";
				break;
			case 49:
				wAns = "n";
				break;
			case 50:
				wAns = "o";
				break;
			case 51:
				wAns = "p";
				break;
			case 52:
				wAns = "q";
				break;
			case 53:
				wAns = "r";
				break;
			case 54:
				wAns = "s";
				break;
			case 55:
				wAns = "t";
				break;
			case 56:
				wAns = "u";
				break;
			case 57:
				wAns = "v";
				break;
			case 58:
				wAns = "w";
				break;
			case 59:
				wAns = "x";
				break;
			case 60:
				wAns = "y";
				break;
			case 61:
				wAns = "z";
				break;
			case 62:
				wAns = "ｱ";
				break;
			case 63:
				wAns = "ｲ";
				break;
			case 64:
				wAns = "ｳ";
				break;
			case 65:
				wAns = "ｴ";
				break;
			case 66:
				wAns = "ｵ";
				break;
			case 67:
				wAns = "ｶ";
				break;
			case 68:
				wAns = "ｷ";
				break;
			case 69:
				wAns = "ｸ";
				break;
			case 70:
				wAns = "ｹ";
				break;
			case 71:
				wAns = "ｺ";
				break;
			case 72:
				wAns = "ｻ";
				break;
			case 73:
				wAns = "ｼ";
				break;
			case 74:
				wAns = "ｽ";
				break;
			case 75:
				wAns = "ｾ";
				break;
			case 76:
				wAns = "ｿ";
				break;
			case 77:
				wAns = "ﾀ";
				break;
			case 78:
				wAns = "ﾁ";
				break;
			case 79:
				wAns = "ﾂ";
				break;
			case 80:
				wAns = "ﾃ";
				break;
			case 81:
				wAns = "ﾄ";
				break;
			case 82:
				wAns = "ﾅ";
				break;
			case 83:
				wAns = "ﾆ";
				break;
			case 84:
				wAns = "ﾇ";
				break;
			case 85:
				wAns = "ﾈ";
				break;
			case 86:
				wAns = "ﾉ";
				break;
			case 87:
				wAns = "ﾊ";
				break;
			case 88:
				wAns = "ﾋ";
				break;
			case 89:
				wAns = "ﾌ";
				break;
			case 90:
				wAns = "ﾍ";
				break;
			case 91:
				wAns = "ﾎ";
				break;
			case 92:
				wAns = "ﾏ";
				break;
			case 93:
				wAns = "ﾐ";
				break;
			case 94:
				wAns = "ﾑ";
				break;
			case 95:
				wAns = "ﾒ";
				break;
			case 96:
				wAns = "ﾓ";
				break;
			case 97:
				wAns = "ﾔ";
				break;
			case 98:
				wAns = "ﾕ";
				break;
			case 99:
				wAns = "ﾖ";
				break;
			default:
				wAns = "9";
				break;
			}
		} catch (Exception e) {
			wAns = "?";
		}
		return wAns;
	}

	public static String decode(String wStr) {
		String wAns = " ";
		if (wStr.equals("0")) {
			wAns = "00";
		} else if (wStr.equals("1")) {
			wAns = "01";
		} else if (wStr.equals("2")) {
			wAns = "02";
		} else if (wStr.equals("3")) {
			wAns = "03";
		} else if (wStr.equals("4")) {
			wAns = "04";
		} else if (wStr.equals("5")) {
			wAns = "05";
		} else if (wStr.equals("6")) {
			wAns = "06";
		} else if (wStr.equals("7")) {
			wAns = "07";
		} else if (wStr.equals("8")) {
			wAns = "08";
		} else if (wStr.equals("9")) {
			wAns = "09";
		} else if (wStr.equals("A")) {
			wAns = "10";
		} else if (wStr.equals("B")) {
			wAns = "11";
		} else if (wStr.equals("C")) {
			wAns = "12";
		} else if (wStr.equals("D")) {
			wAns = "13";
		} else if (wStr.equals("E")) {
			wAns = "14";
		} else if (wStr.equals("F")) {
			wAns = "15";
		} else if (wStr.equals("G")) {
			wAns = "16";
		} else if (wStr.equals("H")) {
			wAns = "17";
		} else if (wStr.equals("I")) {
			wAns = "18";
		} else if (wStr.equals("J")) {
			wAns = "19";
		} else if (wStr.equals("K")) {
			wAns = "20";
		} else if (wStr.equals("L")) {
			wAns = "21";
		} else if (wStr.equals("M")) {
			wAns = "22";
		} else if (wStr.equals("N")) {
			wAns = "23";
		} else if (wStr.equals("O")) {
			wAns = "24";
		} else if (wStr.equals("P")) {
			wAns = "25";
		} else if (wStr.equals("Q")) {
			wAns = "26";
		} else if (wStr.equals("R")) {
			wAns = "27";
		} else if (wStr.equals("S")) {
			wAns = "28";
		} else if (wStr.equals("T")) {
			wAns = "29";
		} else if (wStr.equals("U")) {
			wAns = "30";
		} else if (wStr.equals("V")) {
			wAns = "31";
		} else if (wStr.equals("W")) {
			wAns = "32";
		} else if (wStr.equals("X")) {
			wAns = "33";
		} else if (wStr.equals("Y")) {
			wAns = "34";
		} else if (wStr.equals("Z")) {
			wAns = "35";
		} else if (wStr.equals("a")) {
			wAns = "36";
		} else if (wStr.equals("b")) {
			wAns = "37";
		} else if (wStr.equals("c")) {
			wAns = "38";
		} else if (wStr.equals("d")) {
			wAns = "39";
		} else if (wStr.equals("e")) {
			wAns = "40";
		} else if (wStr.equals("f")) {
			wAns = "41";
		} else if (wStr.equals("g")) {
			wAns = "42";
		} else if (wStr.equals("h")) {
			wAns = "43";
		} else if (wStr.equals("i")) {
			wAns = "44";
		} else if (wStr.equals("j")) {
			wAns = "45";
		} else if (wStr.equals("k")) {
			wAns = "46";
		} else if (wStr.equals("l")) {
			wAns = "47";
		} else if (wStr.equals("m")) {
			wAns = "48";
		} else if (wStr.equals("n")) {
			wAns = "49";
		} else if (wStr.equals("o")) {
			wAns = "50";
		} else if (wStr.equals("p")) {
			wAns = "51";
		} else if (wStr.equals("q")) {
			wAns = "52";
		} else if (wStr.equals("r")) {
			wAns = "53";
		} else if (wStr.equals("s")) {
			wAns = "54";
		} else if (wStr.equals("t")) {
			wAns = "55";
		} else if (wStr.equals("u")) {
			wAns = "56";
		} else if (wStr.equals("v")) {
			wAns = "57";
		} else if (wStr.equals("w")) {
			wAns = "58";
		} else if (wStr.equals("x")) {
			wAns = "59";
		} else if (wStr.equals("y")) {
			wAns = "60";
		} else if (wStr.equals("z")) {
			wAns = "61";
		} else if (wStr.equals("ｱ")) {
			wAns = "62";
		} else if (wStr.equals("ｲ")) {
			wAns = "63";
		} else if (wStr.equals("ｳ")) {
			wAns = "64";
		} else if (wStr.equals("ｴ")) {
			wAns = "65";
		} else if (wStr.equals("ｵ")) {
			wAns = "66";
		} else if (wStr.equals("ｶ")) {
			wAns = "67";
		} else if (wStr.equals("ｷ")) {
			wAns = "68";
		} else if (wStr.equals("ｸ")) {
			wAns = "69";
		} else if (wStr.equals("ｹ")) {
			wAns = "70";
		} else if (wStr.equals("ｺ")) {
			wAns = "71";
		} else if (wStr.equals("ｻ")) {
			wAns = "72";
		} else if (wStr.equals("ｼ")) {
			wAns = "73";
		} else if (wStr.equals("ｽ")) {
			wAns = "74";
		} else if (wStr.equals("ｾ")) {
			wAns = "75";
		} else if (wStr.equals("ｿ")) {
			wAns = "76";
		} else if (wStr.equals("ﾀ")) {
			wAns = "77";
		} else if (wStr.equals("ﾁ")) {
			wAns = "78";
		} else if (wStr.equals("ﾂ")) {
			wAns = "79";
		} else if (wStr.equals("ﾃ")) {
			wAns = "80";
		} else if (wStr.equals("ﾄ")) {
			wAns = "81";
		} else if (wStr.equals("ﾅ")) {
			wAns = "82";
		} else if (wStr.equals("ﾆ")) {
			wAns = "83";
		} else if (wStr.equals("ﾇ")) {
			wAns = "84";
		} else if (wStr.equals("ﾈ")) {
			wAns = "85";
		} else if (wStr.equals("ﾉ")) {
			wAns = "86";
		} else if (wStr.equals("ﾊ")) {
			wAns = "87";
		} else if (wStr.equals("ﾋ")) {
			wAns = "88";
		} else if (wStr.equals("ﾌ")) {
			wAns = "89";
		} else if (wStr.equals("ﾍ")) {
			wAns = "90";
		} else if (wStr.equals("ﾎ")) {
			wAns = "91";
		} else if (wStr.equals("ﾏ")) {
			wAns = "92";
		} else if (wStr.equals("ﾐ")) {
			wAns = "93";
		} else if (wStr.equals("ﾑ")) {
			wAns = "94";
		} else if (wStr.equals("ﾒ")) {
			wAns = "95";
		} else if (wStr.equals("ﾓ")) {
			wAns = "96";
		} else if (wStr.equals("ﾔ")) {
			wAns = "97";
		} else if (wStr.equals("ﾕ")) {
			wAns = "98";
		} else if (wStr.equals("ﾖ")) {
			wAns = "99";
		} else if (wStr.equals("?")) {
			wAns = "99";
		}
		return wAns;
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
		buf.append(Channel_Classic.encode(wCnl.substring(1, 3)));
		return buf.toString();
	}
}
