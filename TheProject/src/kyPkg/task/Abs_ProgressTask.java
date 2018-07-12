package kyPkg.task;

import java.awt.Component;
import javax.swing.JComponent;

//Abs_CommonTask
/** Uses a SwingWorker to perform a time-consuming (and utterly fake) task. */
public abstract class Abs_ProgressTask extends Abs_BaseTask
		implements Inf_ProgressTask {
	private ProgressControl prgCtrl = null;

	// --------------------------------------------------------------------------
	// コンストラクタ
	// --------------------------------------------------------------------------
	public Abs_ProgressTask() {
		super();
	}

	@Override
	public void setPrgCtrl(ProgressControl prgCtrl) {
		this.prgCtrl = prgCtrl;
	}

	@Override
	public int getCurrent() {
		if (prgCtrl != null)
			return prgCtrl.getCurrent();
		return -1;
	}

	@Override
	public int getTotalLen() {
		if (prgCtrl != null)
			return prgCtrl.getTotalStep();
		return -1;
	}

	protected void setCurrent(int current) {
		if (prgCtrl != null)
			prgCtrl.setCurrent(current);
	}

	@Override
	public void setProgress(int current, int totalStep) {
		if (prgCtrl != null)
			prgCtrl.resetProgress(totalStep);
	}

	@Override
	public void start(String taskName, int totalStep) {
		setDebug(false);
		super.start(taskName, totalStep);
		//		if (prgCtrl != null)
		//			prgCtrl.start(getTitle(), taskName, 2048);
	}

	@Override
	public void stop() {
		//20160309
		//		if (prgCtrl != null)
		//			prgCtrl.stop(); // かならずストップされているかどうか確認する
		super.stop();
	}

	// @Override
	public void initProgressKit(Component parent, JComponent guiComponent,
			String title, int total) {
		this.prgCtrl = new ProgressControl(parent, guiComponent, 0, title,
				null);//20160309 どうだろ
	}

	@Override
	public void info(String message) {
//こっちはログのみ　20161006@@@@@@
		super.info(message);
		incrementProgress();
	}

	public void incrementProgress() {
		if (prgCtrl != null)
			prgCtrl.incrementProgress();
	}
}