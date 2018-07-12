package kyPkg.gridUtil;

import java.util.List;

import kyPkg.gridPanel.Grid_Panel;
import kyPkg.task.Abs_ProgressTask;
import kyPkg.task.SwingWrk;
import kyPkg.uFile.File2Matrix;

public class Matrix2Grid extends Abs_ProgressTask {
	private Grid_Panel gridPanel;
	private Assign2Grid assign2grid;
	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	public Matrix2Grid(Grid_Panel gridPanel, List<String> headList, File2Matrix file2Matrix) {
		super();
		this.assign2grid = new Assign2Grid(gridPanel.getGrid(), headList, file2Matrix);
		this.gridPanel = gridPanel;
	}
	// ------------------------------------------------------------------------
	// 外部からコールされるトリガー
	// ------------------------------------------------------------------------
	@Override
	public void execute() {
		super.start("Path2Grid", 2048);
		if (isStarted()) {
			final SwingWrk worker = new SwingWrk() {
				@Override
				public Object construct() {
					return new ActualTask(); // 実際の処理
				}
			};
			worker.start();
		}
		super.stop();// 正常終了
	}

	// ------------------------------------------------------------------------
	// 《実際の処理》
	// TODO　数値列なのか・・・文字列なのか判定するには？？
	// ------------------------------------------------------------------------
	class ActualTask {
		ActualTask() {
			// setLengthOfTask(256);
			//			grid.removeAll();// グリッドをクリアする
			assign2grid.execute();
			if (gridPanel != null) {
				gridPanel.enableIt(true);
				gridPanel.setInfo();
				gridPanel.showGrid(); // グリッドを表示する
			}
			setCurrent(256);
			stop();// 正常終了
			//			if (gridPanel != null)
			//				gridPanel.showGrid(); // グリッドを表示する

		}
	}
}
