package kyPkg.gridUtil;

import java.util.List;
import java.util.Vector;

import globals.ResControl;
import kyPkg.converter.Inf_ArrayCnv;
import kyPkg.gridModels.DefaultTableModelMod;
import kyPkg.gridPanel.Grid;
import kyPkg.uFile.File2Matrix;

public class Assign2Grid_org {
	private Grid grid;
	private String path;
	private List<String> headList;
	private Inf_ArrayCnv cnv;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	public Assign2Grid_org(Grid grid, List<String> headList, String path,Inf_ArrayCnv cnv) {
		this.grid = grid;
		this.path = path;
		this.headList = headList;
		this.cnv = cnv;
	}

//	File2Matrix ins = new File2Matrix(path, cnv);
//	ins.execute();
//	return ins.getMatrix();

	
	public void execute() {
		if (grid == null)
			return;
		grid.removeAll();// グリッドをクリアする

		// -------------------------------------------------------------
		// ファイルが空なら？
		// -------------------------------------------------------------
		Vector<Vector> matrix = File2Matrix.extract(path, cnv);

		DefaultTableModelMod tableModel = TModelUtil.matrix2TModel(headList,
				matrix);

		grid.setDefModel(tableModel);

		// -------------------------------------------------------------
		// jTableにtableModelをひもづけ
		// -------------------------------------------------------------
		TModelUtil.attachSortableGrid(grid, tableModel);

		// -------------------------------------------------------------
		// グリッドの幅を自動調整する
		// -------------------------------------------------------------
		TModelUtil.adjustColWidth(grid, tableModel);
		// System.out.println("◆◆◆###<TableModelUtil execute() end >###");
		// -------------------------------------------------------------
		// テーブルの内容は編集可能とするのか、それとも不可とするのか？
		// 数値項目は右寄せとするのか、どういう場合を数値項目と判定するのか？
		// -------------------------------------------------------------
	}

	public void setHeads(List<String> keyList) {
		this.headList = keyList;
	}

	public void setCnv(Inf_ArrayCnv cnv) {
		this.cnv = cnv;
	}

	public static void main(String[] args) {
		test_Assign2Grid();
	}

	public static void test_Assign2Grid() {
		String path = ResControl.getPersonalDir() + "janResult.txt";
		String[] head = { "JAN", "ﾒｰｶｰ", "品目", "漢字名称", "価格", "ｶﾅ名称", "T01",
				"T02", "T03", "T04", "T05", "T06", "V01", "V02", "V03", "V04",
				"V05", "V06", "登録", "更新" };
		List headlist = java.util.Arrays.asList(head);
		Inf_ArrayCnv cnv = null;
		Assign2Grid_org ins = new Assign2Grid_org(null, headlist, "", cnv);

		Vector<Vector> matrix = File2Matrix.extract(path, cnv);
		TModelUtil.matrix2TModel(headlist, matrix);
	}

}
