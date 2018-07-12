package kyPkg.converter;

import java.util.HashMap;

import kyPkg.uCodecs.CharConverter;
import kyPkg.uRegex.SedEmu;

// セル位置に対応したコンバータでセルの文字列を変換する・・・
//XXX 2012/04/09一つのセルについて一つのコンバータしか指定できない・・・・複数指定できるようにするにはどうすればよいか？
//=> index.toString -> hashMap -> listにすればいいかも
public class CelConverter implements Inf_CelConverter {
	private static String TEST_STR = "ﾞγひらがなカタカナ漢字壺壷藪薮･ｰｧｨｩｪｫｬｭｮｯｱﾞｲﾟｳﾞ$#123ﾊﾟﾋﾟﾌﾟｶﾞｷﾞｸﾞ<>*#$%abc:;\\ABC★△■フジ　ユニー　E-プラスふんわり食感　６枚";

	private HashMap<Integer, Inf_StrConverter> hashmap = null;

	public static void test01() {

		CelConverter celConverter = new CelConverter();
		celConverter.addConverter(1, new CharConverter(CharConverter.WIDE));
		celConverter.addConverter(2,
				new CharConverter(CharConverter.WIDE_CLASSIC));
		celConverter.addConverter(3,
				new CharConverter(CharConverter.FIX_LENGTH, "10"));
		celConverter.addConverter(4, new CharConverter(CharConverter.NARROW));
		celConverter.addConverter(5, new CharConverter(CharConverter.NG_FNM));
		celConverter.addConverter(6, new CharConverter(CharConverter.NG_EXCEL));
		celConverter.addConverter(7,
				new CharConverter(CharConverter.KANA2NORMAL));
		celConverter.addConverter(8,
				new CharConverter(CharConverter.HOST_KANJI));
		celConverter.addConverter(9, new SedEmu("漢字壺壷藪薮", "○△□", true));

		System.out.println("#01=>" + celConverter.convert(1, TEST_STR));
		System.out.println("#02=>" + celConverter.convert(2, TEST_STR));
		System.out.println("#03=>" + celConverter.convert(3, TEST_STR));
		System.out.println("#04=>" + celConverter.convert(4, TEST_STR));
		System.out.println("#05=>" + celConverter.convert(5, TEST_STR));
		System.out.println("#06=>" + celConverter.convert(6, TEST_STR));
		System.out.println("#07=>" + celConverter.convert(7, TEST_STR));
		System.out.println("#08=>" + celConverter.convert(8, TEST_STR));
		System.out.println("#09=>" + celConverter.convert(9, TEST_STR));
	}

	public static void test0409() {
		CelConverter celConverter = new CelConverter();
		celConverter.addConverter(8,
				new CharConverter(CharConverter.HOST_KANJI));
		System.out.println("#08=>" + celConverter.convert(8, TEST_STR));
	}

	public static void main(String[] argv) {
		test0409();
	}

	// コンストラクタ
	public CelConverter() {
		hashmap = new HashMap();
	}

	public CelConverter(int[] targetIdx, Inf_StrConverter[] converters) {
		this();
		if (targetIdx.length != converters.length) {
			System.out.println("@CelConverter Parameter Error ");
			return;
		}
		for (int i = 0; i < targetIdx.length; i++) {
			addConverter(targetIdx[i], converters[i]);
		}
	}

	public int getSize() {
		return hashmap.size();
	}

	public void addConverter(int targetIdx, String[] array) {
		addConverter(targetIdx, CnvMap.shiftConverter(array, 1));
	}

	//※注意！！セルは０から始まる
	public void addConverter(int targetCell, Inf_StrConverter cnvMap) {
		hashmap.put(targetCell, cnvMap);
	}

	@Override
	public String convert(int index, String inStr) {
		Inf_StrConverter cnvMap = hashmap.get(index);
		if (cnvMap != null) {
			return cnvMap.convert(inStr);
		} else {
			return inStr;
		}
	}

}
