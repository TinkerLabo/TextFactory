package kyPkg.uDateTime;

import java.util.*;

class Date4Holidays {

	public static int SECOND_MONDAY = -2;
	public static int THIRD_MONDAY = -3;

	int year;
	int month;
	int day;

	Date4Holidays(int y, int m, int d) {
		year = y;
		month = m;
		if (d == SECOND_MONDAY)
			day = secondMonday(y, m);
		else if (d == THIRD_MONDAY)
			day = thirdMonday(y, m);
		else
			day = d;
	}

	public Date4Holidays substituteIfSunday() {
		if (Cal.day_of_week(year, month, day) == 0)
			day += 1;
		return this;
	}

	// 2013-05-20 yuasa
	public static int firstSatday(int y, int m) {
		// System.out.println("debug2:" + ((6 - debug) % 7));
		return (6 - Cal.day_of_week(y, m, 1)) % 7 + 1;
		// 7‚Ì‚É‚O‚É‚È‚é‚æ‚¤‚É“‚V‚µ‚Ä‚¢‚éi“ú—j“ú‚Ü‚Å‚Ì‹——£‚ğo‚µ‚Ä‚¢‚éj
	}

	// 2013-05-20 yuasa
	public static int firstSunday(int y, int m) {
		return (7 - Cal.day_of_week(y, m, 1)) % 7 + 1;
		// 7‚Ì‚É‚O‚É‚È‚é‚æ‚¤‚É“‚V‚µ‚Ä‚¢‚éi“ú—j“ú‚Ü‚Å‚Ì‹——£‚ğo‚µ‚Ä‚¢‚éj
	}

	public static int firstMonday(int y, int m) {
		return (7 - Cal.day_of_week(y, m, 7)) % 7 + 1;
		/*
		 * This expression is just a little magic. Verify 7 patterns instead to
		 * prove over modulo algebra.
		 */
	}

	public static int secondMonday(int y, int m) {
		return (7 - Cal.day_of_week(y, m, 7)) % 7 + 8;
		/*
		 * This expression is just a little magic. Verify 7 patterns instead to
		 * prove over modulo algebra.
		 */
	}

	public static int thirdMonday(int y, int m) {
		return (7 - Cal.day_of_week(y, m, 7)) % 7 + 15;
	}

	/*
	 * “ú–{‚É‚¨‚¯‚ét•ª“_‚Ì’Ê‰ß“ú (¼—ï1850”N‚©‚ç2150”N‚Ü‚Å)
	 * 
	 * ŒvZ®‚Íu‚±‚æ‚İ•Ö—˜’ vP¯ĞŒú¶Št(1983)‚ğQÆ‚µ‚½B
	 * 
	 * ‘–¯‚Ì‹x“ú‚Æ‚µ‚Ä‚Ìt•ª‚Ì“ú‚ÍA‘O”N‚Ì2Œ‰“ú•t‚¯‚ÌŠ¯•ñ(‘—§“V•¶‘ä) ‚É‚æ‚èŒˆ’è‚·‚éB
	 */
	public static Date4Holidays vernal_equinox(int y) {
		if (y <= 1850) {
			return null;
		} else if (y < 1900) {
			int d = (int) (19.8277 + 0.242194 * (y - 1980) - (y - 1983) / 4);
			return new Date4Holidays(y, 3, d);
		} else if (y < 1980) {
			int d = (int) (20.8357 + 0.242194 * (y - 1980) - (y - 1983) / 4);
			return new Date4Holidays(y, 3, d);
		} else if (y < 2100) {
			int d = (int) (20.8431 + 0.242194 * (y - 1980) - (y - 1980) / 4);
			return new Date4Holidays(y, 3, d);
		} else if (y <= 2150) {
			int d = (int) (21.8510 + 0.242194 * (y - 1980) - (y - 1980) / 4);
			return new Date4Holidays(y, 3, d);
		} else /* if (2150 < y) */{
			return null;
		}
	}

	/*
	 * “ú–{‚É‚¨‚¯‚éH•ª“_‚Ì’Ê‰ß“ú (¼—ï1850”N‚©‚ç2150”N‚Ü‚Å)
	 * 
	 * ŒvZ®‚Íu‚±‚æ‚İ•Ö—˜’ vP¯ĞŒú¶Št(1983)‚ğQÆ‚µ‚½
	 * 
	 * ‘–¯‚Ì‹x“ú‚Æ‚µ‚Ä‚ÌH•ª‚Ì“ú‚ÍA‘O”N‚Ì2Œ‰“ú•t‚¯‚ÌŠ¯•ñ(‘—§“V•¶‘ä) ‚É‚æ‚èŒˆ’è‚·‚éB
	 */
	public static Date4Holidays autumnal_equinox(int y) {
		if (y <= 1850) {
			return null;
		}
		if (y < 1900) {
			int d = (int) (22.2588 + 0.242194 * (y - 1980) - (y - 1983) / 4);
			return new Date4Holidays(y, 9, d);
		} else if (y < 1980) {
			int d = (int) (23.2588 + 0.242194 * (y - 1980) - (y - 1983) / 4);
			return new Date4Holidays(y, 9, d);
		} else if (y < 2100) {
			int d = (int) (23.2488 + 0.242194 * (y - 1980) - (y - 1980) / 4);
			return new Date4Holidays(y, 9, d);
		} else if (y <= 2150) {
			int d = (int) (24.2488 + 0.242194 * (y - 1980) - (y - 1980) / 4);
			return new Date4Holidays(y, 9, d);
		} else /* if (2150 < y) */{
			return null;
		}
	}
}

public class Holidays {
	final static int SECOND_MONDAY = Date4Holidays.SECOND_MONDAY;
	final static int THIRD_MONDAY = Date4Holidays.THIRD_MONDAY;

	Vector holidays;

	Holidays(int year) {
		holidays = holidaysInYear(year);
	}

	/*
	 * “ú–{‘‚Ìj“ú‚Íu‘–¯‚Ìj“ú‚ÉŠÖ‚·‚é–@—¥v(º˜a23”N–@—¥‘æ178†)‚É]‚¤
	 * 
	 * Œ³“ú º˜a23(1948)”N7Œ20“ú{s 1Œ1“ú ¬l‚Ì“ú º˜a41(1966)”N6Œ25“ú{s 1Œ15“ú
	 * •½¬12(2000)”N1Œ1“ú{s 1Œ‘æ2Œ—j“ú Œš‘‹L”O“ú º˜a41(1966)”N12Œ9“ú{s 2Œ11“ú t•ª‚Ì“ú
	 * º˜a23(1948)”N7Œ20“ú{s t•ª“ú ‚İ‚Ç‚è‚Ì“ú •½¬Œ³(1989)”N2Œ17“ú{s 4Œ29“ú Œ›–@‹L”O“ú
	 * º˜a23(1948)”N7Œ20“ú{s 5Œ3“ú ‚±‚Ç‚à‚Ì“ú º˜a23(1948)”N7Œ20“ú{s 5Œ5“ú ŠC‚Ì“ú •½¬
	 * 8(1996)”N1Œ1“ú{s 7Œ20“ú •½¬15(2003)”N1Œ1“ú{s 7Œ‘æ3Œ—j“ú Œh˜V‚Ì“ú º˜a41(1966)”N6Œ25“ú{s
	 * 9Œ15“ú •½¬15(2003)”N1Œ1“ú{s 9Œ‘æ3Œ—j“ú H•ª‚Ì“ú º˜a23(1948)”N7Œ20“ú{s H•ª“ú ‘Ìˆç‚Ì“ú
	 * º˜a41(1966)”N6Œ25“ú{s 10Œ10“ú •½¬12(2000)”N1Œ1“ú{s 10Œ‘æ2Œ—j“ú •¶‰»‚Ì“ú
	 * º˜a23(1948)”N7Œ20“ú{s 11Œ3“ú ‹Î˜JŠ´Ó‚Ì“ú º˜a23(1948)”N7Œ20“ú{s 11Œ23“ú “Vc’a¶“ú
	 * º˜a23(1948)”N7Œ20“ú{s 4Œ29“ú •½¬Œ³(1989)”N2Œ17“ú{s 12Œ23“ú
	 * 
	 * U‚è‘Ö‚¦‹x“ú‹K‘¥ º˜a48(1973)”N4Œ12“ú{s u‘–¯‚Ìj“úv‚ª“ú—j“ú‚É‚ ‚½‚é‚Æ‚«‚ÍA‚»‚Ì—‚“ú‚ğ‹x“ú‚Æ‚·‚éB ‘–¯‚Ì‹x“ú
	 * º˜a60(1985)”N12Œ27“ú 5Œ4“ú
	 * 
	 * –¾me‰¤Œ‹¥‚Ì‹V º˜a34(1959)”N3Œ17“ú{s 1959”N4Œ10“ú ‘å‘r‚Ì—ç •½¬Œ³(1989)”N2Œ17“ú{s 1989”N2Œ24“ú
	 * ‘¦ˆÊ‚Ì‹V •½¬ 2(1990)”N6Œ 1“ú{s 1990”N11Œ12“ú “¿me‰¤Œ‹¥‚Ì‹V •½¬ 5(1993)”N4Œ30“ú{s 1993”N6Œ9“ú
	 * 
	 * 
	 * –@—¥ğ•¶A‚¨‚æ‚Ñ‰ü³—š—ğ‚ÍŸ‚ğQÆ‚µ‚½ http://www2s.biglobe.ne.jp/~law/law/ldb/S23H0178.htm
	 * 
	 * •½¬13”N6Œ22“ú‰ü³A•½¬15”N1Œ1“ú{s u‘–¯‚Ìj“ú‚ÉŠÖ‚·‚é–@—¥v‚Ì‰ü³‚ÉŠÖ‚·‚éƒAƒiƒEƒ“ƒX(“àŠt•{)
	 * http://www8.cao.go.jp/chosei/shukujitsu/gaiyou.html
	 * 
	 * ‚»‚Ì‘¼A•½¬13”N6Œ22“ú‰ü³ˆÈ‘O‚Ì–@—¥ğ•¶ http://list.room.ne.jp/~lawtext/1948L178.html
	 * http://www.ron.gr.jp/law/law/syukujit.htm
	 * 
	 * 
	 * í‘O‚Ìj“ú
	 * 
	 * Œ³nÕ –¾¡6(1871)”N10Œ14“ú{s 1Œ3“ú V”N‰ƒ‰ï –¾¡6(1871)”N10Œ14“ú{s 1Œ5“ú F–¾“VcÕ
	 * –¾¡6(1871)”N10Œ14“ú{s 1Œ30“ú ‘å³Œ³(1912)”N9Œ3“ú”p~ ‹IŒ³ß –¾¡6(1871)”N10Œ14“ú{s 2Œ11“ú
	 * º˜a23(1948)”N7Œ20“ú”p~ t‹Gc—ìÕ –¾¡11(1876)”N6Œ5“ú{s t•ª“ú _•“VcÕ –¾¡6(1871)”N10Œ14“ú{s
	 * 4Œ3“ú “V’·ß(º˜a) º˜a2(1927)”N3Œ3“ú{s 4Œ29“ú –¾¡“VcÕ ‘å³Œ³(1912)”N9Œ3“ú{s 7Œ30“ú
	 * º˜a2(1927)”N3Œ3“ú”p~ “V’·ß(‘å³) ‘å³Œ³(1912)”N9Œ3“ú{s 8Œ31“ú º˜a2(1927)”N3Œ3“ú”p~
	 * “V’·ßj“ú(‘å³) ‘å³2(1913)”N7Œ16“ú{s 10Œ31“ú º˜a2(1927)”N3Œ3“ú”p~ _¦Õ
	 * –¾¡6(1871)”N10Œ14“ú{s 9Œ17“ú –¾¡12(1877)”N7Œ5“ú{s 10Œ17“ú H‹Gc—ìÕ –¾¡11(1876)”N6Œ5“ú{s
	 * H•ª“ú “V’·ß(–¾¡) –¾¡6(1871)”N10Œ14“ú{s 11Œ3“ú ‘å³Œ³(1912)”N9Œ3“ú”p~ –¾¡ß
	 * º˜a2(1927)”N3Œ3“ú{s 11Œ3“ú V¦Õ –¾¡6(1871)”N10Œ14“ú{s 11Œ23“ú ‘å³“VcÕ
	 * º˜a2(1927)”N3Œ3“ú{s 12Œ25“ú
	 * 
	 * ˆÈã‚ÍAº˜a23”N7Œ20“úu‘–¯‚Ìj“ú‚ÉŠÖ‚·‚é–@—¥v‚É‚æ‚Á‚Ä”p~‚³‚ê‚½B
	 * 
	 * ‘å—ç‚ÉŠÖ‚·‚é‹x“ú ‘¦ˆÊ‚Ì—ç(‘å³) ‘å³4(1915)”N9Œ21“ú{s 1915”N11Œ10“ú ‘å¦Õ(‘å³) ‘å³4(1915)”N9Œ21“ú{s
	 * 1915”N11Œ14“ú ‘¦ˆÊ‚Ì—ç(‘å³) ‘å³4(1915)”N9Œ21“ú{s 1915”N11Œ16“ú ‘¦ˆÊ‚Ì—ç(º˜a) º˜a3(1928)”N9Œ
	 * 8“ú{s 1928”N11Œ10“ú ‘å¦Õ(º˜a) º˜a3(1928)”N9Œ21“ú{s 1928”N11Œ14“ú ‘¦ˆÊ‚Ì—ç(º˜a)
	 * º˜a3(1928)”N9Œ21“ú{s 1928”N11Œ16“ú
	 * 
	 * 
	 * ğ•¶‚ÍAŸ‚É]‚Á‚½B http://homepage1.nifty.com/gyouseinet/kyujitsu.htm
	 * Ÿ‚àQl‚É‚È‚é‚©‚à‚µ‚ê‚È‚¢B
	 * http://www.nifty.ne.jp/forum/ffortune/calen/calen/yomi99/yomi027.htm
	 * 
	 * 
	 * ¼—ï•ÏŠ·—p‚Ìƒƒ‚: –¾¡0”N = 1865 ‘å³0”N = 1911 º˜a0”N = 1925 •½¬0”N = 1988
	 */
	Vector holidaysInYear(int y) {
		Vector v = new Vector(15);

		// Œ³“ú
		if (1948 < y && y <= 1973)
			v.addElement(new Date4Holidays(y, 1, 1));
		else if (1973 < y)
			v.addElement(new Date4Holidays(y, 1, 1).substituteIfSunday());

		// Œ³nÕ(í‘O)
		if (1871 < y && y <= 1948)
			v.addElement(new Date4Holidays(y, 1, 3));

		// V”N‰ƒ‰ï(í‘O)
		if (1871 < y && y <= 1948)
			v.addElement(new Date4Holidays(y, 1, 5));

		// ¬l‚Ì“ú
		if (1966 < y && y <= 1973)
			v.addElement(new Date4Holidays(y, 1, 15));
		if (1973 < y && y < 2000)
			v.addElement(new Date4Holidays(y, 1, 15).substituteIfSunday());
		else if (2000 <= y)
			v.addElement(new Date4Holidays(y, 1, SECOND_MONDAY));

		// F–¾“VcÕ(–¾¡)
		if (1871 < y && y <= 1912)
			v.addElement(new Date4Holidays(y, 1, 30));

		// Œš‘‹L”O“úA‹IŒ³ß(í‘O)
		if (1871 < y && y <= 1948 || 1966 < y && y <= 1973)
			v.addElement(new Date4Holidays(y, 2, 11));
		else if (1973 < y)
			v.addElement(new Date4Holidays(y, 2, 11).substituteIfSunday());

		// t•ª‚Ì“úAt‹Gc—ìÕ(í‘O)
		Date4Holidays ve = Date4Holidays.vernal_equinox(y);
		if (ve != null) {
			if (1876 < y && y <= 1973)
				v.addElement(ve);
			else if (1973 < y)
				v.addElement(ve.substituteIfSunday());
		}

		// _•“VcÕ(í‘O)
		if (1871 < y && y <= 1948)
			v.addElement(new Date4Holidays(y, 4, 3));

		// ‚İ‚Ç‚è‚Ì“ú(•½¬)A“Vc’a¶“ú(º˜a)A“V’·ß(º˜aí‘O)
		if (1927 <= y && y < 1973)
			v.addElement(new Date4Holidays(y, 4, 29));
		else if (1973 <= y)
			v.addElement(new Date4Holidays(y, 4, 29).substituteIfSunday());

		// Œ›–@‹L”O“ú
		if (1948 < y && y < 1973)
			v.addElement(new Date4Holidays(y, 5, 3));
		else if (1973 <= y)
			v.addElement(new Date4Holidays(y, 5, 3).substituteIfSunday());

		// ‘–¯‚Ìj“ú‚ÉŠÖ‚·‚é–@—¥‘æ3ğ3€(‘–¯‚Ì‹x“ú)
		if (1985 < y)
			v.addElement(new Date4Holidays(y, 5, 4));

		// ‚±‚Ç‚à‚Ì“ú
		if (1948 < y && y < 1973)
			v.addElement(new Date4Holidays(y, 5, 5));
		else if (1973 <= y)
			v.addElement(new Date4Holidays(y, 5, 5).substituteIfSunday());

		// ŠC‚Ì“ú
		if (1996 <= y && y < 2003)
			v.addElement(new Date4Holidays(y, 7, 20).substituteIfSunday());
		else if (1996 <= y)
			v.addElement(new Date4Holidays(y, 7, THIRD_MONDAY));

		// –¾¡“VcÕ(‘å³)
		if (1912 < y && y < 1927)
			v.addElement(new Date4Holidays(y, 7, 30));

		// “V’·ß(‘å³)
		if (1912 < y && y < 1927)
			v.addElement(new Date4Holidays(y, 8, 30));

		// Œh˜V‚Ì“ú
		if (1966 <= y && y < 1973)
			v.addElement(new Date4Holidays(y, 9, 15));
		else if (1973 <= y && y < 2003)
			v.addElement(new Date4Holidays(y, 9, 15).substituteIfSunday());
		else if (2003 <= y)
			v.addElement(new Date4Holidays(y, 9, THIRD_MONDAY));

		// H•ª‚Ì“úAH‹Gc—ìÕ(í‘O)
		Date4Holidays ae = Date4Holidays.autumnal_equinox(y);
		if (ae != null) {
			if (1876 <= y && y < 1973)
				v.addElement(ae);
			else if (1973 <= y)
				v.addElement(ae.substituteIfSunday());
		}

		// ‘Ìˆç‚Ì“ú
		if (1966 <= y && y < 1973)
			v.addElement(new Date4Holidays(y, 10, 10));
		else if (1973 <= y && y < 2000)
			v.addElement(new Date4Holidays(y, 10, 10).substituteIfSunday());
		else if (2000 <= y)
			v.addElement(new Date4Holidays(y, 10, SECOND_MONDAY));

		// _¦(‚©‚ñ‚¶‚å‚¤)Õ(í‘O)
		if (1871 < y && y < 1877)
			v.addElement(new Date4Holidays(y, 9, 17));
		else if (1877 <= y && y < 1948)
			v.addElement(new Date4Holidays(y, 10, 17));

		// “V’·ßj“ú(‘å³)
		if (1913 <= y && y < 1927)
			v.addElement(new Date4Holidays(y, 10, 31));

		// •¶‰»‚Ì“úA–¾¡ß(º˜aí‘O)A“V’·ß(–¾¡)
		if (1871 <= y && y < 1912 || 1927 <= y && y < 1973)
			v.addElement(new Date4Holidays(y, 11, 3));
		else if (1973 <= y)
			v.addElement(new Date4Holidays(y, 11, 3).substituteIfSunday());

		// ‹Î˜JŠ´Ó‚Ì“úAV¦(‚É‚¢‚È‚ß)Õ(í‘O)
		if (1871 <= y && y < 1973)
			v.addElement(new Date4Holidays(y, 11, 23));
		else if (1973 <= y)
			v.addElement(new Date4Holidays(y, 11, 23).substituteIfSunday());

		// “Vc’a¶“ú(•½¬)
		if (1989 <= y)
			v.addElement(new Date4Holidays(y, 12, 23).substituteIfSunday());

		// ‘å³“VcÕ(º˜aí‘O)
		if (1927 <= y && y < 1948)
			v.addElement(new Date4Holidays(y, 12, 25));

		// –¾me‰¤Œ‹¥‚Ì‹V
		if (y == 1959)
			v.addElement(new Date4Holidays(y, 4, 10));

		// º˜a“Vc‘å‘r‚Ì—ç
		if (y == 1989)
			v.addElement(new Date4Holidays(y, 2, 24));

		// ‘¦ˆÊ‚Ì‹V
		if (y == 1990)
			v.addElement(new Date4Holidays(y, 11, 12));

		// “¿me‰¤Œ‹¥‚Ì‹V
		if (y == 1993)
			v.addElement(new Date4Holidays(y, 6, 9));

		// í‘O‚Ì‘å—ç
		if (y == 1915 || y == 1928) {
			v.addElement(new Date4Holidays(y, 11, 10));
			v.addElement(new Date4Holidays(y, 11, 14));
			v.addElement(new Date4Holidays(y, 11, 16));
		}

		return v;
	}

	boolean isHoliday(int m, int d) {
		for (int i = 0; i < holidays.size(); i++) {
			Date4Holidays date = (Date4Holidays) holidays.elementAt(i);
			if (date.month == m && date.day == d)
				return true;
		}
		return false;
	}

	boolean isHoliday(Date4Holidays md) {
		return isHoliday(md.month, md.day);
	}

	public static void main(String[] argv) {
		// testFirstSunday();
		System.out.println("getThisFirstSunday():" + getThisFirstSunday());
	}

	public static String getThisFirstSunday() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("JST"));
		int year = calendar.get(Calendar.YEAR);
		int month = (calendar.get(Calendar.MONTH) + 1);
		int day = Date4Holidays.firstSunday(year, month);
		return DateCalc.getYYYYMMDD(year, month, day, "");
	}

	public static void testFirstSunday() {
		for (int y = 2012; y <= 2030; y++) {
			for (int m = 1; m <= 12; m++) {
				int d = Date4Holidays.firstSunday(y, m);
				int debug = Cal.day_of_week(y, m, d);
				System.out.println(y + " " + m + "/" + d + " => " + debug);
			}
		}
	}

}
