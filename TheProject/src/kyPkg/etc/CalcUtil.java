package kyPkg.etc;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CalcUtil {
	// private static DecimalFormat exFormat1 = new
	// DecimalFormat("###########0.0000");
	// private static DecimalFormat exFormat1 = new
	// DecimalFormat("000000000000.0000");
	private static DecimalFormat exFormat1 = new DecimalFormat(
			"############.####");

	public static String format(double dval) {
		return exFormat1.format(dval);
	}

	// private static String format_org(double dval){
	// return String.valueOf(dval);
	// }
	public static String calcIt2Str(double numElement, int mother, int dotCol) {
		return format(calcIt(numElement, mother, dotCol));
	}

	public static String calcIt2Str(double numElement, double mother,
			int dotCol) {
		return format(calcIt(numElement, mother, dotCol));
	}

	public static double calcIt(double numElement, int intMother, int dotCol) {
		double mother = intMother;
		return calcIt(numElement, mother, dotCol);
	}

	public static double calcIt(double numElement, double mother, int dotCol) {
		if (mother > 0) {
			return roundIt((numElement * 100.0) / mother, dotCol);
		} else if (mother == 0) { // for debug 母数に0を指定した場合実数を戻す（ただし四捨五入はする）
			return roundIt(numElement, dotCol);
		} else {
			return 0;
		}
	}

	// -------------------------------------------------------------------------
	// 率に変換して四捨五入する
	// dval = CalcUtil.calcIt(array[i],monCount,2);
	// -------------------------------------------------------------------------
	public static String calcShareS(int numElement, int mother, int dotCol) {
		return format(calcShare(numElement, mother, dotCol));
	}

	public static String calcShareS(double numElement, int mother, int dotCol) {
		return format(calcShare(numElement, mother, dotCol));
	}

	public static String calcShareS(String strElement, double mother,
			int dotCol) {
		return format(calcShare(Integer.parseInt(strElement), mother, dotCol));
	}

	public static String calcShareS(double numElement, double mother,
			int dotCol) {

		return format(calcShare(numElement, mother, dotCol));
	}

	// 
	public static double calcShare(int intElement, int mother, int dotCol) {
		double numElement = intElement;
		return calcShare(numElement, mother, dotCol);
	}

	public static double calcShare(double numElement, double mother,int dotCol) {
		//#createTester--------------------------------------------------
//		System.out.println("#calcShare#  numElement = " + numElement + " mother = " + mother + ";");
		//--------------------------------------------------		
		
		if (mother > 0) {
			return roundIt((numElement * 100.0) / mother, dotCol);
		} else if (mother == 0) { // for debug 母数に0を指定した場合実数を戻す（ただし四捨五入はする）
			return roundIt(numElement, dotCol);
		} else {
			return 0;
		}
	}

	public static String calcDivS(double numElement, double mother,
			int dotCol) {
		return format(calcDiv(numElement, mother, dotCol));
	}

	public static String calcDivS(int numElement, int mother, int dotCol) {
		return format(calcDiv(numElement, mother, dotCol));
	}

	public static double calcDiv(int intElement, int intMother, int dotCol) {
		double numElement = intElement;
		double mother = intMother;
		return calcDiv(numElement, mother, dotCol);
	}

	public static double calcDiv(double numElement, double mother, int dotCol) {
		if (mother > 0) {
			return roundIt((numElement * 1.0) / mother, dotCol);
		} else if (mother == 0) { // for debug 母数に0を指定した場合実数を戻す（ただし四捨五入はする）
			return roundIt(numElement, dotCol);
		} else {
			return 0;
		}
	}

	// -------------------------------------------------------------------------
	// 小数第dotCol位で四捨五入 CalcUtil.roundIt
	// -------------------------------------------------------------------------
	public static String roundIt2Str(double numElement, int dotCol) {
		return format(roundIt(numElement, dotCol));
	}

	public static double roundIt(double dval, int dotCol) {
		try {
			BigDecimal bi = new BigDecimal(format(dval));
			return bi.setScale(dotCol, BigDecimal.ROUND_HALF_UP).doubleValue();
		} catch (Exception e) {
			System.out.println("@roundIt NumFormatException: " + dval);
			return 0.0;

		}
	}

	public static void main(String[] argv) {
		System.out
				.println("1:" + kyPkg.etc.CalcUtil.calcShareS(1, 4020, 3));
		System.out.println(
				"2:" + kyPkg.etc.CalcUtil.calcShareS(100, 4020, 3));
		System.out.println(
				"3:" + kyPkg.etc.CalcUtil.calcIt2Str(100.0, 4020, 3));
		System.out.println(
				"4:" + kyPkg.etc.CalcUtil.calcIt2Str(4020.01, 4020, 3));
		System.out.println(
				"5:" + kyPkg.etc.CalcUtil.calcShareS(4020, 4020, 3));
	}
}
