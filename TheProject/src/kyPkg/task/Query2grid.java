package kyPkg.task;

import java.util.HashMap;
import java.util.Vector;

import kyPkg.gridModels.DefaultTableModelMod;
import kyPkg.gridPanel.Grid;
import kyPkg.gridPanel.Grid_Panel;
import kyPkg.sql.JDBC;
import kyPkg.util.MD5;
// ---------------------------------------------------------------------
// embed to Grid
// ---------------------------------------------------------------------

public class Query2grid extends Abs_ProgressTask {
	private String message;
	private String sql = "";
	private Grid_Panel grid_Panel = null;
	private int viewLimit;
	private static HashMap<String, DefaultTableModelMod> mapCash = null;
	private String header;
	private JDBC jdbc;
	private String colWidth;

	public static void resetCash() {
		Query2grid.mapCash = new HashMap();
	}

	// �N�G���[�̌��ʂ��O���b�h�ɓǂݍ���
	public Query2grid(Grid_Panel gridPanel, JDBC jdbc, String sql,
			String header, String colWidth, int viewLimit, String msg) {
		super();
		// System.out.println("@Query2grid     start");
		setMessage(msg);
		this.grid_Panel = gridPanel;
		this.jdbc = jdbc;
		this.sql = sql;
		this.header = header;
		this.colWidth = colWidth;
		this.viewLimit = viewLimit;
		// System.out.println("@Query2grid     end");
	}

	@Override
	public void execute() {
		super.start("Query2grid", 2048);
		System.out.println("#Debug 20150930# @Query2grid execute()");
		try {
			setMessage(message);
			loop();
			stop();
			System.out.println("Query2grid ##### DONE #######");
		} catch (Exception e) {
		}
		super.stop();// ����I��
	}

	private void loop() {
//		System.out.println("#Debug 20150930# @Query2grid loop()");
//		System.out.println("    sql:"+sql);

		if (sql.trim().equals(""))
			return;
		String md5 = MD5.getMD5(sql);
		if (mapCash == null)
			mapCash = new HashMap();
		DefaultTableModelMod tModel = mapCash.get(md5);
		tModel = null; // �o�O������悤�Ȃ̂ŁE�E�E�L���b�V���𖳌�������
		if (tModel == null) {
			// SQL�̌��ʂ��}�g���b�N�X���E�E
			System.out.println("SQL�̌��ʂ��}�g���b�N�X���E�E");
			Vector<Vector> matrix = jdbc.query2VectorMatrix(sql);
			// �}�g���b�N�X����e�[�u�����f�����쐬�i���F�N�^�[�����邳���ɃI�[�o�[�w�b�h����E�E�E�j
			System.out.println("�}�g���b�N�X����e�[�u�����f�����쐬");
			tModel = new DefaultTableModelMod(matrix, header, viewLimit);
			mapCash.put(md5, tModel);
		}
		Grid grid = grid_Panel.getGrid();
		grid.putMemo("sql", sql);
		grid.putMemo("header", header);
		grid.putMemo("colWidth", colWidth);

		grid_Panel.setVisible(false);
		// grid_Panel.setTableModel(tModel);
		// grid_Panel.resetColWidth(colWidth);
		grid.setDefModel(tModel);
		grid.resetColWidth(colWidth);
		grid_Panel.setVisible(true);
		System.out.println("#Debug 20150930# @Query2grid loop()�@END");
	}
}
