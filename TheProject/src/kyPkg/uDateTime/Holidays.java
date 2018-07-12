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
		// 7�̎��ɂO�ɂȂ�悤�Ɂ��V���Ă���i���j���܂ł̋������o���Ă���j
	}

	// 2013-05-20 yuasa
	public static int firstSunday(int y, int m) {
		return (7 - Cal.day_of_week(y, m, 1)) % 7 + 1;
		// 7�̎��ɂO�ɂȂ�悤�Ɂ��V���Ă���i���j���܂ł̋������o���Ă���j
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
	 * ���{�ɂ�����t���_�̒ʉߓ� (����1850�N����2150�N�܂�)
	 * 
	 * �v�Z���́u����ݕ֗����v�P���Ќ����t(1983)���Q�Ƃ����B
	 * 
	 * �����̋x���Ƃ��Ă̏t���̓��́A�O�N��2�������t���̊���(�����V����) �ɂ�茈�肷��B
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
	 * ���{�ɂ�����H���_�̒ʉߓ� (����1850�N����2150�N�܂�)
	 * 
	 * �v�Z���́u����ݕ֗����v�P���Ќ����t(1983)���Q�Ƃ���
	 * 
	 * �����̋x���Ƃ��Ă̏H���̓��́A�O�N��2�������t���̊���(�����V����) �ɂ�茈�肷��B
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
	 * ���{���̏j���́u�����̏j���Ɋւ���@���v(���a23�N�@����178��)�ɏ]��
	 * 
	 * ���� ���a23(1948)�N7��20���{�s 1��1�� ���l�̓� ���a41(1966)�N6��25���{�s 1��15��
	 * ����12(2000)�N1��1���{�s 1����2���j�� �����L�O�� ���a41(1966)�N12��9���{�s 2��11�� �t���̓�
	 * ���a23(1948)�N7��20���{�s �t���� �݂ǂ�̓� ������(1989)�N2��17���{�s 4��29�� ���@�L�O��
	 * ���a23(1948)�N7��20���{�s 5��3�� ���ǂ��̓� ���a23(1948)�N7��20���{�s 5��5�� �C�̓� ����
	 * 8(1996)�N1��1���{�s 7��20�� ����15(2003)�N1��1���{�s 7����3���j�� �h�V�̓� ���a41(1966)�N6��25���{�s
	 * 9��15�� ����15(2003)�N1��1���{�s 9����3���j�� �H���̓� ���a23(1948)�N7��20���{�s �H���� �̈�̓�
	 * ���a41(1966)�N6��25���{�s 10��10�� ����12(2000)�N1��1���{�s 10����2���j�� �����̓�
	 * ���a23(1948)�N7��20���{�s 11��3�� �ΘJ���ӂ̓� ���a23(1948)�N7��20���{�s 11��23�� �V�c�a����
	 * ���a23(1948)�N7��20���{�s 4��29�� ������(1989)�N2��17���{�s 12��23��
	 * 
	 * �U��ւ��x���K�� ���a48(1973)�N4��12���{�s �u�����̏j���v�����j���ɂ�����Ƃ��́A���̗������x���Ƃ���B �����̋x��
	 * ���a60(1985)�N12��27�� 5��4��
	 * 
	 * ���m�e�������̋V ���a34(1959)�N3��17���{�s 1959�N4��10�� ��r�̗� ������(1989)�N2��17���{�s 1989�N2��24��
	 * ���ʂ̋V ���� 2(1990)�N6�� 1���{�s 1990�N11��12�� ���m�e�������̋V ���� 5(1993)�N4��30���{�s 1993�N6��9��
	 * 
	 * 
	 * �@���𕶁A����щ��������͎����Q�Ƃ��� http://www2s.biglobe.ne.jp/~law/law/ldb/S23H0178.htm
	 * 
	 * ����13�N6��22�������A����15�N1��1���{�s �u�����̏j���Ɋւ���@���v�̉����Ɋւ���A�i�E���X(���t�{)
	 * http://www8.cao.go.jp/chosei/shukujitsu/gaiyou.html
	 * 
	 * ���̑��A����13�N6��22�������ȑO�̖@���� http://list.room.ne.jp/~lawtext/1948L178.html
	 * http://www.ron.gr.jp/law/law/syukujit.htm
	 * 
	 * 
	 * ��O�̏j��
	 * 
	 * ���n�� ����6(1871)�N10��14���{�s 1��3�� �V�N���� ����6(1871)�N10��14���{�s 1��5�� �F���V�c��
	 * ����6(1871)�N10��14���{�s 1��30�� �吳��(1912)�N9��3���p�~ �I���� ����6(1871)�N10��14���{�s 2��11��
	 * ���a23(1948)�N7��20���p�~ �t�G�c��� ����11(1876)�N6��5���{�s �t���� �_���V�c�� ����6(1871)�N10��14���{�s
	 * 4��3�� �V����(���a) ���a2(1927)�N3��3���{�s 4��29�� �����V�c�� �吳��(1912)�N9��3���{�s 7��30��
	 * ���a2(1927)�N3��3���p�~ �V����(�吳) �吳��(1912)�N9��3���{�s 8��31�� ���a2(1927)�N3��3���p�~
	 * �V���ߏj��(�吳) �吳2(1913)�N7��16���{�s 10��31�� ���a2(1927)�N3��3���p�~ �_����
	 * ����6(1871)�N10��14���{�s 9��17�� ����12(1877)�N7��5���{�s 10��17�� �H�G�c��� ����11(1876)�N6��5���{�s
	 * �H���� �V����(����) ����6(1871)�N10��14���{�s 11��3�� �吳��(1912)�N9��3���p�~ ������
	 * ���a2(1927)�N3��3���{�s 11��3�� �V���� ����6(1871)�N10��14���{�s 11��23�� �吳�V�c��
	 * ���a2(1927)�N3��3���{�s 12��25��
	 * 
	 * �ȏ�́A���a23�N7��20���u�����̏j���Ɋւ���@���v�ɂ���Ĕp�~���ꂽ�B
	 * 
	 * ���Ɋւ���x�� ���ʂ̗�(�吳) �吳4(1915)�N9��21���{�s 1915�N11��10�� �另��(�吳) �吳4(1915)�N9��21���{�s
	 * 1915�N11��14�� ���ʂ̗�(�吳) �吳4(1915)�N9��21���{�s 1915�N11��16�� ���ʂ̗�(���a) ���a3(1928)�N9��
	 * 8���{�s 1928�N11��10�� �另��(���a) ���a3(1928)�N9��21���{�s 1928�N11��14�� ���ʂ̗�(���a)
	 * ���a3(1928)�N9��21���{�s 1928�N11��16��
	 * 
	 * 
	 * �𕶂́A���ɏ]�����B http://homepage1.nifty.com/gyouseinet/kyujitsu.htm
	 * �����Q�l�ɂȂ邩������Ȃ��B
	 * http://www.nifty.ne.jp/forum/ffortune/calen/calen/yomi99/yomi027.htm
	 * 
	 * 
	 * ����ϊ��p�̃���: ����0�N = 1865 �吳0�N = 1911 ���a0�N = 1925 ����0�N = 1988
	 */
	Vector holidaysInYear(int y) {
		Vector v = new Vector(15);

		// ����
		if (1948 < y && y <= 1973)
			v.addElement(new Date4Holidays(y, 1, 1));
		else if (1973 < y)
			v.addElement(new Date4Holidays(y, 1, 1).substituteIfSunday());

		// ���n��(��O)
		if (1871 < y && y <= 1948)
			v.addElement(new Date4Holidays(y, 1, 3));

		// �V�N����(��O)
		if (1871 < y && y <= 1948)
			v.addElement(new Date4Holidays(y, 1, 5));

		// ���l�̓�
		if (1966 < y && y <= 1973)
			v.addElement(new Date4Holidays(y, 1, 15));
		if (1973 < y && y < 2000)
			v.addElement(new Date4Holidays(y, 1, 15).substituteIfSunday());
		else if (2000 <= y)
			v.addElement(new Date4Holidays(y, 1, SECOND_MONDAY));

		// �F���V�c��(����)
		if (1871 < y && y <= 1912)
			v.addElement(new Date4Holidays(y, 1, 30));

		// �����L�O���A�I����(��O)
		if (1871 < y && y <= 1948 || 1966 < y && y <= 1973)
			v.addElement(new Date4Holidays(y, 2, 11));
		else if (1973 < y)
			v.addElement(new Date4Holidays(y, 2, 11).substituteIfSunday());

		// �t���̓��A�t�G�c���(��O)
		Date4Holidays ve = Date4Holidays.vernal_equinox(y);
		if (ve != null) {
			if (1876 < y && y <= 1973)
				v.addElement(ve);
			else if (1973 < y)
				v.addElement(ve.substituteIfSunday());
		}

		// �_���V�c��(��O)
		if (1871 < y && y <= 1948)
			v.addElement(new Date4Holidays(y, 4, 3));

		// �݂ǂ�̓�(����)�A�V�c�a����(���a)�A�V����(���a��O)
		if (1927 <= y && y < 1973)
			v.addElement(new Date4Holidays(y, 4, 29));
		else if (1973 <= y)
			v.addElement(new Date4Holidays(y, 4, 29).substituteIfSunday());

		// ���@�L�O��
		if (1948 < y && y < 1973)
			v.addElement(new Date4Holidays(y, 5, 3));
		else if (1973 <= y)
			v.addElement(new Date4Holidays(y, 5, 3).substituteIfSunday());

		// �����̏j���Ɋւ���@����3��3��(�����̋x��)
		if (1985 < y)
			v.addElement(new Date4Holidays(y, 5, 4));

		// ���ǂ��̓�
		if (1948 < y && y < 1973)
			v.addElement(new Date4Holidays(y, 5, 5));
		else if (1973 <= y)
			v.addElement(new Date4Holidays(y, 5, 5).substituteIfSunday());

		// �C�̓�
		if (1996 <= y && y < 2003)
			v.addElement(new Date4Holidays(y, 7, 20).substituteIfSunday());
		else if (1996 <= y)
			v.addElement(new Date4Holidays(y, 7, THIRD_MONDAY));

		// �����V�c��(�吳)
		if (1912 < y && y < 1927)
			v.addElement(new Date4Holidays(y, 7, 30));

		// �V����(�吳)
		if (1912 < y && y < 1927)
			v.addElement(new Date4Holidays(y, 8, 30));

		// �h�V�̓�
		if (1966 <= y && y < 1973)
			v.addElement(new Date4Holidays(y, 9, 15));
		else if (1973 <= y && y < 2003)
			v.addElement(new Date4Holidays(y, 9, 15).substituteIfSunday());
		else if (2003 <= y)
			v.addElement(new Date4Holidays(y, 9, THIRD_MONDAY));

		// �H���̓��A�H�G�c���(��O)
		Date4Holidays ae = Date4Holidays.autumnal_equinox(y);
		if (ae != null) {
			if (1876 <= y && y < 1973)
				v.addElement(ae);
			else if (1973 <= y)
				v.addElement(ae.substituteIfSunday());
		}

		// �̈�̓�
		if (1966 <= y && y < 1973)
			v.addElement(new Date4Holidays(y, 10, 10));
		else if (1973 <= y && y < 2000)
			v.addElement(new Date4Holidays(y, 10, 10).substituteIfSunday());
		else if (2000 <= y)
			v.addElement(new Date4Holidays(y, 10, SECOND_MONDAY));

		// �_��(���񂶂傤)��(��O)
		if (1871 < y && y < 1877)
			v.addElement(new Date4Holidays(y, 9, 17));
		else if (1877 <= y && y < 1948)
			v.addElement(new Date4Holidays(y, 10, 17));

		// �V���ߏj��(�吳)
		if (1913 <= y && y < 1927)
			v.addElement(new Date4Holidays(y, 10, 31));

		// �����̓��A������(���a��O)�A�V����(����)
		if (1871 <= y && y < 1912 || 1927 <= y && y < 1973)
			v.addElement(new Date4Holidays(y, 11, 3));
		else if (1973 <= y)
			v.addElement(new Date4Holidays(y, 11, 3).substituteIfSunday());

		// �ΘJ���ӂ̓��A�V��(�ɂ��Ȃ�)��(��O)
		if (1871 <= y && y < 1973)
			v.addElement(new Date4Holidays(y, 11, 23));
		else if (1973 <= y)
			v.addElement(new Date4Holidays(y, 11, 23).substituteIfSunday());

		// �V�c�a����(����)
		if (1989 <= y)
			v.addElement(new Date4Holidays(y, 12, 23).substituteIfSunday());

		// �吳�V�c��(���a��O)
		if (1927 <= y && y < 1948)
			v.addElement(new Date4Holidays(y, 12, 25));

		// ���m�e�������̋V
		if (y == 1959)
			v.addElement(new Date4Holidays(y, 4, 10));

		// ���a�V�c��r�̗�
		if (y == 1989)
			v.addElement(new Date4Holidays(y, 2, 24));

		// ���ʂ̋V
		if (y == 1990)
			v.addElement(new Date4Holidays(y, 11, 12));

		// ���m�e�������̋V
		if (y == 1993)
			v.addElement(new Date4Holidays(y, 6, 9));

		// ��O�̑��
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
