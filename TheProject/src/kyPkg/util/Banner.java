package kyPkg.util;

import java.util.ArrayList;
import java.util.List;

//文字の高さが11桁版
public class Banner {
	static int limit = 10;
	static String back = " ";
	static String fore = "X";
	private static Inf_Griphs griphs;
	//予めセットしておけば、グリフを変更できる　
	//Banner.setGriphs(new Griphs7());
	public static void setGriphs(Inf_Griphs griphs) {
		Banner.griphs = griphs;
	}
	public static void setLimit(int pLimit) {
		limit = pLimit;
	}
	public static void setBack(String pBack) {
		back = pBack;
	}
	public static void setFore(String pFore) {
		fore = pFore;
	}
	// cur:~(126)
	// cur:ｱ(65393)
	// cur:ｲ(65394)
	// cur:ｳ(65395)
	public static void bannerPrint(int limit, String src) {
		List<String> list = banner(src, limit, fore, back);
		for (String element : list) {
			System.out.println(element);
		}
	}

	public static List<String> banner(String src) {
		return banner(src, limit, fore, back);
	}

	public static List<String> banner(String src, int limit, String fore,
			String back) {
		if (griphs == null)
			griphs = new Griphs11();
		char sp = ' ';
		List<String> result = new ArrayList();
		StringBuffer[] buffs = new StringBuffer[griphs.getHeight()];
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
			int row = (ind / 8 * griphs.getHeight());
			int col = (ind % 8 * griphs.getWidth());
			// System.out.println("ind:" + ind);
			// System.out.println("=>" + ((char) (ind + (char) ' ')));
			for (int c = 0; c < griphs.getWidth(); c++) {
				// System.out.println("col=>" + (col + c));
				for (int seq = 0; seq < buffs.length; seq++) {
					if (griphs.getGlyphs()[row + seq].charAt(col + c) == '@') {
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
	// ########################################################################
	// Main
	// ########################################################################
	public static void main(String[] args) {
		test01();
	}

	public static void test00() {
		String src = " !\"#$%&'" + "()*+,-./" + "01234567" + "89:;<=>?"
				+ "@ABCDEFG" + "HIJKLMNO" + "PQRSTUVW" + "XYZ[\\]^_"
				+ "`abcdefg" + "hijklmno" + "pqrstuvw" + "xyz{|}~";
		// !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_
		src = "AaBbCcDd{|}~ｱｲｳｴｵ";
		List<String> list = Banner.banner(src);
		for (String element : list) {
			System.out.println("=>" + element);
		}
	}

	public static void test01() {
		// for (int i = 0; i < 128; i++) {
		// int a = (char) i + ((char) ' ');
		// System.out.println(a + "==>" + ((char) a));
		// }
		List<String> strlist = new ArrayList();
		strlist.add(" !\"#$%&'");
		strlist.add("()*+,-./");
		strlist.add("01234567");
		strlist.add("89:;<=>?");
		strlist.add("@ABCDEFG");
		strlist.add("HIJKLMNO");
		strlist.add("PQRSTUVW");
		strlist.add("XYZ[\\]^_");
		strlist.add("`abcdefg");
		strlist.add("hijklmno");
		strlist.add("pqrstuvw");
		strlist.add("xyz{|}~");
		strlist.add("(1)@100%Orange");
		strlist.add("<A>Hello&GoodBye?!");
		strlist.add("#Error(1234)");
		strlist.add("NotFound=>49001801234");
		strlist.add("(T_T)ERROR!");
		strlist.add("(^_^)/NormalEnd!!");
		strlist.add("(:-))Good!");
		strlist.add("(QPR)");
		for (String src : strlist) {
			System.out.println("banner " + src);
			Banner.bannerPrint(30, src);
			// List<String> list = Banner11.banner(src, 30, "X", " ");
			// for (String element : list) {
			// System.out.println(element);
			// }
		}
	}

}
