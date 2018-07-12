package kyPkg.filter;

import java.util.HashMap;

public class DictYYMM {
	private HashMap<String, String> hmap;

	public DictYYMM() {
		super();
		hmap = new HashMap<String, String>();
	}

	public String getName(String key) {
		String value = hmap.get(key);
		if (value == null) {
			value = cnvYYMM(key);
			hmap.put(key, value);
		}
		return value;
	}

	private static String cnvYYMM(String val) {
		if (val.length() != 4)
			return "H" + val + "H";
		String pre = val.substring(0, 2);
		String pst = val.substring(2);
		String res ="20" + pre + "”N" + pst + "ŒŽ"; 
//		System.out.println("cnvYYMM>"+res);
		return res;
	}

	public static void main(String[] argv) {
		DictYYMM dicYYMM = new DictYYMM();
		dicYYMM.getName("1001");
		dicYYMM.getName("1001");
		dicYYMM.getName("1001");
		dicYYMM.getName("1102");
		dicYYMM.getName("1102");
		dicYYMM.getName("1102");
		dicYYMM.getName("1203");
		dicYYMM.getName("1203");
		dicYYMM.getName("1203");
		dicYYMM.getName("1304");
		dicYYMM.getName("1304");
		dicYYMM.getName("1304");
		dicYYMM.getName("1405");
		dicYYMM.getName("1506");
		dicYYMM.getName("1607");
	
	
	}

}
