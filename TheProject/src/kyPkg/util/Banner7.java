package kyPkg.util;

import java.util.ArrayList;
import java.util.List;

//ï∂éöÇÃçÇÇ≥Ç™7åÖî≈
public class Banner7 {
	static String BACK = " ";
	static String FORE = "*";
	static int LIMIT = 10;
	static int HEIGHT = 7;
	static int WIDTH = 7;
	static String[] glyphs = {
			"         @@@  @@  @@   @ @   @@@@@          @@     @@@  ",
			"         @@@  @@  @@   @ @  @  @  @@@   @  @  @    @@@  ",
			"         @@@   @  @  @@@@@@@@  @   @@  @    @@      @   ",
			"          @            @ @   @@@@@    @    @@@     @    ",
			"                     @@@@@@@   @  @  @    @   @ @       ",
			"         @@@           @ @  @  @  @ @  @@ @    @        ",
			"         @@@           @ @   @@@@@ @   @@  @@@@ @       ",

			"   @@    @@                                            @",
			"  @        @   @   @    @                             @ ",
			" @          @   @ @     @                            @  ",
			" @          @ @@@@@@@ @@@@@   @@@   @@@@@           @   ",
			" @          @   @ @     @     @@@                  @    ",
			"  @        @   @   @    @      @            @@@   @     ",
			"   @@    @@                   @             @@@  @      ",

			"  @@@     @    @@@@@  @@@@@ @      @@@@@@@ @@@@@ @@@@@@@",
			" @   @   @@   @     @@     @@    @ @      @     @@    @ ",
			"@   @ @ @ @         @      @@    @ @      @          @  ",
			"@  @  @   @    @@@@@  @@@@@ @@@@@@@ @@@@@ @@@@@@    @   ",
			"@ @   @   @   @            @     @       @@     @  @    ",
			" @   @    @   @      @     @     @ @     @@     @  @    ",
			"  @@@   @@@@@ @@@@@@@ @@@@@      @  @@@@@  @@@@@   @    ",

			" @@@@@  @@@@@          @@@      @           @     @@@@@ ",
			"@     @@     @  @@@    @@@     @             @   @     @",
			"@     @@     @  @@@           @     @@@@@     @        @",
			" @@@@@  @@@@@@         @@@   @                 @     @@ ",
			"@     @      @         @@@    @     @@@@@     @     @   ",
			"@     @@     @  @@@     @      @             @          ",
			" @@@@@  @@@@@   @@@    @        @           @       @   ",

			" @@@@@    @   @@@@@@  @@@@@ @@@@@@ @@@@@@@@@@@@@@ @@@@@ ",
			"@     @  @ @  @     @@     @@     @@      @      @     @",
			"@ @@@ @ @   @ @     @@      @     @@      @      @      ",
			"@ @ @ @@     @@@@@@@ @      @     @@@@@@  @@@@@  @  @@@@",
			"@ @@@@ @@@@@@@@     @@      @     @@      @      @     @",
			"@     @@     @@     @@     @@     @@      @      @     @",
			" @@@@@ @     @@@@@@@  @@@@@ @@@@@@ @@@@@@@@       @@@@@ ",

			"@     @  @*@        @@    @ @      @     @@     @@@@@@@@",
			"@     @   @         @@   @  @      @@   @@@@    @@     @",
			"@     @   @         @@  @   @      @ @ @ @@ @   @@     @",
			"@@@@@@@   @         @@@@    @      @  @  @@  @  @@     @",
			"@     @   @   @     @@  @   @      @     @@   @ @@     @",
			"@     @   @   @     @@   @  @      @     @@    @@@     @",
			"@     @  @@@   @@@@@ @    @ @@@@@@@@     @@     @@@@@@@@",

			"@@@@@@  @@@@@ @@@@@@  @@@@@ @@@@@@@@     @@     @@     @",
			"@     @@     @@     @@     @   @   @     @@     @@  @  @",
			"@     @@     @@     @@         @   @     @@     @@  @  @",
			"@@@@@@ @     @@@@@@@  @@@@@    @   @     @@     @@  @  @",
			"@      @   @ @@   @        @   @   @     @ @   @ @  @  @",
			"@      @    @ @    @ @     @   @   @     @  @ @  @  @  @",
			"@       @@@@ @@     @ @@@@@    @    @@@@@    @    @@ @@ ",

			"@     @@     @@@@@@@@ @@@@@ @     @ @@@@@    @          ",
			" @   @  @   @      @  @      @   @      @   @ @         ",
			"  @ @    @ @      @   @     @@@@@@@     @  @   @        ",
			"   @      @      @    @        @        @               ",
			"  @ @     @     @     @     @@@@@@@     @               ",
			" @   @    @    @      @        @        @               ",
			"@     @   @   @@@@@@@ @@@@@    @    @@@@@        @@@@@@@",

			"  @@@                                                   ",
			"  @@@     @@   @@@@@   @@@@  @@@@@  @@@@@@ @@@@@@  @@@@ ",
			"   @     @  @  @    @ @    @ @    @ @      @      @    @",
			"    @   @    @ @@@@@  @      @    @ @@@@@  @@@@@  @     ",
			"        @@@@@@ @    @ @      @    @ @      @      @  @@@",
			"        @    @ @    @ @    @ @    @ @      @      @    @",
			"        @    @ @@@@@   @@@@  @@@@@  @@@@@@ @       @@@@ ",

			"                                                        ",
			" @    @    @        @ @    @ @      @    @ @    @  @@@@ ",
			" @    @    @        @ @   @  @      @@  @@ @@   @ @    @",
			" @@@@@@    @        @ @@@@   @      @ @@ @ @ @  @ @    @",
			" @    @    @        @ @  @   @      @    @ @  @ @ @    @",
			" @    @    @   @    @ @   @  @      @    @ @   @@ @    @",
			" @    @    @    @@@@  @    @ @@@@@@ @    @ @    @  @@@@ ",

			"                                                        ",
			" @@@@@   @@@@  @@@@@   @@@@   @@@@@ @    @ @    @ @    @",
			" @    @ @    @ @    @ @         @   @    @ @    @ @    @",
			" @    @ @    @ @    @  @@@@     @   @    @ @    @ @    @",
			" @@@@@  @  @ @ @@@@@       @    @   @    @ @    @ @ @@ @",
			" @      @   @  @   @  @    @    @   @    @  @  @  @@  @@",
			" @       @@@ @ @    @  @@@@     @    @@@@    @@   @    @",

			"                       @@@     @     @@@   @@    @@@@@@@",
			" @    @  @   @ @@@@@@ @        @        @ @  @  @@@@@@@@",
			"  @  @    @ @      @  @        @        @     @@ @@@@@@@",
			"   @@      @      @  @@                 @@       @@@@@@@",
			"   @@      @     @    @        @        @        @@@@@@@",
			"  @  @     @    @     @        @        @        @@@@@@@",
			" @    @    @   @@@@@@  @@@     @     @@@         @@@@@@@" };

	public static void main(String[] args) {
		test01();
	}

	public static void test01() {
		// for (int i = 0; i < 128; i++) {
		// int a = (char) i + ((char) ' ');
		// System.out.println(a + "==>" + ((char) a));
		// }
		String src = " !\"#$%&'" + "()*+,-./" + "01234567" + "89:;<=>?"
				+ "@ABCDEFG" + "HIJKLMNO" + "PQRSTUVW" + "XYZ[\\]^_"
				+ "`abcdefg" + "hijklmno" + "pqrstuvw" + "xyz{|}~";
		// !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_
		src = "AaBbCcDd{|}~±≤≥¥µ";
		src = "NotFound!";
		List<String> list = Banner7.banner(src);
		for (String element : list) {
			System.out.println("=>" + element);
		}
	}

	// cur:~(126)
	// cur:±(65393)
	// cur:≤(65394)
	// cur:≥(65395)
	public static List<String> banner(String src) {
		return banner(src, LIMIT, FORE, BACK);
	}
	public static List<String> banner(String src, int limit, String fore,String back) {
		char sp = ' ';
		List<String> result = new ArrayList();
		StringBuffer[] buffs = new StringBuffer[HEIGHT];
		for (int i = 0; i < buffs.length; i++) {
			buffs[i] = new StringBuffer();
		}
		int len = src.length();
		if (len > limit)
			len = limit;
		String mark;
		int ind = 0;
		for (int b = 0; b < len; b++) {
			for (int seq = 0; seq < buffs.length; seq++) {
				buffs[seq].append(back);
			}
			char cur = src.charAt(b);
			// System.out.println("cur:" + cur + "(" + (int) cur + ")");
			if (cur < sp || (sp + 95) < cur) {
				ind = 95;
			} else {
				ind = cur - sp;
			}
			int row = (ind / 8 * HEIGHT);
			int col = (ind % 8 * WIDTH);
			// System.out.println("ind:" + ind);
			// System.out.println("=>" + ((char) (ind + (char) ' ')));
			for (int c = 0; c < WIDTH; c++) {
				// System.out.println("col=>" + (col + c));
				for (int seq = 0; seq < buffs.length; seq++) {
					if (glyphs[row + seq].charAt(col + c) == '@') {
						mark = fore;
					} else {
						mark = back;
					}
					buffs[seq].append(mark);
				}
			}
		}
		for (int seq = 0; seq < buffs.length; seq++) {
			result.add(buffs[seq].toString());
		}
		return result;
	}
}
