package kyPkg.mySwing;

import java.util.List;

public interface InfListPanel {
	public abstract List getList();
	public abstract void setListData(List list);
	public void addElement(String element);
	public void clear();

}