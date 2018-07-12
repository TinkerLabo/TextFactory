package kyPkg.filter;

import java.text.DecimalFormat;
import java.util.HashMap;

public class DictWEEK {
	private HashMap<String, String> codeMap;
	private HashMap<String, String> nameMap;
	private String baseYMD = "";
	private static DecimalFormat df00 = new DecimalFormat("00");

	public DictWEEK(String baseYMD) {
		super();
		this.nameMap = new HashMap<String, String>();
		this.codeMap = new HashMap<String, String>();
		this.baseYMD = baseYMD;
	}

	public String getCode(String key) {
		String value = codeMap.get(key);
		if (value == null) {
			// XXX 正規表現でチェックさせる
			if (key.length() != 8)
				return "？" + key + "？";
			int week = kyPkg.uDateTime.DateCalc.weekDiff(baseYMD, key) + 1;
			value = df00.format(week);
//			System.out.println("weekDiff:" + key + " => " + value);
			codeMap.put(key, value);
		}
		return value;
	}

	public String getName(String key) {
		String value = nameMap.get(key);
		if (value == null) {
			value = cnvWEEK(key);
			nameMap.put(key, value);
		}
		return value;
	}

	private String cnvWEEK(String key) {
		String code = getCode(key);
		String res = code + "週";
		return res;
	}

	public static void main(String[] argv) {
		DictWEEK dicYYMM = new DictWEEK("20120201");
		dicYYMM.getName("20120201");
		dicYYMM.getName("20120207");
		dicYYMM.getName("20120214");
		dicYYMM.getName("20120221");
		dicYYMM.getName("20120228");
	}

}
