package kyPkg.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConverterList {
	private Map<Integer, Inf_StrConverter> convMap;

	// -------------------------------------------------------------------------
	// コンストラクター
	// -------------------------------------------------------------------------
	public ConverterList() {
		super();
		convMap = new HashMap();
	}

	// -------------------------------------------------------------------------
	// 文字を変換するインタフェース　
	// -------------------------------------------------------------------------
	public void addConverter(int col, Inf_StrConverter converter) {
		convMap.put(col, converter);
	}

	public String[] convert(String[] array) {
		Set<Integer> keySet = convMap.keySet();
		for (Integer col : keySet) {
			Inf_StrConverter converter = convMap.get(col);
			if (array.length > col)
				array[col] = converter.convert(array[col]);
		}
		return array;
	}
}
