package kyPkg.uDateTime;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

/**************************************************************************
 * DateCalc	
 * @author	ken yuasa
 * @version	1.0
 * <ul>
 * <ll>�y�T�v�z�ėp���Ԍv�Z�N���X
 * </ul>
 **************************************************************************/
public class DateCalc {
	public static final String KARA = "�`";
	public static final int SUN = 1;
	public static final int MON = 2;
	public static final int TUE = 3;
	public static final int WED = 4;
	public static final int THU = 5;
	public static final int FRI = 6;
	public static final int SAT = 7;
	private Date baseDate; // ����t
	private static final ParsePosition parsePos = new ParsePosition(0);
	private static final SimpleDateFormat SDF_Normal = new SimpleDateFormat(
			"yyyyMMdd");
	private static final SimpleDateFormat SDF_Slant = new SimpleDateFormat(
			"yyyy/MM/dd");
	private static final SimpleDateFormat SDF_Kanji = new SimpleDateFormat(
			"yyyy�NMM��dd��");
	private static final DecimalFormat df00 = new DecimalFormat("00");
	private static final DecimalFormat df0000 = new DecimalFormat("0000");
	private static String bef1;
	private static String aft1;
	private static String bef2;
	private static String aft2;

	private static String format0x2(int iVal) {
		return df00.format(iVal);
	}

	private static String format0x4(int iVal) {
		return df0000.format(iVal);
	}

	public static String supressedTermName(String befYmd, String aftYmd) {
		Calendar befCalendar = Calendar.getInstance();
		befCalendar.setTime(cnvYmdStr2Date(befYmd));

		Calendar aftCalendar = Calendar.getInstance();
		aftCalendar.setTime(cnvYmdStr2Date(aftYmd));

		String bYY = format0x4(befCalendar.get(Calendar.YEAR)) + "�N";
		String bMM = format0x2(befCalendar.get(Calendar.MONTH) + 1) + "��";
		String bDD = format0x2(befCalendar.get(Calendar.DATE)) + "��";
		String aYY = format0x4(aftCalendar.get(Calendar.YEAR)) + "�N";
		String aMM = format0x2(aftCalendar.get(Calendar.MONTH) + 1) + "��";
		String aDD = format0x2(aftCalendar.get(Calendar.DATE)) + "��";
		if (aYY.equals(bYY)) {
			aYY = "";
			if (aMM.equals(bMM)) {
				aMM = "";
			}
		}
		return bYY + bMM + bDD + KARA + aYY + aMM + aDD;
	}

	public static String supressedTermName_org(String befYmd, String aftYmd) {
		Calendar befCalendar = Calendar.getInstance();
		befCalendar.setTime(cnvYmdStr2Date(befYmd));

		Calendar aftCalendar = Calendar.getInstance();
		aftCalendar.setTime(cnvYmdStr2Date(aftYmd));

		String bYY = String.valueOf(befCalendar.get(Calendar.YEAR)) + "�N";
		String bMM = String.valueOf(befCalendar.get(Calendar.MONTH) + 1) + "��";
		String bDD = String.valueOf(befCalendar.get(Calendar.DATE)) + "��";
		String aYY = String.valueOf(aftCalendar.get(Calendar.YEAR)) + "�N";
		String aMM = String.valueOf(aftCalendar.get(Calendar.MONTH) + 1) + "��";
		String aDD = String.valueOf(aftCalendar.get(Calendar.DATE)) + "��";
		if (aYY.equals(bYY)) {
			aYY = "";
			if (aMM.equals(bMM)) {
				aMM = "";
			}
		}
		return bYY + bMM + bDD + KARA + aYY + aMM + aDD;
	}

	public void holiday() {
		// -------- ����2012(����24)�N
		// 20120101 ����
		// 20120102 �U�֋x��
		// 20120109 ���l�̓�
		// 20120211 �����L�O�̓�
		// 20120320 �t���̓�
		// 20120429 ���a�̓�
		// 20120430 �U�֋x��
		// 20120503 ���@�L�O��
		// 20120504 �݂ǂ�̓�
		// 20120505 ���ǂ��̓�
		// 20120716 �C�̓�
		// 20120917 �h�V�̓�
		// 20120922 �H���̓�
		// 20121008 �̈�̓�
		// 20121103 �����̓�
		// 20121123 �ΘJ���ӂ̓�
		// 20121223 �V�c�a����
		// 20121224 �U�֋x��
		// -------- ����2013(����25)�N
		// 20130101 ����
		// 20130114 ���l�̓�
		// 20130211 �����L�O�̓�
		// 20130320 �t���̓�
		// 20130429 ���a�̓�
		// 20130503 ���@�L�O��
		// 20130504 �݂ǂ�̓�
		// 20130505 ���ǂ��̓�
		// 20130506 �U�֋x��
		// 20130715 �C�̓�
		// 20130916 �h�V�̓�
		// 20130923 �H���̓�
		// 20131014 �̈�̓�
		// 20131103 �����̓�
		// 20131104 �U�֋x��
		// 20131123 �ΘJ���ӂ̓�
		// 20131223 �V�c�a����
		// -------- ����2014(����26)�N
		// 20140101 ����
		// 20140113 ���l�̓�
		// 20140211 �����L�O�̓�
		// 20140321 �t���̓�
		// 20140429 ���a�̓�
		// 20140503 ���@�L�O��
		// 20140504 �݂ǂ�̓�
		// 20140505 ���ǂ��̓�
		// 20140506 �U�֋x��
		// 20140721 �C�̓�
		// 20140915 �h�V�̓�
		// 20140923 �H���̓�
		// 20141013 �̈�̓�
		// 20141103 �����̓�
		// 20141123 �ΘJ���ӂ̓�
		// 20141124 �U�֋x��
		// 20141223 �V�c�a����
	}

	public void quater() {
		HashMap<String, String> quater = new HashMap();
		quater.put("07", "0401\t0630");
		quater.put("10", "0701\t0930");
		quater.put("01", "1001\t1231");
		quater.put("04", "0101\t0331");
	}

	// �N���̌v�Z���@(�킴�킴�֐�������܂ł��Ȃ��C������E�E�E)
	public static Date beginningOfTheYear(Calendar cal) {
		Calendar ins = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = 0;//
		int date = 1;
		ins.set(year, month, date, 0, 0, 0);
		return ins.getTime();
	}

	// �����̌v�Z���@
	public static Date beginningOfTheMonth(Calendar cal, int diffMonth) {
		Calendar ins = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + diffMonth;
		int date = 1;
		ins.set(year, month, date, 0, 0, 0);
		return ins.getTime();
	}

	// �����̌v�Z���@
	public static Date endOfTheMonth(Calendar cal, int diffMonth) {
		Calendar ins = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + diffMonth;
		int date = 1;
		ins.set(year, month, date, 0, 0, 0);

		// ���������擾����
		date = ins.getActualMaximum(Calendar.DATE);
		ins.set(year, month, date, 0, 0, 0);
		return ins.getTime();
	}

	// getDateList("20120101",365);
	// ��ƂȂ���t�A�Ƃ�������ǂ̂��炢��̗\��\�����邩
	// �o�̓t�H�[�}�b�g�@�ˁ@���s���A�^�C�v�A�W�v�J�n�A�I�����t
	// XXX R�F���O�̂��̂������E�E�E����
	// XXX ���F�N������̏�����������
	public static List<String> createTriggerList(String dateStr, int days) {
		String ymd = "";
		SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");
		String rec = "";
		List<String> list = new ArrayList();
		Calendar cal = getCalendar(dateStr);
		for (int i = 0; i < days; i++) {
			cal.add(Calendar.DATE, 1);
			if (cal.get(Calendar.DAY_OF_WEEK) == SUN) {
			} else if (cal.get(Calendar.DAY_OF_WEEK) == SAT) {
				// �f�[�^��Fix���Ă����Ԃŏ��߂Ă̓y�j���Ƀ}���X���[����уN�H�[�^���[���N��
				int weekOf = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);
				int date = cal.get(Calendar.DATE);
				ymd = "";
				if (weekOf == 2 && date > 8) {
					ymd = df1.format(cal.getTime());
				} else if (weekOf == 3 && date <= 15) {
					ymd = df1.format(cal.getTime());
				}

				// "20120101,Y,20111101,20120131,"
				if (!ymd.equals("")) {
					// C:Continue�p���f�[�^�����i�y�ы��f�[�^�폜�p�u���ԂQ�p�����[�^�v�쐬�j
					Date endDate = endOfTheMonth(cal, -1);
					bef1 = df1
							.format(beginningOfTheYear(date2Calendar(endDate)));// �N�n
					aft1 = df1.format(endDate);
					rec = ymd + ",C," + bef1 + "," + aft1;
					bef2 = bef1;
					aft2 = df1.format(endOfTheMonth(cal, -2));
					if (!aft2.matches(".*1231$")) {
						rec = rec + "," + bef2 + "," + aft2;
						// XXX ���ꂪ�N���܂Łi�`1231�j�Ȃ�폜�f�[�^�𐶐����Ȃ��悤�ɂ���΂悢�E�E����
					}
					list.add(rec);

					// bef1 = df1.format(beginningOfTheYear(cal));
					// aft1 = df1.format(endOfTheMonth(cal, -1));
					// rec = ymd + ",C," + bef1 + "," + aft1;
					// bef2 = df1.format(beginningOfTheYear(cal));
					// aft2 = df1.format(endOfTheMonth(cal, -2));
					// if (!aft2.matches(".*1231$")) {
					// rec = rec + "," + bef2 + "," + aft2;
					// // XXX ���ꂪ�N���܂Łi�`1231�j�Ȃ�폜�f�[�^�𐶐����Ȃ��悤�ɂ���΂悢�E�E����
					// }
					// list.add(rec);
					// M:�}���X���[�̏���
					bef1 = df1.format(beginningOfTheMonth(cal, -1)); // �O������
					aft1 = df1.format(endOfTheMonth(cal, -1)); // �O���̖���
					rec = ymd + ",M," + bef1 + "," + aft1;
					list.add(rec);
					// System.out.println(rec);

					int month = cal.get(Calendar.MONTH) + 1;
					// Q:�N�H�[�^���[�̏���
					int thisYear = cal.get(Calendar.YEAR);
					int lastYear = cal.get(Calendar.YEAR);
					switch (month) {
					case 7:
						bef1 = thisYear + "0401";
						aft1 = thisYear + "0630";
						rec = ymd + ",Q," + bef1 + "," + aft1;
						list.add(rec);
						// System.out.println(rec);
						break;
					case 10:
						bef1 = thisYear + "0701";
						aft1 = thisYear + "0930";
						rec = ymd + ",Q," + bef1 + "," + aft1;
						list.add(rec);
						// System.out.println(rec);
						break;
					case 1:
						bef1 = lastYear + "1001";
						aft1 = lastYear + "1231";
						rec = ymd + ",Q," + bef1 + "," + aft1;
						list.add(rec);
						// System.out.println(rec);
						break;
					case 4:
						bef1 = thisYear + "0101";
						aft1 = thisYear + "0331";
						rec = ymd + ",Q," + bef1 + "," + aft1;
						list.add(rec);
						// System.out.println(rec);
						break;
					default:
						break;
					}
				}

			} else {
				// weekly�̏���
				// holyday���H
			}
		}
		return list;
	}

	//Logic�ڐA�p��VB�̂��̂𓥏P
	// System.out.println("yyyy �N     : " + DateDiff("yyyy", xYmd1, xYmd2) + 1)
	// System.out.println("q    �l���� : " + DateDiff("q", xYmd1, xYmd2)+ 1)
	// System.out.println("m    ��     : " + DateDiff("m", xYmd1, xYmd2) + 1)
	// System.out.println("Y    �N�ԒʎZ�� : " + DateDiff("y", xYmd1, xYmd2)+ 1)
	// System.out.println("d    ��        : " + DateDiff("d", xYmd1, xYmd2) + 1)
	// System.out.println("w    �T��      : " + DateDiff("w", xYmd1, xYmd2) + 1)
	// System.out.println("ww   �T        : " + DateDiff("ww", xYmd1, xYmd2)+ 1)
	public static int dateDiff(String type, String befYMD, String aftYMD) {
		if (type.equals("yyyy")) {
			return yearDiff(befYMD, aftYMD);
		} else if (type.equals("m")) {
			return monthDiff(befYMD, aftYMD);
		} else if (type.equals("w")) {
			return weekDiff(befYMD, aftYMD);
		} else if (type.equals("d")) {
			return DateCalc.dateDiff(befYMD, aftYMD);
		} else {
			return -1;
		}
	}

	//Logic�ڐA�p��VB�̂��̂𓥏P
	//FIXME 20150422����������Ńe�X�g���Ă��Ȃ�
	public static String dateAdd(String type, int val, String aftYMD) {
		if (type.equals("yyyy")) {
			return DateCalc.yearAdd(val, aftYMD);
		} else if (type.equals("m")) {
			return DateCalc.monthAdd(val, aftYMD);
		} else if (type.equals("w")) {
			return DateCalc.weekAdd(val, aftYMD);
		} else if (type.equals("d")) {
			return DateCalc.dateAdd(val, aftYMD);
		} else {
			return "";
		}
	}

	// ����t��艽�N�ڂ��H
	public static int yearDiff(String befYMD, String aftYMD) {
		int date = DateCalc.dateDiff(befYMD, aftYMD);
		return date / 365;
	}

	// ����t��艽���ڂ��H(������Ǝ��M���Ȃ��E�E�E)
	public static int monthDiff(String befYMD, String aftYMD) {
		Calendar calBef = getCalendar(befYMD);
		Calendar calAft = getCalendar(aftYMD);
		int iBef_Y = calBef.get(Calendar.YEAR);
		int iAft_Y = calAft.get(Calendar.YEAR);
		int iBef_M = calBef.get(Calendar.MONTH);
		int iAft_M = calAft.get(Calendar.MONTH);
		int diffM = iAft_M - iBef_M;
		int diffY = iAft_Y - iBef_Y;
		return (diffY * 12) + diffM;
	}

	// ����t��艽�T�ڂ��H
	public static int weekDiff(String befYMD, String aftYMD) {
		int date = DateCalc.dateDiff(befYMD, aftYMD);
		return date / 7;
	}

	// ��̓��t����������Ă��邩�E�E�E
	// �၄�@�@�@�Ȃ炘�����݂����ȁE�E�E�E�����͂O��
	public static int dateDiff(String befYMD, String aftYMD) {
		// ��r������t���Z�b�g
		Calendar calAft = getCalendar(aftYMD);
		Calendar calBef = getCalendar(befYMD);
		Date dateAft = calAft.getTime();
		Date dateBef = calBef.getTime();
		// ���t�̍������߂�
		long diff = dateAft.getTime() - dateBef.getTime();

		// ���t�̍� diff �̓~���b�ɂȂ��Ă���̂ŁA
		// �����Ɍv�Z���ĕ\������
		int day = (int) (diff / 1000 / 60 / 60 / 24);
		return day;
	}

	//FIXME 20150422����������Ńe�X�g���Ă��Ȃ�
	public static String yearAdd(int val, String ymd) {
		Calendar cal = getCalendar(ymd);
		cal.add(Calendar.YEAR, val);
		return SDF_Normal.format(cal.getTime());
	}

	//FIXME 20150422����������Ńe�X�g���Ă��Ȃ�
	public static String monthAdd(int val, String ymd) {
		Calendar cal = getCalendar(ymd);
		cal.add(Calendar.MONTH, val);
		return SDF_Normal.format(cal.getTime());
	}

	//FIXME 20150422����������Ńe�X�g���Ă��Ȃ�
	public static String weekAdd(int val, String ymd) {
		Calendar cal = getCalendar(ymd);
		cal.add(Calendar.WEEK_OF_YEAR, val);
		return SDF_Normal.format(cal.getTime());
	}

	//FIXME 20150422����������Ńe�X�g���Ă��Ȃ�
	public static String dateAdd(int val, String ymd) {
		Calendar cal = getCalendar(ymd);
		cal.add(Calendar.DAY_OF_MONTH, val);
		return SDF_Normal.format(cal.getTime());
	}

	// -------------------------------------------------------------------------
	// #formatYMD_KJ# kyPkg.util.DateCalc.formatYMD_KJ("20091111")
	// -------------------------------------------------------------------------
	public static String formatYMD_KJ(String dateStr) {
		if (dateStr.equals("00000000"))
			return "�s��";//���͓��t��"00000000"���ƕϊ����ʂ��o�O��
		return DateCalc.formatYMD("yyyy'�N'MM'��'dd'��'", dateStr);
	}

	public static String cnvTermName(String befYmd, String aftYmd) {
		String bYMD = kyPkg.uDateTime.DateCalc.formatYMD_KJ(befYmd);
		String aYMD = kyPkg.uDateTime.DateCalc.formatYMD_KJ(aftYmd);
		return bYMD + KARA + aYMD;
	}

	// -------------------------------------------------------------------------
	// #formatYMD# ���t������𐮌`�o�͂���
	// <�g�p�၄�@
	// kyPkg.util.DateCalc.formatYMD("yyyy/MM/dd","2009�N11��30��");
	// -------------------------------------------------------------------------
	public static String formatYMD(String pattern, String bYmd) {
		Calendar calendar = Calendar.getInstance();
		//		System.out.println("#20161227# bYmd=>"+bYmd);
		calendar.setTime(cnvYmdStr2Date(bYmd));
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(calendar.getTime());
	}

	// -------------------------------------------------------------------------
	// #getNendo# �N�x�����߂�E�E�E (�S���P���͑O�N�x�ƂȂ�)
	// kyPkg.util.kUtil.getNendo("19670101")
	// -------------------------------------------------------------------------
	public static int getNendo(String yyyymmdd) {
		if (yyyymmdd.length() != 8) {
			return -1;
		}
		String yyyy = yyyymmdd.substring(0, 4);
		String mmdd = yyyymmdd.substring(4, 8);
		int iyyyy = Integer.parseInt(yyyy);
		int immdd = Integer.parseInt(mmdd);
		if (immdd < 402) {
			iyyyy--;
		}
		return iyyyy;
	}

	// -------------------------------------------------------------------------
	// #getFiscalOld#�@�N�x�N������߂�
	// getFiscalOld("19670402")
	// -------------------------------------------------------------------------
	public static int getFiscalOld(String birthDay) {
		int baseNendo = getNendo(kyPkg.uDateTime.DateCalc.getToday());
		int checkNendo = getNendo(birthDay);
		return (baseNendo - checkNendo);
	}

	// -------------------------------------------------------------------------
	// �f�[�^�x�[�X��UPDATE���t�𐶐������Ă���
	//	timeStamp = DateCalc.getCurrent("yyyy/MM/dd HH:mm");
	//	int hour = Integer.parseInt(DateCalc.getCurrent("HH"));
	// -------------------------------------------------------------------------
	public static String getCurrent(String pattern) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	// -------------------------------------------------------------------------
	// �^�C���X�^���v���擾����
	// ex> MMDD_HHSS=>1128_1346
	// -------------------------------------------------------------------------
	public static String getTimeStamp() {
		return getTimeStamp(4, 4, "_");
	}

	// public static String getTimeStamp_org(int datePos, int timeLen, String
	// delim) {
	// String today = kyPkg.uDateTime.DateCalc.getToday();
	// String time = kyPkg.uDateTime.DateCalc.thisTime("");
	// if (datePos > 0 && datePos <= 8) {
	// today = today.substring(datePos);
	// }
	// if (timeLen > 0 && timeLen <= 9)
	// time = time.substring(0, timeLen);
	// return today + delim + time;
	// }
	public static String getTimeStamp(int datePos, int timeLen, String delim) {
		return getTimeStamp(datePos, timeLen, delim, "", "");
	}

	public static String getTimeStamp(int datePos, int timeLen, String delim,
			String dSep, String tSep) {
		String today = kyPkg.uDateTime.DateCalc.getToday(dSep);
		String time = kyPkg.uDateTime.DateCalc.thisTime(tSep);
		// System.out.println("today:"+today);
		// System.out.println("time:"+time);
		if (datePos > 0 && datePos <= 8) {
			today = today.substring(datePos);
		}
		if (timeLen > 0 && timeLen <= 9) {
			time = time.substring(0, timeLen);
		}
		return today + delim + time;
	}

	public static Calendar date2Calendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		// ���ԃt�B�[���h�N���A
		calendar.clear();
		calendar.setTime(date);
		return calendar;
	}

	private static Calendar getCalendar(String dateStr) {
		return date2Calendar(cnvYmdStr2Date(dateStr));
	}

	// -------------------------------------------------------------------------
	// ���t�������date�^�ɕϊ�����
	// -------------------------------------------------------------------------

	public static Date cnvYmdStr2Date(String dateStr) {
		parsePos.setIndex(0);
		if (dateStr.indexOf("/") > 0) {
			return SDF_Slant.parse(dateStr, parsePos);
		} else if (dateStr.indexOf("�N") > 0) {
			return SDF_Kanji.parse(dateStr, parsePos);
		} else {
			return SDF_Normal.parse(dateStr, parsePos);
		}
	}

	// private static Calendar getCalendar(String dateStr) {
	// Calendar calendar = Calendar.getInstance();
	// // ���ԃt�B�[���h�N���A
	// calendar.clear();
	// calendar.setTime(cnvStr2Date(dateStr));
	// return calendar;
	// }

	// -------------------------------------------------------------------------
	// �N�ް����擾
	// -------------------------------------------------------------------------
	public static int getYear(String dateStr) {
		return getCalendar(dateStr).get(Calendar.YEAR);
	}

	// -------------------------------------------------------------------------
	// ���ް����擾
	// -------------------------------------------------------------------------
	public static int getMonth(String dateStr) {
		//MONTH��0����11�܂ł̒l������
		return getCalendar(dateStr).get(Calendar.MONTH) + 1;
	}

	// -------------------------------------------------------------------------
	// ���ް����擾
	// -------------------------------------------------------------------------
	public static int getDate(String dateStr) {
		return getCalendar(dateStr).get(Calendar.DATE);
	}

	// -------------------------------------------------------------------------
	// 1(��)�`7(�y)�̐��l��Ԃ�
	// -------------------------------------------------------------------------
	public static int getDayOfWeek(String dateStr) {

		return getCalendar(dateStr).get(Calendar.DAY_OF_WEEK);
	}

	// -------------------------------------------------------------------------
	// ���̉��x�ڂ̗j����
	// -------------------------------------------------------------------------
	public static int getDayOfWeekInMonth(String dateStr) {
		return getCalendar(dateStr).get(Calendar.DAY_OF_WEEK_IN_MONTH);
	}

	// -------------------------------------------------------------------------
	// ���̉��T�ڂ�
	// -------------------------------------------------------------------------
	public static int getWeekOfMonth(String dateStr) {
		return getCalendar(dateStr).get(Calendar.WEEK_OF_MONTH);
	}

	// -------------------------------------------------------------------------
	// �N�̉��T�ڂ�
	// -------------------------------------------------------------------------
	public static int getWeekOfYear(String dateStr) {
		return getCalendar(dateStr).get(Calendar.WEEK_OF_YEAR);
	}

	// -------------------------------------------------------------------------
	// �N�̉����ڂ�
	// -------------------------------------------------------------------------
	public static int getDayOfYear(String dateStr) {
		return getCalendar(dateStr).get(Calendar.DAY_OF_YEAR);
	}

	// -------------------------------------------------------------------------
	// // �����ް����擾(24���Ԑ�)
	// -------------------------------------------------------------------------
	// public static int getHour_of_day(String dateStr) {
	// return getCalendar(dateStr).get(Calendar.HOUR_OF_DAY);
	// }
	// -------------------------------------------------------------------------
	// // ���ް����擾
	// -------------------------------------------------------------------------
	// public static int getMinute(String dateStr) {
	// return getCalendar(dateStr).get(Calendar.MINUTE);
	// }
	// -------------------------------------------------------------------------
	// // �b�ް����擾
	// -------------------------------------------------------------------------
	// public static int getSecond(String dateStr) {
	// return getCalendar(dateStr).get(Calendar.SECOND);
	// }
	// -------------------------------------------------------------------------
	// // �ؕb�ް����擾
	// -------------------------------------------------------------------------
	// public static int getMillisecond(String dateStr) {
	// return getCalendar(dateStr).get(Calendar.MILLISECOND);
	// }
	// -------------------------------------------------------------------------
	// // �ߑO�E�ߌ�̕ʁB0or1 �̐��l�ŕԂ�
	// -------------------------------------------------------------------------
	// public static int getAmPm(String dateStr) {
	// return getCalendar(dateStr).get(Calendar.AM_PM);
	// }
	// -------------------------------------------------------------------------
	// // �����ް����擾(12���Ԑ�)
	// -------------------------------------------------------------------------
	// public static int getHour(String dateStr) {
	// return getCalendar(dateStr).get(Calendar.HOUR);
	// }
	/**************************************************************************
	 * DateCalc	�R���X�g���N�^			
	 * @param dateStr	��Ƃ�����t������		 
	 **************************************************************************/
	public DateCalc(String dateStr) {
		//		System.out.println("����t:" + dateStr);
		baseDate = cnvYmdStr2Date(dateStr);
		if (baseDate == null) {
			// XXX�@�G���[����?!
		}
	}

	/***************************************************************************
	 * �y�t�@�C���̍ŏI�X�V���t�z
	 * 
	 * @param pPath
	 *            ��������t�@�C���̃p�X
	 * @param fmt
	 *            ���t�t�H�[�}�b�g�p�^�[��
	 * @return �t�@�C���̍X�V���t���Ԃ� <br>
	 *         �s�g�p��t<br>
	 *         String timeStmp = DateCalc.getLastModDate(path, "yyyyMMddHHmmss");
	 **************************************************************************/
	public static String getLastModDate(String path, String fmt) {
		File file = new File(path);
		if (file.exists() == false || !file.isFile())
			return "";
		long dd = file.lastModified();
		SimpleDateFormat dateFormat = new SimpleDateFormat(fmt, Locale.JAPAN);
		return dateFormat.format(new Long(dd));
	}
	//�t�@�C���̍쐬���t�͓���炵���E�E�EJINI���g���΂悢�炵�����ڐA����������Ƃ������ƁE�E�E

	// ---------------------------------------------------------------------
	// ��������_�Ɏw�肳�ꂽ���t��Ԃ�
	// @param offsetDay �����牽������Ă��邩�i 1 �Ȃ疾���A-1 �Ȃ����j
	// @return YYYY/MM/DD �`��
	//	�g�p��> kyPkg.util.DateCalc.getThisYear();
	// ---------------------------------------------------------------------
	public static int getThisYear() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("JST"));
		return calendar.get(Calendar.YEAR);
	}

	public static int getLastYear() {
		return getThisYear() - 1;
	}

	public static int getThisMonth() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("JST"));
		return (calendar.get(Calendar.MONTH) + 1);
	}

	public static int getThisDay() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("JST"));
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	// ---------------------------------------------------------------------
	// �����̓��t�i2013�N08��22���j��Ԃ�
	// System.out.println(kyPkg.uDateTime.DateCalc.getTodayKJ( ) );
	// ---------------------------------------------------------------------
	public static String getTodayKJ() {
		return formatYMD_KJ(getToday(""));
	}

	// -------------------------------------------------------------------------
	// �����̓��t��Ԃ��@ ex=>20120201
	// �g�p��@System.out.println("������:" + kyPkg.uDateTime.DateCalc.today());
	// -------------------------------------------------------------------------
	public static String today() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		return sdf.format(calendar.getTime());
	}

	// ---------------------------------------------------------------------
	// �����̓��t�iyyyymmdd�j��Ԃ�
	// String today = kyPkg.uDateTime.DateCalc.getToday();
	// ---------------------------------------------------------------------

	public static String getToday() {
		return getToday("");
	}

	public static String getToday(String delimiter) {
		return kyPkg.uDateTime.DateCalc.offsetToday(0, delimiter);
	}

	private static String offsetToday(int offsetDay, String delimiter) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("JST"));
		// �w��΍����A���t��␳
		calendar.add(Calendar.DATE, offsetDay);
		// XXX decimal�Ńt�H�[�}�b�g������
		int year = calendar.get(Calendar.YEAR);
		int month = (calendar.get(Calendar.MONTH) + 1);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		// String sYY = "0000" + year;
		// String sMM = "00" + month;
		// String sDD = "00" + day;
		// sYY = sYY.substring(sYY.length() - 4, sYY.length());
		// sMM = sMM.substring(sMM.length() - 2, sMM.length());
		// sDD = sDD.substring(sDD.length() - 2, sDD.length());
		// return sYY + delimiter + sMM + delimiter + sDD;
		return getYYYYMMDD(year, month, day, delimiter);
	}

	public static String getYYYYMMDD(int year, int month, int day,
			String delimiter) {
		String sYY = "0000" + year;
		sYY = sYY.substring(sYY.length() - 4, sYY.length());
		String sMM = "00" + month;
		sMM = sMM.substring(sMM.length() - 2, sMM.length());
		String sDD = "00" + day;
		sDD = sDD.substring(sDD.length() - 2, sDD.length());
		return sYY + delimiter + sMM + delimiter + sDD;
	}

	// ----------------------------------------------------------------
	// ���݂̎����i������j��Ԃ�
	// �၄ kUtil.thisTime("_");
	// ----------------------------------------------------------------
	public static String thisTime() {
		return kyPkg.uDateTime.DateCalc.thisTime(":");
	}

	public static String thisTime(String pDlm) {
		Calendar wCal = Calendar.getInstance();
		int iHH = wCal.get(Calendar.HOUR_OF_DAY); // 24���ԕ\���̏ꍇ
		int iMM = wCal.get(Calendar.MINUTE);
		int iSS = wCal.get(Calendar.SECOND);
		int iMS = wCal.get(Calendar.MILLISECOND);
		DecimalFormat df00 = new DecimalFormat("00");
		DecimalFormat df000 = new DecimalFormat("000");
		String sHH = df00.format(iHH);
		String sMM = df00.format(iMM);
		String sSS = df00.format(iSS);
		String sMS = df000.format(iMS);
		// String Current = iHH + pDlm + iMM + pDlm + iSS + pDlm + iMS;
		String Current = sHH + pDlm + sMM + pDlm + sSS + pDlm + sMS;
		return Current;
	}

	/**
	 * *************************************************************************
	 * Date�I�u�W�F�N�g�Ԃ̓��������߂� Date startDate = new Date(96, 8, 4); Date endDate =
	 * new Date(96, 8, 6); System.out.println("Diffence = " + days(startDate,
	 * endDate)); �� 86400000�ؕb��1��
	 * ************************************************************************
	 */
	private long diff(String dateStr, String type) {
		Date checkDate = cnvYmdStr2Date(dateStr);
		if (checkDate == null) {
			return Integer.MIN_VALUE;
		}
		if (type.startsWith("D")) {
			// 86400000 = 24*60*60*1000
			return ((checkDate.getTime() - baseDate.getTime()) / 86400000);
		} else if (type.startsWith("H")) {
			// 3600000 = 60*60*1000
			return ((checkDate.getTime() - baseDate.getTime()) / 3600000);
		} else {
			return Integer.MIN_VALUE;
		}
	}

	// -------------------------------------------------------------------------
	// #diffDays# ����t���牽������Ă��邩
	// �����ӁI�I�G���[�̏ꍇInteger.MIN_VALUE���Ԃ�D�D�D�D
	// -------------------------------------------------------------------------
	public int diffDays(String dateStr) {
		return (int) diff(dateStr, "Day");
		// Date checkDate = cnvStr2Date(dateStr);
		// if (checkDate == null) {
		// return Integer.MIN_VALUE;
		// } else {
		// // 86400000 = 24*60*60*1000
		// return (int) ((checkDate.getTime() - baseDate.getTime()) / 86400000);
		// }
	}

	// -------------------------------------------------------------------------
	// ����t���牽���ԗ���Ă��邩
	// ���G���[�̏ꍇLong.MIN_VALUE���Ԃ�̂ŗv���ӁI�I
	// ���g�������Ƃ͈�x���Ȃ��I�I�����Ă݂������Ȃ̂ŁE�E�E�v�e�X�g�I�I
	// -------------------------------------------------------------------------
	public int diffHours(String dateStr) {
		return (int) diff(dateStr, "Hour");
		// Date checkDate = cnvStr2Date(dateStr);
		// if (checkDate == null) {
		// return Integer.MIN_VALUE;
		// } else {
		// // 3600000 = 60*60*1000
		// return (int)(checkDate.getTime() - baseDate.getTime()) / 3600000;
		// }
	}

	/**************************************************************************
	 * dateMap_Mod				
	 * <ul>
	 * <ll>�y�T�v�z�J�n�E�I�����Ԃ�amount�P�ʂɕ������āh���ԁ`���ԁh�Ƃ������̃��X�g�𐶐�����
	 * </ul>
	 * @param bYmd			�J�n���t 
	 * @param aYmd			�I�����t 
	 * @param pattern		�o�͓��t�p�^�[��	 
	 * @param sliceType		��؂�P��	 
	 * @param amount		��؂萔	 
	 **************************************************************************/

	public static List<String> dateMap_Mod(String bYmd, String aYmd,
			String pattern, String sliceType, int amount) {
		int field = -1;

		//20151013 
		if (sliceType.equals(DateUtil.DAY)) {
			field = Calendar.DATE;
		} else if (sliceType.equals(DateUtil.WEEK)) {
			field = Calendar.DATE;
			amount = amount * 7;
		} else if (sliceType.equals(DateUtil.MONTH)) {
			field = Calendar.MONTH;
		} else if (sliceType.equals(DateUtil.YEAR)) {
			field = Calendar.YEAR;
		} else {
			System.out.println("ERROR!!@dateMap_Mod?!");
		}

		//		sliceType = sliceType.toUpperCase();// ���擪�P���������Q�Ƃ��Ȃ�
		//		char type = sliceType.charAt(0);
		//		switch (type) {
		//		case 'Y':
		//			field = Calendar.YEAR;
		//			break;
		//		case 'M':
		//			field = Calendar.MONTH;
		//			break;
		//		case 'W':
		//			field = Calendar.DATE;
		//			amount = amount * 7;
		//			break;
		//		default:
		//			field = Calendar.DATE;
		//			break;
		//		}
		//		System.out.println("bYmd>>" + bYmd + " aYmd>>" + aYmd);
		Date bDate = cnvYmdStr2Date(bYmd);
		Date aDate = cnvYmdStr2Date(aYmd);
		Calendar befCal = Calendar.getInstance();
		Calendar aftCal = Calendar.getInstance();
		befCal.setTime(bDate);
		aftCal.setTime(aDate);

		SimpleDateFormat df1 = new SimpleDateFormat(pattern);
		String last = df1.format(aftCal.getTime()); // �I�[

		aftCal.add(Calendar.DATE, 1);// �������

		List<String> list = new ArrayList();
		// befCal��aftCal�łȂ烋�[�v�����E�E�E
		while (aftCal.after(befCal)) {
			String befYmd = df1.format(befCal.getTime());
			// System.out.println("key:" + key );
			befCal.add(field, amount); // �C���N�������g����
			Calendar cloneCal = (Calendar) befCal.clone();
			cloneCal.add(Calendar.DATE, -1);// ����O
			if (aftCal.after(cloneCal)) {
				String aftYmd = df1.format(cloneCal.getTime());
				list.add(befYmd + KARA + aftYmd);
			} else {
				list.add(befYmd + KARA + last);// ~
			}
		}
		return list;
	}

	// -------------------------------------------------------------------------
	// XXX �����N�A���Ȃǂ̓T�v���X���鏈�����ق����E�E�E�ނ��������H�H
	// XXX �����X�g���T�v���X���郍�W�b�N��p�ӂ���H�H�i�擪�U�������O�Ɠ����Ȃ�\�����Ȃ��E�E�E�Ƃ��j
	// -------------------------------------------------------------------------
	// getTime()�Ŏ擾�ł���̂́A
	// Date �I�u�W�F�N�g�ŕ\�����A1970 �N 1 �� 1 �� 00:00:00 GMT ����̃~���b��
	// -------------------------------------------------------------------------
	// ���t�����ԃV�[�P���X�ɕϊ����郍�W�b�N
	// -------------------------------------------------------------------------
	public static void forTest() {
		String dateStr = kyPkg.uDateTime.DateCalc.today();
		System.out.println("today:" + dateStr);
		// XXX ���肦�Ȃ����t���w�肷��ƌ듮�������E�E�E��~�ށ@�y���f�B���O
		dateStr = "20121331";
		System.out.println("assertEquals(kyPkg.util.DateCalc.getYear(\""
				+ dateStr + "\")," + getYear(dateStr) + ");");
		System.out.println("assertEquals(kyPkg.util.DateCalc.getMonth(\""
				+ dateStr + "\")," + getMonth(dateStr) + ");");
		System.out.println("assertEquals(kyPkg.util.DateCalc.getDate(\""
				+ dateStr + "\")," + getDate(dateStr) + ");");
		System.out.println("assertEquals(kyPkg.util.DateCalc.getDayOfWeek(\""
				+ dateStr + "\")," + getDayOfWeek(dateStr) + ");");
		System.out.println(
				"assertEquals(kyPkg.util.DateCalc.getDayOfWeekInMonth(\""
						+ dateStr + "\")," + getDayOfWeekInMonth(dateStr)
						+ ");");
		System.out.println("assertEquals(kyPkg.util.DateCalc.getWeekOfMonth(\""
				+ dateStr + "\")," + getWeekOfMonth(dateStr) + ");");
		System.out.println("assertEquals(kyPkg.util.DateCalc.getWeekOfYear(\""
				+ dateStr + "\")," + getWeekOfYear(dateStr) + ");");
		System.out.println("assertEquals(kyPkg.util.DateCalc.getDayOfYear(\""
				+ dateStr + "\")," + getDayOfYear(dateStr) + ");");

	}

	public static void testSupressedTermName() {
		List<String> testList = new ArrayList();
		testList.add("20110101\t20100105");
		testList.add("20110101\t20110105");
		testList.add("20110101\t20120105");
		testList.add("20120101\t20120205");
		testList.add("20120101\t20110105");
		for (String term : testList) {
			String[] array = term.split("\t");
			String ans = supressedTermName(array[0], array[1]);
			System.out.println(ans);
		}
	}

	public static void testCreateTriggerList() {
		List<String> triggerList = kyPkg.uDateTime.DateCalc
				.createTriggerList("20110101", 365);
		for (String str : triggerList) {
			System.out.println("DateCalc>" + str);
		}

	}

	public static void testDateDiff03() {
		//VB=>? DateDiff("yyyy", #2012/01/01#, #2013/12/8#)
		//DateDiff("yyyy", "20120101", "20120101")
		for (int i = 1; i <= 12; i++) {

			String bef = "2012/" + i + "/01";
			System.out.println("--<" + bef
					+ ">---------------------------------------------");
			System.out.println("test  M>" + dateDiff("m", bef, "20120208"));
			System.out.println("test  M>" + dateDiff("m", bef, "20120308"));
			System.out.println("test  M>" + dateDiff("m", bef, "20120408"));
			System.out.println("test  M>" + dateDiff("m", bef, "20120508"));
			System.out.println("test  M>" + dateDiff("m", bef, "20120608"));
			System.out.println("test  M>" + dateDiff("m", bef, "20120708"));
			System.out.println("test  M>" + dateDiff("m", bef, "20120808"));
			System.out.println("test  M>" + dateDiff("m", bef, "20120908"));
			System.out.println("test  M>" + dateDiff("m", bef, "20121008"));
			System.out.println("test  M>" + dateDiff("m", bef, "20121108"));
			System.out.println("test  M>" + dateDiff("m", bef, "20121208"));
			System.out.println("test  M>" + dateDiff("m", bef, "20130108"));
			System.out.println("test  M>" + dateDiff("m", bef, "20130208"));
			System.out.println("test  M>" + dateDiff("m", bef, "20130308"));
			System.out.println("test  M>" + dateDiff("m", bef, "20130408"));
			System.out.println("test  M>" + dateDiff("m", bef, "20130501"));
			System.out.println("test  M>" + dateDiff("m", bef, "20130602"));
			System.out.println("test  M>" + dateDiff("m", bef, "20130703"));
			System.out.println("test  M>" + dateDiff("m", bef, "20130804"));
			System.out.println("test  M>" + dateDiff("m", bef, "20130905"));
			System.out.println("test  M>" + dateDiff("m", bef, "20131006"));
			System.out.println("test  M>" + dateDiff("m", bef, "20131107"));
			System.out.println("test  M>" + dateDiff("m", bef, "20131208"));

		}
		//        String aft = "20131208";
		//        System.out.println("test  Y>" + DateDiff("yyyy", bef, aft));
		//        System.out.println("test  M>" + DateDiff("m", bef, aft));
		//        System.out.println("test  W>" + DateDiff("w", bef, aft));
		//        System.out.println("test  D>" + DateDiff("d", bef, aft));

	}

	public static void test01() {
		System.out.println("getTimeStamp()>" + DateCalc.getTimeStamp());
		System.out.println(
				"getTimeStamp(0,4,\",\")>" + DateCalc.getTimeStamp(0, 4, ","));
		System.out.println("getTimeStamp(0,4,\",\")>"
				+ DateCalc.getTimeStamp(0, 4, ",", "", ""));
		System.out.println("getTimeStamp(0,4,\",\")>"
				+ DateCalc.getTimeStamp(0, 8, "','", "/", ":"));
	}

	public static void test02() {
		System.out.println("test  0>" + weekDiff("20120101", "20120101"));
		System.out.println("test  0>" + weekDiff("20120101", "20120107"));
		System.out.println("test  1>" + weekDiff("20120101", "20120108"));
		System.out.println("test  1>" + weekDiff("20120101", "20120109"));
		System.out.println("test  1>" + weekDiff("20120101", "20120110"));
		System.out.println("test  1>" + weekDiff("20120101", "20120111"));
		System.out.println("test  1>" + weekDiff("20120101", "20120112"));
		System.out.println("test  1>" + weekDiff("20120101", "20120113"));
		System.out.println("test  1>" + weekDiff("20120101", "20120114"));
		System.out.println("test  2>" + weekDiff("20120101", "20120115"));
		System.out.println("test  2>" + weekDiff("20120101", "20120121"));
		System.out.println("test  3>" + weekDiff("20120101", "20120122"));
		System.out.println("test  3>" + weekDiff("20120101", "20120128"));
		System.out.println("test  4>" + weekDiff("20120101", "20120129"));
		System.out.println("test  4>" + weekDiff("20120101", "20120131"));
		System.out.println("test  4>" + weekDiff("20120101", "20120201"));
		System.out.println("test  4>" + weekDiff("20120101", "20120204"));
		System.out.println("test  5>" + weekDiff("20120101", "20120205"));
	}

	public static void testGetAvgOfDateDiff() {
		DecimalFormat df = new DecimalFormat("#0.00");
		Set<String> dates = new HashSet();
		dates.add("20150706");
		dates.add("20150505");
		dates.add("20150904");
		dates.add("20153703");
		dates.add("20151202");
		dates.add("20150101");
		dates.add("20150712");
		dates.add("20150511");
		dates.add("20150910");
		dates.add("20150709");
		dates.add("20151208");
		dates.add("20150107");
		String avgStr = df
				.format(kyPkg.uDateTime.DateCalc.getAvgOfDateDiff(dates));
		System.out.println("avgStr:" + avgStr);
	}

	//-------------------------------------------------------------------------
	//���ύw���Ԋu���v�Z����
	//-------------------------------------------------------------------------
	public static double getAvgOfDateDiff(Set<String> dates) {
		if (dates.size() == 1)
			return 0.0;//1�������Ȃ��ꍇ��OverHead�������
		List<String> list = new ArrayList(dates);
		Collections.sort(list);
		String preYmd = "";
		double sum = 0;
		int cnt = 0;
		for (String ymd : list) {
			if (!preYmd.equals("") && !preYmd.equals(ymd)) {
				int diff = dateDiff(preYmd, ymd);
				//				System.out.println(preYmd + " - " + ymd + " Diff=>" + diff);
				sum += diff;
				cnt++;
			}
			preYmd = ymd;
		}
		if (cnt > 0) {
			//			System.out.println("sum:" + (sum));
			//			System.out.println("cnt:" + (cnt));
			//			System.out.println("avg:" + (sum / cnt));
			return sum / cnt;
		} else {
			return 0.0;
		}
	}

	public static void main(String[] argv) {
		testGetAvgOfDateDiff();
	}

}
