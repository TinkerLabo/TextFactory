package kyPkg.filter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import kyPkg.converter.Corpus;

public class SubstrCnvTest {
	@Test
	public void testConvert() {

		String ans = new kyPkg.converter.SubstrCnv("1,1,5,"+Corpus.FIX_HALF)
				.convert2Str("あいうえおかきくけこ");
		System.out.println("1>" + ans);
		assertEquals("ｱｲｳｴｵ", ans);
		
		ans = new kyPkg.converter.SubstrCnv("1,1,25,"+Corpus.FIX_WIDE).convert2Str("ｱｲｳｴｵかきくけこ");
		System.out.println("2>" + ans);
		assertEquals("アイウエオかきくけこ　　　　　　　　　　　　　　　", ans);

		//assertEquals(0, monitorFormatTest());
	}



}
