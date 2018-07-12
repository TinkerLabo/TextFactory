package kyPkg.converter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class RangeConvertTest {

	@Test
	public void test01() {
		List<String> paramList = new ArrayList();
		paramList.add("12-	4");
		paramList.add("7-11	3");
		paramList.add("2-6	2");
		paramList.add("1	1");
		RangeConvert ins = new RangeConvert(paramList);
		assertEquals("1", ins.convert("1", null));
		assertEquals("2", ins.convert("2", null));
		assertEquals("2", ins.convert("3", null));
		assertEquals("2", ins.convert("4", null));
		assertEquals("2", ins.convert("5", null));
		assertEquals("2", ins.convert("6", null));
		assertEquals("3", ins.convert("7", null));
		assertEquals("3", ins.convert("8", null));
		assertEquals("3", ins.convert("9", null));
		assertEquals("3", ins.convert("10", null));
		assertEquals("3", ins.convert("11", null));
		assertEquals("4", ins.convert("12", null));
		assertEquals("4", ins.convert("13", null));
		assertEquals("", ins.convert("xx", null));
	}

	@Test
	public void test02() {
		List<String> paramList = new ArrayList();
		paramList.add("6-	6");
		paramList.add("5-	5");
		paramList.add("4-	4");
		paramList.add("3-	3");
		paramList.add("2-	2");
		paramList.add("1-	1");
		paramList.add("	0");
		RangeConvert ins = new RangeConvert(paramList);
		assertEquals("1", ins.convert("1", null));
		assertEquals("2", ins.convert("2", null));
		assertEquals("3", ins.convert("3", null));
		assertEquals("4", ins.convert("4", null));
		assertEquals("5", ins.convert("5", null));
		assertEquals("6", ins.convert("6", null));
		assertEquals("6", ins.convert("7", null));
		assertEquals("6", ins.convert("8", null));
		assertEquals("6", ins.convert("9", null));
		assertEquals("6", ins.convert("10", null));
		assertEquals("6", ins.convert("11", null));
		assertEquals("6", ins.convert("12", null));
		assertEquals("6", ins.convert("13", null));
		assertEquals("0", ins.convert("xx", null));
	}

}
