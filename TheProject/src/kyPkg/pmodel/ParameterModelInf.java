package kyPkg.pmodel;

import java.util.List;

public interface ParameterModelInf {
	public abstract String getParams();

	public abstract String get(String wKey);

	public abstract String[] getArray(String wKey );

	public abstract List getList(String wKey );

	public abstract void checkIt();

	public void saveAsText(String path);

}