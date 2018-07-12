package kyPkg.etc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HashMapMod extends HashMap {
	public HashMapMod() {
		super();
	}
	public HashMapMod(Object key,Object value) {
		super();
		put(key,value);
	}

	public Object get(Object key, Object default_value) {
		Object obj = super.get(key);
		if (obj == null) {
			return default_value;
		}
		put(key, default_value);
		return obj;
	}

	public Boolean getByBoolean(Object key) {
		return getByBoolean(key, false);// default
	}

	public Boolean getByBoolean(Object key, Boolean default_value) {
		Object val = get(key);
		if (val != null && val instanceof Boolean) {
			return (Boolean) val;
		} else {
			put(key, default_value);
			return default_value;
		}
	}

	public Integer getByInteger(Object key) {
		return getByInteger(key, 0);// default
	}

	public Integer getByInteger(Object key, Integer default_value) {
		Object val = get(key);
		if (val != null && val instanceof Integer) {
			return (Integer) val;
		} else {
			put(key, default_value);
			return default_value;
		}
	}

	public Float getByFloat(Object key) {
		return getByFloat(key, 0.0f);// default
	}

	public Float getByFloat(Object key, Float default_value) {
		Object val = get(key);
		if (val != null && val instanceof Float) {
			return (Float) val;
		} else {
			put(key, default_value);
			return default_value;
		}
	}

	public Double getByDouble(Object key) {
		return getByDouble(key, 0.0);// default
	}

	public Double getByDouble(Object key, Double default_value) {
		Object val = get(key);
		if (val != null && val instanceof Double) {
			return (Double) val;
		} else {
			put(key, default_value);
			return default_value;
		}
	}

	public String getByString(Object key) {
		return getByString(key, "");// default
	}

	public String getByString(Object key, String default_value) {
		Object val = get(key);
		if (val != null && val instanceof String) {
			return (String) val;
		} else {
			put(key, default_value);
			return default_value;
		}
	}
	
	public List getByList(Object key) {
		return getByList(key, new ArrayList());// default
	}

	public List getByList(Object key, List default_value) {
		Object val = get(key);
		if (val != null && val instanceof List) {
			return (List) val;
		} else {
			put(key, default_value);
			return default_value;
		}
	}

}
