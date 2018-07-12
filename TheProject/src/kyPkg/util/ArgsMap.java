package kyPkg.util;

import java.util.HashMap;
import kyPkg.uFile.HashMapUtil;

public class ArgsMap {
	private HashMap<String, String> hash;
	//-------------------------------------------------------------------------
	// コンストラクタ
	//-------------------------------------------------------------------------
	public ArgsMap(String  ymlPath) {
		boolean uCaseOpt=true;
		hash = HashMapUtil.file2HashMapX(ymlPath, uCaseOpt);
	}

	//-------------------------------------------------------------------------
	// （※keyは大文字にしておく）
	//-------------------------------------------------------------------------
	public void put(String key, String value) {
		hash.put(key.toUpperCase(), value);
	}

	//-------------------------------------------------------------------------
	// XXX パラメータにregexをつけて値のverifyをしても良いかも知れない
	//-------------------------------------------------------------------------
	public String get(String key) {
		return get(key, "");
	}

	public String get(String key, String defaultValue) {
		String val = hash.get(key.toUpperCase());
		if (val == null) {
			hash.put(key.toUpperCase(), defaultValue);
			return defaultValue;
		} else {
			return val;
		}
	}

	public HashMap<String, String> getHash() {
		return hash;
	}

}
