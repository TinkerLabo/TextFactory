package kyPkg.frame;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import kyPkg.gridModels.DefaultTableModelMod;
import kyPkg.gridPanel.Grid_Panel;
public class GridControl  {
	private Grid_Panel gridPanel;
	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------
	public GridControl(Grid_Panel gridPanel) {
		this.gridPanel = gridPanel;
	}
	public void showGrid(String path, String[] headArray, String colwidth,int limit) {
		if(gridPanel==null) return;
		//System.out.println("showGrid@GridControl");
		List<String> janheadList = Arrays.asList(headArray);
		Vector<Vector> matrix = kyPkg.uFile.MatrixUtil.file2VectorMatrixPlus(path, limit);
		gridPanel.setDefModel(new DefaultTableModelMod(matrix, janheadList));
		gridPanel.resetColWidth(colwidth);
	}
	public void resetGrid() {
		Thread thread = new Thread(){
			@Override
			public void run(){
				//System.out.println("###  Yeah!! Thread Started!!!");
				if(gridPanel==null) return;
				gridPanel.setDefModel(new DefaultTableModelMod());
				gridPanel.resetColWidth("10,20,30");
		}
		};
		thread.start();
	}
}
