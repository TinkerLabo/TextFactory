package kyPkg.sql.gui;

import java.util.HashMap;
import java.util.Set;

public class DelimiterRes {
	private static HashMap<String, String> name2Ext;
	private static HashMap<String, String> name2Dlm;
	private static HashMap<String, String> dlm2Name;

	
	public static Set<String> getNameSet() {
		return getName2ExtMap().keySet();
	}

	public static String getExt(String name) {
		return getName2ExtMap().get(name);
	}

	public static String getDlm(String name) {
		return getName2DlmMap().get(name);
	}
	
	public static String getName(String delimiter) {
		return getDlm2NameMap().get(delimiter);
	}

	private static HashMap<String, String> getName2ExtMap() {
		if (name2Ext == null) {
			name2Ext = new HashMap();
			name2Ext.put("Tab", ".prn");
			name2Ext.put("Comma", ".csv");
			name2Ext.put("SPACE", ".txt");
			name2Ext.put(" | ", ".txt");
		}
		return name2Ext;
	}

	private static HashMap<String, String> getName2DlmMap() {
		if (name2Dlm == null) {
			name2Dlm = new HashMap();
			name2Dlm.put("Tab", "\t");
			name2Dlm.put("Comma", ",");
			name2Dlm.put("SPACE", " ");
			name2Dlm.put(" | ", "|");
		}
		return name2Dlm;
	}

	private static HashMap<String, String> getDlm2NameMap() {
		if (dlm2Name == null) {
			dlm2Name = new HashMap();
			dlm2Name.put("\t", "Tab");
			dlm2Name.put(",", "Comma");
			dlm2Name.put(" ", "SPACE");
			dlm2Name.put("|", " | ");
		}
		return dlm2Name;
	}

}
