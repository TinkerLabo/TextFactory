package kyPkg.converter;

import java.util.ArrayList;
import java.util.List;

import kyPkg.uDateTime.DateCalc;
import kyPkg.uDateTime.DateUtil;

/**************************************************************************
 * DateConverter				
 * @author	ken yuasa
 * @version	1.0
 * <ul>
 * <ll>【概要】
 * </ul>
 **************************************************************************/
public class DateConverter implements Inf_StrConverter {
	private int sliceVal;
	private String sliceType;

	public int getInterval() {
		return sliceVal;
	}

	public String getSliceType() {
		return sliceType;
	}

	private DateCalc dateCalc;

	private List<String> monthList;

	private List<Integer> listBef;
	private List<Integer> listAft;

	public List<String> getMonthList() {
		return monthList;
	}

	/**************************************************************************
	 * DateConverter				
	 * <ul>
	 * <ll>【概要】
	 * </ul>
	 * @param befYmd		開始日付	 
	 * @param aftYmd		終了日付	 
	 * @param sliceType		期間区切りタイプ（期間を区切る形式）{DAY, WEEK, MONTH, YEAR};	 
	 * @param sliceVal		区切る幅（日数など）	 
	 **************************************************************************/
	public DateConverter(String befYmd, String aftYmd, String sliceType,
			int sliceVal) {
		super();
		if (sliceType.equals(DateUtil.WEEK)) {
			this.sliceVal = sliceVal * 7;
			this.sliceType = DateUtil.DAY;
		} else {
			this.sliceVal = sliceVal;
			this.sliceType = sliceType;
		}
		this.dateCalc = new DateCalc(befYmd);

		//System.out.println("@DateConverter befYmd:" + befYmd + " aftYmd:" + aftYmd);

		monthList = DateCalc.dateMap_Mod(befYmd, aftYmd, "yyyy/MM/dd",	sliceType, sliceVal);
		//20151014 期間判定ロジックを修正
		listBef = new ArrayList();
		listAft = new ArrayList();
		int seq = 0;
		for (String element : monthList) {
			element = element.replaceAll("/", "");
			String[] term = element.split(DateCalc.KARA);
			if (term.length >= 2) {
				try {
					listBef.add(Integer.valueOf(term[0]));
					listAft.add(Integer.valueOf(term[1]));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	//	-----------------------------------------------------------------------
	//	キャッシュ機能を追加すれば少しは早くなるが・・・丁寧に検証しないとバグを抱え込む可能性あり 20151014
	//	-----------------------------------------------------------------------
	private int getSeq(String term) {
		try {
			int val = Integer.valueOf(term);
			int bef = 0;
			int aft = 0;
			for (int seq = 0; seq < listBef.size(); seq++) {
				bef = listBef.get(seq);
				aft = listAft.get(seq);
				if (bef <= val && val <= aft)
					return seq;
			}

		} catch (Exception e) {
			e.getStackTrace();
		}
		return -1;
	}

	@Override
	public String convert(String xDate) {
		int seq = -1;
		if (sliceType.equals(DateUtil.DAY) || sliceType.equals(DateUtil.WEEK)) {
			int diff = dateCalc.diffDays(xDate);
			if (diff != Integer.MIN_VALUE)
				seq = diff / sliceVal;
		} else if (sliceType.equals(DateUtil.MONTH)
				|| sliceType.equals(DateUtil.YEAR)) {
			String term = xDate;
			seq = getSeq(term);
		}
		if (seq >= 0) {
			return String.valueOf(seq);
		} else
			return "error";
	}

	public static void main(String[] argv) {
		testDateConverterDay();
		//	testDateConverterMonth();
		//	testDateConverterYear();
	}

	//20151014
	public static void testDateConverterMonth() {
		String befYmd = "20140901";
		String aftYmd = "20150831";
		String sliceType = DateUtil.MONTH;
		int sliceVal = 1;
		DateConverter ins = new DateConverter(befYmd, aftYmd, sliceType,
				sliceVal);
		System.out.println("20140801:" + ins.convert("20140801"));
		System.out.println("----------------------------------");
		System.out.println("20140901:" + ins.convert("20140901"));
		System.out.println("20141001:" + ins.convert("20141001"));
		System.out.println("20141101:" + ins.convert("20141101"));
		System.out.println("20141201:" + ins.convert("20141201"));
		System.out.println("20150101:" + ins.convert("20150101"));
		System.out.println("20150201:" + ins.convert("20150201"));
		System.out.println("20150301:" + ins.convert("20150301"));
		System.out.println("20150401:" + ins.convert("20150401"));
		System.out.println("20150501:" + ins.convert("20150501"));
		System.out.println("20150601:" + ins.convert("20150601"));
		System.out.println("20150701:" + ins.convert("20150701"));
		System.out.println("20150801:" + ins.convert("20150801"));
		System.out.println("----------------------------------");
		System.out.println("20140930:" + ins.convert("20140930"));
		System.out.println("20141031:" + ins.convert("20141031"));
		System.out.println("20141130:" + ins.convert("20141130"));
		System.out.println("20141231:" + ins.convert("20141231"));
		System.out.println("20150131:" + ins.convert("20150131"));
		System.out.println("20150228:" + ins.convert("20150228"));
		System.out.println("20150331:" + ins.convert("20150331"));
		System.out.println("20150430:" + ins.convert("20150430"));
		System.out.println("20150531:" + ins.convert("20150531"));
		System.out.println("20150630:" + ins.convert("20150630"));
		System.out.println("20150731:" + ins.convert("20150731"));
		System.out.println("20150831:" + ins.convert("20150831"));
		System.out.println("----------------------------------");
		System.out.println("20150901:" + ins.convert("20150901"));
		List<String> monthList = ins.getMonthList();
		for (String element : monthList) {
			System.out.println(">>" + element);
		}
	}

	//20151014
	public static void testDateConverterYear() {
		String befYmd = "20130901";
		String aftYmd = "20150831";
		String sliceType = DateUtil.YEAR;
		int sliceVal = 1;
		DateConverter ins = new DateConverter(befYmd, aftYmd, sliceType,
				sliceVal);

		System.out.println("20130801:" + ins.convert("20130801"));
		System.out.println("----------------------------------");
		System.out.println("20130901:" + ins.convert("20130901"));
		System.out.println("20140831:" + ins.convert("20140831"));
		System.out.println("----------------------------------");
		System.out.println("20140901:" + ins.convert("20140901"));
		System.out.println("20150831:" + ins.convert("20150831"));
		System.out.println("----------------------------------");
		System.out.println("20150901:" + ins.convert("20150901"));

		List<String> monthList = ins.getMonthList();
		for (String element : monthList) {
			System.out.println(">>" + element);
		}
	}

	public static void testDateConverterDay() {
		String befYmd = "20140901";
		String aftYmd = "20150831";
		String sliceType = DateUtil.WEEK;
		int sliceVal = 1;
		//		String sliceType = DateUtil.DAY;
		//		int sliceVal = 7;
		DateConverter ins = new DateConverter(befYmd, aftYmd, sliceType,
				sliceVal);
		System.out.println("----------------------------------");
		System.out.println("20140831:" + ins.convert("20140831"));
		System.out.println("----------------------------------");
		System.out.println("20140901:" + ins.convert("20140901"));
		System.out.println("20140907:" + ins.convert("20140907"));
		System.out.println("20140914:" + ins.convert("20140914"));
		System.out.println("20140921:" + ins.convert("20140921"));
		System.out.println("20140928:" + ins.convert("20140928"));
		System.out.println("20141005:" + ins.convert("20141005"));
		System.out.println("20141012:" + ins.convert("20141012"));
		System.out.println("20141019:" + ins.convert("20141019"));
		System.out.println("20141026:" + ins.convert("20141026"));
		System.out.println("20141102:" + ins.convert("20141102"));
		System.out.println("20141109:" + ins.convert("20141109"));
		System.out.println("20141116:" + ins.convert("20141116"));
		System.out.println("20141123:" + ins.convert("20141123"));
		System.out.println("20141130:" + ins.convert("20141130"));
		System.out.println("20141207:" + ins.convert("20141207"));
		System.out.println("20141214:" + ins.convert("20141214"));
		System.out.println("20141221:" + ins.convert("20141221"));
		System.out.println("20141228:" + ins.convert("20141228"));
		System.out.println("20150104:" + ins.convert("20150104"));
		System.out.println("20150111:" + ins.convert("20150111"));
		System.out.println("20150118:" + ins.convert("20150118"));
		System.out.println("20150125:" + ins.convert("20150125"));
		System.out.println("20150201:" + ins.convert("20150201"));
		System.out.println("20150208:" + ins.convert("20150208"));
		System.out.println("20150215:" + ins.convert("20150215"));
		System.out.println("20150222:" + ins.convert("20150222"));
		System.out.println("20150301:" + ins.convert("20150301"));
		System.out.println("20150308:" + ins.convert("20150308"));
		System.out.println("20150315:" + ins.convert("20150315"));
		System.out.println("20150322:" + ins.convert("20150322"));
		System.out.println("20150329:" + ins.convert("20150329"));
		System.out.println("20150405:" + ins.convert("20150405"));
		System.out.println("20150412:" + ins.convert("20150412"));
		System.out.println("20150419:" + ins.convert("20150419"));
		System.out.println("20150426:" + ins.convert("20150426"));
		System.out.println("20150503:" + ins.convert("20150503"));
		System.out.println("20150510:" + ins.convert("20150510"));
		System.out.println("20150517:" + ins.convert("20150517"));
		System.out.println("20150524:" + ins.convert("20150524"));
		System.out.println("20150531:" + ins.convert("20150531"));
		System.out.println("20150607:" + ins.convert("20150607"));
		System.out.println("20150614:" + ins.convert("20150614"));
		System.out.println("20150621:" + ins.convert("20150621"));
		System.out.println("20150628:" + ins.convert("20150628"));
		System.out.println("20150705:" + ins.convert("20150705"));
		System.out.println("20150712:" + ins.convert("20150712"));
		System.out.println("20150719:" + ins.convert("20150719"));
		System.out.println("20150726:" + ins.convert("20150726"));
		System.out.println("20150802:" + ins.convert("20150802"));
		System.out.println("20150809:" + ins.convert("20150809"));
		System.out.println("20150816:" + ins.convert("20150816"));
		System.out.println("20150823:" + ins.convert("20150823"));
		System.out.println("20150830:" + ins.convert("20150830"));
		System.out.println("20150831:" + ins.convert("20150831"));
		System.out.println("----------------------------------");
		System.out.println("20150901:" + ins.convert("20150901"));

		List<String> monthList = ins.getMonthList();
		for (String element : monthList) {
			System.out.println(">>" + element);
		}
	}

	public static void testDateConverterDay_org() {
		String befYmd = "20010101";
		String aftYmd = "20010331";
		String sliceType = DateUtil.DAY;
		int sliceVal = 7;
		DateConverter ins = new DateConverter(befYmd, aftYmd, sliceType,
				sliceVal);
		System.out.println("20010101:" + ins.convert("20010101"));
		System.out.println("20010102:" + ins.convert("20010102"));
		System.out.println("20010103:" + ins.convert("20010103"));
		System.out.println("20010104:" + ins.convert("20010104"));
		System.out.println("20010105:" + ins.convert("20010105"));
		System.out.println("20010106:" + ins.convert("20010106"));
		System.out.println("20010107:" + ins.convert("20010107"));
		System.out.println("----------------------------------");
		System.out.println("20010108:" + ins.convert("20010108"));
		System.out.println("20010109:" + ins.convert("20010109"));
		System.out.println("20010110:" + ins.convert("20010110"));
		System.out.println("20010111:" + ins.convert("20010111"));
		System.out.println("20010112:" + ins.convert("20010112"));
		System.out.println("20010113:" + ins.convert("20010113"));
		System.out.println("20010114:" + ins.convert("20010114"));
		System.out.println("----------------------------------");
		System.out.println("20010115:" + ins.convert("20010115"));
		System.out.println("20010116:" + ins.convert("20010116"));
		System.out.println("20010117:" + ins.convert("20010117"));
		System.out.println("20010118:" + ins.convert("20010118"));
		System.out.println("20010119:" + ins.convert("20010119"));
		List<String> monthList = ins.getMonthList();
		for (String element : monthList) {
			System.out.println(">>" + element);
		}
	}

}
