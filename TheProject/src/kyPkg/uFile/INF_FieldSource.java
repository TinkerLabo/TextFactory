package kyPkg.uFile;

import java.util.List;

import kyPkg.util.CnvArray;

public interface INF_FieldSource {
	public abstract List<String> getSelectedFields();

	public abstract String getTable();

	public abstract List<String> getFieldList();

	public abstract CnvArray getConverter();

	public abstract String getFields();
	// public abstract void incore(String sourceDir, boolean initOpt);
	// public abstract List<String> getDefaultList();
	// public abstract void saveSettings(List<String> selectedFields,String
	// sourceDir);

}