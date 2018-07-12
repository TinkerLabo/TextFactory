package kyPkg.converter;

import java.util.ArrayList;
import java.util.List;

import kyPkg.uFile.ListArrayUtil;

public class RangeConvert implements Inf_Converter{
	public static final String TITLE = "RangeConvert";
	public static final String EXPLAIN = "";
	public static final String SAMPLE = "";
	
	private static final String delimiter = "\t";
	private String defaultVal;
	private List<String> rList;
	private List<Integer> val1;
	private List<Integer> val2;

	public RangeConvert(String path) {
		this(ListArrayUtil.file2List(path));
	}

	public RangeConvert(List<String> list) {
		if (list == null)
			return;
		defaultVal = "";
		rList = new ArrayList();
		val1 = new ArrayList();
		val2 = new ArrayList();
		for (String rec : list) {
			StringBuffer keyBuf = new StringBuffer();
			String[] array1 = rec.split(delimiter, -1);
			if (array1 != null && array1.length == 2) {
				if (array1[0].equals("")) {
					defaultVal = array1[1];
				} else {
					String[] array2 = array1[0].split("-");
					if (array2 != null) {
						if (array2.length == 2) {
							Integer a = getIntValue(array2[0]);
							Integer b = getIntValue(array2[1]);
							if (a != null && b != null && a < b) {
								val1.add(a);
								val2.add(b);
								rList.add(array1[1]);
							} else {
								System.err.println(
										"#ERROR @ parameter :" + array1[0]);
							}
						} else if (array2.length == 1) {
							Integer a = getIntValue(array2[0]);
							if (a != null) {
								val1.add(a);
								val2.add(Integer.MAX_VALUE);
								rList.add(array1[1]);
							} else {
								System.err.println(
										"#ERROR @ parameter :" + array1[0]);
							}
						}
					}

				}
			}
		}
	}

	// ------------------------------------------------------------------------
	// Convert
	// ------------------------------------------------------------------------
	@Override
	public String convert(String cell, String[] list) {
		return convert(cell);
	}
	public String convert(String cell) {
		Integer iVal = getIntValue(cell);
		if (iVal != null) {
			for (int i = 0; i < rList.size(); i++) {
				if (val1.get(i) <= iVal && iVal <= val2.get(i))
					return rList.get(i);
			}
		}
		return defaultVal;
	}

	private Integer getIntValue(String strVal) {
		try {
			return Integer.valueOf(strVal);
		} catch (java.lang.NumberFormatException e) {
			return null;
		}
	}

	public static void main(String[] argv) {
		test01();
		test02();
		test03();
	}

	public static void test01() {
		System.out.println("-< test01 >------------------------------------");
		RangeConvert ins = new RangeConvert("c:/rangep2.txt");
		System.out.println("ins.check1:" + ins.convert("1"));
		System.out.println("ins.check2:" + ins.convert("2"));
		System.out.println("ins.check3:" + ins.convert("3"));
		System.out.println("ins.check4:" + ins.convert("4"));
		System.out.println("ins.check5:" + ins.convert("5"));
		System.out.println("ins.check6:" + ins.convert("6"));
		System.out.println("ins.check7:" + ins.convert("7"));
		System.out.println("ins.check8:" + ins.convert("8"));
		System.out.println("ins.check9:" + ins.convert("9"));
		System.out.println("ins.check10:" + ins.convert("10"));
		System.out.println("ins.check11:" + ins.convert("11"));
		System.out.println("ins.check12:" + ins.convert("12"));
		System.out.println("ins.check13:" + ins.convert("13"));
		System.out.println("ins.checkxx:" + ins.convert("xx"));
	}

	public static void test02() {
		System.out.println("-< test02 >------------------------------------");
		List<String> paramList = new ArrayList();
		paramList.add("12-	4");
		paramList.add("7-11	3");
		paramList.add("2-6	2");
		paramList.add("1	1");
		RangeConvert ins = new RangeConvert(paramList);
		System.out.println("ins.check1:" + ins.convert("1"));
		System.out.println("ins.check2:" + ins.convert("2"));
		System.out.println("ins.check3:" + ins.convert("3"));
		System.out.println("ins.check4:" + ins.convert("4"));
		System.out.println("ins.check5:" + ins.convert("5"));
		System.out.println("ins.check6:" + ins.convert("6"));
		System.out.println("ins.check7:" + ins.convert("7"));
		System.out.println("ins.check8:" + ins.convert("8"));
		System.out.println("ins.check9:" + ins.convert("9"));
		System.out.println("ins.check10:" + ins.convert("10"));
		System.out.println("ins.check11:" + ins.convert("11"));
		System.out.println("ins.check12:" + ins.convert("12"));
		System.out.println("ins.check13:" + ins.convert("13"));
		System.out.println("ins.checkxx:" + ins.convert("xx"));
	}

	public static void test03() {
		System.out.println("-< test03 >------------------------------------");
		List<String> paramList = new ArrayList();
		paramList.add("6-	6");
		paramList.add("5-	5");
		paramList.add("4-	4");
		paramList.add("3-	3");
		paramList.add("2-	2");
		paramList.add("1-	1");
		paramList.add("	0");
		RangeConvert ins = new RangeConvert(paramList);
		System.out.println("ins.check1:" + ins.convert("1"));
		System.out.println("ins.check2:" + ins.convert("2"));
		System.out.println("ins.check3:" + ins.convert("3"));
		System.out.println("ins.check4:" + ins.convert("4"));
		System.out.println("ins.check5:" + ins.convert("5"));
		System.out.println("ins.check6:" + ins.convert("6"));
		System.out.println("ins.check7:" + ins.convert("7"));
		System.out.println("ins.check8:" + ins.convert("8"));
		System.out.println("ins.check9:" + ins.convert("9"));
		System.out.println("ins.check10:" + ins.convert("10"));
		System.out.println("ins.check11:" + ins.convert("11"));
		System.out.println("ins.check11:" + ins.convert("12"));
		System.out.println("ins.check11:" + ins.convert("13"));
		System.out.println("ins.checkxx:" + ins.convert("xx"));
	}
}
