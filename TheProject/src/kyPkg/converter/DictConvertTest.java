package kyPkg.converter;

import static org.junit.Assert.*;

import org.junit.Test;

public class DictConvertTest {

	@Test
	public void test01() {
		String dictPath = "C:/@QPR/home/qpr/res/channel.dic";
		Inf_Converter translate = new DictConvert(dictPath);
		assertEquals("贈答　　　",translate.convert("0", null));
		assertEquals("スーパー　",translate.convert("1", null));
		assertEquals("ＣＶＳ　　",translate.convert("2", null));
		assertEquals("一般小売店",translate.convert("3", null));
		assertEquals("百貨店　　",translate.convert("4", null));
		assertEquals("薬粧店　　",translate.convert("5", null));
		assertEquals("自動販売機",translate.convert("6", null));
		assertEquals("通信販売　",translate.convert("7", null));
		assertEquals("訪販・宅配",translate.convert("8", null));
		assertEquals("その他店舗",translate.convert("9", null));
		assertEquals("セブンイレブン",translate.convert("20", null));
		assertEquals("ファミリーマート",translate.convert("21", null));
		assertEquals("ローソン　",translate.convert("22", null));
		assertEquals("",translate.convert("xx", null));
		assertEquals("",translate.convert("", null));
		assertEquals("",translate.convert(null, null));
	}
//	@Test
//	public void test00() {
//		String dictPath = "C:/@QPR/home/qpr/res/channel.dic";
//		Inf_Converter translate = new DictConvert(dictPath);
//		System.out.println("assertEquals(\"" + translate.convert("0", null)+"\",translate.convert(\"0\", null));");
//		System.out.println("assertEquals(\"" + translate.convert("1", null)+"\",translate.convert(\"1\", null));");
//		System.out.println("assertEquals(\"" + translate.convert("2", null)+"\",translate.convert(\"2\", null));");
//		System.out.println("assertEquals(\"" + translate.convert("3", null)+"\",translate.convert(\"3\", null));");
//		System.out.println("assertEquals(\"" + translate.convert("4", null)+"\",translate.convert(\"4\", null));");
//		System.out.println("assertEquals(\"" + translate.convert("5", null)+"\",translate.convert(\"5\", null));");
//		System.out.println("assertEquals(\"" + translate.convert("6", null)+"\",translate.convert(\"6\", null));");
//		System.out.println("assertEquals(\"" + translate.convert("7", null)+"\",translate.convert(\"7\", null));");
//		System.out.println("assertEquals(\"" + translate.convert("8", null)+"\",translate.convert(\"8\", null));");
//		System.out.println("assertEquals(\"" + translate.convert("9", null)+"\",translate.convert(\"9\", null));");
//		System.out.println("assertEquals(\"" + translate.convert("20", null)+"\",translate.convert(\"20\", null));");
//		System.out.println("assertEquals(\"" + translate.convert("21", null)+"\",translate.convert(\"21\", null));");
//		System.out.println("assertEquals(\"" + translate.convert("22", null)+"\",translate.convert(\"22\", null));");
//		System.out.println("assertEquals(\"" + translate.convert("xx", null)+"\",translate.convert(\"xx\", null));");
//		System.out.println("assertEquals(\"" + translate.convert("", null)+"\",translate.convert(\"\", null));");
//		System.out.println("assertEquals(\"" + translate.convert(null, null)+"\",translate.convert(null, null));");
//	}
}
