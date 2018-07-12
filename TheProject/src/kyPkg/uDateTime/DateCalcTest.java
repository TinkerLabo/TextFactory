package kyPkg.uDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DateCalcTest {
	@Test
	public void testWeekDiff() {
		assertEquals(kyPkg.uDateTime.DateCalc.weekDiff("20120101", "20120101"),
				0);
		assertEquals(kyPkg.uDateTime.DateCalc.weekDiff("20120101", "20120107"),
				0);
		assertEquals(kyPkg.uDateTime.DateCalc.weekDiff("20120101", "20120108"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.weekDiff("20120101", "20120109"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.weekDiff("20120101", "20120110"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.weekDiff("20120101", "20120111"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.weekDiff("20120101", "20120112"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.weekDiff("20120101", "20120113"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.weekDiff("20120101", "20120114"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.weekDiff("20120101", "20120115"),
				2);
		assertEquals(kyPkg.uDateTime.DateCalc.weekDiff("20120101", "20120121"),
				2);
		assertEquals(kyPkg.uDateTime.DateCalc.weekDiff("20120101", "20120122"),
				3);
		assertEquals(kyPkg.uDateTime.DateCalc.weekDiff("20120101", "20120128"),
				3);
		assertEquals(kyPkg.uDateTime.DateCalc.weekDiff("20120101", "20120129"),
				4);
		assertEquals(kyPkg.uDateTime.DateCalc.weekDiff("20120101", "20120131"),
				4);
		assertEquals(kyPkg.uDateTime.DateCalc.weekDiff("20120101", "20120201"),
				4);
		assertEquals(kyPkg.uDateTime.DateCalc.weekDiff("20120101", "20120204"),
				4);
		assertEquals(kyPkg.uDateTime.DateCalc.weekDiff("20120101", "20120205"),
				5);
	}

	@Test
	public void testDateDiff() {
		assertEquals(kyPkg.uDateTime.DateCalc.dateDiff("20120101", "20120101"),
				0);
		assertEquals(kyPkg.uDateTime.DateCalc.dateDiff("20120101", "20120107"),
				6);
		assertEquals(kyPkg.uDateTime.DateCalc.dateDiff("20120101", "20120114"),
				13);
		assertEquals(kyPkg.uDateTime.DateCalc.dateDiff("20120101", "20120121"),
				20);
		assertEquals(kyPkg.uDateTime.DateCalc.dateDiff("20120101", "20120128"),
				27);
		assertEquals(kyPkg.uDateTime.DateCalc.dateDiff("20111231", "20120101"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.dateDiff("20120131", "20120201"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.dateDiff("20120229", "20120301"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.dateDiff("20120331", "20120401"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.dateDiff("20120430", "20120501"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.dateDiff("20120531", "20120601"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.dateDiff("20120630", "20120701"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.dateDiff("20120731", "20120801"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.dateDiff("20120831", "20120901"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.dateDiff("20120930", "20121001"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.dateDiff("20121031", "20121101"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.dateDiff("20121130", "20121201"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.dateDiff("20121231", "20130101"),
				1);
	}

	@Test
	public void testGetFiscalOld() {
		// @ 2012
		// 来年になったら＋１しないと・・・エラーになるぜ
		assertEquals(kyPkg.uDateTime.DateCalc.getFiscalOld("19670402"), 49);
		assertEquals(kyPkg.uDateTime.DateCalc.getFiscalOld("20110101"), 6);
		assertEquals(kyPkg.uDateTime.DateCalc.getFiscalOld("20110401"), 6);
		assertEquals(kyPkg.uDateTime.DateCalc.getFiscalOld("20110402"), 5);
		// System.out.println("assertEquals(kyPkg.util.DateCalc.getFiscalOld(\"19670402\"),"+kyPkg.util.DateCalc.getFiscalOld("19670402")+");");
		// System.out.println("assertEquals(kyPkg.util.DateCalc.getFiscalOld(\"20110101\"),"+kyPkg.util.DateCalc.getFiscalOld("20110101")+");");
		// System.out.println("assertEquals(kyPkg.util.DateCalc.getFiscalOld(\"20110401\"),"+kyPkg.util.DateCalc.getFiscalOld("20110401")+");");
		// System.out.println("assertEquals(kyPkg.util.DateCalc.getFiscalOld(\"20110402\"),"+kyPkg.util.DateCalc.getFiscalOld("20110402")+");");
	}

	@Test
	public void testGetNendo() {
		assertEquals(kyPkg.uDateTime.DateCalc.getNendo("19670101"), 1966);
		assertEquals(kyPkg.uDateTime.DateCalc.getNendo("20120101"), 2011);
		assertEquals(kyPkg.uDateTime.DateCalc.getNendo("20120331"), 2011);
		assertEquals(kyPkg.uDateTime.DateCalc.getNendo("20120401"), 2011);
		assertEquals(kyPkg.uDateTime.DateCalc.getNendo("20120402"), 2012);
		// System.out.println("assertEquals(kyPkg.util.DateCalc.getNendo(\"19670101\"),"+kyPkg.util.DateCalc.getNendo("19670101")+");");
	}

	@Test
	public void testformatYMD() {
		assertEquals(
				kyPkg.uDateTime.DateCalc.formatYMD("yyyy/MM/dd", "2009年09月01日"),
				"2009/09/01");
		assertEquals(
				kyPkg.uDateTime.DateCalc.formatYMD("yyyy/MM/dd", "2009年11月30日"),
				"2009/11/30");
		assertEquals(
				kyPkg.uDateTime.DateCalc.formatYMD("yyyy/MM/dd", "2009年11月30日"),
				"2009/11/30");
		assertEquals(
				kyPkg.uDateTime.DateCalc.formatYMD("yyyy/MM/dd", "2012年01月31日"),
				"2012/01/31");

		assertEquals(kyPkg.uDateTime.DateCalc.formatYMD("yyyy'年'MM'月'dd'日'",
				"20091111"), "2009年11月11日");

		// 処理を繰り返した場合の確認
		assertEquals(
				kyPkg.uDateTime.DateCalc.formatYMD("yyyy/MM/dd", "2009年09月01日"),
				"2009/09/01");
		assertEquals(
				kyPkg.uDateTime.DateCalc.formatYMD("yyyy/MM/dd", "2009年11月30日"),
				"2009/11/30");
		assertEquals(
				kyPkg.uDateTime.DateCalc.formatYMD("yyyy/MM/dd", "2009年11月30日"),
				"2009/11/30");
		assertEquals(
				kyPkg.uDateTime.DateCalc.formatYMD("yyyy/MM/dd", "2012年01月31日"),
				"2012/01/31");

		assertEquals(kyPkg.uDateTime.DateCalc.formatYMD("yyyy'年'MM'月'dd'日'",
				"20091111"), "2009年11月11日");

		assertEquals(kyPkg.uDateTime.DateCalc.formatYMD_KJ("20091111"),
				"2009年11月11日");

	}

	@Test
	// -------------------------------------------------------------------------
	// 月の何週目か
	// -------------------------------------------------------------------------
	public void testGetWeekOfMonth() {
		assertEquals(kyPkg.uDateTime.DateCalc.getWeekOfMonth("20120201"), 1);
		assertEquals(kyPkg.uDateTime.DateCalc.getWeekOfMonth("20120205"), 2);
		assertEquals(kyPkg.uDateTime.DateCalc.getWeekOfMonth("20120208"), 2);
		assertEquals(kyPkg.uDateTime.DateCalc.getWeekOfMonth("20120229"), 5);
		// assertEquals(kyPkg.util.DateCalc.getWeekOfMonth("20120230"),1);//?!
		// assertEquals(kyPkg.util.DateCalc.getWeekOfMonth("20120231"),1);//?!
	}

	@Test
	// -------------------------------------------------------------------------
	// 年の何週目か
	// -------------------------------------------------------------------------
	public void testGetWeekOfYear() {
		assertEquals(kyPkg.uDateTime.DateCalc.getWeekOfYear("20120201"), 5);
		assertEquals(kyPkg.uDateTime.DateCalc.getWeekOfYear("20120205"), 6);
		assertEquals(kyPkg.uDateTime.DateCalc.getWeekOfYear("20120208"), 6);
		assertEquals(kyPkg.uDateTime.DateCalc.getWeekOfYear("20120229"), 9);
		// assertEquals(kyPkg.util.DateCalc.getWeekOfYear("20120230"),9);//?!
		// assertEquals(kyPkg.util.DateCalc.getWeekOfYear("20120231"),9);//?!
	}

	@Test
	// -------------------------------------------------------------------------
	// 月の何度目の曜日か
	// -------------------------------------------------------------------------
	public void testGetDayOfWeekInMonth() {
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfWeekInMonth("20120201"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfWeekInMonth("20120205"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfWeekInMonth("20120208"),
				2);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfWeekInMonth("20120229"),
				5);
		// assertEquals(kyPkg.util.DateCalc.getDayOfWeekInMonth("20120230"),1);//?!
		// assertEquals(kyPkg.util.DateCalc.getDayOfWeekInMonth("20120231"),1);//?!
	}

	@Test
	public void testGetWeekOfMonthx() {
		assertEquals(kyPkg.uDateTime.DateCalc.getYear("20120201"), 2012);
		assertEquals(kyPkg.uDateTime.DateCalc.getMonth("20120201"), 2);
		assertEquals(kyPkg.uDateTime.DateCalc.getDate("20120201"), 1);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfWeek("20120201"), 4);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfYear("20120201"), 32);

		assertEquals(kyPkg.uDateTime.DateCalc.getYear("20120205"), 2012);
		assertEquals(kyPkg.uDateTime.DateCalc.getMonth("20120205"), 2);
		assertEquals(kyPkg.uDateTime.DateCalc.getDate("20120205"), 5);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfWeek("20120205"), 1);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfYear("20120205"), 36);

		assertEquals(kyPkg.uDateTime.DateCalc.getYear("20120208"), 2012);
		assertEquals(kyPkg.uDateTime.DateCalc.getMonth("20120208"), 2);
		assertEquals(kyPkg.uDateTime.DateCalc.getDate("20120208"), 8);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfWeek("20120208"), 4);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfYear("20120208"), 39);

		assertEquals(kyPkg.uDateTime.DateCalc.getYear("20120229"), 2012);
		assertEquals(kyPkg.uDateTime.DateCalc.getMonth("20120229"), 2);
		assertEquals(kyPkg.uDateTime.DateCalc.getDate("20120229"), 29);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfWeek("20120229"), 4);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfYear("20120229"), 60);

		// TODO 実際にはありえない日付を指定すると・・・・オカシイ（なんだかでたらめな数字になる）
		assertEquals(kyPkg.uDateTime.DateCalc.getYear("20120230"), 2012);
		assertEquals(kyPkg.uDateTime.DateCalc.getMonth("20120230"), 3);
		assertEquals(kyPkg.uDateTime.DateCalc.getDate("20120230"), 1);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfWeek("20120230"), 5);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfYear("20120230"), 61);

		assertEquals(kyPkg.uDateTime.DateCalc.getMonth("20120230"), 3);
		assertEquals(kyPkg.uDateTime.DateCalc.getDate("20120230"), 1);

		assertEquals(kyPkg.uDateTime.DateCalc.getYear("20120231"), 2012);
		assertEquals(kyPkg.uDateTime.DateCalc.getMonth("20120231"), 3);
		assertEquals(kyPkg.uDateTime.DateCalc.getDate("20120231"), 2);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfWeek("20120231"), 6);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfYear("20120231"), 62);

		// でたらめな日付だと予想外の動きをするので要注意
		assertEquals(kyPkg.uDateTime.DateCalc.getYear("20121331"), 2013);
		assertEquals(kyPkg.uDateTime.DateCalc.getMonth("20121331"), 1);
		assertEquals(kyPkg.uDateTime.DateCalc.getDate("20121331"), 31);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfWeek("20121331"), 5);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfWeekInMonth("20121331"),
				5);
		assertEquals(kyPkg.uDateTime.DateCalc.getWeekOfMonth("20121331"), 5);
		assertEquals(kyPkg.uDateTime.DateCalc.getWeekOfYear("20121331"), 5);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfYear("20121331"), 31);

		assertEquals(kyPkg.uDateTime.DateCalc.getYear("20121332"), 2013);// 年はずれてくれる
		assertEquals(kyPkg.uDateTime.DateCalc.getMonth("20121332"), 2);// 月がずれてくれる
		assertEquals(kyPkg.uDateTime.DateCalc.getDate("20121332"), 1);// 日付は1か・・・？！
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfWeek("20121332"), 6);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfWeekInMonth("20121332"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.getWeekOfMonth("20121332"), 1);
		assertEquals(kyPkg.uDateTime.DateCalc.getWeekOfYear("20121332"), 5);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfYear("20121332"), 32);

		assertEquals(kyPkg.uDateTime.DateCalc.getYear("20121333"), 2013);
		assertEquals(kyPkg.uDateTime.DateCalc.getMonth("20121333"), 2);

		assertEquals(kyPkg.uDateTime.DateCalc.getDate("20121333"), 2);// 日付は２か・・・？！
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfWeek("20121333"), 7);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfWeekInMonth("20121333"),
				1);
		assertEquals(kyPkg.uDateTime.DateCalc.getWeekOfMonth("20121333"), 1);
		assertEquals(kyPkg.uDateTime.DateCalc.getWeekOfYear("20121333"), 5);
		assertEquals(kyPkg.uDateTime.DateCalc.getDayOfYear("20121333"), 33);

	}

	@Test
	public void testdateMap_Mod() {
		List<String> testList = new ArrayList();
		testList.add("20010101～20010107");
		testList.add("20010108～20010114");
		testList.add("20010115～20010121");
		testList.add("20010122～20010128");
		testList.add("20010129～20010204");
		testList.add("20010205～20010211");
		testList.add("20010212～20010218");
		testList.add("20010219～20010225");
		testList.add("20010226～20010304");
		testList.add("20010305～20010311");
		testList.add("20010312～20010318");
		testList.add("20010319～20010325");
		testList.add("20010326～20010331");

		String befYmd = "20010101";
		String aftYmd = "20010331";
		int cut = 7;

		assertEquals(kyPkg.uDateTime.DateCalc.dateMap_Mod(befYmd, aftYmd,
				"yyyyMMdd", DateUtil.DAY, cut), testList);
		// 処理を繰り返した場合の確認
		assertEquals(kyPkg.uDateTime.DateCalc.dateMap_Mod(befYmd, aftYmd,
				"yyyyMMdd", DateUtil.DAY, cut), testList);
		assertEquals(kyPkg.uDateTime.DateCalc.dateMap_Mod(befYmd, aftYmd,
				"yyyyMMdd", DateUtil.DAY, cut), testList);
		testList.add("bug");
		assertNotSame(kyPkg.uDateTime.DateCalc.dateMap_Mod(befYmd, aftYmd,
				"yyyyMMdd", DateUtil.DAY, cut), testList);
	}

	@Test
	public void testgetNendo() {
		assertEquals(DateCalc.getNendo("20151209"), 2015);
		assertEquals(DateCalc.getNendo("20160109"), 2015);
		assertEquals(DateCalc.getNendo("20160209"), 2015);
		assertEquals(DateCalc.getNendo("20160309"), 2015);
		assertEquals(DateCalc.getNendo("20160409"), 2016);
		assertEquals(DateCalc.getNendo("20160509"), 2016);
		assertEquals(DateCalc.getNendo("20160609"), 2016);
		assertEquals(DateCalc.getNendo("20160709"), 2016);
		assertEquals(DateCalc.getNendo("20160809"), 2016);
		assertEquals(DateCalc.getNendo("20160909"), 2016);
		assertEquals(DateCalc.getNendo("20161009"), 2016);
		assertEquals(DateCalc.getNendo("20161109"), 2016);
		assertEquals(DateCalc.getNendo("20161209"), 2016);
	}

	@Test
	public void testdiffDays() {
		String befYmd = "20010101";
		kyPkg.uDateTime.DateCalc dateCalc = new kyPkg.uDateTime.DateCalc(
				befYmd); // 基準日付を設定する
		assertEquals(dateCalc.diffDays("20010101"), 0);
		assertEquals(dateCalc.diffDays("20010107"), 6);
		assertEquals(dateCalc.diffDays("20010108"), 7);
		assertEquals(dateCalc.diffDays("20010114"), 13);
		assertEquals(dateCalc.diffDays("20010115"), 14);
		assertEquals(dateCalc.diffDays("20010121"), 20);
		assertEquals(dateCalc.diffDays("20010122"), 21);
		assertEquals(dateCalc.diffDays("20010128"), 27);
		assertEquals(dateCalc.diffDays("20010129"), 28);
		assertEquals(dateCalc.diffDays("20010204"), 34);
		assertEquals(dateCalc.diffDays("20010205"), 35);
		assertEquals(dateCalc.diffDays("20010211"), 41);
		assertEquals(dateCalc.diffDays("20010212"), 42);
		assertEquals(dateCalc.diffDays("20010218"), 48);
		assertEquals(dateCalc.diffDays("20010219"), 49);
		assertEquals(dateCalc.diffDays("20010225"), 55);
		assertEquals(dateCalc.diffDays("20010226"), 56);
		assertEquals(dateCalc.diffDays("20010304"), 62);
		assertEquals(dateCalc.diffDays("20010305"), 63);
		assertEquals(dateCalc.diffDays("20010311"), 69);
		assertEquals(dateCalc.diffDays("20010312"), 70);
		assertEquals(dateCalc.diffDays("20010318"), 76);
		assertEquals(dateCalc.diffDays("20010319"), 77);
		assertEquals(dateCalc.diffDays("20010325"), 83);
		assertEquals(dateCalc.diffDays("20010326"), 84);
		assertEquals(dateCalc.diffDays("20010331"), 89);
		assertEquals(dateCalc.diffDays(""), Integer.MIN_VALUE);
	}

}
