package kyPkg.converter;

public class BaseConverter {

	public BaseConverter() {
		super();
	}

	public boolean getBool(String[] params, int index, boolean defaultValue) {
		if (params.length >= index) {
			String val = params[index];
			val = val.toLowerCase();
			if (val.equals("true"))
				return true;
		}
		return defaultValue;
	}

	public int getInt(String[] params, int index, int defaultValue) {
		if (params.length >= index) {
			String s = params[index];
			try {
				return Integer.parseInt(s);
			} catch (Exception e) {
			}
		}
		return defaultValue;
	}

	public String getString(String[] params, int index, String defaultValue) {
		if (params.length >= index) {
			return params[index];
		}
		return defaultValue;
	}

}