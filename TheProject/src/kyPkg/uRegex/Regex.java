package kyPkg.uRegex;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kyPkg.panel.Pn_Cond;

//kyPkg.uRegex.Regex
public class Regex {
	static Pattern ptnFieldName = null;
	static Pattern ptnMuliAns = null;
	static Pattern ptnNumeric = null;
	static Pattern ptnDotNumeric = null;
	static Pattern ptn49Numeric = null;
	static Pattern ptnOraTime1 = null;
	static Pattern ptnOraTime2 = null;
	static Pattern ptnAlphaNumeric = null;
	static Pattern patHalfWidthStr = null;
	static Pattern patFullWidthStr = null;
	static Pattern patInteger = null;
	static Pattern patDouble = null;

	static Pattern patHiragana = null;
	static Pattern patKatakana = null;
	static Pattern patKatakanaHalf = null;

	static Pattern ptnYear = null;

	// --------------------------------------------------------------------
	// ���X�g���̃p�^�[���Ƀ}�b�`�����G�������g�݂̂����o��
	// --------------------------------------------------------------------
	public static List<String> limitTheList(String regex, List<String> srclist) {
		regex = regex.trim();
		if (!regex.equals("")) {
			Pattern pattern = Regex.getPatternEx(regex, false);
			List<String> dstList = new ArrayList();
			if (srclist != null) {
				for (String element : srclist) {
					if (pattern.matcher(element).matches())
						dstList.add(element);
				}
			}
			return dstList;
		} else {
			return srclist;
		}
	}

	// -------------------------------------------------------------------------
	// ���K�\���p�^�[�����쐬����
	// ignoreCase => �啶������������ʂ���ꍇtrue
	// �������p�^�[����������g���ꍇ��matches���x�^�[
	// �s�g�p��t
	// Pattern pattern = kyPkg.util.Regex.getPattern(pRegex);
	// wFlg = pattern.matcher(pCharSeq).find();
	// -------------------------------------------------------------------------
	public static Pattern getPatternEx(String regex, boolean ignoreCase) {
		if (!regex.startsWith("^"))
			regex = ".*" + regex.trim();
		if (!regex.endsWith("$"))
			regex = regex.trim() + ".*";
//		System.out.println("#debug# 20150402 regex:" + regex);

		return getPattern(regex, ignoreCase);
	}

	// -------------------------------------------------------------------------
	// �����p�^�[�����R���p�C��
	// -------------------------------------------------------------------------
	public static Pattern getPattern(String regex, boolean ignoreCase) {
		try {
			if (ignoreCase) {
				return Pattern.compile(regex, Pattern.CASE_INSENSITIVE);// �啶���Ə���������ʂ��Ȃ�
			} else {
				return Pattern.compile(regex);
			}
		} catch (java.util.regex.PatternSyntaxException e) {
			System.out.println("PatternSyntaxException??");
			return null;
		}
	}

	// kyPkg.uRegex.Regex.isMatch(String str)
	public static boolean isMatch(String str, String pattern) {
		Pattern patAdhoc = Pattern.compile(pattern);
		return (patAdhoc.matcher(str).matches());
	}

	// �y�Ђ炪�ȁz
	// �၄kyPkg.uRegex.Regex.isHiragana(str)
	public static boolean isHiragana(String str) {
		if (patHiragana == null)
			patHiragana = Pattern.compile("^[��-�U]+$");
		return (patHiragana.matcher(str).matches());
	}

	// �y�J�^�J�i�z
	// �၄kyPkg.uRegex.Regex.isHiragana(str)
	public static boolean iskatakana(String str) {
		if (patKatakana == null)
			patKatakana = Pattern.compile("^[�@-��]+$");
		return (patKatakana.matcher(str).matches());
	}

	// �y���p�J�^�J�i�z
	// �၄kyPkg.uRegex.RegexiskatakanaHalf(str)
	public static boolean iskatakanaHalf(String str) {
		if (patKatakanaHalf == null)
			patKatakanaHalf = Pattern.compile("^[�-�+]+$");
		return (patKatakanaHalf.matcher(str).matches());
	}

	// �y�S�p�z
	// �၄kyPkg.uRegex.Regex.isFullWidthString(str)
	public static boolean isFullWidthString(String str) {
		if (patFullWidthStr == null)
			patFullWidthStr = Pattern.compile("^[^ -~�-�]+$");
		return (patFullWidthStr.matcher(str).matches());
	}

	// �y���p�z
	// �၄kyPkg.uRegex.Regex.isHalfWidthString(str)
	public static boolean isHalfWidthString(String str) {
		if (patHalfWidthStr == null)
			patHalfWidthStr = Pattern.compile("^[ -~�-�]+$");
		return (patHalfWidthStr.matcher(str).matches());
	}

	// �y���p�p���z
	// �၄kyPkg.uRegex.Regex.isAlphaNumeric(str)
	public static boolean isAlphaNumeric(String str) {
		if (ptnAlphaNumeric == null)
			ptnAlphaNumeric = Pattern.compile("^[a-zA-Z0-9]+$");
		return (ptnAlphaNumeric.matcher(str).matches());
	}

	// �y���p�����z
	// �၄kyPkg.uRegex.Regex.isNumeric(str)
	public static boolean isNumeric(String str) {
		if (ptnNumeric == null)
			ptnNumeric = Pattern.compile("^[-]?[0-9]+$");
		return (ptnNumeric.matcher(str).matches());
	}

	// �y���p�N�z4�P�^
	// �၄kyPkg.uRegex.Regex.isYear(str)
	public static boolean isYear(String str) {
		if (ptnYear == null)
			ptnYear = Pattern.compile("^[12][09][0-9][0-9]$");
		return (ptnYear.matcher(str).matches());
	}

	public static boolean isDotNumeric(String str) {
		if (ptnDotNumeric == null)
			ptnDotNumeric = Pattern.compile("^[-]?[0-9]*[.]?[0-9]+$");
		return (ptnDotNumeric.matcher(str).matches());
	}

	// �y���p�p���Ŏn�܂锼�p�p���z
	// �၄kyPkg.uRegex.Regex.isFieldName(str)
	public static boolean isFieldName(String str) {
		if (ptnFieldName == null)
			ptnFieldName = Pattern.compile("^[a-zA-Z]+[-_a-zA-Z0-9]*$");
		return (ptnFieldName.matcher(str).matches());
	}

	// �y49�Ŏn�܂锼�p�����z
	// �၄kyPkg.uRegex.Regex.isAlphaNumeric(str)
	public static boolean is49Numeric(String str) {
		if (ptn49Numeric == null)
			ptn49Numeric = Pattern.compile("^49[0-9]+$");
		return (ptn49Numeric.matcher(str).matches());
	}

	// �y�}���`�A���T�[�̉񓚁z���ǂ���
	public static boolean isMuliAns(String str) {
		if (ptnMuliAns == null)
			ptnMuliAns = Pattern.compile("^\"[0-9,]*\"$");
		return (ptnMuliAns.matcher(str).matches());
	}

	// �y�I���N���̃^�C���X�^���v2000.00.00 00:00:00�z
	// �၄kyPkg.uRegex.Regex.isTimeStamp(str)
	public static boolean isTimeStamp(String str) {
		if (ptnOraTime1 == null)
			ptnOraTime1 = Pattern
					.compile("^[0-9]{4}\\.[0-9]{2}\\.[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}+$");
		if (ptnOraTime1.matcher(str).matches())
			return true;
		if (ptnOraTime2 == null)
			ptnOraTime2 = Pattern
					.compile("^[0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}+$");
		return (ptnOraTime2.matcher(str).matches());
	}

	// �၄kyPkg.uRegex.Regex.isInteger(String str)
	public static boolean isInteger(String str) {
		if (patInteger == null)
			patInteger = Pattern.compile("^([0-9]\\d*|0)$");
		return (patInteger.matcher(str).matches());
	}

	// �၄kyPkg.uRegex.Regex.isDouble(String str)
	// �����_���܂܂�Ă�����E�E�E��~
	public static boolean isDouble(String str) {
		if (patDouble == null)
			patDouble = Pattern.compile("^([0-9]\\d*|0)\\.(\\d+)?$");
		// patDouble = Pattern.compile("^([0-9]\\d*|0)(\\.\\d+)?$");
		return (patDouble.matcher(str).matches());
	}

	// DRY�@
	// -------------------------------------------------------------------------
	// ���K�\���Ƀ}�b�`�����ꍇ�A����Q�Ƃ����X�g�ŕԂ��i�}�b�`���Ȃ��ꍇ��null���Ԃ�j
	// -------------------------------------------------------------------------
	// �s�g�p��t�@��pattern�́i�v���O�����̊J�n�����Łj���炩���߃R���p�C�����Ă����Ɨǂ��B
	// Pattern pattern = Pattern.compile("([A-Z])([\\d][\\d]*)");
	// List ans = kyPkg.util.Regex.parseIt(pattern,val);
	// -------------------------------------------------------------------------
	public static List<String> parseIt(Pattern pattern, String val) {
		List list = null;
		Matcher matcher = pattern.matcher(val);
		if (matcher.find()) {
			list = new ArrayList();
			// System.out.println("parseIt found >> " + val);
			// System.out.println("groupCount():" + matcher.groupCount());
			for (int i = 1; i <= matcher.groupCount(); i++) {
				String valx = matcher.group(i);
				// System.out.println(">group" + i + ":" + valx);
				list.add(valx);
			}
		}
		return list;
	}

	public static void testRegCompile() {
		// �e�X�g�f�[�^
		List<String> testData = new ArrayList();
		testData.add("ooooQuick");
		testData.add("Quick");
		testData.add("Quickoooo");

		// <<���K�\��>>�����쐬
		List<String> regList = new ArrayList();
		regList.add(".*Quick$");
		regList.add("^Quick$");
		regList.add("^Quick.*");
		HashMap<String, Pattern> regMap = Regex.regCompile(regList);

		// <<���K�\��>>�}�b�`���O
		for (String element : testData) {
			if (Regex.regMatch(regMap, element)) {
				System.out.println(" match!:" + element);
			}
		}

	}

	// �������̂ǂꂩ�Ɉꌏ�ł��}�b�`�����true��Ԃ��i��r���鏇�Ԃ͕s��j
	public static boolean regMatch(HashMap<String, Pattern> regMap,
			String string) {
		Set<String> set = regMap.keySet();
		for (String regex : set) {
			Pattern pattern = regMap.get(regex);
			if (pattern.matcher(string).matches()) {
				// System.out.println(regex + " !!");
				return true;
			}
		}
		return false;
	}

	// -------------------------------------------------------------------------
	// ���K�\�����������
	// �i���K�\�����X�g���R���p�C�����ăn�b�V���}�b�v�Ɋi�[����j
	// -------------------------------------------------------------------------
	public static HashMap<String, Pattern> regCompile(List<String> list) {
		HashMap<String, Pattern> hmap = new HashMap();
		for (String regex : list) {
			Pattern pattern = Pattern.compile(regex);
			hmap.put(regex, pattern);
		}
		return hmap;
	}

	// -------------------------------------------------------------------------
	// �啶����������B���Ƀ}�b�`���O�������ꍇ���P
	// ������p�^�[�����R���p�C�������̂Œ��ӂ���
	// �s�g�p��t
	// wFlg = kyPkg.util.Regex.matchesIgnoreCase(pRegex, pCharSeq);
	// -------------------------------------------------------------------------
	public static boolean matches(String regex, String string) {
		Pattern pattern = getPattern(regex, true);
		return pattern.matcher(string).matches();
	}

	// -------------------------------------------------------------------------
	// posix �����N���X�@[:digit:]
	// -------------------------------------------------------------------------
	public static String[] regTermArray(String str) {
		if (str == null)
			return null;
		String[] array = str.split("[-_�`]", -1);
		if (array.length == 2) {
			array[0] = regDate(array[0]);
			array[1] = regDate(array[1]);
		} else {
			array = null;
		}
		return array;
	}

	public static String[] regTermArrayKJ(String str) {
		String[] array = str.split("[-_�`]");
		if (array.length == 2) {
			array[0] = regDateKJ(array[0]);
			array[1] = regDateKJ(array[1]);
		} else {
			array = null;
		}
		return array;
	}

	// -------------------------------------------------------------------------
	// ���p�̊��Ԗ��ɏ��������Ă���"2013�N03��01���`2013�N03��31��" =>20130301_20130331
	// �g�p�၄	Regex.cnv2RegTerm("2013�N03��01���`2013�N03��31��")
	// -------------------------------------------------------------------------
	public static String cnv2RegTerm(String str) {
		String[] array = regTermArray(str);
		if (array != null & array[0] != null & array[1] != null) {
			return array[0] + "_" + array[1];
		} else {
			return "";
		}
	}

	public static String regTermKJ(String str) {
		String[] array = regTermArrayKJ(str);
		if (array != null & array[0] != null & array[1] != null) {
			return array[0] + "�`" + array[1];
		} else {
			return "";
		}
	}

	// -------------------------------------------------------------------------
	// // ���t���������t�H�[�}�b�g���ǂ�������A�ʖڂȂ�X�y�[�X��Ԃ�������
	// -------------------------------------------------------------------------
	// private static String verifyYmdxx(String str, String y, String m, String
	// d) {
	// String thisYear = "2011";
	// String[] array = str.split("/");
	// String ans = "";
	// switch (array.length) {
	// case 1:
	// // yyyymmdd
	// // if(str.length()==4){}
	// // if(str.length()==6){}
	// if (str.length() == 8) {
	// ans = str.substring(0, 4) + y + str.substring(4, 6) + m
	// + str.substring(6, 8) + d;
	// }
	// break;
	// case 2:
	// // mm/dd
	// ans = thisYear + y + array[0] + m + array[1] + d;
	// break;
	// case 3:
	// // yyyy/mm/dd
	// ans = array[0] + y + array[1] + m + array[1] + d;
	// break;
	// default:
	// break;
	// }
	// return ans;
	// }

	public static int[] getTodayArray() {
		int[] rtn = new int[3];
		Calendar today = Calendar.getInstance(TimeZone.getTimeZone("JST"));
		rtn[0] = today.get(Calendar.YEAR);
		rtn[1] = (today.get(Calendar.MONTH) + 1);
		rtn[2] = today.get(Calendar.DAY_OF_MONTH);
		return rtn;
	}

	// posix�����N���X�@[:digit:]���g���Ȃ��̂͂Ȃ����H�E�E�E
	// ��������t�iYYYY/MM/DD�Ȃǁj���A���K������int[3]�Ɋi�[���ĕԂ�
	public static int[] str2DateArray(String str) {
		int[] rtn = new int[3];
		String year = "0";
		String month = "0";
		String date = "0";
		// �\�ߊ�����ϊ����Ă���
		str = str.replaceAll("�N", "/");
		str = str.replaceAll("��", "/");

		// �S�p�˔��p�ϊ�
		kyPkg.uCodecs.CharConv conv = kyPkg.uCodecs.CharConv.getInstance();
		str = conv.cnvNarrow(str, '?');

		Pattern patY_M_D = Pattern
				.compile("([0-9]+)([,./-])([0-9]+)([,./-])([0-9]+)");
		Pattern patM_D = Pattern.compile("([0-9]+)([,./-])([0-9]+)");
		Pattern patNUM = Pattern.compile("([0-9]+)");
		Matcher matcher = patY_M_D.matcher(str);

		if (matcher.find()) {
			year = matcher.group(1);
			month = matcher.group(3);
			date = matcher.group(5);
		} else {
			matcher = patM_D.matcher(str);
			if (matcher.find()) {
				month = matcher.group(1);
				date = matcher.group(3);
			} else {
				matcher = patNUM.matcher(str);
				if (matcher.find()) {
					switch (matcher.group(1).length()) {
					case 8:
						year = matcher.group(1).substring(0, 4);
						month = matcher.group(1).substring(4, 6);
						date = matcher.group(1).substring(6, 8);
						break;
					case 6:
						year = matcher.group(1).substring(0, 2);
						month = matcher.group(1).substring(2, 4);
						date = matcher.group(1).substring(4, 6);
						break;
					case 4:
						// year = "20??";
						month = matcher.group(1).substring(0, 2);
						date = matcher.group(1).substring(2, 4);
						break;
					default:
						break;
					}
				} else {
					int groupCount = matcher.groupCount();
					System.out.println("unmatch?! str:" + str + " groupCount:"
							+ groupCount);
				}
			}
		}
		int iYear = Integer.parseInt(year);
		int iMonth = Integer.parseInt(month);
		int iDate = Integer.parseInt(date);
		int[] todayArray = getTodayArray();

		// Date Range Check 0���̍������Ƃ������ʓ��t���ω����邩�ǂ����H
		// XXX �J�����_�[���t�Ƃ��Đ��������ǂ������肷�邩�H
		if (iDate < 1 || 31 < iDate) {
			iDate = -1;
			// iDate = todayArray[3];
		}
		// Month Range Check
		if (iMonth < 1 || 12 < iMonth) {
			iMonth = -1;
			// iDate = todayArray[2];
		}
		// Year Range Check
		if (iYear == 0) {// �N���w�肳��Ă��Ȃ������ꍇ���s���t�ŕ␳����
			if (iMonth <= todayArray[1]) {
				iYear = todayArray[0]; // ������菬�����Ȃ瓖�N
			} else {
				iYear = todayArray[0] - 1; // �������傫���Ȃ�O�N
			}
		} else if (iYear < 100) {
			iYear = 2000 + iYear; // �␳����
		} else if (iYear > todayArray[0]) {
			// range Error�@�␳���Ȃ�
			iYear = -1;
			// iYear = todayArray[0];
		}
		// XXX �����W�`�F�b�N
		// System.out.println("in>" + str + " year:" + iYear + " month:" +
		// iMonth + " date:" + iDate);
		rtn[0] = iYear;
		rtn[1] = iMonth;
		rtn[2] = iDate;
		return rtn;
	}

	// ���t������𐳋K������=>
	public static String regDate(String sDate) {
		return fromatDateArray(str2DateArray(sDate), "", "", "");
	}

	public static String regDateKJ(String sDate) {
		return fromatDateArray(str2DateArray(sDate), "�N", "��", "��");
	}

	// int[3]�Ɋi�[����Ă�����t�i�N�A���A���j���t�H�[�}�b�g���ĕ�����(yyyy/mm/dd)�Ƃ��ĕԂ�
	public static String fromatDateArray(int[] array) {
		return fromatDateArray(array, "/", "/", "");
	}

	public static String fromatDateArray(int[] array, String delim1,
			String delim2, String delim3) {
		DecimalFormat df0000 = new DecimalFormat("0000");
		DecimalFormat df00 = new DecimalFormat("00");
		if (array.length == 3 & array[0] >= 1 & array[1] >= 1 & array[2] >= 1) {
			String sY = df0000.format(array[0]);
			String sM = df00.format(array[1]);
			String sD = df00.format(array[2]);
			return sY + delim1 + sM + delim2 + sD + delim3;
		} else {
			return null;
		}
	}

	public static void testRegDate() {
		List<String> testData = new ArrayList();
		testData.add("20110614");
		testData.add("2011�N06��14��");
		testData.add("10�N06��14��");
		testData.add("2010/06/14");
		testData.add("2010.06.14");
		testData.add("06/14");
		testData.add("2/3");
		testData.add("12/31");
		testData.add("06��14��");
		testData.add("110614");
		testData.add("0614");
		testData.add("2011/1/1");
		testData.add("2011.1.1");
		testData.add("11/1/1");
		testData.add("11.1.1");
		testData.add("2011,1,1");
		testData.add("11,1,1");
		System.out.println("<regTerm>");
		for (String param : testData) {
			System.out.println(param + "\t\t\t =>" + regDate(param));
		}
		// �v���������p�ϊ��H
		System.out.println("<kanjiVersion>");
		for (String param : testData) {
			System.out.println(param + "\t\t\t =>" + regDateKJ(param));
		}
	}

	public static void testRegTerm() {
		List<String> testData = new ArrayList();
		testData.add("20110614_20120731");
		testData.add("?�`yyyy�Nmm��dd��");
		testData.add("�Q�O�P�P�N�U���P�S���`�Q�O�P�Q�N�V���R�P��");
		testData.add("2011�N06��14���`2012�N07��31��");
		testData.add("10�N06��14���`12�N07��31��");
		testData.add("2010/06/14-2010/07/31");
		testData.add("2010.06.14-2010.07.31");
		testData.add("06/14-07/31");
		testData.add("2/3-7/31");
		testData.add("12/31-7/31");
		testData.add("06��14���`7��31��");
		testData.add("110614-120731");
		testData.add("0614-0731");
		testData.add("2011/1/1-2012/07/31");
		testData.add("2011.1.1-2012.07.31");
		testData.add("11/1/1-12/7/31");
		testData.add("11.1.1-12.07.31");
		testData.add("2011,1,1-2012,07,31");
		testData.add("11,1,1-12,07,31");
		System.out.println("<regTerm>");
		for (String param : testData) {
			System.out.println(param + "\t\t\t =>"
					+ kyPkg.uRegex.Regex.cnv2RegTerm(param));
		}
		// �v���������p�ϊ��H
		System.out.println("<kanjiVersion>");
		for (String param : testData) {
			System.out.println(param + "\t\t\t =>"
					+ kyPkg.uRegex.Regex.regTermKJ(param));
		}
	}

	// 2�̓��t���O�N���ǂ������肷��ɂ́E�E�E
	public static boolean isLastYear(String lastDate, String thisDate) {
		// ���ꂼ�ꐳ�K������
		String current = fromatDateArray(str2DateArray(thisDate));
		String compare = fromatDateArray(str2DateArray(lastDate));
		String resultDate = kyPkg.uDateTime.DateUtil.edate1(current, "year",
				-1, "yyyy/MM/dd");
		if (resultDate.equals(compare)) {
			return true;
		} else {
			return false;
		}
	}

	public static void test01() {
		// 2�̓��t���O�N���ǂ������肷��ɂ́E�E�E
		System.out.println("A:"
				+ kyPkg.uRegex.Regex.isLastYear("10�N06��14��", "11�N06��14��"));
		System.out.println("B:"
				+ kyPkg.uRegex.Regex.isLastYear("2010/06/14", "2011�N06��14��"));
		System.out.println("C:"
				+ kyPkg.uRegex.Regex.isLastYear("10/06/14", "2011/07/14"));
	}

	// =>res:Orange is yen_100, Banana is yen_180.
	public static void test120409() {
		String str = "Orange is 100yen, Banana is 180yen.";
		String regex = "(\\d.+?)(yen)";
		String replace = "$2_$1";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		// String result = m.replaceFirst("$2_$1");
		String result = m.replaceAll(replace);
		System.out.println("res:" + result);
	}

	// ���K�\���̌���Q��
	public static void test120925() {
		List<String> list = new ArrayList();
		list.add("MK�@���@01797(�V���{���ʐ΂���)�@�ƈ�v����");
		list.add("K1�@���@87133(�O������)�@�ƈ�v����");
		list.add("MK�@���@73512(�T����)�@�ƈ�v����");
		// list.add("MK�@���@01797(�V���{���ʐ΂���)�@�Ŏn�܂�");
		// list.add("MK�@���@01797(�V���{���ʐ΂���)�@�ŏI���");
		// list.add("MK�@�Ɂ@01797(�V���{���ʐ΂���)�@���܂܂��");
		System.out.println(kyPkg.uRegex.Regex.condList2String(list));
	}

	// ------------------------------------------------------------------------
	// MK�@���@01797(�V���{���ʐ΂���)�@�ƈ�v����
	// MK�@���@73512(�T����)�@�ƈ�v����
	// MK�@���@87133(�O������)�@�ƈ�v����
	// ------------------------------------------------------------------------
	public static String condList2String(List<String> condList) {
		HashMap<String, List<String>> hmap = new HashMap();
		String regex = "(\\S*)(�@���@|�@�Ɂ@)(\\S*)�@" + Pn_Cond.getPattern2();
		Pattern pat = Pattern.compile(regex);
		for (String element : condList) {
			Matcher matcher = pat.matcher(element);
			if (matcher.find()) {
				String key = matcher.group(1).trim() + matcher.group(2).trim()
						+ "\t" + matcher.group(4).trim();
				String value = matcher.group(3).trim();
				List<String> curlist = hmap.get(key);
				if (curlist == null)
					curlist = new ArrayList();
				curlist.add(value);
				curlist.add("�A");
				hmap.put(key, curlist);
			} else {
				System.out.println("@condList2String pattern not found:"
						+ element);
				return null;
			}
		}
		StringBuffer buf = new StringBuffer();
		List<String> keyList = new ArrayList(hmap.keySet());
		Collections.sort(keyList);
		for (String key : keyList) {
			String[] array = key.split("\t");
			buf.append(array[0]);
			List<String> curlist = hmap.get(key);
			for (String value : curlist) {
				buf.append(value);
			}
			buf.append(array[1] + "�B");
		}

		return buf.toString();
	}

	public static void test120629() {
		String str = "CREATE INDEX X_FIELD_NAME ON TABLE_NAME (FIELD_NAME)";
		String regex1 = "(TABLE_NAME)";
		String replace1 = "TTT";
		Pattern pat1 = Pattern.compile(regex1);
		Matcher mat1 = pat1.matcher(str);
		str = mat1.replaceAll(replace1);

		System.out.println("res:" + str);
		// �������A����ł͈ȉ��Ɠ����E�E�E�ϊ��̕p�x�ɉ����Ă��ȁE�E�E�E�ӂ�
		System.out.println("=>"
				+ "CREATE INDEX X_FIELD_NAME ON TABLE_NAME (FIELD_NAME)"
						.replaceAll("TABLE_NAME", "TTT"));
	}

	public static void test130415() {
		String type1 = "2010.03.12 21:18:56";
		String type2 = "2010/03/12 21:18:56";
		if (kyPkg.uRegex.Regex.isTimeStamp(type1)) {
			System.out.println("it's a isTimeStamp!!" + type1);
		}
		if (kyPkg.uRegex.Regex.isTimeStamp(type2)) {
			System.out.println("it's a isTimeStamp!!" + type2);
		}

	}

	public static void test130516() {
		String val = "\"2,3,7,16,17,18,19,21\"";
		if (kyPkg.uRegex.Regex.isMuliAns(val)) {
			System.out.println("it's a isMuliAns!!" + val);
		} else {
			System.out.println("it's not a isMuliAns!!" + val);
		}

	}

	private static void test0626() {
		String regex = "";
		// regex = "(\\S*)(�@���@|�@�Ɂ@)(\\S*)�@(���܂܂��|�ŏI���|�Ŏn�܂�|�ƈ�v����)";
		// regex = "(\\w*)(�@���@|�@�Ɂ@)(\\S*)�@(���܂܂��|�ŏI���|�Ŏn�܂�|�ƈ�v����)";
		regex = "(\\S*)�@(���܂܂��|�ŏI���|�Ŏn�܂�|�ƈ�v����)";
		regex = "(\\w*)(�@���@|�@�Ɂ@)(\\S*)�@";
		regex = "(\\S*)(�@���@|�@�Ɂ@)(\\S*)�@(���܂܂��|�ŏI���|�Ŏn�܂�|�ƈ�v����)";
		Pattern pat = Pattern.compile(regex);
		String element = "";
		element = "�L���`�ЁE���N�Ё@�ƈ�v����";
		element = "K1�@���@00060(�L���`�ЁE���N��  (2696))�@";
		element = "K1�@���@00060(�L���`�ЁE���N��)�@�ƈ�v����";
		Matcher matcher = pat.matcher(element);
		if (matcher.find()) {
			System.out.println("found");
		} else {
			System.out.println("not found");

		}

	}

	public static void main(String[] args) {
		test0626();
		// test130516();
		// test130415();
		// test120925();
		// testRegTerm();
		// testRegDate();
		// test120409();
		// test120629();
		// testRegCompile();
	}
}
