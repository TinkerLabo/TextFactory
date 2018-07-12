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

	// クエリーの結果をグリッドに読み込む
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
		super.stop();// 正常終了
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
		tModel = null; // バグがあるようなので・・・キャッシュを無効化する
		if (tModel == null) {
			// SQLの結果をマトリックス化・・
			System.out.println("SQLの結果をマトリックス化・・");
			Vector<Vector> matrix = jdbc.query2VectorMatrix(sql);
			// マトリックスからテーブルモデルを作成（ヴェクター化するさいにオーバーヘッドあり・・・）
			System.out.println("マトリックスからテーブルモデルを作成");
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
		System.out.println("#Debug 20150930# @Query2grid loop()　END");
	}
}
