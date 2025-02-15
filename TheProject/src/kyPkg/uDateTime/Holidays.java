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
		// 7の時に０になるように％７している（日曜日までの距離を出している）
	}

	// 2013-05-20 yuasa
	public static int firstSunday(int y, int m) {
		return (7 - Cal.day_of_week(y, m, 1)) % 7 + 1;
		// 7の時に０になるように％７している（日曜日までの距離を出している）
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
	 * 日本における春分点の通過日 (西暦1850年から2150年まで)
	 * 
	 * 計算式は「こよみ便利帳」恒星社厚生閣(1983)を参照した。
	 * 
	 * 国民の休日としての春分の日は、前年の2月初日付けの官報(国立天文台) により決定する。
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
	 * 日本における秋分点の通過日 (西暦1850年から2150年まで)
	 * 
	 * 計算式は「こよみ便利帳」恒星社厚生閣(1983)を参照した
	 * 
	 * 国民の休日としての秋分の日は、前年の2月初日付けの官報(国立天文台) により決定する。
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
	 * 日本国の祝日は「国民の祝日に関する法律」(昭和23年法律第178号)に従う
	 * 
	 * 元日 昭和23(1948)年7月20日施行 1月1日 成人の日 昭和41(1966)年6月25日施行 1月15日
	 * 平成12(2000)年1月1日施行 1月第2月曜日 建国記念日 昭和41(1966)年12月9日施行 2月11日 春分の日
	 * 昭和23(1948)年7月20日施行 春分日 みどりの日 平成元(1989)年2月17日施行 4月29日 憲法記念日
	 * 昭和23(1948)年7月20日施行 5月3日 こどもの日 昭和23(1948)年7月20日施行 5月5日 海の日 平成
	 * 8(1996)年1月1日施行 7月20日 平成15(2003)年1月1日施行 7月第3月曜日 敬老の日 昭和41(1966)年6月25日施行
	 * 9月15日 平成15(2003)年1月1日施行 9月第3月曜日 秋分の日 昭和23(1948)年7月20日施行 秋分日 体育の日
	 * 昭和41(1966)年6月25日施行 10月10日 平成12(2000)年1月1日施行 10月第2月曜日 文化の日
	 * 昭和23(1948)年7月20日施行 11月3日 勤労感謝の日 昭和23(1948)年7月20日施行 11月23日 天皇誕生日
	 * 昭和23(1948)年7月20日施行 4月29日 平成元(1989)年2月17日施行 12月23日
	 * 
	 * 振り替え休日規則 昭和48(1973)年4月12日施行 「国民の祝日」が日曜日にあたるときは、その翌日を休日とする。 国民の休日
	 * 昭和60(1985)年12月27日 5月4日
	 * 
	 * 明仁親王結婚の儀 昭和34(1959)年3月17日施行 1959年4月10日 大喪の礼 平成元(1989)年2月17日施行 1989年2月24日
	 * 即位の儀 平成 2(1990)年6月 1日施行 1990年11月12日 徳仁親王結婚の儀 平成 5(1993)年4月30日施行 1993年6月9日
	 * 
	 * 
	 * 法律条文、および改正履歴は次を参照した http://www2s.biglobe.ne.jp/~law/law/ldb/S23H0178.htm
	 * 
	 * 平成13年6月22日改正、平成15年1月1日施行 「国民の祝日に関する法律」の改正に関するアナウンス(内閣府)
	 * http://www8.cao.go.jp/chosei/shukujitsu/gaiyou.html
	 * 
	 * その他、平成13年6月22日改正以前の法律条文 http://list.room.ne.jp/~lawtext/1948L178.html
	 * http://www.ron.gr.jp/law/law/syukujit.htm
	 * 
	 * 
	 * 戦前の祝日
	 * 
	 * 元始祭 明治6(1871)年10月14日施行 1月3日 新年宴会 明治6(1871)年10月14日施行 1月5日 孝明天皇祭
	 * 明治6(1871)年10月14日施行 1月30日 大正元(1912)年9月3日廃止 紀元節 明治6(1871)年10月14日施行 2月11日
	 * 昭和23(1948)年7月20日廃止 春季皇霊祭 明治11(1876)年6月5日施行 春分日 神武天皇祭 明治6(1871)年10月14日施行
	 * 4月3日 天長節(昭和) 昭和2(1927)年3月3日施行 4月29日 明治天皇祭 大正元(1912)年9月3日施行 7月30日
	 * 昭和2(1927)年3月3日廃止 天長節(大正) 大正元(1912)年9月3日施行 8月31日 昭和2(1927)年3月3日廃止
	 * 天長節祝日(大正) 大正2(1913)年7月16日施行 10月31日 昭和2(1927)年3月3日廃止 神嘗祭
	 * 明治6(1871)年10月14日施行 9月17日 明治12(1877)年7月5日施行 10月17日 秋季皇霊祭 明治11(1876)年6月5日施行
	 * 秋分日 天長節(明治) 明治6(1871)年10月14日施行 11月3日 大正元(1912)年9月3日廃止 明治節
	 * 昭和2(1927)年3月3日施行 11月3日 新嘗祭 明治6(1871)年10月14日施行 11月23日 大正天皇祭
	 * 昭和2(1927)年3月3日施行 12月25日
	 * 
	 * 以上は、昭和23年7月20日「国民の祝日に関する法律」によって廃止された。
	 * 
	 * 大礼に関する休日 即位の礼(大正) 大正4(1915)年9月21日施行 1915年11月10日 大嘗祭(大正) 大正4(1915)年9月21日施行
	 * 1915年11月14日 即位の礼(大正) 大正4(1915)年9月21日施行 1915年11月16日 即位の礼(昭和) 昭和3(1928)年9月
	 * 8日施行 1928年11月10日 大嘗祭(昭和) 昭和3(1928)年9月21日施行 1928年11月14日 即位の礼(昭和)
	 * 昭和3(1928)年9月21日施行 1928年11月16日
	 * 
	 * 
	 * 条文は、次に従った。 http://homepage1.nifty.com/gyouseinet/kyujitsu.htm
	 * 次も参考になるかもしれない。
	 * http://www.nifty.ne.jp/forum/ffortune/calen/calen/yomi99/yomi027.htm
	 * 
	 * 
	 * 西暦変換用のメモ: 明治0年 = 1865 大正0年 = 1911 昭和0年 = 1925 平成0年 = 1988
	 */
	Vector holidaysInYear(int y) {
		Vector v = new Vector(15);

		// 元日
		if (1948 < y && y <= 1973)
			v.addElement(new Date4Holidays(y, 1, 1));
		else if (1973 < y)
			v.addElement(new Date4Holidays(y, 1, 1).substituteIfSunday());

		// 元始祭(戦前)
		if (1871 < y && y <= 1948)
			v.addElement(new Date4Holidays(y, 1, 3));

		// 新年宴会(戦前)
		if (1871 < y && y <= 1948)
			v.addElement(new Date4Holidays(y, 1, 5));

		// 成人の日
		if (1966 < y && y <= 1973)
			v.addElement(new Date4Holidays(y, 1, 15));
		if (1973 < y && y < 2000)
			v.addElement(new Date4Holidays(y, 1, 15).substituteIfSunday());
		else if (2000 <= y)
			v.addElement(new Date4Holidays(y, 1, SECOND_MONDAY));

		// 孝明天皇祭(明治)
		if (1871 < y && y <= 1912)
			v.addElement(new Date4Holidays(y, 1, 30));

		// 建国記念日、紀元節(戦前)
		if (1871 < y && y <= 1948 || 1966 < y && y <= 1973)
			v.addElement(new Date4Holidays(y, 2, 11));
		else if (1973 < y)
			v.addElement(new Date4Holidays(y, 2, 11).substituteIfSunday());

		// 春分の日、春季皇霊祭(戦前)
		Date4Holidays ve = Date4Holidays.vernal_equinox(y);
		if (ve != null) {
			if (1876 < y && y <= 1973)
				v.addElement(ve);
			else if (1973 < y)
				v.addElement(ve.substituteIfSunday());
		}

		// 神武天皇祭(戦前)
		if (1871 < y && y <= 1948)
			v.addElement(new Date4Holidays(y, 4, 3));

		// みどりの日(平成)、天皇誕生日(昭和)、天長節(昭和戦前)
		if (1927 <= y && y < 1973)
			v.addElement(new Date4Holidays(y, 4, 29));
		else if (1973 <= y)
			v.addElement(new Date4Holidays(y, 4, 29).substituteIfSunday());

		// 憲法記念日
		if (1948 < y && y < 1973)
			v.addElement(new Date4Holidays(y, 5, 3));
		else if (1973 <= y)
			v.addElement(new Date4Holidays(y, 5, 3).substituteIfSunday());

		// 国民の祝日に関する法律第3条3項(国民の休日)
		if (1985 < y)
			v.addElement(new Date4Holidays(y, 5, 4));

		// こどもの日
		if (1948 < y && y < 1973)
			v.addElement(new Date4Holidays(y, 5, 5));
		else if (1973 <= y)
			v.addElement(new Date4Holidays(y, 5, 5).substituteIfSunday());

		// 海の日
		if (1996 <= y && y < 2003)
			v.addElement(new Date4Holidays(y, 7, 20).substituteIfSunday());
		else if (1996 <= y)
			v.addElement(new Date4Holidays(y, 7, THIRD_MONDAY));

		// 明治天皇祭(大正)
		if (1912 < y && y < 1927)
			v.addElement(new Date4Holidays(y, 7, 30));

		// 天長節(大正)
		if (1912 < y && y < 1927)
			v.addElement(new Date4Holidays(y, 8, 30));

		// 敬老の日
		if (1966 <= y && y < 1973)
			v.addElement(new Date4Holidays(y, 9, 15));
		else if (1973 <= y && y < 2003)
			v.addElement(new Date4Holidays(y, 9, 15).substituteIfSunday());
		else if (2003 <= y)
			v.addElement(new Date4Holidays(y, 9, THIRD_MONDAY));

		// 秋分の日、秋季皇霊祭(戦前)
		Date4Holidays ae = Date4Holidays.autumnal_equinox(y);
		if (ae != null) {
			if (1876 <= y && y < 1973)
				v.addElement(ae);
			else if (1973 <= y)
				v.addElement(ae.substituteIfSunday());
		}

		// 体育の日
		if (1966 <= y && y < 1973)
			v.addElement(new Date4Holidays(y, 10, 10));
		else if (1973 <= y && y < 2000)
			v.addElement(new Date4Holidays(y, 10, 10).substituteIfSunday());
		else if (2000 <= y)
			v.addElement(new Date4Holidays(y, 10, SECOND_MONDAY));

		// 神嘗(かんじょう)祭(戦前)
		if (1871 < y && y < 1877)
			v.addElement(new Date4Holidays(y, 9, 17));
		else if (1877 <= y && y < 1948)
			v.addElement(new Date4Holidays(y, 10, 17));

		// 天長節祝日(大正)
		if (1913 <= y && y < 1927)
			v.addElement(new Date4Holidays(y, 10, 31));

		// 文化の日、明治節(昭和戦前)、天長節(明治)
		if (1871 <= y && y < 1912 || 1927 <= y && y < 1973)
			v.addElement(new Date4Holidays(y, 11, 3));
		else if (1973 <= y)
			v.addElement(new Date4Holidays(y, 11, 3).substituteIfSunday());

		// 勤労感謝の日、新嘗(にいなめ)祭(戦前)
		if (1871 <= y && y < 1973)
			v.addElement(new Date4Holidays(y, 11, 23));
		else if (1973 <= y)
			v.addElement(new Date4Holidays(y, 11, 23).substituteIfSunday());

		// 天皇誕生日(平成)
		if (1989 <= y)
			v.addElement(new Date4Holidays(y, 12, 23).substituteIfSunday());

		// 大正天皇祭(昭和戦前)
		if (1927 <= y && y < 1948)
			v.addElement(new Date4Holidays(y, 12, 25));

		// 明仁親王結婚の儀
		if (y == 1959)
			v.addElement(new Date4Holidays(y, 4, 10));

		// 昭和天皇大喪の礼
		if (y == 1989)
			v.addElement(new Date4Holidays(y, 2, 24));

		// 即位の儀
		if (y == 1990)
			v.addElement(new Date4Holidays(y, 11, 12));

		// 徳仁親王結婚の儀
		if (y == 1993)
			v.addElement(new Date4Holidays(y, 6, 9));

		// 戦前の大礼
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
