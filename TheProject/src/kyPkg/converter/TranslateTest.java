package kyPkg.converter;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

public class TranslateTest {

	@Test
	public void test00() {
		Inf_Converter translate = new Translate("a,b,c", "Hello,I,Love", "You");
		//		System.out.println("# a=>" + translate.convert("a", null));
		//		System.out.println("# b=>" + translate.convert("b", null));
		//		System.out.println("# c=>" + translate.convert("c", null));
		//		System.out.println("# d=>" + translate.convert("d", null));
		assertEquals("Hello", translate.convert("a", null));
		assertEquals("I", translate.convert("b", null));
		assertEquals("Love", translate.convert("c", null));
		assertEquals("You", translate.convert("d", null));
	}

	@Test
	public void test01() {
		//�t�B���^�Ƃ��Ă��������ꍇ�̗�i�p�����[�^�p�^�[�����ƂɃC���X�^���X��p�ӂ��Ă����ė��p����j
		String param = "a,b,c:Hello,I,Love:You";
		HashMap<String, Inf_Converter> map = new HashMap();
		map.put(param, new Translate(param));
		Inf_Converter translate = map.get(param);
		//		if (translate != null) {
		//			System.out.println("# a=>" + translate.convert("a", null));
		//			System.out.println("# b=>" + translate.convert("b", null));
		//			System.out.println("# c=>" + translate.convert("c", null));
		//			System.out.println("# d=>" + translate.convert("d", null));
		//		}
		assertEquals("Hello", translate.convert("a", null));
		assertEquals("I", translate.convert("b", null));
		assertEquals("Love", translate.convert("c", null));
		assertEquals("You", translate.convert("d", null));
	}

	@Test
	public void test02() {
		//		System.out.println("�m��X�V�敪");
		String param = "1,3,9,2:01,03,05,09:00";
		HashMap<String, Inf_Converter> map = new HashMap();
		map.put(param, new Translate(param));
		Inf_Converter translate = map.get(param);
		//		if (translate != null) {
		//			System.out.println("# 1=>" + translate.convert("1", null));
		//			System.out.println("# 3=>" + translate.convert("3", null));
		//			System.out.println("# 9=>" + translate.convert("9", null));
		//			System.out.println("# 2=>" + translate.convert("2", null));
		//			System.out.println("# 0=>" + translate.convert("0", null));
		//		}
		assertEquals("01", translate.convert("1", null));
		assertEquals("03", translate.convert("3", null));
		assertEquals("05", translate.convert("9", null));
		assertEquals("09", translate.convert("2", null));
		assertEquals("00", translate.convert("0", null));
	}

	@Test
	public void test03() {
		//		System.out.println("�A���i�t���O");
		String param = "0,1:,1:null";
		HashMap<String, Inf_Converter> map = new HashMap();
		map.put(param, new Translate(param));
		Inf_Converter translate = map.get(param);
		//		if (translate != null) {
		//			System.out.println("# 0=>" + translate.convert("0", null));
		//			System.out.println("# 1=>" + translate.convert("1", null));
		//			System.out.println("# x=>" + translate.convert("x", null));
		//		}
		assertEquals("", translate.convert("0", null));
		assertEquals("1", translate.convert("1", null));
		assertEquals("", translate.convert("x", null));
	}

	@Test
	public void test04() {
		//		System.out.println("�W���Z�k�t���O");
		String param = "0,1:1,2:null";
		HashMap<String, Inf_Converter> map = new HashMap();
		map.put(param, new Translate(param));
		Inf_Converter translate = map.get(param);
		//		if (translate != null) {
		//			System.out.println("# 0=>" + translate.convert("0", null));
		//			System.out.println("# 1=>" + translate.convert("1", null));
		//			System.out.println("# x=>" + translate.convert("x", null));
		//		}
		assertEquals("1", translate.convert("0", null));
		assertEquals("2", translate.convert("1", null));
		assertEquals("", translate.convert("x", null));
	}

	@Test
	public void test05() {
		//��null�Ƃ�����������w�肷��Ƌ󕶎��Ɖ��߂����B
		//		System.out.println("�W���Z�k�t���O2");
		String param = "0_1_null_a:1_2_@_null:null";
		HashMap<String, Inf_Converter> map = new HashMap();
		map.put(param, new Translate(param));
		Inf_Converter translate = map.get(param);
		//		if (translate != null) {
		//			System.out.println("# 0=>" + translate.convert("0", null));
		//			System.out.println("# 1=>" + translate.convert("1", null));
		//			System.out.println("# a=>" + translate.convert("a", null));
		//			System.out.println("# null=>" + translate.convert("", null));
		//			System.out.println("# x=>" + translate.convert("x", null));
		//		}
		assertEquals("1", translate.convert("0", null));
		assertEquals("2", translate.convert("1", null));
		assertEquals("", translate.convert("a", null));
		assertEquals("@", translate.convert("", null));
		assertEquals("", translate.convert("x", null));
	}
}
