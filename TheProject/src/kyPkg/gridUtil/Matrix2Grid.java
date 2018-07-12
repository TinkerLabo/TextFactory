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
	// �O������R�[�������g���K�[
	// ------------------------------------------------------------------------
	@Override
	public void execute() {
		super.start("Path2Grid", 2048);
		if (isStarted()) {
			final SwingWrk worker = new SwingWrk() {
				@Override
				public Object construct() {
					return new ActualTask(); // ���ۂ̏���
				}
			};
			worker.start();
		}
		super.stop();// ����I��
	}

	// ------------------------------------------------------------------------
	// �s���ۂ̏����t
	// TODO�@���l��Ȃ̂��E�E�E������Ȃ̂����肷��ɂ́H�H
	// ------------------------------------------------------------------------
	class ActualTask {
		ActualTask() {
			// setLengthOfTask(256);
			//			grid.removeAll();// �O���b�h���N���A����
			assign2grid.execute();
			if (gridPanel != null) {
				gridPanel.enableIt(true);
				gridPanel.setInfo();
				gridPanel.showGrid(); // �O���b�h��\������
			}
			setCurrent(256);
			stop();// ����I��
			//			if (gridPanel != null)
			//				gridPanel.showGrid(); // �O���b�h��\������

		}
	}
}
