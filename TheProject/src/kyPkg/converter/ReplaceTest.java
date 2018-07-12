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
		Inf_Converter translate = new Replace("��:\t");
		assertEquals(
				"0077052044313: ���[�Y �\�[�Z�[�W �����N�X �U�W�O��	99999: �A��	11503: �{���\�[�Z�[�W",
				translate.convert(
						"0077052044313: ���[�Y �\�[�Z�[�W �����N�X �U�W�O����99999: �A����11503: �{���\�[�Z�[�W",
						null));
	}

}
