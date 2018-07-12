package kyPkg.converter;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReplaceTest {

 
	@Test
	public void test02() {
		Inf_Converter translate = new Replace("::.");
		assertEquals("a.b.c.d.e.f.g", translate.convert("a:b:c:d:e:f:g", null));
	}

	@Test
	public void test03() {
		Inf_Converter translate = new Replace("┃:\t");
		assertEquals(
				"0077052044313: ローズ ソーセージ リンクス ６８０ｇ	99999: 輸入	11503: 畜肉ソーセージ",
				translate.convert(
						"0077052044313: ローズ ソーセージ リンクス ６８０ｇ┃99999: 輸入┃11503: 畜肉ソーセージ",
						null));
	}

}
