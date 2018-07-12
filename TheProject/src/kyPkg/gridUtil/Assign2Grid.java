package kyPkg.gridUtil;

import static kyPkg.gridUtil.TModelUtil.matrix2TModel;

import java.util.List;
import java.util.Vector;

import globals.ResControl;
import kyPkg.converter.Inf_ArrayCnv;
import kyPkg.gridModels.DefaultTableModelMod;
import kyPkg.gridPanel.Grid;
import kyPkg.uFile.File2Matrix;

public class Assign2Grid {
	private Grid grid;
	private List<String> headList;
	private File2Matrix file2Matrix;
	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	public Assign2Grid(Grid grid, List<String> headList,
			File2Matrix file2Matrix) {
		this.grid = grid;
		this.headList = headList;
		this.file2Matrix = file2Matrix;
	}
	public void execute() {
		if (grid == null)
			return;
		grid.removeAll();// �O���b�h���N���A����
		file2Matrix.execute();
		Vector<Vector> matrix = file2Matrix.getMatrix();// �t�@�C������Ȃ�H�ǂ��Ȃ邩�v�m�F
		DefaultTableModelMod tableModel = matrix2TModel(headList, matrix);
		grid.setDefModel(tableModel);
		TModelUtil.attachSortableGrid(grid, tableModel);// jTable��tableModel���Ђ��Â�
		TModelUtil.adjustColWidth(grid, tableModel);// �O���b�h�̕���������������
		// -------------------------------------------------------------
		// �e�[�u���̓��e�͕ҏW�\�Ƃ���̂��A����Ƃ��s�Ƃ���̂��H
		// ���l���ڂ͉E�񂹂Ƃ���̂��A�ǂ������ꍇ�𐔒l���ڂƔ��肷��̂��H
		// -------------------------------------------------------------
	}
	public void setHeads(List<String> keyList) {
		this.headList = keyList;
	}

	public static void main(String[] args) {
		test_Assign2Grid();
	}

	public static void test_Assign2Grid() {
		String path = ResControl.getPersonalDir() + "janResult.txt";
		String[] head = { "JAN", "Ұ��", "�i��", "��������", "���i", "�Ŗ���", "T01",
				"T02", "T03", "T04", "T05", "T06", "V01", "V02", "V03", "V04",
				"V05", "V06", "�o�^", "�X�V" };
		List headlist = java.util.Arrays.asList(head);
		Inf_ArrayCnv cnv = null;
		Assign2Grid ins = new Assign2Grid(null, headlist, new File2Matrix(""));
		Vector<Vector> matrix = File2Matrix.extract(path, cnv);
		TModelUtil.matrix2TModel(headlist, matrix);
	}
}
