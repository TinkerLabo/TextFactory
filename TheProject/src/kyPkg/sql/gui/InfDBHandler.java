package kyPkg.sql.gui;

import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.ListModel;

public interface InfDBHandler {
	public void createSample();

	public ComboBoxModel query2Cmodel();

	public boolean queryExUpdate();

	public ListModel query2Lmodel();

//	public boolean query2Grid(Jp_SortableGrid pnlGrid);
	
	public JDBC_GUI getInsJDBC() ;	

	public List<List> query2Matrix();

	public String isExist(String wTnm);

	public void importFromText(String texPath, String table, String delimiter,
			boolean optHeader);

}
